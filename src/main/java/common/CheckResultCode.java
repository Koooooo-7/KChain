package common;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public enum CheckResultCode {

    NOT_EMPTY("Property is empty"),
    IN_CASES("Property is not in cases"),
    HAS_ONE(""),
    DUPLICATED("Property is duplicated with others"),
    ;
    private String reason;

    CheckResultCode(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
