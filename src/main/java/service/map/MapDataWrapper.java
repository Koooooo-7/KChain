package service.map;

import core.DataWrapper;
import core.RuleContext;

import java.util.Map;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class MapDataWrapper extends DataWrapper<Map<String, Object>, Map<String, String>> {
    public MapDataWrapper(Map<String, Object> data, RuleContext<Map<String, Object>, Map<String, String>> ruleContext) {
        super(data, ruleContext);
    }
}
