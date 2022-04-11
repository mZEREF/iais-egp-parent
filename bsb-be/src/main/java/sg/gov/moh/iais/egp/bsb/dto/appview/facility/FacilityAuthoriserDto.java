package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;

import java.util.Date;

@Data
public class FacilityAuthoriserDto {
    private String id;

    private String facilityId;

    private String salutation;

    private String name;

    private String idType;

    private String idNumber;

    private String nationality;

    private String designation;

    private String contactNo;

    private String email;

    private Date employmentStartDate;

    private String employmentPeriod;

    private String workArea;

    private Date securityClearanceDate;
}
