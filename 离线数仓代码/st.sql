
-- 关于执行顺序，请参考README.markdown文档
-- 本文件为hive数仓st层 建表、处理的总sql语句

-- ST层：品牌表 st_car_brand
drop table if exists st_car_brand;
create table if not exists st_car_brand (
    id int comment '主键ID',
    brand varchar(50) comment '品牌名称'
)
comment 'ST品牌表'
stored as textfile;

-- 从车系信息数据提取品牌表 ods_car_series_data -> st_car_brand
-- 插入去重后的品牌数据，并去除空值
insert overwrite table st_car_brand
select
    row_number() over () as id,
    brand_name
from
    (select distinct brand_name
     from ods_car_series_data
     where brand_name is not null and brand_name != '') as distinct_brands;

--查询表 验证数据文件是否映射成功
select * from st_car_brand limit 10;

--统计行数 231
select count(*) as cnt from st_car_brand;



-- ST层:车系表 st_car_series
drop table if exists st_car_series;
create table if not exists st_car_series (
    id int comment '车系ID',
    brand varchar(50) comment '品牌名称',
    series varchar(50) comment '车系名称',
    type varchar(50) comment '汽车类型',
    price_min decimal(10,2) comment '最低参考价格',
    price_max decimal(10,2) comment '最高参考价格'
)
comment 'ST车系表'
stored as textfile;

-- 从车系信息数据提取品牌表 ods_car_series_data -> st_car_series
-- 插入去重后的车系数据，并将价格范围拆分
insert overwrite table st_car_series
select
    row_number() over (order by brand_name, series_name) as id,
    brand_name as brand,
    series_name as series,
    car_type as type,  -- 提取汽车类型数据
    cast(regexp_replace(split(price_range, '-')[0], '万', '') as decimal(10,2)) as price_min,
    cast(regexp_replace(split(price_range, '-')[1], '万', '') as decimal(10,2)) as price_max
from
    (select distinct brand_name, series_name, car_type, price_range
     from ods_car_series_data
     where brand_name is not null and brand_name != ''
       and series_name is not null and series_name != ''
       and car_type is not null and car_type != ''
       and price_range is not null and price_range != '') as distinct_series;

--查询表 验证数据文件是否映射成功
select * from db_car.st_car_series limit 100;

--统计行数 1488
select count(*) as cnt from db_car.st_car_series;



-- ST层:车型表 st_car_tirm
drop table if exists st_car_tirm;
create table if not exists st_car_tirm (
    id int comment '车型ID',
    brand varchar(50) comment '品牌名称',
    series varchar(100) comment '车系名称',
    tirm varchar(100) comment '车型名称',
    energy_type varchar(50) comment '能源类型',
    type varchar(50) comment '汽车类型',
    price decimal(10,2) comment '参考价格'
)
comment 'ST车型表'
stored as textfile;

-- 从dwd车型表提取 dwd_car_tirm -> st_car_tirm
insert into table st_car_tirm
select
    tirm.id,
    brand.brand as brand,
    tirm.series,
    tirm.tirm,
    tirm.energy_type,
    tirm.type,
    tirm.price
from
    dwd_car_tirm tirm
join
    st_join_series_tirm join_series_tirm
on
    tirm.id = join_series_tirm.tirm_id
join
    st_join_brand_series join_brand_series
on
    join_series_tirm.series_id = join_brand_series.series_id
join
    st_car_brand brand
on
    join_brand_series.brand_id = brand.id;

--查询表 验证数据文件是否映射成功
select * from db_car.st_car_tirm limit 100;

--统计行数 1488
select count(*) as cnt from db_car.st_car_tirm;



-- ST层：品牌-车系关联表 st_join_brand_series
drop table if exists st_join_brand_series;
create table if not exists st_join_brand_series (
    brand_id int comment '品牌ID',
    series_id int comment '车系ID'
)
comment '品牌-车系关联表'
stored as textfile;

-- 插入品牌ID和车系ID到关联表中
insert overwrite table st_join_brand_series
select
    b.id as brand_id,
    s.id as series_id
from
    st_car_brand b
join
    st_car_series s
on
    b.brand = s.brand;

--查询表 验证数据文件是否映射成功
select * from st_join_brand_series limit 10;

--统计行数 1488
select count(*) as cnt from st_join_brand_series;



-- ST层：车系-车型关联表 st_join_series_tirm
drop table if exists st_join_series_tirm;
create table if not exists st_join_series_tirm (
    series_id int comment '车系ID',
    tirm_id int comment '车型ID'
)
comment '车系-车型关联表'
stored as textfile;

