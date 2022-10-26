package sg.gov.moh.iais.egp.bsb.constant.module;

public class RfiConstants {
    private RfiConstants() {}
    // mask appId key
    public static final String KEY_RFI_APP_ID                               = "rfiAppId";
    public static final String KEY_MASKED_RFT_DATA_ID                       = "maskedRfiDataId";
    public static final String KEY_RFT_DATA_ID                              = "rfiDataId";
    public static final String KEY_RFT_TYPE                                 = "rfiType";

    public static final String KEY_PAGE_APP_EDIT_SELECT_DTO                 = "pageAppEditSelectDto";

    public static final String KEY_RFI_DISPLAY_DTO                          = "rfiDisplayDto";

    public static final String KEY_CRUD_ACTION_TYPE                         = "crud_action_type";
    public static final String KEY_ACTION_TYPE_PRE_ACKNOWLEDGE              = "preAcknowledge";
    public static final String KEY_ACTION_TYPE_PREPARE_RFI                  = "prepareRfi";

    // judge this process is or is not rfi
    public static final String KEY_CONFIRM_RFI                              = "confirmRfi";
    public static final String KEY_CONFIRM_RFI_Y                            = "Y";
}
