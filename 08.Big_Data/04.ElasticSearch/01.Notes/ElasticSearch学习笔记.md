# 引言

ES 的业务场景主要为超大数据量的查询，当在日常业务中对千万级别的数据量进行分页查询时，使用 Mysql 进行处理很可能出现会超时，此时就可以考虑使用 ES 来进行搜索。

# 入门

## 1  倒排索引

### 1.1  基本概念

- 正排索引：通过 id（主键，索引）查找内容，再从内容中查找关键字；
- 倒排索引：通过 keyword（关键字，索引）关联 id。

> - 词条：索引中存储和查询的最小单元（分词）；
> - 词典：字典、词条的集合（底层可以是 B+ 树或哈希表）；
> - 倒排表：……。

### 1.2  搜索原理

1. 判断当前搜索的词汇是否在词典中，若是则进行下一步；
2. 找到当前词汇在倒排表中的指针，通过倒排表去获取当前词汇对应的文档 id 的列表；
3. 通过文档 id 去查找对应的数据（文档）。

> PUT 方法具有幂等性，发送同样的内容，返回的结果一样。简单理解，你创建 index 不能创建两个一样的，所以需要符合幂等性（使用 PUT），但是添加数据可以有多条一样的数据，这违反了幂等性所以添加数据不需要使用 PUT，而可以使用 POST。

## 2  分词器

- 当保存文档数据时，ES 会将文字进行分词，并将拆解后的数据保存到倒排索引中；
- 当进行查询时，ES 也会将查询内容进行分词，并在倒排索引中去进行匹配（全文检索匹配：`match`，类似于模糊查询）。

## 3  搜索配置

我们可以为索引数据中的多个字段分别设置不同的搜索配置：

- `"type": "text"`：该字段为文本，可以被分词；
- `"type": "keyword"`：该字段为关键词，不能被分词，需要精确匹配；
- `"index": true or false`：该字段是否能被索引。

![Snipaste_2023-08-30_01-04-49](ElasticSearch学习笔记.assets/Snipaste_2023-08-30_01-04-49.png)

## 4  文档分析

文档分析：将一块文本拆分成适合于倒排索引的独立的词条。

分析器包括：

- 字符过滤器：对特殊内容进行转换。
- 分词器：字符串 -> 词条。
- Token 过滤器：将数据装换为特定的格式。

> ES 的内置分析器包括：标准分析器、简单分析器、空格分析器、语言分析器等。
>

# 基础功能

## 1  索引操作

### 1.1  创建索引

下面的请求会自动创建一个名为 `customer` 的索引（如果不存在），然后添加一个 ID 为 1 的新文档，同时存储并建立 `firstname` 和 `lastname` 字段的索引。

```
POST /customer/_doc/1
{
  "firstname": "Jennifer",
  "lastname": "Walters"
}
```

> 注意：创建文档（_doc）时，需要指定唯一性标识（即 _id，类似于数据表的主键），既可以手动指定，也可以使用 POST 自动生成。

![image-20230916215743691](ElasticSearch学习笔记.assets/image-20230916215743691.png)

### 1.2  批量添加数据

下面的请求会在 `customer` 索引下一次性添加多条文本数据。

```
PUT customer/_bulk
{ "create": { } }
{ "firstname": "Monica","lastname":"Rambeau"}
{ "create": { } }
{ "firstname": "Carol","lastname":"Danvers"}
{ "create": { } }
{ "firstname": "Wanda","lastname":"Maximoff"}
{ "create": { } }
{ "firstname": "Jennifer","lastname":"Takeda"}
```

> 注意：这里并没有为每条数据指定唯一性标识（_id），此时该 POST 请求会自动为它们分配唯一性标识（很复杂，不易辨认）。

![image-20230916222241331](ElasticSearch学习笔记.assets/image-20230916222241331.png)

> 注意：ES 不允许修改索引信息，因为一旦修改索引，会导致大量的数据重新分配，所以只允许重新建立索引。

### 1.3  查询索引

下面的请求会获取 `customer` 索引下 `_id` 为 1 的文档数据。

```
GET /customer/_doc/1
```

