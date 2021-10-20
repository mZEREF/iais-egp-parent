package sg.gov.moh.iais.egp.bsb.constant;

import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;

public class MasterCodeConstants {
    private MasterCodeConstants() {}

    public static final String YES = "Y";
    public static final String NO = "N";

    public static final String APP_TYPE_NEW = "BSBAPTY001";
    public static final String APP_TYPE_RENEW = "BSBAPTY002";
    public static final String APP_TYPE_RFC = "BSBAPTY003";
    public static final String APP_TYPE_CANCEL = "BSBAPTY004";
    public static final String APP_TYPE_DEREGISTRATION = "BSBAPTY005";
    public static final String APP_TYPE_REVOCATION = "BSBAPTY006";
    public static final String APP_TYPE_SUSPEND = "BSBAPTY007";
    public static final String APP_TYPE_REINST = "BSBAPTY008";
    public static final String APP_TYPE_WITHDRAW = "BSBAPTY009";

    public static final String APP_STATUS_PEND_DO = "BSBAPST001";
    public static final String APP_STATUS_PEND_AO = "BSBAPST002";
    public static final String APP_STATUS_PEND_HM = "BSBAPST003";
    public static final String APP_STATUS_PEND_INPUT = "BSBAPST004";
    public static final String APP_STATUS_PEND_INSPECTION = "BSBAPST005";
    public static final String APP_STATUS_PEND_CERT = "BSBAPST006";
    public static final String APP_STATUS_WITHDRAW = "BSBAPST007";
    public static final String APP_STATUS_REJECTED = "BSBAPST008";
    public static final String APP_STATUS_APPROVED = "BSBAPST009";
    public static final String APP_STATUS_REGISTERED = "BSBAPST010";

    public static final String PROCESS_TYPE_FAC_REG = "PROTYPE001";
    public static final String PROCESS_TYPE_APPROVE_POSSESS = "PROTYPE002";
    public static final String PROCESS_TYPE_APPROVE_LSP = "PROTYPE003";
    public static final String PROCESS_TYPE_SP_APPROVE_HANDLE = "PROTYPE004";
    public static final String PROCESS_TYPE_FAC_CERTIFIER_REG = "PROTYPE005";

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

    public static final String APPROVAL_STATUS_ACTIVE = "APPRSTA001";
    public static final String APPROVAL_STATUS_CANCELLED = "APPRSTA002";
    public static final String APPROVAL_STATUS_REVOKED = "APPRSTA003";
    public static final String APPROVAL_STATUS_SUSPENDED = "APPRSTA004";
    public static final String APPROVAL_STATUS_EXPIRED = "APPRSTA005";
}
