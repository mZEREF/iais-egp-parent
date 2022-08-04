package sg.gov.moh.iais.egp.bsb.service.inbox;

import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_SCHEDULING;

public class InsAppointmentJudge  implements AppActionJudge{
    private final String appType;
    private final String appStatus;

    public InsAppointmentJudge(String appType, String appStatus) {
        this.appType = appType;
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        List<String> insAppointmentAppTypes = Arrays.asList(MasterCodeConstants.APP_TYPE_NEW, MasterCodeConstants.APP_TYPE_RENEW);
        return insAppointmentAppTypes.contains(appType) && appStatus.equals(APP_STATUS_PEND_APPOINTMENT_SCHEDULING);
    }
}
