package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class FacProfileDto implements Serializable {
    private String facilityName;
    private String facilityClassification;
    private List<String> existFacActivityTypeApprovalList;
}
