package com.seu.vehiclism.CarTirm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seu.vehiclism.CarTirm.entity.CarTirm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车型表 服务类
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
public interface ICarTirmService extends IService<CarTirm> {

    List<CarTirm> getAllTirms();
    IPage<CarTirm> getTirmsByPage(Integer pageNum, Integer pageSize);

    IPage<CarTirm> getFilteredTirmsByPage(Integer pageNum, Integer pageSize, List<String> brands, List<String> types, List<String> series, List<String> tirms, List<String> energyTypes, Integer priceMin, Integer priceMax);

    IPage<CarTirm> getKeywordTirmsByPage(Integer pageNum, Integer pageSize, String key);

    List<CarTirm> getCarTrimsByIds(List<Long> Ids);
    IPage<CarTirm> getCarTrimsBySeriesIdsByPage(List<Long> seriesIds, Page<CarTirm> page);

    List<CarTirm> getFilteredTirms(List<String> brands, List<String> types, List<String> series, List<String> tirms, List<String> energyTypes, Integer priceMin, Integer priceMax);

    List<CarTirm> getCarTirmsById(List<Long> tirmIds);
    List<String> getDistinctEnergyTypes();
}
