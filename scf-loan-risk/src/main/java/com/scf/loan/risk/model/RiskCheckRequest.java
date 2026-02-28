package com.scf.loan.risk.model;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RiskCheckRequest {

    @NotBlank(message = "企业名称不能为空")
    private String enterpriseName;

    @NotNull(message = "融资金额不能为空")
    @DecimalMin(value = "0.01", message = "融资金额必须大于0")
    private BigDecimal loanAmount;

    @NotNull(message = "发票金额不能为空")
    @DecimalMin(value = "0.01", message = "发票金额必须大于0")
    private BigDecimal invoiceAmount;

    @Min(value = 0, message = "逾期天数不能小于0")
    @Max(value = 365, message = "逾期天数过大")
    private Integer overdueDays;

    @NotNull(message = "历史违约次数不能为空")
    @Min(value = 0, message = "历史违约次数不能小于0")
    private Integer historicalDefaultCount;

    @NotNull(message = "核心企业确权状态不能为空")
    private Boolean coreEnterpriseConfirmed;
}
