package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.constant.Stage;

import java.util.Date;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class AppAndMiscDto extends BaseEntityDto {

    private String id;

    private List<ApplicationMiscDto> appMiscs;

    private String applicationNo;

    private String appType;

    private String processType;

    private String submissionType;

    private String status;

    private Stage stage;

    private Date submitDate;

    private Date approveDate;

    private String assigned;

    private String applicantUen;

    private String applicantUserId;

    private String doUserId;

    private String doInChargeUserId;

    private String aoUserId;

    private String hmUserId;
}
