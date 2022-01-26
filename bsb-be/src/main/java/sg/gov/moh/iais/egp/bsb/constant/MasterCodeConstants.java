package sg.gov.moh.iais.egp.bsb.constant;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

public class MasterCodeConstants {
    private MasterCodeConstants() {}

    public static final String YES = "Y";
    public static final String NO = "N";

    public static final String FIRST_SCHEDULE_PART_I = "SCHTYPE001";
    public static final String FIRST_SCHEDULE_PART_II = "SCHTYPE002";
    public static final String SECOND_SCHEDULE = "SCHTYPE003";
    public static final String THIRD_SCHEDULE = "SCHTYPE004";
    public static final String FOURTH_SCHEDULE = "SCHTYPE005";
    public static final String FIFTH_SCHEDULE = "SCHTYPE006";

    public static final String APP_TYPE_NEW = "BSBAPTY001";
    public static final String APP_TYPE_RENEW = "BSBAPTY002";
    public static final String APP_TYPE_RFC = "BSBAPTY003";
    public static final String APP_TYPE_CANCEL = "BSBAPTY004";
    public static final String APP_TYPE_DEREGISTRATION = "BSBAPTY005";
    public static final String APP_TYPE_REVOCATION = "BSBAPTY006";
    public static final String APP_TYPE_SUSPEND = "BSBAPTY007";
    public static final String APP_TYPE_REINST = "BSBAPTY008";
    public static final String APP_TYPE_WITHDRAW = "BSBAPTY009";

    public static final String APP_STATUS_PEND_DO                       = "BSBAPST001";
    public static final String APP_STATUS_PEND_AO                       = "BSBAPST002";
    public static final String APP_STATUS_PEND_HM                       = "BSBAPST003";
    public static final String APP_STATUS_PEND_INPUT                    = "BSBAPST004";
    public static final String APP_STATUS_ALL_INSPECTION                = "BSBAPST005";  // Not a actual status, it means all inspection status
    public static final String APP_STATUS_WITHDRAW                      = "BSBAPST007";
    public static final String APP_STATUS_REJECTED                      = "BSBAPST008";
    public static final String APP_STATUS_APPROVED                      = "BSBAPST009";
    public static final String APP_STATUS_REGISTERED                    = "BSBAPST010";
    public static final String APP_STATUS_DRAFT                         = "BSBAPST011";
    public static final String APP_STATUS_REMOVED                       = "BSBAPST012";
    public static final String APP_STATUS_PEND_APPOINTMENT_SCHEDULE     = "BSBAPST021";
    public static final String APP_STATUS_PEND_SUBMIT_SELF_ASSESSMENT   = "BSBAPST022";
    public static final String APP_STATUS_PEND_INSPECTION_READINESS     = "BSBAPST023";
    public static final String APP_STATUS_PEND_CLARIFICATION            = "BSBAPST024";
    public static final String APP_STATUS_PEND_INSPECTION               = "BSBAPST025";
    public static final String APP_STATUS_PEND_INSPECTION_REPORT        = "BSBAPST026";
    public static final String APP_STATUS_PEND_AO_REVIEW                = "BSBAPST027";
    public static final String APP_STATUS_PEND_REPORT_REVISION          = "BSBAPST028";
    public static final String APP_STATUS_PEND_REPORT_FINALISATION      = "BSBAPST029";
    public static final String APP_STATUS_PEND_NC_RECTIFICATION         = "BSBAPST030";
    public static final String APP_STATUS_PEND_DO_RECTIFICATION_REVIEW  = "BSBAPST031";
    public static final String APP_STATUS_PEND_AO_RECTIFICATION_REVIEW  = "BSBAPST032";
    public static final String APP_STATUS_PEND_SUBMIT_FOLLOW_UP_ITEMS   = "BSBAPST033";
    public static final String APP_STATUS_PEND_FOLLOW_UP_ITEMS_REVIEW   = "BSBAPST034";
    public static final String APP_STATUS_PEND_EXTENSION_REVIEW         = "BSBAPST035";
    public static final Set<String> COMMON_QUERY_APP_STATUS;
    public static final Set<String> INSPECTION_APP_STATUS;
    public static final Set<String> UNAVAILABLE_APP_STATUS;

