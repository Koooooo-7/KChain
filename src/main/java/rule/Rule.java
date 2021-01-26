package rule;

import java.util.function.*;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public enum Rule {
    NOT_EMPTY,
    IN_CASES,
    HAS_ONE,
    CUSTOMIZED,
    ;


    /**
     * Test if empty
     *
     * @param property        the property of element
     * @param testEmptyRule   how to check if its value is empty
     * @param resultProcessor how to deal with the test result
     * @param <T>             the test object
     * @return predicate
     */
    public <T> Predicate<T> testNotEmpty(String property
            , Predicate<T> testEmptyRule
            , TrdFunction<String, T, Boolean, Boolean> resultProcessor) {
        assert NOT_EMPTY == this : "error type reference on Rule NOT_EMPTY";
        return test(property, (t) -> true, (s, t, predicatedResult) -> true, testEmptyRule, resultProcessor);
    }

    public <T> Predicate<T> testInCases(String property
            , Predicate<T> testInCasesRule
            , TrdFunction<String, T, Boolean, Boolean> resultProcessor) {
        assert IN_CASES == this : "error type reference on Rule IN_CASES";
        return test(property, (t) -> true, (s, t, predicatedResult) -> true, testInCasesRule, resultProcessor);
    }

    /**
     * Customer test
     *
     * @param func the predicated function
     * @param <T>  the test object
     * @return predicate
     */
    public <T> Predicate<T> testOnCustomized(Predicate<T> func) {
        assert CUSTOMIZED == this : "error type reference on Rule CUSTOMIZED";
        return func;
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
    private <T> Predicate<T> test(String property
            , Predicate<T> testOnPredicate, TrdFunction<String, T, Boolean, Boolean> failedPredicatedProcessor
            , Predicate<T> testRule, TrdFunction<String/*property*/
            , T/*input DataWrapper*/
            , Boolean/*result*/
            , Boolean/*flag*/> resultProcessor) {
        return t -> {
            if (!testOnPredicate.test(t)) {
                return failedPredicatedProcessor.apply(property, t, Boolean.FALSE);
            }
            boolean result = testRule.test(t);
            return resultProcessor.apply(property, t, result);
        };
    }

}
