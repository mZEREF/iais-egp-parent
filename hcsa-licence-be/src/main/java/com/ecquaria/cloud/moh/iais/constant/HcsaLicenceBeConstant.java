package com.ecquaria.cloud.moh.iais.constant;

import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;

public class HcsaLicenceBeConstant {

    private HcsaLicenceBeConstant() {
    }

    public static final String GET_HCI_SERVICE_SELECTION_NAME_TAG = "name";
    public static final String GET_HCI_SERVICE_SELECTION_COED_TAG = "code";
    public static final String RESULT_LAST_COMPLIANCE_FULL_NAME = "Full Compliance";
    public static final String RESULT_LAST_COMPLIANCE_PARTIAL_NAME = "Partial Compliance";
    public static final String RESULT_LAST_COMPLIANCE_FULL_CODE = "full";
    public static final String RESULT_LAST_COMPLIANCE_PARTIAL_CODE = "part";   // NC
    public static final String RISK_TYPE_LAST_INSPECTION = "lastInsp";
    public static final String RISK_TYPE_SECOND_INSPECTION = "secLastInsp";
    public static final String RISK_TYPE_FINANCIAL = "finance";
    public static final String RISK_TYPE_LEADERSHIP = "ledership";
    public static final String RISK_TYPE_LEGISLATIVE_BREACHES = "leg";
    public static final String RISK_TYPE_OVERALL= "overall";
    public static final String INCLUDE_RISK_TYPE_LEADERSHIP_KEY_TEXT = "Leadership and Governance Risk";
    public static final String INCLUDE_RISK_TYPE_INSPECTION_KEY_TEXT = "Compliance Risk";
    public static final String ERROR_MESSAGE_MIN_NC = " for Minimum Number of NCs";
    public static final String ERROR_MESSAGE_MAX_NC = " for Maximum Number of NCs";
    public static final String ERROR_MESSAGE_MIN_CASES = " for Minimum Number of Cases";
    public static final String ERROR_MESSAGE_MAX_CASES = " for Maximum Number of Cases";
    public static final String ERROR_MESSAGE_MIN_NC_NO_SPACE = "Minimum Number of NCs";
    public static final String ERROR_MESSAGE_MAX_NC_NO_SPACE = "Maximum Number of NCs";
    public static final String ERROR_MESSAGE_MIN_CASES_NO_SPACE = "Minimum Number of Cases";
    public static final String ERROR_MESSAGE_MAX_CASES_NO_SPACE = "Maximum Number of Cases";
    public static final String MOH_RISK_CONIG_MENU  = "MohRiskConigMenu";
    public static final String RISK_NEED_BACK_BUTTON = "backButtonNeed";
    public static final String RISK_NEED_BACK_BUTTON_YES = "Y";
    public static final String RISK_NEED_BACK_BUTTON_NO = "N";
    public static final String  SEARCH_PRAM_FOR_AUDIT_LIST_RESULT = "auditTaskDataDtos";
    public static final String  SEARCH_PRAM_FOR_AUDIT_LIST = "auditTaskDataDtos_pram";
    public static final String  SEARCH_PRAM_FOR_AUDIT_LIST_TRUE_RESULT = "auditTaskDataDtosResult";
    public static final String SESSION_ROLEIDS_FOR_AUDIT = "roleIdsForAudit";
    public static final String SESSION_ROLEIDS_FOR_AUDIT_SELECT = "roleIdsForAuditSelect";
    public static final String REQUEST_FOR_ACK_CODE = "RSMVerisonChanged";

    public static final String SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE = "specialServiceForChecklistDecide";
    public static final String SPECIAL_SERVICE_FOR_CHECKLIST_DTOS = "specialServiceForChecklistDecideDtos";

    public static final String CHECK_LIST_ERROR_TAB_NAME  = "errorTab";
    public static final String CHECK_LIST_ERROR_SPEC_TAB_NAME  = "errorSpecTab";

    public static final String CHECK_LIST_COM_TAB_NAME  = "nowComTabIn";

    public static final int RISK_Validate_HIGH_MAX = 999;

    public static final String APP_VEHICLE_FLAG = "appVehicleFlag";

    public static final String APP_VEHICLE_NO_LIST ="appVehicleNoList";

    public static final String EDIT_VEHICLE_FLAG = InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT + "_"+ InspectionConstants.SWITCH_ACTION_EDIT;

    public static final String REPORT_ACK_CLARIFICATION_FLAG = "askType";

    public static final String  SEARCH_PARAM_CHANGE_TUC_DATE = "licencePremiseParam";
    public static final String  SEARCH_RESULT_CHANGE_TUC_DATE = "licencePremiseResult";
    public static final String  KEY_SVC_TYPE_OPTIONS = "licSvcTypeOption";
}
