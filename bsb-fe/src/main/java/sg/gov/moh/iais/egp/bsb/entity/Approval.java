package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Approval {
    private String id;
    private String processType;
    private String approveNo;
    private String status;
    private Date approvalSubmissionDate;
    private Date approvalStartDate;
    private Date approvalExpiryDate;
    private Date createdAt;
    private String createdBy;
    private Date modifiedAt;
    private String modifiedBy;
}
