#!/usr/bin/env python3
"""
vLLM Benchmark Comparison Script

Parses two vLLM benchmark result text blocks (before/after a change),
computes per-metric percentage changes, and generates a Markdown report
with grouped metrics table matching the standard format.

Usage:
    python3 compare_benchmarks.py <input_file>
    python3 compare_benchmarks.py --before before.txt --after after.txt
    python3 compare_benchmarks.py --stdin < combined.txt
"""

import argparse
import os
import re
import sys
from datetime import datetime


# Ordered metric groups for the main comparison table
THROUGHPUT_METRICS = [
    "request throughput (req/s)",
    "output token throughput (tok/s)",
    "total token throughput (tok/s)",
    "peak output token throughput (tok/s)",
]

LATENCY_METRICS = [
    "benchmark duration (s)",
    "mean ttft (ms)",
    "median ttft (ms)",
    "p99 ttft (ms)",
    "mean tpot (ms)",
    "median tpot (ms)",
    "p99 tpot (ms)",
    "mean itl (ms)",
    "median itl (ms)",
    "p99 itl (ms)",
]

# Metrics excluded from the main table (informational only)
EXCLUDED_METRICS = {
    "successful requests",
    "failed requests",
    "request rate configured (rps)",
    "total input tokens",
    "total generated tokens",
    "peak concurrent requests",
}


def parse_benchmark_block(text: str) -> dict:
    """Extract metric->value pairs from a vLLM benchmark result block."""
    metrics = {}
    pattern = re.compile(r"^([A-Za-z][^:]+?):\s+([\d.]+)\s*$", re.MULTILINE)
    for match in pattern.finditer(text):
        name = match.group(1).strip()
        value = float(match.group(2))
        metrics[name.lower()] = (name, value)
    return metrics


def pct_change(before: float, after: float) -> float:
    if before == 0:
        return float("inf") if after != 0 else 0.0
    return (after - before) / abs(before) * 100


def fmt_value(value: float) -> str:
    """Format a number with thousands separator; integers omit decimals."""
    if value == int(value):
        return f"{int(value):,}"
    return f"{value:,.2f}"


def fmt_pct(delta: float) -> str:
    """Format percentage with 2 decimal places, sign, and direction arrow."""
    sign = "+" if delta >= 0 else ""
    arrow = "↑" if delta > 0 else "↓"
    return f"{sign}{delta:.2f}% {arrow}"


def split_before_after(text: str):
    """Split combined text into (before_block, after_block)."""
    lines = text.splitlines()
    before_start = after_start = None

    for i, line in enumerate(lines):
        low = line.lower()
        if before_start is None and "before" in low:
            before_start = i
        elif before_start is not None and after_start is None and "after" in low:
            after_start = i
            break

    if before_start is None or after_start is None:
        sys.exit(
            "ERROR: Could not locate 'Before' and 'After' sections in the input.\n"
            "Make sure your input contains lines with 'Before' and 'After' labels."
        )

    before_block = "\n".join(lines[before_start:after_start])
    after_block = "\n".join(lines[after_start:])
    return before_block, after_block


def metric_row(key: str, before_metrics: dict, after_metrics: dict):
    """Build a single table row for a metric key. Returns None if metric is absent."""
    before_entry = before_metrics.get(key)
    after_entry = after_metrics.get(key)
    if not before_entry and not after_entry:
        return None

    display_name = (before_entry or after_entry)[0]
    b_val = before_entry[1] if before_entry else None
    a_val = after_entry[1] if after_entry else None

    b_str = fmt_value(b_val) if b_val is not None else "N/A"
    a_str = fmt_value(a_val) if a_val is not None else "N/A"

    if b_val is not None and a_val is not None:
        delta = pct_change(b_val, a_val)
        cmp_str = fmt_pct(delta)
    elif b_val is None:
        cmp_str = "N/A (new)"
        delta = None
    else:
        cmp_str = "N/A (removed)"
        delta = None

    return f"| {display_name} | {b_str} | {a_str} | {cmp_str} |", delta


