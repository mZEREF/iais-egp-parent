package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPLICANT_INPUT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_FOLLOW_UP_ITEM_VERIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_WITHDRAWN;

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
        // TODO: check these app status
        List<String> notWithdrawableAppStatus = Arrays.asList(APP_STATUS_WITHDRAWN, MasterCodeConstants.APP_STATUS_REJECTED, MasterCodeConstants.APP_STATUS_APPROVED, MasterCodeConstants.APP_STATUS_DRAFT, MasterCodeConstants.APP_STATUS_REMOVED,
                APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION, APP_STATUS_PEND_DO_FOLLOW_UP_ITEM_VERIFICATION, MasterCodeConstants.APP_STATUS_PEND_EXTENSION_REVIEW,
                MasterCodeConstants.APP_STATUS_PEND_AFC_REPORT_UPLOAD, MasterCodeConstants.APP_STATUS_PEND_AFC_INPUT, APP_STATUS_PEND_APPLICANT_INPUT, MasterCodeConstants.APP_STATUS_PEND_DO_REPORT_REVIEW, MasterCodeConstants.APP_STATUS_PEND_AO_REPORT_REVIEW);
        return withdrawableAppTypes.contains(appType) && !notWithdrawableAppStatus.contains(appStatus);
    }
}
