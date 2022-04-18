package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.AppViewClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.ResponseConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.afc.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.approval.ApprovalAppDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.approval.ApprovalProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.CancellationApprovalDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.DeRegistrationAFCDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.DeRegistrationFacilityDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.facility.*;
import sg.gov.moh.iais.egp.bsb.dto.appview.inspection.RectifyFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.datasubmission.DataSubmissionInfo;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_PRIMARY_DOC_DTO;


@Service
@Slf4j
public class AppViewService {
    private static final String DOC_TYPE_OF_OTHERS = "Others";
    private final AppViewClient appViewClient;
    private final DocSettingService docSettingService;

    public AppViewService(AppViewClient appViewClient, DocSettingService docSettingService) {
        this.appViewClient = appViewClient;
        this.docSettingService = docSettingService;
    }

    /**
     * only use for process new, rfc, renewal, deregistration, cancellation module
     */
    public static String judgeProcessAppModuleType(String processType, String appType){
        String module = "";
        if (org.springframework.util.StringUtils.hasLength(processType) && org.springframework.util.StringUtils.hasLength(appType)){
            switch (processType) {
                case PROCESS_TYPE_FAC_REG:
                    if (appType.equals(APP_TYPE_NEW)){
                        module = MODULE_VIEW_NEW_FACILITY;
                    }else if (appType.equals(APP_TYPE_DEREGISTRATION)){
                        module = MODULE_VIEW_DEREGISTRATION_FACILITY;
                    }
                    break;
                case PROCESS_TYPE_APPROVE_POSSESS:
                case PROCESS_TYPE_APPROVE_LSP:
                case PROCESS_TYPE_SP_APPROVE_HANDLE:
                    if (appType.equals(APP_TYPE_NEW)){
                        module = MODULE_VIEW_NEW_APPROVAL_APP;
                    } else if (appType.equals(APP_TYPE_CANCEL)){
                        module = MODULE_VIEW_CANCELLATION_APPROVAL_APP;
                    }
                    break;
                case PROCESS_TYPE_FAC_CERTIFIER_REG:
                    if (appType.equals(APP_TYPE_NEW)){
                        module = MODULE_VIEW_NEW_FAC_CER_REG;
                    }else if (appType.equals(APP_TYPE_DEREGISTRATION)){
                        module = MODULE_VIEW_DEREGISTRATION_FAC_CER_REG;
                    }
                    break;
                default:
                    log.info("don't have such processType {}", StringUtils.normalizeSpace(processType));
                    break;
            }
        }
        return module;
    }

    /**
     * retrieve new facility registration view data
     */
    public void retrieveFacReg(HttpServletRequest request, String applicationId) {
        // retrieve app data of facility registration
        ResponseDto<FacilityRegisterDto> resultDto = appViewClient.getFacRegDtoByAppId(applicationId);
        if (resultDto.ok()) {
            FacilityRegisterDto facilityRegisterDto = resultDto.getEntity();
            // TODO retrieve company address
            OrgAddressInfo orgAddressInfo = new OrgAddressInfo();
            orgAddressInfo.setUen("185412420D");
            orgAddressInfo.setCompName("DBO Laboratories");
            orgAddressInfo.setPostalCode("980335");
            orgAddressInfo.setAddressType("ADDTY001");
            orgAddressInfo.setBlockNo("10");
            orgAddressInfo.setFloor("03");
            orgAddressInfo.setUnitNo("01");
            orgAddressInfo.setStreet("Toa Payoh Lorong 2");
            orgAddressInfo.setBuilding("-");
            ParamUtil.setRequestAttr(request, KEY_ORG_ADDRESS, orgAddressInfo);

            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, facilityRegisterDto.getFacilityProfileDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, facilityRegisterDto.getFacilityOperatorDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, facilityRegisterDto.getFacilityAuthoriserDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN_OFFICER, facilityRegisterDto.getFacilityAdminAndOfficerDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, facilityRegisterDto.getFacilityCommitteeDto());

            FacilitySelectionDto selectionDto = facilityRegisterDto.getFacilitySelectionDto();
            if (MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification())) {
                ParamUtil.setRequestAttr(request, KEY_IS_CF, Boolean.TRUE);

                ParamUtil.setRequestAttr(request, NODE_NAME_AFC, facilityRegisterDto.getAfcDto());
            } else {
                ParamUtil.setRequestAttr(request, KEY_IS_CF, Boolean.FALSE);

                List<BiologicalAgentToxinDto> batList = new ArrayList<>(facilityRegisterDto.getBiologicalAgentToxinMap().values());
                ParamUtil.setRequestAttr(request, KEY_BAT_LIST, batList);
            }

