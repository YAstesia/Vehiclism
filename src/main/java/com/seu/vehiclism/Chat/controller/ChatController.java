package com.seu.vehiclism.Chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seu.vehiclism.CarRegionalSale.entity.CarRegionalSeriesSale;
import com.seu.vehiclism.CarRegionalSale.service.ICarRegionalSeriesSaleService;
import com.seu.vehiclism.CarSeries.entity.CarDefectReason;
import com.seu.vehiclism.CarSeries.entity.CarEvlIdx;
import com.seu.vehiclism.CarSeries.entity.CarPurchasePurpose;
import com.seu.vehiclism.CarSeries.entity.CarSeries;
import com.seu.vehiclism.CarSeries.service.ICarDefectReasonService;
import com.seu.vehiclism.CarSeries.service.ICarEvlIdxService;
import com.seu.vehiclism.CarSeries.service.ICarPurchasePurposeService;
import com.seu.vehiclism.CarSeries.service.ICarSeriesService;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ICarPurchasePurposeService carPurchasePurposeService;
    private final ICarRegionalSeriesSaleService carRegionalSeriesSaleService;
    private final ICarEvlIdxService carEvlIdxService;
    private final ICarSeriesService carSeriesService;
    private final ICarDefectReasonService carDefectReasonService;
    private final ChatClient chatClient;

    @Value("classpath:salesStrategy_prompt.st")
    private Resource templateResource;

    public ChatController(ICarPurchasePurposeService carPurchasePurposeService,
                          ICarRegionalSeriesSaleService carRegionalSeriesSaleService,
                          ICarEvlIdxService carEvlIdxService,
                          ICarSeriesService carSeriesService,
                          ICarDefectReasonService carDefectReasonService, ChatClient chatClient) {
        this.carPurchasePurposeService = carPurchasePurposeService;
        this.carRegionalSeriesSaleService = carRegionalSeriesSaleService;
        this.carEvlIdxService = carEvlIdxService;
        this.carSeriesService = carSeriesService;
        this.carDefectReasonService = carDefectReasonService;
        this.chatClient = chatClient;
    }

    /**
     * 提供销售策略
     * @param request 包含区域、销售群体和车型偏好
     * @return AI 生成的销售策略
     */
    @PostMapping("/salesStrategy")
    public String getSalesStrategy(@RequestBody Map<String, Object> request) {
        try {
            String region = (String) request.get("region");
            String saleGroup = (String) request.get("saleGroup");

            // 获取地区的车系销售前十
            List<CarRegionalSeriesSale> regionalTopTen = (region != null && !region.isEmpty())
                    ? carRegionalSeriesSaleService.getProvinceTopTen(region)
                    : carRegionalSeriesSaleService.getAllTopTen();

            // 获取车系的购车目的和评分
            Map<String, List<CarPurchasePurpose>> purchasePurposesMap = getPurchasePurposesBySeriesList(regionalTopTen);
            Map<String, CarEvlIdx> evaluationDataMap = getCarEvaluationsBySeriesList(regionalTopTen);

            // 构造车系详细信息
            String seriesData = buildSeriesData(regionalTopTen, purchasePurposesMap, evaluationDataMap);

            // 加载st模板
            PromptTemplate promptTemplate = new PromptTemplate(templateResource);
            Prompt prompt = promptTemplate.create(Map.of(
                    "region", region != null ? region : "全国",
                    "saleGroup", saleGroup != null ? saleGroup : "所有群体",
                    "seriesData", seriesData
            ));

            // 调用AI接口
            ChatResponse chatResponse = chatClient.call(prompt);
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();

            return assistantMessage.getContent();

        } catch (Exception e) {
            return "调用AI时出错: " + e.getMessage();
        }
    }

    /**
     * 根据车系获取购车目的列表
     * @param regionalTopTen
     * @return
     */
    private Map<String, List<CarPurchasePurpose>> getPurchasePurposesBySeriesList(List<CarRegionalSeriesSale> regionalTopTen) {
        return regionalTopTen.stream().collect(Collectors.toMap(
                CarRegionalSeriesSale::getSeries,
                series -> {
                    Long seriesId = carSeriesService.getSeriesIdByName(series.getSeries());
                    if (seriesId != null) {
                        return carPurchasePurposeService.list(new QueryWrapper<CarPurchasePurpose>().eq("series_id", seriesId));
                    }
                    return Collections.emptyList();
                }
        ));
    }

    /**
     * 根据车系列表获取评分列表
     * @param regionalTopTen
     * @return
     */
    private Map<String, CarEvlIdx> getCarEvaluationsBySeriesList(List<CarRegionalSeriesSale> regionalTopTen) {
        return regionalTopTen.stream().collect(Collectors.toMap(
                CarRegionalSeriesSale::getSeries,
                series -> {
                    Long seriesId = carSeriesService.getSeriesIdByName(series.getSeries());
                    if (seriesId != null) {
                        return carEvlIdxService.getOne(new QueryWrapper<CarEvlIdx>().eq("series_id", seriesId));
                    }
                    return null;
                }
        ));
    }

    /**
     * 构造推荐车系信息输入流
     * @param regionalTopTen
     * @param purchasePurposesMap
     * @param evaluationDataMap
     * @return
     */
    private String buildSeriesData(List<CarRegionalSeriesSale> regionalTopTen,
                                   Map<String, List<CarPurchasePurpose>> purchasePurposesMap,
                                   Map<String, CarEvlIdx> evaluationDataMap) {
        StringBuilder seriesData = new StringBuilder();

        // 添加车系信息及详细数据
        for (int i = 0; i < regionalTopTen.size(); i++) {
            CarRegionalSeriesSale sale = regionalTopTen.get(i);

            // 获取车系的详细信息
            CarSeries carSeries = carSeriesService.getOne(new QueryWrapper<CarSeries>().eq("series", sale.getSeries()));

            seriesData.append(i + 1).append(". ").append(sale.getSeries()).append("\n")
                    .append("   - 销量: ").append(sale.getTotalSale()).append("\n");

            // 添加车系的详细信息
            if (carSeries != null) {
                seriesData.append("   - 品牌: ").append(carSeries.getBrand()).append("\n")
                        .append("   - 价格区间: ").append(carSeries.getPriceMin()).append(" - ").append(carSeries.getPriceMax()).append("\n")
                        .append("   - 类型: ").append(carSeries.getType()).append("\n");
            }

            // 购车目的
            seriesData.append("   - 购车目的: ");
            List<CarPurchasePurpose> purposes = purchasePurposesMap.get(sale.getSeries());
            if (purposes != null && !purposes.isEmpty()) {
                for (CarPurchasePurpose purpose : purposes) {
                    seriesData.append(purpose.getPurpose()).append(" (").append(purpose.getPercentage()).append("%), ");
                }
                seriesData.setLength(seriesData.length() - 2); // 删除最后一个逗号
            } else {
                seriesData.append("无购车目的数据");
            }
            seriesData.append("\n");

            // 评分数据
            CarEvlIdx eval = evaluationDataMap.get(sale.getSeries());
            if (eval != null) {
                seriesData.append("   - 综合评分: ").append(eval.getOverallRating()).append("\n")
                        .append("     空间评分: ").append(eval.getSpace()).append("\n")
                        .append("     驾驶感受评分: ").append(eval.getDriveFeel()).append("\n")
                        .append("     能耗评分: ").append(eval.getPowerConsum()).append("\n")
                        .append("     外观评分: ").append(eval.getOutDecor()).append("\n")
                        .append("     内饰评分: ").append(eval.getInDecor()).append("\n")
                        .append("     性价比评分: ").append(eval.getQpRatio()).append("\n")
                        .append("     配置评分: ").append(eval.getConfigure()).append("\n");
            } else {
                seriesData.append("   - 无评分数据\n");
            }

            // 新增的百车故障数和故障原因数据
            Long seriesId = carSeriesService.getSeriesIdByName(sale.getSeries());
            if (seriesId != null) {
                List<CarDefectReason> defectReasons = carDefectReasonService.getDefectReasonsBySeriesId(seriesId.toString());
                if (defectReasons != null && !defectReasons.isEmpty()) {
                    seriesData.append("   - 百车故障数: ").append(defectReasons.size()).append("\n")
                            .append("   - 故障原因:\n");
                    for (CarDefectReason reason : defectReasons) {
                        seriesData.append("       - ").append(reason.getReason()).append(" (").append(reason.getCount()).append("次)\n");
                    }
                } else {
                    seriesData.append("   - 无百车故障数和故障原因数据\n");
                }
            } else {
                seriesData.append("   - 无百车故障数和故障原因数据\n");
            }
        }
        return seriesData.toString();
    }

