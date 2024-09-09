package com.seu.vehiclism.JoinSeriesTirm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 车系-车型关联表
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JoinSeriesTirm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 车系ID
     */
    private Long seriesId;

    /**
     * 车型ID
     */
    private Long tirmId;


}
