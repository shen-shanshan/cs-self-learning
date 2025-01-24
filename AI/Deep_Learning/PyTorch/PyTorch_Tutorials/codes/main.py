import torch as t

if __name__ == '__main__':
    # 验证 PyTorch 是否安装成功
    print(t.cuda.is_available())
