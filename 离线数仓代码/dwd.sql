
-- 关于执行顺序，请参考README.markdown文档
-- 本文件为hive数仓DW-dwd层 建表、处理的总sql语句

-- dwd层：车型表 （缺少品牌名称） dwd_car_tirm
drop table if exists dwd_car_tirm;
create table if not exists dwd_car_tirm (
    id int comment '车型ID',
    series varchar(100) comment '车系名称',
    tirm varchar(100) comment '车型名称',
    energy_type varchar(50) comment '能源类型',
    type varchar(50) comment '汽车类型',
    price decimal(10,2) comment '参考价格'
)
comment 'dwd车型表'
stored as textfile;

-- 从车型信息数据提取品牌表 ods_car_tirm_data -> st_car_brand
-- 插入去重后的车型数据  price存在null情况
insert overwrite table dwd_car_tirm
select
    row_number() over (order by series_name, tirm_name) as id,
    series_name as series,
    tirm_name as tirm,
    max(case when config_name = '能源类型' then config_value else null end) as energy_type,
    max(case when config_name = '级别' then config_value else null end) as type,
    cast(regexp_replace(max(case when config_name = '厂商指导价(元)' then config_value else '-' end), '万', '') as decimal(10,2)) as price
from
    db_car.ods_car_tirm_data
where
    config_name in ('能源类型', '级别', '厂商指导价(元)')
group by
    series_name, tirm_name;

--查询表 验证数据文件是否映射成功
select * from db_car.dwd_car_tirm limit 100;

--统计行数 12765
select count(*) as cnt from db_car.dwd_car_tirm;



