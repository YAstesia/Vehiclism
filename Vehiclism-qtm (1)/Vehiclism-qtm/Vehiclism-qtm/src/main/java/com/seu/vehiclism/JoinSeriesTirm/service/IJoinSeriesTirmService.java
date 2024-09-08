package com.seu.vehiclism.JoinSeriesTirm.service;

import com.seu.vehiclism.JoinSeriesTirm.entity.JoinSeriesTirm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 车系-车型关联表 服务类
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
public interface IJoinSeriesTirmService extends IService<JoinSeriesTirm> {

    List<Long> getTirms(Long id);
}
