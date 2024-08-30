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
        '6': 'MPV',
        '7': 'SUV'
    }
    return mapping.get(level, '未知级别')

# 格式化年月为 "xxxx年xx月"
def format_year_month(year_month):
    year = year_month[:4]
    month = year_month[4:]
    return f"{year}年{month}月"

# 抓取数据并保存到文件中
def fetch_and_save_data(year_month, file_handle):
    for level1 in range(1, 8):  # 级别从1到7
        page = 1
        while True:
            url = f"https://xl.16888.com/level-{level1}-{year_month}-{year_month}-{page}.html"
            try:
                response = requests.get(url, headers=headers)
                response.raise_for_status()
                soup = BeautifulSoup(response.content, 'html.parser')

                # 输出调试信息，显示部分HTML内容
                print(f"正在处理: {url}")
                print(soup.prettify()[:1000])  # 输出前1000个字符的HTML内容

                # 查找所有表格
                tables = soup.find_all('table')
                if not tables:
                    print(f"未找到数据表格: {url}")
                    break

                # 查找包含数据的表格
                data_found = False
                for table in tables:
                    rows = table.find_all('tr')
                    for row in rows:
                        # 提取车型
                        a_tag = row.find('a', href=True)
                        if a_tag and a_tag['href'].startswith('/s'):
                            model_name = a_tag.text.strip()

                            # 提取销量数据
                            sales_td = row.find('td', class_='xl-td-t3')
                            if sales_td:
                                sales_data = sales_td.text.strip()


                                # 提取售价数据
                                price_td = row.find('td', class_='xl-td-t5')
                                if price_td:
                                    price_data = price_td.text.strip()

                                    # 获取映射后的车辆类型和格式化的年月
                                    category = map_level_to_category(str(level1))
                                    formatted_year_month = format_year_month(year_month)

                                    # 将数据按 Hive 格式保存，字段间用制表符分隔
                                    file_handle.write(f"{model_name}\t{sales_data}\t{price_data}\t{category}\t{formatted_year_month}\n")
                                    data_found = True

                if not data_found:
                    print(f"数据表格为空或已结束: {url}")
                    break

                page += 1
            except Exception as e:
                print(f"抓取数据失败: {url}, 错误: {e}")
                break

def main():
    start_year = 2015
    start_month = 1
    end_year = 2024
    end_month = 7

    months = generate_months(start_year, start_month, end_year, end_month)

    with open('car_type_data.txt', 'w', encoding='utf-8') as f:
        # 写入表头
        f.write("车型名称\t销量数据\t售价\t级别\t年月\n")
        for year_month in months:
            print(f"抓取数据: 月份 {year_month}")
            fetch_and_save_data(year_month, f)

    print("数据抓取并保存完成")

if __name__ == '__main__':
    main()
