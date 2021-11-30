package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import net.sf.oval.constraint.ValidateWithMethod;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnquiryDto extends PagingAndSortingDto implements Serializable {
    private static final String MESSAGE_END_DATE_EARLIER_THAN_START_DATE = "EndDate can not be earlier than startDate.";
    private String id;

    private String applicationNo;

    private String applicationType;

    private String applicationStatus;

    private Date applicationSubmissionDateFrom;

    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkSubDtToAfterFrom", parameterType = Date.class, profiles = {"app"})
    private Date applicationSubmissionDateTo;

    private Date approvalDateFrom;

    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkApprovalDtToAfterFrom", parameterType = Date.class, profiles = {"app"})
    private Date approvalDateTo;

    private String facilityClassification;

    private List<String> facilityType;

    private String facilityName;

    private String scheduleType;

    private String biologicalAgent;

    private String riskLevelOfTheBiologicalAgent;

    private String processType;

    private Date facilityExpiryDateFrom;

    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkExpiryDtToAfterFrom", parameterType = Date.class, profiles = {"fac"})
    private Date facilityExpiryDateTo;

    private String gazettedArea;

    private String facilityOperator;

    private String facilityAdmin;

    private String authorisedPersonnelWorkingInFacility;

    private String biosafetyCommitteePersonnel;

    private String facilityStatus;

    private String approvedFacilityCertifier;

    private String[] natureOfTheSample;

    private List<String> natureOfTheSamples;

    private String approvalType;

    private String approvalNo;

    private Date approvalSubmissionDateFrom;

    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkApprovalSubDtToAfterFrom", parameterType = Date.class, profiles = {"approval"})
    private Date approvalSubmissionDateTo;

    private String approvalStatus;

    private String organisationName;

    private String facilityAdministrator;

    private String afcStatus;

    private String teamMemberName;

    private String teamMemberID;

    private Date approvedDateFrom;

    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkApprovedDtToAfterFrom", parameterType = Date.class, profiles = {"approval,org"})
    private Date approvedDateTo;

    private String action;


    public void clearAllFields() {
        id = "";
        applicationNo = "";
        applicationType = "";
        applicationStatus = "";
        applicationSubmissionDateFrom = null;
        applicationSubmissionDateTo = null;
        approvalDateFrom = null;
        approvalDateTo = null;
        facilityClassification = "";
        facilityType = null;
        facilityName = "";
        scheduleType = "";
        biologicalAgent = "";
        riskLevelOfTheBiologicalAgent = "";
        processType = "";
        facilityExpiryDateFrom = null;
        facilityExpiryDateTo = null;
        facilityAdmin = "";
        gazettedArea = "";
        facilityOperator = "";
        authorisedPersonnelWorkingInFacility = "";
        biosafetyCommitteePersonnel = "";
        facilityStatus = "";
        approvedFacilityCertifier = "";
        natureOfTheSample = null;
        natureOfTheSamples = null;
        approvalType = "";
        approvalSubmissionDateFrom = null;
        approvalSubmissionDateTo = null;
        approvalStatus = "";
        organisationName = "";
        facilityAdministrator = "";
        approvedDateFrom = null;
        approvedDateTo = null;
        afcStatus = "";
        teamMemberName = "";
        teamMemberID = "";
    }
    private boolean checkSubDtToAfterFrom(Date applicationSubmissionDateTo) {
        if (applicationSubmissionDateTo == null || applicationSubmissionDateFrom == null){
            return true;
        }
        return applicationSubmissionDateTo.after(applicationSubmissionDateFrom);
    }

    private boolean checkApprovalDtToAfterFrom(Date approvalDateTo) {
        if (approvalDateTo == null || approvalDateFrom == null){
            return true;
        }
        return approvalDateTo.after(approvalDateFrom);
    }

    private boolean checkExpiryDtToAfterFrom(Date facilityExpiryDateTo) {
        if (facilityExpiryDateTo == null || facilityExpiryDateFrom == null){
            return true;
        }
        return facilityExpiryDateTo.after(facilityExpiryDateFrom);
    }

    private boolean checkApprovalSubDtToAfterFrom(Date approvalSubmissionDateTo) {
        if (approvalSubmissionDateTo == null || approvalSubmissionDateFrom == null){
            return true;
        }
        return approvalSubmissionDateTo.after(approvalSubmissionDateFrom);
    }

    private boolean checkApprovedDtToAfterFrom(Date approvedDateTo) {
        if (approvedDateTo == null || approvedDateFrom == null){
            return true;
        }
        return approvedDateTo.after(approvedDateFrom);
    }
}
