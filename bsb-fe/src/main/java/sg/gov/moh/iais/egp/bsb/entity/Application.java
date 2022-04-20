package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
public class Application implements Serializable {
    private String id;

    private List<ApplicationMisc> appMiscs;

    private Facility facility;

    private String applicationNo;

    private String appType;

    private String processType;

    private String status;

    private Date applicationDt;

    private Date approvalDate;

    private Date doVerifiedDt;

    private Date aoVerifiedDt;

    private Date hmVerifiedDt;

    private String assigned;

    private LocalDate effectiveDate;

    private String stageId;

    private String toBeSuspendReinstateAppId;

    private String doUserId;

    private String aoUserId;

    private String hmUserId;

    private int selfAssessmentFlag;

    private LocalDate prefInspectionStart;

    private LocalDate prefInspectionEnd;

    private Date createdAt;

    private String createdBy;

    private Date modifiedAt;

    private String modifiedBy;
}
