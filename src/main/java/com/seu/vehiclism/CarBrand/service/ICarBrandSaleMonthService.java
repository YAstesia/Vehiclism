package com.seu.vehiclism.CarBrand.service;

import com.seu.vehiclism.CarBrand.entity.CarBrandSaleMonth;
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
public interface ICarBrandSaleMonthService extends IService<CarBrandSaleMonth> {

    List<CarBrandSaleMonth> getBrandSaleMonth(Integer year, Integer month);
}
