---
name: open-source-engineer-schedule-planning-master
description: A skill that helps open source engineers plan their schedules effectively, including feature development, PR review, Issue resolution, information acquisition, etc.
---

# Open Source Engineer Schedule Planning Master

## Overview

This skill is designed to assist open source engineers in planning their schedules effectively. It provides tools and resources for managing feature development, PR review, Issue resolution, information acquisition, and more. By using this skill, engineers can optimize their time and ensure that they are focusing on the most important tasks.

## When to Use This Skill

Use this skill when you need to:

- Plan your daily, weekly, or monthly schedule as an open source engineer.
- Prioritize tasks such as feature development, PR review, and Issue resolution.
- Acquire information and resources related to your projects.
- Stay organized and manage your time effectively.

## Skill Inputs

The skill can take various inputs, such as:

- Specific tasks or projects you want to focus on.
- Timeframes for scheduling (e.g., daily, weekly, monthly).
- Prioritization preferences (e.g., focus on feature development first, then PR review).
- Any specific goals or deadlines you have in mind.

**Main arguments:**

1. `timeframe`: The desired scheduling timeframe, e.g., "day", "week" and "month", default is "week".

2. `projects`: A list of projects or repositories you are working on, e.g., "vllm-project/vllm" and "vllm-project/vllm-ascend", "vllm-project/vllm-omni".

3. `modules`: A list of modules or areas you own or you want to focus on, e.g., "multi-modal", "structured output" and "elastic scaling".

4. `your_id`: Your GitHub username or identifier to filter tasks assigned to you.

5. `tasks`: A list of tasks to be included in the schedule, with prioritization from 1 (highest) to 5 (lowest) and deadline information. Like: [task_name, priority_level, deadline], where deadline can be None if not specified.

## How to Use This Skill

1. Fetch recent (last `timeframe` to this moment) PRs and issues with `open` state in these `projects` from GitHub, which are related to these `modules` or are explicitly assigned to `your_id`. Fetch relevant information about these PRs and issues, such as their titles, labels, priority levels (if specified), and deadlines (if specified). You can use GitHub CLI or GitHub API to fetch this information. You can also fetch any other relevant information that may help in scheduling, such as the estimated time required for each task or any dependencies between tasks.

2. Fetch recent news and updates related to large language models and their applications. This can include new research papers, blog posts, or announcements from relevant organizations. You can use web scraping techniques or APIs from news sources to gather this information.

3. Analyze the list of `tasks` that you want to include in the schedule.

3. Prioritize the `tasks` based on their priority levels, deadlines, and estimated time required.

4. Generate a schedule (Put the schedule in a markdown file and save it to `~/.claude/skills/open-source-engineer-schedule-planning-master/outputs/schedule.md`.) for the specified `timeframe`, ensuring that high-priority tasks are allocated sufficient time and that deadlines are met. The generated schedule should be organized and easy to follow, allowing you to manage your time effectively. The schedule should also include any relevant information or resources needed to complete the tasks.

5. Take [reference](references/reference.md) as a reference and take [template](./assets/template.md) as a template for the format of the schedule.

**Rules to obey:**

1. Don't split the tasks to Morning, Afternoon and Evening, just list the tasks needs to do everday.
2. Offer titles, links, labels and authors for fetched PRs, Issues and recent news (papers or blogs), and briefly summarise their contents. Don't include status, created time and other information. Just focus on the titles, links and summaries.
3. Put similar PRs or issues together.
4. Don't include "Success Metrics", "Next Week Preview" and "Weekly Time Allocation Summary" sections in the schedule.
5. Should include "Task Overview", "Daily Schedule", "Key Focus Areas This Week" and "Important Notes" sections in the schedule.
6. Use some Emojis to make the schedule more visually appealing and easier to read.
7. Summarize potential collaboration opportunities with upstream maintainers or other contributors, if applicable.

## Example Prompt

```
/open-source-engineer-schedule-planning-master
I'm an open source engineer, please help me to plan my schedule for the next week.
I want to focus on the vllm-project/vllm and vllm-project/vllm-ascend repositories, especially on multi-modal, structured output and elastic scaling modules.
My GitHub username is shen-shanshan.
I have the following tasks to include in my schedule:
1. Optimize Qwen3-VL performance with priority 1 and a deadline of April 1, 2026. This may last for about 2 weeks.
2. Resolve issues related to multi-modal models, such as VL, Omni, OCR models or directly assigned to me in vllm-ascend with priority 2.
3. Review PRs related to multi-modal models in vllm-ascend with priority 3.
4. Keep up with recent news about large language models and their applications with priority 4.
```

## Requirements

Take Mac OS as an example, you need to install the following dependencies:

```bash
# Check if Homebrew is installed.
which brew
# /opt/homebrew/bin/brew

# Install the GitHub CLI:
brew install gh
# After installation, authenticate with:
gh auth login
```

## Author

[@shen-shanshan](https://github.com/shen-shanshan)
