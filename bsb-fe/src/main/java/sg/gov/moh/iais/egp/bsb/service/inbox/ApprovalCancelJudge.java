package sg.gov.moh.iais.egp.bsb.service.inbox;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_CONDITIONAL_INVENTORY_MOVEMENT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_OTHERS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_FAC_CERTIFIER_REG;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE;

@AllArgsConstructor
public class ApprovalCancelJudge implements AppActionJudge {
    private final String processType;
    private final String approvalStatus;

    @Override
    public boolean judge() {
        List<String> cancelProcessTypes = Arrays.asList(PROCESS_TYPE_APPROVE_POSSESS, PROCESS_TYPE_APPROVE_LSP, PROCESS_TYPE_SP_APPROVE_HANDLE, PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE, PROCESS_TYPE_FAC_CERTIFIER_REG);
        List<String> cancelApprovalStatus = Arrays.asList(APPROVAL_STATUS_ACTIVE, APPROVAL_STATUS_SUSPENDED_OTHERS, APPROVAL_STATUS_SUSPENDED_CONDITIONAL_INVENTORY_MOVEMENT, APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL);
        return cancelProcessTypes.contains(processType) && cancelApprovalStatus.contains(approvalStatus);
    }
}
