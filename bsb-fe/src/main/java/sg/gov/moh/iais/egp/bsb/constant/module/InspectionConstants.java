package sg.gov.moh.iais.egp.bsb.constant.module;

public class InspectionConstants {
    private InspectionConstants() {}

    public static final String KEY_APP_ID = "appId";
    public static final String MASK_PARAM_COMMENT_REPORT = "commentInsReportAppId";

    public static final String KEY_COMMENT_REPORT_DATA = "commentInsReportDTO";
    public static final String KEY_REPORT_REPO_ID = "reportRepoId";

    public static final String KEY_ROUTE = "route";

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

    //Reschedule Appointment
    public static final String MODULE_NAME = "Inbox";
    public static final String KEY_APPOINTMENT_LIST_DATA_LIST = "dataList";
    public static final String KEY_APPOINTMENT_PAGE_INFO = "pageInfo";

    public static final String KEY_APPOINTMENT_SEARCH_DTO = "apptSearchDto";

    public static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    public static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";
}
