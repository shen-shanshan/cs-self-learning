你好，我自己开了个云服务器，方便有些时候下班之后在家处理一些紧急的事情，可以帮我添加这个IP到白名单上吗。

公网IP：47.107.44.120

需要添加的服务器：
Ascend01 A2 服务器，IP：139.9.155.20
Ascend A3 4机集群，IP：1.95.9.213
Coder Nvidia-A100
Coder Nvidia-T4
Coder Ascend A2 B4

---

配置 coder：

```bash
# curl -L https://coder.com/install.sh | sh
curl -fsSL https://codespaces.osinfra.cn/install.sh | sh
# check
coder version

coder login https://codespaces.osinfra.cn
# token: gu9u1KGYcA-AxOq4R12WXd1IHoRIYPZiY
# check
coder whoami
coder list

coder config-ssh
ssh coder.<workspaceName>

# 查看模板参数
coder templates list
coder templates list -o json
```
