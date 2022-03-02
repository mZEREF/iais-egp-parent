package sg.gov.moh.iais.egp.bsb.constant.module;


public class AppViewConstants {
    private AppViewConstants() {}

    // mask param used by task list
    public static final String MASK_PARAM_APP_ID                            = "appId";
    public static final String MASK_PARAM_APP_VIEW_MODULE_TYPE              = "appViewModuleType";

    public static final String MODULE_NAME                                  = "MohBeAppView";
    public static final String FUNCTION_NAME                                = "MohBeAppView";

    public static final String NODE_NAME_FAC_PROFILE                        = "facProfile";
    public static final String NODE_NAME_FAC_OPERATOR                       = "facOperator";
    public static final String NODE_NAME_FAC_AUTH                           = "facAuth";
    public static final String NODE_NAME_FAC_ADMIN                          = "facAdmin";
    public static final String NODE_NAME_FAC_OFFICER                        = "facOfficer";
    public static final String NODE_NAME_FAC_COMMITTEE                      = "facCommittee";
    public static final String KEY_BAT_LIST                                 = "batList";

    public static final String KEY_APPROVAL_PROFILE_LIST                    = "approvalProfileList";

    public static final String NODE_NAME_ORG_PROFILE                        = "orgProfile";
    public static final String NODE_NAME_ORG_CERTIFYING_TEAM                = "orgCerTeam";
    public static final String NODE_NAME_ORG_FAC_ADMINISTRATOR              = "orgAdmin";

    public static final String KEY_DOC_SETTINGS                             = "docSettings";
    public static final String KEY_SAVED_FILES                              = "savedFiles";
    public static final String KEY_PRIMARY_DOC_DTO                          = "primaryDocDto";

    //deregistration view dto
    public static final String KEY_DE_REGISTRATION_FACILITY_DTO             = "deRegistrationFacilityDto";
    public static final String KEY_CANCELLATION_APPROVAL_DTO                = "cancellationApprovalDto";
    public static final String KEY_DE_REGISTRATION_AFC_DTO                  = "deRegistrationAFCDto";

    //inspection dto
    public static final String KEY_INSPECTION_FOLLOW_UP_ITEMS_DTO           = "inspectionFollowUpItemsDto";

    public static final String MODULE_VIEW_NEW_FACILITY                     = "viewNewFacility";
    public static final String MODULE_VIEW_NEW_APPROVAL_APP                 = "viewNewApprovalApp";
    public static final String MODULE_VIEW_NEW_FAC_CER_REG                  = "viewNewFacCerReg";
    public static final String MODULE_VIEW_DEREGISTRATION_FACILITY          = "viewDeRegistrationFacility";
    public static final String MODULE_VIEW_CANCELLATION_APPROVAL_APP        = "viewCancellationApprovalApp";
    public static final String MODULE_VIEW_DEREGISTRATION_FAC_CER_REG       = "viewDeRegistrationFacCerReg";
    public static final String MODULE_VIEW_INSPECTION_FOLLOW_UP_ITEMS       = "viewInspectionFollowUpItems";

    public static final String KEY_VIEW_DATA_SUBMISSION                     = "dataSubInfo";
}
