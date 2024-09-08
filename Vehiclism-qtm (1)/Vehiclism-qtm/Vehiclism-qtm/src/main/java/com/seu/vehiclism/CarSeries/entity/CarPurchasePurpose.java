package com.seu.vehiclism.CarSeries.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
 * @since 2024-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarPurchasePurpose implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 车系ID
     */
    private String seriesId;

    /**
     * 购车目的
     */
    private String purpose;

    /**
     * 数量
     */
    private Integer cnt;

    /**
     * 占比
     */
    private BigDecimal percentage;

    /**
     * 记录时间
     */
    private LocalDate time;


}
