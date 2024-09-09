package com.seu.vehiclism.CarSeries.service.impl;

import com.seu.vehiclism.CarSeries.entity.CarSeriesSaleYear;
import com.seu.vehiclism.CarSeries.mapper.CarSeriesSaleYearMapper;
import com.seu.vehiclism.CarSeries.service.ICarSeriesSaleYearService;
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
public class CarSeriesSaleYearServiceImpl extends ServiceImpl<CarSeriesSaleYearMapper, CarSeriesSaleYear> implements ICarSeriesSaleYearService {

    @Autowired
    private CarSeriesSaleYearMapper carSeriesSaleYearMapper;
    @Override
    public List<CarSeriesSaleYear> getSeriesSaleYear(Integer year) {

        return carSeriesSaleYearMapper.getSeriesSaleYear(year);
    }
}
