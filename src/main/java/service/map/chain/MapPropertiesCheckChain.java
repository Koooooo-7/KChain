package service.map.chain;

import common.CheckResultCode;
import common.CommonUtil;
import core.ChainContext;
import core.IChain;
import rule.Rule;
import rule.TrdConsumer;
import rule.TrdFunction;
import service.map.MapDataWrapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class MapPropertiesCheckChain implements IChain<MapDataWrapper, List<MapDataWrapper>> {

    private static Predicate<MapDataWrapper> testEmptyRule(String property) {
        return mapDataWrapper -> CommonUtil.isNotEmpty(mapDataWrapper.getData().get(property));
    }

    private static TrdFunction<String, MapDataWrapper, Boolean, Boolean> resultProcessor(String property, CheckResultCode code) {
        return (s, dw, ruleCheckResult) -> {
            if (ruleCheckResult) {
                return dw.getFlag();
            }
            dw.getRuleContext().setCheckResult(property, code, dw);
            return dw.getFlag();
        };
    }

    /**
     * The demo check properties in a map.
     * Test
     * key    rule
     * name   not empty
     * age    not empty
     *
     * @param ctx the Check Context
     * @return predicates
     */
    @Override
    public Predicate<MapDataWrapper> getPredicateChain(ChainContext ctx) {
        return Rule.testNotEmpty("name"
                , testEmptyRule("name")
                , resultProcessor("name", CheckResultCode.NOT_EMPTY))
                .and(Rule.testNotEmpty("age"
                        , testEmptyRule("age")
                        , resultProcessor("age", CheckResultCode.NOT_EMPTY)
                ));

    }

    /**
     * The demo check the collection of maps.
     * Test
     * no duplicated name in those maps.
     * no duplicated age in those maps.
     *
     * @param ctx the Check Context
     * @return functions
     */
    @Override
    public Function<List<MapDataWrapper>, List<MapDataWrapper>> getFunction(ChainContext ctx) {
        return testDuplicatedInCollection(ctx, "name", dataWrapper -> true, dataWrapper -> dataWrapper.getString("name"), ruleContextSolverOnDuplicated)
                .andThen(testDuplicatedInCollection(ctx, "age", dataWrapper -> true, dataWrapper -> dataWrapper.getString("age"), ruleContextSolverOnDuplicated));
    }

    private Function<List<MapDataWrapper>, List<MapDataWrapper>> testDuplicatedInCollection(ChainContext ctx, String property
            , Predicate<? super MapDataWrapper> filter
            , Function<MapDataWrapper, String> mapping
            , TrdConsumer<ChainContext, String, List<MapDataWrapper>> ruleContextSolverOnDuplicated) {
        return mapDataWrappers -> {
            Map<Object, List<MapDataWrapper>> propertiesGroupByMap = mapDataWrappers
                    .parallelStream()
                    .filter(filter)
                    .collect(Collectors
                            .groupingBy(mapping));

            propertiesGroupByMap.forEach((key, val) -> {
                // has duplicated val in those properties
                if (Objects.nonNull(key) && val.size() > 1) {
                    ruleContextSolverOnDuplicated.accept(ctx, property, val);
                }
            });
            return mapDataWrappers;
        };
    }

    TrdConsumer<ChainContext, String, List<MapDataWrapper>> ruleContextSolverOnDuplicated = (ctx, property, values) -> {
        values.parallelStream().forEach(it -> {
            it.getRuleContext().setCheckResult(property, CheckResultCode.DUPLICATED, it);
        });
    };

}
