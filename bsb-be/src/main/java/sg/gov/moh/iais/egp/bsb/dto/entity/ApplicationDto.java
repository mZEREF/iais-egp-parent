package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class ApplicationDto extends BaseEntityDto {
    private String id;

    private List<ApplicationMiscDto> appMiscs;

    private String applicationNo;

    private String appType;

    private String processType;

    private String status;

    private Date applicationDt;

    private LocalDate approvalDate;

    private Date doVerifiedDt;

    private Date aoVerifiedDt;

    private Date hmVerifiedDt;

    private String assigned;

    private LocalDate effectiveDate;

    private ApprovalDto approval;

    private String stageId;

    private String forMainActivity;

    private ApplicationGroupDto appGroup;

    private String toBeSuspendReinstateAppId;

    private String doUserId;

    private String aoUserId;

    private String hmUserId;

    private LocalDate prefInspectionStart;

    private LocalDate prefInspectionEnd;
}
