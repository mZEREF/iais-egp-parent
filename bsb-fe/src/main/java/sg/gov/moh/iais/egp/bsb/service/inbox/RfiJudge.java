package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;


public class RfiJudge implements AppActionJudge {
    private final String appStatus;

    public RfiJudge(String appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return MasterCodeConstants.APP_STATUS_PEND_INPUT.equals(appStatus);
    }
}
