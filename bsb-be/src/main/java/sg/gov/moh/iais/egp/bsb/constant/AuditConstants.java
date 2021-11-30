package sg.gov.moh.iais.egp.bsb.constant;


/**
 * @author Zhu Tangtang
 * @date 2021/7/6 15:04
 */
public final class AuditConstants {
    private AuditConstants() {}

    public static final String PARAM_AUDIT_SEARCH = "auditSearch";
    public static final String PARAM_AUDIT_TYPE = "auditType";
    public static final String PARAM_AUDIT_DATE = "auditDate";

    public static final String PARAM_CREATE_AUDIT = "createAudit";
    public static final String PARAM_CANCEL_AUDIT = "cancelAudit";

    public static final String PARAM_REMARKS = "remark";
    public static final String PARAM_REASON = "reason";
    public static final String PARAM_DECISION = "decision";
    public static final String PARAM_REASON_FOR_CHANGE = "reasonForChange";

    public static final String PARAM_FACILITY_NAME = "facilityName";
    public static final String PARAM_FACILITY_CLASSIFICATION = "facilityClassification";
    public static final String PARAM_FACILITY_TYPE = "facilityType";

    public static final String MODULE_AUDIT = "Audit";
    public static final String FUNCTION_AUDIT = "Audit";

    public static final String KEY_AUDIT_PAGE_INFO = "pageInfo";
    public static final String KEY_AUDIT_DATA_LIST = "dataList";
    public static final String KEY_OFFICER_PROCESS_DATA = "processData";
    public static final String KEY_ACTION_VALUE = "action_value";
    public static final String KEY_ACTION_ADDT = "action_additional";

    public static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    public static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";

    public static final String FACILITY_ID = "facId";
    public static final String FACILITY_LIST = "facilityList";
    public static final String AUDIT_LIST = "auditList";

    public static final String FACILITY = "facility";
    public static final String FACILITY_AUDIT = "facilityAudit";
    public static final String FACILITY_AUDIT_APP = "facilityAuditAPP";
    public static final String AUDIT_ID = "auditId";
    public static final String LAST_AUDIT_DATE = "lastAuditDt";
    public static final String PARAM_SCENARIO_CATEGORY = "scenarioCategory";

    public static final String AUDIT_APP_ID = "auditAppId";
    public static final String AUDIT_DOC_DTO = "auditDocDto";
    public static final String AUDIT_OUTCOME = "auditOutcome";
    public static final String FINAL_REMARK = "finalRemark";
    public static final String AO_REMARKS = "aoRemark";
    public static final String PARAM_HISTORY = "history";

    public static final String PARAM_YEAR = "year";

    public static final String PARAM_AUDIT_STATUS_PENDING_TASK_ASSIGNMENT = "AUDITST001";
    public static final String PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT = "AUDITST002";
    public static final String PARAM_AUDIT_STATUS_COMPLETED = "AUDITST003";
    public static final String PARAM_AUDIT_STATUS_PENDING_DO = "AUDITST004";
    public static final String PARAM_AUDIT_STATUS_PENDING_AO = "AUDITST005";
    public static final String PARAM_AUDIT_STATUS_SUSPENDED = "AUDITST006";
    public static final String PARAM_AUDIT_STATUS_CANCELLED = "AUDITST007";

    public static final String SEPARATOR                   = "--v--";
    public static final String KEY_SECTION_IDXES          = "sectionIdx";

    public static final String KEY_APP_ID = "appId";
    public static final String KEY_TASK_ID = "taskId";

    public static final String BACK_URL = "backUrl";
    public static final String BACK_URL_TASK_LIST = "/bsb-be/eservicecontinue/INTRANET/MohBsbTaskList";
    public static final String BACK_URL_CANCEL_LIST = "/bsb-be/eservicecontinue/INTRANET/AuditCancellationList";
}
