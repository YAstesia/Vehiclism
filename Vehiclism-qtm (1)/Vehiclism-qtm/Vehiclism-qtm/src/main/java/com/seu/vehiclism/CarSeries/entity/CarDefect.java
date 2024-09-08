package com.seu.vehiclism.CarSeries.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarDefect implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 车系ID
     */
    private Integer seriesId;

    /**
     * 百车故障数
     */
    private Integer count;

    /**
     * 车身外观
     */
    private Integer exterior;

    /**
     * 行驶过程
     */
    private Integer driving;

    /**
     * 功能操作
     */
    private Integer control;

    /**
     * 电子设备
     */
    private Integer electronic;

    /**
     * 座椅
     */
    private Integer seat;

    /**
     * 空调系统
     */
    private Integer acSystem;

    /**
     * 内饰
     */
    private Integer interior;

    /**
     * 动力系统
     */
    private Integer powerTrain;

    /**
     * 变速系统
     */
    private Integer transmission;

    /**
     * 记录时间
     */
    private Date recordTime;

}
