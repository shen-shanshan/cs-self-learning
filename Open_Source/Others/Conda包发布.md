# 发布 conda 包

## Anaconda 账号

```
usrname: shen-shanshan
passwd: `7ABJaq?wY$V.$$K`
email: qq
```

## 操作步骤

```bash
git clone git@github.com:shen-shanshan/staged-recipes.git
cd /staged-recipes/recipes
mkdir vllm-ascend
cd vllm-ascend
vim meta.yaml
```

## 参考资料

- [Contributing packages | doc](https://conda-forge.org/docs/maintainer/adding_pkgs/#generating-the-recipe)
- [staged-recipes | github](https://github.com/conda-forge/staged-recipes)
- [Add vllm-ascend package | pr](https://github.com/conda-forge/staged-recipes/pull/28805)
- [上传和安装 conda 包](https://docs.anaconda.net.cn/anacondaorg/user-guide/packages/conda-packages/)
- [在 Conda 上发布 python 包](https://blog.csdn.net/sinat_41621566/article/details/118521088)
- [SPDX License List](https://spdx.org/licenses/)
