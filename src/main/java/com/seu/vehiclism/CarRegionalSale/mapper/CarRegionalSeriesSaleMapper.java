package com.seu.vehiclism.CarRegionalSale.mapper;

import com.seu.vehiclism.CarRegionalSale.entity.CarRegionalSeriesSale;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.IntegerTypeHandler;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author QTM
 * @since 2024-09-04
 */
public interface CarRegionalSeriesSaleMapper extends BaseMapper<CarRegionalSeriesSale> {
    @Select("SELECT series, SUM(total_sale) AS total_sale "
            + "FROM car_regional_series_sale "
            + "WHERE province = #{province} "
            + "GROUP BY series "
            + "ORDER BY total_sale DESC "
            + "LIMIT 10")
    @Results({
            @Result(property = "series", column = "series"),
            @Result(property = "totalSale", column = "total_sale", typeHandler = IntegerTypeHandler.class)
    })
    List<CarRegionalSeriesSale> getProvinceTopTenSeries(@Param("province") String province);
    @Select("SELECT series, SUM(total_sale) AS total_sale "
            + "FROM car_regional_series_sale "
            + "GROUP BY series "
            + "ORDER BY total_sale DESC "
            + "LIMIT 10")
    @Results({
            @Result(property = "series", column = "series"),
            @Result(property = "totalSale", column = "total_sale", typeHandler = IntegerTypeHandler.class)
    })
    List<CarRegionalSeriesSale> getTopTenSeries();
}
