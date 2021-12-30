package sg.gov.moh.iais.egp.bsb.dto.incident;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.FacilityProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.IncidentViewDto;

/**
 * @author YiMing
 * @version 2021/12/27 17:27
 **/
@Data
public class ApplicationInfoDto {
    private String appType;
    private String referenceNo;
    private String submissionDate;
    private String status;
    private IncidentViewDto incidentViewDto;
    private FacilityProfileDto facilityProfileDto;

}
