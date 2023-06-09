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

    public static final String KEY_MANUAL_AUDIT = "saveAuditDto";
    public static final String KEY_CANCEL_AUDIT = "cancelAuditDto";

    public static final String PARAM_DO_REMARKS = "doRemarks";
    public static final String PARAM_DO_REASON = "doReason";
    public static final String PARAM_DO_DECISION = "doDecision";
    public static final String PARAM_AO_REMARKS = "aoRemarks";
    public static final String PARAM_AO_REASON = "aoReason";
    public static final String PARAM_AO_DECISION = "aoDecision";
    public static final String PARAM_CANCEL_REASON = "cancelReason";

    public static final String PARAM_DO_AUDIT_DT = "doProcessAuditDt";
    public static final String PARAM_AO_AUDIT_DT = "aoProcessAuditDt";
    public static final String PARAM_MODULE_TYPE = "moduleType";

    public static final String BACK_URL = "backUrl";
    public static final String BACK_URL_TASK_LIST = "/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList";
    public static final String BACK_URL_CANCEL_LIST = "/bsb-web/eservicecontinue/INTRANET/AuditCancellationList";
}
