package com.seu.vehiclism.CarSeries.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarDefectReason implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 车系ID
     */
    private String seriesId;

    /**
     * 故障原因
     */
    private String reason;

    /**
     * 故障数量
     */
    private Integer count;

    /**
     * 记录时间
     */
    private Date time;

}
