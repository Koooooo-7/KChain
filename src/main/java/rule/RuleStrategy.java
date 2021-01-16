package rule;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public enum RuleStrategy {
    FULL_CHECK,
    FAST_FAILED;


    public boolean getPassFlagOnStrategy(boolean pass) {
        switch (this) {
            case FULL_CHECK:
                return true;

            case FAST_FAILED:
                return pass;

            default:
                break;
        }
        return false;
    }
}
