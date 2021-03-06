package core;

import common.CheckResultCode;
import rule.RuleStrategy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 *
 * @param <T> the Type for DataWrapper data type.
 * @param <R> the Type for return Result.
 */
public abstract class RuleContext<T, R> {

    private AtomicBoolean pass = new AtomicBoolean(true);
    private RuleStrategy strategy;
    private final Lock lock = new ReentrantLock();

    public RuleContext(final RuleStrategy strategy) {
        this.strategy = strategy;
    }

    public void setCheckResult(String property, CheckResultCode code, DataWrapper<T, R> dataWrapper) {
        this.pass.set(false);
        lock.lock();
        try {
            storeResult(property, code, dataWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "RuleContext{" +
                "pass=" + pass +
                ", strategy=" + strategy +
                ", result=" + getResult() +
                '}';
    }

    protected abstract void storeResult(String property, CheckResultCode code, DataWrapper<T, R> dataWrapper);

    public Boolean getPassFlag() {
        return this.strategy.getPassFlagOnStrategy(this.pass.get());
    }

    public abstract R getResult();

}
