package sg.gov.moh.iais.egp.bsb.constant;


public enum AssessmentState {
    // assessment are never submitted
    NONE,

    // not filled or uploaded, but waiting for input
    PEND,

    // filled or uploaded, but not submitted
    DRAFT,

    // already submitted to officer
    SUBMITTED,

    // assessment need to be changed due to RFI
    RFI
}
