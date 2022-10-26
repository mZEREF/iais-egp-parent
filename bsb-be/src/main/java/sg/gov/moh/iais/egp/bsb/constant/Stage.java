package sg.gov.moh.iais.egp.bsb.constant;


/**
 * Stages for flows in BSB system. Saved in application table.
 * <p>
 * The order is important! It decides the sequence, some business logic relies on it.
 * <p>
 * The name is important! The name is persisted into the DB, changing of name will lead to incorrect data!
 * You must do data patch with it altogether!
 */
public enum Stage {
    DRAFT,
    SCREENING,
    VERIFICATION,
    PRE_INSPECTION,
    ACTUAL_INSPECTION,
    CERTIFICATION,
    INSPECTION_REPORT,
    NC_RECTIFICATION,
    FOLLOW_UP,
    PROCESSING,
    TERMINAL
}
