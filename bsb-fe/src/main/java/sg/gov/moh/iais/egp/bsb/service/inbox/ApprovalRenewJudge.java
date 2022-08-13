package sg.gov.moh.iais.egp.bsb.service.inbox;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_FAC_CERTIFIER_REG;

public class ApprovalRenewJudge implements AppActionJudge {
    private final String processType;
    private final String approvalStatus;

    public ApprovalRenewJudge(String processType, String approvalStatus) {
        this.processType = processType;
        this.approvalStatus = approvalStatus;
    }

    @Override
    public boolean judge() {
        return PROCESS_TYPE_FAC_CERTIFIER_REG.equals(processType) && APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL.equals(approvalStatus);
    }
}
