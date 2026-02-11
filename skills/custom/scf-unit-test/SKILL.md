---
name: "scf-unit-test"
description: "Applies TDD for non-DTO changes in SCF Loan. Invoke for any feature, bugfix, refactor, or behavior change outside DTO/entity/enum/mapper."
---

# SCF Unit Test (TDD)

## Purpose

Enforce TDD for SCF Loan so every non-DTO change is backed by a test that failed first, then passed.

## When to Invoke

- New features
- Bug fixes
- Refactoring
- Behavior changes
- Any change outside DTO/entity/enum/mapper

## Rules

- DTO/entity/enum/mapper only changes can skip new tests
- Non-DTO changes must follow TDD and include unit tests
- No production code without a failing test first
- Always run tests for affected modules
- Report test commands and results

## Workflow (Red-Green-Refactor)

1. Identify changed files and classify them as DTO-only or non-DTO
2. Write a minimal failing test for the desired behavior
3. Run the test and confirm the failure is expected
4. Write minimal production code to pass
5. Run tests again and confirm all pass
6. Refactor while keeping tests green

## Test Commands

- Run module tests:
  - `mvn -pl <module> -am test`
- Examples:
  - `mvn -pl scf-loan-common -am test`
  - `mvn -pl scf-loan-biz -am test`
  - `mvn -pl scf-loan-web -am test`

## Output Checklist

- Which files changed and their classification
- Which tests were added or updated
- Test command(s) executed and results
- Confirmation that tests failed first for new behavior
