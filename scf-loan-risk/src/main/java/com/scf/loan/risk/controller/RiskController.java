package com.scf.loan.risk.controller;

import com.scf.loan.risk.model.RiskCheckRequest;
import com.scf.loan.risk.model.RiskCheckResponse;
import com.scf.loan.risk.service.RiskEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
public class RiskController {

    private final RiskEvaluationService riskEvaluationService;

    @PostMapping("/evaluate")
    public RiskCheckResponse evaluate(@Valid @RequestBody RiskCheckRequest request) {
        return riskEvaluationService.evaluate(request);
    }
}