> 注意：一个索引下的每一条数据都由多个字段（这里是 `firstname` 和 `lastname`）组成，除此之外，还包括 `_index`（归属的索引）、`_id`（唯一性标识）、`_score`（得分，即与搜索文本的匹配程度）等信息。

![image-20230916215628771](ElasticSearch学习笔记.assets/image-20230916215628771.png)

> 注意：新文档可以立即从集群中的任何节点获取。

## 2  搜索操作

### 2.1  匹配所有数据

```
GET index/_search
{
	"query": {
		"match_all": {...}
	}
}
```

> 说明：
>
> - `index`：索引；
> - `_search`：搜索该索引下的所有数据；
> - `query`：指定查询条件；
> - `match_all`：匹配所有数据。

### 3.2  分词查询

```
GET index/_search
{
	"query": {
		"match": {
			"FIELD": "TEXT"
			"name": "zhang san"
		}
	}
}
```

> 说明：
>
> - `match`：分词查询，会查出所有 `FIELD` 字段中包含 `TEXT` 中的分词的数据；
> - `FIELD`：需要匹配的字段；
> - `TEXT`：需要匹配的内容，ES 会将其进行分词（"zhang" 和  "san" 会被分为两个关键词）并保存。

### 3.3  精确查询

```
GET index/_search
{
	"query": {
		"term": {
			"name": {
				"value": "zhang san"
			}
		}
	}
}
```

> 说明：
>
> - `term`：ES 会将 `TEXT` 当作一个完整的关键词去进行匹配（这里的 name 不会做分词，ES 会将 "zhang san" 作为一个完整的关键词）。

## 4  聚合搜索

## 5  索引模板

## 6  中文分词

中文分词插件：IK 分词器。

## 7  文档评分

ES 的评分机制是一个基于词频和逆文档词频的公式，简称为 TF-IDF 公式。

> - TF（term frequency）：词频，搜索文本中的各个词条（term）在查询文本中出现了多少次。
> - IDF（inverse document frequency）：逆文档词频，搜索文本中的各个词条（term）在整个索引的所有文档中出现了多少次，出现的次数越多，说明越不重要，也就越不相关，得分就越低。
>

得分越高（和搜索文本中的各个词条越匹配）的查询文本会被放在搜索结果的越前面。

# 进阶功能

## 1  EQL

事件查询语言（基于时间序列）。

## 2  SQL

结构化查询语言。

## 3  NLP

为了支持类 BERT 模型，ES 通过 PyTorch 模型支持大多数最常见的 NLP 任务。

# 集群部署

## 1  索引分片

- 同一个索引的数据分为 P0、P1、P2 三个分片。
- 每个分片都可以分为：1 主 + N 备（绿色为主（master），紫色为副本）。

![image-20230831001104554](ElasticSearch学习笔记.assets/image-20230831001104554.png)

- 新增数据：路由算法（`hash(主键id) % 分片数`）。
- 查询数据：可以查每一个节点（轮询策略）。

> 注意：同一分片的主备数据不能放在同一个节点上。

## 2  写入数据

![image-20230831002611037](ElasticSearch学习笔记.assets/image-20230831002611037.png)

## 3  读取数据

![image-20230831003211307](ElasticSearch学习笔记.assets/image-20230831003211307.png)

## 4  更新数据

![image-20230831003551210](ElasticSearch学习笔记.assets/image-20230831003551210.png)

- P1 协调节点将更新请求转发到目标节点 P0（主）。
- 考虑到并发写加锁的问题，需要不断地更新（主），并将更新同步到副本。

> 延时 = 主分片的延时 + 并行写入副本的最大延时。

# 性能优化

# Java SDK

# 参考资料

## 1  学习视频

- [【尚硅谷】ElasticSearch教程入门到精通（基于ELK技术栈elasticsearch 7.x+8.x新特性）_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1hh411D7sb/?spm_id_from=333.999.0.0)。

## 2  技术文档

- 入门教程：[肝了两小时的 Elasticsearch 保姆级入门 (qq.com)](https://mp.weixin.qq.com/s/O7dv7Np_PDPpDSSOSsiQtg)；
- Elasticsearch 官方文档：https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/getting-started-java.html；
- Github 文档：https://github.com/elastic/elasticsearch。