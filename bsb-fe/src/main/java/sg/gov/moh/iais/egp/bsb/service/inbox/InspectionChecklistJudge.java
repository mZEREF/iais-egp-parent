package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;


public class InspectionChecklistJudge implements AppActionJudge {
    private final String appStatus;

    public InspectionChecklistJudge(String appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return MasterCodeConstants.APP_STATUS_PEND_CHECKLIST_SUBMISSION.equals(appStatus);
    }
}
