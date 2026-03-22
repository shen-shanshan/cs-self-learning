针对 PR `https://github.com/vllm-project/vllm-ascend/pull/7427` 中提到的问题和代码修改，帮我写一个测试用例。
我想模拟在单卡上连续启动两个 vllm-ascend 服务时（模型使用 `Qwen3-0.6B`），第二个服务会不会出现 Available KV cache memory 不够的情况。
你可以先思考能不能直接针对 `determine_available_memory()` 这个方法写 ut 来覆盖这种场景。
如果 ut 无法测试这种场景的话，你就写一个 e2e 测试来看护这种场景。
测试代码写完之后，记得要加到 `.github/workflows/` 目录下对应的 `.yaml` 文件中，这样才能在 GitHub 上触发 CI。
