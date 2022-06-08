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
                MasterCodeConstants.APP_STATUS_APPROVED, MasterCodeConstants.APP_STATUS_DRAFT, MasterCodeConstants.APP_STATUS_REMOVED,
                MasterCodeConstants.APP_STATUS_PEND_SUBMIT_FOLLOW_UP_ITEMS,MasterCodeConstants.APP_STATUS_PEND_FOLLOW_UP_ITEMS_REVIEW,MasterCodeConstants.APP_STATUS_PEND_EXTENSION_REVIEW,
                MasterCodeConstants.APP_STATUS_PEND_AFC_REPORT_UPLOAD,MasterCodeConstants.APP_STATUS_PEND_AFC_INPUT,MasterCodeConstants.APP_STATUS_PEND_APPLICANT_REPORT_REVIEW,
                MasterCodeConstants.APP_STATUS_PEND_DO_REPORT_REVIEW,MasterCodeConstants.APP_STATUS_PEND_AO_REPORT_REVIEW);
        return withdrawableAppTypes.contains(appType) && !notWithdrawableAppStatus.contains(appStatus);
    }
}
