package com.seu.vehiclism.Search.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.vehiclism.Search.SaleInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SaleInfoMapper extends BaseMapper<SaleInfo> {

    @Select("SELECT cs.id AS series_id, cs.brand, cs.type, cs.series, cs.price_min, cs.price_max, "
            + "cst.year, cst.month, cst.total_sale "
            + "FROM car_series cs "
            + "JOIN car_sales cst ON cs.id = cst.series_id "
            + "WHERE cs.id = #{seriesId}")
    @Results({
            @Result(property = "id", column = "series_id"),
            @Result(property = "brand", column = "brand"),
            @Result(property = "type", column = "type"),
            @Result(property = "series", column = "series"),
            @Result(property = "priceMin", column = "price_min"),
            @Result(property = "priceMax", column = "price_max"),
            @Result(property = "year", column = "year"),
            @Result(property = "month", column = "month"),
            @Result(property = "totalSale", column = "total_sale")
    })
    List<SaleInfo> selectSaleInfos(@Param("seriesId") Long seriesIds);
    @Select("SELECT cs.id AS series_id, cs.brand, cs.type, cs.series, cs.price, "
            + "cst.year, cst.month, cst.total_sale "
            + "FROM car_series cs "
            + "JOIN car_sales cst ON cs.id = cst.series_id ")
    @Results({
            @Result(property = "id", column = "series_id"),
            @Result(property = "brand", column = "brand"),
            @Result(property = "type", column = "type"),
            @Result(property = "series", column = "series"),
            @Result(property = "price", column = "price"),
            @Result(property = "year", column = "year"),
            @Result(property = "month", column = "month"),
            @Result(property = "totalSale", column = "total_sale")
    })
    List<SaleInfo> selectAll();
}
