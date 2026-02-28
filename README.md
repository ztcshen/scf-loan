# SCF Loan Project

## Overview
SCF (Supply Chain Finance) Loan is a core financing service responsible for managing the lifecycle of loan orders, from application to repayment.

## Modules
- **scf-loan-web**: Web entry point (Controller).
- **scf-loan-service**: Business logic interface.
- **scf-loan-biz**: Business logic implementation.
- **scf-loan-dal**: Data access layer (MyBatis Plus).
- **scf-loan-common**: Shared DTOs, Enums, and Utils.
- **scf-loan-job**: Scheduled tasks.
- **scf-loan-risk**: 风控项目（规则引擎 + 风险评估 API）。

## Development Workflow
This project uses **Agent Skills** to enhance development efficiency:
- **`skills/custom`**: Project-specific tools (e.g., `db-generate`).
- **`skills/superpowers`**: Advanced workflows (`brainstorming`, `writing-plans`, `TDD`).
- **`skills/anthropics`**: General-purpose capabilities (`doc-coauthoring`).

## Documentation
- [Technical Design](docs/design/order/financing-order-service-design.md)
