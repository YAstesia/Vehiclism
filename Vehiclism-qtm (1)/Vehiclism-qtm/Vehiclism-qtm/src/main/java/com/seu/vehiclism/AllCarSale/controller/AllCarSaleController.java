package com.seu.vehiclism.AllCarSale.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.AllCarSale.entity.AllCarSale;
import com.seu.vehiclism.AllCarSale.service.IAllCarSaleService;
import com.seu.vehiclism.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author QTM
 * @since 2024-09-02
 */
@RestController
@RequestMapping("/AllCarSale")
public class AllCarSaleController {
    private final IAllCarSaleService allCarSaleService;

    public AllCarSaleController(IAllCarSaleService allCarSaleService) {
        this.allCarSaleService = allCarSaleService;
    }

    @GetMapping("/Year")
    public Response<List<YearCarSale>>getYearCarSale(){
        List<YearCarSale>yearCarSales=allCarSaleService.getYearlySales();
        return Response.newSuccess(yearCarSales,"查询成功");
    }
    @PostMapping("/Month")
    public Response<List<AllCarSale>>getMonthCarSale(@RequestBody Map<String,Object> request){
        Integer year=(Integer) request.get("year");
        QueryWrapper<AllCarSale>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("year",year);
        queryWrapper.orderByAsc("month");
        List<AllCarSale>allCarSales=allCarSaleService.getBaseMapper().selectList(queryWrapper);
        return Response.newSuccess(allCarSales,"查询成功");
    }
}
