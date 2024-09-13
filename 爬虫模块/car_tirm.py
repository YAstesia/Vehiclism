# 爬取车型详细配置信息
# Author：杨畅

import requests
import json
import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry

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
    scroll_pause_time = 2  # 页面加载等待时间
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


# 获取车辆配置信息
def get_car_configurations(car_id, series_name, txt_file):
    config_url = f"https://car-web-api.autohome.com.cn/car/param/getParamConf?mode=1&site=1&seriesid={car_id}"

    try:
        response = session.get(config_url, timeout=10)
        response.raise_for_status()  # 检查响应状态码是否为200
        data = response.json()

        # 初始化列表来存储所有车型及其配置
        car_models = []

        # 检查请求是否成功
        if data['returncode'] == 0 and data['message'] == "success":
            # 获取titlelist作为配置项名称的参考，同时获取itemtype
            title_list = {}
            for title in data['result']['titlelist']:
                itemtype = title['itemtype']
                for item in title['items']:
                    title_list[item['titleid']] = {'itemname': item['itemname'], 'itemtype': itemtype}

            # 提取车系信息
            series_name = data['result']['bread']['seriesname']

            # 遍历每个车型的数据
            for car_data in data['result']['datalist']:
                specname = car_data.get('specname', '未知车型')
                model_info = {
                    'specname': specname,
                    'parameters': {}
                }

                # 遍历配置列表
                for param in car_data['paramconflist']:
                    titleid = param.get('titleid')
                    item_info = title_list.get(titleid, {'itemname': '未知配置', 'itemtype': '未知类型'})
                    itemname = item_info['itemname']
                    itemtype = item_info['itemtype']

                    # 跳过颜色相关配置和车型名称
                    if "颜色" in itemname or "车型名称" in itemname:
                        continue

                    itemvalue = param.get('itemname', '').strip()
                    sublist_output = []

                    # 处理sublist中的值
                    if param.get('sublist'):
                        sublist = param['sublist']
                        for sub_item in sublist:
                            value_indicator = sub_item.get('value', '')
                            name = sub_item.get('name', '')
                            priceinfo = sub_item.get('priceinfo', '')

                            if value_indicator == '●':
                                sublist_output.append(f"{value_indicator}{name}")
                            elif value_indicator == '○':
                                if priceinfo:
                                    sublist_output.append(f"{value_indicator}{name} ({priceinfo})")
                                else:
                                    sublist_output.append(f"{value_indicator}{name}")

                    # 将itemvalue和sublist内容结合，并使用\n实现分行
                    if itemvalue and sublist_output:
                        itemvalue = f"{itemvalue}" + ' '.join(sublist_output).strip()
                    elif not itemvalue and sublist_output:
                        itemvalue = ' '.join(sublist_output).strip()

                    # 如果值为空，将其设置为'-'
                    if not itemvalue:
                        itemvalue = '-'

                    # 将配置项类型和名称与其值对应
                    if itemtype not in model_info['parameters']:
                        model_info['parameters'][itemtype] = {}
                    model_info['parameters'][itemtype][itemname] = itemvalue

                # 添加车型信息
                car_models.append(model_info)

            # 写入结果到TXT文件
            for model in car_models:
                for itemtype, params in model['parameters'].items():
                    for param_name, param_value in params.items():
                        # 写入TXT格式：series_name\tspecname\tconfig_type\tconfig_name\tconfig_value
                        output = f"{series_name}\t{model['specname']}\t{itemtype}\t{param_name}\t{param_value}\n"
                        txt_file.write(output)

    except requests.exceptions.RequestException as e:
        txt_file.write(f"{series_name}\tError fetching car config: {str(e)}\n")


def main():

    # 目标页面的URL
    url = 'https://www.autohome.com.cn/car/'
    driver.get(url)
    time.sleep(3)

    # 定义保存结果的TXT文件路径
    txt_filename = 'car_tirm_data.txt'

    # 打开文件用于写入
    with open(txt_filename, mode='w', encoding='utf-8') as txt_file:
        txt_file.write("Series Name\tSpec Name\tConfig Type\tConfig Name\tConfig Value\n")

        # 滚动页面，确保所有车辆加载完成
        scroll_to_bottom(driver)

        # 获取所有车辆元素
        cars = driver.find_elements(By.CSS_SELECTOR, 'li[id^="s"]')

        # 遍历每个车辆元素，提取ID并爬取详细配置
        for car in cars:
            try:
                # 检查是否存在"指导价：暂无"
                price_element = car.find_elements(By.CSS_SELECTOR, 'div > a.red')
                if not price_element:
                    continue  # 如果没有找到价格链接，跳过这个车辆

                price = price_element[0].text.strip()
                if "暂无" in price:
                    continue  # 跳过指导价为“暂无”的车辆

                # 获取车辆ID和名称
                car_id = car.get_attribute('id').replace('s', '')  # 获取车辆ID
                car_name = car.find_element(By.CSS_SELECTOR, 'h4 > a').text.strip()

                # 爬取详细配置信息
                get_car_configurations(car_id, car_name, txt_file)

            except Exception as e:
                txt_file.write(f"{car_name}\tError: {str(e)}\n")
                continue

    # 关闭浏览器
    driver.quit()

    print(f"数据已成功保存到 {txt_filename} 文件中。")


if __name__ == "__main__":
    main()
