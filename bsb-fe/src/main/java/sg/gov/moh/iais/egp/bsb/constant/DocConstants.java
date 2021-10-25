package sg.gov.moh.iais.egp.bsb.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DocConstants {
    private DocConstants() {}

    public static final String DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES = "bsfCoordinatorCert";
    public static final String DOC_TYPE_INVENTORY_FILE = "inventoryFile";
    public static final String DOC_TYPE_EMAC_ENDORSEMENT = "gmacEndorsement";
    public static final String DOC_TYPE_RISK_ASSESS_PLAN = "riskAssessPlan";
    public static final String DOC_TYPE_STANDARD_OPERATING_PROCEDURE = "stdOperatingProcedure";
    public static final String DOC_TYPE_EMERGENCY_RESPONSE_PLAN = "emgResponsePlan";
    public static final String DOC_TYPE_BIO_SAFETY_COM = "bsfCom";
    public static final String DOC_TYPE_FACILITY_PLAN_LAYOUT = "facPlanLayout";
    public static final String DOC_TYPE_OTHERS = "others";

    /* This constant may be removed in the future, we may get the order and other settings from DB */
    public static final List<String> FAC_REG_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES, DOC_TYPE_INVENTORY_FILE, DOC_TYPE_EMAC_ENDORSEMENT,
            DOC_TYPE_RISK_ASSESS_PLAN, DOC_TYPE_STANDARD_OPERATING_PROCEDURE, DOC_TYPE_EMERGENCY_RESPONSE_PLAN,
            DOC_TYPE_BIO_SAFETY_COM, DOC_TYPE_FACILITY_PLAN_LAYOUT, DOC_TYPE_OTHERS));
}
