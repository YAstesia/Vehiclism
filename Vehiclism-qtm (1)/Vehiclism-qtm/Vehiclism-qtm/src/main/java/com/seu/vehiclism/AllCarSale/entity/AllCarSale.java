package com.seu.vehiclism.AllCarSale.entity;

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
 * @since 2024-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AllCarSale implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer year;

    private Integer month;

    private Integer totalSale;


}
