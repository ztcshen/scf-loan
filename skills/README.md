> **Note:** This repository contains custom and vendor implementation of skills for SCF Loan Project. For information about the Agent Skills standard, see [agentskills.io](http://agentskills.io).

# Skills
Skills are folders of instructions, scripts, and resources that Agent loads dynamically to improve performance on specialized tasks. Skills teach Agent how to complete specific tasks in a repeatable way, whether that's creating documents with your company's brand guidelines, analyzing data using your organization's specific workflows, or automating personal tasks.

For more information, check out:
- [What are skills?](https://support.claude.com/en/articles/12512176-what-are-skills)
- [Using skills in Claude](https://support.claude.com/en/articles/12512180-using-skills-in-claude)
- [How to create custom skills](https://support.claude.com/en/articles/12512198-creating-custom-skills)
- [Equipping agents for the real world with Agent Skills](https://anthropic.com/engineering/equipping-agents-for-the-real-world-with-agent-skills)

# About This Repository

This repository contains skills that demonstrate what's possible with Agent's skills system. These skills range from creative applications (art, music, design) to technical tasks (testing web apps, MCP server generation) to enterprise workflows (communications, branding, etc.).

Each skill is self-contained in its own folder with a `SKILL.md` file containing the instructions and metadata that Agent uses. Browse through these skills to get inspiration for your own skills or to understand different patterns and approaches.

# Skill Sets
- [./custom](./custom): Custom skills for SCF Loan Project (e.g. `db-generate`, `git-commit`)
- [./anthropics](./anthropics): Vendor skills imported from [anthropics/agent-skills](https://github.com/anthropics/agent-skills) (e.g. `skill-creator`, `mcp-builder`)
- [./superpowers](./superpowers): Vendor skills imported from [obra/superpowers](https://github.com/obra/superpowers) (e.g. `brainstorming`, `systematic-debugging`)

# Creating a Basic Skill

Skills are simple to create - just a folder with a `SKILL.md` file containing YAML frontmatter and instructions. You can use the **template-skill** in this repository as a starting point:

```markdown
---
name: my-skill-name
description: A clear description of what this skill does and when to use it
---

# My Skill Name

[Add your instructions here that Agent will follow when this skill is active]

## Examples
- Example usage 1
- Example usage 2

## Guidelines
- Guideline 1
- Guideline 2
```

The frontmatter requires only two fields:
- `name` - A unique identifier for your skill (lowercase, hyphens for spaces)
- `description` - A complete description of what the skill does and when to use it

The markdown content below contains the instructions, examples, and guidelines that Agent will follow. For more details, see [How to create custom skills](https://support.claude.com/en/articles/12512198-creating-custom-skills).
