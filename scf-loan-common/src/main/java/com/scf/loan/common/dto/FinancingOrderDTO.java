package com.scf.loan.common.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 融资订单DTO
 */
@Data
public class FinancingOrderDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 会员号
     */
    private String uid;

    /**
     * 环境标识
     */
    private String env;

    /**
     * 融资订单号
     */
    private String financingOrderId;

    /**
     * 授信协议类型
     */
    private String creditContractType;

    /**
     * 成员ID
     */
    private String creditContractId;

    /**
     * 商户订单号
     */
    private String merTransNo;

    /**
     * 供应链金融协议号
     */
    private String contractNo;

    /**
     * 审批状态 审批中(applying)，审批失败(refused)，审批成功(accept)
     */
    private String applyStatus;

    /**
     * 放款申请中(loaning)，放款申请失败(loanFailed)，放款申请成功(loanSuccess)
     */
    private String loanConfirmStatus;

    /**
     * 还款状态: 还款中(repaying),  逾期中(overDue), 已结清(clearUp)
     */
    private String repayStatus;

    /**
     * 放款时间
     */
    private LocalDateTime loanTime;

    /**
     * 结清时间
     */
    private LocalDateTime clearUpTime;

    /**
     * 放款金额
     */
    private Long loanAmount;

    /**
     * 订单状态最近一次流转时间, 可根据该时间进行取数和告警
     */
    private LocalDateTime stateChangeTime;

    /**
     * 扩展信息, 可以用来暂存最新的还款计划
     */
    private String extInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 删除标志 0:有效 1:无效
     */
    private Integer delFlag;

    /**
     * 乐观锁版本号, 更新时需复核版本号
     */
    private Integer revision;

    /**
     * 当期开始日期 yyyy-MM-dd
     */
    private LocalDate startDate;

    /**
     * 当期到期日期 yyyy-MM-dd
     */
    private LocalDate dueDate;
}
