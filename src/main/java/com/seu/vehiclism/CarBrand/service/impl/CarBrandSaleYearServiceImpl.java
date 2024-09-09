package com.seu.vehiclism.CarBrand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.CarBrand.entity.CarBrandSaleMonth;
import com.seu.vehiclism.CarBrand.entity.CarBrandSaleYear;
import com.seu.vehiclism.CarBrand.mapper.CarBrandSaleYearMapper;
import com.seu.vehiclism.CarBrand.service.ICarBrandSaleYearService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-09-05
 */
@Service
public class CarBrandSaleYearServiceImpl extends ServiceImpl<CarBrandSaleYearMapper, CarBrandSaleYear> implements ICarBrandSaleYearService {

    @Autowired
    private CarBrandSaleYearMapper carBrandSaleYearMapper;
    @Override
    public List<CarBrandSaleYear> getBrandSaleYear(Integer year) {
        return carBrandSaleYearMapper.getBrandSaleYear(year);
    }
}
