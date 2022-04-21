package sg.gov.moh.iais.egp.bsb.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DocConstants {
    private DocConstants() {}
    public static final String KEY_COMMON_DOC_DTO = "commonDocDto";

    public static final String DOC_TYPE_DATA_COMMITTEE = "committeeData";
    public static final String DOC_TYPE_DATA_AUTHORISER = "authoriserData";
    public static final String DOC_TYPE_DATA_CERTIFYING_TEAM = "certifyingTeamData";

    public static final String DOC_TYPE_GAZETTE_ORDER = "gazetteOrder";
    public static final String DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES = "bsfCoordinatorCert";
    public static final String DOC_TYPE_INVENTORY_FILE = "inventoryFile";
    public static final String DOC_TYPE_GMAC_ENDORSEMENT = "gmacEndorsement";
    public static final String DOC_TYPE_RISK_ASSESS_PLAN = "riskAssessPlan";
    public static final String DOC_TYPE_STANDARD_OPERATING_PROCEDURE = "stdOperatingProcedure";
    public static final String DOC_TYPE_EMERGENCY_RESPONSE_PLAN = "emgResponsePlan";
    public static final String DOC_TYPE_BIO_SAFETY_COM = "bsfCom";
    public static final String DOC_TYPE_FACILITY_PLAN_LAYOUT = "facPlanLayout";
    public static final String DOC_RED_TEAMING_REPORT = "REPTYPE01";
    public static final String DOC_PNEF_INVENTORY_REPORT = "REPTYPE02";
    public static final String DOC_BI_ANNUAL_TOXIN_REPORT = "REPTYPE03";
    public static final String DOC_PEF_INVENTORY_REPORT = "REPTYPE04";
    public static final String DOC_FACILITY_SELF_INSPECTION_REPORT = "REPTYPE05";
    public static final String DOC_EMERGENCY_RESPONSE_SELF_AUDIT_REPORT = "REPTYPE06";
    public static final String DOC_ANNUAL_LENTIVIRUS_REPORT = "REPTYPE07";
    public static final String DOC_FIFTH_SCHEDULE_INVENTORY_UPDATE= "REPTYPE07";
    public static final String DOC_REPORT_UPLOAD = "report";
    public static final String DOC_TYPE_OTHERS = "others";
    public static final String DOC_INCIDENT_REPORT = "incidentReport";
    public static final String DOC_INCIDENT_ACTION_REPORT = "incidentActionReport";

    public static final String DOC_TYPE_RISK_ASSESSMENT = "riskAssessment";
    public static final String DOC_TYPE_APPROVAL_DOCUMENT_FROM_MOH = "approvalDocumentFromMoh";
    public static final String DOC_TYPE_SPECIAL_APPROVAL_TO_HANDLE = "specialApprovalToHandle";
    public static final String DOC_TYPE_COMPANY_INFORMATION = "companyInformation";
    public static final String DOC_TYPE_SOP_FOR_CERTIFICATION = "sopForCertification";
    public static final String DOC_TYPE_TESTIMONIALS= "testimonials";
    public static final String DOC_TYPE_CURRICULUM_VITAE = "curriculumVitae";
    public static final String DOC_TYPE_INVENTORY_AGENT   = "inventoryBat";
    public static final String DOC_TYPE_INVENTORY_TOXIN   = "inventoryToxins";

    /* This constant may be removed in the future, we may get the order and other settings from DB */
    public static final List<String> FAC_REG_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES, DOC_TYPE_INVENTORY_FILE, DOC_TYPE_GMAC_ENDORSEMENT,
            DOC_TYPE_RISK_ASSESS_PLAN, DOC_TYPE_STANDARD_OPERATING_PROCEDURE, DOC_TYPE_EMERGENCY_RESPONSE_PLAN,
            DOC_TYPE_BIO_SAFETY_COM, DOC_TYPE_FACILITY_PLAN_LAYOUT, DOC_TYPE_OTHERS));

    public static final List<String> APP_POSSESS_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_BIO_SAFETY_COM, DOC_TYPE_RISK_ASSESSMENT, DOC_TYPE_STANDARD_OPERATING_PROCEDURE,
            DOC_TYPE_GMAC_ENDORSEMENT, DOC_TYPE_OTHERS));

    public static final List<String> APP_LARGE_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_BIO_SAFETY_COM, DOC_TYPE_RISK_ASSESSMENT, DOC_TYPE_STANDARD_OPERATING_PROCEDURE,
            DOC_TYPE_EMERGENCY_RESPONSE_PLAN, DOC_TYPE_OTHERS));

    public static final List<String> APP_SPECIAL_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_BIO_SAFETY_COM, DOC_TYPE_RISK_ASSESSMENT, DOC_TYPE_APPROVAL_DOCUMENT_FROM_MOH,
            DOC_TYPE_EMERGENCY_RESPONSE_PLAN, DOC_TYPE_SPECIAL_APPROVAL_TO_HANDLE, DOC_TYPE_OTHERS));

    /* This constant may be removed in the future, we may get the order and other settings from DB */
    public static final List<String> FAC_REG_CERTIFIER_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_COMPANY_INFORMATION,DOC_TYPE_SOP_FOR_CERTIFICATION,DOC_TYPE_OTHERS));

    public static final List<String> DATA_SUBMISSION_REPORT_AND_INVENTORY = Collections.unmodifiableList(Arrays.asList(
            DOC_RED_TEAMING_REPORT,DOC_PNEF_INVENTORY_REPORT,DOC_BI_ANNUAL_TOXIN_REPORT,DOC_PEF_INVENTORY_REPORT,
            DOC_FACILITY_SELF_INSPECTION_REPORT,DOC_EMERGENCY_RESPONSE_SELF_AUDIT_REPORT,DOC_ANNUAL_LENTIVIRUS_REPORT,DOC_FIFTH_SCHEDULE_INVENTORY_UPDATE, DOC_TYPE_OTHERS));


    public static final String DOC_TYPE_INSPECTION_REPORT = "inspectionReport";
    public static final String DOC_TYPE_APPLICANT_COMMENT_REPORT = "commentedInsReport";
    public static final String DOC_TYPE_INSPECTION_NON_COMPLIANCE = "nonComplianceReport";
    public static final String DOC_TYPE_FOLLOW_UP = "insFollowUp";
    public static final String DOC_TYPE_ADHOC_RFI_UP = "adhocRfiUp";
}
