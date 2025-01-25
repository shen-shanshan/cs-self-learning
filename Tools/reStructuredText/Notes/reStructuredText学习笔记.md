# reStructuredText 学习笔记

## 官方网站

- [<u>Sphinx</u>](https://www.sphinx-doc.org/en/master/)；
- [<u>sphinx_rtd_theme</u>](https://github.com/readthedocs/sphinx_rtd_theme)；
- [<u>Read the Docs</u>](https://about.readthedocs.com/?ref=readthedocs.org)。

## 入门教程

- [<u>Learn reStructureText</u>](https://learn-rst.readthedocs.io/zh-cn/latest/reST-%E5%85%A5%E9%97%A8.html)；
- [<u>sphinx 文档规范与模板</u>](https://ebf-contribute-guide.readthedocs.io/zh-cn/latest/README.html)；
- [<u>赵子清技术文章</u>](https://zzqcn.github.io/design/rest/index.html)。

## 环境搭建

安装依赖：

```
Sphinx
sphinx-autobuild
sphinx-rtd-theme
recommonmark
sphinxext-remoteliteralinclude
sphinx-copybutton
```

执行命令：

```bash
sphinx-quickstart
make html
```

双击 `build` 目录下的 `index.html` 即可以在浏览器中查看效果。

也可以在 VSCode 中实时预览 `.rst` 文件，需要添加以下配置：

```json
{
    "esbonio.sphinx.confDir": "conf.py文件所在的文件夹",
    "restructuredtext.linter.run": "off"
}
```

> [!NOTE]
> 需要安装 esbonio 服务器（官方说明：Starting from version 190.1.17, this feature is removed. You will be recommended to install Esbonio extension instead. To learn more about how to configure Esbonio, you can visit [this site](https://docs.esbon.io/en/latest/).）。
>
> 参考资料：[reStructuredText VSCode 官方文档](https://docs.lextudio.com/restructuredtext/)。
