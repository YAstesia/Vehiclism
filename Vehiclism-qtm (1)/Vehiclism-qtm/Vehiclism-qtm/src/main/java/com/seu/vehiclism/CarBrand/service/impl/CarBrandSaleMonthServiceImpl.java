package com.seu.vehiclism.CarBrand.service.impl;

import com.seu.vehiclism.CarBrand.entity.CarBrandSaleMonth;
import com.seu.vehiclism.CarBrand.mapper.CarBrandSaleMonthMapper;
import com.seu.vehiclism.CarBrand.service.ICarBrandSaleMonthService;
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
public class CarBrandSaleMonthServiceImpl extends ServiceImpl<CarBrandSaleMonthMapper, CarBrandSaleMonth> implements ICarBrandSaleMonthService {

    @Autowired
    private CarBrandSaleMonthMapper carBrandSaleMonthMapper;
    @Override
    public List<CarBrandSaleMonth> getBrandSaleMonth(Integer year, Integer month) {
        return carBrandSaleMonthMapper.getBrandSaleMonth(year,month);
    }
}
