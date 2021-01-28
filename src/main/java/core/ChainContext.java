package core;


import rule.RuleStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class ChainContext {

    // the cache you may wanna share on chain.
    // the life circle is only from the chain test/apply begin to end.
    // when there is multi threads, it is only shared in same subSize batch.
    private static final ThreadLocal<CacheEntity> cache = new ThreadLocal<>();
    private RuleStrategy ruleStrategy;

    public ChainContext() {
    }

    public ChainContext(RuleStrategy ruleStrategy) {
        this.ruleStrategy = ruleStrategy;
    }

    public RuleStrategy getRuleStrategy() {
        return ruleStrategy;
    }

    public void setRuleStrategy(RuleStrategy ruleStrategy) {
        this.ruleStrategy = ruleStrategy;
    }

    public void setCache(String key, Object val) {
        CacheEntity entity = cache.get();
        if (Objects.isNull(entity)) {
            entity = CacheEntity.supplier.get();
        }
        entity.put(key, val);
        cache.set(entity);
    }

    public Optional<Object> getCache(String key) {
        CacheEntity entity = cache.get();
        // in case of calling getting before setting.
        if (Objects.isNull(entity)) {
            entity = CacheEntity.supplier.get();
            cache.set(entity);
        }
        return Optional.ofNullable(cache.get().get(key));
    }

    void removeCache() {
        cache.remove();
    }

    public interface ICache<V> {
        V get(String key);

        void put(String key, V val);
    }

    public static class CacheEntity implements ICache<Object> {

        private static final Supplier<CacheEntity> supplier = CacheEntity::new;
        private Map<String, Object> contain = new HashMap<>();

        @Override
        public Object get(String key) {
            return contain.get(key);
        }

        @Override
        public void put(String key, Object val) {
            this.contain.put(key, val);
        }
    }
}
