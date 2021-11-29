package sg.gov.moh.iais.egp.bsb.dto.audit;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Zhu Tangtang
 **/
@Data
public class OfficerProcessAuditDto implements Serializable {
    //facility
    private String facName;
    private String facAddress;
    private String facClassification;
    private String activityType;
    //audit
    private String remarks;
    private String changeReason;
    private String auditType;
    private Date auditDate;
    private String auditId;
    private String auditStatus;
    //audit app
    private Date requestAuditDate;
    private String doReason;
    private String doRemarks;
    private String doDecision;
    private String auditAppId;
    private String auditAppStatus;
    private String aoReason;
    private String aoRemarks;
    //task
    private String taskId;
    //application
    private String appStatus;
}