-- 插入数据到车系-车型关联表
insert into table st_join_series_tirm
select
    scs.id as series_id,
    dct.id as tirm_id
from
    st_car_series scs
join
    dwd_car_tirm dct
on
    scs.series = dct.series;

--查询表 验证数据文件是否映射成功
select * from st_join_series_tirm limit 10;

--统计行数 12765
select count(*) as cnt from st_join_series_tirm;



-- ST层：车型配置参数表 st_car_tirm_config
drop table if exists st_car_tirm_config;
create table if not exists st_car_tirm_config (
    id int comment '配置参数ID',
    tirm_id int comment '车型ID',
    config_type varchar(50) comment '配置类型',
    config_name varchar(50) comment '配置名称',
    config_value varchar(200) comment '配置参数'
)
comment 'ST车型配置参数表'
stored as textfile;

-- 从车型信息数据提取 ods_car_tirm_data -> st_car_tirm_config
insert overwrite table st_car_tirm_config
select
    row_number() over (order by tirm_name, config_type, config_name) as id,
    dct.id as tirm_id,
    ocd.config_type,
    ocd.config_name,
    ocd.config_value
from
    (select distinct tirm_name, config_type, config_name, config_value
     from ods_car_tirm_data
     where config_type is not null and config_type != ''
       and config_name is not null and config_name != '') ocd
join
    dwd_car_tirm dct
on
    ocd.tirm_name = dct.tirm;

--查询表 验证数据文件是否映射成功
select * from st_car_tirm_config limit 10;

--统计行数 2362010
select count(*) as cnt from st_car_tirm_config;



-- ST层：车系评价指标表 st_car_evl_idx
drop table if exists st_car_evl_idx;
create table if not exists st_car_evl_idx (
    id int comment '主键ID',
    series_id int comment '车系ID',
    overall_rating decimal(3,2) comment '综合评分',
    space decimal(3,2) comment '空间评分',
    drive_feel decimal(3,2) comment '驾驶感受评分',
    power_consum decimal(3,2) comment '能耗评分',
    out_decor decimal(3,2) comment '外观评分',
    in_decor decimal(3,2) comment '内饰评分',
    qp_ratio decimal(3,2) comment '性价比评分',
    configure decimal(3,2) comment '配置评分',
    record_time date comment '记录时间'
)
comment 'ST车系评价指标表'
stored as textfile;

-- 从dwd车系评价指标表提取 dwd_car_evl_idx -> st_car_evl_idx
-- 将车系名称替换为车系ID
insert overwrite table st_car_evl_idx
select
    dwd.id,
    st.id as series_id,
    dwd.overall_rating,
    dwd.space,
    dwd.drive_feel,
    dwd.power_consum,
    dwd.out_decor,
    dwd.in_decor,
    dwd.qp_ratio,
    dwd.configure,
    dwd.record_time
from
    dwd_car_evl_idx dwd
join
    st_car_series st
on
    dwd.series = st.series
where
    st.series is not null and st.series != '';

--查询表 验证数据文件是否映射成功
select * from st_car_evl_idx limit 10;

--统计行数 1488
select count(*) as cnt from st_car_evl_idx;



-- ST层：车系百车故障数分布 st_car_quality
drop table if exists st_car_quality;
create table if not exists st_car_quality (
    id int comment '主键ID',
    series_id int comment '车系ID',
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
    record_time date comment '记录时间'
)
comment 'ST车系百车故障数分布表'
stored as textfile;

-- 从dwd车系百车故障数分布表提取 dwd_car_quality -> st_car_quality
-- 将车系名称替换为车系ID
insert overwrite table st_car_quality
select
    dwd.id,
    st.id as series_id,
    dwd.count,
    dwd.exterior,
    dwd.driving,
    dwd.control,
    dwd.electronic,
    dwd.seat,
    dwd.ac_system,
    dwd.interior,
    dwd.power_train,
    dwd.transmission,
    dwd.record_time
from
    dwd_car_quality dwd
join
    st_car_series st
on
    dwd.series = st.series;

--查询表 验证数据文件是否映射成功
select * from st_car_quality limit 10;

--统计行数 1488
select count(*) as cnt from st_car_quality;



-- ST层：车系故障原因表 st_car_quality_reason
drop table if exists st_car_quality_reason;
create table if not exists st_car_quality_reason (
    id int comment '主键ID',
    series_id int comment '车系ID',
    reason varchar(255) comment '故障原因',
    count smallint comment '故障数',
    record_time date comment '记录时间'
)
comment 'ST车系故障原因表'
stored as textfile;

