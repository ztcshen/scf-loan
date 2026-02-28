package com.scf.loan.risk.service;

import com.scf.loan.risk.model.RiskCheckRequest;
import com.scf.loan.risk.model.RiskCheckResponse;
import com.scf.loan.risk.model.RiskLevel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RiskEvaluationServiceTest {

    private final RiskEvaluationService service = new RiskEvaluationService();

    @Test
    void shouldReturnLowRiskForHealthyEnterprise() {
        RiskCheckRequest request = baseRequest();
        request.setLoanAmount(new BigDecimal("400000"));
        request.setInvoiceAmount(new BigDecimal("1000000"));
        request.setOverdueDays(0);
        request.setHistoricalDefaultCount(0);
        request.setCoreEnterpriseConfirmed(Boolean.TRUE);

        RiskCheckResponse response = service.evaluate(request);

        assertEquals(RiskLevel.LOW, response.getRiskLevel());
        assertEquals("自动通过", response.getDecision());
        assertEquals(100, response.getScore());
    }

    @Test
    void shouldRejectWhenMultipleHighRiskRulesTriggered() {
        RiskCheckRequest request = baseRequest();
        request.setLoanAmount(new BigDecimal("950000"));
        request.setInvoiceAmount(new BigDecimal("1000000"));
        request.setOverdueDays(90);
        request.setHistoricalDefaultCount(2);
        request.setCoreEnterpriseConfirmed(Boolean.FALSE);

        RiskCheckResponse response = service.evaluate(request);

        assertEquals(RiskLevel.REJECT, response.getRiskLevel());
        assertEquals("拒绝", response.getDecision());
        assertEquals(0, response.getScore());
    }

    private RiskCheckRequest baseRequest() {
        RiskCheckRequest request = new RiskCheckRequest();
        request.setEnterpriseName("测试企业");
        return request;
    }
}
