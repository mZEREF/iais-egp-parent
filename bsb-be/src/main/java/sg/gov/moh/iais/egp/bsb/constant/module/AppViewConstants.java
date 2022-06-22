package sg.gov.moh.iais.egp.bsb.constant.module;


public class AppViewConstants {
    private AppViewConstants() {}
    // mask param used by appView
    public static final String MASK_PARAM_APP_ID                            = "appId";
    public static final String MASK_PARAM_APP_VIEW_MODULE_TYPE              = "appViewModuleType";
    public static final String KEY_TASK_TYPE                                = "taskType";
    public static final String KEY_APP_VIEW_URL                             = "appViewUrl";

    public static final String KEY_APP_VIEW_URL_FACILITY                    = "/bsb-web/eservice/INTRANET/BsbBeViewFacilityRegistration";
    public static final String KEY_APP_VIEW_URL_APPROVAL_APP                = "";
    public static final String KEY_APP_VIEW_URL_FAC_CER_REG                 = "";

    public static final String MODULE_NAME                                  = "MohBeAppView";
    public static final String FUNCTION_NAME                                = "MohBeAppView";

    public static final String NODE_NAME_AFC                                = "afc";
    public static final String NODE_NAME_FAC_PROFILE                        = "facProfile";
    public static final String NODE_NAME_FAC_OPERATOR                       = "facOperator";
    public static final String NODE_NAME_FAC_ADMIN_OFFICER                  = "facAdminOfficer";
    public static final String KEY_ORG_ADDRESS                              = "organizationAddress";
    public static final String KEY_BAT_LIST                                 = "batList";
    public static final String KEY_DECLARATION_CONFIG                       = "configList";
    public static final String KEY_DECLARATION_ANSWER_MAP                   = "answerMap";
    public static final String KEY_FACILITY_REGISTRATION_DTO                = "facilityRegistrationDto";

    public static final String KEY_DOC_SETTINGS                             = "docSettings";
    public static final String KEY_OTHER_DOC_TYPES                          = "otherDocTypes";
    public static final String KEY_FILE_MAP_SAVED                           = "savedFiles";

    public static final String KEY_IS_CF                                    = "isCertifiedFacility";
    public static final String KEY_IS_UCF                                   = "isUncertifiedFacility";
    public static final String KEY_IS_RF                                    = "isRegisteredFacility";

    public static final String KEY_APPROVAL_PROFILE_LIST                    = "approvalProfileList";

    public static final String NODE_NAME_ORG_PROFILE                        = "orgProfile";
    public static final String NODE_NAME_ORG_CERTIFYING_TEAM                = "orgCerTeam";
    public static final String NODE_NAME_ORG_FAC_ADMINISTRATOR              = "orgAdmin";

    public static final String KEY_SAVED_FILES                              = "savedFiles";
    public static final String KEY_PRIMARY_DOC_DTO                          = "primaryDocDto";

    public static final String KEY_ACTION_EXPAND_FILE                       = "expandFile";
    public static final String KEY_INDEED_ACTION_TYPE                       = "indeed_action_type";
    public static final String KEY_DATA_LIST                                = "DATA_LIST";

    //deregistration view dto
    public static final String KEY_DE_REGISTRATION_FACILITY_DTO             = "deRegistrationFacilityDto";
    public static final String KEY_CANCELLATION_APPROVAL_DTO                = "cancellationApprovalDto";
    public static final String KEY_DE_REGISTRATION_AFC_DTO                  = "deRegistrationAFCDto";

    //inspection dto
    public static final String KEY_INSPECTION_FOLLOW_UP_ITEMS_DTO           = "inspectionFollowUpItemsDto";

    public static final String MODULE_VIEW_NEW_APPROVAL_APP                 = "viewNewApprovalApp";
    public static final String MODULE_VIEW_NEW_FAC_CER_REG                  = "viewNewFacCerReg";
    public static final String MODULE_VIEW_DEREGISTRATION_FACILITY          = "viewDeRegistrationFacility";
    public static final String MODULE_VIEW_CANCELLATION_APPROVAL_APP        = "viewCancellationApprovalApp";
    public static final String MODULE_VIEW_DEREGISTRATION_FAC_CER_REG       = "viewDeRegistrationFacCerReg";
    public static final String MODULE_VIEW_INSPECTION_FOLLOW_UP_ITEMS       = "viewInspectionFollowUpItems";

    public static final String KEY_VIEW_DATA_SUBMISSION                     = "dataSubInfo";

    // facility compare
    public static final String COMPARE_FAC_PROFILE                          = "compareFacProfile";
    public static final String COMPARE_FAC_OPERATOR                         = "compareFacOperator";
    public static final String COMPARE_MAIN_ADMIN                           = "compareMainAdmin";
    public static final String COMPARE_ALTER_ADMIN                          = "compareAlterAdmin";
    public static final String COMPARE_OFFICERS                             = "compareOfficers";
    public static final String COMPARE_BIO_SAFETY_COMMITTEE_IS_DIFFERENT    = "compareBioSafetyCommitteeIsDifferent";
    public static final String COMPARE_AUTHORIZER_IS_DIFFERENT              = "compareAuthorizerIsDifferent";
    public static final String COMPARE_AFC                                  = "compareAfc";
    public static final String COMPARE_BAT_MAP                              = "compareBatMap";
    public static final String COMPARE_DOC_MAP                              = "compareDocMap";
}
