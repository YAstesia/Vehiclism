# 爬取车系百车故障数/故障原因信息
# Author：杨畅

import requests
import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from bs4 import BeautifulSoup
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
        if new_height == last_height:
            break
        last_height = new_height

# 获取所有车系ID，并过滤掉参考价为“暂无”的车系
def get_all_series_ids_and_names():
    url = 'https://www.autohome.com.cn/car/'
    driver.get(url)
    scroll_to_bottom(driver)

    car_series = driver.find_elements(By.CSS_SELECTOR, 'li[id^="s"]')
    series_info = []
    for car in car_series:
        price_element = car.find_elements(By.CSS_SELECTOR, 'div > a.red')
        if not price_element:
            continue  # 如果没有找到价格链接，跳过该车辆

        price = price_element[0].text.strip()
        if "暂无" in price:
            continue  # 跳过指导价为“暂无”的车辆

        # 获取车辆ID和名称
        car_id = car.get_attribute('id').replace('s', '')  # 获取车辆ID
        car_name = car.find_element(By.CSS_SELECTOR, 'h4 > a').text.strip()  # 获取车辆名称
        series_info.append((car_id, car_name))

    return series_info


# 爬取某个车系的故障信息
def scrape_car_quality(series_id, series_name):
    url = f'https://k.autohome.com.cn/{series_id}/quality/01'
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'
    }

    try:
        response = requests.get(url, headers=headers)
    except requests.exceptions.RequestException as e:
        return f"{series_name}\t0\t0\t0\t0\t0\t0\t0\t0\t0\t"  # 请求失败

    if response.status_code != 200:
        return f"{series_name}\t0\t0\t0\t0\t0\t0\t0\t0\t0\t"  # 请求失败

    soup = BeautifulSoup(response.text, 'html.parser')

    # 提取新车质量研究数据（2-12个月百车故障数）
    quality_section = soup.find('div', class_='quality-show')
    if not quality_section:
        return f"{series_name}\t0\t0\t0\t0\t0\t0\t0\t0\t0\t"  # 页面不存在或未找到数据

    new_car_quality = quality_section.find('a', class_='fn-left cur')

    if new_car_quality:
        # 处理百车故障数为“暂无”的情况
        new_car_faults_text = new_car_quality.find('b').text.strip()
        if '暂无' in new_car_faults_text:
            new_car_faults = 0
        else:
            try:
                new_car_faults = int(new_car_faults_text.split('：')[-1].replace('）', ''))
            except ValueError:
                new_car_faults = 0

        participants_text = new_car_quality.text.strip().split('（')[-1]
        try:
            participants = int(participants_text.split('人')[0])
        except ValueError:
            participants = 0
    else:
        new_car_faults = 0
        participants = 0

    # 提取故障分布情况
    distribution_section = quality_section.find('div', id='quality-chart-box-01')
    fault_distribution = {}
    if distribution_section:
        distribution_items = distribution_section.find_all('dl')
        for item in distribution_items:
            fault_type = item.find('dt').text.strip()
            fault_percentage = item.find('dd').text.strip()
            fault_distribution[fault_type] = fault_percentage
    else:
        return f"{series_name}\t0\t0\t0\t0\t0\t0\t0\t0\t0\t"  # 未找到故障分布数据

    # 提取最多故障问题及其数量
    most_faults_section = quality_section.find('div', class_='quality-most')
    most_faults = []
    if most_faults_section:
        most_faults_items = most_faults_section.find_all('li')
        for fault in most_faults_items:
            fault_name = fault.find('em').text.strip()
            fault_count = int(fault.find('span').text.strip().split()[0])
            most_faults.append((fault_name, fault_count))

    # 准备输出数据
    distribution_values = [fault_distribution.get(key, '0').replace('%', '') for key in
                           ['车身外观', '行驶过程', '功能操作', '电子设备', '座椅', '空调系统', '内饰', '动力系统',
                            '变速系统']]

    most_faults_flat = []
    for fault_name, fault_count in most_faults:
        most_faults_flat.append(fault_name)
        most_faults_flat.append(str(fault_count))

    # 输出格式化数据，使用\t作为分隔符
    output = [series_name, str(new_car_faults)] + distribution_values + most_faults_flat
    return "\t".join(output)


def main():

    output_filename = 'car_quality_data.txt'

    series_info = get_all_series_ids_and_names()

    if not series_info:
        print("未找到任何有效的车系")
        return

    with open(output_filename, mode='w', encoding='utf-8') as output_file:
        for series_id, series_name in series_info:
            result = scrape_car_quality(series_id, series_name)
            output_file.write(result + "\n")
            print(result)

    driver.quit()
    print(f"数据已成功保存到 {txt_filename} 文件中。")


if __name__ == "__main__":
    main()
