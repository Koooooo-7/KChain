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

import java.util.HashMap;
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
    public void testMapChain() {
        Map<String, Object> map = mock(HashMap.class);
        when(map.get("name")).thenReturn("");
        when(map.get("age")).thenReturn(24);
        MapRuleContext ruleContext = new MapRuleContext(RuleStrategy.FULL_CHECK);

        MapDataWrapper mapDataWrapper = new MapDataWrapper(map, ruleContext);
        Assert.assertNull(mapDataWrapper.get(""));
        Assert.assertNull(mapDataWrapper.getString(""));
        Assert.assertNull(mapDataWrapper.getLong(""));
        mapPropertiesCheckChain.test(ctx, mapDataWrapper);
        Map<String, String> result = ruleContext.getResult();
        Assert.assertEquals(Rule.NOT_EMPTY.name(), result.get("name"));

    }
}
