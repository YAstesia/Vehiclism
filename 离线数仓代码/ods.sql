
-- 关于执行顺序，请参考README.markdown文档
-- 本文件为hive数仓ods层 建表、处理的总sql语句

-- ODS层：车系信息数据表 ods_car_series_data
drop table if exists ods_car_series_data;
create table if not exists ods_car_series_data (
    brand_name varchar(50) comment '品牌名称',
    series_name varchar(100) comment '车系名称',
    car_type varchar(50) comment '汽车类型',
    price_range varchar(20) comment '车系价格',
    overall_rating decimal(3,2) comment '综合评分',
    space_rating decimal(3,2) comment '空间评分',
    driving_rating decimal(3,2) comment '驾驶感受评分',
    fuel_rating decimal(3,2) comment '能耗评分',
    appearance_rating decimal(3,2) comment '外观评分',
    interior_rating decimal(3,2) comment '内饰评分',
    cost_performance_rating decimal(3,2) comment '性价比评分',
    configuration_rating decimal(3,2) comment '配置评分'
)
comment 'ODS车系信息数据表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/car_series_data.txt' into table db_car.ods_car_series_data;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_car_series_data limit 10;

--统计行数 1550
select count(*) as cnt from db_car.ods_car_series_data;



-- ODS层：车系购车目的数据表 ods_car_purpose_data
drop table if exists ods_car_purpose_data;
create table if not exists ods_car_purpose_data (
    series varchar(100) comment '车系名称',
    purpose varchar(50) comment '购车目的',
    purpose_cnt varchar(50) comment '数量'
)
comment 'ODS车系购车目的数据表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/car_purpose_data.txt' into table db_car.ods_car_purpose_data;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_car_purpose_data limit 100;

--统计行数 9328
select count(*) as cnt from db_car.ods_car_purpose_data;



-- ODS层：车型信息数据表 ods_car_tirm_data
drop table if exists ods_car_tirm_data;
create table if not exists ods_car_tirm_data (
    series_name varchar(100) comment '车系名称',
    tirm_name varchar(100) comment '车型名称',
    config_type varchar(50) comment '参数配置类型',
    config_name varchar(50) comment '参数配置名称',
    config_value varchar(300) comment '参数配置数值'
)
comment 'ODS车型信息数据表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/car_tirm_configurations_data.txt' into table db_car.ods_car_tirm_data;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_car_tirm_data limit 100;

--统计行数 9328
select count(*) as cnt from db_car.ods_car_tirm_data;



-- ODS层：车系故障数据表 ods_car_quality_data
drop table if exists ods_car_quality_data;
create table if not exists ods_car_quality_data (
    series varchar(100) comment '车系名称',
    count int comment '百车故障数',
    exterior int comment '车身外观',
    driving int comment '行驶过程',
    control int comment '功能操作',
    electronic int comment '电子设备',
    seat int comment '座椅',
    ac_system int comment '空调系统',
    interior int comment '内饰',
    power_train int comment '动力系统',
    transmission int comment '变速系统',
    reason_1 varchar(100) comment '故障原因_1',
    cnt_1 int comment '故障数_1',
    reason_2 varchar(100) comment '故障原因_2',
    cnt_2 int comment '故障数_2',
    reason_3 varchar(100) comment '故障原因_3',
    cnt_3 int comment '故障数_3',
    reason_4 varchar(100) comment '故障原因_4',
    cnt_4 int comment '故障数_4',
    reason_5 varchar(100) comment '故障原因_5',
    cnt_5 int comment '故障数_5',
    reason_6 varchar(100) comment '故障原因_6',
    cnt_6 int comment '故障数_6',
    reason_7 varchar(100) comment '故障原因_7',
    cnt_7 int comment '故障数_7',
    reason_8 varchar(100) comment '故障原因_8',
    cnt_8 int comment '故障数_8',
    reason_9 varchar(100) comment '故障原因_9',
    cnt_9 int comment '故障数_9',
    reason_10 varchar(100) comment '故障原因_10',
    cnt_10 int comment '故障数_10'
)
comment 'ODS车系故障数据表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/car_quality_data.txt' into table db_car.ods_car_quality_data;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_car_quality_data limit 100;

