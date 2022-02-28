package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;


public class DraftAppJudge implements AppActionJudge {
    private final String appStatus;

    public DraftAppJudge(String appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return MasterCodeConstants.APP_STATUS_DRAFT.equals(appStatus);
    }
}
