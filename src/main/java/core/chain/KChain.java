package core.chain;

import java.util.List;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class KChain<T, G> implements Chain<T, G> {

    private ChainContext chainContext;
    private IChain<T, G> chain;

    public KChain(ChainContext chainContext,
                  IChain chain) {
        this.chainContext = chainContext;
        this.chain = chain;
    }


    @Override
    public void test(T data) {
        chain.test(this.chainContext, data);
        this.chainContext.removeCache();
    }

    public void test(List<T> data) {
        test(data, ChainExecutorFactory.Executor.DEFAULT.getExecutor());
    }

    @Override
    public void test(List<T> data, ChainExecutorFactory.IExecutor executor) {
        executor.exec(this.chain, chainContext, data);
        this.chainContext.removeCache();
    }

    @Override
    public void apply(G data) {
        try {
            chain.apply(this.chainContext, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.chainContext.removeCache();
    }
}
