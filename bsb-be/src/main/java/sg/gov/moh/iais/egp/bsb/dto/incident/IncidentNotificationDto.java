package sg.gov.moh.iais.egp.bsb.dto.incident;


import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.IncidentDocDto;

import java.io.Serializable;
import java.util.List;


/**
 * @author YiMing
 * @version 2021/12/28 9:21
 **/
@Data
public class IncidentNotificationDto implements Serializable {
    private ApplicationInfoDto applicationInfoDto;
    private IncidentBatViewDto incidentBatViewDto;
    private IncidentPersonnelDto incidentPersonnelDto;
    private List<IncidentDocDto> incidentDocDtoList;
    private ProcessingDto processingDto;
    private String flag;
}
