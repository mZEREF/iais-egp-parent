package sg.gov.moh.iais.egp.bsb.constant;

public class FacRegisterConstants {
    private FacRegisterConstants() {}

    public static final String MODULE_NAME_NEW             = "Facility Registration";
    public static final String MODULE_NAME_RFC             = "Rfc Facility Registration";
    public static final String MODULE_NAME_RENEWAL         = "Renewal Facility Registration";

    public static final String KEY_ROOT_NODE_GROUP         = "facRegRoot";

    public static final String NODE_NAME_BEFORE_BEGIN      = "beforeBegin";
    public static final String NODE_NAME_COMPANY_INFO      = "compInfo";
    public static final String NODE_NAME_FAC_SELECTION     = "serviceSelection";
    public static final String NODE_NAME_FAC_INFO          = "facInfo";
    public static final String NODE_NAME_FAC_BAT_INFO      = "batInfo";
    public static final String NODE_NAME_OTHER_INFO        = "otherInfo";
    public static final String NODE_NAME_PRIMARY_DOC       = "primaryDocs";
    public static final String NODE_NAME_AFC               = "afc";
    public static final String NODE_NAME_PREVIEW_SUBMIT    = "previewSubmit";
    public static final String NODE_NAME_FAC_PROFILE       = "facProfile";
    public static final String NODE_NAME_FAC_OPERATOR      = "facOperator";
    public static final String NODE_NAME_FAC_AUTH          = "facAuth";
    public static final String NODE_NAME_FAC_ADMIN_OFFICER = "facAdminOfficer";
    public static final String NODE_NAME_FAC_COMMITTEE     = "facCommittee";

    public static final String NODE_PATH_FAC_PROFILE       = NODE_NAME_FAC_INFO + "_" + NODE_NAME_FAC_PROFILE;
    public static final String NODE_PATH_FAC_OPERATOR      = NODE_NAME_FAC_INFO + "_" + NODE_NAME_FAC_OPERATOR;
    public static final String NODE_PATH_FAC_ADMIN_OFFICER = NODE_NAME_FAC_INFO + "_" + NODE_NAME_FAC_ADMIN_OFFICER;
    public static final String NODE_PATH_FAC_COMMITTEE     = NODE_NAME_FAC_INFO + "_" + NODE_NAME_FAC_COMMITTEE;
    public static final String NODE_PATH_FAC_AUTH          = NODE_NAME_FAC_INFO + "_" + NODE_NAME_FAC_AUTH;

    public static final String STEP_NAME_COMMITTEE_PREVIEW = "committeePreview";
    public static final String STEP_NAME_AUTHORISER_PREVIEW= "authoriserPreview";

    public static final String KEY_IS_CF                   = "isCertifiedFacility";

    public static final String KEY_EDIT_APP_ID             = "editId";
    public static final String KEY_ACTION_TYPE             = "action_type";
    public static final String KEY_INDEED_ACTION_TYPE      = "indeed_action_type";
    public static final String KEY_ACTION_VALUE            = "action_value";
    public static final String KEY_VALIDATION_ERRORS       = "errorMsg";
    public static final String KEY_VALID_DATA_FILE         = "VALID_FILE";
    public static final String KEY_ERROR_IN_DATA_FILE      = "DATA_HAS_ERROR";
    public static final String KEY_DATA_ERRORS             = "DATA_ERRORS";
    public static final String KEY_DATA_LIST               = "DATA_LIST";
    public static final String KEY_SOURCE_NODE_PATH        = "srcNodePath";

    public static final String KEY_NAV_NEXT                = "next";
    public static final String KEY_NAV_PREVIOUS            = "previous";

    public static final String KEY_ACTION_LOAD_DATA_FILE   = "loadDataFile";
    public static final String KEY_ACTION_DELETE_DATA_FILE = "deleteDataFile";
    public static final String KEY_ACTION_EXPAND_FILE      = "expandFile";
    public static final String KEY_ACTION_SUBMIT           = "submit";
    public static final String KEY_ACTION_JUMP             = "jump";
    public static final String KEY_ACTION_SAVE_AS_DRAFT    = "draft";
    public static final String KEY_JUMP_DEST_NODE          = "destNode";
    public static final String KEY_DEST_NODE_ROUTE         = "nodeRoute";

    public static final String KEY_SHOW_ERROR_SWITCH       = "needShowValidationError";

    public static final String KEY_SELECTED_CLASSIFICATION = "SELECTED_CLASSIFICATION";
    public static final String KEY_SELECTED_ACTIVITIES     = "SELECTED_ACTIVITIES";

    public static final String KEY_ORG_ADDRESS             = "organizationAddress";

    public static final String KEY_OPTIONS_NATIONALITY     = "nationalityOps";
    public static final String KEY_OPTION_SALUTATION       = "salutationOps";
    public static final String KEY_OPTIONS_FAC_TYPE        = "facTypeOps";
    public static final String KEY_OPTIONS_ADDRESS_TYPE    = "addressTypeOps";
    public static final String KEY_OPTIONS_AFC             = "afcOps";

    public static final String ERR_MSG_BAT_NOT_NULL        = "Biological Agent/Toxin node group must not be null!";
    public static final String ERR_MSG_NULL_GROUP          = "Node group must not be null!";
    public static final String ERR_MSG_NULL_NAME           = "Name must not be null!";
    public static final String ERR_MSG_INVALID_ACTION      = "Invalid action";

    //view application special constant
    public static final String KEY_APP_ID                 = "appId";
    public static final String KEY_MASKED_EDIT_APP_ID     = "maskedEditId";
    public static final String KEY_APPROVE_NO             = "approveNo";

    //rfc special constant
    public static final String KEY_PROCESS_TYPE                          = "processType";
    //This oldFacilityRegisterDto is the original data, which is for comparison with each DTO modification before Submit(unchangeable)
    public static final String KEY_OLD_FACILITY_REGISTER_DTO             = "oldFacilityRegisterDto";

    //renewal special node name
    public static final String KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP = "viewApprovalRoot";
    public static final String NODE_NAME_INSTRUCTION                     = "instruction";
    public static final String NODE_NAME_REVIEW                          = "review";
    //this is only use for display instruction info (put in session)
    public static final String KEY_INSTRUCTION_INFO                      = "instructionInfo";
    //renewal special action value 'reviewEdit'
    public static final String KEY_ACTION_REVIEW_EDIT                    = "reviewEdit";
}
