import requests
from bs4 import BeautifulSoup

# 自定义请求头，避免被反爬
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

# 抓取数据并保存到文件中
def fetch_and_save_data(year_month, file_handle):
    page = 1
    while True:
        url = f"https://xl.16888.com/brand-0-{year_month}-{year_month}-{page}.html"
        try:
            response = requests.get(url, headers=headers)
            response.raise_for_status()
            soup = BeautifulSoup(response.content, 'html.parser')

            # 输出调试信息，显示部分HTML内容
            print(f"正在处理: {url}")

            # 查找所有行(tr)
            rows = soup.find_all('tr')
            if not rows:
                print(f"未找到数据行: {url}")
                break

            data_found = False
            for row in rows:
                # 提取品牌名称
                brand_td = row.find('td', class_='xl-td-t2')
                if brand_td:
                    brand_name = brand_td.text.strip()

                    # 提取国别、销量和市场份额数据
                    tds = row.find_all('td', class_='xl-td-t3')
                    if len(tds) >= 3:
                        country = tds[0].text.strip()
                        sales = tds[1].text.strip().replace(',', '')  # 去掉销量中的逗号
                        market_share = tds[2].text.strip()

                        # 将数据按 Hive 格式保存，字段间用制表符分隔
                        file_handle.write(f"{brand_name}\t{country}\t{sales}\t{market_share}\t{year_month}\n")
                        data_found = True

            if not data_found:
                print(f"数据行为空或已结束: {url}")
                break

            page += 1
        except Exception as e:
            print(f"抓取数据失败: {url}, 错误: {e}")
            break

def main():
    start_year = 2019
    start_month = 1
    end_year = 2024
    end_month = 7

    months = generate_months(start_year, start_month, end_year, end_month)

    with open('brand_sales_data.txt', 'w', encoding='utf-8') as f:
        # 写入表头
        f.write("品牌名称\t国别\t销量\t市场份额\t年月\n")
        for year_month in months:
            print(f"抓取数据: 月份 {year_month}")
            fetch_and_save_data(year_month, f)

    print("数据抓取并保存完成")

if __name__ == '__main__':
    main()
