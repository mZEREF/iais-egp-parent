package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.AppViewClient;
import sg.gov.moh.iais.egp.bsb.client.ApplicationDocClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationInfoClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.ResponseConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.deferrenew.DeferRenewViewDto;
import sg.gov.moh.iais.egp.bsb.dto.compare.CompareWrap;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToLargeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToPossessDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.FacAuthorisedDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.FacProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.OtherApplicationInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.ViewWithdrawnDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.CompareUtil;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_ADHOC_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_FAC_CERTIFIER_REG;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_FAC_REG;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_RENEW_DEFER;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_AFC;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_ALTER_ADMIN;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_AUTHORIZER_IS_DIFFERENT;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_BAT_INFO_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_BAT_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_BIO_SAFETY_COMMITTEE_IS_DIFFERENT;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_DOC_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_FACILITY_AUTHORISER_DTO_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_FAC_OPERATOR;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_FAC_PROFILE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_MAIN_ADMIN;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_OFFICERS;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_SATH_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.COMPARE_WORK_ACTIVITY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_BAT_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_BAT_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_DECLARATION_ANSWER_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_DECLARATION_CONFIG;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_DOC_SETTINGS;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_FACILITY_REGISTRATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_FAC_AUTHORISED_PERSON_LIST_WANTED;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_FAC_PROFILE_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_FILE_MAP_SAVED;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_IS_CF;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_IS_FIFTH_RF;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_IS_PV_RF;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_IS_RF;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_IS_UCF;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_ORG_ADDRESS;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_OTHER_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_SELECTED_ACTIVITIES;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_SELECTED_CLASSIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.MASK_PARAM_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_AFC;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_FAC_ADMIN_OFFICER;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_FAC_OPERATOR;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.NODE_NAME_FAC_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_PAGE_APP_EDIT_SELECT_DTO;


@Service
@Slf4j
@RequiredArgsConstructor
public class AppViewService {
    private static final String LOG_ERROR_NO_MATCH_PROCESS_TYPE = "no match processType {}";
    private static final String LOG_ERROR_NO_SUCH_DOC_TYPE_DOC_RECORD_INFO = "no such doc type docRecordInfo";