-- 从dwd车系故障原因表提取 dwd_car_quality_reason -> st_car_quality_reason
-- 将车系名称替换为车系ID
insert overwrite table st_car_quality_reason
select
    dwd.id,
    st.id as series_id,
    dwd.reason,
    dwd.count,
    dwd.record_time
from
    dwd_car_quality_reason dwd
join
    st_car_series st
on
    dwd.series = st.series;

--查询表 验证数据文件是否映射成功
select * from st_car_quality_reason limit 10;

--统计行数 6408
select count(*) as cnt from st_car_quality_reason;



-- ST层：车系购车目的表 st_car_purpose
drop table if exists st_car_purpose;
create table if not exists st_car_purpose (
    id int comment '主键ID',
    series_id int comment '车系ID',
    purpose varchar(255) comment '购车目的',
    cnt int comment '数量',
    percentage decimal(5,2) comment '占比',
    record_time date comment '记录时间'
)
comment 'ST车系购车目的表'
row format delimited
fields terminated by '\t'
stored as textfile;

-- 从dwd车系购车目的表提取 dwd_car_purpose -> st_car_purpose
-- 将车系名称替换为车系ID
insert overwrite table st_car_purpose
select
    dwd.id,
    st.id as series_id,
    dwd.purpose,
    dwd.cnt,
    dwd.percentage,
    dwd.record_time
from
    dwd_car_purpose dwd
join
    st_car_series st
on
    dwd.series = st.series;

--查询表 验证数据文件是否映射成功
select * from st_car_purpose limit 10;

--统计行数 9328
select count(*) as cnt from st_car_purpose;



-- ST层：车系图片表 st_car_series_img
drop table if exists st_car_series_img;
create table if not exists st_car_series_img (
    id int comment '主键ID',
    series_id int comment '车系ID',
    url varchar(300) comment '图片链接'
)
comment 'ST车系图片表'
stored as textfile;

-- 从车系图片数据提取 ods_car_series_img_data -> st_car_sales_total
insert into table st_car_series_img
select
    row_number() over (order by series_table.series) as id,  -- 明确指定series来自st_car_series表
    series_table.id as series_id,
    img.url
from
    (select distinct series, url from ods_car_series_img_data) img  -- 去重
join
    st_car_series series_table
on
    img.series = series_table.series;  -- 明确指定series来自两个不同的表

--查询表 验证数据文件是否映射成功
select * from st_car_series_img limit 10;

--统计行数 1546  1484
select count(*) as cnt from st_car_series_img;



-- ST层：车型图片表 st_car_tirm_img
drop table if exists st_car_tirm_img;
create table if not exists st_car_tirm_img (
    id int comment '主键ID',
    tirm_id int comment '车型ID',
    url varchar(300) comment '图片链接'
)
comment '车型图片表'
stored as textfile;

-- 从车型图片数据提取 ods_car_tirm_img_data -> st_car_tirm_img
insert into table st_car_tirm_img
select
    row_number() over (order by tirm_table.tirm) as id,  -- 明确指定tirm来自dwd_car_tirm表
    tirm_table.id as tirm_id,
    img.url
from
    (select distinct tirm, url from ods_car_tirm_img_data) img  -- 去重
join
    dwd_car_tirm tirm_table
on
    img.tirm = tirm_table.tirm;  -- 明确指定tirm来自两个不同的表

--查询表 验证数据文件是否映射成功
select * from st_car_tirm_img limit 10;

--统计行数 22256  21246
select count(*) as cnt from st_car_tirm_img;



-- ST层：汽车总销量表 st_car_sales_total
drop table if exists db_car.st_car_sales_total;
create table db_car.st_car_sales_total (
    year int comment '年份',
    month int comment '月份',
    total_sale int comment '销售总量'
)
comment 'ST汽车总销量表'
row format delimited
fields terminated by '\t'
stored as textfile;

-- 从汽车总销量数据提取 ods_car_sales_total_data -> st_car_sales_total
insert into table db_car.st_car_sales_total
select distinct  -- 去重
    cast(substring(year_month, 1, 4) as int) as year,
    cast(substring(year_month, 6, 2) as int) as month,
    total_sale
from db_car.ods_car_sales_total_data;

--查询表 验证数据文件是否映射成功
select * from st_car_sales_total limit 10;

--统计行数 211
select count(*) as cnt from st_car_sales_total;



