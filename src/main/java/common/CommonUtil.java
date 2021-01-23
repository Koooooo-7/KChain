package common;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author Koy  https://github.com/Koooooo-7
 * @Description
 */

public class CommonUtil {

    public static boolean isNotEmpty(Object obj) {
        if (obj instanceof String) {
            return StringUtils.isNotEmpty((String) obj);
        }

        if (obj instanceof Long) {
            return 0L != (Long) obj;
        }

        if (obj instanceof Integer) {
            return 0 != (Integer) obj;
        }

        return Objects.nonNull(obj);
    }

    public static boolean isEmpty(Object object) {
        return !isNotEmpty(object);
    }

    public static boolean isNotEmpty(Object obj, Class<?> clz) {
        if (String.class.equals(clz)) {
            return StringUtils.isNotEmpty((String) obj);
        }

        if (Long.class.equals(clz)) {
            return null != obj && 0L != (Long) obj;
        }

        if (Integer.class.equals(clz)) {
            return null != obj && 0 != (Integer) obj;
        }

        return Objects.nonNull(obj);
    }

    public static boolean isEmpty(Object obj, Class<?> clz) {
        return !isNotEmpty(obj, clz);
    }

}
