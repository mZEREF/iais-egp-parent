package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

public class InspectionFollowUpJudge implements AppActionJudge{
    private final String appType;
    private final String appStatus;

    public InspectionFollowUpJudge(String appType, String appStatus) {
        this.appType = appType;
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return appType.equals(MasterCodeConstants.APP_TYPE_NEW) && appStatus.equals(MasterCodeConstants.APP_STATUS_PEND_SUBMIT_FOLLOW_UP_ITEMS);
    }
}
