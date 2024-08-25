# 学习路线

- 计算机概论与硬件相关知识
- Linux 的安装与常用命令
- Linux 的基本概念（用户、用户组、权限、程序等）
- vi 文本编辑器
- shell 与 shell 脚本
- 软件管理
- 网络基础

# 基本概念

## 1 Linux 简介

### 1.1 常见的操作系统

- 桌面操作系统
  - Windows：用户数量多
  - Mac OS：操作体验好，办公人士首选
  - Linux：用户数量少
- 服务器操作系统
  - UNIX：安全、稳定、付费
  - Linux：安全、稳定、免费、占有率高
  - Windows Server：付费、占有率低
- 移动设备操作系统
  - Android：基于 Linux、开源，主要用于智能手机、平板电脑和智能电视
  - IOS：苹果公司开发、不开源，用于：iPhone、iPad
- 嵌入式操作系统
  - Linux：机顶盒、路由器、交换机

### 1.2 Linux 系统版本

- 内核版：由 Linus Torvalds 及其团队开发、维护，免费、开源，负责控制硬件
- 发行版：基于 Linux 内核版进行扩展，由各个 Linux 厂商开发、维护，有收费版本和免费版本
  - Ubuntu：以桌面应用为主
  - RedHat：应用最广泛、收费
  - CentOS：RedHat 的社区版、免费
  - openSUSE：对个人完全免费、图形界面华丽
  - Fedora：功能完备、快速更新、免费
  - 红旗 Linux

## 2 目录结构

![image-20221114162703656](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114162703656.png)

- bin：存放二进制可执行文件
- boot：存放系统引导时使用的各种文件
- dev：存放设备文件
- etc：存放系统配置文件
- home：存放系统用户的文件
- lib：存放程序运行所需的共享库和内核模块
- opt：存放额外安装的可选应用程序包
- root：超级用户目录
- sbin：存放二进制可执行文件（只有 root 用户才能访问）
- tmp：存放临时文件
- usr：存放系统应用程序
- var：存放运行时需要改变数据的文件（如：日志文件）

![image-20221114163042030](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114163042030.png)

## 3 文件权限

![image-20221114203314648](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114203314648.png)

![image-20221114203428769](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114203428769.png)

![image-20221114203602787](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114203602787.png)

# 环境搭建

## 1 虚拟机安装

### 1.1 虚拟机（CentOS）安装

