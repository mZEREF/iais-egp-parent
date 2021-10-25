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
}
