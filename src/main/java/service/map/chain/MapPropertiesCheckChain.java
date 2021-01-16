package service.map.chain;

import core.ChainContext;
import core.IChain;
import rule.Rule;
import service.map.MapDataWrapper;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

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
                    return Objects.isNull(name);
                }
                , (property, dw, ruleCheckResult) -> {
                    if (ruleCheckResult) {
                        return dw.getFlag();
                    }
                    dw.getRuleContext().setCheckResult("name", Rule.NOT_EMPTY, dw);
                    return dw.getFlag();
                }).and(
                Rule.testNotEmpty
                        ("age"
                                , (dw) -> true
                                , (s, mapMapDatawrapper, aBoolean) -> mapMapDatawrapper.getFlag()
                                , dw -> {
                                    Object age = dw.getData().get("age");
                                    return !"".equals(age);
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


}
