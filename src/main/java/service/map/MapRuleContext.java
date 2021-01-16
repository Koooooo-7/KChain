package service.map;

import core.DataWrapper;
import core.RuleContext;
import rule.Rule;
import rule.RuleStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class MapRuleContext extends RuleContext<Map<String, Object>, Map<String, String>> {

    private HashMap<String, String> result = new HashMap<>();

    public MapRuleContext(RuleStrategy strategy) {
        super(strategy);
    }

    @Override
    protected void storeResult(String property, Rule rule, DataWrapper<Map<String, Object>, Map<String, String>> dataWrapper) {
        result.put(property, rule.name());
    }

    @Override
    public Map<String, String> getResult() {
        return this.result;
    }


}
