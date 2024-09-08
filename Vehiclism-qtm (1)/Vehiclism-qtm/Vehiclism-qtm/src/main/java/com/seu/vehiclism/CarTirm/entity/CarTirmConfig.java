package com.seu.vehiclism.CarTirm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author QTM
 * @since 2024-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarTirmConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置参数ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 车型ID
     */
    private Long tirmId;

    /**
     * 配置类型
     */
    private String configType;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置参数
     */
    private String configValue;


}
