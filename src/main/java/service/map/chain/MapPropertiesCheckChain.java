package service.map.chain;

import core.ChainContext;
import core.IChain;
import org.apache.commons.lang3.StringUtils;
import rule.Rule;
import rule.TrdConsumer;
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

    @Override
    public Predicate<MapDataWrapper> getPredicateChain(ChainContext ctx) {
        return Rule.<MapDataWrapper>testNotEmpty("name"
                , (dw) -> true
                , (s, mapMapDatawrapper, aBoolean) -> mapMapDatawrapper.getFlag()
                , dw -> {
                    Object name = dw.getData().get("name");
                    return StringUtils.isNotEmpty((String) name);
                }
                , (property, dw, ruleCheckResult) -> {
                    if (ruleCheckResult) {
                        return dw.getFlag();
                    }
                    dw.getRuleContext().setCheckResult("name", Rule.NOT_EMPTY, dw);
                    return dw.getFlag();
                }).and(Rule.testNotEmpty("age", (dw) -> true, (s, mapMapDatawrapper, aBoolean) -> mapMapDatawrapper.getFlag(), dw -> {
                    Object age = dw.getData().get("age");
                    return 0 != (Integer) age;
                }
                , (property, dw, ruleCheckResult) -> {
                    if (ruleCheckResult) {
                        return dw.getFlag();
                    }
                    dw.getRuleContext().setCheckResult("age", Rule.NOT_EMPTY, dw);
                    return dw.getFlag();
                })
        );

    }

    @Override
    public Function<List<MapDataWrapper>, List<MapDataWrapper>> getFunction(ChainContext ctx) {
        return testDuplicatedInCollection(ctx, "name", dataWrapper -> dataWrapper.getString("name"), ruleContextSolverOnDuplicated)
                .andThen(testDuplicatedInCollection(ctx, "age", dataWrapper -> dataWrapper.getString("age"), ruleContextSolverOnDuplicated));
    }

    private Function<List<MapDataWrapper>, List<MapDataWrapper>> testDuplicatedInCollection(ChainContext ctx, String property
            , Function<MapDataWrapper, String> mapping
            , TrdConsumer<ChainContext, String, List<MapDataWrapper>> ruleContextSolverOnDuplicated) {
        return mapDataWrappers -> {
            Map<Object, List<MapDataWrapper>> propertiesGroupByMap = mapDataWrappers
                    .parallelStream()
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
            it.getRuleContext().setCheckResult(property, Rule.DUPLICATED, it);
        });
    };

}
