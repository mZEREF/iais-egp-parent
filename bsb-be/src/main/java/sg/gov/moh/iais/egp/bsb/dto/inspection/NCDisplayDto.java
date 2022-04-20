package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;

import java.io.Serializable;

@Data
public class NCDisplayDto implements Serializable {
    private String id;
    private String itemDescription;
    private String finding;
    private String actionRequired;
    private Boolean rectified;
    private String applicantInput;
    private Boolean excludeFromApplicantVersion;
}
