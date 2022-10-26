package sg.gov.moh.iais.egp.bsb.util;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RequestObjectMappingUtil {
    private RequestObjectMappingUtil() {}

    /**
     * This method is design for sneaky validation of the field. That is:
     * <p>
     * If a field is not visible for the scenario, we let the field value be null. If a field is visible
     * for the scenario, we ensure the value won't be null and be empty string if not filled.
     * <p>
     * For the validation part, we annotated the field with {@code @NotEmpty} and without {@code @NotNull}.
     * So we ensure the validation for visible scenario, and let the invisible scenario pass.
     * <p>
     * However, the sneaky implementation has a loophole. If the user use a tool to remove the parameter in
     * the request, the validation can be bypassed. This method is designed to resolve it! Use this method
     * to ensure the value for visible scenario be empty string!
     * @param value parameter value
     * @param visible if the field is visible for the scenario
     */
    public static String sneakyParamVal(String value, boolean visible) {
        return value == null && visible ? "" : value;
    }


    /**
     * This method is intended for BSB add more section function to get a new list of DTOs from data read from
     * page, and keep the index info (by returning a map).
     * <p>
     * In BSB implementation, it uses an index array (input element) to hold existing sections and a deleted index
     * array to indicate a section is once deleted. If a section is not deleted (can not be found in the deleted
     * index array), and is less than the DTO list's size, it must be an original section! So we should reuse it.
     * If we do not reuse it, the new uploaded files are lost!
     * @param type class type of the DTO
     * @param origin existing data list
     * @param idxArr read index list from page
     * @param deletedIndArr read deleted index list from page
     * @param <T> type of the DTO which holds the data
     * @return a map, the key is the index related to it, value is a DTO which may be reused from the original
     *         list or a new instance
     * @throws InstantiationException see {@link Class#newInstance()}
     * @throws IllegalAccessException see {@link Class#newInstance()}
     * @author chenwei
     */
    public static <T> Map<Integer, T> readAndReuseSectionDto(Class<T> type, List<T> origin,
                                                             Collection<Integer> idxArr,
                                                             Collection<Integer> deletedIndArr)
            throws InstantiationException, IllegalAccessException {
        Map<Integer, T> map = Maps.newLinkedHashMapWithExpectedSize(idxArr.size());
        for (int idx : idxArr) {
            T info;
            if (origin.size() > idx && !deletedIndArr.contains(idx)) {
                info = origin.get(idx);
            } else {
                info = type.newInstance();
            }
            map.put(idx, info);
        }
        return map;
    }
}
