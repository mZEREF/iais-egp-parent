package sg.gov.moh.iais.egp.bsb.dto.processderegistration;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 */
@Data
public class SubmissionDetailsDto implements Serializable {
    //submission details
    private String applicationType;
    private String applicationNo;
    private String facilityType;
    private String processType;
    private String submissionDt;
    private String currentStatus;

    //applicant details
    private String facilityOrOrganisationName;
    private String facilityOrOrganisationAddress;
    private String facilityOrOrganisationAdmin;
    private String telephone;
    private String email;
}
