package sg.gov.moh.iais.egp.bsb.dto.incident;


import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.FollowupNoteDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.IncidentDocDto;

import java.util.List;


/**
 * @author YiMing
 * @version 2022/1/20 13:19
 **/
@Data
public class FollowupViewDto {
    private ApplicationInfoDto applicationInfoDto;
    private IncidentBatViewDto incidentBatViewDto;
    private IncidentPersonnelDto incidentPersonnelDto;
    private List<IncidentDocDto> incidentDocDtoList;
    private List<FollowupNoteDto> followupNoteDtoList;
}
