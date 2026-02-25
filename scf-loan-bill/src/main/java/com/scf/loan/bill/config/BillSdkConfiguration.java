package com.scf.loan.bill.config;

import com.scf.loan.bill.plan.RepayPlanStrategy;
import com.scf.loan.bill.plan.RepayPlanStrategyRouter;
import com.scf.loan.bill.plan.strategy.EqualPrincipalInterestStrategy;
import com.scf.loan.bill.plan.strategy.EqualPrincipalStrategy;
import com.scf.loan.bill.plan.strategy.InterestFirstPrincipalLastStrategy;
import com.scf.loan.bill.service.RepayPlanService;
import com.scf.loan.bill.service.RepayService;
import com.scf.loan.bill.service.RepayTrialService;
import com.scf.loan.bill.service.impl.RepayPlanServiceImpl;
import com.scf.loan.bill.service.impl.RepayServiceImpl;
import com.scf.loan.bill.service.impl.RepayTrialServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BillSdkConfiguration {
    @Bean
    public RepayPlanStrategy equalPrincipalStrategy() {
        return new EqualPrincipalStrategy();
    }

    @Bean
    public RepayPlanStrategy equalPrincipalInterestStrategy() {
        return new EqualPrincipalInterestStrategy();
    }

    @Bean
    public RepayPlanStrategy interestFirstPrincipalLastStrategy() {
        return new InterestFirstPrincipalLastStrategy();
    }

    @Bean
    public RepayPlanStrategyRouter repayPlanStrategyRouter(List<RepayPlanStrategy> strategies) {
        return new RepayPlanStrategyRouter(strategies);
    }

    @Bean
    public RepayPlanService repayPlanService(RepayPlanStrategyRouter router) {
        return new RepayPlanServiceImpl(router);
    }

    @Bean
    public RepayTrialService repayTrialService(RepayPlanService repayPlanService) {
        return new RepayTrialServiceImpl(repayPlanService);
    }

    @Bean
    public RepayService repayService(RepayTrialService repayTrialService) {
        return new RepayServiceImpl(repayTrialService);
    }
}
