package sg.gov.moh.iais.egp.bsb.dto.process.approval;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Data
@Slf4j
public class ApprovalAppDto implements Serializable {
    //View Application button info
    private Map<String, ApprovalProfileDto> approvalProfileMap;
    private Collection<DocRecordInfo> docRecordInfos;
}