--统计行数 1489
select count(*) as cnt from db_car.ods_car_quality_data;



-- ODS层：车系图片数据表 ods_car_series_img_data
drop table if exists ods_car_series_img_data;
create table if not exists ods_car_series_img_data (
    series varchar(100) comment '车系名称',
    url varchar(300) comment '图片链接'
)
comment 'ODS车系图片数据表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/car_series_image_data.txt' into table db_car.ods_car_series_img_data;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_car_series_img_data limit 100;

--统计行数 1550
select count(*) as cnt from db_car.ods_car_series_img_data;



-- ODS层：车型图片数据表 ods_car_tirm_img_data
drop table if exists ods_car_tirm_img_data;
create table if not exists ods_car_tirm_img_data (
    brand varchar(50) comment '品牌名称',
    tirm varchar(100) comment '车型名称',
    url varchar(300) comment '图片链接'
)
comment 'ODS车型图片数据表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/car_tirm_image_data.txt' into table db_car.ods_car_tirm_img_data;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_car_tirm_img_data limit 100;

--统计行数 24421
select count(*) as cnt from db_car.ods_car_tirm_img_data;



-- ODS层：汽车总销量数据表 ods_car_sales_total_data
drop table if exists ods_car_sales_total_data;
create table if not exists ods_car_sales_total_data (
    year_month varchar(100) comment '车系名称',
    total_sale int comment '总销量'
)
comment 'ODS汽车总销量数据表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/car_sales_data.txt' into table db_car.ods_car_sales_total_data;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_car_sales_total_data limit 100;

--统计行数 211
select count(*) as cnt from db_car.ods_car_sales_total_data;



-- ODS层：汽车车系销量数据表 ods_car_sales_series_data
drop table if exists ods_car_sales_series_data;
create table if not exists ods_car_sales_series_data (
    series varchar(100) comment '车型名称',
    total_sale int comment '总销量',
    price varchar(20) comment '价格',
    type varchar(50) comment '汽车类型',
    year_month varchar(100) comment '时间'
)
comment 'ODS汽车车系销量数据表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/car_series_sales_data.txt' into table db_car.ods_car_sales_series_data;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_car_sales_series_data limit 100;

--统计行数 60250
select count(*) as cnt from db_car.ods_car_sales_series_data;



-- ODS层：汽车品牌销量数据表 ods_car_sales_brand_data
drop table if exists ods_car_sales_brand_data;
create table if not exists ods_car_sales_brand_data (
    brand varchar(100) comment '品牌名称',
    country varchar(20) comment '国别',
    total_sale int comment '总销量',
    market_share varchar(20) comment '市场份额',
    year_month varchar(100) comment '时间'
)
comment 'ODS汽车品牌销量数据表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/car_brand_sales_data.txt' into table db_car.ods_car_sales_brand_data;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_car_sales_brand_data limit 100;

--统计行数 6612
select count(*) as cnt from db_car.ods_car_sales_brand_data;



-- ODS层：汽车城市销量数据表 ods_car_sales_city_data
drop table if exists ods_car_sales_city_data;
create table if not exists ods_car_sales_city_data (
    city varchar(100) comment '城市名称',
    series varchar(100) comment '车系名称',
    total_sale int comment '总销量'
)
comment 'ODS汽车城市销量数据表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/car_sales_region_data.txt' into table db_car.ods_car_sales_city_data;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_car_sales_city_data limit 100;

--统计行数 137721
select count(*) as cnt from db_car.ods_car_sales_city_data;


-- ODS层：城市与省份对照表 ods_city_province_mapping
drop table if exists ods_city_province_mapping;
create table if not exists ods_city_province_mapping (
    city varchar(50) comment '城市',
    province varchar(50) comment '省份'
)
comment 'ODS层城市与省份对照表'
row format delimited
fields terminated by '\t'
stored as textfile;
load data local inpath '/root/data/proj-carBigData/log/city_province_mapping.txt' into table db_car.ods_city_province_mapping;

--查询表 验证数据文件是否映射成功
select * from db_car.ods_city_province_mapping limit 100;

--统计行数 327
select count(*) as cnt from db_car.ods_city_province_mapping;