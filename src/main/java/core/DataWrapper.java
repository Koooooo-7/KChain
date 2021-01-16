package core;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class DataWrapper<T, R> {

    private T data;
    private RuleContext<T, R> ruleContext;

    public DataWrapper(T data, RuleContext<T, R> ruleContext) {
        this.data = data;
        this.ruleContext = ruleContext;
    }

    public T getData() {
        return data;
    }

    public Object get(String property) {
        return null;
    }

    public String getString(String property) {
        return null;
    }

    public Long getLong(String property) {
        return null;
    }

    public RuleContext<T, R> getRuleContext() {
        return ruleContext;
    }


    public boolean getFlag() {
        return this.ruleContext.getPassFlag();
    }
}
