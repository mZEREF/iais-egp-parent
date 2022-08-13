package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;


public class RFIJudge implements AppActionJudge {
    private final String appStatus;

    public RFIJudge(String appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return MasterCodeConstants.APP_STATUS_PEND_APPLICANT_INPUT.equals(appStatus) || MasterCodeConstants.APP_STATUS_PEND_APPLICANT_CLARIFICATION.equals(appStatus);
    }
}
