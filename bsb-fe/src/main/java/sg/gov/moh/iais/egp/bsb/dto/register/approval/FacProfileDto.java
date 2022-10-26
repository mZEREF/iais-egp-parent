package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.SourceFacDetails;

import java.io.Serializable;
import java.util.List;


@Data
public class FacProfileDto implements Serializable {
    private String facilityName;
    private String facilityClassification;
    private List<String> existFacActivityTypeApprovalList;

    //preload the gazette info which used to decide the availability of schedule
    private String protectedPlace;

    //preload facility detail when proc mode is 'Already in possession'
    private SourceFacDetails sourceFacDetails;
}
