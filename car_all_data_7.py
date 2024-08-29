from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
import requests
from bs4 import BeautifulSoup
import json
import time


# 定义从主页面提取车型级别的函数
def get_levelname_from_main_page(car_link):
    try:
        # 发送HTTP请求获取主页面内容
        response = requests.get(car_link)
        if response.status_code == 200:
            # 解析HTML内容
            soup = BeautifulSoup(response.text, 'html.parser')

            # 查找包含车型级别信息的标签
            levelname_tag = soup.find('dd', class_='type')
            if levelname_tag:
                levelname = levelname_tag.find('span', class_='type__item')
                if levelname:
                    return levelname.text.strip()  # 返回车型级别名称
    except Exception as e:
        print(f"从 {car_link} 获取 levelname 时出错: {e}")
    return "0"  # 返回默认值 "0"


# 定义处理口碑页面数据的函数
def handle_koubai_page(soup, car_name, price, car_link, car_id):
    script_tag = None
    script_tags = soup.find_all('script')

    # 查找包含口碑数据的 script 标签
    for script in script_tags:
        if script.string and 'seriesScoreList' in script.string:
            script_tag = script.string.strip()
            break

    if script_tag:
        # 提取并解析包含口碑数据的 JSON 格式数据
        json_text_start = script_tag.find('{')
        json_text_end = script_tag.rfind('}') + 1
        json_data = script_tag[json_text_start:json_text_end]

        try:
            full_data = json.loads(json_data)  # 加载 JSON 数据
            data = full_data['props']['pageProps']['baseData']

            # 获取品牌名称、车系名称和车型级别
            brand_name = data.get('brandName', '0') or '0'
            series_name = data.get('seriesname', '0') or '0'
            level_name = data.get('levelname', '0') or '0'

            # 如果口碑页面中没有车型级别信息，从主页面获取
            if level_name == "0":
                level_name = get_levelname_from_main_page(car_link)

            # 获取总体评分
            overall_score = f"{float(data.get('seriesAverage', 0)):.2f}" if data.get('seriesAverage') else '0.00'

            # 获取各项评分
            scores = []
            score_list = data.get('seriesScoreList', [])
            for i in range(7):
                if i < len(score_list):
                    score_item = score_list[i].get('score', 0)
                    scores.append(f"{float(score_item):.2f}" if score_item else '0.00')
                else:
                    scores.append('0.00')

            # 获取百车故障数和故障原因数据
            quality_url = f'https://k.autohome.com.cn/{car_id}/quality/01'
            quality_data = fetch_quality_data(quality_url)

            # 将所有信息拼接在一起
            result = [brand_name, series_name, level_name, price, overall_score] + scores + quality_data
            formatted_result = "\t".join(result)  # 使用制表符分隔
            print(formatted_result)  # 输出最终结果

        except json.JSONDecodeError as e:
            print(f"{car_name}\tJSON解析失败: {str(e)}")
    else:
        print(f"{car_name}\t" + "\t".join([''] * 11))


# 定义爬取故障数据的函数
def fetch_quality_data(quality_url):
    try:
        # 设置请求头，模拟浏览器访问
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'
        }
        response = requests.get(quality_url, headers=headers)

        if response.status_code == 200:
            # 解析故障页面的HTML内容
            soup = BeautifulSoup(response.text, 'html.parser')

            # 提取新车质量研究数据（2-12个月百车故障数）
            quality_section = soup.find('div', class_='quality-show')
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

            # 提取最多故障问题及其数量
            most_faults_section = quality_section.find('div', class_='quality-most')
            most_faults = []
            if most_faults_section:
                most_faults_items = most_faults_section.find_all('li')
                for fault in most_faults_items:
                    fault_name = fault.find('em').text.strip()
                    fault_count = int(fault.find('span').text.strip().split()[0])
                    most_faults.append((fault_name, fault_count))

            # 准备输出数据，拼接故障信息
            distribution_values = [fault_distribution.get(key, '0').replace('%', '') for key in
                                   ['车身外观', '行驶过程', '功能操作', '电子设备', '座椅', '空调系统', '内饰',
                                    '动力系统', '变速系统']]

            most_faults_flat = []
            for fault_name, fault_count in most_faults:
                most_faults_flat.append(fault_name)
                most_faults_flat.append(str(fault_count))

            return [str(new_car_faults), str(participants)] + distribution_values + most_faults_flat
        else:
            return ['0'] * 11

    except Exception as e:
        print(f"获取故障数据时出错: {e}")
        return ['0'] * 11


# 设置ChromeDriver路径并启动浏览器
chrome_service = Service()
driver = webdriver.Chrome(service=chrome_service)

# 目标页面的URL
url = 'https://www.autohome.com.cn/car/'
driver.get(url)
time.sleep(3)  # 等待页面加载完成

# 滚动页面，加载更多内容
driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
time.sleep(2)  # 再次等待页面加载新内容

# 获取所有车辆元素
cars = driver.find_elements(By.CSS_SELECTOR, 'li[id^="s"]')

# 遍历每辆车，提取相关数据
for car in cars:
    try:
        # 检查是否存在"指导价：暂无"
        price_element = car.find_elements(By.CSS_SELECTOR, 'div > a.red')
        if not price_element:
            continue  # 如果没有找到价格链接，跳过该车辆

        price = price_element[0].text.strip()
        if "暂无" in price:
            continue  # 跳过指导价为“暂无”的车辆

        # 获取车辆名称、链接和ID
        car_name = car.find_element(By.CSS_SELECTOR, 'h4 > a').text.strip()
        car_link = car.find_element(By.CSS_SELECTOR, 'h4 > a').get_attribute('href')
        car_id = car.get_attribute('id').replace('s', '')

        # 构造口碑页面的链接
        koubai_link = car_link.replace("www.autohome.com.cn", "k.autohome.com.cn").split('#')[0] + "#pvareaid=3454440"

        # 访问口碑页面并处理数据
        response = requests.get(koubai_link)
        if response.status_code == 200:
            soup = BeautifulSoup(response.text, 'html.parser')
            handle_koubai_page(soup, car_name, price, car_link, car_id)
        else:
            print(f"{car_name}\tError: 无法访问口碑页面，状态码: {response.status_code}")

    except Exception as e:
        print(f"{car_name}\tError: {str(e)}")
        continue

# 关闭浏览器
driver.quit()
