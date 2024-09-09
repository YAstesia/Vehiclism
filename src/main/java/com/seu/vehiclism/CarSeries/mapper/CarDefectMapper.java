package com.seu.vehiclism.CarSeries.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.vehiclism.CarSeries.entity.CarDefect;
import com.seu.vehiclism.CarSeries.entity.CarEvlIdx;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YC
 * @since 2024-09-08
 */
public interface CarDefectMapper extends BaseMapper<CarDefect> {
    @Select("SELECT * FROM car_defect WHERE series_id = #{seriesId}")
    CarDefect getDefectBySeriesId(@Param("seriesId") Long seriesId);
}
