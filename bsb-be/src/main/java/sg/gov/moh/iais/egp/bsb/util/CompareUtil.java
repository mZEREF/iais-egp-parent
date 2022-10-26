package sg.gov.moh.iais.egp.bsb.util;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
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

    public static <T> List<CompareWrap<T>> compareAndAssembleList (List<T> oldList, List<T> newList, Function<T, String> function, Class<T> clazz) {
        List<CompareWrap<T>> compareWrapList = new ArrayList<>();
        T t;
        try {
            t = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IaisRuntimeException("No arguments constructor method", e);
        }

        if (CollectionUtils.isEmpty(oldList) && !CollectionUtils.isEmpty(newList)) {
            newList.forEach(s -> compareWrapList.add(new CompareWrap<>(t, s)));
        } else if (!CollectionUtils.isEmpty(oldList) && CollectionUtils.isEmpty(newList)) {
            oldList.forEach(s -> compareWrapList.add(new CompareWrap<>(s, t)));
        } else if (!CollectionUtils.isEmpty(oldList) && !CollectionUtils.isEmpty(newList)) {
            List<String> oldMarkList = oldList.stream().map(function).collect(Collectors.toList());
            List<String> newMarkList = newList.stream().map(function).collect(Collectors.toList());
            List<String> addList = new ArrayList<>();
            List<String> deleteList = new ArrayList<>();
            List<String> remainList = new ArrayList<>();
            filterStringList(oldMarkList, newMarkList, addList, deleteList, remainList);

            Map<String, T> oldMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(function, oldList);
            Map<String, T> newMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(function, newList);

            remainList.forEach(s -> compareWrapList.add(new CompareWrap<>(oldMap.get(s), newMap.get(s))));
            deleteList.forEach(s -> compareWrapList.add(new CompareWrap<>(oldMap.get(s), t)));
            addList.forEach(s -> compareWrapList.add(new CompareWrap<>(t, newMap.get(s))));
        }
        return compareWrapList;
    }

}
