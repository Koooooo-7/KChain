import org.junit.Assert;
import org.junit.Test;
import rule.RuleStrategy;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class RuleStrategyTest {
    @Test
    public void testRuleStrategy(){
        boolean fullCheckPassFlagOnStrategy = RuleStrategy.FULL_CHECK.getPassFlagOnStrategy(false);
        Assert.assertTrue(fullCheckPassFlagOnStrategy);

        boolean fastFailedPassFlagOnStrategy = RuleStrategy.FAST_FAILED.getPassFlagOnStrategy(false);
        Assert.assertFalse(fastFailedPassFlagOnStrategy);
    }
}
