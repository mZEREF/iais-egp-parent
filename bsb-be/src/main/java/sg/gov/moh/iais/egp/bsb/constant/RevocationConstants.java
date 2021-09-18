package sg.gov.moh.iais.egp.bsb.constant;

import lombok.NoArgsConstructor;

/**
 * @author Zhu Tangtang
 * @date 2021/7/6 15:04
 */

@NoArgsConstructor
public final class RevocationConstants {

    public static final String PARAM_REVOCATION_DETAIL = "revocationDetail";
    public static final String PARAM_APPLICATION_MISC_LIST = "applicationMiscList";
    public static final String PARAM_APPLICATION_SEARCH_RESULT = "applicationSearchResult";
    public static final String PARAM_APPLICATION_SEARCH = "applicationSearch";
    public static final String PARAM_PROCESSING_HISTORY = "processingHistory";

    public static final String PARAM_AUDIT_SEARCH = "auditSearch";
    public static final String PARAM_AUDIT_TYPE = "auditType";

    public static final String PARAM_APPROVAL_NO = "approvalNo";
    public static final String PARAM_FACILITY_NAME = "facilityName";
    public static final String PARAM_FACILITY_ADDRESS = "facilityAddress";
    public static final String PARAM_FACILITY_CLASSIFICATION = "facilityClassification";
    public static final String PARAM_FACILITY_TYPE = "facilityType";
    public static final String PARAM_REASON = "reason";
    public static final String PARAM_AOREMARKS = "AORemarks";
    public static final String PARAM_DOREMARKS = "DORemarks";
    public static final String PARAM_APPLICATION_STATUS = "applicationStatus";
    public static final String PARAM_APPROVAL = "approval";
    public static final String PARAM_APPROVAL_STATUS = "approvalStatus";
    public static final String PARAM_FACILITY_ID = "facilityId";
    public static final String PARAM_APPLICATION_ID = "applicationId";
    public static final String PARAM_REASON_TYPE_AO = "REASON01";
    public static final String PARAM_REASON_TYPE_DO = "REASON02";
    public static final String PARAM_APP_ID = "appId";
    public static final String PARAM_SEARCH_APP_DATE_FROM = "searchAppDateFrom";
    public static final String PARAM_SEARCH_APP_DATE_TO = "searchAppDateTo";

    public static final String PARAM_PROCESS_TYPE = "processType";
    public static final String PARAM_APPLICATION_DATE = "applicationDate";
    public static final String PARAM_APPLICATION_NO = "applicationNo";
    public static final String PARAM_APPLICATION_TYPE = "applicationType";

    public static final String PARAM_BLOCK_NO = "blockNo";
    public static final String PARAM_FLOOR_NO = "floorNo";
    public static final String PARAM_POSTAL_CODE = "postalCode";
    public static final String PARAM_UNIT_NO = "unitNo";
    public static final String PARAM_STREET_NAME = "streetName";

    //application status
    public static final String PARAM_DECISION = "decision";
    public static final String PARAM_APPLICATION_STATUS_PENDING_AO = "BSBAPST002";
    //facility status and approval status
    public static final String PARAM_FACILITY_STATUS_REVOKED = "FACSTA007";
    public static final String PARAM_APPROVAL_STATUS_REVOKED = "APPRSTA003";

    public static final String PARAM_APPLICATION_TYPE_REVOCATION = "BSBAPTY006";

    public static final String PARAM_PROCESS_TYPE_APPROVAL_TO_POSSESS = "PROTYPE002";
    public static final String PARAM_PROCESS_TYPE_APPROVAL_TO_LSP = "PROTYPE003";
    public static final String PARAM_PROCESS_TYPE_SPECIAL_APPROVAL_TO_HANDLE = "PROTYPE004";
    public static final String PARAM_PROCESS_TYPE_FACILITY_REGISTRATION = "PROTYPE001";
    public static final String PARAM_PROCESS_TYPE_AFC_REGISTRATION = "PROTYPE005";

    public static final String MODULE_REVOCATION = "Revocation";
    public static final String FUNCTION_REVOCATION = "Revocation";

    public static final String KEY_APPLICATION_PAGE_INFO = "pageInfo";
    public static final String KEY_APPLICATION_DATA_LIST = "dataList";
    public static final String KEY_ACTION_VALUE = "action_value";
    public static final String KEY_ACTION_ADDT = "action_additional";

    public static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    public static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";
    public static final String AUDIT_DOC_DTO = "auditDocDto";

    public static final String FACILITY = "facility";
}
