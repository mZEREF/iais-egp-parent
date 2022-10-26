package sg.gov.moh.iais.egp.bsb.constant;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MasterCodeConstants {
    private MasterCodeConstants() {}

    public static final String YES = "Y";
    public static final String NO = "N";
    public static final String YES_UPPER_CASE = "YES";
    public static final String NO_UPPER_CASE = "NO";
    public static final String YES_CAPITALIZED = "Yes";
    public static final String NO_CAPITALIZED = "No";

    public static String displayYesNo(String value) {
        String display = null;
        if (YES.equals(value)) {
            display = YES_CAPITALIZED;
        } else if (NO.equals(value)) {
            display = NO_CAPITALIZED;
        }
        return display;
    }

    public static final String FIRST_SCHEDULE_PART_1 = "SCHTYPE011";
    public static final String FIRST_SCHEDULE_PART_2 = "SCHTYPE012";
    public static final String SECOND_SCHEDULE = "SCHTYPE020";
    public static final String THIRD_SCHEDULE = "SCHTYPE030";
    public static final String FOURTH_SCHEDULE = "SCHTYPE040";
    public static final String FIFTH_SCHEDULE = "SCHTYPE050";

    public static final String APP_TYPE_NEW = "BSBAPTY001";
    public static final String APP_TYPE_RENEW = "BSBAPTY002";
    public static final String APP_TYPE_RFC = "BSBAPTY003";
    public static final String APP_TYPE_CANCEL = "BSBAPTY004";
    public static final String APP_TYPE_DEREGISTRATION = "BSBAPTY005";
    public static final String APP_TYPE_REVOCATION = "BSBAPTY006";
    public static final String APP_TYPE_SUSPEND = "BSBAPTY007";
    public static final String APP_TYPE_REINST = "BSBAPTY008";
    public static final String APP_TYPE_WITHDRAW = "BSBAPTY009";
    public static final String APP_TYPE_SUBMISSION = "BSBAPTY010";
    public static final String APP_TYPE_RFI        = "BSBAPTY012";
    public static final String APP_TYPE_REPORTABLE_EVENT = "BSBAPTY013";

    // App Status
    // main
    public static final String APP_STATUS_REMOVED                                   = "BSBAPST000";
    public static final String APP_STATUS_DRAFT                                     = "BSBAPST001";
    public static final String APP_STATUS_APPROVED                                  = "BSBAPST002";
    public static final String APP_STATUS_ACCEPTED                                  = "BSBAPST003";
    public static final String APP_STATUS_PARTIAL_ACCEPTANCE                        = "BSBAPST004";
    public static final String APP_STATUS_VERIFIED                                  = "BSBAPST005";
    public static final String APP_STATUS_WITHDRAWN                                 = "BSBAPST006";
    public static final String APP_STATUS_REJECTED                                  = "BSBAPST007";
    public static final String APP_STATUS_PEND_APPLICANT_CLARIFICATION              = "BSBAPST008";
    public static final String APP_STATUS_PEND_APPLICANT_INPUT                      = "BSBAPST009";
    public static final String APP_STATUS_PEND_INSPECTION_CERTIFICATION             = "BSBAPST010"; // DO
    // screening
    public static final String APP_STATUS_PEND_DO_SCREENING                         = "BSBAPST020"; // DO
    public static final String APP_STATUS_PEND_DO_CLARIFICATION                     = "BSBAPST021"; // DO
    public static final String APP_STATUS_PEND_AO_SCREENING                         = "BSBAPST022"; // AO
    public static final String APP_STATUS_PEND_HM_DECISION                          = "BSBAPST023"; // HM
    // processing
    public static final String APP_STATUS_PEND_DO_RECOMMENDATION                    = "BSBAPST030"; // DO
    public static final String APP_STATUS_PEND_AO_APPROVAL                          = "BSBAPST031"; // AO
    public static final String APP_STATUS_PEND_HM_APPROVAL                          = "BSBAPST032"; // HM
    public static final String APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT             = "BSBAPST033"; // DO
    public static final String APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW            = "BSBAPST034"; // AO
    public static final String APP_STATUS_PEND_DO_VERIFICATION                      = "BSBAPST035"; // DO
    // pre-inspection
    public static final String APP_STATUS_PEND_INSPECTION_TASK_ASSIGNMENT           = "BSBAPST100"; // DO
    public static final String APP_STATUS_PEND_CHECKLIST_SUBMISSION                 = "BSBAPST101"; // DO
    public static final String APP_STATUS_PEND_APPOINTMENT_SCHEDULING               = "BSBAPST102"; // DO
    public static final String APP_STATUS_PEND_APPOINTMENT_CONFIRMATION             = "BSBAPST103"; // DO
    public static final String APP_STATUS_PEND_INSPECTION_READINESS                 = "BSBAPST104"; // DO
    // on-site inspection
    public static final String APP_STATUS_PEND_INSPECTION                           = "BSBAPST200"; // DO
    // post-inspection(report)
    public static final String APP_STATUS_PEND_INSPECTION_REPORT                    = "BSBAPST300"; // AO
    public static final String APP_STATUS_PEND_INSPECTION_REPORT_REVIEW             = "BSBAPST301"; // AO
    public static final String APP_STATUS_PEND_INSPECTION_REPORT_REVISION           = "BSBAPST302"; // DO
    public static final String APP_STATUS_PEND_DO_REPORT_APPROVAL                   = "BSBAPST303"; // DO
    public static final String APP_STATUS_PEND_AO_REPORT_APPROVAL                   = "BSBAPST304"; // AO
    public static final String APP_STATUS_PEND_HM_REPORT_APPROVAL                   = "BSBAPST305"; // HM
    public static final String APP_STATUS_PEND_DO_CLARIFICATION_OF_REPORT_APPROVAL  = "BSBAPST306"; // DO
    // post-inspection(Non-Compliance)
    public static final String APP_STATUS_PEND_NC_RECTIFICATION                     = "BSBAPST310"; // Applicant
    public static final String APP_STATUS_PEND_NC_RECTIFICATION_CLARIFICATION       = "BSBAPST311"; // Applicant
    public static final String APP_STATUS_PEND_NC_NOTIFICATION_EMAIL                = "BSBAPST312"; // DO
    public static final String APP_STATUS_PEND_NC_NOTIFICATION_EMAIL_REVIEW         = "BSBAPST313"; // AO
    public static final String APP_STATUS_PEND_DO_RECTIFICATION_REVIEW              = "BSBAPST314"; // DO
    public static final String APP_STATUS_PEND_AO_RECTIFICATION_REVIEW              = "BSBAPST315"; // AO
    public static final String APP_STATUS_PEND_DO_CLARIFICATION_OF_NC_REVIEW        = "BSBAPST316"; // DO
    // post-inspection(Follow-Up)
    public static final String APP_STATUS_PEND_EXTENSION_REVIEW                     = "BSBAPST320"; // DO
    public static final String APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION            = "BSBAPST321"; // Applicant
    public static final String APP_STATUS_PEND_DO_FOLLOW_UP_ITEM_VERIFICATION       = "BSBAPST322"; // DO
    public static final String APP_STATUS_PEND_AO_FOLLOW_UP_ITEM_VERIFICATION       = "BSBAPST323"; // AO
    // certification
    public static final String APP_STATUS_PEND_AFC_REPORT_UPLOAD                    = "BSBAPST400"; // Applicant
    public static final String APP_STATUS_PEND_AFC_SELECTION                        = "BSBAPST401"; // Applicant
    public static final String APP_STATUS_PEND_AFC_SELECTION_REVIEW                 = "BSBAPST402"; // DO
    public static final String APP_STATUS_PEND_DO_REPORT_REVIEW                     = "BSBAPST403"; // DO
    public static final String APP_STATUS_PEND_AO_REPORT_REVIEW                     = "BSBAPST404"; // AO
    public static final String APP_STATUS_PEND_AFC_INPUT                            = "BSBAPST405"; // Applicant
    // defer renew
    public static final String APP_STATUS_PEND_DO_EXTENSION_APPROVAL                = "BSBAPST501";
    public static final String APP_STATUS_PEND_AO_EXTENSION_APPROVAL                = "BSBAPST502";
    public static final String APP_STATUS_PEND_HM_EXTENSION_APPROVAL                = "BSBAPST503";

    public static final Set<String> DO_QUERY_PENDING_DO_CLARIFICATION_APP_STATUS;
    public static final Set<String> AO_QUERY_APP_STATUS;
    public static final Set<String> HM_QUERY_APP_STATUS;
    public static final Set<String> TASK_LIST_QUERY_APP_TYPE;
    public static final Set<String> TASK_POOL_QUERY_APP_TYPE;
    public static final Set<String> INSPECTION_APP_STATUS;

    // not an actual app status, this is a compound status
    public static final String DO_QUERY_PENDING_DO_CLARIFICATION                   = "Pending DO Clarification";

    static {
        Set<String> doQueryPendingDOClarificationAppStatus = Sets.newLinkedHashSetWithExpectedSize(3);
        doQueryPendingDOClarificationAppStatus.add(APP_STATUS_PEND_DO_CLARIFICATION);
        doQueryPendingDOClarificationAppStatus.add(APP_STATUS_PEND_DO_CLARIFICATION_OF_REPORT_APPROVAL);
        doQueryPendingDOClarificationAppStatus.add(APP_STATUS_PEND_DO_CLARIFICATION_OF_NC_REVIEW);
        DO_QUERY_PENDING_DO_CLARIFICATION_APP_STATUS = Collections.unmodifiableSet(doQueryPendingDOClarificationAppStatus);

        Set<String> aoQueryAppStatus = Sets.newLinkedHashSetWithExpectedSize(10);
        // BE don't add 'Draft', because BE DB don't save it
        aoQueryAppStatus.add(APP_STATUS_PEND_AO_SCREENING);
        aoQueryAppStatus.add(APP_STATUS_PEND_AO_APPROVAL);
        aoQueryAppStatus.add(APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW);
        aoQueryAppStatus.add(APP_STATUS_PEND_INSPECTION_REPORT);
        aoQueryAppStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVIEW);
        aoQueryAppStatus.add(APP_STATUS_PEND_AO_REPORT_APPROVAL);
        aoQueryAppStatus.add(APP_STATUS_PEND_NC_NOTIFICATION_EMAIL_REVIEW);
        aoQueryAppStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        aoQueryAppStatus.add(APP_STATUS_PEND_AO_FOLLOW_UP_ITEM_VERIFICATION);
        aoQueryAppStatus.add(APP_STATUS_PEND_AO_REPORT_REVIEW);
        AO_QUERY_APP_STATUS = Collections.unmodifiableSet(aoQueryAppStatus);

        Set<String> hmQueryAppStatus = Sets.newLinkedHashSetWithExpectedSize(3);
        // BE don't add 'Draft', because BE DB don't save it
        hmQueryAppStatus.add(APP_STATUS_PEND_HM_DECISION);
        hmQueryAppStatus.add(APP_STATUS_PEND_HM_APPROVAL);
        hmQueryAppStatus.add(APP_STATUS_PEND_HM_REPORT_APPROVAL);
        HM_QUERY_APP_STATUS = Collections.unmodifiableSet(hmQueryAppStatus);

        Set<String> taskListQueryAppType = Sets.newLinkedHashSetWithExpectedSize(10);
        // BE don't add 'Draft', because BE DB don't save it
        taskListQueryAppType.add(APP_TYPE_NEW);
        taskListQueryAppType.add(APP_TYPE_RENEW);
        taskListQueryAppType.add(APP_TYPE_RFC);
        taskListQueryAppType.add(APP_TYPE_DEREGISTRATION);
        taskListQueryAppType.add(APP_TYPE_SUBMISSION);
        taskListQueryAppType.add(APP_TYPE_CANCEL);
        taskListQueryAppType.add(APP_TYPE_WITHDRAW);
        taskListQueryAppType.add(APP_TYPE_SUSPEND);
        taskListQueryAppType.add(APP_TYPE_REVOCATION);
        taskListQueryAppType.add(APP_TYPE_REPORTABLE_EVENT);
        TASK_LIST_QUERY_APP_TYPE = Collections.unmodifiableSet(taskListQueryAppType);

        Set<String> taskPoolQueryAppType = Sets.newLinkedHashSetWithExpectedSize(10);
        // BE don't add 'Draft', because BE DB don't save it
        taskPoolQueryAppType.add(APP_TYPE_NEW);
        taskPoolQueryAppType.add(APP_TYPE_RENEW);
        taskPoolQueryAppType.add(APP_TYPE_RFC);
        taskPoolQueryAppType.add(APP_TYPE_CANCEL);
        taskPoolQueryAppType.add(APP_TYPE_DEREGISTRATION);
        taskPoolQueryAppType.add(APP_TYPE_WITHDRAW);
        TASK_POOL_QUERY_APP_TYPE = Collections.unmodifiableSet(taskPoolQueryAppType);

        Set<String> inspectionAppStatus = Sets.newHashSetWithExpectedSize(22);
        inspectionAppStatus.add(APP_STATUS_PEND_INSPECTION_TASK_ASSIGNMENT);
        inspectionAppStatus.add(APP_STATUS_PEND_CHECKLIST_SUBMISSION);
        inspectionAppStatus.add(APP_STATUS_PEND_APPOINTMENT_SCHEDULING);
        inspectionAppStatus.add(APP_STATUS_PEND_APPOINTMENT_CONFIRMATION);
        inspectionAppStatus.add(APP_STATUS_PEND_INSPECTION_READINESS);
        inspectionAppStatus.add(APP_STATUS_PEND_INSPECTION);
        inspectionAppStatus.add(APP_STATUS_PEND_INSPECTION_REPORT);
        inspectionAppStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVISION);
        inspectionAppStatus.add(APP_STATUS_PEND_DO_REPORT_APPROVAL);
        inspectionAppStatus.add(APP_STATUS_PEND_AO_REPORT_APPROVAL);
        inspectionAppStatus.add(APP_STATUS_PEND_HM_REPORT_APPROVAL);
        inspectionAppStatus.add(APP_STATUS_PEND_DO_CLARIFICATION_OF_REPORT_APPROVAL);
        inspectionAppStatus.add(APP_STATUS_PEND_NC_RECTIFICATION);
        inspectionAppStatus.add(APP_STATUS_PEND_NC_RECTIFICATION_CLARIFICATION);
        inspectionAppStatus.add(APP_STATUS_PEND_NC_NOTIFICATION_EMAIL);
        inspectionAppStatus.add(APP_STATUS_PEND_NC_NOTIFICATION_EMAIL_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_DO_CLARIFICATION_OF_NC_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_EXTENSION_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION);
        inspectionAppStatus.add(APP_STATUS_PEND_DO_FOLLOW_UP_ITEM_VERIFICATION);
        inspectionAppStatus.add(APP_STATUS_PEND_AO_FOLLOW_UP_ITEM_VERIFICATION);
        INSPECTION_APP_STATUS = Collections.unmodifiableSet(inspectionAppStatus);
    }

    public static final List<SelectOption> DO_SEARCH_APP_STATUS_SELECT_OPTION;

    static {
        List<SelectOption> doSearchAppStatusSelectOption = new ArrayList<>(22);
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_INSPECTION_CERTIFICATION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_INSPECTION_CERTIFICATION)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_DO_SCREENING, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_SCREENING)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_APPLICANT_INPUT, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_APPLICANT_INPUT)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_CHECKLIST_SUBMISSION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_CHECKLIST_SUBMISSION)));
        doSearchAppStatusSelectOption.add(new SelectOption(DO_QUERY_PENDING_DO_CLARIFICATION, DO_QUERY_PENDING_DO_CLARIFICATION));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_DO_RECOMMENDATION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_RECOMMENDATION)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_DO_VERIFICATION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_VERIFICATION)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_INSPECTION_TASK_ASSIGNMENT, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_INSPECTION_TASK_ASSIGNMENT)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_CHECKLIST_SUBMISSION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_CHECKLIST_SUBMISSION)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_APPOINTMENT_SCHEDULING, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_APPOINTMENT_SCHEDULING)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_APPOINTMENT_CONFIRMATION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_APPOINTMENT_CONFIRMATION)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_INSPECTION_READINESS, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_INSPECTION_READINESS)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_INSPECTION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_INSPECTION)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_INSPECTION_REPORT_REVISION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_INSPECTION_REPORT_REVISION)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_DO_REPORT_APPROVAL, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_REPORT_APPROVAL)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_NC_NOTIFICATION_EMAIL, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_NC_NOTIFICATION_EMAIL)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_EXTENSION_REVIEW, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_EXTENSION_REVIEW)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_DO_FOLLOW_UP_ITEM_VERIFICATION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_FOLLOW_UP_ITEM_VERIFICATION)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_AFC_SELECTION_REVIEW, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_AFC_SELECTION_REVIEW)));
        doSearchAppStatusSelectOption.add(new SelectOption(APP_STATUS_PEND_DO_REPORT_REVIEW, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_REPORT_REVIEW)));
        DO_SEARCH_APP_STATUS_SELECT_OPTION = Collections.unmodifiableList(doSearchAppStatusSelectOption);
    }

    public static final String PROCESS_TYPE_FAC_REG = "PROTYPE001";
    public static final String PROCESS_TYPE_APPROVE_POSSESS = "PROTYPE002";
    public static final String PROCESS_TYPE_APPROVE_LSP = "PROTYPE003";
    public static final String PROCESS_TYPE_SP_APPROVE_HANDLE = "PROTYPE004";
    public static final String PROCESS_TYPE_FAC_CERTIFIER_REG = "PROTYPE005";
    public static final String PROCESS_TYPE_DATA_SUBMISSION = "PROTYPE006";
    public static final String PROCESS_TYPE_AUDIT = "PROTYPE007";
    public static final String PROCESS_TYPE_INCIDENT_NOTIFICATION = "PROTYPE008";
    public static final String PROCESS_TYPE_INVESTIGATION_REPORT = "PROTYPE009";
    public static final String PROCESS_TYPE_FOLLOW_UP_REPORT_1A = "PROTYPE010";
    public static final String PROCESS_TYPE_FOLLOW_UP_REPORT_1B = "PROTYPE011";
    public static final String PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE = "PROTYPE012";
    public static final String PROCESS_TYPE_ADHOC_RFI = "PROTYPE013";
    public static final String PROCESS_TYPE_INSPECTION_FOLLOW_UP = "PROTYPE014";
    public static final String PROCESS_TYPE_RENEW_DEFER = "PROTYPE015";
    public static final String PROCESS_TYPE_ADHOC_INSPECTION = "PROTYPE016";
    public static final Set<String> APPLY_APPROVE_PROCESS_TYPES;
    public static final Set<String> COMMON_QUERY_APP_SUB_TYPE;
    public static final Set<String> MOH_PROCESS_FACILITY_PROCESS_TYPE;
    public static final Set<String> MOH_PROCESS_BAT_PROCESS_TYPE;

    static {
        Set<String> applyApprovalProcessType = Sets.newHashSetWithExpectedSize(4);
        applyApprovalProcessType.add(PROCESS_TYPE_APPROVE_POSSESS);
        applyApprovalProcessType.add(PROCESS_TYPE_APPROVE_LSP);
        applyApprovalProcessType.add(PROCESS_TYPE_SP_APPROVE_HANDLE);
        applyApprovalProcessType.add(PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE);
        APPLY_APPROVE_PROCESS_TYPES = Collections.unmodifiableSet(applyApprovalProcessType);

        Set<String> commonQueryAppSubType = Sets.newLinkedHashSetWithExpectedSize(7);
        // BE don't add 'Draft', because BE DB don't save it
        commonQueryAppSubType.add(PROCESS_TYPE_FAC_REG);
        commonQueryAppSubType.add(PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE);
        commonQueryAppSubType.add(PROCESS_TYPE_APPROVE_POSSESS);
        commonQueryAppSubType.add(PROCESS_TYPE_APPROVE_LSP);
        commonQueryAppSubType.add(PROCESS_TYPE_SP_APPROVE_HANDLE);
        commonQueryAppSubType.add(PROCESS_TYPE_FAC_CERTIFIER_REG);
        commonQueryAppSubType.add(PROCESS_TYPE_DATA_SUBMISSION);
        COMMON_QUERY_APP_SUB_TYPE = Collections.unmodifiableSet(commonQueryAppSubType);

        Set<String> mohProcessFacilityProcessType = Sets.newHashSetWithExpectedSize(2);
        mohProcessFacilityProcessType.add(PROCESS_TYPE_FAC_REG);
        mohProcessFacilityProcessType.add(PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE);
        MOH_PROCESS_FACILITY_PROCESS_TYPE = Collections.unmodifiableSet(mohProcessFacilityProcessType);

        Set<String> mohProcessBatProcessType = Sets.newHashSetWithExpectedSize(3);
        mohProcessBatProcessType.add(PROCESS_TYPE_APPROVE_POSSESS);
        mohProcessBatProcessType.add(PROCESS_TYPE_APPROVE_LSP);
        mohProcessBatProcessType.add(PROCESS_TYPE_SP_APPROVE_HANDLE);
        MOH_PROCESS_BAT_PROCESS_TYPE = Collections.unmodifiableSet(mohProcessBatProcessType);
    }

    public static final String FAC_CLASSIFICATION_BSL3 = "FACCLA001";
    public static final String FAC_CLASSIFICATION_BSL4 = "FACCLA002";
    public static final String FAC_CLASSIFICATION_UF = "FACCLA003";
    public static final String FAC_CLASSIFICATION_LSPF = "FACCLA004";
    public static final String FAC_CLASSIFICATION_RF = "FACCLA005";
    public static final Set<String> VALID_FAC_CLASSIFICATION;
    public static final Set<String> CERTIFIED_CLASSIFICATION;
    public static final Set<String> UNCERTIFIED_CLASSIFICATION;
    public static final List<SelectOption> FACILITY_MANAGEMENT_SEARCH_FAC_CLASSIFICATION;
    public static final List<SelectOption> ADHOC_INSPECTION_SEARCH_FAC_CLASSIFICATION;

    static {
        Set<String> certifiedClassification = Sets.newHashSetWithExpectedSize(2);
        certifiedClassification.add(FAC_CLASSIFICATION_BSL3);
        certifiedClassification.add(FAC_CLASSIFICATION_BSL4);
        CERTIFIED_CLASSIFICATION = Collections.unmodifiableSet(certifiedClassification);

        Set<String> uncertifiedClassification = Sets.newHashSetWithExpectedSize(2);
        uncertifiedClassification.add(FAC_CLASSIFICATION_UF);
        uncertifiedClassification.add(FAC_CLASSIFICATION_LSPF);
        UNCERTIFIED_CLASSIFICATION = Collections.unmodifiableSet(uncertifiedClassification);

        Set<String> facClassification = Sets.newHashSetWithExpectedSize(5);
        facClassification.add(FAC_CLASSIFICATION_BSL3);
        facClassification.add(FAC_CLASSIFICATION_BSL4);
        facClassification.add(FAC_CLASSIFICATION_UF);
        facClassification.add(FAC_CLASSIFICATION_LSPF);
        facClassification.add(FAC_CLASSIFICATION_RF);
        VALID_FAC_CLASSIFICATION = Collections.unmodifiableSet(facClassification);

        List<SelectOption> facilityManagementSearchFacClassifications = new ArrayList<>(5);
        facilityManagementSearchFacClassifications.add(new SelectOption(FAC_CLASSIFICATION_BSL3, MasterCodeUtil.getCodeDesc(FAC_CLASSIFICATION_BSL3)));
        facilityManagementSearchFacClassifications.add(new SelectOption(FAC_CLASSIFICATION_BSL4, MasterCodeUtil.getCodeDesc(FAC_CLASSIFICATION_BSL4)));
        facilityManagementSearchFacClassifications.add(new SelectOption(FAC_CLASSIFICATION_UF, MasterCodeUtil.getCodeDesc(FAC_CLASSIFICATION_UF)));
        facilityManagementSearchFacClassifications.add(new SelectOption(FAC_CLASSIFICATION_LSPF, MasterCodeUtil.getCodeDesc(FAC_CLASSIFICATION_LSPF)));
        facilityManagementSearchFacClassifications.add(new SelectOption(FAC_CLASSIFICATION_RF, MasterCodeUtil.getCodeDesc(FAC_CLASSIFICATION_RF)));
        FACILITY_MANAGEMENT_SEARCH_FAC_CLASSIFICATION = Collections.unmodifiableList(facilityManagementSearchFacClassifications);

        List<SelectOption> adhocInspectionSearchFacClassifications = new ArrayList<>(4);
        adhocInspectionSearchFacClassifications.add(new SelectOption(FAC_CLASSIFICATION_BSL3, MasterCodeUtil.getCodeDesc(FAC_CLASSIFICATION_BSL3)));
        adhocInspectionSearchFacClassifications.add(new SelectOption(FAC_CLASSIFICATION_BSL4, MasterCodeUtil.getCodeDesc(FAC_CLASSIFICATION_BSL4)));
        adhocInspectionSearchFacClassifications.add(new SelectOption(FAC_CLASSIFICATION_UF, MasterCodeUtil.getCodeDesc(FAC_CLASSIFICATION_UF)));
        adhocInspectionSearchFacClassifications.add(new SelectOption(FAC_CLASSIFICATION_LSPF, MasterCodeUtil.getCodeDesc(FAC_CLASSIFICATION_LSPF)));
        ADHOC_INSPECTION_SEARCH_FAC_CLASSIFICATION = Collections.unmodifiableList(adhocInspectionSearchFacClassifications);
    }

    public static final String ACTIVITY_POSSESS_FIRST_SCHEDULE = "ACTVITY001";
    public static final String ACTIVITY_POSSESS_SECOND_SCHEDULE = "ACTVITY002";
    public static final String ACTIVITY_POSSESS_THIRD_SCHEDULE = "ACTVITY003";
    public static final String ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE = "ACTVITY004";
    public static final String ACTIVITY_POSSESS_FIFTH_SCHEDULE = "ACTVITY005";
    public static final String ACTIVITY_LSP_FIRST_SCHEDULE = "ACTVITY011";
    public static final String ACTIVITY_LSP_THIRD_SCHEDULE = "ACTVITY012";
    public static final String ACTIVITY_LSP_FIRST_THIRD_SCHEDULE = "ACTVITY013";
    public static final String ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED = "ACTVITY021";
    public static final String ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV = "ACTVITY022";
    public static final String ACTIVITY_SP_HANDLE_PV_POTENTIAL = "ACTVITY023";

    public static final Set<String> VALID_BLS3_ACTIVITIES;
    public static final Set<String> VALID_BLS4_ACTIVITIES;
    public static final Set<String> VALID_UF_ACTIVITIES;
    public static final Set<String> VALID_LSPF_ACTIVITIES;
    public static final Set<String> VALID_RF_ACTIVITIES;
    public static final List<SelectOption> FACILITY_MANAGEMENT_SEARCH_ACTIVITY_TYPE;
    public static final List<SelectOption> ADHOC_INSPECTION_SEARCH_ACTIVITY_TYPE;

    static {
        Set<String> bls3And4Set = Sets.newHashSetWithExpectedSize(5);
        bls3And4Set.add(ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE);
        bls3And4Set.add(ACTIVITY_POSSESS_FIFTH_SCHEDULE);
        bls3And4Set.add(ACTIVITY_LSP_FIRST_THIRD_SCHEDULE);
        bls3And4Set.add(ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV);
        VALID_BLS3_ACTIVITIES = Collections.unmodifiableSet(bls3And4Set);
        VALID_BLS4_ACTIVITIES = Collections.unmodifiableSet(bls3And4Set);

        Set<String> ufAndLspfSet = Sets.newHashSetWithExpectedSize(3);
        ufAndLspfSet.add(ACTIVITY_POSSESS_FIRST_SCHEDULE);
        ufAndLspfSet.add(ACTIVITY_POSSESS_FIFTH_SCHEDULE);
        ufAndLspfSet.add(ACTIVITY_LSP_FIRST_THIRD_SCHEDULE);
        VALID_UF_ACTIVITIES = Collections.unmodifiableSet(ufAndLspfSet);
        VALID_LSPF_ACTIVITIES = Collections.unmodifiableSet(ufAndLspfSet);

        Set<String> rfSet = Sets.newHashSetWithExpectedSize(2);
        rfSet.add(ACTIVITY_SP_HANDLE_PV_POTENTIAL);
        rfSet.add(ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED);
        VALID_RF_ACTIVITIES = Collections.unmodifiableSet(rfSet);

        List<SelectOption> facilityManagementSearchActivityTypes = new ArrayList<>(7);
        facilityManagementSearchActivityTypes.add(new SelectOption(ACTIVITY_POSSESS_FIRST_SCHEDULE, MasterCodeUtil.getCodeDesc(ACTIVITY_POSSESS_FIRST_SCHEDULE)));
        facilityManagementSearchActivityTypes.add(new SelectOption(ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE, MasterCodeUtil.getCodeDesc(ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE)));
        facilityManagementSearchActivityTypes.add(new SelectOption(ACTIVITY_POSSESS_FIFTH_SCHEDULE, MasterCodeUtil.getCodeDesc(ACTIVITY_POSSESS_FIFTH_SCHEDULE)));
        facilityManagementSearchActivityTypes.add(new SelectOption(ACTIVITY_LSP_FIRST_THIRD_SCHEDULE, MasterCodeUtil.getCodeDesc(ACTIVITY_LSP_FIRST_THIRD_SCHEDULE)));
        facilityManagementSearchActivityTypes.add(new SelectOption(ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV, MasterCodeUtil.getCodeDesc(ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV)));
        facilityManagementSearchActivityTypes.add(new SelectOption(ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED, MasterCodeUtil.getCodeDesc(ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED)));
        facilityManagementSearchActivityTypes.add(new SelectOption(ACTIVITY_SP_HANDLE_PV_POTENTIAL, MasterCodeUtil.getCodeDesc(ACTIVITY_SP_HANDLE_PV_POTENTIAL)));
        FACILITY_MANAGEMENT_SEARCH_ACTIVITY_TYPE = Collections.unmodifiableList(facilityManagementSearchActivityTypes);

        List<SelectOption> adhocInspectionSearchActivityTypes = new ArrayList<>(5);
        adhocInspectionSearchActivityTypes.add(new SelectOption(ACTIVITY_POSSESS_FIRST_SCHEDULE, MasterCodeUtil.getCodeDesc(ACTIVITY_POSSESS_FIRST_SCHEDULE)));
        adhocInspectionSearchActivityTypes.add(new SelectOption(ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE, MasterCodeUtil.getCodeDesc(ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE)));
        adhocInspectionSearchActivityTypes.add(new SelectOption(ACTIVITY_POSSESS_FIFTH_SCHEDULE, MasterCodeUtil.getCodeDesc(ACTIVITY_POSSESS_FIFTH_SCHEDULE)));
        adhocInspectionSearchActivityTypes.add(new SelectOption(ACTIVITY_LSP_FIRST_THIRD_SCHEDULE, MasterCodeUtil.getCodeDesc(ACTIVITY_LSP_FIRST_THIRD_SCHEDULE)));
        adhocInspectionSearchActivityTypes.add(new SelectOption(ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV, MasterCodeUtil.getCodeDesc(ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV)));
        ADHOC_INSPECTION_SEARCH_ACTIVITY_TYPE = Collections.unmodifiableList(adhocInspectionSearchActivityTypes);
    }

    public static final String FACILITY_TYPE_ACADEMIC = "FACTYPE001";
    public static final String FACILITY_TYPE_CLINICAL = "FACTYPE002";
    public static final String FACILITY_TYPE_COMMERCIAL = "FACTYPE003";
    public static final String FACILITY_TYPE_GOVERNMENT = "FACTYPE004";
    public static final String FACILITY_TYPE_OTHERS = "FACTYPE005";

    public static final String ADMIN_TYPE_MAIN = "main";
    public static final String ADMIN_TYPE_ALTERNATIVE = "alter";

    public static final String SAMPLE_NATURE_CULTURE_ISOLATE = "BNOTS001";
    public static final String SAMPLE_NATURE_PURE_TOXIN = "BNOTS002";
    public static final String SAMPLE_NATURE_CLINICAL = "BNOTS003";
    public static final String SAMPLE_NATURE_ANIMAL = "BNOTS004";
    public static final String SAMPLE_NATURE_ENVIRONMENTAL = "BNOTS005";
    public static final String SAMPLE_NATURE_FOOD = "BNOTS006";
    public static final String SAMPLE_NATURE_OTHER = "BNOTS007";

    public static final String ENTITY_STATUS_PROCESSING = "BSBCENS001";
    public static final String ENTITY_STATUS_REJECTED = "BSBCENS002";
    public static final String ENTITY_STATUS_IN_USE = "BSBCENS003";
    public static final String ENTITY_STATUS_DEPRECATED = "BSBCENS004";

    public static final String APPROVAL_TYPE_POSSESS = "APPRTY001";
    public static final String APPROVAL_TYPE_LSP = "APPRTY002";
    public static final String APPROVAL_TYPE_SP_HANDLE = "APPRTY003";
    public static final String APPROVAL_TYPE_HANDLE_FST_EXEMPTED = "APPRTY005";
    public static final String APPROVAL_TYPE_ACTIVITY = "APPRTY010";
    /* Main approval types mean: main BAT approval types, now only has three */
    public static final Set<String> BAT_MAIN_APPROVAL_TYPES;
    public static final Set<String> BAT_APPROVAL_TYPES;

    static {
        Set<String> applyApprovalTypes = Sets.newHashSetWithExpectedSize(4);
        applyApprovalTypes.add(APPROVAL_TYPE_POSSESS);
        applyApprovalTypes.add(APPROVAL_TYPE_LSP);
        applyApprovalTypes.add(APPROVAL_TYPE_SP_HANDLE);
        applyApprovalTypes.add(APPROVAL_TYPE_HANDLE_FST_EXEMPTED);
        BAT_APPROVAL_TYPES = Collections.unmodifiableSet(applyApprovalTypes);

        applyApprovalTypes = new HashSet<>(applyApprovalTypes);
        applyApprovalTypes.remove(APPROVAL_TYPE_HANDLE_FST_EXEMPTED);
        BAT_MAIN_APPROVAL_TYPES = Collections.unmodifiableSet(applyApprovalTypes);
    }

    public static final String APPROVAL_STATUS_ACTIVE = "APPRSTA001";
    public static final String APPROVAL_STATUS_CANCELLED = "APPRSTA002";
    public static final String APPROVAL_STATUS_REVOKED = "APPRSTA003";
    public static final String APPROVAL_STATUS_SUSPENDED = "APPRSTA004";
    public static final String APPROVAL_STATUS_EXPIRED = "APPRSTA005";
    public static final String APPROVAL_STATUS_SUSPENDED_NC = "APPRSTA006";
    public static final String APPROVAL_STATUS_SUSPENDED_OTHERS = "APPRSTA007";
    public static final String APPROVAL_STATUS_SUSPENDED_PENDING_INVESTIGATION = "APPRSTA008";
    public static final String APPROVAL_STATUS_SUSPENDED_CONDITIONAL_INVENTORY_MOVEMENT = "APPRSTA009";
    public static final String APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL = "APPRSTA010";
    public static final String APPROVAL_STATUS_DEREGISTERED = "APPRSTA011";

    public static final List<SelectOption> FACILITY_MANAGEMENT_SEARCH_FACILITY_STATUS;
    public static final List<SelectOption> ADHOC_INSPECTION_SEARCH_FACILITY_STATUS;
    static {
        List<SelectOption> facilityManagementSearchFacilityStatus = new ArrayList<>(9);
        facilityManagementSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_ACTIVE, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_ACTIVE)));
        facilityManagementSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_EXPIRED, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_EXPIRED)));
        facilityManagementSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_DEREGISTERED, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_DEREGISTERED)));
        facilityManagementSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_NC, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_NC)));
        facilityManagementSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL)));
        facilityManagementSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_OTHERS, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_OTHERS)));
        facilityManagementSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_CONDITIONAL_INVENTORY_MOVEMENT, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_CONDITIONAL_INVENTORY_MOVEMENT)));
        facilityManagementSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_PENDING_INVESTIGATION, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_PENDING_INVESTIGATION)));
        facilityManagementSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_REVOKED, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_REVOKED)));
        FACILITY_MANAGEMENT_SEARCH_FACILITY_STATUS = Collections.unmodifiableList(facilityManagementSearchFacilityStatus);

        List<SelectOption> adhocInspectionSearchFacilityStatus = new ArrayList<>(2);
        adhocInspectionSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_ACTIVE, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_ACTIVE)));
        adhocInspectionSearchFacilityStatus.add(new SelectOption(APPROVAL_STATUS_SUSPENDED, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED)));
        ADHOC_INSPECTION_SEARCH_FACILITY_STATUS = Collections.unmodifiableList(adhocInspectionSearchFacilityStatus);
    }

    //BE DECISION VALUE
    public static final String MOH_PROCESS_DECISION_SCREENED_BY_DO_PROCEED_TO_NEXT_STAGE                  = "MOHPRO001";
    public static final String MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION                               = "MOHPRO002";
    public static final String MOH_PROCESS_DECISION_REJECT                                                = "MOHPRO003";
    public static final String MOH_PROCESS_DECISION_APPROVE_TO_PROCEED_TO_NEXT_STAGE                      = "MOHPRO004";
    public static final String MOH_PROCESS_DECISION_ROUTE_TO_HM                                           = "MOHPRO005";
    public static final String MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO                                      = "MOHPRO006";
    public static final String MOH_PROCESS_DECISION_APPROVE                                               = "MOHPRO007";
    public static final String MOH_PROCESS_DECISION_ACCEPT                                                = "MOHPRO008";
    public static final String MOH_PROCESS_DECISION_ROUTE_TO_AO_FOR_APPROVAL                              = "MOHPRO009";
    public static final String MOH_PROCESS_DECISION_SELECT_ANOTHER_DATE                                   = "MOHPRO010";
    public static final String MOH_PROCESS_DECISION_CONFIRM_PROPOSED_DATE                                 = "MOHPRO011";
    public static final String MOH_PROCESS_DECISION_MARK_INSPECTION_TASK_AS_READY                         = "MOHPRO012";
    public static final String MOH_PROCESS_DECISION_PROCEED_TO_INSPECTION_REPORT_PREPARATION              = "MOHPRO013";
    public static final String MOH_PROCESS_DECISION_ROUTE_TO_AO_FOR_REVIEW                                = "MOHPRO014";
    public static final String MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO_FOR_REVISION                         = "MOHPRO015";
    public static final String MOH_PROCESS_DECISION_ACCEPT_AND_ROUTE_INSPECTION_REPORT_TO_APPLICANT       = "MOHPRO016";
    public static final String MOH_PROCESS_DECISION_ROUTE_BACK_TO_APPLICANT                               = "MOHPRO017";
    public static final String MOH_PROCESS_DECISION_MARK_AS_FINAL_AND_ROUTE_TO_AO                         = "MOHPRO018";
    public static final String MOH_PROCESS_DECISION_ROUTE_TO_HM_FOR_REVIEW                                = "MOHPRO019";
    public static final String MOH_PROCESS_DECISION_ROUTE_TO_APPLICANT                                    = "MOHPRO020";
    public static final String MOH_PROCESS_DECISION_APPROVE_AND_ROUTE_TO_APPLICANT                        = "MOHPRO021";
    public static final String MOH_PROCESS_DECISION_ROUTE_TO_AO                                           = "MOHPRO022";
    public static final String MOH_PROCESS_DECISION_REJECT_APPLICATION                                    = "MOHPRO024";
    public static final String MOH_PROCESS_DECISION_APPROVE_RECOMMENDATION                                = "MOHPRO025";
    public static final String MOH_PROCESS_DECISION_REJECT_RECOMMENDATION                                 = "MOHPRO026";
    public static final String MOH_PROCESS_DECISION_APPROVE_APPLICATION                                   = "MOHPRO027";
    // rfc do screen
    public static final String MOH_PROCESS_DECISION_INSPECTION_AND_CERTIFICATION                          = "MOHPRO028";

    //Reason(s) for Facility Deregistration
    public static final String REASONS_FOR_FACILITY_DEREGISTRATION_1 = "BSBRFFD001";
    public static final String REASONS_FOR_FACILITY_DEREGISTRATION_2 = "BSBRFFD002";
    public static final String REASONS_FOR_FACILITY_DEREGISTRATION_3 = "BSBRFFD003";
    public static final String REASONS_FOR_FACILITY_DEREGISTRATION_4 = "BSBRFFD004";
    public static final String REASONS_FOR_FACILITY_DEREGISTRATION_5 = "BSBRFFD005";
    public static final String REASONS_FOR_FACILITY_DEREGISTRATION_6 = "BSBRFFD006";
    public static final String REASONS_FOR_FACILITY_DEREGISTRATION_7 = "BSBRFFD007";
    public static final String REASONS_FOR_FACILITY_DEREGISTRATION_8 = "BSBRFFD008";
    //Reason(s) for Approval Cancellation
    public static final String REASONS_FOR_APPROVAL_CANCELLATION_1 = "BSBRFAC001";
    public static final String REASONS_FOR_APPROVAL_CANCELLATION_2 = "BSBRFAC002";
    public static final String REASONS_FOR_APPROVAL_CANCELLATION_3 = "BSBRFAC003";
    public static final String REASONS_FOR_APPROVAL_CANCELLATION_4 = "BSBRFAC004";
    //Reason(s) for AFC Deregistration
    public static final String REASONS_FOR_AFC_DEREGISTRATION_1 = "BSBRFAD001";
    public static final String REASONS_FOR_AFC_DEREGISTRATION_2 = "BSBRFAD002";
    public static final String REASONS_FOR_AFC_DEREGISTRATION_3 = "BSBRFAD003";
    public static final String REASONS_FOR_AFC_DEREGISTRATION_4 = "BSBRFAD004";
    public static final String REASONS_FOR_AFC_DEREGISTRATION_5 = "BSBRFAD005";

    public static final String KEY_DATA_SUBMISSION_TYPE_CONSUME = "DATTYPE001";
    public static final String KEY_DATA_SUBMISSION_TYPE_DISPOSAL = "DATTYPE002";
    public static final String KEY_DATA_SUBMISSION_TYPE_EXPORT = "DATTYPE003";
    public static final String KEY_DATA_SUBMISSION_TYPE_IMPORT = "DATTYPE004";
    public static final String KEY_DATA_SUBMISSION_TYPE_TRANSFER = "DATTYPE005";
    public static final String KEY_DATA_SUBMISSION_TYPE_RECEIPT = "DATTYPE006";
    public static final String KEY_DATA_SUBMISSION_TYPE_RED_TEAMING_REPORT = "DATTYPE007";
    public static final String KEY_DATA_SUBMISSION_TYPE_BAT_INVENTORY = "DATTYPE008";
    public static final String KEY_DATA_SUBMISSION_TYPE_REQUEST_FOR_TRANSFER = "DATTYPE009";
    public static final String KEY_DATA_SUBMISSION_ACKNOWLEDGEMENT_OF_RECEIPT_OF_TRANSFER = "DATTYPE010";

    //Mode of Procurement
    public static final String PROCUREMENT_MODE_LOCAL_TRANSFER = "BMOP001";
    public static final String PROCUREMENT_MODE_IMPORT = "BMOP002";
    public static final String PROCUREMENT_MODE_ALREADY_IN_POSSESSION = "BMOP003";
    public static final String PROCUREMENT_MODE_PURCHASE_FROM_LOCAL_SUPPLIER = "BMOP004";

    //Type of work that will be carried out involving the biological agent/toxin
    public static final String WORK_TYPE_CULTURING_ISOLATION_BAT = "BSBWT001";
    public static final String WORK_TYPE_SEROLOGICAL_TEST = "BSBWT002";
    public static final String WORK_TYPE_MOLECULAR_TEST = "BSBWT003";
    public static final String WORK_TYPE_ANIMAL_STUDIES = "BSBWT004";
    public static final String WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT = "BSBWT005";
    public static final String WORK_TYPE_OTHERS = "BSBWT006";

    // Bsb moh processing approval status
    public static final String PROCESSING_STATUS_APPROVAL = "BSBMPAS001";
    public static final String PROCESSING_STATUS_REJECT = "BSBMPAS002";

    // Inspection report Deficiency
    public static final String VALUE_DEFICIENCY_MAJOR = "BSBIRD001";
    public static final String VALUE_DEFICIENCY_MINOR = "BSBIRD002";
    public static final String VALUE_DEFICIENCY_NIL = "BSBIRD003";
    // Inspection report Outcome
    public static final String VALUE_OUTCOME_PASS = "BSBIRO001";
    public static final String VALUE_OUTCOME_PASS_WITH_CONDITION = "BSBIRO002";
    public static final String VALUE_OUTCOME_FAIL = "BSBIRO003";

    //Inspection date status
    public static final String PENDING_DO_CONFIRM = "INSDTST001";
    public static final String CONFIRMED          = "INSDTST002";

    public static final String ROLE_IN_FACILITY_MAIN_ADMINISTRATOR = "RLINFAC001";
    public static final String ROLE_IN_FACILITY_ALTERNATE_ADMINISTRATOR = "RLINFAC002";
    public static final String ROLE_IN_FACILITY_OFFICER = "RLINFAC003";
    public static final String ROLE_IN_FACILITY_OPERATOR = "RLINFAC004";
    public static final String ROLE_IN_FACILITY_COMMITTEE_MEMBER = "RLINFAC005";
    public static final String ROLE_IN_FACILITY_AUTHORISER = "RLINFAC006";


    //DOCUMENT TYPE

    // Registered Facility with Facility Activity Type Handling of poliovirus potentially infectious materials
    public static final String DOC_TYPE_RF_PV_BIO_SAFETY_MANUAL = "DOCTPV001";
    public static final String DOC_TYPE_RF_PV_DECLARATIONS = "DOCTPV002";
    public static final String DOC_TYPE_RF_PV_OTHER_SUPPORTING_DOCUMENTS = "DOCTPV003";

    //Registered Facility with Facility Activity Type Handling of Fifth Schedule toxin for exempted purposes
    public static final String DOC_TYPE_RF_ETF_BIANNUAL_REPORT = "DOCTETF001";
    public static final String DOC_TYPE_RF_ETF_BIO_SAFETY_MANUAL = "DOCTETF002";
    public static final String DOC_TYPE_RF_ETF_COMPANY_INFO = "DOCTETF003";
    public static final String DOC_TYPE_RF_ETF_DECLARATIONS = "DOCTETF004";
    public static final String DOC_TYPE_RF_ETF_OTHER_SUPPORTING_DOCUMENTS = "DOCTETF005";
    public static final String DOC_TYPE_RF_ETF_TOXIN_INVENTORY = "DOCTETF006";

    //Certified Facility
    public static final String DOC_TYPE_CF_BIO_SAFETY_COMMITTEE = "DOCTCF001";
    public static final String DOC_TYPE_CF_APPROVAL_DOCUMENT_FROM_MOH = "DOCTCF002";
    public static final String DOC_TYPE_CF_APPROVAL_DOCUMENT_FROM_OTHER_AUTHORITY = "DOCTCF003";
    public static final String DOC_TYPE_CF_AUDIT_ACTION_PLAN_OR_RESPONSE = "DOCTCF004";
    public static final String DOC_TYPE_CF_AUDIT_EMERGENCY_RESPONSE_DRILL_REPORT = "DOCTCF005";
    public static final String DOC_TYPE_CF_AUDIT_OTHER_SUPPORTING_DOCUMENTS = "DOCTCF006";
    public static final String DOC_TYPE_CF_CERTIFICATION_ACTION_PLAN_OR_RESPONSE_FOR_MOH = "DOCTCF007";
    public static final String DOC_TYPE_CF_CERTIFICATION_ACTION_PLAN_OR_RESPONSE_FOR_AFC = "DOCTCF008";
    public static final String DOC_TYPE_CF_CERTIFICATION_AFC_CERTIFICATION_REPORT_AND_SUPPORTING_DOCUMENTS = "DOCTCF009";
    public static final String DOC_TYPE_CF_CERTIFICATION_EQUIPMENT_CALIBRATION_DOCUMENTS = "DOCTCF010";
    public static final String DOC_TYPE_CF_CERTIFICATION_FACILITY_CERTIFICATE = "DOCTCF011";
    public static final String DOC_TYPE_CF_CERTIFICATION_FACILITY_SELF_INSPECTION= "DOCTCF012";
    public static final String DOC_TYPE_CF_CERTIFICATION_FACILITY_SELF_INSPECTION_SUPPORTING_DOCUMENTS  = "DOCTCF013";
    public static final String DOC_TYPE_CF_CERTIFICATION_OTHER_SUPPORTING_DOCUMENTS = "DOCTCF014";
    public static final String DOC_TYPE_CF_CERTIFICATION_TIME_LINE = "DOCTCF015";
    public static final String DOC_TYPE_CF_FACILITY_COMPANY_INFORMATION = "DOCTCF016";
    public static final String DOC_TYPE_CF_FACILITY_DECLARATION_BY_FACILITY_OPERATOR = "DOCTCF017";
    public static final String DOC_TYPE_CF_FACILITY_GAZETTE_ORDER = "DOCTCF018";
    public static final String DOC_TYPE_CF_FACILITY_INVENTORY_TEMPLATE = "DOCTCF019";
    public static final String DOC_TYPE_CF_FACILITY_OTHER_DECLARATIONS = "DOCTCF020";
    public static final String DOC_TYPE_CF_FACILITY_OTHER_SUPPORTING_DOCUMENTS = "DOCTCF021";
    public static final String DOC_TYPE_CF_FACILITY_RED_TEAMING_REPORT = "DOCTCF022";
    public static final String DOC_TYPE_CF_FACILITY_RISK_ASSESSMENT_AND_MANAGEMENT = "DOCTCF023";
    public static final String DOC_TYPE_CF_FACILITY_SOP_POLICIES = "DOCTCF024";
    public static final String DOC_TYPE_CF_FACILITY_TRAINING_RECORDS = "DOCTCF025";
    public static final String DOC_TYPE_CF_ENDORSEMENT_GENETIC_MODIFICATION_ADVISORY_COMMITTEE = "DOCTCF026";

    //UCF, BMF
    public static final String DOC_TYPE_UCF_OR_BMP_BIO_SAFETY_COMMITTEE = "DOCTUCF001";
    public static final String DOC_TYPE_UCF_OR_BMP_APPROVAL_DOCUMENT_FROM_MOH = "DOCTUCF002";
    public static final String DOC_TYPE_UCF_OR_BMP_APPROVAL_DOCUMENT_FROM_OTHER_AUTHORITY = "DOCTUCF003";
    public static final String DOC_TYPE_UCF_OR_BMP_ENDORSEMENT_GENETIC_MODIFICATION_ADVISORY_COMMITTEE = "DOCTUCF004";
    public static final String DOC_TYPE_UCF_OR_BMP_FACILITY_APPLICATION_LETTER = "DOCTUCF005";
    public static final String DOC_TYPE_UCF_OR_BMP_FACILITY_COMPANY_INFORMATION = "DOCTUCF006";
    public static final String DOC_TYPE_UCF_OR_BMP_FACILITY_DECLARATION_BY_FACILITY_OPERATOR = "DOCTUCF007";
    public static final String DOC_TYPE_UCF_OR_BMP_FACILITY_GAZETTE_ORDER = "DOCTUCF008";
    public static final String DOC_TYPE_UCF_OR_BMP_FACILITY_INVENTORY_TEMPLATE = "DOCTUCF009";
    public static final String DOC_TYPE_UCF_OR_BMP_FACILITY_OTHER_DECLARATIONS = "DOCTUCF010";
    public static final String DOC_TYPE_UCF_OR_BMP_FACILITY_OTHER_SUPPORTING_DOCUMENTS = "DOCTUCF011";
    public static final String DOC_TYPE_UCF_OR_BMP_FACILITY_SOP_POLICIES = "DOCTUCF012";
    public static final String DOC_TYPE_UCF_OR_BMP_FACILITY_TRAINING_RECORDS = "DOCTUCF013";
    public static final String DOC_TYPE_UCF_OR_BMP_INSPECTION_ACTION_PLAN_OR_RESPONSE = "DOCTUCF014";
    public static final String DOC_TYPE_UCF_OR_BMP_INSPECTION_FACILITY_SELF_INSPECTION = "DOCTUCF015";
    public static final String DOC_TYPE_UCF_OR_BMP_INSPECTION_FACILITY_SELF_INSPECTION_SUPPORTING_DOCUMENT = "DOCTUCF016";
    public static final String DOC_TYPE_UCF_OR_BMP_INSPECTION_OTHER_SUPPORTING_DOCUMENTS = "DOCTUCF017";


    //MOH-AFC
    public static final String DOC_TYPE_AFC_ACADEMIC_AND_PROFESSIONAL_CERTIFICATE = "DOCTAFC001";
    public static final String DOC_TYPE_AFC_OTHER_SUPPORTING_DOCUMENTS = "DOCTAFC002";
    public static final String DOC_TYPE_AFC_CERTIFICATION_EQUIPMENT_CALIBRATION_DOCUMENTS = "DOCTAFC003";

    // MOH
    public static final String DOC_TYPE_MOH_BSB_ASSESSMENT_AND_RECOMMENDATION = "DOCTMOH001";
    public static final String DOC_TYPE_MOH_EMAIL_COMMUNICATIONS = "DOCTMOH002";
    public static final String DOC_TYPE_MOH_FACILITY_AUDIT_REPORT = "DOCTMOH003";
    public static final String DOC_TYPE_MOH_FACILITY_INSPECTION_REPORT = "DOCTMOH004";
    public static final String DOC_TYPE_MOH_INVESTIGATION_DOCUMENT= "DOCTMOH005";
    public static final String DOC_TYPE_MOH_OFFLINE_APPROVALS = "DOCTMOH006";
    public static final String DOC_TYPE_MOH_OTHER_SUPPORTING_DOCUMENTS = "DOCTMOH007";

    //Document Type Tagging – Approval for Facility Activity Type, Special Approval to Handle, Approval to Possess, Approval to Large Scale Produce
    public static final String DOC_TYPE_APPROVAL_FACILITY_APPLICATION_LETTER = "DOCTAPR001";
    public static final String DOC_TYPE_APPROVAL_FACILITY_BIO_SAFETY_AND_BIO_SECURITY_MANUAL = "DOCTAPR002";
    public static final String DOC_TYPE_APPROVAL_FACILITY_DECLARATION_BY_FACILITY_OPERATOR = "DOCTAPR003";
    public static final String DOC_TYPE_APPROVAL_FACILITY_OTHER_DECLARATIONS = "DOCTAPR004";
    public static final String DOC_TYPE_APPROVAL_FACILITY_OTHER_SUPPORTING_DOCUMENTS = "DOCTAPR005";
    public static final String DOC_TYPE_APPROVAL_FACILITY_TRAINING_RECORDS = "DOCTAPR006";

    //submission type
    public static final String SUBMISSION_TYPE_NEW_FACILITY_REGISTRATION = "SUBTYPE001";
    public static final String SUBMISSION_TYPE_NEW_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE = "SUBTYPE002";
    public static final String SUBMISSION_TYPE_NEW_APPROVAL_TO_PROCESS = "SUBTYPE003";
    public static final String SUBMISSION_TYPE_NEW_APPROVAL_TO_LARGE_SCALE_PRODUCE = "SUBTYPE004";
    public static final String SUBMISSION_TYPE_NEW_APPROVAL_TO_SPECIAL_HANDLE = "SUBTYPE005";
    public static final String SUBMISSION_TYPE_RENEW_FACILITY_REGISTRATION = "SUBTYPE006";
    public static final String SUBMISSION_TYPE_CANCELLATION_OF_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE = "SUBTYPE007";
    public static final String SUBMISSION_TYPE_CANCELLATION_OF_APPROVAL_TO_PROCESS = "SUBTYPE008";
    public static final String SUBMISSION_TYPE_CANCELLATION_OF_APPROVAL_TO_LARGE_SCALE_PRODUCE = "SUBTYPE009";
    public static final String SUBMISSION_TYPE_CANCELLATION_OF_SPECIAL_HANDLE = "SUBTYPE010";
    public static final String SUBMISSION_TYPE_DEREGISTRATION_OF_FACILITY = "SUBTYPE011";
    public static final String SUBMISSION_TYPE_RFC_FACILITY_REGISTRATION = "SUBTYPE012";
    public static final String SUBMISSION_TYPE_RFC_APPROVAL_TO_PROCESS = "SUBTYPE013";
    public static final String SUBMISSION_TYPE_RFC_APPROVAL_TO_LARGE_SCALE_PRODUCE = "SUBTYPE014";
    public static final String SUBMISSION_TYPE_RFC_APPROVAL_TO_SPECIAL_HANDLE = "SUBTYPE015";
    public static final String SUBMISSION_TYPE_DATA_SUBMISSION_OR_INVENTORY_NOTIFICATION = "SUBTYPE016";
    public static final String SUBMISSION_TYPE_REPORTABLE_EVENT = "SUBTYPE017";
    public static final String SUBMISSION_TYPE_RFI_AD_HOC_ONLY = "SUBTYPE018";
    public static final String SUBMISSION_TYPE_DATA_SUBMISSION = "SUBTYPE019";
    public static final String SUBMISSION_TYPE_NEW_FACILITY_CERTIFIER_REG = "SUBTYPE020";
    public static final String SUBMISSION_TYPE_RENEW_FACILITY_CERTIFIER_REG = "SUBTYPE021";
    public static final String SUBMISSION_TYPE_DEREGISTRATION_OF_FACILITY_CERTIFIER_REG = "SUBTYPE022";
    public static final String SUBMISSION_TYPE_RFC_FACILITY_CERTIFIER = "SUBTYPE023";
    public static final String SUBMISSION_TYPE_NOTIFICATION_OF_INVENTORY_MOVEMENT = "SUBTYPE024";
    public static final String SUBMISSION_TYPE_AUDIT_REPORT_SUBMISSION = "SUBTYPE025";
    public static final String SUBMISSION_TYPE_DEFERMENT_OF_RENEWAL = "SUBTYPE026";

    public static final Set<String> COMMON_QUERY_SUBMISSION_TYPE;

    static {
        Set<String> commonQuerySubmissionType = Sets.newHashSetWithExpectedSize(25);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_DATA_SUBMISSION);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_REPORTABLE_EVENT);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_NEW_FACILITY_REGISTRATION);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_NEW_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_NEW_APPROVAL_TO_PROCESS);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_NEW_APPROVAL_TO_LARGE_SCALE_PRODUCE);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_NEW_APPROVAL_TO_SPECIAL_HANDLE);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_NEW_FACILITY_CERTIFIER_REG);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_RENEW_FACILITY_REGISTRATION);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_RENEW_FACILITY_CERTIFIER_REG);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_CANCELLATION_OF_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_CANCELLATION_OF_APPROVAL_TO_PROCESS);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_CANCELLATION_OF_APPROVAL_TO_LARGE_SCALE_PRODUCE);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_CANCELLATION_OF_SPECIAL_HANDLE);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_DEREGISTRATION_OF_FACILITY);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_DEREGISTRATION_OF_FACILITY_CERTIFIER_REG);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_RFC_FACILITY_REGISTRATION);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_RFC_APPROVAL_TO_PROCESS);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_RFC_APPROVAL_TO_LARGE_SCALE_PRODUCE);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_RFC_APPROVAL_TO_SPECIAL_HANDLE);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_RFC_FACILITY_CERTIFIER);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_NOTIFICATION_OF_INVENTORY_MOVEMENT);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_RFI_AD_HOC_ONLY);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_AUDIT_REPORT_SUBMISSION);
        commonQuerySubmissionType.add(SUBMISSION_TYPE_DEFERMENT_OF_RENEWAL);
        COMMON_QUERY_SUBMISSION_TYPE = Collections.unmodifiableSet(commonQuerySubmissionType);
    }
}
