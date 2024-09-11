# README.数据库文件



本文件夹内为离线数仓中进行数据表建立、数据清洗处理等操作的sql文件。



关于本文件夹中文件：

| 文件名称          | 说明                                  |
| ----------------- | ------------------------------------- |
| hive.sql          | hive数仓建库、建表、数据处理总sql语句 |
| ods.sql           | hive数仓ods层sql语句                  |
| dwd.sql           | hive数仓dwd层sql语句                  |
| st.sql            | hive数仓st层sql语句                   |
| springBootSql.sql | 后端数据库建表sql语句                 |



以下为离线数仓中表格简要说明，关于各个表格及架构的详细说明，可参考《详细设计文档》-第五部分 单元模块设计-大数据模块

| 表格名称                         | 表格说明             | 数仓层 | 来源                                                         | 执行顺序       |
| -------------------------------- | -------------------- | ------ | ------------------------------------------------------------ | -------------- |
| ods_car_series_data              | 车系信息数据表       | ODS    | 爬取文件 car_series_data.txt                                 | 1              |
| ods_car_tirm_data                | 车型信息数据表       | ODS    | 爬取文件 car_tirm_data.txt                                   | 1              |
| ods_car_purpose_data             | 车系购车目的数据表   | ODS    | 爬取文件 car_purpose_data.txt                                | 1              |
| ods_car_quality_data             | 车系故障数据表       | ODS    | 爬取文件 car_quality_data.txt                                | 1              |
| ods_car_series_img_data          | 车系图片数据表       | ODS    | 爬取文件car_sales_series_data.txt                            | 1              |
| ods_car_tirm_img_data            | 车型图片数据表       | ODS    | 爬取文件 car_tirm_img_data.txt                               | 1              |
| ods_car_sales_total_data         | 汽车总销量数据表     | ODS    | 爬取文件 car_sales_total_data.txt                            | 1              |
| ods_car_sales_series_data        | 汽车车系销量数据表   | ODS    | 爬取文件 car_sales_series_data.txt                           | 1              |
| ods_car_sales_brand_data         | 汽车品牌销量数据表   | ODS    | 爬取文件 car_sales_brand_data.txt                            | 1              |
| ods_car_sales_city_data          | 汽车城市销量数据表   | ODS    | 爬取文件 car_sales_city_data.txt                             | 1              |
| ods_city_province_mapping        | 城市与省份对照表     | ODS    | 将dwd_city经pyhon文件 [cityToProvince.py](..\..\..\..\..\Python\Project\car_big_data\cityToProvince.py) 处理后得到 | dwd_city创建后 |
| dwd_car_tirm                     | 车型表               | DW-dwd | ods_car_tirm_data                                            | 2              |
| dwd_car_evl_idx                  | 车系评价指标表       | DW-dwd | ods_car_series_data                                          | 2              |
| dwd_car_quality                  | 车系百车故障数分布表 | DW-dwd | ods_car_quality_data                                         | 2              |
| dwd_car_quality_reason           | 车系故障原因数据表   | DW-dwd | ods_car_quality_data                                         | 2              |
| dwd_car_purpose                  | 车系购车目的表       | DW-dwd | ods_car_purpose_data                                         | 2              |
| dwd_city                         | 城市表               | DW-dwd | ods_car_sales_city_data                                      | 2              |
| st_car_brand                     | 品牌表               | ST     | ods_car_series_data                                          | 3              |
| st_car_series                    | 车系表               | ST     | ods_car_series_data                                          | 3              |
| st_join_brand_series             | 品牌-车系关联表      | ST     | st_car_brand 、st_car_series s                               | 4              |
| st_join_series_tirm              | 车系-车型关联表      | ST     | st_car_series、dwd_car_tirm                                  | 4              |
| t_car_tirm                       | 车型表               | ST     | dwd_car_tirm                                                 | 5              |
| st_car_tirm_config               | 车型配置参数表       | ST     | ods_car_tirm_data、dwd_car_tirm dct                          | 5              |
| st_car_evl_idx                   | 车系评价指标表       | ST     | dwd_car_evl_idx、st_car_series                               | 5              |
| st_car_quality                   | 车系百车故障数分布   | ST     | dwd_car_quality、st_car_series                               | 5              |
| st_car_quality_reason            | 车系故障原因表       | ST     | dwd_car_quality_reason、st_car_series                        | 5              |
| st_car_purpose                   | 车系购车目的表       | ST     | dwd_car_purpose、st_car_series                               | 5              |
| st_car_series_img                | 车系图片表           | ST     | ods_car_series_img_data、st_car_series                       | 5              |
| st_car_tirm_img                  | 车型图片表           | ST     | ods_car_tirm_img_data、dwd_car_tirm                          | 5              |
| st_car_sales_total               | 汽车总销量表         | ST     | ods_car_sales_total_data                                     | 5              |
| st_car_sales_series              | 车系销量表           | ST     | ods_car_sales_series_data、st_car_series                     | 5              |
| st_car_series_sales_month_rank10 | 车系月销量前十表     | ST     | ods_car_sales_series_data                                    | 5              |
| st_car_series_sales_year_rank10  | 车系年销量前十表     | ST     | ods_car_sales_series_data                                    | 5              |
| st_car_brand_sales_rank10        | 品牌月销量前十表     | ST     | ods_car_sales_brand_data                                     | 5              |
| st_car_brand_sales_year_rank10   | 品牌年销量前十表     | ST     | ods_car_sales_brand_data                                     | 5              |
| st_car_sales_city                | 汽车城市销量表       | ST     | ods_car_sales_city_data、ods_city_province_mapping           | 5              |
| st_car_city_series_rank10        | 城市销量前十车系表   | ST     | ods_car_sales_city_data、ods_city_province_mapping           | 5              |



其他说明：

- 文件负责人：杨畅
- 模块负责人：杨畅

