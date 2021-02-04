package core.chain;


import rule.RuleStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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

    // the Inheritable thread local which could be shared in multi threads.
    private static final InheritableThreadLocal<CacheEntity> inheritableCache = new InheritableThreadLocal<>();

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
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

    public void setInheritableCache(String key, Object val) {
        try {
            lock.writeLock().lockInterruptibly();
            try {
                CacheEntity entity = inheritableCache.get();
                if (Objects.isNull(entity)) {
                    entity = CacheEntity.supplier.get();
                }
                entity.put(key, val);
                inheritableCache.set(entity);
            } finally {
                lock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Optional<Object> getInheritableCache(String key) {
        try {
            lock.readLock().lockInterruptibly();
            try {
                CacheEntity entity = inheritableCache.get();
                // in case of calling getting before setting.
                if (Objects.isNull(entity)) {
                    entity = CacheEntity.supplier.get();
                    inheritableCache.set(entity);
                }
                return Optional.ofNullable(inheritableCache.get().get(key));
            } finally {
                lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    void removeCache() {
        cache.remove();
        inheritableCache.remove();
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
