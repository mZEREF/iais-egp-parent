package com.ecquaria.cloud.moh.iais.constant;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/5/8
 **/

public final class SelfAssessmentConstant {
    private SelfAssessmentConstant() {throw new IllegalStateException("can not construct class");}

    public static final String SELF_ASSESSMENT_QUERY_ATTR  = "selfAssessmentQueryAttr";
    public static final String SELF_ASSESSMENT_DETAIL_ATTR  = "selfAssessmentDetail";
    public static final String SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION  = "tabIndex";
    public static final String SELF_ASSESSMENT_DETAIL_CAN_EDIT_ANSWER_FLAG  = "canEditAnswerFlag";
    public static final String SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION  = "prevTabIndex";
    public static final String SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP  = "detailIndexMap";
    public static final String SELF_ASSESSMENT_EACH_DETAIL_CORR_ID  = "selfAssessmentCorrId";
    public static final String SELF_ASSESSMENT_HAS_SUBMITTED_FLAG  = "hasSubmitted";
    public static final String SELF_ASSESSMENT_HAS_SUBMITTED_ERROR_MSG  = "hasSubmittedMsg";

/*
    public static final String SELF_ASSESSMENT_CURRENT_ACTION_GROUP_ID  = "self_assessment_current_action_group_id";
    public static final String SELF_ASSESSMENT_CURRENT_ACTION_CORR_ID  = "self_assessment_current_action_corr_id";*/

    public static final String SELF_ASSESSMENT_RFI_ACTION  = "rfi";
}
