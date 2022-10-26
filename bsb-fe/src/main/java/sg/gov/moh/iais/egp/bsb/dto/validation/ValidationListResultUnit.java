package sg.gov.moh.iais.egp.bsb.dto.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationListResultUnit {
    private int sequence;
    private String field;
    private String message;


    /**
     * Gets a list validation result units from an error map.
     * The error map is assumed to created by WebValidationHelper, whose key is error field and value is error message.
     * This method assumes that the key of the map has the format: field + SEPARATOR + index. The separator should be
     * ensured to exist only one in the key; the index starts from 0 (so our sequence should add 1).
     * @param errorMap error map retrieved from WebValidationHelper
     * @param separator separator string of the map's key
     * @return a list of validation result units
     */
    public static List<ValidationListResultUnit> fromFlatMap(Map<String, String> errorMap, String separator) {
        if (errorMap == null || errorMap.isEmpty()) {
            return Collections.emptyList();
        }
        if (separator == null || "".equals(separator)) {
            throw new IllegalArgumentException("Separator can not be empty");
        }
        List<ValidationListResultUnit> resultList = new ArrayList<>(errorMap.size());
        for (Map.Entry<String, String> entry : errorMap.entrySet()) {
            String key = entry.getKey();
            String[] parts = key.split(separator, 2);
            int sequence = Integer.parseInt(parts[1]) + 1;
            ValidationListResultUnit unit = new ValidationListResultUnit(sequence, parts[0], entry.getValue());
            resultList.add(unit);
        }
        return resultList;
    }


    /**
     * Gets a list validation result units from a list error map.
     * This error map is assumed to got from {@link ValidationListResultDto}, whose key is index of the DTO and value
     * is the errorMap of the DTO.
     */
    public static List<ValidationListResultUnit> fromDataErrorMap(Map<Integer, Map<String, String>> dataErrorMap) {
        if (dataErrorMap == null || dataErrorMap.isEmpty()) {
            return Collections.emptyList();
        }
        int amount = dataErrorMap.values().stream().mapToInt(Map::size).sum();
        List<ValidationListResultUnit> resultList = new ArrayList<>(amount);
        for (Map.Entry<Integer, Map<String, String>> dtoEntry : dataErrorMap.entrySet()) {
            int sequence = dtoEntry.getKey() + 1;
            for (Map.Entry<String, String> itemEntry : dtoEntry.getValue().entrySet()) {
                ValidationListResultUnit unit = new ValidationListResultUnit(sequence, itemEntry.getKey(), itemEntry.getValue());
                resultList.add(unit);
            }
        }
        return resultList;
    }


    /**
     * Gets a list validation result units from a file data validation result DTO.
     * The value of the fields in the DTO (T) is the error message.
     * So the method will convert it to a wanted map.
     * @see #fromDataErrorMap(Map)
     */
    public static <T extends Serializable> List<ValidationListResultUnit> fromDateErrorMap(FileDataValidationResultDto<T> resultDto) {
        LinkedHashMap<Integer, T> dataErrorMap = resultDto.getDataErrorMap();
        if (dataErrorMap == null || dataErrorMap.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Integer, Map<String, String>> map = resultDto.unfoldDataErrorMap();
        return fromDataErrorMap(map);
    }
}
