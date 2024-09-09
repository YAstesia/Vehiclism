package com.seu.vehiclism.JoinSeriesTirm.service.impl;

import com.seu.vehiclism.JoinSeriesTirm.entity.JoinSeriesTirm;
import com.seu.vehiclism.JoinSeriesTirm.mapper.JoinSeriesTirmMapper;
import com.seu.vehiclism.JoinSeriesTirm.service.IJoinSeriesTirmService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 车系-车型关联表 服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
@Service
public class JoinSeriesTirmServiceImpl extends ServiceImpl<JoinSeriesTirmMapper, JoinSeriesTirm> implements IJoinSeriesTirmService {

    @Autowired
    private JoinSeriesTirmMapper joinSeriesTirmMapper;
    @Override
    public List<Long> getTirms(Long id) {
        return joinSeriesTirmMapper.getTirms(id);
    }
}
