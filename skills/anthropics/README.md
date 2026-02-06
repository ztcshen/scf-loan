# Anthropics Skills

This directory contains third-party or generic skills imported from the [Anthropics Agent Skills](https://github.com/anthropics/agent-skills) repository. These skills provide general-purpose capabilities and tools.

## Skills Overview

| Skill Name | Description | Source/Origin |
|------------|-------------|---------------|
| `skill-creator` | The meta-skill for creating and validating new skills. Follows the official Agent Skills specification. | [anthropic-agent-skills](https://github.com/anthropics/agent-skills) |
| `mcp-builder` | Tools and guides for building Model Context Protocol (MCP) servers. | [modelcontextprotocol](https://modelcontextprotocol.io) |
| `webapp-testing` | Utilities for automated web application testing (Playwright/Selenium integration). | [anthropic-agent-skills](https://github.com/anthropics/agent-skills) |
| `doc-coauthoring` | Structured workflow for collaborative documentation creation. | [anthropic-agent-skills](https://github.com/anthropics/agent-skills) |
| `xlsx` | Skills for creating, reading, and editing Excel spreadsheets (using pandas/openpyxl). | [anthropic-agent-skills](https://github.com/anthropics/agent-skills) |

## Maintenance Guidelines

- **Upstream Updates**: These skills should generally be kept in sync with their upstream repositories.
- **Minimal Modification**: Avoid modifying the core logic of these skills unless necessary. If project-specific adaptation is needed, consider wrapping them or moving them to `../custom`.
- **Structure**: Maintain the standard structure (`SKILL.md`, `scripts/`, `reference/`) to ensure compatibility.
