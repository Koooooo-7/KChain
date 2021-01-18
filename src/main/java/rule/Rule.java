package rule;

import java.util.function.*;

public enum Rule {
    NOT_EMPTY,
    IN_CASE,
    HAS_ONE,
    DUPLICATED,
    ;


//    public static <T> Predicate<T> testNotEmpty(String property
//            , TrdFunction<String, T, Boolean, Boolean> failedPredicatedProcessor
//            , TrdFunction<String, T, Boolean, Boolean> resultProcessor) {
//        return testNotEmpty(property, (t) -> true, failedPredicatedProcessor, (t) -> true, resultProcessor);
//    }

    /**
     * testNotEmpty
     *
     * @param property        the key to mark the value
     * @param testOnPredicate pre check on if do the test
     * @param testRule        the rule how to test empty
     * @param resultProcessor how to deal the result
     * @param <T>             input params
     * @return Predicated<T>
     */
    public static <T> Predicate<T> testNotEmpty(String property
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
