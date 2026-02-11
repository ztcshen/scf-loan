package com.scf.loan.common.utils.scf;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScfInterestUtilsTest {

    @Test
    public void testCalculateInterestDays() {
        LocalDate startDate = LocalDate.of(2025, 10, 18);
        assertEquals(1, ScfInterestUtils.calculateInterestDays(startDate, LocalDate.of(2025, 10, 18)));
        assertEquals(1, ScfInterestUtils.calculateInterestDays(startDate, LocalDate.of(2025, 10, 19)));
        assertEquals(2, ScfInterestUtils.calculateInterestDays(startDate, LocalDate.of(2025, 10, 20)));
        assertEquals(3, ScfInterestUtils.calculateInterestDays(startDate, LocalDate.of(2025, 10, 21)));
    }

    @Test
    public void testCalculateInterestDaysWithRepayDateList() {
        LocalDate startDate = LocalDate.of(2025, 10, 18);
        List<LocalDate> repayDateList = Arrays.asList(
                LocalDate.of(2025, 10, 20),
                LocalDate.of(2025, 10, 25)
        );

        assertEquals(1, ScfInterestUtils.calculateInterestDays(startDate, repayDateList, LocalDate.of(2025, 10, 19)));
        assertEquals(0, ScfInterestUtils.calculateInterestDays(startDate, repayDateList, LocalDate.of(2025, 10, 20)));
        assertEquals(1, ScfInterestUtils.calculateInterestDays(startDate, repayDateList, LocalDate.of(2025, 10, 21)));
        assertEquals(2, ScfInterestUtils.calculateInterestDays(startDate, repayDateList, LocalDate.of(2025, 10, 22)));
        assertEquals(1, ScfInterestUtils.calculateInterestDays(startDate, repayDateList, LocalDate.of(2025, 10, 26)));
    }

    @Test
    public void testCalculateInterestDaysWithEmptyRepayDateList() {
        LocalDate startDate = LocalDate.of(2025, 10, 18);
        assertEquals(2, ScfInterestUtils.calculateInterestDays(startDate, Collections.emptyList(), LocalDate.of(2025, 10, 20)));
    }

    @Test
    public void testCalculateInterestDaysWithNullParams() {
        LocalDate startDate = LocalDate.of(2025, 10, 18);
        assertThrows(IllegalArgumentException.class, () -> ScfInterestUtils.calculateInterestDays(null, startDate));
        assertThrows(IllegalArgumentException.class, () -> ScfInterestUtils.calculateInterestDays(startDate, (LocalDate) null));
        assertThrows(IllegalArgumentException.class, () -> ScfInterestUtils.calculateInterestDays(null, Collections.emptyList(), startDate));
        assertThrows(IllegalArgumentException.class, () -> ScfInterestUtils.calculateInterestDays(startDate, Collections.emptyList(), null));
    }

    @Test
    public void testCalculatePenaltyDays() {
        LocalDate dueDate = LocalDate.of(2025, 10, 20);
        assertEquals(0, ScfInterestUtils.calculatePenaltyDays(dueDate, LocalDate.of(2025, 10, 20)));
        assertEquals(0, ScfInterestUtils.calculatePenaltyDays(dueDate, LocalDate.of(2025, 10, 19)));
        assertEquals(1, ScfInterestUtils.calculatePenaltyDays(dueDate, LocalDate.of(2025, 10, 21)));
        assertEquals(3, ScfInterestUtils.calculatePenaltyDays(dueDate, LocalDate.of(2025, 10, 23)));
    }

    @Test
    public void testCalculatePenaltyDaysWithRepayDateList() {
        LocalDate dueDate = LocalDate.of(2025, 10, 20);
        List<LocalDate> repayDateList = Arrays.asList(
                LocalDate.of(2025, 10, 22),
                LocalDate.of(2025, 10, 25)
        );

        assertEquals(1, ScfInterestUtils.calculatePenaltyDays(dueDate, repayDateList, LocalDate.of(2025, 10, 21)));
        assertEquals(0, ScfInterestUtils.calculatePenaltyDays(dueDate, repayDateList, LocalDate.of(2025, 10, 22)));
        assertEquals(1, ScfInterestUtils.calculatePenaltyDays(dueDate, repayDateList, LocalDate.of(2025, 10, 23)));
        assertEquals(1, ScfInterestUtils.calculatePenaltyDays(dueDate, repayDateList, LocalDate.of(2025, 10, 26)));
    }

    @Test
    public void testCalculatePenaltyDaysWithRepayDateListDueDateAfterLatestRepayDate() {
        LocalDate dueDate = LocalDate.of(2025, 10, 24);
        List<LocalDate> repayDateList = Arrays.asList(
                LocalDate.of(2025, 10, 22),
                LocalDate.of(2025, 10, 23)
        );

        assertEquals(2, ScfInterestUtils.calculatePenaltyDays(dueDate, repayDateList, LocalDate.of(2025, 10, 26)));
    }

    @Test
    public void testCalculatePenaltyDaysWithEmptyRepayDateList() {
        LocalDate dueDate = LocalDate.of(2025, 10, 20);
        assertEquals(2, ScfInterestUtils.calculatePenaltyDays(dueDate, Collections.emptyList(), LocalDate.of(2025, 10, 22)));
    }

    @Test
    public void testCalculatePenaltyDaysWithNullParams() {
        LocalDate dueDate = LocalDate.of(2025, 10, 20);
        assertThrows(IllegalArgumentException.class, () -> ScfInterestUtils.calculatePenaltyDays(null, Collections.emptyList(), dueDate));
        assertThrows(IllegalArgumentException.class, () -> ScfInterestUtils.calculatePenaltyDays(dueDate, Collections.emptyList(), null));
    }

    @Test
    public void testCalculateInterest() {
        assertEquals(0L, ScfInterestUtils.calculateInterest(null, 100L, 1L));
        assertEquals(0L, ScfInterestUtils.calculateInterest(100L, null, 1L));
        assertEquals(0L, ScfInterestUtils.calculateInterest(100L, 100L, 0L));
        assertEquals(100L, ScfInterestUtils.calculateInterest(100L, 1_0000_0000L, 1L));
    }

    @Test
    public void testConvertRateToBigDecimal() {
        BigDecimal expected = new BigDecimal("0.000000010000");
        BigDecimal actual = ScfInterestUtils.convertRateToBigDecimal(1L);
        assertEquals(0, actual.compareTo(expected));
        assertEquals(0, ScfInterestUtils.convertRateToBigDecimal(null).compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testCalculateUnpaidInterest() {
        BigDecimal rate = new BigDecimal("0.01");
        assertEquals(0L, ScfInterestUtils.calculateUnpaidInterest(null, rate, 1));
        assertEquals(0L, ScfInterestUtils.calculateUnpaidInterest(100L, rate, 0));
        assertEquals(1L, ScfInterestUtils.calculateUnpaidInterest(100L, rate, 1));
    }
}
