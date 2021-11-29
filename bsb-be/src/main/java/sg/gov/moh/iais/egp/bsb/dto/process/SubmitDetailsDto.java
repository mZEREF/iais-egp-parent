package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationMiscDto;
import sg.gov.moh.iais.egp.bsb.dto.process.afc.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.process.approval.ApprovalAppDto;
import sg.gov.moh.iais.egp.bsb.dto.process.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.entity.Biological;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class SubmitDetailsDto implements Serializable {
    //submission details
    private String applicationNo;
    private String applicationType;
    private String processType;
    private String submissionDate;
    private String applicationStatus;

    private String activityType;
    private String facilityName;
    private String blkNo;
    private String streetName;
    private String floorNo;
    private String unitNo;
    private String postalCode;

    private String facilityOrApprovalExpiryDate;
    //applicant details
    private String facilityOrOrganisationName;
    private String facilityOrOrganisationAddress;
    private String facilityOrOrganisationAdmin;
    private String telephone;
    private String email;
    //list bat
    private List<Biological> biologicalList;
    //view application info, different dto are displayed according to different processType
    private FacilityRegisterDto facilityRegisterDto;
    private ApprovalAppDto approvalAppDto;
    private FacilityCertifierRegisterDto facilityCertifierRegisterDto;
    //the last ao,do,hm applicationMisc
    private Map<String, ApplicationMiscDto> applicationMiscDtoMap;
}
