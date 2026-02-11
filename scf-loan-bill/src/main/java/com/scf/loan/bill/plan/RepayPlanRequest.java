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
    private List<ChargeRate> chargeRates;
}
