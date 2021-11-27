package sg.gov.moh.iais.egp.bsb.constant;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
public class ProcessContants {
    private ProcessContants() {}

    public static final String CRUD_ACTION_TYPE = "crud_action_type";
    public static final String CRUD_ACTION_TYPE_PROCESS = "process";
    public static final String CRUD_ACTION_TYPE_PREPARE = "prepare";

    public static final String PROCESS_FLOW_DOSCREENING = "DOScreening";
    public static final String PROCESS_FLOW_AOSCREENING = "AOScreening";
    public static final String PROCESS_FLOW_HMSCREENING = "HMScreening";
    public static final String PROCESS_FLOW_DOPROCESSING = "DOProcessing";
    public static final String PROCESS_FLOW_AOPROCESSING = "AOProcessing";
    public static final String PROCESS_FLOW_HMPROCESSING = "HMProcessing";

    public static final String DECISION_DO_SCREENED_BY_DO = "MOHPRO001";
    public static final String DECISION_DO_REQUEST_FOR_INFORMATION = "MOHPRO002";
    public static final String DECISION_DO_REJECT = "MOHPRO003";
    public static final String DECISION_DO_RECOMMEND_APPROVAL = "MOHPRO004";
    public static final String DECISION_DO_RECOMMEND_REJECTION = "MOHPRO005";

    public static final String DECISION_AO_APPROVE_FOR_INSPECTION = "MOHPRO006";
    public static final String DECISION_AO_REJECT = "MOHPRO007";
    public static final String DECISION_AO_ROUT_TO_DO = "MOHPRO008";
    public static final String DECISION_AO_ROUT_TO_HM = "MOHPRO009";
    public static final String DECISION_AO_APPROVED = "MOHPRO010";

    public static final String DECISION_HM_APPROVE = "MOHPRO011";
    public static final String DECISION_HM_REJECT = "MOHPRO012";

    public static final String MOH_PROCESS = "mohProcess";

    public static final String ERRORMSG = "errorMsg";

    public static final String KEY_VALIDATION_ERRORS = "errorMsg";
    public static final String KEY_APP_ID = "appId";
    public static final String KEY_TASK_ID = "taskId";
    public static final String KEY_ID = "id";

    public static final String KEY_SUBMIT_DETAILS_DTO = "submitDetailsDto";
    public static final String KEY_MOH_PROCESS_DTO = "mohProcessDto";
    public static final String KEY_BAT_LIST= "batList";
    public static final String KEY_APPROVAL_PROFILE_LIST = "approvalProfileList";

    public static final String ERR_MSG_FAIL_LOAD_SUBMIT_DETAILS = "Fail to load submitDetails";

    public static final String MODULE_NAME = "Moh Process";
}
