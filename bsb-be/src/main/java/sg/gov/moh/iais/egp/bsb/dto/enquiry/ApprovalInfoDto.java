package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 15:51
 * DESCRIPTION: TODO
 **/

@Getter
@Setter
public class ApprovalInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String approvalType;
    private String approvalStatus;
    private String facilityClassification;
    private String facilityType;
    private String facilityName;
    private String facilityAddress;
    private String facilityStatus;
    private String agent;
    private String natureOfTheSample;
    private String riskLevelOfTheBiologicalAgent;
    private String physicalPossessionOfBA;
    private String action;

}
