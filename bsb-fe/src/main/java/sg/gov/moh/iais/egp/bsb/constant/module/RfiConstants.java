package sg.gov.moh.iais.egp.bsb.constant.module;

public class RfiConstants {
    private RfiConstants() {}

    // * module name from be api RfiConstants *
    public static final String MODULE_NAME_FACILITY_REGISTRATION           = "Facility Registration";
    public static final String MODULE_NAME_INSPECTION_SELF_ASSESSMENT      = "Inspection Self-Assessment";
    public static final String MODULE_NAME_INSPECTION_REPORT               = "Inspection Report";
    public static final String MODULE_NAME_INSPECTION_NC                   = "Inspection Non-Compliance";
    public static final String MODULE_NAME_INSPECTION_FOLLOW_UP            = "Inspection Follow-Up";

    public static final String KEY_APP_ID                                  = "appId";
    public static final String KEY_RFI_APP_ID                              = "rfiAppId";

    public static final String KEY_RFI_DISPLAY_DTO                         = "rfiDisplayDto";
    public static final String KEY_APPLICATION_RFI_INDICATOR_DTO_LIST      = "applicationRfiIndicatorDtoList";

    public static final String KEY_CRUD_ACTION_TYPE                        = "crud_action_type";
    public static final String KEY_ACTION_TYPE_PRE_ACKNOWLEDGE             = "preAcknowledge";
    public static final String KEY_ACTION_TYPE_PREPARE_RFI                 = "prepareRfi";
}
