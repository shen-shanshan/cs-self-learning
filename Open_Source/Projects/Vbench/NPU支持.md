# VBench NPU Support

## Installation

```bash
pip install torch==2.9.0 torch-npu==2.9.0 torchvision==0.24.0 -i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
pip install pkg_resources -i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
```

`decord` 没有 `linux_aarch64` PyPI wheel，需要手动编译安装。

```bash
sudo apt update
sudo apt install software-properties-common -y

# https://github.com/dmlc/decord?utm_source=chatgpt.com#installation
sudo add-apt-repository ppa:jonathonf/ffmpeg-4
sudo apt-get update
sudo apt-get install -y build-essential python3-dev python3-setuptools make cmake
sudo apt-get install -y ffmpeg libavcodec-dev libavfilter-dev libavformat-dev libavutil-dev

# Build the shared library in source root directory:
git clone --recursive git@github.com:dmlc/decord.git
cd decord
mkdir build && cd build
cmake .. -DUSE_CUDA=0 -DCMAKE_BUILD_TYPE=Release
make

# Install python bindings:
python3 setup.py install --user
```

```bash
pip install VBench -i https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
```

## Usage

```bash
export HF_ENDPOINT=https://hf-mirror.com
python -c "
from huggingface_hub import hf_hub_download
hf_hub_download(
    repo_id='OpenGVLab/VBench_Used_Models',
    filename='l16_ptk710_ftk710_ftk400_f16_res224.pth',
)
"

gdown 1hviZzsInIgJA96ppVj4B2DHhTZWeM4nc --output lavie.zip

# scp lavie.zip user@server:/path/
scp -P 8333 lavie.zip root@139.9.155.20:/vllm-workspace/VBench/sampled_videos/
unzip lavie.zip
```

```python
from vbench import VBench

my_VBench = VBench(device, "vbench/VBench_full_info.json", "outputs/evaluation_results")
my_VBench.evaluate(
    videos_path = "sampled_videos/lavie/human_action",
    name = "lavie_human_action",
    dimension_list = ["human_action"],
)
```

## Outputs

Log:

```bash
Use device: npu
Evaluation meta data saved to outputs/evaluation_results/lavie_human_action_full_info.json
/root/miniconda3/envs/vbench/lib/python3.10/site-packages/timm/models/layers/__init__.py:48: FutureWarning: Importing from timm.models.layers is deprecated, please import via timm.layers
  warnings.warn(f"Importing from {__name__} is deprecated, please import via timm.layers", FutureWarning)
/root/miniconda3/envs/vbench/lib/python3.10/site-packages/timm/models/registry.py:4: FutureWarning: Importing from timm.models.registry is deprecated, please import via timm.models
  warnings.warn(f"Importing from {__name__} is deprecated, please import via timm.models", FutureWarning)
/vllm-workspace/VBench/vbench/third_party/umt/models/modeling_finetune.py:348: UserWarning: Overwriting vit_large_patch16_224 in registry with vbench.third_party.umt.models.modeling_finetune.vit_large_patch16_224. This is because the name being registered conflicts with an existing name. Please check if this is not expected.
  def vit_large_patch16_224(pretrained=False, **kwargs):
cur_full_info_path: outputs/evaluation_results/lavie_human_action_full_info.json
Use checkpoint: False
Checkpoint number: 16
n_position: 3136
pre_n_position: 1568
Pretraining uses 8 frames, but current frame is 16
Interpolate the position embedding
Use learnable position embedding
100%|████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████| 500/500 [02:05<00:00,  3.98it/s]
Evaluation results saved to outputs/evaluation_results/lavie_human_action_eval_results.json
```

Result:

```json
{
    "human_action": [
        0.964,
        [
            {
                "video_path": "sampled_videos/lavie/human_action/A person is riding a bike-0.mp4",
                "video_results": true,
                "cor_num_per_video": 1
            },
            // ...
        ]
    ]
}
```
