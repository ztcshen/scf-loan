package com.scf.loan.bill.plan.strategy;

import com.scf.loan.bill.plan.RepayPlanRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

public abstract class BaseRepayPlanStrategy {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    protected void validate(RepayPlanRequest request) {
        if (request == null) {
            throw new ConstraintViolationException("参数不合法", Collections.emptySet());
        }
        Set<ConstraintViolation<RepayPlanRequest>> violations = VALIDATOR.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
