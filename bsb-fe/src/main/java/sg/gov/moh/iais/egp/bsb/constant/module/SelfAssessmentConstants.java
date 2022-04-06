package sg.gov.moh.iais.egp.bsb.constant.module;

public class SelfAssessmentConstants {
    private SelfAssessmentConstants() {}

    public static final String KEY_ENTRY_APP_ID = "entryAppId";
    public static final String KEY_APP_ID = "appId";
    public static final String MASK_PARAM = "selfAssessAppId";

    public static final String KEY_DATA_DTO = "dataDto";
    public static final String KEY_ACTIONS = "actions";
    public static final String KEY_CURRENT_ACTION = "cur_action";

    public static final String KEY_SELF_ASSESSMENT_CHK_LST = "bsbSelfAssessmentCheckList";
    public static final String KEY_SELF_ASSESSMENT_CONFIG = "bsbSelfAssessmentConfig";
    public static final String KEY_COMMON_SELF_ASSESSMENT_CONFIG = "bsbCommonSelfAssessmentConfig";
    public static final String KEY_SELF_ASSESSMENT_ANSWER_MAP = "bsbSelfAssessmentAnswerMap";

    public static final String KEY_CHKL_CONFIGS = "checklistConfigDtos";
    public static final String KEY_ANSWER_MAP = "answerMap";
    public static final String KEY_EDITABLE = "editable";

    // actions for self assessment
    public static final String ACTION_FILL = "Fill";
    public static final String ACTION_EDIT = "Edit";
    public static final String ACTION_VIEW = "View";
    public static final String ACTION_DOWNLOAD = "Download";
    public static final String ACTION_UPLOAD = "Upload";
    public static final String ACTION_PRINT = "Print";

    // key separator
    public static final String KEY_SEPARATOR = "--";
    public static final String KEY_REMARKS = "-remarks";
}
