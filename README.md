# query-dsl-json

### 介绍

使用 JSON 作为查询条件对数据进行查询。

### 案例

```json
{
  "from": "T_ADS_DIM_APP_DATA",
  "query": {
    "IND_CODE": {
      "$like": "CH002007006013%"
    }
  }
}

select * from `T_ADS_DIM_APP_DATA` where (`IND_CODE` like 'CH002007006013%')
```

```json
{
  "select": "ID, IND_VALUE",
  "from": "T_ADS_DIM_APP_DATA",
  "query": {
    "IND_CODE": "CH002007006013",
    "DATA_TYPE": 1
  }
}
select `ID`, `IND_VALUE` from `T_ADS_DIM_APP_DATA` where (`IND_CODE` = 'CH002007006013' and `DATA_TYPE` = 1)
```

```json
{
  "select": "ID, IND_VALUE",
  "from": "T_ADS_DIM_APP_DATA",
  "query": {
    "$or": [
      {
        "$and": {
          "IND_CODE": "CH002007006013",
          "DATA_TYPE": 1
        }
      },
      {
        "DATA_TYPE": 2
      }
    ]
  }
}

select `ID`, `IND_VALUE` from `T_ADS_DIM_APP_DATA` where (((`IND_CODE` = 'CH002007006013' and `DATA_TYPE` = 1) or `DATA_TYPE` = 2))
```

### 关键字

| 关键字  | 值      |
|------|--------|
| $eq  | =      |
| $ne  | !=     |
| $gt  | \>     |
| $gte | \>=    |
| $lt  | \<     |
| $lte | \<=    |
| $in  | in     |
| $nin | not in |
|      |        |
| $and | and    |
| $or  | or     |
| $not | not    |


### 注
Q: 是否存在 sql 注入问题？

A：对于可能存在的 sql 注入问题，使用了以下两种方式
1. 使用 prepareStatement 对 sql 进行预编译，方式 参数注入。
2. 对表名、字段名 上使用定界符（mysql: `, DB2: "），防止字段的sql注入。但是这样的话，字段名和表名是大小写敏感的。请注意。
> 使用 sqlmap 对接口进行了sql注入检查。现有代码情况下，未发现sql注入风险。


### 联系方式
邮箱： shx_jx@163.com