package com.scf.loan.bill.plan;

import com.scf.loan.bill.plan.enums.InterestType;
import com.scf.loan.bill.plan.enums.PeriodUnit;
import com.scf.loan.bill.plan.enums.RepayMethod;

import com.scf.loan.common.dto.RepayPlanItem;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RepayPlanStrategyRouterTest {

    @Test
    public void testRouteFallbackToRepayMethodOnly() {
        RepayPlanStrategyRouter router = new RepayPlanStrategyRouter(Collections.singletonList(
                new StubStrategy(buildKey(RepayMethod.EQUAL_PRINCIPAL, null, null))
        ));
        RepayPlanStrategyKey key = buildKey(RepayMethod.EQUAL_PRINCIPAL, InterestType.DAILY.getCode(), PeriodUnit.DAY.getCode());
        RepayPlanStrategy strategy = router.route(key);
        assertEquals(RepayMethod.EQUAL_PRINCIPAL, strategy.key().getRepayMethod());
    }

    @Test
    public void testRouteThrowsWhenStrategyMissing() {
        RepayPlanStrategyRouter router = new RepayPlanStrategyRouter(Collections.singletonList(
                new StubStrategy(buildKey(RepayMethod.EQUAL_PRINCIPAL, null, null))
        ));
        RepayPlanStrategyKey key = buildKey(RepayMethod.EQUAL_PRINCIPAL_INTEREST, null, null);
        assertThrows(IllegalArgumentException.class, () -> router.route(key));
    }

    @Test
    public void testDuplicateStrategyKeyThrows() {
        RepayPlanStrategyKey key = buildKey(RepayMethod.EQUAL_PRINCIPAL, null, null);
        List<RepayPlanStrategy> strategies = Arrays.asList(
                new StubStrategy(key),
                new StubStrategy(key)
        );
        assertThrows(IllegalStateException.class, () -> new RepayPlanStrategyRouter(strategies));
    }

    private RepayPlanStrategyKey buildKey(RepayMethod repayMethod, String interestType, String periodUnit) {
        RepayPlanStrategyKey key = new RepayPlanStrategyKey();
        key.setRepayMethod(repayMethod);
        key.setInterestType(interestType);
        key.setPeriodUnit(periodUnit);
        return key;
    }

    private static class StubStrategy implements RepayPlanStrategy {
        private final RepayPlanStrategyKey key;

        private StubStrategy(RepayPlanStrategyKey key) {
            this.key = key;
        }

        @Override
        public RepayPlanStrategyKey key() {
            return key;
        }

        @Override
        public List<RepayPlanItem> generate(RepayPlanRequest request) {
            return Collections.emptyList();
        }
    }
}
