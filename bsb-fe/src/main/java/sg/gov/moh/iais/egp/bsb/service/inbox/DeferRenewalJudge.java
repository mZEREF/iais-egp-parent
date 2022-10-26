package sg.gov.moh.iais.egp.bsb.service.inbox;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.FAC_CLASSIFICATION_RF;


@AllArgsConstructor
public class DeferRenewalJudge implements FacActionJudge {
    private final String facStatus;
    private final String facClassification;
    private final Boolean renewable;

    @Override
    public boolean judge() {
        List<String> facStatusList = Arrays.asList(APPROVAL_STATUS_ACTIVE, APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL);
        return facStatusList.contains(facStatus) && !FAC_CLASSIFICATION_RF.equals(facClassification) && Boolean.TRUE == renewable;
    }
}
