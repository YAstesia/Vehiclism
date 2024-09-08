package com.seu.vehiclism.CarSeries.mapper;

import com.seu.vehiclism.CarSeries.entity.CarSeriesSaleMonth;
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
public interface CarSeriesSaleMonthMapper extends BaseMapper<CarSeriesSaleMonth> {
    @Select("SELECT id, year, month, ranking, series, total_sale "
            + "FROM car_series_sale_month "
            + "WHERE year = #{year} AND month = #{month} "
            + "ORDER BY ranking ASC")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "year", column = "year"),
            @Result(property = "month", column = "month"),
            @Result(property = "ranking", column = "ranking"),
            @Result(property = "series", column = "series"),
            @Result(property = "totalSale", column = "total_sale")
    })
    List<CarSeriesSaleMonth> getSeriesSaleMonth(Integer year, Integer month);
}
