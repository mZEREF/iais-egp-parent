package sg.gov.moh.iais.egp.bsb.service.inbox;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_NC_RECTIFICATION;

public class InspectionNCJudge implements AppActionJudge{
    private final String appStatus;

    public InspectionNCJudge(String appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return appStatus.equals(APP_STATUS_PEND_NC_RECTIFICATION);
    }
}
