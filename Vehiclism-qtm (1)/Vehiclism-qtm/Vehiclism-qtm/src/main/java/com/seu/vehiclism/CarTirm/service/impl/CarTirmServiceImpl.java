package com.seu.vehiclism.CarTirm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seu.vehiclism.CarBrand.mapper.CarBrandMapper;
import com.seu.vehiclism.CarSeries.mapper.CarSeriesMapper;
import com.seu.vehiclism.CarTirm.entity.CarTirm;
import com.seu.vehiclism.CarTirm.mapper.CarTirmMapper;
import com.seu.vehiclism.CarTirm.service.ICarTirmService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车型表 服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
@Service
public class CarTirmServiceImpl extends ServiceImpl<CarTirmMapper, CarTirm> implements ICarTirmService {
    private final CarTirmMapper carTirmMapper;
    private final CarBrandMapper carBrandMapper;
    private final CarSeriesMapper carSeriesMapper;

    public CarTirmServiceImpl(CarTirmMapper carTirmMapper, CarBrandMapper carBrandMapper, CarSeriesMapper carSeriesMapper) {
        this.carTirmMapper = carTirmMapper;
        this.carBrandMapper = carBrandMapper;
        this.carSeriesMapper = carSeriesMapper;
    }

    @Override
    public List<CarTirm> getAllTirms() {
        return carTirmMapper.selectList(null);
    }
    @Override
    public IPage<CarTirm> getTirmsByPage(Integer pageNum, Integer pageSize) {
        Page<CarTirm> page = new Page<>(pageNum, pageSize);
        carTirmMapper.selectPage(page, null);
        return page;
    }

    @Override
    public IPage<CarTirm> getFilteredTirmsByPage(Integer pageNum, Integer pageSize, List<String> brands, List<String> types, List<String> series, List<String> tirms, List<String> energyTypes, Integer priceMin, Integer priceMax) {
        // 创建分页对象
        IPage<CarTirm> page = new Page<>(pageNum, pageSize);
        // 构建查询条件
        QueryWrapper<CarTirm> queryWrapper = new QueryWrapper<>();
        if (brands != null && !brands.isEmpty()) {
            queryWrapper.in("brand",brands);
        }
        if (types != null && !types.isEmpty()) {
            queryWrapper.in("type",types);
        }
        if (series != null && !series.isEmpty()) {
            queryWrapper.in("series",series);
        }
        if (tirms != null && !tirms.isEmpty()) {
            queryWrapper.in("tirm", tirms);
        }
        if (energyTypes != null && !energyTypes.isEmpty()) {
            queryWrapper.in("energy_type", energyTypes);
        }
        if (priceMin != null) {
            queryWrapper.ge("price", priceMin);
        }
        if (priceMax != null) {
            queryWrapper.le("price", priceMax);
        }
        // 执行分页查询
        carTirmMapper.selectPage(page, queryWrapper);
        return page;
    }

    @Override
    public IPage<CarTirm> getKeywordTirmsByPage(Integer pageNum, Integer pageSize, String key) {
        // 创建分页对象
        IPage<CarTirm> page = new Page<>(pageNum, pageSize);
        // 构建查询条件
        QueryWrapper<CarTirm>queryWrapper=new QueryWrapper<>();
        if(key!=null&&!key.isEmpty()){
            queryWrapper.like("brand",key)
                    .or()
                    .like("type",key)
                    .or()
                    .like("series",key)
                    .or()
                    .like("tirm",key)
                    .or()
                    .like("energy_type",key);
        }
        // 添加排序规则：先按品牌，然后按车系，最后按车型排序
        queryWrapper.orderBy(true, true, "brand")
                .orderBy(true, true, "series")
                .orderBy(true, true, "tirm");
        carTirmMapper.selectPage(page,queryWrapper);
        return page;
    }

    @Override
    public IPage<CarTirm> getCarTrimsBySeriesIdsByPage(List<Long> seriesIds,Page<CarTirm> page) {
        // 如果 seriesIds 为空或为空列表，直接返回空列表
        if (seriesIds == null || seriesIds.isEmpty()) {
            return page.setRecords(Collections.emptyList());
        }
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<CarTirm> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("series", seriesIds);
        queryWrapper.orderBy(true, true, "brand")
                .orderBy(true, true, "series")
                .orderBy(true, true, "tirm");
        // 执行查询
        return carTirmMapper.selectPage(page,queryWrapper);
    }

    @Override
    public List<CarTirm> getFilteredTirms(List<String> brands, List<String> types, List<String> series, List<String> tirms, List<String> energyTypes, Integer priceMin, Integer priceMax) {
        // 构建查询条件
        QueryWrapper<CarTirm> queryWrapper = new QueryWrapper<>();
        if (brands != null && !brands.isEmpty()) {
            queryWrapper.in("brand",brands);
        }
        if (types != null && !types.isEmpty()) {
            queryWrapper.in("type",types);
        }
        if (series != null && !series.isEmpty()) {
            queryWrapper.in("series",series);
        }
        if (tirms != null && !tirms.isEmpty()) {
            queryWrapper.in("tirm", tirms);
        }
        if (energyTypes != null && !energyTypes.isEmpty()) {
            queryWrapper.in("energy_type", energyTypes);
        }
        if (priceMin != null) {
            queryWrapper.ge("price", priceMin);
        }
        if (priceMax != null) {
            queryWrapper.le("price", priceMax);
        }
        queryWrapper.orderBy(true, true, "brand")
                .orderBy(true, true, "series")
                .orderBy(true, true, "tirm");
        // 执行分页查询
        return carTirmMapper.selectList(queryWrapper);
    }

    @Override
    public List<CarTirm> getCarTirmsById(List<Long> tirmIds) {
        QueryWrapper<CarTirm>queryWrapper=new QueryWrapper<>();
        queryWrapper.in("id",tirmIds);
        return carTirmMapper.selectList(queryWrapper);
    }

    @Override
    public List<String> getDistinctEnergyTypes() {
        List<String> carEnergyType=carTirmMapper.getDistinctEnergyTypes();
        return carEnergyType;
    }

    @Override
    public List<CarTirm> getCarTrimsByIds(List<Long> Ids) {
        // 如果 seriesIds 为空或为空列表，直接返回空列表
        if (Ids == null || Ids.isEmpty()) {
            return Collections.emptyList();
        }
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<CarTirm> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", Ids).orderByAsc("price");
        // 执行查询
        return carTirmMapper.selectList(queryWrapper);
    }
}
