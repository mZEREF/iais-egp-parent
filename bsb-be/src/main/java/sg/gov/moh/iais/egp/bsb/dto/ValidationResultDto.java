package sg.gov.moh.iais.egp.bsb.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.Map;


/**
 * Common DTO used for validation APIs.
 * When egp part call api part just to do data validation, we can use this class
 * rather than {@link ResponseDto} for convenience.
 */
@Data
public class ValidationResultDto implements Serializable {
    private boolean pass;

    @JsonProperty("error_map")
    private Map<String, String> errorMap;


    public static ValidationResultDto of(boolean pass, Map<String, String> errorMap) {
        ValidationResultDto resultDto = new ValidationResultDto();
        resultDto.setPass(pass);
        resultDto.setErrorMap(errorMap);
        return resultDto;
    }


    @SneakyThrows
    public String toErrorMsg() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(errorMap);
    }

    @SneakyThrows
    public static String toErrorMsg(Object errorMsgHolder) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(errorMsgHolder);
    }
}
