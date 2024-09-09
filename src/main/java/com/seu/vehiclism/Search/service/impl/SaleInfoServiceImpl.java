package com.seu.vehiclism.Search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.vehiclism.CarSeries.entity.CarSeries;
import com.seu.vehiclism.CarSeries.mapper.CarSeriesMapper;
import com.seu.vehiclism.Search.SaleInfo;
import com.seu.vehiclism.Search.mapper.SaleInfoMapper;
import com.seu.vehiclism.Search.service.SaleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleInfoServiceImpl extends ServiceImpl<SaleInfoMapper, SaleInfo> implements SaleInfoService {

    @Autowired
    private CarSeriesMapper carSeriesMapper;

    @Autowired
    private SaleInfoMapper saleInfoMapper;

    @Override
    public IPage<SaleInfo> filterSales(Integer pageNum, Integer pageSize, List<String> brands, List<String> types,
                                       List<String> series, Integer priceMin, Integer priceMax, Date timeStart, Date timeEnd) {
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("参数缺失");
        }

        // 创建分页对象
        IPage<SaleInfo> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<CarSeries> queryWrapper = new QueryWrapper<>();

        if (brands != null && !brands.isEmpty()) {
            queryWrapper.in("brand", brands);
        }
        if (types != null && !types.isEmpty()) {
            queryWrapper.in("type", types);
        }
        if (series != null && !series.isEmpty()) {
            queryWrapper.in("series", series);
        }
        if (priceMin != null) {
            queryWrapper.ge("price_min", priceMin);
        }
        if (priceMax != null) {
            queryWrapper.le("price_max", priceMax);
        }

        // 获取符合条件的车系ID列表
        List<CarSeries> carSeriesList = carSeriesMapper.selectList(queryWrapper);
        List<Long> seriesIds = carSeriesList.stream().map(CarSeries::getId).collect(Collectors.toList());

//        System.out.println(seriesIds);
        // 查询销售信息
        List<SaleInfo> saleInfos=new ArrayList<>();
        for(int i=0;i<seriesIds.size();++i){
            List<SaleInfo>saleInfoList=saleInfoMapper.selectSaleInfos(seriesIds.get(i));
            saleInfos.addAll(saleInfoList);
        }
//        List<SaleInfo> saleInfos = saleInfoMapper.selectSaleInfos(seriesIds);

        // 过滤时间范围
        List<SaleInfo> filteredSaleInfos = new ArrayList<>();
        for (SaleInfo saleInfo : saleInfos) {
            if (timeStart == null || timeEnd == null ||
                    (saleInfo.getYear() >= timeStart.getYear() + 1900 && saleInfo.getMonth() >= timeStart.getMonth() + 1) &&
                            (saleInfo.getYear() <= timeEnd.getYear() + 1900 && saleInfo.getMonth() <= timeEnd.getMonth() + 1)) {
                filteredSaleInfos.add(saleInfo);
            }
        }

// 计算总记录数
        int total = filteredSaleInfos.size();

        // 分页处理
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        List<SaleInfo> paginatedRecords = filteredSaleInfos.subList(start, end);

        // 设置分页数据
        page.setRecords(paginatedRecords);
        page.setTotal(total);

        return page;
    }
    @Override
    public IPage<SaleInfo> getKeywordSaleByPage(Integer pageNum, Integer pageSize, String key) {
        if (pageNum == null || pageSize == null) {
            throw new IllegalArgumentException("参数缺失");
        }

        // 创建分页对象
        IPage<SaleInfo> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<CarSeries> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("brand",key).or().like("series",key).or().like("type",key).orderByAsc("brand");

        // 获取符合条件的车系ID列表
        List<CarSeries> carSeriesList = carSeriesMapper.selectList(queryWrapper);
        List<Long> seriesIds = carSeriesList.stream().map(CarSeries::getId).collect(Collectors.toList());

//        System.out.println(seriesIds);
        // 查询销售信息
        List<SaleInfo> saleInfos=new ArrayList<>();
        for(int i=0;i<seriesIds.size();++i){
            List<SaleInfo>saleInfoList=saleInfoMapper.selectSaleInfos(seriesIds.get(i));
            saleInfos.addAll(saleInfoList);
        }

// 计算总记录数
        int total = saleInfos.size();

        // 分页处理
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        List<SaleInfo> paginatedRecords = saleInfos.subList(start, end);

        // 设置分页数据
        page.setRecords(paginatedRecords);
        page.setTotal(total);

        return page;
    }
    @Override
    public List<SaleInfo> AllSales() {
        List<SaleInfo>saleInfos = saleInfoMapper.selectAll();
        return saleInfos;
    }


}
