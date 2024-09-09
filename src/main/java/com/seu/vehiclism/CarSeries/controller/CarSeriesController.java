package com.seu.vehiclism.CarSeries.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.CarSeries.entity.*;
import com.seu.vehiclism.CarSeries.service.*;
import com.seu.vehiclism.CarSeriesImg.service.ICarSeriesImgService;
import com.seu.vehiclism.Response;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 车系表 前端控制器
 * </p>
 *
 * @author QTM
 * @since 2024-08-29
 */
@RestController
@RequestMapping("/carseries")
public class CarSeriesController {
    private final ICarSeriesService carSeriesService;
    private final ICarSeriesImgService carSeriesImgService;
    private final ICarSeriesSaleYearService carSeriesSaleYearService;
    private final ICarSeriesSaleMonthService carSeriesSaleMonthService;
    private final ICarPurchasePurposeService carPurchasePurposeService;
    private final ICarEvlIdxService carEvlIdxService;
    private final ICarDefectService carDefectService;
    private final ICarDefectReasonService carDefectReasonService;
    private final ChatClient chatClient;

    @Value("classpath:purchasePurpose_prompt.st")
    private Resource templateResource;

    public CarSeriesController(ICarSeriesService carSeriesService, ICarSeriesImgService carSeriesImgService, ICarSeriesSaleYearService carSeriesSaleYearService, ICarSeriesSaleMonthService carSeriesSaleMonthService, ICarPurchasePurposeService carPurchasePurposeService, ICarEvlIdxService carEvlIdxService, ICarDefectService carDefectService, ICarDefectReasonService carDefectReasonService, ChatClient chatClient) {
        this.carSeriesService = carSeriesService;
        this.carSeriesImgService = carSeriesImgService;
        this.carSeriesSaleYearService = carSeriesSaleYearService;
        this.carSeriesSaleMonthService = carSeriesSaleMonthService;
        this.carPurchasePurposeService = carPurchasePurposeService;
        this.carEvlIdxService = carEvlIdxService;
        this.carDefectService = carDefectService;
        this.carDefectReasonService = carDefectReasonService;
        this.chatClient = chatClient;
    }

    @GetMapping("/series")
    public Response<List<CarSeries>> searchSeries(){
        try{
            List<CarSeries>brands=carSeriesService.getAllSeries();
            return Response.newSuccess(brands,"查询成功");
        }catch (Exception e){
            return Response.newFail("获取品牌失败");
        }
    }

    @PostMapping("/getCarSeries")
    Response<CarSeries>getCarSeries(@RequestBody Map<String,Object>request){
        String series=(String)request.get("series");
        QueryWrapper<CarSeries>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("series",series);
        return Response.newSuccess(carSeriesService.getOne(queryWrapper),"查询成功");
    }

    @PostMapping("/img")
    public Response<String>getImg(@RequestBody Map<String,Object> request){
        Long id;
        try {
            id=Long.parseLong(request.get("id").toString());
            String img=carSeriesImgService.getImg(id);
            return Response.newSuccess(img,"查询成功");
        }catch (Exception e){
            return Response.newFail(e.getMessage());
        }
    }

    @PostMapping("/getSeriesSaleYear")
    public Response<List<CarSeriesSaleYear>>getSeriesSaleYear(@RequestBody Map<String,Object>request){
        Integer year=(Integer) request.get("year");
        return Response.newSuccess(carSeriesSaleYearService.getSeriesSaleYear(year),"查询成功");
    }

    @PostMapping("/getSeriesSaleMonth")
    public Response<List<CarSeriesSaleMonth>>getSeriesSaleMonth(@RequestBody Map<String,Object>request){
        Integer year=(Integer) request.get("year");
        Integer month=(Integer) request.get("month");
        return Response.newSuccess(carSeriesSaleMonthService.getSeriesSaleMonth(year,month),"查询成功");
    }

    /**
     * 根据车系名称返回车系ID
     * @param request
     * @return
     */
    @PostMapping("/getSeriesIdByName")
    public Response<Long> getSeriesIdByName(@RequestBody Map<String, String> request) {
        String seriesName = request.get("seriesName");
        Long seriesId = carSeriesService.getSeriesIdByName(seriesName);
        if (seriesId != null) {
            return Response.newSuccess(seriesId, "查询成功");
        } else {
            return Response.newFail("未找到对应的车系ID");
        }
    }

