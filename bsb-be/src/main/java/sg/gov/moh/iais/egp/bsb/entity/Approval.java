package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;

@Data
public class Approval extends BaseEntity {
    private String id;

    private String processType;

    private String approveNo;

    private String status;

    private Date approvalSubmissionDate;

    private Date approvalStartDate;

    private Date approvalExpiryDate;
}
