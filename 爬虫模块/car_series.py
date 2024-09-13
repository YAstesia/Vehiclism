# 爬取车系基本信息（品牌、车系、评分）
# Author：杨畅

import requests
import json
import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry

# 设置ChromeDriver路径
chrome_service = Service()
driver = webdriver.Chrome(service=chrome_service)

# 使用Session并设置重试机制
session = requests.Session()
retry = Retry(
    total=5,  # 总共重试5次
    backoff_factor=0.5,  # 重试时的间隔时间指数增长
    status_forcelist=[500, 502, 503, 504],  # 针对这些状态码进行重试
)
adapter = HTTPAdapter(max_retries=retry)
session.mount('http://', adapter)
session.mount('https://', adapter)

# 滚动页面到最底部
def scroll_to_bottom(driver):
    scroll_pause_time = 2  # 页面加载等待时间
    last_height = driver.execute_script("return document.body.scrollHeight")

    while True:
        # 向下滚动页面到底部
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(scroll_pause_time)

        # 计算新的滚动高度
        new_height = driver.execute_script("return document.body.scrollHeight")
        scroll_position = driver.execute_script("return window.pageYOffset + window.innerHeight")

        # 如果页面高度不变，或者已经到达页面底部，表示已滚动到底部
        if new_height == last_height and scroll_position >= new_height:
            break

        last_height = new_height

# 从主页面提取levelname
def get_levelname_from_main_page(car_link):
    try:
        response = session.get(car_link)
        if response.status_code == 200:
            soup = BeautifulSoup(response.text, 'html.parser')
            levelname_tag = soup.find('dd', class_='type')
            if levelname_tag:
                levelname = levelname_tag.find('span', class_='type__item')
                if levelname:
                    return levelname.text.strip()
    except Exception as e:
        print(f"Error fetching levelname from {car_link}: {e}")
    return "0"


# 处理口碑页面数据
def handle_koubai_page(soup, car_name, price, car_link, txt_file):
    script_tag = None
    script_tags = soup.find_all('script')
    for script in script_tags:
        if script.string and 'seriesScoreList' in script.string:
            script_tag = script.string.strip()
            break

    if script_tag:
        json_text_start = script_tag.find('{')
        json_text_end = script_tag.rfind('}') + 1
        json_data = script_tag[json_text_start:json_text_end]

        try:
            full_data = json.loads(json_data)
            data = full_data['props']['pageProps']['baseData']
            brand_name = data.get('brandName', '0') or '0'
            series_name = data.get('seriesname', '0') or '0'
            level_name = data.get('levelname', '0') or '0'

            # 如果 level_name 为空，则从主页面获取
            if level_name == "0":
                level_name = get_levelname_from_main_page(car_link)

            overall_score = f"{float(data.get('seriesAverage', 0)):.2f}" if data.get('seriesAverage') else '0.00'

            scores = []
            score_list = data.get('seriesScoreList', [])
            for i in range(7):
                if i < len(score_list):
                    score_item = score_list[i].get('score', 0)
                    scores.append(f"{float(score_item):.2f}" if score_item else '0.00')
                else:
                    scores.append('0.00')

            # 保留需要的列
            result = [brand_name, series_name, level_name, price, overall_score] + scores
            formatted_result = "\t".join(result)
            txt_file.write(formatted_result + "\n")

        except json.JSONDecodeError as e:
            txt_file.write(f"{car_name}\tJSON解析失败: {str(e)}\n")
    else:
        txt_file.write(f"{car_name}\t" + "\t".join([''] * 11) + "\n")


def main():

    # 目标页面的URL
    url = 'https://www.autohome.com.cn/car/'
    driver.get(url)
    time.sleep(3)

    # 定义保存结果的TXT文件路径
    txt_filename = 'car_koubai_data.txt'

    # 打开文件用于写入
    with open(txt_filename, mode='w', encoding='utf-8') as txt_file:
        txt_file.write(
            "Brand Name\tSeries Name\tLevel Name\tPrice\tOverall Score\tScore1\tScore2\tScore3\tScore4\tScore5\tScore6\tScore7\n")

        # 滚动到页面底部，确保所有内容加载完成
        scroll_to_bottom(driver)

        # 获取所有车辆元素
        cars = driver.find_elements(By.CSS_SELECTOR, 'li[id^="s"]')

        for car in cars:
            try:
                # 检查是否存在"指导价：暂无"
                price_element = car.find_elements(By.CSS_SELECTOR, 'div > a.red')
                if not price_element:
                    continue  # 如果没有找到价格链接，跳过这个车辆

                price = price_element[0].text.strip()
                if "暂无" in price:
                    continue  # 跳过指导价为“暂无”的车辆

                # 获取车辆名称和链接
                car_name = car.find_element(By.CSS_SELECTOR, 'h4 > a').text.strip()
                car_link = car.find_element(By.CSS_SELECTOR, 'h4 > a').get_attribute('href')

                # 构造口碑页面的链接
                koubai_link = car_link.replace("www.autohome.com.cn", "k.autohome.com.cn").split('#')[
                                  0] + "#pvareaid=3454440"

                # 访问口碑页面
                response = session.get(koubai_link)
                if response.status_code == 200:
                    soup = BeautifulSoup(response.text, 'html.parser')
                    handle_koubai_page(soup, car_name, price, car_link, txt_file)
                else:
                    txt_file.write(f"{car_name}\tError: 无法访问口碑页面，状态码: {response.status_code}\n")

            except Exception as e:
                txt_file.write(f"{car_name}\tError: {str(e)}\n")
                continue

    # 关闭浏览器
    driver.quit()

    print(f"数据已成功保存到 {txt_filename} 文件中。")


# 执行主函数
if __name__ == "__main__":
    main()
