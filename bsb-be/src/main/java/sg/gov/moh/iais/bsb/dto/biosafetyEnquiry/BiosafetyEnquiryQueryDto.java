package sg.gov.moh.iais.bsb.dto.biosafetyEnquiry;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 16:08
 * DESCRIPTION: TODO
 **/
@Getter
@Setter
public class BiosafetyEnquiryQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String applicationNo;
    private String applicationType;
    private String applicationStatus;
    private Date applicationSubmissionDate;
    private Date approvalDate;
    private String facilityClassification;
    private String facilityType;
    private String facilityName;
    private String biologicalAgent;
    private String riskLevelOfTheBiologicalAgent;
    private String processType;
    private String verifiedByDO;
    private String verifiedByAO;
    private String verifiedByHM;
    private String approvalType;
    private String approvalStatus;
    private String facilityAddress;
    private String facilityStatus;
    private String agent;
    private String natureOfTheSample;
    private String physicalPossessionOfBA;
    private String organisationName;
    private String organisationAddress;
    private String AFCStatus;
    private String administrator;
    private Date approvedDate;
    private Date expiryDate;
    private String facilityExpiryDate;
    private String gazettedArea;
    private String facilityOperator;
    private String facilityAdmin;
    private String currentFacilityStatus;
    private String approvedFacilityCertifier;
    private String action;

}
