package com.seu.vehiclism.CarSales.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.CarSales.entity.CarSales;
import com.seu.vehiclism.CarSales.service.ICarSalesService;
import com.seu.vehiclism.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 汽车销售记录表 前端控制器
 * </p>
 *
 * @author QTM
 * @since 2024-09-01
 */
@RestController
@RequestMapping("/CarSales")
public class CarSalesController {
    private final ICarSalesService carSalesService;

    public CarSalesController(ICarSalesService carSalesService) {
        this.carSalesService = carSalesService;
    }
    @PostMapping("")
    public Response<List<CarSales>> getCarSales(@RequestBody Map<String,Object> request){
        Long id;
        try {
            id = Long.parseLong(request.get("id").toString());
            Integer year=(Integer) request.get("year");
            QueryWrapper<CarSales>queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("series_id",id).eq("year",year).orderByAsc("month");
            return Response.newSuccess(carSalesService.list(queryWrapper),"查询成功");
        }catch(Exception e){
            return Response.newFail(e.getMessage());
        }
    }
}
