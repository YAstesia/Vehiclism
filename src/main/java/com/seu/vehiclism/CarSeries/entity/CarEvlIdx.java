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
 * @since 2024-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarEvlIdx implements Serializable {

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
     * 综合评分
     */
    private BigDecimal overallRating;

    /**
     * 空间评分
     */
    private BigDecimal space;

    /**
     * 驾驶感受评分
     */
    private BigDecimal driveFeel;

    /**
     * 能耗评分
     */
    private BigDecimal powerConsum;

    /**
     * 外观评分
     */
    private BigDecimal outDecor;

    /**
     * 内饰评分
     */
    private BigDecimal inDecor;

    /**
     * 性价比评分
     */
    private BigDecimal qpRatio;

    /**
     * 配置评分
     */
    private BigDecimal configure;

    /**
     * 记录时间
     */
    private LocalDate time;


}
