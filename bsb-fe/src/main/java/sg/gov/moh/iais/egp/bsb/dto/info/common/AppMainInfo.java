package sg.gov.moh.iais.egp.bsb.dto.info.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppMainInfo extends AppBasicInfo {
    private Date date;

    private String assigned;

    @JsonProperty("stage_id")
    private String stageId;

    @JsonProperty("for_main_activity")
    private String forMainActivity;

    @JsonProperty("app_group_id")
    private String appGroupId;
}
