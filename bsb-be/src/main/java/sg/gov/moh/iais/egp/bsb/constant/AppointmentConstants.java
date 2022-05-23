package sg.gov.moh.iais.egp.bsb.constant;

/**
 * @author tangtang
 * @date 2022/3/7 15:41
 */
public class AppointmentConstants {
    private AppointmentConstants() {}

    public static final String APPOINTMENT_REVIEW_DATA = "apptReviewData";
    public static final String APPOINTMENT_INSPECTION_DATE_DTO = "apptInspectionDateDto";
    public static final String APPOINTMENT_INSPECTION_DATE_DRAFT_DTO = "scheduledInspApptDraftDtos";
    public static final String INSPECTION_INFO_DTO = "inspectionInfoDto";
    public static final String BUTTON_FLAG = "buttonFlag";
    public static final String SPEC_BUTTON_FLAG = "specButtonFlag";
    public static final String INSP_DATE_LIST = "inspDateList";
    public static final String APPT_REF_NO_USER_CAL_DTO_MAP = "apptRefNoUserCalDtoMap";

    public static final String PROCESS_DEC_CONFIRM_DATE = "confirm";
    public static final String PROCESS_DEC_SPECIFY_NEW_DATE = "specify";
    public static final String KEY_SPECIFY_START_DATE = "specifyStartDate";
    public static final String KEY_SPECIFY_START_HOUR = "specifyStartHour";
    public static final String KEY_SPECIFY_END_DATE = "specifyEndDate";
    public static final String KEY_SPECIFY_END_HOUR = "specifyEndHour";

    public static final String KEY_START_HOURS_OPTION = "startHoursOption";
    public static final String KEY_END_HOURS_OPTION = "endHoursOption";

    public static final String KEY_ERROR_SPECIFIC_DATE = "errorSpecificDate";
    public static final String ERROR_MSG_END_DATE_BEFORE_START_DATE = "EndDate can not be earlier than startDate";
    public static final String ERROR_MSG_COLLISION_DATE = "non-working day, please reselect another date";

    public static final String BACK_URL = "backUrl";
    public static final String BACK_URL_TASK_LIST = "/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList";
    public static final String BACK_URL_RESCHEDULE_APPOINTMENT = "/bsb-web/eservicecontinue/INTRANET/BsbOfficerRescheduleApptList";

}
