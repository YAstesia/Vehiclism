# 爬取半年内城市车系销量信息
# Author：张天立

from selenium import webdriver
from selenium.webdriver.edge.service import Service
from selenium.webdriver.edge.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import time

# 设置 Selenium WebDriver 和 EdgeOptions
def setup_edge_driver():
    edge_options = Options()
    edge_options.add_argument(
        "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36 Edg/128.0.0.0")
    edge_options.add_argument("--headless")  # 使用无头模式
    service = Service(executable_path="E:\\python\\Scripts\\MicrosoftWebDriver.exe")
    driver = webdriver.Edge(service=service, options=edge_options)
    return driver

# 获取城市 ID 和名称的映射
def fetch_city_ids():
    city_mapping = {}
    driver = setup_edge_driver()
    try:
        base_url = "https://www.autohome.com.cn/rank/1-4-0-0-0_9000-0-0/2024-02_2024-07.html"
        driver.get(base_url)
        wait = WebDriverWait(driver, 10)

        # 模拟点击城市下拉框
        dropdown = driver.find_element(By.CSS_SELECTOR, "span.css-18rz210")
        dropdown.click()

        # 等待下拉框内容加载
        wait.until(EC.visibility_of_element_located((By.CSS_SELECTOR,
                                                     "body > div:nth-child(38) > div > div > div > div > div > div > div > div > div.ant-city-select__citylist > dl > dd > span")))

        # 获取城市选项
        city_options = driver.find_elements(By.CSS_SELECTOR,
                                            "body > div:nth-child(38) > div > div > div > div > div > div > div > div > div.ant-city-select__citylist > dl > dd > span")

        for option in city_options:
            city_name = option.text
            city_id = option.get_attribute("data-value")
            city_mapping[city_id] = city_name
    finally:
        driver.quit()

    return city_mapping

# 模拟滚动直到页面底部没有新数据出现
def scroll_page(driver):
    last_height = driver.execute_script("return document.body.scrollHeight")

    while True:
        # 滚动到底部
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(2)  # 等待页面加载

        # 计算新的页面高度并与之前的高度对比
        new_height = driver.execute_script("return document.body.scrollHeight")
        if new_height == last_height:
            break  # 如果页面高度没有变化，说明已经到底部
        last_height = new_height

# 抓取数据并保存到文件中
def fetch_and_save_data(city_id, city_name, file_handle):
    url = f"https://www.autohome.com.cn/rank/1-4-0-{city_id}-0_9000-0-0/2024-02_2024-07.html"

    driver = setup_edge_driver()
    driver.get(url)
    driver.maximize_window()

    # 模拟滚动以加载所有数据
    scroll_page(driver)

    # 获取页面源代码并解析
    page_source = driver.page_source
    soup = BeautifulSoup(page_source, 'html.parser')

    # 查找车型名称
    car_names = soup.find_all('div', class_='tw-text-nowrap tw-text-lg tw-font-medium hover:tw-text-[#ff6600]')
    # 查找销量数据
    sales_numbers = soup.find_all('span',
                                  class_='tw-pt-[5px] tw-font-avenir tw-text-[22px] tw-font-[500] tw-leading-none')
    # 查找价格数据
    prices = soup.find_all('div', class_='tw-font-avenir tw-font-medium tw-text-[#717887]')

    # 确保数据匹配
    if len(car_names) == len(sales_numbers) == len(prices):
        for car, sales, price in zip(car_names, sales_numbers, prices):
            model_name = car.text.strip()
            sales_data = sales.text.strip()
            price_data = price.text.strip()

            # 将数据按制表符分隔格式保存
            file_handle.write(f"{city_name}\t{model_name}\t{sales_data}\t{price_data}\n")
    else:
        print(f"数据不匹配: {url}")
        print(f"车型数量: {len(car_names)}, 销量数量: {len(sales_numbers)}, 价格数量: {len(prices)}")

    driver.quit()

def main():
    # 获取城市 ID 和名称
    city_mapping = fetch_city_ids()

    with open('car_sales_city_data.txt', 'w', encoding='utf-8') as f:
        # 写入表头
        f.write("城市\t车型名称\t销量数据\t价格\n")
        for city_id, city_name in city_mapping.items():
            print(f"抓取数据: 城市ID {city_id} ({city_name})")
            fetch_and_save_data(city_id, city_name, f)

    print("数据抓取并保存完成")

if __name__ == '__main__':
    main()
