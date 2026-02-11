# AI 工具

## 环境配置

- Token 购买：[灵芽 API](https://api.lingyaai.cn/)
- [Claude Code in VSCode 配置](https://api.lingyaai.cn/model_intro/Claude-Code.html)

## 使用教程

- [Agent Skills](https://agentskills.io/home)
- [Claude Code Docs](https://code.claude.com/docs/en/overview)
- [Extend Claude with skills](https://code.claude.com/docs/en/skills#share-skills)
- [Claude Code in Slack](https://code.claude.com/docs/en/slack)

## 常用技巧

Press `Option+K` (Mac) / `Alt+K` (Windows/Linux) to also insert an `@-mention` reference (like `@file.ts#5-10`) into your prompt.

`/skill-name args`

**上下文管理：**

- `Esc`: Stop Claude mid-action with the Esc key. Context is preserved, so you can redirect.
- `Esc` + `Esc` or `/rewind`: Press Esc twice or run /rewind to open the rewind menu and restore previous conversation and code state, or summarize from a selected message.
- `"Undo that"`: Have Claude revert its changes.
- `/clear`: Reset context between unrelated tasks. Long sessions with irrelevant context can reduce performance.

After two failed corrections, /clear and write a better initial prompt incorporating what you learned.
A clean session with a better prompt almost always outperforms a long session with accumulated corrections.
