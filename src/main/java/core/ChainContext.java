package core;


import rule.RuleStrategy;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class ChainContext {
    private RuleStrategy ruleStrategy;

    public ChainContext(RuleStrategy ruleStrategy) {
        this.ruleStrategy = ruleStrategy;
    }

    public RuleStrategy getRuleStrategy() {
        return ruleStrategy;
    }

    public void setRuleStrategy(RuleStrategy ruleStrategy) {
        this.ruleStrategy = ruleStrategy;
    }
}
