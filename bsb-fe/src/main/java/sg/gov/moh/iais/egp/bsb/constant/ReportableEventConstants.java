package sg.gov.moh.iais.egp.bsb.constant;

/**
 * @author YiMing
 * @version 2021/12/1 16:24
 **/
public class ReportableEventConstants {
    private ReportableEventConstants() {}

    public static final String MODULE_NAME_NOTIFICATION_OF_INCIDENT = "Notification of Incident";
    public static final String MODULE_NAME_INVESTIGATION_REPORT = "Investigation Report";
    public static final String MODULE_NAME_INCIDENT_FOLLOW_UP = "Incident Follow-up";
    public static final String KEY_ROOT_NODE_GROUP_INCIDENT_NOT = "incidentNotRoot";
    public static final String KEY_ROOT_NODE_GROUP_INVEST_REPORT = "investRepoRoot";

    public static final String NODE_NAME_INCIDENT_INFO         = "incidentInfo";
    public static final String NODE_NAME_PERSON_REPORTING_INFO = "reportingPerson";
    public static final String NODE_NAME_PERSON_INVOLVED_INFO  = "involvedPerson";
    public static final String NODE_NAME_DOCUMENTS             = "documents";
    public static final String NODE_NAME_PREVIEW_SUBMIT        = "previewSubmit";

    public static final String NODE_NAME_INCIDENT_INVESTIGATION = "incidentInvest";
    public static final String NODE_NAME_MEDICAL_INVESTIGATION = "medicalInvest";
    public static final String NODE_NAME_REFERENCE_NO_SELECTION = "referNoSelect";


    //usual key --------------------
    public static final String KEY_ACTION_VALUE       = "action_value";
    public static final String KEY_ACTION_TYPE        = "action_type";
    public static final String KEY_NODE_ROUTE         = "nodeRoute";
    public static final String KEY_JUMP_DEST_NODE     = "destNode";
    public static final String KEY_INDEED_ACTION_TYPE = "indeed_action_type";

    public static final String KEY_ACTION_JUMP   = "jump";
    public static final String KEY_ACTION_SUBMIT = "submit";

    public static final String KEY_NAV_NEXT      = "next";
    public static final String KEY_NAV_BACK      = "back";
    public static final String KEY_NAV_SAVE_DRAFT = "draft";

    public static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    public static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    public static final String ERR_MSG_INVALID_ACTION = "Invalid action";
    public static final String KEY_SHOW_ERROR_SWITCH = "needShowValidationError";
    public static final String KEY_VALIDATION_ERRORS = "errorMsg";

    public static final String KEY_PROCESS_TYPE_NOTIFICATION = "notification";
    public static final String KEY_PROCESS_TYPE_INVESTIGATION = "investigation";
    public static final String KEY_PROCESS_TYPE_FOLLOW_UP_1A = "followup1A";
    public static final String KEY_INCIDENT_TITLE = "incidentTitle";
    public static final String KEY_TITLE_INCIDENT_NOTIFICATION = "Notification of Incident";
    public static final String KEY_TITLE_INVESTIGATION_REPORT  = "Investigation Report";
    public static final String KEY_TITLE_FOLLOW_UP_REPORT_1A   = "Follow-up Report 1A";
    public static final String KEY_TITLE_FOLLOW_UP_REPORT_1B   = "Follow-up Report 1B";
}
