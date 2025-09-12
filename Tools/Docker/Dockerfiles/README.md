# Dockerfiles

Build images:

```bash
docker build -t sss:dev .
```

```bash
crpi-4u800xgxt7n61vri.cn-beijing.personal.cr.aliyuncs.com/opensourcetest/vllm-ascend090

m.daocloud.io/quay.io/ascend/vllm-ascend:v0.9.0rc1

swr.cn-southwest-2.myhuaweicloud.com/modelfoundry/dev_images/{用户名}/{镜像名}:{commit_id}
# mine:
swr.cn-southwest-2.myhuaweicloud.com/modelfoundry/dev_images/s00845128/sss_ubuntu_base:a6ab75a
```

[Coder 开发环境使用方法](https://github.com/cosdt/cosdt.github.io/issues/29)

```bash
# 挂载的目录没有必要的文件
cp /etc/skel/.bash_logout /etc/skel/.bashrc /etc/skel/.profile ~/
```
