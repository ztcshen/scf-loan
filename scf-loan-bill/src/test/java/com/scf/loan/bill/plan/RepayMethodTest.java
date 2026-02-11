package com.scf.loan.bill.plan;

import com.scf.loan.bill.plan.enums.RepayMethod;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepayMethodTest {

    @Test
    public void testRepayMethodEnumValues() {
        RepayMethod[] values = RepayMethod.values();
        assertEquals(3, values.length);
        assertEquals(RepayMethod.EQUAL_PRINCIPAL, RepayMethod.valueOf("EQUAL_PRINCIPAL"));
    }
}
