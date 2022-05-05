package sg.gov.moh.iais.egp.bsb.dto.task;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Wenkang
 * @date 2019/11/7 13:18
 */
@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationGroupDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String groupNo;
    private String declStmt;
    private Integer isCharitable;
    private Integer isBundledFee;
    private Integer isByGiro;
    private Double amount;
    private String pmtRefNo;
    private String pmtStatus;
    private Date submitDt;
    private String submitBy;
    private Integer isInspectionNeeded;
    private Integer isPreInspection;
    private String status;
    private Date modifiedAt;
    private String feeDetails;
    private String licenseeId;
    private Date prefInspStartDate;
    private Date prefInspEndDate;
    private boolean autoApprove;
    private String appType;
    private AuditTrailDto auditTrailDto;
    private Date effectDate;
    private Date paymentDt;
    private Date ao3ApprovedDt;
    private String newLicenseeId;
    private String payMethod;
    private boolean submittedPrefDateFlag = false;  //only use by submitted inspection pref date
    // for payment updating
    private boolean autoRfc;
}
