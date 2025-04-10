# LLaMA-Factory 昇腾微调实践

## 实验记录

Training Approaches: PPO/DPO/...

“训练阶段”和“微调方法”有什么不同？

程序入口：**llamafactory-cli**

- train：训练；
- chat：推理；
- export：导出；
- ……

原始模型直接推理：

```bash
ASCEND_RT_VISIBLE_DEVICES=0 llamafactory-cli chat --model_name_or_path qwen/Qwen1.5-7B --adapter_name_or_path saves/Qwen1.5-7B/lora/sft --template qwen --finetuning_type lora

ASCEND_RT_VISIBLE_DEVICES=0 llamafactory-cli chat --model_name_or_path qwen/Qwen1.5-7B --template qwen --finetuning_type lora
```

自定义数据集构建：……

基于 LoRA 的 sft 指令微调：

```bash
ASCEND_RT_VISIBLE_DEVICES=0 llamafactory-cli train ./qwen1_5_lora_sft_ds.yaml
FORCE_TORCHRUN=1 llamafactory-cli train ./qwen1_5_lora_sft_ds.yaml
```

动态合并 LoRA 的推理：

```bash
ASCEND_RT_VISIBLE_DEVICES=0 llamafactory-cli chat --model_name_or_path qwen/Qwen1.5-7B --adapter_name_or_path saves/Qwen1.5-7B/lora/sft --template qwen --finetuning_type lora
```

## 参考资料

- [<u>LLaMA-Factory GitHub</u>](https://github.com/hiyouga/LLaMA-Factory?tab=readme-ov-file)
- [<u>LLaMA-Factory QuickStart</u>](https://zhuanlan.zhihu.com/p/695287607)
