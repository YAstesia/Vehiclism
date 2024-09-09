package com.seu.vehiclism.CarTirm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.CarTirm.entity.CarTirmConfig;
import com.seu.vehiclism.CarTirm.mapper.CarTirmConfigMapper;
import com.seu.vehiclism.CarTirm.service.ICarTirmConfigService;
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
 * @since 2024-09-06
 */
@Service
public class CarTirmConfigServiceImpl extends ServiceImpl<CarTirmConfigMapper, CarTirmConfig> implements ICarTirmConfigService {

    @Autowired
    private final CarTirmConfigMapper carTirmConfigMapper;

    public CarTirmConfigServiceImpl(CarTirmConfigMapper carTirmConfigMapper) {
        this.carTirmConfigMapper = carTirmConfigMapper;
    }

    @Override
    public List<CarTirmConfig> getConfigByTirmId(Long id) {
        QueryWrapper<CarTirmConfig>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("tirm_id",id);
        return carTirmConfigMapper.selectList(queryWrapper);
    }
}
