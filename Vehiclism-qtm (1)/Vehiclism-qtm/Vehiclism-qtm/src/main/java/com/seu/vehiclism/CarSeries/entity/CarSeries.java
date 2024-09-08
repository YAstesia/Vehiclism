package com.seu.vehiclism.CarSeries.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 车系表
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarSeries implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车系ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 品牌名
     */
    private String brand;

    /**
     * 汽车类型
     */
    private String type;

    /**
     * 车系
     */
    private String series;

    /**
     * 参考价格_最低价
     */
    private BigDecimal priceMin;

    /**
     * 参考价格_最高价
     */
    private BigDecimal priceMax;

}
