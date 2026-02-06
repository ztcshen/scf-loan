---
name: state-machine-generator
description: Generates Java state machine code (Enums, Events, Transitions) based on user input. Invoke when user wants to implement a state machine or workflow logic.
---

# State Machine Generator

This skill generates standard Java code for managing state transitions. It supports both simple Enum-based state machines and Spring State Machine configurations.

## Features
- Generates `State` and `Event` enums
- Generates a `StateMachine` interface and implementation
- Generates visualization (PlantUML) for the state machine

## Usage

1. **Define the State Machine**:
   Provide the states and transitions.
   Example:
   ```
   States: APPLYING, ACCEPT, REFUSED, LOANING, REPAYING, CLEARED, OVERDUE
   Transitions:
   - APPLYING -> ACCEPT (Event: APPROVE)
   - APPLYING -> REFUSED (Event: REJECT)
   - ACCEPT -> LOANING (Event: LOAN_START)
   - LOANING -> REPAYING (Event: LOAN_SUCCESS)
   ```

2. **Generate Code**:
   Run the generator script.

   ```bash
   python skills/custom/state-machine-generator/generate_sm.py --name OrderStateMachine --package com.scf.loan.biz.statemachine
   ```

## Configuration
The generator uses Jinja2 templates located in the `templates/` directory. You can customize them to fit your project's coding style.
