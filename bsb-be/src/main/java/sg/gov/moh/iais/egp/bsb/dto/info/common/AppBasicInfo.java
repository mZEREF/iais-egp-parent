package sg.gov.moh.iais.egp.bsb.dto.info.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.Stage;

import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppBasicInfo implements Serializable {
    @JMap
    private String id;

    @JMap("applicationNo")
    @JsonProperty("app_no")
    private String appNo;

    @JMap
    private String appType;

    @JMap
    @JsonProperty("process_type")
    private String processType;

    @JMap
    private String status;

    @JMap
    private Stage stage;
}
