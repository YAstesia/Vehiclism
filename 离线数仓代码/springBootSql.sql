
-- 本文件为后端数据库的建表总sql语句
-- 数据可于文件夹：st层数据 中导入

create table admin
(
    id       bigint unsigned auto_increment comment '管理员主键ID'
        primary key,
    name     varchar(50)  not null comment '管理员姓名',
    password varchar(255) not null comment '管理员密码',
    email    varchar(50)  not null comment '管理员邮箱'
)
    comment '管理员信息表';

create table all_car_sale
(
    id         bigint unsigned auto_increment
        primary key,
    year       smallint not null,
    month      smallint not null,
    total_sale int      not null
);

create table car_brand
(
    id    bigint unsigned auto_increment comment '品牌主键ID'
        primary key,
    brand varchar(50) not null comment '品牌名'
)
    comment '品牌表';

create table car_brand_sale_month
(
    id         bigint unsigned auto_increment comment '主键ID'
        primary key,
    year       smallint    not null comment '年份',
    month      smallint    not null comment '月份',
    ranking    smallint    not null comment '排名',
    brand      varchar(50) not null comment '品牌名称',
    total_sale int         not null comment '车系总销量'
);

create table car_brand_sale_year
(
    id         bigint unsigned auto_increment comment '主键ID'
        primary key,
    year       smallint    not null comment '年份',
    ranking    smallint    not null comment '排名',
    brand      varchar(50) not null comment '车系名称',
    total_sale int         not null comment '车系总销量'
);

create table car_defect
(
    id           bigint unsigned auto_increment comment '主键ID'
        primary key,
    series_id    int  not null comment '车系ID',
    count        int  not null comment '百车故障数（为0时可能是评价人数少/若所有指标均为0则表示暂无信息）',
    exterior     int  not null comment '车身外观',
    driving      int  not null comment '行驶过程',
    control      int  not null comment '功能操作',
    electronic   int  not null comment '电子设备',
    seat         int  not null comment '座椅',
    ac_system    int  not null comment '空调系统',
    interior     int  not null comment '内饰',
    power_train  int  not null comment '动力系统',
    transmission int  not null comment '变速系统',
    record_time  date not null comment '记录时间'
);

create table car_defect_reason
(
    id        bigint unsigned auto_increment comment '主键ID'
        primary key,
    series_id varchar(50)  not null comment '车系ID',
    reason    varchar(255) not null comment '故障原因',
    count     smallint     not null comment '故障数',
    time      date         not null comment '记录时间'
);

create table car_evl_idx
(
    id             bigint unsigned auto_increment comment '主键ID'
        primary key,
    series_id      varchar(50)   not null comment '车系ID',
    overall_rating decimal(3, 2) not null comment '综合评分',
    space          decimal(3, 2) not null comment '空间评分',
    drive_feel     decimal(3, 2) not null comment '驾驶感受评分',
    power_consum   decimal(3, 2) not null comment '能耗评分',
    out_decor      decimal(3, 2) not null comment '外观评分',
    in_decor       decimal(3, 2) not null comment '内饰评分',
    qp_ratio       decimal(3, 2) not null comment '性价比评分',
    configure      decimal(3, 2) not null comment '配置评分',
    time           date          not null comment '记录时间'
);

create table car_purchase_purpose
(
    id         bigint unsigned auto_increment comment '主键ID'
        primary key,
    series_id  varchar(50)   not null comment '车系ID',
    purpose    varchar(255)  not null comment '购车目的',
    cnt        int           not null comment '数量',
    percentage decimal(5, 2) not null comment '占比',
    time       date          not null comment '记录时间'
);

create table car_regional_sale
(
    id         bigint unsigned auto_increment
        primary key,
    region     varchar(50)   not null,
    total_sale int           not null,
    percentage decimal(4, 2) not null,
    province   varchar(50)   null
);

create table car_regional_series_sale
(
    id         bigint unsigned auto_increment
        primary key,
    region     varchar(50) not null,
    ranking    smallint    not null,
    series     varchar(50) not null,
    total_sale int         not null,
    province   varchar(50) null
);

