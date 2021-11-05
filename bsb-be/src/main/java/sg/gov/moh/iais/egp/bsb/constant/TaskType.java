package sg.gov.moh.iais.egp.bsb.constant;

/**
 * BSB task types (also means stages in sometimes)
 * API part has a enum with the same name and it has more information.
 */
public enum TaskType {
    SCREENING_DO, SCREENING_AO, SCREENING_HM,
    PRE_INSPECTION, INSPECTION, POST_INSPECTION,
    PROCESSING_DO, PROCESSING_AO, PROCESSING_HM
}
