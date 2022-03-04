package sg.gov.moh.iais.egp.bsb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class DocSettingService {
    public List<DocSetting> getFacRegDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(9);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES, "BioSafety Coordinator Certificates", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_INVENTORY_FILE, "Inventory File", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESS_PLAN, "Risk Assessment Plan", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_EMERGENCY_RESPONSE_PLAN, "Emergency Response Plan", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COM, "Approval/Endorsement : Biosafety Com", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_FACILITY_PLAN_LAYOUT, "Facility Plan/Layout", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

    public List<DocSetting> getApprovalAppDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(5);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COM, "Approval/Endorsement: Biosafety Committee", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESSMENT, "Risk Assessment", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure (SOP)", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

    public List<DocSetting> getFacCerRegDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(5);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_COMPANY_INFORMATION, "Company Information", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_SOP_FOR_CERTIFICATION, "SOP for Certification", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_TESTIMONIALS, "Testimonials", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_CURRICULUM_VITAE, "Curriculum Vitae", true));
        return docSettings;
    }
}
