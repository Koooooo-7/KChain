import org.junit.Assert;
import org.junit.Test;
import rule.Rule;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class RuleTest {
    @Test
    public void testCustomerRule() {
        boolean result = Rule.testOnCustomized("1"::equals).test("1");
        Assert.assertTrue(result);
    }
}
