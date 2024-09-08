package com.seu.vehiclism.CarBrand.mapper;

import com.seu.vehiclism.CarBrand.entity.CarBrandSaleMonth;
import com.seu.vehiclism.CarBrand.entity.CarBrandSaleYear;
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
public interface CarBrandSaleYearMapper extends BaseMapper<CarBrandSaleYear> {
    @Select("SELECT id, year, ranking, brand, total_sale "
            + "FROM car_brand_sale_year "
            + "WHERE year = #{year} "
            + "ORDER BY ranking ASC")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "year", column = "year"),
            @Result(property = "ranking", column = "ranking"),
            @Result(property = "brand", column = "brand"),
            @Result(property = "totalSale", column = "total_sale")
    })
    List<CarBrandSaleYear> getBrandSaleYear(Integer year);
}
