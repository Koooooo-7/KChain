package rule;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

@FunctionalInterface
public interface TrdFunction<T, G, U, R> {

    R apply(T t, G g, U u);

    default <V> TrdFunction<T, G, U, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, G g, U u) -> after.apply(apply(t, g, u));
    }
}
