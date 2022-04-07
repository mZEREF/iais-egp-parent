package sg.gov.moh.iais.egp.bsb.constant.module;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
public class ProcessContants {
    private ProcessContants() {}

    public static final String CRUD_ACTION_TYPE_PROCESS             = "process";
    public static final String CRUD_ACTION_TYPE_PREPARE             = "prepare";

    public static final String MOH_PROCESS_PAGE_VALIDATION          = "mohProcessPageValidation";

    public static final String MODULE_NAME                          = "Moh Process";
    public static final String FUNCTION_NAME_DO_SCREENING           = "DO Screening";
    public static final String FUNCTION_NAME_AO_SCREENING           = "AO Screening";
    public static final String FUNCTION_NAME_HM_SCREENING           = "HM Screening";
    public static final String FUNCTION_NAME_DO_PROCESSING          = "DO Processing";
    public static final String FUNCTION_NAME_AO_PROCESSING          = "AO Processing";
    public static final String FUNCTION_NAME_HM_PROCESSING          = "HM Processing";

    public static final String MODULE_NAME_DO_SCREENING             = "doScreening";
    public static final String MODULE_NAME_AO_SCREENING             = "aoScreening";
    public static final String MODULE_NAME_HM_SCREENING             = "hmScreening";
    public static final String MODULE_NAME_DO_PROCESSING            = "doProcessing";
    public static final String MODULE_NAME_AO_PROCESSING            = "aoProcessing";
    public static final String MODULE_NAME_HM_PROCESSING            = "hmProcessing";

    public static final String KEY_MOH_PROCESS_DTO                  = "mohProcessDto";

    public static final String KEY_REMARKS                          = "remarks";
    public static final String KEY_PROCESSING_DECISION              = "processingDecision";
    public static final String KEY_INSPECTION_REQUIRED              = "inspectionRequired";
    public static final String KEY_CERTIFICATION_REQUIRED           = "certificationRequired";
    public static final String KEY_SELECT_MOH_USER                  = "selectMohUser";
}
