package sg.gov.moh.iais.egp.bsb.constant;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static String readUpperCaseYesNo(String raw) {
        String value = null;
        if (YES_UPPER_CASE.equals(raw)) {
            value = YES;
        } else if (NO_UPPER_CASE.equals(raw)) {
            value = NO;
        }
        return value;
    }
    public static String displayYesNo(String value) {
        String display = null;
        if (YES.equals(value)) {
            display = YES_CAPITALIZED;
        } else if (NO.equals(value)) {
            display = NO_CAPITALIZED;
        }
        return display;
    }


    public static final String FIRST_SCHEDULE_PART_I = "SCHTYPE001";
    public static final String FIRST_SCHEDULE_PART_II = "SCHTYPE002";
    public static final String SECOND_SCHEDULE = "SCHTYPE003";
    public static final String THIRD_SCHEDULE = "SCHTYPE004";
    public static final String FOURTH_SCHEDULE = "SCHTYPE005";
    public static final String FIFTH_SCHEDULE = "SCHTYPE006";

    public static final List<String> BIOLOGICAL_AGENT_SCHEDULE_TYPE_ALL = Collections.unmodifiableList(Arrays.asList(
            FIRST_SCHEDULE_PART_I,FIRST_SCHEDULE_PART_II,SECOND_SCHEDULE,
            THIRD_SCHEDULE,FOURTH_SCHEDULE,FIFTH_SCHEDULE));

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

    public static final List<SelectOption> INBOX_APPLICATION_SEARCH_APP_TYPE_FAC;
    public static final List<SelectOption> INBOX_APPLICATION_SEARCH_APP_TYPE_AFC;

    static {
        List<SelectOption> inboxApplicationSearchAppTypeFAC = new ArrayList<>(6);
        inboxApplicationSearchAppTypeFAC.add(new SelectOption(APP_TYPE_NEW, MasterCodeUtil.getCodeDesc(APP_TYPE_NEW)));
        inboxApplicationSearchAppTypeFAC.add(new SelectOption(APP_TYPE_RENEW, MasterCodeUtil.getCodeDesc(APP_TYPE_RENEW)));
        inboxApplicationSearchAppTypeFAC.add(new SelectOption(APP_TYPE_RFC, MasterCodeUtil.getCodeDesc(APP_TYPE_RFC)));
        inboxApplicationSearchAppTypeFAC.add(new SelectOption(APP_TYPE_CANCEL, MasterCodeUtil.getCodeDesc(APP_TYPE_CANCEL)));
        inboxApplicationSearchAppTypeFAC.add(new SelectOption(APP_TYPE_DEREGISTRATION, MasterCodeUtil.getCodeDesc(APP_TYPE_DEREGISTRATION)));
        inboxApplicationSearchAppTypeFAC.add(new SelectOption(APP_TYPE_WITHDRAW, MasterCodeUtil.getCodeDesc(APP_TYPE_WITHDRAW)));
        INBOX_APPLICATION_SEARCH_APP_TYPE_FAC = Collections.unmodifiableList(inboxApplicationSearchAppTypeFAC);

        List<SelectOption> inboxApplicationSearchAppTypeAFC = new ArrayList<>(4);
        inboxApplicationSearchAppTypeAFC.add(new SelectOption(APP_TYPE_NEW, MasterCodeUtil.getCodeDesc(APP_TYPE_NEW)));
        inboxApplicationSearchAppTypeAFC.add(new SelectOption(APP_TYPE_RENEW, MasterCodeUtil.getCodeDesc(APP_TYPE_RENEW)));
        inboxApplicationSearchAppTypeAFC.add(new SelectOption(APP_TYPE_WITHDRAW, MasterCodeUtil.getCodeDesc(APP_TYPE_WITHDRAW)));
        inboxApplicationSearchAppTypeAFC.add(new SelectOption(APP_TYPE_RFC, MasterCodeUtil.getCodeDesc(APP_TYPE_RFC)));
        INBOX_APPLICATION_SEARCH_APP_TYPE_AFC = Collections.unmodifiableList(inboxApplicationSearchAppTypeAFC);
    }

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
    public static final String APP_STATUS_PEND_INSPECTION_CERTIFICATION             = "BSBAPST010";
    // screening
    public static final String APP_STATUS_PEND_DO_SCREENING                         = "BSBAPST020";
    public static final String APP_STATUS_PEND_DO_CLARIFICATION                     = "BSBAPST021";
    public static final String APP_STATUS_PEND_AO_SCREENING                         = "BSBAPST022";
    public static final String APP_STATUS_PEND_HM_DECISION                          = "BSBAPST023";
    // processing
    public static final String APP_STATUS_PEND_DO_RECOMMENDATION                    = "BSBAPST030";
    public static final String APP_STATUS_PEND_AO_APPROVAL                          = "BSBAPST031";
    public static final String APP_STATUS_PEND_HM_APPROVAL                          = "BSBAPST032";
    public static final String APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT             = "BSBAPST033";
    public static final String APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW            = "BSBAPST034";
    public static final String APP_STATUS_PEND_DO_VERIFICATION                      = "BSBAPST035";
    // pre-inspection
    public static final String APP_STATUS_PEND_INSPECTION_TASK_ASSIGNMENT           = "BSBAPST100";
    public static final String APP_STATUS_PEND_CHECKLIST_SUBMISSION                 = "BSBAPST101";
    public static final String APP_STATUS_PEND_APPOINTMENT_SCHEDULING               = "BSBAPST102";
    public static final String APP_STATUS_PEND_APPOINTMENT_CONFIRMATION             = "BSBAPST103";
    public static final String APP_STATUS_PEND_INSPECTION_READINESS                 = "BSBAPST104";
    // on-site inspection
    public static final String APP_STATUS_PEND_INSPECTION                           = "BSBAPST200";
    // post-inspection(report)
    public static final String APP_STATUS_PEND_INSPECTION_REPORT                    = "BSBAPST300";
    public static final String APP_STATUS_PEND_INSPECTION_REPORT_REVIEW             = "BSBAPST301";
    public static final String APP_STATUS_PEND_INSPECTION_REPORT_REVISION           = "BSBAPST302";
    public static final String APP_STATUS_PEND_DO_REPORT_APPROVAL                   = "BSBAPST303";
    public static final String APP_STATUS_PEND_AO_REPORT_APPROVAL                   = "BSBAPST304";
    public static final String APP_STATUS_PEND_HM_REPORT_APPROVAL                   = "BSBAPST305";
    // post-inspection(Non-Compliance)
    public static final String APP_STATUS_PEND_NC_RECTIFICATION                     = "BSBAPST310";
    public static final String APP_STATUS_PEND_NC_RECTIFICATION_CLARIFICATION       = "BSBAPST311";
    public static final String APP_STATUS_PEND_NC_NOTIFICATION_EMAIL                = "BSBAPST312";
    public static final String APP_STATUS_PEND_NC_NOTIFICATION_EMAIL_REVIEW         = "BSBAPST313";
    public static final String APP_STATUS_PEND_DO_RECTIFICATION_REVIEW              = "BSBAPST314";
    public static final String APP_STATUS_PEND_AO_RECTIFICATION_REVIEW              = "BSBAPST315";
    // post-inspection(Follow-Up)
    public static final String APP_STATUS_PEND_EXTENSION_REVIEW                     = "BSBAPST320";
    public static final String APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION            = "BSBAPST321";
    public static final String APP_STATUS_PEND_DO_FOLLOW_UP_ITEM_VERIFICATION       = "BSBAPST322";
    public static final String APP_STATUS_PEND_AO_FOLLOW_UP_ITEM_VERIFICATION       = "BSBAPST323";
    // certification
    public static final String APP_STATUS_PEND_AFC_REPORT_UPLOAD                    = "BSBAPST400";
    public static final String APP_STATUS_PEND_AFC_SELECTION                        = "BSBAPST401";
    public static final String APP_STATUS_PEND_AFC_SELECTION_REVIEW                 = "BSBAPST402";
    public static final String APP_STATUS_PEND_DO_REPORT_REVIEW                     = "BSBAPST403";
    public static final String APP_STATUS_PEND_AO_REPORT_REVIEW                     = "BSBAPST404";
    public static final String APP_STATUS_PEND_AFC_INPUT                            = "BSBAPST405";

    public static final Set<String> INSPECTION_APP_STATUS;

    // not an actual app status, this is a compound status
    public static final String PENDING_MOH                               = "Pending MOH";
    public static final String APPROVED                                  = "Approved";
    public static final String PENDING_INSPECTION_OR_CERTIFICATION       = "Pending Inspection/Certification";

    public static final Set<String> PENDING_MOH_APP_STATUS;
    public static final Set<String> APPROVED_APP_STATUS;
    public static final Set<String> PENDING_INSPECTION_OR_CERTIFICATION_APP_STATUS;

    static {
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
        inspectionAppStatus.add(APP_STATUS_PEND_NC_RECTIFICATION);
        inspectionAppStatus.add(APP_STATUS_PEND_NC_RECTIFICATION_CLARIFICATION);
        inspectionAppStatus.add(APP_STATUS_PEND_NC_NOTIFICATION_EMAIL);
        inspectionAppStatus.add(APP_STATUS_PEND_NC_NOTIFICATION_EMAIL_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_EXTENSION_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION);
        inspectionAppStatus.add(APP_STATUS_PEND_DO_FOLLOW_UP_ITEM_VERIFICATION);
        inspectionAppStatus.add(APP_STATUS_PEND_AO_FOLLOW_UP_ITEM_VERIFICATION);
        INSPECTION_APP_STATUS = Collections.unmodifiableSet(inspectionAppStatus);

        Set<String> pendingMohAppStatus = Sets.newHashSetWithExpectedSize(27);
        pendingMohAppStatus.add(APP_STATUS_PEND_DO_SCREENING);
        pendingMohAppStatus.add(APP_STATUS_PEND_DO_CLARIFICATION);
        pendingMohAppStatus.add(APP_STATUS_PEND_AO_SCREENING);
        pendingMohAppStatus.add(APP_STATUS_PEND_HM_DECISION);
        pendingMohAppStatus.add(APP_STATUS_PEND_DO_RECOMMENDATION);
        pendingMohAppStatus.add(APP_STATUS_PEND_AO_APPROVAL);
        pendingMohAppStatus.add(APP_STATUS_PEND_HM_APPROVAL);
        pendingMohAppStatus.add(APP_STATUS_PEND_DO_VERIFICATION);
        pendingMohAppStatus.add(APP_STATUS_PEND_INSPECTION_CERTIFICATION);
        pendingMohAppStatus.add(APP_STATUS_PEND_INSPECTION_TASK_ASSIGNMENT);
        pendingMohAppStatus.add(APP_STATUS_PEND_APPOINTMENT_CONFIRMATION);
        pendingMohAppStatus.add(APP_STATUS_PEND_INSPECTION_READINESS);
        pendingMohAppStatus.add(APP_STATUS_PEND_INSPECTION);
        pendingMohAppStatus.add(APP_STATUS_PEND_INSPECTION_REPORT);
        pendingMohAppStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVIEW);
        pendingMohAppStatus.add(APP_STATUS_PEND_INSPECTION_REPORT_REVISION);
        pendingMohAppStatus.add(APP_STATUS_PEND_DO_REPORT_APPROVAL);
        pendingMohAppStatus.add(APP_STATUS_PEND_AO_REPORT_APPROVAL);
        pendingMohAppStatus.add(APP_STATUS_PEND_HM_REPORT_APPROVAL);
        pendingMohAppStatus.add(APP_STATUS_PEND_NC_NOTIFICATION_EMAIL);
        pendingMohAppStatus.add(APP_STATUS_PEND_NC_NOTIFICATION_EMAIL_REVIEW);
        pendingMohAppStatus.add(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW);
        pendingMohAppStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        pendingMohAppStatus.add(APP_STATUS_PEND_DO_FOLLOW_UP_ITEM_VERIFICATION);
        pendingMohAppStatus.add(APP_STATUS_PEND_AO_FOLLOW_UP_ITEM_VERIFICATION);
        pendingMohAppStatus.add(APP_STATUS_PEND_AFC_SELECTION_REVIEW);
        pendingMohAppStatus.add(APP_STATUS_PEND_DO_REPORT_REVIEW);
        pendingMohAppStatus.add(APP_STATUS_PEND_AO_REPORT_REVIEW);
        PENDING_MOH_APP_STATUS = Collections.unmodifiableSet(pendingMohAppStatus);

        Set<String> approvedAppStatus = Sets.newHashSetWithExpectedSize(4);
        approvedAppStatus.add(APP_STATUS_APPROVED);
        approvedAppStatus.add(APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT);
        approvedAppStatus.add(APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW);
        approvedAppStatus.add(APP_STATUS_PARTIAL_ACCEPTANCE);
        APPROVED_APP_STATUS = Collections.unmodifiableSet(approvedAppStatus);

        Set<String> pendingInspectionOrCertificationAppStatus = Sets.newHashSetWithExpectedSize(2);
        pendingInspectionOrCertificationAppStatus.add(APP_STATUS_PEND_EXTENSION_REVIEW);
        pendingInspectionOrCertificationAppStatus.add(APP_STATUS_PEND_AFC_SELECTION);
        PENDING_INSPECTION_OR_CERTIFICATION_APP_STATUS = Collections.unmodifiableSet(pendingInspectionOrCertificationAppStatus);
    }

    public static final List<SelectOption> INBOX_APP_SEARCH_STATUS_FAC;
    public static final List<SelectOption> INBOX_APP_SEARCH_STATUS_AFC;

    static {
        List<SelectOption> appSearchStatusFAC = new ArrayList<>(17);
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_DRAFT, MasterCodeUtil.getCodeDesc(APP_STATUS_DRAFT)));
        appSearchStatusFAC.add(new SelectOption(PENDING_MOH, PENDING_MOH));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_PEND_APPLICANT_INPUT, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_APPLICANT_INPUT)));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_PEND_CHECKLIST_SUBMISSION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_CHECKLIST_SUBMISSION)));
        appSearchStatusFAC.add(new SelectOption(PENDING_INSPECTION_OR_CERTIFICATION, PENDING_INSPECTION_OR_CERTIFICATION));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_PEND_APPLICANT_CLARIFICATION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_APPLICANT_CLARIFICATION)));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_PEND_APPOINTMENT_SCHEDULING, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_APPOINTMENT_SCHEDULING)));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_PEND_AFC_REPORT_UPLOAD, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_AFC_REPORT_UPLOAD)));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_PEND_AFC_INPUT, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_AFC_INPUT)));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_PEND_NC_RECTIFICATION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_NC_RECTIFICATION)));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_PEND_NC_RECTIFICATION_CLARIFICATION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_NC_RECTIFICATION_CLARIFICATION)));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_FOLLOW_UP_ITEM_SUBMISSION)));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_VERIFIED, MasterCodeUtil.getCodeDesc(APP_STATUS_VERIFIED)));
        appSearchStatusFAC.add(new SelectOption(APPROVED, APPROVED));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_REJECTED, MasterCodeUtil.getCodeDesc(APP_STATUS_REJECTED)));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_ACCEPTED, MasterCodeUtil.getCodeDesc(APP_STATUS_ACCEPTED)));
        appSearchStatusFAC.add(new SelectOption(APP_STATUS_WITHDRAWN, MasterCodeUtil.getCodeDesc(APP_STATUS_WITHDRAWN)));
        INBOX_APP_SEARCH_STATUS_FAC = Collections.unmodifiableList(appSearchStatusFAC);

        List<SelectOption> appSearchStatusAFC = new ArrayList<>(7);
        appSearchStatusAFC.add(new SelectOption(APP_STATUS_DRAFT, MasterCodeUtil.getCodeDesc(APP_STATUS_DRAFT)));
        appSearchStatusAFC.add(new SelectOption(PENDING_MOH, PENDING_MOH));
        appSearchStatusAFC.add(new SelectOption(APP_STATUS_PEND_APPLICANT_INPUT, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_APPLICANT_INPUT)));
        appSearchStatusAFC.add(new SelectOption(APP_STATUS_PEND_APPLICANT_CLARIFICATION, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_APPLICANT_CLARIFICATION)));
        appSearchStatusAFC.add(new SelectOption(APPROVED, APPROVED));
        appSearchStatusAFC.add(new SelectOption(APP_STATUS_REJECTED, MasterCodeUtil.getCodeDesc(APP_STATUS_REJECTED)));
        appSearchStatusAFC.add(new SelectOption(APP_STATUS_WITHDRAWN, MasterCodeUtil.getCodeDesc(APP_STATUS_WITHDRAWN)));
        INBOX_APP_SEARCH_STATUS_AFC = Collections.unmodifiableList(appSearchStatusAFC);
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
    public static final Set<String> APPLY_APPROVE_PROCESS_TYPES;

    public static final List<SelectOption> INBOX_APPLICATION_SEARCH_PROCESS_TYPE_FAC;
    public static final List<SelectOption> INBOX_APPROVAL_SEARCH_PROCESS_TYPE_FAC;
    public static final List<SelectOption> INBOX_APPROVAL_SEARCH_PROCESS_TYPE_AFC;

    static {
        Set<String> applyApprovalProcessType = Sets.newHashSetWithExpectedSize(3);
        applyApprovalProcessType.add(PROCESS_TYPE_APPROVE_POSSESS);
        applyApprovalProcessType.add(PROCESS_TYPE_APPROVE_LSP);
        applyApprovalProcessType.add(PROCESS_TYPE_SP_APPROVE_HANDLE);
        APPLY_APPROVE_PROCESS_TYPES = Collections.unmodifiableSet(applyApprovalProcessType);

        List<SelectOption> inboxApplicationSearchProcessTypeFAC = new ArrayList<>(5);
        inboxApplicationSearchProcessTypeFAC.add(new SelectOption(PROCESS_TYPE_FAC_REG, MasterCodeUtil.getCodeDesc(PROCESS_TYPE_FAC_REG)));
        inboxApplicationSearchProcessTypeFAC.add(new SelectOption(PROCESS_TYPE_APPROVE_POSSESS, MasterCodeUtil.getCodeDesc(PROCESS_TYPE_APPROVE_POSSESS)));
        inboxApplicationSearchProcessTypeFAC.add(new SelectOption(PROCESS_TYPE_APPROVE_LSP, MasterCodeUtil.getCodeDesc(PROCESS_TYPE_APPROVE_LSP)));
        inboxApplicationSearchProcessTypeFAC.add(new SelectOption(PROCESS_TYPE_SP_APPROVE_HANDLE, MasterCodeUtil.getCodeDesc(PROCESS_TYPE_SP_APPROVE_HANDLE)));
        inboxApplicationSearchProcessTypeFAC.add(new SelectOption(PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE, MasterCodeUtil.getCodeDesc(PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE)));
        INBOX_APPLICATION_SEARCH_PROCESS_TYPE_FAC = Collections.unmodifiableList(inboxApplicationSearchProcessTypeFAC);

        List<SelectOption> inboxApprovalSearchProcessTypeFAC = new ArrayList<>(4);
        inboxApprovalSearchProcessTypeFAC.add(new SelectOption(PROCESS_TYPE_APPROVE_POSSESS, MasterCodeUtil.getCodeDesc(PROCESS_TYPE_APPROVE_POSSESS)));
        inboxApprovalSearchProcessTypeFAC.add(new SelectOption(PROCESS_TYPE_APPROVE_LSP, MasterCodeUtil.getCodeDesc(PROCESS_TYPE_APPROVE_LSP)));
        inboxApprovalSearchProcessTypeFAC.add(new SelectOption(PROCESS_TYPE_SP_APPROVE_HANDLE, MasterCodeUtil.getCodeDesc(PROCESS_TYPE_SP_APPROVE_HANDLE)));
        inboxApprovalSearchProcessTypeFAC.add(new SelectOption(PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE, MasterCodeUtil.getCodeDesc(PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE)));
        INBOX_APPROVAL_SEARCH_PROCESS_TYPE_FAC = Collections.unmodifiableList(inboxApprovalSearchProcessTypeFAC);

        List<SelectOption> inboxApprovalSearchProcessTypeAFC = new ArrayList<>(1);
        inboxApprovalSearchProcessTypeAFC.add(new SelectOption(PROCESS_TYPE_FAC_CERTIFIER_REG, MasterCodeUtil.getCodeDesc(PROCESS_TYPE_FAC_CERTIFIER_REG)));
        INBOX_APPROVAL_SEARCH_PROCESS_TYPE_AFC = Collections.unmodifiableList(inboxApprovalSearchProcessTypeAFC);
    }

    public static final String FAC_CLASSIFICATION_BSL3 = "FACCLA001";
    public static final String FAC_CLASSIFICATION_BSL4 = "FACCLA002";
    public static final String FAC_CLASSIFICATION_UF = "FACCLA003";
    public static final String FAC_CLASSIFICATION_LSPF = "FACCLA004";
    public static final String FAC_CLASSIFICATION_RF = "FACCLA005";
    public static final Set<String> VALID_FAC_CLASSIFICATION;
    public static final Set<String> CERTIFIED_CLASSIFICATION;
    public static final Set<String> UNCERTIFIED_CLASSIFICATION;

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

    public static final List<SelectOption> INBOX_APPROVAL_SEARCH_STATUS_FAC;
    public static final List<SelectOption> INBOX_APPROVAL_SEARCH_STATUS_AFC;

    static {
        List<SelectOption> inboxApprovalSearchStatusFAC = new ArrayList<>(7);
        inboxApprovalSearchStatusFAC.add(new SelectOption(APPROVAL_STATUS_ACTIVE, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_ACTIVE)));
        inboxApprovalSearchStatusFAC.add(new SelectOption(APPROVAL_STATUS_CANCELLED, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_CANCELLED)));
        inboxApprovalSearchStatusFAC.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL)));
        inboxApprovalSearchStatusFAC.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_PENDING_INVESTIGATION, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_PENDING_INVESTIGATION)));
        inboxApprovalSearchStatusFAC.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_NC, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_NC)));
        inboxApprovalSearchStatusFAC.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_OTHERS, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_OTHERS)));
        inboxApprovalSearchStatusFAC.add(new SelectOption(APPROVAL_STATUS_REVOKED, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_REVOKED)));
        INBOX_APPROVAL_SEARCH_STATUS_FAC = Collections.unmodifiableList(inboxApprovalSearchStatusFAC);

        List<SelectOption> inboxApprovalSearchStatusAFC = new ArrayList<>(7);
        inboxApprovalSearchStatusAFC.add(new SelectOption(APPROVAL_STATUS_ACTIVE, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_ACTIVE)));
        inboxApprovalSearchStatusAFC.add(new SelectOption(APPROVAL_STATUS_DEREGISTERED, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_DEREGISTERED)));
        inboxApprovalSearchStatusAFC.add(new SelectOption(APPROVAL_STATUS_EXPIRED, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_EXPIRED)));
        inboxApprovalSearchStatusAFC.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_PENDING_RENEWAL)));
        inboxApprovalSearchStatusAFC.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_PENDING_INVESTIGATION, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_PENDING_INVESTIGATION)));
        inboxApprovalSearchStatusAFC.add(new SelectOption(APPROVAL_STATUS_SUSPENDED_OTHERS, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_SUSPENDED_OTHERS)));
        inboxApprovalSearchStatusAFC.add(new SelectOption(APPROVAL_STATUS_REVOKED, MasterCodeUtil.getCodeDesc(APPROVAL_STATUS_REVOKED)));
        INBOX_APPROVAL_SEARCH_STATUS_AFC = Collections.unmodifiableList(inboxApprovalSearchStatusAFC);
    }

    public static final String REPORTING_OF_INCIDENT_ADVERSE_INCIDENT = "REPORT001";
    public static final String REPORTING_OF_INCIDENT_NEAR_MISS = "REPORT002";

    public static final String INCIDENT_TYPE_BIO_SAFETY = "INDTYPE001";
    public static final String INCIDENT_TYPE_BIO_SECURITY = "INDTYPE002";
    public static final String INCIDENT_TYPE_GENERAL_SAFETY = "INDTYPE003";

    public static final String CAUSE_OF_INCIDENT_POLICY_AND_PROCEDURES = "INCAUSE001";
    public static final String CAUSE_OF_INCIDENT_TRAINING = "INCAUSE002";
    public static final String CAUSE_OF_INCIDENT_EQUIPMENT_FAILURE = "INCAUSE003";
    public static final String CAUSE_OF_INCIDENT_ENGINEERING = "INCAUSE004";
    public static final String CAUSE_OF_INCIDENT_PERSONAL_PROTECTIVE_EQUIPMENT = "INCAUSE005";
    public static final String CAUSE_OF_INCIDENT_INADEQUATE_EQUIPMENT = "INCAUSE006";
    public static final String CAUSE_OF_INCIDENT_ENVIRONMENT_FACTORS = "INCAUSE007";
    public static final String CAUSE_OF_INCIDENT_HUMAN_BEHAVIOURAL_FACTORS = "INCAUSE008";
    public static final String CAUSE_OF_INCIDENT_HUMAN_PERFORMANCE = "INCAUSE009";
    public static final String CAUSE_OF_INCIDENT_OTHERS = "INCAUSE010";

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

    //Mode of Procurement
    public static final String PROCUREMENT_MODE_LOCAL_TRANSFER = "BMOP001";
    public static final String PROCUREMENT_MODE_IMPORT = "BMOP002";
    public static final String PROCUREMENT_MODE_ALREADY_IN_POSSESSION = "BMOP003";

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
}
