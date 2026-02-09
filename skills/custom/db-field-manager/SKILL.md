# Database Field Manager Skill

This skill allows incremental updates to the codebase when database fields are added or removed, preserving manual changes in the files.

## Commands

### `db-add-field`
Adds a new field to the Entity, DTO, and Converter classes.

**Arguments:**
- `table` (required): The database table name (e.g., `t_scf_financing_order`).
- `field` (required): The column name in the database (e.g., `risk_score`).
- `type` (required): The MySQL column type (e.g., `decimal`, `varchar`, `int`).
- `comment` (optional): The column comment.
- `base-package` (optional): The base package name (default: `com.scf.loan`).

**Example:**
```bash
db-add-field --table t_scf_financing_order --field risk_score --type decimal --comment "风控评分"
```

### `db-remove-field`
Removes a field from the Entity, DTO, and Converter classes.

**Arguments:**
- `table` (required): The database table name.
- `field` (required): The column name.
- `base-package` (optional): The base package name (default: `com.scf.loan`).

**Example:**
```bash
db-remove-field --table t_scf_financing_order --field risk_score
```

## Supported Files
- **Entity**: Adds/Removes field definition and JavaDoc.
- **DTO**: Adds/Removes field definition and JavaDoc.
- **Converter**: Adds/Removes mapping logic in `toDTO` and `toEntity` methods.

## Notes
- Does not modify Mapper or Service files as they usually don't require changes for simple field additions.
- Automatically handles basic Java imports (e.g., `BigDecimal`, `LocalDateTime`).
