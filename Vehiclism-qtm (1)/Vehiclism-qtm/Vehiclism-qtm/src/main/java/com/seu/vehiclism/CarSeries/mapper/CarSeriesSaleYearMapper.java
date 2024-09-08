package com.seu.vehiclism.CarSeries.mapper;

import com.seu.vehiclism.CarSeries.entity.CarSeriesSaleYear;
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
public interface CarSeriesSaleYearMapper extends BaseMapper<CarSeriesSaleYear> {
    @Select("SELECT id, year, ranking, series, total_sale "
            + "FROM car_series_sale_year "
            + "WHERE year = #{year} "
            + "ORDER BY ranking ASC")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "year", column = "year"),
            @Result(property = "ranking", column = "ranking"),
            @Result(property = "series", column = "series"),
            @Result(property = "totalSale", column = "total_sale")
    })
    List<CarSeriesSaleYear> getSeriesSaleYear(Integer year);
}
