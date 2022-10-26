package sg.gov.moh.iais.egp.bsb.service.inbox;


import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AFC_INPUT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AFC_REPORT_UPLOAD;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_NEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_RENEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_TYPE_RFC;

public class AFCUploadReportJudge implements AppActionJudge{
    private final String appType;
    private final String appStatus;

    public AFCUploadReportJudge(String appType, String appStatus) {
        this.appType = appType;
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        List<String> afcAppTypes = Arrays.asList(APP_TYPE_NEW, APP_TYPE_RENEW, APP_TYPE_RFC);
        List<String> afcAppStatus = Arrays.asList(APP_STATUS_PEND_AFC_REPORT_UPLOAD, APP_STATUS_PEND_AFC_INPUT);
        return afcAppTypes.contains(appType) && afcAppStatus.contains(appStatus);
    }
}
