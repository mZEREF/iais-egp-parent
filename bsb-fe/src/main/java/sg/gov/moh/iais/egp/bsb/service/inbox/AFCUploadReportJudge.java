package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

import java.util.Arrays;
import java.util.List;

public class AFCUploadReportJudge implements AppActionJudge{
    private final String appType;
    private final String appStatus;

    public AFCUploadReportJudge(String appType, String appStatus) {
        this.appType = appType;
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        List<String> afcAppTypes = Arrays.asList(MasterCodeConstants.APP_TYPE_NEW, MasterCodeConstants.APP_TYPE_RENEW, MasterCodeConstants.APP_TYPE_RFC);
        List<String> afcAppStatus = Arrays.asList(MasterCodeConstants.APP_STATUS_PEND_AFC_REPORT_UPLOAD, MasterCodeConstants.APP_STATUS_PEND_AFC_INPUT);
        return afcAppTypes.contains(appType) && afcAppStatus.contains(appStatus);
    }
}
