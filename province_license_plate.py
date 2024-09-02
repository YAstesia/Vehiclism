import requests
from bs4 import BeautifulSoup

headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36 Edg/128.0.0.0"
}

# 获取所有月份范围
def generate_months(start_year, start_month, end_year, end_month):
    months = []
    current_year = start_year
    current_month = start_month
    while (current_year < end_year) or (current_year == end_year and current_month <= end_month):
        months.append(f"{current_year:04d}{current_month:02d}")
        current_month += 1
        if current_month > 12:
            current_month = 1
            current_year += 1
    return months

# 将等级数字映射为车辆类型名称
def map_level_to_category(level):
    mapping = {
        '1': '微型车',
        '2': '小型车',
        '3': '紧凑型车',
        '4': '中型车',
        '5': '中大型车',
        '6': '大型车',
        '7': 'MPV',
        '8': 'SUV',
        '9': '跑车',
        '10': '电动车'
    }
    return mapping.get(level, '未知级别')

# 将燃料类型数字映射为燃料名称
def map_fuel_type(fuel_type):
    mapping = {
        '1': '燃油车',
        '2': '纯电动',
        '3': '增程',
        '4': '油电混合',
        '6': '新能源'
    }
    return mapping.get(fuel_type, '未知燃料类型')

# 格式化年月为 "xxxx年xx月"
def format_year_month(year_month):
    year = year_month[:4]
    month = year_month[4:]
    return f"{year}年{month}月"

# 获取省份和城市信息
def get_province_and_city_ids():
    province_city_ids = set()  # 使用集合来存储唯一的城市信息
    seen_province_ids = set()  # 用于跟踪已经处理过的省份 ID
    url = "https://xl.16888.com/city-2024-07-1-0-0-0-0-0-1.html"
    response = requests.get(url, headers=headers)
    soup = BeautifulSoup(response.content, 'html.parser')

    # 查找所有省份
    provinces = soup.find_all('a', {'data-id': True})
    for province in provinces:
        province_id = province['data-id']
        if province_id.isdigit() and 2 <= int(province_id) <= 32 and province_id not in seen_province_ids:
            seen_province_ids.add(province_id)  # 添加到已处理集合中
            province_name = province.text.strip()
            print(f"获取到省份: {province_name}, ID: {province_id}")

            # 获取该省份下的所有城市
            city_url = f"https://xl.16888.com/city-2024-07-{province_id}-0-0-0-0-0-1.html"
            city_response = requests.get(city_url, headers=headers)
            city_soup = BeautifulSoup(city_response.content, 'html.parser')
            cities = city_soup.find_all('a', href=True)
            for city in cities:
                href = city['href']
                if href.startswith('/city-') and href.count('-') == 8 and '-0-0-0-' in href:
                    city_parts = href.split('-')
                    if len(city_parts) > 4:
                        city_id = city_parts[4]
                        city_name = city.text.strip()

                        # 跳过省份和城市名称相同的情况，除了直辖市（北京、上海、重庆、天津）
                        if province_name != city_name or province_name in ['北京', '上海', '重庆', '天津']:
                            province_city_ids.add((province_id, city_id, province_name, city_name))  # 使用集合自动去重

    return list(province_city_ids)  # 将集合转换回列表

# 抓取数据并保存到文件中
def fetch_and_save_data(year_month, province_id, city_id, province_name, city_name, file_handle):
    fuel_types = ['1', '2', '3', '4', '6']  # 正确的燃料类型列表
    for level in range(1, 11):  # 级别从1到10
        for fuel_type in fuel_types:  # 依次遍历燃料类型
            page = 1
            while True:
                url = f"https://xl.16888.com/city-{year_month[:4]}-{year_month[4:]}-{province_id}-{city_id}-{level}-0-0-{fuel_type}-{page}.html"
                try:
                    response = requests.get(url, headers=headers)
                    response.raise_for_status()
                    soup = BeautifulSoup(response.content, 'html.parser')

                    # 输出调试信息，显示部分HTML内容
                    print(f"正在处理: {url}")
                    print(soup.prettify()[:1000])  # 输出前1000个字符的HTML内容

                    # 查找数据表格
                    rows = soup.find_all('tr')
                    if not rows:
                        print(f"未找到数据行: {url}")
                        break

                    # 查找包含数据的行
                    data_found = False
                    for row in rows:
                        # 提取车型名称
                        a_tag = row.find('a', href=True)
                        if a_tag and a_tag['href'].startswith('//'):
                            model_name = a_tag.text.strip()

                            # 提取上牌数
                            sales_td = row.find('em')
                            if sales_td:
                                sales_data = sales_td.text.strip()

                                # 提取指导价
                                price_td = row.find('span')
                                if price_td:
                                    price_data = price_td.text.strip()

                                    # 获取映射后的车辆类型、燃料类型和格式化的年月
                                    category = map_level_to_category(str(level))
                                    fuel_name = map_fuel_type(fuel_type)
                                    formatted_year_month = format_year_month(year_month)

                                    # 创建数据行并直接写入文件
                                    data_line = f"{model_name}\t{sales_data}\t{price_data}\t{category}\t{fuel_name}\t{formatted_year_month}\t{province_name}\t{city_name}\n"
                                    file_handle.write(data_line)
                                    data_found = True

                    if not data_found:
                        print(f"数据行为空或已结束: {url}")
                        break

                    page += 1
                except Exception as e:
                    print(f"抓取数据失败: {url}, 错误: {e}")
                    break

def main():
    start_year = 2022
    start_month = 6
    end_year = 2024
    end_month = 7

    months = generate_months(start_year, start_month, end_year, end_month)
    province_city_ids = get_province_and_city_ids()

    with open('province_license_plate_data.txt', 'w', encoding='utf-8') as f:
        # 写入表头
        f.write("车型名称\t销量数据\t售价\t级别\t燃料类型\t年月\t省份\t城市\n")
        for year_month in months:
            for province_id, city_id, province_name, city_name in province_city_ids:
                print(f"抓取数据: {year_month}, 省份 {province_name}, 城市 {city_name}")
                fetch_and_save_data(year_month, province_id, city_id, province_name, city_name, f)

    print("数据抓取并保存完成")

if __name__ == '__main__':
    main()
