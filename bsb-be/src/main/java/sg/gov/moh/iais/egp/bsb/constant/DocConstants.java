package sg.gov.moh.iais.egp.bsb.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DocConstants {
    private DocConstants() {}

    public static final String DOC_TYPE_OTHERS = "others";

    public static final String DOC_TYPE_DATA_COMMITTEE = "committeeData";
    public static final String DOC_TYPE_DATA_AUTHORISER = "authoriserData";
    public static final String DOC_TYPE_DATA_CERTIFYING_TEAM = "certifyingTeamData";


    public static final String DOC_TYPE_AFC_ACADEMIC_PROFESSIONAL_CERTIFICATE = "DOCT101001";
    public static final String DOC_TYPE_AFC_OTHER_SUPPORTING_DOC = "DOCT101999";
    public static final String DOC_TYPE_APPROVAL_DOCUMENT_FROM_MOH = "DOCT201001";
    public static final String DOC_TYPE_APPROVAL_DOCUMENT_FROM_OTHER_AUTHORITY = "DOCT201002";
    public static final String DOC_TYPE_APPROVAL_ENDORSEMENT_BIOSAFETY_COMMITTEE = "DOCT201003";
    public static final String DOC_TYPE_ENDORSEMENT_GENETIC_MODIFICATION_ADVISORY_COMMITTEE = "DOCT201004";
    public static final String DOC_TYPE_AUDIT_ACTION_PLAN_RESPONSE = "DOCT202001";
    public static final String DOC_TYPE_AUDIT_EMG_RESPONSE_DRILL_REPORT = "DOCT202002";
    public static final String DOC_TYPE_AUDIT_OTHER_SUPPORTING_DOC = "DOCT202999";
    public static final String DOC_TYPE_CER_ACTION_PLAN_RESPONSE_FOR_MOH = "DOCT203001";
    public static final String DOC_TYPE_CER_ACTION_PLAN_RESPONSE_FOR_AFC = "DOCT203002";
    public static final String DOC_TYPE_CER_AFC_CER_REPORT_AND_SUPPORTING_DOC = "DOCT203003";
    public static final String DOC_TYPE_CER_EQUIPMENT_CALIBRATION_DOC = "DOCT203004";
    public static final String DOC_TYPE_CER_FACILITY_CERTIFICATE = "DOCT203005";
    public static final String DOC_TYPE_CER_FACILITY_SELF_INSPECTION = "DOCT203006";
    public static final String DOC_TYPE_CER_FACILITY_SELF_INS_SUPPORTING_DOC = "DOCT203007";
    public static final String DOC_TYPE_CER_TIMELINE = "DOCT203008";
    public static final String DOC_TYPE_CER_OTHER_SUPPORTING_DOC = "DOCT203999";
    public static final String DOC_TYPE_INS_ACTION_PLAN_RESPONSE = "DOCT204001";
    public static final String DOC_TYPE_INS_FACILITY_SELF_INSPECTION = "DOCT204002";
    public static final String DOC_TYPE_INS_FACILITY_SELF_INSPECTION_SUPPORTING_DOC = "DOCT204003";
    public static final String DOC_TYPE_INS_OTHER_SUPPORTING_DOC = "DOCT204999";
    public static final String DOC_TYPE_FACILITY_ADMIN_OVERSIGHT_PLAN = "DOCT205001";
    public static final String DOC_TYPE_FACILITY_LAYOUT_ACMV_SCHEMATICS = "DOCT205002";
    public static final String DOC_TYPE_FACILITY_BIO_SAFETY_COORDINATOR_CERTIFICATES = "DOCT205003";
    public static final String DOC_TYPE_FACILITY_PERSONNEL_LIST = "DOCT205004";
    public static final String DOC_TYPE_FACILITY_APPLICATION_LETTER = "DOCT205005";
    public static final String DOC_TYPE_FACILITY_COMPANY_INFO = "DOCT205006";
    public static final String DOC_TYPE_FACILITY_BIOSAFETY_AND_BIOSECURITY_MANUAL = "DOCT205007";
    public static final String DOC_TYPE_FACILITY_DECLARATION_BY_OPERATOR = "DOCT205008";
    public static final String DOC_TYPE_FACILITY_GAZETTE_ORDER = "DOCT205009";
    public static final String DOC_TYPE_FACILITY_INVENTORY_TEMPLATE = "DOCT205010";
    public static final String DOC_TYPE_FACILITY_OTHER_DECLARATION = "DOCT205011";
    public static final String DOC_TYPE_FACILITY_RED_TEAMING_REPORT = "DOCT205012";
    public static final String DOC_TYPE_FACILITY_RISK_ASSESSMENT_AND_MGMT = "DOCT205013";
    public static final String DOC_TYPE_FACILITY_SOP_POLICIES = "DOCT205014";
    public static final String DOC_TYPE_FACILITY_TRAINING_RECORDS = "DOCT205015";
    public static final String DOC_TYPE_FACILITY_OTHER_SUPPORTING_DOC = "DOCT205999";
    public static final String DOC_TYPE_PV_INVENTORY_REPORTING_FORM = "DOCT206001";
    public static final String DOC_TYPE_PV_RISK_MITIGATION_PLAN = "DOCT206002";
    public static final String DOC_TYPE_PV_BIOSAFETY_BIOSECURITY_MANUAL = "DOCT206003";
    public static final String DOC_TYPE_PV_DECLARATIONS = "DOCT206004";
    public static final String DOC_TYPE_PV_OTHER_SUPPORTING_DOC = "DOCT206999";
    public static final String DOC_TYPE_ETF_BIANNUAL_REPORT = "DOCT207001";
    public static final String DOC_TYPE_ETF_BIOSAFETY_BIOSECURITY_MANUAL = "DOCT207002";
    public static final String DOC_TYPE_ETF_COMPANY_INFO = "DOCT207003";
    public static final String DOC_TYPE_ETF_DECLARATION = "DOCT207004";
    public static final String DOC_TYPE_ETF_TOXIN_INVENTORY = "DOCT207005";
    public static final String DOC_TYPE_ETF_OTHER_SUPPORTING_DOC = "DOCT207999";
    public static final String DOC_TYPE_MOH_ASSESSMENT_AND_RECOMMENDATION = "DOCT301001";
    public static final String DOC_TYPE_MOH_EMAIL_COMMUNICATION = "DOCT301002";
    public static final String DOC_TYPE_MOH_FACILITY_AUDIT_REPORT = "DOCT301003";
    public static final String DOC_TYPE_MOH_FACILITY_INSPECTION_REPORT = "DOCT301004";
    public static final String DOC_TYPE_MOH_INVESTIGATION_DOC = "DOCT301005";
    public static final String DOC_TYPE_MOH_OFFLINE_APPROVALS = "DOCT301006";
    public static final String DOC_TYPE_MOH_OTHER_SUPPORTING_DOC = "DOCT301999";


    // TODO change and delete the doc types
    public static final String DOC_TYPE_INS_CHECKLIST   = "insCheckList";
    public static final String DOC_RED_TEAMING_REPORT = "REPTYPE01";
    public static final String DOC_PNEF_INVENTORY_REPORT = "REPTYPE02";
    public static final String DOC_BI_ANNUAL_TOXIN_REPORT = "REPTYPE03";
    public static final String DOC_PEF_INVENTORY_REPORT = "REPTYPE04";
    public static final String DOC_FACILITY_SELF_INSPECTION_REPORT = "REPTYPE05";
    public static final String DOC_EMERGENCY_RESPONSE_SELF_AUDIT_REPORT = "REPTYPE06";
    public static final String DOC_ANNUAL_LENTIVIRUS_REPORT = "REPTYPE07";
    public static final String DOC_FIFTH_SCHEDULE_INVENTORY_UPDATE= "REPTYPE07";
    public static final String DOC_REPORT_UPLOAD = "report";

    public static final String DOC_INCIDENT_REPORT = "incidentReport";
    public static final String DOC_INCIDENT_ACTION_REPORT = "incidentActionReport";

    public static final String DOC_TYPE_COMPANY_INFORMATION = "companyInformation";
    public static final String DOC_TYPE_SOP_FOR_CERTIFICATION = "sopForCertification";
    public static final String DOC_TYPE_TESTIMONIALS= "testimonials";
    public static final String DOC_TYPE_CURRICULUM_VITAE = "curriculumVitae";
    public static final String DOC_TYPE_INVENTORY_AGENT   = "inventoryBat";
    public static final String DOC_TYPE_INVENTORY_TOXIN   = "inventoryToxins";


    public static final String PARAM_REPO_ID_DOC_MAP          = "repoIdDocMap";

    /* This constant may be removed in the future, we may get the order and other settings from DB */
    public static final List<String> FAC_REG_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_FACILITY_ADMIN_OVERSIGHT_PLAN, DOC_TYPE_FACILITY_LAYOUT_ACMV_SCHEMATICS,
            DOC_TYPE_FACILITY_BIO_SAFETY_COORDINATOR_CERTIFICATES, DOC_TYPE_APPROVAL_ENDORSEMENT_BIOSAFETY_COMMITTEE,
            DOC_TYPE_FACILITY_APPLICATION_LETTER, DOC_TYPE_FACILITY_RISK_ASSESSMENT_AND_MGMT,
            DOC_TYPE_FACILITY_BIOSAFETY_AND_BIOSECURITY_MANUAL,
            DOC_TYPE_FACILITY_SOP_POLICIES,
            DOC_TYPE_FACILITY_INVENTORY_TEMPLATE));

    public static final List<String> APP_POSSESS_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_APPROVAL_ENDORSEMENT_BIOSAFETY_COMMITTEE, DOC_TYPE_FACILITY_RISK_ASSESSMENT_AND_MGMT,
            DOC_TYPE_FACILITY_SOP_POLICIES, DOC_TYPE_ENDORSEMENT_GENETIC_MODIFICATION_ADVISORY_COMMITTEE));

    public static final List<String> APP_LARGE_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_APPROVAL_ENDORSEMENT_BIOSAFETY_COMMITTEE, DOC_TYPE_FACILITY_RISK_ASSESSMENT_AND_MGMT,
            DOC_TYPE_FACILITY_SOP_POLICIES, DOC_TYPE_ENDORSEMENT_GENETIC_MODIFICATION_ADVISORY_COMMITTEE));

    public static final List<String> APP_SPECIAL_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_APPROVAL_ENDORSEMENT_BIOSAFETY_COMMITTEE, DOC_TYPE_FACILITY_RISK_ASSESSMENT_AND_MGMT, DOC_TYPE_APPROVAL_DOCUMENT_FROM_MOH,
            DOC_TYPE_FACILITY_SOP_POLICIES, DOC_TYPE_ENDORSEMENT_GENETIC_MODIFICATION_ADVISORY_COMMITTEE));

    /* This constant may be removed in the future, we may get the order and other settings from DB */
    public static final List<String> FAC_REG_CERTIFIER_DOC_TYPE_ORDER = Collections.unmodifiableList(Arrays.asList(
            DOC_TYPE_COMPANY_INFORMATION,DOC_TYPE_SOP_FOR_CERTIFICATION));

    public static final List<String> DATA_SUBMISSION_REPORT_AND_INVENTORY = Collections.unmodifiableList(Arrays.asList(
            DOC_RED_TEAMING_REPORT,DOC_PNEF_INVENTORY_REPORT,DOC_BI_ANNUAL_TOXIN_REPORT,DOC_PEF_INVENTORY_REPORT,
            DOC_FACILITY_SELF_INSPECTION_REPORT,DOC_EMERGENCY_RESPONSE_SELF_AUDIT_REPORT,DOC_ANNUAL_LENTIVIRUS_REPORT,DOC_FIFTH_SCHEDULE_INVENTORY_UPDATE));


    public static final String DOC_TYPE_APPLICANT_COMMENT_REPORT = "commentedInsReport";
    public static final String DOC_TYPE_INSPECTION_NON_COMPLIANCE = "nonComplianceReport";
    public static final String DOC_TYPE_FOLLOW_UP = "insFollowUp";
    public static final String DOC_TYPE_ADHOC_RFI_UP = "adhocRfiUp";


    public static final String KEY_COMMON_DOC_DTO = "commonDocDto";
}
