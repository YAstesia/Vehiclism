package com.seu.vehiclism.CarSeriesImg.service;

import com.seu.vehiclism.CarSeriesImg.entity.CarSeriesImg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 车系图片表 服务类
 * </p>
 *
 * @author QTM
 * @since 2024-09-02
 */
public interface ICarSeriesImgService extends IService<CarSeriesImg> {

    String getImg(Long id);
}
