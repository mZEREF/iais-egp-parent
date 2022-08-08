package sg.gov.moh.iais.egp.common.function;

import java.util.Collection;

/**
 * Util functions used by bsb tag library
 */
public class UtilFunctions {
    private UtilFunctions() {}

    /**
     * Check if any element in collection 2 is contained in the first collection
     * @return true if first collection contains any element in collection2
     */
    public static boolean collectionContainsAny(Collection<?> collection, Collection<?> collection2) {
        boolean contain = false;
        for (Object obj : collection2) {
            if (collection.contains(obj)) {
                contain = true;
                break;
            }
        }
        return contain;
    }

    /**
     * Check if any element in array is contained in the first collection
     * @return true if first collection contains any element in the array
     */
    public static boolean collectionContainsAny(Collection<?> collection, Object[] array) {
        boolean contain = false;
        for (Object el : array) {
            if (collection.contains(el)) {
                contain = true;
                break;
            }
        }
        return contain;
    }

    /**
     * Display value of Yes or No
     * @param value Y or N
     * @return Yes or No or null
     */
    public static String displayYesOrNo(String value) {
        String display = null;
        if ("Y".equals(value)) {
            display = "Yes";
        } else if ("N".equals(value)) {
            display = "No";
        }
        return display;
    }
}
