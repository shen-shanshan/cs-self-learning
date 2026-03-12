#!/usr/bin/env python3
"""
Fetch and organize multimodal-related open issues from vllm-project/vllm-ascend.

Prefers GitHub CLI (gh) for authentication. Falls back to raw API with optional token.

Usage:
    python3 fetch_multimodal_issues.py [--token <github_token>] [--output <format>] [--keywords <kw1,kw2>]

Options:
    --token     GitHub personal access token (optional, bypassed if gh CLI is available)
    --output    Output format: 'markdown' (default) or 'json'
    --keywords  Comma-separated extra keywords to search (appended to defaults)

Examples:
    python3 fetch_multimodal_issues.py
    python3 fetch_multimodal_issues.py --token ghp_xxx
    python3 fetch_multimodal_issues.py --output json
    python3 fetch_multimodal_issues.py --keywords "Florence,CLIP"
"""

import sys
import json
import argparse
import subprocess
import time
import urllib.request
import urllib.parse
import urllib.error
from datetime import datetime

REPO = "vllm-project/vllm-ascend"

# Multimodal-related keywords, grouped to minimize API calls (max ~6 OR terms per query)
KEYWORD_GROUPS = [
    ["multimodal", "multi-modal", "VLM", "Qwen-VL", "Qwen2-VL", "Qwen2.5-VL"],
    ["Qwen-Omni", "Qwen2-Omni", "Qwen2.5-Omni", "LLaVA", "InternVL"],
    ["MiniCPM-V", "Phi-3-vision", "Phi-4-vision", "PaliGemma", "Florence"],
    ["vision language", "image understanding", "video understanding"],
]

ALL_KEYWORDS = [kw for group in KEYWORD_GROUPS for kw in group]


def gh_available():
    """Check if GitHub CLI is available."""
    try:
        result = subprocess.run(
            ["gh", "auth", "status"], capture_output=True, text=True, timeout=5
        )
        return result.returncode == 0
    except (FileNotFoundError, subprocess.TimeoutExpired):
        return False


def search_with_gh(query):
    """Search issues using gh CLI (handles authentication automatically)."""
    cmd = [
        "gh", "api",
        "-H", "Accept: application/vnd.github.v3+json",
        f"/search/issues?q={urllib.parse.quote(query)}&per_page=100&sort=created&order=desc",
    ]
    result = subprocess.run(cmd, capture_output=True, text=True, timeout=30)
    if result.returncode != 0:
        print(f"gh CLI error: {result.stderr}", file=sys.stderr)
        return []
    data = json.loads(result.stdout)
    return data.get("items", [])


def search_with_api(query, token=None):
    """Search issues using GitHub REST API."""
    encoded = urllib.parse.quote(query)
    url = f"https://api.github.com/search/issues?q={encoded}&per_page=100&sort=created&order=desc"
    headers = {
        "Accept": "application/vnd.github.v3+json",
        "User-Agent": "vllm-ascend-issue-fetcher/1.0",
    }
    if token:
        headers["Authorization"] = f"token {token}"

    req = urllib.request.Request(url, headers=headers)
    try:
        with urllib.request.urlopen(req, timeout=30) as resp:
            data = json.loads(resp.read().decode("utf-8"))
            return data.get("items", [])
    except urllib.error.HTTPError as e:
        body = e.read().decode("utf-8")
        if e.code == 403:
            print(
                f"\nRate limit exceeded. Use --token or install gh CLI for authentication.",
                file=sys.stderr,
            )
            sys.exit(1)
        print(f"HTTP {e.code}: {body}", file=sys.stderr)
        return []
    except urllib.error.URLError as e:
        print(f"URL Error: {e.reason}", file=sys.stderr)
        return []


def build_query(keywords):
    """Build a GitHub search query with OR-joined keywords in title."""
    # GitHub search: (kw1 OR kw2 OR ...) in:title
    kw_or = " OR ".join(f'"{kw}"' for kw in keywords)
    return f"repo:{REPO} is:open is:issue ({kw_or}) in:title"


