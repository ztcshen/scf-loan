package com.scf.loan.risk.service;

import com.scf.loan.risk.model.RiskCheckRequest;
import com.scf.loan.risk.model.RiskCheckResponse;
import com.scf.loan.risk.model.RiskLevel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiskEvaluationService {

    public RiskCheckResponse evaluate(RiskCheckRequest request) {
        int score = 100;
        List<String> reasons = new ArrayList<>();

        BigDecimal financingRatio = request.getLoanAmount()
            .divide(request.getInvoiceAmount(), 4, RoundingMode.HALF_UP);

        if (financingRatio.compareTo(new BigDecimal("0.8")) > 0) {
            score -= 25;
            reasons.add("融资金额占发票比例过高");
        } else if (financingRatio.compareTo(new BigDecimal("0.6")) > 0) {
            score -= 10;
            reasons.add("融资金额占发票比例偏高");
        }

        if (request.getOverdueDays() != null && request.getOverdueDays() > 30) {
            score -= 30;
            reasons.add("近12个月存在30天以上逾期");
        } else if (request.getOverdueDays() != null && request.getOverdueDays() > 0) {
            score -= 12;
            reasons.add("近12个月存在短期逾期");
        }

        if (request.getHistoricalDefaultCount() > 0) {
            score -= 20 + request.getHistoricalDefaultCount() * 5;
            reasons.add("存在历史违约记录");
        }

        if (!request.getCoreEnterpriseConfirmed()) {
            score -= 15;
            reasons.add("核心企业未确权");
        }

        if (score < 0) {
            score = 0;
        }

        RiskLevel level = resolveLevel(score);
        String decision = resolveDecision(level);

        if (reasons.isEmpty()) {
            reasons.add("未触发显著风险规则");
        }

        return RiskCheckResponse.builder()
            .score(score)
            .riskLevel(level)
            .decision(decision)
            .reasons(reasons)
            .build();
    }

    private RiskLevel resolveLevel(int score) {
        if (score < 40) {
            return RiskLevel.REJECT;
        }
        if (score < 60) {
            return RiskLevel.HIGH;
        }
        if (score < 80) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.LOW;
    }

    private String resolveDecision(RiskLevel riskLevel) {
        switch (riskLevel) {
            case REJECT:
                return "拒绝";
            case HIGH:
                return "人工复核";
            case MEDIUM:
                return "谨慎通过";
            default:
                return "自动通过";
        }
    }
}
