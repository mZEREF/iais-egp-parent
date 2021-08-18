package sg.gov.moh.iais.egp.bsb.dto.revocation;


import lombok.Data;

import java.io.Serializable;

/**
 * @author Zhu Tangtang
 * @date 2021/7/26 17:08
 */
@Data
public class RevocationDetailsDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String applicationId;
    private String facilityId;
    private String facilityName;
    private String facilityAddress;
    private String blockNo;
    private String postalCode;
    private String floorNo;
    private String unitNo;
    private String streetName;
    private String facilityClassification;
    private String facilityType;
    private String approval;
    private String approvalStatus;
    private String currentStatus;
    private String applicationNo;
}
