package com.seu.vehiclism.CarRegionalSale.service;

import com.seu.vehiclism.CarRegionalSale.ProvinceSale;
import com.seu.vehiclism.CarRegionalSale.entity.CarRegionalSale;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.vehiclism.Response;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author QTM
 * @since 2024-09-03
 */
public interface ICarRegionalSaleService extends IService<CarRegionalSale> {

    List<CarRegionalSale> getTopTenCity();

    List<ProvinceSale> getTopTenProvince();

    List<ProvinceSale> getProvince();

    List<CarRegionalSale> getProvinceCity(String province);

    List<String> getCitiesFromProvince(String province);

    List<String> getCities();
}
