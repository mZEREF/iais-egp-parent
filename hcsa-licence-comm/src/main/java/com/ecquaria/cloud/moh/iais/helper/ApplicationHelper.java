package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPsnEditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PersonFieldDto;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.client.LicCommClient;
import lombok.extern.slf4j.Slf4j;
import sop.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.CURRENTSERVICEID;

/**
 * NewApplicationHelper
 *
 * @author suocheng
 * @date 2/24/2020
 */

@Slf4j
public final class ApplicationHelper {

    private static Map<String, String> PSN_STEP_MAP;
    private static Map<String, String> NAME_MAP;

    static {
        PSN_STEP_MAP = IaisCommonUtils.genNewHashMap();
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, HcsaConsts.STEP_PRINCIPAL_OFFICERS);
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, HcsaConsts.STEP_PRINCIPAL_OFFICERS);
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS);
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL, HcsaConsts.STEP_SERVICE_PERSONNEL);
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, HcsaConsts.STEP_MEDALERT_PERSON);
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, HcsaConsts.STEP_CLINICAL_DIRECTOR);
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_VEHICLES, HcsaConsts.STEP_VEHICLES);
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_CHARGES, HcsaConsts.STEP_CHARGES);
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_CHARGES_OTHER, HcsaConsts.STEP_CHARGES);
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, HcsaConsts.STEP_SECTION_LEADER);
        PSN_STEP_MAP.put(ApplicationConsts.PERSONNEL_PSN_KAH, HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER);
        PSN_STEP_MAP = Collections.unmodifiableMap(PSN_STEP_MAP);

        NAME_MAP = IaisCommonUtils.genNewHashMap();
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, HcsaConsts.CLINICAL_GOVERNANCE_OFFICER);
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, HcsaConsts.NOMINEE);
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, HcsaConsts.CLINICAL_GOVERNANCE_OFFICER);
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL, HcsaConsts.SERVICE_PERSONNEL);
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, HcsaConsts.MEDALERT_PERSON);
        NAME_MAP.put(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, HcsaConsts.CLINICAL_DIRECTOR);
        NAME_MAP.put(ApplicationConsts.PERSONNEL_VEHICLES, HcsaConsts.VEHICLE);
        NAME_MAP.put(ApplicationConsts.PERSONNEL_CHARGES, "General Conveyance Charges");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_CHARGES_OTHER, "Medical Equipment and Other Charges");
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, HcsaConsts.SECTION_LEADER);
        NAME_MAP.put(ApplicationConsts.PERSONNEL_PSN_KAH, HcsaConsts.KEY_APPOINTMENT_HOLDER);
        NAME_MAP = Collections.unmodifiableMap(NAME_MAP);
    }

    public static boolean isBackend() {
        return AppConsts.USER_DOMAIN_INTRANET.equals(ConfigHelper.getString("iais.current.domain"));
    }

    public static boolean isFrontend() {
        return AppConsts.USER_DOMAIN_INTERNET.equals(ConfigHelper.getString("iais.current.domain"));
    }

    public static String getStep(String personType) {
        return PSN_STEP_MAP.get(personType);
    }

    public static String getName(String personType) {
        return NAME_MAP.get(personType);
    }

    public static LoginContext getLoginContext(HttpServletRequest request) {
        return (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
    }

    public static String getLicenseeId(HttpServletRequest request) {
        String licenseeId = "";
        if (isBackend()) {
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
            if (appSubmissionDto != null) {
                licenseeId = appSubmissionDto.getLicenseeId();
            }
        } else {
            LoginContext loginContext = getLoginContext(request);
            if (loginContext != null) {
                licenseeId = loginContext.getLicenseeId();
            }
        }
        return licenseeId;
    }

    public static String getLicenseeId(List<AppSubmissionDto> appSubmissionDtos) {
        String licenseeId = "";
        if (!IaisCommonUtils.isEmpty(appSubmissionDtos)) {
            for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
                licenseeId = appSubmissionDto.getLicenseeId();
                if (!StringUtil.isEmpty(licenseeId)) {
                    break;
                }
            }
        }
        return licenseeId;
    }

    public static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,
                HcsaAppConst.APPSUBMISSIONDTO);
        if (appSubmissionDto == null) {
            log.info(StringUtil.changeForLog("appSubmissionDto is empty "));
            appSubmissionDto = new AppSubmissionDto();
            setOldAppSubmissionDto(appSubmissionDto, request);
        }
        return appSubmissionDto;
    }

    public static void setAppSubmissionDto(AppSubmissionDto oldAppSubmissionDto, HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, HcsaAppConst.APPSUBMISSIONDTO, oldAppSubmissionDto);
    }

    public static AppSubmissionDto getOldAppSubmissionDto(HttpServletRequest request) {
        return getOldAppSubmissionDto(false, request);
    }

    public static AppSubmissionDto getOldAppSubmissionDto(boolean onlySession, HttpServletRequest request) {
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,
                HcsaAppConst.OLDAPPSUBMISSIONDTO);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        if (oldAppSubmissionDto == null && appSubmissionDto != null) {
            if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, "oldRenewAppSubmissionDto");
            }
        }
        if (!onlySession && oldAppSubmissionDto == null && appSubmissionDto != null) {
            log.info(StringUtil.changeForLog("OldAppSubmissionDto is empty from Session"));
            oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
            if (oldAppSubmissionDto != null) {
                setOldAppSubmissionDto(oldAppSubmissionDto, request);
            } else {
                log.info(StringUtil.changeForLog("No OldAppSubmissionDto Found!"));
            }
        }
        return oldAppSubmissionDto;
    }

    public static void setOldAppSubmissionDto(AppSubmissionDto oldAppSubmissionDto, HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, HcsaAppConst.OLDAPPSUBMISSIONDTO, oldAppSubmissionDto);
    }

    public static String getCurrentServiceId(HttpServletRequest request){
        return (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
    }

    public static AppSvcRelatedInfoDto getAppSvcRelatedInfo(AppSubmissionDto appSubmissionDto, String currentSvcId, String appNo) {
        log.info(StringUtil.changeForLog("service id: " + currentSvcId + " - appNo: " + appNo));
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        if (appSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (appSvcRelatedInfoDtos != null && !appSvcRelatedInfoDtos.isEmpty()) {
                for (AppSvcRelatedInfoDto svcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    if (currentSvcId.equals(svcRelatedInfoDto.getServiceId()) && (StringUtil.isEmpty(appNo) ||
                            appNo.equals(svcRelatedInfoDto.getAppNo()))) {
                        appSvcRelatedInfoDto = svcRelatedInfoDto;
                        break;
                    }
                }
            }
        }
        return appSvcRelatedInfoDto;
    }

    /*
     * get current svc dto
     * */
    public static AppSvcRelatedInfoDto getAppSvcRelatedInfo(HttpServletRequest request) {
        String currSvcId = (String) ParamUtil.getSessionAttr(request, CURRENTSERVICEID);
        return getAppSvcRelatedInfo(request, currSvcId);
    }

    public static AppSvcRelatedInfoDto getAppSvcRelatedInfo(HttpServletRequest request, String currentSvcId) {
        log.debug(StringUtil.changeForLog("getAppSvcRelatedInfo service id:" + currentSvcId));
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        return getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, null);
    }

    public static AppSvcRelatedInfoDto getAppSvcRelatedInfo(HttpServletRequest request, String currentSvcId, String appNo) {
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        return getAppSvcRelatedInfo(appSubmissionDto, currentSvcId, appNo);
    }

    public static void reSetAdditionalFields(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto,
            AppEditSelectDto appEditSelectDto) {
        reSetAdditionalFields(appSubmissionDto, appEditSelectDto);
        reSetAdditionalFields(appSubmissionDto, oldAppSubmissionDto);
    }

    public static void reSetAdditionalFields(AppSubmissionDto appSubmissionDto, AppEditSelectDto appEditSelectDto) {
        reSetAdditionalFields(appSubmissionDto, appEditSelectDto, null);
    }

    public static void reSetAdditionalFields(AppSubmissionDto appSubmissionDto, AppEditSelectDto appEditSelectDto, String appGrpNo) {
        if (appSubmissionDto == null || appSubmissionDto.getAppGrpPremisesDtoList() == null || appEditSelectDto == null) {
            return;
        }
        boolean isNeedNewLicNo = appEditSelectDto.isNeedNewLicNo();
        boolean isAutoRfc = appEditSelectDto.isAutoRfc();
        reSetAdditionalFields(appSubmissionDto, isNeedNewLicNo, isAutoRfc, appGrpNo);
    }

    public static void reSetAdditionalFields(AppSubmissionDto appSubmissionDto, boolean isNeedNewLicNo, boolean isAutoRfc,
            String appGrpNo) {
        if (appSubmissionDto == null || appSubmissionDto.getAppGrpPremisesDtoList() == null) {
            return;
        }
        int selfAssMtFlag = isAutoRfc ? ApplicationConsts.PROHIBIT_SUBMIT_RFI_SELF_ASSESSMENT :
                ApplicationConsts.PENDING_SUBMIT_SELF_ASSESSMENT;
        appSubmissionDto.setAutoRfc(isAutoRfc);
        appSubmissionDto.setIsNeedNewLicNo(isNeedNewLicNo ? AppConsts.YES : AppConsts.NO);
        appSubmissionDto.getAppGrpPremisesDtoList().forEach(appGrpPremisesDto -> {
            appGrpPremisesDto.setNeedNewLicNo(Boolean.valueOf(isNeedNewLicNo));
            appGrpPremisesDto.setSelfAssMtFlag(selfAssMtFlag);
        });
        if (!StringUtil.isEmpty(appGrpNo)) {
            appSubmissionDto.setAppGrpNo(appGrpNo);
        }
    }

    public static void reSetAdditionalFields(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        if (appSubmissionDto == null || oldAppSubmissionDto == null) {
            return;
        }
        log.info(StringUtil.changeForLog("The original Licence: " + appSubmissionDto.getLicenceNo()
                + " - appGrpNo: " + appSubmissionDto.getAppGrpNo()));
        reSetAdditionalFields(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
    }

    public static void reSetAdditionalFields(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        if (appGrpPremisesDtoList == null || oldAppGrpPremisesDtoList == null
                || appGrpPremisesDtoList.isEmpty() || oldAppGrpPremisesDtoList.isEmpty()) {
            return;
        }
        int oldSize = oldAppGrpPremisesDtoList.size();
        int size = appGrpPremisesDtoList.size();
        for (int i = 0; i < size; i++) {
            AppGrpPremisesDto oldAppGrpPremisesDto;
            if (i < oldSize) {
                oldAppGrpPremisesDto = oldAppGrpPremisesDtoList.get(i);
            } else {
                oldAppGrpPremisesDto = oldAppGrpPremisesDtoList.get(0);
            }
            reSetAdditionalFields(appGrpPremisesDtoList.get(i), oldAppGrpPremisesDto);
        }
    }

    public static void reSetAdditionalFields(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        if (appGrpPremisesDto == null || oldAppGrpPremisesDto == null) {
            return;
        }
        String premisesIndexNo = oldAppGrpPremisesDto.getPremisesIndexNo();
        appGrpPremisesDto.setPremisesIndexNo(premisesIndexNo);
        if (StringUtil.isEmpty(appGrpPremisesDto.getOldHciCode())) {
            appGrpPremisesDto.setOldHciCode(oldAppGrpPremisesDto.getOldHciCode());
        }
        boolean clearHciCode = false;
        if (AppConsts.NO.equals(appGrpPremisesDto.getExistingData())) {
            clearHciCode = !Objects.equals(oldAppGrpPremisesDto.getPremisesSelect(),
                    getPremisesKey(appGrpPremisesDto));
            if (clearHciCode) {
                appGrpPremisesDto.setHciCode(null);
            } else if (StringUtil.isEmpty(appGrpPremisesDto.getHciCode())) {
                appGrpPremisesDto.setHciCode(oldAppGrpPremisesDto.getHciCode());
            }
        } else if (StringUtil.isEmpty(appGrpPremisesDto.getHciCode()) && Objects.equals(oldAppGrpPremisesDto.getPremisesSelect(),
                getPremisesKey(appGrpPremisesDto))) {
            appGrpPremisesDto.setHciCode(oldAppGrpPremisesDto.getHciCode());
        }
        boolean eqHciNameChange = RfcHelper.eqHciNameChange(appGrpPremisesDto,
                oldAppGrpPremisesDto);
        if (eqHciNameChange) {
            appGrpPremisesDto.setHciNameChanged(1);
        }
        log.info(StringUtil.changeForLog("##### reSetAdditionalFields : " + premisesIndexNo + " - ClearHciCode: "
                + clearHciCode + " - HciNameChange: " + eqHciNameChange + " - HCI Code: " + appGrpPremisesDto.getHciCode()));
    }

    public static void addToNonAuto(List<AppSubmissionDto> sourceList, List<AppSubmissionDto> notAutoSaveList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return;
        }
        List<AppSubmissionDto> newAuto = IaisCommonUtils.genNewArrayList();
        sourceList.stream().forEach(dto -> {
            String licenceId = Optional.ofNullable(dto.getLicenceId()).orElse("");
            Optional<AppSubmissionDto> optional = notAutoSaveList.stream()
                    .filter(source -> licenceId.equals(source.getLicenceId()))
                    .findAny();
            if (optional.isPresent()) {
                reSetNonAutoDataByAppEditSelectDto(dto, optional.get());
            } else {
                newAuto.add(dto);
            }
        });
        notAutoSaveList.addAll(newAuto);
    }

    public static void addToAuto(List<AppSubmissionDto> sourceList, List<AppSubmissionDto> autoSaveList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return;
        }
        List<AppSubmissionDto> newAuto = IaisCommonUtils.genNewArrayList();
        sourceList.stream().forEach(dto -> {
            String licenceId = Optional.ofNullable(dto.getLicenceId()).orElse("");
            Optional<AppSubmissionDto> optional = autoSaveList.stream()
                    .filter(source -> licenceId.equals(source.getLicenceId()))
                    .findAny();
            if (optional.isPresent()) {
                reSetAutoDataByAppEditSelectDto(optional.get(), dto);
            } else {
                newAuto.add(dto);
            }
        });
        autoSaveList.addAll(newAuto);
    }

    public static void addToAuto(List<AppSubmissionDto> sourceList, List<AppSubmissionDto> autoSaveList,
            List<AppSubmissionDto> notAutoSaveAppsubmission) {
        if (sourceList == null || sourceList.isEmpty()) {
            return;
        }
        List<AppSubmissionDto> notInNonAuto = IaisCommonUtils.genNewArrayList();
        sourceList.stream().forEach(dto -> {
            String licenceId = Optional.ofNullable(dto.getLicenceId()).orElse("");
            Optional<AppSubmissionDto> optional = notAutoSaveAppsubmission.stream()
                    .filter(source -> licenceId.equals(source.getLicenceId()))
                    .findAny();
            if (optional.isPresent()) {
                reSetAutoDataByAppEditSelectDto(optional.get(), dto);
            } else {
                notInNonAuto.add(dto);
            }
        });
        List<AppSubmissionDto> notInAuto = IaisCommonUtils.genNewArrayList();
        notInNonAuto.stream().forEach(dto -> {
            String licenceId = Optional.ofNullable(dto.getLicenceId()).orElse("");
            Optional<AppSubmissionDto> optional = autoSaveList.stream()
                    .filter(source -> licenceId.equals(source.getLicenceId()))
                    .findAny();
            if (optional.isPresent()) {
                reSetAutoDataByAppEditSelectDto(optional.get(), dto);
            } else {
                notInAuto.add(dto);
            }
        });
        autoSaveList.addAll(notInAuto);
    }

    public static void reSetNonAutoDataByAppEditSelectDto(AppSubmissionDto targetDto, AppSubmissionDto scourceDto) {
        if (scourceDto == null || targetDto == null) {
            return;
        }
        log.info(StringUtil.changeForLog("##### Reset Data: " + targetDto.getLicenceNo() + " : " + targetDto.getAppGrpNo()
                + " : " + targetDto.getLicenceId() + " : " + scourceDto.getAppGrpNo()));
        AppEditSelectDto source = scourceDto.getChangeSelectDto();
        AppEditSelectDto target = targetDto.getChangeSelectDto();
        log.info(StringUtil.changeForLog("Source App Edit Select Dto: " + JsonUtil.parseToJson(source)));
        log.info(StringUtil.changeForLog("Target App Edit Select Dto: " + JsonUtil.parseToJson(target)));
        if (source == null || target == null) {
            return;
        }
        if (source.isLicenseeEdit()) {
            target.setLicenseeEdit(true);
        }
        if (source.isPremisesEdit()) {
            target.setPremisesEdit(true);
            reSetPremeses(targetDto, scourceDto.getAppGrpPremisesDtoList());
        }
        if (source.isDocEdit()) {
            target.setDocEdit(true);
        }
        if (source.isServiceEdit()) {
            target.setServiceEdit(true);
            List<AppSvcRelatedInfoDto> sourceSvcInfoList = scourceDto.getAppSvcRelatedInfoDtoList();
            List<AppSvcRelatedInfoDto> targetSvcInfoList = targetDto.getAppSvcRelatedInfoDtoList();
            handleAppSvcRelatedInfoDtos(targetSvcInfoList, sourceSvcInfoList,
                    Optional.ofNullable(scourceDto.getAppEditSelectDto()).map(AppEditSelectDto::getPersonnelEditList).orElse(null));
        }
    }

    public static void reSetAutoDataByAppEditSelectDto(AppSubmissionDto targetDto, AppSubmissionDto scourceDto) {
        if (scourceDto == null || targetDto == null) {
            return;
        }
        log.info(StringUtil.changeForLog("##### Reset Data: " + targetDto.getLicenceId() + " : " + targetDto.getLicenceNo()));
        AppEditSelectDto source = scourceDto.getChangeSelectDto();
        AppEditSelectDto target = targetDto.getChangeSelectDto();
        log.info(StringUtil.changeForLog("Source App Edit Select Dto: " + JsonUtil.parseToJson(source)));
        log.info(StringUtil.changeForLog("Target App Edit Select Dto: " + JsonUtil.parseToJson(target)));
        if (source == null || target == null) {
            return;
        }
        if (source.isLicenseeEdit()) {
            target.setLicenseeEdit(true);
            targetDto.setSubLicenseeDto((SubLicenseeDto) CopyUtil.copyMutableObject(scourceDto.getSubLicenseeDto()));
        }
        if (source.isPremisesEdit()) {
            target.setPremisesEdit(true);
            reSetPremeses(targetDto, scourceDto.getAppGrpPremisesDtoList());
        }
        if (source.isDocEdit()) {
            target.setDocEdit(true);
        }
        if (source.isServiceEdit()) {
            target.setServiceEdit(true);
            List<AppSvcRelatedInfoDto> sourceSvcInfoList = scourceDto.getAppSvcRelatedInfoDtoList();
            List<AppSvcRelatedInfoDto> targetSvcInfoList = targetDto.getAppSvcRelatedInfoDtoList();
            handleAppSvcRelatedInfoDtos(targetSvcInfoList, sourceSvcInfoList, source.getPersonnelEditList());
        }
    }

    private static void handleAppSvcRelatedInfoDtos(List<AppSvcRelatedInfoDto> targetSvcInfoList,
            List<AppSvcRelatedInfoDto> sourceSvcInfoList, List<String> personnelEditList) {
        if (sourceSvcInfoList == null || sourceSvcInfoList.isEmpty() || targetSvcInfoList == null || targetSvcInfoList.isEmpty()
                || personnelEditList == null || personnelEditList.isEmpty()) {
            return;
        }
        AppSvcRelatedInfoDto sourceSvcInfo = sourceSvcInfoList.get(0);
        AppSvcRelatedInfoDto targetSvcInfo = targetSvcInfoList.get(0);
        if (personnelEditList.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO)
                || personnelEditList.contains(HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS)) {
            List<AppSvcPrincipalOfficersDto> deList = IaisCommonUtils.genNewArrayList(sourceSvcInfo.getAppSvcCgoDtoList().size());
            CopyUtil.copyMutableObjectList(sourceSvcInfo.getAppSvcCgoDtoList(), deList);
            targetSvcInfo.setAppSvcCgoDtoList(deList);
        }
        if (personnelEditList.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP)
                || personnelEditList.contains(HcsaConsts.STEP_MEDALERT_PERSON)) {
            List<AppSvcPrincipalOfficersDto> deList = IaisCommonUtils.genNewArrayList(
                    sourceSvcInfo.getAppSvcMedAlertPersonList().size());
            CopyUtil.copyMutableObjectList(sourceSvcInfo.getAppSvcMedAlertPersonList(), deList);
            targetSvcInfo.setAppSvcMedAlertPersonList(deList);
        }
        if (personnelEditList.contains(ApplicationConsts.PERSONNEL_PSN_TYPE_PO)
                || personnelEditList.contains(HcsaConsts.STEP_PRINCIPAL_OFFICERS)) {
            List<AppSvcPrincipalOfficersDto> deList = IaisCommonUtils.genNewArrayList(
                    sourceSvcInfo.getAppSvcPrincipalOfficersDtoList().size());
            CopyUtil.copyMutableObjectList(sourceSvcInfo.getAppSvcPrincipalOfficersDtoList(), deList);
            targetSvcInfo.setAppSvcPrincipalOfficersDtoList(deList);
        }
        if (personnelEditList.contains(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR)
                || personnelEditList.contains(HcsaConsts.STEP_CLINICAL_DIRECTOR)) {
            List<AppSvcPrincipalOfficersDto> deList = IaisCommonUtils.genNewArrayList(
                    sourceSvcInfo.getAppSvcClinicalDirectorDtoList().size());
            CopyUtil.copyMutableObjectList(sourceSvcInfo.getAppSvcClinicalDirectorDtoList(), deList);
            targetSvcInfo.setAppSvcClinicalDirectorDtoList(deList);
        }
        if (personnelEditList.contains(ApplicationConsts.PERSONNEL_PSN_KAH)
                || personnelEditList.contains(HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER)) {
            List<AppSvcPrincipalOfficersDto> deList = IaisCommonUtils.genNewArrayList(
                    sourceSvcInfo.getAppSvcKeyAppointmentHolderDtoList().size());
            CopyUtil.copyMutableObjectList(sourceSvcInfo.getAppSvcKeyAppointmentHolderDtoList(), deList);
            targetSvcInfo.setAppSvcKeyAppointmentHolderDtoList(deList);
        }

        targetSvcInfo.setAppSvcDocDtoLit(sourceSvcInfo.getAppSvcDocDtoLit());
    }

    public static int getMaxFileIndex(Integer maxSeqNum, boolean checkGlobal, HttpServletRequest request) {
        int seqNum = maxSeqNum != null ? maxSeqNum + 1 : 0;
        Integer maxFileIndex = 0;
        if (checkGlobal && request != null) {
            maxFileIndex = (Integer) ParamUtil.getSessionAttr(request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR);
        }
        if (maxFileIndex != null && (maxFileIndex > seqNum)) {
            seqNum = maxFileIndex;
        }
        if (checkGlobal && request != null) {
            ParamUtil.setSessionAttr(request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR, seqNum);
        }
        return seqNum;
    }

    public static int getMaxFileIndex(Integer maxSeqNum) {
        return getMaxFileIndex(maxSeqNum, true, MiscUtil.getCurrentRequest());
    }

    public static void reSetMaxFileIndex(int maxSeqNum, HttpServletRequest request) {
        getMaxFileIndex(maxSeqNum, true, request);
    }

    public static void reSetMaxFileIndex(int maxSeqNum) {
        reSetMaxFileIndex(maxSeqNum, MiscUtil.getCurrentRequest());
    }


    public static List<SelectOption> getIdTypeSelOp() {
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("", HcsaAppConst.FIRESTOPTION);
        idTypeSelectList.add(idType0);
        SelectOption idType1 = new SelectOption(OrganizationConstants.ID_TYPE_NRIC, "NRIC");
        SelectOption idType2 = new SelectOption(OrganizationConstants.ID_TYPE_FIN, "FIN");
        idTypeSelectList.add(idType2);
        idTypeSelectList.add(idType1);
        return idTypeSelectList;
    }

    public static AppSubmissionDto setSubmissionDtoSvcData(HttpServletRequest request, AppSubmissionDto appSubmissionDto)
            throws CloneNotSupportedException {
        List<HcsaServiceDto> hcsaServiceDtoList = HcsaServiceCacheHelper.receiveAllHcsaService();
        if (appSubmissionDto != null && hcsaServiceDtoList != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                    //set hcsaService info
                    for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
                        String svcId = appSvcRelatedInfoDto.getServiceId();
                        String name = appSvcRelatedInfoDto.getServiceName();
                        if (!StringUtil.isEmpty(svcId)) {
                            if (hcsaServiceDto.getId().equals(svcId)) {
                                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                            }

                        } else if (!StringUtil.isEmpty(name)) {
                            if (hcsaServiceDto.getSvcName().equals(name)) {
                                appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                            }

                        }
                    }
                    //set svc cgo dropdown info
                    /*List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
                        List<SelectOption> specialtyList = genSpecialtySelectList(appSvcRelatedInfoDto.getServiceCode(), true);
                        List<String> specialtyKeyList = IaisCommonUtils.genNewArrayList();
                        for (SelectOption sp : specialtyList) {
                            specialtyKeyList.add(sp.getValue());
                        }
                        List<SelectOption> allSpecialtyList = getAllSpecialtySelList();
                        for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtos) {
                            if (specialtyKeyList.contains(appSvcCgoDto.getSpeciality())) {
                                continue;
                            }
                            appSvcCgoDto.setNeedSpcOptList(true);
                            appSvcCgoDto.setSpcOptList(allSpecialtyList);
                            Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
                            specialtyAttr.put("name", "specialty");
                            specialtyAttr.put("class", "specialty");
                            specialtyAttr.put("style", "display: none;");
                            String specialtySelectStr = generateDropDownHtml(specialtyAttr, allSpecialtyList,
                                    null, appSvcCgoDto.getSpeciality());
                            appSvcCgoDto.setSpecialityHtml(specialtySelectStr);
                        }
                    }*/
                }
            }
        }
        //todo:change place
        Object rfi = ParamUtil.getSessionAttr(request, HcsaAppConst.REQUESTINFORMATIONCONFIG);
        if (appSubmissionDto != null) {
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                    || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                    || rfi != null) {
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
                AppSubmissionDto sessionAttr = getOldAppSubmissionDto(true, request);
                if (sessionAttr == null) {
                    setOldAppSubmissionDto(oldAppSubmissionDto, request);
                }
            }
        }
        return appSubmissionDto;
    }


    public static String generateDropDownHtml(Map<String, String> premisesOnSiteAttr, List<SelectOption> selectOptionList,
            String firestOption, String checkedVal) {
        return generateDropDownHtml(premisesOnSiteAttr, selectOptionList, firestOption, checkedVal, true);
    }

    public static String generateDropDownHtml(Map<String, String> premisesOnSiteAttr, List<SelectOption> selectOptionList,
            String firestOption, String checkedVal, boolean needSort) {
        //sort dropdown
        List<SelectOption> sortSelOptionList = IaisCommonUtils.genNewArrayList();
        if (needSort) {
            List<SelectOption> pleaseSelectSp = IaisCommonUtils.genNewArrayList();
            List<SelectOption> newPremisesSp = IaisCommonUtils.genNewArrayList();
            List<SelectOption> newPsnSp = IaisCommonUtils.genNewArrayList();
            List<SelectOption> otherSp = IaisCommonUtils.genNewArrayList();
            for (SelectOption sp : selectOptionList) {
                String val = sp.getValue();
                if (StringUtil.isEmpty(sp.getValue()) || "-1".equals(val)) {
                    pleaseSelectSp.add(sp);
                } else if (HcsaAppConst.NEW_PREMISES.equals(val)) {
                    newPremisesSp.add(sp);
                } else if (HcsaAppConst.NEW_PSN.equals(val)) {
                    newPsnSp.add(sp);
                } else if ("other".equals(val) || MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(
                        val) || HcsaAppConst.DESIGNATION_OTHERS.equals(val)) {
                    otherSp.add(sp);
                }
            }
            sortSelOptionList.addAll(pleaseSelectSp);
            sortSelOptionList.addAll(newPremisesSp);
            sortSelOptionList.addAll(newPsnSp);

            List<SelectOption> needSortList = IaisCommonUtils.genNewArrayList();
            for (SelectOption sp : selectOptionList) {
                String val = sp.getValue();
                boolean pleaseSelectVal = StringUtil.isEmpty(val) || "-1".equals(val);
                boolean newPremisesVal = HcsaAppConst.NEW_PREMISES.equals(val);
                boolean newPsnVal = HcsaAppConst.NEW_PSN.equals(val);
                boolean otherVal = "other".equals(val) || MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(
                        val) || HcsaAppConst.DESIGNATION_OTHERS.equals(val);
                if (pleaseSelectVal || newPremisesVal || newPsnVal || otherVal) {
                    continue;
                }
                needSortList.add(sp);
            }
            doSortSelOption(needSortList);
            sortSelOptionList.addAll(needSortList);
            sortSelOptionList.addAll(otherSp);
        } else {
            sortSelOptionList.addAll(selectOptionList);
        }

        StringBuilder sBuffer = new StringBuilder(100);
        sBuffer.append("<select ");
        for (Map.Entry<String, String> entry : premisesOnSiteAttr.entrySet()) {
//            sBuffer.append(entry.getKey()+"=\""+entry.getValue()+"\" ");
            sBuffer.append(entry.getKey())
                    .append("=\"")
                    .append(entry.getValue())
                    .append('\"');
        }
        sBuffer.append(" >");
        if (!StringUtil.isEmpty(firestOption)) {
//            sBuffer.append("<option value=\"\">"+ firestOption +"</option>");
            sBuffer.append("<option value=\"\">")
                    .append(firestOption)
                    .append("</option>");
        }
        for (SelectOption sp : sortSelOptionList) {
            if (!StringUtil.isEmpty(checkedVal)) {
                if (checkedVal.equals(sp.getValue())) {
//                    sBuffer.append("<option selected=\"selected\" value=\""+sp.getValue()+"\">"+ sp.getText() +"</option>");
                    sBuffer.append("<option selected=\"selected\" value=\"")
                            .append(sp.getValue())
                            .append("\">")
                            .append(sp.getText())
                            .append("</option>");
                } else {
                    sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
                }
            } else {
                sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
            }
        }
        sBuffer.append("</select>");
        String classNameValue = premisesOnSiteAttr.get("class");
        String className = "premSelect";
        if (!StringUtil.isEmpty(classNameValue)) {
            className = classNameValue;
        }
        sBuffer.append("<div class=\"nice-select ").append(className).append("\" tabindex=\"0\">");
        if (!StringUtil.isEmpty(checkedVal)) {
            String text = getTextByValue(sortSelOptionList, checkedVal);
            sBuffer.append("<span selected=\"selected\" class=\"current\">").append(text).append("</span>");
        } else {
            if (!StringUtil.isEmpty(firestOption)) {
                sBuffer.append("<span class=\"current\">").append(firestOption).append("</span>");
            } else {
                sBuffer.append("<span class=\"current\">").append(sortSelOptionList.get(0).getText()).append("</span>");
            }
        }
        sBuffer.append("<ul class=\"list\">");

        if (!StringUtil.isEmpty(checkedVal)) {
            for (SelectOption kv : sortSelOptionList) {
                if (checkedVal.equals(kv.getValue())) {
                    sBuffer.append("<li selected=\"selected\" data-value=\"").append(kv.getValue()).append(
                            "\" class=\"option selected\">").append(kv.getText()).append("</li>");
                } else {
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(
                            kv.getText()).append("</li>");
                }
            }
        } else if (!StringUtil.isEmpty(firestOption)) {
            sBuffer.append("<li data-value=\"\" class=\"option selected\">").append(firestOption).append("</li>");
            for (SelectOption kv : sortSelOptionList) {
                sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(kv.getText()).append(
                        "</li>");
            }
        } else {
            for (int i = 0; i < sortSelOptionList.size(); i++) {
                SelectOption kv = sortSelOptionList.get(i);
                if (i == 0) {
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option selected\">").append(
                            kv.getText()).append("</li>");
                } else {
                    sBuffer.append(" <li data-value=\"").append(kv.getValue()).append("\" class=\"option\">").append(
                            kv.getText()).append("</li>");
                }
            }
        }
        sBuffer.append("</ul>")
                .append("</div>");
        return sBuffer.toString();
    }

    public static boolean isGetDataFromPage(AppSubmissionDto appSubmissionDto, String currentType, String isClickEdit, boolean isRfi) {
        if (appSubmissionDto == null) {
            return true;
        }
        boolean isNewApp = !isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
        boolean isOther = false;

        if (appSubmissionDto.isNeedEditController()) {
            boolean canEdit = checkCanEdit(appSubmissionDto.getAppEditSelectDto(), currentType);
            isOther = canEdit && AppConsts.YES.equals(isClickEdit);
        }
        return isNewApp || isOther;
    }

    //just for one svc
    public static void setLaboratoryDisciplinesInfo(AppSubmissionDto appSubmissionDto,
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos) {
        Map<String, HcsaSvcSubtypeOrSubsumedDto> map = IaisCommonUtils.genNewHashMap();
        recursingSvcScope(hcsaSvcSubtypeOrSubsumedDtos, map);
        if (appSubmissionDto == null) {
            return;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) || !IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                setSvcScopeInfo(appGrpPremisesDtos, appSvcRelatedInfoDto, map);
            }
        }
    }

    //
    public static void setLaboratoryDisciplinesInfo(List<AppGrpPremisesDto> appGrpPremisesDtos,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto, List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos) {
        Map<String, HcsaSvcSubtypeOrSubsumedDto> map = IaisCommonUtils.genNewHashMap();
        recursingSvcScope(hcsaSvcSubtypeOrSubsumedDtos, map);
        if (appSvcRelatedInfoDto == null) {
            return;
        }
        setSvcScopeInfo(appGrpPremisesDtos, appSvcRelatedInfoDto, map);
    }

    public static void reSetPremeses(AppSubmissionDto appSubmissionDto, AppGrpPremisesDto appGrpPremisesDto) {
        List<AppGrpPremisesDto> appGrpPremisesDtos = new ArrayList<>(1);
        appGrpPremisesDtos.add(appGrpPremisesDto);
        reSetPremeses(appSubmissionDto, appGrpPremisesDtos);
    }

    public static void reSetPremeses(AppSubmissionDto appSubmissionDto, List<AppGrpPremisesDto> appGrpPremisesDtos) {
        log.info(StringUtil.changeForLog("The original Licence: " + appSubmissionDto.getLicenceNo()
                + " - appGrpNo: " + appSubmissionDto.getAppGrpNo()));
        List<AppGrpPremisesDto> copyMutableObjects = (List<AppGrpPremisesDto>) CopyUtil.copyMutableObjectList(appGrpPremisesDtos);
        reSetAdditionalFields(copyMutableObjects, appSubmissionDto.getAppGrpPremisesDtoList());
        appSubmissionDto.setAppGrpPremisesDtoList(copyMutableObjects);
    }

    public static void setPremise(AppGrpPremisesDto appGrpPremisesDto, String premIndexNo, AppSubmissionDto oldAppSubmissionDto) {
        String oldHciCode = null;
        List<LicenceDto> licenceDtos = null;
        if (oldAppSubmissionDto != null && oldAppSubmissionDto.getAppGrpPremisesDtoList() != null) {
            oldHciCode = oldAppSubmissionDto.getAppGrpPremisesDtoList().stream()
                    .filter(dto -> Objects.equals(premIndexNo, dto.getPremisesIndexNo()))
                    .map(dto -> Optional.ofNullable(dto.getOldHciCode()).orElseGet(() -> dto.getHciCode()))
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElse(null);
            licenceDtos = oldAppSubmissionDto.getAppGrpPremisesDtoList().stream()
                    .filter(dto -> Objects.equals(premIndexNo, dto.getPremisesIndexNo()))
                    .map(AppGrpPremisesDto::getLicenceDtos)
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElse(null);
            //appGrpPremisesDto.setOldHciCode(oldHciCode);
            appGrpPremisesDto.setLicenceDtos(licenceDtos);
        }
        log.info(StringUtil.changeForLog("--- Old Hci Code: " + oldHciCode));
        log.info(StringUtil.changeForLog("--- Maybe Affected Licence size: " + (licenceDtos == null ? 0 : licenceDtos.size())));
        appGrpPremisesDto.setPremisesIndexNo(premIndexNo);
    }

    public static AppGrpPremisesDto setOldHciCode(AppGrpPremisesDto appGrpPremisesDto) {
        if (appGrpPremisesDto == null) {
            return appGrpPremisesDto;
        }
        if (StringUtil.isEmpty(appGrpPremisesDto.getOldHciCode())) {
            appGrpPremisesDto.setOldHciCode(appGrpPremisesDto.getHciCode());
        }
        return appGrpPremisesDto;
    }

    public static AppGrpPremisesDto setWrkTime(AppGrpPremisesDto appGrpPremisesDto) {
        if (appGrpPremisesDto == null) {
            return appGrpPremisesDto;
        }
        List<OperationHoursReloadDto> weeklyDtos = appGrpPremisesDto.getWeeklyDtoList();
        if (!IaisCommonUtils.isEmpty(weeklyDtos)) {
            for (OperationHoursReloadDto weeklyDto : weeklyDtos) {
                setReloadTime(weeklyDto);
            }
        }

        List<OperationHoursReloadDto> phDtos = appGrpPremisesDto.getPhDtoList();
        if (!IaisCommonUtils.isEmpty(phDtos)) {
            for (OperationHoursReloadDto phDto : phDtos) {
                setReloadTime(phDto);
            }
        }

        List<AppPremEventPeriodDto> eventDtos = appGrpPremisesDto.getEventDtoList();
        if (!IaisCommonUtils.isEmpty(eventDtos)) {
            for (AppPremEventPeriodDto eventDto : eventDtos) {
                Date start = eventDto.getStartDate();
                if (start != null) {
                    eventDto.setStartDateStr(Formatter.formatDate(start));
                }
                Date end = eventDto.getEndDate();
                if (end != null) {
                    eventDto.setEndDateStr(Formatter.formatDate(end));
                }
            }
        }
        if (appGrpPremisesDto.getCertIssuedDt() != null) {
            String certIssuedDtStr = Formatter.formatDate(appGrpPremisesDto.getCertIssuedDt());
            appGrpPremisesDto.setCertIssuedDtStr(certIssuedDtStr);
        }
        return appGrpPremisesDto;
    }

    /**
     * @param appSubmissionDto
     * @return
     * @Descriptio cgo, po, dpo, map,
     */
    public static Map<String, AppSvcPrincipalOfficersDto> getPsnMapFromSubDto(AppSubmissionDto appSubmissionDto) {
        Map<String, AppSvcPrincipalOfficersDto> psnMap = IaisCommonUtils.genNewHashMap();
        if (appSubmissionDto == null) {
            return psnMap;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                //cgo
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                if (!IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
                    for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtos) {
                        AppSvcPrincipalOfficersDto psnDto = psnMap.get(appSvcCgoDto.getIdNo());
                        if (psnDto != null) {
                            continue;
                        }
                        psnDto = transferCgoToPsnDto(appSvcCgoDto);
                        psnMap.put(appSvcCgoDto.getIdNo(), psnDto);
                    }
                }
                //po and dpo
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
                    for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                        AppSvcPrincipalOfficersDto psnDto = psnMap.get(appSvcPrincipalOfficersDto.getIdNo());
                        if (psnDto != null) {
                            psnDto.setOfficeTelNo(appSvcPrincipalOfficersDto.getOfficeTelNo());
                        } else {
                            psnDto = appSvcPrincipalOfficersDto;
                        }
                        psnMap.put(appSvcPrincipalOfficersDto.getIdNo(), psnDto);
                    }
                }
                //medAlert
                List<AppSvcPrincipalOfficersDto> appSvcMedAlertPsnDtos = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                if (!IaisCommonUtils.isEmpty(appSvcMedAlertPsnDtos)) {
                    for (AppSvcPrincipalOfficersDto appSvcMedAlertPsnDto : appSvcMedAlertPsnDtos) {
                        AppSvcPrincipalOfficersDto psnDto = psnMap.get(appSvcMedAlertPsnDto.getIdNo());
                        if (psnDto != null) {

                        } else {
                            psnDto = appSvcMedAlertPsnDto;
                        }
                        psnMap.put(appSvcMedAlertPsnDto.getIdNo(), psnDto);
                    }
                }
            }
        }
        return psnMap;
    }

    public static List<AppSvcPrincipalOfficersDto> transferCgoToPsnDtoList(List<AppSvcPrincipalOfficersDto> appSvcCgoDtos) {
        List<AppSvcPrincipalOfficersDto> psnDtos = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
            return psnDtos;
        }
        for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtos) {
            AppSvcPrincipalOfficersDto psnDto = MiscUtil.transferEntityDto(appSvcCgoDto, AppSvcPrincipalOfficersDto.class);
            psnDtos.add(psnDto);
        }
        return psnDtos;
    }

    public static Map<String, AppSvcPersonAndExtDto> initSetPsnIntoSelMap(Map<String, AppSvcPersonAndExtDto> personMap,
            List<AppSvcPrincipalOfficersDto> psnDtos, String svcCode) {
        if (IaisCommonUtils.isEmpty(psnDtos)) {
            return personMap;
        }
        for (AppSvcPrincipalOfficersDto psnDto : psnDtos) {
            if (!AppValidatorHelper.psnDoPartValidate(psnDto.getIdType(), psnDto.getIdNo(), psnDto.getName())) {
                continue;
            }
            String personMapKey = getPersonKey(psnDto.getNationality(), psnDto.getIdType(), psnDto.getIdNo());
            AppSvcPersonAndExtDto appSvcPersonAndExtDto = personMap.get(personMapKey);
            List<AppSvcPersonExtDto> appSvcPersonExtDtos = IaisCommonUtils.genNewArrayList();
            AppSvcPrincipalOfficersDto person = genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto, svcCode, true);
           /* Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
            specialtyAttr.put("name", "specialty");
            specialtyAttr.put("class", "specialty");
            specialtyAttr.put("style", "display: none;");
            String speciality = psnDto.getSpeciality();*/
            if (person == null) {
                /*if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())) {
                    psnDto.setNeedSpcOptList(true);
                    List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode, true);
                    boolean canMatch = false;
                    for (SelectOption sp : specialityOpts) {
                        if (sp.getValue().equals(speciality)) {
                            canMatch = true;
                            break;
                        }
                    }
                    if (!canMatch) {
                        log.info(StringUtil.changeForLog("can not match speciality:" + speciality + ",when svcCode:" + svcCode));
                        specialityOpts = getAllSpecialtySelList();
                    }
                    psnDto.setSpcOptList(specialityOpts);
                    String specialtySelectStr = generateDropDownHtml(specialtyAttr, specialityOpts, null,
                            psnDto.getSpeciality());
                    psnDto.setSpecialityHtml(specialtySelectStr);
                }*/
                psnDto.setAssignSelect(getPersonKey(psnDto.getNationality(), psnDto.getIdType(), psnDto.getIdNo()));
                AppSvcPersonAndExtDto newPersonAndExtDto = new AppSvcPersonAndExtDto();
                AppSvcPersonDto appSvcPersonDto = MiscUtil.transferEntityDto(psnDto, AppSvcPersonDto.class);
                AppSvcPersonExtDto appSvcPersonExtDto = MiscUtil.transferEntityDto(psnDto, AppSvcPersonExtDto.class);
                appSvcPersonExtDto.setServiceCode(svcCode);
                appSvcPersonExtDto.setAssignSelect(psnDto.getAssignSelect());
                appSvcPersonExtDtos.add(appSvcPersonExtDto);
                newPersonAndExtDto.setPersonDto(appSvcPersonDto);
                newPersonAndExtDto.setPersonExtDtoList(appSvcPersonExtDtos);
                newPersonAndExtDto.setLicPerson(psnDto.isLicPerson());
                personMap.put(personMapKey, newPersonAndExtDto);
            } else {
                //set different page column
                person.setAssignSelect(getPersonKey(psnDto.getNationality(), psnDto.getIdType(), psnDto.getIdNo()));
                person.setSalutation(psnDto.getSalutation());
                person.setName(psnDto.getName());
                person.setNationality(psnDto.getNationality());
                person.setIdType(psnDto.getIdType());
                person.setIdNo(psnDto.getIdNo());
                if (!ApplicationConsts.PERSONNEL_PSN_KAH.equals(psnDto.getPsnType())) {
                    person.setMobileNo(psnDto.getMobileNo());
                    person.setEmailAddr(psnDto.getEmailAddr());
                }
                String designation = psnDto.getDesignation();
                if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnDto.getPsnType())) {
                    person.setDesignation(designation);
                    if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                        person.setOtherDesignation(psnDto.getOtherDesignation());
                    }
                    person.setProfessionBoard(psnDto.getProfessionBoard());
                    person.setProfRegNo(psnDto.getProfRegNo());
                    person.setSpeciality(psnDto.getSpeciality());
                    person.setSpecialtyGetDate(handleDate(psnDto.getSpecialtyGetDate(), psnDto.getSpecialtyGetDateStr()));
                    person.setSpecialtyGetDateStr(handleDateString(psnDto.getSpecialtyGetDate(), psnDto.getSpecialtyGetDateStr()));
                    person.setTypeOfCurrRegi(psnDto.getTypeOfCurrRegi());
                    person.setCurrRegiDate(handleDate(psnDto.getCurrRegiDate(), psnDto.getCurrRegiDateStr()));
                    person.setCurrRegiDateStr(handleDateString(psnDto.getCurrRegiDate(), psnDto.getCurrRegiDateStr()));
                    person.setPraCerEndDate(handleDate(psnDto.getPraCerEndDate(), psnDto.getPraCerEndDateStr()));
                    person.setPraCerEndDateStr(handleDateString(psnDto.getPraCerEndDate(), psnDto.getPraCerEndDateStr()));
                    person.setTypeOfRegister(psnDto.getTypeOfRegister());
                    person.setRelevantExperience(psnDto.getRelevantExperience());
                    person.setHoldCerByEMS(psnDto.getHoldCerByEMS());
                    person.setAclsExpiryDate(handleDate(psnDto.getAclsExpiryDate(), psnDto.getAclsExpiryDateStr()));
                    person.setAclsExpiryDateStr(handleDateString(psnDto.getAclsExpiryDate(), psnDto.getAclsExpiryDateStr()));
                    person.setBclsExpiryDate(handleDate(psnDto.getBclsExpiryDate(), psnDto.getBclsExpiryDateStr()));
                    person.setBclsExpiryDateStr(handleDateString(psnDto.getBclsExpiryDate(), psnDto.getBclsExpiryDateStr()));
                }
                if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())) {
                    person.setDesignation(designation);
                    if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                        person.setOtherDesignation(psnDto.getOtherDesignation());
                    }
                    person.setProfessionType(psnDto.getProfessionType());
                    person.setProfRegNo(psnDto.getProfRegNo());
                    person.setSpeciality(psnDto.getSpeciality());
                    person.setSpecialityOther(psnDto.getSpecialityOther());
                    person.setSubSpeciality(psnDto.getSubSpeciality());
                    person.setQualification(psnDto.getQualification());
                    person.setOtherQualification(psnDto.getOtherQualification());
                    //
                    /*person.setNeedSpcOptList(true);
                    List<SelectOption> spcOpts = person.getSpcOptList();
                    List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode, false);
                    if (!IaisCommonUtils.isEmpty(spcOpts)) {
                        for (SelectOption sp : spcOpts) {
                            if (!specialityOpts.contains(sp) && !"other".equals(sp.getValue())) {
                                specialityOpts.add(sp);
                            }
                        }
                        String specialityVal = psnDto.getSpeciality();
                        if (!StringUtil.isEmpty(specialityVal)) {
                            SelectOption sp = getSpecialtyByValue(specialityVal);
                            if (!specialityOpts.contains(sp)) {
                                specialityOpts.add(sp);
                            }
                        }
                    } else {
                        log.info(StringUtil.changeForLog("person spcOpts is empty"));
                    }
                    SelectOption otherSp = new SelectOption("other", "Others");
                    boolean flag = false;
                    for (SelectOption selectOption : specialityOpts) {
                        String value = selectOption.getValue();
                        if ("other".equals(value)) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        specialityOpts.add(otherSp);
                    }
                    person.setSpcOptList(specialityOpts);

                    boolean canMatch = false;
                    for (SelectOption sp : specialityOpts) {
                        if (sp.getValue().equals(speciality)) {
                            canMatch = true;
                            break;
                        }
                    }
                    if (!canMatch) {
                        log.info(StringUtil.changeForLog("can not match speciality:" + speciality + ",when svcCode:" + svcCode));
                        specialityOpts = getAllSpecialtySelList();
                    }
                    String specialtySelectStr = generateDropDownHtml(specialtyAttr, specialityOpts, null,
                            person.getSpeciality());
                    person.setSpecialityHtml(specialtySelectStr);
                    psnDto.setSpcOptList(specialityOpts);
                    psnDto.setSpecialityHtml(specialtySelectStr);
                    psnDto.setNeedSpcOptList(true);*/
                }
                if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnDto.getPsnType())) {
                    person.setDesignation(psnDto.getDesignation());
                    if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                        person.setOtherDesignation(psnDto.getOtherDesignation());
                    }
                    person.setOfficeTelNo(psnDto.getOfficeTelNo());
                }
                if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnDto.getPsnType())) {
                    person.setDesignation(psnDto.getDesignation());
                    if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                        person.setOtherDesignation(psnDto.getOtherDesignation());
                    }
                    person.setOfficeTelNo(psnDto.getOfficeTelNo());
                }
                psnDto.setAssignSelect(person.getAssignSelect());
                psnDto.setLicPerson(person.isLicPerson());

                AppSvcPersonAndExtDto newPersonAndExtDto = new AppSvcPersonAndExtDto();
                AppSvcPersonDto appSvcPersonDto = MiscUtil.transferEntityDto(person, AppSvcPersonDto.class);
                AppSvcPersonExtDto appSvcPersonExtDto = MiscUtil.transferEntityDto(person, AppSvcPersonExtDto.class);
                appSvcPersonExtDto.setServiceCode(svcCode);
                appSvcPersonExtDto.setAssignSelect(person.getAssignSelect());
                appSvcPersonExtDtos = appSvcPersonAndExtDto.getPersonExtDtoList();
                if (IaisCommonUtils.isEmpty(appSvcPersonExtDtos)) {
                    appSvcPersonExtDtos = IaisCommonUtils.genNewArrayList();
                }
                appSvcPersonExtDtos.add(appSvcPersonExtDto);
                newPersonAndExtDto.setPersonDto(appSvcPersonDto);
                newPersonAndExtDto.setPersonExtDtoList(appSvcPersonExtDtos);
                newPersonAndExtDto.setLicPerson(person.isLicPerson());
                personMap.put(personMapKey, newPersonAndExtDto);
            }
        }
        return personMap;
    }

    public static Date handleDate(Date date, String str) {
        Date newDate = null;
        if (date != null) {
            newDate = (Date) date.clone();
        } else {
            newDate = DateUtil.parseDate(str, Formatter.DATE);
        }
        return newDate;
    }

    public static String handleDateString(Date date, String str) {
        String newDate = null;
        if (date != null) {
            newDate = Formatter.formatDate(date);
        } else if (CommonValidator.isDate(str)) {
            newDate = str;
        }
        return newDate;
    }

    @Deprecated
    public static Map<String, AppSvcPrincipalOfficersDto> getLicPsnIntoSelMap(HttpServletRequest request,
            List<PersonnelListQueryDto> licPsnDtos) {
        Map<String, AppSvcPrincipalOfficersDto> personMap = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(licPsnDtos)) {
            for (PersonnelListQueryDto psnDto : licPsnDtos) {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(psnDto.getSvcName());
                if (hcsaServiceDto == null) {
                    log.info(StringUtil.changeForLog("service name:" + psnDto.getSvcName() + " can not get HcsaServiceDto ..."));
                    continue;
                }
                String svcCode = hcsaServiceDto.getSvcCode();
                String personMapKey = getPersonKey(psnDto.getNationality(), psnDto.getIdType(), psnDto.getIdNo());
                AppSvcPrincipalOfficersDto person = personMap.get(personMapKey);
                Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
                specialtyAttr.put("name", "specialty");
                specialtyAttr.put("class", "specialty");
                specialtyAttr.put("style", "display: none;");
                if (person == null) {
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())) {
                        psnDto.setNeedSpcOptList(true);
                        List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode, true);
                        psnDto.setSpcOptList(specialityOpts);
                        String specialtySelectStr = generateDropDownHtml(specialtyAttr, specialityOpts, null,
                                psnDto.getSpeciality());
                        psnDto.setSpecialityHtml(specialtySelectStr);
                    }
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = MiscUtil.transferEntityDto(psnDto,
                            AppSvcPrincipalOfficersDto.class);
                    appSvcPrincipalOfficersDto.setLicPerson(true);
                    appSvcPrincipalOfficersDto.setNeedDisabled(true);
                    personMap.put(personMapKey, appSvcPrincipalOfficersDto);
                } else {
                    //set different page column
                    person.setSalutation(psnDto.getSalutation());
                    person.setName(psnDto.getName());
                    person.setIdType(psnDto.getIdType());
                    person.setIdNo(psnDto.getIdNo());
                    person.setMobileNo(psnDto.getMobileNo());
                    person.setEmailAddr(psnDto.getEmailAddr());
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnDto.getPsnType())) {
                        person.setDesignation(psnDto.getDesignation());
                        person.setProfessionType(psnDto.getProfessionType());
                        person.setProfRegNo(psnDto.getProfRegNo());
                        person.setSpeciality(psnDto.getSpeciality());
                        //person.setSpecialityOther(psnDto.getSpecialityOther());
                        person.setSubSpeciality(psnDto.getSubSpeciality());
                        //
                        person.setNeedSpcOptList(true);
                        List<SelectOption> spcOpts = person.getSpcOptList();
                        List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode, false);
                        if (!IaisCommonUtils.isEmpty(spcOpts)) {
                            for (SelectOption sp : spcOpts) {
                                if (!specialityOpts.contains(sp)) {
                                    specialityOpts.add(sp);
                                }
                            }
                            person.setSpcOptList(specialityOpts);
                        } else {
                            SelectOption sp = new SelectOption("other", "Others");
                            specialityOpts.add(sp);
                            person.setSpcOptList(specialityOpts);
                        }
                        String specialtySelectStr = generateDropDownHtml(specialtyAttr, specialityOpts, null,
                                person.getSpeciality());
                        person.setSpecialityHtml(specialtySelectStr);
                    }
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnDto.getPsnType())) {
                        person.setOfficeTelNo(psnDto.getOfficeTelNo());
                    }
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnDto.getPsnType())) {
                        person.setOfficeTelNo(psnDto.getOfficeTelNo());
                    }
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnDto.getPsnType())) {

                    }

                    person.setLicPerson(true);
                    personMap.put(personMapKey, person);
                }
            }
        }
        return personMap;
    }

    public static Map<String, AppSvcPersonAndExtDto> getLicPsnIntoSelMap(List<FeUserDto> feUserDtos,
            List<PersonnelListQueryDto> licPsnDtos, Map<String, AppSvcPersonAndExtDto> personMap) {
        //user account
        if (!IaisCommonUtils.isEmpty(feUserDtos)) {
            for (FeUserDto feUserDto : feUserDtos) {
                String idType = feUserDto.getIdType();
                String idNo = feUserDto.getIdNumber();
                if (StringUtil.isEmpty(idNo) || StringUtil.isEmpty(idType)) {
                    continue;
                }
                String nationality = AppConsts.NATIONALITY_SG;
                AppSvcPersonAndExtDto appSvcPersonAndExtDto = new AppSvcPersonAndExtDto();
                AppSvcPersonDto appSvcPersonDto = new AppSvcPersonDto();
//                    appSvcPersonDto.setCurPersonelId("");
                appSvcPersonDto.setSalutation(feUserDto.getSalutation());
                appSvcPersonDto.setName(feUserDto.getDisplayName());
                appSvcPersonDto.setIdType(idType);
                appSvcPersonDto.setIdNo(idNo);
                appSvcPersonDto.setNationality(nationality);
                appSvcPersonDto.setDesignation(feUserDto.getDesignation());
                appSvcPersonDto.setOtherDesignation(feUserDto.getDesignationOther());
                appSvcPersonDto.setMobileNo(feUserDto.getMobileNo());
                appSvcPersonDto.setEmailAddr(feUserDto.getEmail());
                appSvcPersonDto.setOfficeTelNo(feUserDto.getOfficeTelNo());
                appSvcPersonDto.setCurPersonelId(null);
                appSvcPersonAndExtDto.setPersonDto(appSvcPersonDto);
                appSvcPersonAndExtDto.setLicPerson(true);
                appSvcPersonAndExtDto.setLoadingType(ApplicationConsts.PERSON_LOADING_TYPE_BLUR);
                personMap.put(getPersonKey(nationality, idType, idNo), appSvcPersonAndExtDto);
            }
        }

        if (!IaisCommonUtils.isEmpty(licPsnDtos)) {
            Map<String, String> specialtyAttr = getSpecialtyAttr();
            for (PersonnelListQueryDto psnDto : licPsnDtos) {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(psnDto.getSvcName());
                if (hcsaServiceDto == null) {
                    log.info(StringUtil.changeForLog("service name:" + psnDto.getSvcName() + " can not get HcsaServiceDto ..."));
                    continue;
                }
                String svcCode = hcsaServiceDto.getSvcCode();
                String personMapKey = getPersonKey(psnDto.getNationality(), psnDto.getIdType(), psnDto.getIdNo());
                AppSvcPersonAndExtDto appSvcPersonAndExtDto = personMap.get(personMapKey);
                String speciality = psnDto.getSpeciality();
                if (appSvcPersonAndExtDto == null) {
                    //cgo speciality
                    if (!StringUtil.isEmpty(speciality)) {
                        psnDto.setNeedSpcOptList(true);
                        List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode, true);
                        psnDto.setSpcOptList(specialityOpts);
                        String specialtySelectStr = generateDropDownHtml(specialtyAttr, specialityOpts, null,
                                psnDto.getSpeciality());
                        psnDto.setSpecialityHtml(specialtySelectStr);
                    }
                    appSvcPersonAndExtDto = new AppSvcPersonAndExtDto();
                    AppSvcPersonDto appSvcPersonDto = MiscUtil.transferEntityDto(psnDto, AppSvcPersonDto.class);
                    List<AppSvcPersonExtDto> appSvcPersonExtDtos = IaisCommonUtils.genNewArrayList();
                    AppSvcPersonExtDto appSvcPersonExtDto = MiscUtil.transferEntityDto(psnDto, AppSvcPersonExtDto.class);
                    AppSvcPrincipalOfficersDto person = MiscUtil.transferEntityDto(psnDto, AppSvcPrincipalOfficersDto.class);
                    AppPsnEditDto appPsnEditDto = setNeedEditField(person);
                    appSvcPersonExtDto.setPsnEditDto(appPsnEditDto);
                    appSvcPersonExtDto.setServiceCode(svcCode);
                    appSvcPersonExtDtos.add(appSvcPersonExtDto);
                    appSvcPersonAndExtDto.setPersonDto(appSvcPersonDto);
                    appSvcPersonAndExtDto.setPersonExtDtoList(appSvcPersonExtDtos);
                    appSvcPersonAndExtDto.setLicPerson(true);
                    personMap.put(personMapKey, appSvcPersonAndExtDto);
                } else {
                    List<AppSvcPersonExtDto> appSvcPersonExtDtos = appSvcPersonAndExtDto.getPersonExtDtoList();
                    if (IaisCommonUtils.isEmpty(appSvcPersonExtDtos)) {
                        appSvcPersonExtDtos = IaisCommonUtils.genNewArrayList();
                    }

//                    AppSvcPersonExtDto currSvcPsnExtDto = getPsnExtDtoBySvcCode(appSvcPersonExtDtos,svcCode);
                    AppSvcPrincipalOfficersDto person = genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto, svcCode, true);

//                    person.setDesignation(psnDto.getDesignation());

                    person.setProfessionType(psnDto.getProfessionType());
                    person.setProfRegNo(psnDto.getProfRegNo());
                    person.setSpeciality(psnDto.getSpeciality());
                    //person.setSpecialityOther(psnDto.getSpecialityOther());
                    person.setSubSpeciality(psnDto.getSubSpeciality());
                    //cgo speciality
                    if (!StringUtil.isEmpty(speciality)) {
                        person.setNeedSpcOptList(true);
                        List<SelectOption> spcOpts = psnDto.getSpcOptList();
                        if (IaisCommonUtils.isEmpty(spcOpts)) {
                            spcOpts = genSpecialtySelectList(svcCode, true);
                        }
                        List<SelectOption> specialityOpts = genSpecialtySelectList(svcCode, false);
                        if (!IaisCommonUtils.isEmpty(spcOpts)) {
                            for (SelectOption sp : spcOpts) {
                                if (!specialityOpts.contains(sp)) {
                                    specialityOpts.add(sp);
                                }
                            }
                            person.setSpcOptList(specialityOpts);
                        } else {
                            SelectOption sp = new SelectOption("other", "Others");
                            specialityOpts.add(sp);
                            person.setSpcOptList(specialityOpts);
                        }
                        String specialtySelectStr = generateDropDownHtml(specialtyAttr, specialityOpts, null,
                                person.getSpeciality());
                        person.setSpecialityHtml(specialtySelectStr);
                    }
                    AppSvcPersonExtDto currSvcPsnExtDto = MiscUtil.transferEntityDto(person, AppSvcPersonExtDto.class);
                    AppPsnEditDto appPsnEditDto = setNeedEditField(person);
                    currSvcPsnExtDto.setPsnEditDto(appPsnEditDto);
                    currSvcPsnExtDto.setServiceCode(svcCode);
                    appSvcPersonExtDtos.add(currSvcPsnExtDto);
                    AppSvcPersonDto appSvcPersonDto = MiscUtil.transferEntityDto(psnDto, AppSvcPersonDto.class);
                    appSvcPersonAndExtDto.setPersonDto(appSvcPersonDto);
                    appSvcPersonAndExtDto.setPersonExtDtoList(appSvcPersonExtDtos);
                    appSvcPersonAndExtDto.setLicPerson(true);
                    personMap.put(personMapKey, appSvcPersonAndExtDto);
                }
            }
        }
        return personMap;
    }

    public static List<SelectOption> genSpecialtySelectList(String svcCode, boolean needOtherOpt) {
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(svcCode)) {
            if (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please Select");
                SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl2);
                if (needOtherOpt) {
                    SelectOption ssl4 = new SelectOption("other", "Others");
                    specialtySelectList.add(ssl4);
                }
            } else if (AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)) {
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please Select");
                SelectOption ssl2 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
                SelectOption ssl3 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                if (needOtherOpt) {
                    SelectOption ssl4 = new SelectOption("other", "Others");
                    specialtySelectList.add(ssl4);
                }
            } else {
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please Select");
                SelectOption ssl2 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
                SelectOption ssl3 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
                SelectOption ssl4 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl5 = new SelectOption("Haematology", "Haematology");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl5);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);

                if (needOtherOpt) {
                    SelectOption ssl6 = new SelectOption("other", "Others");
                    specialtySelectList.add(ssl6);
                }
            }
        }
        return specialtySelectList;
    }

    public static List<SelectOption> genEasMtsSpecialtySelectList(String svcCode) {
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        specialtySelectList.add(new SelectOption("-1", "Please Select"));
        if (!StringUtil.isEmpty(svcCode)) {
            if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(svcCode)) {
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_NO_SPECIALTY,
                        MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_NO_SPECIALTY)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_EMERGENCY_MEDICINE,
                        MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_EMERGENCY_MEDICINE)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_GENERAL_SURGERY,
                        MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_GENERAL_SURGERY)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_ANAESTHESIA,
                        MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_ANAESTHESIA)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_INTENSIVE_CARE,
                        MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_INTENSIVE_CARE)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_OTHERS,
                        MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_OTHERS)));

            } else if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(svcCode)) {
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_NO_SPECIALTY,
                        MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_NO_SPECIALTY)));
                specialtySelectList.add(new SelectOption(ApplicationConsts.EAS_MTS_SPECIALTY_OTHERS,
                        MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_SPECIALTY_OTHERS)));
            }
        }
        return specialtySelectList;
    }

    public static List<SelectOption> genEasMtsDesignationSelectList(List<HcsaServiceDto> hcsaServiceDtos) {
        List<SelectOption> designationSelectList = IaisCommonUtils.genNewArrayList();
        designationSelectList.add(new SelectOption("-1", "Please Select"));
        if (!IaisCommonUtils.isEmpty(hcsaServiceDtos)) {
            boolean hasEasSvc = false;
            boolean hasMtsSvc = false;
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(hcsaServiceDto.getSvcCode())) {
                    hasEasSvc = true;
                } else if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(hcsaServiceDto.getSvcCode())) {
                    hasMtsSvc = true;
                }
            }
            SelectOption sp1 = new SelectOption(ApplicationConsts.EAS_MTS_DESIGNATION_ONLY_EAS,
                    MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_DESIGNATION_ONLY_EAS));
            SelectOption sp2 = new SelectOption(ApplicationConsts.EAS_MTS_DESIGNATION_ONLY_MTS,
                    MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_DESIGNATION_ONLY_MTS));
            SelectOption sp3 = new SelectOption(ApplicationConsts.EAS_MTS_DESIGNATION_EAS_AND_MTS,
                    MasterCodeUtil.getCodeDesc(ApplicationConsts.EAS_MTS_DESIGNATION_EAS_AND_MTS));
            if (hasEasSvc && hasMtsSvc) {
                designationSelectList.add(sp1);
                designationSelectList.add(sp2);
            } else if (hasEasSvc) {
                designationSelectList.add(sp1);
            } else if (hasMtsSvc) {
                designationSelectList.add(sp2);
            }
            designationSelectList.add(sp3);
        }
        return designationSelectList;
    }

    //todo: change this mode
    public static List<SelectOption> getAllSpecialtySelList() {
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        SelectOption ssl1 = new SelectOption("-1", "Please Select");
        SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
        SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
        SelectOption ssl4 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
        SelectOption ssl5 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
        SelectOption ssl6 = new SelectOption("other", "Others");
        specialtySelectList.add(ssl1);
        specialtySelectList.add(ssl4);
        specialtySelectList.add(ssl3);
        specialtySelectList.add(ssl5);
        specialtySelectList.add(ssl2);
        specialtySelectList.add(ssl6);
        return specialtySelectList;
    }

    public static SelectOption getSpecialtyByValue(String specialtyVal) {
        SelectOption result = new SelectOption(specialtyVal, specialtyVal);
        if (!StringUtil.isEmpty(specialtyVal)) {
            List<SelectOption> allSpecialty = getAllSpecialtySelList();
            for (SelectOption sp : allSpecialty) {
                if (specialtyVal.equals(sp.getValue())) {
                    result = sp;
                    break;
                }
            }
        }
        return result;
    }

    public static AppSubmissionDto syncPsnData(AppSubmissionDto appSubmissionDto, Map<String, AppSvcPersonAndExtDto> personMap) {
        if (appSubmissionDto == null || personMap == null) {
            return appSubmissionDto;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                String svcCode = appSvcRelatedInfoDto.getServiceCode();
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList(), personMap, svcCode);
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcCgoDtoList(), personMap, svcCode);
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), personMap, svcCode);
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(), personMap, svcCode);
                syncPsnDto(appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList(), personMap, svcCode);
            }
        }
        return appSubmissionDto;
    }


    public static List<SelectOption> genAssignPersonSel(HttpServletRequest request, boolean needFirstOpt) {
        List<SelectOption> psnSelectList = IaisCommonUtils.genNewArrayList();
        if (needFirstOpt) {
            SelectOption sp0 = new SelectOption("-1", HcsaAppConst.FIRESTOPTION);
            psnSelectList.add(sp0);
        }
        SelectOption sp1 = new SelectOption(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW, "I'd like to add a new personnel");
        psnSelectList.add(sp1);

        List<SelectOption> personList = IaisCommonUtils.genNewArrayList();
        Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request,
                HcsaAppConst.PERSONSELECTMAP);
        personMap.forEach((k, v) -> {
            AppSvcPersonDto personDto = v.getPersonDto();
            SelectOption sp = new SelectOption(k, getPersonView(personDto.getIdType(), personDto.getIdNo(), personDto.getName()));
            personList.add(sp);
        });
        //sort
        if (personList != null) {
            personList.sort(Comparator.comparing(SelectOption::getText));
            psnSelectList.addAll(personList);
        }
        return psnSelectList;
    }


    public static void setPreviewPo(AppSvcRelatedInfoDto appSvcRelatedInfoDto, HttpServletRequest request) {
        List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
        if (appSvcRelatedInfoDto != null) {
            for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList()) {
                if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                    principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                    deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
        }
        ParamUtil.setRequestAttr(request, "ReloadPrincipalOfficers", principalOfficersDtos);
        ParamUtil.setRequestAttr(request, "ReloadDeputyPrincipalOfficers", deputyPrincipalOfficersDtos);

    }

    public static void setTimeList(HttpServletRequest request) {
        List<SelectOption> timeHourList = getTimeHourList();
        List<SelectOption> timeMinList = getTimeMinList();
        ParamUtil.setRequestAttr(request, "premiseHours", timeHourList);
        ParamUtil.setRequestAttr(request, "premiseMinute", timeMinList);

    }

    public static List<SelectOption> getTimeHourList() {
        List<SelectOption> timeHourList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < 24; i++) {
            timeHourList.add(new SelectOption(String.valueOf(i), i < 10 ? "0" + String.valueOf(i) : String.valueOf(i)));
        }
        return timeHourList;
    }

    public static List<SelectOption> getTimeMinList() {
        List<SelectOption> timeMinList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < 60; i++) {
            timeMinList.add(new SelectOption(String.valueOf(i), i < 10 ? "0" + String.valueOf(i) : String.valueOf(i)));
        }
        return timeMinList;
    }

    public static void setPremSelect(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,
                HcsaAppConst.APPSUBMISSIONDTO);
        String appType = appSubmissionDto != null ? appSubmissionDto.getAppType() : ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION;
        checkPremisesMap(request);
        Map<String, AppGrpPremisesDto> licAppGrpPremisesDtoMap = (Map<String, AppGrpPremisesDto>) request.getSession()
                .getAttribute(HcsaAppConst.LIC_PREMISES_MAP);
        Map<String, AppGrpPremisesDto> appPremisesMap = (Map<String, AppGrpPremisesDto>) request.getSession()
                .getAttribute(HcsaAppConst.APP_PREMISES_MAP);
        Map<String, String> target = IaisCommonUtils.genNewHashMap(4);
        target.put(ApplicationConsts.PREMISES_TYPE_ON_SITE, "premisesSelect");
        target.put(ApplicationConsts.PREMISES_TYPE_CONVEYANCE, "conveyancePremSel");
        target.put(ApplicationConsts.PREMISES_TYPE_OFF_SITE, "offSitePremSel");
        target.put(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE, "easMtsPremSel");
        String addtional = " (Pending MOH Approval)";
        target.forEach((premiseType, sessionKey) -> {
            List<SelectOption> premisesSelect = getPremisesSel(appType);
            setPremSelect(premisesSelect, premiseType, "", licAppGrpPremisesDtoMap);
            setPremSelect(premisesSelect, premiseType, addtional, appPremisesMap);
            ParamUtil.setSessionAttr(request, sessionKey, (Serializable) premisesSelect);
        });
    }

    private static void setPremSelect(List<SelectOption> premisesSelect, String premiseType, String addtional,
            Map<String, AppGrpPremisesDto> premiseMap) {
        List<SelectOption> existingPrems = IaisCommonUtils.genNewArrayList();
        if (premiseMap != null && !premiseMap.isEmpty()) {
            for (Map.Entry<String, AppGrpPremisesDto> entry : premiseMap.entrySet()) {
                AppGrpPremisesDto item = entry.getValue();
                String premKey = entry.getKey();
                if (Objects.equals(premiseType, item.getPremisesType())) {
                    existingPrems.add(new SelectOption(premKey, item.getAddress() + addtional));
                }
            }
        }
        //sort
        doSortSelOption(existingPrems);
        premisesSelect.addAll(existingPrems);
    }

    public static void doSortSelOption(List<SelectOption> selectOptions) {
        Collections.sort(selectOptions, Comparator.comparing(SelectOption::getText));
    }

    /**
     * for preview page
     */
    public static void setDocInfo(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, List<AppSvcDocDto> appSvcDocDtos,
            List<HcsaSvcDocConfigDto> primaryDocConfig, List<HcsaSvcDocConfigDto> svcDocConfig) {
        if (!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                if (!IaisCommonUtils.isEmpty(primaryDocConfig)) {
                    for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : primaryDocConfig) {
                        String docConfigId = appGrpPrimaryDocDto.getSvcComDocId();
                        if (!StringUtil.isEmpty(docConfigId) && docConfigId.equals(hcsaSvcDocConfigDto.getId())) {
                            appGrpPrimaryDocDto.setSvcComDocName(hcsaSvcDocConfigDto.getDocTitle());
                            //break;
                        }
                    }
                }
            }
        }
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                if (!IaisCommonUtils.isEmpty(svcDocConfig)) {
                    for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : svcDocConfig) {
                        String docConfigId = appSvcDocDto.getSvcDocId();
                        if (!StringUtil.isEmpty(docConfigId) && docConfigId.equals(hcsaSvcDocConfigDto.getId())) {
                            appSvcDocDto.setUpFileName(hcsaSvcDocConfigDto.getDocTitle());
                            if (AppConsts.NO.equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                                appSvcDocDto.setPremisesVal("");
                                appSvcDocDto.setPremisesType("");
                            }
                            String dupForPerson = hcsaSvcDocConfigDto.getDupForPerson();
                            if (!StringUtil.isEmpty(dupForPerson)) {
                                appSvcDocDto.setDupForPerson(dupForPerson);
                                appSvcDocDto.setPersonType(getPsnType(dupForPerson));
                            }
                            //break;
                        }
                    }
                }
            }
        }
    }

    public static String getPremisesKey(AppGrpPremisesDto appGrpPremisesDto) {
        return MiscUtil.getPremisesKey(appGrpPremisesDto);
    }

    public static String getPremisesKey(AppGrpPremisesEntityDto rfiPremises) {
        if (rfiPremises == null) {
            return "";
        }
        String additional = rfiPremises.getPremisesType() + ApplicationConsts.DELIMITER + rfiPremises.getHciName();
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(rfiPremises.getPremisesType())) {
            additional += ApplicationConsts.DELIMITER + rfiPremises.getVehicleNo();
        }
        return MiscUtil.getPremisesKey(additional, rfiPremises.getPostalCode(), rfiPremises.getBlkNo(),
                rfiPremises.getStreetName(), rfiPremises.getBuildingName(), rfiPremises.getFloorNo(), rfiPremises.getUnitNo(),
                rfiPremises.getAppPremisesOperationalUnitDtos());
    }

    public static String getPremisesKey(PremisesListQueryDto premisesListQueryDto) {
        if (premisesListQueryDto == null) {
            return "";
        }
        String additional = premisesListQueryDto.getPremisesType() + ApplicationConsts.DELIMITER + premisesListQueryDto.getHciName();
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())) {
            additional += ApplicationConsts.DELIMITER + premisesListQueryDto.getVehicleNo();
        }
        return MiscUtil.getPremisesKey(additional, premisesListQueryDto.getPostalCode(), premisesListQueryDto.getBlkNo(),
                premisesListQueryDto.getStreetName(), premisesListQueryDto.getBuildingName(), premisesListQueryDto.getFloorNo(),
                premisesListQueryDto.getUnitNo(), premisesListQueryDto.getPremisesFloorUnits());
    }

    public static String getPremisesKey(PremisesDto premisesDto) {
        return MiscUtil.getPremisesKey(premisesDto);
    }

    public static List<String> genPremisesHciList(AppGrpPremisesDto premisesDto) {
        List<String> premisesHciList = IaisCommonUtils.genNewArrayList();
        if (premisesDto != null) {
            String premisesHciPre = "";
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesDto.getPremisesType())) {
                premisesHciPre = premisesDto.getHciName() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesDto.getPremisesType())) {
                premisesHciPre =
                        premisesDto.getHciName() + premisesDto.getConveyanceVehicleNo() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesDto.getPremisesType())) {
                premisesHciPre = premisesDto.getHciName() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesDto.getPremisesType())) {
                premisesHciPre = premisesDto.getHciName() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }
            premisesHciList.add(premisesHciPre + premisesDto.getFloorNo() + premisesDto.getUnitNo());
            List<AppPremisesOperationalUnitDto> operationalUnitDtos = premisesDto.getAppPremisesOperationalUnitDtos();
            if (!IaisCommonUtils.isEmpty(operationalUnitDtos)) {
                for (AppPremisesOperationalUnitDto operationalUnitDto : operationalUnitDtos) {
                    premisesHciList.add(premisesHciPre + operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo());
                }
            }
        }
        return premisesHciList;
    }

    public static List<String> genPremisesHciList(PremisesDto premisesDto) {
        List<String> premisesHciList = IaisCommonUtils.genNewArrayList();
        if (premisesDto != null) {
            String premisesHciPre = "";
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesDto.getPremisesType())) {
                premisesHciPre = premisesDto.getHciName() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesDto.getPremisesType())) {
                premisesHciPre = premisesDto.getHciName() + premisesDto.getVehicleNo() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesDto.getPremisesType())) {
                premisesHciPre = premisesDto.getHciName() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesDto.getPremisesType())) {
                premisesHciPre = premisesDto.getHciName() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }
            premisesHciList.add(premisesHciPre + premisesDto.getFloorNo() + premisesDto.getUnitNo());
            List<PremisesOperationalUnitDto> operationalUnitDtos = premisesDto.getPremisesOperationalUnitDtos();
            if (!IaisCommonUtils.isEmpty(operationalUnitDtos)) {
                for (PremisesOperationalUnitDto operationalUnitDto : operationalUnitDtos) {
                    premisesHciList.add(premisesHciPre + operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo());
                }
            }
        }
        return premisesHciList;
    }

    public static String getHciName(AppGrpPremisesDto appGrpPremisesDto) {
        String hciName = "";
        if (appGrpPremisesDto == null) {
            hciName = null;
        } else if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            hciName = appGrpPremisesDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            hciName = appGrpPremisesDto.getOffSiteHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            hciName = appGrpPremisesDto.getConveyanceHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            hciName = appGrpPremisesDto.getEasMtsHciName();
        }
        return hciName;
    }

    public static boolean checkIsRfi(HttpServletRequest request) {
        Object requestInformationConfig = ParamUtil.getSessionAttr(request, HcsaAppConst.REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        return isRfi;
    }

    public static AppSvcPrincipalOfficersDto getPsnInfoFromLic(HttpServletRequest request, String personKey) {
        AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
        Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request,
                HcsaAppConst.PERSONSELECTMAP);
        String svcCode = (String) ParamUtil.getSessionAttr(request, HcsaAppConst.CURRENTSVCCODE);
        if (personMap != null) {
            AppSvcPersonAndExtDto appSvcPersonAndExtDto = personMap.get(personKey);
            AppSvcPrincipalOfficersDto person = genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto, svcCode, true);
            if (person != null) {
                appSvcPrincipalOfficersDto = person;
            } else {
                personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(request,
                        HcsaAppConst.LICPERSONSELECTMAP);
                if (personMap != null) {
                    AppSvcPersonAndExtDto personAndExtDto = personMap.get(personKey);
                    AppSvcPrincipalOfficersDto personDto = genAppSvcPrincipalOfficersDto(personAndExtDto, svcCode, true);
                    if (personDto != null) {
                        appSvcPrincipalOfficersDto = personDto;
                    }
                }
            }
        }
        return appSvcPrincipalOfficersDto;
    }

    public static String getPersonKey(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto) {
        if (appSvcPrincipalOfficersDto == null) {
            return null;
        }
        return getPersonKey(appSvcPrincipalOfficersDto.getNationality(), appSvcPrincipalOfficersDto.getIdType(),
                appSvcPrincipalOfficersDto.getIdNo());
    }

    public static String getPersonKey(String nationality, String idType, String idNo) {
        return IaisCommonUtils.getPersonKey(nationality, idType, idNo);
    }

    public static String getIdNo(String personKey) {
        return IaisCommonUtils.getIdNo(personKey);
    }

    public static String getPersonView(String idType, String idNo, String name) {
        return name + ", " + idNo + " (" + MasterCodeUtil.getCodeDesc(idType) + ")";
    }

    public static String getPhName(List<SelectOption> phDtos, String dateStr) {
        String result = "";
        if (IaisCommonUtils.isEmpty(phDtos) || StringUtil.isEmpty(dateStr)) {
            return result;
        }
        for (SelectOption publicHolidayDto : phDtos) {
            if (dateStr.equals(publicHolidayDto.getValue())) {
                result = publicHolidayDto.getText();
                break;
            }
        }
        return result;
    }

    public static List<AppSvcRelatedInfoDto> addOtherSvcInfo(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            List<HcsaServiceDto> hcsaServiceDtos, boolean needSort) {
        if (!IaisCommonUtils.isEmpty(hcsaServiceDtos)) {
            List<HcsaServiceDto> otherSvcDtoList = IaisCommonUtils.genNewArrayList();
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                //
                for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                    String svcCode = hcsaServiceDto.getSvcCode();
                    int i = 0;
                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                        if (svcCode.equals(appSvcRelatedInfoDto.getServiceCode())) {
                            break;
                        }
                        String baseSvcId = appSvcRelatedInfoDto.getBaseServiceId();
                        //specified svc
                        if (!StringUtil.isEmpty(baseSvcId)) {
                            HcsaServiceDto baseSvcDto = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                            if (baseSvcDto == null) {
                                log.info(StringUtil.changeForLog("current svc id is dirty data ..."));
                                continue;
                            }
                            if (svcCode.equals(baseSvcDto.getSvcCode())) {
                                break;
                            }
                        }
                        if (i == appSvcRelatedInfoDtos.size() - 1) {
                            otherSvcDtoList.add(hcsaServiceDto);
                        }
                        i++;
                    }
                }
            } else {
                otherSvcDtoList.addAll(hcsaServiceDtos);
            }
            //create other appSvcDto
            if (!IaisCommonUtils.isEmpty(otherSvcDtoList)) {
                for (HcsaServiceDto hcsaServiceDto : otherSvcDtoList) {
                    AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                    appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                    appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                    appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                    appSvcRelatedInfoDto.setServiceType(ApplicationConsts.SERVICE_TYPE_BASE);
                    appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                }
            }
            if (needSort) {
                appSvcRelatedInfoDtos = sortAppSvcRelatDto(appSvcRelatedInfoDtos);
            }
        }
        return appSvcRelatedInfoDtos;
    }

    public static List<AppSvcRelatedInfoDto> sortAppSvcRelatDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos) {
        List<AppSvcRelatedInfoDto> newAppSvcDto = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            List<AppSvcRelatedInfoDto> baseDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcRelatedInfoDto> specDtos = IaisCommonUtils.genNewArrayList();
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                String svcCode = appSvcRelatedInfoDto.getServiceCode();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(svcCode);
                if (hcsaServiceDto == null) {
                    log.info(StringUtil.changeForLog("svc code:" + svcCode + " can not found HcsaServiceDto"));
                    continue;
                }
                String serviceType = hcsaServiceDto.getSvcType();
                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                if (ApplicationConsts.SERVICE_TYPE_BASE.equals(serviceType)) {
                    baseDtos.add(appSvcRelatedInfoDto);
                } else if (ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(serviceType)) {
                    specDtos.add(appSvcRelatedInfoDto);
                }
            }

            if (!IaisCommonUtils.isEmpty(baseDtos)) {
                baseDtos.sort(Comparator.comparing(AppSvcRelatedInfoDto::getServiceName));
                newAppSvcDto.addAll(baseDtos);
            }
            if (!IaisCommonUtils.isEmpty(specDtos)) {
                specDtos.sort(Comparator.comparing(AppSvcRelatedInfoDto::getServiceName));
                newAppSvcDto.addAll(specDtos);
            }
            appSvcRelatedInfoDtos = newAppSvcDto;
        }
        return newAppSvcDto;
    }

    public static String getPremisesHci(AppAlignLicQueryDto item) {
        String additional;
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(item.getPremisesType())) {
            additional = item.getHciName() + item.getVehicleNo();
        } else {
            additional = item.getHciName();
        }
        return MiscUtil.getPremisesKey(additional, item.getPostalCode(), item.getBlkNo(), item.getStreetName(),
                item.getBuildingName(), item.getFloorNo(), item.getUnitNo(),
                MiscUtil.transferEntityDtos(item.getPremisesOperationalUnitDtos(),
                        AppPremisesOperationalUnitDto.class));
    }

    public static boolean isAllFieldNull(AppSvcPrincipalOfficersDto person) throws Exception {
        boolean result = true;
        if (person != null) {
            PersonFieldDto personFieldDto = MiscUtil.transferEntityDto(person, PersonFieldDto.class);
            if ("-1".equals(personFieldDto.getSpeciality())) {
                personFieldDto.setSpeciality(null);
            }
            Class psnClsa = personFieldDto.getClass();
            Field[] fs = psnClsa.getDeclaredFields();
            for (Field f : fs) {
                Object value = ReflectionUtil.getPropertyObj(f, personFieldDto);
                if (!StringUtil.isEmpty(value)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public static AppPsnEditDto setNeedEditField(AppSvcPrincipalOfficersDto person) {
        AppPsnEditDto appPsnEditDto = new AppPsnEditDto();
        if (person != null) {
            PersonFieldDto personFieldDto = MiscUtil.transferEntityDto(person, PersonFieldDto.class);
            if ("-1".equals(personFieldDto.getSpeciality())) {
                personFieldDto.setSpeciality(null);
            }
            Class psnClsa = personFieldDto.getClass();
            Field[] fs = psnClsa.getDeclaredFields();
            for (Field f : fs) {
                if (Modifier.isStatic(f.getModifiers())) {
                    continue;
                }
                Object value = ReflectionUtil.getPropertyObj(f, personFieldDto);
                if (StringUtil.isEmpty(value)) {
                    ReflectionUtil.setPropertyObj(f.getName(), Boolean.TRUE, appPsnEditDto);
                }
            }
            //confirm with mingde , person_ext field can edit anytime
            appPsnEditDto.setDesignation(true);
            appPsnEditDto.setOtherDesignation(true);
            appPsnEditDto.setSubSpeciality(true);
            appPsnEditDto.setSpecialityOther(true);
            appPsnEditDto.setSpeciality(true);
            appPsnEditDto.setProfRegNo(true);
            appPsnEditDto.setProfessionType(true);
            appPsnEditDto.setQualification(true);
            appPsnEditDto.setOtherQualification(true);
            appPsnEditDto.setProfessionBoard(true);
            appPsnEditDto.setSpecialtyGetDate(true);
            appPsnEditDto.setTypeOfCurrRegi(true);
            appPsnEditDto.setCurrRegiDate(true);
            appPsnEditDto.setPraCerEndDate(true);
            appPsnEditDto.setTypeOfRegister(true);
            appPsnEditDto.setRelevantExperience(true);
            appPsnEditDto.setHoldCerByEMS(true);
            appPsnEditDto.setAclsExpiryDate(true);
            appPsnEditDto.setBclsExpiryDate(true);
            appPsnEditDto.setNoRegWithProfBoard(true);
            appPsnEditDto.setTransportYear(true);
            appPsnEditDto.setOfficeTelNo(true);

            if (ApplicationConsts.PERSON_LOADING_TYPE_BLUR.equals(person.getLoadingType())) {
                appPsnEditDto.setIdType(true);
                appPsnEditDto.setIdNo(true);
                appPsnEditDto.setNationality(true);
            }
        }
        return appPsnEditDto;
    }

    public static String[] setPsnValue(String[] arr, int i, AppSvcPrincipalOfficersDto person, String fieldName) {
        if (arr == null || arr.length <= i) {
            return new String[0];
        }
        ReflectionUtil.setPropertyObj(fieldName, arr[i], person);
        return removeArrIndex(arr, i);
    }

    public static AppSvcPrincipalOfficersDto genAppSvcPrincipalOfficersDto(AppSvcPersonAndExtDto appSvcPersonAndExtDto, String svcCode,
            boolean removeCurrExt) {
        if (appSvcPersonAndExtDto == null) {
            return null;
        }
        AppSvcPrincipalOfficersDto person = new AppSvcPrincipalOfficersDto();
        AppSvcPersonDto appSvcPersonDto = appSvcPersonAndExtDto.getPersonDto();
        if (appSvcPersonDto != null) {
            person = MiscUtil.transferEntityDto(appSvcPersonDto, AppSvcPrincipalOfficersDto.class);
        }
        List<AppSvcPersonExtDto> appSvcPersonExtDtos = appSvcPersonAndExtDto.getPersonExtDtoList();
        AppSvcPersonExtDto appSvcPersonExtDto = getPsnExtDtoBySvcCode(appSvcPersonExtDtos, svcCode);
        if (appSvcPersonExtDto == null) {
            appSvcPersonExtDto = new AppSvcPersonExtDto();
        }
        if (removeCurrExt && !IaisCommonUtils.isEmpty(appSvcPersonExtDtos)) {
            appSvcPersonExtDtos.remove(appSvcPersonExtDto);
        }
        Map<String, String> fieldMap = IaisCommonUtils.genNewHashMap();
        person = MiscUtil.transferEntityDto(appSvcPersonExtDto, AppSvcPrincipalOfficersDto.class, fieldMap, person);
        //transfer
        person.setLicPerson(appSvcPersonAndExtDto.isLicPerson());
        AppPsnEditDto appPsnEditDto = setNeedEditField(person);
        person.setPsnEditDto(appPsnEditDto);
        return person;
    }

    public static Map<String, String> getSpecialtyAttr() {
        Map<String, String> specialtyAttr = IaisCommonUtils.genNewHashMap();
        specialtyAttr.put("name", "specialty");
        specialtyAttr.put("class", "specialty");
        specialtyAttr.put("style", "display: none;");
        return specialtyAttr;
    }

    public static void setPhName(List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos) {
        if (!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)) {
            for (AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodDtos) {
                String dayName = appPremPhOpenPeriodDto.getDayName();
                String phDate = appPremPhOpenPeriodDto.getPhDate();
                if (StringUtil.isEmpty(dayName) && !StringUtil.isEmpty(phDate)) {
                    appPremPhOpenPeriodDto.setDayName(MasterCodeUtil.getCodeDesc(phDate));
                }
            }
        }
    }

    public static boolean isCharity(HttpServletRequest request) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        boolean isCharity = false;
        if (loginContext != null && AcraConsts.ENTITY_TYPE_CHARITIES.equals(loginContext.getLicenseeEntityType())) {
            isCharity = true;
        }
        return isCharity;
    }

    public static List<SelectOption> getGiroAccOptions(List<GiroAccountInfoDto> giroAccountInfoDtos) {
        List<SelectOption> selectOptionList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(giroAccountInfoDtos)) {
            for (GiroAccountInfoDto giroAccountInfoDto : giroAccountInfoDtos) {
                selectOptionList.add(new SelectOption(giroAccountInfoDto.getAcctNo(), giroAccountInfoDto.getAcctNo()));
            }
        }
        return selectOptionList;
    }

    public static List<SelectOption> getGiroAccOptions(List<AppSubmissionDto> appSubmissionDtos,
            AppSubmissionDto appSubmissionDto) {
        List<GiroAccountInfoDto> giroAccountInfoDtos = getGiroAccount(appSubmissionDtos, appSubmissionDto);
        return getGiroAccOptions(giroAccountInfoDtos);
    }

    private static List<GiroAccountInfoDto> getGiroAccount(List<AppSubmissionDto> appSubmissionDtos,
            AppSubmissionDto appSubmissionDto) {
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        if (appSubmissionDtos != null && !appSubmissionDtos.isEmpty()) {
            appSubmissionDtos.stream()
                    .filter(dto -> !StringUtil.isEmpty(dto.getLicenceId()))
                    .forEach(dto -> licIds.add(dto.getLicenceId()));

        } else if (appSubmissionDto != null) {
            if (!StringUtil.isEmpty(appSubmissionDto.getLicenceId())) {
                licIds.add(appSubmissionDto.getLicenceId());
            }
        }
        if (licIds.isEmpty()) {
            return IaisCommonUtils.genNewArrayList();
        }
        int size = appSubmissionDtos != null && !appSubmissionDtos.isEmpty() ? appSubmissionDtos.size() : 1;
        List<GiroAccountInfoDto> result = IaisCommonUtils.genNewArrayList();
        LicCommService licCommService = SpringContextHelper.getContext().getBean(LicCommService.class);
        List<GiroAccountInfoDto> giroAccountInfoDtos = licCommService.getGiroAccountsByLicIds(licIds);
        if (giroAccountInfoDtos != null && !giroAccountInfoDtos.isEmpty()) {
            giroAccountInfoDtos.stream()
                    .collect(Collectors.groupingBy(GiroAccountInfoDto::getAcctNo))
                    .forEach((k, list) -> {
                        if (list.size() == size) {
                            result.add(list.get(0));
                        }
                    });
        }
        return result;
    }

    public static boolean newAndNotRfi(HttpServletRequest request, String appType) {
        return !checkIsRfi(request) && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
    }

    public static AppGrpPremisesDto getAppGrpPremisesDto(List<AppGrpPremisesDto> appGrpPremisesDtos, String premIndexNo,
            String premType) {
        AppGrpPremisesDto appGrpPremisesDto = null;
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            for (AppGrpPremisesDto appGrpPremisesDto1 : appGrpPremisesDtos) {
                String currPremIndexNo = StringUtil.nullToEmptyStr(appGrpPremisesDto1.getPremisesIndexNo());
                String currPremType = StringUtil.nullToEmpty(appGrpPremisesDto1.getPremisesType());
                if (currPremIndexNo.equals(premIndexNo) && currPremType.equals(premType)) {
                    appGrpPremisesDto = appGrpPremisesDto1;
                    break;
                }
            }
        }
        return appGrpPremisesDto;
    }

    public static List<AppGrpPrimaryDocDto> getAppGrpprimaryDocDto(String docId, List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos) {
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(docId)) {
            for (AppGrpPrimaryDocDto docDto : appGrpPrimaryDocDtos) {
                if (docDto.getSvcComDocId().equals(docId)) {
                    appGrpPrimaryDocDtoList.add(docDto);
                }
            }
        }
        return appGrpPrimaryDocDtoList;
    }

    public static AppGrpPrimaryDocDto genEmptyPrimaryDocDto(String docConfigId) {
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
        appGrpPrimaryDocDto.setSvcComDocId(docConfigId);
        appGrpPrimaryDocDto.setSeqNum(-1);
        return appGrpPrimaryDocDto;
    }

    public static void removePremiseEmptyAlignInfo(AppSubmissionDto appSubmissionDto) {
        log.debug(StringUtil.changeForLog("remove Premise Empty Align Info start ..."));
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if (IaisCommonUtils.isEmpty(appGrpPremisesDtoList)) {
            log.debug(StringUtil.changeForLog("appGrpPremisesDtoList is empty ..."));
        }
        //remove empty align primary doc
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = removeEmptyAlignPrimaryDoc(appGrpPremisesDtoList,
                appSubmissionDto.getAppGrpPrimaryDocDtos());
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                //remove empty align laboratoryDisciplinesDto
                List<AppSvcLaboratoryDisciplinesDto> laboratoryDisciplinesDtos = removeEmptyAlignSvcScope(
                        appGrpPremisesDtoList, appSvcRelatedInfoDto);
                appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(laboratoryDisciplinesDtos);
                //remove empty align disciplineAllocation
                List<AppSvcDisciplineAllocationDto> disciplineAllocationDtos = removeEmptyAlignAllocation(
                        appGrpPremisesDtoList, appSvcRelatedInfoDto);
                appSvcRelatedInfoDto.setAppSvcDisciplineAllocationDtoList(disciplineAllocationDtos);
                //remove empty align svc spec doc
                List<AppSvcDocDto> appSvcDocDtos = removeEmptyAlignSvcDoc(appGrpPremisesDtoList,
                        appSvcRelatedInfoDto);
                appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                //remove empty align business info
                List<AppSvcBusinessDto> appSvcBusinessDtos = removeEmptyAlignBusiness(appGrpPremisesDtoList,
                        appSvcRelatedInfoDto);
                appSvcRelatedInfoDto.setAppSvcBusinessDtoList(appSvcBusinessDtos);
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
        log.debug(StringUtil.changeForLog("remove Premise Empty Align Info end ..."));
    }


    public static void updatePremisesAddress(AppSubmissionDto appSubmissionDto) {
        log.debug(StringUtil.changeForLog("update Premise Address start ..."));
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtoList)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                if (!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos)) {
                    for (AppSvcLaboratoryDisciplinesDto laboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtos) {
                        AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDto(appGrpPremisesDtoList,
                                laboratoryDisciplinesDto.getPremiseVal(), laboratoryDisciplinesDto.getPremiseType());
                        if (appGrpPremisesDto != null) {
                            laboratoryDisciplinesDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                        }
                    }
                }
                List<AppSvcBusinessDto> appSvcBusinessDtos = appSvcRelatedInfoDto.getAppSvcBusinessDtoList();
                if (!IaisCommonUtils.isEmpty(appSvcBusinessDtos)) {
                    for (AppSvcBusinessDto appSvcBusinessDto : appSvcBusinessDtos) {
                        AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDto(appGrpPremisesDtoList,
                                appSvcBusinessDto.getPremIndexNo(), appSvcBusinessDto.getPremType());
                        if (appGrpPremisesDto != null) {
                            appSvcBusinessDto.setPremAddress(appGrpPremisesDto.getAddress());
                        }
                    }
                }
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
        log.debug(StringUtil.changeForLog("update Premise Address end ..."));
    }


    public static void svcDocMandatoryValidate(List<HcsaSvcDocConfigDto> svcDocConfigDtos, List<AppSvcDocDto> appSvcDocDtos,
            List<AppGrpPremisesDto> appGrpPremisesDtos, AppSvcRelatedInfoDto appSvcRelatedInfoDto, Map<String, String> errorMap) {

        String err006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Document", "field");
        if (!IaisCommonUtils.isEmpty(svcDocConfigDtos)) {
            int i = 0;
            String suffix = "Error";
            for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : svcDocConfigDtos) {
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                String dupForPerson = hcsaSvcDocConfigDto.getDupForPerson();
                String configId = hcsaSvcDocConfigDto.getId();
                String errKey = i + "svcDoc" + appSvcRelatedInfoDto.getServiceCode();
                Boolean isMandatory = hcsaSvcDocConfigDto.getIsMandatory();
                i++;
                if (!isMandatory) {
                    continue;
                }
                boolean mandatoryFlag;
                if (isMandatory) {
                    mandatoryFlag = false;
                } else {
                    mandatoryFlag = true;
                }
                if (IaisCommonUtils.isEmpty(appSvcDocDtos)) {
                    appSvcDocDtos = IaisCommonUtils.genNewArrayList();
                }
                if ("0".equals(dupForPrem)) {
                    if (StringUtil.isEmpty(dupForPerson)) {
                        for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                            String svcDocId = appSvcDocDto.getSvcDocId();
                            if (hcsaSvcDocConfigDto.getId().equals(svcDocId)) {
                                mandatoryFlag = true;
                                break;
                            }
                        }
                        if (!mandatoryFlag) {
                            errorMap.put(errKey + suffix, err006);
                        }
                    } else {
                        List<AppSvcPrincipalOfficersDto> psnDtoList = getPsnByDupForPerson(appSvcRelatedInfoDto,
                                dupForPerson);
                        for (AppSvcPrincipalOfficersDto psnDto : psnDtoList) {
                            String psnIndexNo = psnDto.getIndexNo();
                            AppSvcDocDto appSvcDocDto = getSvcDtoByConfigIdAndPsnIndexNo(appSvcDocDtos, configId, "", "", psnIndexNo);
                            if (appSvcDocDto == null) {
                                String specErrKey = errKey + psnIndexNo + suffix;
                                errorMap.put(specErrKey, err006);
                            }
                        }
                    }
                } else if ("1".equals(dupForPrem)) {
                    if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                            String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                            if (StringUtil.isEmpty(dupForPerson)) {
                                AppSvcDocDto appSvcDocDto = getSvcDtoByConfigIdAndPsnIndexNo(appSvcDocDtos, configId, premIndexNo,
                                        appGrpPremisesDto.getPremisesType(), "");
                                if (appSvcDocDto == null) {
                                    String specErrKey = errKey + premIndexNo + suffix;
                                    errorMap.put(specErrKey, err006);
                                }
                            } else {
                                List<AppSvcPrincipalOfficersDto> psnDtoList = getPsnByDupForPerson(
                                        appSvcRelatedInfoDto, dupForPerson);
                                for (AppSvcPrincipalOfficersDto psnDto : psnDtoList) {
                                    String psnIndexNo = psnDto.getIndexNo();
                                    AppSvcDocDto appSvcDocDto = getSvcDtoByConfigIdAndPsnIndexNo(appSvcDocDtos, configId,
                                            appGrpPremisesDto.getPremisesIndexNo(), appGrpPremisesDto.getPremisesType(), psnIndexNo);
                                    if (appSvcDocDto == null) {
                                        String specErrKey = errKey + premIndexNo + psnIndexNo + suffix;
                                        errorMap.put(specErrKey, err006);
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * show others
     */
    public static List<AppSvcChckListDto> handlerPleaseIndicateLab(List<AppSvcChckListDto> appSvcChckListDtos,
            Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap) throws CloneNotSupportedException {
        List<AppSvcChckListDto> newAppSvcChckListDtos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcChckListDtos) && svcScopeAlignMap != null) {
            AppSvcChckListDto targetDto = getScopeDtoByRecursiveTarNameUpward(appSvcChckListDtos, svcScopeAlignMap,
                    HcsaAppConst.PLEASEINDICATE, HcsaAppConst.SERVICE_SCOPE_LAB_OTHERS);
            if (targetDto != null) {
                for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                    AppSvcChckListDto newAppSvcChckListDto = (AppSvcChckListDto) CopyUtil.copyMutableObject(appSvcChckListDto);
                    String chkName = newAppSvcChckListDto.getChkName();
                    if (HcsaAppConst.PLEASEINDICATE.equals(chkName)) {
                        continue;
                    }
                    if (HcsaAppConst.SERVICE_SCOPE_LAB_OTHERS.equals(chkName)) {
                        chkName = chkName + " (" + targetDto.getOtherScopeName() + ")";
                        newAppSvcChckListDto.setChkName(chkName);
                    }
                    newAppSvcChckListDtos.add(newAppSvcChckListDto);
                }
            } else {
                newAppSvcChckListDtos = appSvcChckListDtos;
            }
        }
        return newAppSvcChckListDtos;
    }

    public static AppSvcChckListDto getScopeDtoByRecursiveTarNameUpward(List<AppSvcChckListDto> appSvcChckListDtos,
            Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap, String recursiveStartName, String recursiveEndName) {
        AppSvcChckListDto targetDto = null;
        if (svcScopeAlignMap != null && !IaisCommonUtils.isEmpty(appSvcChckListDtos) && !StringUtil.isEmpty(
                recursiveStartName) && !StringUtil.isEmpty(recursiveEndName)) {
            for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                String chkName = appSvcChckListDto.getChkName();
                if (recursiveStartName.equals(chkName)) {
                    HcsaSvcSubtypeOrSubsumedDto targetConfigDto = svcScopeAlignMap.get(appSvcChckListDto.getChkLstConfId());
                    if (targetConfigDto != null) {
                        String parentId = targetConfigDto.getParentId();
                        if (!StringUtil.isEmpty(parentId)) {
                            HcsaSvcSubtypeOrSubsumedDto parentDto = getScopeConfigByRecursiveTarNameUpward(svcScopeAlignMap,
                                    recursiveEndName, parentId);
                            if (parentDto != null && recursiveEndName.equals(parentDto.getName())) {
                                targetDto = getSvcChckListDtoByConfigId(targetConfigDto.getId(), appSvcChckListDtos);
                            }
                        }
                    }
                    break;
                }
            }
        }
        return targetDto;
    }

    public static AppSvcChckListDto getSvcChckListDtoByConfigName(String configName, List<AppSvcChckListDto> appSvcChckListDtos) {
        AppSvcChckListDto result = null;
        if (!StringUtil.isEmpty(configName) && !IaisCommonUtils.isEmpty(appSvcChckListDtos)) {
            for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                if (configName.equals(appSvcChckListDto.getChkName())) {
                    result = appSvcChckListDto;
                    break;
                }
            }
        }
        return result;
    }

    public static HashMap<String, List<AppSvcDisciplineAllocationDto>> getDisciplineAllocationDtoList(
            AppSubmissionDto appSubmissionDto, String svcId) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("get DisciplineAllocationDtoList start..."));
        if (appSubmissionDto == null || StringUtil.isEmpty(svcId)) {
            return null;
        }
        log.info(StringUtil.changeForLog(svcId + "svcId"));
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = null;
        if (!IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())) {
            for (AppSvcRelatedInfoDto item : appSubmissionDto.getAppSvcRelatedInfoDtoList()) {
                log.info(StringUtil.changeForLog(item.getServiceId() + "item.getServiceId()"));
                if (svcId.equals(item.getServiceId())) {
                    appSvcRelatedInfoDto = item;
                    break;
                }
            }
        }
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        if (appSvcRelatedInfoDto != null) {
            allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        } else {
            log.info(StringUtil.changeForLog("can not found the match appSvcRelatedInfoDto ..."));
            return null;
        }
        Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap = ApplicationHelper.getScopeAlignMap(svcId);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        HashMap<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = IaisCommonUtils.genNewHashMap();
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
            List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = IaisCommonUtils.genNewArrayList();
            String premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
            //get curr premises's appSvcChckListDto
            List<AppSvcChckListDto> appSvcChckListDtoList = IaisCommonUtils.genNewArrayList();
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            List<AppSvcPersonnelDto> appSlList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
            if (!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtoList) && !StringUtil.isEmpty(premisesIndexNo)) {
                log.info(StringUtil.changeForLog(
                        "appSvcLaboratoryDisciplinesDtoList size:" + appSvcLaboratoryDisciplinesDtoList.size()));
                for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
                    if (premisesIndexNo.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())) {
                        appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                        break;
                    }
                }
                log.debug(StringUtil.changeForLog("appSvcChckListDtoList size:" + appSvcChckListDtoList.size()));
                List<AppSvcChckListDto> newAppSvcChckListDtos = ApplicationHelper.handlerPleaseIndicateLab(appSvcChckListDtoList,
                        svcScopeAlignMap);
                log.debug(StringUtil.changeForLog("newAppSvcChckListDtos size:" + appSvcChckListDtoList.size()));
                for (AppSvcChckListDto appSvcChckListDto : newAppSvcChckListDtos) {
                    AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto = null;
                    String chkSvcId = appSvcChckListDto.getChkLstConfId();
                    if (!StringUtil.isEmpty(chkSvcId) && !IaisCommonUtils.isEmpty(allocationDto)) {
                        log.info(StringUtil.changeForLog("allocationDto size:" + allocationDto.size()));
                        for (AppSvcDisciplineAllocationDto allocation : allocationDto) {
                            if (premisesIndexNo.equals(allocation.getPremiseVal()) && chkSvcId.equals(allocation.getChkLstConfId())) {
                                log.info(StringUtil.changeForLog("set chkName ..."));
                                appSvcDisciplineAllocationDto = (AppSvcDisciplineAllocationDto) CopyUtil.copyMutableObject(allocation);
                                //set chkName
                                String chkName = appSvcChckListDto.getChkName();
                                if (HcsaAppConst.PLEASEINDICATE.equals(chkName)) {
                                    appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getOtherScopeName());
                                } else {
                                    appSvcDisciplineAllocationDto.setChkLstName(chkName);
                                }
                                //set selCgoName
                                String cgoPerson = allocation.getCgoPerson();
                                if (!IaisCommonUtils.isEmpty(appSvcCgoDtoList) && !StringUtil.isEmpty(cgoPerson)) {
                                    for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtoList) {
                                        if (Objects.equals(cgoPerson, ApplicationHelper.getPersonKey(appSvcCgoDto))) {
                                            log.info(StringUtil.changeForLog("set cgoSel ..."));
                                            appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                                            break;
                                        }
                                    }
                                }
                                if (!IaisCommonUtils.isEmpty(appSlList) && !StringUtil.isEmpty(allocation.getSlIndex())) {
                                    for (AppSvcPersonnelDto avpd : appSlList) {
                                        if (allocation.getSlIndex().equals(avpd.getIndexNo())) {
                                            appSvcDisciplineAllocationDto.setSectionLeaderName(avpd.getName());
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if (appSvcDisciplineAllocationDto == null) {
                            log.info(StringUtil.changeForLog("new AppSvcDisciplineAllocationDto"));
                            appSvcDisciplineAllocationDto = new AppSvcDisciplineAllocationDto();
                            appSvcDisciplineAllocationDto.setPremiseVal(premisesIndexNo);
                            appSvcDisciplineAllocationDto.setChkLstConfId(appSvcChckListDto.getChkLstConfId());
                            appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                        }
                        reloadDisciplineAllocation.add(appSvcDisciplineAllocationDto);
                    }
                }
                reloadDisciplineAllocationMap.put(premisesIndexNo, reloadDisciplineAllocation);
            }
        }
        log.info(StringUtil.changeForLog("get DisciplineAllocationDtoList end..."));
        return reloadDisciplineAllocationMap;
    }

    public static int getAlocationAutualSize(List<AppSvcDisciplineAllocationDto> daList, String svcId,
            Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap) {
        if (daList == null || daList.isEmpty()) {
            return 0;
        }
        if (svcScopeAlignMap == null) {
            svcScopeAlignMap = getScopeAlignMap(svcId);
        }
        if (svcScopeAlignMap == null) {
            return daList.size();
        }
        Map<String, HcsaSvcSubtypeOrSubsumedDto> map = svcScopeAlignMap;
        long count = daList.stream()
                .map(dto -> map.get(dto.getChkLstConfId()))
                .filter(Objects::nonNull)
                .filter(dto -> StringUtil.isIn(dto.getName(), new String[]{HcsaAppConst.PLEASEINDICATE,
                        HcsaAppConst.SERVICE_SCOPE_LAB_OTHERS}))
                .count();
        return count > 1 ? daList.size() - 1 : daList.size();
    }

    public static Map<String, HcsaSvcSubtypeOrSubsumedDto> getScopeAlignMap(String svcId) {
        return getScopeAlignMap(svcId, null);
    }

    public static Map<String, HcsaSvcSubtypeOrSubsumedDto> getScopeAlignMap(String svcId, HttpServletRequest request) {
        List<HcsaSvcSubtypeOrSubsumedDto> svcScopeDtoList = null;
        if (request != null) {
            svcScopeDtoList = (List<HcsaSvcSubtypeOrSubsumedDto>) ParamUtil.getSessionAttr(request, "HcsaSvcSubtypeOrSubsumedDto");
        }
        if (svcScopeDtoList == null) {
            ConfigCommService configService = SpringHelper.getBean(ConfigCommService.class);
            svcScopeDtoList = configService.loadLaboratoryDisciplines(svcId);
        }
        Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap = IaisCommonUtils.genNewHashMap();
        if (svcScopeDtoList == null || svcScopeDtoList.isEmpty()) {
            return svcScopeAlignMap;
        }
        recursingSvcScope(svcScopeDtoList, svcScopeAlignMap);
        return svcScopeAlignMap;
    }

    //key is config id
    public static void recursingSvcScope(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos,
            Map<String, HcsaSvcSubtypeOrSubsumedDto> allCheckListMap) {

        for (HcsaSvcSubtypeOrSubsumedDto dto : hcsaSvcSubtypeOrSubsumedDtos) {
            allCheckListMap.put(dto.getId(), dto);
            if (dto.getList() != null && dto.getList().size() > 0) {
                recursingSvcScope(dto.getList(), allCheckListMap);
            }
        }

    }

    //key is config name
    public static void recursingSvcScopeKeyIsName(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos,
            Map<String, HcsaSvcSubtypeOrSubsumedDto> allCheckListMap) {

        for (HcsaSvcSubtypeOrSubsumedDto dto : hcsaSvcSubtypeOrSubsumedDtos) {
            allCheckListMap.put(dto.getName(), dto);
            if (dto.getList() != null && dto.getList().size() > 0) {
                recursingSvcScopeKeyIsName(dto.getList(), allCheckListMap);
            }
        }

    }

    public static void setPremAddress(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    setPremAddressForSvcScope(appSubmissionDto.getAppGrpPremisesDtoList(), appSvcRelatedInfoDto);
                }
            }
        }
    }

    public static String generateMultipleDropDown(Map<String, String> pageAttr, List<SelectOption> selectOptionList,
            String firestOption, List<String> checkValList) {
        StringBuilder result = new StringBuilder();
        if (!IaisCommonUtils.isEmpty(selectOptionList) && !IaisCommonUtils.isEmpty(pageAttr)) {
            String id = pageAttr.get("id");
            if (StringUtil.isEmpty(id)) {
                id = "";
            }
            result.append("<div class=\"row\"><div class=\"col-md-12 multi-select\">")
                    .append("<div style=\"height: 200px; border: 1px solid darkgrey;overflow: scroll\" id=\"")
                    .append(id)
                    .append("Clear\">");
            int i = 0;
            for (SelectOption sp : selectOptionList) {
                String alignId = pageAttr.get("name") + i;
                result.append("<label class=\"checkbox-custom check-primary\" style=\"margin-left: 2px\">")
                        .append("<input value=\"")
                        .append(sp.getValue())
                        .append('\"');
                for (Map.Entry<String, String> entry : pageAttr.entrySet()) {
                    result.append(entry.getKey())
                            .append("=\"");
                    if ("id".equals(entry.getKey())) {
                        result.append(alignId)
                                .append('\"');
                    } else {
                        result.append(entry.getValue())
                                .append('\"');
                    }
                }
                result.append("type=\"checkbox\">")
                        .append(" <label for=\"")
                        .append(alignId)
                        .append("\">")
                        .append("<span>")
                        .append(sp.getText())
                        .append("</span>")
                        .append("</label>")
                        .append("</label><br>");
                i++;
            }
            result.append("</div></div></div>");
        }

        return result.toString();
    }

    public static List<SelectOption> genWorkingDaySp() {
        List<SelectOption> workingDaySp = IaisCommonUtils.genNewArrayList();
        SelectOption sp1 = new SelectOption("Mon", "Monday");
        SelectOption sp2 = new SelectOption("Tue", "Tuesday");
        SelectOption sp3 = new SelectOption("Wed", "Wednesday");
        SelectOption sp4 = new SelectOption("Thu", "Thursday");
        SelectOption sp5 = new SelectOption("Fri", "Friday");
        SelectOption sp6 = new SelectOption("Sat", "Saturday");
        SelectOption sp7 = new SelectOption("Sun", "Sunday");
        workingDaySp.add(sp1);
        workingDaySp.add(sp2);
        workingDaySp.add(sp3);
        workingDaySp.add(sp4);
        workingDaySp.add(sp5);
        workingDaySp.add(sp6);
        workingDaySp.add(sp7);
        return workingDaySp;
    }

    public static List<AppSvcPrincipalOfficersDto> getPsnByDupForPerson(AppSvcRelatedInfoDto appSvcRelatedInfoDto,
            String dupForPerson) {
        List<AppSvcPrincipalOfficersDto> psnDtoList = IaisCommonUtils.genNewArrayList();
        List<AppSvcPrincipalOfficersDto> svcPsnDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        switch (dupForPerson) {
            case ApplicationConsts.DUP_FOR_PERSON_CGO:
                List<AppSvcPrincipalOfficersDto> cgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                psnDtoList = transferCgoToPsnDtoList(cgoDtos);
                break;
            case ApplicationConsts.DUP_FOR_PERSON_PO:
                if (!IaisCommonUtils.isEmpty(svcPsnDtoList)) {
                    for (AppSvcPrincipalOfficersDto svcPsnDto : svcPsnDtoList) {
                        if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(svcPsnDto.getPsnType())) {
                            psnDtoList.add(svcPsnDto);
                        }
                    }
                }
                break;
            case ApplicationConsts.DUP_FOR_PERSON_DPO:
                if (!IaisCommonUtils.isEmpty(svcPsnDtoList)) {
                    for (AppSvcPrincipalOfficersDto svcPsnDto : svcPsnDtoList) {
                        if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(svcPsnDto.getPsnType())) {
                            psnDtoList.add(svcPsnDto);
                        }
                    }
                }
                break;
            case ApplicationConsts.DUP_FOR_PERSON_MAP:
                if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDto.getAppSvcMedAlertPersonList())) {
                    psnDtoList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                }
                break;
            case ApplicationConsts.DUP_FOR_PERSON_SVCPSN:
                List<AppSvcPersonnelDto> spDtos = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
                if (!IaisCommonUtils.isEmpty(spDtos)) {
                    for (AppSvcPersonnelDto spDto : spDtos) {
                        AppSvcPrincipalOfficersDto psnDto = new AppSvcPrincipalOfficersDto();
                        psnDto.setIndexNo(spDto.getIndexNo());
                        psnDtoList.add(psnDto);
                    }
                }
                break;
            case ApplicationConsts.DUP_FOR_PERSON_CD:
                if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList())) {
                    psnDtoList = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
                }
                break;
            case ApplicationConsts.DUP_FOR_PERSON_SL:
                List<AppSvcPersonnelDto> appSvcSectionLeaderList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
                if (!IaisCommonUtils.isEmpty(appSvcSectionLeaderList)) {
                    for (AppSvcPersonnelDto spDto : appSvcSectionLeaderList) {
                        AppSvcPrincipalOfficersDto psnDto = new AppSvcPrincipalOfficersDto();
                        psnDto.setIndexNo(spDto.getIndexNo());
                        psnDtoList.add(psnDto);
                    }
                }
                break;
            default:
                break;
        }
        return psnDtoList;
    }


    public static Map<String, List<AppGrpPrimaryDocDto>> genPrimaryDocReloadMap(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos,
            List<AppGrpPremisesDto> appGrpPremisesDtos, List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos) {
        Map<String, List<AppGrpPrimaryDocDto>> reloadMap = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos) && !IaisCommonUtils.isEmpty(
                appGrpPrimaryDocDtos)) {
            for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocConfigDtos) {
                String configId = hcsaSvcDocConfigDto.getId();
                String configTitle = hcsaSvcDocConfigDto.getDocTitle();
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                if ("0".equals(dupForPrem)) {
                    List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos1 = getAppGrpPrimaryDocDtoByConfigId(appGrpPrimaryDocDtos, configId,
                            "");
                    setPrimaryDocDisplayTitle(appGrpPrimaryDocDtos1, configTitle);
                    reloadMap.put(configId, appGrpPrimaryDocDtos1);
                } else if ("1".equals(dupForPrem)) {
                    int premCount = 1;
                    String premTitleTemplate = HcsaAppConst.TITLE_MODE_OF_SVCDLVY + " ${premCount}: ${configTitle}";
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos1 = getAppGrpPrimaryDocDtoByConfigId(appGrpPrimaryDocDtos,
                                configId, appGrpPremisesDto.getPremisesIndexNo());
                        String displayTitle = premTitleTemplate.replace("${premCount}", String.valueOf(premCount)).replace(
                                "${configTitle}", configTitle);
                        setPrimaryDocDisplayTitle(appGrpPrimaryDocDtos1, displayTitle);
                        reloadMap.put(appGrpPremisesDto.getPremisesIndexNo() + configId, appGrpPrimaryDocDtos1);
                        premCount++;
                    }
                }
            }
        }
        //do sort
        if (!IaisCommonUtils.isEmpty(reloadMap)) {
            reloadMap.forEach((k, v) -> {
                if (v != null && v.size() > 1) {
                    Collections.sort(v, Comparator.comparing(AppGrpPrimaryDocDto::getSeqNum));
                }
            });
        }
        return reloadMap;
    }

    public static Map<String, List<AppSvcDocDto>> genSvcDocReloadMap(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos,
            List<AppGrpPremisesDto> appGrpPremisesDtos, AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        Map<String, List<AppSvcDocDto>> reloadMap = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && appSvcRelatedInfoDto != null && !IaisCommonUtils.isEmpty(
                appGrpPremisesDtos)) {
            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocConfigDtos) {
                String configId = hcsaSvcDocConfigDto.getId();
                String configTitle = hcsaSvcDocConfigDto.getDocTitle();
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                String dupForPerson = hcsaSvcDocConfigDto.getDupForPerson();
                if ("0".equals(dupForPrem)) {
                    setSvcDocDisplayTitle(dupForPrem, 0, "", dupForPerson, configId, configTitle, appSvcDocDtos, appSvcRelatedInfoDto,
                            reloadMap);
                } else if ("1".equals(dupForPrem)) {
                    int premCount = 1;
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                        setSvcDocDisplayTitle(dupForPrem, premCount, appGrpPremisesDto.getPremisesIndexNo(), dupForPerson, configId,
                                configTitle, appSvcDocDtos, appSvcRelatedInfoDto, reloadMap);
                        premCount++;
                    }
                }
            }
        }
        //do sort
        if (!IaisCommonUtils.isEmpty(reloadMap)) {
            reloadMap.forEach((k, v) -> {
                if (v != null && v.size() > 1) {
                    Collections.sort(v, Comparator.comparing(AppSvcDocDto::getSeqNum));
                }
            });
        }
        return reloadMap;
    }

    //for single premises
    public static void addPremAlignForPrimaryDoc(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos,
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, List<AppGrpPremisesDto> appGrpPremisesDtos) {
        if (!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && !IaisCommonUtils.isEmpty(
                appGrpPrimaryDocDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            for (HcsaSvcDocConfigDto config : hcsaSvcDocConfigDtos) {
                if ("1".equals(config.getDupForPrem())) {
                    List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = getAppGrpprimaryDocDto(config.getId(),
                            appGrpPrimaryDocDtos);
                    if (!IaisCommonUtils.isEmpty(
                            appGrpPrimaryDocDtoList) && appGrpPremisesDtos != null && appGrpPremisesDtos.size() > 0) {
                        String premIndex = appGrpPremisesDtos.get(0).getPremisesIndexNo();
                        String premType = appGrpPremisesDtos.get(0).getPremisesType();
                        for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
                            appGrpPrimaryDocDto.setPremisessName(premIndex);
                            appGrpPrimaryDocDto.setPremisessType(premType);
                        }
                    }
                }
            }
        }
    }

    //for single premises
    public static void addPremAlignForSvcDoc(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos, List<AppSvcDocDto> appSvcDocDtos,
            List<AppGrpPremisesDto> appGrpPremisesDtos) {
        if (IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) || IaisCommonUtils.isEmpty(appSvcDocDtos)
                || IaisCommonUtils.isEmpty(appGrpPremisesDtos) || appGrpPremisesDtos.size() != 1) {
            return;
        }
        for (HcsaSvcDocConfigDto config : hcsaSvcDocConfigDtos) {
            if ("1".equals(config.getDupForPrem())) {
                List<AppSvcDocDto> appSvcDocDtoList = getAppSvcDocDtoByConfigId(appSvcDocDtos, config.getId());
                if (!IaisCommonUtils.isEmpty(appSvcDocDtoList)) {
                    String premIndex = appGrpPremisesDtos.get(0).getPremisesIndexNo();
                    String premType = appGrpPremisesDtos.get(0).getPremisesType();
                    for (AppSvcDocDto appSvcDocDto : appSvcDocDtoList) {
                        appSvcDocDto.setPremisesType(premType);
                        appSvcDocDto.setPremisesVal(premIndex);
                    }
                }
            }
        }
    }

    public static void setDupForPersonAttr(HttpServletRequest request, AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        if (appSvcRelatedInfoDto != null) {
            ParamUtil.setRequestAttr(request, HcsaAppConst.GOVERNANCEOFFICERSDTOLIST,
                    appSvcRelatedInfoDto.getAppSvcCgoDtoList());
            List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
            assignPoDpoDto(appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), principalOfficersDtos,
                    deputyPrincipalOfficersDtos);
            ParamUtil.setRequestAttr(request, "ReloadPrincipalOfficers", principalOfficersDtos);
            ParamUtil.setRequestAttr(request, "ReloadDeputyPrincipalOfficers", deputyPrincipalOfficersDtos);
            ParamUtil.setRequestAttr(request, "AppSvcMedAlertPsn", appSvcRelatedInfoDto.getAppSvcMedAlertPersonList());
            ParamUtil.setRequestAttr(request, "AppSvcPersonnelDtoList", appSvcRelatedInfoDto.getAppSvcPersonnelDtoList());
            ParamUtil.setRequestAttr(request, "clinicalDirectorDtoList", appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList());
            ParamUtil.setRequestAttr(request, "sectionLeaderList", appSvcRelatedInfoDto.getAppSvcSectionLeaderList());
        }
    }

    public static String genMutilSelectOpHtml(Map<String, String> attrMap, List<SelectOption> selectOptionList, String firestOption,
            List<String> checkedVals, boolean multiSelect, boolean isTransfer) {
        StringBuilder sBuffer = new StringBuilder(100);
        sBuffer.append("<div ");
        if (!isTransfer) {
            sBuffer.append("class=\"col-md-12 col-xs-12 multi-select\"");
        }
        sBuffer.append("><select ");
        if (multiSelect) {
            sBuffer.append("multiple=\"multiple\" ");
        }
        String name = "";
        for (Map.Entry<String, String> entry : attrMap.entrySet()) {
            sBuffer.append(entry.getKey())
                    .append("=\"")
                    .append(entry.getValue())
                    .append('\"');
            if (StringUtil.isEmpty(name) && "name".equals(entry.getKey())) {
                name = entry.getValue();
            }
        }
        sBuffer.append(" >");
        if (!StringUtil.isEmpty(firestOption)) {
            sBuffer.append("<option value=\"\">")
                    .append(firestOption)
                    .append("</option>");
        }
        for (SelectOption sp : selectOptionList) {
            if (!IaisCommonUtils.isEmpty(checkedVals)) {
                if (checkedVals.contains(sp.getValue())) {
                    sBuffer.append("<option selected=\"selected\" value=\"")
                            .append(sp.getValue())
                            .append("\">")
                            .append(sp.getText())
                            .append("</option>");
                } else {
                    sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
                }
            } else {
                sBuffer.append("<option value=\"").append(sp.getValue()).append("\">").append(sp.getText()).append("</option>");
            }
        }
        sBuffer.append("</select>").append("</div>");
        // error span
        sBuffer.append("<div class=\"col-md-12 col-xs-12\">")
                .append("<span class=\"error-msg \" name=\"iaisErrorMsg\" id=\"error_").append(name).append("\"></span>")
                .append("</div>");
        return sBuffer.toString();
    }

    public static String genMutilSelectOpHtml(Map<String, String> attrMap, List<SelectOption> selectOptionList, String firestOption,
            List<String> checkedVals, boolean multiSelect) {
        return genMutilSelectOpHtml(attrMap, selectOptionList, firestOption, checkedVals, multiSelect, false);
    }

    public static String getPsnType(String dupForPerson) {
        String psnType = "common";
        if (!StringUtil.isEmpty(dupForPerson)) {
            switch (dupForPerson) {
                case ApplicationConsts.DUP_FOR_PERSON_CGO:
                    psnType = ApplicationConsts.PERSONNEL_PSN_TYPE_CGO;
                    break;
                case ApplicationConsts.DUP_FOR_PERSON_PO:
                    psnType = ApplicationConsts.PERSONNEL_PSN_TYPE_PO;
                    break;
                case ApplicationConsts.DUP_FOR_PERSON_DPO:
                    psnType = ApplicationConsts.PERSONNEL_PSN_TYPE_DPO;
                    break;
                case ApplicationConsts.DUP_FOR_PERSON_MAP:
                    psnType = ApplicationConsts.PERSONNEL_PSN_TYPE_MAP;
                    break;
                case ApplicationConsts.DUP_FOR_PERSON_SVCPSN:
                    psnType = ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL;
                    break;
                case ApplicationConsts.DUP_FOR_PERSON_CD:
                    psnType = ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR;
                    break;
                case ApplicationConsts.DUP_FOR_PERSON_SL:
                    psnType = ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER;
                    break;
                default:
                    break;
            }
        }
        return psnType;
    }

    public static List<AppSvcDocDto> getSvcDocumentByParams(List<AppSvcDocDto> appSvcDocDtos, String configId, String premIndex,
            String psnIndex) {
        List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos) && !StringUtil.isEmpty(configId)) {
            appSvcDocDtoList = getAppSvcDocDtoByConfigId(appSvcDocDtos, configId, premIndex, psnIndex);
        }
        return appSvcDocDtoList;
    }

    public static HcsaSvcDocConfigDto getHcsaSvcDocConfigDtoById(List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos, String id) {
        HcsaSvcDocConfigDto result = null;
        if (!IaisCommonUtils.isEmpty(hcsaSvcDocConfigDtos) && !StringUtil.isEmpty(id)) {
            for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocConfigDtos) {
                if (id.equals(hcsaSvcDocConfigDto.getId())) {
                    result = hcsaSvcDocConfigDto;
                    break;
                }
            }
        }
        return result;
    }

    public static void assignPoDpoDto(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos,
            List<AppSvcPrincipalOfficersDto> principalOfficersDtos, List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos) {
        if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
            for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                    principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                    deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
        }
    }

    public static boolean isMultiPremService(List<HcsaServiceDto> hcsaServiceDtos) {
        boolean flag = true;
        if (!IaisCommonUtils.isEmpty(hcsaServiceDtos)) {
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                String svcCode = hcsaServiceDto.getSvcCode();
                if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(
                        svcCode) || AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(svcCode)) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public static List<HcsaServiceDto> sortHcsaServiceDto(List<HcsaServiceDto> hcsaServiceDtoList) {
        List<HcsaServiceDto> baseList = new ArrayList();
        List<HcsaServiceDto> specifiedList = new ArrayList();
        List<HcsaServiceDto> subList = new ArrayList();
        List<HcsaServiceDto> otherList = new ArrayList();
        //class
        for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
            switch (hcsaServiceDto.getSvcType()) {
                case ApplicationConsts.SERVICE_TYPE_BASE:
                    baseList.add(hcsaServiceDto);
                    break;
                case ApplicationConsts.SERVICE_TYPE_SUBSUMED:
                    subList.add(hcsaServiceDto);
                    break;
                case ApplicationConsts.SERVICE_TYPE_SPECIFIED:
                    specifiedList.add(hcsaServiceDto);
                    break;
                default:
                    otherList.add(hcsaServiceDto);
                    break;
            }
        }
        //Sort
        sortService(baseList);
        sortService(specifiedList);
        sortService(subList);
        sortService(otherList);
        hcsaServiceDtoList = IaisCommonUtils.genNewArrayList();
        hcsaServiceDtoList.addAll(baseList);
        hcsaServiceDtoList.addAll(specifiedList);
        hcsaServiceDtoList.addAll(subList);
        hcsaServiceDtoList.addAll(otherList);
        return hcsaServiceDtoList;
    }

    public static List<SelectOption> genDesignationOpList(boolean needOthers) {
        List<SelectOption> idTypeSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
        return idTypeSelectList;
    }

    //=============================================================================
    //private method
    //=============================================================================

    private static void sortService(List<HcsaServiceDto> list) {
        list.sort((h1, h2) -> h1.getSvcName().compareTo(h2.getSvcName()));
    }

    private static HcsaSvcSubtypeOrSubsumedDto getScopeConfigByRecursiveTarNameUpward(
            Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap, String targetChkName, String startId) {
        HcsaSvcSubtypeOrSubsumedDto targetDto = null;
        if (svcScopeAlignMap != null && !StringUtil.isEmpty(startId) && !StringUtil.isEmpty(targetChkName)) {
            HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = svcScopeAlignMap.get(startId);
            if (hcsaSvcSubtypeOrSubsumedDto != null) {
                if (targetChkName.equals(hcsaSvcSubtypeOrSubsumedDto.getName())) {
                    targetDto = hcsaSvcSubtypeOrSubsumedDto;
                } else if (!StringUtil.isEmpty(hcsaSvcSubtypeOrSubsumedDto.getParentId())) {
                    targetDto = getScopeConfigByRecursiveTarNameUpward(svcScopeAlignMap, targetChkName,
                            hcsaSvcSubtypeOrSubsumedDto.getParentId());
                }
            }
        }
        return targetDto;
    }

    private static List<AppGrpPrimaryDocDto> removeEmptyAlignPrimaryDoc(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos) {
        List<AppGrpPrimaryDocDto> newGrpPrimaryDocDtos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                String docPremName = appGrpPrimaryDocDto.getPremisessName();
                String docPremType = appGrpPrimaryDocDto.getPremisessType();
                //add prem doc
                if (!StringUtil.isEmpty(docPremName) && !StringUtil.isEmpty(docPremType)) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                        String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                        String premType = appGrpPremisesDto.getPremisesType();
                        if (docPremName.equals(premIndexNo) && docPremType.equals(premType)) {
                            newGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                        }
                    }
                } else if (StringUtil.isEmpty(docPremName) && StringUtil.isEmpty(docPremType)) {
                    //add comm doc
                    newGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                }
            }
            appGrpPrimaryDocDtos = newGrpPrimaryDocDtos;
        }
        return appGrpPrimaryDocDtos;
    }

    private static List<AppSvcLaboratoryDisciplinesDto> removeEmptyAlignSvcScope(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        List<AppSvcLaboratoryDisciplinesDto> laboratoryDisciplinesDtos = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        if (!IaisCommonUtils.isEmpty(laboratoryDisciplinesDtos)) {
            List<AppSvcLaboratoryDisciplinesDto> newLaboratoryDisciplinesDtos = IaisCommonUtils.genNewArrayList();
            for (AppSvcLaboratoryDisciplinesDto laboratoryDisciplinesDto : laboratoryDisciplinesDtos) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    if (laboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getPremisesIndexNo())) {
                        newLaboratoryDisciplinesDtos.add(laboratoryDisciplinesDto);
                        break;
                    }
                }
            }
            laboratoryDisciplinesDtos = newLaboratoryDisciplinesDtos;
        }
        return laboratoryDisciplinesDtos;
    }

    private static List<AppSvcDisciplineAllocationDto> removeEmptyAlignAllocation(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        List<AppSvcDisciplineAllocationDto> disciplineAllocationDtos = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        if (!IaisCommonUtils.isEmpty(disciplineAllocationDtos)) {
            List<AppSvcDisciplineAllocationDto> newDisciplineAllocations = IaisCommonUtils.genNewArrayList();
            for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : disciplineAllocationDtos) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    if (appSvcDisciplineAllocationDto.getPremiseVal().equals(appGrpPremisesDto.getPremisesIndexNo())) {
                        newDisciplineAllocations.add(appSvcDisciplineAllocationDto);
                        break;
                    }
                }
            }
            disciplineAllocationDtos = newDisciplineAllocations;
        }
        return disciplineAllocationDtos;
    }

    private static List<AppSvcDocDto> removeEmptyAlignSvcDoc(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
            List<AppSvcDocDto> newAppSvcDocDtos = IaisCommonUtils.genNewArrayList();
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                String docPremType = appSvcDocDto.getPremisesType();
                String docPremVal = appSvcDocDto.getPremisesVal();
                if (StringUtil.isEmpty(docPremType) && StringUtil.isEmpty(docPremVal)) {
                    newAppSvcDocDtos.add(appSvcDocDto);
                } else if (!StringUtil.isEmpty(docPremType) && !StringUtil.isEmpty(docPremVal)) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                        String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                        String premType = appGrpPremisesDto.getPremisesType();
                        if (docPremVal.equals(premIndexNo) && docPremType.equals(premType)) {
                            newAppSvcDocDtos.add(appSvcDocDto);
                        }
                    }
                }
            }
            appSvcDocDtos = newAppSvcDocDtos;
        }
        return appSvcDocDtos;
    }

    private static List<AppSvcBusinessDto> removeEmptyAlignBusiness(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        List<AppSvcBusinessDto> appSvcBusinessDtos = appSvcRelatedInfoDto.getAppSvcBusinessDtoList();
        List<AppSvcBusinessDto> newBusinessDtos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcBusinessDtos)) {
            for (AppSvcBusinessDto appSvcBusinessDto : appSvcBusinessDtos) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    if (Objects.equals(appGrpPremisesDto.getPremisesIndexNo(), appSvcBusinessDto.getPremIndexNo())) {
                        newBusinessDtos.add(appSvcBusinessDto);
                        break;
                    }
                }
            }
        }
        return newBusinessDtos;
    }

    private static List<SelectOption> getPremisesSel(String appType) {
        List<SelectOption> selectOptionList = IaisCommonUtils.genNewArrayList();
        SelectOption cps1 = new SelectOption("-1", HcsaAppConst.FIRESTOPTION);
        selectOptionList.add(cps1);
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            SelectOption cps2 = new SelectOption("newPremise", "Moving to a new address");
            selectOptionList.add(cps2);
        } else {
            SelectOption cps2 = new SelectOption("newPremise", "Add a new mode of service delivery");
            selectOptionList.add(cps2);
        }
        return selectOptionList;
    }

    private static boolean checkCanEdit(AppEditSelectDto appEditSelectDto, String currentType) {
        boolean pageCanEdit = false;
        if (appEditSelectDto != null) {
            if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isPremisesEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_INFORMATION.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isServiceEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isDocEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_DOCUMENT.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isServiceEdit() || appEditSelectDto.isDocEdit();
            } else if (ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_LICENSEE.equals(currentType)) {
                pageCanEdit = appEditSelectDto.isLicenseeEdit();
            }
        }
        return pageCanEdit;
    }

    private ApplicationHelper() {
        throw new IllegalStateException("Utility class");
    }


    private static void setPremAddressForSvcScope(List<AppGrpPremisesDto> appGrpPremisesDtos,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcLaboratoryDisciplinesDto> newAppSvcLaboratoryDisciplinesDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtos) {
                    String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                    String premval = appSvcLaboratoryDisciplinesDto.getPremiseVal();
                    if (!StringUtil.isEmpty(premIndexNo) && premIndexNo.equals(premval)) {
                        appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                        newAppSvcLaboratoryDisciplinesDtoList.add(appSvcLaboratoryDisciplinesDto);
                    }
                }
            }
            appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(newAppSvcLaboratoryDisciplinesDtoList);
        }
    }

    private static void setSvcScopeInfo(List<AppGrpPremisesDto> appGrpPremisesDtos, AppSvcRelatedInfoDto appSvcRelatedInfoDto,
            Map<String, HcsaSvcSubtypeOrSubsumedDto> map) {
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtos)) {
            setPremAddressForSvcScope(appGrpPremisesDtos, appSvcRelatedInfoDto);
            for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtos) {
                if (!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList())) {
                    for (AppSvcChckListDto appSvcChckListDto : appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList()) {
                        HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = map.get(appSvcChckListDto.getChkLstConfId());
                        if (hcsaSvcSubtypeOrSubsumedDto != null) {
                            appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                            appSvcChckListDto.setChkLstType(hcsaSvcSubtypeOrSubsumedDto.getType());
                            appSvcChckListDto.setChkCode(hcsaSvcSubtypeOrSubsumedDto.getCode());
                        }
                    }
                }
            }
        }
    }

    private static AppSvcPrincipalOfficersDto transferCgoToPsnDto(AppSvcPrincipalOfficersDto appSvcCgoDto) {
        AppSvcPrincipalOfficersDto psnDto = new AppSvcPrincipalOfficersDto();
        if (appSvcCgoDto == null) {
            return psnDto;
        }
        psnDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
        psnDto.setSalutation(appSvcCgoDto.getSalutation());
        psnDto.setName(appSvcCgoDto.getName());
        psnDto.setIdType(appSvcCgoDto.getIdType());
        psnDto.setIdNo(appSvcCgoDto.getIdNo());
        psnDto.setDesignation(appSvcCgoDto.getDesignation());
        psnDto.setOfficeTelNo(appSvcCgoDto.getOfficeTelNo());
        psnDto.setMobileNo(appSvcCgoDto.getMobileNo());
        psnDto.setOfficeTelNo(appSvcCgoDto.getOfficeTelNo());
        psnDto.setEmailAddr(appSvcCgoDto.getEmailAddr());

        psnDto.setProfessionType(appSvcCgoDto.getProfessionType());
        psnDto.setProfRegNo(appSvcCgoDto.getProfRegNo());
        psnDto.setSpeciality(appSvcCgoDto.getSpeciality());
        psnDto.setSpecialityOther(appSvcCgoDto.getSpecialityOther());
        psnDto.setSubSpeciality(appSvcCgoDto.getSubSpeciality());
        psnDto.setLicPerson(appSvcCgoDto.isLicPerson());
        return psnDto;
    }

    private static void syncPsnDto(List<AppSvcPrincipalOfficersDto> appSvcCgoDtos, Map<String, AppSvcPersonAndExtDto> personMap,
            String svcCode) {
        if (IaisCommonUtils.isEmpty(appSvcCgoDtos) || personMap == null || StringUtil.isEmpty(svcCode)) {
            return;
        }
        for (AppSvcPrincipalOfficersDto person : appSvcCgoDtos) {
            String personKey = getPersonKey(person.getNationality(), person.getIdType(), person.getIdNo());
            AppSvcPersonAndExtDto appSvcPersonAndExtDto = personMap.get(personKey);
            AppSvcPrincipalOfficersDto selPerson = genAppSvcPrincipalOfficersDto(appSvcPersonAndExtDto, svcCode, false);
            if (selPerson != null) {
                syncPsnDto(selPerson, person);
            }
        }
    }

    public static void syncPsnDto(AppSvcPrincipalOfficersDto source, AppSvcPrincipalOfficersDto person) {
        if (source == null || person == null) {
            return;
        }
        person.setAssignSelect(getPersonKey(source.getNationality(), source.getIdType(), source.getIdNo()));
        person.setSalutation(source.getSalutation());
        person.setName(source.getName());
        person.setIdType(source.getIdType());
        person.setIdNo(source.getIdNo());
        person.setNationality(source.getNationality());
        String mobileNo = source.getMobileNo();
        if (!StringUtil.isEmpty(mobileNo)) {
            person.setMobileNo(mobileNo);
        }
        String emailAddr = source.getEmailAddr();
        if (!StringUtil.isEmpty(mobileNo)) {
            person.setEmailAddr(emailAddr);
        }
        String designation = source.getDesignation();
        if (!StringUtil.isEmpty(designation)) {
            person.setDesignation(designation);
        }
        String otherDesignation = source.getOtherDesignation();
        if (!StringUtil.isEmpty(otherDesignation)) {
            person.setOtherDesignation(otherDesignation);
        }
        String professionType = source.getProfessionType();
        if (!StringUtil.isEmpty(professionType)) {
            person.setProfessionType(professionType);
        }
        String profRegNo = source.getProfRegNo();
        if (!StringUtil.isEmpty(profRegNo)) {
            person.setProfRegNo(profRegNo);
        }
        String speciality = source.getSpeciality();
        if (!StringUtil.isEmpty(speciality)) {
            person.setSpeciality(speciality);
        }
        String specialityOther = source.getSpecialityOther();
        if (!StringUtil.isEmpty(specialityOther)) {
            person.setSpecialityOther(specialityOther);
        }
        String subSpeciality = source.getSubSpeciality();
        if (!StringUtil.isEmpty(subSpeciality)) {
            person.setSubSpeciality(subSpeciality);
        }
        String qualification = source.getQualification();
        if (!StringUtil.isEmpty(qualification)) {
            person.setQualification(qualification);
        }
        String otherQualification = source.getOtherQualification();
        if (!StringUtil.isEmpty(otherQualification)) {
            person.setOtherQualification(otherQualification);
        }
        String officeTelNo = source.getOfficeTelNo();
        if (!StringUtil.isEmpty(officeTelNo)) {
            person.setOfficeTelNo(officeTelNo);
        }
        person.setNeedSpcOptList(source.isNeedSpcOptList());
        List<SelectOption> spcOptList = source.getSpcOptList();
        if (!IaisCommonUtils.isEmpty(spcOptList)) {
            person.setSpcOptList(source.getSpcOptList());
        }
        String specHtml = source.getSpecialityHtml();
        if (!StringUtil.isEmpty(specHtml)) {
            person.setSpecialityHtml(specHtml);
        }
        String professionBoard = source.getProfessionBoard();
        if (!StringUtil.isEmpty(professionBoard)) {
            person.setProfessionBoard(professionBoard);
        }
        Date specialtyGetDate = source.getSpecialtyGetDate();
        if (specialtyGetDate != null) {
            person.setSpecialtyGetDate(specialtyGetDate);
        }
        String specialtyGetDateStr = source.getSpecialtyGetDateStr();
        if (!StringUtil.isEmpty(specialtyGetDateStr)) {
            person.setSpecialtyGetDateStr(specialtyGetDateStr);
        }
        String typeOfCurrRegi = source.getTypeOfCurrRegi();
        if (!StringUtil.isEmpty(typeOfCurrRegi)) {
            person.setTypeOfCurrRegi(typeOfCurrRegi);
        }
        Date currRegiDate = source.getCurrRegiDate();
        if (currRegiDate != null) {
            person.setCurrRegiDate(currRegiDate);
        }
        String currRegiDateStr = source.getCurrRegiDateStr();
        if (!StringUtil.isEmpty(currRegiDateStr)) {
            person.setCurrRegiDateStr(currRegiDateStr);
        }
        Date praCerEndDate = source.getPraCerEndDate();
        if (praCerEndDate != null) {
            person.setPraCerEndDate(praCerEndDate);
        }
        String praCerEndDateStr = source.getPraCerEndDateStr();
        if (!StringUtil.isEmpty(praCerEndDateStr)) {
            person.setPraCerEndDateStr(praCerEndDateStr);
        }
        String typeOfRegister = source.getTypeOfRegister();
        if (!StringUtil.isEmpty(typeOfRegister)) {
            person.setTypeOfRegister(typeOfRegister);
        }
        String relevantExperience = source.getRelevantExperience();
        if (!StringUtil.isEmpty(relevantExperience)) {
            person.setRelevantExperience(relevantExperience);
        }
        String holdCerByEMS = source.getHoldCerByEMS();
        if (!StringUtil.isEmpty(holdCerByEMS)) {
            person.setHoldCerByEMS(holdCerByEMS);
        }
        Date aclsExpiryDate = source.getAclsExpiryDate();
        if (aclsExpiryDate != null) {
            person.setAclsExpiryDate(aclsExpiryDate);
        }
        String aclsExpiryDateStr = source.getAclsExpiryDateStr();
        if (!StringUtil.isEmpty(aclsExpiryDateStr)) {
            person.setAclsExpiryDateStr(aclsExpiryDateStr);
        }
    }

    private static String getTextByValue(List<SelectOption> selectOptions, String value) {
        String text = "";
        if (!IaisCommonUtils.isEmpty(selectOptions) && !StringUtil.isEmpty(value)) {
            for (SelectOption sp : selectOptions) {
                if (value.equals(sp.getValue())) {
                    text = sp.getText();
                    break;
                }
            }
        }
        return text;
    }

    private static AppSvcChckListDto getSvcChckListDtoByConfigId(String configId, List<AppSvcChckListDto> appSvcChckListDtos) {
        AppSvcChckListDto result = null;
        if (!StringUtil.isEmpty(configId) && !IaisCommonUtils.isEmpty(appSvcChckListDtos)) {
            for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                if (configId.equals(appSvcChckListDto.getChkLstConfId())) {
                    result = appSvcChckListDto;
                    break;
                }
            }
        }
        return result;
    }


    private static String[] removeArrIndex(String[] arrs, int index) {
        if (arrs == null) {
            return new String[]{""};
        }
        String[] newArrs = new String[arrs.length - 1];
        int j = 0;
        for (int i = 0; i < arrs.length; i++) {
            if (i != index) {
                newArrs[j] = arrs[i];
                j++;
            }
        }
        return newArrs;
    }

    private static AppSvcPersonExtDto getPsnExtDtoBySvcCode(List<AppSvcPersonExtDto> appSvcPersonExtDtos, String svcCode) {
        AppSvcPersonExtDto appSvcPersonExtDto = null;
        if (!IaisCommonUtils.isEmpty(appSvcPersonExtDtos) && !StringUtil.isEmpty(svcCode)) {
            for (AppSvcPersonExtDto extPsn : appSvcPersonExtDtos) {
                String serviceCode = extPsn.getServiceCode();
                String serviceName = extPsn.getServiceName();
                if (!StringUtil.isEmpty(serviceCode)) {
                    if (svcCode.equals(serviceCode)) {
                        appSvcPersonExtDto = extPsn;
                        break;
                    }
                } else if (!StringUtil.isEmpty(serviceName)) {
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(svcCode);
                    boolean flag = hcsaServiceDto != null && serviceName.equals(hcsaServiceDto.getSvcName());
                    if (flag) {
                        appSvcPersonExtDto = extPsn;
                        break;
                    }
                }
            }
        }
        return appSvcPersonExtDto;
    }

    private static void setReloadTime(OperationHoursReloadDto operationHoursReloadDto) {
        List<String> selectValList = operationHoursReloadDto.getSelectValList();
        if (!IaisCommonUtils.isEmpty(selectValList)) {
            String[] selectArr = (String[]) selectValList.toArray(new String[selectValList.size()]);
            String phSelect = ParamUtil.StringsToString(selectArr);
            operationHoursReloadDto.setSelectVal(phSelect);
        }
        Time startTime = operationHoursReloadDto.getStartFrom();
        Time endTime = operationHoursReloadDto.getEndTo();
        if (startTime != null) {
            LocalTime localTimeStart = startTime.toLocalTime();
            operationHoursReloadDto.setStartFromHH(String.valueOf(localTimeStart.getHour()));
            operationHoursReloadDto.setStartFromMM(String.valueOf(localTimeStart.getMinute()));
        }
        if (endTime != null) {
            LocalTime localTimeStart = endTime.toLocalTime();
            operationHoursReloadDto.setEndToHH(String.valueOf(localTimeStart.getHour()));
            operationHoursReloadDto.setEndToMM(String.valueOf(localTimeStart.getMinute()));
        }
    }


    private static AppSvcDocDto getSvcDtoByConfigIdAndPsnIndexNo(List<AppSvcDocDto> appSvcDocDtos, String configId, String premIndexNo,
            String premType, String psnIndexNo) {
        AppSvcDocDto appSvcDocDto = null;
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
            for (AppSvcDocDto appSvcDocDto1 : appSvcDocDtos) {
                String currConfigId = appSvcDocDto1.getSvcDocId();
                String cuurPremIndex = appSvcDocDto1.getPremisesVal();
                if (StringUtil.isEmpty(cuurPremIndex)) {
                    cuurPremIndex = "";
                }
                String currPsnIndex = appSvcDocDto1.getPsnIndexNo();
                if (StringUtil.isEmpty(currPsnIndex)) {
                    currPsnIndex = "";
                }
                String currPremType = appSvcDocDto1.getPremisesType();
                if (StringUtil.isEmpty(currPremType)) {
                    currPremType = "";
                }
                if (currConfigId.equals(configId)
                        && cuurPremIndex.equals(premIndexNo)
                        && currPsnIndex.equals(psnIndexNo)
                        && currPremType.equals(premType)) {
                    appSvcDocDto = appSvcDocDto1;
                    break;
                }
            }
        }

        return appSvcDocDto;
    }

    private static List<AppGrpPrimaryDocDto> getAppGrpPrimaryDocDtoByConfigId(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,
            String configId, String premIndex) {
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos) && !StringUtil.isEmpty(configId)) {
            if (StringUtil.isEmpty(premIndex)) {
                premIndex = "";
            }
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                String currPremIndex = appGrpPrimaryDocDto.getPremisessName();
                if (StringUtil.isEmpty(currPremIndex)) {
                    currPremIndex = "";
                }
                if (!StringUtil.isEmpty(appGrpPrimaryDocDto.getFileRepoId()) && configId.equals(
                        appGrpPrimaryDocDto.getSvcComDocId()) && premIndex.equals(currPremIndex)) {
                    appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto);
                }
            }
        }
        return appGrpPrimaryDocDtoList;
    }

    private static void setPrimaryDocDisplayTitle(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, String displayTitle) {
        if (!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos) && !StringUtil.isEmpty(displayTitle)) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                appGrpPrimaryDocDto.setDisplayTitle(displayTitle);
            }
        }
    }

    private static List<AppSvcDocDto> getAppSvcDocDtoByConfigId(List<AppSvcDocDto> appSvcDocDtos, String configId, String premIndex,
            String psnIndex) {
        List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos) && !StringUtil.isEmpty(configId)) {
            if (StringUtil.isEmpty(premIndex)) {
                premIndex = "";
            }
            if (StringUtil.isEmpty(psnIndex)) {
                psnIndex = "";
            }
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                String currPremIndex = appSvcDocDto.getPremisesVal();
                if (StringUtil.isEmpty(currPremIndex)) {
                    currPremIndex = "";
                }
                String currPsnIndex = appSvcDocDto.getPsnIndexNo();
                if (StringUtil.isEmpty(currPsnIndex)) {
                    currPsnIndex = "";
                }
                if (configId.equals(appSvcDocDto.getSvcDocId()) && premIndex.equals(currPremIndex) && psnIndex.equals(currPsnIndex)) {
                    appSvcDocDtoList.add(appSvcDocDto);
                }
            }
        }
        return appSvcDocDtoList;
    }

    private static List<AppSvcDocDto> getAppSvcDocDtoByConfigId(List<AppSvcDocDto> appSvcDocDtos, String configId) {
        List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos) && !StringUtil.isEmpty(configId)) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                if (configId.equals(appSvcDocDto.getSvcDocId())) {
                    appSvcDocDtoList.add(appSvcDocDto);
                }
            }
        }
        return appSvcDocDtoList;
    }

    private static void setSvcDocDisplayTitle(List<AppSvcDocDto> appSvcDocDtos, String displayTitle) {
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos) && !StringUtil.isEmpty(displayTitle)) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                appSvcDocDto.setDisplayTitle(displayTitle);
            }
        }
    }

    public static String getDocDisplayTitle(HcsaSvcDocConfigDto entity, Integer num) {
        if (entity == null) {
            return null;
        }
        String dupForPrem = entity.getDupForPrem();
        String dupForPerson = entity.getDupForPerson();
        String docTitle = entity.getDocTitle();
        return getDocDisplayTitle(dupForPrem, dupForPerson, docTitle, num);
    }

    public static String getDocDisplayTitle(String dupForPrem, String dupForPerson, String docTitle, Integer num) {
        log.info(StringUtil.changeForLog("The dupForPrem -->: " + dupForPrem));
        log.info(StringUtil.changeForLog("The dupForPerson -->: " + dupForPerson));
        log.info(StringUtil.changeForLog("The docTitle -->: " + docTitle));
        String result = null;
        if (dupForPerson == null && "0".equals(dupForPrem)) {
            result = docTitle;
        } else if (dupForPerson == null && "1".equals(dupForPrem)) {
            result = ApplicationConsts.TITLE_MODE_OF_SVCDLVY + "  1: " + docTitle;
        } else if (dupForPerson != null) {
            StringBuilder title = new StringBuilder();
            if ("1".equals(dupForPrem)) {
                title.append(ApplicationConsts.TITLE_MODE_OF_SVCDLVY).append(" 1: ");
            }
            title.append(getDupForPersonName(dupForPerson));
            if (num != null) {
                title.append(' ').append(num);
            }
            title.append(": ").append(docTitle);
            result = title.toString();
        }
        log.info(StringUtil.changeForLog("The Result -->: " + result));
        return result;
    }

    private static String getDupForPersonName(String dupForPerson) {
        String psnName = "";
        switch (dupForPerson) {
            case ApplicationConsts.DUP_FOR_PERSON_CGO:
                psnName = HcsaConsts.CLINICAL_GOVERNANCE_OFFICER;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_PO:
                psnName = HcsaConsts.CLINICAL_GOVERNANCE_OFFICER;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_DPO:
                psnName = HcsaConsts.NOMINEE;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_MAP:
                psnName = HcsaConsts.MEDALERT_PERSON;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_SVCPSN:
                psnName = HcsaConsts.SERVICE_PERSONNEL;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_CD:
                psnName = HcsaConsts.CLINICAL_DIRECTOR;
                break;
            case ApplicationConsts.DUP_FOR_PERSON_SL:
                psnName = HcsaConsts.SECTION_LEADER;
                break;
            default:
                break;
        }
        return psnName;
    }

    private static void setSvcDocDisplayTitle(String dupForPrem, int premCount, String premIndex, String dupForPerson,
            String configId, String configTitle, List<AppSvcDocDto> appSvcDocDtos,
            AppSvcRelatedInfoDto appSvcRelatedInfoDto, Map<String, List<AppSvcDocDto>> reloadMap) {
        String reloadKey;
        if ("1".equals(dupForPrem)) {
            reloadKey = premIndex + configId;
        } else {
            reloadKey = configId;
        }
        if (StringUtil.isEmpty(dupForPerson)) {
            List<AppSvcDocDto> appSvcDocDtoList = getAppSvcDocDtoByConfigId(appSvcDocDtos, configId, premIndex, "");
            String displayTitle = getDocDisplayTitle(dupForPrem, dupForPerson, configTitle, premCount);
            setSvcDocDisplayTitle(appSvcDocDtoList, displayTitle);
            reloadMap.put(reloadKey, appSvcDocDtoList);
        } else {
            List<AppSvcPrincipalOfficersDto> psnList = getPsnByDupForPerson(appSvcRelatedInfoDto, dupForPerson);
            int psnCount = 1;
            for (AppSvcPrincipalOfficersDto psn : psnList) {
                String psnIndex = psn.getIndexNo();
                List<AppSvcDocDto> appSvcDocDtoList = getAppSvcDocDtoByConfigId(appSvcDocDtos, configId, premIndex, psnIndex);
                String displayTitle = getDocDisplayTitle(dupForPrem, dupForPerson, configTitle, psnCount++);
                setSvcDocDisplayTitle(appSvcDocDtoList, displayTitle);
                reloadMap.put(reloadKey + psnIndex, appSvcDocDtoList);
            }
        }

    }

    public static List<SelectOption> getReasonOption() {
//        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
//        SelectOption so1 = new SelectOption(ApplicationConsts.CESSATION_REASON_NOT_PROFITABLE, "Not Profitable");
//        SelectOption so2 = new SelectOption(ApplicationConsts.CESSATION_REASON_REDUCE_WORKLOA, "Retiring");
//        SelectOption so3 = new SelectOption(ApplicationConsts.CESSATION_REASON_OTHER, "Others");
//        riskLevelResult.add(so1);
//        riskLevelResult.add(so2);
//        riskLevelResult.add(so3);
//        return riskLevelResult;
        return MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_CESSION_REASION);
    }

    public static List<SelectOption> getPatientsOption() {
        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI, "HCI");
        SelectOption so2 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO, "Professional Regn. No.");
        SelectOption so3 = new SelectOption(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER, "Others");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        riskLevelResult.add(so3);
        return riskLevelResult;
    }

    public static boolean canLicenseeEdit(AppSubmissionDto appSubmissionDto, boolean isRFI) {
        if (appSubmissionDto == null || appSubmissionDto.getSubLicenseeDto() == null) {
            return false;
        }
        String licenseeType = appSubmissionDto.getSubLicenseeDto().getLicenseeType();
        String appType = appSubmissionDto.getAppType();
        boolean oldLicenseeEdit = Optional.ofNullable(appSubmissionDto.getAppEditSelectDto())
                .map(AppEditSelectDto::isLicenseeEdit)
                .orElseGet(() -> Boolean.TRUE);
        return canLicenseeEdit(licenseeType, appType, oldLicenseeEdit, isRFI);
    }

    public static boolean canLicenseeEdit(String licenseeType, String appType, boolean oldLicenseeEdit, boolean isRFI) {
        if (OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(licenseeType)) {
            return false;
        }
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
            return !isRFI || oldLicenseeEdit;
        }
        if (OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(licenseeType)) {
            return oldLicenseeEdit;
        }
        return false;
    }

    public static List<SelectOption> genSubLicessOption(Map<String, SubLicenseeDto> licenseeMap) {
        List<SelectOption> options = IaisCommonUtils.genNewArrayList();
        options.add(new SelectOption("-1", "Please Select"));
        options.add(new SelectOption(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW, "I'd like to add a new licensee"));
        if (licenseeMap != null) {
            licenseeMap.forEach((personKey, dto) ->
                    options.add(new SelectOption(personKey, getPersonView(dto.getIdType(), dto.getIdNumber(), dto.getLicenseeName())))
            );
        }
        return options;
    }

    public static Map<String, SubLicenseeDto> genSubLicessMap(List<SubLicenseeDto> subLicenseeDtoList) {
        Map<String, SubLicenseeDto> map = IaisCommonUtils.genNewLinkedHashMap();
        if (subLicenseeDtoList != null) {
            subLicenseeDtoList.stream().forEach(
                    dto -> map.put(getPersonKey(dto.getNationality(), dto.getIdType(), dto.getIdNumber()), dto));
        }
        return map;
    }

    public static boolean isEmpty(String assignSel) {
        return StringUtil.isEmpty(assignSel) || "-1".equals(assignSel);
    }

    public static String getAssignSelect(Set<String> keySet, String nationality, String idType, String idNumber) {
        String assignSelect = "-1";
        String personKey = getPersonKey(nationality, idType, idNumber);
        if (keySet != null && keySet.contains(personKey)) {
            assignSelect = personKey;
        } else if (!StringUtil.isEmpty(personKey)) {
            assignSelect = IaisEGPConstant.ASSIGN_SELECT_ADD_NEW;
        }
        return assignSelect;
    }

    public static String getAssignSelect(String nationality, String idType, String idNumber, String defaultVal) {
        String personKey = getPersonKey(nationality, idType, idNumber);
        if (StringUtil.isEmpty(personKey)) {
            personKey = defaultVal;
        }
        return personKey;
    }

    public static List<String> getRelatedAppId(String appId, String licenceId, String svcName) {
        List<String> appIds = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isNotEmpty(licenceId)) {
            LicCommClient licenceClient = SpringContextHelper.getContext().getBean(LicCommClient.class);
            List<LicAppCorrelationDto> licAppCorrDtos = licenceClient.getAllRelatedLicAppCorrs(licenceId, svcName).getEntity();
            if (licAppCorrDtos != null && !licAppCorrDtos.isEmpty()) {
                licAppCorrDtos.forEach(dto -> appIds.add(dto.getApplicationId()));
            }
        }
        if (StringUtil.isNotEmpty(appId)) {
            appIds.add(appId);
        }
        return appIds;
    }

    public static boolean isIn(String chkLstConfId, String premiseIndex, List<AppSvcLaboratoryDisciplinesDto> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        for (AppSvcLaboratoryDisciplinesDto dto : list) {
            if (Objects.equals(premiseIndex, dto.getPremiseVal()) && isIn(chkLstConfId, dto.getAppSvcChckListDtoList())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIn(String chkLstConfId, List<AppSvcChckListDto> appSvcChckListDtoList) {
        if (StringUtil.isEmpty(chkLstConfId)) {
            return true;
        }
        if (appSvcChckListDtoList == null || appSvcChckListDtoList.isEmpty()) {
            return false;
        }
        for (AppSvcChckListDto dto : appSvcChckListDtoList) {
            if (chkLstConfId.equals(dto.getChkLstConfId())) {
                return true;
            }
        }
        return false;
    }

    public static List<AppGrpPremisesDto> updatePremisesIndex(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        if (appGrpPremisesDtoList == null || appGrpPremisesDtoList.isEmpty() || oldAppGrpPremisesDtoList == null || oldAppGrpPremisesDtoList.isEmpty()) {
            return appGrpPremisesDtoList;
        }
        String premisesIndexNo = oldAppGrpPremisesDtoList.get(0).getPremisesIndexNo();
        return updatePremisesIndex(appGrpPremisesDtoList, premisesIndexNo);
    }

    public static List<AppGrpPremisesDto> updatePremisesIndex(List<AppGrpPremisesDto> appGrpPremisesDtoList, String premisesIndexNo) {
        if (appGrpPremisesDtoList == null || appGrpPremisesDtoList.isEmpty()) {
            return appGrpPremisesDtoList;
        }
        for (AppGrpPremisesDto dto : appGrpPremisesDtoList) {
            dto.setPremisesIndexNo(premisesIndexNo);
        }
        return appGrpPremisesDtoList;
    }

    /**
     * Handle floor no
     *
     * @param floorNo
     * @param floorNoErr
     * @return
     */
    public static String handleFloorNo(String floorNo, String floorNoErr) {
        if (StringUtil.isEmpty(floorNoErr)) {
            return handleFloorNo(floorNo);
        }
        return floorNo;
    }

    /**
     * Handle floor no
     *
     * @param floorNo
     * @return
     */
    public static String handleFloorNo(String floorNo) {
        return MiscUtil.getFloorNo(floorNo);
    }

    public static Map<String, AppGrpPremisesDto> checkPremisesMap(HttpServletRequest request) {
        return checkPremisesMap(true, request);
    }

    public static Map<String, AppGrpPremisesDto> checkPremisesMap(boolean withCurrent, HttpServletRequest request) {
        LicCommService licCommService = SpringHelper.getBean(LicCommService.class);
        String licenseeId = getLicenseeId(request);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppGrpPremisesDto> licenceList = IaisCommonUtils.genNewArrayList();
        boolean handleCurrent = appSubmissionDto != null && appSubmissionDto.getAppGrpPremisesDtoList() != null;
        Map<String, AppGrpPremisesDto> licAppGrpPremisesDtoMap = (Map<String, AppGrpPremisesDto>) request.getSession()
                .getAttribute(HcsaAppConst.LIC_PREMISES_MAP);
        if (licAppGrpPremisesDtoMap == null) {
            List<AppGrpPremisesDto> licencePremisesDtoList =
                    IaisCommonUtils.getList(licCommService.getLicencePremisesDtoList(licenseeId));
            // check licence data with current
            if (handleCurrent) {
                licenceList = IaisCommonUtils.getList(licencePremisesDtoList.stream()
                        .filter(premisesDto -> appSubmissionDto.getAppGrpPremisesDtoList().stream()
                                .anyMatch(dto -> Objects.equals(premisesDto.getPremisesIndexNo(), dto.getPremisesIndexNo())
                                        || Objects.equals(premisesDto.getPremisesSelect(), dto.getPremisesSelect())))
                        .collect(Collectors.toList()));
                licAppGrpPremisesDtoMap = IaisCommonUtils.getMap(licencePremisesDtoList.stream()
                        .collect(Collectors.toMap(AppGrpPremisesDto::getPremisesSelect, Function.identity(), (v1, v2) -> {
                            v1.setRelatedServices(IaisCommonUtils.combineList(v1.getRelatedServices(), v2.getRelatedServices()));
                            return v1;
                        })));
                for (AppGrpPremisesDto dto : licenceList) {
                    licAppGrpPremisesDtoMap.remove(dto.getPremisesSelect());
                }
            }
        }
        Map<String, AppGrpPremisesDto> newAppMap = IaisCommonUtils.genNewHashMap();
        Map<String, AppGrpPremisesDto> appPremisesMap = (Map<String, AppGrpPremisesDto>) request.getSession()
                .getAttribute(HcsaAppConst.APP_PREMISES_MAP);
        if (appPremisesMap == null) {
            AppCommService appCommService = SpringHelper.getBean(AppCommService.class);
            List<AppGrpPremisesDto> activePendingPremiseList = IaisCommonUtils.getList(
                    appCommService.getActivePendingPremiseList(licenseeId));
            List<AppGrpPremisesDto> appList = IaisCommonUtils.getList(activePendingPremiseList.stream()
                    .filter(premisesDto -> appSubmissionDto.getAppGrpPremisesDtoList().stream()
                            .anyMatch(dto -> Objects.equals(premisesDto.getPremisesIndexNo(), dto.getPremisesIndexNo())
                                    || Objects.equals(premisesDto.getPremisesSelect(), dto.getPremisesSelect())))
                    .collect(Collectors.toList()));
            appPremisesMap = IaisCommonUtils.getMap(activePendingPremiseList.stream()
                    .collect(Collectors.toMap(AppGrpPremisesDto::getPremisesSelect, Function.identity(), (v1, v2) -> {
                        v1.setRelatedServices(IaisCommonUtils.combineList(v1.getRelatedServices(), v2.getRelatedServices()));
                        return v1;
                    })));
            for (AppGrpPremisesDto dto : appList) {
                appPremisesMap.remove(dto.getPremisesSelect());
            }
            if (handleCurrent && (!licenceList.isEmpty() || !appList.isEmpty())) {
                boolean isRfi = checkIsRfi(request);
                int check = isRfi ? 3 : 2;
                for (AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()) {
                    String key = initCurrentPremises(licAppGrpPremisesDtoMap, newAppMap, licenceList, appList,
                            appGrpPremisesDto, check);
                    boolean isSamePremiseKey = key == null || Objects.equals(key, appGrpPremisesDto.getPremisesSelect());
                    log.info(StringUtil.changeForLog("isSamePremiseKey: " + isSamePremiseKey));
                    if (!isSamePremiseKey) {
                        initCurrentPremises(licAppGrpPremisesDtoMap, newAppMap, licenceList, appList,
                                appGrpPremisesDto, 1);
                    }
                }
            }
        } else {
            newAppMap = appPremisesMap;
        }
        request.getSession().setAttribute(HcsaAppConst.APP_PREMISES_MAP, newAppMap);
        request.getSession().setAttribute(HcsaAppConst.LIC_PREMISES_MAP, licAppGrpPremisesDtoMap);
        Map<String, AppGrpPremisesDto> allData = IaisCommonUtils.genNewHashMap();
        allData.putAll(licAppGrpPremisesDtoMap);
        allData.putAll(newAppMap);
        if (withCurrent) {
            reSetCurrentPremises(allData, request);
        }
        return allData;
    }

    /**
     * @param licAppGrpPremisesDtoMap
     * @param newAppMap
     * @param licenceList
     * @param appList
     * @param srcDto
     * @param check                   1: PremisesSelect; 2: premiseIndexNo; 3:RFI
     * @return
     */
    private static String initCurrentPremises(Map<String, AppGrpPremisesDto> licAppGrpPremisesDtoMap,
            Map<String, AppGrpPremisesDto> newAppMap,
            List<AppGrpPremisesDto> licenceList, List<AppGrpPremisesDto> appList,
            AppGrpPremisesDto srcDto, int check) {
        if (check <= 0) {
            return null;
        }
        List<AppGrpPremisesDto> entryList = IaisCommonUtils.genNewArrayList();
        entryList.addAll(licenceList);
        entryList.addAll(appList);
        if (entryList.isEmpty()) {
            return null;
        }
        AppGrpPremisesDto premiseEntry = null;
        boolean isLicence = false;
        if (check == 3) {
            premiseEntry = (AppGrpPremisesDto) CopyUtil.copyMutableObject(srcDto);
            if (StringUtil.isEmpty(premiseEntry.getExistingData())) {
                premiseEntry.setExistingData(AppConsts.NO);
            } else if (AppConsts.YES.equals(premiseEntry.getExistingData())) {
                premiseEntry.setFromDB(true);
            }
        } else {
            if (!licenceList.isEmpty()) {
                premiseEntry = licenceList.stream()
                        .filter(dto -> checkPremises(srcDto, dto, check))
                        .findAny()
                        .orElse(null);
            }
            if (premiseEntry != null) {
                isLicence = true;
            } else if (!appList.isEmpty()) {
                isLicence = false;
                premiseEntry = appList.stream()
                        .filter(dto -> checkPremises(srcDto, dto, check))
                        .findAny()
                        .orElse(null);
            }
        }
        if (premiseEntry != null) {
            String key = premiseEntry.getPremisesSelect();
            AppGrpPremisesDto tarDto = premiseEntry;
            List<String> relatedServices = entryList.stream()
                    .filter(entry -> checkPremises(entry, tarDto, check))
                    .map(AppGrpPremisesDto::getRelatedServices)
                    .filter(Objects::nonNull)
                    .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
            AppGrpPremisesDto appGrpPremisesDto = newAppMap.remove(key);
            if (appGrpPremisesDto != null) {
                relatedServices.addAll(appGrpPremisesDto.getRelatedServices());
            }
            if (isLicence) {
                appGrpPremisesDto = licAppGrpPremisesDtoMap.get(key);
                if (appGrpPremisesDto != null) {
                    relatedServices.addAll(appGrpPremisesDto.getRelatedServices());
                }
                premiseEntry.setRelatedServices(relatedServices);
                licAppGrpPremisesDtoMap.put(key, premiseEntry);
            } else {
                appGrpPremisesDto = licAppGrpPremisesDtoMap.remove(key);
                if (appGrpPremisesDto != null) {
                    relatedServices.addAll(appGrpPremisesDto.getRelatedServices());
                }
                premiseEntry.setRelatedServices(relatedServices);
                newAppMap.put(key, premiseEntry);
            }
            return key;
        }
        return null;
    }

    /**
     * @param srcDto
     * @param tarDto
     * @param check  1: PremisesSelect; 2: premiseIndexNo; 3:RFI
     * @return
     */
    private static boolean checkPremises(AppGrpPremisesDto srcDto, AppGrpPremisesDto tarDto, int check) {
        if (srcDto == null || tarDto == null || check <= 0) {
            return false;
        }
        if (1 == check) {
            return Objects.equals(srcDto.getPremisesSelect(), tarDto.getPremisesSelect());
        } else if (2 == check) {
            return Objects.equals(srcDto.getPremisesIndexNo(), tarDto.getPremisesIndexNo());
        } else if (3 == check) {
            return Objects.equals(srcDto.getPremisesSelect(), tarDto.getPremisesSelect())
                    || Objects.equals(srcDto.getPremisesIndexNo(), tarDto.getPremisesIndexNo());
        } else {
            return false;
        }
    }

    private static void reSetCurrentPremises(Map<String, AppGrpPremisesDto> allData, HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        if (appSubmissionDto == null || appSubmissionDto.getAppGrpPremisesDtoList() == null) {
            return;
        }
        if (!appSubmissionDto.getAppGrpPremisesDtoList().stream().allMatch(AppGrpPremisesDto::isFilled)) {
            return;
        }
        String errorMsg = (String) request.getAttribute(IaisEGPConstant.ERRORMSG);
        if (StringUtil.isNotEmpty(errorMsg) && !"[]".equals(errorMsg)) {
            log.info(StringUtil.changeForLog("------ Has Error ------"));
            return;
        }
        if (!StringUtil.isEmpty(appSubmissionDto.getDraftNo())) {
            Map<String, String> errorMap = AppValidatorHelper.doValidatePremiss(appSubmissionDto, null, null, false, false);
            if (errorMap != null && !errorMap.isEmpty()) {
                log.info(StringUtil.changeForLog("------ Has Error ------"));
                return;
            }
        }
        Map<String, AppGrpPremisesDto> licAppGrpPremisesDtoMap = (Map<String, AppGrpPremisesDto>) request.getSession()
                .getAttribute(HcsaAppConst.LIC_PREMISES_MAP);
        Map<String, AppGrpPremisesDto> appPremisesMap = (Map<String, AppGrpPremisesDto>) request.getSession()
                .getAttribute(HcsaAppConst.APP_PREMISES_MAP);
        for (AppGrpPremisesDto premises : appSubmissionDto.getAppGrpPremisesDtoList()) {
            String premisesSelect = getPremisesKey(premises);
            AppGrpPremisesDto newDto = (AppGrpPremisesDto) CopyUtil.copyMutableObject(premises);
            newDto.setRelatedServices(null);// new premise
            newDto.setFromDB(false);
            // itself
            List<Map.Entry<String, AppGrpPremisesDto>> entryList = getPremisesFromMap(premises, allData);
            log.info(StringUtil.changeForLog("The same premise index no size in Map: " + (entryList.size())));
            if (entryList.isEmpty()) {// not have
                if (licAppGrpPremisesDtoMap.get(premisesSelect) == null && appPremisesMap.get(premisesSelect) == null) {
                    appPremisesMap.put(premisesSelect, newDto);
                    request.getSession().setAttribute(HcsaAppConst.APP_PREMISES_MAP, appPremisesMap);
                    allData.put(premisesSelect, newDto);
                    premises.setPremisesSelect(premisesSelect);
                    premises.setExistingData(AppConsts.NO);
                } else {
                    premises.setExistingData(AppConsts.YES);
                }
            } else {// have
                Map.Entry<String, AppGrpPremisesDto> entry = entryList.get(0);
                String oldPremSel = entry.getKey();
                if (Objects.equals(oldPremSel, premises.getPremisesSelect())
                        || ApplicationConsts.NEW_PREMISES.equals(premises.getPremisesSelect())) {// check itself or add new
                    newDto.setRelatedServices(entry.getValue().getRelatedServices());
                    allData.remove(oldPremSel);
                    allData.put(premisesSelect, newDto);
                    if (licAppGrpPremisesDtoMap.get(oldPremSel) != null) {
                        licAppGrpPremisesDtoMap.remove(oldPremSel);
                        licAppGrpPremisesDtoMap.put(premisesSelect, newDto);
                        request.getSession().setAttribute(HcsaAppConst.LIC_PREMISES_MAP, licAppGrpPremisesDtoMap);
                    } else {
                        appPremisesMap.remove(oldPremSel);
                        appPremisesMap.put(premisesSelect, newDto);
                        request.getSession().setAttribute(HcsaAppConst.APP_PREMISES_MAP, appPremisesMap);
                    }
                    premises.setPremisesSelect(premisesSelect);
                    premises.setExistingData(AppConsts.NO);
                } else {
                    premises.setExistingData(AppConsts.YES);
                }
                /*
                // retain the original one
                boolean needReset = false;
                Map.Entry<String, AppGrpPremisesDto> entry = entryList.stream()
                        .filter(e -> !e.getValue().isFromDB())
                        .findAny()
                        .orElse(null);// not from DB
                String oldPremSel = entry != null ? entry.getKey() : "-1";
                if (entry != null) {// has added updated or NEW_PREMISES
                    allData.remove(oldPremSel);
                    if (licAppGrpPremisesDtoMap.get(oldPremSel) != null) {
                        licAppGrpPremisesDtoMap.remove(oldPremSel);
                        request.getSession().setAttribute(NewApplicationDelegator.LIC_PREMISES_MAP, licAppGrpPremisesDtoMap);
                    } else {
                        appPremisesMap.remove(oldPremSel);
                    }
                    needReset = true;
                } else if (ApplicationConsts.NEW_PREMISES.equals(premises.getPremisesSelect())) {
                    needReset = true;
                } else {
                    entry = entryList.stream()
                            .filter(e -> e.getValue().isFromDB())
                            .findAny()
                            .orElse(null);// not from DB
                    oldPremSel = entry != null ? entry.getKey() : "-1";
                    if (Objects.equals(oldPremSel, premises.getPremisesSelect())
                            && !Objects.equals(oldPremSel, premisesSelect)) {
                        needReset = true;
                    }
                }
                if (needReset) {
                    allData.put(premisesSelect, newDto);
                    appPremisesMap.put(premisesSelect, newDto);
                    request.getSession().setAttribute(NewApplicationDelegator.APP_PREMISES_MAP, appPremisesMap);
                    premises.setPremisesSelect(premisesSelect);
                    premises.setExistingData(AppConsts.NO);
                }*/
            }
        }
        setAppSubmissionDto(appSubmissionDto, request);
    }

    /**
     * maybe 2 records: one is modified, one is from DB
     *
     * @param premises
     * @param map
     * @return
     */
    private static List<Map.Entry<String, AppGrpPremisesDto>> getPremisesFromMap(AppGrpPremisesDto premises,
            Map<String, AppGrpPremisesDto> map) {
        return IaisCommonUtils.getList(map.entrySet().stream()
                .filter(e -> Objects.equals(premises.getPremisesIndexNo(), e.getValue().getPremisesIndexNo()))
                .collect(Collectors.toList()));
    }

    public static void clearPremisesMap(HttpServletRequest request) {
        request.getSession().removeAttribute(HcsaAppConst.LIC_PREMISES_MAP);
        request.getSession().removeAttribute(HcsaAppConst.APP_PREMISES_MAP);
        request.getSession().removeAttribute("premisesSelect");
        request.getSession().removeAttribute("conveyancePremSel");
        request.getSession().removeAttribute("offSitePremSel");
        request.getSession().removeAttribute("easMtsPremSel");
    }

    public static AppGrpPremisesDto getPremisesFromMap(String premSelectVal, HttpServletRequest request) {
        log.info(StringUtil.changeForLog("##### Prem select val: " + StringUtil.clarify(premSelectVal)));
        Map<String, AppGrpPremisesDto> premisesDtoMap = checkPremisesMap(false, request);
        return (AppGrpPremisesDto) CopyUtil.copyMutableObject(premisesDtoMap.get(premSelectVal));
    }

    public static String getParamName(String prefix, String name) {
        StringBuilder param = new StringBuilder();
        if (!StringUtil.isEmpty(prefix)) {
            param.append(prefix).append(StringUtil.capitalize(name));
        } else {
            param.append(name);
        }
        //param.append(StringUtil.getNonNull(suffix));
        return param.toString();
    }

    public static String getPrsFlag() {
        return ConfigHelper.getString("moh.halp.prs.enable");
    }

    public static List<SelectOption> genPersonnelTypeSel(String currentSvcCod) {
        List<SelectOption> personnelTypeSel = IaisCommonUtils.genNewArrayList();
        if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(currentSvcCod)) {
            SelectOption personnelTypeOp1 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL));
            SelectOption personnelTypeOp2 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST));
            SelectOption personnelTypeOp3 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER));
            SelectOption personnelTypeOp4 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE));
            personnelTypeSel.add(personnelTypeOp1);
            personnelTypeSel.add(personnelTypeOp2);
            personnelTypeSel.add(personnelTypeOp3);
            personnelTypeSel.add(personnelTypeOp4);
        } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(currentSvcCod)) {
            SelectOption personnelTypeOp3 = new SelectOption(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER, MasterCodeUtil.getCodeDesc(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER));
            personnelTypeSel.add(personnelTypeOp3);
        } else if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(currentSvcCod)) {

        } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(currentSvcCod)) {

        }
        ApplicationHelper.doSortSelOption(personnelTypeSel);
        return personnelTypeSel;
    }

    public static void setStepColor(Map<String, String> map, String serviceConfig, AppSubmissionDto appSubmissionDto) {
        List<String> strList = IaisCommonUtils.genNewArrayList();
        if (map != null) {
            map.forEach((k, v) -> {
                if (!StringUtil.isEmpty(v)) {
                    strList.add(v);
                }
            });
        }
        if (serviceConfig != null) {
            strList.add(serviceConfig);
        }
        appSubmissionDto.setStepColor(strList);
    }

    public static String emailAddressesToString(List<String> emailAddresses) {
        StringBuilder emailAddress = new StringBuilder();
        if (emailAddresses.isEmpty()) {
            return emailAddress.toString();
        }
        if (emailAddresses.size() == 1) {
            emailAddress.append(emailAddresses.get(0));
        } else {
            for (int i = 0; i < emailAddresses.size(); i++) {
                if (i == emailAddresses.size() - 1) {
                    emailAddress.append(emailAddresses.get(i));
                } else {
                    emailAddress.append(emailAddresses.get(i)).append(", ");
                }
            }
        }
        return emailAddress.toString();
    }

    public static List<AppSvcChckListDto> handlerPleaseIndicateLab(List<AppSvcChckListDto> appSvcChckListDtos) {
        List<AppSvcChckListDto> newAppSvcChckListDtos = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcChckListDtos)) {
            AppSvcChckListDto targetDto = appSvcChckListDtos.stream()
                    .filter(dto -> Objects.equals(HcsaAppConst.PLEASEINDICATE, dto.getChkName()))
                    .findAny()
                    .orElse(null);
            if (targetDto != null) {
                for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                    AppSvcChckListDto newAppSvcChckListDto = (AppSvcChckListDto) CopyUtil.copyMutableObject(appSvcChckListDto);
                    String chkName = newAppSvcChckListDto.getChkName();
                    if (HcsaAppConst.PLEASEINDICATE.equals(chkName)) {
                        continue;
                    }
                    if (HcsaAppConst.SERVICE_SCOPE_LAB_OTHERS.equals(chkName)) {
                        chkName = chkName + " (" + targetDto.getOtherScopeName() + ")";
                        newAppSvcChckListDto.setChkName(chkName);
                    }
                    newAppSvcChckListDtos.add(newAppSvcChckListDto);
                }
            } else {
                newAppSvcChckListDtos = appSvcChckListDtos;
            }
        }
        return newAppSvcChckListDtos;
    }
}
