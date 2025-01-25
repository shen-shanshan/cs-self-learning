class AAA:
    def __init__(self):
        print("init...")
    
if __name__ == '__main__':
    a = AAA()
    print(a.__class__)
    print(a.__class__.__name__)

    qualname = "vllm.lora.punica_wrapper.punica_gpu.PunicaWrapperGPU"
    list = qualname.rsplit(".", 1)[1]
    print(list)
