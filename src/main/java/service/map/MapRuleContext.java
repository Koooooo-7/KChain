package service.map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import common.CheckResultCode;
import core.DataWrapper;
import core.RuleContext;
import rule.RuleStrategy;

import java.util.Map;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class MapRuleContext extends RuleContext<Map<String, Object>, ListMultimap<String, String>> {

    private ListMultimap<String, String> result = ArrayListMultimap.create();

    public MapRuleContext(RuleStrategy strategy) {
        super(strategy);
    }

    @Override
    protected void storeResult(String property, CheckResultCode code, DataWrapper<Map<String, Object>, ListMultimap<String, String>> dataWrapper) {
        result.put(property, code.name());
    }

    @Override
    public ListMultimap<String, String> getResult() {
        return this.result;
    }


}
