package com.ecquaria.cloud.moh.iais.constant;

/**
 * DataSubmissionConstant
 *
 * @author suocheng
 * @date 10/21/2021
 */

public class DataSubmissionConstant {
    private DataSubmissionConstant() {throw new IllegalStateException("DataSubmissionConstant class");}
    public static String CRUD_TYPE                          = "crud_type";
    public static String CRUD_ACTION_TYPE_DS                = "crud_action_type_ds";
    public static String CRUD_ACTION_TYPE_AR                = "crud_action_type_ar";
    public static String CRUD_ACTION_TYPE_CT                = "crud_action_type_ct";
    public static String CRUD_ACTION_TYPE_DP                = "crud_action_type_dp";

    public static String CRUD_TYPE_FROM_DRAFT               = "fromDraft";
    public static String CRUD_TYPE_RFC                      = "rfc";

    public static String CURRENT_PAGE_STAGE                 = "currentPageStage";

    public static String AR_DATA_SUBMISSION                 = "arSuperDataSubmissionDto";
    public static String AR_PREMISES_MAP                    = "AR_PREMISES_MAP";
    public static String AR_PREMISES                        = "AR_PREMISES";
    public static String AR_PATIENT                         = "AR_PATIENT";

    public static String DP_DATA_SUBMISSION                 = "dpSuperDataSubmissionDto";
    public static String DP_PREMISES_MAP                    = "DP_PREMISES_MAP";
    public static String DP_PREMISES                        = "DP_PREMISES";
}
