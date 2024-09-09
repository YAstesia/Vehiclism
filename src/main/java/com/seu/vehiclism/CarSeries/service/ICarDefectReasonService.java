package com.seu.vehiclism.CarSeries.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.vehiclism.CarSeries.entity.CarDefectReason;

import java.util.List;

public interface ICarDefectReasonService extends IService<CarDefectReason> {

    List<CarDefectReason> getDefectReasonsBySeriesId(String seriesId);
}
