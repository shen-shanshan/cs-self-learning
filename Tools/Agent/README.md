# AI 工具

## 环境配置

- [灵芽 API](https://api.lingyaai.cn/)
- [Claude Code in VSCode 配置](https://api.lingyaai.cn/model_intro/Claude-Code.html)

## 使用教程

- [Agent Skills](https://agentskills.io/home)
- [Claude Code Docs](https://code.claude.com/docs/en/overview)
- [Extend Claude with skills](https://code.claude.com/docs/en/skills#share-skills)
- [Claude Code in Slack](https://code.claude.com/docs/en/slack)

## 使用技巧

### 常用快捷键

Press `Option+K` (Mac) / `Alt+K` (Windows/Linux) to also insert an `@-mention` reference (like `@file.ts#5-10`) into your prompt.

`/skill-name args`

`shift` + `tab`：切换交互模式。

### 上下文管理

- `Esc`: Stop Claude mid-action with the Esc key. Context is preserved, so you can redirect.
- `Esc` + `Esc` or `/rewind`: Press Esc twice or run /rewind to open the rewind menu and restore previous conversation and code state, or summarize from a selected message.
- `"Undo that"`: Have Claude revert its changes.

**清理/压缩上下文：**

- `/clear`: Reset context between unrelated tasks. Long sessions with irrelevant context can reduce performance.
- `/compact`: Clear conversation history but keep a summary in context.

After two failed corrections, /clear and write a better initial prompt incorporating what you learned.
A clean session with a better prompt almost always outperforms a long session with accumulated corrections.

**切回之前的上下文：**

- `/resume`: Resume a previous conversation.

### Plan Mode

在执行任何代码修改前，让模型先输出一个结构化的执行计划（Plan），供你确认或调整。

使用场景（大改动、不确定需求）：

- 重构模块
- 改接口设计
- 多文件联动修改
- 引入新框架

降低“AI乱改代码”的风险。

实用 prompt：

```
Use plan mode. Give me a very detailed plan including:
- files to change
- exact functions
- potential risks
- test cases

Use plan mode. Propose 2–3 different approaches with trade-offs.

Use plan mode. Only modify files under src/api/, do not touch core logic.
```

Plan → Review → Refine → Execute

**怎么复制并保存 plan？**

```bash
# open plan in vim
/plan open
# copy the whole file
:%y+
```

### 切换模型

`/model`

### 其它

- [不再触发 Claude 使用限制，大幅降低 Token 的 10 个有效习惯！](https://mp.weixin.qq.com/s/HRehP9A9AFs8quxcc2WVhQ)
