package sg.gov.moh.iais.egp.bsb.service.inbox;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION;

public class InspectionFollowUpJudge implements AppActionJudge{
    private final String appStatus;

    public InspectionFollowUpJudge(String appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return appStatus.equals(APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION);
    }
}
