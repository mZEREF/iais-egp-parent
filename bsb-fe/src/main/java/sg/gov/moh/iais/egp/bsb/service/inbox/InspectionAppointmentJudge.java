package sg.gov.moh.iais.egp.bsb.service.inbox;


import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_SCHEDULING;

public class InspectionAppointmentJudge implements AppActionJudge {
    private final String appStatus;

    public InspectionAppointmentJudge(String appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public boolean judge() {
        return appStatus.equals(APP_STATUS_PEND_APPOINTMENT_SCHEDULING);
    }
}
