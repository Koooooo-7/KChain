package core.chain;

import java.util.List;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public interface Chain<T, G> {

    void test(T data);

    void test(List<T> data);

    void test(List<T> data, ChainExecutorFactory.IExecutor executor);

    void apply(G data);
}
