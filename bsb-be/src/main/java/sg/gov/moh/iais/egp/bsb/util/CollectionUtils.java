package sg.gov.moh.iais.egp.bsb.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.vavr.Function1;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionUtils {
    private CollectionUtils() {}
    private static final String ERR_MSG_NULL_COLLECTION = "Collection should not be null";
    private static final String ERR_MSG_NULL_FUNC = "Function should not be null";

    /**
     * Group a list of obj by a field of it
     * @param collection the objs to be grouped
     * @param function the method to get the field of the obj
     * @param <X> object type
     * @param <Y> field type
     * @return a map, the key is the field, the value is a list of objects
     */
    public static <X, Y> Map<Y, List<X>> groupCollectionToMap(Function<X, Y> function, Collection<X> collection) {
        Assert.notNull(collection, ERR_MSG_NULL_COLLECTION);
        Assert.notNull(function, ERR_MSG_NULL_FUNC);
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
    public static <X, Y> Map<Y, X> uniqueIndexMap(Function<X, Y> function, Collection<X> collection) {
        Assert.notNull(collection, ERR_MSG_NULL_COLLECTION);
        Assert.notNull(function, ERR_MSG_NULL_FUNC);
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
     * @param <X> input object type
     * @param <Y> output object type
     * @param function function that convert input object to destination object
     * @param xList a list of input objects
     * @return a non-null list of destination object
     */
    public static <X, Y> List<Y> listMapping(Function<X, Y> function, List<X> xList) {
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


    /**
     * Retrieves a sublist for some condition.
     * @param predicate condition to determine if we keep the element in the sublist
     * @param list input list
     * @param <T> input element type
     * @return a list of sublist; null of the input list is null
     */
    public static <T> List<T> filterList(Predicate<T> predicate, List<T> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        Assert.notNull(predicate, ERR_MSG_NULL_FUNC);
        List<T> subList = new ArrayList<>(list.size());
        for (T t : list) {
            if (predicate.test(t)) {
                subList.add(t);
            }
        }
        return subList;
    }


    /**
     * Gets the size of the collection.
     * <p>
     * This method is designed for this scenario:
     * When you want to create a collection or map with a suitable initial capacity, you need to get the size
     * of the related collection, so you need to check if it is null or get the size of it. Use this method to
     * avoid a null check in business logic.
     * @param collection nullable
     * @return 0 if the collection is null, or else the size of it
     */
    public static int nullableCollSize(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    /**
     * Gets the size of the map.
     * <p>
     * This method is designed for this scenario:
     * When you want to create a collection or map with a suitable initial capacity, you need to get the size
     * of the related map, so you need to check if it is null or get the size of it. Use this method to
     * avoid a null check in business logic.
     * @param map nullable
     * @return 0 if the map is null, or else the size of it
     */
    public static int nullableMapSize(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }

    /**
     * Checks if the collection argument is null or empty before apply a function.
     * A tool method to reduce a little code for your business.
     * @param func function to use the argument
     * @param paramList the argument collection
     * @param <P> argument's element type
     * @param <T> result list's element type
     * @return a empty list if the argument is null or empty, or else a list result of the function
     */
    public static <P, T> List<T> safeApplyColl(Function1<Collection<P>, List<T>> func, Collection<P> paramList) {
        List<T> resultList;
        if (paramList == null || paramList.isEmpty()) {
            resultList = Collections.emptyList();
        } else {
            Assert.notNull(func, ERR_MSG_NULL_FUNC);
            resultList = func.apply(paramList);
        }
        return resultList;
    }

    /**
     * Safely apply the collection argument, and check if the result size is the same as the arguments
     * @see #safeApplyColl(io.vavr.Function1, java.util.Collection)
     * @throws java.lang.IllegalStateException if the size of argument and result is not equal
     */
    public static <P, T> List<T> safeApplyCollRestrictMatch(Function1<Collection<P>, List<T>> func, Collection<P> paramList) {
        List<T> resultList = safeApplyColl(func, paramList);
        if (paramList != null && paramList.size() > resultList.size()) {
            throw new IllegalStateException("Not a one-to-one matching, want:" + paramList.size() + ", get:" + resultList.size());
        }
        return resultList;
    }

    /**
     * Converts elements of a collection conditionally, and gets a list as the result.
     * @param condFunc condition predicate
     * @param transformFunc function used to convert the element of the collection
     * @param <F> type before transformation
     * @param <T> type after transformation
     */
    public static <F, T> List<T> conditionalTransform2List(Predicate<F> condFunc, Function1<F, T> transformFunc, Collection<F> initList) {
        List<T> resultList;
        if (initList == null || initList.isEmpty()) {
            resultList = new ArrayList<>();
        } else {
            Assert.notNull(condFunc, ERR_MSG_NULL_FUNC);
            Assert.notNull(transformFunc, ERR_MSG_NULL_FUNC);
            resultList = new ArrayList<>(initList.size());
            for (F f : initList) {
                if (condFunc.test(f)) {
                    T t = transformFunc.apply(f);
                    resultList.add(t);
                }
            }
        }
        return resultList;
    }

    /**
     * Converts elements of a collection conditionally, and gets a set as the result.
     * @param condFunc condition predicate
     * @param transformFunc function used to convert the element of the collection
     * @param <F> type before transformation
     * @param <T> type after transformation
     */
    public static <F, T> Set<T> conditionalTransform2Set(Predicate<F> condFunc, Function1<F, T> transformFunc, Collection<F> initSet) {
        Set<T> resultSet;
        if (initSet == null || initSet.isEmpty()) {
            resultSet = new LinkedHashSet<>();
        } else {
            Assert.notNull(condFunc, ERR_MSG_NULL_FUNC);
            Assert.notNull(transformFunc, ERR_MSG_NULL_FUNC);
            resultSet = Sets.newLinkedHashSetWithExpectedSize(initSet.size());
            for (F f : initSet) {
                if (condFunc.test(f)) {
                    T t = transformFunc.apply(f);
                    resultSet.add(t);
                }
            }
        }
        return resultSet;
    }


    /**
     * Finds the wanted element in the collection by an element field which equals to the target value
     * @param fieldGetter function to get field of the element
     * @param list collection contains (or not) the wanted element
     * @param targetValue target value of the field
     * @param <T> type of the element
     * @param <F> type of the field
     * @return wanted element, or null if not found
     */
    public static <T, F> T findElementByFieldEquals(Function<T, F> fieldGetter, Collection<T> list, F targetValue) {
        Assert.notNull(fieldGetter, ERR_MSG_NULL_FUNC);
        T target = null;
        if (list != null) {
            for (T element : list) {
                F field = fieldGetter.apply(element);
                if (Objects.equals(field, targetValue)) {
                    target = element;
                    break;
                }
            }
        }
        return target;
    }

    /**
     * Finds the wanted element in the collection by an element field which is a member of the target collection
     * @param fieldGetter function to get field of the element
     * @param list collection contains (or not) the wanted element
     * @param targetValueCollection collection contains all target values of the field
     * @param <T> type of the element
     * @param <F> type of the field
     * @return wanted element, or null if not found
     */
    public static <T, F> T findElementByFieldIn(Function<T, F> fieldGetter, Collection<T> list, Collection<F> targetValueCollection) {
        Assert.notNull(fieldGetter, ERR_MSG_NULL_FUNC);
        T target = null;
        if (list != null && targetValueCollection != null) {
            for (T element : list) {
                F field = fieldGetter.apply(element);
                if (targetValueCollection.contains(field)) {
                    target = element;
                    break;
                }
            }
        }
        return target;
    }
}
