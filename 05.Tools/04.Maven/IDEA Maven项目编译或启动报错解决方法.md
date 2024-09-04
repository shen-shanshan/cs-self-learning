# IDEA Maven 项目编译或启动报错解决方法

## 方法一：拉取最新依赖

拉取最新的 dev 分支代码，然后点击下图中的圆圈，重新加载依赖项。

![Snipaste_2024-09-04_19-55-21](images/Snipaste_2024-09-04_19-55-21.png)

## 方法二：强制更新依赖

运行以下命令，强制更新依赖项。

```
mvn clean install -s settings.xml -U
```

![Snipaste_2024-09-04_19-56-29](C:/Users/SSS/Desktop/Snipaste_2024-09-04_19-56-29.png)

## 方法三：清除 IDEA 缓存

文件 -> 清除缓存 -> 勾选所有项，点击【清除并重启】。

![Snipaste_2024-09-04_19-57-18](images/Snipaste_2024-09-04_19-57-18.png)

## 方法四：检查 settings 文件

在 `settings.xml` 文件的 `<localRepository>` 标签中定义的路径为本地仓库路径。

检查 IDEA 中设置的本地仓库路径是否与 `settings.xml` 文件中设置的一致。

![Snipaste_2024-09-04_19-58-16](images/Snipaste_2024-09-04_19-58-16.png)

## 方法五：删除 repository 中的某些文件

若 Maven 报错如下（这是 Maven 的一个 bug）：

```
[ERROR] Malformed \uxxxx encoding.
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
```

此时我们可以去项目的 `repository` 目录中搜索并删除所有的 `resolver-status.properties` 文件，然后再重新下载依赖。

## 方法六：取消勾选脱机模式

当 Maven 报错信息如下：

```
Cannot access mcr-huawei-product-maven xxx in offline mode and the artifact xxx has not been downloaded from it before.
```

此时可以取消勾选 Maven 中的【脱机工作】选项，这个选项的意思是不读取远程仓库，只读取本地已有的仓库。

![Snipaste_2024-09-04_19-58-50](images/Snipaste_2024-09-04_19-58-50.png)

上面报错的原因就是因为本地仓库缺少相应的依赖，还选择了脱机工作，导致下载不了相应的依赖。

## 方法七：配置启动 VM 选项

报错信息如下，原因是缺少微服务配置中心相关的配置项：

```
java.lang.IllegalStateException: Required key 'configcenter_url' not found
```

此时可以检查项目启动的 VM 选项是否缺少相应的配置，补充相应的配置即可。

![Snipaste_2024-09-04_19-59-28](images/Snipaste_2024-09-04_19-59-28.png)