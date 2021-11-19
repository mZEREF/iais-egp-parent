package sg.gov.moh.iais.egp.bsb.constant;

/**
 * AUTHOR: YiMing
 * DATE:2021/9/14 18:01
 * DESCRIPTION: TODO
 **/
public class EmailConstants {
    private EmailConstants() {}

    //delivery mode
    public static final String TEMPLETE_DELIVERY_MODE_EMAIL = "DEMD001";
    public static final String TEMPLETE_DELIVERY_MODE_SMS = "DEMD002";
    public static final String TEMPLETE_DELIVERY_MODE_MSG = "DEMD003";
    public static final String TEMPLETE_DELIVERY_MODE_NA = "DEMD004";

    //Notification Template ID
    public static final String MSG_TEMPLATE_NEW_APP_REJECT = "0E4B4155-942F-434B-A111-FC6C8843A36A";
    public static final String MSG_TEMPLATE_NEW_APP_APPROVAL = "85665530-65AB-4610-AB97-C6F15F841685";
    public static final String MSG_TEMPLATE_NEW_APP_APPLICANT_SUBMIT = "A05502B1-FF33-476B-A30C-C60B0DF2554F";
    public static final String MSG_TEMPLATE_NEW_APP_NO_SELF_ASSESSMENT = "B0B2FE52-B789-4439-AE90-A624646F0A57";
    public static final String MSG_TEMPLATE_NEW_APP_REMIND_PNEF_INVENTORY = "C8A49886-40CE-460E-A9EC-65C397FE8E7F";
    public static final String MSG_TEMPLATE_NEW_APP_REGISTER_FACILITY_SUCCESSFUL = "989CBC7A-D4AA-4C89-8777-B5F45C928D9D";
    public static final String MSG_TEMPLATE_NEW_APP_REQUEST_FOR_INFO = "680BADFA-162D-4A6F-8F35-9D12157D0349";
    public static final String MSG_TEMPLATE_REVOCATION_AO_APPROVED = "219D5DED-48B7-4ED5-9D87-AB6D59A67B1F";
    public static final String MSG_TEMPLATE_REVOCATION_USER_APPROVED = "BF5A1991-69E9-49CC-AE8A-AC7E9DAECF75";

    //Notification Msg ID
    public static final String STATUS_NEW_APP_SUBMITTED = "BISNEW001";
    public static final String STATUS_NEW_APP_NON_SELF_ASSESSMENT = "BISNEW002";
    public static final String STATUS_NEW_APP_REJECT   = "BISNEW004";
    public static final String STATUS_NEW_APP_APPROVED = "BISNEW003";
    public static final String STATUS_NEW_APP_REQUEST_FOR_INFO = "BISNEW005";
    public static final String STATUS_NEW_APP_REMIND_PNEF_INVENTORY = "BISNEW006";
    public static final String STATUS_NEW_APP_REGISTERED_FACILITY_SUCCESSFUL = "BISNEW008";
    public static final String STATUS_REVOCATION_APPROVAL_AO       = "BISEmail001";
    public static final String STATUS_REVOCATION_APPROVAL_USER     = "BISEmail002";

    //Letter Template ID
    public static final String MSG_TEMPLATE_APPROVAL_FOR_UNCERTIFIED_FACILITY = "71A3B01F-DE69-4A7B-99A0-B58213466515";
    public static final String MSG_TEMPLATE_APPROVAL_LETTER_FOR_CERTIFIED_FACILITY = "CECEDC58-6341-4B06-8348-5177B8E4EA23";
    public static final String MSG_TEMPLATE_APPROVAL_LETTER_FOR_LARGE_SCALE_PRODUCTION_FIRST_THIRD = "15806B26-F535-4289-A302-44048C137ED2";
    public static final String MSG_TEMPLATE_APPROVAL_LETTER_OF_POSSESSION_FOR_FIRST_TIME_USE_OF_AGENT = "AC706878-115E-4C9A-947A-4AA7499A841E";
    public static final String MSG_TEMPLATE_APPROVAL_LETTER_FOR_SPECIAL_APPROVAL = "C8A559A1-F4B0-4CE4-8C6C-F8389E01DDF3";
    public static final String MSG_TEMPLATE_APPROVAL_LETTER_FOR_MOH_APPROVED_FACILITY_CERTIFIER = "7E99B5E1-F09F-4580-861E-0B50EA141089";
    public static final String MSG_TEMPLATE_RENEWAL_OF_REGISTRATION_AS_A_CERTIFIED_FACILITY = "540AE4CF-9B0A-4370-AD42-EAB80F162C35";
    public static final String MSG_TEMPLATE_NOTICE_OF_REVOCATION_FOR_APPROVAL = "7965B021-E60E-4E1B-B478-9F28361CA958";
    public static final String MSG_TEMPLATE_NOTICE_OF_REVOCATION_FOR_REGISTERED_FACILITY = "9EA3812F-DB00-4D5D-8C77-17B0C6F1DA82";
    public static final String MSG_TEMPLATE_NOTICE_OF_REVOCATION_FOR_CERTIFIED_FACILITY = "CA386361-8A26-4850-B522-7967204D4FC7";

    //Letter Type
    public static final String MSG_LETTER_TYPE_UNCERTIFIED_FACILITY                            = "letter1";
    public static final String MSG_LETTER_TYPE_CERTIFIED_FACILITY                              = "letter2";
    public static final String MSG_LETTER_TYPE_LARGE_SCALE_PRODUCTION                          = "letter3";
    public static final String MSG_LETTER_TYPE_POSSESSION_FOR_FIRST_TIME_USE_OF_AGENT          = "letter4";
    public static final String MSG_LETTER_TYPE_SPECIAL_APPROVAL                                = "letter5";
    public static final String MSG_LETTER_TYPE_MOH_APPROVED_FACILITY_CERTIFIER                 = "letter6";
    public static final String MSG_LETTER_TYPE_RENEWAL_OF_REGISTRATION_AS_A_CERTIFIED_FACILITY = "letter7";
    public static final String MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_APPROVAL               = "letter8";
    public static final String MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_REGISTERED_FACILITY    = "letter9";
    public static final String MSG_LETTER_TYPE_NOTICE_OF_REVOCATION_FOR_CERTIFIED_FACILITY     = "letter10";
}
