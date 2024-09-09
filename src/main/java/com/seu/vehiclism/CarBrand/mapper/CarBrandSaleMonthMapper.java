package com.seu.vehiclism.CarBrand.mapper;

import com.seu.vehiclism.CarBrand.entity.CarBrandSaleMonth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author QTM
 * @since 2024-09-05
 */
public interface CarBrandSaleMonthMapper extends BaseMapper<CarBrandSaleMonth> {
    @Select("SELECT id, year, month, ranking, brand, total_sale "
            + "FROM car_brand_sale_month "
            + "WHERE year = #{year} AND month = #{month} "
            + "ORDER BY ranking ASC")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "year", column = "year"),
            @Result(property = "month", column = "month"),
            @Result(property = "ranking", column = "ranking"),
            @Result(property = "brand", column = "brand"),
            @Result(property = "totalSale", column = "total_sale")
    })
    List<CarBrandSaleMonth> getBrandSaleMonth(Integer year, Integer month);
}
