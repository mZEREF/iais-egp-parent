package sg.gov.moh.iais.egp.bsb.dto.followup.view;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.util.List;

/**
 * @author YiMing
 * @version 2022/1/20 9:50
 **/
@Data
public class Followup1AViewDto {
    private String referenceNo;
    private String followupStatus;
    private String applicationId;
    private List<Followup1ACauseDto> causeDtoList;
    private List<DocRecordInfo> docRecordInfos;
}
