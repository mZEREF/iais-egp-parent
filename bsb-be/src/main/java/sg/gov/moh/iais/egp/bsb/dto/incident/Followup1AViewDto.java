package sg.gov.moh.iais.egp.bsb.dto.incident;


import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.Followup1ACauseDto;

import java.util.List;


/**
 * @author YiMing
 * @version 2022/1/20 9:50
 **/
@Data
public class Followup1AViewDto {
    private String referenceNo;
    private List<Followup1ACauseDto> causeDtoList;
    private List<DocRecordInfo> docRecordInfos;
}
