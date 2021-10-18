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


    @SneakyThrows
    public String toErrorMsg() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(errorMap);
    }
}
