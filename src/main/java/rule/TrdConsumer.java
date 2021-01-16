package rule;

import java.util.Objects;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

@FunctionalInterface
public interface TrdConsumer<T, G, U> {

    void accept(T t, G g, U u);

    default TrdConsumer<T, G, U> andThen(TrdConsumer<? super T, ? super G, ? super U> after) {
        Objects.requireNonNull(after);
        return (T t, G g, U u) -> {
            accept(t, g, u);
            after.accept(t, g, u);
        };
    }
}
