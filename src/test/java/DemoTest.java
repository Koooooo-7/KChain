import demo.App;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class DemoTest {

    @Test
    @DisplayName("Call main on Demo App.class")
    public void runMain(){
        App.main(new String[]{});
    }
}