    private final AppViewClient appViewClient;
    private final DocSettingService docSettingService;
    private final OrganizationInfoClient orgInfoClient;
    private final ApplicationDocClient applicationDocClient;

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(MASK_PARAM_APP_ID);
        String appId = MaskHelper.unmask(MASK_PARAM_APP_ID, maskedAppId);
        ParamUtil.setSessionAttr(request, MASK_PARAM_APP_ID, appId);
    }

    public void retrieveDataByProcessType(HttpServletRequest request, String appId, String processType, boolean isRfc) {
        switch (processType) {
            case PROCESS_TYPE_FAC_REG:
            case PROCESS_TYPE_ADHOC_INSPECTION:
                retrieveFacReg(request, appId, isRfc);
                break;
            case PROCESS_TYPE_APPROVE_POSSESS:
            case PROCESS_TYPE_APPROVE_LSP:
            case PROCESS_TYPE_SP_APPROVE_HANDLE:
            case PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                retrieveApprovalBatAndActivity(request, appId);
                break;
            case PROCESS_TYPE_FAC_CERTIFIER_REG:
                retrieveFacCerReg(request, appId);
                break;
            case PROCESS_TYPE_RENEW_DEFER:
                retrieveDeferRenew(request, appId);
                break;
            default:
                log.error(LOG_ERROR_NO_MATCH_PROCESS_TYPE, StringUtils.normalizeSpace(processType));
                break;
        }
    }

    public void retrieveHasRfiDataByProcessType(HttpServletRequest request, String appId, String taskId, String processType) {
        switch (processType) {
            case PROCESS_TYPE_FAC_REG:
                FacilityRegisterDto oldFacRegDto = appViewClient.getRfiOldFacilityRegistrationData(appId, taskId).getEntity();
                retrieveCompareFacRegDto(request, appId, oldFacRegDto);
                break;
            case PROCESS_TYPE_APPROVE_POSSESS:
            case PROCESS_TYPE_APPROVE_LSP:
            case PROCESS_TYPE_SP_APPROVE_HANDLE:
            case PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                ApprovalBatAndActivityDto oldApprovalAppDto = appViewClient.getRfiOldApprovalBatAndActivityData(appId, taskId).getEntity();
                retrieveCompareApprovalBatAndActivity(request, appId, oldApprovalAppDto);
                break;
            case PROCESS_TYPE_FAC_CERTIFIER_REG:
                // doesn't do now
                retrieveFacCerReg(request, appId);
                break;
            default:
                log.error(LOG_ERROR_NO_MATCH_PROCESS_TYPE, StringUtils.normalizeSpace(processType));
                break;
        }
        ParamUtil.setRequestAttr(request, "needCompare", true);
    }

    public void retrieveDoRfiDataByProcessType(HttpServletRequest request, String appId, String processType, boolean isRfc) {
        PageAppEditSelectDto facilityPageAppEditSelectDto;
        switch (processType) {
            case PROCESS_TYPE_FAC_REG:
                retrieveFacReg(request, appId, isRfc);
                facilityPageAppEditSelectDto = getFacilityPageAppEditSelectDto(request);
                break;
            case PROCESS_TYPE_APPROVE_POSSESS:
            case PROCESS_TYPE_APPROVE_LSP:
            case PROCESS_TYPE_SP_APPROVE_HANDLE:
            case PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                retrieveApprovalBatAndActivity(request, appId);
                facilityPageAppEditSelectDto = getApprovalAppOrAFCPageAppEditSelectDto(request);
                break;
            case PROCESS_TYPE_FAC_CERTIFIER_REG:
                retrieveFacCerReg(request, appId);
                facilityPageAppEditSelectDto = getApprovalAppOrAFCPageAppEditSelectDto(request);
                break;
            case PROCESS_TYPE_RENEW_DEFER:
                retrieveDeferRenew(request, appId);
                facilityPageAppEditSelectDto = getDeferRenewPageAppEditSelectDto(request);
                break;
            default:
                facilityPageAppEditSelectDto = null;
                log.error(LOG_ERROR_NO_MATCH_PROCESS_TYPE, StringUtils.normalizeSpace(processType));
                break;
        }
        ParamUtil.setSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO, facilityPageAppEditSelectDto);
    }

    //get Rfc new and old data to compare to show
    public void retrieveRfcDataByProcessType(HttpServletRequest request, String appId, String processType) {
        switch (processType) {
            case PROCESS_TYPE_FAC_REG:
                FacilityRegisterDto oldFacRegDto = appViewClient.getRfcOldFacRegDtoByAppId(appId).getEntity();
                retrieveCompareFacRegDto(request, appId, oldFacRegDto);
                break;
            case PROCESS_TYPE_APPROVE_POSSESS:
            case PROCESS_TYPE_APPROVE_LSP:
            case PROCESS_TYPE_SP_APPROVE_HANDLE:
            case PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                ApprovalBatAndActivityDto oldApprovalAppDto = appViewClient.retrieveRfcOldApprovalAppAppDataByApplicationId(appId).getEntity();
                retrieveCompareApprovalBatAndActivity(request, appId, oldApprovalAppDto);
                break;
            case PROCESS_TYPE_FAC_CERTIFIER_REG:
                // doesn't do now
                retrieveFacCerReg(request, appId);
                break;
            default:
                log.error(LOG_ERROR_NO_MATCH_PROCESS_TYPE, StringUtils.normalizeSpace(processType));
                break;
        }
        ParamUtil.setRequestAttr(request, "needCompare", true);
    }

    /**
     * retrieve new facility registration view data
     */
    public void retrieveFacReg(HttpServletRequest request, String applicationId, boolean isRfc) {
        // retrieve app data of facility registration
        ResponseDto<FacilityRegisterDto> resultDto = appViewClient.getFacRegDtoByAppId(applicationId);
        if (resultDto.ok()) {
            FacilityRegisterDto facilityRegisterDto = resultDto.getEntity();
            processFacilityRefisterDto(request, isRfc, facilityRegisterDto);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    public void prepareViewFacility(HttpServletRequest request, String facId){
        ResponseDto<FacilityRegisterDto> resultDto = appViewClient.getFacRegDtoByFacId(facId);
        if (resultDto.ok()) {
            FacilityRegisterDto facilityRegisterDto = resultDto.getEntity();
            processFacilityRefisterDto(request, false, facilityRegisterDto);
            ParamUtil.setRequestAttr(request, "isFacility", Boolean.TRUE);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    private void processFacilityRefisterDto(HttpServletRequest request, boolean isRfc, FacilityRegisterDto facilityRegisterDto) {
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
        String facClassification = selectionDto.getFacClassification();
        List<String> activityTypes = selectionDto.getActivityTypes();
        ParamUtil.setRequestAttr(request, KEY_SELECTED_CLASSIFICATION, facClassification);
        ParamUtil.setRequestAttr(request, KEY_SELECTED_ACTIVITIES, activityTypes);

        boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        ParamUtil.setRequestAttr(request, KEY_IS_CF, isCf ? Boolean.TRUE : Boolean.FALSE);
        boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        ParamUtil.setRequestAttr(request, KEY_IS_UCF, isUcf ? Boolean.TRUE : Boolean.FALSE);
        boolean isRf = MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(selectionDto.getFacClassification());
        ParamUtil.setRequestAttr(request, KEY_IS_RF, isRf ? Boolean.TRUE : Boolean.FALSE);
        boolean isFifthRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED.equals(selectionDto.getActivityTypes().get(0));
        ParamUtil.setRequestAttr(request, KEY_IS_FIFTH_RF, isFifthRf ? Boolean.TRUE : Boolean.FALSE);
        boolean isPvRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(selectionDto.getActivityTypes().get(0));
        ParamUtil.setRequestAttr(request, KEY_IS_PV_RF, isPvRf ? Boolean.TRUE : Boolean.FALSE);

        FacilityProfileDto profileDto = facilityRegisterDto.getFacilityProfileDto();
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, profileDto);
        if (isCf || isUcf) {
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, facilityRegisterDto.getFacilityOperatorDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_AFC, facilityRegisterDto.getAfcDto());
        }
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN_OFFICER, facilityRegisterDto.getFacilityAdminAndOfficerDto());

        // rfc no need show bat
        if (!isRfc && (isUcf || isFifthRf)) {
            List<BiologicalAgentToxinDto> batList = new ArrayList<>(facilityRegisterDto.getBiologicalAgentToxinMap().values());
            ParamUtil.setRequestAttr(request, KEY_BAT_LIST, batList);
        }

        OtherApplicationInfoDto otherAppInfoDto = facilityRegisterDto.getOtherAppInfoDto();
        List<DeclarationItemMainInfo> config = appViewClient.getDeclarationConfigInfoById(otherAppInfoDto.getDeclarationId());
        otherAppInfoDto.setDeclarationConfig(config);
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_CONFIG, otherAppInfoDto.getDeclarationConfig());
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_ANSWER_MAP, otherAppInfoDto.getAnswerMap());

        List<DocSetting> facRegDocSetting = docSettingService.getFacRegDocSettings(selectionDto.getFacClassification(), selectionDto.getActivityTypes(), profileDto.getInfoList().size());
        ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, facRegDocSetting);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setPvRf(isPvRf);
        Map<Integer, String> facilityNameIndexMap = Maps.newLinkedHashMapWithExpectedSize(profileDto.getInfoList().size());
        for (int i = 0; i < profileDto.getInfoList().size(); i++) {
            FacilityProfileInfo profileInfo = profileDto.getInfoList().get(i);
            facilityNameIndexMap.put(i, profileInfo.getFacName());
        }
        primaryDocDto.setFacilityNameIndexMap(facilityNameIndexMap);
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, facilityRegisterDto.getDocRecordInfos()));
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, savedFiles);
        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(facRegDocSetting, savedFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);
    }

    /**
     * retrieve rfi facility registration view data
     */
    public void retrieveCompareFacRegDto(HttpServletRequest request, String applicationId, FacilityRegisterDto oldFacRegDto) {
        // retrieve app data of facility registration
        ResponseDto<FacilityRegisterDto> resultDto = appViewClient.getFacRegDtoByAppId(applicationId);
        if (resultDto.ok()) {
            FacilityRegisterDto newFacRegDto = resultDto.getEntity();
            compareFacility(request, oldFacRegDto, newFacRegDto);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    /**
     * retrieve new approval bat and activity view data
     */
    public void retrieveApprovalBatAndActivity(HttpServletRequest request, String applicationId) {
        ResponseDto<ApprovalBatAndActivityDto> resultDto = appViewClient.getApprovalBatAndActivityDtoByAppId(applicationId);
        if (resultDto.ok()) {
            ApprovalBatAndActivityDto approvalBatAndActivityDto = resultDto.getEntity();

            FacProfileDto facProfileDto = approvalBatAndActivityDto.getFacProfileDto();
            ParamUtil.setRequestAttr(request, KEY_FAC_PROFILE_DTO, facProfileDto);

            String processType = approvalBatAndActivityDto.getApprovalSelectionDto().getProcessType();
            ParamUtil.setRequestAttr(request, KEY_PROCESS_TYPE, processType);

            switch (processType) {
                case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                    ApprovalToPossessDto dto = approvalBatAndActivityDto.getApprovalToPossessDto();
                    ParamUtil.setRequestAttr(request, KEY_BAT_INFO, dto);
                    break;
                case PROCESS_TYPE_APPROVE_LSP:
                    ApprovalToLargeDto lspDto = approvalBatAndActivityDto.getApprovalToLargeDto();
                    ParamUtil.setRequestAttr(request, KEY_BAT_INFO, lspDto);
                    break;
                case PROCESS_TYPE_SP_APPROVE_HANDLE:
                    ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalBatAndActivityDto.getApprovalToSpecialDto());
                    FacAuthorisedDto facAuthorisedDto = approvalBatAndActivityDto.getFacAuthorisedDto();
                    List<String> authIdList = facAuthorisedDto.getAuthorisedPersonIdList();
                    List<FacilityAuthoriserDto> authDtoList = appViewClient.getAuthorisedPersonnelByAuthIds(authIdList);
                    ParamUtil.setRequestAttr(request, KEY_FAC_AUTHORISED_PERSON_LIST_WANTED, authDtoList);
                    break;
                case PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                    ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalBatAndActivityDto.getApprovalToActivityDto());
                    break;
                default:
                    log.error(LOG_ERROR_NO_MATCH_PROCESS_TYPE, StringUtils.normalizeSpace(processType));
                    break;
            }
            List<DocSetting> approvalAppDocSettings = docSettingService.getApprovalAppDocSettings(processType);
            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, approvalAppDocSettings);
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, approvalBatAndActivityDto.getDocRecordInfos()));
            Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, savedFiles);

            Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(approvalAppDocSettings, savedFiles.keySet());
            ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    public void retrieveCompareApprovalBatAndActivity(HttpServletRequest request, String applicationId, ApprovalBatAndActivityDto oldApprovalBatAndActivityDto) {
        ResponseDto<ApprovalBatAndActivityDto> resultDto = appViewClient.getApprovalBatAndActivityDtoByAppId(applicationId);
        if (resultDto.ok()) {
            compareBatAndActivity(request, oldApprovalBatAndActivityDto, resultDto.getEntity());
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    /**
     * retrieve new facility certifier registration view data
     */
    public void retrieveFacCerReg(HttpServletRequest request, String applicationId) {
        // do nothing now
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

    public PageAppEditSelectDto getFacilityPageAppEditSelectDto(HttpServletRequest request) {
        PageAppEditSelectDto dto = (PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO);
        if (dto == null) {
            dto = new PageAppEditSelectDto(false, false, false, false);
        }
        return dto;
    }

    public PageAppEditSelectDto getApprovalAppOrAFCPageAppEditSelectDto(HttpServletRequest request) {
        PageAppEditSelectDto dto = (PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO);
        if (dto == null) {
            dto = new PageAppEditSelectDto(false, false);
        }
        return dto;
    }

    public PageAppEditSelectDto getDeferRenewPageAppEditSelectDto(HttpServletRequest request) {
        PageAppEditSelectDto dto = (PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO);
        if (dto == null) {
            dto = new PageAppEditSelectDto(false);
        }
        return dto;
    }

    private void compareFacility(HttpServletRequest request, FacilityRegisterDto oldFacRegDto, FacilityRegisterDto newFacRegDto) {
        ParamUtil.setRequestAttr(request, KEY_FACILITY_REGISTRATION_DTO, newFacRegDto);
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
        ParamUtil.setRequestAttr(request, KEY_ORG_ADDRESS, orgAddressInfo);

        FacilitySelectionDto selectionDto = newFacRegDto.getFacilitySelectionDto();
        String facClassification = selectionDto.getFacClassification();
        List<String> activityTypes = selectionDto.getActivityTypes();
        ParamUtil.setRequestAttr(request, KEY_SELECTED_CLASSIFICATION, facClassification);
        ParamUtil.setRequestAttr(request, KEY_SELECTED_ACTIVITIES, activityTypes);

        boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        ParamUtil.setRequestAttr(request, KEY_IS_CF, isCf ? Boolean.TRUE : Boolean.FALSE);
        boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        ParamUtil.setRequestAttr(request, KEY_IS_UCF, isUcf ? Boolean.TRUE : Boolean.FALSE);
        boolean isRf = MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(selectionDto.getFacClassification());
        ParamUtil.setRequestAttr(request, KEY_IS_RF, isRf ? Boolean.TRUE : Boolean.FALSE);
        boolean isFifthRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED.equals(selectionDto.getActivityTypes().get(0));
        ParamUtil.setRequestAttr(request, KEY_IS_FIFTH_RF, isFifthRf ? Boolean.TRUE : Boolean.FALSE);
        boolean isPvRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(selectionDto.getActivityTypes().get(0));
        ParamUtil.setRequestAttr(request, KEY_IS_PV_RF, isPvRf ? Boolean.TRUE : Boolean.FALSE);

        // compare facilityProfile
        List<FacilityProfileInfo> oldFacilityProfileInfos = oldFacRegDto.getFacilityProfileDto().getInfoList();
        List<FacilityProfileInfo> newFacilityProfileInfos = newFacRegDto.getFacilityProfileDto().getInfoList();
        int size = Math.max(oldFacilityProfileInfos.size(), newFacilityProfileInfos.size());
        List<CompareWrap<FacilityProfileInfo>> compareFacProfiles = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            CompareWrap<FacilityProfileInfo> facProfileCompareWrap = new CompareWrap<>(oldFacilityProfileInfos.get(i), newFacilityProfileInfos.get(i));
            compareFacProfiles.add(facProfileCompareWrap);
        }
        ParamUtil.setRequestAttr(request, COMPARE_FAC_PROFILE_LIST, compareFacProfiles);

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

        if (!isRf) {
            // compare facility committee
            String oldCommitteeDataRepoId = oldFacRegDto.getDocRecordInfos().stream().filter(docRecordInfo -> docRecordInfo.getDocType().equals("committeeData")).map(DocRecordInfo::getRepoId).findFirst().orElseThrow(() -> new IllegalStateException(LOG_ERROR_NO_SUCH_DOC_TYPE_DOC_RECORD_INFO));
            String newCommitteeDataRepoId = newFacRegDto.getDocRecordInfos().stream().filter(docRecordInfo -> docRecordInfo.getDocType().equals("committeeData")).map(DocRecordInfo::getRepoId).findFirst().orElseThrow(() -> new IllegalStateException(LOG_ERROR_NO_SUCH_DOC_TYPE_DOC_RECORD_INFO));
            if (!oldCommitteeDataRepoId.equals(newCommitteeDataRepoId)) {
                ParamUtil.setRequestAttr(request, COMPARE_BIO_SAFETY_COMMITTEE_IS_DIFFERENT, true);
            }
            // compare facility authorizer
            String oldAuthorizerDataRepoId = oldFacRegDto.getDocRecordInfos().stream().filter(docRecordInfo -> docRecordInfo.getDocType().equals("authoriserData")).map(DocRecordInfo::getRepoId).findFirst().orElseThrow(() -> new IllegalStateException(LOG_ERROR_NO_SUCH_DOC_TYPE_DOC_RECORD_INFO));
            String newAuthorizerDataRepoId = newFacRegDto.getDocRecordInfos().stream().filter(docRecordInfo -> docRecordInfo.getDocType().equals("authoriserData")).map(DocRecordInfo::getRepoId).findFirst().orElseThrow(() -> new IllegalStateException(LOG_ERROR_NO_SUCH_DOC_TYPE_DOC_RECORD_INFO));
            if (!oldAuthorizerDataRepoId.equals(newAuthorizerDataRepoId)) {
                ParamUtil.setRequestAttr(request, COMPARE_AUTHORIZER_IS_DIFFERENT, true);
            }
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

        List<DocSetting> facRegDocSetting = docSettingService.getFacRegDocSettings(selectionDto.getFacClassification(), selectionDto.getActivityTypes(), oldFacilityProfileInfos.size());
        ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, facRegDocSetting);
        FacilityProfileDto oldProfile = oldFacRegDto.getFacilityProfileDto();
        PrimaryDocDto oldPrimaryDocDto = new PrimaryDocDto();
        oldPrimaryDocDto.setPvRf(isPvRf);
        Map<Integer, String> oldFacilityNameIndexMap = Maps.newLinkedHashMapWithExpectedSize(oldFacilityProfileInfos.size());
        for (int i = 0; i < oldProfile.getInfoList().size(); i++) {
            FacilityProfileInfo profileInfo = oldProfile.getInfoList().get(i);
            oldFacilityNameIndexMap.put(i, profileInfo.getFacName());
        }
        oldPrimaryDocDto.setFacilityNameIndexMap(oldFacilityNameIndexMap);
        oldPrimaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, oldFacRegDto.getDocRecordInfos()));
        Map<String, List<DocRecordInfo>> oldSavedFiles = oldPrimaryDocDto.getExistDocTypeMap();

        PrimaryDocDto newPrimaryDocDto = new PrimaryDocDto();
        FacilityProfileDto newProfile = newFacRegDto.getFacilityProfileDto();
        newPrimaryDocDto.setPvRf(isPvRf);
        Map<Integer, String> newFacNameIndexMap = Maps.newLinkedHashMapWithExpectedSize(newFacilityProfileInfos.size());
        for (int i = 0; i < newProfile.getInfoList().size(); i++) {
            FacilityProfileInfo profileInfo = newProfile.getInfoList().get(i);
            newFacNameIndexMap.put(i, profileInfo.getFacName());
        }
        newPrimaryDocDto.setFacilityNameIndexMap(newFacNameIndexMap);
        newPrimaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, newFacRegDto.getDocRecordInfos()));
        Map<String, List<DocRecordInfo>> newSavedFiles = newPrimaryDocDto.getExistDocTypeMap();
        Set<String> newTypeSet = newSavedFiles.keySet();
        Set<String> newOtherDocTypes = DocSettingService.computeOtherDocTypes(facRegDocSetting, newTypeSet);
        ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, newSavedFiles);
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, newOtherDocTypes);

        // compare doc (Premise: When the applicant does the RFI, the doc type cannot be changed)
        Map<String, List<CompareWrap<DocRecordInfo>>> compareDocMap = Maps.newLinkedHashMapWithExpectedSize(oldSavedFiles.size());
        Set<String> oldTypeSet = oldSavedFiles.keySet();
        Set<String> allTypeSet = new HashSet<>(newTypeSet);
        allTypeSet.addAll(oldTypeSet);
        for (String docType : allTypeSet) {
            List<DocRecordInfo> oldDocInfos = oldSavedFiles.get(docType);
            List<DocRecordInfo> newDocInfos = newSavedFiles.get(docType);
            List<CompareWrap<DocRecordInfo>> compareDocs = CompareUtil.compareAndAssembleList(oldDocInfos, newDocInfos, DocRecordInfo::getRepoId, DocRecordInfo.class);
            compareDocMap.put(docType, compareDocs);
        }
        ParamUtil.setRequestAttr(request, COMPARE_DOC_MAP, compareDocMap);
    }

    private void compareBatAndActivity(HttpServletRequest request, ApprovalBatAndActivityDto oldApprovalBatAndActivityDto, ApprovalBatAndActivityDto newApprovalBatAndActivityDto) {
        FacProfileDto facProfileDto = newApprovalBatAndActivityDto.getFacProfileDto();
        ParamUtil.setRequestAttr(request, KEY_FAC_PROFILE_DTO, facProfileDto);

        String processType = newApprovalBatAndActivityDto.getApprovalSelectionDto().getProcessType();
        ParamUtil.setRequestAttr(request, KEY_PROCESS_TYPE, processType);

        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS: {
                ApprovalToPossessDto newAtp = newApprovalBatAndActivityDto.getApprovalToPossessDto();
                ApprovalToPossessDto oldAtp = oldApprovalBatAndActivityDto.getApprovalToPossessDto();
                List<CompareWrap<BATInfo>> compareBatInfos = CompareUtil.compareAndAssembleList(oldAtp.getBatInfos(), newAtp.getBatInfos(), BATInfo::getBatEntityId, BATInfo.class);
                ParamUtil.setRequestAttr(request, COMPARE_BAT_INFO_LIST, compareBatInfos);
                break;
            }
            case PROCESS_TYPE_APPROVE_LSP: {
                ApprovalToLargeDto newLspDto = newApprovalBatAndActivityDto.getApprovalToLargeDto();
                ApprovalToLargeDto oldLspDto = oldApprovalBatAndActivityDto.getApprovalToLargeDto();
                List<CompareWrap<BATInfo>> compareBatInfos = CompareUtil.compareAndAssembleList(oldLspDto.getBatInfos(), newLspDto.getBatInfos(), BATInfo::getBatEntityId, BATInfo.class);
                ParamUtil.setRequestAttr(request, COMPARE_BAT_INFO_LIST, compareBatInfos);
                break;
            }
            case PROCESS_TYPE_SP_APPROVE_HANDLE: {
                ApprovalToSpecialDto newSath = newApprovalBatAndActivityDto.getApprovalToSpecialDto();
                ApprovalToSpecialDto oldSath = oldApprovalBatAndActivityDto.getApprovalToSpecialDto();
                CompareWrap<ApprovalToSpecialDto> compareSath = new CompareWrap<>(oldSath, newSath);
                ParamUtil.setRequestAttr(request, COMPARE_SATH_DTO, compareSath);
                List<ApprovalToSpecialDto.WorkActivity> newWorkActivities = newSath.getWorkActivities();
                List<ApprovalToSpecialDto.WorkActivity> oldWorkActivities = oldSath.getWorkActivities();
                int size = Math.max(newWorkActivities.size(), oldWorkActivities.size());
                List<CompareWrap<ApprovalToSpecialDto.WorkActivity>> compareWorkActivityList = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    CompareWrap<ApprovalToSpecialDto.WorkActivity> workActivityCompareWrap = new CompareWrap<>(oldWorkActivities.get(i), newWorkActivities.get(i));
                    compareWorkActivityList.add(workActivityCompareWrap);
                }
                ParamUtil.setRequestAttr(request, COMPARE_WORK_ACTIVITY_LIST, compareWorkActivityList);
                //
                FacAuthorisedDto newFacAuthorisedDto = newApprovalBatAndActivityDto.getFacAuthorisedDto();
                List<String> newAuthIdList = newFacAuthorisedDto.getAuthorisedPersonIdList();
                List<FacilityAuthoriserDto> newAuthDtoList = appViewClient.getAuthorisedPersonnelByAuthIds(newAuthIdList);

                FacAuthorisedDto oldFacAuthorisedDto = oldApprovalBatAndActivityDto.getFacAuthorisedDto();
                List<String> oldAuthIdList = oldFacAuthorisedDto.getAuthorisedPersonIdList();
                List<FacilityAuthoriserDto> oldAuthDtoList = appViewClient.getAuthorisedPersonnelByAuthIds(oldAuthIdList);

                List<CompareWrap<FacilityAuthoriserDto>> compareFacAuthorisers = CompareUtil.compareAndAssembleList(oldAuthDtoList, newAuthDtoList, FacilityAuthoriserDto::getId, FacilityAuthoriserDto.class);
                ParamUtil.setRequestAttr(request, COMPARE_FACILITY_AUTHORISER_DTO_LIST, compareFacAuthorisers);
                break;
            }
            default:
                log.error(LOG_ERROR_NO_MATCH_PROCESS_TYPE, StringUtils.normalizeSpace(processType));
                break;
        }
        List<DocSetting> approvalAppDocSettings = docSettingService.getApprovalAppDocSettings(processType);
        ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, approvalAppDocSettings);

        PrimaryDocDto oldPrimaryDocDto = new PrimaryDocDto();
        oldPrimaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, oldApprovalBatAndActivityDto.getDocRecordInfos()));
        Map<String, List<DocRecordInfo>> oldSavedFiles = oldPrimaryDocDto.getExistDocTypeMap();

        PrimaryDocDto newPrimaryDocDto = new PrimaryDocDto();
        newPrimaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, newApprovalBatAndActivityDto.getDocRecordInfos()));
        Map<String, List<DocRecordInfo>> newSavedFiles = newPrimaryDocDto.getExistDocTypeMap();
        ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, newSavedFiles);

        Set<String> newTypeSet = newSavedFiles.keySet();
        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(approvalAppDocSettings, newTypeSet);
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);

        // compare doc (Premise: When the applicant does the RFI, the doc type cannot be changed)
        Map<String, List<CompareWrap<DocRecordInfo>>> compareDocMap = Maps.newLinkedHashMapWithExpectedSize(oldSavedFiles.size());
        Set<String> oldTypeSet = oldSavedFiles.keySet();
        Set<String> allTypeSet = new HashSet<>(newTypeSet);
        allTypeSet.addAll(oldTypeSet);
        for (String docType : allTypeSet) {
            List<DocRecordInfo> oldDocInfos = oldSavedFiles.get(docType);
            List<DocRecordInfo> newDocInfos = newSavedFiles.get(docType);
            List<CompareWrap<DocRecordInfo>> compareDocs = CompareUtil.compareAndAssembleList(oldDocInfos, newDocInfos, DocRecordInfo::getRepoId, DocRecordInfo.class);
            compareDocMap.put(docType, compareDocs);
        }
        ParamUtil.setRequestAttr(request, COMPARE_DOC_MAP, compareDocMap);
    }

    /**
     * retrieve new facility registration view data
     */
    public void retrieveDeferRenew(HttpServletRequest request, String applicationId) {
        // retrieve app data of facility registration
        ResponseDto<DeferRenewViewDto> resultDto = appViewClient.getDeferRenewDataByAppId(applicationId);
        if (resultDto.ok()) {
            DeferRenewViewDto deferRenewViewDto = resultDto.getEntity();
            processDeferRenewViewDto(request, deferRenewViewDto);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    private void processDeferRenewViewDto(HttpServletRequest request, DeferRenewViewDto deferRenewViewDto) {
        ParamUtil.setRequestAttr(request, "deferRenewViewDto", deferRenewViewDto);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, deferRenewViewDto.getSavedDocInfos()));
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, savedFiles);
        ParamUtil.setRequestAttr(request, "docTypes", savedFiles.keySet());
    }
}
