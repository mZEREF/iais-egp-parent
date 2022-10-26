package sg.gov.moh.iais.egp.bsb.service.inbox;

import lombok.AllArgsConstructor;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APPROVAL_STATUS_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.FAC_CLASSIFICATION_RF;

@AllArgsConstructor
public class FacApplyApprovalJudge implements FacActionJudge {
    private final String facStatus;
    private final String facClassification;

    @Override
    public boolean judge() {
        return APPROVAL_STATUS_ACTIVE.equals(facStatus) && !FAC_CLASSIFICATION_RF.equals(facClassification);
    }
}
