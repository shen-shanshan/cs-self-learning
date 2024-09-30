# 提交 PR 注意事项

## 示例 1

**Contributor:**

I have provided a draft version of the VLLM, with the following key changes:

1. Protocol changes: Aligned with OpenAI /v1/audio/speech.
2. Removed the custom inference part, keeping the inference logic consistent between streaming and non-streaming.
3. Optimized some potential issues of inconsistency caused by streaming inference.

These changes may have a significant impact. Feel free to leave comments to guide me in further improvements.

> 总结：分点列出具体的改动点，并虚心接受指正。

**Reviewer:**

Requested changes must be addressed to merge this pull request.

> 说明：这表示将 reviewer 提出的问题修改了就可以合并了。

## 示例 2

每一次代码提交，附上详细的 commit message，并附上相应的 `xxx_test.py`。

## 示例 3

**What changes were proposed in this pull request?**

...

**Why are the changes needed?**

1. ...
2. ...
3. ...

> 注意：可以附上原理图进行说明。

**Does this PR introduce any user-facing change?**

...

**How was this patch test?**

...

> 注意：可以附上测试日志进行说明。
