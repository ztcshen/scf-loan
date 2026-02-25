package com.scf.loan.bill.plan;

import com.scf.loan.bill.plan.enums.RepayMethod;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class RepayPlanRequest {
    @NotNull
    private RepayMethod repayMethod;
    @NotNull
    @Positive
    private Long principal;
    @NotNull
    @Positive
    private Long dailyRate;
    @NotNull
    @Positive
    private Long penaltyDailyRate;
    @NotNull
    private LocalDate loanDate;
    @NotNull
    @Positive
    private Integer periodDays;
    @NotNull
    @Positive
    private Integer periodCount;
    private String interestType;
    private String periodUnit;
    private String dayCountConvention;
    private String rateType;
    private String graceType;
    private String settlementMode;
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
