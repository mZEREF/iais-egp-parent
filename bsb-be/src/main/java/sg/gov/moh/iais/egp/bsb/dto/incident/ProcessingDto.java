package sg.gov.moh.iais.egp.bsb.dto.incident;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.MohInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.ProcessHistoryDto;

import java.util.List;
import java.util.Map;


/**
 * @author YiMing
 * @version 2021/12/28 9:08
 **/
@Data
public class ProcessingDto {
    private String applicationId;
    private String taskId;
    private String currentStatus;
    private String remarks;
    private String decision;
    private String approvalOfficer;
    private Map<String, MohInfoDto> newRemarkMap;
    private List<ProcessHistoryDto> processHistoryList;
}
