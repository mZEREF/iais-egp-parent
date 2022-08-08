package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPLICANT_INPUT;

public class ApplicantUploadCertReportJudge implements AppActionJudge{
    private final String appType;
    private final String appStatus;

    public ApplicantUploadCertReportJudge(String appType, String appStatus) {
        this.appType = appType;
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        List<String> afcAppTypes = Arrays.asList(MasterCodeConstants.APP_TYPE_NEW, MasterCodeConstants.APP_TYPE_RENEW, MasterCodeConstants.APP_TYPE_RFC);
        // TODO: check this app status
        return afcAppTypes.contains(appType) && appStatus.equals(APP_STATUS_PEND_APPLICANT_INPUT);
    }
}
