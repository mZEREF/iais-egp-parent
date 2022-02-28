package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;


public class AppEditableJudge implements AppActionJudge {
    private final String appStatus;
    private final String assigned;

    public AppEditableJudge(String appStatus, String assigned) {
        this.appStatus = appStatus;
        this.assigned = assigned;
    }

    @Override
    public boolean judge() {
        return MasterCodeConstants.APP_STATUS_PEND_DO.equals(appStatus) && !MasterCodeConstants.YES.equals(assigned);
    }
}
