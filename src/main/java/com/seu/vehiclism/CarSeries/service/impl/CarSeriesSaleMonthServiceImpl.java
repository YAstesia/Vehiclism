package com.seu.vehiclism.CarSeries.service.impl;

import com.seu.vehiclism.CarSeries.entity.CarSeriesSaleMonth;
import com.seu.vehiclism.CarSeries.mapper.CarSeriesSaleMonthMapper;
import com.seu.vehiclism.CarSeries.service.ICarSeriesSaleMonthService;
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
public class CarSeriesSaleMonthServiceImpl extends ServiceImpl<CarSeriesSaleMonthMapper, CarSeriesSaleMonth> implements ICarSeriesSaleMonthService {
@Autowired
    private CarSeriesSaleMonthMapper carSeriesSaleMonthMapper;
    @Override
    public List<CarSeriesSaleMonth> getSeriesSaleMonth(Integer year, Integer month) {
        return carSeriesSaleMonthMapper.getSeriesSaleMonth(year,month);
    }
}
