package com.seu.vehiclism.UserFavoriteCar.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seu.vehiclism.CarTirm.entity.CarTirm;
import com.seu.vehiclism.UserFavoriteCar.entity.UserFavoriteCar;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户收藏车系表 服务类
 * </p>
 *
 * @author QTM
 * @since 2024-09-01
 */
public interface IUserFavoriteCarService extends IService<UserFavoriteCar> {

    IPage<CarTirm> getUserFavoriteCarByPage(Long id, Integer pageNum, Integer pageSize);
    List<CarTirm> getUserFavoriteCar(Long id);

}
