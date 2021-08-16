package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 15:38
 * DESCRIPTION: TODO
 **/

@Data
public class FacilityInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String facilityId;

    private String facilityName;

    private String facilityAddress;

    private String facilityClassification;

    private String facilityType;

    private String biologicalAgent;

    private String riskLevelOfTheBiologicalAgent;

    private Date facilityExpiryDate;

    private String gazettedArea;

    private String facilityOperator;

    private String facilityAdmin;

    private String currentFacilityStatus;

    private String approvedFacilityCertifier;

//    @Transient
//    private String action;
}
