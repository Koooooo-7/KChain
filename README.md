
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

**English | [中文](README.zh.md)**

> *`KChain`, a flexible and configurable chain.*	

---

## What is the problem


Sometimes, we will face a situations that we need to check various `properties set` such as `Map<String,Object>` or `Entity` entities ( named it as `properties set`).

Obviously, there will be one or more different verification rules for different properties.

So, it is necessary to have a `chain` structure to solve those problems.

`KChain` is an implementation to solve these problems.




The following two dimensions are usually considered:

**Check Properties**

- For a single `properties set`, such as `Map<String,Object>`

​       It needs to check whether its different properties match one or more rules. 

​       For example, the value of  the key called`name` in a `Map<String,Object>` can not be empty.

  

- For batch of `perperties set` , such as `List<User>`

​       It is necessary to check whether they meet certain rules with each other.

​      For example, such as *the same name of `User`* can not exist in the batch of `User` set.



**Rule Strategy** 

- Fail fast   

  When one of the rule conditions is not met, return directly. 

- Full check  

   Running all the verification rules, verify all the rule items for its perperties.

  

---

## How to solve it

There are some components which play an important role in `KChain`.

- `ChainContext`

  The context for the `chain` which can hold the informations you wanna use in the whole chain life circle.

  Such as some global parameters or special symbol.

  

- `DataWrapper`

  Wrapping the `properties set` and the `Rulecontext` together.

  

- `RuleContext`

  It is used to store the check result and the return flag bit of `Predicate` based on different check strategies.

  

- `Rule`

  It contains some rules for checking.

  There are three built-in rules for now:

  - `testNotEmpty`，whether the perperty value is empty based on the rule.

  - `testInCases`，whether the perperty value is in the given collection (` contains`).

  - `testOnCustomized`，the fully customized implementation  rule extension.

    

- `RuleStrategy`

  The check strategy.

  - `FULL_CHECK`

  - `FAIL_FAST ` 

    It is usually used to check a single `perperties set` to quickly know whether the `perperties set` is valid.
  
    
  
- `IChain`

  The interface of chain，build-in:

  - `Predicate`

    `test()`

  ​       Mainly for the verification of a single `properties set`, the verification rule of each property in the `perperties set` can be configured.
  

  - `Function`
  
    `apply()`
    
    Mainly for the verification of batch of `properties set`, the verification rule of  the property between each ` perperties set` can be configured.
    
    Build in the `duplicate rule ` .
    
    


- `ChainBuiler`

​       Build the chain to use, build in.

---

## How to use it

> All the basic usage you can find in`demo/App.java`.
>
> You can expand as much as you want, no matter the `Rule`, or the  implementations of getting property value.



Requests  **JDK8+**, the core is using the `@FunctionalInterface`.

- Maven

```xml
<dependency>
   <groupId>com.koy</groupId>
   <artifactId>kchain</artifactId>
   <version>1.0.0</version>
</dependency>
```



### Demo

---

### `Map`

Suppose that we need to verify the `name` and `age`  in `map`. The rules are as follows:

**In Single `Map`**

- `name` cannot empty
- `age`can not empty
- `age` should in the cases

**In Map Collections**

- `name` cannot duplicate in collections

- `age` cannot duplicate in collections



#### Build the single properties set chain

- `name` cannot empty
- `age` cannot empty
- `age` should in `[1,2,3,4,5]`

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



#### Build the batch properties set chain

- `name`  cannot duplicated in batch of properties set
- `age`cannot duplicated in batch of properties set

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





#### Usage

```java
 public static void demoOnMapBuilder(List<HashMap<String, Object>> maps) {
        // wrap date to dataWrapper
        List<MapDataWrapper> mapDataWrappers = Lists.newArrayListWithCapacity(2);
        for (HashMap<String, Object> map : maps) {
            MapDataWrapper mapDataWrapper = new MapDataWrapper(map, new MapRuleContext(RuleStrategy.FULL_CHECK));
            mapDataWrappers.add(mapDataWrapper);
        }

        // build the chain with FULL_CHECK strategy
        Chain<MapDataWrapper, List<MapDataWrapper>> chain = ChainBuilder.newBuilder()
                .setChainContext(new ChainContext(RuleStrategy.FULL_CHECK))
                .setChain(new MapPropertiesCheckChain())
                .build();

        // Verify the property in the map
        // There use 6 thread to execute it to make it speeder
        chain.test(mapDataWrappers);

        // Verify the rules between batch properties sets
        chain.apply(mapDataWrappers);

        // show the result
        for (MapDataWrapper mapDataWrapper : mapDataWrappers) {
            System.out.println(mapDataWrapper.getRuleContext().getResult().toString());
        }

    }
```

