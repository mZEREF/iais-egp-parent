package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

import java.util.Arrays;
import java.util.List;

public class AppWithdrawableJudge implements AppActionJudge {
    private final String appType;
    private final String appStatus;

    public AppWithdrawableJudge(String appType, String appStatus) {
        this.appType = appType;
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        List<String> withdrawableAppTypes = Arrays.asList(MasterCodeConstants.APP_TYPE_NEW, MasterCodeConstants.APP_TYPE_RENEW,
                MasterCodeConstants.APP_TYPE_RFC, MasterCodeConstants.APP_TYPE_CANCEL, MasterCodeConstants.APP_TYPE_DEREGISTRATION,
                MasterCodeConstants.APP_TYPE_SUBMISSION);
        List<String> notWithdrawableAppStatus = Arrays.asList(MasterCodeConstants.APP_STATUS_WITHDRAW, MasterCodeConstants.APP_STATUS_REJECTED,
                MasterCodeConstants.APP_STATUS_APPROVED, MasterCodeConstants.APP_STATUS_DRAFT, MasterCodeConstants.APP_STATUS_REMOVED);
        return withdrawableAppTypes.contains(appType) && !notWithdrawableAppStatus.contains(appStatus);
    }
}
