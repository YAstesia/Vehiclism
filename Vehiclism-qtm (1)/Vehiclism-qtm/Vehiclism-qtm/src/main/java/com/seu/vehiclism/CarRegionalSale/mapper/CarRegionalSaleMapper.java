package com.seu.vehiclism.CarRegionalSale.mapper;

import com.seu.vehiclism.CarRegionalSale.entity.CarRegionalSale;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.IntegerTypeHandler;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author QTM
 * @since 2024-09-03
 */
@Mapper
public interface CarRegionalSaleMapper extends BaseMapper<CarRegionalSale> {

    /**
     * 获取排名前十的省份
     *
     * @return 排名前十的省份列表
     */
    @Select("SELECT province, SUM(total_sale) AS total_sale, SUM(percentage) AS percentage "
            + "FROM car_regional_sale "
            + "GROUP BY province "
            + "ORDER BY total_sale DESC "
            + "LIMIT 10")
    @Results({
            @Result(property = "province", column = "province"),
            @Result(property = "total_sale", column = "total_sale", typeHandler = IntegerTypeHandler.class),
            @Result(property = "percentage", column = "percentage")
    })
    List<Map<String, Object>> getTopTenProvinces();
    @Select("SELECT province, SUM(total_sale) AS total_sale, SUM(percentage) AS percentage "
            + "FROM car_regional_sale "
            + "GROUP BY province "
            + "ORDER BY total_sale DESC ")
    @Results({
            @Result(property = "province", column = "province"),
            @Result(property = "total_sale", column = "total_sale", typeHandler = IntegerTypeHandler.class),
            @Result(property = "percentage", column = "percentage")
    })
    List<Map<String, Object>> getProvinces();
    @Select("SELECT  region "
            + "FROM car_regional_sale "
            + "WHERE province=#{province} "
            + "ORDER BY total_sale DESC ")
//    @Results({
//            @Result(property = "province", column = "province"),
//            @Result(property = "total_sale", column = "total_sale", typeHandler = IntegerTypeHandler.class),
//            @Result(property = "percentage", column = "percentage")
//    })
    List<String> getCitiesFromProvince(String province);
    @Select("SELECT region FROM car_regional_sale "
            + "ORDER BY total_sale DESC ")
    List<String> getCities();
}
