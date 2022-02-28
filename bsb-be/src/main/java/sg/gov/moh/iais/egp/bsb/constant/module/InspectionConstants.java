package sg.gov.moh.iais.egp.bsb.constant.module;

public class InspectionConstants {
    private InspectionConstants() {}

    public static final String KEY_APP_ID = "appId";
    public static final String KEY_TASK_ID = "taskId";

    public static final String KEY_SELF_ASSESSMENT_CHK_LST = "bsbSelfAssessmentCheckList";
    public static final String KEY_SELF_ASSESSMENT_CONFIG = "bsbSelfAssessmentConfig";
    public static final String KEY_SELF_ASSESSMENT_ANSWER_MAP = "bsbSelfAssessmentAnswerMap";
    public static final String KEY_RESULT_MSG = "resultMsg";

    public static final String KEY_CHKL_CONFIG = "checklistConfigDto";
    public static final String KEY_ANSWER_MAP = "answerMap";
    public static final String KEY_EDITABLE = "editable";
    public static final String KEY_SELF_ASSESSMENT_UNAVAILABLE = "selfAssessmentUnavailable";

    public static final String KEY_INS_DECISION = "insDecision";
    public static final String KEY_INS_INFO = "insInfo";
    public static final String KEY_INS_FINDING = "insFindingList";
    public static final String KEY_INS_NON_COMPLIANCE = "insRectificationList";
    public static final String KEY_INS_OUTCOME = "insOutcome";
    public static final String KEY_INS_CHK_LST = "bsbInspectionChecklist";
    public static final String KEY_INS_CHKL_ANSWER_MAP = "bsbInspectionChecklistAnswerMap";

    public static final String KEY_CHKL_ITEM_SELECTION = "itemSelection";
    public static final String KEY_INSPECTION_CONFIG = "bsbInspectionConfig";

    public static final String KEY_SEPARATOR = "--";
    public static final String KEY_SECTION_SEPARATOR = "--v--";

    public static final String TAB_ACTIVE = "activeTab";
    public static final String TAB_FAC_INFO = "tabInfo";
    public static final String TAB_INS_DETAIL = "tabInsDetails";
    public static final String TAB_DOC = "tabDocuments";
    public static final String TAB_INS_FINDING = "tabInsFinding";
    public static final String TAB_PROCESSING = "tabProcessing";
    public static final String TAB_INS_REPORT = "tabInsReport";

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

    public static final String VALUE_OUTCOME_DEFICIENCY_MAJOR = "Major";
    public static final String VALUE_OUTCOME_DEFICIENCY_MINOR = "Minor";
    public static final String VALUE_OUTCOME_DEFICIENCY_NIL = "NIL";
    public static final String VALUE_OUTCOME_FOLLOW_UP_REQUIRED_YES = "Yes";
    public static final String VALUE_OUTCOME_FOLLOW_UP_REQUIRED_NO = "No";
    public static final String VALUE_OUTCOME_OUTCOME_PASS = "Pass";
    public static final String VALUE_OUTCOME_OUTCOME_PASS_WITH_CONDITION = "Pass with condition(s)";
    public static final String VALUE_OUTCOME_OUTCOME_FAIL = "Fail";

    public static final String KEY_ROUTE = "route";

    // DO review inspection follow-up items
    public static final String KEY_DO_REVIEW_FOLLOW_UP_ITEMS_DTO = "doReviewFollowUpItemsDto";
}
