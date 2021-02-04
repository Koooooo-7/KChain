import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import common.CheckResultCode;
import core.chain.ChainContext;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class PropertiesCheckChainTest {

    private static MapPropertiesCheckChain mapPropertiesCheckChain;
    private static EntityPropertiesCheckChain entityPropertiesCheckChain;
    private static ChainContext ctx;

    @BeforeAll
    public static void init() {
        mapPropertiesCheckChain = new MapPropertiesCheckChain();
        entityPropertiesCheckChain = new EntityPropertiesCheckChain();
        ctx = new ChainContext(RuleStrategy.FULL_CHECK);
    }

    @Test
    public void testMapChainOnPredicated() {
        Map<String, Object> map = mock(HashMap.class);
        when(map.get("name")).thenReturn("");
        when(map.get("age")).thenReturn(24);

        MapRuleContext ruleContext = new MapRuleContext(ctx.getRuleStrategy());
        MapDataWrapper mapDataWrapper = new MapDataWrapper(map, ruleContext);

        Assertions.assertNull(mapDataWrapper.get(""));
        Assertions.assertEquals("", mapDataWrapper.getString(""));
        Assertions.assertNull(mapDataWrapper.getLong(""));

        mapPropertiesCheckChain.test(ctx, mapDataWrapper);
        ListMultimap<String, String> result = ruleContext.getResult();
        Assertions.assertTrue(result.get("name").contains(CheckResultCode.NOT_EMPTY.getReason()));
        Assertions.assertTrue(StringUtils.isNotEmpty(ruleContext.toString()));

    }


    @Test
    public void testMapChainOnFunction() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "");
        map.put("age", 24);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "Kobe");
        map2.put("age", 24);

        MapRuleContext ruleContext = new MapRuleContext(ctx.getRuleStrategy());
        MapRuleContext ruleContext2 = new MapRuleContext(ctx.getRuleStrategy());

        MapDataWrapper mapDataWrapper = new MapDataWrapper(map, ruleContext);
        MapDataWrapper mapDataWrapper2 = new MapDataWrapper(map2, ruleContext2);

        List<MapDataWrapper> dataWrappers = Lists.newArrayListWithCapacity(2);
        dataWrappers.add(mapDataWrapper);
        dataWrappers.add(mapDataWrapper2);

        MapPropertiesCheckChain mapPropertiesCheckChain = new MapPropertiesCheckChain();
        mapPropertiesCheckChain.test(ctx, mapDataWrapper);
        mapPropertiesCheckChain.apply(ctx, dataWrappers);

        Assertions.assertTrue(mapDataWrapper.getRuleContext().getResult().get("name").contains(CheckResultCode.NOT_EMPTY.getReason()));
        Assertions.assertTrue(mapDataWrapper.getRuleContext().getResult().get("age").contains(CheckResultCode.DUPLICATED.getReason()));
        Assertions.assertFalse(mapDataWrapper2.getRuleContext().getResult().get("name").contains(CheckResultCode.NOT_EMPTY.getReason()));
        Assertions.assertTrue(mapDataWrapper2.getRuleContext().getResult().get("age").contains(CheckResultCode.DUPLICATED.getReason()));
    }

    @Test
    public void testEntityChainOnPredicated() {
        User user = new User("", 24);

        EntityRuleContext ruleContext = new EntityRuleContext(ctx.getRuleStrategy());
        EntityDataWrapper entityDataWrapper = new EntityDataWrapper(user, ruleContext);

        Assertions.assertNull(entityDataWrapper.get(""));
        Assertions.assertNull(entityDataWrapper.getString(""));
        Assertions.assertNull(entityDataWrapper.getLong(""));

        entityPropertiesCheckChain.test(ctx, entityDataWrapper);

        ListMultimap<String, String> result = ruleContext.getResult();
        Assertions.assertTrue(result.get("name").contains(CheckResultCode.NOT_EMPTY.getReason()));
        Assertions.assertTrue(StringUtils.isNotEmpty(ruleContext.toString()));
    }
}
