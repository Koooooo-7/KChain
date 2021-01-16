package demo;

import core.ChainContext;
import core.DataWrapper;
import core.IChain;

import java.util.List;
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


}
