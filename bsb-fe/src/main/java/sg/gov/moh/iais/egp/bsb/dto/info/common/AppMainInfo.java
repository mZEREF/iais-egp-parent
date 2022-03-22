package sg.gov.moh.iais.egp.bsb.dto.info.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppMainInfo extends AppBasicInfo {
    @JMap("applicationDt")
    private Date date;

    @JMap
    private String assigned;

    @JMap
    @JsonProperty("stage_id")
    private String stageId;
}
