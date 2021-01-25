package core;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class DefaultPropertiesCheckChain implements IChain<DataWrapper<Object, Object>, List<DataWrapper<Object, Object>>> {

    @Override
    public Predicate<DataWrapper<Object, Object>> getPredicateChain(ChainContext ctx) {
        return (dataWrapper) -> true;
    }


    @Override
    public Function<List<DataWrapper<Object, Object>>, List<DataWrapper<Object, Object>>> getFunction(ChainContext ctx) {
        return dataWrappers -> dataWrappers;
    }
}
