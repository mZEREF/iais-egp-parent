package sg.gov.moh.iais.egp.bsb.constant.module;

import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;

public class InspectionConstants {
    private InspectionConstants() {}

    public static final String KEY_APP_ID = "appId";
    public static final String KEY_TASK_ID = "taskId";
    public static final String KEY_ACTION_TYPE_SKIP = "skip";

    public static final String KEY_APP_STATUS = "appStatus";

    public static final String KEY_INS_CHECKLIST_DTO = "inspectionChecklistDto";
    public static final String KEY_SELF_ASSESSMENT_CHK_LST = "bsbSelfAssessmentCheckList";
    public static final String KEY_SELF_ASSESSMENT_CONFIG = "bsbSelfAssessmentConfig";
    public static final String KEY_SELF_ASSESSMENT_ANSWER_MAP = "bsbSelfAssessmentAnswerMap";
    public static final String KEY_RESULT_MSG = "resultMsg";

    public static final String KEY_CHKL_CONFIG = "checklistConfigDto";
    public static final String KEY_ANSWER_MAP = "answerMap";
    public static final String KEY_EDITABLE = "editable";
    public static final String KEY_SELF_ASSESSMENT_AVAILABLE = "selfAssessmentAvailable";
    public static final String KEY_CAN_RFI = "canRfi";

    public static final String KEY_INS_DECISION = "insDecision";
    public static final String KEY_INS_INFO = "insInfo";
    public static final String KEY_INS_DTO_INS_LETTER = "letterDto";
    public static final String KEY_INS_NC_EMAIL_DTO = "ncEmailDto";
    public static final String KEY_INS_FINDING = "insFindingList";
    public static final String KEY_INS_NON_COMPLIANCE = "insRectificationList";
    public static final String KEY_INS_OUTCOME = "insOutcome";
    public static final String KEY_INS_CHK_LST = "bsbInspectionChecklist";
    public static final String KEY_INS_CHKL_ANSWER_MAP = "bsbInspectionChecklistAnswerMap";
    public static final String KEY_INS_REPORT = "reportDto";
    public static final String KEY_INS_DOC_RECORD_INFO_SUB_TYPE_MAP = "insRecordInfoSubTypeMap";

    public static final String KEY_CHKL_ITEM_SELECTION = "itemSelection";
    public static final String KEY_INSPECTION_CONFIG = "bsbInspectionConfig";

    public static final String KEY_SEPARATOR = "--";
    public static final String KEY_SECTION_SEPARATOR = "--v--";

    public static final String TAB_ACTIVE = "activeTab";
    public static final String TAB_FAC_INFO = "tabInfo";
    public static final String TAB_INS_FINDING = "tabInsFinding";
    public static final String TAB_RECTIFICATION = "tabRectification";
    public static final String TAB_APPROVAL_LETTER = "tabApprovalLetter";
    public static final String TAB_PROCESSING = "tabProcessing";
    public static final String TAB_INS_REPORT = "tabInsReport";
    public static final String TAB_SUBMIT_INTO = "sumissionInfo";
    public static final String TAB_DOC = "tabDocuments";
    public static final String TAB_FAC_DETAIL = "tabFacilityDetails";
    public static final String TAB_CHECKLIST = "tabChecklist";
    public static final String TAB_INS_FOLLOW_UP = "tabFollowUp";
    public static final String TAB_FAC_DETAILS = "tabFacDetails";
    public static final String TAB_AFC_SELECTION = "tabAfcSelection";
    public static final String TAB_CERTIFICATION = "tabCertification";

    public static final String KEY_FINDING_ITEM = "findingItem";
    public static final String KEY_FINDING_TYPE = "findingType";
    public static final String KEY_FINDING_REMARK = "findingRemark";
    public static final String KEY_FINDING_DEADLINE = "deadline";

    public static final String KEY_OUTCOME_DEFICIENCY = "deficiency";
    public static final String KEY_OUTCOME_FOLLOWUP = "followUpReq";
    public static final String KEY_OUTCOME_OUTCOME = "outcome";
    public static final String KEY_OUTCOME_REMARKS = "remarks";

    public static final String VALUE_FINDING_TYPE_NC = "Non-compliance";
    public static final String VALUE_FINDING_TYPE_FOLLOW_UP = "Follow-up";

    public static final String KEY_ROUTE = "route";
    public static final String KEY_DASHBOARD_MSG = "dashboardMsg";

    public static final String KEY_ADHOC_CHECKLIST_ACTION_FLAG = "adhocActionFlag";
    public static final String KEY_ADHOC_CHECKLIST_LIST_ATTR = "adhocCheckListAttr";
    public static final String KEY_ADHOC_CHECKLIST_OLD_LIST_ATTR = "adhocCheckListAttrOld";

    public static final int VALUE_RFI_FLAG_SELF = 0;
    public static final int VALUE_RFI_FLAG_APPLICATION = 1;
    public static final int VALUE_RFI_FLAG_SELF_APPLICATION = 2;

    public static final String KEY_AFTER_SAVE_REPORT          = "AFTER_SAVE_REPORT";

    public static final String KEY_REVIEW_FOLLOW_UP_DTO = "reviewFollowUpDto";
    public static final String KEY_REQUEST_EXTENSION = "requestExtension";
    public static final String KEY_INS_SUBMIT_REPORT_DATE_DTO = "insSubmitReportDataDto";

    // upload file
    public static final String ACTION_UPLOAD_TYPE           = "act_upload_type";
    public static final String FILE_APPEND_ID               = "uploadFile";
    public static final String SEESION_FILES_MAP_AJAX       = IaisEGPConstant.SEESION_FILES_MAP_AJAX + FILE_APPEND_ID;
    public static final int START_ROW                       = 1;
    public static final String FILE_ITEM_ERROR_MSGS         = "fileItemErrorMsgs";

    public static final String SHEET_NAME_COMMON            = "General Regulation";
    public static final String SHEET_NAME_BSB               = "BSB Regulation";



    //AFC
    public static final String KEY_AFC_DASHBOARD_MSG = "AFC Certification Documents Submission";
    public static final String KEY_APPLICANT_DASHBOARD_MSG = "AFC Certification Documents Review";
    public static final String KEY_AFC_ACK_MSG = "Your Submission is successfully uploaded.";

    public static final String KEY_REVIEW_AFC_REPORT_DTO = "reviewAFCReportDto";
    public static final String PARAM_PREPARE = "prepare";
    public static final String PARAM_NEXT = "next";
    public static final String PARAM_CAN_ACTION_ROLE = "canActionRole";

    public static final String KEY_HAS_NON_COMPLIANCE = "hasNonCompliance";

    public static final String FINAL_ROUND            = "finalRound";
}
