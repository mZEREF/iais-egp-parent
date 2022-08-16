package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.approval.ApprovalBasicInfo;

import java.util.List;


@Data
public class InboxApprovalAFCResultDto {
    private PageInfo pageInfo;
    private List<ApprovalBasicInfo> approvalInfos;
}
