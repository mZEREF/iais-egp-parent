package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.AppViewClient;
import sg.gov.moh.iais.egp.bsb.client.ApplicationDocClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationInfoClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.ResponseConstants;
import sg.gov.moh.iais.egp.bsb.constant.TaskType;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.afc.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.approval.ApprovalAppDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.approval.ApprovalProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.CancellationApprovalDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.DeRegistrationAFCDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation.DeRegistrationFacilityDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.inspection.RectifyFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap;
import sg.gov.moh.iais.egp.bsb.dto.datasubmission.DataSubmissionInfo;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.OtherApplicationInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.ViewWithdrawnDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.CompareUtil;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_AFC;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_ALTER_ADMIN;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_AUTHORIZER_IS_DIFFERENT;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_BAT_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_BIO_SAFETY_COMMITTEE_IS_DIFFERENT;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_DOC_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_FAC_OPERATOR;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_FAC_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_MAIN_ADMIN;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_OFFICERS;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_APPROVAL_PROFILE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_BAT_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_CANCELLATION_APPROVAL_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_DECLARATION_ANSWER_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_DECLARATION_CONFIG;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_DE_REGISTRATION_AFC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_DE_REGISTRATION_FACILITY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_DOC_SETTINGS;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_FACILITY_REGISTRATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_FILE_MAP_SAVED;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_INSPECTION_FOLLOW_UP_ITEMS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_IS_CF;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_IS_RF;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_IS_UCF;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_ORG_ADDRESS;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_OTHER_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_PRIMARY_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_SAVED_FILES;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_VIEW_DATA_SUBMISSION;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_AFC;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_FAC_ADMIN_OFFICER;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_FAC_OPERATOR;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_FAC_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_ORG_CERTIFYING_TEAM;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_ORG_FAC_ADMINISTRATOR;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_ORG_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;


@Service
@Slf4j
public class AppViewService {
    private final AppViewClient appViewClient;
    private final DocSettingService docSettingService;
    private final OrganizationInfoClient orgInfoClient;
    private final ApplicationDocClient applicationDocClient;

    public AppViewService(AppViewClient appViewClient, DocSettingService docSettingService, OrganizationInfoClient orgInfoClient, ApplicationDocClient applicationDocClient) {
        this.appViewClient = appViewClient;
        this.docSettingService = docSettingService;
        this.orgInfoClient = orgInfoClient;
        this.applicationDocClient = applicationDocClient;
    }