            OtherApplicationInfoDto otherAppInfoDto = facilityRegisterDto.getOtherAppInfoDto();
            // TODO after we save the declaration data into app misc, remove the hardcode
            List<DeclarationItemMainInfo> config = appViewClient.getDeclarationConfigInfoById("B95B8F95-62B1-EC11-BE76-000C298D317C");
            otherAppInfoDto.setDeclarationConfig(config);
            ParamUtil.setRequestAttr(request, KEY_DECLARATION_CONFIG, otherAppInfoDto.getDeclarationConfig());
            ParamUtil.setRequestAttr(request, KEY_DECLARATION_ANSWER_MAP, otherAppInfoDto.getAnswerMap());

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, docSettingService.getFacRegDocSettings());
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(facilityRegisterDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_SAVED_FILES, savedFiles);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    /**
     * retrieve new approval app view data
     */
    public void retrieveApprovalApp(HttpServletRequest request, String applicationId) {
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

    /**
     * retrieve new facility certifier registration view data
     */
    public void retrieveFacCerReg(HttpServletRequest request, String applicationId) {
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

    /**
     * retrieve deregistration facility view data
     */
    public void retrieveDeregistrationFac(HttpServletRequest request, String applicationId){
        ResponseDto<DeRegistrationFacilityDto> resultDto = appViewClient.getDeRegistrationFacilityDtoByAppId(applicationId);
        if (resultDto.ok()){
            DeRegistrationFacilityDto deRegistrationFacilityDto = resultDto.getEntity();
            ParamUtil.setRequestAttr(request, KEY_DE_REGISTRATION_FACILITY_DTO, deRegistrationFacilityDto);

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, getDeRegistrationDocSettings());
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(deRegistrationFacilityDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_SAVED_FILES, saveFiles);
            ParamUtil.setSessionAttr(request, KEY_PRIMARY_DOC_DTO, primaryDocDto);
        }else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    /**
     * retrieve cancellation approval app view data
     */
    public void retrieveCancellationApproval(HttpServletRequest request, String applicationId){
        ResponseDto<CancellationApprovalDto> resultDto = appViewClient.getCancellationApprovalDtoByAppId(applicationId);
        if (resultDto.ok()){
            CancellationApprovalDto cancellationApprovalDto = resultDto.getEntity();
            ParamUtil.setRequestAttr(request, KEY_CANCELLATION_APPROVAL_DTO, cancellationApprovalDto);

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, getDeRegistrationDocSettings());
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(cancellationApprovalDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_SAVED_FILES, saveFiles);
            ParamUtil.setSessionAttr(request, KEY_PRIMARY_DOC_DTO, primaryDocDto);
        }else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    /**
     * retrieve deregistration AFC view data
     */
    public void retrieveDeRegistrationAFC(HttpServletRequest request, String applicationId){
        ResponseDto<DeRegistrationAFCDto> resultDto = appViewClient.getDeRegistrationAFCDtoByAppId(applicationId);
        if (resultDto.ok()){
            DeRegistrationAFCDto deRegistrationAFCDto = resultDto.getEntity();
            ParamUtil.setRequestAttr(request, KEY_DE_REGISTRATION_AFC_DTO, deRegistrationAFCDto);

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, getDeRegistrationDocSettings());
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(deRegistrationAFCDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_SAVED_FILES, saveFiles);
            ParamUtil.setSessionAttr(request, KEY_PRIMARY_DOC_DTO, primaryDocDto);
        }else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    /**
     * retrieve data submission app view data
     */
    public void retrieveDataSubmissionInfo(HttpServletRequest request, String applicationId){
        ResponseDto<DataSubmissionInfo> resultDto = appViewClient.getDataSubmissionInfo(applicationId);
        if (resultDto.ok()){
            DataSubmissionInfo dataSubmissionInfo = resultDto.getEntity();
            ParamUtil.setRequestAttr(request, KEY_VIEW_DATA_SUBMISSION, dataSubmissionInfo);
            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, getDataSubmissionDocSettings());
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(dataSubmissionInfo.getDocs(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_SAVED_FILES, saveFiles);
            ParamUtil.setSessionAttr(request, KEY_PRIMARY_DOC_DTO, primaryDocDto);
        }else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    /**
     * retrieve inspection follow-up items
     */
    public void retrieveInspectionFollowUpItems(HttpServletRequest request, String applicationId){
        ResponseDto<RectifyFindingFormDto> resultDto = appViewClient.getFollowUpItemsFindingFormDtoByAppId(applicationId);
        if (resultDto.ok()){
            RectifyFindingFormDto rectifyFindingFormDto = resultDto.getEntity();
            ParamUtil.setRequestAttr(request, KEY_INSPECTION_FOLLOW_UP_ITEMS_DTO, rectifyFindingFormDto);
        }else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    /* Will be removed in future, will get this from config mechanism */
    private List<DocSetting> getApprovalAppDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(5);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COM, "Approval/Endorsement: Biosafety Committee", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESSMENT, "Risk Assessment", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure (SOP)", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, DOC_TYPE_OF_OTHERS, false));
        return docSettings;
    }

    /* Will be removed in future, will get this from config mechanism */
    private List<DocSetting> getFacCerRegDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(5);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_COMPANY_INFORMATION, "Company Information", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_SOP_FOR_CERTIFICATION, "SOP for Certification", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, DOC_TYPE_OF_OTHERS, false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_TESTIMONIALS, "Testimonials", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_CURRICULUM_VITAE, "Curriculum Vitae", true));
        return docSettings;
    }

    /* Will be removed in future, will get this from config mechanism */
    public List<DocSetting> getDeRegistrationDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(1);
        docSettings.add(new DocSetting("attachments", "Attachments", true));
        return docSettings;
    }

    /* Will be removed in future, will get this from config mechanism */
    public List<DocSetting> getDataSubmissionDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(3);
        docSettings.add(new DocSetting("ityBat", "ItyBat", false));
        docSettings.add(new DocSetting("ityToxin", "ItyToxin", false));
        docSettings.add(new DocSetting("others", DOC_TYPE_OF_OTHERS, false));
        return docSettings;
    }
}
