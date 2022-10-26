package sg.gov.moh.iais.egp.bsb.dto.incident;


import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.util.List;


/**
 * @author YiMing
 * @version 2022/1/20 10:16
 **/
@Data
public class Followup1BViewDto {
    private String personnelName;

    private String followupDescription;

    private String testResult;

    private String isExpected;

    private String followupDuration;

    private String followupEntityDt;

    private String remark;

    private List<DocRecordInfo> docRecordInfos;

}
