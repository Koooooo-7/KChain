package rule;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

@FunctionalInterface
public interface TrdFunction<T, G, U, R> {

    R apply(T t, G g, U u);

    default <V> TrdFunction<T, R, U, V> andThen(TrdFunction<? super R, ? super G, ? super U, ? extends V> after) {
        return null;
    }
}
