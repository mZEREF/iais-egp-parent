package sg.gov.moh.iais.egp.bsb.dto.info.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class AppBasicInfo implements Serializable {
    private String id;

    @JsonProperty("app_no")
    private String appNo;

    private String appType;

    @JsonProperty("process_type")
    private String processType;

    private String status;
}
