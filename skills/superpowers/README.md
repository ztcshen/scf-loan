# Superpowers Skills

This directory contains meta-skills from the [Superpowers](https://github.com/obra/superpowers) project. These skills are designed to enhance the agent's software development capabilities, enforcing best practices and systematic workflows.

## Skills Overview

| Skill Name | Description | Source |
|------------|-------------|--------|
| `brainstorming` | A collaborative design skill that helps refine ideas into detailed specifications before coding. | [Superpowers](https://github.com/obra/superpowers/tree/main/skills/brainstorming) |
| `systematic-debugging` | A rigorous 4-phase debugging process (Root Cause Investigation -> Reproduction -> Fix -> Verification) to solve complex issues. | [Superpowers](https://github.com/obra/superpowers/tree/main/skills/systematic-debugging) |
| `writing-plans` | Converts specs into bite-sized (2-5 min) implementation tasks with exact file paths and test commands. | [Superpowers](https://github.com/obra/superpowers/tree/main/skills/writing-plans) |
| `executing-plans` | Automates the execution of written plans in batches, with regular reporting and checkpoints. | [Superpowers](https://github.com/obra/superpowers/tree/main/skills/executing-plans) |
| `test-driven-development` | Enforces the Red-Green-Refactor cycle: write failing test -> minimal code to pass -> refactor. | [Superpowers](https://github.com/obra/superpowers/tree/main/skills/test-driven-development) |
| `requesting-code-review` | Simulates a senior engineer review, checking for architecture, style, and potential bugs before merging. | [Superpowers](https://github.com/obra/superpowers/tree/main/skills/requesting-code-review) |
| `finishing-a-development-branch` | Guides the final verification and merge process (Merge/PR/Cleanup) to ensure clean integration. | [Superpowers](https://github.com/obra/superpowers/tree/main/skills/finishing-a-development-branch) |

## Usage Guidelines

- **Brainstorming**: Invoke when starting a new feature or module. It will guide you through a Socratic dialogue to uncover edge cases and design decisions.
- **Writing Plans**: Use after design is approved. It breaks down the work into a precise "todo list" for the agent (or you) to execute.
- **Executing Plans**: The engine that drives the development. It reads the plan and executes it task-by-task.
- **Test-Driven Development (TDD)**: The default mode for writing code. Always start with a test.
- **Requesting Code Review**: Invoke periodically (e.g., after every 3 tasks or a major feature) to get a quality check.
- **Systematic Debugging**: Invoke when encountering a difficult bug or test failure. It enforces a "no fix without root cause" policy.
- **Finishing Branch**: Invoke when development is done to safely merge and clean up.

## Maintenance

These skills are synced from the upstream Superpowers repository. Avoid modifying them directly unless necessary for project compatibility.
