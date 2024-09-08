package com.seu.vehiclism.AllCarSale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.AllCarSale.controller.YearCarSale;
import com.seu.vehiclism.AllCarSale.entity.AllCarSale;
import com.seu.vehiclism.AllCarSale.mapper.AllCarSaleMapper;
import com.seu.vehiclism.AllCarSale.service.IAllCarSaleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-09-02
 */
@Service
public class AllCarSaleServiceImpl extends ServiceImpl<AllCarSaleMapper, AllCarSale> implements IAllCarSaleService {
    @Autowired
    private AllCarSaleMapper allCarSaleMapper;
    @Override
    public List<YearCarSale> getYearlySales() {
        List<YearCarSale> yearlySales = new ArrayList<>();
        for (int year = 2015; year <= 2024; year++){
            QueryWrapper<AllCarSale>queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("year",year);
            List<AllCarSale>monthlySales=allCarSaleMapper.selectList(queryWrapper);
            int totalSaleForYear = monthlySales.stream()
                    .mapToInt(AllCarSale::getTotalSale)
                    .sum();
            YearCarSale yearCarSale = new YearCarSale();
            yearCarSale.setYear(year);
            yearCarSale.setTotalSale(totalSaleForYear);
            yearlySales.add(yearCarSale);
        }
        return yearlySales;
    }
}
