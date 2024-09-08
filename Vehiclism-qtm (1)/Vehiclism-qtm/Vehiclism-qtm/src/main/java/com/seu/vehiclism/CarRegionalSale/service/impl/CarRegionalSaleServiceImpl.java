package com.seu.vehiclism.CarRegionalSale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.CarRegionalSale.ProvinceSale;
import com.seu.vehiclism.CarRegionalSale.entity.CarRegionalSale;
import com.seu.vehiclism.CarRegionalSale.mapper.CarRegionalSaleMapper;
import com.seu.vehiclism.CarRegionalSale.service.ICarRegionalSaleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-09-03
 */
@Service
public class CarRegionalSaleServiceImpl extends ServiceImpl<CarRegionalSaleMapper, CarRegionalSale> implements ICarRegionalSaleService {

    @Autowired
    private CarRegionalSaleMapper carRegionalSaleMapper;
    @Override
    public List<CarRegionalSale> getTopTenCity() {
            // 创建查询构造器
            QueryWrapper<CarRegionalSale> queryWrapper = new QueryWrapper<>();

            // 对 total_sale 进行降序排序
            queryWrapper.orderByDesc("total_sale");

            // 添加 SQL 语句限制结果数量
            queryWrapper.last("LIMIT 10");

            // 执行查询
            List<CarRegionalSale> topTenCities = carRegionalSaleMapper.selectList(queryWrapper);

            // 返回成功响应
            return topTenCities;
    }

    @Override
    public List<ProvinceSale> getTopTenProvince() {
        // 使用 Mapper 的 getTopTenProvinces 方法执行查询
        List<Map<String, Object>> topTenProvincesResult = carRegionalSaleMapper.getTopTenProvinces();

        // 将查询结果转换为 ProvinceSale 对象列表
        List<ProvinceSale> topTenProvinces = topTenProvincesResult.stream()
                .map(map -> {
                    ProvinceSale provinceSale = new ProvinceSale();
                    provinceSale.setProvince((String) map.get("province"));
                    provinceSale.setTotalSale((Integer) map.get("total_sale"));
                    provinceSale.setPercentage(new BigDecimal(map.get("percentage").toString())); // 使用 BigDecimal 类型
                    return provinceSale;
                })
                .collect(Collectors.toList());
        for(int i=0;i<topTenProvinces.size();++i){
            topTenProvinces.get(i).setRanking(i+1);
        }
        return topTenProvinces;
    }

    @Override
    public List<ProvinceSale> getProvince() {
        // 使用 Mapper 的 getTopTenProvinces 方法执行查询
        List<Map<String, Object>> ProvincesResult = carRegionalSaleMapper.getProvinces();

        // 将查询结果转换为 ProvinceSale 对象列表
        List<ProvinceSale> Provinces = ProvincesResult.stream()
                .map(map -> {
                    ProvinceSale provinceSale = new ProvinceSale();
                    provinceSale.setProvince((String) map.get("province"));
                    provinceSale.setTotalSale((Integer) map.get("total_sale"));
                    provinceSale.setPercentage(new BigDecimal(map.get("percentage").toString())); // 使用 BigDecimal 类型
                    return provinceSale;
                })
                .collect(Collectors.toList());
        for(int i=0;i<Provinces.size();++i){
            Provinces.get(i).setRanking(i+1);
        }
        return Provinces;
    }

    @Override
    public List<CarRegionalSale> getProvinceCity(String province) {
        // 创建查询构造器
        QueryWrapper<CarRegionalSale> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("province", province);
        // 对 total_sale 进行降序排序
        queryWrapper.orderByDesc("total_sale");

        // 执行查询
        List<CarRegionalSale> ProvinceCities = carRegionalSaleMapper.selectList(queryWrapper);

        // 返回成功响应
        return ProvinceCities;
    }

    @Override
    public List<String> getCitiesFromProvince(String province) {
        return carRegionalSaleMapper.getCitiesFromProvince(province);
    }

    @Override
    public List<String> getCities() {
        return carRegionalSaleMapper.getCities();
    }
}
