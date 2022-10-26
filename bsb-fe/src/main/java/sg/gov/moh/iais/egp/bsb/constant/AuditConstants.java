package sg.gov.moh.iais.egp.bsb.constant;

/**
 * @author Zhu Tangtang
 * @date 2021/7/6 15:04
 */

public class AuditConstants {
    private AuditConstants() {}

    public static final String PARAM_AUDIT_SEARCH = "auditSearch";
    public static final String PARAM_AUDIT_TYPE = "auditType";
    public static final String PARAM_AUDIT_DATE = "auditDate";

    public static final String PARAM_REMARKS = "remark";
    public static final String PARAM_REASON_FOR_CHANGE = "changeReason";

    public static final String PARAM_FACILITY_NAME = "facilityName";
    public static final String PARAM_FACILITY_CLASSIFICATION = "facilityClassification";
    public static final String PARAM_FACILITY_TYPE = "facilityType";

    public static final String KEY_AUDIT_PAGE_INFO = "pageInfo";
    public static final String KEY_AUDIT_DATA_LIST = "dataList";
    public static final String KEY_ACTION_VALUE = "action_value";
    public static final String KEY_ACTION_ADDT = "action_additional";

    public static final String FACILITY_AUDIT = "facilityAudit";
    public static final String SELF_AUDIT_DATA = "selfAudit";
    public static final String AUDIT_ID = "auditId";
    public static final String LAST_AUDIT_DATE = "lastAuditDt";
    public static final String PARAM_SCENARIO_CATEGORY = "scenarioCategory";

    public static final String PARAM_YEAR = "year";

    public static final String PARAM_AUDIT_DTO = "auditDto";
    public static final String PARAM_CHANGE_DT = "changeDt";
    public static final String PARAM_SPECIFY_DT = "specifyDt";
    public static final String PARAM_MODULE_TYPE = "moduleType";

    public static final String PARAM_AUDIT_STATUS_PENDING_TASK_ASSIGNMENT = "AUDITST001";
    public static final String PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT = "AUDITST002";
    public static final String PARAM_AUDIT_STATUS_COMPLETED = "AUDITST003";
    public static final String PARAM_AUDIT_STATUS_PENDING_DO = "AUDITST004";
    public static final String PARAM_AUDIT_STATUS_PENDING_AO = "AUDITST005";
    public static final String PARAM_AUDIT_STATUS_SUSPENDED = "AUDITST006";
    public static final String PARAM_AUDIT_STATUS_CANCELLED = "AUDITST007";
}
