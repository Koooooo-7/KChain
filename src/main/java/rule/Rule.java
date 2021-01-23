package rule;

import java.util.function.*;

public enum Rule {
    NOT_EMPTY,
    IN_CASE,
    HAS_ONE,
    ;


    public static <T> Predicate<T> testNotEmpty(String property
            , Predicate<T> testEmptyRule
            , TrdFunction<String, T, Boolean, Boolean> resultProcessor) {
        return test(property, (t) -> true, (s, t, predicatedResult) -> true, testEmptyRule, resultProcessor);
    }

    /**
     * The base structure of test.
     *
     * @param property        the key to mark the value
     * @param testOnPredicate pre check on if do the test
     * @param testRule        the rule how to test empty
     * @param resultProcessor how to deal the result
     * @param <T>             input params
     * @return Predicated<T>
     */
    public static <T> Predicate<T> test(String property
            , Predicate<T> testOnPredicate, TrdFunction<String, T, Boolean, Boolean> failedPredicatedProcessor
            , Predicate<T> testRule, TrdFunction<String, T, Boolean, Boolean> resultProcessor) {
        return t -> {
            if (!testOnPredicate.test(t)) {
                return failedPredicatedProcessor.apply(property, t, Boolean.FALSE);
            }
            boolean result = testRule.test(t);
            return resultProcessor.apply(property, t, result);
        };
    }

}
