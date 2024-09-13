# 爬取车型图片信息
# Author：杨畅

import requests
from bs4 import BeautifulSoup
import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry

# 创建一个Session对象并设置重试策略
session = requests.Session()
retry = Retry(total=5, backoff_factor=0.5, status_forcelist=[500, 502, 503, 504])
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


# 获取车系名称
def get_series_name(series_id):
    series_url = f"https://car.autohome.com.cn/pic/series/{series_id}.html"
    response = session.get(series_url)
    if response.status_code != 200:
        return "未知车系"
    soup = BeautifulSoup(response.text, 'html.parser')
    series_name_tag = soup.select_one('.breadnav a:last-child')
    return series_name_tag.text.strip() if series_name_tag else "未知车系"


# 获取车系主页中所有非停产车型的图片链接
def get_car_image_links(series_id):
    series_url = f"https://car.autohome.com.cn/pic/series/{series_id}.html"
    response = session.get(series_url)
    if response.status_code != 200:
        return []

    soup = BeautifulSoup(response.text, 'html.parser')
    models = soup.select('.search-pic-cardl dt, .search-pic-cardl dd ul li a')
    if not models:
        return []

    all_image_links = []
    current_year = None

    for model in models:
        if model.name == 'dt':
            current_year = model.text.strip()
        else:
            model_name = f"{current_year} {model.text.split('(')[0].strip()}"
            if "停产" in model_name:
                continue
            model_series_id = model['href'].split('-s')[1].split('/')[0]
            model_link = f"https://car.autohome.com.cn/pic/series-s{model_series_id}/{series_id}.html"

            try:
                model_response = session.get(model_link)
                if model_response.status_code != 200:
                    continue
                model_soup = BeautifulSoup(model_response.text, 'html.parser')

                # 查找车身外观部分的链接
                exterior_section = model_soup.find('a', string=lambda text: text and '车身外观' in text)
                if exterior_section:
                    exterior_images = exterior_section.find_parent('div').find_next_sibling('div').select('img')
                    for img in exterior_images[:3]:
                        image_url = f"https:{img['src']}"
                        hd_image_url = image_url.replace("480x360_0_q95_c42_autohomecar__", "")
                        all_image_links.append((model_name, hd_image_url))
                else:
                    # 如果没有车身外观部分，获取官图部分的图片链接
                    official_section = model_soup.find('a', string=lambda text: text and '官图' in text)
                    if official_section:
                        official_images = official_section.find_parent('div').find_next_sibling('div').select('img')
                        for img in official_images[:3]:
                            image_url = f"https:{img['src']}"
                            hd_image_url = image_url.replace("480x360_0_q95_c42_autohomecar__", "")
                            all_image_links.append((model_name, hd_image_url))
                time.sleep(1)
            except requests.exceptions.RequestException:
                continue

    return all_image_links


def main():
    output_filename = 'car_tirm_img_data.txt'
    with open(output_filename, mode='w', encoding='utf-8') as output_file:
        # 访问汽车主页面并滚动到底部
        url = 'https://www.autohome.com.cn/car/'
        driver.get(url)
        scroll_to_bottom(driver)

        # 获取所有指导价不为"暂无"的车系ID
        car_series = driver.find_elements(By.CSS_SELECTOR, 'li[id^="s"]')
        series_ids = []
        for car in car_series:
            price_element = car.find_elements(By.CSS_SELECTOR, 'div > a.red')
            if price_element and "暂无" not in price_element[0].text.strip():
                series_id = car.get_attribute('id').replace('s', '')
                series_ids.append(series_id)

        if not series_ids:
            print("未找到任何有效的车系ID")
            return

        # 爬取每个车系的图片链接并写入txt文件
        for series_id in series_ids:
            series_name = get_series_name(series_id)
            image_links = get_car_image_links(series_id)
            if not image_links:
                print(f"未获取到任何图片链接，车系ID: {series_id}")
            for model_name, link in image_links:
                output_file.write(f"{series_name}\t{model_name}\t{link}\n")

    driver.quit()
    print(f"所有图片链接已成功保存到 {output_filename} 文件中。")


if __name__ == "__main__":
    main()