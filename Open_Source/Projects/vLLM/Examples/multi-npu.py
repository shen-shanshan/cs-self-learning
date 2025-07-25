import gc

import torch

from vllm import LLM, SamplingParams
from vllm.distributed.parallel_state import (destroy_distributed_environment,
                                             destroy_model_parallel)

def clean_up():
    destroy_model_parallel()
    destroy_distributed_environment()
    gc.collect()
    torch.npu.empty_cache()

if __name__ == "__main__":

    prompts = [
        "Hello, my name is",
        "The future of AI is",
    ]
    sampling_params = SamplingParams(temperature=0.6, top_p=0.95, top_k=40)
    llm = LLM(model="/shared/cache/modelscope/hub/models/Qwen/Qwen2.5-7B-Instruct",
            pipeline_parallel_size=2,
            enforce_eager=True,
            trust_remote_code=True,
            max_model_len=1024)

    outputs = llm.generate(prompts, sampling_params)
    for output in outputs:
        prompt = output.prompt
        generated_text = output.outputs[0].text
        print(f"Prompt: {prompt!r}, Generated text: {generated_text!r}")

    del llm
    clean_up()
