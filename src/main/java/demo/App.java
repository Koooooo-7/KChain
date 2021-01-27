package demo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import core.*;
import rule.RuleStrategy;
import service.entity.EntityDataWrapper;
import service.entity.EntityRuleContext;
import service.entity.User;
import service.entity.chain.EntityPropertiesCheckChain;
import service.map.MapDataWrapper;
import service.map.MapRuleContext;
import service.map.chain.MapPropertiesCheckChain;

import java.util.HashMap;
import java.util.List;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class App {

    public static void main(String[] args) {
        demoOnMapWithFullCheck();
        demoOnMapWithFailFast();
        demoOnEntityWithFullCheck();
        main0();
    }

    public static void main0() {

        HashMap<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put("name", "");
        map.put("age", 24);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "Kobe");
        map2.put("age", 24);

        List<HashMap<String, Object>> maps = Lists.newArrayListWithCapacity(2);
        maps.add(map);
        maps.add(map2);

        demoOnMapBuilder(maps);
    }


    /**
     * Demo on map
     * map1 [name="", age=24]
     * map2 [name="Kobe", age=24]
     * <p>
     * test map1 that the name is empty
     * test map1 and map2 that the age is duplicated
     */
    public static void demoOnMapWithFullCheck() {
        // mock maps
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "");
        map.put("age", 24);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "Kobe");
        map2.put("age", 24);

        ChainContext ctx = new ChainContext(RuleStrategy.FULL_CHECK);

        // wrap date to dataWrapper
        MapRuleContext ruleContext = new MapRuleContext(ctx.getRuleStrategy());
        MapDataWrapper mapDataWrapper = new MapDataWrapper(map, ruleContext);
        MapRuleContext ruleContext2 = new MapRuleContext(ctx.getRuleStrategy());
        MapDataWrapper mapDataWrapper2 = new MapDataWrapper(map2, ruleContext2);
        List<MapDataWrapper> dataWrappers = Lists.newArrayListWithCapacity(2);
        dataWrappers.add(mapDataWrapper);
        dataWrappers.add(mapDataWrapper2);

        MapPropertiesCheckChain mapPropertiesCheckChain = new MapPropertiesCheckChain();
        mapPropertiesCheckChain.test(ctx, mapDataWrapper);
        mapPropertiesCheckChain.apply(ctx, dataWrappers);

        System.out.println(mapDataWrapper.getRuleContext().getResult());
        System.out.println(mapDataWrapper2.getRuleContext().getResult());
    }

    /**
     * Demo on map on Fail Fast
     * map1 [name="", age=24]
     * <p>
     * test map1 that the name is empty, it wont test the age next.
     */
    public static void demoOnMapWithFailFast() {
        // mock maps
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "");
        map.put("age", 24);

        ChainContext ctx = new ChainContext(RuleStrategy.FULL_CHECK);

        // wrap date to dataWrapper
        MapRuleContext ruleContext = new MapRuleContext(ctx.getRuleStrategy());
        MapDataWrapper mapDataWrapper = new MapDataWrapper(map, ruleContext);

        MapPropertiesCheckChain mapPropertiesCheckChain = new MapPropertiesCheckChain();
        mapPropertiesCheckChain.test(ctx, mapDataWrapper);

        System.out.println(mapDataWrapper.getRuleContext().getResult());
    }

    /**
     * Demo on entity
     * user1 [name="", age=24]
     * user1 [name="Kobe", age=24]
     * <p>
     * test user1 that the name is empty
     * test user1 and user2 that the age is duplicated
     */
    public static void demoOnEntityWithFullCheck() {
        User user = new User("", 24);
        User user2 = new User("Kobe", 24);


        ChainContext ctx = new ChainContext(RuleStrategy.FULL_CHECK);

        // wrap date to dataWrapper
        EntityRuleContext ruleContext = new EntityRuleContext(ctx.getRuleStrategy());
        EntityDataWrapper entityDataWrapper = new EntityDataWrapper(user, ruleContext);
        EntityRuleContext ruleContext2 = new EntityRuleContext(ctx.getRuleStrategy());
        EntityDataWrapper entityDataWrapper2 = new EntityDataWrapper(user2, ruleContext2);
        List<EntityDataWrapper> dataWrappers = Lists.newArrayListWithCapacity(2);
        dataWrappers.add(entityDataWrapper);
        dataWrappers.add(entityDataWrapper2);

        EntityPropertiesCheckChain entityPropertiesCheckChain = new EntityPropertiesCheckChain();
        entityPropertiesCheckChain.test(ctx, entityDataWrapper);
        entityPropertiesCheckChain.apply(ctx, dataWrappers);

        System.out.println(entityDataWrapper.getRuleContext().getResult());
        System.out.println(entityDataWrapper2.getRuleContext().getResult());
    }

    /**
     * @param maps input properties set
     *             map1 [name="", age=24]
     *             map2 [name="Kobe", age=24]
     */
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
        for (MapDataWrapper m : mapDataWrappers) {
            chain.test(m);
        }

        // Verify the rules between batch properties sets
        chain.apply(mapDataWrappers);

        // show the result
        for (MapDataWrapper mapDataWrapper : mapDataWrappers) {
            System.out.println(mapDataWrapper.getRuleContext().getResult().toString());
        }

    }
}
