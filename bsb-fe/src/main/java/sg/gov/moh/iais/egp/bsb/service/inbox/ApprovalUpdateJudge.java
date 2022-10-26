package sg.gov.moh.iais.egp.bsb.service.inbox;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE;

@AllArgsConstructor
public class ApprovalUpdateJudge implements AppActionJudge {
    private final String processType;
    private final String approvalStatus;

    @Override
    public boolean judge() {
        List<String> updateProcessTypes = Arrays.asList(PROCESS_TYPE_APPROVE_POSSESS, PROCESS_TYPE_APPROVE_LSP, PROCESS_TYPE_SP_APPROVE_HANDLE);
        return updateProcessTypes.contains(processType) && APPROVAL_STATUS_ACTIVE.equals(approvalStatus);
    }
}