    static {
        Set<String> commonQueryAppStatus = Sets.newLinkedHashSetWithExpectedSize(9);
        // BE don't add 'Draft', because BE DB don't save it
        commonQueryAppStatus.add(APP_STATUS_PEND_DO);
        commonQueryAppStatus.add(APP_STATUS_PEND_AO);
        commonQueryAppStatus.add(APP_STATUS_PEND_HM);
        commonQueryAppStatus.add(APP_STATUS_PEND_INPUT);
        commonQueryAppStatus.add(APP_STATUS_ALL_INSPECTION);
        commonQueryAppStatus.add(APP_STATUS_WITHDRAW);
        commonQueryAppStatus.add(APP_STATUS_REJECTED);
        commonQueryAppStatus.add(APP_STATUS_APPROVED);
        commonQueryAppStatus.add(APP_STATUS_REGISTERED);
        COMMON_QUERY_APP_STATUS = Collections.unmodifiableSet(commonQueryAppStatus);

        Set<String> inspectionAppStatus = Sets.newHashSetWithExpectedSize(15);
        inspectionAppStatus.add(APP_STATUS_PEND_APPOINTMENT_SCHEDULE);
        inspectionAppStatus.add(APP_STATUS_PEND_SUBMIT_SELF_ASSESSMENT);
        inspectionAppStatus.add(APP_STATUS_PEND_INSPECTION_READINESS);
        inspectionAppStatus.add(APP_STATUS_PEND_CLARIFICATION);
        inspectionAppStatus.add(APP_STATUS_PEND_INSPECTION);
        inspectionAppStatus.add(APP_STATUS_PEND_INSPECTION_REPORT);
        inspectionAppStatus.add(APP_STATUS_PEND_AO_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_REPORT_REVISION);
        inspectionAppStatus.add(APP_STATUS_PEND_REPORT_FINALISATION);
        inspectionAppStatus.add(APP_STATUS_PEND_NC_RECTIFICATION);
        inspectionAppStatus.add(APP_STATUS_PEND_DO_RECTIFICATION_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_AO_RECTIFICATION_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_SUBMIT_FOLLOW_UP_ITEMS);
        inspectionAppStatus.add(APP_STATUS_PEND_FOLLOW_UP_ITEMS_REVIEW);
        inspectionAppStatus.add(APP_STATUS_PEND_EXTENSION_REVIEW);
        INSPECTION_APP_STATUS = Collections.unmodifiableSet(inspectionAppStatus);

        Set<String> unavailableAppStatus = Sets.newHashSetWithExpectedSize(1);
        unavailableAppStatus.add(APP_STATUS_REMOVED);
        UNAVAILABLE_APP_STATUS = Collections.unmodifiableSet(unavailableAppStatus);
    }


    public static final String PROCESS_TYPE_FAC_REG = "PROTYPE001";
    public static final String PROCESS_TYPE_APPROVE_POSSESS = "PROTYPE002";
    public static final String PROCESS_TYPE_APPROVE_LSP = "PROTYPE003";
    public static final String PROCESS_TYPE_SP_APPROVE_HANDLE = "PROTYPE004";
    public static final String PROCESS_TYPE_FAC_CERTIFIER_REG = "PROTYPE005";
    public static final Set<String> APPLY_APPROVE_PROCESS_TYPES;

    static {
        Set<String> applyApprovalProcessType = Sets.newHashSetWithExpectedSize(3);
        applyApprovalProcessType.add(PROCESS_TYPE_APPROVE_POSSESS);
        applyApprovalProcessType.add(PROCESS_TYPE_APPROVE_LSP);
        applyApprovalProcessType.add(PROCESS_TYPE_SP_APPROVE_HANDLE);
        APPLY_APPROVE_PROCESS_TYPES = Collections.unmodifiableSet(applyApprovalProcessType);
    }

    public static final String FAC_CLASSIFICATION_BSL3 = "FACCLA001";
    public static final String FAC_CLASSIFICATION_BSL4 = "FACCLA002";
    public static final String FAC_CLASSIFICATION_UF = "FACCLA003";
    public static final String FAC_CLASSIFICATION_LSPF = "FACCLA004";
    public static final String FAC_CLASSIFICATION_RF = "FACCLA005";

    public static final String ACTIVI_FIRST_SECOND_SCHEDULE_BA = "ACTVITY001";
    public static final String ACTIVI_FIRST_SCHEDULE_BA = "ACTVITY002";
    public static final String ACTIVI_FIFTH_SCHEDULE_TOXIN = "ACTVITY003";
    public static final String ACTIVI_PV_MATERIALS = "ACTVITY004";
    public static final String ACTIVI_LSP_FIRST_SCHEDULE = "ACTVITY005";
    public static final String ACTIVI_LSP_THIRD_SCHEDULE = "ACTVITY006";
    public static final String ACTIVI_HANDLE_FIFTH_SCHEDULE_TOXIN = "ACTVITY007";
    public static final String ACTIVI_EXEMPTED_HANDLE_FIFTH_SCHEDULE_TOXIN = "ACTVITY008";
    public static final String ACTIVI_HANDLE_PV_MATERIAL = "ACTVITY009";
    public static final String ACTIVI_HANDLE_PV_POTENTIAL_MATERIAL = "ACTVITY010";

