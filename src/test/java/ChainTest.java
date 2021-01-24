import core.ChainContext;
import core.DefaultPropertiesCheckChain;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rule.RuleStrategy;
import rule.TrdConsumer;
import rule.TrdFunction;

import java.util.function.Function;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class ChainTest {
    @Test
    public void testDefaultPropertiesChain() {
        DefaultPropertiesCheckChain defaultPropertiesCheckChain = new DefaultPropertiesCheckChain();
        defaultPropertiesCheckChain.getPredicateChain(new ChainContext(RuleStrategy.FULL_CHECK));
        defaultPropertiesCheckChain.getFunction(new ChainContext(RuleStrategy.FAST_FAILED));
    }

    @Test
    public void testChainContext() {
        ChainContext chainContext = new ChainContext(RuleStrategy.FAST_FAILED);
        chainContext.setRuleStrategy(RuleStrategy.FAST_FAILED);
    }

    @Test
    public void testTrdConsumer() {
        TrdConsumer<String, String, String> consumer1 = (s1, s2, s3) -> {
        };
        TrdConsumer<String, String, String> consumer2 = (s1, s2, s3) -> {
        };

        consumer1.andThen(consumer2).accept("1", "2", "3");

    }

    @Test
    public void testTrdFunction() {
        TrdFunction<String, String, String, String> function1 = (s1, s2, s3) -> s1;
        Function<String, String> function2 = s1 -> s1;
        String result = function1.andThen(function2).apply("1", "2", "3");
        Assertions.assertTrue(StringUtils.equals("1", result));

    }
}
