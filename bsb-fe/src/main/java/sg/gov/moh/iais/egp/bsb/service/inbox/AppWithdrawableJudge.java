package sg.gov.moh.iais.egp.bsb.service.inbox;


import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_ACCEPTED;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_APPROVED;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PARTIAL_ACCEPTANCE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_REJECTED;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_REMOVED;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_WITHDRAWN;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_CANCEL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_DEREGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_NEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_RENEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_RFC;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_SUBMISSION;

public class AppWithdrawableJudge implements AppActionJudge {
    private final String appType;
    private final String appStatus;

    public AppWithdrawableJudge(String appType, String appStatus) {
        this.appType = appType;
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        List<String> withdrawableAppTypes = Arrays.asList(APP_TYPE_NEW, APP_TYPE_RENEW, APP_TYPE_RFC, APP_TYPE_CANCEL, APP_TYPE_DEREGISTRATION, APP_TYPE_SUBMISSION);
        List<String> notWithdrawableAppStatus = Arrays.asList(APP_STATUS_WITHDRAWN, APP_STATUS_REJECTED, APP_STATUS_DRAFT, APP_STATUS_REMOVED,
                APP_STATUS_APPROVED, APP_STATUS_ACCEPTED, APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT, APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW, APP_STATUS_PARTIAL_ACCEPTANCE);
        return withdrawableAppTypes.contains(appType) && !notWithdrawableAppStatus.contains(appStatus);
    }
}
