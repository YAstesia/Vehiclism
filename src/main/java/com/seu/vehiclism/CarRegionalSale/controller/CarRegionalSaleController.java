package com.seu.vehiclism.CarRegionalSale.controller;


import com.seu.vehiclism.CarRegionalSale.ProvinceSale;
import com.seu.vehiclism.CarRegionalSale.entity.CarRegionalSale;
import com.seu.vehiclism.CarRegionalSale.entity.CarRegionalSeriesSale;
import com.seu.vehiclism.CarRegionalSale.service.ICarRegionalSaleService;
import com.seu.vehiclism.CarRegionalSale.service.ICarRegionalSeriesSaleService;
import com.seu.vehiclism.Response;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author QTM
 * @since 2024-09-03
 */
@RestController
@RequestMapping("/CarRegionalSale")
public class CarRegionalSaleController {
//    private static final Map<String, String> cityToProvinceMap = new HashMap<>();
    private final ICarRegionalSaleService carRegionalSaleService;
    private final ICarRegionalSeriesSaleService carRegionalSeriesSaleService;

    public CarRegionalSaleController(ICarRegionalSaleService carRegionalSaleService, ICarRegionalSeriesSaleService carRegionalSeriesSaleService) {
        this.carRegionalSaleService = carRegionalSaleService;
        this.carRegionalSeriesSaleService = carRegionalSeriesSaleService;
    }

    /**
     * 获取省份销量信息
     * @return
     */
    @PostMapping("/Provinces")
    Response<List<ProvinceSale>> getProvinceSale(){
        return Response.newSuccess(carRegionalSaleService.getProvince(),"查询成功");
    }

    /**
     * 获取省份销量前十车系
     * @return
     */
    @PostMapping("/TopTenProvinces")
    Response<List<ProvinceSale>>getTopTenProvinceSale(){
        return Response.newSuccess(carRegionalSaleService.getTopTenProvince(),"查询成功");
    }


    @PostMapping("/Cities")
    Response<List<CarRegionalSale>>getCitySale(){
        return null;
    }

    @PostMapping("/TopTenCities")
    Response<List<CarRegionalSale>>getTopTenCitySale(){
        return Response.newSuccess(carRegionalSaleService.getTopTenCity(),"查询成功");
    }

    /**
     * 获取省份城市信息
     * @param request
     * @return
     */
    @PostMapping("/ProvinceCities")
    Response<List<CarRegionalSale>>getProvinceCity(@RequestBody Map<String,Object>request){
        String province=(String)request.get("provinceName");
        return Response.newSuccess(carRegionalSaleService.getProvinceCity(province),"查询成功");
    }

