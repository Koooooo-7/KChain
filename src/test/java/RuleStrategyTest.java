import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rule.RuleStrategy;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class RuleStrategyTest {
    @Test
    public void testRuleStrategy(){
        boolean fullCheckPassFlagOnStrategy = RuleStrategy.FULL_CHECK.getPassFlagOnStrategy(false);
        Assertions.assertTrue(fullCheckPassFlagOnStrategy);

        boolean fastFailedPassFlagOnStrategy = RuleStrategy.FAST_FAILED.getPassFlagOnStrategy(false);
        Assertions.assertFalse(fastFailedPassFlagOnStrategy);

        Assertions.assertTrue(StringUtils.isNotEmpty(RuleStrategy.FULL_CHECK.toString()));
    }
}
