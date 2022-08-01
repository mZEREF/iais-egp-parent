package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION;

public class InspectionFollowUpJudge implements AppActionJudge{
    private final String appType;
    private final String appStatus;

    public InspectionFollowUpJudge(String appType, String appStatus) {
        this.appType = appType;
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        // TODO: check this app status
        return appType.equals(MasterCodeConstants.APP_TYPE_NEW) && appStatus.equals(APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION);
    }
}
