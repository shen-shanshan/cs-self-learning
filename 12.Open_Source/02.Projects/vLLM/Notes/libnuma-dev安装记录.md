# libnuma-dev 安装记录

## 我的环境

Ubuntu 20.04

```bash
(base) sss@ascend910b-02:~/github$ uname -a
Linux ascend910b-02 4.19.90-vhulk2211.3.0.h1654.eulerosv2r10.aarch64 #1 SMP Mon Dec 4 18:15:37 UTC 2023 aarch64 aarch64 aarch64 GNU/Linux
(base) sss@ascend910b-02:/tmp$ arch
aarch64
(base) sss@ascend910b-02:/tmp$ dpkg --print-architecture
arm64
```

## 安装过程

```bash
(base) sss@ascend910b-02:~/github$ sudo apt-get install -y libnuma-dev
Reading package lists... Done
Building dependency tree       
Reading state information... Done
You might want to run 'apt --fix-broken install' to correct these.
The following packages have unmet dependencies:
 libnuma-dev : Depends: libnuma1 (= 2.0.12-1) but it is not going to be installed
 libtinfo-dev : Depends: libncurses-dev (= 6.2-0ubuntu2.1) but it is not going to be installed
 llvm-12-linker-tools : Depends: libllvm12 (>= 1:9~svn298832-1~) but it is not going to be installed
 llvm-12-tools : Depends: libllvm12 (>= 1:9~svn298832-1~) but it is not going to be installed
 python3-clang-12 : Depends: libclang-12-dev but it is not going to be installed
E: Unmet dependencies. Try 'apt --fix-broken install' with no packages (or specify a solution).
```

