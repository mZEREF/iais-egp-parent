package sg.gov.moh.iais.egp.bsb.constant.module;


public class AppViewConstants {
    private AppViewConstants() {}
    // mask param used by appView
    public static final String MASK_PARAM_APP_ID                            = "appId";
    public static final String MASK_PARAM_APP_VIEW_MODULE_TYPE              = "appViewModuleType";
    public static final String KEY_APP_VIEW_URL                             = "appViewUrl";

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

    public static final String KEY_IS_CF                   = "isCertifiedFacility";
    public static final String KEY_IS_UCF                  = "isUncertifiedFacility";
    public static final String KEY_IS_RF                   = "isRegisteredFacility";
    public static final String KEY_IS_FIFTH_RF             = "isSPFifthRegisteredFacility";
    public static final String KEY_IS_PV_RF                = "isPolioVirusRegisteredFacility";
    public static final String KEY_IS_RFC                  = "isRfc";

    public static final String KEY_ACTION_EXPAND_FILE                       = "expandFile";
    public static final String KEY_INDEED_ACTION_TYPE                       = "indeed_action_type";
    public static final String KEY_DATA_LIST                                = "DATA_LIST";

    public static final String KEY_SELECTED_CLASSIFICATION       = "SELECTED_CLASSIFICATION";
    public static final String KEY_SELECTED_ACTIVITIES           = "SELECTED_ACTIVITIES";

    public static final String MODULE_VIEW_NEW_FAC_CER_REG                  = "viewNewFacCerReg";
    public static final String MODULE_VIEW_DEREGISTRATION_FACILITY          = "viewDeRegistrationFacility";
    public static final String MODULE_VIEW_CANCELLATION_APPROVAL_APP        = "viewCancellationApprovalApp";
    public static final String MODULE_VIEW_DEREGISTRATION_FAC_CER_REG       = "viewDeRegistrationFacCerReg";
    public static final String MODULE_VIEW_INSPECTION_FOLLOW_UP_ITEMS       = "viewInspectionFollowUpItems";

    // facility compare
    public static final String COMPARE_FAC_PROFILE_LIST                     = "compareFacProfiles";
    public static final String COMPARE_FAC_OPERATOR                         = "compareFacOperator";
    public static final String COMPARE_MAIN_ADMIN                           = "compareMainAdmin";
    public static final String COMPARE_ALTER_ADMIN                          = "compareAlterAdmin";
    public static final String COMPARE_OFFICERS                             = "compareOfficers";
    public static final String COMPARE_BIO_SAFETY_COMMITTEE_IS_DIFFERENT    = "compareBioSafetyCommitteeIsDifferent";
    public static final String COMPARE_AUTHORIZER_IS_DIFFERENT              = "compareAuthorizerIsDifferent";
    public static final String COMPARE_AFC                                  = "compareAfc";
    public static final String COMPARE_BAT_MAP                              = "compareBatMap";
    public static final String COMPARE_DOC_MAP                              = "compareDocMap";

    // approval bat and activity
    public static final String KEY_PROCESS_TYPE                             = "processType";
    public static final String KEY_FAC_PROFILE_DTO                          = "facProfileDto";
    public static final String KEY_BAT_INFO                                 = "batInfo";
    public static final String KEY_FAC_AUTHORISED_PERSON_LIST_WANTED        = "facAuthListWanted";

    // approval bat and activity compare
    public static final String COMPARE_BAT_INFO_LIST                        = "compareBatInfos";
    public static final String COMPARE_SATH_DTO                             = "compareSathDto";
    public static final String COMPARE_FACILITY_AUTHORISER_DTO_LIST         = "compareFacAuthorisers";
    public static final String COMPARE_WORK_ACTIVITY_LIST                   = "compareWorkActivitys";
}
