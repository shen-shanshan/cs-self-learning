---
name: vllm-benchmark-summary
description: >
  Compare vLLM serving benchmark outputs before and after a code change.
  Parses plain-text vLLM benchmark output (the "Serving Benchmark Result" block),
  computes per-metric percentage changes with improvement/regression markers,
  generates a Markdown report with a full metrics table and a key-changes summary,
  and saves it to ./outputs/.
  Use when the user pastes or provides vLLM benchmark output and asks to compare,
  summarize, or analyze performance differences between two runs (e.g., "before this PR"
  vs "after this PR", or any before/after wording).
---

# vLLM Benchmark Summary

## Workflow

1. **Identify the input** — the user pastes combined text containing a "before" block and an "after" block (or provides two separate files).
2. **Save the input** to `/tmp/bench_input.txt`, then run the script.
3. **Run the script** using the bundled `./scripts/compare_benchmarks.py`. This generates the Performance Metrics table.
4. **Generate the Summary** — after the script runs, analyze the table data and write a narrative `## AI Summary` section (see guidelines below).
5. **Assemble and save** — append the Summary to the output file, then show the complete report to the user.

## Running the Script

### Combined text input (most common)

Save the user's pasted text to `/tmp/bench_input.txt`, then:

```bash
python3 /Users/shanshan-shen/.claude/skills/vllm-benchmark-summary/scripts/compare_benchmarks.py \
    /tmp/bench_input.txt \
    --output-dir /Users/shanshan-shen/.claude/skills/vllm-benchmark-summary/outputs \
    --title "vLLM Benchmark Comparison"
```

### Two separate files

```bash
python3 /Users/shanshan-shen/.claude/skills/vllm-benchmark-summary/scripts/compare_benchmarks.py \
    --before before.txt --after after.txt \
    --output-dir /Users/shanshan-shen/.claude/skills/vllm-benchmark-summary/outputs \
    --title "vLLM Benchmark Comparison"
```

## Input Format

The script expects the standard vLLM benchmark output block:

```
Before this PR:          ← any line containing "before" (case-insensitive)

============ Serving Benchmark Result ============
Metric Name:                     value
...
==================================================

After this PR:           ← any line containing "after" (case-insensitive)

============ Serving Benchmark Result ============
...
==================================================
```

Metric lines must follow the pattern `Metric Name: <number>`. Section separator lines (`===`, `---`) are ignored automatically.

## Output

The script creates a report file under `/Users/shanshan-shen/.claude/skills/vllm-benchmark-summary/outputs/` containing:

- **Performance Metrics table** — metrics grouped into **Throughput** and **Latency** sections, with left-aligned columns and percentage comparison (e.g. `+0.54% ↑`, `-9.86% ↓`)

After the script runs, **read the output file path from the script's stdout**, then **append a `## AI Summary` section** with narrative bullet points, and save the complete report.

### Comparison column format

- Two decimal places with sign and direction arrow: `+0.54% ↑`, `-9.86% ↓`
- Arrow indicates direction of change only (↑ = value increased, ↓ = value decreased), not whether it is an improvement

### Summary Writing Guidelines

Write 4–6 narrative bullet points covering:
- The standout improvement (largest positive delta for a "lower is better" metric, or largest increase for "higher is better")
- Notable TTFT / TPOT / ITL changes with before→after values
- Overall throughput trend (flat, improved, or regressed)
- Any minor regressions worth flagging, noting if they may be within noise
- Any peak or burst metric anomalies with context

Direction conventions — use these to frame improvements vs. regressions:
- **Lower is better**: TTFT, TPOT, ITL (all variants), Benchmark duration, Failed requests
- **Higher is better**: Request throughput, Output/Total token throughput

Style rules:
- Use `**bold**` to emphasize key percentages and before→after values (e.g. `**-18.45%**`, `439ms → 359ms`)
- Do **not** use ✅/❌ markers — write in plain narrative style
- Keep bullets concise (1–2 sentences each)

### Table format

Refer to the `reference/reference.md` file for a full example of the generated report format and content.

## Notes

- Always use `--title "vLLM Benchmark Comparison"` unless the user specifies a different title.
- Save the output file into `/Users/shanshan-shen/.claude/skills/vllm-benchmark-summary/outputs`.
- The script requires only the Python standard library — no extra dependencies.
