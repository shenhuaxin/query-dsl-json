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
    "$or": {
      "$and": {
        "IND_CODE": "CH002007006013",
        "DATA_TYPE": 1
      },
      "DATA_TYPE": 2
    }
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
