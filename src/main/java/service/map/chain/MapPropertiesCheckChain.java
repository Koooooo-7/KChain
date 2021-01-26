package service.map.chain;

import common.CheckResultCode;
import common.CommonUtil;
import core.ChainContext;
import core.IChain;
import rule.Rule;
import rule.TrdFunction;
import service.common.CommonTestComponents;
import service.map.MapDataWrapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static service.common.CommonTestComponents.testDuplicatedInCollection;

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
        return Rule.NOT_EMPTY.<MapDataWrapper>testNotEmpty("name"
                , testEmptyRule("name")
                , resultProcessor("name", CheckResultCode.NOT_EMPTY))
                .and(Rule.NOT_EMPTY.testNotEmpty("age"
                        , testEmptyRule("age")
                        , resultProcessor("age", CheckResultCode.NOT_EMPTY)
                ))
                .and(Rule.IN_CASES.testInCases("age",
                        CommonTestComponents.testInCasesRule(dataWrapper -> Integer.valueOf(dataWrapper.getString("age"))
                                , Arrays.asList(1, 2, 3, 4, 5))
                        , resultProcessor("age", CheckResultCode.IN_CASES)
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
        return CommonTestComponents.<MapDataWrapper>testDuplicatedInCollection(ctx, "name", dataWrapper -> true, dataWrapper -> dataWrapper.getString("name"))
                .andThen(testDuplicatedInCollection(ctx, "age", dataWrapper -> true, dataWrapper -> dataWrapper.getString("age")));
    }

}
