package service.entity;

import com.google.common.collect.ListMultimap;
import core.DataWrapper;
import core.RuleContext;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class EntityDataWrapper extends DataWrapper<User, ListMultimap<String, String>> {
    public EntityDataWrapper(User user, RuleContext<User, ListMultimap<String, String>> ruleContext) {
        super(user, ruleContext);
    }
}