-- ST层：车系销量表 st_car_sales_series
drop table if exists st_car_sales_series;
create table if not exists st_car_sales_series (
    id int comment '主键ID',
    series_id int comment '车系ID',
    year int comment '年份',
    month int comment '月份',
    total_sale int comment '总销量'
)
comment 'ST车系销量表'
stored as textfile;

-- 从车系总销量数据提取 ods_car_sales_series_data -> st_car_sales_series
insert overwrite table st_car_sales_series
select distinct  -- 去重
    row_number() over (order by st.id) as id,
    st.id as series_id,
    cast(substr(ods.year_month, 1, 4) as int) as year,
    cast(substr(ods.year_month, 6, 2) as int) as month,
    ods.total_sale
from
    ods_car_sales_series_data ods
join
    st_car_series st
on
    ods.series = st.series
where
    ods.series is not null
    and ods.series != '';

--查询表 验证数据文件是否映射成功
select * from st_car_sales_series limit 10;

--统计行数 28179
select count(*) as cnt from st_car_sales_series;



-- ST层：车系月销量前十表 st_car_series_sales_month_rank10
drop table if exists st_car_series_sales_month_rank10;
create table if not exists st_car_series_sales_month_rank10 (
    id int comment '主键ID',
    year int comment '年份',
    month int comment '月份',
    rank int comment '排名',
    series varchar(100) comment '车系名称',
    total_sale int comment '车系总销量'
)
comment 'ST车系月销量前十表'
stored as textfile;

-- 插入每个月销量前十的车系数据 ods_car_sales_series_data -> st_car_series_sales_month_rank10
insert overwrite table st_car_series_sales_month_rank10
select
    row_number() over (order by year, month, rank) as id,
    year,
    month,
    rank,
    series,
    total_sale
from (
    select
        year,
        month,
        series,
        total_sale,
        rank() over (partition by year, month order by total_sale desc) as rank
    from (
        select
            cast(substr(year_month, 1, 4) as int) as year,
            cast(substr(year_month, 6, 2) as int) as month,
            series,
            total_sale
        from
            ods_car_sales_series_data
        where
            cast(substr(year_month, 1, 4) as int) >= 2019
    ) as base_data
) ranked_data
where rank <= 10;

--查询表 验证数据文件是否映射成功
select * from st_car_series_sales_month_rank10 limit 10;

--统计行数 670
select count(*) as cnt from st_car_series_sales_month_rank10;



-- ST层：车系年销量前十表 st_car_series_sales_year_rank10
drop table if exists st_car_series_sales_year_rank10;
create table if not exists st_car_series_sales_year_rank10 (
    id int comment '主键ID',
    year int comment '年份',
    rank int comment '排名',
    series varchar(100) comment '车系名称',
    total_sale int comment '车系总销量'
)
comment 'ST车系年销量前十表'
stored as textfile;

-- 插入每年销量前十的车系数据 ods_car_sales_series_data -> st_car_series_sales_year_rank10
insert overwrite table st_car_series_sales_year_rank10
select
    row_number() over (order by year, rank) as id,
    year,
    rank,
    series,
    total_sale
from (
    select
        year,
        series,
        sum(total_sale) as total_sale,
        rank() over (partition by year order by sum(total_sale) desc) as rank
    from (
        select
            cast(substr(year_month, 1, 4) as int) as year,
            series,
            total_sale
        from
            ods_car_sales_series_data
        where
            cast(substr(year_month, 1, 4) as int) >= 2019
    ) as base_data
    group by
        year, series
) ranked_data
where rank <= 10;

--查询表 验证数据文件是否映射成功
select * from st_car_series_sales_year_rank10 limit 10;

--统计行数 60
select count(*) as cnt from st_car_series_sales_year_rank10;



-- ST层：品牌月销量前十表 st_car_brand_sales_rank10
drop table if exists st_car_brand_sales_month_rank10;
create table if not exists st_car_brand_sales_month_rank10 (
    id int comment '主键ID',
    year int comment '年份',
    month int comment '月份',
    rank int comment '排名',
    brand varchar(50) comment '品牌名称',
    total_sale int comment '品牌总销量'
)
comment 'ST品牌月销量前十表'
stored as textfile;

-- 插入每个月销量前十的品牌数据 ods_car_sales_brand_data -> st_car_brand_sales_month_rank10
insert overwrite table st_car_brand_sales_month_rank10
select
    row_number() over (order by year, month, rank) as id,
    year,
    month,
    rank,
    brand,
    total_sale
