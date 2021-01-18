import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import core.ChainContext;
import core.DataWrapper;
import core.IChain;
import demo.DefaultPropertiesCheckChain;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rule.Rule;
import rule.RuleStrategy;
import service.map.MapDataWrapper;
import service.map.MapRuleContext;
import service.map.chain.MapPropertiesCheckChain;

import java.util.ArrayList;
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

    private MapPropertiesCheckChain mapPropertiesCheckChain;
    private ChainContext ctx;

    @Before
    public void init() {
        mapPropertiesCheckChain = new MapPropertiesCheckChain();
        ctx = new ChainContext();
    }

    @Test
    public void testMapChainOnPredicated() {
        Map<String, Object> map = mock(HashMap.class);
        when(map.get("name")).thenReturn("");
        when(map.get("age")).thenReturn(24);

        MapRuleContext ruleContext = new MapRuleContext(RuleStrategy.FULL_CHECK);
        MapDataWrapper mapDataWrapper = new MapDataWrapper(map, ruleContext);

        Assert.assertNull(mapDataWrapper.get(""));
        Assert.assertEquals("", mapDataWrapper.getString(""));
        Assert.assertNull(mapDataWrapper.getLong(""));

        mapPropertiesCheckChain.test(ctx, mapDataWrapper);
        ListMultimap<String, String> result = ruleContext.getResult();
        Assert.assertTrue(result.get("name").contains(Rule.NOT_EMPTY.name()));

    }


    @Test
    public void testMapChainOnFunction() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "");
        map.put("age", 24);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "Kobe");
        map2.put("age", 24);

        MapRuleContext ruleContext = new MapRuleContext(RuleStrategy.FULL_CHECK);
        MapRuleContext ruleContext2 = new MapRuleContext(RuleStrategy.FULL_CHECK);
        MapDataWrapper mapDataWrapper = new MapDataWrapper(map, ruleContext);
        MapDataWrapper mapDataWrapper2 = new MapDataWrapper(map2, ruleContext2);
        List<MapDataWrapper> dataWrappers = Lists.newArrayListWithCapacity(2);
        dataWrappers.add(mapDataWrapper);
        dataWrappers.add(mapDataWrapper2);
        ChainContext ctx = new ChainContext();
        MapPropertiesCheckChain mapPropertiesCheckChain = new MapPropertiesCheckChain();
        mapPropertiesCheckChain.test(ctx, mapDataWrapper);
        mapPropertiesCheckChain.apply(ctx, dataWrappers);

        Assert.assertTrue(mapDataWrapper.getRuleContext().getResult().get("name").contains(Rule.NOT_EMPTY.name()));
        Assert.assertTrue(mapDataWrapper.getRuleContext().getResult().get("age").contains(Rule.DUPLICATED.name()));
        Assert.assertFalse(mapDataWrapper2.getRuleContext().getResult().get("name").contains(Rule.NOT_EMPTY.name()));
        Assert.assertTrue(mapDataWrapper2.getRuleContext().getResult().get("age").contains(Rule.DUPLICATED.name()));
    }
}
