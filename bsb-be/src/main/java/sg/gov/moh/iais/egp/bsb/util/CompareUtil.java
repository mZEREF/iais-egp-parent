package sg.gov.moh.iais.egp.bsb.util;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class CompareUtil {
    private CompareUtil() {}

    public static void filterList(List<String> oldList, List<String> newList,
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
}
