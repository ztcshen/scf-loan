package com.scf.loan.bill.plan;

import com.scf.loan.bill.plan.enums.RepayMethod;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class RepayPlanRequest {
    /**
     * 还款方式枚举，用于策略路由的主键字段。
     */
    @NotNull
    private RepayMethod repayMethod;
    /**
     * 放款本金，单位：分。
     */
    @NotNull
    @Positive
    private Long principal;
    /**
     * 日利率，单位：亿分比。
     */
    @NotNull
    @Positive
    private Long dailyRate;
    /**
     * 罚息日利率，单位：亿分比。
     */
    @NotNull
    @Positive
    private Long penaltyDailyRate;
    /**
     * 放款日期，作为首期起息日。
     */
    @NotNull
    private LocalDate loanDate;
    /**
     * 每期天数，配合 periodCount 计算还款区间。
     */
    @NotNull
    @Positive
    private Integer periodDays;
    /**
     * 总期数。
     */
    @NotNull
    @Positive
    private Integer periodCount;
    /**
     * 计息方式扩展字段，用于更细粒度策略路由。
     */
    private String interestType;
    /**
     * 期单位（天/月等）扩展字段，用于更细粒度策略路由。
     */
    private String periodUnit;
    /**
     * 计息天数约定扩展字段，用于更细粒度策略路由。
     */
    private String dayCountConvention;
    /**
     * 利率类型扩展字段，用于更细粒度策略路由。
     */
    private String rateType;
    /**
     * 宽限规则扩展字段，用于更细粒度策略路由。
     */
    private String graceType;
    /**
     * 结算方式扩展字段，用于更细粒度策略路由。
     */
    private String settlementMode;
    /**
     * 费用/利率配置列表。
     * <p>
     * 用于支持多科目的灵活费率配置（如利息、罚息、手续费等），包含科目类型、费率单位及数值。
     */
    private List<ChargeRate> chargeRates;

    public RepayMethod getRepayMethod() {
        return repayMethod;
    }

    public void setRepayMethod(RepayMethod repayMethod) {
        this.repayMethod = repayMethod;
    }

    public Long getPrincipal() {
        return principal;
    }

    public void setPrincipal(Long principal) {
        this.principal = principal;
    }

    public Long getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Long dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Long getPenaltyDailyRate() {
        return penaltyDailyRate;
    }

    public void setPenaltyDailyRate(Long penaltyDailyRate) {
        this.penaltyDailyRate = penaltyDailyRate;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public Integer getPeriodDays() {
        return periodDays;
    }

    public void setPeriodDays(Integer periodDays) {
        this.periodDays = periodDays;
    }

    public Integer getPeriodCount() {
        return periodCount;
    }

    public void setPeriodCount(Integer periodCount) {
        this.periodCount = periodCount;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public String getDayCountConvention() {
        return dayCountConvention;
    }

    public void setDayCountConvention(String dayCountConvention) {
        this.dayCountConvention = dayCountConvention;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getGraceType() {
        return graceType;
    }

    public void setGraceType(String graceType) {
        this.graceType = graceType;
    }

    public String getSettlementMode() {
        return settlementMode;
    }

    public void setSettlementMode(String settlementMode) {
        this.settlementMode = settlementMode;
    }

    public List<ChargeRate> getChargeRates() {
        return chargeRates;
    }

    public void setChargeRates(List<ChargeRate> chargeRates) {
        this.chargeRates = chargeRates;
    }
}
