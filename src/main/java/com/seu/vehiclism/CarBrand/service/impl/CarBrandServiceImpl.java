package com.seu.vehiclism.CarBrand.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seu.vehiclism.CarBrand.entity.CarBrand;
import com.seu.vehiclism.CarBrand.mapper.CarBrandMapper;
import com.seu.vehiclism.CarBrand.service.ICarBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
@Service
public class CarBrandServiceImpl extends ServiceImpl<CarBrandMapper, CarBrand> implements ICarBrandService {
    private final CarBrandMapper carBrandMapper;
    public CarBrandServiceImpl(CarBrandMapper carBrandMapper) {
        this.carBrandMapper = carBrandMapper;
    }
    @Override
    public List<CarBrand> getAllBrands() {
        return carBrandMapper.selectList(null);
    }

    @Override
    public IPage<CarBrand> getBrandsByPage(Integer pageNum, Integer pageSize) {
        Page<CarBrand> page = new Page<>(pageNum, pageSize);
        carBrandMapper.selectPage(page, null);
        return page;
    }
}
