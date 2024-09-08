package com.seu.vehiclism.AllCarSale.service;

import com.seu.vehiclism.AllCarSale.controller.YearCarSale;
import com.seu.vehiclism.AllCarSale.entity.AllCarSale;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author QTM
 * @since 2024-09-02
 */
public interface IAllCarSaleService extends IService<AllCarSale> {

    List<YearCarSale> getYearlySales();
}
