package com.seu.vehiclism.CarBrand.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seu.vehiclism.CarBrand.entity.CarBrand;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
public interface ICarBrandService extends IService<CarBrand> {

    List<CarBrand> getAllBrands();

    IPage<CarBrand> getBrandsByPage(Integer pageNum, Integer pageSize);
}
