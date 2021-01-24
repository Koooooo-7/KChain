import common.CommonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.entity.User;

import static org.mockito.Mockito.mock;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class UtilTest {

    @Test
    public void testIsNotEmpty() {
        boolean notEmpty = CommonUtil.isNotEmpty("1");
        Assertions.assertTrue(notEmpty);

        boolean notEmpty1 = CommonUtil.isNotEmpty(1);
        Assertions.assertTrue(notEmpty1);

        boolean notEmpty2 = CommonUtil.isNotEmpty(1L);
        Assertions.assertTrue(notEmpty2);

        boolean notEmpty3 = CommonUtil.isNotEmpty(mock(User.class));
        Assertions.assertTrue(notEmpty3);

        boolean notEmpty4 = CommonUtil.isNotEmpty("1", String.class);
        Assertions.assertTrue(notEmpty4);

        boolean notEmpty5 = CommonUtil.isNotEmpty(1, Integer.class);
        Assertions.assertTrue(notEmpty5);

        boolean notEmpty6 = CommonUtil.isNotEmpty(1L, Long.class);
        Assertions.assertTrue(notEmpty6);

        boolean notEmpty7 = CommonUtil.isNotEmpty(mock(User.class), User.class);
        Assertions.assertTrue(notEmpty7);
    }

    @Test
    public void testIsEmpty() {
        boolean empty = CommonUtil.isEmpty("", String.class);
        Assertions.assertTrue(empty);
    }
}
