package com.seu.vehiclism.CarSeries.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.vehiclism.CarSeries.entity.CarDefectReason;
import com.seu.vehiclism.CarSeries.mapper.CarDefectReasonMapper;
import com.seu.vehiclism.CarSeries.service.ICarDefectReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarDefectReasonServiceImpl extends ServiceImpl<CarDefectReasonMapper, CarDefectReason> implements ICarDefectReasonService {

    @Autowired
    private CarDefectReasonMapper carDefectReasonMapper;

    @Override
    public List<CarDefectReason> getDefectReasonsBySeriesId(String seriesId) {
        QueryWrapper<CarDefectReason> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("series_id", seriesId);
        return carDefectReasonMapper.selectList(queryWrapper);
    }
}
