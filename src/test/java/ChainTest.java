import core.ChainContext;
import demo.DefaultPropertiesCheckChain;
import org.junit.Test;
import rule.RuleStrategy;

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
}
