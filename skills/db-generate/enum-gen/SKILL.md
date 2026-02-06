---
name: enum-gen
description: "Generates Java Enum classes from database table comments. Invoke when user needs to extract enums from DDL or integrate enum generation into db-generate."
---

# Enum Generator

This skill scans database table comments, extracts key-value pairs (e.g., "0:No, 1:Yes"), and generates standard Java Enum classes implementing the MyBatis-Plus `IEnum` interface.

## Usage

```bash
python skills/enum-gen/enum_gen.py --table <TABLE_NAME> [options]
```

## Options

- `--table`: Target table name (required).
- `--column`: Target column name (optional). If not specified, scans all columns.
- `--db-url`: Database URL (optional).
- `--output-dir`: Output directory for generated Java files.
- `--package`: Base package name (default: `com.scf.loan`).

## Integration

This skill is designed to be integrated into the `db-generate` workflow.
