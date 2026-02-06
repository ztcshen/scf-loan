# Custom Skills

This directory contains skills that are specifically developed and customized for the SCF Loan Project. These skills encapsulate the project's unique business logic, development standards, and workflows.

## Skills Overview

| Skill Name | Description | Trigger |
|------------|-------------|---------|
| `db-generate` | Generates standard Java code (Entity, Mapper, Service, DTO) from database tables. | "Generate code for table X", "Scaffold entity" |
| `db-remove` | Safely removes generated code files for specific tables. | "Clean up code for table X", "Delete scaffolded code" |
| `git-commit` | Automates the Git commit process with tests, build validation, and standardized messages. | "Commit changes", "Save code" |
| `orchestrator` | Manages complex workflows and service coordination. | (Internal use / Workflow triggers) |
| `service-runner` | Helper to run and debug services locally. | "Run service X", "Debug module Y" |

## Development Guidelines

- **Project Specific**: These skills should rely on project-specific configurations (e.g., package names `com.scf.loan`, database schemas).
- **Maintenance**: Maintained by the SCF Loan development team.
- **Modification**: Feel free to modify these skills to adapt to changing business requirements.
