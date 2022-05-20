package sg.gov.moh.iais.egp.bsb.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class DocSettingService {
    private static final String TYPE_DISPLAY_OTHERS = "Others";

    /**
     * Computes a distinct list of document types which are not included in the doc settings
     * from a list of key sets from document map.
     * @param docSettings document settings in a page
     * @param existingDocTypes a list of key sets of doc map
     */
    @SafeVarargs
    public static Set<String> computeOtherDocTypes(List<DocSetting> docSettings, Set<String>... existingDocTypes) {
        Set<String> otherDocTypes = new HashSet<>();
        if (existingDocTypes != null && existingDocTypes.length > 0) {
            for (Set<String> oneTypeSet : existingDocTypes) {
                List<String> oneSetTypes = new ArrayList<>(oneTypeSet);
                docSettings.forEach(s -> oneSetTypes.remove(s.getType()));
                otherDocTypes.addAll(oneSetTypes);
            }
        }
        return otherDocTypes;
    }

    /**
     * Hard code of supporting document list used by facility registration.
     * This method may be changed to retrieve config from DB in the future.
     * @author chenwei
     * @param classification facility classification
     * @param activityTypes facility activity types
     * @return a list of doc settings
     */
    public List<DocSetting> getFacRegDocSettings(String classification, List<String> activityTypes) {
        List<DocSetting> docSettings;
        if (MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(classification)) {
            docSettings = new ArrayList<>(4);
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_FACILITY_ADMIN_OVERSIGHT_PLAN, "Facility Administrative Oversight Plan", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_FACILITY_LAYOUT_ACMV_SCHEMATICS, "Facility Layout and ACMV Schematics", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES, "Biosafety Coordinator's Certificates", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COMMITTEE, "Approval/Endorsement: Biosafety Committee", false));
        } else if (MasterCodeConstants.FAC_CLASSIFICATION_UF.equals(classification)) {
            docSettings = new ArrayList<>(9);
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_FACILITY_ADMIN_OVERSIGHT_PLAN, "Facility Administrative Oversight Plan", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_FACILITY_LAYOUT_ACMV_SCHEMATICS, "Facility Layout and ACMV Schematics", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES, "Biosafety Coordinator's Certificates", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COMMITTEE, "Approval/Endorsement: Biosafety Committee", false));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_APPLICATION_LETTER, "Application Letter", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESS_MGMT_WORK_ACTIVITIES, "Risk Assessment and Management: Work Activities", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_EMERGENCY_RESPONSE_PLAN, "Emergency Response Plan", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_INVENTORY_TEMPLATE, "Inventory Template", true));
        } else if (MasterCodeConstants.FAC_CLASSIFICATION_LSPF.equals(classification)) {
            docSettings = new ArrayList<>(7);
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_FACILITY_ADMIN_OVERSIGHT_PLAN, "Facility Administrative Oversight Plan", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_FACILITY_LAYOUT_ACMV_SCHEMATICS, "Facility Layout and ACMV Schematics", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES, "Biosafety Coordinator's Certificates", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COMMITTEE, "Approval/Endorsement: Biosafety Committee", false));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_APPLICATION_LETTER, "Application Letter", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESS_MGMT_WORK_ACTIVITIES, "Risk Assessment and Management: Work Activities", true));
            docSettings.add(new DocSetting(DocConstants.DOC_TYPE_EMERGENCY_RESPONSE_PLAN, "Emergency Response Plan", true));
        } else if (MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(classification)) {
            docSettings = new ArrayList<>(2);
            String activityType = activityTypes.get(0);
            if (MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED.equals(activityType)) {
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_PERSONNEL_HANDLE_TOXIN, "Personnel authorised to handle Fifth Schedule Toxins", true));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BS_SECURITY_MEASURE, "Biosafety and Biosecurity Measures", true));
            } else if (MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(activityType)) {
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_REPORTING_FORM, "Reporting Form", true));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_MITIGATION_PLAN, "Risk Mitigation Plan", true));
            } else {
                throw new IllegalArgumentException("Invalid facility activity type");
            }
        } else {
            throw new IllegalArgumentException("Invalid facility classification");
        }
        return docSettings;
    }

    public List<DocSetting> getApprovalAppDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(5);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COMMITTEE, "Approval/Endorsement: Biosafety Committee", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESSMENT, "Risk Assessment", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure (SOP)", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, TYPE_DISPLAY_OTHERS, false));
        return docSettings;
    }

    public List<DocSetting> getApprovalAppDocSettings(String approvalType) {
        List<DocSetting> docSettings = new ArrayList<>(5);
        switch (approvalType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COMMITTEE, "Approval/Endorsement: Biosafety Committee", true));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESSMENT, "Risk Assessment", true));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure (SOP)", false));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COMMITTEE, "Approval/Endorsement: Biosafety Committee", false));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESSMENT, "Risk Assessment", false));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure (SOP)", false));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_EMERGENCY_RESPONSE_PLAN, "Emergency Response Plan", false));
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COMMITTEE, "Approval/Endorsement: Biosafety Committee", true));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESSMENT, "Risk Assessment", true));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_APPROVAL_DOCUMENT_FROM_MOH, "Approval Document From MOH", true));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_EMERGENCY_RESPONSE_PLAN, "Emergency Response Plan", true));
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COMMITTEE, "Approval/Endorsement: Biosafety Committee", false));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESSMENT, "Risk Assessment", false));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure (SOP)", false));
                docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
                break;
            default:
                log.info("no such processType {}", StringUtils.normalizeSpace(approvalType));
                break;
        }
        return docSettings;
    }

    public List<DocSetting> getFacCerRegDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(5);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_COMPANY_INFORMATION, "Company Information", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_SOP_FOR_CERTIFICATION, "SOP for Certification", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, TYPE_DISPLAY_OTHERS, false));
        return docSettings;
    }

    public List<DocSetting> getAttachmentsDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(1);
        docSettings.add(new DocSetting("attachments", "Attachments", true));
        return docSettings;
    }

    public List<DocSetting> getIncidentNotDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(3);
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_REPORT, "Incident Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_ACTION_REPORT, "Incident Action Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, TYPE_DISPLAY_OTHERS, false));
        return docSettings;
    }

    public List<DocSetting> getOthersDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(1);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, TYPE_DISPLAY_OTHERS, false));
        return docSettings;
    }

    public List<DocSetting> getDataSubmissionDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(3);
        docSettings.add(new DocSetting("ityBat", "ItyBat", false));
        docSettings.add(new DocSetting("ityToxin", "ItyToxin", false));
        docSettings.add(new DocSetting("others", TYPE_DISPLAY_OTHERS, false));
        return docSettings;
    }
}
