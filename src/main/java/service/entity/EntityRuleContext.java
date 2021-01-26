package service.entity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import common.CheckResultCode;
import core.DataWrapper;
import core.RuleContext;
import rule.RuleStrategy;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class EntityRuleContext extends RuleContext<User, ListMultimap<String, String>> {

    private ListMultimap<String, String> result = ArrayListMultimap.create();

    public EntityRuleContext(RuleStrategy strategy) {
        super(strategy);
    }

    @Override
    protected void storeResult(String property, CheckResultCode code, DataWrapper<User, ListMultimap<String, String>> dataWrapper) {
        result.put(property, code.getReason());
    }

    @Override
    public ListMultimap<String, String> getResult() {
        return this.result;
    }


}
