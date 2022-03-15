package sg.gov.moh.iais.egp.bsb.dto.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationListResultDto extends ValidationResultDto implements Serializable {
    @JsonProperty("data_error_map")
    protected LinkedHashMap<Integer, Map<String, String>> dataErrorMap;


    public static ValidationListResultDto of(boolean pass, Map<String, String> errorMap, Map<Integer, Map<String, String>> dataErrorMap) {
        ValidationListResultDto listResultDto = new ValidationListResultDto();
        listResultDto.setPass(pass);
        if (errorMap != null) {
            listResultDto.setErrorMap(new HashMap<>(errorMap));
        }
        if (dataErrorMap != null) {
            listResultDto.setDataErrorMap(new LinkedHashMap<>(dataErrorMap));
        }
        return listResultDto;
    }


    /** Get an error message JSON contains both normal error and data error. */
    @SneakyThrows
    public String toErrorMsg(String separator) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> fullErrorMap = new HashMap<>();
        if (super.errorMap != null) {
            fullErrorMap.putAll(super.errorMap);
        }
        if (this.dataErrorMap != null) {
            fullErrorMap.putAll(flatDataErrorMap(separator));
        }
        return mapper.writeValueAsString(fullErrorMap);
    }


    /**
     * Flatten data error map, convert it to a key-value map.
     * Spread the integer key to each keys in the value map.
     * @return a map, the key is field + separator + dataErrorMap's key
     */
    public Map<String, String> flatDataErrorMap(String separator) {
        if (this.dataErrorMap == null || this.dataErrorMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> flatDataErrorMap = new HashMap<>();
        for (Map.Entry<Integer, Map<String, String>> dtoEntry : dataErrorMap.entrySet()) {
            int index = dtoEntry.getKey();
            Map<String, String> errorMap = dtoEntry.getValue();
            for (Map.Entry<String, String> itemEntry : errorMap.entrySet()) {
                flatDataErrorMap.put(itemEntry.getKey() + separator + index, itemEntry.getValue());
            }
        }
        return flatDataErrorMap;
    }
}
