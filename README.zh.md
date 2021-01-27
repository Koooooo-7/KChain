
# KChain
---
<p align="center">
<a href="https://codecov.io/gh/Koooooo-7/KChain">
    <img src="https://codecov.io/gh/Koooooo-7/KChain/branch/main/graph/badge.svg?token=URSIFCIfsY"/>
</a>
&nbsp;
<a href="https://travis-ci.com/Koooooo-7/KChain.svg?branch=main">
    <img src="https://travis-ci.com/Koooooo-7/KChain.svg?branch=main"/>
</a>
&nbsp;
<a href="https://opensource.org/licenses/MIT" rel="nofollow">
<img src="https://img.shields.io/badge/License-MIT-brightgreen.svg" alt="License" style="max-width:100%;">
</a>
&nbsp;
<a href="https://opensource.org/licenses/MIT" rel="nofollow">
<img src="https://img.shields.io/badge/Java-JDK8%2B-orange" alt="jdk8+"> 
</a>
</p>

**中文 | [English](README.md)**

> *`KChain`, 一个灵活可配置的校验链。*	

---

## 解决了什么问题

有时，我们会遇到需要对`Map<String,Object>`或者`Entity`实体（以下统称为`属性集合 prperties set`）等各种`属性集合`做校验的情况。

显然会对不同的属性有不同的一个或者多个校验规则。

这时，就需要有一个`链式`结构来对`属性集合`进行一套校验，对其不同的属性来跑一个或者多个校验规则。

`KChain`就是为了解决这些问题的一种实现。



通常会以下两个维度的情况来考虑：

**校验对象**

- 对于单个属性集合，如`Map<String,Object>`

  需要通过链式校验其不同的属性是否符合一个或者多个规则，如`key`为`name`的值不能为空。

  

- 对于批量的属性集合，如`List<User>`

  需要通过链式校验其彼此之间是否符合某些规则，如*该批次的`User`集合中不能存在同名的`User`*。

  

**校验策略**

- 快速失败

  当有一个校验条件不满足时，直接返回。

- 充分校验

  跑完所有的校验规则，对其属性校验其所有的规则项。

---

## 如何解决的

这里介绍`KChain`的几个重要的组件及其针对上面提出问题各解决了什么部分。

- `ChainContext`

  校验链的上下文，可以在其中放置在校验链中需要使用的校验信息，如校验所需的全局参数或特殊的标识符等。

  

- `DataWrapper`

  包装了单个的数据集(`T data`)和其对应的结果集上下文(`RuleContext`)，形成一一绑定。

  

- `RuleContext`

  用来存放校验结果和基于不同校验策略下的`Predicate`返回标志位。

  

- `Rule`

  这里是用来定义一些通用规则的地方，目前已经有的内置规则有3种：

  - `testNotEmpty`，检测属性值是否为空，具体检测规则自己实现。

  - `testInCases`，检测属性值是否在给定的集合之中(`contains`)。

  - `testOnCustomized`，完全自定义实现的规则扩展。

    

- `RuleStrategy`

  校验策略，校验链所依赖的校验策略。

  - `FULL_CHECK`

    

  - `FAIL_FAST ` 

    通常作用与对单个属性集合的校验来快速判断是否改属性集合合法
    
    

- `IChain`

  校验链接口，有两种。

  - `Predicate`

    `test()`

    主要可以针对单个属性集合的校验遍历，配置属性集合中每个属性的校验规则。

  - `Function`
  
    `apply()`
    
    主要可以针对批量的属性集合的校验遍历，配置批量属性集合中相互之间的校验，内置`查重`校验规则。
    
    


- `ChainBuiler`
  

构建校验链，内置。

---

## 怎么使用

> 基本的使用都在`demo`目录下演示，你可以尽情去扩展，不论是`Rule`中的校验规则，还是获取属性集合值或其他的各种实现。



**其核心使用的是`JDK8`的函数式接口，所以要求`JDK8+`**。

- Maven

```xml
<dependency>
   <groupId>com.koy</groupId>
   <artifactId>kchain</artifactId>
   <version>1.0.0</version>
</dependency>
```



### `Map`示例

假设我们需要校验`Map`中的`name`属性和`age`属性，其规则如下：

**单个Map中**

- `name` 不能为空。
- `age`不能为空。
- `age`要在给定的值选项内。

**Map集合中**

- `name` 不能重复。

- `age` 不能重复。



#### 构造属性检测校验链

- `name` 不能为空
- `age`不能为空
- `age`是否在`[1,2,3,4,5]`这几个值之中。

```java
    @Override
    public Predicate<MapDataWrapper> getPredicateChain(ChainContext ctx) {
        return Rule.NOT_EMPTY.<MapDataWrapper>testNotEmpty("name"
                , testEmptyRule("name")
                , resultProcessor("name", CheckResultCode.NOT_EMPTY))
                .and(Rule.NOT_EMPTY.testNotEmpty("age"
                        , testEmptyRule("age")
                        , resultProcessor("age", CheckResultCode.NOT_EMPTY)
                ))
                .and(Rule.IN_CASES.testInCases("age",
                        CommonTestComponents.testInCasesRule(dataWrapper -> Integer.valueOf(dataWrapper.getString("age"))
                                , Arrays.asList(1, 2, 3, 4, 5))
                        , resultProcessor("age", CheckResultCode.IN_CASES)
                ));

    }
```



#### 构造集合属性检测校验链

- `name`在批量的属性集合之中不能重复
- `age`在批量的属性集合之中不能重复

```java
    @Override
    public Function<List<MapDataWrapper>, List<MapDataWrapper>> getFunction(ChainContext ctx) {
        return CommonTestComponents.<MapDataWrapper>testDuplicatedInCollection(ctx
                , "name"
                , dataWrapper -> true, dataWrapper -> dataWrapper.getString("name"))
                .andThen(testDuplicatedInCollection(ctx
                        , "age"
                        , dataWrapper -> true
                        , dataWrapper -> dataWrapper.getString("age")));
    }
```



#### 使用

```java
 public static void demoOnMapBuilder(List<HashMap<String, Object>> maps) {
        // 包装数据成为 dataWrapper
        List<MapDataWrapper> mapDataWrappers = Lists.newArrayListWithCapacity(2);
        for (HashMap<String, Object> map : maps) {
            MapDataWrapper mapDataWrapper = new MapDataWrapper(map, new MapRuleContext(RuleStrategy.FULL_CHECK));
            mapDataWrappers.add(mapDataWrapper);
        }

        // 构建为 FULL_CHECK 策略的校验链
        Chain<MapDataWrapper, List<MapDataWrapper>> chain = ChainBuilder.newBuilder()
                .setChainContext(new ChainContext(RuleStrategy.FULL_CHECK))
                .setChain(new MapPropertiesCheckChain())
                .build();

        // 验证对于每个map中的属性的规则
        for (MapDataWrapper m : mapDataWrappers) {
            chain.test(m);
        }

        // 验证对于批量的maps之间的属性规则
        chain.apply(mapDataWrappers);

        // 简单打印结果
        for (MapDataWrapper mapDataWrapper : mapDataWrappers) {
            System.out.println(mapDataWrapper.getRuleContext().getResult().toString());
        }

    }
```

