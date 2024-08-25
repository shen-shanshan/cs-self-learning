# 高频 SQL 50 题（基础版）

> 刷题链接：[高频 SQL 50 题（基础版） - 学习计划 - 力扣（LeetCode）全球极客挚爱的技术成长平台](https://leetcode.cn/studyplan/sql-free-50/)

## 1  查询

### [1683. 无效的推文](https://leetcode.cn/problems/invalid-tweets/)

使用 `length()` 获取某个字段的长度。

```sql
select tweet_id from Tweets
where length(content) > 15;
```

## 2  连接

### [1581. 进店却未进行过交易的顾客](https://leetcode.cn/problems/customer-who-visited-but-did-not-make-any-transactions/)

`group by` 分组后，进行 `having` 的字段必须出现在 `select` 后的字段中，否则会报错。

`left join` 左关联后，若 `select` 后的字段中有在两张表中都同时存在的字段（`visit_id`），则应在字段前加上表名进行区分（`Visits.visit_id`），否则会报错。

```sql
# 正解
select customer_id, count(distinct(Visits.visit_id)) as count_no_trans
from Visits
left join Transactions on Visits.visit_id = Transactions.visit_id
where transaction_id is null
group by customer_id;

# 反例1
select customer_id, count(distinct(Visits.visit_id)) as count_no_trans
from Visits
left join Transactions on Visits.visit_id = Transactions.visit_id
group by customer_id
having transaction_id is null; 
# Unknown column 'transaction_id' in 'having clause'

# 反例2
select customer_id, count(distinct(visit_id)) as count_no_trans
from Visits
left join Transactions on Visits.visit_id = Transactions.visit_id
where transaction_id is null
group by customer_id; 
# Column 'visit_id' in field list is ambiguous
```

### [197. 上升的温度](https://leetcode.cn/problems/rising-temperature/)

自关联时，需要对同一张表使用两个不同的别名进行区分。

> 日期相关操作可以参考：[sql语句中日期相减的操作_sql日期加减-CSDN博客](https://blog.csdn.net/sungencheng/article/details/123672494)。

```sql
select w1.id
from Weather as w1
left join Weather as w2 on w1.recordDate = date_add(w2.recordDate, interval 1 day)
where w1.Temperature > w2.Temperature;
```

