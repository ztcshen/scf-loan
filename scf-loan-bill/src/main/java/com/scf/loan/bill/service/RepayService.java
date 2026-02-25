package com.scf.loan.bill.service;

import com.scf.loan.bill.plan.RepayRequest;
import com.scf.loan.bill.plan.RepayResult;

public interface RepayService {
    RepayResult repay(RepayRequest request);
}
