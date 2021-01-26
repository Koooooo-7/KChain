import demo.App;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.entity.User;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class DemoTest {

    @Test
    @DisplayName("test Demos")
    public void testDemoApp() {
        App.main(new String[]{});
    }

    @Test
    public void testUserEntity() {
        User user = new User("", 0);
        user.setName("Kobe");
        user.setAge(24);
        Assertions.assertEquals("Kobe", user.getName());
        Assertions.assertEquals(24, user.getAge());
        Assertions.assertTrue(StringUtils.isNotEmpty(user.toString()));
    }
}
