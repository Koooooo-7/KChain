package rule;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public enum RuleStrategy {
    FULL_CHECK,
    FAIL_FAST;


    public boolean getPassFlagOnStrategy(boolean pass) {
        switch (this) {
            case FULL_CHECK:
                return true;

            case FAIL_FAST:
                return pass;

            default:
                break;
        }
        return false;
    }
}
