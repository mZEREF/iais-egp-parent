package sg.gov.moh.iais.egp.bsb.constant.module;


/**
 * @author : LiRan
 * @date : 2022/3/17
 */
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


    public static final String KEY_PROCESS_TYPE = "processType";

    public static final String KEY_NOT_EXIST_FAC_ACTIVITY_TYPE_APPROVAL_LIST = "notExistFacActivityTypeApprovalList";

    public static final String NODE_NAME_BEGIN = "begin";
    public static final String NODE_NAME_APPROVAL_SELECTION = "approvalSelection";
    public static final String NODE_NAME_COMPANY_INFO = "companyInfo";

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

    public static final String KEY_EDIT_APP_ID = "editId";
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

    public static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    public static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    public static final String ERR_MSG_INVALID_ACTION = "Invalid action";

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
