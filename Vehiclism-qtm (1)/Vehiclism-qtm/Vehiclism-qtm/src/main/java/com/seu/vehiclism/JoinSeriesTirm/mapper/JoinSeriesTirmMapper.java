package com.seu.vehiclism.JoinSeriesTirm.mapper;

import com.seu.vehiclism.JoinSeriesTirm.entity.JoinSeriesTirm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 车系-车型关联表 Mapper 接口
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
public interface JoinSeriesTirmMapper extends BaseMapper<JoinSeriesTirm> {

    @Select("SELECT tirm_id "
    +"FROM join_series_tirm "
    +"WHERE series_id =#{id}")
    List<Long> getTirms(Long id);
}
