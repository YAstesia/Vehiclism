package com.seu.vehiclism.CarSeries.service;

import com.seu.vehiclism.CarSeries.entity.CarSeriesSaleMonth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author QTM
 * @since 2024-09-05
 */
public interface ICarSeriesSaleMonthService extends IService<CarSeriesSaleMonth> {

    List<CarSeriesSaleMonth> getSeriesSaleMonth(Integer year, Integer month);
}
