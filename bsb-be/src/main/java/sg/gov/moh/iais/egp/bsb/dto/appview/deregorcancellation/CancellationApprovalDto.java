package sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2022/1/11
 */
@Data
public class CancellationApprovalDto implements Serializable {
    private String approvalNo;
    private String processType;
    private String draftAppNo;

    private String facilityName;
    private String facilityAddress;
    private String approvalType;
    private String biologicalAgentToxin;
    private String physicalPossession;
    private String reasons;
    private String remarks;

    private List<DocRecordInfo> docRecordInfos;
}
