# 爬取车系图片信息
# Author：杨畅

import requests
from bs4 import BeautifulSoup
import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

# 创建一个Session对象并设置重试策略
session = requests.Session()
session.mount('http://', requests.adapters.HTTPAdapter(max_retries=3))
session.mount('https://', requests.adapters.HTTPAdapter(max_retries=3))

# 设置ChromeDriver路径
chrome_service = Service()
driver = webdriver.Chrome(service=chrome_service)

# 滚动页面到最底部
def scroll_to_bottom(driver):
    scroll_pause_time = 2
    last_height = driver.execute_script("return document.body.scrollHeight")

    while True:
        # 向下滚动页面到底部
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(scroll_pause_time)
        # 计算新的滚动高度和当前位置
        new_height = driver.execute_script("return document.body.scrollHeight")
        scroll_position = driver.execute_script("return window.pageYOffset + window.innerHeight")
        # 如果页面高度不变，或者已经到达页面底部，表示已滚动到底部
        if new_height == last_height and scroll_position >= new_height:
            break
        last_height = new_height


# 获取车系名称和图片URL
def get_series_name_and_image(series_url):
    response = session.get(series_url)

    if response.status_code != 200:
        print(f"请求失败，状态码: {response.status_code}")
        return None, None

    soup = BeautifulSoup(response.text, 'html.parser')

    # 提取车系名称
    series_name_tag = soup.select_one('.athm-crumb span:last-child')
    if not series_name_tag:
        print("未找到车系名称")
        return None, None
    series_name = series_name_tag.text.strip()

    # 提取图片链接
    ld_json_script = soup.find('script', type='application/ld+json')
    if not ld_json_script:
        print("未找到图片信息")
        return series_name, None

    ld_json_data = ld_json_script.string.strip()
    try:
        # 查找图片的链接部分
        start_index = ld_json_data.find('"images": [') + len('"images": [')
        end_index = ld_json_data.find(']', start_index)
        images_str = ld_json_data[start_index:end_index]
        # 提取第一张图片链接
        image_url = images_str.split('"')[1]
        # 转换为高清图片链接
        hd_image_url = image_url.replace("380x285_0_q87_autohomecar__", "")
    except IndexError:
        print("解析图片链接失败")
        return series_name, None

    return series_name, hd_image_url


def main():
    output_filename = 'car_series_img_data.txt'
    with open(output_filename, mode='w', encoding='utf-8') as output_file:
        url = 'https://www.autohome.com.cn/car/'
        driver.get(url)
        scroll_to_bottom(driver)

        # 获取所有指导价不为"暂无"的车系ID
        car_series = driver.find_elements(By.CSS_SELECTOR, 'li[id^="s"]')
        series_ids = []
        for car in car_series:
            try:
                price_element = car.find_elements(By.CSS_SELECTOR, 'div > a.red')
                if not price_element:
                    continue  # 如果没有找到价格链接，跳过这个车辆

                price = price_element[0].text.strip()
                if "暂无" in price:
                    continue  # 跳过指导价为“暂无”的车辆

                series_id = car.get_attribute('id').replace('s', '')
                series_ids.append(series_id)
            except Exception as e:
                print(f"处理车辆时出错: {e}")
                continue

        if not series_ids:
            print("未找到任何有效的车系ID")
            driver.quit()
            return

        for series_id in series_ids:
            series_url = f"https://www.autohome.com.cn/{series_id}/"
            series_name, image_url = get_series_name_and_image(series_url)

            if series_name and image_url:
                output_file.write(f"{series_name}\t{image_url}\n")
            else:
                print(f"未获取到有效数据，车系ID: {series_id}")

    driver.quit()
    print(f"所有图片链接已成功保存到 {output_filename} 文件中。")


if __name__ == "__main__":
    main()
