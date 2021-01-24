import common.CommonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rule.Rule;
import service.entity.EntityDataWrapper;
import service.entity.User;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void testNotEmpty() {
        User user = mock(User.class);
        when(user.getName()).thenReturn("Kobe");
        EntityDataWrapper entityDataWrapper = mock(EntityDataWrapper.class);
        when(entityDataWrapper.getData()).thenReturn(user);
        when(entityDataWrapper.getFlag()).thenReturn(true);

        Rule.NOT_EMPTY.<EntityDataWrapper>testNotEmpty("name", dw -> {
            String name = dw.getData().getName();
            return CommonUtil.isEmpty(name);
        }, (property, dataWrapper, result) -> dataWrapper.getFlag()).test(entityDataWrapper);
    }
}