//    /**
//     * 提供销售策略
//     * @param request 包含区域、销售群体和车型偏好
//     * @return 拼接的车系信息和传递给 AI 的完整内容
//     */
//    @PostMapping("/salesStrategy")
//    public String getSalesStrategy(@RequestBody Map<String, Object> request) {
//        try {
//            String region = (String) request.get("region");
//            String saleGroup = (String) request.get("saleGroup");
//
//            // 获取地区的车系销售前十
//            List<CarRegionalSeriesSale> regionalTopTen = (region != null && !region.isEmpty())
//                    ? carRegionalSeriesSaleService.getProvinceTopTen(region)
//                    : carRegionalSeriesSaleService.getAllTopTen();
//
//            // 获取车系的购车目的和评分
//            Map<String, List<CarPurchasePurpose>> purchasePurposesMap = getPurchasePurposesBySeriesList(regionalTopTen);
//            Map<String, CarEvlIdx> evaluationDataMap = getCarEvaluationsBySeriesList(regionalTopTen);
//
//            // 构造车系详细信息
//            String seriesData = buildSeriesData(regionalTopTen, purchasePurposesMap, evaluationDataMap);
//
//            // 拼接完整的传递给AI的信息
//            String contentToSendToAI = "基于以下数据，请为 " + (region != null ? region : "全国") + " 地区的 "
//                    + (saleGroup != null ? saleGroup : "所有群体") + " 生成汽车销售策略：\n" + seriesData;
//
//            // 输出拼接的信息
//            return contentToSendToAI;
//
//        } catch (Exception e) {
//            return "生成信息时出错: " + e.getMessage();
//        }
//    }
//
//    /**
//     * 构造推荐车系信息输入流
//     * @param regionalTopTen
//     * @param purchasePurposesMap
//     * @param evaluationDataMap
//     * @return
//     */
//    private String buildSeriesData(List<CarRegionalSeriesSale> regionalTopTen,
//                                   Map<String, List<CarPurchasePurpose>> purchasePurposesMap,
//                                   Map<String, CarEvlIdx> evaluationDataMap) {
//        StringBuilder seriesData = new StringBuilder();
//
//        // 添加车系信息及详细数据
//        for (int i = 0; i < regionalTopTen.size(); i++) {
//            CarRegionalSeriesSale sale = regionalTopTen.get(i);
//
//            // 获取车系的详细信息
//            CarSeries carSeries = carSeriesService.getOne(new QueryWrapper<CarSeries>().eq("series", sale.getSeries()));
//
//            seriesData.append(i + 1).append(". ").append(sale.getSeries()).append("\n")
//                    .append("   - 销量: ").append(sale.getTotalSale()).append("\n");
//
//            // 添加车系的详细信息
//            if (carSeries != null) {
//                seriesData.append("   - 品牌: ").append(carSeries.getBrand()).append("\n")
//                        .append("   - 价格区间: ").append(carSeries.getPriceMin()).append(" - ").append(carSeries.getPriceMax()).append("\n")
//                        .append("   - 类型: ").append(carSeries.getType()).append("\n");
//            }
//
//            // 购车目的
//            seriesData.append("   - 购车目的: ");
//            List<CarPurchasePurpose> purposes = purchasePurposesMap.get(sale.getSeries());
//            if (purposes != null && !purposes.isEmpty()) {
//                for (CarPurchasePurpose purpose : purposes) {
//                    seriesData.append(purpose.getPurpose()).append(" (").append(purpose.getPercentage()).append("%), ");
//                }
//                seriesData.setLength(seriesData.length() - 2); // 删除最后一个逗号
//            } else {
//                seriesData.append("无购车目的数据");
//            }
//            seriesData.append("\n");
//
//            // 评分数据
//            CarEvlIdx eval = evaluationDataMap.get(sale.getSeries());
//            if (eval != null) {
//                seriesData.append("   - 综合评分: ").append(eval.getOverallRating()).append("\n")
//                        .append("     空间评分: ").append(eval.getSpace()).append("\n")
//                        .append("     驾驶感受评分: ").append(eval.getDriveFeel()).append("\n")
//                        .append("     能耗评分: ").append(eval.getPowerConsum()).append("\n")
//                        .append("     外观评分: ").append(eval.getOutDecor()).append("\n")
//                        .append("     内饰评分: ").append(eval.getInDecor()).append("\n")
//                        .append("     性价比评分: ").append(eval.getQpRatio()).append("\n")
//                        .append("     配置评分: ").append(eval.getConfigure()).append("\n");
//            } else {
//                seriesData.append("   - 无评分数据\n");
//            }
//
//            // 新增的百车故障数和故障原因数据
//            Long seriesId = carSeriesService.getSeriesIdByName(sale.getSeries());
//            if (seriesId != null) {
//                List<CarDefectReason> defectReasons = carDefectReasonService.getDefectReasonsBySeriesId(seriesId.toString());
//                if (defectReasons != null && !defectReasons.isEmpty()) {
//                    seriesData.append("   - 百车故障数: ").append(defectReasons.size()).append("\n")
//                            .append("   - 故障原因:\n");
//                    for (CarDefectReason reason : defectReasons) {
//                        seriesData.append("       - ").append(reason.getReason()).append(" (").append(reason.getCount()).append("次)\n");
//                    }
//                } else {
//                    seriesData.append("   - 无百车故障数和故障原因数据\n");
//                }
//            } else {
//                seriesData.append("   - 无百车故障数和故障原因数据\n");
//            }
//        }
//        return seriesData.toString();
//    }
//
//        /**
//     * 根据车系获取购车目的列表
//     * @param regionalTopTen
//     * @return
//     */
//    private Map<String, List<CarPurchasePurpose>> getPurchasePurposesBySeriesList(List<CarRegionalSeriesSale> regionalTopTen) {
//        return regionalTopTen.stream().collect(Collectors.toMap(
//                CarRegionalSeriesSale::getSeries,
//                series -> {
//                    Long seriesId = carSeriesService.getSeriesIdByName(series.getSeries());
//                    if (seriesId != null) {
//                        return carPurchasePurposeService.list(new QueryWrapper<CarPurchasePurpose>().eq("series_id", seriesId));
//                    }
//                    return Collections.emptyList();
//                }
//        ));
//    }
//
//    /**
//     * 根据车系列表获取评分列表
//     * @param regionalTopTen
//     * @return
//     */
//    private Map<String, CarEvlIdx> getCarEvaluationsBySeriesList(List<CarRegionalSeriesSale> regionalTopTen) {
//        return regionalTopTen.stream().collect(Collectors.toMap(
//                CarRegionalSeriesSale::getSeries,
//                series -> {
//                    Long seriesId = carSeriesService.getSeriesIdByName(series.getSeries());
//                    if (seriesId != null) {
//                        return carEvlIdxService.getOne(new QueryWrapper<CarEvlIdx>().eq("series_id", seriesId));
//                    }
//                    return null;
//                }
//        ));
//    }
}


