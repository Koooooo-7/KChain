package core;


/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description ChainBuilder, reference by Guava CacheBuilder(com.google.common.cache.CacheBuilder).
 */

public final class ChainBuilder<T, G> {
    private ChainContext chainContext;
    private IChain<? extends T, ? extends G> chain;

    private ChainBuilder() {
    }


    public ChainBuilder<T, G> setChainContext(ChainContext chainContext) {
        this.chainContext = chainContext;
        return this;
    }

    public ChainBuilder<T, G> setChain(IChain<? extends T, ? extends G> chain) {
        this.chain = chain;
        return this;
    }

    public <T1 extends T, G1 extends G> Chain<T1, G1> build() {
        return new KChain<>(chainContext, chain);
    }

    public static ChainBuilder<Object, Object> newBuilder() {
        return new ChainBuilder<>();
    }


}
