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
    public static String CRUD_ACTION_TYPE_VSS               = "crud_action_type_vss";
    public static String CRUD_ACTION_TYPE_TOP               = "crud_action_type_top";

    public static String ACTION_STATUS                      = "DS_ACTION_STATUS";

    public static String CRUD_TYPE_FROM_DRAFT               = "fromDraft";
    public static String CRUD_TYPE_RFC                      = "rfc";

    public static String CURRENT_PAGE_STAGE                 = "currentPageStage";
    public static String PAGE_STAGE_PAGE                    = "page";
    public static String PAGE_STAGE_PREVIEW                 = "preview";
    public static String PAGE_STAGE_ACK                     = "ack";

    public static String EMAIL_ADDRESS                      = "emailAddress";
    public static String SUBMITTED_BY                       = "submittedBy";

    public static String AR_DATA_LIST                       = "arSuperList";

    public static String AR_DATA_SUBMISSION                 = "arSuperDataSubmissionDto";
    public static String AR_OLD_DATA_SUBMISSION             = "arOldSuperDataSubmissionDto";
    public static String AR_PREMISES_MAP                    = "AR_PREMISES_MAP";
    public static String AR_PREMISES                        = "AR_PREMISES";

    public static String DP_DATA_SUBMISSION                 = "dpSuperDataSubmissionDto";
    public static String DP_OLD_DATA_SUBMISSION             = "dpOldSuperDataSubmissionDto";
    public static String DP_PREMISES_MAP                    = "DP_PREMISES_MAP";
    public static String DP_PREMISES                        = "DP_PREMISES";

    public static String VSS_DATA_SUBMISSION                 = "vssSuperDataSubmissionDto";
    public static String VSS_OLD_DATA_SUBMISSION             = "vssOldSuperDataSubmissionDto";
    public static String VSS_PREMISES_MAP                    = "VSS_PREMISES_MAP";
    public static String VSS_PREMISES                        = "VSS_PREMISES";

    public static String TOP_DATA_SUBMISSION                 = "topSuperDataSubmissionDto";
    public static String TOP_OLD_DATA_SUBMISSION             = "topOldSuperDataSubmissionDto";
    public static String TOP_PREMISES_MAP                    = "TOP_PREMISES_MAP";
    public static String TOP_PREMISES                        = "TOP_PREMISES";

    public static String DS_TITLE_NEW                       = "New Data Submission";
    public static String DS_TITLE_RFC                       = "Amendment";

    public static String PRINT_FLAG                         = "printflag";
    public static String PRINT_FLAG_PTART                   = "PTART";
    public static String PRINT_FLAG_PTDRP                   = "PTDRP";
    public static String PRINT_FLAG_ART                     = "ART";
    public static String PRINT_FLAG_DRP                     = "DRP";
    public static String PRINT_FLAG_LDT                     = "LDT";
    public static String PRINT_FLAG_VSS                     = "VSS";
    public static String PRINT_FLAG_TOP                     = "TOP";
    public static String PRINT_FLAG_ACKART                  = "ACKART";
    public static String PRINT_FLAG_ACKLDT                  = "ACKLDT";
    public static String PRINT_FLAG_ACKDRP                  = "ACKDRP";
    public static String PRINT_FLAG_ACKVSS                  = "ACKVSS";
    public static String PRINT_FLAG_ACKTOP                  = "ACKVSS";

    public static String LAB_SUPER_DATA_SUBMISSION          = "LdtSuperDataSubmissionDto";
    public static String LDT_PREMISS_OPTION                 = "premissOptions";
    public static String LDT_CANOT_LDT                      = "cannotCLT";

    public static String FILE_ITEM_ERROR_MSGS               = "fileItemErrorMsgs";


    public static String RFC_NO_CHANGE_ERROR               = "RFC_NO_CHANGE_ERROR";
}
