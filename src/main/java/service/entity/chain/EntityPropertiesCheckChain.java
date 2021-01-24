package service.entity.chain;

import common.CheckResultCode;
import common.CommonUtil;
import core.ChainContext;
import core.IChain;
import rule.Rule;
import rule.TrdFunction;
import service.common.CommonTestComponents;
import service.entity.EntityDataWrapper;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static service.common.CommonTestComponents.testDuplicatedInCollection;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class EntityPropertiesCheckChain implements IChain<EntityDataWrapper, List<EntityDataWrapper>> {

    private static TrdFunction<String, EntityDataWrapper, Boolean, Boolean> resultProcessor(String property, CheckResultCode code) {
        return (s, dw, ruleCheckResult) -> {
            if (ruleCheckResult) {
                return dw.getFlag();
            }
            dw.getRuleContext().setCheckResult(property, code, dw);
            return dw.getFlag();
        };
    }

    /**
     * The demo check properties in a entity.
     * Test
     * key    rule
     * name   not empty
     * age    not empty
     *
     * @param ctx the Check Context
     * @return predicates
     */
    @Override
    public Predicate<EntityDataWrapper> getPredicateChain(ChainContext ctx) {
        return Rule.NOT_EMPTY.<EntityDataWrapper>testNotEmpty("name"
                , dataWrapper -> CommonUtil.isNotEmpty(dataWrapper.getData().getName())
                , resultProcessor("name", CheckResultCode.NOT_EMPTY))
                .and(Rule.NOT_EMPTY.testNotEmpty("age"
                        , dataWrapper -> CommonUtil.isNotEmpty(dataWrapper.getData().getAge())
                        , resultProcessor("age", CheckResultCode.NOT_EMPTY)
                ));

    }

    /**
     * The demo check the collection of entity.
     * Test
     * no duplicated name in those entities.
     * no duplicated age in those entities.
     *
     * @param ctx the Check Context
     * @return functions
     */
    @Override
    public Function<List<EntityDataWrapper>, List<EntityDataWrapper>> getFunction(ChainContext ctx) {
        return CommonTestComponents.<EntityDataWrapper>testDuplicatedInCollection(ctx, "name", dataWrapper -> true, dataWrapper -> dataWrapper.getData().getName())
                .andThen(testDuplicatedInCollection(ctx, "age", dataWrapper -> true, dataWrapper -> {
                    Integer age = dataWrapper.getData().getAge();
                    if (CommonUtil.isEmpty(age)) {
                        return "";
                    }
                    return age + "";
                }));
    }


}
