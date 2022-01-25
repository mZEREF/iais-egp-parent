package sg.gov.moh.iais.egp.bsb.constant.module;

/**
 * @author : LiRan
 * @date : 2022/1/20
 */
public class ProcessDeregistrationConstants {
    private ProcessDeregistrationConstants() {}

    public static final String MODULE_NAME                        = "MOH Processing";
    public static final String FUNCTION_NAME_DO_PROCESS           = "DO Processing";
    public static final String FUNCTION_NAME_AO_PROCESS           = "AO Processing";
    public static final String FUNCTION_NAME_HM_PROCESS           = "HM Processing";

    public static final String KEY_DO_PROCESS_DTO                 = "doProcessDto";
    public static final String KEY_AO_PROCESS_DTO                 = "aoProcessDto";
    public static final String KEY_HM_PROCESS_DTO                 = "hmProcessDto";
    public static final String KEY_SUBMISSION_DETAILS_DTO         = "submissionDetailsDto";

    public static final String KEY_SUBJECT                        = "subject";
    public static final String KEY_DYNAMIC_CONTENT                = "dynamicContent";
    public static final String KEY_DO_REMARKS                     = "doRemarks";
    public static final String KEY_AO_REMARKS                     = "aoRemarks";
    public static final String KEY_HM_REMARKS                     = "hmRemarks";
    public static final String KEY_FINAL_REMARKS                  = "finalRemarks";
    public static final String KEY_PROCESSING_DECISION            = "processingDecision";
    public static final String KEY_REASON_FOR_REJECTION           = "reasonForRejection";

    public static final String PROCESS_PAGE_VALIDATION            = "processPageValidation";

    public static final String INDEED_ACTION_TYPE_PREPARE_DATA    = "prepareData";
    public static final String INDEED_ACTION_TYPE_DO_PROCESS      = "doProcess";
}
