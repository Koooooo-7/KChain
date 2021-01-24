import common.CommonUtil;
import org.junit.Assert;
import org.junit.Test;
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
        Assert.assertTrue(notEmpty);

        boolean notEmpty1 = CommonUtil.isNotEmpty(1);
        Assert.assertTrue(notEmpty1);

        boolean notEmpty2 = CommonUtil.isNotEmpty(1L);
        Assert.assertTrue(notEmpty2);

        boolean notEmpty3 = CommonUtil.isNotEmpty(mock(User.class));
        Assert.assertTrue(notEmpty3);

        boolean notEmpty4 = CommonUtil.isNotEmpty("1", String.class);
        Assert.assertTrue(notEmpty4);

        boolean notEmpty5 = CommonUtil.isNotEmpty(1, Integer.class);
        Assert.assertTrue(notEmpty5);

        boolean notEmpty6 = CommonUtil.isNotEmpty(1L, Long.class);
        Assert.assertTrue(notEmpty6);

        boolean notEmpty7 = CommonUtil.isNotEmpty(mock(User.class), User.class);
        Assert.assertTrue(notEmpty7);
    }

    @Test
    public void testIsEmpty() {
        boolean empty = CommonUtil.isEmpty("", String.class);
        Assert.assertTrue(empty);
    }
}
