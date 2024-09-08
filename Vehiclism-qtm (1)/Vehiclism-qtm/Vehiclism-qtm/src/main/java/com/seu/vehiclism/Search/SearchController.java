package com.seu.vehiclism.Search;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.seu.vehiclism.CarBrand.entity.CarBrand;
import com.seu.vehiclism.CarBrand.service.ICarBrandService;
import com.seu.vehiclism.CarSeries.entity.CarSeries;
import com.seu.vehiclism.CarSeries.service.ICarSeriesService;
import com.seu.vehiclism.CarTirm.entity.CarTirm;
import com.seu.vehiclism.CarTirm.service.ICarTirmService;
import com.seu.vehiclism.Response;
import com.seu.vehiclism.Search.service.SaleInfoService;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("search")
public class SearchController {
    private final ICarBrandService carBrandService;
    private final ICarSeriesService carSeriesService;
    private final ICarTirmService carTirmService;
    private final SaleInfoService saleInfoService;
    public SearchController(ICarBrandService carBrandService, ICarSeriesService carSeriesService, ICarTirmService carTirmService, SaleInfoService saleInfoService) {
        this.carBrandService = carBrandService;
        this.carSeriesService = carSeriesService;
        this.carTirmService = carTirmService;
        this.saleInfoService = saleInfoService;
    }
    @GetMapping("/series")
    public Response<IPage<CarSeries>> searchSeries(@RequestBody Map<String, Object> request) {
        Integer pageNum=(Integer) request.get("pageNum");
        Integer pageSize=(Integer) request.get("pageSize");
        if (pageNum == null || pageSize == null) {
            return Response.newFail("参数缺失");
        }
        try {
            IPage<CarSeries> series = carSeriesService.getSeriesByPage(pageNum, pageSize);
            return Response.newSuccess(series, "查询成功");
        } catch (Exception e) {
            return Response.newFail("获取车型失败");
        }
    }
    @GetMapping("/tirm")
    public Response<IPage<CarTirm>> searchTirms(@RequestBody Map<String, Object>request) {
        Integer pageNum=(Integer) request.get("pageNum");
        Integer pageSize=(Integer) request.get("pageSize");
        if (pageNum == null || pageSize == null) {
            return Response.newFail("参数缺失");
        }
        try {
            IPage<CarTirm> tirms = carTirmService.getTirmsByPage(pageNum, pageSize);
            return Response.newSuccess(tirms, "查询成功");
        } catch (Exception e) {
            return Response.newFail("获取车型失败");
        }
    }
    @GetMapping("/brand")
    public Response<IPage<CarBrand>> searchBrands(@RequestBody Map<String, Object>request) {
        Integer pageNum=(Integer) request.get("pageNum");
        Integer pageSize=(Integer) request.get("pageSize");
        if (pageNum == null || pageSize == null) {
            return Response.newFail("参数缺失");
        }
        try {
            IPage<CarBrand> brands = carBrandService.getBrandsByPage(pageNum, pageSize);
            return Response.newSuccess(brands, "查询成功");
        } catch (Exception e) {
            return Response.newFail("获取车型失败");
        }
    }
    @PostMapping("/filter")
    public Response<IPage<CarTirm>>filterSearch(@RequestBody Map<String, Object> request){
        Integer pageNum = (Integer) request.get("pageNum");
        Integer pageSize = (Integer) request.get("pageSize");
        List<String> brands = (List<String>) request.get("brands");
        List<String> types = (List<String>) request.get("types");
        List<String> series = (List<String>) request.get("series");
        List<String> tirms = (List<String>) request.get("tirm");
        List<String> energyTypes = (List<String>) request.get("energyTypes");
        Integer priceMin = (Integer) request.get("priceMin");
        Integer priceMax = (Integer) request.get("priceMax");
        if (pageNum == null || pageSize == null) {
            return Response.newFail("参数缺失");
        }
        try {
            IPage<CarTirm> result = carTirmService.getFilteredTirmsByPage(pageNum, pageSize, brands, types, series, tirms, energyTypes, priceMin, priceMax);
            return Response.newSuccess(result, "查询成功");
        } catch (Exception e) {
            return Response.newFail(e.getMessage());
        }
    }
    @PostMapping("/filter1")
    public Response<List<CarTirm>>filterSearch1(@RequestBody Map<String, Object> request){
        List<String> brands = (List<String>) request.get("brands");
        List<String> types = (List<String>) request.get("types");
        List<String> series = (List<String>) request.get("series");
        List<String> tirms = (List<String>) request.get("tirm");
        List<String> energyTypes = (List<String>) request.get("energyTypes");
        Integer priceMin = (Integer) request.get("priceMin");
        Integer priceMax = (Integer) request.get("priceMax");
        try {
            List<CarTirm> result = carTirmService.getFilteredTirms(brands, types, series, tirms, energyTypes, priceMin, priceMax);
            return Response.newSuccess(result, "查询成功");
        } catch (Exception e) {
            return Response.newFail(e.getMessage());
        }
    }
    @PostMapping("/keyword")
    public Response<IPage<CarTirm>>keySearch(@RequestBody Map<String,Object>request){
        Integer pageNum = (Integer) request.get("pageNum");
        Integer pageSize = (Integer) request.get("pageSize");
        String key=(String)request.get("keyword");
        if (pageNum == null || pageSize == null) {
            return Response.newFail("参数缺失");
        }try{
            IPage<CarTirm>result=carTirmService.getKeywordTirmsByPage(pageNum,pageSize,key);
            return Response.newSuccess(result,"查询成功");
        }catch (Exception e){
            return Response.newFail(e.getMessage());
        }
    }
    @PostMapping("/SaleKeyword")
    public Response<IPage<SaleInfo>>salekeySearch(@RequestBody Map<String,Object>request){
        Integer pageNum = (Integer) request.get("pageNum");
        Integer pageSize = (Integer) request.get("pageSize");
        String key=(String)request.get("keyword");
        if (pageNum == null || pageSize == null) {
            return Response.newFail("参数缺失");
        }try{
            IPage<SaleInfo>result=saleInfoService.getKeywordSaleByPage(pageNum,pageSize,key);
            return Response.newSuccess(result,"查询成功");
        }catch (Exception e){
            return Response.newFail(e.getMessage());
        }
    }
    @PostMapping("/filterSales")
    public Response<IPage<SaleInfo>> filterSales(@RequestBody Map<String, Object> request) {
        Integer pageNum = Optional.ofNullable(request.get("pageNum")).map(o -> ((Number) o).intValue()).orElse(null);
        Integer pageSize = Optional.ofNullable(request.get("pageSize")).map(o -> ((Number) o).intValue()).orElse(null);
        List<String> brands = Optional.ofNullable(request.get("brands")).map(o -> (List<String>) o).orElse(null);
        List<String> types = Optional.ofNullable(request.get("types")).map(o -> (List<String>) o).orElse(null);
        List<String> series = Optional.ofNullable(request.get("series")).map(o -> (List<String>) o).orElse(null);
        Integer priceMin = Optional.ofNullable(request.get("priceMin")).map(o -> ((Number) o).intValue()).orElse(null);
        Integer priceMax = Optional.ofNullable(request.get("priceMax")).map(o -> ((Number) o).intValue()).orElse(null);
        Date timeStart=null;Date timeEnd=null;
        if(request.get("timeStart")!=null){
            timeStart = parseDate(Optional.ofNullable(request.get("timeStart")).map(o -> (String) o).orElse(null));
        }
        if(request.get("timeEnd")!=null){
            timeEnd = parseDate(Optional.ofNullable(request.get("timeEnd")).map(o -> (String) o).orElse(null));
        }

        if (pageNum == null || pageSize == null) {
            return Response.newFail("参数缺失");
        }

        try {
            IPage<SaleInfo> result = saleInfoService.filterSales(pageNum, pageSize, brands, types, series, priceMin, priceMax, timeStart, timeEnd);
            return Response.newSuccess(result, "查询成功");
        } catch (Exception e) {
            return Response.newFail(e.getMessage());
        }
    }

    @GetMapping("/AllSales")
    public Response<List<SaleInfo>>AllSales(){
        try{
            List<SaleInfo> result = saleInfoService.AllSales();
            return Response.newSuccess(result,"查询成功");
        }catch (Exception e){
            return Response.newFail(e.getMessage());
        }
    }
    private Date parseDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateString, e);
        }
    }
}
