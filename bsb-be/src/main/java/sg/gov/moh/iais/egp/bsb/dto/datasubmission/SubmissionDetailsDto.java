package sg.gov.moh.iais.egp.bsb.dto.datasubmission;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SubmissionDetailsDto implements Serializable {
    //submission details
    private String applicationType;
    private String applicationNo;
    private String processType;
    private Date submissionDate;
    private String currentStatus;
    private String submissionType;
    private String submissionNo;

    //facility Profile
    private String facilityOrOrganisationName;
    private String facilityOrOrganisationAddress;
    private String telephone;
    private String email;
}
