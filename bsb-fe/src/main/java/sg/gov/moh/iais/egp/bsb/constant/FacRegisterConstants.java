package sg.gov.moh.iais.egp.bsb.constant;

public class FacRegisterConstants {
    private FacRegisterConstants() {}

    public static final String DECLARATION_TYPE            = "Facility Registration";

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

    public static final String KEY_SAMPLE_COMMITTEE        = "committeeSampleFile";
    public static final String KEY_SAMPLE_AUTHORISER       = "authoriserSampleFile";
    public static final String STEP_NAME_COMMITTEE_PREVIEW = "committeePreview";
    public static final String STEP_NAME_AUTHORISER_PREVIEW= "authoriserPreview";

    public static final String KEY_IS_CF                   = "isCertifiedFacility";
    public static final String KEY_IS_UCF                  = "isUncertifiedFacility";
    public static final String KEY_IS_RF                   = "isRegisteredFacility";
    public static final String KEY_IS_FIFTH_RF             = "isSPFifthRegisteredFacility";
    public static final String KEY_IS_PV_RF                = "isPolioVirusRegisteredFacility";
    public static final String KEY_IS_RFC                  = "isRfc";
    public static final String KEY_IS_RENEW                = "isRenew";

    public static final String KEY_ALLOW_SAVE_DRAFT        = "isAllowToSaveDraft";
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

    public static final String KEY_SELECTED_CLASSIFICATION = "SELECTED_CLASSIFICATION";
    public static final String KEY_SELECTED_ACTIVITIES     = "SELECTED_ACTIVITIES";

    public static final String KEY_SCHEDULE_BAT_MAP        = "scheduleBatMap";
    public static final String KEY_SCHEDULE_BAT_MAP_JSON   = "scheduleBatMapJson";
    public static final String KEY_BAT_LIST                = "batList";
    public static final String KEY_BAT_CONTAINS_IMPORT     = "batContainsImport";

    public static final String KEY_FIND_GAZETTE_ERROR      = "findGazetteError";
    public static final String KEY_DOC_SETTINGS            = "docSettings";
    public static final String KEY_OTHER_DOC_TYPES         = "otherDocTypes";
    public static final String KEY_FILE_MAP_NEW            = "newFiles";
    public static final String KEY_FILE_MAP_SAVED          = "savedFiles";
    public static final String KEY_DOC_TYPES_JSON          = "docTypeOpsJson";

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

    public static final String SOURCE_FACILITY_DETAILS = "sourceFacDetails";

    public static final String KEY_APP_NO                  = "appNo";
    public static final String KEY_APP_DT                  = "appDt";

    public static final String KEY_SHOW_ERROR_SWITCH       = "needShowValidationError";

    public static final String KEY_OPTIONS_NATIONALITY     = "nationalityOps";
    public static final String KEY_OPTIONS_COUNTRY         = "countryOps";
    public static final String KEY_OPTION_SALUTATION       = "salutationOps";
    public static final String KEY_OPTIONS_FAC_TYPE        = "facTypeOps";
    public static final String KEY_OPTIONS_ADDRESS_TYPE    = "addressTypeOps";
    public static final String KEY_OPTIONS_PIM_RISK_LEVEL  = "opvSabinPIMRiskLevelOps";
    public static final String KEY_OPTIONS_AFC             = "afcOps";
    public static final String KEY_OPTIONS_SCHEDULE        = "scheduleOps";
    public static final String KEY_SCHEDULE_FIRST_OPTION   = "firstScheduleOp";
    public static final String KEY_OPTIONS_DOC_TYPES       = "docTypeOps";
    public static final String KEY_LAST_TWO_ROUND_AFC_SET  = "lastTwoAfc";
    public static final String KEY_LAST_TWO_ROUND_AFC_JSON  = "lastTwoAfcJson";

    public static final String KEY_DECLARATION_CONFIG      = "configList";
    public static final String KEY_DECLARATION_ANSWER_MAP  = "answerMap";
    public static final String KEY_DECLARATION_ERROR_MAP   = "answerErrorMap";

    public static final String KEY_PRINT_MASK_PARAM        = "printFacReg";
    public static final String KEY_PRINT_MASKED_ID         = "printFacRegId";

    public static final String ERR_MSG_BAT_NOT_NULL        = "Biological Agent/Toxin node group must not be null!";
    public static final String ERR_MSG_NULL_GROUP          = "Node group must not be null!";
    public static final String ERR_MSG_NULL_NAME           = "Name must not be null!";
    public static final String ERR_MSG_INVALID_ACTION      = "Invalid action";
    public static final String ERR_MSG_INVALID_CLASSIFICATION   = "Invalid facility classification";
    public static final String ERR_MSG_INVALID_ACTIVITY    = "Invalid facility activity type";
    public static final String ERR_MSG_RF_INVALID_ACTIVITY = "Registered Facility is only allowed to select one activity";

    public static final String ELIGIBLE_DRAFT_REGISTER_DTO = "eligibleDraftRegisterDto";
    public static final String ACTION_LOAD_DRAFT = "action_load_draft";
    public static final String HAVE_SUITABLE_DRAFT_DATA = "haveSuitableDraftData";

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
    public static final String KEY_EDITABLE_FIELD_SET                    = "editableFieldSet";
    public static final String KEY_FAC_ID                                = "facId";
    public static final String KEY_CURRENT_FAC_ID                        = "currentFacilityId";


    public static final String SEPARATOR  = "--v--";

    public static final String KEY_ALL_FIELD = "allField";
}
