---
name: db-remove
description: Use this skill to remove generated code files associated with a specific database table. Trigger when the user wants to "clean up code for table X", "delete entity Y", or "remove scaffolded code".
---

# Database Code Remover

This skill safely removes Java code files (Entity, Mapper, Service, etc.) that were generated for a specific database table.

## Usage

### 1. Remove Code for a Single Table

(Note: Use `--tables` parameter even for a single table)

```bash
python db_remove.py --tables t_scf_financing_order --base-dir scf-loan
```

### 2. Remove Code for Multiple Tables

```bash
python db_remove.py --tables t_scf_financing_order,t_scf_repayment_plan --base-dir scf-loan
```

### 3. Dry Run (Preview Deletion)

Always recommended to check what will be deleted before actual execution.

```bash
python db_remove.py --tables t_scf_financing_order --base-dir scf-loan --dry-run
```

### 4. Force Delete

Use when file permissions might block deletion.

```bash
python db_remove.py --tables t_scf_financing_order --base-dir scf-loan --force
```

## Safety

- By default, the script will ask for confirmation.
- Use `--dry-run` to see a list of files without deleting them.