    /**
     * 获取车系购车目的
     * @param request
     * @return
     */
    @PostMapping("/getSeriesPurpose")
    public Response<List<CarPurchasePurpose>>getSeriesPurpose(@RequestBody Map<String,Object>request){
        String series=(String)request.get("series");
        QueryWrapper<CarSeries>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("series",series);
        Long series_id=carSeriesService.getOne(queryWrapper).getId();
        QueryWrapper<CarPurchasePurpose>queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("series_id",series_id);
        queryWrapper1.orderByDesc("cnt");
        return Response.newSuccess(carPurchasePurposeService.list(queryWrapper1),"查询成功");
    }

    /**
     * 获取车系评价指标
     * @param request
     * @return
     */
    @PostMapping("/evl")
    public Response<CarEvlIdx>getSeriesCarEvlIdx(@RequestBody Map<String,Object>request){
        Long id;
        try {
            id=Long.parseLong(request.get("id").toString());
            QueryWrapper<CarEvlIdx>queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("series_id",id);
            return Response.newSuccess(carEvlIdxService.getOne(queryWrapper),"查询成功");
        }catch (Exception e){
            return Response.newFail(e.getMessage());
        }
    }


    /**
     * 根据车系ID获取百车故障数
     * @param request 请求体，包含seriesId
     * @return 返回百车故障数详细信息
     */
    @PostMapping("/defect")
    public Response<CarDefect> getCarDefect(@RequestBody Map<String, Object> request) {
        Long seriesId;
        try {
            seriesId = Long.parseLong(request.get("seriesId").toString());
            QueryWrapper<CarDefect> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("series_id", seriesId);
            CarDefect carDefect = carDefectService.getOne(queryWrapper);
            if (carDefect != null) {
                return Response.newSuccess(carDefect, "查询成功");
            } else {
                return Response.newFail("未找到指定车系的百车故障数据");
            }
        } catch (Exception e) {
            return Response.newFail("查询失败: " + e.getMessage());
        }
    }


    /**
     * 根据车系ID获取故障原因
     * @param request 请求体，包含seriesId
     * @return 返回车系故障原因列表
     */
    @PostMapping("/defectReason")
    public Response<List<CarDefectReason>> getDefectReasonsBySeriesId(@RequestBody Map<String, Object> request) {
        String seriesId;
        try {
            seriesId = request.get("seriesId").toString();  // 直接获取 String 类型的 seriesId
            List<CarDefectReason> defectReasons = carDefectReasonService.getDefectReasonsBySeriesId(seriesId);
            if (defectReasons != null && !defectReasons.isEmpty()) {
                return Response.newSuccess(defectReasons, "查询成功");
            } else {
                return Response.newFail("未找到指定车系的故障原因数据");
            }
        } catch (Exception e) {
            return Response.newFail("查询失败: " + e.getMessage());
        }
    }

    /**
     * 购车目的分析文本
     * @param request
     * @return
     */
    @PostMapping("/getSeriesPurposeAnalysis")
    public Response<String> getSeriesPurposeAnalysis(@RequestBody Map<String, Object> request) {

        // 获取车系名称
        String series = (String) request.get("series");

        // 查询车系的购车目的数据
        QueryWrapper<CarSeries> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("series", series);
        CarSeries carSeries = carSeriesService.getOne(queryWrapper);

        // 没有找到对应的车系
        if (carSeries == null) {
            return Response.newFail("未找到指定车系");
        }

        Long seriesId = carSeries.getId();

        // 查询购车目的数据
        QueryWrapper<CarPurchasePurpose> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("series_id", seriesId);
        queryWrapper1.orderByDesc("cnt");
        List<CarPurchasePurpose> purposes = carPurchasePurposeService.list(queryWrapper1);

        // 检查是否有购车目的数据
        if (purposes.isEmpty()) {
            return Response.newSuccess("该车系的购车目的数据暂缺", "分析成功");
        }

        // 检查是否所有的购车目的占比为0
        boolean allZero = purposes.stream().allMatch(p -> p.getCnt() == 0);
        if (allZero) {
            return Response.newSuccess("该车系的购车目的数据暂缺", "分析成功");
        }

        // 构造提示词模板
        PromptTemplate promptTemplate = new PromptTemplate(templateResource);

        // 将购车目的数据整理为用于传递给AI的输入
        String purposeData = purposes.stream()
                .map(p -> String.format("%s 占比%.2f%%", p.getPurpose(), p.getPercentage()))
                .collect(Collectors.joining(", "));

        // 构造prompt
        Prompt prompt = promptTemplate.create(Map.of("series", series, "data", purposeData));

        // 调用AI进行分析
        ChatResponse chatResponse = chatClient.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();

        return Response.newSuccess(assistantMessage.getContent(), "分析成功");
    }


}