> 参考教程：
>
> - [使用VMware15安装Linux(CentOS6.5)_牧码ya的博客-CSDN博客](https://blog.csdn.net/tian_ci/article/details/107423462?ops_request_misc={"request_id"%3A"164534243716780264014334"%2C"scm"%3A"20140713.130102334.."}&request_id=164534243716780264014334&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~baidu_landing_v2~default-1-107423462.pc_search_result_positive&utm_term=VMware15安装centOS6.5&spm=1018.2226.3001.4187)

### 1.2 网卡设置

由于启动服务器时未加载网卡，导致 IP 地址初始化失败。

![image-20221114161833823](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114161833823.png)

修改网络初始化设置，设定网卡在系统启动时初始化，修改完成后需要重启服务器。

![image-20221114162016400](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114162016400.png)

![image-20221114162218411](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114162218411.png)

### 1.3 SSH 连接工具

SSH（Secure Shell）：建立在应用层基础上的安全协议。

常用的 SSH 连接工具：

- putty
- secureCRT
- xshell
- finalshell

## 2 常用软件安装

### 2.1 软件安装方式

- 二进制发布包安装：软件已经针对具体平台编译打包发布，只要解压、修改配置即可
- rpm 安装：软件已经安装 Redhat 的包管理规范进行打包，使用 rpm 命令进行安装，不能自行解决库依赖问题
- yum 安装：一种在线软件安装方式，本质上还是 rpm 安装，自动下载安装包并安装，安装过程中自动解决库依赖问题
- 源码编译安装：软件以源码工程的形式发布，需要自己编译打包

### 2.2 Jdk

![image-20221114194950023](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114194950023.png)

### 2.3 Tomcat

![image-20221114195102629](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114195102629.png)

![image-20221114195139155](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114195139155.png)

![image-20221114195427077](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114195427077.png)

![image-20221114195459399](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114195459399.png)

需要发放的端口：

- Tomcat：8080 端口
- MySQL：3306 端口
- Redis：6379 端口

开放端口后需要执行 `firewall-cmd --reload` 使其立即生效。

注意：CentOS 6.x 版本无法使用上面的防火墙相关命令。

![image-20221114200148786](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114200148786.png)

`kill -9 [进程id]`：表示强制结束进程

> 参考资料：
>
> - [查看linux虚拟机开放的端口号时出现:firewall-cmd: command not found问题 | 码农家园 (codenong.com)](https://www.codenong.com/cs109661136/)
> - [解决：Error: Cannot retrieve metalink for repository: epel. Please verify its path and try again_浅唱~幸福的博客-CSDN博客](https://blog.csdn.net/weixin_39643007/article/details/103837643)
> - [Cannot retrieve repository metadata (repomd.xml) for repository: epel. Please verify its path and tr_冰雪Love齐迹的博客-CSDN博客](https://blog.csdn.net/liqi_q/article/details/83063264)
> - [-bash:systemctl:command not found【CentOS 6.8】_ALeon>_+的博客-CSDN博客](https://blog.csdn.net/qq_44079145/article/details/124538898)

### 2.4 MySQL

![image-20221114200435241](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114200435241.png)

![image-20221114200519552](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114200519552.png)

![image-20221114200544825](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114200544825.png)

![image-20221114200603030](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114200603030.png)

![image-20221114200621147](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114200621147.png)

![image-20221114200703827](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114200703827.png)

![image-20221114200814872](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114200814872.png)

![image-20221114200835931](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114200835931.png)

使用新密码登录：`mysql -uroot -proot`

![image-20221114200912510](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114200912510.png)

> 参考资料：
>
> - [Linux安装Mysql server镜像安装失败 警告：mysql-community-server-5.7.24-1.el7.x86_64.rpm: 头V3 DSA/SHA1 Signature_超帆的博客-CSDN博客](https://blog.csdn.net/qq_210431387/article/details/109451033)

### 2.5 Git

![image-20221114202207764](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114202207764.png)

![image-20221114202232486](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114202232486.png)

### 2.6 Maven

将 Maven 的安装包上传到 Linux 进行安装并配置其环境变量。

![image-20221114202600048](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114202600048.png)

## 3 项目部署（Java Web）

首先，将在 IDEA 中开发的 Java Web 项目打包为 jar 包。

![image-20221114201150532](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114201150532.png)

![image-20221114201209319](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114201209319.png)

启动程序：`java -jar xxx.jar`

![image-20221114201415169](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114201415169.png)

![image-20221114201642823](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114201642823.png)

注意：这里的 `hello.log` 是相对路径，最后会在当前的 `app` 目录下生成日志文件。

查看日志：

- `more hello.log`
- `tail -f hello.log`

![image-20221114202000481](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114202000481.png)

![image-20221114203741856](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114203741856.png)

![image-20221114203810237](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114203810237-16684294911091.png)

# 常用命令

## 1 命令格式

`command [-options] [parameter]`：

- 命令
- 选项：对命令进行控制，可以省略
- 参数：命令的参数，可以为 0 个或多个

注意：

- `[...]`：代表可选
- 命令、选项、参数之间用空格进行分隔 

## 2 使用技巧

- `Tab` 键自动补全
- 连续两次 `Tab` 键，给出操作提示
- 使用上下箭头快速调出曾经使用过的命令
- 使用 `clear` 命令或 `Ctrl+l` 快捷键实现清屏

## 3 常用命令

### 3.1 ls

- 含义：list
- 作用：查看指定目录下的内容
- 语法：`ls [-al] [dir]`
  - `-a`：显示所有文件及目录（包括以 `.` 开头的隐藏文件）
  - `-l`：显示文件名及其详细信息

由于经常使用组合 `ls l`，因此可以直接简写为 `ll`。

### 3.2 pwd

- 含义：print work directory
- 作用：查看当前所在目录

### 3.3 cd

- 含义：change directory
- 作用：切换目录
- 语法：`cd [dir]`
  - `~`：用户的 `home` 目录
  - `.`：当前目录
  - `..`：上一级目录

### 3.4 touch

- 含义：touch
- 作用：如果文件不存在，则新建文件
- 语法：`touch [dir]`

### 3.5 mkdir

- 含义：make directory
- 作用：创建目录
- 语法：``mkdir [-p] [dir]``
  - `-p`：确保目录名称存在，不存在的就创建一个，可以通过该选项实现多层目录同时创建

### 3.6 rm

- 含义：remove
- 作用：删除指定文件
- 语法：``rm [-rf] [dir]``
  - `-r`：将目录及目录中的所有文件（目录）逐一（递归）删除
  - `-f`：无需确认，直接删除

### 3.7 cat

- 作用：显示文件内容
- 语法：`cat [-n] [fileName]`
  - `-n`：由 1 开始对所有输出的行数编号

### 3.8 more

- 作用：以分页的形式显示文件内容
- 语法：`more [fileName]`
- 操作说明：
  - `回车`：向下一行
  - `空格`：向下一屏
  - `b`：返回上一屏
  - `q` 或者 `Ctrl+c`：退出

### 3.9 tail

- 作用：查看文件末尾的内容
- 语法：`tail [-f] [fileName]`
  - `-f`：动态读取文件末尾内容并显示，通常用于日志文件的内容输出
- 操作说明：
  - `Ctrl+c`：退出
- 示例：
  - `tail /etc/profile`：显示文件末尾 10 行的内容
  - `tail -20 /etc/profile`：显示文件末尾 20 行的内容
  - `tail -f /etc/profile`：动态显示文件末尾的内容

### 3.10 rmdir

- 作用：删除空目录
- 语法：`rmdir [-p] dirName`
  - `-p`：若子目录被删除后导致父目录为空，则一并删除

### 3.11 cp

- 作用：用于复制文件或目录
- 语法：`cp [-r] source dest`
  - `-r`：如果复制的是目录需要使用此选项，此时将复制该目录下所有的子目录和文件

### 3.12 mv

- 作用：为文件或目录改名，或将文件或目录移动到其它位置
- 语法：`mv [source] [dest]`

### 3.13 tar

- 作用：对文件进行打包、解包、压缩、解压
- 语法：`tar [-zcxvf] fileName [files]`
  - `-z`：即 gzip，对文件进行压缩或解压
  - `-c`：即 create，创建新的包文件
  - `-x`：即 extract，从包文件中还原文件
  - `-v`：即 verbose，显示命令的执行过程
  - `-f`：即 file，指定包文件的名称
- 注意：
  - `xxx.tar`：只完成了打包，并没有压缩
  - `xxx.tar.gz`：打包并压缩
- 示例：
  - 打包：
    - `tar -cvf xxx.tar ./*`：将当前目录下的所有文件打包，并命名为 xxx.tar
    - `tar -zcvf xxx.tar ./*`：将当前目录下的所有文件打包并压缩，并命名为 xxx.tar
  - 解包：
    - `tar -xvf xxx.tar`：将 xxx.tar 解包，并将解包后的文件放在当前目录
    - `tar -zxvf xxx.tar.gz`：将 xxx.tar 解压和解包，并将解压和解包后的文件放在当前目录
    - `tar -zxvf xxx.tar.gz -C /usr/local`：将 xxx.tar 解压和解包，并将解压和解包后的文件放在 `/usr/local` 目录

### 3.14 vi/vim

- 作用：文本编辑
- 语法：`vi/vim [fileName]`

vim 由 vi 发展而来，功能更强大，实际工作中更常用，安装：`yum install vim`。

vim 三种模式：

- **命令模式**：打开文件后的默认模式
- **插入模式**：在命令模式下按 `i` 进入，按 `ESC` 回到命令模式
- **底行模式**：在命令模式下按 `:` 进入，可以执行 `wq` 保存退出、`q!` 不保存退出、`set nu` 显示行号；在命令模式下按 `/` 进入，可以对文件内容进行查找

### 3.15 find

- 作用：在指定目录下查找文件
- 语法：`find [dirName] -option fileName`
  - `-name`：根据文件名进行查找
- 示例：
  - `find . -name "*.java"`：在当前目录及其子目录下查找以 `.java` 结尾的文件

### 3.16 grep

- 作用：从指定文件中查找指定的文本内容
- 语法：`grep [word] [fileName]`
- 示例：
  - `grep hello *.java`：在当前目录下所有以 `.java` 结尾的文件中查找包含 hello 字符串的位置

### 3.17 防火墙

`systemctl` 是  Linux 的系统级命令，用于管理 Linux 中的服务，可以对服务进行启动、停止、重启、查看状态等操作。

- `systemctl status firewalld`：查看防火墙状态
- `systemctl stop firewalld`：暂时关闭防火墙
- `systemctl disable firewalld`：永久关闭防火墙
- `systemctl start firewalld`：开启防火墙

`firewall-cmd` 是 Linux 中专门用于控制防火墙的命令。

- `firewall-cmd --state`：查看防火墙状态
- `firewall-cmd --zone=public --add-port=8080/tcp --permanent`：开放指定端口
- `firewall-cmd --zone=public --remove-port=8080/tcp --permanent`：关闭指定端口
- `firewall-cmd --reload`：使改动立即生效
- `firewall-cmd --zone=public --list-ports`：查看开放的端口

注意：开放端口后需要执行 `reload` 使其生效。

### 3.18 补充

- `./`：运行文件
- `sudo xxx`：执行程序
- `sudo su`：进入 sudo 模式
- `sudo shutdown -h now`：关机
- `sudo reboot`：重启
- `sudo apt-get install package`：更新安装包
- `ultimate -a`：查看系统默认分配的资源
- `ultimate -s xxx`：设置栈空间的大小
- `zip -r myfile.zip test/`：将 test 目录打包成 myfile.zip
- `rpm -Uvh --force --nodeps *.rpm`：安装 `.rpm` 文件
- `clear`：清空当前终端
- `kill -9 [进程id]`：杀死进程
- `echo '...' >> 文件名`：追加新内容到文件
- `uname -a(或r)`：查看当前 Linux 内核
- `sudo dpkg --get-selections | grep linux-image`：查看已安装有哪些内核
- `sudo apt-get install linux-image-lowlatency linux-headers-lowlatency`：安装低延迟内核

## 4 常用快捷键

- `Ctrl+shift+c`：复制
- `Ctrl+shift+v`：粘贴
- `Ctrl+alt+t`：打开终端
- `Ctrl+f`：查找
- `Ctrl+c`：结束进程
- `Ctrl+z`：中断进程

# 常见错误

## 1 乱码

![image-20221114164053892](Linux%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0.assets/image-20221114164053892.png)

## 2 段错误（核心已转储）

访问的内存超出了系统给这个程序分配的内存空间。

程序运行在：代码段、数据段，当程序发生越界访问，CPU 会产生相应的保护。

程序访问了不可访问的内存，原因可能是：

- 该内存不存在
- 该内存是受到系统保护的
- 缺少文件、或文件损坏

错误原因：

- 访问了系统数据区：
  - 尤其是往系统保护的内存地址写数据，如：给一个指针以 0 地址（野指针、空指针）。
- 内存越界：
  - 这类问题的典型代表就是数组越界。
  - 缓存溢出也可能引起“段错误”，对于这种 `while(1){...}` 的程序，这个问题最容易发生，多此 sprintf 或着 strcat 有可能将某个 buffer 填满，造成缓存溢出。所以每次使用前，最好 memset 一下，不过要是一开始就是段错误，而不是运行了一会儿出现的，缓存溢出的可能性就比较小。
- 随意使用指针转换：
  - 一个指向一段内存的指针，除非确定这段内存原先就分配为某种结构或类型，或者这种结构或类型的数组，否则不要将它转换为这种结构或类型的指针，而应该将这段内存拷贝到一个这种结构或类型中，再访问这个结构或类型。这是因为如果这段内存的开始地址不是按照这种结构或类型对齐的，那么访问它时就很容易因为 bus error 而 core dump。
- 多线程程序使用了线程不安全的函数
- 多线程读写的数据未加锁保护
- 栈溢出：
  - Linux 默认给一个进程分配的栈空间大小为 8M。C++ 申请变量时，new 操作申请的变量在堆中，其他变量一般在存储在栈中。 因此，如果你数组开的过大变会出现这种问题。

> 参考资料：
>
> - [段错误（核心已转储）（core dumped）问题的分析方法_yuan-CSDN博客_段错误,核心已转储](https://blog.csdn.net/weixin_40877924/article/details/108762118)