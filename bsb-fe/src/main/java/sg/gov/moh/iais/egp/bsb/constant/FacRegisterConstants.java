package sg.gov.moh.iais.egp.bsb.constant;

public class FacRegisterConstants {
    private FacRegisterConstants() {}

    public static final String NODE_NAME_COMPANY_INFO = "compInfo";
    public static final String NODE_NAME_FAC_SELECTION = "serviceSelection";
    public static final String NODE_NAME_FAC_INFO = "facInfo";
    public static final String NODE_NAME_FAC_BAT_INFO = "batInfo";
    public static final String NODE_NAME_OTHER_INFO = "otherInfo";
    public static final String NODE_NAME_PRIMARY_DOC = "primaryDocs";
    public static final String NODE_NAME_PREVIEW_SUBMIT = "previewSubmit";
    public static final String NODE_NAME_FAC_PROFILE = "facProfile";
    public static final String NODE_NAME_FAC_OPERATOR = "facOperator";
    public static final String NODE_NAME_FAC_AUTH = "facAuth";
    public static final String NODE_NAME_FAC_ADMIN = "facAdmin";
    public static final String NODE_NAME_FAC_OFFICER = "facOfficer";
    public static final String NODE_NAME_FAC_COMMITTEE = "facCommittee";

    public static final String KEY_ROOT_NODE_GROUP = "facRegRoot";

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

    public static final String KEY_NATIONALITY_OPTIONS = "nationalityOps";

    public static final String ERR_MSG_BAT_NOT_NULL = "Biological Agent/Toxin node group must not be null!";
    public static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    public static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    public static final String ERR_MSG_INVALID_ACTION = "Invalid action";

    //view application special constant
    public static final String KEY_APP_ID = "appId";
    public static final String KEY_MASKED_EDIT_APP_ID = "maskedEditId";
    public static final String KEY_APPROVE_NO = "approveNo";

    //rfc special constant
    public static final String KEY_PROCESS_TYPE = "processType";
    //This oldFacilityRegisterDto is the original data, which is for comparison with each DTO modification before Submit(unchangeable)
    public static final String KEY_OLD_FACILITY_REGISTER_DTO = "oldFacilityRegisterDto";
}
