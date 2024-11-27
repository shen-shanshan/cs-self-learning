# cann.patch

## TODO

- 新建 PR，合入 cann 的 patch（git apply cann.path）
- 代码：[cann.patch](https://github.com/gpustack/llama-box/blob/main/llama-box/patches/llama.cpp/cann.patch)
- 发 issue 和邮件，联系 Frank Mai（thxcode0824@gmail.com），抄送 🌹（huafengchun@gmail.com），添加 coauthor（示例：Co-authored-by: wangshuai09 <391746016@qq.com>）

> [!NOTE]
>
> Please add the [CANN] prefix/tag in issues/PRs titles to help the CANN-team check/address them without delay.

## Apply Patch

```bash
# 直接在 llama.cpp 的根目录执行
git apply --check /home/sss/github/llama-box/llama-box/patches/llama.cpp/cann.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/cann.patch

git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/cann-1.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/cann-2.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/cann-3.patch

git apply --check /home/sss/github/llama-box/llama-box/patches/llama.cpp/clip.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/clip.patch

git apply --check /home/sss/github/llama-box/llama-box/patches/llama.cpp/embedding.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/embedding.patch ?

git apply --check /home/sss/github/llama-box/llama-box/patches/llama.cpp/ggml.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/ggml.patch ?

git apply --check /home/sss/github/llama-box/llama-box/patches/llama.cpp/model.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/model.patch

git apply --check /home/sss/github/llama-box/llama-box/patches/llama.cpp/model_py.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/model_py.patch

git apply --check /home/sss/github/llama-box/llama-box/patches/llama.cpp/ngram_cache.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/ngram_cache.patch

git apply --check /home/sss/github/llama-box/llama-box/patches/llama.cpp/template.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/template.patch

git apply --check /home/sss/github/llama-box/llama-box/patches/llama.cpp/tokenizer.patch
git apply /home/sss/github/llama-box/llama-box/patches/llama.cpp/tokenizer.patch
```

cann.patch：

```bash
diff --git a/ggml/src/ggml-cann/ggml-cann.cpp b/ggml/src/ggml-cann/ggml-cann.cpp
index 77634088..a9a1cfb6 100644
--- a/ggml/src/ggml-cann/ggml-cann.cpp
+++ b/ggml/src/ggml-cann/ggml-cann.cpp
```

回退更改：

```bash
git reset --hard 3952a221af54b8a6549bc2bd4a7363ef7ad3081e
```

切换到指定 commit：

```bash
git checkout 4a8ccb3

# Note: switching to '4a8ccb3'.

# You are in 'detached HEAD' state. You can look around, make experimental
# changes and commit them, and you can discard any commits you make in this
# state without impacting any branches by switching back to a branch.

# If you want to create a new branch to retain commits you create, you may
# do so (now or later) by using -c with the switch command. Example:

#   git switch -c <new-branch-name>

# Or undo this operation with:

#   git switch -

# Turn off this advice by setting config variable advice.detachedHead to false

# HEAD is now at 4a8ccb37 CUDA: no -sm row for very small matrices (#10185)

# 创建一个新分支来保留所创建的提交
git switch -c <new branch name>

# 回到项目的“当前”状态
git checkout master
```

### Test

```bash
./build/bin/llama-cli -m ./my_models/qwen2.5-1.5b/qwen2.5-1.5b-instruct-q8_0.gguf -p "Building a website can be done in 10 steps:" -ngl 32
```

## Commit message

git commit --amend

"improve inferencing performance for ascend npu.


Co-authored-by: Frank Mai <thxCode@thxcode0824@gmail.com>"
