package sg.gov.moh.iais.egp.bsb.constant.module;


public class ApprovalBatAndActivityConstants {
    private ApprovalBatAndActivityConstants() {
    }

    public static final String KEY_ROOT_NODE_GROUP = "approvalAppRoot";
    public static final String KEY_APPROVAL_BAT_AND_ACTIVITY_DTO = "approvalBatAndActivityDto";
    public static final String KEY_APPROVAL_SELECTION_DTO = "approvalSelectionDto";
    public static final String KEY_FAC_PROFILE_DTO = "facProfileDto";
    public static final String KEY_APPROVAL_TO_ACTIVITY_DTO = "approvalToActivityDto";
    public static final String KEY_BAT_INFO = "batInfo";
    public static final String KEY_FAC_AUTHORISED_DTO = "facAuthorisedDto";
    public static final String KEY_FACILITY_ID = "facId";


    public static final String KEY_PROCESS_TYPE = "processType";

    public static final String KEY_NOT_EXIST_FAC_ACTIVITY_TYPE_APPROVAL_LIST = "notExistFacActivityTypeApprovalList";

    public static final String KEY_DOC_SETTINGS             = "docSettings";
    public static final String KEY_OTHER_DOC_TYPES          = "otherDocTypes";
    public static final String NODE_NAME_BEGIN              = "begin";
    public static final String NODE_NAME_APPROVAL_SELECTION = "approvalSelection";
    public static final String NODE_NAME_COMPANY_INFO       = "companyInfo";
    public static final String KEY_DOC_TYPES_JSON           = "docTypeOpsJson";
    public static final String KEY_OPTIONS_DOC_TYPES        = "docTypeOps";

    public static final String KEY_APP_NO                  = "appNo";
    public static final String KEY_APP_DT                  = "appDt";

    public static final String KEY_PRINT_MASK_PARAM        = "printApprovalApp";
    public static final String KEY_PRINT_MASKED_ID         = "printApprovalAppId";

    public static final String NODE_NAME_APP_INFO = "appInfo";
    public static final String NODE_NAME_FAC_PROFILE = "facProfile";
    public static final String NODE_NAME_POSSESS_BAT = "possessBat";
    public static final String NODE_NAME_LARGE_BAT = "largeBat";
    public static final String NODE_NAME_SPECIAL_BAT = "specialBat";
    public static final String NODE_NAME_FAC_AUTHORISED = "facAuthorised";
    public static final String NODE_NAME_FAC_ACTIVITY = "facActivity";
    public static final String NODE_NAME_PRIMARY_DOC = "primaryDoc";
    public static final String NODE_NAME_PREVIEW = "preview";

    public static final String SELECTION_FACILITY_ID = "selectionFacilityId";

    public static final String FEIGN_CLIENT = "approvalBatAndActivityFeignClient";

    public static final String KEY_ALLOW_SAVE_DRAFT        = "isAllowToSaveDraft";
    public static final String KEY_EDIT_APP_ID = "editId";
    public static final String KEY_ACTION_TYPE = "action_type";
    public static final String KEY_INDEED_ACTION_TYPE = "indeed_action_type";
    public static final String KEY_ACTION_VALUE = "action_value";
    public static final String KEY_VALIDATION_ERRORS = "errorMsg";
    public static final String ACTION_LOAD_DRAFT = "action_load_draft";
    public static final String HAVE_SUITABLE_DRAFT_DATA = "haveSuitableDraftData";
    public static final String DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO = "draftApprovalBatAndActivityDto";

    public static final String KEY_NAV_NEXT = "next";
    public static final String KEY_NAV_PREVIOUS            = "previous";

    public static final String KEY_ACTION_SUBMIT = "submit";
    public static final String KEY_ACTION_JUMP = "jump";
    public static final String KEY_JUMP_DEST_NODE = "destNode";
    public static final String KEY_DEST_NODE_ROUTE = "nodeRoute";

    public static final String KEY_SHOW_ERROR_SWITCH = "needShowValidationError";

    public static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    public static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    public static final String ERR_MSG_INVALID_ACTION = "Invalid action";

    public static final String KEY_SCHEDULE_BAT_MAP        = "scheduleBatMap";
    public static final String KEY_SCHEDULE_BAT_MAP_JSON   = "scheduleBatMapJson";
    public static final String KEY_AUTH_PERSONNEL_DETAIL_MAP_JSON   = "authPersonnelDetailMapJson";

    public static final String KEY_USER_ID_FACILITY_AUTH_MAP = "facilityAuthIdMap";
    public static final String KEY_OPTIONS_AUTH_PERSONNEL = "authPersonnelOps";

    public static final String KEY_OPTIONS_NATIONALITY     = "nationalityOps";
    public static final String KEY_OPTIONS_ADDRESS_TYPE    = "addressTypeOps";
    public static final String KEY_OPTIONS_SCHEDULE        = "scheduleOps";
    public static final String KEY_SCHEDULE_FIRST_OPTION   = "firstScheduleOp";

    public static final String KEY_ACTION_SAVE_AS_DRAFT = "draft";

    // draft
    public static final String KEY_IND_AFTER_SAVE_AS_DRAFT          = "AFTER_SAVE_AS_DRAFT";
}
