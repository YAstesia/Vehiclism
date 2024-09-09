package com.seu.vehiclism.Search.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.vehiclism.Search.SaleInfo;

import java.util.Date;
import java.util.List;

public interface SaleInfoService extends IService<SaleInfo> {

    IPage<SaleInfo> filterSales(Integer pageNum, Integer pageSize, List<String> brands, List<String> types,
                                List<String> series, Integer priceMin, Integer priceMax, Date timeStart, Date timeEnd);

    List<SaleInfo> AllSales();

    IPage<SaleInfo> getKeywordSaleByPage(Integer pageNum, Integer pageSize, String key);
}