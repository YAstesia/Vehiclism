package com.seu.vehiclism.UserFavoriteCar.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seu.vehiclism.CarTirm.entity.CarTirm;
import com.seu.vehiclism.CarTirm.service.ICarTirmService;
import com.seu.vehiclism.UserFavoriteCar.entity.UserFavoriteCar;
import com.seu.vehiclism.UserFavoriteCar.mapper.UserFavoriteCarMapper;
import com.seu.vehiclism.UserFavoriteCar.service.IUserFavoriteCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户收藏车系表 服务实现类
 * </p>
 *
 * @author QTM
 * @since 2024-09-01
 */
@Service
public class UserFavoriteCarServiceImpl extends ServiceImpl<UserFavoriteCarMapper, UserFavoriteCar> implements IUserFavoriteCarService {
    @Autowired
    UserFavoriteCarMapper userFavoriteCarMapper;
    @Autowired
    private ICarTirmService carTirmService;
    @Override
    public IPage<CarTirm> getUserFavoriteCarByPage(Long id, Integer pageNum, Integer pageSize) {
        QueryWrapper<UserFavoriteCar>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        List<UserFavoriteCar>favoriteCars=baseMapper.selectList(queryWrapper);
        List<Long>seriesIds=favoriteCars.stream()
                .map(UserFavoriteCar::getTirmId)
                .collect(Collectors.toList());
        Page<CarTirm> page=new Page<>(pageNum,pageSize);
        return carTirmService.getCarTrimsBySeriesIdsByPage(seriesIds,page);
    }
    @Override
    public List<CarTirm> getUserFavoriteCar(Long id) {
        QueryWrapper<UserFavoriteCar>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        List<UserFavoriteCar>favoriteCars=baseMapper.selectList(queryWrapper);
        List<Long>TirmIds=favoriteCars.stream()
                .map(UserFavoriteCar::getTirmId)
                .collect(Collectors.toList());
        return carTirmService.getCarTirmsById(TirmIds);
    }
}