    /**
     *
     * @return
     */
    @PostMapping("/Regions")
    Response<List<ProvinceSale>>getRegionSale(){
        List<ProvinceSale> provinceSales = carRegionalSaleService.getProvince();

        // 映射表，将省份映射到地区
        Map<String, String> provinceToRegionMap = new HashMap<>();
        provinceToRegionMap.put("北京市", "华北地区");
        provinceToRegionMap.put("天津市", "华北地区");
        provinceToRegionMap.put("河北省", "华北地区");
        provinceToRegionMap.put("山西省", "华北地区");
        provinceToRegionMap.put("内蒙古自治区", "华北地区");

        provinceToRegionMap.put("广东省", "华南地区");
        provinceToRegionMap.put("广西壮族自治区", "华南地区");
        provinceToRegionMap.put("海南省", "华南地区");
        provinceToRegionMap.put("福建省", "华南地区");
        provinceToRegionMap.put("台湾省", "华南地区");
        provinceToRegionMap.put("香港", "华南地区");
        provinceToRegionMap.put("澳门", "华南地区");

        provinceToRegionMap.put("河南省", "华中地区");
        provinceToRegionMap.put("湖北省", "华中地区");
        provinceToRegionMap.put("湖南省", "华中地区");

        provinceToRegionMap.put("辽宁省", "东北地区");
        provinceToRegionMap.put("吉林省", "东北地区");
        provinceToRegionMap.put("黑龙江省", "东北地区");

        provinceToRegionMap.put("陕西省", "西北地区");
        provinceToRegionMap.put("甘肃省", "西北地区");
        provinceToRegionMap.put("青海省", "西北地区");
        provinceToRegionMap.put("宁夏回族自治区", "西北地区");
        provinceToRegionMap.put("新疆维吾尔自治区", "西北地区");

        provinceToRegionMap.put("四川省", "西南地区");
        provinceToRegionMap.put("贵州省", "西南地区");
        provinceToRegionMap.put("云南省", "西南地区");
        provinceToRegionMap.put("西藏自治区", "西南地区");
        provinceToRegionMap.put("重庆市", "西南地区");
        provinceToRegionMap.put("江西省", "华东地区");
        provinceToRegionMap.put("安徽省", "华东地区");
        provinceToRegionMap.put("山东省", "华东地区");
        provinceToRegionMap.put("江苏省", "华东地区");
        provinceToRegionMap.put("浙江省", "华东地区");
        provinceToRegionMap.put("上海市", "华东地区");

        // 结果列表
        List<ProvinceSale> regionalSales = Arrays.asList(
                new ProvinceSale("华北地区", 0, BigDecimal.ZERO),
                new ProvinceSale("华南地区", 0, BigDecimal.ZERO),
                new ProvinceSale("华中地区", 0, BigDecimal.ZERO),
                new ProvinceSale("东北地区", 0, BigDecimal.ZERO),
                new ProvinceSale("西北地区", 0, BigDecimal.ZERO),
                new ProvinceSale("西南地区", 0, BigDecimal.ZERO),
                new ProvinceSale("华东地区", 0, BigDecimal.ZERO)
        );

        // 总销售额
        int totalSales = 0;

        // 遍历省份销售数据，累计每个地区的销售额
        for (ProvinceSale sale : provinceSales) {
            String region = provinceToRegionMap.get(sale.getProvince());
            if (region != null) {
                ProvinceSale rs = findRegionalSale(region, regionalSales);
                rs.setTotalSale(rs.getTotalSale() + sale.getTotalSale());
                totalSales += sale.getTotalSale();
            }
        }

        // 计算每个地区的销售占比
        for (ProvinceSale rs : regionalSales) {
            if (totalSales > 0) {
                BigDecimal percentage = BigDecimal.valueOf(rs.getTotalSale())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(totalSales), 2, RoundingMode.HALF_UP);
                rs.setPercentage(percentage);
            }
        }
        // 返回结果
        return Response.newSuccess(regionalSales,"查询成功");
    }
    private ProvinceSale findRegionalSale(String region, List<ProvinceSale> regionalSales) {
        return regionalSales.stream()
                .filter(rs -> rs.getProvince().equals(region))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/AllTopTenSeries")
    Response<List<CarRegionalSeriesSale>>getAllTopTenSeries(){
        return Response.newSuccess(carRegionalSeriesSaleService.getAllTopTen(),"查询成功");
    }

    @PostMapping("/ProvinceTopTenSeries")
    Response<List<CarRegionalSeriesSale>>getProvinceTopTenSeries(@RequestBody Map<String,Object>request){
        String province=(String)request.get("provinceName");
        return Response.newSuccess(carRegionalSeriesSaleService.getProvinceTopTen(province),"查询成功");
    }

    @PostMapping("/CityTopTenSeries")
    Response<List<CarRegionalSeriesSale>>getCityTopTenSeries(@RequestBody Map<String,Object>request){
        String city=(String)request.get("cityName");
        return Response.newSuccess(carRegionalSeriesSaleService.getCityTopTen(city),"查询成功");
    }

    @PostMapping("/getCitiesFromProvince")
    Response<List<String>>getCitiesFromProvince(@RequestBody Map<String,Object>request){
        String province=(String)request.get("provinceName");
        return Response.newSuccess(carRegionalSaleService.getCitiesFromProvince(province),"查询成功");
    }

    @PostMapping("/getCities")
    Response<List<String>>getCities(){
        return Response.newSuccess(carRegionalSaleService.getCities(),"查询成功");
    }
}
