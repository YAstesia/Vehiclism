package com.seu.vehiclism.CarRegionalSale;

import java.math.BigDecimal;

public class ProvinceSale {
    private String province;
    private Integer totalSale;
    private BigDecimal percentage;
    private int ranking;

    public ProvinceSale(String p, int i, BigDecimal zero) {
        setProvince(p);
        setTotalSale(i);
        setPercentage(zero);
    }
    public ProvinceSale() {
    }

    // Getters and Setters
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Integer totalSale) {
        this.totalSale = totalSale;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}
