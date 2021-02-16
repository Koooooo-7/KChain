package service.common;

import common.CheckResultCode;
import common.CommonUtil;
import core.chain.ChainContext;
import core.DataWrapper;
import rule.TrdConsumer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class CommonTestComponents {

    /**
     * Test in case on Collection cases.
     * @param getValue the way to get value
     * @param cases the cases in rule
     * @param <T> the test type
     * @param <G> the value type
     * @return
     */
    public static <T, G> Predicate<T> testInCasesRule(Function<T, G> getValue, Collection<G> cases) {
        return t -> cases.contains(getValue.apply(t));
    }

    public static <T extends DataWrapper> Function<List<T>, List<T>> testDuplicatedInCollection(ChainContext ctx, String property
            , Predicate<T> filter
            , Function<T, String> mapping) {
        return testDuplicatedInCollection(ctx, property, filter, mapping, ruleContextSolverOnDuplicated());
    }

    /**
     * test if duplicated property in properties sets.
     * @param ctx Chain Context
     * @param property the property need to check with others
     * @param filter the filter before check
     * @param mapping the mapping rules on grouping by
     * @param ruleContextSolverOnDuplicated the solver
     * @param <T> the type of property set, i.e. Map -> List<Map>.
     * @return
     */
    public static <T extends DataWrapper> Function<List<T>, List<T>> testDuplicatedInCollection(ChainContext ctx, String property
            , Predicate<T> filter
            , Function<T, String> mapping
            , TrdConsumer<ChainContext, String, List<T>> ruleContextSolverOnDuplicated) {
        return dataWrappers -> {
            Map<Object, List<T>> propertiesGroupByMap = dataWrappers
                    .parallelStream()
                    .filter(filter)
                    .collect(Collectors
                            .groupingBy(mapping));

            propertiesGroupByMap.forEach((key, val) -> {
                // has duplicated val in those properties
                if (CommonUtil.isNotEmpty(key) && val.size() > 1) {
                    ruleContextSolverOnDuplicated.accept(ctx, property, val);
                }
            });
            return dataWrappers;
        };
    }

    private static <T extends DataWrapper> TrdConsumer<ChainContext, String, List<T>> ruleContextSolverOnDuplicated() {
        return (ctx, property, values) -> {
            values.parallelStream().forEach(it -> {
                it.getRuleContext().setCheckResult(property, CheckResultCode.DUPLICATED, it);
            });
        };
    }
}
