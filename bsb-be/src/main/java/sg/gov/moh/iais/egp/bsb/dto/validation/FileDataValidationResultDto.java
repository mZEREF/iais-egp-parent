package sg.gov.moh.iais.egp.bsb.dto.validation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDataValidationResultDto<T extends Serializable> extends ValidationResultDto implements Serializable {
    @JsonProperty("data_error_map")
    protected LinkedHashMap<Integer, T> dataErrorMap;


    /** Convert FileData's dataErrorMap to the normal list result DTO dataErrorMap.
     * After the convert, the field name is replaced with JSON name. */
    public Map<Integer, Map<String, String>> unfoldDataErrorMap() {
        if (this.dataErrorMap == null) {
            throw new IllegalStateException("No data error exists");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, Map<String, String>> map = Maps.newLinkedHashMapWithExpectedSize(this.dataErrorMap.size());
        for (Map.Entry<Integer, T> dtoEntry : dataErrorMap.entrySet()) {
            Map<String, String> objMap = mapper.convertValue(dtoEntry.getValue(), new TypeReference<Map<String, String>>() {});
            map.put(dtoEntry.getKey(), objMap);
        }
        return map;
    }


    public static <T extends Serializable> FileDataValidationResultDto<T> of(boolean pass, Map<String, String> errorMap, Map<Integer, T> dataErrorMap) {
        FileDataValidationResultDto<T> dto = new FileDataValidationResultDto<>();
        dto.setPass(pass);
        if (errorMap != null) {
            dto.setErrorMap(new HashMap<>(errorMap));
        }
        if (dataErrorMap != null) {
            dto.setDataErrorMap(new LinkedHashMap<>(dataErrorMap));
        }
        return dto;
    }
}
