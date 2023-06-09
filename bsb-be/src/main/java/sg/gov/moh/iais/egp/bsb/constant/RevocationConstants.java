package sg.gov.moh.iais.egp.bsb.constant;


/**
 * @author Zhu Tangtang
 * @date 2021/7/6 15:04
 */
public final class RevocationConstants {
    private RevocationConstants() {}

    public static final String PARAM_REVOCATION_DETAIL = "revocationDetail";
    public static final String PARAM_APPLICATION_SEARCH = "applicationSearch";
    public static final String PARAM_FACILITY_SEARCH = "facilitySearch";

    public static final String PARAM_APPROVAL_NO = "approvalNo";
    public static final String PARAM_APPROVAL_ID = "approvalId";
    public static final String PARAM_REASON = "reason";
    public static final String PARAM_AO_REMARKS = "AORemarks";
    public static final String PARAM_AO_DECISION = "aoDecision";
    public static final String PARAM_DO_REMARKS = "DORemarks";

    //facility status and approval status
    public static final String PARAM_APPLICATION_TYPE_REVOCATION = "BSBAPTY006";

    public static final String KEY_APPLICATION_PAGE_INFO = "pageInfo";
    public static final String KEY_APPLICATION_DATA_LIST = "dataList";
    public static final String KEY_ACTION_VALUE = "action_value";
    public static final String KEY_ACTION_ADDT = "action_additional";

    public static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    public static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";
    public static final String KEY_CAN_UPLOAD = "canUpload";

    public static final String BACK_URL = "backUrl";
    public static final String FROM = "from";
    public static final String FAC = "fac";
    public static final String APP = "app";
    public static final String PARAM_REVOKE_DTO = "revokeDto";

    public static final String TASK_LIST_URL = "/bsb-web/eservice/INTRANET/MohBsbTaskList";
    public static final String APPROVAL_LIST_URL = "/bsb-web/eservice/INTRANET/FacilityList";

    public static final String KEY_NON_OBJECT_ERROR = "please ensure your object has value";
}
