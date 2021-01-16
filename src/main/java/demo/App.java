package demo;

import core.ChainContext;
import rule.RuleStrategy;
import service.map.MapDataWrapper;
import service.map.MapRuleContext;
import service.map.chain.MapPropertiesCheckChain;

import java.util.HashMap;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class App {

    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "");
        map.put("age", 11);
        MapRuleContext ruleContext = new MapRuleContext(RuleStrategy.FULL_CHECK);
        MapPropertiesCheckChain mapPropertiesCheckChain = new MapPropertiesCheckChain();
        mapPropertiesCheckChain.test(new ChainContext(), new MapDataWrapper(map, ruleContext));

    }
}
