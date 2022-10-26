package sg.gov.moh.iais.egp.bsb.dto.incident;


import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.FollowupNoteDto;

import java.io.Serializable;
import java.util.List;


/**
 * @author YiMing
 * @version 2022/1/20 13:19
 **/
@Data
public class FollowupViewDto implements Serializable {
    private ApplicationInfoDto applicationInfoDto;
    private IncidentBatViewDto incidentBatViewDto;
    private IncidentPersonnelDto incidentPersonnelDto;
    private List<DocDisplayDto> supportDocDisplayDtoList;
    private List<FollowupNoteDto> followupNoteDtoList;
}
