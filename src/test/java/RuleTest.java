import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rule.Rule;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class RuleTest {

    @Test
    public void testAssertionsOnErrorTypeReference() {
        Assertions.assertThrows(AssertionError.class,
                () -> Rule.NOT_EMPTY.testOnCustomized("1"::equals).test("1"));

    }

    @Test
    public void testCustomerRule() {
        boolean result = Rule.CUSTOMIZED.testOnCustomized("1"::equals).test("1");
        Assertions.assertTrue(result);
    }
}
