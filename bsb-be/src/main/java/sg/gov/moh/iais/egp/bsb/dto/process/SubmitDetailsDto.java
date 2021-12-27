package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationMiscDto;
import sg.gov.moh.iais.egp.bsb.entity.Biological;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class SubmitDetailsDto implements Serializable {
    //submission details
    private ApplicationDto applicationDto;

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
    //the last ao,do,hm applicationMisc
    private Map<String, ApplicationMiscDto> applicationMiscDtoMap;
}