```bash
(base) sss@ascend910b-02:~/github$ sudo apt --fix-broken install
Reading package lists... Done
Building dependency tree       
Reading state information... Done
Correcting dependencies... Done
The following packages were automatically installed and are no longer required:
  libncurses-dev libtinfo-dev llvm-12-linker-tools llvm-12-tools python3-pygments
Use 'sudo apt autoremove' to remove them.
The following additional packages will be installed:
  libclang-12-dev libclang-common-12-dev libclang1-12 libgc1c2 libllvm12 libncurses-dev libobjc-9-dev libobjc4
Suggested packages:
  ncurses-doc
The following NEW packages will be installed:
  libclang-12-dev libclang-common-12-dev libclang1-12 libgc1c2 libllvm12 libncurses-dev libobjc-9-dev libobjc4
0 upgraded, 8 newly installed, 0 to remove and 32 not upgraded.
4 not fully installed or removed.
Need to get 43.4 MB of archives.
After this operation, 348 MB of additional disk space will be used.
Do you want to continue? [Y/n] y
Get:1 http://mirrors.aliyun.com/ubuntu-ports focal-updates/main arm64 libncurses-dev arm64 6.2-0ubuntu2.1 [327 kB]
Get:2 http://mirrors.aliyun.com/ubuntu-ports focal-updates/main arm64 libllvm12 arm64 1:12.0.0-3ubuntu1~20.04.5 [16.1 MB]
Get:3 http://mirrors.aliyun.com/ubuntu-ports focal/main arm64 libgc1c2 arm64 1:7.6.4-0.4ubuntu1 [74.0 kB]                                                                                            
Get:4 http://mirrors.aliyun.com/ubuntu-ports focal-updates/universe arm64 libobjc4 arm64 10.5.0-1ubuntu1~20.04 [39.5 kB]                                                                             
Get:5 http://mirrors.aliyun.com/ubuntu-ports focal-updates/universe arm64 libobjc-9-dev arm64 9.4.0-1ubuntu1~20.04.2 [211 kB]                                                                        
Get:6 http://mirrors.aliyun.com/ubuntu-ports focal-updates/universe arm64 libclang1-12 arm64 1:12.0.0-3ubuntu1~20.04.5 [4769 kB]                                                                     
Get:7 http://mirrors.aliyun.com/ubuntu-ports focal-updates/universe arm64 libclang-common-12-dev arm64 1:12.0.0-3ubuntu1~20.04.5 [3571 kB]                                                           
Get:8 http://mirrors.aliyun.com/ubuntu-ports focal-updates/universe arm64 libclang-12-dev arm64 1:12.0.0-3ubuntu1~20.04.5 [18.3 MB]                                                                  
Fetched 43.4 MB in 38s (1128 kB/s)
perl: warning: Setting locale failed.
perl: warning: Please check that your locale settings:
        LANGUAGE = (unset),
        LC_ALL = (unset),
        LANG = "en_US.UTF-8"
    are supported and installed on your system.
perl: warning: Falling back to the standard locale ("C").
debconf: delaying package configuration, since apt-utils is not installed
(Reading database ... 33832 files and directories currently installed.)
Preparing to unpack .../0-libncurses-dev_6.2-0ubuntu2.1_arm64.deb ...
Unpacking libncurses-dev:arm64 (6.2-0ubuntu2.1) ...
dpkg: error processing archive /tmp/apt-dpkg-install-yyg61m/0-libncurses-dev_6.2-0ubuntu2.1_arm64.deb (--unpack):
 unable to create '/usr/include/curses.h.dpkg-new' (while processing './usr/include/curses.h'): No such file or directory
dpkg-deb: error: paste subprocess was killed by signal (Broken pipe)
Preparing to unpack .../1-libllvm12_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb ...
Unpacking libllvm12:arm64 (1:12.0.0-3ubuntu1~20.04.5) ...
dpkg: error processing archive /tmp/apt-dpkg-install-yyg61m/1-libllvm12_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb (--unpack):
 unable to create '/usr/lib/aarch64-linux-gnu/libLLVM-12.so.1.dpkg-new' (while processing './usr/lib/aarch64-linux-gnu/libLLVM-12.so.1'): No such file or directory
dpkg-deb: error: paste subprocess was killed by signal (Broken pipe)
Preparing to unpack .../2-libgc1c2_1%3a7.6.4-0.4ubuntu1_arm64.deb ...
Unpacking libgc1c2:arm64 (1:7.6.4-0.4ubuntu1) ...
dpkg: error processing archive /tmp/apt-dpkg-install-yyg61m/2-libgc1c2_1%3a7.6.4-0.4ubuntu1_arm64.deb (--unpack):
 unable to create '/usr/lib/aarch64-linux-gnu/libgc.so.1.3.2.dpkg-new' (while processing './usr/lib/aarch64-linux-gnu/libgc.so.1.3.2'): No such file or directory
Preparing to unpack .../3-libobjc4_10.5.0-1ubuntu1~20.04_arm64.deb ...
Unpacking libobjc4:arm64 (10.5.0-1ubuntu1~20.04) ...
dpkg: error processing archive /tmp/apt-dpkg-install-yyg61m/3-libobjc4_10.5.0-1ubuntu1~20.04_arm64.deb (--unpack):
 unable to create '/usr/lib/aarch64-linux-gnu/libobjc.so.4.0.0.dpkg-new' (while processing './usr/lib/aarch64-linux-gnu/libobjc.so.4.0.0'): No such file or directory
Preparing to unpack .../4-libobjc-9-dev_9.4.0-1ubuntu1~20.04.2_arm64.deb ...
Unpacking libobjc-9-dev:arm64 (9.4.0-1ubuntu1~20.04.2) ...
dpkg: error processing archive /tmp/apt-dpkg-install-yyg61m/4-libobjc-9-dev_9.4.0-1ubuntu1~20.04.2_arm64.deb (--unpack):
 error creating directory './usr/lib/gcc/aarch64-linux-gnu/9/include/objc': No such file or directory
dpkg-deb: error: paste subprocess was killed by signal (Broken pipe)
Preparing to unpack .../5-libclang1-12_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb ...
Unpacking libclang1-12 (1:12.0.0-3ubuntu1~20.04.5) ...
dpkg: error processing archive /tmp/apt-dpkg-install-yyg61m/5-libclang1-12_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb (--unpack):
 unable to create '/usr/lib/aarch64-linux-gnu/libclang-12.so.1.dpkg-new' (while processing './usr/lib/aarch64-linux-gnu/libclang-12.so.1'): No such file or directory
dpkg-deb: error: paste subprocess was killed by signal (Broken pipe)
Preparing to unpack .../6-libclang-common-12-dev_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb ...
Unpacking libclang-common-12-dev (1:12.0.0-3ubuntu1~20.04.5) ...
dpkg: error processing archive /tmp/apt-dpkg-install-yyg61m/6-libclang-common-12-dev_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb (--unpack):
 error creating directory './usr/include/clang': No such file or directory
dpkg-deb: error: paste subprocess was killed by signal (Broken pipe)
Preparing to unpack .../7-libclang-12-dev_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb ...
Unpacking libclang-12-dev (1:12.0.0-3ubuntu1~20.04.5) ...
dpkg: error processing archive /tmp/apt-dpkg-install-yyg61m/7-libclang-12-dev_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb (--unpack):
 error creating symbolic link './usr/lib/aarch64-linux-gnu/libclang-12.so': No such file or directory
Errors were encountered while processing:
 /tmp/apt-dpkg-install-yyg61m/0-libncurses-dev_6.2-0ubuntu2.1_arm64.deb
 /tmp/apt-dpkg-install-yyg61m/1-libllvm12_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb
 /tmp/apt-dpkg-install-yyg61m/2-libgc1c2_1%3a7.6.4-0.4ubuntu1_arm64.deb
 /tmp/apt-dpkg-install-yyg61m/3-libobjc4_10.5.0-1ubuntu1~20.04_arm64.deb
 /tmp/apt-dpkg-install-yyg61m/4-libobjc-9-dev_9.4.0-1ubuntu1~20.04.2_arm64.deb
 /tmp/apt-dpkg-install-yyg61m/5-libclang1-12_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb
 /tmp/apt-dpkg-install-yyg61m/6-libclang-common-12-dev_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb
 /tmp/apt-dpkg-install-yyg61m/7-libclang-12-dev_1%3a12.0.0-3ubuntu1~20.04.5_arm64.deb
W: Could not open file /var/log/apt/eipp.log.xz - open (17: File exists)
W: Could not open file '/var/log/apt/eipp.log.xz' - EIPP::OrderInstall (17: File exists)
E: Sub-process /usr/bin/dpkg returned an error code (1)
```

The error message No such file or directory means that the .deb file does not exist, or does not exist in the current directory.

## 参考资料

- [<u>APT/dpkg errors! /var/lib/dpkg/status</u>](https://www.linuxquestions.org/questions/debian-26/apt-dpkg-errors-var-lib-dpkg-status-155478/)
- [<u>Unmet dependencies. Try 'apt --fix-broken install'- Conflict in linux-image</u>](https://askubuntu.com/questions/1373622/unmet-dependencies-try-apt-fix-broken-install-conflict-in-linux-image)
- [<u>dpkg-deb: error: paste subprocess was killed by signal (Broken pipe)</u>](https://blog.csdn.net/wtlll/article/details/121191625)
