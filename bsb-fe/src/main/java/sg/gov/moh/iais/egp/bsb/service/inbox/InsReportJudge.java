package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

public class InsReportJudge implements AppActionJudge {
    private final String appStatus;

    public InsReportJudge(String appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return MasterCodeConstants.APP_STATUS_PEND_INSPECTION_REPORT.equals(appStatus);
    }
}
