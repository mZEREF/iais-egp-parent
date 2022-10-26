package sg.gov.moh.iais.egp.bsb.service.inbox;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_CONDITIONAL_INVENTORY_MOVEMENT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_NC;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_OTHERS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_PENDING_INVESTIGATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL;


@AllArgsConstructor
public class IncidentReportingJudge implements FacActionJudge {
    private final String facStatus;

    @Override
    public boolean judge() {
        List<String> facStatusList = Arrays.asList(APPROVAL_STATUS_ACTIVE, APPROVAL_STATUS_SUSPENDED_CONDITIONAL_INVENTORY_MOVEMENT, APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL, APPROVAL_STATUS_SUSPENDED_NC, APPROVAL_STATUS_SUSPENDED_PENDING_INVESTIGATION, APPROVAL_STATUS_SUSPENDED_OTHERS);
        return facStatusList.contains(facStatus);
    }
}
