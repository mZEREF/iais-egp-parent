package sg.gov.moh.iais.egp.bsb.constant;

/**
 * @author YiMing
 * @version 2021/12/1 16:24
 **/
public class IncidentNotificationConstants {
    private IncidentNotificationConstants() {}

    public static final String MODULE_NAME = "Notification of Incident";
    public static final String KEY_ROOT_NODE_GROUP = "incidentNotRoot";

    public static final String NODE_NAME_INCIDENT_INFO         = "incidentInfo";
    public static final String NODE_NAME_PERSON_REPORTING_INFO = "reportingPerson";
    public static final String NODE_NAME_PERSON_INVOLVED_INFO  = "involvedPerson";
    public static final String NODE_NAME_DOCUMENTS             = "documents";
    public static final String NODE_NAME_PREVIEW_SUBMIT        = "previewSubmit";


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


}
