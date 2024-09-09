package com.seu.vehiclism.CarTirm.service;

import com.seu.vehiclism.CarTirm.entity.CarTirmConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author QTM
 * @since 2024-09-06
 */
public interface ICarTirmConfigService extends IService<CarTirmConfig> {

    List<CarTirmConfig> getConfigByTirmId(Long id);
}
