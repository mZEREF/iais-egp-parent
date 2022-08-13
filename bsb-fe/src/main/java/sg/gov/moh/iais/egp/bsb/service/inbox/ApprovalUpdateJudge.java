package sg.gov.moh.iais.egp.bsb.service.inbox;

import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE;

public class ApprovalUpdateJudge implements AppActionJudge {
    private final String processType;
    private final String approvalStatus;

    public ApprovalUpdateJudge(String processType, String approvalStatus) {
        this.processType = processType;
        this.approvalStatus = approvalStatus;
    }

    @Override
    public boolean judge() {
        List<String> updateProcessTypes = Arrays.asList(PROCESS_TYPE_APPROVE_POSSESS, PROCESS_TYPE_APPROVE_LSP, PROCESS_TYPE_SP_APPROVE_HANDLE, PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE);
        return updateProcessTypes.contains(processType) && APPROVAL_STATUS_ACTIVE.equals(approvalStatus);
    }
}