from (
    select
        year,
        month,
        brand,
        total_sale,
        rank() over (partition by year, month order by total_sale desc) as rank
    from (
        select
            cast(substr(year_month, 1, 4) as int) as year,
            cast(substr(year_month, 5, 2) as int) as month,
            brand,
            total_sale
        from
            ods_car_sales_brand_data
    ) as base_data
) ranked_data
where rank <= 10;

--查询表 验证数据文件是否映射成功
select * from st_car_brand_sales_month_rank10 limit 10;

--统计行数 670
select count(*) as cnt from st_car_brand_sales_month_rank10;



-- ST层：品牌年销量前十表 st_car_brand_sales_year_rank10
drop table if exists st_car_brand_sales_year_rank10;
create table if not exists st_car_brand_sales_year_rank10 (
    id int comment '主键ID',
    year int comment '年份',
    rank int comment '排名',
    brand varchar(50) comment '品牌名称',
    total_sale int comment '品牌总销量'
)
comment 'ST品牌年销量前十表'
stored as textfile;

-- 插入每年销量前十的品牌数据 ods_car_sales_brand_data -> st_car_brand_sales_year_rank10
insert overwrite table st_car_brand_sales_year_rank10
select
    row_number() over (order by year, rank) as id,
    year,
    rank,
    brand,
    total_sale
from (
    select
        year,
        brand,
        sum(total_sale) as total_sale,
        rank() over (partition by year order by sum(total_sale) desc) as rank
    from (
        select
            cast(substr(year_month, 1, 4) as int) as year,
            brand,
            total_sale
        from
            ods_car_sales_brand_data
    ) as base_data
    group by year, brand
) ranked_data
where rank <= 10;

--查询表 验证数据文件是否映射成功
select * from st_car_brand_sales_year_rank10 limit 10;

--统计行数 60
select count(*) as cnt from st_car_brand_sales_year_rank10;



--  ST层：汽车城市销量表 st_car_sales_city
drop table if exists st_car_sales_city;
create table if not exists st_car_sales_city (
    id int comment '主键ID',
    province varchar(50) comment '省份',
    city varchar(50) comment '城市',
    total_sale int comment '城市总销量',
    percentage decimal(4,2) comment '城市销量占比'
)
comment 'ST城市销量表'
stored as textfile;

-- 从汽车城市销量数据表提取，加入省份信息 ods_car_sales_city_data -> st_car_sales_city
insert overwrite table st_car_sales_city
select
    row_number() over (order by sum(total_sale) desc) as id,  -- 根据总销量降序排列
    mapping.province,
    city_data.city,
    sum(city_data.total_sale) as total_sale,
    round(sum(city_data.total_sale) * 100.0 / (select sum(total_sale) from ods_car_sales_city_data), 2) as percentage
from
    ods_car_sales_city_data city_data
join
    ods_city_province_mapping mapping
on
    city_data.city = mapping.city
group by
    mapping.province, city_data.city;

-- 查询表，验证数据文件是否映射成功
select * from st_car_sales_city limit 10;

-- 统计行数 327
select count(*) as cnt from st_car_sales_city;



--  ST层：城市销量前十车系表 st_car_city_series_rank10
drop table if exists st_car_city_series_rank10;
create table if not exists st_car_city_series_rank10 (
    id int comment '主键ID',
    province varchar(50) comment '省份',
    city varchar(50) comment '城市',
    ranking int comment '排名',
    series varchar(100) comment '车系名称',
    total_sale int comment '车系总销量'
)
comment 'ST城市销量前十车系排名表'
stored as textfile;

-- 从汽车城市销量数据表提取，加入省份信息 ods_car_sales_city_data -> st_car_city_series_rank10
insert overwrite table st_car_city_series_rank10
select
    row_number() over (order by mapping.province, ranking_data.city, ranking_data.ranking) as id,  -- 根据省份、城市、排名生成ID
    mapping.province,
    ranking_data.city,
    ranking_data.ranking,
    ranking_data.series,
    ranking_data.total_sale
from (
    select
        city_data.city as city,  -- 添加别名，消除歧义
        city_data.series as series,
        city_data.total_sale as total_sale,
        row_number() over (partition by city_data.city order by city_data.total_sale desc) as ranking
    from
        ods_car_sales_city_data city_data
) ranking_data
join
    ods_city_province_mapping mapping
on
    ranking_data.city = mapping.city  -- 确保JOIN条件唯一
where
    ranking_data.ranking <= 10;

-- 查询表，验证数据是否映射成功
select * from st_car_city_series_rank10 limit 10;

-- 统计行数
select count(*) as cnt from st_car_city_series_rank10;
