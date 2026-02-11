package com.scf.loan.biz.service.impl;

import com.scf.loan.common.dto.RepayPlanItem;
import com.scf.loan.common.dto.RepayPlanSubjectDetail;
import com.scf.loan.common.enums.ChargeSubject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RepayPlanServiceImplTest {

    @Test
    public void testGenerateEqualPrincipalPlan() {
        RepayPlanServiceImpl service = new RepayPlanServiceImpl();
        long principal = 100L;
        long dailyRate = 1_0000_0000L;
        LocalDate loanDate = LocalDate.of(2026, 2, 1);

        List<RepayPlanItem> items = service.generateEqualPrincipalPlan(
                principal,
                dailyRate,
                loanDate,
                1,
                2
        );

        assertEquals(2, items.size());

        RepayPlanItem first = items.get(0);
        assertEquals(1, first.getPeriod());
        assertEquals(LocalDate.of(2026, 2, 1), first.getStartDate());
        assertEquals(LocalDate.of(2026, 2, 2), first.getDueDate());
        assertEquals(50L, getAmount(first, ChargeSubject.PRINCIPAL));
        assertEquals(100L, getAmount(first, ChargeSubject.INTEREST));

        RepayPlanItem second = items.get(1);
        assertEquals(2, second.getPeriod());
        assertEquals(LocalDate.of(2026, 2, 2), second.getStartDate());
        assertEquals(LocalDate.of(2026, 2, 3), second.getDueDate());
        assertEquals(50L, getAmount(second, ChargeSubject.PRINCIPAL));
        assertEquals(50L, getAmount(second, ChargeSubject.INTEREST));
    }

    @Test
    public void testGenerateEqualPrincipalPlanWithInvalidParams() {
        RepayPlanServiceImpl service = new RepayPlanServiceImpl();
        LocalDate loanDate = LocalDate.of(2026, 2, 1);
        assertThrows(IllegalArgumentException.class, () -> service.generateEqualPrincipalPlan(0L, 1L, loanDate, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> service.generateEqualPrincipalPlan(1L, 0L, loanDate, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> service.generateEqualPrincipalPlan(1L, 1L, null, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> service.generateEqualPrincipalPlan(1L, 1L, loanDate, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> service.generateEqualPrincipalPlan(1L, 1L, loanDate, 1, 0));
    }

    private long getAmount(RepayPlanItem item, ChargeSubject subject) {
        for (RepayPlanSubjectDetail detail : item.getAmountDetail()) {
            if (detail != null && subject == detail.getSubject()) {
                return detail.getAmount();
            }
        }
        return 0L;
    }
}
