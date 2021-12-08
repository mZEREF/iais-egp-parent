package sg.gov.moh.iais.egp.bsb.constant;


/**
 * @author Zhu Tangtang
 * @date 2021/7/6 15:04
 */
public final class RevocationConstants {
    private RevocationConstants() {}

    public static final String PARAM_REVOCATION_DETAIL = "revocationDetail";
    public static final String PARAM_APPLICATION_MISC_LIST = "applicationMiscList";
    public static final String PARAM_APPLICATION_SEARCH = "applicationSearch";
    public static final String PARAM_APPLICATION = "application";
    public static final String PARAM_FACILITY_SEARCH = "facilitySearch";
    public static final String PARAM_PROCESSING_HISTORY = "processingHistory";

    public static final String PARAM_APPROVAL_NO = "approvalNo";
    public static final String PARAM_APPROVAL_ID = "approvalId";
    public static final String PARAM_REASON = "reason";
    public static final String PARAM_AO_REMARKS = "AORemarks";
    public static final String PARAM_AO_DECISION = "aoDecision";
    public static final String PARAM_DO_REMARKS = "DORemarks";
    public static final String PARAM_APPROVAL_STATUS = "approvalStatus";
    public static final String PARAM_REASON_TYPE_AO = "REASON01";
    public static final String PARAM_REASON_TYPE_DO = "REASON02";
    public static final String PARAM_APP_ID = "appId";

    //facility status and approval status
    public static final String PARAM_APPLICATION_TYPE_REVOCATION = "BSBAPTY006";

    public static final String PARAM_PROCESS_TYPE_FACILITY_REGISTRATION = "PROTYPE001";
    public static final String PARAM_PROCESS_TYPE_AFC_REGISTRATION = "PROTYPE005";

    public static final String PARAM_APPLICATION_STATUS_PENDING_DO = "BSBAPST001";
    public static final String PARAM_APPLICATION_STATUS_PENDING_AO = "BSBAPST002";
    public static final String PARAM_APPLICATION_STATUS_PENDING_HM = "BSBAPST003";
    public static final String PARAM_APPLICATION_STATUS_REJECTED = "BSBAPST008";
    public static final String PARAM_APPLICATION_STATUS_APPROVED = "BSBAPST009";

    public static final String MODULE_REVOCATION = "Revocation";
    public static final String FUNCTION_REVOCATION = "Revocation";

    public static final String KEY_APPLICATION_PAGE_INFO = "pageInfo";
    public static final String KEY_APPLICATION_DATA_LIST = "dataList";
    public static final String KEY_ACTION_VALUE = "action_value";
    public static final String KEY_ACTION_ADDT = "action_additional";

    public static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    public static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";
    public static final String AUDIT_DOC_DTO = "auditDocDto";
    public static final String KEY_CAN_UPLOAD = "canUpload";

    public static final String FLAG = "flag";
    public static final String BACK = "back";
    public static final String FROM = "from";
    public static final String FAC = "fac";
    public static final String APP = "app";
    public static final String REVOCATION_FACILITY = "revocationFacility";
    public static final String REVOCATION_TASK_LIST = "revocationTaskList";
    public static final String PARAM_REVOKE_DTO = "revokeDto";

    public static final String KEY_NON_OBJECT_ERROR = "please ensure your object has value";
}
