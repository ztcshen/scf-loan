package com.scf.loan.risk.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RiskCheckResponse {
    Integer score;
    RiskLevel riskLevel;
    String decision;
    List<String> reasons;
}