create table car_sales
(
    id         bigint unsigned auto_increment comment '主键ID'
        primary key,
    series_id  bigint unsigned not null comment '车系ID',
    year       smallint        not null comment '年份',
    month      smallint        not null comment '月份',
    total_sale int             not null comment '总销量'
)
    comment '汽车销售记录表';

create table car_series
(
    id        bigint unsigned auto_increment comment '车系ID'
        primary key,
    brand     varchar(50)    not null comment '品牌名',
    series    varchar(50)    not null comment '车系',
    type      varchar(50)    not null comment '汽车类型',
    price_min decimal(10, 2) null comment '最低价格',
    price_max decimal(10, 2) null comment '最高价格'
)
    comment '车系表';

create table car_series_img
(
    id        bigint unsigned auto_increment comment '图片ID'
        primary key,
    series_id bigint       not null comment '车系ID',
    url       varchar(255) not null comment '图片路径'
)
    comment '车系图片表';

create table car_series_sale_month
(
    id         bigint unsigned auto_increment comment '主键ID'
        primary key,
    year       smallint    not null comment '年份',
    month      smallint    not null comment '月份',
    ranking    smallint    not null comment '排名',
    series     varchar(50) not null comment '车系名称',
    total_sale int         not null comment '车系总销量'
);

create table car_series_sale_year
(
    id         bigint unsigned auto_increment comment '主键ID'
        primary key,
    year       smallint    not null comment '年份',
    ranking    smallint    not null comment '排名',
    series     varchar(50) not null comment '车系名称',
    total_sale int         not null comment '车系总销量'
);

create table car_tirm
(
    id          bigint unsigned auto_increment comment '车型ID'
        primary key,
    brand       varchar(50)    not null comment '品牌名',
    series      varchar(50)    not null comment '车系',
    tirm        varchar(50)    not null comment '车型',
    energy_type varchar(50)    not null comment '能源类型',
    type        varchar(50)    not null comment '汽车类型',
    price       decimal(10, 2) null comment '参考价格'
)
    comment '车型表';

create table car_tirm_config
(
    id           bigint unsigned auto_increment comment '配置参数ID'
        primary key,
    tirm_id      bigint unsigned not null comment '车型ID',
    config_type  varchar(50)     not null comment '配置类型',
    config_name  varchar(50)     not null comment '配置名称',
    config_value varchar(200)    null comment '配置参数'
);

create table car_tirm_img
(
    id      bigint unsigned auto_increment comment '图片ID'
        primary key,
    tirm_id bigint       not null comment '车型ID',
    url     varchar(255) not null comment '图片路径'
)
    comment '车型图片表';

create table join_brand_series
(
    id        bigint unsigned auto_increment comment '主键ID'
        primary key,
    brand_id  bigint unsigned not null comment '品牌ID',
    series_id bigint unsigned not null comment '车系ID'
)
    comment '品牌-车系关联表';

create table join_series_tirm
(
    id        bigint unsigned auto_increment comment '主键ID'
        primary key,
    series_id bigint unsigned not null comment '车系ID',
    tirm_id   bigint unsigned not null comment '车型ID'
)
    comment '车系-车型关联表';

create table user
(
    id       bigint unsigned auto_increment comment '用户主键ID'
        primary key,
    name     varchar(50)  not null comment '用户昵称',
    password varchar(255) not null comment '用户密码',
    email    varchar(50)  not null comment '用户邮箱',
    phone    varchar(11)  not null comment '联系电话',
    company  varchar(100) null comment '公司',
    contact  varchar(50)  null comment '联系人姓名'
)
    comment '用户表';

create table user_favorite_car
(
    id      bigint unsigned auto_increment comment '主键ID'
        primary key comment '主键',
    user_id bigint unsigned not null comment '用户ID',
    tirm_id bigint unsigned not null comment '车型ID'
)
    comment '用户收藏车系表';

