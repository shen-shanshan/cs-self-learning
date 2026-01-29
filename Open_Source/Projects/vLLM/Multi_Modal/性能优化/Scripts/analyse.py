from torch_npu.profiler.profiler import analyse

if __name__ == "__main__":
    analyse(profiler_path="./vllm_profile")
