package com.seu.vehiclism.CarTirm.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.CarTirm.entity.CarTirmConfig;
import com.seu.vehiclism.CarTirm.service.ICarTirmConfigService;
import com.seu.vehiclism.CarTirm.service.ICarTirmService;
import com.seu.vehiclism.CarTirm.entity.CarTirm;
import com.seu.vehiclism.CarTirmImg.service.ICarTirmImgService;
import com.seu.vehiclism.JoinSeriesTirm.service.IJoinSeriesTirmService;
import com.seu.vehiclism.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车型表 前端控制器
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
@RestController
@RequestMapping("/cartirm")
public class CarTirmController {
    private final ICarTirmService carTirmService;
    private final ICarTirmImgService carTirmImgService;
    private final IJoinSeriesTirmService joinSeriesTirmService;
    private final ICarTirmConfigService carTirmConfigService;

    public CarTirmController(ICarTirmService carTirmService, ICarTirmImgService carTirmImgService, IJoinSeriesTirmService joinSeriesTirmService, ICarTirmConfigService carTirmConfigService) {
        this.carTirmService = carTirmService;
        this.carTirmImgService = carTirmImgService;
        this.joinSeriesTirmService = joinSeriesTirmService;
        this.carTirmConfigService = carTirmConfigService;
    }
    @GetMapping("/tirms")
    public Response<List<CarTirm>> searchTirm() {
        try {
            List<CarTirm> tirms = carTirmService.getAllTirms();
            return Response.newSuccess(tirms, "查询成功");
        } catch (Exception e) {
            return Response.newFail("获取品牌失败");
        }
    }
    @PostMapping("/img")
    public Response<String>getImg(@RequestBody Map<String,Object> request){
        Long id;
        try {
            id=Long.parseLong(request.get("id").toString());
            String img=carTirmImgService.getImg(id);
            return Response.newSuccess(img,"查询成功");
        }catch (Exception e){
            return Response.newFail(e.getMessage());
        }
    }
    @PostMapping("/getCarTirmBySeriesId")
    public Response<List<CarTirm>>getBySeriesId(@RequestBody Map<String,Object> request){
        Long id;
        try {
            id=Long.parseLong(request.get("id").toString());
            List<Long>ids=joinSeriesTirmService.getTirms(id);
            return Response.newSuccess(carTirmService.getCarTrimsByIds(ids),"查询成功");
        }catch (Exception e){
            return Response.newFail(e.getMessage());
        }
    }
    @PostMapping("/getAllEnergyType")
    public Response<List<String>>getAllEnergyType(){
        try {
            return Response.newSuccess(carTirmService.getDistinctEnergyTypes(), "查询成功");
        }catch (Exception e){
            return Response.newFail(e.getMessage());
        }
    }
    @PostMapping("/getCarTirm")
    public Response<CarTirm>getCarTirm(@RequestBody Map<String,Object>request){
        String tirm=(String)request.get("tirm");
        QueryWrapper<CarTirm>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("tirm",tirm);
        return Response.newSuccess(carTirmService.getOne(queryWrapper),"查询成功");
    }
    @PostMapping("/config")
    public Response<List<CarTirmConfig>>getConfig(@RequestBody Map<String,Object>request){
        Long id;
        try {
            id=Long.parseLong(request.get("id").toString());
            return Response.newSuccess(carTirmConfigService.getConfigByTirmId(id),"查询成功");
        }catch (Exception e){
            return Response.newFail(e.getMessage());
        }
    }
}
