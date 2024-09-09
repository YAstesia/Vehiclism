package com.seu.vehiclism.CarSeriesImg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.CarSeriesImg.entity.CarSeriesImg;
import com.seu.vehiclism.CarSeriesImg.mapper.CarSeriesImgMapper;
import com.seu.vehiclism.CarSeriesImg.service.ICarSeriesImgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车系图片表 服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-09-02
 */
@Service
public class CarSeriesImgServiceImpl extends ServiceImpl<CarSeriesImgMapper, CarSeriesImg> implements ICarSeriesImgService {
    private final CarSeriesImgMapper carSeriesImgMapper;

    public CarSeriesImgServiceImpl(CarSeriesImgMapper carSeriesImgMapper) {
        this.carSeriesImgMapper = carSeriesImgMapper;
    }

    @Override
    public String getImg(Long id) {
        QueryWrapper<CarSeriesImg> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("series_id",id);
        String img=carSeriesImgMapper.selectOne(queryWrapper).getUrl();
        return img;
    }
}
