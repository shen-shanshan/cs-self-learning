---
name: vllm-ascend-multimodal-issues
description: Fetch and organize multimodal-related open issues from the GitHub project vllm-project/vllm-ascend. Use this skill when the user wants to collect, search, or summarize open issues related to multimodal models (e.g., Qwen-VL, Qwen2.5-VL, Qwen-Omni, LLaVA, InternVL, MiniCPM-V, etc.) in the vllm-ascend repository. Triggered by requests like "搜集vllm-ascend中多模态相关的issue", "整理Qwen-VL的open issue", "查一下vllm-ascend有哪些多模态问题还没解决".
---

# vllm-ascend 多模态 Open Issues 搜集

## 概述

从 GitHub 仓库 `vllm-project/vllm-ascend` 抓取所有多模态相关的 open issues，并生成结构化的 Markdown 报告（按 issue 列表 + 按关键词分类）。

## 使用方式

运行 `scripts/fetch_multimodal_issues.py`，优先使用 `gh` CLI 认证（无需手动提供 token）：

```bash
python3 scripts/fetch_multimodal_issues.py
```

可选参数：

| 参数 | 说明 | 示例 |
|------|------|------|
| `--token` | GitHub token（有 gh CLI 时不需要） | `--token ghp_xxx` |
| `--output` | 输出格式：`markdown`（默认）或 `json` | `--output json` |
| `--keywords` | 追加额外关键词（逗号分隔） | `--keywords "Kimi,DeepSeek-VL"` |

## 默认搜索关键词

脚本按组批量搜索（减少 API 请求次数）：

- **通用**: `multimodal`, `multi-modal`, `VLM`
- **Qwen 系列**: `Qwen-VL`, `Qwen2-VL`, `Qwen2.5-VL`, `Qwen-Omni`, `Qwen2-Omni`, `Qwen2.5-Omni`
- **其他模型**: `LLaVA`, `InternVL`, `MiniCPM-V`, `Phi-3-vision`, `Phi-4-vision`, `PaliGemma`, `Florence`
- **场景词**: `vision language`, `image understanding`, `video understanding`

## 输出格式

生成的 Markdown 报告包含两部分：

1. **Issue 列表**（表格）：issue 编号（含链接）、标题、作者、创建日期、Labels、匹配关键词
2. **按关键词分类**：每个关键词下列出匹配的 issue

Take [reference](references/reference.md) as a reference.

## 工作流

1. 运行脚本，输出保存为 Markdown 文件或直接展示
2. 如需用户追加关键词，使用 `--keywords`
3. 如需 JSON 格式做进一步处理，使用 `--output json`

## 认证说明

- **有 gh CLI**（推荐）：脚本自动检测并使用，无速率限制问题
- **无 gh CLI，有 token**：使用 `--token` 参数
- **无认证**：搜索分组间有 6 秒延迟，避免触发 GitHub 速率限制（10 次/分钟）

## 示例

```
帮我整理github上vllm-project/vllm-ascend项目中，多模态相关的、处于open状态的issue，要求按问题类型进行分类，每一个问题类别下需要有对应issue的链接，并最终生成一份报告，放到outputs目录下。
```
