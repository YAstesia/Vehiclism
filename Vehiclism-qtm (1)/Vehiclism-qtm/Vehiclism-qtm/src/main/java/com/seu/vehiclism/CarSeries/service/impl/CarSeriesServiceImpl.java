package com.seu.vehiclism.CarSeries.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seu.vehiclism.CarSeries.entity.CarSeries;
import com.seu.vehiclism.CarSeries.mapper.CarSeriesMapper;
import com.seu.vehiclism.CarSeries.service.ICarSeriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 车系表 服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
@Service
public class CarSeriesServiceImpl extends ServiceImpl<CarSeriesMapper, CarSeries> implements ICarSeriesService {
    private final CarSeriesMapper carSeriesMapper;
    public CarSeriesServiceImpl(CarSeriesMapper carSeriesMapper) {
        this.carSeriesMapper = carSeriesMapper;
    }
    @Override
    public List<CarSeries> getAllSeries() {
        return carSeriesMapper.selectList(null);
    }

    @Override
    public IPage<CarSeries> getSeriesByPage(Integer pageNum, Integer pageSize) {
        Page<CarSeries> page = new Page<>(pageNum, pageSize);
        carSeriesMapper.selectPage(page, null);
        return page;
    }

    @Override
    public Long getSeriesIdByName(String seriesName) {
        QueryWrapper<CarSeries> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("series", seriesName);
        CarSeries series = carSeriesMapper.selectOne(queryWrapper);
        return series != null ? series.getId() : null;
    }

}
