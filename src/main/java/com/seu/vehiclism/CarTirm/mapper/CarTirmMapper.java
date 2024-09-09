package com.seu.vehiclism.CarTirm.mapper;

import com.seu.vehiclism.CarTirm.entity.CarTirm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车型表 Mapper 接口
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
public interface CarTirmMapper extends BaseMapper<CarTirm> {
    @Select("SELECT DISTINCT energy_type FROM car_tirm")
    List<String> getDistinctEnergyTypes();
}