    public static final Set<String> VALID_BLS3_ACTIVITIES;
    public static final Set<String> VALID_BLS4_ACTIVITIES;
    public static final Set<String> VALID_UF_ACTIVITIES;
    public static final Set<String> VALID_LSPF_ACTIVITIES;
    public static final Set<String> VALID_RF_ACTIVITIES;

    static {
        Set<String> bls3Set = Sets.newHashSetWithExpectedSize(4);
        bls3Set.add(ACTIVI_FIRST_SECOND_SCHEDULE_BA);
        bls3Set.add(ACTIVI_LSP_FIRST_SCHEDULE);
        bls3Set.add(ACTIVI_HANDLE_FIFTH_SCHEDULE_TOXIN);
        bls3Set.add(ACTIVI_HANDLE_PV_MATERIAL);
        VALID_BLS3_ACTIVITIES = Collections.unmodifiableSet(bls3Set);

        Set<String> bls4Set = Sets.newHashSetWithExpectedSize(4);
        bls4Set.add(ACTIVI_FIRST_SECOND_SCHEDULE_BA);
        bls4Set.add(ACTIVI_LSP_FIRST_SCHEDULE);
        bls4Set.add(ACTIVI_FIFTH_SCHEDULE_TOXIN);
        bls4Set.add(ACTIVI_PV_MATERIALS);
        VALID_BLS4_ACTIVITIES = Collections.unmodifiableSet(bls4Set);

        Set<String> ufSet = Sets.newHashSetWithExpectedSize(3);
        ufSet.add(ACTIVI_FIRST_SCHEDULE_BA);
        ufSet.add(ACTIVI_LSP_THIRD_SCHEDULE);
        ufSet.add(ACTIVI_FIFTH_SCHEDULE_TOXIN);
        VALID_UF_ACTIVITIES = Collections.unmodifiableSet(ufSet);

        Set<String> lspfSet = Sets.newHashSetWithExpectedSize(2);
        lspfSet.add(ACTIVI_LSP_FIRST_SCHEDULE);
        lspfSet.add(ACTIVI_LSP_THIRD_SCHEDULE);
        VALID_LSPF_ACTIVITIES = Collections.unmodifiableSet(lspfSet);

        Set<String> rfSet = Sets.newHashSetWithExpectedSize(2);
        rfSet.add(ACTIVI_EXEMPTED_HANDLE_FIFTH_SCHEDULE_TOXIN);
        rfSet.add(ACTIVI_HANDLE_PV_POTENTIAL_MATERIAL);
        VALID_RF_ACTIVITIES = Collections.unmodifiableSet(rfSet);
    }

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
    public static final String APPROVAL_TYPE_FAC_REG = "APPRTY004";
    public static final Set<String> APPLY_APPROVAL_TYPES;

    static {
        Set<String> applyApprovalTypes = Sets.newHashSetWithExpectedSize(3);
        applyApprovalTypes.add(APPROVAL_TYPE_POSSESS);
        applyApprovalTypes.add(APPROVAL_TYPE_LSP);
        applyApprovalTypes.add(APPROVAL_TYPE_SP_HANDLE);
        APPLY_APPROVAL_TYPES = Collections.unmodifiableSet(applyApprovalTypes);
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

    //BE DECISION VALUE
    public static final String MOH_PROCESSING_DECISION_SCREENED_BY_DO = "MOHPRO001";
    public static final String MOH_PROCESSING_DECISION_REQUEST_FOR_INFO = "MOHPRO002";
    public static final String MOH_PROCESSING_DECISION_REJECT = "MOHPRO003";
    public static final String MOH_PROCESSING_DECISION_RECOMMEND_APPROVAL = "MOHPRO004";
    public static final String MOH_PROCESSING_DECISION_RECOMMEND_REJECT = "MOHPRO005";
    public static final String MOH_PROCESSING_DECISION_APPROVE_FOR_INSPECTION = "MOHPRO006";
    public static final String MOH_PROCESSING_DECISION_APPROVE = "MOHPRO007";
    public static final String MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO = "MOHPRO008";
    public static final String MOH_PROCESSING_DECISION_ROUTE_BACK_TO_HM = "MOHPRO009";
    public static final String MOH_PROCESSING_DECISION_VERIFIED = "MOHPRO010";
    public static final String MOH_PROCESSING_DECISION_INTERNAL_CLARIFICATIONS = "MOHPRO011";
    public static final String MOH_PROCESSING_DECISION_MARK_AS_READY = "MOHPRO021";
    public static final String MOH_PROCESSING_DECISION_SUBMIT_REPORT_TO_AO_FOR_REVIEW = "MOHPRO022";
    public static final String MOH_PROCESSING_DECISION_ROUTE_REPORT_TO_APPLICANT = "MOHPRO023";
    public static final String MOH_PROCESSING_DECISION_MARK_AS_FINAL = "MOHPRO024";

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
}
