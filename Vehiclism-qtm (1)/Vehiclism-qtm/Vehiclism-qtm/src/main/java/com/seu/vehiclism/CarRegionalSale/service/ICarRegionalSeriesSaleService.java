package com.seu.vehiclism.CarRegionalSale.service;

import com.seu.vehiclism.CarRegionalSale.entity.CarRegionalSeriesSale;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author QTM
 * @since 2024-09-04
 */
public interface ICarRegionalSeriesSaleService extends IService<CarRegionalSeriesSale> {

    List<CarRegionalSeriesSale> getCityTopTen(String city);

    List<CarRegionalSeriesSale> getProvinceTopTen(String province);

    List<CarRegionalSeriesSale> getAllTopTen();
}
