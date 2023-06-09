package sg.gov.moh.iais.egp.bsb.constant.module;

public class InspectionConstants {
    private InspectionConstants() {}

    public static final String KEY_APP_ID = "appId";
    public static final String MASK_PARAM_COMMENT_REPORT = "commentInsReportAppId";

    public static final String KEY_COMMENT_REPORT_DATA = "commentInsReportDTO";
    public static final String KEY_REPORT_REPO_ID = "reportRepoId";
    public static final String KEY_INSPECTION_REPORT_DTO = "reportDto";

    public static final String KEY_ROUTE = "route";
    public static final String KEY_ACK_MSG = "ackMsg";
    public static final String KEY_DASHBOARD_MSG = "dashboardMsg";

    //NC Rectification and Follow-up items
    public static final String KEY_NCS_RECTIFICATION_DISPLAY_DATA = "ncsPreData";
    public static final String KEY_RECTIFY_SAVED_DATA_MAP = "ncsSavedRemarkMap";
    public static final String KEY_RECTIFY_SAVED_DOC_DTO = "ncsSavedDocDto";
    public static final String KEY_RECTIFY_ITEM_SAVE_DTO = "rectifyItemSaveDto";
    public static final String KEY_ITEM_VALUE     = "itemValue";
    public static final String KEY_MASKED_ITEM_VALUE = "itemVal";
    public static final String KEY_ITEM_RECTIFY_MAP = "rectifyMap";
    public static final String KEY_ALL_ITEM_RECTIFY = "isAllRectify";
    public static final String KEY_REMARKS = "remarks";
    public static final String KEY_REQUEST_EXTENSION_OF_DUE_DATE = "requestExtensionOfDueDate";
    public static final String KEY_REASON_FOR_EXTENSION = "reasonForExtension";
    public static final String KEY_NEW_SAVED_DOCUMENT = "newSavedDoc";
    public static final String KEY_SAVED_DOCUMENT = "oldSavedDoc";
    public static final String KEY_FOLLOW_UP_VIEW_DTO = "followUpViewDto";
    public static final String KEY_REMARK_HISTORY_LIST = "remarkHistoryList";
    public static final String KEY_REVIEW_AFC_REPORT_DTO = "reviewAFCReportDto";

    //Reschedule Appointment
    public static final String KEY_APPOINTMENT_LIST_DATA_LIST = "dataList";
    public static final String KEY_APPOINTMENT_PAGE_INFO = "pageInfo";

    public static final String KEY_APPOINTMENT_SEARCH_DTO = "apptSearchDto";

    //AFC
    public static final String KEY_AFC_DASHBOARD_MSG = "AFC Certification Documents Submission";
    public static final String KEY_APPLICANT_DASHBOARD_MSG = "AFC Certification Documents Review";
    public static final String KEY_AFC_ACK_MSG = "Your Submission is successfully uploaded.";

    public static final String PARAM_PREPARE = "prepare";
    public static final String PARAM_NEXT = "next";
    public static final String PARAM_CAN_ACTION_ROLE = "canActionRole";

    public static final String FINAL_ROUND            = "finalRound";
}
