package com.seu.vehiclism.CarRegionalSale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.CarRegionalSale.entity.CarRegionalSeriesSale;
import com.seu.vehiclism.CarRegionalSale.mapper.CarRegionalSaleMapper;
import com.seu.vehiclism.CarRegionalSale.mapper.CarRegionalSeriesSaleMapper;
import com.seu.vehiclism.CarRegionalSale.service.ICarRegionalSeriesSaleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-09-04
 */
@Service
public class CarRegionalSeriesSaleServiceImpl extends ServiceImpl<CarRegionalSeriesSaleMapper, CarRegionalSeriesSale> implements ICarRegionalSeriesSaleService {

    @Autowired
    private CarRegionalSeriesSaleMapper carRegionalSeriesSaleMapper;
    @Override
    public List<CarRegionalSeriesSale> getCityTopTen(String city) {
        QueryWrapper<CarRegionalSeriesSale>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("region",city);
        queryWrapper.orderByAsc("ranking");
        List<CarRegionalSeriesSale> cityTopTen=carRegionalSeriesSaleMapper.selectList(queryWrapper);
        return cityTopTen;
    }

    @Override
    public List<CarRegionalSeriesSale> getProvinceTopTen(String province) {
        List<CarRegionalSeriesSale>provinceTopTen=carRegionalSeriesSaleMapper.getProvinceTopTenSeries(province);
        for(int i=0;i<provinceTopTen.size();++i){
            provinceTopTen.get(i).setRanking(i+1);
        }
        return provinceTopTen;
    }

    @Override
    public List<CarRegionalSeriesSale> getAllTopTen() {
        List<CarRegionalSeriesSale>TopTen=carRegionalSeriesSaleMapper.getTopTenSeries();
        for(int i=0;i<TopTen.size();++i){
            TopTen.get(i).setRanking(i+1);
        }
        return TopTen;
    }
}
