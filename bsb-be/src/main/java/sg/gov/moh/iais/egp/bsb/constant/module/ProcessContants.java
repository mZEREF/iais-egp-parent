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

    public static final String KEY_DO_SCREENING_DTO                 = "doScreeningDto";
    public static final String KEY_AO_SCREENING_DTO                 = "aoScreeningDto";
    public static final String KEY_HM_SCREENING_DTO                 = "hmScreeningDto";
    public static final String KEY_DO_PROCESSING_DTO                = "doProcessingDto";
    public static final String KEY_AO_PROCESSING_DTO                = "aoProcessingDto";
    public static final String KEY_SUBMIT_DETAILS_DTO               = "submitDetailsDto";

    public static final String KEY_MOH_PROCESS_DTO                          = "mohProcessDto";
    public static final String KEY_INFO_DTO                                 = "infoDto";

    public static final String KEY_REMARKS                                  = "remarks";
    public static final String KEY_PROCESSING_DECISION                      = "processingDecision";
    public static final String KEY_INSPECTION_REQUIRED                      = "inspectionRequired";
    public static final String KEY_SELECT_APPROVING_OFFICER                 = "selectAO";

    public static final String KEY_RISK_LEVEL                       = "riskLevel";
    public static final String KEY_RISK_LEVEL_COMMENTS              = "riskLevelComments";
    public static final String KEY_ERP_REPORT_DATE                  = "erpReportDate";
    public static final String KEY_RED_TEAMING_REPORT_DATE          = "redTeamingReportDate";
    public static final String KEY_LENTIVIRUS_REPORT_DATE           = "lentivirusReportDate";
    public static final String KEY_INTERAL_INSPECTION_REPORT        = "internalInspectionReportDate";
    public static final String KEY_SELECTED_AFC                     = "selectedAfc";
    public static final String KEY_VALIDITY_START_DATE              = "validityStartDate";
    public static final String KEY_VALIDITY_END_DATE                = "validityEndDate";
    public static final String KEY_AO_REMARKS                       = "aoRemarks";
    public static final String KEY_REVIEWING_DECISION               = "reviewingDecision";
    public static final String KEY_FINAL_REMARKS                    = "finalRemarks";
    public static final String KEY_HM_REMARKS                       = "hmRemarks";
}
