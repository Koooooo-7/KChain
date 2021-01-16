package core;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public interface IChain<T, G> {

    default Function<G, G> getFunction(ChainContext ctx) {
        return t -> t;
    }

    default Predicate<T> getPredicateChain(ChainContext ctx) {
        return r -> true;
    }


    default void apply(ChainContext ctx, G g) {
        getFunction(ctx).apply(g);
    }

    default void test(ChainContext ctx, T t) {
        getPredicateChain(ctx).test(t);
    }

}
