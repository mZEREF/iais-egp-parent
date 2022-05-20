package sg.gov.moh.iais.egp.bsb.constant;


public enum AssessmentState {
    // assessment are never submitted
    NONE,

    // not submitted, but waiting for submission
    PEND,

    // already submitted
    SUBMITTED,

    // assessment need to be changed due to RFI
    RFI,

    // there should be an assessment, but actually not in DB
    /* It takes a cost to judge error situation, so we usually don't check and will not use this value */
    EXCEPTION
}
