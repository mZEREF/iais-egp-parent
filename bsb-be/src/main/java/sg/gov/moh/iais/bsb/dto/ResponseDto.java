package sg.gov.moh.iais.bsb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.constant.ResponseConstants;

import java.util.HashMap;
import java.util.Map;


/**
 * Common DTO used to return data to EGP part.
 * Expected errors will not return 4xx/5xx, we will give 2xx but status is not ok.
 * When send ok, the error_code, error_description and error_infos should be ignored,
 * When send error, get the errorCode for program and debug, get the errorDesc for user-friendly message,
 * get extra error info from the errorInfos.
 *
 * Use the {@link #ok(T)} method and {@link #fail()} method to construct the instance with a fluent API.
 *
 * How to use the error_infos is your freedom.
 *
 * @param <T> the type of the wanted DTO
 */
@ApiModel("Common Response DTO")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseDto<T> {
    private String status;

    @ApiModelProperty(value = "error code")
    @JsonProperty("error_code")
    private String errorCode;

    @ApiModelProperty(value = "error description")
    @JsonProperty("error_description")
    private String errorDesc;

    @ApiModelProperty(value = "error infos map")
    @JsonProperty("error_infos")
    private Map<String, Object> errorInfos;

    @ApiModelProperty(value = "result data")
    private T entity;



    public static <T> ResponseDto<T> ok(T entity) {
        ResponseDto<T> dto = new ResponseDto<>();
        dto.setStatus(ResponseConstants.STATUS_OK);
        dto.setEntity(entity);
        return dto;
    }

    public static FailResponseBuilder fail() {
        return new FailResponseBuilder();
    }

    public static FailResponseBuilder fail(String errorStatus) {
        return new FailResponseBuilder(errorStatus);
    }






    public static class FailResponseBuilder {
        private String status;
        private String errorCode;
        private String errorDesc;
        private Map<String, Object> errorInfos;

        public FailResponseBuilder() {
            this.status = ResponseConstants.STATUS_ERROR;
            errorInfos = new HashMap<>();
        }

        public FailResponseBuilder(String status) {
            this.status = status;
            errorInfos = new HashMap<>();
        }

        public FailResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        public FailResponseBuilder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public FailResponseBuilder errorDesc(String errorDesc) {
            this.errorDesc = errorDesc;
            return this;
        }

        public FailResponseBuilder putErrorInfo(String key, Object value) {
            this.errorInfos.put(key, value);
            return this;
        }

        public FailResponseBuilder setErrorInfos(Map<String, Object> errorInfos) {
            this.errorInfos = errorInfos;
            return this;
        }

        public <T> ResponseDto<T> build() {
            ResponseDto<T> dto = new ResponseDto<>();
            dto.setStatus(this.status);
            dto.setErrorCode(this.errorCode);
            dto.setErrorDesc(this.errorDesc);
            if (!this.errorInfos.isEmpty()) {
                dto.setErrorInfos(this.errorInfos);
            }
            return dto;
        }
    }
}