def fetch_all_multimodal_issues(extra_keywords=None, token=None):
    """Fetch all multimodal-related open issues, deduplicating by issue number."""
    use_gh = gh_available()
    if use_gh:
        print("  Using gh CLI for authenticated requests.", file=sys.stderr)
    elif token:
        print("  Using API token for authenticated requests.", file=sys.stderr)
    else:
        print(
            "  Warning: No authentication. Rate limits apply (10 req/min).",
            file=sys.stderr,
        )

    groups = list(KEYWORD_GROUPS)
    if extra_keywords:
        groups.append(extra_keywords)

    seen = {}
    for i, group in enumerate(groups):
        query = build_query(group)
        print(f"  Searching group {i+1}/{len(groups)}: {group}", file=sys.stderr)

        if use_gh:
            items = search_with_gh(query)
        else:
            items = search_with_api(query, token)
            if i < len(groups) - 1:
                time.sleep(6)  # Respect GitHub rate limit for unauthenticated: 10/min

        for issue in items:
            num = issue["number"]
            if num not in seen:
                seen[num] = issue

    print(
        f"\n  Found {len(seen)} unique multimodal-related open issues.",
        file=sys.stderr,
    )
    return sorted(seen.values(), key=lambda x: x["number"], reverse=True)


def format_date(iso_str):
    """Format ISO date string to YYYY-MM-DD."""
    try:
        dt = datetime.strptime(iso_str, "%Y-%m-%dT%H:%M:%SZ")
        return dt.strftime("%Y-%m-%d")
    except Exception:
        return iso_str[:10]


def extract_matched_keywords(issue, keywords):
    """Find which keywords matched this issue's title."""
    title_lower = issue.get("title", "").lower()
    return [kw for kw in keywords if kw.lower() in title_lower]


def render_markdown(issues, keywords):
    """Render issues as a Markdown report."""
    now = datetime.utcnow().strftime("%Y-%m-%d %H:%M UTC")
    lines = [
        f"# vllm-ascend 多模态相关 Open Issues",
        f"",
        f"> 仓库: [{REPO}](https://github.com/{REPO})  ",
        f"> 生成时间: {now}  ",
        f"> 共 **{len(issues)}** 个 issue",
        f"",
        "## Issue 列表",
        "",
        "| # | 标题 | 作者 | 创建日期 | Labels | 匹配关键词 |",
        "|---|------|------|----------|--------|-----------|",
    ]

    for issue in issues:
        num = issue["number"]
        title = issue["title"].replace("|", "\\|")
        url = issue["html_url"]
        author = issue.get("user", {}).get("login", "unknown")
        created = format_date(issue.get("created_at", ""))
        labels = ", ".join(
            f"`{l['name']}`" for l in issue.get("labels", [])
        ) or "-"
        matched = extract_matched_keywords(issue, keywords)
        matched_str = ", ".join(matched[:3]) if matched else "-"
        lines.append(
            f"| [#{num}]({url}) | {title} | @{author} | {created} | {labels} | {matched_str} |"
        )

    # Group by matched keyword
    lines += ["", "## 按关键词分类", ""]
    keyword_map = {}
    for issue in issues:
        for kw in extract_matched_keywords(issue, keywords):
            keyword_map.setdefault(kw, []).append(issue)

    for kw in keywords:
        if kw not in keyword_map:
            continue
        kw_issues = keyword_map[kw]
        lines.append(f"### {kw} ({len(kw_issues)})")
        lines.append("")
        for issue in kw_issues:
            num = issue["number"]
            title = issue["title"]
            url = issue["html_url"]
            lines.append(f"- [#{num}]({url}) {title}")
        lines.append("")

    return "\n".join(lines)


def main():
    parser = argparse.ArgumentParser(
        description=f"Fetch multimodal-related open issues from {REPO}"
    )
    parser.add_argument("--token", help="GitHub personal access token")
    parser.add_argument(
        "--output",
        choices=["markdown", "json"],
        default="markdown",
        help="Output format (default: markdown)",
    )
    parser.add_argument(
        "--keywords",
        help="Extra comma-separated keywords to add to search",
    )
    args = parser.parse_args()

    extra_keywords = None
    if args.keywords:
        extra_keywords = [k.strip() for k in args.keywords.split(",") if k.strip()]

    print(
        f"Fetching multimodal open issues from {REPO}...",
        file=sys.stderr,
    )
    issues = fetch_all_multimodal_issues(extra_keywords, token=args.token)

    all_keywords = ALL_KEYWORDS + (extra_keywords or [])

    if args.output == "json":
        print(json.dumps(issues, ensure_ascii=False, indent=2))
    else:
        print(render_markdown(issues, all_keywords))


if __name__ == "__main__":
    main()
