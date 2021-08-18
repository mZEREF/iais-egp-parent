package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Zhu Tangtang
 * @date 2021/7/26 18:01
 */
@Data
public class AOQueryInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String facilityId;
    private String applicationNo;
    private String appType;
    private String facilityName;
    private String blockNo;
    private String postalCode;
    private String floorNo;
    private String unitNo;
    private String streetName;
    private String facilityType;
    private String processType;
    private String biologicalAgentsAndToxins;
    private Date applicationDt;
    private Date approvalDate;
    private String status;
    private String facilityStatus;
    private String createdBy;
    private Date createdAt;
    private String modifiedBy;
    private Date modifiedAt;
}
