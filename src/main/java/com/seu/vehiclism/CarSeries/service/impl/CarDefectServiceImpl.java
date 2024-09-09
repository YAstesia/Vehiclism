package com.seu.vehiclism.CarSeries.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.vehiclism.CarSeries.entity.CarDefect;
import com.seu.vehiclism.CarSeries.mapper.CarDefectMapper;
import com.seu.vehiclism.CarSeries.service.ICarDefectService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YC
 * @since 2024-09-08
 */
@Service
public class CarDefectServiceImpl extends ServiceImpl<CarDefectMapper, CarDefect> implements ICarDefectService {
    private CarDefectMapper carDefectMapper;

    public void CarDefectService(CarDefectMapper carDefectMapper) {
        this.carDefectMapper = carDefectMapper;
    }

    public CarDefect getDefectBySeriesId(Long seriesId) {
        return carDefectMapper.getDefectBySeriesId(seriesId);
    }

}

