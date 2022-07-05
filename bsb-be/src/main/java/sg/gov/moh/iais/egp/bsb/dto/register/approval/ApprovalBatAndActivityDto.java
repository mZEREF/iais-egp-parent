package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;

import java.io.Serializable;
import java.util.Collection;


@Data
@NoArgsConstructor
public class ApprovalBatAndActivityDto implements Serializable {
    private String appType;
    private String appId;
    private ApprovalSelectionDto approvalSelectionDto;
    private FacProfileDto facProfileDto;
    private BiologicalAgentToxinDto approvalToPossessDto;
    private ApprovalToLargeDto approvalToLargeDto;
    private ApprovalToSpecialDto approvalToSpecialDto;
    private ApprovalToActivityDto approvalToActivityDto;
    private FacAuthorisedDto facAuthorisedDto;
    private Collection<DocRecordInfo> docRecordInfos;
}
