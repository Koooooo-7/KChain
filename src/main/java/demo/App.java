package demo;

import com.google.common.collect.Lists;
import core.ChainContext;
import rule.RuleStrategy;
import service.entity.EntityDataWrapper;
import service.entity.EntityRuleContext;
import service.entity.User;
import service.entity.chain.EntityPropertiesCheckChain;
import service.map.MapDataWrapper;
import service.map.MapRuleContext;
import service.map.chain.MapPropertiesCheckChain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class App {

    public static void main(String[] args) {
        demoOnMap();
        demoOnEntity();
    }


    /**
     * Demo
     * map1 [name="", age=24]
     * map2 [name="Kobe", age=24]
     * <p>
     * test map1 that the name is empty
     * test map1 and map2 that the age is duplicated
     */
    public static void demoOnMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "");
        map.put("age", 24);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "Kobe");
        map2.put("age", 24);

        // TODO: wrap the build steps in Builder.
        MapPropertiesCheckChain mapPropertiesCheckChain = new MapPropertiesCheckChain();
        ChainContext ctx = new ChainContext(RuleStrategy.FULL_CHECK);
        MapRuleContext ruleContext = new MapRuleContext(ctx.getRuleStrategy());
        MapDataWrapper mapDataWrapper = new MapDataWrapper(map, ruleContext);

        mapPropertiesCheckChain.test(ctx, mapDataWrapper);

        MapRuleContext ruleContext2 = new MapRuleContext(ctx.getRuleStrategy());
        MapDataWrapper mapDataWrapper2 = new MapDataWrapper(map2, ruleContext2);
        List<MapDataWrapper> dataWrappers = Lists.newArrayListWithCapacity(2);
        dataWrappers.add(mapDataWrapper);

        dataWrappers.add(mapDataWrapper2);
        mapPropertiesCheckChain.apply(ctx, dataWrappers);

        System.out.println(mapDataWrapper.getRuleContext().getResult());
        System.out.println(mapDataWrapper2.getRuleContext().getResult());
    }

    public static void demoOnEntity() {
        User user = new User("", 24);
        User user2 = new User("Kobe", 24);

        EntityPropertiesCheckChain entityPropertiesCheckChain = new EntityPropertiesCheckChain();
        ChainContext ctx = new ChainContext(RuleStrategy.FULL_CHECK);
        EntityRuleContext ruleContext = new EntityRuleContext(ctx.getRuleStrategy());
        EntityDataWrapper entityDataWrapper = new EntityDataWrapper(user, ruleContext);

        entityPropertiesCheckChain.test(ctx, entityDataWrapper);

        EntityRuleContext ruleContext2 = new EntityRuleContext(ctx.getRuleStrategy());
        EntityDataWrapper entityDataWrapper2 = new EntityDataWrapper(user2, ruleContext2);
        List<EntityDataWrapper> dataWrappers = Lists.newArrayListWithCapacity(2);
        dataWrappers.add(entityDataWrapper);

        dataWrappers.add(entityDataWrapper2);
        entityPropertiesCheckChain.apply(ctx, dataWrappers);

        System.out.println(entityDataWrapper.getRuleContext().getResult());
        System.out.println(entityDataWrapper2.getRuleContext().getResult());
    }
}
