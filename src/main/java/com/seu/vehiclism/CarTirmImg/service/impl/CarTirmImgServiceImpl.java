package com.seu.vehiclism.CarTirmImg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.CarTirmImg.entity.CarTirmImg;
import com.seu.vehiclism.CarTirmImg.mapper.CarTirmImgMapper;
import com.seu.vehiclism.CarTirmImg.service.ICarTirmImgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 车型图片表 服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-09-02
 */
@Service
public class CarTirmImgServiceImpl extends ServiceImpl<CarTirmImgMapper, CarTirmImg> implements ICarTirmImgService {

    private final CarTirmImgMapper carTirmImgMapper;

    public CarTirmImgServiceImpl(CarTirmImgMapper carTirmImgMapper) {
        this.carTirmImgMapper = carTirmImgMapper;
    }

    @Override
    public String getImg(Long id) {
        QueryWrapper<CarTirmImg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tirm_id", id);

        // 使用 selectList 获取所有记录
        List<CarTirmImg> imgs = carTirmImgMapper.selectList(queryWrapper);

        // 检查列表是否为空
        if (imgs.isEmpty()) {
            // 返回默认值或抛出自定义异常，表示没有找到图片
            return ""; // 或者 throw new NoImageFoundException("No image found for tirm_id: " + id);
        }

        // 返回第一张图片的 URL
        return imgs.get(0).getUrl();
    }
}
