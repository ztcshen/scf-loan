package com.scf.loan.bill.controller;

import com.scf.loan.bill.plan.RepayPlanRequest;
import com.scf.loan.bill.plan.RepayRequest;
import com.scf.loan.bill.plan.RepayResult;
import com.scf.loan.bill.plan.RepayTrialRequest;
import com.scf.loan.bill.plan.RepayTrialResult;
import com.scf.loan.bill.service.RepayPlanService;
import com.scf.loan.bill.service.RepayService;
import com.scf.loan.bill.service.RepayTrialService;
import com.scf.loan.common.dto.RepayPlanItem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repay")
public class RepayController {

    @Autowired
    private RepayPlanService repayPlanService;

    @Autowired
    private RepayTrialService repayTrialService;

    @Autowired
    private RepayService repayService;

    @PostMapping("/plan")
    public List<RepayPlanItem> generatePlan(@Valid @RequestBody RepayPlanRequest request) {
        return repayPlanService.generatePlan(request);
    }

    @PostMapping("/trial")
    public RepayTrialResult trial(@Valid @RequestBody RepayTrialRequest request) {
        return repayTrialService.trial(request);
    }

    @PostMapping("/repay")
    public RepayResult repay(@Valid @RequestBody RepayRequest request) {
        return repayService.repay(request);
    }
}
