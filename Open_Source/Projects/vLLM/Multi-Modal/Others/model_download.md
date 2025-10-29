```bash
Host Ascend-01-docker
    HostName 139.9.155.20
    Port 8333
    User root
    ForwardAgent yes
```

密码：333

进入容器后：

```bash
tmux attach -t download
cd /home/sss/model
python download.py
```
