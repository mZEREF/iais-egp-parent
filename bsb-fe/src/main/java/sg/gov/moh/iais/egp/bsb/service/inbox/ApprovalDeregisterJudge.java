package sg.gov.moh.iais.egp.bsb.service.inbox;

import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_CONDITIONAL_INVENTORY_MOVEMENT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_OTHERS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_FAC_CERTIFIER_REG;

public class ApprovalDeregisterJudge implements AppActionJudge {
    private final String processType;
    private final String approvalStatus;

    public ApprovalDeregisterJudge(String processType, String approvalStatus) {
        this.processType = processType;
        this.approvalStatus = approvalStatus;
    }

    @Override
    public boolean judge() {
        List<String> cancelApprovalStatus = Arrays.asList(APPROVAL_STATUS_ACTIVE, APPROVAL_STATUS_SUSPENDED_OTHERS, APPROVAL_STATUS_SUSPENDED_CONDITIONAL_INVENTORY_MOVEMENT, APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL);
        return PROCESS_TYPE_FAC_CERTIFIER_REG.equals(processType) && cancelApprovalStatus.contains(approvalStatus);
    }
}
