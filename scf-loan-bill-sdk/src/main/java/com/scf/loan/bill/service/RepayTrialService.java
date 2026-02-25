package com.scf.loan.bill.service;

import com.scf.loan.bill.plan.RepayTrialRequest;
import com.scf.loan.bill.plan.RepayTrialResult;

public interface RepayTrialService {
    RepayTrialResult trial(RepayTrialRequest request);
}