-- dwd层：车系评价指标表 dwd_car_evl_idx
-- 为车系名称，不是车系ID
drop table if exists dwd_car_evl_idx;
create table if not exists dwd_car_evl_idx (
    id int comment '主键ID',
    series varchar(50) comment '车系名称',
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
comment 'dwd车系评价指标表'
stored as textfile;

-- -- 从车型信息数据提取品牌表 ods_car_series_data -> dwd_car_evl_idx
insert overwrite table dwd_car_evl_idx
select
    row_number() over (order by series_name) as id,
    series_name as series,
    overall_rating,
    space_rating as space,
    driving_rating as drive_feel,
    fuel_rating as power_consum,
    appearance_rating as out_decor,
    interior_rating as in_decor,
    cost_performance_rating as qp_ratio,
    configuration_rating as configure,
    current_date() as record_time  -- 记录当前日期作为记录时间
from
    (select distinct series_name, overall_rating, space_rating, driving_rating,
                     fuel_rating, appearance_rating, interior_rating, cost_performance_rating,
                     configuration_rating
     from ods_car_series_data
     where series_name is not null and series_name != '') as distinct_series;

--查询表 验证数据文件是否映射成功
select * from db_car.dwd_car_evl_idx limit 100;

--统计行数 1488
select count(*) as cnt from db_car.dwd_car_evl_idx;



-- dwd层：车系百车故障数分布表 dwd_car_quality
drop table if exists dwd_car_quality;
create table if not exists dwd_car_quality (
    id int comment '主键ID',
    series varchar(50) comment '车系名称',
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
comment 'dwd车系百车故障数分布表'
stored as textfile;

-- 从汽车故障信息数据提取 ods_car_quality_data -> dwd_car_evl_idx
insert overwrite table dwd_car_quality
select
    row_number() over (order by series) as id,
    series,
    count,
    coalesce(exterior, 0),   -- 对 null 的数据用 0 插入
    coalesce(driving, 0),
    coalesce(control, 0),
    coalesce(electronic, 0),
    coalesce(seat, 0),
    coalesce(ac_system, 0),
    coalesce(interior, 0),
    coalesce(power_train, 0),
    coalesce(transmission, 0),
    current_date() as record_time
from
    (select distinct series, count, exterior, driving, control, electronic,
                     seat, ac_system, interior, power_train, transmission
     from ods_car_quality_data) as distinct_data;

--查询表 验证数据文件是否映射成功
select * from db_car.dwd_car_quality limit 100;

--统计行数 1489
select count(*) as cnt from db_car.dwd_car_quality;



-- dwd层：车系故障原因数据表 dwd_car_quality_reason
drop table if exists dwd_car_quality_reason;
create table if not exists dwd_car_quality_reason (
    id int comment '主键ID',
    series varchar(50) comment '车系名称',
    reason varchar(255) comment '故障原因',
    count int comment '故障数',
    record_time date comment '记录时间'
)
comment 'dwd车系故障原因数据表'
stored as textfile;

-- 从汽车故障信息数据提取 ods_car_quality_data -> dwd_car_quality_reason
insert overwrite table dwd_car_quality_reason
select
    row_number() over (order by series, reason) as id,
    series,
    reason,
    count,
    current_date() as record_time
from (
    select series, reason_1 as reason, cnt_1 as count from ods_car_quality_data where reason_1 is not null and reason_1 != ''
    union all
    select series, reason_2 as reason, cnt_2 as count from ods_car_quality_data where reason_2 is not null and reason_2 != ''
    union all
    select series, reason_3 as reason, cnt_3 as count from ods_car_quality_data where reason_3 is not null and reason_3 != ''
    union all
    select series, reason_4 as reason, cnt_4 as count from ods_car_quality_data where reason_4 is not null and reason_4 != ''
    union all
    select series, reason_5 as reason, cnt_5 as count from ods_car_quality_data where reason_5 is not null and reason_5 != ''
    union all
    select series, reason_6 as reason, cnt_6 as count from ods_car_quality_data where reason_6 is not null and reason_6 != ''
    union all
    select series, reason_7 as reason, cnt_7 as count from ods_car_quality_data where reason_7 is not null and reason_7 != ''
    union all
    select series, reason_8 as reason, cnt_8 as count from ods_car_quality_data where reason_8 is not null and reason_8 != ''
    union all
    select series, reason_9 as reason, cnt_9 as count from ods_car_quality_data where reason_9 is not null and reason_9 != ''
    union all
    select series, reason_10 as reason, cnt_10 as count from ods_car_quality_data where reason_10 is not null and reason_10 != ''
) as all_reasons;

--查询表 验证数据文件是否映射成功
select * from db_car.dwd_car_quality_reason limit 100;

--统计行数 6408
select count(*) as cnt from db_car.dwd_car_quality_reason;



-- dwd层：车系购车目的表 dwd_car_purpose
drop table if exists dwd_car_purpose;
create table if not exists dwd_car_purpose (
    id int comment '主键ID',
    series varchar(50) comment '车系名称',
    purpose varchar(255) comment '购车目的',
    cnt int comment '数量',
    percentage decimal(5,2) comment '占比',
    record_time date comment '记录时间'
)
comment 'dwd车系购车目的表'
row format delimited
fields terminated by '\t'
stored as textfile;

-- 从汽车购车目的数据提取 ods_car_purpose_data -> dwd_car_purpose
insert overwrite table dwd_car_purpose
select
    row_number() over (order by series, purpose) as id,
    series,
    purpose,
    sum(cast(purpose_cnt as int)) as cnt,
    cast(sum(cast(purpose_cnt as int)) as decimal(10,2)) / total_count * 100 as percentage,
    current_date() as record_time
from (
    select
        series,
        purpose,
        sum(cast(purpose_cnt as int)) as purpose_cnt,
        sum(sum(cast(purpose_cnt as int))) over (partition by series) as total_count
    from
        ods_car_purpose_data
    group by
        series, purpose
) as purpose_data
group by series, purpose, total_count;

--查询表 验证数据文件是否映射成功
select * from db_car.dwd_car_purpose limit 100;

--统计行数 9328
select count(*) as cnt from db_car.dwd_car_purpose;



-- dwd层：城市表 dwd_city
drop table if exists dwd_city;
create table if not exists dwd_city (
    city varchar(50) comment '城市'
)
comment 'dwd城市表'
row format delimited
fields terminated by '\t'
stored as textfile;

-- 从汽车城市销量数据表提取 ods_car_sales_city_data -> dwd_city
-- 插入去重后的城市数据
insert overwrite table dwd_city
select distinct city
from ods_car_sales_city_data
where city is not null and city != '';

-- 查询表验证是否提取成功
select * from dwd_city limit 100;

-- 统计行数 327
select count(*) as cnt from dwd_city;