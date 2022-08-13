package com.ecquaria.cloud.moh.iais.constant;

/**
 * DataSubmissionConstant
 *
 * @author suocheng
 * @date 10/21/2021
 */

public class DataSubmissionConstant {

    private DataSubmissionConstant() {throw new IllegalStateException("DataSubmissionConstant class");}

    public static final String CRUD_TYPE                          = "crud_type";
    public static final String CRUD_ACTION_TYPE_DS                = "crud_action_type_ds";
    public static final String CRUD_ACTION_TYPE_AR                = "crud_action_type_ar";
    public static final String CRUD_ACTION_TYPE_CT                = "crud_action_type_ct";
    public static final String CRUD_ACTION_TYPE_DP                = "crud_action_type_dp";
    public static final String CRUD_ACTION_TYPE_VSS               = "crud_action_type_vss";
    public static final String CRUD_ACTION_TYPE_TOP               = "crud_action_type_top";
    public static final String JUMP_ACTION_TYPE                   = "jump_action_type";
    public static final String ACTION_TYPE                        = "action_type";
    public static final String JUMP_TO_SUBMITTED_STAGE            = "jump_to_submitted_stage";
    public static final String TARGET_STAGE_USER_PERMISSIONS        = "target_stage_user_permissions";

    public static final String ACTION_STATUS                      = "DS_ACTION_STATUS";

    public static final String CRUD_TYPE_FROM_DRAFT               = "fromDraft";
    public static final String CRUD_TYPE_RFC                      = "rfc";

    public static final String CURRENT_PAGE_STAGE                 = "currentPageStage";
    public static final String PAGE_STAGE_PAGE                    = "page";
    public static final String PAGE_STAGE_PREVIEW                 = "preview";
    public static final String PAGE_STAGE_ACK                     = "ack";

    public static final String EMAIL_ADDRESS                      = "emailAddress";
    public static final String SUBMITTED_BY                       = "submittedBy";

    public static final String AR_DATA_LIST                       = "arSuperList";
    public static final String DP_DATA_LIST                       = "dpSuperList";

    public static final String AR_DATA_SUBMISSION                 = "arSuperDataSubmissionDto";
    public static final String AR_OLD_DATA_SUBMISSION             = "arOldSuperDataSubmissionDto";
    public static final String AR_PREMISES_MAP                    = "AR_PREMISES_MAP";
    public static final String AR_PREMISES                        = "AR_PREMISES";

    public static final String AR_TRANSFER_OUT_IN_PREMISES_SEL    = "transferOutInPremisesSelect";
    public static final String AR_TRANSFER_BIND_STAGE_ID          = "bindStageSubmissionId";
    public static final String AR_TRANSFER_BIND_STAGE_SUPER_DTO   = "bindStageArDto";

    public static final String DP_DATA_SUBMISSION                 = "dpSuperDataSubmissionDto";
    public static final String DP_OLD_DATA_SUBMISSION             = "dpOldSuperDataSubmissionDto";
    public static final String DP_PREMISES_MAP                    = "DP_PREMISES_MAP";
    public static final String DP_PREMISES                        = "DP_PREMISES";

    public static final String VSS_DATA_SUBMISSION                 = "vssSuperDataSubmissionDto";
    public static final String VSS_OLD_DATA_SUBMISSION             = "vssOldSuperDataSubmissionDto";
    public static final String VSS_PREMISES_MAP                    = "VSS_PREMISES_MAP";
    public static final String VSS_PREMISES                        = "VSS_PREMISES";

    public static final String TOP_DATA_SUBMISSION                 = "topSuperDataSubmissionDto";
    public static final String TOP_OLD_DATA_SUBMISSION             = "topOldSuperDataSubmissionDto";
    public static final String TOP_PREMISES_MAP                    = "TOP_PREMISES_MAP";
    public static final String TOP_PREMISES                        = "TOP_PREMISES";

    public static final String DS_TITLE_NEW                       = "New Data Submission";
    public static final String DS_TITLE_RFC                       = "Amendment";

