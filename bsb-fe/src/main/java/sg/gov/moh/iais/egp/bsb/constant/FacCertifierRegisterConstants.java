package sg.gov.moh.iais.egp.bsb.constant;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
public class FacCertifierRegisterConstants {
    private FacCertifierRegisterConstants() {}

    public static final String NODE_NAME_COMPANY_INFO = "compInfo";
    public static final String NODE_NAME_ORGANISATION_INFO = "orgInfo";
    public static final String NODE_NAME_FAC_PRIMARY_DOCUMENT = "primaryDoc";
    public static final String NODE_NAME_CER_PREVIEW_SUBMIT = "previewSubmit";
    public static final String NODE_NAME_COMPANY_PROFILE = "companyProfile";
    public static final String NODE_NAME_COMPANY_CERTIFYING_TEAM = "companyCerTeam";
    public static final String NODE_NAME_COMPANY_FAC_ADMINISTRATOR = "companyAdmin";

    public static final String KEY_ROOT_NODE_GROUP = "facCertifierRegRoot";

    public static final String KEY_EDIT_APP_ID = "editId";
    public static final String KEY_ACTION_TYPE = "action_type";
    public static final String KEY_INDEED_ACTION_TYPE = "indeed_action_type";
    public static final String KEY_ACTION_VALUE = "action_value";
    public static final String KEY_VALIDATION_ERRORS = "errorMsg";
    public static final String TEXT_VALUE_PLEASE_SELECT = "Please Select";
    public static final String TEXT_VALUE_SINGAPORE = "Singapore";
    public static final String TEXT_VALUE_CHINA = "China";
    public static final String TEXT_VALUE_MALAYSIA = "Malaysia";

    public static final String KEY_NAV_NEXT = "next";
    public static final String KEY_NAV_BACK = "back";
    public static final String KEY_NAV_SAVE_DRAFT = "draft";

    public static final String KEY_NATIONALITY_OPTIONS = "nationalityOps";
    public static final String KEY_COUNTRY_OPTIONS = "countryOps";
    public static final String KEY_POSITION_OPTIONS = "positionOps";

    public static final String KEY_ACTION_SUBMIT = "submit";
    public static final String KEY_ACTION_JUMP = "jump";
    public static final String KEY_JUMP_DEST_NODE = "destNode";
    public static final String KEY_DEST_NODE_ROUTE = "nodeRoute";

    public static final String KEY_SHOW_ERROR_SWITCH = "needShowValidationError";

    public static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    public static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    public static final String ERR_MSG_INVALID_ACTION = "Invalid action";

    //Rfc special Constant
    public static final String KEY_PROCESS_TYPE = "processType";
    //This oldFacilityCertifierRegisterDto is the original data, which is for comparison with each DTO modification before Submit(unchangeable)
    public static final String KEY_OLD_FAC_CER_REG_DTO = "oldFacilityCertifierRegisterDto";

    //renewal special node name
    public static final String KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP = "viewApprovalRoot";
    public static final String NODE_NAME_INSTRUCTION = "instruction";
    public static final String NODE_NAME_REVIEW = "review";
    //this is only use for display instruction info (put in session)
    public static final String KEY_INSTRUCTION_INFO = "instructionInfo";
    //renewal special action value 'reviewEdit'
    public static final String KEY_ACTION_REVIEW_EDIT = "reviewEdit";
}
