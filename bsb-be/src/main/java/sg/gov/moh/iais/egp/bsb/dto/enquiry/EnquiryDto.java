package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 16:21
 * DESCRIPTION: TODO
 **/

@Data
@Repository
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnquiryDto implements Serializable {
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

    private List<String> facilityType;

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

    private String afcStatus;

    private String teamMemberName;

    private String teamMemberID;

    private Date approvedDateFrom;

    private Date approvedDateTo;
}
