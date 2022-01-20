package sg.gov.moh.iais.egp.bsb.constant;

/**
 * @author YiMing
 * @version 2021/12/30 16:13
 **/
public class IncidentProcessConstants {
    private IncidentProcessConstants(){}

    public static final String MOH_PROCESSING_DECISION_REQUEST_FOR_INFO = "MOHPRO002";
    public static final String MOH_PROCESSING_DECISION_SCREENED_BY_DO = "MOHPRO001";
    public static final String MOH_PROCESSING_DECISION_APPROVE = "MOHPRO007";
    public static final String MOH_PROCESSING_DECISION_REJECT = "MOHPRO003";
    public static final String MOH_PROCESSING_DECISION_RECOMMEND_REJECT = "MOHPRO005";
    public static final String MOH_PROCESSING_DECISION_APPROVE_FOR_INSPECTION = "MOHPRO006";
    public static final String MOH_PROCESSING_DECISION_RECOMMEND_APPROVAL = "MOHPRO004";
    public static final String MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO = "MOHPRO008";
    public static final String MOH_PROCESSING_DECISION_ROUTE_BACK_TO_HM = "MOHPRO009";


    public static final String ACTION_TYPE_SUBMIT = "submit";
    public static final String ACTION_TYPE_BACK = "doBack";
    public static final String ACTION_TYPE_VIEW_APPLICATION = "view";
    public static final String ACTION_TYPE_PREPARE = "prepare";

    public static final String PARAM_ACTION_TYPE = "action_type";

    public static final String KEY_INDEED_ACTION_TYPE = "indeed_action_type";
}
