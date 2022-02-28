package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;


public class SelfAssessJudge implements AppActionJudge {
    private final String appStatus;

    public SelfAssessJudge(String appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return MasterCodeConstants.APP_STATUS_PEND_SUBMIT_SELF_ASSESSMENT.equals(appStatus);
    }
}