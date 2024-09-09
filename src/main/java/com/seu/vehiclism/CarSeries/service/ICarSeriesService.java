package com.seu.vehiclism.CarSeries.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seu.vehiclism.CarSeries.entity.CarSeries;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 车系表 服务类
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
public interface ICarSeriesService extends IService<CarSeries> {

    List<CarSeries> getAllSeries();

    IPage<CarSeries> getSeriesByPage(Integer pageNum, Integer pageSize);

    Long getSeriesIdByName(String seriesName);
}
