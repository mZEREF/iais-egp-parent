package sg.gov.moh.iais.egp.bsb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.ResponseConstants;

import java.util.Map;


/**
 * Common DTO used to received the data from API part.
 * Expected errors in API part will not return 4xx/5xx, it will give 2xx but status is not ok.
 * When get ok, the error_code, error_description and error_infos should be ignored,
 * When get error, get the errorCode for program and debug, get the errorDesc for user-friendly message,
 * get extra error info from the errorInfos.
 * @param <T> the type of the wanted DTO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseDto<T> {
    private String status;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_description")
    private String errorDesc;

    @JsonProperty("error_infos")
    private Map<String, Object> errorInfos;

    private T entity;


    /**
     * Check if the API part report no error
     * @return true if the dto gets wanted data
     */
    public boolean ok() {
        return ResponseConstants.STATUS_OK.equals(status);
    }
}
