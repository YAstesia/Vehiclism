# 爬取汽车总销量信息（年、月）
# Author：杨畅

import requests
from bs4 import BeautifulSoup

# 爬取地址
base_url = 'https://xl.16888.com/month-{}.html'
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3'
}

# 存储所有页的数据
all_data = []

# 爬取第1到第5页的数据
for page_num in range(1, 6):
    url = base_url.format(page_num)
    response = requests.get(url, headers=headers)
    soup = BeautifulSoup(response.text, 'html.parser')

    # 查找表格
    table = soup.find('table', class_='xl-table-def xl-table-a')
    if not table:
        continue  # 如果页面没有找到表格，跳过

    rows = table.find_all('tr')[1:]  # 忽略表头

    # 存储当前页面的数据
    for row in rows:
        cols = row.find_all('td')
        time = cols[0].text.strip()
        sales = cols[1].text.strip()
        yoy = cols[2].text.strip()
        all_data.append([time, sales, yoy])

# 保存为纯文本文件，使用制表符分隔
txt_file = 'car_sales_total_data.txt'
with open(txt_file, 'w', encoding='utf-8') as f:
    for row in all_data:
        f.write('\t'.join(row) + '\n')

print(f"数据已保存为 {txt_file}")
