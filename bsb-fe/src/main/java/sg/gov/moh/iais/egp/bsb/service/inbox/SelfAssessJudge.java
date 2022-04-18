package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

import java.util.Arrays;


public class SelfAssessJudge implements AppActionJudge {
    private final String appStatus;

    public SelfAssessJudge(String appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return Arrays.asList(
                MasterCodeConstants.APP_STATUS_PEND_SUBMIT_SELF_ASSESSMENT,
                MasterCodeConstants.APP_STATUS_PEND_INSPECTION_READINESS,
                MasterCodeConstants.APP_STATUS_PEND_CLARIFICATION).contains(appStatus);
    }
}
