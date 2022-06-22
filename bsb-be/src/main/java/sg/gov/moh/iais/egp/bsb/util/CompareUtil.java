package sg.gov.moh.iais.egp.bsb.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap;

import java.util.ArrayList;                                                                                                                                                                       
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class CompareUtil {
    private CompareUtil() {}

    public static void filterStringList(List<String> oldList, List<String> newList,
                                  List<String> addList, List<String> deleteList, List<String> remainList) {
        if (CollectionUtils.isEmpty(oldList)) {
            oldList = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(newList)) {
            newList = new ArrayList<>();
        }

        List<String> intersectionList = new ArrayList<>(newList);
        intersectionList.retainAll(oldList);

        addList.addAll(newList);
        addList.removeAll(intersectionList);

        deleteList.addAll(oldList);
        deleteList.removeAll(intersectionList);

        remainList.addAll(intersectionList);
    }

    /**
     *
     * @param oldList
     * @param newList
     * @param function
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<CompareWrap<T>> compareAndAssembleList (List<T> oldList, List<T> newList, Function<T, String> function, Class<T> clazz) {
        List<String> oldMarkList = oldList.stream().map(function).collect(Collectors.toList());
        List<String> newMarkList = oldList.stream().map(function).collect(Collectors.toList());
        List<String> addList = new ArrayList<>();
        List<String> deleteList = new ArrayList<>();
        List<String> remainList = new ArrayList<>();
        filterStringList(oldMarkList, newMarkList, addList, deleteList, remainList);

        List<CompareWrap<T>> compareWrapList = new ArrayList<>(oldList.size() + newList.size());
        Map<String, T> oldMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(oldList, function);
        Map<String, T> newMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(newList, function);

        try {
            T t = clazz.getConstructor().newInstance();
            remainList.forEach(s -> compareWrapList.add(new CompareWrap<>(oldMap.get(s), newMap.get(s))));
            deleteList.forEach(s -> compareWrapList.add(new CompareWrap<>(oldMap.get(s), t)));
            addList.forEach(s -> compareWrapList.add(new CompareWrap<>(t, newMap.get(s))));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Object gets instance errors through reflection!");
        }
        return compareWrapList;
    }

}
