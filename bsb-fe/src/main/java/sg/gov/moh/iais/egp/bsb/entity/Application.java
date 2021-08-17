package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class Application {
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

    private Date createdAt;

    private String createdBy;

    private Date modifiedAt;

    private String modifiedBy;
}
