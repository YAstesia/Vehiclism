package com.seu.vehiclism.CarBrand.controller;


import com.seu.vehiclism.CarBrand.entity.CarBrand;
import com.seu.vehiclism.CarBrand.entity.CarBrandSaleMonth;
import com.seu.vehiclism.CarBrand.entity.CarBrandSaleYear;
import com.seu.vehiclism.CarBrand.service.ICarBrandSaleMonthService;
import com.seu.vehiclism.CarBrand.service.ICarBrandSaleYearService;
import com.seu.vehiclism.CarBrand.service.ICarBrandService;
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
 * @since 2024-08-29
 */
@RestController
@RequestMapping("")
public class CarBrandController {
    private final ICarBrandService carBrandService;
    private final ICarBrandSaleMonthService carBrandSaleMonthService;
    private final ICarBrandSaleYearService carBrandSaleYearService;
    public CarBrandController(ICarBrandService carBrandService, ICarBrandSaleMonthService carBrandSaleMonthService, ICarBrandSaleYearService carBrandSaleYearService) {
        this.carBrandService = carBrandService;
        this.carBrandSaleMonthService = carBrandSaleMonthService;
        this.carBrandSaleYearService = carBrandSaleYearService;
    }
    @GetMapping("/brands")
    public Response<List<CarBrand>> searchBrand(){
        try{
            List<CarBrand>brands=carBrandService.getAllBrands();
            return Response.newSuccess(brands,"查询成功");
        }catch (Exception e){
            return Response.newFail("获取品牌失败");
        }
    }
    @PostMapping("/getBrandSaleMonth")
    public Response<List<CarBrandSaleMonth>>getBrandSaleMonth(@RequestBody Map<String,Object> request){
        Integer year=(Integer) request.get("year");
        Integer month=(Integer) request.get("month");
        return Response.newSuccess(carBrandSaleMonthService.getBrandSaleMonth(year,month),"查询成功");
    }
    @PostMapping("/getBrandSaleYear")
    public Response<List<CarBrandSaleYear>>getBrandSaleYear(@RequestBody Map<String,Object> request){
        Integer year=(Integer) request.get("year");
        return Response.newSuccess(carBrandSaleYearService.getBrandSaleYear(year),"查询成功");
    }
}