    public static void facilityRegistrationViewApp(HttpServletRequest request, String appId) {
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, appId);
        ParamUtil.setRequestAttr(request, AppViewConstants.KEY_APP_VIEW_URL, AppViewConstants.KEY_APP_VIEW_URL_FACILITY);
    }

    /**
     * When MOH need facility registration information from Applicant through RFI
     * used for MohDOScreeningDelegator, MohDOProcessingDelegator, PreInspectionDelegator
     */
    public static void facilityRegistrationViewApp(HttpServletRequest request, String appId, TaskType taskType) {
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, appId);
        ParamUtil.setRequestAttr(request, AppViewConstants.KEY_APP_VIEW_URL, AppViewConstants.KEY_APP_VIEW_URL_FACILITY);
        ParamUtil.setRequestAttr(request, AppViewConstants.KEY_TASK_TYPE, taskType);
    }

    public static void approvalAppViewApp(HttpServletRequest request, String appId) {
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, appId);
        ParamUtil.setRequestAttr(request, AppViewConstants.KEY_APP_VIEW_URL, AppViewConstants.KEY_APP_VIEW_URL_APPROVAL_APP);
    }

    public static void facilityCertificationRegistrationViewApp(HttpServletRequest request, String appId) {
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, appId);
        ParamUtil.setRequestAttr(request, AppViewConstants.KEY_APP_VIEW_URL, AppViewConstants.KEY_APP_VIEW_URL_FAC_CER_REG);
    }

    /**
     * retrieve new facility registration view data
     */
    public void retrieveFacReg(HttpServletRequest request, String applicationId) {
        // retrieve app data of facility registration
        ResponseDto<FacilityRegisterDto> resultDto = appViewClient.getFacRegDtoByAppId(applicationId);
        if (resultDto.ok()) {
            FacilityRegisterDto facilityRegisterDto = resultDto.getEntity();
            ParamUtil.setSessionAttr(request, KEY_FACILITY_REGISTRATION_DTO, facilityRegisterDto);
            String uen = facilityRegisterDto.getUen();
            LicenseeDto licenseeDto = orgInfoClient.getLicenseeDtoByUen(uen).getEntity().get(0);
            OrgAddressInfo orgAddressInfo = new OrgAddressInfo();
            orgAddressInfo.setUen(uen);
            orgAddressInfo.setCompName(licenseeDto.getName());
            orgAddressInfo.setPostalCode(licenseeDto.getPostalCode());
            orgAddressInfo.setAddressType(licenseeDto.getAddrType());
            orgAddressInfo.setBlockNo(licenseeDto.getBlkNo());
            orgAddressInfo.setFloor(licenseeDto.getFloorNo());
            orgAddressInfo.setUnitNo(licenseeDto.getUnitNo());
            orgAddressInfo.setStreet(licenseeDto.getStreetName());
            orgAddressInfo.setBuilding(licenseeDto.getBuildingName());
            ParamUtil.setSessionAttr(request, KEY_ORG_ADDRESS, orgAddressInfo);

            FacilitySelectionDto selectionDto = facilityRegisterDto.getFacilitySelectionDto();
            boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
            ParamUtil.setRequestAttr(request, KEY_IS_CF, isCf ? Boolean.TRUE : Boolean.FALSE);
            boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
            ParamUtil.setRequestAttr(request, KEY_IS_UCF, isUcf ? Boolean.TRUE : Boolean.FALSE);
            boolean isRf = MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(selectionDto.getFacClassification());
            ParamUtil.setRequestAttr(request, KEY_IS_RF, isRf ? Boolean.TRUE : Boolean.FALSE);

            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, facilityRegisterDto.getFacilityProfileDto());
            if (isCf || isUcf) {
                ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, facilityRegisterDto.getFacilityOperatorDto());
            }
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN_OFFICER, facilityRegisterDto.getFacilityAdminAndOfficerDto());

            if (isCf) {
                ParamUtil.setRequestAttr(request, NODE_NAME_AFC, facilityRegisterDto.getAfcDto());
            } else if (isUcf) {
                List<BiologicalAgentToxinDto> batList = new ArrayList<>(facilityRegisterDto.getBiologicalAgentToxinMap().values());
                ParamUtil.setRequestAttr(request, KEY_BAT_LIST, batList);
            }

            OtherApplicationInfoDto otherAppInfoDto = facilityRegisterDto.getOtherAppInfoDto();
            List<DeclarationItemMainInfo> config = appViewClient.getDeclarationConfigInfoById(otherAppInfoDto.getDeclarationId());
            otherAppInfoDto.setDeclarationConfig(config);
            ParamUtil.setRequestAttr(request, KEY_DECLARATION_CONFIG, otherAppInfoDto.getDeclarationConfig());
            ParamUtil.setRequestAttr(request, KEY_DECLARATION_ANSWER_MAP, otherAppInfoDto.getAnswerMap());

            List<DocSetting> facRegDocSetting = docSettingService.getFacRegDocSettings(selectionDto.getFacClassification(), selectionDto.getActivityTypes());
            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, facRegDocSetting);
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(facilityRegisterDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, savedFiles);
            Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(facRegDocSetting, savedFiles.keySet());
            ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    /**
     * retrieve rfi facility registration view data
     */
    public void retrieveRfiFacReg(HttpServletRequest request, String applicationId, FacilityRegisterDto oldFacRegDto) {
        // retrieve app data of facility registration
        ResponseDto<FacilityRegisterDto> resultDto = appViewClient.getFacRegDtoByAppId(applicationId);
        if (resultDto.ok()) {
            FacilityRegisterDto newFacRegDto = resultDto.getEntity();
            ParamUtil.setSessionAttr(request, KEY_FACILITY_REGISTRATION_DTO, newFacRegDto);
            String uen = newFacRegDto.getUen();
            LicenseeDto licenseeDto = orgInfoClient.getLicenseeDtoByUen(uen).getEntity().get(0);
            OrgAddressInfo orgAddressInfo = new OrgAddressInfo();
            orgAddressInfo.setUen(uen);
            orgAddressInfo.setCompName(licenseeDto.getName());
            orgAddressInfo.setPostalCode(licenseeDto.getPostalCode());
            orgAddressInfo.setAddressType(licenseeDto.getAddrType());
            orgAddressInfo.setBlockNo(licenseeDto.getBlkNo());
            orgAddressInfo.setFloor(licenseeDto.getFloorNo());
            orgAddressInfo.setUnitNo(licenseeDto.getUnitNo());
            orgAddressInfo.setStreet(licenseeDto.getStreetName());
            orgAddressInfo.setBuilding(licenseeDto.getBuildingName());
            ParamUtil.setSessionAttr(request, KEY_ORG_ADDRESS, orgAddressInfo);

            FacilitySelectionDto selectionDto = newFacRegDto.getFacilitySelectionDto();
            boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
            ParamUtil.setRequestAttr(request, KEY_IS_CF, isCf ? Boolean.TRUE : Boolean.FALSE);
            boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
            ParamUtil.setRequestAttr(request, KEY_IS_UCF, isUcf ? Boolean.TRUE : Boolean.FALSE);
            boolean isRf = MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(selectionDto.getFacClassification());
            ParamUtil.setRequestAttr(request, KEY_IS_RF, isRf ? Boolean.TRUE : Boolean.FALSE);

            // compare facilityProfile
            CompareWrap<FacilityProfileDto> compareFacProfile = new CompareWrap<>(oldFacRegDto.getFacilityProfileDto(), newFacRegDto.getFacilityProfileDto());
            ParamUtil.setRequestAttr(request, COMPARE_FAC_PROFILE, compareFacProfile);

            if (isCf || isUcf) {
                // compare facilityOperator
                CompareWrap<FacilityOperatorDto> compareFacOperator = new CompareWrap<>(oldFacRegDto.getFacilityOperatorDto(), newFacRegDto.getFacilityOperatorDto());
                ParamUtil.setRequestAttr(request, COMPARE_FAC_OPERATOR, compareFacOperator);
            }

            FacilityAdminAndOfficerDto oldFacAdminAndOfficerDto = oldFacRegDto.getFacilityAdminAndOfficerDto();
            FacilityAdminAndOfficerDto newFacAdminAndOfficerDto = newFacRegDto.getFacilityAdminAndOfficerDto();
            // compare mainAdmin
            CompareWrap<EmployeeInfo> compareMainAdmin = new CompareWrap<>(oldFacAdminAndOfficerDto.getMainAdmin(), newFacAdminAndOfficerDto.getMainAdmin());
            ParamUtil.setRequestAttr(request, COMPARE_MAIN_ADMIN, compareMainAdmin);
            // compare alternativeAdmin
            CompareWrap<EmployeeInfo> compareAlterAdmin = new CompareWrap<>(oldFacAdminAndOfficerDto.getAlternativeAdmin(), newFacAdminAndOfficerDto.getAlternativeAdmin());
            ParamUtil.setRequestAttr(request, COMPARE_ALTER_ADMIN, compareAlterAdmin);
            // compare officer list
            List<CompareWrap<EmployeeInfo>> compareOfficers = CompareUtil.compareAndAssembleList(oldFacAdminAndOfficerDto.getOfficerList(), newFacAdminAndOfficerDto.getOfficerList(), EmployeeInfo::getIdNumber, EmployeeInfo.class);
            ParamUtil.setRequestAttr(request, COMPARE_OFFICERS, compareOfficers);

            // compare facility committee
            String oldCommitteeDataRepoId = oldFacRegDto.getDocRecordInfos().stream().filter(docRecordInfo -> docRecordInfo.getDocType().equals("committeeData")).map(DocRecordInfo::getRepoId).findFirst().orElseThrow(() -> new IllegalStateException("no such doc type docRecordInfo"));
            String newCommitteeDataRepoId = newFacRegDto.getDocRecordInfos().stream().filter(docRecordInfo -> docRecordInfo.getDocType().equals("committeeData")).map(DocRecordInfo::getRepoId).findFirst().orElseThrow(() -> new IllegalStateException("no such doc type docRecordInfo"));
            if (!oldCommitteeDataRepoId.equals(newCommitteeDataRepoId)) {
                ParamUtil.setRequestAttr(request, COMPARE_BIO_SAFETY_COMMITTEE_IS_DIFFERENT, true);
            }
            // compare facility authorizer
            String oldAuthorizerDataRepoId = oldFacRegDto.getDocRecordInfos().stream().filter(docRecordInfo -> docRecordInfo.getDocType().equals("authoriserData")).map(DocRecordInfo::getRepoId).findFirst().orElseThrow(() -> new IllegalStateException("no such doc type docRecordInfo"));
            String newAuthorizerDataRepoId = newFacRegDto.getDocRecordInfos().stream().filter(docRecordInfo -> docRecordInfo.getDocType().equals("authoriserData")).map(DocRecordInfo::getRepoId).findFirst().orElseThrow(() -> new IllegalStateException("no such doc type docRecordInfo"));
            if (!oldAuthorizerDataRepoId.equals(newAuthorizerDataRepoId)) {
                ParamUtil.setRequestAttr(request, COMPARE_AUTHORIZER_IS_DIFFERENT, true);
            }

            if (isCf) {
                // compare afc
                CompareWrap<FacilityAfcDto> compareAfc = new CompareWrap<>(oldFacRegDto.getAfcDto(), newFacRegDto.getAfcDto());
                ParamUtil.setRequestAttr(request, COMPARE_AFC, compareAfc);
            } else if (isUcf) {
                // compare bat (Premise: When the applicant does the RFI, the facilityActivity type cannot be changed)
                List<BiologicalAgentToxinDto> oldBatList = new ArrayList<>(oldFacRegDto.getBiologicalAgentToxinMap().values());
                List<BiologicalAgentToxinDto> newBatList = new ArrayList<>(newFacRegDto.getBiologicalAgentToxinMap().values());
                Map<String, List<CompareWrap<BATInfo>>> compareBatMap = Maps.newLinkedHashMapWithExpectedSize(oldBatList.size());
                for (int i = 0; i < oldBatList.size(); i++) {
                    List<BATInfo> oldBatInfos = oldBatList.get(i).getBatInfos();
                    List<BATInfo> newBatInfos = newBatList.get(i).getBatInfos();
                    List<CompareWrap<BATInfo>> compareBatInfos = CompareUtil.compareAndAssembleList(oldBatInfos, newBatInfos, BATInfo::getBatName, BATInfo.class);

                    String activityType = oldBatList.get(i).getActivityType();
                    compareBatMap.put(activityType, compareBatInfos);
                }
                ParamUtil.setRequestAttr(request, COMPARE_BAT_MAP, compareBatMap);
            }

            OtherApplicationInfoDto otherAppInfoDto = oldFacRegDto.getOtherAppInfoDto();
            List<DeclarationItemMainInfo> config = appViewClient.getDeclarationConfigInfoById(otherAppInfoDto.getDeclarationId());
            otherAppInfoDto.setDeclarationConfig(config);
            ParamUtil.setRequestAttr(request, KEY_DECLARATION_CONFIG, otherAppInfoDto.getDeclarationConfig());
            ParamUtil.setRequestAttr(request, KEY_DECLARATION_ANSWER_MAP, otherAppInfoDto.getAnswerMap());

            List<DocSetting> facRegDocSetting = docSettingService.getFacRegDocSettings(selectionDto.getFacClassification(), selectionDto.getActivityTypes());
            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, facRegDocSetting);
            PrimaryDocDto oldPrimaryDocDto = new PrimaryDocDto();
            oldPrimaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(oldFacRegDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> oldSavedFiles = oldPrimaryDocDto.getExistDocTypeMap();

            PrimaryDocDto newPrimaryDocDto = new PrimaryDocDto();
            newPrimaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(newFacRegDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> newSavedFiles = newPrimaryDocDto.getExistDocTypeMap();
            Set<String> newOtherDocTypes = DocSettingService.computeOtherDocTypes(facRegDocSetting, newSavedFiles.keySet());
            ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, newSavedFiles);
            ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, newOtherDocTypes);

            // compare doc (Premise: When the applicant does the RFI, the doc type cannot be changed)
            Map<String, List<CompareWrap<DocRecordInfo>>> compareDocMap = Maps.newLinkedHashMapWithExpectedSize(oldSavedFiles.size());
            Set<Map.Entry<String, List<DocRecordInfo>>> docSet = oldSavedFiles.entrySet();
            for (Map.Entry<String, List<DocRecordInfo>> entry : docSet) {
                String docType = entry.getKey();
                List<DocRecordInfo> oldDocInfos = oldSavedFiles.get(docType);
                List<DocRecordInfo> newDocInfos = newSavedFiles.get(docType);
                List<CompareWrap<DocRecordInfo>> compareDocs = CompareUtil.compareAndAssembleList(oldDocInfos, newDocInfos, DocRecordInfo::getRepoId, DocRecordInfo.class);
                compareDocMap.put(docType, compareDocs);
            }
            ParamUtil.setRequestAttr(request, COMPARE_DOC_MAP, compareDocMap);

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

//            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, docSettingService.getApprovalAppDocSettings());
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

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, docSettingService.getFacCerRegDocSettings());
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

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, docSettingService.getAttachmentsDocSettings());
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

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, docSettingService.getAttachmentsDocSettings());
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

            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, docSettingService.getAttachmentsDocSettings());
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
            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, docSettingService.getDataSubmissionDocSettings());
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

    public void retrieveWithdrawnData(HttpServletRequest request, String applicationId){
        ResponseDto<ViewWithdrawnDto> responseDto = appViewClient.getApplicantSubmitWithdrawDataByAppId(applicationId);
        if (responseDto.ok()){
            ViewWithdrawnDto viewWithdrawnDto = responseDto.getEntity();
            ParamUtil.setRequestAttr(request, "viewWithdrawnDto", viewWithdrawnDto);
            //show applicant submit doc
            List<DocDisplayDto> supportDocDisplayDto = applicationDocClient.getApplicationDocForDisplay(applicationId);
            ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, supportDocDisplayDto);
            //provide for download support doc
            Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(supportDocDisplayDto);
            ParamUtil.setSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP, (Serializable) repoIdDocNameMap);
        }
    }
}
