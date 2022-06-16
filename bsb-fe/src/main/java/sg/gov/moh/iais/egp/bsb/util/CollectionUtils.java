package sg.gov.moh.iais.egp.bsb.util;

import com.google.common.collect.Maps;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CollectionUtils {
    private CollectionUtils() {}

    private static final String ERR_MSG_COLLECTION_NOT_NULL = "collection must not be null";
    private static final String ERR_MSG_FUNC_NOT_NULL = "function must not be null";
    public static final String ERR_MSG_NULL_FUNC = "Function should not be null";

    /**
     * Group a list of obj by a field of it
     * @param collection the objs to be grouped
     * @param function the method to get the field of the obj
     * @param <X> object type
     * @param <Y> field type
     * @return a map, the key is the field, the value is a list of objects
     */
    public static <X, Y> Map<Y, List<X>> groupCollectionToMap(Collection<X> collection, Function<X, Y> function) {
        Assert.notNull(collection, ERR_MSG_COLLECTION_NOT_NULL);
        Assert.notNull(function, ERR_MSG_FUNC_NOT_NULL);
        Map<Y, List<X>> map = Maps.newLinkedHashMapWithExpectedSize(collection.size());
        for (X x : collection) {
            Y key = function.apply(x);
            List<X> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(x);
            map.put(key, list);
        }
        return map;
    }

    /**
     * Get a map to find the elements easily in the collection
     * @param collection the objects to be grouped
     * @param function the method to get the field of the obj
     * @param <X> object type
     * @param <Y> field type
     * @return a map, the key is the field, the value is the element of the collection
     */
    public static <X, Y> Map<Y, X> uniqueIndexMap(Collection<X> collection, Function<X, Y> function) {
        Assert.notNull(collection, ERR_MSG_COLLECTION_NOT_NULL);
        Assert.notNull(function, ERR_MSG_FUNC_NOT_NULL);
        Map<Y, X> map = Maps.newLinkedHashMapWithExpectedSize(collection.size());
        for (X x : collection) {
            Y key = function.apply(x);
            map.put(key, x);
        }
        return map;
    }

    /** Mapping a list of raw objects to a list of destination objects.
     * <p>
     * If you just want a view of input list (you don't call add, addAll or set on the returned list), you can use guava
     * function {@link com.google.common.collect.Lists#transform(List, com.google.common.base.Function)}.
     * This method will return a view, all transformed item will be lazily evaluated.
     * @param xList a list of input objects
     * @param function function that convert input object to destination object
     * @param <X> input object type
     * @param <Y> output object type
     * @return a non-null list of destination object
     */
    public static <X, Y> List<Y> listMapping(List<X> xList, Function<X, Y> function) {
        if (xList == null || xList.isEmpty()) {
            return new ArrayList<>(0);
        }
        Assert.notNull(function, ERR_MSG_NULL_FUNC);
        List<Y> yList = new ArrayList<>(xList.size());
        for (X x : xList) {
            Y y = function.apply(x);
            yList.add(y);
        }
        return yList;
    }
}