    public static final String DS_TITLE_ART                       = "Assisted Reproduction";
    public static final String DS_TITLE_DRP                       = "Drug Practices";
    public static final String DS_TITLE_LDT                       = "Laboratory Developed Test";
    public static final String DS_TITLE_TOP                       = "Termination Of Pregnancy";
    public static final String DS_TITLE_PATIENT                   = "Patient Information";
    public static final String DS_TITLE_DONOR_SAMPLE              = "Donor Samples";
    public static final String DS_TITLE_CYCEL_STAGE               = "Cycle Stages";

    public static final String DS_SHOW_PATIENT                    = "Patient";
    public static final String DS_SHOW_HUSBAND                    = "Husband";

    public static final String PRINT_FLAG                         = "printflag";
    public static final String PRINT_FLAG_PTART                   = "PTART";
    public static final String PRINT_FLAG_PTDRP                   = "PTDRP";
    public static final String PRINT_FLAG_PTVSS                   = "PTVSS";
    public static final String PRINT_FLAG_PTTOP                   = "PTTOP";
    public static final String PRINT_FLAG_ART                     = "ART";
    public static final String PRINT_FLAG_DRP                     = "DRP";
    public static final String PRINT_FLAG_LDT                     = "LDT";
    public static final String PRINT_FLAG_VSS                     = "VSS";
    public static final String PRINT_FLAG_TOP                     = "TOP";
    public static final String PRINT_FLAG_ACKWD                   = "ACKWD";
    public static final String PRINT_FLAG_ACKART                  = "ACKART";
    public static final String PRINT_FLAG_ACKLDT                  = "ACKLDT";
    public static final String PRINT_FLAG_ACKDRP                  = "ACKDRP";
    public static final String PRINT_FLAG_ACKVSS                  = "ACKVSS";
    public static final String PRINT_FLAG_ACKTOP                  = "ACKTOP";

    public static final String LAB_SUPER_DATA_SUBMISSION          = "LdtSuperDataSubmissionDto";
    public static final String LDT_OLD_DATA_SUBMISSION             = "LdtOldSuperDataSubmissionDto";
    public static final String LDT_PREMISS_OPTION                 = "premissOptions";
    public static final String LDT_CANOT_LDT                      = "cannotCLT";
    public static final String LDT_IS_GUIDE                       = "selfAssessmentGuide";

    public static final String FILE_ITEM_ERROR_MSGS               = "fileItemErrorMsgs";


    public static final String RFC_NO_CHANGE_ERROR               = "RFC_NO_CHANGE_ERROR";

    public static final String DFT_ERROR_MC                      = "-1";

    //doctor source
    public static final String TOP_DOCTOR_INFO_FROM_PRS          = "TOPP"; //TOP get doctor info from PRS
    public static final String TOP_DOCTOR_INFO_FROM_ELIS         = "TOPE"; //TOP get doctor info from eLis
    public static final String TOP_DOCTOR_INFO_USER_NEW_REGISTER = "TOPT"; //TOP register new doctor
    public static final String DP_DOCTOR_INFO_FROM_PRS           = "DRPP"; //DP get doctor info from PRS
    public static final String DP_DOCTOR_INFO_FROM_ELIS          = "DRPE"; //DP get doctor info from eLis
    public static final String DP_DOCTOR_INFO_USER_NEW_REGISTER  = "DRPT"; //DP register new doctor
    public static final String VS_DOCTOR_INFO_FROM_PRS           = "VSSP"; //VSS get doctor info from PRS
    public static final String VS_DOCTOR_INFO_FROM_ELIS          = "VSSE"; //VSS get doctor info from eLis
    public static final String VS_DOCTOR_INFO_USER_NEW_REGISTER  = "VSST"; //VSS register new doctor

    //AR Cycle stage status
    public static final String AR_CYCLE_STAGE_STATUS_ONGOING        = "active";      //ongoing stage
    public static final String AR_CYCLE_STAGE_STATUS_SUBMITTED      = "completed";    //submitted stage
    public static final String AR_CYCLE_STAGE_STATUS_INVALID        = "disabled";      //invalid stage

    //The current stage, the actions that the user can do
    public static final String AR_CYCLE_USER_PERMISSIONS_VIEW         = "onlyView";
    public static final String AR_CYCLE_USER_PERMISSIONS_NEW          = "canNew";
    public static final String AR_CYCLE_USER_PERMISSIONS_RFC          = "canRFC";
    public static final String AR_CYCLE_USER_PERMISSIONS_NEW_AND_RFC  = "canNewAndRfc";
}
