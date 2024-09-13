# 爬取车系购车目的
# Author：杨畅

import requests
import json
import time
from collections import defaultdict
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from requests.adapters import HTTPAdapter
from requests.packages.urllib3.util.retry import Retry

# 使用Session并设置重试机制
session = requests.Session()
retry = Retry(connect=5, backoff_factor=0.5)
adapter = HTTPAdapter(max_retries=retry)
session.mount('http://', adapter)
session.mount('https://', adapter)

# 设置ChromeDriver路径
chrome_service = Service()
driver = webdriver.Chrome(service=chrome_service)

# 滚动页面到最底部
def scroll_to_bottom(driver):
    scroll_pause_time = 2
    last_height = driver.execute_script("return document.body.scrollHeight")

    while True:
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(scroll_pause_time)
        new_height = driver.execute_script("return document.body.scrollHeight")
        scroll_position = driver.execute_script("return window.pageYOffset + window.innerHeight")
        if new_height == last_height and scroll_position >= new_height:
            break
        last_height = new_height

# 获取车系的购车目的分布
def get_purpose_distribution_for_series(series_id, series_name):
    purpose_count = defaultdict(int)
    page_index = 1
    page_size = 20

    while True:
        url = f"https://koubeiipv6.app.autohome.com.cn/pc/series/list?pm=3&seriesId={series_id}&pageIndex={page_index}&pageSize={page_size}&yearid=0&ge=0&seriesSummaryKey=0&order=0"

        try:
            response = session.get(url)
            response.raise_for_status()
            data = response.json()

            if not data['result']['list']:
                break

            for entry in data['result']['list']:
                purposes = entry.get('purposes', [])
                for purpose in purposes:
                    purpose_name = purpose['name']
                    purpose_count[purpose_name] += 1

            page_index += 1
            time.sleep(1)

        except requests.exceptions.RequestException as e:
            print(f"请求失败: {e}")
            break

    total_purposes = sum(purpose_count.values())
    return purpose_count, total_purposes

def main():
    # 目标页面的URL
    url = 'https://www.autohome.com.cn/car/'
    driver.get(url)
    time.sleep(3)

    # 滚动页面确保所有内容加载完成
    scroll_to_bottom(driver)

    # 获取所有车辆元素
    cars = driver.find_elements(By.CSS_SELECTOR, 'li[id^="s"]')

    # 定义保存结果的TXT文件路径
    txt_filename = 'car_purpose_data.txt'

    # 打开文件用于写入
    with open(txt_filename, mode='w', encoding='utf-8') as txt_file:
        txt_file.write("Series Name\tPurpose\tCount\tProportion\tTotal Purposes\n")

        for car in cars:
            try:
                price_element = car.find_elements(By.CSS_SELECTOR, 'div > a.red')
                if not price_element:
                    continue

                price = price_element[0].text.strip()
                if "暂无" in price:
                    continue

                car_id = car.get_attribute('id').replace('s', '')
                car_name = car.find_element(By.CSS_SELECTOR, 'h4 > a').text.strip()

                # 获取购车目的分布
                purpose_distribution, total_purposes = get_purpose_distribution_for_series(car_id, car_name)

                if total_purposes > 0:
                    for purpose, count in purpose_distribution.items():
                        proportion = (count / total_purposes) * 100
                        txt_file.write(f"{car_name}\t{purpose}\t{count}\t{proportion:.2f}%\t{total_purposes}\n")

            except Exception as e:
                print(f"Error processing car with ID {car_id}: {str(e)}")
                continue

    # 关闭浏览器
    driver.quit()

    print(f"数据已成功保存到 {txt_filename} 文件中。")


if __name__ == "__main__":
    main()
