package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;


import java.util.List;


/**
 * @author YiMing
 * @version 2021/12/27 17:24
 **/
@Data
public class IncidentViewDto {
    private IncidentInfoDto incidentInfoDto;
    private PersonReportingInfoDto reportingInfoDto;

    private List<PersonInvolvedInfoDto> personInvolvedInfos;

    private List<DocRecordInfo> docRecordInfos;


}
