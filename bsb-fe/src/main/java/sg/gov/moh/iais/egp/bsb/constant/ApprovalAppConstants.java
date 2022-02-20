package sg.gov.moh.iais.egp.bsb.constant;

/**
 * @author : LiRan
 * @date : 2021/10/8
 */
public class ApprovalAppConstants {
    private ApprovalAppConstants() {}

    public static final String NODE_NAME_COMPANY_INFO = "compInfo";
    public static final String NODE_NAME_ACTIVITY = "activity";
    public static final String NODE_NAME_APPROVAL_PROFILE = "approvalProfile";
    public static final String NODE_NAME_PRIMARY_DOC = "primaryDocs";
    public static final String NODE_NAME_PREVIEW_SUBMIT = "previewSubmit";

    //approval application delegator constants
    public static final String KEY_ROOT_NODE_GROUP = "approvalAppRoot";

    public static final String KEY_EDIT_APP_ID = "editId";
    public static final String KEY_PROCESS_TYPE = "processType";
    public static final String KEY_ACTION_TYPE = "action_type";
    public static final String KEY_INDEED_ACTION_TYPE = "indeed_action_type";
    public static final String KEY_ACTION_VALUE = "action_value";
    public static final String KEY_VALIDATION_ERRORS = "errorMsg";

    public static final String KEY_NAV_NEXT = "next";
    public static final String KEY_NAV_BACK = "back";

    public static final String KEY_ACTION_SUBMIT = "submit";
    public static final String KEY_ACTION_JUMP = "jump";
    public static final String KEY_JUMP_DEST_NODE = "destNode";
    public static final String KEY_DEST_NODE_ROUTE = "nodeRoute";

    public static final String KEY_SHOW_ERROR_SWITCH = "needShowValidationError";

    public static final String ERR_MSG_BAT_NOT_NULL = "Biological Agent/Toxin node group must not be null!";
    public static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    public static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    public static final String ERR_MSG_INVALID_ACTION = "Invalid action";

    public static final String FACILITY_ID_SELECT = "facilityIdSelect";
    public static final String ACTIVITY_ID_SELECT_DTO = "activityIdSelectDto";

    public static final String KEY_COUNTRY_OPTIONS = "countryOps";

    //RFC
    //This oldFacilityRegisterDto is the original data, which is for comparison with each DTO modification before Submit(unchangeable)
    public static final String KEY_OLD_APPROVAL_APP_DTO = "oldApprovalAppDto";
    public static final String KEY_APPROVE_NO = "approveNo";

    //view special constant
    public static final String KEY_APP_ID = "appId";
    public static final String KEY_MASKED_EDIT_APP_ID = "maskedEditId";

    public static final String KEY_ACTION_SAVE_AS_DRAFT = "draft";

    // draft
    public static final String KEY_IND_AFTER_SAVE_AS_DRAFT          = "AFTER_SAVE_AS_DRAFT";
}
