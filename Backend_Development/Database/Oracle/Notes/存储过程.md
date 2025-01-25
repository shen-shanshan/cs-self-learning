# Oracle 存储过程

## 基本概念

存储过程（Stored Procedure）是在大型数据库系统中，一组为了完成特定功能的 SQL 语句集，存储在数据库中，经过第一次编译后再次调用不需要再次编译，用户通过指定存储过程的名字并给出参数（如果该存储过程带有参数）来调用存储过程。

## 基础语法

### 创建包

在一个包中，可以定义多个存储过程。

```plsql
CREATE OR REPLACE PACKAGE BODY [包名]
IS
--定义存储过程
...
```

### 创建存储过程

```plsql
CREATE OR REPLACE PROCEDURE [存储过程名称] [别名]
AS (或 IS)
BEGIN
	...
END;
```

带参数的存储过程：`IN` 为入参，`OUT` 为出参。

```plsql
CREATE OR REPLACE PROCEDURE [存储过程名称] (param1 IN TYPE, param2 OUT TYPE) --定义参数
AS
	--定义变量
    vs_msg VARCHAR2(4000);
    ...
    --定义游标（一个可以遍历的结果集） 
    CURSOR [游标名称] IS
        SELECT ...
        FROM ...
        WHERE ...
BEGIN
	--写入变量
	SELECT COUNT(*) INTO [变量]
	FROM [表A]
    WHERE ...
    --判断结构
    IF(...) THEN
    	...
    ELSIF(...) THEN
    	...
    ELSE
    	RAISE [异常名称]
	END IF;
	--提交结果
	COMMIT; 
EXCEPTION
	--异常处理（OTHERS表示除了声明外的任意错误）
	WHEN OTHERS THEN
		...
	ROLLBACK
END;
```

### 创建视图

```plsql
CREATE OR REPLACE VIEW [视图名称] [别名]
SELECT ...
FROM ...
WHERE ...
```

> 注意：在视图中只能用 `AS` 不能用 `IS`。

### 创建游标

```plsql
...
```

### SELECT INTO

`SELECT INTO` 语句主要用于将查询到的数据写入到变量中。

> 注意：
>
> - 只适用于查询返回单行的情况，如果查询返回多行结果，将会报错；
> - 查询的列数和变量数必须匹配；
> - 如果查询没有返回任何结果，那么将会抛出 `NO_DATA_FOUND` 异常。因此，在使用 `SELECT INTO` 语句之前，最好先确定查询结果确实存在；
> - 需要确保变量的类型与查询的列的类型相匹配。可以使用表的列定义来定义变量的类型，以确保匹配。

示例：

```plsql
CREATE OR REPLACE PROCEDURE get_employee_name(emp_id IN NUMBER)
IS
	--定义一个变量来存储查询结果（使用表的列定义来定义变量的类型，以确保匹配）
    v_employee_name employees.employee_name%TYPE; 
BEGIN
	SELECT employee_name INTO v_employee_name
	FROM employees
	WHERE employee_id = emp_id;
END;
```

### MERGE INTO

`MERGE INTO` 语句主要用于向目标表写入数据。

示例：

```plsql
--写入目标表target
MERGE INTO target_table target
--将查询出的数据当作tmp临时表
USING
(
	--创建临时表
    WITH tmp AS ...
    --查询数据
    SELECT ... FROM tmp, ...(其它表)
) tmp
--限定条件
ON target.id = tmp.id
--符合条件时，更新
WHEN MATCHED THEN
	UPDATE SET ... = ...
--不符合条件时，新增
WHEN NOT MATCHED THEN
	INSERT (...) VALUES (...)
EXCEPTION
WHEN OTHERS THEN
	...
COMMIT;
```

### WHEN THEN

```plsql
--当某个字段为空，则...
WHEN ... IS NOT NULL
THEN ...
```

### CASE WHEN

分为普通式（与编程语言中常见的 `case` 用法类似）和搜索式。

搜索式：

```plsql
CASE
WHEN [条件1] THEN 语句块1;
WHEN [条件2] THEN 语句块2;
ELSE 语句块3;
END CASE;
```

示例：

```plsql
--根据条件为某个字段赋值为Y或N
CASE
WHEN EXISTS (SELECT 1 FROM ...)
THEN 'Y'
ELSE 'N'
END
```

### EXISTS

```plsql
--查(T1表的a字段的值 在 T2表的a字段的值中存在)的数据
SELECT * FROM T1 WHERE EXISTS (
	SELECT 1 FROM T2 WHERE T1.a = T2.a
)
```

示例：

```plsql
WHERE ...
AND ...
--排除某些数据
AND NOT EXITS
(
	SELECT 1
    FROM ...
    WHERE ...
)
```

> `SELECT 1`：增加一个临时列，每行的列值是写在 `SELECT` 后的数。
>
> 为什么 `EXITS` 后面要使用 `SELECT 1` ？
>
> 因为 `SELECT 1` 的效率更高。从查询效率上来说，`SELECT 1` > `SELECT anycol` > `SELECT *`，因为不用查字典表（字段列表）。
>
> `SELECT 1` 和 `SELECT *` 从作用上来说是没有差别的，都是查看是否有记录，一般是作条件查询用的。其中，`SELECT 1` 中的 `1` 是一个常量（可以为任意数值），查到的所有行的值都是它。
>
> 参考资料：
>
> - [select 1 from ... sql语句中的1代表什么意思？_sql select 1 from-CSDN博客](https://blog.csdn.net/wolovedaima123/article/details/81070484)。

### WITH AS

`WITH AS` 是一个查询语句，用于构建一张临时表，之后便可以多次使用它进行数据的分析和处理。

> 为什么要使用 `WITH AS` 语句？
>
> 好处是增加了 SQL 的易读性。当需要构造多个子查询时，使用 `WITH AS` 语句会使代码结构更加清晰。并且可以实现“一次分析，多次使用”，减少了读表的次数，提升了 SQL 的性能。

### 常用函数

#### trunc()

```plsql
--按指定的format截取date，默认为使用最近的日期（当前日期）截取
trunc(date, [format])
```

#### instr()

```plsql
...
```

#### nvl()

```plsql
--若字段a不为空，则取a的值；否则取默认值0
nvl(a, 0)
```

### 常用符号

#### :=

赋值函数，相当于 `=`。

#### =>

调用存储过程时，`=>` 用于指定参数名进行调用（传参），可以跳过一些有默认值的参数。

#### <>

不等于。

#### ..

表示数值的范围。

```plsql
--1到9
num := 1..9
```

#### ||

字符串拼接。

```plsql
str := "my name is " || name || "."
```

#### (+)

```plsql
...
```

## 实际案例

BEGIN、EXCEPTION、END 这三个是一个层级的。

一个 MERGE INTO 是一个整体，结尾要有分号，EXCEPTION、END 结尾也要有分号。