package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Approval implements Serializable {
    private String id;
    private String processType;
    private String approveNo;
    private String status;
    private Date approvalDate;
    private Date approvalStartDate;
    private Date approvalExpiryDate;
    private String suspendedStatus;
    private String renewable;
}
