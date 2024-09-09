package com.seu.vehiclism.CarTirmImg.service;

import com.seu.vehiclism.CarTirmImg.entity.CarTirmImg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 车型图片表 服务类
 * </p>
 *
 * @author QTM
 * @since 2024-09-02
 */
public interface ICarTirmImgService extends IService<CarTirmImg> {

    String getImg(Long id);
}
