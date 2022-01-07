package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.AppViewClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ResponseConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.AppViewDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.afc.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.approval.ApprovalAppDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.approval.ApprovalProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.facility.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;

/**
 * @author : LiRan
 * @date : 2021/12/27
 */
@Delegator
@Slf4j
public class MohBeAppViewDelegator {
    public static final String KEY_APP_VIEW_DTO                              = "appViewDto";

    private static final String MODULE_NAME                                  = "MohBeAppView";
    private static final String FUNCTION_NAME                                = "MohBeAppView";

    private static final String NODE_NAME_FAC_PROFILE                        = "facProfile";
    private static final String NODE_NAME_FAC_OPERATOR                       = "facOperator";
    private static final String NODE_NAME_FAC_AUTH                           = "facAuth";
    private static final String NODE_NAME_FAC_ADMIN                          = "facAdmin";
    private static final String NODE_NAME_FAC_OFFICER                        = "facOfficer";
    private static final String NODE_NAME_FAC_COMMITTEE                      = "facCommittee";
    private static final String KEY_BAT_LIST                                 = "batList";

    private static final String KEY_APPROVAL_PROFILE_LIST                    = "approvalProfileList";

    private static final String NODE_NAME_ORG_PROFILE                        = "orgProfile";
    private static final String NODE_NAME_ORG_CERTIFYING_TEAM                = "orgCerTeam";
    private static final String NODE_NAME_ORG_FAC_ADMINISTRATOR              = "orgAdmin";

    private static final String KEY_DOC_SETTINGS                             = "docSettings";
    private static final String KEY_SAVED_FILES                              = "savedFiles";
    private static final String KEY_PRIMARY_DOC_DTO                          = "primaryDocDto";


    private final AppViewClient appViewClient;

    @Autowired
    public MohBeAppViewDelegator(AppViewClient appViewClient) {
        this.appViewClient = appViewClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_PRIMARY_DOC_DTO);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void prepareViewForm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppViewDto appViewDto = (AppViewDto) ParamUtil.getSessionAttr(request, KEY_APP_VIEW_DTO);
        if (appViewDto != null){
            String applicationId = appViewDto.getApplicationId();
            String processType = appViewDto.getProcessType();
            switch (processType) {
                case PROCESS_TYPE_FAC_REG:
                    retrieveFacReg(request, applicationId);
                    break;
                case PROCESS_TYPE_APPROVE_POSSESS:
                case PROCESS_TYPE_APPROVE_LSP:
                case PROCESS_TYPE_SP_APPROVE_HANDLE:
                    retrieveApprovalApp(request, applicationId);
                    break;
                case PROCESS_TYPE_FAC_CERTIFIER_REG:
                    retrieveFacCerReg(request, applicationId);
                    break;
                default:
                    log.info("don't have such processType {}", StringUtils.normalizeSpace(processType));
                    break;
            }
        }
    }

    private void retrieveFacReg(HttpServletRequest request, String applicationId) {
        ResponseDto<FacilityRegisterDto> resultDto = appViewClient.getFacRegDtoByAppId(applicationId);
        if (resultDto.ok()){
            FacilityRegisterDto facilityRegisterDto = resultDto.getEntity();

            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, facilityRegisterDto.getFacilityProfileDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, facilityRegisterDto.getFacilityOperatorDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, facilityRegisterDto.getFacilityAuthoriserDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN, facilityRegisterDto.getFacilityAdministratorDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OFFICER, facilityRegisterDto.getFacilityOfficerDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, facilityRegisterDto.getFacilityCommitteeDto());

            List<BiologicalAgentToxinDto> batList = new ArrayList<>(facilityRegisterDto.getBiologicalAgentToxinMap().values());
            ParamUtil.setRequestAttr(request, KEY_BAT_LIST, batList);

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, getFacRegDocSettings());
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(facilityRegisterDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_SAVED_FILES, saveFiles);
            ParamUtil.setSessionAttr(request, KEY_PRIMARY_DOC_DTO, primaryDocDto);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    private void retrieveApprovalApp(HttpServletRequest request, String applicationId) {
        ResponseDto<ApprovalAppDto> resultDto = appViewClient.getApprovalAppDtoByAppId(applicationId);
        if (resultDto.ok()){
            ApprovalAppDto approvalAppDto = resultDto.getEntity();
            List<ApprovalProfileDto> approvalProfileDtoList = new ArrayList<>(approvalAppDto.getApprovalProfileMap().values());
            ParamUtil.setRequestAttr(request, KEY_APPROVAL_PROFILE_LIST, approvalProfileDtoList);

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, getApprovalAppDocSettings());
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(approvalAppDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_SAVED_FILES, saveFiles);
            ParamUtil.setSessionAttr(request, KEY_PRIMARY_DOC_DTO, primaryDocDto);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    private void retrieveFacCerReg(HttpServletRequest request, String applicationId) {
        ResponseDto<FacilityCertifierRegisterDto> resultDto = appViewClient.getFacCerRegDtoByAppId(applicationId);
        if (resultDto.ok()){
            FacilityCertifierRegisterDto facilityCertifierRegisterDto = resultDto.getEntity();
            ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, facilityCertifierRegisterDto.getProfileDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_ORG_CERTIFYING_TEAM, facilityCertifierRegisterDto.getCertifyingTeamDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_ORG_FAC_ADMINISTRATOR, facilityCertifierRegisterDto.getAdministratorDto());

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, getFacCerRegDocSettings());
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(facilityCertifierRegisterDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_SAVED_FILES, saveFiles);
            ParamUtil.setSessionAttr(request, KEY_PRIMARY_DOC_DTO, primaryDocDto);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    /* Will be removed in future, will get this from config mechanism */
    private List<DocSetting> getFacRegDocSettings () {
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

    /* Will be removed in future, will get this from config mechanism */
    private List<DocSetting> getApprovalAppDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(5);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COM, "Approval/Endorsement: Biosafety Committee", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESSMENT, "Risk Assessment", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure (SOP)", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

    /* Will be removed in future, will get this from config mechanism */
    private List<DocSetting> getFacCerRegDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(5);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_COMPANY_INFORMATION, "Company Information", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_SOP_FOR_CERTIFICATION, "SOP for Certification", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_TESTIMONIALS, "Testimonials", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_CURRICULUM_VITAE, "Curriculum Vitae", true));
        return docSettings;
    }
}