def build_grouped_table(
    before_metrics: dict,
    after_metrics: dict,
    before_label: str,
    after_label: str,
) -> str:
    """Build the main grouped metrics table."""
    header = (
        f"| Metric | {before_label} | {after_label} | Comparison |\n"
        f"| :----- | :------------- | :------------ | :--------- |"
    )
    rows = [header]

    def add_group(group_name: str, keys: list):
        group_rows = []
        for key in keys:
            result = metric_row(key, before_metrics, after_metrics)
            if result is None:
                continue
            row_str, _ = result
            group_rows.append(row_str)
        if group_rows:
            rows.append(f"| **{group_name}** | | | |")
            rows.extend(group_rows)

    add_group("Throughput", THROUGHPUT_METRICS)
    add_group("Latency", LATENCY_METRICS)

    # Any remaining metrics not in the above groups or excluded set
    known_keys = set(THROUGHPUT_METRICS + LATENCY_METRICS) | EXCLUDED_METRICS
    extra_keys = [k for k in before_metrics if k not in known_keys]
    extra_keys += [k for k in after_metrics if k not in known_keys and k not in extra_keys]
    if extra_keys:
        add_group("Other", extra_keys)

    return "\n".join(rows)


def build_report(
    before_metrics: dict,
    after_metrics: dict,
    title: str,
    before_label: str,
    after_label: str,
) -> str:
    main_table = build_grouped_table(
        before_metrics, after_metrics, before_label, after_label
    )

    now = datetime.now().strftime("%Y-%m-%d %H:%M")

    return f"""# vLLM Benchmark Comparison Report

> Generated: {now}
> {title}

---

## Performance Metrics

{main_table}

---
"""


def main():
    parser = argparse.ArgumentParser(description="Compare vLLM benchmark results.")
    group = parser.add_mutually_exclusive_group()
    group.add_argument("input_file", nargs="?", help="Combined before/after text file")
    group.add_argument("--stdin", action="store_true", help="Read combined text from stdin")
    parser.add_argument("--before", help="File containing the 'before' benchmark block")
    parser.add_argument("--after", help="File containing the 'after' benchmark block")
    parser.add_argument("--title", default="vLLM Benchmark Comparison",
                        help="Short description for the report header")
    parser.add_argument("--before-label", default="Before this PR",
                        help="Column header for the before column (default: 'Before this PR')")
    parser.add_argument("--after-label", default="After this PR",
                        help="Column header for the after column (default: 'After this PR')")
    parser.add_argument("--output-dir", default="outputs",
                        help="Directory to write the markdown report (default: ./outputs)")
    args = parser.parse_args()

    if args.before and args.after:
        with open(args.before) as f:
            before_text = f.read()
        with open(args.after) as f:
            after_text = f.read()
    elif args.stdin:
        combined = sys.stdin.read()
        before_text, after_text = split_before_after(combined)
    elif args.input_file:
        with open(args.input_file) as f:
            combined = f.read()
        before_text, after_text = split_before_after(combined)
    else:
        combined = sys.stdin.read()
        before_text, after_text = split_before_after(combined)

    before_metrics = parse_benchmark_block(before_text)
    after_metrics = parse_benchmark_block(after_text)

    if not before_metrics:
        sys.exit("ERROR: No metrics found in the 'before' block. Check the input format.")
    if not after_metrics:
        sys.exit("ERROR: No metrics found in the 'after' block. Check the input format.")

    report = build_report(
        before_metrics, after_metrics,
        args.title, args.before_label, args.after_label,
    )

    os.makedirs(args.output_dir, exist_ok=True)
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
    out_path = os.path.join(args.output_dir, f"benchmark_comparison_{timestamp}.md")
    with open(out_path, "w") as f:
        f.write(report)

    print(f"Report written to: {out_path}")
    print(report)


if __name__ == "__main__":
    main()
