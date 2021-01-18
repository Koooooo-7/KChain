package service.map;

import com.google.common.collect.ListMultimap;
import core.DataWrapper;
import core.RuleContext;

import java.util.Map;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class MapDataWrapper extends DataWrapper<Map<String, Object>, ListMultimap<String, String>> {
    public MapDataWrapper(Map<String, Object> data, RuleContext<Map<String, Object>, ListMultimap<String, String>> ruleContext) {
        super(data, ruleContext);
    }

    @Override
    public String getString(String property) {
        Object val = getData().get(property);
        if (val instanceof String) {
            return (String) val;
        }
        return "";
    }
}
