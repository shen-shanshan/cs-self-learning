# from vllm.vllm_debug import vllm_debug
# from vllm_ascend.vllm_debug import vllm_debug

import torch

STOP = False


def vllm_debug(info: dict, print_once: bool = False):
    global STOP
    
    if torch.distributed.get_rank() == 0 and not STOP:
        print("-" * 100)
        for k, v in info.items():
            print(f"{k}: {v}")
        print("-" * 100)
        
        if print_once:
            STOP = True
