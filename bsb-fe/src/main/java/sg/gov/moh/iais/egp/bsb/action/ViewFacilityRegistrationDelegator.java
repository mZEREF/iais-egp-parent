package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.OtherApplicationInfoDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService;
import sg.gov.moh.iais.egp.bsb.service.ViewAppService;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_FACILITY_REGISTRATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_VIEW_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_EXPAND_FILE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_BAT_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DECLARATION_ANSWER_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DECLARATION_CONFIG;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DOC_SETTINGS;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_FILE_MAP_SAVED;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_INDEED_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_CF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_FIFTH_RF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_PV_RF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_RF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_UCF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OTHER_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_AFC;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_ADMIN_OFFICER;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_OPERATOR;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.INBOX_APP_URL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.INBOX_FAC_URL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.INBOX_MSG_URL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_FACILITY_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_FACILITY_REGISTRATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_GO_BACK_URL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_SELECTED_ACTIVITIES;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_SELECTED_CLASSIFICATION;

@Delegator(value = "bsbViewFacRegAppDelegator")
@Slf4j
@RequiredArgsConstructor
public class ViewFacilityRegistrationDelegator {
    private final FacilityRegisterClient facRegClient;
    private final OrganizationInfoService organizationInfoService;
    private final DocSettingService docSettingService;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_FACILITY_ID);
        session.removeAttribute(KEY_FACILITY_REGISTRATION_DTO);
        AuditTrailHelper.auditFunction(MODULE_VIEW_APPLICATION, FUNCTION_FACILITY_REGISTRATION);
    }

    public void init(BaseProcessClass bpc) {
        ViewAppService.init(bpc);
    }

    public FacilityRegisterDto getFacilityRegisterDto(HttpServletRequest request, String appId, String facilityId) {
        FacilityRegisterDto facilityRegisterDto = (FacilityRegisterDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_REGISTRATION_DTO);
        if (facilityRegisterDto == null) {
            if (StringUtils.hasLength(appId)) {
                ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getFacilityRegistrationAppDataByApplicationId(appId);
                if (!resultDto.ok()) {
                    throw new IaisRuntimeException("Fail to retrieve app data");
                }
                facilityRegisterDto = resultDto.getEntity();
                ParamUtil.setSessionAttr(request, KEY_GO_BACK_URL, INBOX_APP_URL);
            } else if (StringUtils.hasLength(facilityId)) {
                ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getRfcFacilityRegistrationAppDataByFacilityId(facilityId);
                if (!resultDto.ok()) {
                    throw new IaisRuntimeException("Fail to retrieve app data");
                }
                facilityRegisterDto = resultDto.getEntity();
                ParamUtil.setSessionAttr(request, KEY_GO_BACK_URL, INBOX_FAC_URL);
            } else {
                facilityRegisterDto = new FacilityRegisterDto();
                ParamUtil.setSessionAttr(request, KEY_GO_BACK_URL, INBOX_MSG_URL);
            }
            ParamUtil.setSessionAttr(request, KEY_FACILITY_REGISTRATION_DTO, facilityRegisterDto);
        }
        return facilityRegisterDto;
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String facilityId = (String) ParamUtil.getSessionAttr(request, KEY_FACILITY_ID);
        FacilityRegisterDto facilityRegisterDto = getFacilityRegisterDto(request, appId, facilityId);

        OrgAddressInfo orgAddressInfo = organizationInfoService.getAddressByUen(facilityRegisterDto.getUen());
        ParamUtil.setSessionAttr(request, OrganizationInfoService.KEY_ORG_ADDRESS, orgAddressInfo);

        FacilitySelectionDto selectionDto = facilityRegisterDto.getFacilitySelectionDto();
        String facClassification = selectionDto.getFacClassification();
        List<String> activityTypes = selectionDto.getActivityTypes();
        ParamUtil.setRequestAttr(request, KEY_SELECTED_CLASSIFICATION, facClassification);
        ParamUtil.setRequestAttr(request, KEY_SELECTED_ACTIVITIES, activityTypes);

        boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(facClassification);
        ParamUtil.setRequestAttr(request, KEY_IS_CF, isCf ? Boolean.TRUE : Boolean.FALSE);
        boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(facClassification);
        ParamUtil.setRequestAttr(request, KEY_IS_UCF, isUcf ? Boolean.TRUE : Boolean.FALSE);
        boolean isRf = MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(facClassification);
        ParamUtil.setRequestAttr(request, KEY_IS_RF, isRf ? Boolean.TRUE : Boolean.FALSE);
        boolean isFifthRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED.equals(activityTypes.get(0));
        ParamUtil.setRequestAttr(request, KEY_IS_FIFTH_RF, isFifthRf ? Boolean.TRUE : Boolean.FALSE);
        boolean isPvRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(activityTypes.get(0));
        ParamUtil.setRequestAttr(request, KEY_IS_PV_RF, isPvRf ? Boolean.TRUE : Boolean.FALSE);


        FacilityProfileDto profileDto = facilityRegisterDto.getFacilityProfileDto();
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, profileDto);
        if (isCf || isUcf) {
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, facilityRegisterDto.getFacilityOperatorDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_AFC, facilityRegisterDto.getAfcDto());
        }
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN_OFFICER, facilityRegisterDto.getFacilityAdminAndOfficerDto());

        if (isUcf || isFifthRf) {
            List<BiologicalAgentToxinDto> batList = new ArrayList<>(facilityRegisterDto.getBiologicalAgentToxinMap().values());
            ParamUtil.setRequestAttr(request, KEY_BAT_LIST, batList);
        }

        OtherApplicationInfoDto otherAppInfoDto = facilityRegisterDto.getOtherAppInfoDto();
        List<DeclarationItemMainInfo> config = facRegClient.getDeclarationConfigInfoById(otherAppInfoDto.getDeclarationId());
        otherAppInfoDto.setDeclarationConfig(config);
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_CONFIG, otherAppInfoDto.getDeclarationConfig());
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_ANSWER_MAP, otherAppInfoDto.getAnswerMap());

        List<DocSetting> facRegDocSetting = docSettingService.getFacRegDocSettings(facClassification, activityTypes, profileDto.getInfoList().size());
        ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, facRegDocSetting);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        Map<Integer, String> facilityNameIndexMap = Maps.newLinkedHashMapWithExpectedSize(profileDto.getInfoList().size());
        for (int i = 0; i < profileDto.getInfoList().size(); i++) {
            FacilityProfileInfo profileInfo = profileDto.getInfoList().get(i);
            facilityNameIndexMap.put(i, profileInfo.getFacName());
        }
        primaryDocDto.setFacilityNameIndexMap(facilityNameIndexMap);
        primaryDocDto.setPvRf(isPvRf);
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, facilityRegisterDto.getDocRecordInfos()));
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, savedFiles);
        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(facRegDocSetting, savedFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);
    }

    public void handleAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_TYPE);
        if (KEY_ACTION_EXPAND_FILE.equals(actionType)) {
            String actionValue = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_VALUE);
            if ("bsbCommittee".equals(actionValue)) {
                ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, "committee");
            } else if ("facAuth".equals(actionValue)) {
                ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, "authoriser");
            } else {
                throw new IaisRuntimeException("Invalid action value");
            }
        } else {
            throw new IaisRuntimeException("Invalid action type");
        }
    }

    public void preCommitteeInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String facilityId = (String) ParamUtil.getSessionAttr(request, KEY_FACILITY_ID);
        FacilityRegisterDto facilityRegisterDto = getFacilityRegisterDto(request, appId, facilityId);
        FacilityCommitteeDto facCommitteeDto = facilityRegisterDto.getFacilityCommitteeDto();
        List<FacilityCommitteeFileDto> dataList = facCommitteeDto.getDataListForDisplay();
        ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
    }

    public void preAuthoriserInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String facilityId = (String) ParamUtil.getSessionAttr(request, KEY_FACILITY_ID);
        FacilityRegisterDto facilityRegisterDto = getFacilityRegisterDto(request, appId, facilityId);
        FacilityAuthoriserDto facAuthDto = facilityRegisterDto.getFacilityAuthoriserDto();
        List<FacilityAuthoriserFileDto> dataList = facAuthDto.getDataListForDisplay();
        ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
    }
}
