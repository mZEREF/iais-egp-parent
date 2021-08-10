package sg.gov.moh.iais.bsb.dto.biosafetyEnquiry;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 16:21
 * DESCRIPTION: TODO
 **/

@Getter
@Setter
public class BiosafetyEnquiryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String applicationNo;
    private String applicationType;
    private String applicationStatus;
    private Date applicationSubmissionDateFrom;
    private Date applicationSubmissionDateTo;
    private Date approvalDateFrom;
    private Date approvalDateTo;
    private String facilityClassification;
    private String facilityType;
    private String facilityName;
    private String scheduleType;
    private String biologicalAgent;
    private String riskLevelOfTheBiologicalAgent;
    private String processType;
    private Date facilityExpiryDateFrom;
    private Date facilityExpiryDateTo;
    private String gazettedArea;
    private String facilityOperator;
    private String facilityAdmin;
    private String authorisedPersonnelWorkingInFacility;
    private String biosafetyCommitteePersonnel;
    private String facilityStatus;
    private String approvedFacilityCertifier;
    private String natureOfTheSample;
    private String approvalType;
    private Date approvalSubmissionDateFrom;
    private Date approvalSubmissionDateTo;
    private String approvalStatus;
    private String organisationName;
    private String facilityAdministrator;
    private String AfcStatus;
    private String teamMemberName;
    private String teamMemberID;
}
