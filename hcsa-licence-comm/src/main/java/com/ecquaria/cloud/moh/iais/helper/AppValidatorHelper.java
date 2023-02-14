package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSecDetailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocumentShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SpecialServiceSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpSecondAddrDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremGroupOutsourcedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremNonLicRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoAbortDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoMedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoNurseDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoTopDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoTopPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOutsouredDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSpecialServiceInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmFormDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.CheckCoLocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.SuppleFormItemConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ReflectionUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.VehNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.validation.DeclarationsUtil;
import com.ecquaria.cloud.moh.iais.validation.ValidateVehicle;
import com.ecquaria.egp.core.common.constants.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @Auther chenlei on 5/4/2022.
 */
@Slf4j
public final class AppValidatorHelper {

    private static LicCommService getLicCommService() {
        return SpringHelper.getBean(LicCommService.class);
    }

    private static SystemParamConfig getSystemParamConfig() {
        return SpringHelper.getBean(SystemParamConfig.class);
    }

    private static ConfigCommService getConfigCommService() {
        return SpringHelper.getBean(ConfigCommService.class);
    }

    private static AppCommService getAppCommService() {
        return SpringHelper.getBean(AppCommService.class);
    }

    public static String repLength(String... ars) {
        int length = ars.length;
        String errorMsg = MessageUtil.getMessageDesc("GENERAL_ERR0041");
        if (length == 0) {
            repLength(errorMsg);
        } else if (length == 1) {
            String field = ars[0].replace("{field}", "field");
            field = field.replace("{maxlength}", "100");
            return field;
        } else if (length == 2) {
            Iterator<String> iterator = Arrays.stream(ars).iterator();
            if (iterator.hasNext()) {
                errorMsg = errorMsg.replace("{field}", iterator.next());
            }
            if (iterator.hasNext()) {
                errorMsg = errorMsg.replace("{maxlength}", iterator.next());
            }

            return errorMsg;
        } else if (length == 3) {
            Iterator<String> iterator = Arrays.stream(ars).iterator();
            String ars0 = iterator.hasNext() ? iterator.next() : "";
            String ars1 = iterator.hasNext() ? iterator.next() : "";
            String messageDesc = MessageUtil.getMessageDesc(ars0);
            messageDesc = messageDesc.replace("{field}", ars0);
            messageDesc = messageDesc.replace("{maxlength}", ars1);
            return messageDesc;
        } else if (length == 4) {
            Iterator<String> iterator = Arrays.stream(ars).iterator();
            String ars0 = iterator.hasNext() ? iterator.next() : "";
            String ars1 = iterator.hasNext() ? iterator.next() : "";
            String ars2 = iterator.hasNext() ? iterator.next() : "";
            String ars3 = iterator.hasNext() ? iterator.next() : "";
            errorMsg = errorMsg.replace(ars0, ars1);
            errorMsg = errorMsg.replace(ars2, ars3);
            return errorMsg;
        } else if (length == 5) {
            Iterator<String> iterator = Arrays.stream(ars).iterator();
            String ars0 = iterator.hasNext() ? iterator.next() : "";
            String messageDesc = MessageUtil.getMessageDesc(ars0);
            if (messageDesc != null) {
                String ars1 = iterator.hasNext() ? iterator.next() : "";
                String ars2 = iterator.hasNext() ? iterator.next() : "";
                String ars3 = iterator.hasNext() ? iterator.next() : "";
                String ars4 = iterator.hasNext() ? iterator.next() : "";
                messageDesc = messageDesc.replace(ars1, ars2);
                messageDesc = messageDesc.replace(ars3, ars4);
            }
            return messageDesc;
        }
        return errorMsg;
    }

    /**
     * validate the all submission data
     *
     * @param request
     * @return
     */
    public static Map<String, String> doPreviewAndSumbit(HttpServletRequest request) {
        Map<String, String> previewAndSubmitMap = IaisCommonUtils.genNewHashMap();
        //
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(request);
        List<String> premisesHciList = ApplicationHelper.checkPremisesHciList(appSubmissionDto.getLicenseeId(), ApplicationHelper.checkIsRfi(request),
                oldAppSubmissionDto, false, request);
        doPreviewSubmitValidate(previewAndSubmitMap, premisesHciList, appSubmissionDto, request);
        return previewAndSubmitMap;
    }

    public static void doPreviewSubmitValidate(Map<String, String> previewAndSubmitMap, List<String> premisesHciList,
            AppSubmissionDto appSubmissionDto, HttpServletRequest request) {
        boolean isRfi = ApplicationHelper.checkIsRfi(request);
        doPreviewSubmitValidate(previewAndSubmitMap, appSubmissionDto, premisesHciList, isRfi);
    }

    public static List<String> doPreviewSubmitValidate(Map<String, String> errorMap, AppSubmissionDto appSubmissionDto,
            boolean isRfi) {
        return IaisCommonUtils.genNewArrayList(0);
        /*List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                ApplicationHelper.setWrkTime(appGrpPremisesDto);
            }
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
        }
        return doPreviewSubmitValidate(errorMap, appSubmissionDto, null, null, isRfi, null);*/
    }

    private static List<String> doPreviewSubmitValidate(Map<String, String> errorMap, AppSubmissionDto appSubmissionDto,
            List<String> premisesHciList, boolean isRfi) {
        List<String> errorList = IaisCommonUtils.genNewArrayList();
        if (appSubmissionDto == null) {
            return errorList;
        }
        if (errorMap == null) {
            errorMap = IaisCommonUtils.genNewHashMap();
        }
        Map<String, String> coMap = appSubmissionDto.getCoMap();
        if (coMap == null) {
            coMap = ApplicationHelper.createCoMap(true);
        }
        // sub licensee (licensee details)
        SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
        boolean isValid = validateSubLicenseeDto(errorMap, subLicenseeDto);
        if (!isValid) {
            coMap.put(HcsaAppConst.SECTION_LICENSEE, "");
            errorList.add(HcsaAppConst.SECTION_LICENSEE);
        } else {
            coMap.put(HcsaAppConst.SECTION_LICENSEE, HcsaAppConst.SECTION_LICENSEE);
        }
        // premises
        Map<String, String> premissMap = doValidatePremises(appSubmissionDto, premisesHciList, isRfi, false);
        premissMap.remove("hciNameUsed");
        if (!premissMap.isEmpty()) {
            errorMap.putAll(premissMap);
            coMap.put(HcsaAppConst.SECTION_PREMISES, "");
            errorList.add(HcsaAppConst.SECTION_PREMISES);
        } else {
            coMap.put(HcsaAppConst.SECTION_PREMISES, HcsaAppConst.SECTION_PREMISES);
        }
        // Category/Discipline & Specialised Service/Specified Test
        List<AppPremSpecialisedDto> appPremSpecialisedDtoList = appSubmissionDto.getAppPremSpecialisedDtoList();
        if (appPremSpecialisedDtoList != null) {
            List<String> svcCodes = appPremSpecialisedDtoList.stream()
                    .map(AppPremSpecialisedDto::getBaseSvcCode)
                    .filter(StringUtil::isNotEmpty)
                    .collect(Collectors.toList());
            StringJoiner joiner = new StringJoiner(AppConsts.DFT_DELIMITER);
            for (String svcCode : svcCodes) {
                Map<String, String> speMap = doValidateSpecialisedDtoList(svcCode, appSubmissionDto.getAppPremSpecialisedDtoList());
                if (!speMap.isEmpty()) {
                    joiner.add(svcCode);
                    errorMap.putAll(speMap);
                    IaisCommonUtils.addToList(HcsaAppConst.SECTION_SPECIALISED, errorList);
                }
            }
            String sign = joiner.toString();
            coMap.put(HcsaAppConst.SECTION_MULTI_SS, sign);
            if (StringUtil.isEmpty(sign)) {
                coMap.put(HcsaAppConst.SECTION_SPECIALISED, HcsaAppConst.SECTION_SPECIALISED);
            } else {
                coMap.put(HcsaAppConst.SECTION_SPECIALISED, "");
                errorList.add(HcsaAppConst.SECTION_SPECIALISED);
            }
        }
        // service info
        StringJoiner joiner = new StringJoiner(AppConsts.DFT_DELIMITER);
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto currSvcInfoDto : dto) {
            Map<String, String> map = doCheckBox(currSvcInfoDto, appSubmissionDto, null, errorList);
            if (!map.isEmpty()) {
                errorMap.putAll(map);
                IaisCommonUtils.addToList(HcsaAppConst.SECTION_SVCINFO, errorList);
                joiner.add(currSvcInfoDto.getServiceCode());
            }
        }
        String sign = joiner.toString();
        coMap.put(HcsaAppConst.SECTION_MULTI_SVC, sign);
        if (StringUtil.isEmpty(sign)) {
            coMap.put(HcsaAppConst.SECTION_SVCINFO, HcsaAppConst.SECTION_SVCINFO);
        } else {
            coMap.put(HcsaAppConst.SECTION_SVCINFO, "");
            errorList.add(HcsaAppConst.SECTION_SVCINFO);
        }
        appSubmissionDto.setCoMap(coMap);
        setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                appSubmissionDto.getLicenceNo());
        log.info(StringUtil.changeForLog("Error Message for App Submission Validation: " + errorMap));
        log.info(StringUtil.changeForLog("Co Map: " + coMap));
        return errorList;
    }

    public static Map<String, String> doCheckBox(AppSvcRelatedInfoDto dto, AppSubmissionDto appSubmissionDto,
            Map<String, AppSvcPersonAndExtDto> licPersonMap) {
        return doCheckBox(dto, appSubmissionDto, licPersonMap, IaisCommonUtils.genNewArrayList());
    }

    private static Map<String, String> doCheckBox(AppSvcRelatedInfoDto dto, AppSubmissionDto appSubmissionDto,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, List<String> errorList) {
        List<AppSvcRelatedInfoDto> dtos=appSubmissionDto.getAppSvcRelatedInfoDtoList();
        SubLicenseeDto subLicenseeDto=appSubmissionDto.getSubLicenseeDto();
        if (dto == null) {
            return IaisCommonUtils.genNewHashMap();
        }
        String serviceId = dto.getServiceId();
        String prsFlag = ApplicationHelper.getPrsFlag();

        ConfigCommService configCommService = getConfigCommService();
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = configCommService.getHcsaServiceStepSchemesByServiceId(serviceId);
        List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig = configCommService.getSvcAllPsnConfig(hcsaServiceStepSchemeDtos, serviceId);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : currentSvcAllPsnConfig) {
            String psnType = hcsaSvcPersonnelDto.getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
            valiatePersonnelCount(dto, psnType, mandatoryCount, errorMap, hcsaServiceStepSchemeDtos, errorList);
        }
        int prevSize;
        String prsError = MessageUtil.getMessageDesc("GENERAL_ERR0042");
        for (HcsaServiceStepSchemeDto step : hcsaServiceStepSchemeDtos) {
            String currentStep = Optional.ofNullable(step)
                    .map(HcsaServiceStepSchemeDto::getStepCode)
                    .orElse("");
            String stepName = Optional.ofNullable(step)
                    .map(HcsaServiceStepSchemeDto::getStepName)
                    .orElse("");
            prevSize = errorMap.size();
            switch (currentStep) {
                case HcsaConsts.STEP_BUSINESS_NAME:
                    // business name
                    List<AppSvcBusinessDto> appSvcBusinessDtoList = dto.getAppSvcBusinessDtoList();
                    doValidateBusiness(appSubmissionDto,appSvcBusinessDtoList, dto.getApplicationType(), dto.getLicenceId(), dto.getServiceId(),errorMap);
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                case HcsaConsts.STEP_VEHICLES:
                    // Vehicles
                    List<String> ids = ApplicationHelper.getRelatedId(dto.getAppId(), dto.getLicenceId(),
                            dto.getServiceName());

                    List<AppSvcVehicleDto> otherExistedVehicles = getAppCommService().getActiveVehicles(ids, true);
                    List<AppSvcVehicleDto> appSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
                    if (!IaisCommonUtils.isEmpty(dtos)) {
                        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : dtos) {
                            // Don't add current service vehicles
                            if (Objects.equals(appSvcRelatedInfoDto.getServiceId(), serviceId)) {
                                continue;
                            }
                            List<AppSvcVehicleDto> appSvcVehicleDtoList = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
                            if (!IaisCommonUtils.isEmpty(appSvcVehicleDtoList)) {
                                appSvcVehicleDtos.addAll(appSvcVehicleDtoList);
                            }
                        }
                    }
                    new ValidateVehicle().doValidateVehicles(errorMap, appSvcVehicleDtos, dto.getAppSvcVehicleDtoList(),
                            otherExistedVehicles, false);
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                case HcsaConsts.STEP_CLINICAL_DIRECTOR:
                    // Clinical Director
                    String currSvcCode = dto.getServiceCode();
                    if (StringUtil.isEmpty(currSvcCode)) {
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
                        currSvcCode = Optional.of(hcsaServiceDto).map(HcsaServiceDto::getSvcCode).orElse("");
                    }
                    List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = dto.getAppSvcClinicalDirectorDtoList();
                    Map<String, String> cdMap = doValidateClincalDirector(appSvcClinicalDirectorDtos, licPersonMap, false,
                            currSvcCode);
                    if (appSvcClinicalDirectorDtos != null && cdMap.isEmpty() && "Y".equals(prsFlag)) {
                        int i = 0;
                        for (AppSvcPrincipalOfficersDto person : appSvcClinicalDirectorDtos) {
                            if (!checkProfRegNo(person.getProfRegNo())) {
                                errorMap.put("profRegNo" + i, prsError);
                                break;
                            }
                            i++;
                        }
                    }
                    if (!cdMap.isEmpty()) {
                        errorMap.putAll(cdMap);
                    }
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                case HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS:
                    List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = dto.getAppSvcCgoDtoList();
                    Map<String, String> govenMap = doValidateGovernanceOfficers(appSvcCgoDtoList, licPersonMap, false);
                    if (appSvcCgoDtoList != null && govenMap.isEmpty() && "Y".equals(prsFlag)) {
                        int i = 0;
                        for (AppSvcPrincipalOfficersDto person : appSvcCgoDtoList) {
                            if (!checkProfRegNo(person.getProfRegNo())) {
                                govenMap.put("profRegNo" + i, prsError);
                                break;
                            }
                            i++;
                        }
                    }
                    if (!govenMap.isEmpty()) {
                        errorMap.putAll(govenMap);
                    }
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                case HcsaConsts.STEP_SECTION_LEADER: {
                    // Section Leader
                    String currSvcCodes = dto.getServiceCode();
                    if (StringUtil.isEmpty(currSvcCodes)) {
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
                        currSvcCodes = Optional.of(hcsaServiceDto).map(HcsaServiceDto::getSvcCode).orElse("");
                    }
                    doValidateSectionLeader(errorMap, dto.getAppSvcSectionLeaderList(), currSvcCodes);
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                }
                case HcsaConsts.STEP_CHARGES:
                    AppSvcChargesPageDto appSvcChargesPageDto = dto.getAppSvcChargesPageDto();
                    Map<String, String> errMap = doValidateCharges(appSvcChargesPageDto);
                    if (!errMap.isEmpty()){
                        errorMap.putAll(errMap);
                    }
//                    new ValidateCharges().doValidateCharges(errorMap, dto.getAppSvcChargesPageDto());
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                case HcsaConsts.STEP_SERVICE_PERSONNEL:
                    SvcPersonnelDto svcPersonnelDto = dto.getSvcPersonnelDto();
                    doValidateSvcPersonnel(errorMap, svcPersonnelDto);
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                case HcsaConsts.STEP_PRINCIPAL_OFFICERS: {
                    List<AppSvcPrincipalOfficersDto> poList = dto.getAppSvcPrincipalOfficersDtoList();
                    List<AppSvcPrincipalOfficersDto> dpoList = dto.getAppSvcNomineeDtoList();
                    Map<String, String> map = doValidatePoAndDpo(poList, dpoList, licPersonMap, subLicenseeDto, false);
                    if (!map.isEmpty()) {
                        errorMap.putAll(map);
                    }
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                }
                case HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER: {
                    List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderList = dto.getAppSvcKeyAppointmentHolderDtoList();
                    Map<String, String> map = doValidateKeyAppointmentHolder(appSvcKeyAppointmentHolderList,
                            licPersonMap, false);
                    if (!map.isEmpty()) {
                        errorMap.putAll(map);
                    }
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                }
                case HcsaConsts.STEP_MEDALERT_PERSON: {
                    List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = dto.getAppSvcMedAlertPersonList();
                    Map<String, String> map = doValidateMedAlertPsn(appSvcMedAlertPersonList, licPersonMap);
                    if (!map.isEmpty()) {
                        errorMap.putAll(map);
                    }
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                }
                case HcsaConsts.STEP_OTHER_INFORMATION: {
                    String currcode = dto.getServiceCode();
                    List<AppSvcOtherInfoDto> appSvcOtherInfoDtos = dto.getAppSvcOtherInfoList();
                    Map<String, String> map = doValidateOtherInformation(appSvcOtherInfoDtos, currcode);
                    if (!map.isEmpty()) {
                        errorMap.putAll(map);
                    }
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                }
                case HcsaConsts.STEP_SUPPLEMENTARY_FORM: {
                    Map<String, String> map = doValidateSupplementaryFormList(dto.getAppSvcSuplmFormList());
                    if (!map.isEmpty()) {
                        errorMap.putAll(map);
                    }
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                }
                case HcsaConsts.STEP_SPECIAL_SERVICES_FORM: {
                    List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoList = dto.getAppSvcSpecialServiceInfoList();
                    doValidateSpecialServicesForm(appSvcSpecialServiceInfoList, errorMap, licPersonMap);
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                }
                case HcsaConsts.STEP_OUTSOURCED_PROVIDERS: {
                    AppSvcOutsouredDto appSvcOutsouredDto = dto.getAppSvcOutsouredDto();
                    Map<String, String> map = doValidationOutsourced(appSvcOutsouredDto, dto.getCurAt());
                    if (!map.isEmpty()) {
                        errorMap.putAll(map);
                    }
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
                }
                case HcsaConsts.STEP_DOCUMENTS:
                    doValidateSvcDocuments(dto.getDocumentShowDtoList(), errorMap);
                    addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
                    break;
            }
        }
        log.info(StringUtil.changeForLog("Error Message in doCheckBox for [" + dto.getServiceCode() + "] : " + errorMap));
        log.info(StringUtil.changeForLog("Error Steps in doCheckBox for [" + dto.getServiceCode() + "] : " + errorList));
        return errorMap;
    }

    private static String getStepName(String step, List<HcsaServiceStepSchemeDto> stepDtos) {
        if (step == null || stepDtos == null) {
            return "";
        }
        return stepDtos.stream()
                .filter(dto -> step.equals(dto.getStepCode()))
                .findAny()
                .map(HcsaServiceStepSchemeDto::getStepName)
                .orElse("");
    }

    private static void addErrorStep(String currentStep, String stepName, boolean needAdd, List<String> errorList) {
        if (errorList == null || !needAdd || currentStep == null || errorList.contains(currentStep)) {
            return;
        }
        errorList.add(currentStep + ":" + stepName);
    }

    /**
     * validate all related service infos mandatory count
     *
     * @param dto
     * @param psnType
     * @param mandatoryCount
     * @param errorMap
     */
    private static void valiatePersonnelCount(AppSvcRelatedInfoDto dto, String psnType, int mandatoryCount,
            Map<String, String> errorMap,
            List<HcsaServiceStepSchemeDto> stepDtos, List<String> errorList) {
        if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcPrincipalOfficersDtoList(), errorMap,
                    psnType, mandatoryCount, stepDtos, errorList);
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcCgoDtoList(), errorMap,
                    psnType, mandatoryCount, stepDtos, errorList);
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcMedAlertPersonList(), errorMap,
                    psnType, mandatoryCount, stepDtos, errorList);
        } else if (ApplicationConsts.PERSONNEL_VEHICLES.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcVehicleDtoList(), errorMap,
                    psnType, mandatoryCount, stepDtos, errorList);
        } else if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcClinicalDirectorDtoList(), errorMap,
                    psnType, mandatoryCount, stepDtos, errorList);
        } else if (ApplicationConsts.PERSONNEL_CHARGES.equals(psnType)) {
            List<AppSvcChargesDto> appSvcChargesDtos = Optional.ofNullable(dto.getAppSvcChargesPageDto())
                    .map(AppSvcChargesPageDto::getGeneralChargesDtos)
                    .orElse(null);
            validatePersonMandatoryCount(appSvcChargesDtos, errorMap, psnType, mandatoryCount, stepDtos, errorList);
        } else if (ApplicationConsts.PERSONNEL_CHARGES_OTHER.equals(psnType)) {
            List<AppSvcChargesDto> otherChargesDtos = Optional.ofNullable(dto.getAppSvcChargesPageDto())
                    .map(AppSvcChargesPageDto::getOtherChargesDtos)
                    .orElse(null);
            validatePersonMandatoryCount(otherChargesDtos, errorMap, psnType, mandatoryCount, stepDtos, errorList);
        } else if (ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER.equals(psnType)) {
            List<AppSvcPersonnelDto> sectionLeaderList = dto.getAppSvcSectionLeaderList();
            validatePersonMandatoryCount(sectionLeaderList, errorMap, psnType, mandatoryCount, stepDtos, errorList);
        } else if (ApplicationConsts.PERSONNEL_PSN_KAH.equals(psnType)) {
            List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = dto.getAppSvcKeyAppointmentHolderDtoList();
            validatePersonMandatoryCount(appSvcKeyAppointmentHolderDtoList, errorMap, psnType, mandatoryCount, stepDtos, errorList);
        }
    }

    private static String validateMandatoryCount(String psnType, Integer mandatoryCount){
        String mandatoryErrMsg = MessageUtil.getMessageDesc("NEW_ERR0025");
        if (StringUtil.isNotEmpty(mandatoryErrMsg)){
            mandatoryErrMsg = mandatoryErrMsg.replace("{psnType}",
                    psnType);
            mandatoryErrMsg = mandatoryErrMsg.replace("{mandatoryCount}", String.valueOf(mandatoryCount));
        }
        return mandatoryErrMsg;
    }

    private static void validatePersonMandatoryCount(List<?> list, Map<String, String> map, String psnType, Integer mandatoryCount,
            List<HcsaServiceStepSchemeDto> stepDtos, List<String> errorList) {
        boolean isValid = true;
        if (list == null) {
            if (mandatoryCount > 0) {
                map.put("error" + psnType, "No related Personnel found!");
                isValid = false;
            }
        } else if (list.size() < mandatoryCount) {
            map.put("error" + psnType, validateMandatoryCount(IaisCommonUtils.getPersonName(psnType, ApplicationHelper.isBackend()),mandatoryCount));
            isValid = false;
        }
        if (!isValid) {
            String step = ApplicationHelper.getStep(psnType);
            String stepName = getStepName(step, stepDtos);
            addErrorStep(step, stepName, true, errorList);
        }
    }

    public static Map<String, String> doValidatePremises(AppSubmissionDto appSubmissionDto, List<String> premisesHciList,
            boolean rfi, boolean checkOthers) {
        //do validate one premiss
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        List<String> addressList = IaisCommonUtils.genNewArrayList();
        String licenseeId = appSubmissionDto.getLicenseeId();
        String licenceId = appSubmissionDto.getLicenceId();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Set<String> distinctVehicleNos = IaisCommonUtils.genNewHashSet();
        boolean needAppendMsg = false;
        String premiseTypeError = "";
        String selectPremises = "";
        int size = appGrpPremisesDtoList.size();
        //List<String> typeList = IaisCommonUtils.genNewArrayList(size);
        for (int i = 0; i < size; i++) {
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
            String premiseType = appGrpPremisesDto.getPremisesType();
            boolean hciFlag = false;
            if (StringUtil.isEmpty(premiseType)) {
                if ("".equals(premiseTypeError)) {
                    premiseTypeError = MessageUtil.getMessageDesc("GENERAL_ERR0006");
                }
                errorMap.put("premisesType" + i, premiseTypeError);
            } else {
                String premisesSelect = appGrpPremisesDto.getPremisesSelect();
                String appType = appSubmissionDto.getAppType();
                if (StringUtil.isEmpty(premisesSelect) || HcsaAppConst.DFT_FIRST_CODE.equals(premisesSelect)) {
                    if ("".equals(selectPremises)) {
                        selectPremises = MessageUtil.replaceMessage("GENERAL_ERR0006", "Add or select a premises from the list",
                                "field");
                    }
                    errorMap.put("premisesSelect" + i, selectPremises);
                } else {
                    //List<String> floorUnitNo = new ArrayList<>(10);
                    String hciName = appGrpPremisesDto.getHciName();
                    String hciNameKey = "hciName" + i;
                    //migrated licence need  judge
                    if (StringUtil.isEmpty(hciName)) {
                        errorMap.put(hciNameKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Business Name", "field"));
                    } else {
                        if (hciName.length() > 100) {
                            errorMap.put(hciNameKey, repLength("Business Name", "100"));
                        }
                        checkHciName(hciNameKey, hciName, appType, licenceId, errorMap);
                    }

                    switch (premiseType) {
                        case ApplicationConsts.PREMISES_TYPE_PERMANENT:
                            String scdfRefNo = appGrpPremisesDto.getScdfRefNo();
                            if (!StringUtil.isEmpty(scdfRefNo) && scdfRefNo.length() > 66) {
                                errorMap.put("scdfRefNo" + i, repLength("Fire Safety & Shelter Bureau Ref No.", "66"));
                            }
                            String certIssuedDtStr = appGrpPremisesDto.getCertIssuedDtStr();
                            if (!StringUtil.isEmpty(certIssuedDtStr) && !CommonValidator.isDate(certIssuedDtStr)) {
                                errorMap.put("certIssuedDt" + i, "GENERAL_ERR0033");
                            }
                            // Co-Location Services
                            validateCoLocation(errorMap, i, appGrpPremisesDto.getLocateWtihHcsa(),
                                    appGrpPremisesDto.getLocateWtihNonHcsa(), appGrpPremisesDto.getAppPremNonLicRelationDtos());
                            break;
                        case ApplicationConsts.PREMISES_TYPE_CONVEYANCE:
                            String vehicleNo = appGrpPremisesDto.getVehicleNo();
                            AppSvcRelatedInfoDto dto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                            AppCommService appCommService = getAppCommService();
                            List<String> ids = ApplicationHelper.getRelatedId(dto.getAppId(), dto.getLicenceId(),
                                    dto.getServiceName());
                            List<String> vehicles = appCommService.getActiveConveyanceVehicles(ids, true);
                            validateVehicleNo(errorMap, i, vehicleNo, distinctVehicleNos, vehicles);
                            if (appSubmissionDto.getAppSvcRelatedInfoDtoList().size() > 1 && errorMap.get("vehicleNo" + i) == null) {
                                // 86820
                                // GENERAL_ERR0072 - This vehicle is already operating a healthcare service
                                errorMap.put("vehicleNo" + i, "GENERAL_ERR0072");
                            }
                            // Co-Location Services
                            validateCoLocation(errorMap, i, appGrpPremisesDto.getLocateWtihHcsa(),
                                    appGrpPremisesDto.getLocateWtihNonHcsa(), appGrpPremisesDto.getAppPremNonLicRelationDtos());
                            break;
                        case ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE:
                            String easMtsUseOnly = appGrpPremisesDto.getEasMtsUseOnly();
                            String easMtsPubHotline = appGrpPremisesDto.getEasMtsPubHotline();
                            String email = appGrpPremisesDto.getEasMtsPubEmail();
                            if (StringUtil.isEmpty(easMtsUseOnly)) {
                                errorMap.put("easMtsUseOnly" + i, MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                            }
                            // "Public Hotline"
                            if (StringUtil.isEmpty(easMtsPubHotline)) {
                                if (!"UOT002".equals(easMtsUseOnly)) {
                                    errorMap.put("easMtsPubHotline" + i, MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                                }
                            } else if (!easMtsPubHotline.matches("^[6|89][0-9]{7}$")) {
                                // GENERAL_ERR0080 - Please key in a valid public hotline
                                errorMap.put("easMtsPubHotline" + i, MessageUtil.getMessageDesc("GENERAL_ERR0080"));
                            }
                            if (StringUtil.isEmpty(email)) {
                                if (!"UOT002".equals(easMtsUseOnly)) {
                                    errorMap.put("easMtsPubEmail" + i, "GENERAL_ERR0006");
                                }
                            } else if (email.length() > 320) {
                                errorMap.put("easMtsPubEmail" + i, repLength("Public Email", "320"));
                            } else if (!ValidationUtils.isEmail(email)) {
                                errorMap.put("easMtsPubEmail" + i, MessageUtil.getMessageDesc("GENERAL_ERR0014"));
                            }
                            break;
                    }
                    Map<String, String> map = validateContactInfo(appGrpPremisesDto, i, addressList);
                    if (!map.isEmpty()) {
                        errorMap.putAll(map);
                    } else {
                        String hciNameErr = errorMap.get(hciNameKey);
                        String vehicleNo = errorMap.get("conveyanceVehicleNo" + i);
                        hciFlag = StringUtil.isEmpty(hciNameErr) && StringUtil.isEmpty(vehicleNo);
                    }
                    log.info(StringUtil.changeForLog("hciFlag:" + hciFlag));
                }
            }
            //0062204 - 82421
            if (hciFlag) {
                List<String> currentHcis = ApplicationHelper.genPremisesHciList(appGrpPremisesDto);
                if (!IaisCommonUtils.isEmpty(premisesHciList)) {
                    checkHciIsSame(currentHcis, premisesHciList, errorMap, "premisesHci" + i);
                }
            }

            // rfc, renewal
            if (!rfi && checkOthers) {
                validateAffectedLicences(appSubmissionDto, errorMap, appGrpPremisesDtoList);
            }

            if (checkOthers && errorMap.isEmpty()) {
                checkOthers(appGrpPremisesDto, i, licenseeId, appGrpPremisesDtoList, errorMap);
                //65116
                if (hciFlag) {
                    CheckCoLocationDto checkCoLocationDto = new CheckCoLocationDto();
                    checkCoLocationDto.setLicenseeId(licenseeId);
                    checkCoLocationDto.setAppGrpPremisesDto(appGrpPremisesDto);
                    Boolean flag = getLicCommService().getOtherLicseePremises(checkCoLocationDto);
                    if (flag != null && flag) {
                        needAppendMsg = true;
                    }
                }
            }
            if (!errorMap.isEmpty()) {
                appGrpPremisesDto.setHasError(Boolean.TRUE);
                errMap.putAll(errorMap);
            } else {
                appGrpPremisesDto.setHasError(Boolean.FALSE);
            }
        }

        if (checkOthers) {
            //65116
            // NEW_ACK004 - Records indicate another licensed entity is using this address. Do note co-location requirements may apply.
            String hciNameUsed = errMap.get("hciNameUsed");
            String errMsg = MessageUtil.getMessageDesc("NEW_ACK004");
            if (needAppendMsg) {
                if (StringUtil.isEmpty(hciNameUsed)) {
                    errMap.put("hciNameUsed", errMsg);
                } else {
                    String hciNameMsg = MessageUtil.getMessageDesc(hciNameUsed);
                    errMap.put("hciNameUsed", hciNameMsg + "<br/>" + errMsg);
                }
            }
        }
        log.info(StringUtil.changeForLog("the do doValidatePremiss end ...."));
        return errMap;
    }

    private static void checkOthers(AppGrpPremisesDto appGrpPremisesDto, int i, String licenseeId,
            List<AppGrpPremisesDto> appGrpPremisesDtoList, Map<String, String> errorMap) {
        List<PremisesDto> premisesDtos =
                getLicCommService().getPremisesDtoByHciNameAndPremType(appGrpPremisesDto.getHciName(),
                        appGrpPremisesDto.getPremisesType(), licenseeId);
        if (!IaisCommonUtils.isEmpty(premisesDtos)) {
            // NEW_ACK011 - The business name you have keyed in is currently in used.
            errorMap.put("hciNameUsed", MessageUtil.getMessageDesc("NEW_ACK011"));
        }
        String premisesSelect = ApplicationHelper.getPremisesKey(appGrpPremisesDto);
        if (appGrpPremisesDtoList.stream().anyMatch(dto -> !Objects.equals(appGrpPremisesDto.getPremisesIndexNo(),
                dto.getPremisesIndexNo()) && Objects.equals(premisesSelect, ApplicationHelper.getPremisesKey(dto)))) {
            // NEW_ERR0012 - This is a repeated entry
            errorMap.put("premisesHci" + i, "NEW_ERR0012");
        } else {
            HttpServletRequest request = MiscUtil.getCurrentRequest();
            AppGrpPremisesDto premises = null;
            if (request != null) {
                premises = ApplicationHelper.getPremisesFromMap(premisesSelect, request);
            }
            if (premises != null && (ApplicationConsts.NEW_PREMISES.equals(appGrpPremisesDto.getPremisesSelect())
                    || !Objects.equals(appGrpPremisesDto.getPremisesSelect(), premisesSelect))) {
                errorMap.put("premisesHci" + i,
                        MessageUtil.replaceMessage("GENERAL_ERR0050", HcsaConsts.MODE_OF_SVC_DELIVERY, "field"));
            }
        }
    }

    private static void validateAffectedLicences(AppSubmissionDto appSubmissionDto, Map<String, String> errorMap,
            List<AppGrpPremisesDto> appGrpPremisesDtoList) {
        String appType = appSubmissionDto.getAppType();
        if (!StringUtil.isIn(appType, new String[]{ApplicationConsts.APPLICATION_TYPE_RENEWAL,
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE})) {
            return;
        }
        AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(0);
        List<LicenceDto> licenceDtos = appGrpPremisesDto.getLicenceDtos();
        if (IaisCommonUtils.isEmpty(licenceDtos)) {
            return;
        }
        // mandatory for RFC and Renewal, not RFI
        String[] selectedLicences = appGrpPremisesDto.getSelectedLicences();
        if (selectedLicences == null || selectedLicences.length == 0 || selectedLicences[0] == null) {
            errorMap.put("selectedLicences", "GENERAL_ERR0006");
        }
    }

    /**
     * There is an existing licence for this service.
     *
     * @param currentHcis
     * @param premisesHciList
     * @param errorMap
     * @param errName
     */
    private static void checkHciIsSame(List<String> currentHcis, List<String> premisesHciList, Map<String, String> errorMap,
            String errName) {
        for (String hci : currentHcis) {
            if (premisesHciList.contains(hci)) {
                errorMap.put(errName, "NEW_ERR0005");
            }
        }
    }

    private static void checkHciName(String key, String hciName, String appType, String licenceId, Map<String, String> errorMap) {
        int hciNameChanged = 0;
        if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
            hciNameChanged = checkNameChanged(hciName, null, licenceId);
        }
        if (2 == hciNameChanged || 4 == hciNameChanged) {
            //no need validate hci name have keyword (is migrated and hci name never changed)
        } else {
            Map<Integer, String> map = checkBlacklist(hciName);
            if (!map.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                AtomicInteger length = new AtomicInteger();
                map.forEach((k, v) -> {
                    length.getAndIncrement();
                    sb.append(v);
                    if (map.size() != length.get()) {
                        sb.append(',').append(' ');
                    }
                });
                errorMap.put(key, MessageUtil.replaceMessage("GENERAL_ERR0016", sb.toString(), "keywords"));
            }
        }
    }

    private static void validateCoLocation(Map<String, String> errorMap, int index, String locateWtihHcsa, String locateWtihNonHcsa,
            List<AppPremNonLicRelationDto> appPremNonLicRelationDtos) {
        if (StringUtil.isEmpty(locateWtihHcsa)) {
            errorMap.put("locateWtihHcsa" + index, "GENERAL_ERR0006");
        }
        if (StringUtil.isEmpty(locateWtihNonHcsa)) {
            errorMap.put("locateWtihNonHcsa" + index, "GENERAL_ERR0006");
        } else if (!AppConsts.YES.equals(locateWtihNonHcsa)) {
            return;
        }
        if (IaisCommonUtils.isEmpty(appPremNonLicRelationDtos)) {
            errorMap.put(index + "CoBusinessName0", "GENERAL_ERR0006");
            errorMap.put(index + "CoSvcName0", "GENERAL_ERR0006");
        } else {
            List<String> nonLicRelList = IaisCommonUtils.genNewArrayList();
            int i = 0;
            for (AppPremNonLicRelationDto appPremNonLicRelationDto : appPremNonLicRelationDtos) {
                String coBusinessName = appPremNonLicRelationDto.getBusinessName();
                String coSvcName = appPremNonLicRelationDto.getProvidedService();
                if (StringUtil.isEmpty(coBusinessName) && StringUtil.isEmpty(coSvcName)) {
                    continue;
                }
                boolean isValid = true;
                if (StringUtil.isEmpty(coBusinessName)) {
                    errorMap.put(index + "CoBusinessName" + i, "GENERAL_ERR0006");
                    isValid = false;
                } else if (coBusinessName.length() > 100) {
                    errorMap.put(index + "CoBusinessName" + i, repLength("Business Name", "100"));
                    isValid = false;
                }
                if (StringUtil.isEmpty(coSvcName)) {
                    errorMap.put(index + "CoSvcName" + i, "GENERAL_ERR0006");
                    isValid = false;
                } else if (coSvcName.length() > 100) {
                    errorMap.put(index + "CoSvcName" + i, repLength("Services Provided", "100"));
                    isValid = false;
                }
                if (isValid) {
                    String data = coBusinessName + AppConsts.DFT_DELIMITER3 + coSvcName;
                    if (nonLicRelList.contains(data)) {
                        // NEW_ERR0012 - This is a repeated entry
                        errorMap.put(index + "CoBusinessName" + i, "NEW_ERR0012");
                    } else {
                        nonLicRelList.add(data);
                    }
                }
                i++;
            }

        }

    }

    /**
     * 4.2.3.4	Mode of Service Delivery
     * 13.	A vehicle number should not be repeated across other licenced conveyances from other services
     * (i.e. vehicle number used for EAS cannot be used for Clinical Lab conveyance)
     */
    private static void validateVehicleNo(Map<String, String> errorMap, int index, String vehicleNo,
            Set<String> distinctVehicleNos, List<String> vehicles) {
        if (StringUtil.isEmpty(vehicleNo)) {
            errorMap.put("vehicleNo" + index, MessageUtil.replaceMessage("GENERAL_ERR0006", "Vehicle No.", "field"));
        } else {
            if (vehicleNo.length() > 10) {
                String errorMsg = repLength("Vehicle No.", "10");
                errorMap.put("vehicleNo" + index, errorMsg);
            }
            boolean b = VehNoValidator.validateNumber(vehicleNo);
            if (!b) {
                errorMap.put("vehicleNo" + index, "GENERAL_ERR0017");
            }

            if (distinctVehicleNos.contains(vehicleNo)) {
                // NEW_ERR0012 - This is a repeated entry
                errorMap.put("vehicleNo" + index, "NEW_ERR0012");
            } else if (vehicles.stream().anyMatch(vehicleNo::equalsIgnoreCase)) {
                // NEW_ERR0016 - This record already exists.
                errorMap.put("vehicleNo" + index, "NEW_ERR0016");
            } else {
                distinctVehicleNos.add(vehicleNo);
            }
        }

    }

    public static void validateSecondAddress(AppSubmissionDto appSubmissionDto, Map<String, String> errorMap, HttpServletRequest request) {
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<String> mosdAddressList = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(appGrpPremisesDtoList)) {
            return;
        }


        AppGrpPremisesDto dto = appSubmissionDto.getAppGrpPremisesDtoList().get(0);
        StringBuilder mosdAddress = new StringBuilder();
        if (!StringUtil.isEmpty(dto)) {
            mosdAddress.append(StringUtil.getNonNull(dto.getFloorNo()))
                    .append(StringUtil.getNonNull(dto.getBlkNo()))
                    .append(StringUtil.getNonNull(dto.getPostalCode()))
                    .append(StringUtil.getNonNull(dto.getUnitNo()));
        }

        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = dto.getAppPremisesOperationalUnitDtos();
        if (IaisCommonUtils.isNotEmpty(appPremisesOperationalUnitDtos)) {
            for (AppPremisesOperationalUnitDto appPremisesOperationalUnitDto : appPremisesOperationalUnitDtos) {
                mosdAddress.append(StringUtil.getNonNull(appPremisesOperationalUnitDto.getFloorNo()))
                        .append(StringUtil.getNonNull(appPremisesOperationalUnitDto.getUnitNo()));
            }
        }
        String prefix = "address";
        List<String> addressList = IaisCommonUtils.genNewArrayList();
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
            if (StringUtil.isEmpty(appGrpPremisesDto) || IaisCommonUtils.isEmpty(appGrpPremisesDto.getAppGrpSecondAddrDtos())) {
                return;
            }
            List<AppGrpSecondAddrDto> appGrpSecondAddrDtos = appGrpPremisesDto.getAppGrpSecondAddrDtos();
            int size1 = appGrpSecondAddrDtos.size();
            for (int i = 0; i < size1; i++) {
                boolean isEmpty = true;
                AppGrpSecondAddrDto appGrpSecondAddrDto = appGrpSecondAddrDtos.get(i);
                if (StringUtil.isEmpty(appGrpSecondAddrDto)) {
                    return;
                }
                if (i == 0 && size1 == 1) {
                    isEmpty = !ReflectionUtil.isEmpty(appGrpSecondAddrDto, "indexNo", "appGrpPremisesId", "seqNum");
                }
                if (isEmpty) {
                    String postalCode = appGrpSecondAddrDto.getPostalCode();
                    String buildingName = appGrpSecondAddrDto.getBuildingName();
                    String streetName = appGrpSecondAddrDto.getStreetName();
                    String addrType = appGrpSecondAddrDto.getAddrType();
                    String blkNo = appGrpSecondAddrDto.getBlkNo();
                    String blkNoKey = prefix + "blkNo" + i;

                    StringBuilder content = new StringBuilder();
                    content.append(StringUtil.getNonNull(appGrpSecondAddrDto.getFloorNo()));
                    content.append(StringUtil.getNonNull(blkNo));
                    content.append(StringUtil.getNonNull(postalCode));
                    content.append(StringUtil.getNonNull(appGrpSecondAddrDto.getUnitNo()));
                    if (IaisCommonUtils.isNotEmpty(appGrpSecondAddrDto.getAppPremisesOperationalUnitDtos())) {
                        for (int j = 0; j < appGrpSecondAddrDto.getAppPremisesOperationalUnitDtos().size(); j++) {
                            AppPremisesOperationalUnitDto dtos = appGrpSecondAddrDto.getAppPremisesOperationalUnitDtos().get(j);
                            content.append(StringUtil.getNonNull(dtos.getFloorNo())).append(StringUtil.getNonNull(dtos.getUnitNo()));
                        }
                    }
                    mosdAddressList.add(content.toString());


                    if (!StringUtil.isEmpty(buildingName) && buildingName.length() > 66) {
                        String errorMsg = repLength("Building Name", "66");
                        errorMap.put(prefix + "buildingName" + i, errorMsg);
                    }
                    if (StringUtil.isEmpty(streetName)) {
                        errorMap.put(prefix + "streetName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name", "field"));
                    } else if (streetName.length() > 32) {
                        errorMap.put(prefix + "streetName" + i, repLength("Street Name", "32"));
                    }
                    if (StringUtil.isEmpty(addrType)) {
                        errorMap.put(prefix + "addrType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
                    }
                    boolean empty1 = StringUtil.isEmpty(blkNo);
                    if (empty1 && ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {
                        errorMap.put(blkNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
                    } else if (!empty1 && blkNo.length() > 10) {
                        String errorMsg = repLength("Block / House No.", "10");
                        errorMap.put(blkNoKey, errorMsg);
                    }
                    List<String> floorUnitList = IaisCommonUtils.genNewArrayList();
                    validateOperaionUnits(appGrpSecondAddrDto, errorMap, i, floorUnitList, false);
                    String postalCodeKey = prefix + "postalCode" + i;
                    if (!StringUtil.isEmpty(postalCode)) {
                        if (postalCode.length() > 6) {
                            String errorMsg = repLength("Postal Code", "6");
                            errorMap.put(postalCodeKey, errorMsg);
                        } else if (postalCode.length() < 6) {
                            errorMap.put(postalCodeKey, "NEW_ERR0004");
                        } else if (!postalCode.matches("^[0-9]{6}$")) {
                            errorMap.put(postalCodeKey, "NEW_ERR0004");
                        } else if (mosdAddressList.contains(mosdAddress.toString())) {
                            errorMap.put(postalCodeKey, "NEW_ACK010");
                        } else {
                            if (!floorUnitList.isEmpty()) {
                                for (String str : floorUnitList) {
                                    String sb = postalCode + AppConsts.DFT_DELIMITER3 + str;
                                    if (addressList.contains(sb)) {
                                        // NEW_ACK010 - Please take note this premises address is licenced under another licensee.
                                        errorMap.put(postalCodeKey, "There is a duplicated entry for this premises address");
                                    } else {
                                        addressList.add(sb);
                                    }
                                }
                            }
                        }
                    } else {
                        errorMap.put(postalCodeKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code ", "field"));
                    }

                }
            }
        }
    }

    private static Map<String, String> validateContactInfo(AppGrpPremisesDto appGrpPremisesDto, int i, List<String> addressList) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String premisesType = appGrpPremisesDto.getPremisesType();
        String postalCode = appGrpPremisesDto.getPostalCode();
        String buildingName = appGrpPremisesDto.getBuildingName();
        String streetName = appGrpPremisesDto.getStreetName();
        String addrType = appGrpPremisesDto.getAddrType();
        String blkNo = appGrpPremisesDto.getBlkNo();
        String blkNoKey = "blkNo" + i;

        if (!StringUtil.isEmpty(buildingName) && buildingName.length() > 66) {
            String errorMsg = repLength("Building Name", "66");
            errorMap.put("buildingName" + i, errorMsg);
        }

        if (StringUtil.isEmpty(streetName)) {
            errorMap.put("streetName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name", "field"));
        } else if (streetName.length() > 32) {
            errorMap.put("streetName" + i, repLength("Street Name", "32"));
        }

        if (StringUtil.isEmpty(addrType)) {
            errorMap.put("addrType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
        }
        boolean empty1 = StringUtil.isEmpty(blkNo);
        if (empty1 && ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {
            errorMap.put(blkNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
        } else if (!empty1 && blkNo.length() > 10) {
            String errorMsg = repLength("Block / House No.", "10");
            errorMap.put(blkNoKey, errorMsg);
        }
        // validate floor and units
        List<String> floorUnitList = IaisCommonUtils.genNewArrayList();
        validateOperaionUnits(appGrpPremisesDto, i, floorUnitList, errorMap);
        String postalCodeKey = "postalCode" + i;
        if (!StringUtil.isEmpty(postalCode)) {
            if (postalCode.length() > 6) {
                String errorMsg = repLength("Postal Code", "6");
                errorMap.put(postalCodeKey, errorMsg);
            } else if (postalCode.length() < 6) {
                errorMap.put(postalCodeKey, "NEW_ERR0004");
            } else if (!postalCode.matches("^[0-9]{6}$")) {
                errorMap.put(postalCodeKey, "NEW_ERR0004");
            } else {
                if (!floorUnitList.isEmpty()) {
                    for (String str : floorUnitList) {
                        String sb = premisesType + AppConsts.DFT_DELIMITER3 + postalCode + AppConsts.DFT_DELIMITER3 + str;
                        if (addressList.contains(sb)) {
                            // NEW_ACK010 - Please take note this premises address is licenced under another licensee.
                            errorMap.put(postalCodeKey, "NEW_ACK010");
                        } else {
                            addressList.add(sb);
                        }
                    }
                }
            }
        } else {
            errorMap.put(postalCodeKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code ", "field"));
        }
        return errorMap;
    }

    private static boolean validateOperaionUnits(AppGrpPremisesDto appGrpPremisesDto, int i, List<String> floorUnitList, Map<String,
            String> errorMap) {
        boolean addrTypeFlag = true;
        String floorNo = appGrpPremisesDto.getFloorNo();
        String unitNo = appGrpPremisesDto.getUnitNo();
        String blkNo = appGrpPremisesDto.getBlkNo();
        String addrType = appGrpPremisesDto.getAddrType();
        String floorNoKey = ApplicationHelper.getParamName(String.valueOf(i), "floorNo0");
        String unitNoKey = ApplicationHelper.getParamName(String.valueOf(i), "unitNo0");
        boolean empty = StringUtil.isEmpty(floorNo);
        boolean empty2 = StringUtil.isEmpty(unitNo);
        boolean isAptBlkType = ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType);
        if ((isAptBlkType || !empty2) && empty) {
            addrTypeFlag = false;
            errorMap.put(floorNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
        }
        if (!empty && floorNo.length() > 3) {
            addrTypeFlag = false;
            errorMap.put(floorNoKey, repLength("Floor No.", "3"));
        }
        if ((isAptBlkType || !empty) && empty2) {
            addrTypeFlag = false;
            errorMap.put(unitNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
        }
        if (!empty2 && unitNo.length() > 5) {
            addrTypeFlag = false;
            errorMap.put(unitNoKey, repLength("Unit No.", "5"));
        }
        if (addrTypeFlag) {
            String sb = StringUtil.getNonNull(floorNo) + AppConsts.DFT_DELIMITER3 +
                    StringUtil.getNonNull(blkNo) + AppConsts.DFT_DELIMITER3 + unitNo;
            floorUnitList.add(sb);
        }

        String floorErrName = i + "FloorNo";
        String unitErrName = i + "UnitNo";
        String floorUnitErrName = i + "FloorUnit";
        checkOperaionUnit(appGrpPremisesDto.getAppPremisesOperationalUnitDtos(), errorMap, floorErrName, unitErrName,
                floorUnitErrName, floorUnitList, appGrpPremisesDto);
        String floorNoErr = errorMap.get(floorNoKey);
        String unitNoErr = errorMap.get(unitNoKey);
        return StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
    }

    private static void checkOperaionUnit(List<AppPremisesOperationalUnitDto> operationalUnitDtos, Map<String, String> errorMap,
            String floorErrName, String unitErrName, String floorUnitErrName, List<String> floorUnitList,
            AppGrpPremisesDto appGrpPremisesDto) {
        if (!IaisCommonUtils.isEmpty(operationalUnitDtos)) {
            int opIndex = 1;
            for (AppPremisesOperationalUnitDto operationalUnitDto : operationalUnitDtos) {
                boolean flag = true;
                String floorNo = operationalUnitDto.getFloorNo();
                String unitNo = operationalUnitDto.getUnitNo();
                boolean floorNoFlag = StringUtil.isEmpty(floorNo);
                boolean unitNoFlag = StringUtil.isEmpty(unitNo);
                if (!(floorNoFlag && unitNoFlag)) {
                    if (floorNoFlag) {
                        flag = false;
                        errorMap.put(floorErrName + opIndex, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
                    } else if (unitNoFlag) {
                        flag = false;
                        errorMap.put(unitErrName + opIndex, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
                    }
                }

                if (!floorNoFlag && floorNo.length() > 3) {
                    flag = false;
                    errorMap.put(floorErrName + opIndex, repLength("Floor No.", "3"));
                }

                if (!unitNoFlag && unitNo.length() > 5) {
                    flag = false;
                    errorMap.put(unitErrName + opIndex, repLength("Unit No.", "5"));
                }
                if (flag) {
                    if (!StringUtil.isEmpty(floorNo) && !StringUtil.isEmpty(unitNo)) {
                        String blkNo = appGrpPremisesDto.getBlkNo();
                        String floorUnitStr = floorNo + AppConsts.DFT_DELIMITER3 +
                                StringUtil.getNonNull(blkNo) + AppConsts.DFT_DELIMITER3 + unitNo;
                        if (floorUnitList.contains(floorUnitStr)) {
                            errorMap.put(floorUnitErrName + opIndex, "NEW_ERR0017");
                        } else {
                            floorUnitList.add(floorUnitStr);
                        }
                    }
                }
                opIndex++;
            }
        }

    }

    public static boolean validateSubLicenseeDto(Map<String, String> errorMap, SubLicenseeDto subLicenseeDto) {
        if (subLicenseeDto == null) {
            if (errorMap != null) {
                errorMap.put("licenseeType", "Invalid Data");
            }
            return false;
        }
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        String propertyName = "save";
        if (OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(subLicenseeDto.getLicenseeType())) {
            propertyName = "soloSave";
        }
        ValidationResult result = WebValidationHelper.validateProperty(subLicenseeDto, propertyName);
        if (result != null) {
            map = result.retrieveAll();
        }

        // add log
        if (!map.isEmpty()) {
            log.info(StringUtil.changeForLog("Error Message For the Sub Licensee : " + map + " - " +
                    JsonUtil.parseToJson(subLicenseeDto)));
        }
        if (OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(subLicenseeDto.getLicenseeType())) {
            if (!map.isEmpty() && errorMap != null) {
                errorMap.put("licenseeType", "Invalid Licensee Type");
            }
        } else {
            if (errorMap != null) {
                errorMap.putAll(map);
            }
        }
        return map.isEmpty();
    }

    public static Map<String, String> doValidatePoAndDpo(List<AppSvcPrincipalOfficersDto> poList,
            List<AppSvcPrincipalOfficersDto> dpoList, Map<String, AppSvcPersonAndExtDto> licPersonMap,
            SubLicenseeDto subLicenseeDto, boolean checkPRS) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        List<String> psnList = IaisCommonUtils.genNewArrayList();
        map.putAll(validateKeyPersonnel(poList, "po", licPersonMap, psnList, subLicenseeDto, null, checkPRS));
        map.putAll(validateKeyPersonnel(dpoList, "dpo", licPersonMap, psnList, subLicenseeDto, null, checkPRS));
        /*if ("-1".equals(deputySelect)) {
            map.put("deputyPrincipalOfficer", "GENERAL_ERR0006");
        }*/
        return map;
    }

    public static Map<String, String> doValidateKeyAppointmentHolder(List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderList,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, boolean checkPRS) {
        if (appSvcKeyAppointmentHolderList == null) {
            return new HashMap<>(1);
        }
        return validateKeyPersonnel(appSvcKeyAppointmentHolderList, "", licPersonMap, checkPRS);
    }

    public static Map<String, String> doValidateClincalDirector(List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorList,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, boolean checkPRS, String svcCode) {
        if (appSvcClinicalDirectorList == null) {
            return new HashMap<>(1);
        }
        return validateKeyPersonnel(appSvcClinicalDirectorList, "", licPersonMap, null, null, svcCode, checkPRS);
    }

    public static Map<String, String> doValidateMedAlertPsn(List<AppSvcPrincipalOfficersDto> medAlertPsnDtos,
            Map<String, AppSvcPersonAndExtDto> licPersonMap) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if (IaisCommonUtils.isEmpty(medAlertPsnDtos)) {
            return errMap;
        }
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < medAlertPsnDtos.size(); i++) {
            String assignSelect = medAlertPsnDtos.get(i).getAssignSelect();
            if (HcsaAppConst.DFT_FIRST_CODE.equals(assignSelect) || StringUtil.isEmpty(assignSelect)) {
                errMap.put("assignSelect" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Assign a MedAlert Person", "field"));
            } else {
                String idTyp = medAlertPsnDtos.get(i).getIdType();
                String idNo = medAlertPsnDtos.get(i).getIdNo();
                String nationality = medAlertPsnDtos.get(i).getNationality();

                // check person key
                String keyIdType = "idType" + i;
                String keyIdNo = "idNo" + i;
                String keyNationality = "nationality" + i;
                boolean isValid = validateId(nationality, idTyp, idNo, keyNationality, keyIdType, keyIdNo, errMap);
                // check duplicated
                if (isValid) {
                    String personKey = ApplicationHelper.getPersonKey(nationality, idTyp, idNo);
                    boolean licPerson = medAlertPsnDtos.get(i).isLicPerson();
                    String idTypeNoKey = "idTypeNo" + i;
                    isValid = doPsnCommValidate(errMap, personKey, idNo, licPerson, licPersonMap, idTypeNoKey);
                    if (isValid) {
                        if (stringList.contains(personKey)) {
                            errMap.put(keyIdNo, "NEW_ERR0012");
                        } else {
                            stringList.add(personKey);
                        }
                    }
                }

                String salutation = medAlertPsnDtos.get(i).getSalutation();
                if (StringUtil.isEmpty(salutation)) {
                    errMap.put("salutation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Salutation", "field"));
                }

                String name = medAlertPsnDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    errMap.put("name" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field"));
                } else {
                    if (name.length() > 100) {
                        String errorMsg = repLength("Name", "100");
                        errMap.put("name" + i, errorMsg);
                    }
                }

                String mobileNo = medAlertPsnDtos.get(i).getMobileNo();
                if (StringUtil.isEmpty(mobileNo)) {
                    errMap.put("mobileNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Mobile No. ", "field"));
                } else if (!StringUtil.isEmpty(mobileNo)) {
                    if (mobileNo.length() > 8) {
                        String errorMsg = repLength("Mobile No.", "8");
                        errMap.put("mobileNo" + i, errorMsg);
                    }
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        errMap.put("mobileNo" + i, "GENERAL_ERR0007");
                    }
                }
                String emailAddr = medAlertPsnDtos.get(i).getEmailAddr();

                if (StringUtil.isEmpty(emailAddr)) {
                    errMap.put("emailAddr" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Email Address", "field"));
                } else if (!StringUtil.isEmpty(emailAddr)) {
                    if (emailAddr.length() > 320) {
                        String errorMsg = repLength("Email Address", "320");
                        errMap.put("emailAddr" + i, errorMsg);
                    }
                    if (!ValidationUtils.isEmail(emailAddr)) {
                        errMap.put("emailAddr" + i, "GENERAL_ERR0014");
                    }
                }
            }
        }
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        return errMap;
    }

    public static Map<String, String> doValidateGovernanceOfficers(List<AppSvcPrincipalOfficersDto> appSvcCgoList,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, boolean checkPRS) {
        if (appSvcCgoList == null) {
            return new HashMap<>(1);
        }
        return validateKeyPersonnel(appSvcCgoList, "", licPersonMap, checkPRS);
    }

    public static Map<String, String> validateKeyPersonnel(List<AppSvcPrincipalOfficersDto> personList, String prefix,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, boolean checkPRS) {
        return validateKeyPersonnel(personList, prefix, licPersonMap, null, null, null, checkPRS);
    }

    public static Map<String, String> validateKeyPersonnel(List<AppSvcPrincipalOfficersDto> personList, String prefix,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, List<String> psnList, SubLicenseeDto subLicenseeDto,
            String svcCode, boolean checkPRS) {
        if (personList == null || personList.isEmpty()) {
            return IaisCommonUtils.genNewHashMap();
        }
        String psnType;
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if (psnList == null) {
            psnList = IaisCommonUtils.genNewArrayList();
        }
        for (int i = 0; i < personList.size(); i++) {
            AppSvcPrincipalOfficersDto person = personList.get(i);
            psnType = person.getPsnType();
            String assignSelect = person.getAssignSelect();
            String deputyPrincipalOfficer = person.getDeputyPrincipalOfficer();
            if ("-1".equals(deputyPrincipalOfficer)){
                errMap.put("deputyPrincipalOfficer", "GENERAL_ERR0006");
            }
            else if (HcsaAppConst.DFT_FIRST_CODE.equals(assignSelect) || StringUtil.isEmpty(assignSelect)){
                errMap.put(prefix + "assignSelect" + i, "GENERAL_ERR0006");
            }else {
                String idTyp = person.getIdType();
                String idNo = person.getIdNo();
                String nationality = person.getNationality();

                String keyIdType = prefix + "idType" + i;
                String keyIdNo = prefix + "idNo" + i;
                String keyNationality = prefix + "nationality" + i;
                boolean isValid = validateId(nationality, idTyp, idNo, keyNationality, keyIdType, keyIdNo, errMap);
                // check duplicated
                if (isValid) {
                    String personKey = ApplicationHelper.getPersonKey(nationality, idTyp, idNo);
                    boolean licPerson = person.isLicPerson();
                    String idTypeNoKey = prefix + "idTypeNo" + i;
                    isValid = doPsnCommValidate(errMap, personKey, idNo, licPerson, licPersonMap, idTypeNoKey);
                    if (isValid) {
                        if (psnList.contains(personKey)) {
                            errMap.put(keyIdNo, "NEW_ERR0012");
                        } else {
                            psnList.add(personKey);
                        }
                        // 113109
                        if (subLicenseeDto != null && ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)
                                && !OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(subLicenseeDto.getLicenseeType())) {
                            String subLicenseeNationality = subLicenseeDto.getNationality();
                            String subLicenseeKey = ApplicationHelper.getPersonKey(subLicenseeNationality,
                                    subLicenseeDto.getIdType(), subLicenseeDto.getIdNumber());
                            if (Objects.equals(subLicenseeKey, personKey)) {
                                errMap.put(prefix + "personError" + i, MessageUtil.getMessageDesc("NEW_ERR0034"));
                            }
                        }
                    }
                }

                String salutation = person.getSalutation();
                if (StringUtil.isEmpty(salutation)) {
                    errMap.put(prefix + "salutation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Salutation", "field"));
                }
                String name = person.getName();
                if (StringUtil.isEmpty(name)) {
                    errMap.put(prefix + "name" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field"));
                } else {
                    if (name.length() > 100) {
                        String errorMsg = repLength("Name", "100");
                        errMap.put(prefix + "name" + i, errorMsg);
                    }
                }

                String designation = person.getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    errMap.put(prefix + "designation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Designation", "field"));
                } else if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                    String otherDesignation = person.getOtherDesignation();
                    if (StringUtil.isEmpty(otherDesignation)) {
                        errMap.put(prefix + "otherDesignation" + i,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Others Designation", "field"));
                    } else if (otherDesignation.length() > 100) {
                        String errorMsg = repLength("Others Designation", "100");
                        errMap.put(prefix + "otherDesignation" + i, errorMsg);
                    }

                }

                String professionalRegoNo = person.getProfRegNo();
                String typeOfCurrRegi = person.getTypeOfCurrRegi();
                String currRegiDate = person.getCurrRegiDateStr();
                String praCerEndDate = person.getPraCerEndDateStr();
                String typeOfRegister = person.getTypeOfRegister();
                String otherQualification = person.getOtherQualification();
                String aclsExpiryDate = person.getAclsExpiryDateStr();
                String specialityOther = person.getSpecialityOther();
                String specialtyGetDate = person.getSpecialtyGetDateStr();
                String relevantExperience = person.getRelevantExperience();
                String bclsExpiryDate = person.getBclsExpiryDateStr();
                String professionBoard = person.getProfessionBoard();
                String officeTelNo = person.getOfficeTelNo();
                String noRegWithProfBoard = person.getNoRegWithProfBoard();
                if (StringUtils.isNotEmpty(specialityOther)) {
                    if (StringUtils.isEmpty(specialtyGetDate)) {
                        errMap.put(prefix + "specialtyGetDate" + i, "GENERAL_ERR0006");
                    }
                }
                if ("po".equals(prefix) || "dpo".equals(prefix)){
                    if (StringUtil.isEmpty(officeTelNo)) {
                        errMap.put(prefix + "officeTelNo" + i, "GENERAL_ERR0006");
                    } else if (!StringUtil.isEmpty(officeTelNo)) {
                        if (officeTelNo.length() > 8) {
                            String errorMsg = repLength("Contact No.", "8");
                            errMap.put(prefix + "officeTelNo" + i, errorMsg);
                        }
                        if (!CommonValidator.isTelephoneNo(officeTelNo)) {
                            errMap.put(prefix + "officeTelNo" + i, "GENERAL_ERR0015");
                        }
                    }
                }

                if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnType)) {
                    if (StringUtil.isEmpty(aclsExpiryDate)) {
                        errMap.put(prefix + "aclsExpiryDate" + i,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Expiry Date (ACLS)", "field"));
                    }
                    if (!isEarly(aclsExpiryDate)) {
                        errMap.put(prefix + "aclsExpiryDate" + i, "GENERAL_ERR010");
                    }
                    if (!"1".equals(noRegWithProfBoard)){
                        if (StringUtil.isEmpty(professionBoard)) {
                            errMap.put(prefix + "professionBoard" + i,
                                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Professional Board", "field"));
                        }
                        if (StringUtil.isEmpty(professionalRegoNo)) {
                            errMap.put(prefix + "profRegNo" + i, "GENERAL_ERR0006");
                        }
                    }
                    if (StringUtil.isEmpty(typeOfCurrRegi)) {
                        errMap.put(prefix + "typeOfCurrRegi" + i,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of Registration Date", "field"));
                    }
                    if (StringUtil.isEmpty(currRegiDate)) {
                        errMap.put(prefix + "currRegiDate" + i, "GENERAL_ERR0006");
                    }
                    if (StringUtil.isEmpty(praCerEndDate)) {
                        errMap.put(prefix + "praCerEndDate" + i,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Practicing Certificate End Date", "field"));
                    }
                    if (StringUtil.isEmpty(typeOfRegister)) {
                        errMap.put(prefix + "typeOfRegister" + i,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of Register", "field"));
                    }
                    if (StringUtil.isEmpty(designation)) {
                        errMap.put(prefix + "designation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Designation", "field"));
                    }
                    if (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(svcCode)) {
                        String speciality = person.getSpeciality();
                        if (StringUtil.isEmpty(speciality) && StringUtils.isNotEmpty(professionalRegoNo)){
                            if (StringUtil.isEmpty(relevantExperience)){
                                errMap.put(prefix + "relevantExperience" + i, "GENERAL_ERR0006");
                            }
                        }
                    }

                    if (AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(svcCode)) {
                        if (StringUtil.isEmpty(bclsExpiryDate)) {
                            errMap.put(prefix + "bclsExpiryDate" + i,
                                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Expiry Date (BCLS and AED)", "filed"));
                        }
                        if (StringUtil.isNotEmpty(bclsExpiryDate) && bclsExpiryDate.length() > 100) {
                            errMap.put(prefix + "bclsExpiryDate" + i, repLength("Expiry Date (BCLS and AED)", "100"));
                        }
                        if (StringUtil.isEmpty(relevantExperience)){
                            errMap.put(prefix + "relevantExperience" + i, "GENERAL_ERR0006");
                        }
                    }
                    String holdCerByEMS = person.getHoldCerByEMS();
                    if (StringUtil.isEmpty(holdCerByEMS)) {
                        errMap.put(prefix + "holdCerByEMS" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                                "Clinical Governance Officer (CGO) holds a valid certification issued by an Emergency Medical Services (\\\"EMS\\\") Medical Directors workshop",
                                "field"));
                    } else if (AppConsts.NO.equals(holdCerByEMS)) {
                        errMap.put(prefix + "holdCerByEMS" + i, MessageUtil.getMessageDesc("NEW_ERR0031"));
                    }
                    if (StringUtil.isNotEmpty(aclsExpiryDate) && aclsExpiryDate.length() > 100) {
                        errMap.put(prefix + "aclsExpiryDate" + i, repLength("Expiry Date (ACLS)", "100"));
                    }
                } else {
                    if (StringUtil.isNotEmpty(typeOfCurrRegi) && typeOfCurrRegi.length() > 50) {
                        errMap.put(prefix + "typeOfCurrRegi" + i, repLength("Type of Registration Date", "50"));
                    } else {
                        if (StringUtil.isNotEmpty(typeOfCurrRegi)) {
                            typeOfCurrRegi = typeOfCurrRegi.toUpperCase(AppConsts.DFT_LOCALE);
                            String[] target = typeOfCurrRegi.split("[^A-Z0-9]+");
                            if (IaisCommonUtils.isEmpty(target) || !Arrays.asList(target).contains("FULL")) {
                                errMap.put(prefix + "typeOfCurrRegi" + i, "GENERAL_ERR0079");
                            }
                        }
                    }
                }

                if (!StringUtil.isEmpty(professionalRegoNo)) {
                    if (professionalRegoNo.length() > 20) {
                        errMap.put(prefix + "profRegNo" + i, repLength("Professional Regn. No.", "20"));
                    } else if (checkPRS) {
                        validateProfRegNo(errMap, professionalRegoNo, "profRegNo" + i);
                    }
                }
                //Current Registration Date
                if (StringUtil.isNotEmpty(currRegiDate) && !CommonValidator.isDate(currRegiDate)) {
                    errMap.put(prefix + "currRegiDate" + i, "GENERAL_ERR0033");
                }
                if (StringUtil.isNotEmpty(praCerEndDate) && !CommonValidator.isDate(praCerEndDate)) {
                    errMap.put(prefix + "praCerEndDate" + i, "GENERAL_ERR0033");
                }
                if (StringUtil.isNotEmpty(typeOfRegister) && typeOfRegister.length() > 50) {
                    errMap.put(prefix + "typeOfRegister" + i, repLength("Type of Register", "50"));
                }

                if (StringUtil.isNotEmpty(specialityOther) && specialityOther.length() > 100) {
                    errMap.put(prefix + "otherQualification" + i, repLength("Other Specialities", "100"));
                }
                // 86960
                if (!ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnType) && !StringUtil.isEmpty(specialityOther)
                        && StringUtil.isEmpty(specialtyGetDate)) {
                    errMap.put(prefix + "specialtyGetDate" + i, "GENERAL_ERR0006");
                } else if (!StringUtil.isEmpty(specialtyGetDate) && !CommonValidator.isDate(specialtyGetDate)){
                    errMap.put(prefix + "specialtyGetDate" + i, "GENERAL_ERR0033");
                }
                if (StringUtil.isNotEmpty(otherQualification) && otherQualification.length() > 100) {
                    errMap.put(prefix + "otherQualification" + i, repLength("Other Qualification", "100"));
                }
                if (StringUtil.isNotEmpty(relevantExperience) && relevantExperience.length() > 100) {
                    errMap.put(prefix + "relevantExperience" + i, repLength("Relevant Experience", "100"));
                }

                if (!ApplicationConsts.PERSONNEL_PSN_KAH.equals(psnType)){
                    String mobileNo = person.getMobileNo();
                    if (StringUtil.isEmpty(mobileNo)) {
                        errMap.put(prefix + "mobileNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Mobile No. ", "field"));
                    } else if (!StringUtil.isEmpty(mobileNo)) {
                        if (mobileNo.length() > 8) {
                            errMap.put(prefix + "mobileNo" + i, repLength("Mobile No.", "8"));
                        }
                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            errMap.put(prefix + "mobileNo" + i, "GENERAL_ERR0007");
                        }
                    }
                    String emailAddr = person.getEmailAddr();
                    if (StringUtil.isEmpty(emailAddr)) {
                        errMap.put(prefix + "emailAddr" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Email Address", "field"));
                    } else {
                        if (emailAddr.length() > 320) {
                            errMap.put(prefix + "emailAddr" + i, repLength("Email Address", "320"));
                        }
                        if (!ValidationUtils.isEmail(emailAddr)) {
                            errMap.put(prefix + "emailAddr" + i, "GENERAL_ERR0014");
                        }
                    }
                }
            }
        }
        return errMap;
    }

    public static boolean check(String s) {
        boolean b = false;
        String tmp = s;
        tmp = tmp.replaceAll("\\p{P}", "");
        if (s.length() != tmp.length()) {
            b = true;
        }
        return b;
    }

    /*
     * @parameter file
     * @parameter fileTypes
     * @parameter fileSize
     * */
    public static Map<String, Boolean> validateFile(CommonsMultipartFile file, List<String> fileTypes, Long fileSize) {
        Map<String, Boolean> map = new HashMap<>();
        if (file != null) {
            long size = file.getSize();
            String filename = file.getOriginalFilename();
            String fileType = filename == null ? "" : filename.substring(filename.lastIndexOf('.') + 1);
            String s = fileType.toUpperCase();
            if (!fileTypes.contains(s)) {
                map.put("fileType", Boolean.FALSE);
            } else {
                map.put("fileType", Boolean.TRUE);
            }
            if (size > fileSize) {
                map.put("fileSize", Boolean.FALSE);
            } else {
                map.put("fileSize", Boolean.TRUE);
            }
        }

        return map;
    }

    /*
     * @parameter file
     * @parameter fileTypes
     * */

    public static Map<String, Boolean> validateFile(CommonsMultipartFile file) {
        List<String> list = new ArrayList<>();
        list.add("PDF");
        list.add("JPG");
        list.add("PNG");
        list.add("DOCX");
        list.add("DOC");
        Long size = 4 * 1024 * 1024L;
        return validateFile(file, list, size);
    }

    private static void validateOverlapEvent(List<AppPremEventPeriodDto> appPremEventPeriodDtoList, Map<String, String> map,
            String errorPrefix) {
        if (appPremEventPeriodDtoList == null) {
            return;
        }
        for (int i = 0; i < appPremEventPeriodDtoList.size(); i++) {
            for (int j = i + 1; j < appPremEventPeriodDtoList.size() && i != j; j++) {
                String eventName = appPremEventPeriodDtoList.get(i).getEventName();
                String eventName1 = appPremEventPeriodDtoList.get(j).getEventName();
                if (!StringUtil.isEmpty(eventName) && !StringUtil.isEmpty(eventName1)) {
                    if (!eventName.equals(eventName1)) {
                        continue;
                    }
                    Date endDate = appPremEventPeriodDtoList.get(i).getEndDate();
                    Date startDate = appPremEventPeriodDtoList.get(j).getStartDate();
                    if (endDate != null && startDate != null) {
                        if (endDate.after(startDate) || endDate.compareTo(startDate) == 0) {
                            map.put(errorPrefix + j, MessageUtil.getMessageDesc("NEW_ERR0021"));
                        }
                    }
                }
            }
        }
    }

    private static void validateOverlap(List<OperationHoursReloadDto> list, Map<String, String> errorMap, String errorPrefix) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size() && i != j; j++) {
                List<String> selectValList = list.get(i).getSelectValList();
                List<String> selectValList1 = list.get(j).getSelectValList();
                if (selectValList == null || selectValList1 == null) {
                    continue;
                }
                boolean disjoint = Collections.disjoint(selectValList, selectValList1);
                if (disjoint) {
                    continue;
                }
                boolean selectAllDay = list.get(i).isSelectAllDay();
                boolean selectAllDay1 = list.get(j).isSelectAllDay();
                String errMsg = MessageUtil.getMessageDesc("NEW_ERR0021");
                if (selectAllDay || selectAllDay1) {
                    errorMap.put(errorPrefix+ j, errMsg);
                    continue;
                }
                int stime = getTime(list.get(i).getStartFromHH(), list.get(i).getStartFromMM());
                int etime = getTime(list.get(i).getEndToHH(), list.get(i).getEndToMM());
                int stime1 = getTime(list.get(j).getStartFromHH(), list.get(j).getStartFromMM());
                int etime1 = getTime(list.get(j).getEndToHH(), list.get(j).getEndToMM());
                if (stime <= etime1 && etime >= stime1) {
                    errorMap.put(errorPrefix + j, errMsg);
                }
            }
        }
    }

    public static int getTime(String hh, String mm) {
        try {
            int i = Integer.parseInt(hh);
            int i1 = Integer.parseInt(mm);
            return i * 60 + i1;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void doValidateBusiness(AppSubmissionDto appSubmissionDto,List<AppSvcBusinessDto> appSvcBusinessDtos, String appType,
            String licenceId,String currentServiceId, Map<String, String> errorMap) {
        if (appSvcBusinessDtos == null || appSvcBusinessDtos.isEmpty()) {
            return;
        }
        List<AppPremSpecialisedDto> appPremSpecialisedDtoList = appSubmissionDto.getAppPremSpecialisedDtoList().stream()
                .filter(item -> currentServiceId.equals(item.getBaseSvcId()))
                .collect(Collectors.toList());
        for (int i = 0; i < appSvcBusinessDtos.size(); i++) {
            String subfix = "" + i;
            String serviceCode = appSvcBusinessDtos.get(i).getCurrService();
            String premIndexNo = appSvcBusinessDtos.get(i).getPremIndexNo();
            AppPremSpecialisedDto appPremSpecialisedDto = new AppPremSpecialisedDto();
            Optional<AppPremSpecialisedDto> optionalAppPremSpecialisedDto = appPremSpecialisedDtoList.stream().filter(item -> premIndexNo.equals(item.getPremisesVal())).findFirst();
            if (optionalAppPremSpecialisedDto.isPresent()){
                appPremSpecialisedDto = optionalAppPremSpecialisedDto.get();
            }
            boolean match = appPremSpecialisedDto.getCheckedAppPremSubSvcRelDtoList().stream()
                    .map(AppPremSubSvcRelDto::getSvcCode)
                    .anyMatch(AppServicesConsts.SERVICE_CODE_EMERGENCY_DEPARTMENT::equals);
            String businessName = appSvcBusinessDtos.get(i).getBusinessName();
            if (StringUtil.isEmpty(businessName)) {
                errorMap.put("businessName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Business Name", "field"));
            } else {
                if (businessName.length() > 100) {
                    errorMap.put("businessName" + i, repLength("Business Name", "100"));
                }
                int hciNameChanged = 0;
                if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
                    hciNameChanged = checkNameChanged(null, businessName, licenceId);
                }
                if (3 == hciNameChanged || 4 == hciNameChanged) {
                    //no need validate hci name have keyword (is migrated and hci name never changed)
                } else {
                    Map<Integer, String> map = checkBlacklist(businessName);
                    if (!map.isEmpty()) {
                        errorMap.put("businessName" + i, MessageUtil.getMessageDesc("GENERAL_ERR0016"));
                    }
                }
                if (!match && AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(serviceCode) && businessName.toUpperCase().contains(
                        "GENERAL")) {
                    errorMap.put("businessName" + i, MessageUtil.getMessageDesc("GENERAL_ERR0073"));
                }
            }

            String ContactNo = appSvcBusinessDtos.get(i).getContactNo();
            if (StringUtil.isEmpty(ContactNo)) {
                errorMap.put("contactNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Contact No. ", "field"));
            } else if (!StringUtil.isEmpty(ContactNo)) {
                if (ContactNo.length() > 8) {
                    String errorMsg = repLength("Contact No.", "8");
                    errorMap.put("contactNo" + i, errorMsg);
                }
                if (!ContactNo.matches("^[3|689][0-9]{7}$")) {
                    errorMap.put("contactNo" + i, "GENERAL_ERR0007");
                }
            }

            String emailAddr = appSvcBusinessDtos.get(i).getEmailAddr();
            if (StringUtil.isEmpty(emailAddr)) {
                errorMap.put("emailAddr" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Email Address", "field"));
            } else if (!StringUtil.isEmpty(emailAddr)) {
                if (emailAddr.length() > 320) {
                    String errorMsg = repLength("Email Address", "320");
                    errorMap.put("emailAddr" + i, errorMsg);
                }
                if (!ValidationUtils.isEmail(emailAddr)) {
                    errorMap.put("emailAddr" + i, "GENERAL_ERR0014");
                }
            }

            String corporateWebsite = appSvcBusinessDtos.get(i).getCorporateWebsite();
            if (!StringUtil.isEmpty(corporateWebsite)) {
                if (corporateWebsite.length() > 200) {
                    String errorMsg = repLength("Corporate Website", "200");
                    errorMap.put("corporateWebsite" + i, errorMsg);
                }
            }

            if (IaisCommonUtils.isNotEmpty(appSvcBusinessDtos.get(i).getWeeklyDtoList())) {
                validateWeek(appSvcBusinessDtos.get(i), subfix, errorMap);
            }
            if (IaisCommonUtils.isNotEmpty(appSvcBusinessDtos.get(i).getPhDtoList())) {
                validatePh(appSvcBusinessDtos.get(i), subfix, errorMap);

            }
            if (IaisCommonUtils.isNotEmpty(appSvcBusinessDtos.get(i).getEventDtoList())) {
                validateEvent(appSvcBusinessDtos.get(i), subfix, errorMap);
            }
        }
    }

    //event
    private static void validateEvent(AppSvcBusinessDto appSvcBusinessDto, String subfix, Map<String, String> errorMap) {
        List<AppPremEventPeriodDto> eventDtos = appSvcBusinessDto.getEventDtoList();
        String emptyErrMsg = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if (!IaisCommonUtils.isEmpty(eventDtos)) {
            int j = 0;
            for (AppPremEventPeriodDto eventDto : eventDtos) {
                String eventName = eventDto.getEventName();
                Date startDate = eventDto.getStartDate();
                Date endDate = eventDto.getEndDate();
                if (!StringUtil.isEmpty(eventName) || startDate != null || endDate != null) {
                    boolean dateIsEmpty = false;
                    if (StringUtil.isEmpty(eventName)) {
                        errorMap.put(subfix + "onSiteEvent" + j, emptyErrMsg);
                    } else if (eventName.length() > 100) {
                        errorMap.put(subfix + "onSiteEvent" + j, repLength("Event Name", "100"));
                    }
                    if (startDate == null) {
                        errorMap.put(subfix + "onSiteEventStart" + j, emptyErrMsg);
                        dateIsEmpty = true;
                    }
                    if (endDate == null) {
                        errorMap.put(subfix + "onSiteEventEnd" + j, emptyErrMsg);
                        dateIsEmpty = true;
                    }
                    if (!dateIsEmpty) {
                        if (startDate.after(endDate)) {
                            errorMap.put(subfix + "onSiteEventDate" + j, MessageUtil.getMessageDesc("NEW_ERR0020"));
                        }
                    }
                }
                j++;
            }
            validateOverlapEvent(eventDtos, errorMap, subfix + "onSiteEvent");
        }
    }

    //ph
    private static void validatePh(AppSvcBusinessDto appSvcBusinessDto, String subfix, Map<String, String> errorMap) {
        List<OperationHoursReloadDto> phDtos = appSvcBusinessDto.getPhDtoList();
        if (!IaisCommonUtils.isEmpty(phDtos)) {
            int j = 0;
            for (OperationHoursReloadDto phDto : phDtos) {
                Map<String, String> errNameMap = IaisCommonUtils.genNewHashMap();
                errNameMap.put("select", subfix + "onSitePubHoliday");
                errNameMap.put("start", subfix + "onSitePhStart");
                errNameMap.put("end", subfix + "onSitePhEnd");
                errNameMap.put("time", subfix + "onSitePhTime");
                doOperationHoursValidate(phDto, errorMap, errNameMap, j + "", false);
                j++;
            }
            validateOverlap(phDtos, errorMap, subfix + "onSitePubHoliday");
        }
    }

    //weekly
    private static void validateWeek(AppSvcBusinessDto appSvcBusinessDto, String subfix, Map<String, String> errorMap) {
        List<OperationHoursReloadDto> weeklyDtos = appSvcBusinessDto.getWeeklyDtoList();
        String emptyErrMsg = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if (IaisCommonUtils.isEmpty(weeklyDtos)) {
            errorMap.put(subfix + "onSiteWeekly" + 0, emptyErrMsg);
            errorMap.put(subfix + "onSiteWeeklyStart" + 0, emptyErrMsg);
            errorMap.put(subfix + "onSiteWeeklyEnd" + 0, emptyErrMsg);
        } else {
            int j = 0;
            for (OperationHoursReloadDto weeklyDto : weeklyDtos) {
                Map<String, String> errNameMap = IaisCommonUtils.genNewHashMap();
                errNameMap.put("select", subfix + "onSiteWeekly");
                errNameMap.put("start", subfix + "onSiteWeeklyStart");
                errNameMap.put("end", subfix + "onSiteWeeklyEnd");
                errNameMap.put("time", subfix + "onSiteWeeklyTime");
                doOperationHoursValidate(weeklyDto, errorMap, errNameMap, j + "", true);
                j++;
            }
            validateOverlap(weeklyDtos, errorMap, subfix + "onSiteWeekly");
        }
    }

    private static void doOperationHoursValidate(OperationHoursReloadDto operationHoursReloadDto, Map<String, String> errorMap,
            Map<String, String> errNameMap, String count, boolean isMandatory) {
        boolean isEmpty = false;
        String emptyErrMsg = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        boolean selectAllDay = operationHoursReloadDto.isSelectAllDay();
        String selectVal = operationHoursReloadDto.getSelectVal();
        String startHH = operationHoursReloadDto.getStartFromHH();
        String startMM = operationHoursReloadDto.getStartFromMM();
        String endHH = operationHoursReloadDto.getEndToHH();
        String endMM = operationHoursReloadDto.getEndToMM();
        if (!isMandatory) {
            if (StringUtil.isEmpty(selectVal) &&
                    StringUtil.isEmpty(startHH) &&
                    StringUtil.isEmpty(startMM) &&
                    StringUtil.isEmpty(endHH) &&
                    StringUtil.isEmpty(endMM) && !selectAllDay) {
                return;
            }
        }
        if (StringUtil.isEmpty(selectVal)) {
            errorMap.put(errNameMap.get("select") + count, emptyErrMsg);
        }
        if (selectAllDay) {
            Time time = Time.valueOf(LocalTime.of(0, 0, 0));
            operationHoursReloadDto.setStartFrom(time);
            operationHoursReloadDto.setEndTo(time);
        } else {
            if (StringUtil.isEmpty(startHH) || StringUtil.isEmpty(startMM)) {
                errorMap.put(errNameMap.get("start") + count, emptyErrMsg);
                isEmpty = true;
            }
            if (StringUtil.isEmpty(endHH) || StringUtil.isEmpty(endMM)) {
                errorMap.put(errNameMap.get("end") + count, emptyErrMsg);
                isEmpty = true;
            }

            if (!isEmpty) {
                LocalTime startTime = LocalTime.of(Integer.parseInt(startHH), Integer.parseInt(startMM));
                operationHoursReloadDto.setStartFrom(Time.valueOf(startTime));
                LocalTime endTime = LocalTime.of(Integer.parseInt(endHH), Integer.parseInt(endMM));
                operationHoursReloadDto.setEndTo(Time.valueOf(endTime));
                //compare
                if (startTime.isAfter(endTime)) {
                    errorMap.put(errNameMap.get("time") + count, MessageUtil.getMessageDesc("NEW_ERR0015"));
                } else if (startTime.equals(endTime)) {
                    errorMap.put(errNameMap.get("time") + count, MessageUtil.getMessageDesc("NEW_ERR0019"));
                }

            }
        }
    }

    public static Map<String, String> doValidateCharges(AppSvcChargesPageDto appSvcClinicalDirectorDto){
        if (appSvcClinicalDirectorDto == null) {
            return new HashMap<>(1);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<AppSvcChargesDto> generalChargesDtos = appSvcClinicalDirectorDto.getGeneralChargesDtos();
        errorMap.putAll(doValidateGeneralCharges(generalChargesDtos));
        List<AppSvcChargesDto> otherChargesDtos = appSvcClinicalDirectorDto.getOtherChargesDtos();
        errorMap.putAll(doValidateOtherCharges(otherChargesDtos));
        return errorMap;
    }

    private static Map<String, String> doValidateGeneralCharges(List<AppSvcChargesDto> generalChargesDtos){
        if(generalChargesDtos == null || generalChargesDtos.isEmpty()){
            return new HashMap<>(1);
        }
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        for(int i=0;i< generalChargesDtos.size() ; i++){
            String chargesType = generalChargesDtos.get(i).getChargesType();
            if(StringUtil.isEmpty(chargesType)){
                map.put("chargesType"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of Charge", "field"));
            }
            boolean flag=false;
            String minAmount = generalChargesDtos.get(i).getMinAmount();
            if(StringUtil.isEmpty(minAmount)){
                map.put("minAmount"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "this", "field"));
            }else {
                if(minAmount.length()>4){
                    String general_err0041= repLength("Amount","4");
                    map.put("minAmount"+i,general_err0041);
                }else if(!minAmount.matches("^[0-9]+$")){
                    map.put("minAmount"+i,"GENERAL_ERR0002");
                }else {
                    flag=true;
                }

            }
            String maxAmount = generalChargesDtos.get(i).getMaxAmount();
            if(!StringUtil.isEmpty(maxAmount)){
                if(maxAmount.length() > 4){
                    String general_err0041= repLength("Amount","4");
                    map.put("maxAmount"+i,general_err0041);
                }else if(!maxAmount.matches("^[0-9]+$")){
                    map.put("maxAmount"+i,"GENERAL_ERR0002");
                }else if(flag) {
                    int min = Integer.parseInt(minAmount);
                    int max = Integer.parseInt(maxAmount);
                    if(min> max){
                        map.put("maxAmount"+i,MessageUtil.getMessageDesc("NEW_ERR0027"));
                    }
                }
            }
            String remarks = generalChargesDtos.get(i).getRemarks();
            if(!StringUtil.isEmpty(remarks)&&remarks.length() > 150){
                String general_err0041= repLength("Remarks","150");
                map.put("remarks"+i,general_err0041);
            }
        }
        return map;
    }

    private static Map<String, String> doValidateOtherCharges(List<AppSvcChargesDto> otherChargesDtos){
        if(otherChargesDtos == null || otherChargesDtos.isEmpty()){
            return new HashMap<>(1);
        }
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        for(int i=0;i < otherChargesDtos.size();i++){
            String chargesType = otherChargesDtos.get(i).getChargesType();
            if(StringUtil.isEmpty(chargesType)){
                map.put("otherChargesType"+i,MessageUtil.replaceMessage("GENERAL_ERR0006", "Category", "field"));
            }
            String chargesCategory = otherChargesDtos.get(i).getChargesCategory();
            if(StringUtil.isEmpty(chargesCategory)){
                map.put("otherChargesCategory"+i,MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of Charge", "field"));
            }
            boolean flag=false;
            String minAmount = otherChargesDtos.get(i).getMinAmount();
            String errMsg=MessageUtil.replaceMessage("GENERAL_ERR0006", "this", "field");
            if(StringUtil.isEmpty(minAmount)){
                map.put("otherAmountMin"+i,errMsg);
            }else {
                if(minAmount.length()>4){
                    String general_err0041= repLength("Amount","4");
                    map.put("otherAmountMin"+i,general_err0041);
                }else if(!minAmount.matches("^[0-9]+$")){
                    map.put("otherAmountMin"+i,"GENERAL_ERR0002");
                }else {
                    flag=true;
                }
            }
            String maxAmount = otherChargesDtos.get(i).getMaxAmount();
            if(!StringUtil.isEmpty(maxAmount)){
                if(maxAmount.length()>4){
                    String general_err0041= repLength("Amount","4");
                    map.put("otherAmountMax"+i,general_err0041);
                }else if(!maxAmount.matches("^[0-9]+$")){
                    map.put("otherAmountMax"+i,"GENERAL_ERR0002");
                }else if(flag){
                    int min = Integer.parseInt(minAmount);
                    int max = Integer.parseInt(maxAmount);
                    if(min > max){
                        map.put("otherAmountMax"+i,MessageUtil.getMessageDesc("NEW_ERR0027"));
                    }
                }
            }
            String remarks = otherChargesDtos.get(i).getRemarks();
            if(!StringUtil.isEmpty(remarks) && remarks .length() >150){
                String general_err0041= repLength("Remarks","150");
                map.put("otherRemarks"+i,general_err0041);
            }
        }
        return map;
    }

    public static Map<String, String> doValidationOutsourced(AppSvcOutsouredDto appSvcOutsouredDto, String curAt) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if (appSvcOutsouredDto == null) {
            return new HashMap<>(1);
        }
        List<String> svcCodeList = appSvcOutsouredDto.getSvcCodeList();
        SystemParamConfig systemParamConfig = getSystemParamConfig();
        int clbMaxCount = systemParamConfig.getOutsourceAddClbMaxCount();
        int rdsMaxCount = systemParamConfig.getOutsourceAddRlbMaxCount();
        SearchParam searchParam = appSvcOutsouredDto.getSearchParam();
        if ("search".equals(curAt)) {
            if (svcCodeList.contains(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY)
                    && svcCodeList.contains(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES)){
                return errMap;
            }
            String serviceMandatory = MessageUtil.replaceMessage("GENERAL_ERR0006",
                    "Service", "field");
            if (searchParam != null && !searchParam.getFilters().isEmpty()) {
                if (searchParam.getFilters().get("svcName") == null) {
                    errMap.put("serviceCode", serviceMandatory);
                }
                if (StringUtil.isNotEmpty(appSvcOutsouredDto.getSearchSvcName())){
                    searchParam.addFilter("svcName",appSvcOutsouredDto.getSearchSvcName(),true);
                }
            } else if (searchParam == null || searchParam.getFilters().isEmpty()) {
                errMap.put("serviceCode", serviceMandatory);
            }
        }else if ("add".equals(curAt)) {
            AppPremGroupOutsourcedDto appPremGroupOutsourcedDto = appSvcOutsouredDto.getSearchOutsourced();
            if (appPremGroupOutsourcedDto != null) {
                if (appPremGroupOutsourcedDto.getAppPremOutSourceLicenceDto() != null){
                    String prefix = appPremGroupOutsourcedDto.getAppPremOutSourceLicenceDto().getId();
                    String startDate = appPremGroupOutsourcedDto.getStartDateStr();
                    if (StringUtil.isEmpty(startDate)) {
                        errMap.put(prefix + "agreementStartDate", MessageUtil.replaceMessage("GENERAL_ERR0006",
                                "Date of Agreement", "field"));
                    }
                    String endDate = appPremGroupOutsourcedDto.getEndDateStr();
                    if (StringUtil.isEmpty(endDate)) {
                        errMap.put(prefix + "agreementEndDate", MessageUtil.replaceMessage("GENERAL_ERR0006",
                                "End Date of Agreement", "field"));
                    }
                    String outstandingScope = appPremGroupOutsourcedDto.getAppPremOutSourceLicenceDto().getOutstandingScope();
                    if (StringUtil.isEmpty(outstandingScope)) {
                        errMap.put(prefix + "outstandingScope", MessageUtil.replaceMessage("GENERAL_ERR0006",
                                "Scope of Outsourcing", "field"));
                    } else if (outstandingScope.length() > 3000){
                        String errorMsg = repLength("Scope of Outsourcing", "3000");
                        errMap.put(prefix + "outstandingScope", errorMsg);
                    }
                    if (StringUtil.isNotEmpty(startDate) && StringUtil.isNotEmpty(endDate)) {
                        try {
                            if (Formatter.parseDate(startDate).after(Formatter.parseDate(endDate))) {
                                errMap.put(prefix + "agreementStartDate", MessageUtil.replaceMessage("NEW_ERR0037",
                                        "Date of Agreement", "field"));
                            }
                        } catch (ParseException e) {
                            log.info(StringUtil.changeForLog(e.getMessage()), e);
                        }
                    }
                }
            }
        }else {
            errMap.putAll(doValidateAppSvcOutsource(curAt, appSvcOutsouredDto, searchParam, svcCodeList));
        }
        errMap.putAll(doValidateAppSvcOutsourceAddMaxCount(appSvcOutsouredDto.getClinicalLaboratoryList(), clbMaxCount , AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY));
        errMap.putAll(doValidateAppSvcOutsourceAddMaxCount(appSvcOutsouredDto.getRadiologicalServiceList(), rdsMaxCount, AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES));
        return errMap;
    }

    private static Map<String, String> doValidateAppSvcOutsource(String curAt, AppSvcOutsouredDto appSvcOutsouredDto ,SearchParam searchParam, List<String> svcCodeList){
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();

        if (IaisCommonUtils.isNotEmpty(svcCodeList)){
            if (!StringUtil.isIn(curAt, new String[]{"delete","sort","changePage"})){
                if (svcCodeList.contains(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY)
                        && !svcCodeList.contains(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES)){
                    //hcsaService or Bundle checked clb
                    doValidateOutsourcedDto(appSvcOutsouredDto, errMap, null, AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES, searchParam);
                } else if (svcCodeList.contains(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES)
                        && !svcCodeList.contains(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY)){
                    //hcsaService or Bundle checked rds
                    doValidateOutsourcedDto(appSvcOutsouredDto, errMap, AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY, null, searchParam);
                } else if (!svcCodeList.contains(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY)
                        && !svcCodeList.contains(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES)){
                    doValidateOutsourcedDto(appSvcOutsouredDto, errMap, AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY, AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES, searchParam);
                }
            }
        }
        return errMap;
    }

    private static void doValidateOutsourcedDto(AppSvcOutsouredDto appSvcOutsouredDto,Map<String, String> errMap,
                                                String clbType, String rdsType, SearchParam searchParam){
        String rsMandatory = MessageUtil.replaceMessage("GENERAL_ERR0006",
                "Radiological Service", "field");
        String clbMandatory = MessageUtil.replaceMessage("GENERAL_ERR0006",
                "Clinical Laboratory Service", "field");
        //clbList
        if (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(clbType)
                && IaisCommonUtils.isEmpty(appSvcOutsouredDto.getClinicalLaboratoryList())){
            errMap.put("clbList", clbMandatory);
            if (searchParam == null && StringUtil.isEmpty(rdsType)){
                errMap.put("initOutsource", clbMandatory);
            }
        }

        //rdsList
        if (AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(rdsType)
                && IaisCommonUtils.isEmpty(appSvcOutsouredDto.getRadiologicalServiceList())) {
            errMap.put("rdsList", rsMandatory);
            if (searchParam == null && StringUtil.isEmpty(clbType)) {
                errMap.put("initOutsource", rsMandatory);
            }
        }
        if (searchParam == null){
            if (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(clbType)
                    && AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(rdsType)
                    && IaisCommonUtils.isEmpty(appSvcOutsouredDto.getRadiologicalServiceList())
                    && IaisCommonUtils.isEmpty(appSvcOutsouredDto.getClinicalLaboratoryList())){
                errMap.put("initOutsource", MessageUtil.replaceMessage("GENERAL_ERR0006",
                        "Clinical Laboratory Service and Radiological Service", "field"));
            }
        }
    }

    private static Map<String, String> doValidateAppSvcOutsourceAddMaxCount(List<AppPremGroupOutsourcedDto> appPremGroupOutsourcedDtoList, int maxCount, String type){
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if (IaisCommonUtils.isNotEmpty(appPremGroupOutsourcedDtoList)){
            if (appPremGroupOutsourcedDtoList.size() > maxCount){
                if (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(type)){
                    errMap.put("clbList", replaceOutsourceMaxMsg("Clinical Laboratory Service"));
                }
                if (AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(type)){
                    errMap.put("rdsList", replaceOutsourceMaxMsg("Radiological Service"));
                }
            }
        }
        return errMap;
    }

    private static String replaceOutsourceMaxMsg(String replaceDataString){
        String msg = MessageUtil.getMessageDesc("GENERAL_ERR0078");
        if (StringUtil.isEmpty(msg)){
            return "GENERAL_ERR0078";
        }else if (msg.contains("{"+ "data" +"}") && msg.contains("{"+ "count" +"}")){
            msg = msg.replace("{"+ "data" +"}",replaceDataString);
            msg = msg.replace("{"+ "count" +"}", "5");
            return msg;
        }else {
            return msg;
        }
    }
    public static Map<String, String> doValidateOtherInformation(List<AppSvcOtherInfoDto> appSvcOtherInfoDto, String currCode) {
        if (appSvcOtherInfoDto == null) {
            return new HashMap<>(1);
        }
        return doValidateOtherInfo(appSvcOtherInfoDto, currCode);
    }


    public static Map<String, String> doValidateOtherInfo(List<AppSvcOtherInfoDto> appSvcOtherInfoDto, String currCode) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        for (AppSvcOtherInfoDto svcOtherInfoDto : appSvcOtherInfoDto) {
            if (svcOtherInfoDto != null){
                String prefix = svcOtherInfoDto.getPremisesVal();
                doValidateAppSvcOtherInfo(currCode, errMap, svcOtherInfoDto, prefix);
            }
        }
        return errMap;
    }

    private static void doValidateAppSvcOtherInfo(String currCode, Map<String, String> errMap, AppSvcOtherInfoDto svcOtherInfoDto, String prefix) {
        if (StringUtil.isIn(currCode, new String[]{AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE,
                AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE})) {
            AppSvcOtherInfoMedDto appSvcOtherInfoMedDto = svcOtherInfoDto.getAppSvcOtherInfoMedDto();
            validateOtherInfoDsAndMts(errMap, svcOtherInfoDto, prefix, appSvcOtherInfoMedDto);
        }
        if (AppServicesConsts.SERVICE_CODE_RENAL_DIALYSIS_CENTRE.equals(currCode)) {
            AppSvcOtherInfoNurseDto appSvcOtherInfoNurseDto = svcOtherInfoDto.getAppSvcOtherInfoNurseDto();
            validateOtherInfoRdc(errMap, prefix, appSvcOtherInfoNurseDto);
        }
        if (AppServicesConsts.SERVICE_CODE_AMBULATORY_SURGICAL_CENTRE.equals(currCode)) {
            AppSvcOtherInfoMedDto ambulatorySurgicalCentre = svcOtherInfoDto.getOtherInfoMedAmbulatorySurgicalCentre();
            validateOtherInfoASC(errMap, svcOtherInfoDto, prefix, ambulatorySurgicalCentre);
        }
        if (StringUtil.isIn(currCode, new String[]{AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL,
                AppServicesConsts.SERVICE_CODE_AMBULATORY_SURGICAL_CENTRE,
                AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE})) {
            AppSvcOtherInfoTopDto appSvcOtherInfoTopDto = svcOtherInfoDto.getAppSvcOtherInfoTopDto();
            validateOtherInfoAllTop(errMap, svcOtherInfoDto, prefix, appSvcOtherInfoTopDto);
        }
        if (StringUtil.isIn(currCode, new String[]{AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL,
                AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL,
                AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE})) {
            validateOtherInfoYFV(errMap, svcOtherInfoDto, prefix);
        }
    }

    private static void validateOtherInfoAllTop(Map<String, String> errMap, AppSvcOtherInfoDto svcOtherInfoDto, String prefix, AppSvcOtherInfoTopDto appSvcOtherInfoTopDto) {
        if (StringUtil.isEmpty(svcOtherInfoDto.getProvideTop())) {
            validateAppSvcOtherInfoDto(errMap, prefix, "provideTop", "Please indicate ");
        } else if (AppConsts.YES.equals(svcOtherInfoDto.getProvideTop())) {
            validateOtherInfoTop(errMap, svcOtherInfoDto, prefix, appSvcOtherInfoTopDto);
            String declaration = svcOtherInfoDto.getDeclaration();
            if (StringUtil.isEmpty(declaration)){
                errMap.put(prefix + "declaration", MessageUtil.replaceMessage("GENERAL_ERR0006", "Declaration", "field"));
            }
            errMap.putAll(getValidateAppSvcOtherInfoTopPerson(svcOtherInfoDto, prefix));
            errMap.putAll(doValidateSupplementaryForm(svcOtherInfoDto.getAppSvcSuplmFormDto(),
                    svcOtherInfoDto.getPremisesVal()));
        }
    }

    private static void validateAppSvcOtherInfoDto(Map<String, String> errMap, String prefix, String type, String field){
        errMap.put(prefix + type,
                MessageUtil.replaceMessage("GENERAL_ERR0006", field, "field"));
    }

    private static void validateOtherMathAndMaxLength(Map<String, String> errMap, String prefix,
                                                      String value,String type, String filed,
                                                      int maxLength){
        if (StringUtil.isEmpty(value)){
            validateAppSvcOtherInfoDto(errMap, prefix, type, filed);
        }else if (value.length() > maxLength){
            String errorMsg = repLength(filed, String.valueOf(maxLength));
            errMap.put(prefix + type , errorMsg);
        }else if (!StringUtil.isDigit(value) || value.matches("^-[0-9]*[1-9][0-9]*$")){
            errMap.put(prefix + type, MessageUtil.replaceMessage("GENERAL_ERR0002", filed, "field"));
        }
    }

    private static void validateOtherTopMathAndMaxLength(Map<String, String> errMap, String prefix,
                                                      String value,String type, String filed,
                                                      int maxLength,int i){
        if (StringUtil.isEmpty(value)){
            errMap.put(prefix + type + i,
                    MessageUtil.replaceMessage("GENERAL_ERR0006", filed, "field"));
        }else if (value.length() > maxLength){
            String errorMsg = repLength(filed, String.valueOf(maxLength));
            errMap.put(prefix + type + i , errorMsg);
        }
    }

    private static void validateOtherInfoRdc(Map<String, String> errMap, String prefix, AppSvcOtherInfoNurseDto appSvcOtherInfoNurseDto) {
        if (appSvcOtherInfoNurseDto != null) {
            String perShiftNum = appSvcOtherInfoNurseDto.getPerShiftNum();
            validateOtherMathAndMaxLength(errMap, prefix, perShiftNum, "perShiftNum","Nurses per Shift",2);

            String dialysisStationsNum = appSvcOtherInfoNurseDto.getDialysisStationsNum();
            validateOtherMathAndMaxLength(errMap, prefix, dialysisStationsNum, "dialysisStationsNum","Total number of dialysis stations",2);

            String helpBStationNum = appSvcOtherInfoNurseDto.getHelpBStationNum();
            validateOtherMathAndMaxLength(errMap, prefix, helpBStationNum, "helpBStationNum","Number of Hep B stations",2);
            if (appSvcOtherInfoNurseDto.getOpenToPublic() == null) {
                validateAppSvcOtherInfoDto(errMap, prefix, "nisOpenToPublic", "Is the clinic open to general public?");
            }
        }else {
            errMap.put(prefix + "perShiftNum",
                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Nurses per Shift", "field"));
            errMap.put(prefix + "helpBStationNum",
                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Number of Hep B stations", "field"));
            errMap.put(prefix + "dialysisStationsNum",
                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Total number of dialysis stations", "field"));
            errMap.put(prefix + "nisOpenToPublic",
                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Is the clinic open to general public?", "field"));
        }
    }

    private static void validateOtherInfoDsAndMts(Map<String, String> errMap, AppSvcOtherInfoDto svcOtherInfoDto, String prefix, AppSvcOtherInfoMedDto appSvcOtherInfoMedDto) {
        if (appSvcOtherInfoMedDto != null) {
            if (!appSvcOtherInfoMedDto.getMedicalTypeIt() && !appSvcOtherInfoMedDto.getMedicalTypePaper()) {
                validateAppSvcOtherInfoDto(errMap, prefix, "medicalTypeIt", "Type of medical records");
            }
            if (appSvcOtherInfoMedDto.getOpenToPublic() == null) {
                validateAppSvcOtherInfoDto(errMap, prefix, "openToPublic", "Is clinic open to general public?");
            }
            String gfaValue = appSvcOtherInfoMedDto.getGfaValue();
            if (StringUtil.isEmpty(gfaValue)) {
                validateAppSvcOtherInfoDto(errMap, prefix, "gfaValue", "GFA Value (in sqm)");
            } else if (!StringUtil.isDigit(gfaValue)) {
                errMap.put(prefix + "gfaValue", MessageUtil.replaceMessage("GENERAL_ERR0002", "GFA Value (in sqm)", "field"));
            } else if (gfaValue.length() > 7){
                String errorMsg = repLength("GFA Value (in sqm)", "7");
                errMap.put(prefix + "gfaValue" , errorMsg);
            } else if (Integer.parseInt(gfaValue) > 3000 || gfaValue.matches("^((-\\d+)|(0+))$")){
                errMap.put(prefix + "gfaValue" , replaceGFAMinAndMax());
            }
            validateCheckMedicalTypeIt(errMap, prefix, appSvcOtherInfoMedDto);
        }else {
            errMap.put(prefix + "medicalTypeIt",
                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of medical records", "field"));
            errMap.put(prefix + "gfaValue", MessageUtil.replaceMessage("GENERAL_ERR0006", "GFA Value (in sqm)", "field"));
            errMap.put(prefix + "openToPublic",
                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Is clinic open to general public?", "field"));
        }
        if (StringUtil.isEmpty(svcOtherInfoDto.getDsDeclaration())) {
            errMap.put(prefix + "dsDeclaration", MessageUtil.replaceMessage("GENERAL_ERR0006",
                    "I declare that I have met URA's requirements for gross floor area", "field"));

        }
    }

    private static void validateCheckMedicalTypeIt(Map<String, String> errMap, String prefix, AppSvcOtherInfoMedDto appSvcOtherInfoMedDto) {
        if (appSvcOtherInfoMedDto.getMedicalTypeIt()){
            if (StringUtil.isEmpty(appSvcOtherInfoMedDto.getSystemOption())) {
                errMap.put(prefix + "systemOption", MessageUtil.replaceMessage("GENERAL_ERR0006",
                        "List of options for IT system and paper cards / IT system only", "field"));
            }else if ("MED06".equals(appSvcOtherInfoMedDto.getSystemOption())) {
                String otherSystemOption = appSvcOtherInfoMedDto.getOtherSystemOption();
                if (StringUtil.isEmpty(otherSystemOption)) {
                    errMap.put(prefix + "otherSystemOption",
                            MessageUtil.replaceMessage("GENERAL_ERR0006", "Please specify", "field"));
                }else if (otherSystemOption.length() > 50){
                    String errorMsg = repLength("Please specify", "50");
                    errMap.put(prefix + "otherSystemOption" , errorMsg);
                }
            }
        }
    }

    private static void validateOtherInfoASC(Map<String, String> errMap, AppSvcOtherInfoDto svcOtherInfoDto,
                                             String prefix, AppSvcOtherInfoMedDto ambulatorySurgicalCentre) {
        if (ambulatorySurgicalCentre != null) {
            String agfaValue = ambulatorySurgicalCentre.getGfaValue();
            if (StringUtil.isEmpty(agfaValue)) {
                errMap.put(prefix + "agfaValue", MessageUtil.replaceMessage("GENERAL_ERR0006", "GFA Value (in sqm)", "field"));
            } else if (!StringUtil.isDigit(agfaValue)) {
                errMap.put(prefix + "agfaValue", MessageUtil.replaceMessage("GENERAL_ERR0002", "GFA Value (in sqm)", "field"));
            } else if (agfaValue.length() > 7 ){
                String errorMsg = repLength("GFA Value (in sqm)", "7");
                errMap.put(prefix + "agfaValue" , errorMsg);
            } else if (agfaValue.matches("^((-\\d+)|(0+))$") || Integer.parseInt(agfaValue) > 3000){//DS_ERR003
                errMap.put(prefix + "agfaValue" , replaceGFAMinAndMax());
            }
        }else {
            errMap.put(prefix + "agfaValue", MessageUtil.replaceMessage("GENERAL_ERR0006", "GFA Value (in sqm)", "field"));
        }
        if (StringUtil.isEmpty(svcOtherInfoDto.getAscsDeclaration())) {
            errMap.put(prefix + "ascsDeclaration", MessageUtil.replaceMessage("GENERAL_ERR0006",
                    "I declare that I have met URA's requirements for gross floor area", "field"));
        }
    }

    private static void validateOtherInfoYFV(Map<String, String> errMap, AppSvcOtherInfoDto svcOtherInfoDto, String prefix) {
        if (StringUtil.isEmpty(svcOtherInfoDto.getProvideYfVs())) {
            errMap.put(prefix + "provideYfVs", MessageUtil.replaceMessage("GENERAL_ERR0006",
                    "Do you provide Yellow Fever Vaccination Service",
                    "field"));
        }
        if (AppConsts.YES.equals(svcOtherInfoDto.getProvideYfVs())) {
            if (StringUtil.isEmpty(svcOtherInfoDto.getYfCommencementDateStr())) {
                errMap.put(prefix + "yfCommencementDate", MessageUtil.replaceMessage("GENERAL_ERR0006",
                        "Date of Commencement",
                        "field"));
            }
        }
    }

    private static void validateOtherInfoTop(Map<String, String> errMap, AppSvcOtherInfoDto svcOtherInfoDto, String prefix, AppSvcOtherInfoTopDto appSvcOtherInfoTopDto) {
        if (appSvcOtherInfoTopDto != null) {
            String topType = svcOtherInfoDto.getAppSvcOtherInfoTopDto().getTopType();
            if (StringUtil.isEmpty(topType)) {
                validateAppSvcOtherInfoDto(errMap,prefix,"topType","Please indicate ");
            }
            if (appSvcOtherInfoTopDto.getHasConsuAttendCourse() == null) {
                validateAppSvcOtherInfoDto(errMap,prefix,"hasConsuAttendCourse",
                        "My counsellor(s) has attended the TOP counselling refresher course (Please upload the certificates in the document page)");
            }
            if (appSvcOtherInfoTopDto.getProvideHpb() == null) {
                validateAppSvcOtherInfoDto(errMap,prefix,"provideHpb",
                        "The service provider has the necessary counselling facilities e.g. TV set, video player, video on abortion produced by HPB in different languages and the pamphlets produced by HPB");
            }
            if (appSvcOtherInfoTopDto.getOutcomeProcRecord() == null) {
                validateAppSvcOtherInfoDto(errMap,prefix,"outcomeProcRecord","Outcome of procedures are recorded");
            }
            String compCaseNum = svcOtherInfoDto.getAppSvcOtherInfoTopDto().getCompCaseNum();
            validateOtherMathAndMaxLength(errMap,prefix,compCaseNum,"compCaseNum","Number of cases with complications, if any",100);
            errMap.putAll(getValidateAppSvcOtherInfoTopAbort(svcOtherInfoDto, topType, prefix));
        }else {
            errMap.put(prefix + "topType",
                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Please indicate ", "field"));
            errMap.put(prefix + "hasConsuAttendCourse", MessageUtil.replaceMessage("GENERAL_ERR0006",
                    "My counsellor(s) has attended the TOP counselling refresher course (Please upload the certificates in the document page)",
                    "field"));
            errMap.put(prefix + "provideHpb", MessageUtil.replaceMessage("GENERAL_ERR0006",
                    "The service provider has the necessary counselling facilities e.g. TV set, video player, video on abortion produced by HPB in different languages and the pamphlets produced by HPB",
                    "field"));
            errMap.put(prefix + "outcomeProcRecord", MessageUtil.replaceMessage("GENERAL_ERR0006",
                    "Outcome of procedures are recorded",
                    "field"));
            errMap.put(prefix + "compCaseNum", MessageUtil.replaceMessage("GENERAL_ERR0006",
                    "Number of cases with complications, if any",
                    "field"));

        }
    }

    private static String replaceGFAMinAndMax(){
        String msg = MessageUtil.getMessageDesc("DS_ERR003");
        if (StringUtil.isEmpty(msg)){
            return "DS_ERR003";
        }else if (msg.contains("{"+ "field" +"}") && msg.contains("{"+ "minNum" +"}") && msg.contains("{"+ "maxNum" +"}")){
            msg = msg.replace("{"+ "field" +"}", "GFA Value (in sqm)");
            msg = msg.replace("{"+ "minNum" +"}", "1");
            msg = msg.replace("{"+ "maxNum" +"}", "3000");
            return msg;
        }else {
            return msg;
        }
    }

    public static Map<String, String> getValidateAppSvcOtherInfoTopPerson(AppSvcOtherInfoDto appSvcOtherInfoDto, String prefix) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        List<AppSvcOtherInfoTopPersonDto> practitioners = appSvcOtherInfoDto.getOtherInfoTopPersonPractitionersList();
        List<AppSvcOtherInfoTopPersonDto> anaesthetists = appSvcOtherInfoDto.getOtherInfoTopPersonAnaesthetistsList();
        List<AppSvcOtherInfoTopPersonDto> nurses = appSvcOtherInfoDto.getOtherInfoTopPersonNursesList();
        List<AppSvcOtherInfoTopPersonDto> counsellors = appSvcOtherInfoDto.getOtherInfoTopPersonCounsellorsList();
        List<String> idNoList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < practitioners.size(); i++) {
            if (practitioners.get(i) == null){
                errMap.put(prefix + "medAuthByMoh" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                        "Is the medical practitioners authorised by MOH to perform Abortion\n" +
                                "                (if No, please upload a copy of the Obstetrics & Gynaecology certificate and From 2 at the Document page)",
                        "field"));
                errMap.put(prefix + "name" + i,
                        MessageUtil.replaceMessage("GENERAL_ERR0006", "Name of medical practitioner", "field"));
                errMap.put(prefix + "profRegNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Professional Regn. No.", "field"));
                errMap.put(prefix + "idNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "NRIC/FIN No.", "field"));
                errMap.put(prefix + "regType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of Registration", "field"));
                errMap.put(prefix + "qualification" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Qualifications", "field"));
                errMap.put(prefix + "speciality" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Specialties", "field"));
            }else {
                String name = practitioners.get(i).getName();
                String profRegNo = practitioners.get(i).getProfRegNo();
                String idNo = practitioners.get(i).getIdNo();
                String regType = practitioners.get(i).getRegType();
                String qualification = practitioners.get(i).getQualification();
                String specialties = practitioners.get(i).getSpeciality();

                if (practitioners.get(i).getMedAuthByMoh() == null) {
                    errMap.put(prefix + "medAuthByMoh" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                            "Is the medical practitioners authorised by MOH to perform Abortion\n" +
                                    "                (if No, please upload a copy of the Obstetrics & Gynaecology certificate and From 2 at the Document page)",
                            "field"));
                }

                if (StringUtil.isEmpty(profRegNo)) {
                    errMap.put(prefix + "profRegNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Professional Regn. No.", "field"));
                }else {
                    validateProfRegNo(errMap,profRegNo,prefix + "profRegNo" + i);
                }
                if (StringUtil.isEmpty(idNo)) {
                    errMap.put(prefix + "idNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "NRIC/FIN No.", "field"));
                } else if (doValidationOtherInfoIndoList(idNoList, idNo)) {
                    errMap.put(prefix + "idNo" + i, "NEW_ERR0012");
                }else {
                    errMap.putAll(doValidationOtherInfoTopPersonNric(idNo,i,prefix,"idNo"));
                }
                idNoList.add(idNo);
                validateOtherTopMathAndMaxLength(errMap,prefix,regType,"regType","Type of Registration",66,i);
                validateOtherTopMathAndMaxLength(errMap,prefix,name,"name","Name of medical practitioner",100,i);
                validateOtherTopMathAndMaxLength(errMap,prefix,qualification,"qualification","Qualifications",100,i);
                validateOtherTopMathAndMaxLength(errMap,prefix,specialties,"speciality","Specialties",100,i);
            }
        }

        List<String> aidNoList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < anaesthetists.size(); i++) {
            if (anaesthetists.get(i) == null){
                errMap.put(prefix + "aname" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name of anaesthetists", "field"));
                errMap.put(prefix + "aprofRegNo" + i,
                        MessageUtil.replaceMessage("GENERAL_ERR0006", "Professional Regn. No.", "field"));
                errMap.put(prefix + "idANo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "NRIC/FIN No.", "field"));
                errMap.put(prefix + "aregType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Type of Registration", "field"));
                errMap.put(prefix + "aqualification" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Qualifications", "field"));
            }else {
                String name = anaesthetists.get(i).getName();
                String profRegNo = anaesthetists.get(i).getProfRegNo();
                String idNo = anaesthetists.get(i).getIdNo();
                String regType = anaesthetists.get(i).getRegType();
                String qualification = anaesthetists.get(i).getQualification();

                if (StringUtil.isEmpty(profRegNo)) {
                    errMap.put(prefix + "aprofRegNo" + i,
                            MessageUtil.replaceMessage("GENERAL_ERR0006", "Professional Regn. No.", "field"));
                }

                if (StringUtil.isEmpty(idNo)) {
                    errMap.put(prefix + "idANo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "NRIC/FIN No.", "field"));
                } else if (doValidationOtherInfoIndoList(aidNoList, idNo)) {
                    errMap.put(prefix + "idANo" + i, "NEW_ERR0012");
                } else {
                    errMap.putAll(doValidationOtherInfoTopPersonNric(idNo,i,prefix,"idANo"));
                }
                aidNoList.add(idNo);

                validateOtherTopMathAndMaxLength(errMap,prefix,regType,"aregType","Type of Registration",66,i);
                validateOtherTopMathAndMaxLength(errMap,prefix,name,"aname","Name of anaesthetists",100,i);
                validateOtherTopMathAndMaxLength(errMap,prefix,qualification,"aqualification","Qualifications",100,i);
            }
        }

        List<String> nameList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < nurses.size(); i++) {
            if (nurses.get(i) == null){
                errMap.put(prefix + "nname" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name of trained nurses", "field"));
                errMap.put(prefix + "nqualification" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Qualifications", "field"));
            }else {
                String name = nurses.get(i).getName();
                String qualification = nurses.get(i).getQualification();
                if (StringUtil.isEmpty(name)) {
                    errMap.put(prefix + "nname" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name of trained nurses", "field"));
                } else if (name.length() > 100){
                    String errorMsg = repLength("Name of trained nurses", String.valueOf(100));
                    errMap.put(prefix + "nname" + i , errorMsg);
                } else if (doValidationOtherInfoIndoList(nameList, name)) {
                    errMap.put(prefix + "nname" + i, "NEW_ERR0012");
                }
                nameList.add(name);
                validateOtherTopMathAndMaxLength(errMap,prefix,qualification,"nqualification","Qualifications",100,i);
            }
        }

        List<String> cidNoList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < counsellors.size(); i++) {
            if (counsellors.get(i) == null){
                errMap.put(prefix + "cname" + i,
                        MessageUtil.replaceMessage("GENERAL_ERR0006", "Name of certified TOP counsellors(Only Doctor/Nurse)",
                                "field"));
                errMap.put(prefix + "cidNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "NRIC/FIN No.", "field"));
                errMap.put(prefix + "cqualification" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Qualifications", "field"));
            }else {
                String name = counsellors.get(i).getName();
                String idNo = counsellors.get(i).getIdNo();
                String qualification = counsellors.get(i).getQualification();

                if (StringUtil.isEmpty(idNo)) {
                    errMap.put(prefix + "cidNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "NRIC/FIN No.", "field"));
                } else if (doValidationOtherInfoIndoList(cidNoList, idNo)) {
                    errMap.put(prefix + "cidNo" + i, "NEW_ERR0012");
                } else {
                    errMap.putAll(doValidationOtherInfoTopPersonNric(idNo,i,prefix,"cidNo"));
                }
                cidNoList.add(idNo);
                validateOtherTopMathAndMaxLength(errMap,prefix,name,"cname","Name of certified TOP counsellors(Only Doctor/Nurse)",100,i);
                validateOtherTopMathAndMaxLength(errMap,prefix,qualification,"cqualification","Qualifications",100,i);
            }
        }
        return errMap;
    }

    private static Map<String, String> doValidationOtherInfoTopPersonNric(String idNo,int index,String prefix,String type){
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        boolean b = SgNoValidator.validateFin(idNo);
        boolean b1 = SgNoValidator.validateNric(idNo);
        if(!(b||b1)){
            errMap.put(prefix + type + index,MessageUtil.replaceMessage("RFC_ERR0012", "NRIC/FIN No.", "field"));
        }
        return errMap;
    }

    public static Map<String, String> getValidateAppSvcOtherInfoTopAbort(AppSvcOtherInfoDto appSvcOtherInfoDto, String topType,
            String prefix) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        List<AppSvcOtherInfoAbortDto> topByDrug = appSvcOtherInfoDto.getOtherInfoAbortDrugList();
        List<AppSvcOtherInfoAbortDto> topBySurgicalProcedure = appSvcOtherInfoDto.getOtherInfoAbortSurgicalProcedureList();
        List<AppSvcOtherInfoAbortDto> topByAll = appSvcOtherInfoDto.getOtherInfoAbortDrugAndSurgicalList();
        if ((ApplicationConsts.OTHER_INFO_SD.equals(topType)) || (ApplicationConsts.OTHER_INFO_DSP.equals(topType))) {
            for (int i = 0; i < topByDrug.size(); i++) {
                if (topByDrug.get(i) == null){
                    errMap.put(prefix + "year" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                            "Year",
                            "field"));
                    errMap.put(prefix + "abortNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                            "No. of abortions",
                            "field"));
                }else {
                    String year = topByDrug.get(i).getYear();
                    if (StringUtil.isEmpty(year)) {
                        errMap.put(prefix + "year" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                                "Year",
                                "field"));
                    } else if (!StringUtil.isDigit(year) || year.matches("^-[0-9]*[1-9][0-9]*$")) {
                        errMap.put(prefix + "year" + i, MessageUtil.replaceMessage("GENERAL_ERR0002",
                                "Year",
                                "field"));
                    } else if (year.length() > 4){
                        String errorMsg = repLength("Year", "4");
                        errMap.put(prefix + "year" + i, errorMsg);
                    }
                    String abortNum = topByDrug.get(i).getAbortNum();
                    if (StringUtil.isEmpty(abortNum)) {
                        errMap.put(prefix + "abortNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                                "No. of abortions",
                                "field"));
                    } else if (!StringUtil.isDigit(abortNum) || abortNum.matches("^-[0-9]*[1-9][0-9]*$")) {
                        errMap.put(prefix + "abortNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0002",
                                "No. of abortions",
                                "field"));
                    } else if (abortNum.length() > 7){
                        String errorMsg = repLength("No. of abortions", "7");
                        errMap.put(prefix + "abortNum" + i, errorMsg);
                    }
                }
            }
        }

        if ((ApplicationConsts.OTHER_INFO_SSP.equals(topType)) || (ApplicationConsts.OTHER_INFO_DSP.equals(topType))) {
            for (int i = 0; i < topBySurgicalProcedure.size(); i++) {
                if (topBySurgicalProcedure.get(i) == null){
                    errMap.put(prefix + "pyear" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                            "Year",
                            "field"));
                    errMap.put(prefix + "pabortNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                            "No. of abortions",
                            "field"));
                }else {
                    String year = topBySurgicalProcedure.get(i).getYear();
                    if (StringUtil.isEmpty(year)) {
                        errMap.put(prefix + "pyear" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                                "Year",
                                "field"));
                    } else if (!StringUtil.isDigit(year) || year.matches("^-[0-9]*[1-9][0-9]*$")) {
                        errMap.put(prefix + "pyear" + i, MessageUtil.replaceMessage("GENERAL_ERR0002",
                                "Year",
                                "field"));
                    } else if (year.length() > 4){
                        String errorMsg = repLength("Year", "4");
                        errMap.put(prefix + "pyear" + i, errorMsg);
                    }
                    String abortNum = topBySurgicalProcedure.get(i).getAbortNum();
                    if (StringUtil.isEmpty(abortNum)) {
                        errMap.put(prefix + "pabortNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                                "No. of abortions",
                                "field"));
                    } else if (!StringUtil.isDigit(abortNum) || abortNum.matches("^-[0-9]*[1-9][0-9]*$")) {
                        errMap.put(prefix + "pabortNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0002",
                                "No. of abortions",
                                "field"));
                    } else if (abortNum.length() > 7){
                        String errorMsg = repLength("No. of abortions", "7");
                        errMap.put(prefix + "pabortNum" + i, errorMsg);
                    }
                }
            }
        }

        if (ApplicationConsts.OTHER_INFO_DSP.equals(topType)) {
            for (int i = 0; i < topByAll.size(); i++) {
                if (topByAll.get(i) == null){
                    errMap.put(prefix + "ayear" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                            "Year",
                            "field"));
                    errMap.put(prefix + "aabortNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                            "No. of abortions",
                            "field"));
                }else {
                    String year = topByAll.get(i).getYear();
                    if (StringUtil.isEmpty(year)) {
                        errMap.put(prefix + "ayear" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                                "Year",
                                "field"));
                    } else if (!StringUtil.isDigit(year) || year.matches("^-[0-9]*[1-9][0-9]*$")) {
                        errMap.put(prefix + "ayear" + i, MessageUtil.replaceMessage("GENERAL_ERR0002",
                                "Year",
                                "field"));
                    } else if (year.length() > 4){
                        String errorMsg = repLength("Year", "4");
                        errMap.put(prefix + "ayear" + i, errorMsg);
                    }
                    String abortNum = topByAll.get(i).getAbortNum();
                    if (StringUtil.isEmpty(abortNum)) {
                        errMap.put(prefix + "aabortNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                                "No. of abortions",
                                "field"));
                    } else if (!StringUtil.isDigit(abortNum) || abortNum.matches("^-[0-9]*[1-9][0-9]*$")) {
                        errMap.put(prefix + "aabortNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0002",
                                "No. of abortions",
                                "field"));
                    } else if (abortNum.length() > 7){
                        String errorMsg = repLength("No. of abortions", "7");
                        errMap.put(prefix + "aabortNum" + i, errorMsg);
                    }
                }
            }
        }
        return errMap;
    }

    private static boolean doValidationOtherInfoIndoList(List<String> idNoList, String idNo) {
        if (IaisCommonUtils.isNotEmpty(idNoList)) {
            for (String s : idNoList) {
                if (idNo.equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean doPsnCommValidate(Map<String, String> errMap, String personKey, String idNo, boolean licPerson,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, String errKey) {
        boolean isValid = true;
        if (licPersonMap != null && !StringUtil.isEmpty(personKey)
                && !StringUtil.isEmpty(idNo) && !licPerson && needPsnCommValidate()) {
            AppSvcPersonAndExtDto appSvcPersonAndExtDto = licPersonMap.get(personKey);
            if (appSvcPersonAndExtDto != null) {
                errMap.put(errKey, MessageUtil.replaceMessage("NEW_ERR0006", idNo, "ID No."));
                isValid = false;
            }
        }
        return isValid;
    }

    private static boolean needPsnCommValidate() {
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        if (request == null) {
            return false;
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,
                HcsaAppConst.APPSUBMISSIONDTO);
        return appSubmissionDto != null && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())
                && !ApplicationHelper.checkIsRfi(request);
    }

    public static Map<Integer, String> checkBlacklist(String name) {
        return checkBlacklist(name, null);
    }

    public static Map<Integer, String> checkBlacklist(String name, String blacklist) {
        if (StringUtil.isEmpty(name)) {
            return IaisCommonUtils.genNewHashMap();
        }
        if (StringUtil.isEmpty(blacklist)) {
            blacklist = MasterCodeUtil.getCodeDesc("MS001");
        }
        Map<Integer, String> map = new LinkedHashMap<>();
        if (blacklist == null || StringUtil.isEmpty(blacklist) || StringUtil.isEmpty(name)) {
            return map;
        }
        String[] s = blacklist.split("[ ]+");
        name = name.toUpperCase(AppConsts.DFT_LOCALE);
        String[] target = name.split("[^A-Z]+");
        for (String value : s) {
            String t = value.toUpperCase();
            if (Arrays.stream(target).parallel().anyMatch(x -> x.equals(t))) {
                map.put(name.indexOf(t), value);
            }
        }
        return map;
    }

    /**
     * 0: all changed with org licence
     * <br>
     * 2: only hci name <b>not</b> changes with org licence
     * <br>
     * 3: only business name <b>not</b> changes with org licence
     * <br>
     * 4: all <b>not</b> changes with org licence
     * <br>
     *
     * @param hciName
     * @param businessName
     * @param licenceId
     * @return
     */
    public static int checkNameChanged(String hciName, String businessName, String licenceId) {
        if (licenceId == null) {
            return 0;
        }
        List<PremisesDto> premisesDtoList = getLicCommService().getPremisesListByLicenceId(licenceId, Boolean.FALSE, Boolean.TRUE);
        if (premisesDtoList == null || premisesDtoList.isEmpty()) {
            return 0;
        }
        PremisesDto premisesDto = premisesDtoList.get(0);
        boolean sameHciName = Objects.equals(premisesDto.getHciName(), hciName);
        boolean sameBusinessName = Objects.equals(premisesDto.getBusinessName(), businessName);

        int checked = 0;
        if (sameHciName && sameBusinessName) {
            checked = 4;
        } else if (sameHciName) {
            checked = 2;
        } else if (sameBusinessName) {
            checked = 3;
        }
        log.info(StringUtil.changeForLog("Check Name Changed: " + checked));
        return checked;
    }

    public static boolean checkProfRegNo(String profRegNo) {
        return checkProfRegNo(profRegNo, null, MiscUtil.getCurrentRequest());
    }

    public static boolean checkProfRegNo(String profRegNo, ProfessionalResponseDto dto, HttpServletRequest request) {
        String prsFlag = ConfigHelper.getString("moh.halp.prs.enable");
        if (!"Y".equals(prsFlag) || StringUtil.isEmpty(profRegNo)) {
            return true;
        }
        log.info(StringUtil.changeForLog("Prof Reg No is " + profRegNo));
        boolean isValid = true;
        if (dto == null) {
            AppCommService appCommService = SpringContextHelper.getContext().getBean(AppCommService.class);
            dto = appCommService.retrievePrsInfo(profRegNo);
        }
        if (dto == null) {
            return true;
        }
        if ("-1".equals(dto.getStatusCode()) || "-2".equals(dto.getStatusCode())) {
            isValid = false;
        } else if (dto.isHasException()) {
            isValid = false;
            if (request != null) {
                request.setAttribute(HcsaAppConst.PRS_SERVICE_DOWN, HcsaAppConst.PRS_SERVICE_DOWN);
            }
        }
        return isValid;
    }

    public static boolean validateId(String nationality, String idType, String idNo, String keyNationality, String keyIdType,
            String keyIdNo, Map<String, String> errMap) {
        boolean isValid = true;
        if (HcsaAppConst.DFT_FIRST_CODE.equals(idType) || StringUtil.isEmpty(idType)) {
            errMap.put(keyIdType, MessageUtil.replaceMessage("GENERAL_ERR0006", "ID Type", "field"));
            isValid = false;
        }
        if (StringUtil.isEmpty(idNo)) {
            errMap.put(keyIdNo, MessageUtil.replaceMessage("GENERAL_ERR0006", "ID No.", "field"));
            isValid = false;
        } else if (!SgNoValidator.validateMaxLength(idType, idNo)) {
            Map<String, String> argv = IaisCommonUtils.genNewHashMap();
            argv.put("field", "Id No.");
            argv.put("maxlength", OrganizationConstants.MAXLENGTH_ID_NO_STR);
            errMap.put(keyIdNo, MessageUtil.getMessageDesc("GENERAL_ERR0041", argv));
            isValid = false;
        } else if (!SgNoValidator.validateIdNo(idType, idNo)) {
            errMap.put(keyIdNo, "RFC_ERR0012");
            isValid = false;
        }
        if (OrganizationConstants.ID_TYPE_PASSPORT.equals(idType)) {
            // check it only for Passport
            if (HcsaAppConst.DFT_FIRST_CODE.equals(nationality) || StringUtil.isEmpty(nationality)) {
                errMap.put(keyNationality, MessageUtil.replaceMessage("GENERAL_ERR0006", "Nationality", "field"));
                isValid = false;
            }
        }
        return isValid;
    }

    public static String getErrorMsg(Map<AppSubmissionDto, List<String>> errorListMap) {
        if (errorListMap == null || errorListMap.isEmpty()) {
            return "";
        }
        if (errorListMap.entrySet().stream().noneMatch(entry -> IaisCommonUtils.isNotEmpty(entry.getValue()))) {
            return "";
        }
        StringBuilder msg = new StringBuilder(errorListMap.size() * 64);
        msg.append("There are some affected malformed licences: ");
        for (Map.Entry<AppSubmissionDto, List<String>> entry : errorListMap.entrySet()) {
            if (IaisCommonUtils.isNotEmpty(entry.getValue())) {
                msg.append("<br/>&nbsp;&nbsp;");
                handleTabHames(entry.getKey(), entry.getValue(), msg);
                msg.append(", ");
            }
        }
        msg.deleteCharAt(msg.length() - 2);
        msg.deleteCharAt(msg.length() - 1);
        msg.append(". ");
        msg.append("<br/><br/>Please check them before you submitting them again.");

        return msg.toString();
    }

    private static void handleTabHames(AppSubmissionDto appSubmissionDto, List<String> errorList, StringBuilder msg) {
        if (msg == null) {
            return;
        }
        msg.append(appSubmissionDto.getLicenceNo()).append(" - [");
        if (errorList.contains(HcsaAppConst.SECTION_LICENSEE)) {
            msg.append(HcsaAppConst.TITLE_LICENSEE).append(", ");
        }
        if (errorList.contains(HcsaAppConst.SECTION_PREMISES)) {
            msg.append(HcsaAppConst.TITLE_MODE_OF_SVCDLVY).append(", ");
        }
        if (errorList.contains(HcsaAppConst.SECTION_SPECIALISED)) {
            msg.append(HcsaAppConst.TITLE_SPECIALISED).append(", ");
        }
        if (errorList.contains(HcsaAppConst.SECTION_SVCINFO)) {
            msg.append(HcsaAppConst.TITLE_SVCINFO);
            String s = handleStepHames(errorList);
            if (!StringUtil.isEmpty(s)) {
                msg.append(' ').append('(').append(s).append(')');
            }
            msg.append(',').append(' ');
        }
        msg.deleteCharAt(msg.length() - 2);
        msg.deleteCharAt(msg.length() - 1);
        msg.append(']');
    }

    private static String handleStepHames(List<String> errorList) {
        return errorList.stream()
                .filter(s -> s.contains(":"))
                .map(s -> s.substring(s.indexOf(':') + 1))
                .collect(Collectors.joining(", "));
    }

    public static void setErrorRequest(Map<String, String> errorMap, boolean withMain, HttpServletRequest request) {
        if (errorMap == null || errorMap.isEmpty()) {
            return;
        }
        log.info(StringUtil.changeForLog("##### Error: " + errorMap));
        if (withMain) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        }
        List<String> modalRelated = IaisCommonUtils.genNewArrayList();
        modalRelated.add(RfcConst.SHOW_OTHER_ERROR);
        modalRelated.add(RfcConst.SERVICE_CONFIG_CHANGE);
        modalRelated.add(RfcConst.INVALID_LIC);
        modalRelated.add(RfcConst.PENDING_APP);
        errorMap.forEach((k, v) -> {
            if (modalRelated.contains(k)) {
                ParamUtil.setRequestAttr(request, k, v);
            }
        });
    }

    public static boolean psnDoPartValidate(String idType, String idNo, String name) {
        boolean isValid = true;
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNo) || StringUtil.isEmpty(name)) {
            isValid = false;
        } else {
            if (name.length() > 100) {
                isValid = false;
            } else if (!SgNoValidator.validateMaxLength(idType, idNo)) {
                isValid = false;
            } else if (!SgNoValidator.validateIdNo(idType, idNo)) {
                isValid = false;
            }
        }
        return isValid;
    }

    public static Map<String, String> psnMandatoryValidate(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList, String psnType,
            Map<String, String> errMap, int psnLength, String errName, String psnName) {
        int mandatoryCount = getManDatoryCountByPsnType(hcsaSvcPersonnelList, psnType);
        if (psnLength < mandatoryCount) {
            errMap.put(errName, validateMandatoryCount(psnName, mandatoryCount));
        }
        return errMap;
    }

    public static Map<String, String> psnMandatoryPersonnel(Integer mandatoryCount,Map<String, String> errMap, int psnLength, String errName, String psnName) {
        if (StringUtil.isEmpty(mandatoryCount)){
            return errMap;
        }
        if (psnLength < mandatoryCount) {
            errMap.put(errName, validateMandatoryCount(psnName, mandatoryCount));
        }
        return errMap;
    }

    private static int getManDatoryCountByPsnType(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, String psnType) {
        int mandatoryCount = 0;
        if (!IaisCommonUtils.isEmpty(hcsaSvcPersonnelDtos)) {
            for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
                if (hcsaSvcPersonnelDto.getPsnType().equals(psnType)) {
                    mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
                    break;
                }
            }
        }
        return mandatoryCount;
    }

    public static boolean validateDeclarationDoc(Map<String, String> errorMap, String fileAppendId, boolean isMandatory,
            HttpServletRequest request) {
        boolean isValid = true;
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request,
                IaisEGPConstant.SEESION_FILES_MAP_AJAX + fileAppendId);
        if (isMandatory && (fileMap == null || fileMap.isEmpty())) {
            errorMap.put(fileAppendId + "Error", MessageUtil.replaceMessage("GENERAL_ERR0006", "this", "field"));
            isValid = false;
        }
        return isValid;
    }

    public static boolean validateEffectiveDate(String field, String effectiveDateStr, Map<String, String> errorMap) {
        boolean isValid = true;
        if (!CommonValidator.isDate(effectiveDateStr)) {
            errorMap.put(field, MessageUtil.getMessageDesc("GENERAL_ERR0033"));
            isValid = false;
        } else {
            SystemParamConfig systemParamConfig = getSystemParamConfig();
            int configDateSize = systemParamConfig.getRfcPeriodEffdate();
            LocalDate effectiveDate = LocalDate.parse(effectiveDateStr, DateTimeFormatter.ofPattern(Formatter.DATE));
            LocalDate today = LocalDate.now();
            LocalDate configDate = LocalDate.now().plusDays(configDateSize);
            if (!effectiveDate.isAfter(today)) {
                errorMap.put(field, MessageUtil.getMessageDesc("RFC_ERR012"));
                isValid = false;
            } else if (effectiveDate.isAfter(configDate)) {
                String errorMsg = MessageUtil.getMessageDesc("RFC_ERR008");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Formatter.DATE);
                errorMsg = errorMsg.replace("{date}", configDate.format(dtf));
                errorMap.put(field, errorMsg);
                isValid = false;
            }
        }
        return isValid;
    }

    public static void doValidateYears(String year, Map<String, String> errorMap, String svcCode, int index) {
        if (StringUtil.isDigit(year)) {
            int workExpYear = Integer.parseInt(year);
            SystemParamConfig systemParamConfig = SystemParamUtil.getSystemParamConfig();
            if (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(svcCode)) {
                int minWrkExprerience = systemParamConfig.getClbSlMinWrkExprerience();
                if (workExpYear < minWrkExprerience) {
                    errorMap.put("wrkExpYear" + index, MessageUtil.replaceMessage("GENERAL_ERR0055",
                            Integer.toString(minWrkExprerience), "x"));
                }
            } else if (AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(svcCode)) {
                int minWrkExprerience = systemParamConfig.getRdsSlMinWrkExprerience();
                if (workExpYear < minWrkExprerience) {
                    errorMap.put("wrkExpYear" + index, MessageUtil.replaceMessage("GENERAL_ERR0055",
                            Integer.toString(minWrkExprerience), "x"));
                }
            } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)) {
            } else if (AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)) {
                int minWrkExprerience = systemParamConfig.getNmiSlMinWrkExprerience();
                if (workExpYear < minWrkExprerience) {
                    errorMap.put("wrkExpYear" + index, MessageUtil.replaceMessage("GENERAL_ERR0055",
                            Integer.toString(minWrkExprerience), "x"));
                }
            }
        }
    }


    public static void doValidateSectionLeader(Map<String, String> errorMap, List<AppSvcPersonnelDto> appSvcSectionLeaderList,
            String svcCode) {
        if (appSvcSectionLeaderList == null || appSvcSectionLeaderList.isEmpty()) {
            return;
        }
        List<String> errorName = IaisCommonUtils.genNewArrayList();
        String signal = "GENERAL_ERR0006";
        for (int i = 0; i < appSvcSectionLeaderList.size(); i++) {
            AppSvcPersonnelDto appSvcPersonnelDto = appSvcSectionLeaderList.get(i);
            if (!(i == 0 && "isCheck".equals(appSvcPersonnelDto.getRegnNo()) && ReflectionUtil.isEmpty(appSvcPersonnelDto,"personnelType", "indexNo", "prsLoading", "seqNum","regnNo"))) {
                String salutation = appSvcPersonnelDto.getSalutation();
                String name = appSvcPersonnelDto.getName();
                String qualification = appSvcPersonnelDto.getQualification();
                String wrkExpYear = appSvcPersonnelDto.getWrkExpYear();
                if (StringUtil.isEmpty(salutation)) {
                    errorMap.put("salutation" + i, signal);
                }
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, signal);
                } else if (name.length() > 100) {
                    errorMap.put("name" + i, repLength("Name", "100"));
                } else {
                    String target = salutation + name;
                    boolean flag = errorName.stream().anyMatch(target::equalsIgnoreCase);
                    if (flag) {
                        errorMap.put("name" + i, "NEW_ERR0012");
                    } else {
                        errorName.add(target);
                    }
                }
                if (StringUtil.isEmpty(qualification)) {
                    errorMap.put("qualification" + i, signal);
                } else if (qualification.length() > 100) {
                    errorMap.put("qualification" + i, signal);
                }
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, signal);
                } else {
                    if (wrkExpYear.length() > 2) {
                        errorMap.put("wrkExpYear" + i, signal);
                    }
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
                doValidateYears(wrkExpYear, errorMap, svcCode, i);
            }
            appSvcPersonnelDto.setRegnNo(null);
        }
    }

    public static void doValidateSvcDocuments(List<DocumentShowDto> documentShowDtoList, Map<String, String> errorMap) {
        if (IaisCommonUtils.isEmpty(documentShowDtoList)) {
            return;
        }
        SystemParamConfig systemParamConfig = getSystemParamConfig();
        int uploadFileLimit = systemParamConfig.getUploadFileLimit();
        String sysFileType = systemParamConfig.getUploadFileType();
        for (DocumentShowDto documentShowDto : documentShowDtoList) {
            String premisesVal = documentShowDto.getPremisesVal();
            for (DocSectionDto docSectionDto : documentShowDto.getDocSectionList()) {
                String svcCode = docSectionDto.getSvcCode();
                List<DocSecDetailDto> docSecDetailList = docSectionDto.getDocSecDetailList();
                int secSize = docSecDetailList.size();
                for (int j = 0; j < secSize; j++) {
                    String docKey = ApplicationHelper.getSvcDocKey(j, svcCode, premisesVal);
                    DocSecDetailDto docSecDetail = docSecDetailList.get(j);
                    List<AppSvcDocDto> appSvcDocDtoList = docSecDetail.getAppSvcDocDtoList();
                    validateSvcDocuments(errorMap, appSvcDocDtoList, docKey, uploadFileLimit, sysFileType);
                    if (docSecDetail.isMandatory() && (appSvcDocDtoList == null || appSvcDocDtoList.isEmpty())) {
                        errorMap.put(docKey + "Error", "GENERAL_ERR0006");
                    }
                }
            }
        }
    }

    private static void validateSvcDocuments(Map<String, String> map, List<AppSvcDocDto> appSvcDocDtoLit, String preKey,
            int uploadFileLimit, String sysFileType) {
        if (appSvcDocDtoLit != null && !appSvcDocDtoLit.isEmpty()) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLit) {
                boolean isValid = true;
                Integer docSize = appSvcDocDto.getDocSize();
                String docName = appSvcDocDto.getDocName();
                if (docName == null) {
                    continue;
                }
                String errorKey = preKey + appSvcDocDto.getSeqNum() + "Error";
                boolean flag = false;
                String substring = docName.substring(docName.lastIndexOf('.') + 1);
                if (docSize / 1024 > uploadFileLimit) {
                    isValid = false;
                    map.put(errorKey, MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(uploadFileLimit), "sizeMax"));
                }

                if (docName.length() > 100) {
                    isValid = false;
                    map.put(errorKey, "GENERAL_ERR0022");
                }

                String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
                if (sysFileTypeArr != null) {
                    for (String f : sysFileTypeArr) {
                        if (f.equalsIgnoreCase(substring)) {
                            flag = true;
                            break;
                        }
                    }
                }
                if (!flag) {
                    isValid = false;
                    map.put(errorKey, MessageUtil.replaceMessage("GENERAL_ERR0018", sysFileType, "fileType"));
                }
                appSvcDocDto.setPassValidate(isValid);
            }
        }
    }

    public static boolean isEarly(String bclsExpiryDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DEFAULT_DATE_FORMAT);
        Date date = new Date();
        String source = sdf.format(date);
        if (StringUtil.isEmpty(bclsExpiryDateStr)) {
            return false;
        }
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(bclsExpiryDateStr);
            date2 = sdf.parse(source);
        } catch (ParseException e) {
            log.info(StringUtil.changeForLog(e.getMessage()), e);
        }
        return date1 != null && date1.compareTo(date2) >= 0;
    }

    public static void paramValidate(Map<String, String> errorMap, AppSvcPersonnelDto appSvcPersonnelDto, String prefix, int i,
            List<String> personnelNames,Boolean flag) {
        if (flag){
            return;
        }
        String signal = "GENERAL_ERR0006";
        String name = appSvcPersonnelDto.getName();
        if (StringUtil.isEmpty(name)) {
            errorMap.put(prefix + "name" + i, signal);
        } else if (name.length() > 100) {
            String errorMsg = repLength("Name", "100");
            errorMap.put(prefix + "name" + i, errorMsg);
        }
        String wrkExpYear = appSvcPersonnelDto.getWrkExpYear();
        if (StringUtil.isEmpty(wrkExpYear)) {
            errorMap.put(prefix + "wrkExpYear" + i, signal);
        } else {
            if (wrkExpYear.length() > 2) {
                errorMap.put(prefix + "wrkExpYear" + i, repLength("Relevant working experience (Years) ", "2"));
            }
            if (!wrkExpYear.matches("^[0-9]*$")) {
                errorMap.put(prefix + "wrkExpYear" + i, "GENERAL_ERR0002");
            }
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS.equals(prefix)
                || ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER.equals(prefix)) {
            String target = StringUtil.toUpperCase(prefix + name);
            if (personnelNames.contains(target)) {
                errorMap.put(prefix + "name" + i, "NEW_ERR0012");
            } else {
                personnelNames.add(target);
            }
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER.equals(prefix)
                || ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES.equals(prefix)) {
            String designation = appSvcPersonnelDto.getDesignation();
            if (StringUtil.isEmpty(designation)) {
                errorMap.put(prefix + "designation" + i, signal);
            } else if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                String otherDesignation = appSvcPersonnelDto.getOtherDesignation();
                if (StringUtil.isEmpty(otherDesignation)) {
                    errorMap.put(prefix + "otherDesignation" + i, signal);
                } else if (otherDesignation.length() > 100) {
                    String errorMsg = repLength("Others Designation", "100");
                    errorMap.put(prefix + "otherDesignation" + i, errorMsg);
                }
            }
            //              profRegNo
            String profRegNo = appSvcPersonnelDto.getProfRegNo();
            if (StringUtil.isEmpty(profRegNo)) {
                errorMap.put(prefix + "profRegNo" + i, signal);
            } else if (profRegNo.length() > 20) {
                String errorMsg = repLength("Professional Regn. No.", "20");
                errorMap.put(prefix + "profRegNo" + i, errorMsg);
            } else {
                validateProfRegNo(errorMap, profRegNo, prefix+ "profRegNo" + i);
            }
            //                typeOfCurrRegi
            String typeOfCurrRegi = appSvcPersonnelDto.getTypeOfCurrRegi();
            if (StringUtil.isEmpty(typeOfCurrRegi)) {
                errorMap.put(prefix + "typeOfCurrRegi" + i, signal);
            } else if (typeOfCurrRegi.length() > 50) {
                String errorMsg = repLength("Type of Current Registration", "50");
                errorMap.put(prefix + "typeOfCurrRegi" + i, errorMsg);
            }
            // currRegiDate
            String currRegiDate = appSvcPersonnelDto.getCurrRegiDate();
            if (StringUtil.isEmpty(currRegiDate)) {
                errorMap.put(prefix + "currRegiDate" + i, signal);
            } else if (!CommonValidator.isDate(currRegiDate)) {
                errorMap.put(prefix + "currRegiDate" + i, "GENERAL_ERR0033");
            }
            // praCerEndDate
            String praCerEndDateStr = appSvcPersonnelDto.getPraCerEndDate();
            if (StringUtil.isEmpty(praCerEndDateStr)) {
                errorMap.put(prefix + "praCerEndDate" + i, signal);
            } else if (!CommonValidator.isDate(praCerEndDateStr)) {
                errorMap.put(prefix + "praCerEndDate" + i, "GENERAL_ERR0033");
            }

            // typeOfRegister
            String typeOfRegister = appSvcPersonnelDto.getTypeOfRegister();
            if (StringUtil.isEmpty(typeOfRegister)) {
                errorMap.put(prefix + "typeOfRegister" + i, signal);
            } else if (typeOfRegister.length() > 50) {
                errorMap.put(prefix + "typeOfRegister" + i, repLength("Type of Register", "50"));
            }

            // bclsExpiryDate
            String bclsExpiryDateStr = appSvcPersonnelDto.getBclsExpiryDate();
            if (StringUtil.isEmpty(bclsExpiryDateStr)) {
                errorMap.put(prefix + "bclsExpiryDate" + i, signal);
            } else if (!CommonValidator.isDate(bclsExpiryDateStr)) {
                errorMap.put(prefix + "bclsExpiryDate" + i, "GENERAL_ERR0033");
            } else {
                if (!isEarly(bclsExpiryDateStr)) {
                    errorMap.put(prefix + "bclsExpiryDate" + i, "PRF_ERR007");
                }
            }
            // nurse special
            if (ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES.equals(prefix)) {
                String professionType = appSvcPersonnelDto.getProfessionType();
                if (StringUtil.isEmpty(professionType)) {
                    errorMap.put(prefix + "professionType" + i, signal);
                }
                String professionBoard = appSvcPersonnelDto.getProfessionBoard();
                if (StringUtil.isEmpty(professionBoard)) {
                    errorMap.put(prefix + "professionBoard" + i, signal);
                }

                String cprExpiryDate = appSvcPersonnelDto.getCprExpiryDate();
                if (StringUtil.isEmpty(cprExpiryDate)) {
                    errorMap.put(prefix + "cprExpiryDate" + i, signal);
                } else if (!CommonValidator.isDate(cprExpiryDate)) {
                    errorMap.put(prefix + "cprExpiryDate" + i, "GENERAL_ERR0033");
                }else if (!isEarly(cprExpiryDate)){
                    errorMap.put(prefix + "cprExpiryDate" + i, "PRF_ERR007");
                }
            }

        }
        if (ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS.equals(prefix)) {
            String qualification = appSvcPersonnelDto.getQualification();
            if (StringUtil.isEmpty(qualification)) {
                errorMap.put(prefix + "qualification" + i, signal);
            } else if (qualification.length() > 100) {
                errorMap.put(prefix + "qualification" + i, repLength("Qualification", "100"));
            }
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES.equals(
                prefix) || ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST.equals(prefix)) {
            String salutation = appSvcPersonnelDto.getSalutation();
            if (StringUtil.isEmpty(salutation)) {
                errorMap.put(prefix + "salutation" + i, signal);
            }
            String target = StringUtil.toUpperCase(salutation + prefix + name);
            if (personnelNames.contains(target)) {
                errorMap.put(prefix + "name" + i, "NEW_ERR0012");
            } else {
                personnelNames.add(target);
            }
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST.equals(prefix)) {
            //
            String embryologistAuthorized = appSvcPersonnelDto.getEmbryologistAuthorized();
            if (StringUtil.isEmpty(embryologistAuthorized)) {
                errorMap.put(prefix + "embryologistAuthorized" + i, signal);
            }
            String qualification = appSvcPersonnelDto.getQualification();
            if (StringUtil.isNotEmpty(qualification) && qualification.length() > 100) {
                errorMap.put(prefix + "qualification" + i, repLength("Qualification", "100"));
            }
            String numberSupervision = appSvcPersonnelDto.getNumberSupervision();
            if (StringUtil.isEmpty(numberSupervision)) {
                errorMap.put(prefix + "numberSupervision" + i,signal);
            }else if (numberSupervision.length() > 2){
                errorMap.put(prefix + "numberSupervision" + i, repLength("Number of AR procedures done under supervision", "2"));
            }
            else if (!numberSupervision.matches("^[0-9]*$")) {
                errorMap.put(prefix + "numberSupervision" + i, "GENERAL_ERR0002");
            }
        }

        String specialityOther = appSvcPersonnelDto.getSpecialityOther();
        if (!StringUtil.isEmpty(specialityOther) && specialityOther.length() > 100) {
            errorMap.put(prefix + "specialityOther" + i, repLength("Other Specialties", "100"));
        }
        // Date when specialty was obtained
        // 86960
        String specialtyGetDate = appSvcPersonnelDto.getSpecialtyGetDate();
        if (!StringUtil.isEmpty(specialityOther) && StringUtil.isEmpty(specialtyGetDate)) {
            errorMap.put(prefix + "specialtyGetDate" + i, "GENERAL_ERR0006");
        } else if (!StringUtil.isEmpty(specialtyGetDate) && !CommonValidator.isDate(specialtyGetDate)){
            errorMap.put(prefix + "specialtyGetDate" + i, "GENERAL_ERR0033");
        }
    }

    public static void specialValidate(Map<String, String> errorMap, AppSvcPersonnelDto appSvcPersonnelDto, String prefix, int i,
            List<String> errorName, boolean flag) {
        String signal = "GENERAL_ERR0006";
        String personnelSel = appSvcPersonnelDto.getPersonnelType();
        String name = appSvcPersonnelDto.getName();
        String salutation = appSvcPersonnelDto.getSalutation();
        if (StringUtils.isEmpty(personnelSel)) {
            errorMap.put(prefix + "personnelType" + i, signal);
        }
        if (flag) {
            if (StringUtil.isEmpty(salutation)) {
                errorMap.put(prefix + "salutation" + i, signal);
            }
        } else {
            salutation = "";
        }
        if (StringUtil.isEmpty(name)) {
            errorMap.put(prefix + "name" + i, signal);
        } else if (name.length() > 100) {
            errorMap.put(prefix + "name" + i,  repLength("Name", "100"));
        }
        String target = personnelSel + name + salutation;
        boolean flags = errorName.stream().anyMatch(target::equalsIgnoreCase);
        if (flags) {
            errorMap.put(prefix + "name" + i, "NEW_ERR0012");
        } else {
            errorName.add(target);
        }
//                  SPPT004
        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)) {
            String profRegNo = appSvcPersonnelDto.getProfRegNo();
            if (StringUtil.isEmpty(profRegNo)) {
                errorMap.put(prefix + "profRegNo" + i, signal);
            } else if (profRegNo.length() > 20) {
                errorMap.put(prefix + "profRegNo" + i,  repLength("Professional Regn. No.", "20"));
            }//SPPT001
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(
                personnelSel) || ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(
                personnelSel) || ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER.equals(personnelSel)) {
            String wrkExpYear = appSvcPersonnelDto.getWrkExpYear();
            String qualification = appSvcPersonnelDto.getQualification();
            if (StringUtil.isEmpty(wrkExpYear)) {
                errorMap.put(prefix + "wrkExpYear" + i, signal);
            } else {
                if (wrkExpYear.length() > 2) {
                    errorMap.put(prefix + "wrkExpYear" + i, repLength("Relevant working experience (Years) ", "2"));
                }
                if (!wrkExpYear.matches("^[0-9]*$")) {
                    errorMap.put(prefix + "wrkExpYear" + i, "GENERAL_ERR0002");
                }
            }
            if (StringUtil.isEmpty(qualification)) {
                errorMap.put(prefix + "qualification" + i, signal);
            } else if (qualification.length() > 100) {
                errorMap.put(prefix + "qualification" + i, repLength("Qualification", "100"));
            }
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)) {
            String designation = appSvcPersonnelDto.getDesignation();
            if (StringUtil.isEmpty(designation)) {
                errorMap.put(prefix + "designation" + i, signal);
            } else if ("Others".equals(designation)) {
                String otherDesignation = appSvcPersonnelDto.getOtherDesignation();
                if (StringUtil.isEmpty(otherDesignation)) {
                    errorMap.put(prefix + "otherDesignation" + i, signal);
                } else if (otherDesignation.length() > 100) {
                    errorMap.put(prefix + "otherDesignation" + i, repLength("Others Designation", "100"));
                }
            }
        }

    }

    public static void doValidateSvcPersonnel(Map<String, String> errorMap, SvcPersonnelDto svcPersonnelDto) {
        if (StringUtil.isEmpty(svcPersonnelDto)) {
            return;
        }
        Map<String, Integer> minPersonnle = svcPersonnelDto.getMinPersonnle();
        Map<String, Integer> maxPersonnel = svcPersonnelDto.getMaxPersonnel();
        if (StringUtil.isEmpty(minPersonnle)){
            minPersonnle = IaisCommonUtils.genNewHashMap();
        }
        if (StringUtil.isEmpty(maxPersonnel)){
            maxPersonnel = IaisCommonUtils.genNewHashMap();
        }
        List<AppSvcPersonnelDto> normalList = svcPersonnelDto.getNormalList();
        List<AppSvcPersonnelDto> nurseList = svcPersonnelDto.getNurseList();
//        List<AppSvcPersonnelDto> specialList = svcPersonnelDto.getSpecialList();
        List<AppSvcPersonnelDto> embryologistList = svcPersonnelDto.getEmbryologistList();
        List<AppSvcPersonnelDto> arPractitionerList = svcPersonnelDto.getArPractitionerList();
        List<String> personnelNames = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(arPractitionerList)) {
            int count = arPractitionerList.size();
            for (int i = 0; i < count; i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = arPractitionerList.get(i);
                boolean isMandatory = false;
                if (i == 0){
                    isMandatory = isMandatory(minPersonnle,maxPersonnel,ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER,appSvcPersonnelDto);
                }
                paramValidate(errorMap, appSvcPersonnelDto, ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER, i,
                        personnelNames,isMandatory);
            }
            psnMandatoryPersonnel(minPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER),errorMap,count,
                    ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER+"personError0","AR Practitioner");
        }

        if (IaisCommonUtils.isNotEmpty(nurseList)) {
            int count = nurseList.size();
            for (int i = 0; i < count; i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = nurseList.get(i);
                boolean isMandatory = false;
                if (i == 0){
                     isMandatory = isMandatory(minPersonnle,maxPersonnel,ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES,appSvcPersonnelDto);
                }
                paramValidate(errorMap, nurseList.get(i), ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES, i, personnelNames,isMandatory);
            }
            psnMandatoryPersonnel(minPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES),errorMap,count,
                    ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES+"personError0","Nurse");

        }
        if (IaisCommonUtils.isNotEmpty(embryologistList)) {
            int count = embryologistList.size();
            for (int i = 0; i < count; i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = embryologistList.get(i);
                boolean isMandatory = false;
                if (i == 0){
                    isMandatory = isMandatory(minPersonnle,maxPersonnel,ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST,appSvcPersonnelDto);
                }
                paramValidate(errorMap, embryologistList.get(i), ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST, i,
                        personnelNames,isMandatory);
            }
            psnMandatoryPersonnel(minPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST),errorMap,count,
                    ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST+"personError0","Embryologist");
        }
        if (IaisCommonUtils.isNotEmpty(normalList)) {
            int count = normalList.size();
            for (int i = 0; i < count; i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = normalList.get(i);
                boolean isMandatory = false;
                if (i == 0) {
                    isMandatory = isMandatory(minPersonnle, maxPersonnel, ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS, appSvcPersonnelDto);
                }
                paramValidate(errorMap, normalList.get(i), ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS, i, personnelNames,isMandatory);
            }
            psnMandatoryPersonnel(minPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS),errorMap,count,
                    ApplicationConsts.SERVICE_PERSONNEL_TYPE_OTHERS+"personError0","Service Personnel");
        }

/*        if (!StringUtil.isEmpty(specialList) && specialList.size() > 0) {
            int count = specialList.size();
            for (int i = 0; i < count; i++) {
                specialValidate(errorMap, specialList.get(i), ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS, i, personnelNames,
                        false);
            }
            Integer spe = minPersonnle.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS);
            psnMandatoryPersonnel(spe,errorMap,count,ApplicationConsts.SERVICE_PERSONNEL_TYPE_SPECIALS+"personError0","Service Personnel");
        }*/

    }

    /**
     * check whether there is another operation for the original licence
     *
     * @param licenceId
     * @param appCommService
     * @return
     */
    private static boolean validateRelatedApps(String licenceId, AppCommService appCommService, String type) {
        List<ApplicationDto> appByLicIdAndExcludeNew = appCommService.getAppByLicIdAndExcludeNew(licenceId);
        boolean invalid = IaisCommonUtils.isNotEmpty(appByLicIdAndExcludeNew)
                || !appCommService.isOtherOperation(licenceId);
        if (invalid) {
            log.info(StringUtil.changeForLog("##### Invalid Licence - " + type + " : " + licenceId));
            if (appByLicIdAndExcludeNew != null && !appByLicIdAndExcludeNew.isEmpty()) {
                CompletableFuture.runAsync(() -> appByLicIdAndExcludeNew.forEach(dto ->
                        log.warn(StringUtil.changeForLog("##### The error for Pending App: " + dto.getApplicationNo()))));
            }
            //request.setAttribute("rfcPendingApplication", "errorRfcPendingApplication");
        }
        return !invalid;
    }

    public static Map<String, String> validateLicences(String licenceId, Set<String> premiseTypes, String type) {
        log.info(StringUtil.changeForLog("### ValidateLicences Licence Id - " + licenceId));
        LicCommService licCommService = SpringHelper.getBean(LicCommService.class);
        LicenceDto licenceDto = licCommService.getActiveLicenceById(licenceId);
        return validateLicences(licenceDto, premiseTypes, type);
    }

    public static Map<String, String> validateLicences(LicenceDto licenceDto, Set<String> premiseTypes, String type) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (licenceDto == null) {
            errorMap.put(RfcConst.INVALID_LIC, MessageUtil.getMessageDesc(StringUtil.isEmpty(type) ? "RFC_ERR023" :
                    "RFC_ERR024"));
            return errorMap;
        }
        if (StringUtil.isEmpty(type) || HcsaAppConst.SECTION_PREMISES.equals(type)) {
            ConfigCommService configCommService = SpringHelper.getBean(ConfigCommService.class);
            HcsaServiceDto activeHcsaServiceDtoByName = configCommService.getActiveHcsaServiceDtoByName(licenceDto.getSvcName());
            if (activeHcsaServiceDtoByName != null && premiseTypes != null) {
                List<String> serviceIds = IaisCommonUtils.genNewArrayList();
                serviceIds.add(activeHcsaServiceDtoByName.getId());
                for (String premiseType : premiseTypes) {
                    boolean configIsChange = configCommService.serviceConfigIsChange(serviceIds, premiseType);
                    if (!configIsChange) {
                        log.warn(StringUtil.changeForLog("#####The error for serviceConfigIsChange: " + licenceDto.getLicenceNo()));
                        errorMap.put(RfcConst.SERVICE_CONFIG_CHANGE,
                                MessageUtil.replaceMessage("RFC_ERR020", licenceDto.getSvcName(), "ServiceName"));
                        return errorMap;
                    }
                }
            }
        }
        AppCommService appCommService = SpringHelper.getBean(AppCommService.class);
        if (!validateRelatedApps(licenceDto.getId(), appCommService, type)) {
            errorMap.put(RfcConst.PENDING_APP, RfcConst.PENDING_APP_VALUE);
        }
        return errorMap;
    }

    public static Map<String, String> validateLicences(List<AppSubmissionDto> appSubmissionDtos, String type) {
        if (appSubmissionDtos == null || appSubmissionDtos.isEmpty()) {
            return IaisCommonUtils.genNewHashMap();
        }
        return StreamSupport.stream(appSubmissionDtos.spliterator(), appSubmissionDtos.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE)
                .map(dto -> validateLicences(dto.getLicenceId(),
                        dto.getAppGrpPremisesDtoList().stream().map(AppGrpPremisesDto::getPremisesType).collect(Collectors.toSet()),
                        type))
                .collect(IaisCommonUtils::genNewHashMap, Map::putAll, Map::putAll);
    }


    public static void setAudiErrMap(boolean isRfi, String appType, Map<String, String> errMap, String appNo, String licenceNo) {
        if (isRfi) {
            ApplicationDto applicationDto = new ApplicationDto();
            applicationDto.setApplicationNo(appNo);
            WebValidationHelper.saveAuditTrailForNoUseResult(applicationDto, errMap);
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            LicenceDto licenceDto = new LicenceDto();
            licenceDto.setLicenceNo(licenceNo);
            WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto, errMap);
        } else {
            WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        }

    }

    private static boolean validateProfRegNo(Map<String, String> errMap, String profRegNo, String fieldKey) {
        if (StringUtil.isEmpty(profRegNo)) {
            return true;
        }
        boolean isValid = true;
        ProfessionalResponseDto professionalResponseDto = getAppCommService().retrievePrsInfo(profRegNo);
        if (professionalResponseDto != null) {
            if (professionalResponseDto.isHasException() || StringUtil.isNotEmpty(
                    professionalResponseDto.getStatusCode())) {
                log.debug(StringUtil.changeForLog("prs svc down ..."));
                if (professionalResponseDto.isHasException()) {
                    errMap.put(HcsaAppConst.PRS_SERVICE_DOWN, HcsaAppConst.PRS_SERVICE_DOWN);
                    isValid = false;
                } else if ("401".equals(professionalResponseDto.getStatusCode())) {
                    errMap.put(fieldKey, MessageUtil.getMessageDesc("GENERAL_ERR0054"));
                    isValid = false;
                } else {
                    errMap.put(fieldKey, MessageUtil.getMessageDesc("GENERAL_ERR0042"));
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    public static Map<String, String> doValidateSpecialisedDtoList(String svcCode,
            List<AppPremSpecialisedDto> appPremSpecialisedDtoList) {
        if (IaisCommonUtils.isEmpty(appPremSpecialisedDtoList)) {
            return IaisCommonUtils.genNewHashMap();
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        /*
         * 4.2.3.6:
         * 7. There should be at least 1 Category selected for Acute hospital service (ACH),
         * Outpatient Renal Dialysis (RD) Service (RDC) and 1 discipline selected for Clinical Laboratory service (CLB).
         * Likewise, for any service with options to select specialised service/specified test,
         * there must be a minimum of 1 option selected.
         */
        for (AppPremSpecialisedDto specialisedDto : appPremSpecialisedDtoList) {
            if (StringUtil.isNotEmpty(svcCode) && !Objects.equals(specialisedDto.getBaseSvcCode(), svcCode)) {
                continue;
            }
            String premisesVal = specialisedDto.getPremisesVal();
            String baseSvcCode = specialisedDto.getBaseSvcCode();
            /*
             * 84698 & 84864
             * 1. If nothing configured, must be able to proceed to next page
             * 2. If specified service configured, it is mandatory for CLB, RDS and NMS. all other LHS  are optional
             * 3. If category /discipline configured, it is mandatory for all
             */
            List<AppPremScopeDto> appPremScopeDtoList = specialisedDto.getAppPremScopeDtoList();
            if (appPremScopeDtoList != null && !appPremScopeDtoList.isEmpty()) {
                List<AppPremScopeDto> checkedAppPremScopeDtoList = specialisedDto.getCheckedAppPremScopeDtoList();
                if (checkedAppPremScopeDtoList == null || checkedAppPremScopeDtoList.isEmpty()) {
                    errorMap.put(premisesVal + "_sub_type", "GENERAL_ERR0006");
                }
            }
            if (StringUtil.isIn(baseSvcCode, new String[]{AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY,
                    AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES,
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_SERVICE})) {
                List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = specialisedDto.getAppPremSubSvcRelDtoList();
                if (appPremSubSvcRelDtoList != null && !appPremSubSvcRelDtoList.isEmpty()) {
                    List<AppPremSubSvcRelDto> checkedAppPremSubSvcRelDtoList = specialisedDto.getCheckedAppPremSubSvcRelDtoList();
                    if (checkedAppPremSubSvcRelDtoList == null || checkedAppPremSubSvcRelDtoList.isEmpty()) {
                        errorMap.put(premisesVal + "_service", "GENERAL_ERR0006");
                    }
                }
            }
        }
        return errorMap;
    }

    public static Map<String, String> doValidateSupplementaryFormList(List<AppSvcSuplmFormDto> appSvcSuplmFormList) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (IaisCommonUtils.isEmpty(appSvcSuplmFormList)) {
            log.info("The appSvcSuplmFormList is null!!!!");
            return errorMap;
        }
        for (AppSvcSuplmFormDto appSvcSuplmFormDto : appSvcSuplmFormList) {
            errorMap.putAll(doValidateSupplementaryForm(appSvcSuplmFormDto, appSvcSuplmFormDto.getPremisesVal()));
        }
        return errorMap;
    }

    public static Map<String, String> doValidateSupplementaryForm(AppSvcSuplmFormDto appSvcSuplmFormDto, String prefix) {
        if (appSvcSuplmFormDto == null) {
            log.info("The AppSvcSuplmFormDto is null!!!!");
            return IaisCommonUtils.genNewHashMap();
        }
        List<AppSvcSuplmGroupDto> appSvcSuplmGroupDtoList = appSvcSuplmFormDto.getAppSvcSuplmGroupDtoList();
        if (IaisCommonUtils.isEmpty(appSvcSuplmGroupDtoList)) {
            log.info("The AppSvcSuplmItemDto List is null!!!!");
            return IaisCommonUtils.genNewHashMap();
        }
        Map<String, AppSvcSuplmItemDto> itemMap = appSvcSuplmFormDto.genMap(true);
        Map<String, List<AppSvcSuplmItemDto>> radioBatchMap = appSvcSuplmFormDto.genRadioBatchMap(true);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        for (AppSvcSuplmGroupDto appSvcSuplmGroupDto : appSvcSuplmGroupDtoList) {
            int count = appSvcSuplmGroupDto.getCount();
            int minCount = appSvcSuplmGroupDto.getMinCount();
            int maxCount = appSvcSuplmGroupDto.getMaxCount();
            if (minCount==0&&maxCount==0){
                continue;
            }
            List<AppSvcSuplmItemDto> appSvcSuplmItemDtoList = appSvcSuplmGroupDto.getAppSvcSuplmItemDtoList();
            /*
             * check whether the group mandatory is zero or not
             */
            if (minCount == 0) {
                boolean allMatch = appSvcSuplmItemDtoList.stream()
                        .filter(dto -> !StringUtil.isIn(dto.getItemConfigDto().getItemType(), new String[]{
                                HcsaConsts.SUPFORM_ITEM_TYPE_TITLE,
                                HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE,
                                HcsaConsts.SUPFORM_ITEM_TYPE_LABEL
                        }))
                        .allMatch(dto -> StringUtil.isEmpty(dto.getInputValue()));
                if (allMatch) {
                    continue;
                }
            } else if (-1 != minCount && count < minCount) {
                errorMap.put(prefix + appSvcSuplmGroupDto.getGroupId(), validateMandatoryCount(appSvcSuplmGroupDto.getGroupName(), maxCount));
            }
            boolean multiple = count > 1;
            boolean isValid;
            List<String> psn = IaisCommonUtils.genNewArrayList();
            for (AppSvcSuplmItemDto appSvcSuplmItemDto : appSvcSuplmItemDtoList) {
                SuppleFormItemConfigDto itemConfigDto = appSvcSuplmItemDto.getItemConfigDto();
                isValid = true;
                int mandatoryType = itemConfigDto.getMandatoryType();
                String itemType = itemConfigDto.getItemType();
                String inputValue = appSvcSuplmItemDto.getInputValue();
                int seqNum = appSvcSuplmItemDto.getSeqNum();
                String errorKey = prefix + appSvcSuplmItemDto.getItemConfigId() + seqNum;
                if (HcsaConsts.SUPFORM_ITEM_TYPE_TEXT.equals(itemType)) {
                    if ( 1 == mandatoryType && StringUtil.isEmpty(inputValue)) {
                        errorMap.put(errorKey, "GENERAL_ERR0006");
                        isValid = false;
                    } else if (!validateSuplText(errorMap, itemConfigDto, inputValue, errorKey, getAdditionalCondition(itemMap,
                            itemConfigDto.getAdditionalCondition(), seqNum))) {
                        isValid = false;
                    } else if (HcsaConsts.SUPFORM_SPEC_COND_PRS.equals(appSvcSuplmItemDto.getSpecialCondition())) {
                        isValid = validateProfRegNo(errorMap, inputValue, errorKey);
                    }
                } else if (HcsaConsts.SUPFORM_ITEM_TYPE_RADIO.equals(itemType)) {
                    if ( 1 == mandatoryType && StringUtil.isEmpty(inputValue)) {
                        errorMap.put(errorKey, "GENERAL_ERR0006");
                        isValid = false;
                    }
                } else if (HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX.equals(itemType)) {
                    if ( 1 == mandatoryType && StringUtil.isEmpty(inputValue)) {
                        errorMap.put(errorKey, "GENERAL_ERR0006");
                        isValid = false;
                    }
                } else if (HcsaConsts.SUPFORM_ITEM_TYPE_SELECT.equals(itemType)) {
                    if ( 1 == mandatoryType && StringUtil.isEmpty(inputValue)) {
                        errorMap.put(errorKey, "GENERAL_ERR0006");
                        isValid = false;
                    }
                } else if (HcsaConsts.SUPFORM_ITEM_TYPE_BOLD.equals(itemType)){
                    if ( 1 == mandatoryType && StringUtil.isEmpty(inputValue)) {
                        errorMap.put(errorKey, "GENERAL_ERR0006");
                        isValid = false;
                    }
                }
                if (!checkConditonMandatory(itemMap, radioBatchMap, errorMap, appSvcSuplmItemDto, itemConfigDto, prefix)) {
                    isValid = false;
                }

                // check duplication
                if (isValid && multiple) {
                    String psnValue = StringUtil.toLowerCase(getPsnValue(appSvcSuplmItemDto));
                    if (!StringUtil.isEmpty(psnValue)) {
                        if (psn.contains(psnValue)) {
                            errorMap.put(errorKey, "NEW_ERR0012");
                        } else {
                            psn.add(psnValue);
                        }
                    }
                }
            }

        }
        return errorMap;
    }

    private static AppSvcSuplmItemDto getAdditionalCondition(Map<String, AppSvcSuplmItemDto> itemMap, String additionalCondition, int seqNum) {
        if (StringUtil.isEmpty(additionalCondition)) {
            return null;
        }
        return itemMap.get(additionalCondition + seqNum);
    }

    private static String getPsnValue(AppSvcSuplmItemDto appSvcSuplmItemDto) {
        String specialCondition = appSvcSuplmItemDto.getSpecialCondition();
        if (StringUtil.isEmpty(specialCondition)) {
            return "";
        }
        if (StringUtil.toLowerCase(specialCondition).contains("name")) {
            return appSvcSuplmItemDto.getInputValue();
        }
        return "";
    }

    private static boolean validateSuplText(Map<String, String> errorMap, SuppleFormItemConfigDto itemConfigDto, String inputValue,
            String errorKey, AppSvcSuplmItemDto srcSuplmItemDto) {
        if (StringUtil.isEmpty(inputValue)) {
            return true;
        }
        int size = errorMap.size();
        int maxLength = itemConfigDto.getMaxLength();
        if (maxLength > 0 && inputValue.length() > maxLength) {
            if (StringUtil.isEmpty(itemConfigDto.getDisplayInfo())){
                errorMap.put(errorKey, repLength(itemConfigDto.getRadioLabels(), String.valueOf(maxLength)));
            }else {
                errorMap.put(errorKey, repLength(itemConfigDto.getDisplayInfo(), String.valueOf(maxLength)));
            }
        }
        String dataType = Optional.ofNullable(itemConfigDto.getDataType()).orElse("");
        switch (dataType) {
            case HcsaConsts.SUPFORM_DATA_TYPE_INTEGER:
                if (!StringUtil.isDigit(inputValue)) {
                    //GENERAL_ERR0002 - Only numbers are allowed.
                    errorMap.put(errorKey, "GENERAL_ERR0002");
                }
                break;
            case HcsaConsts.SUPFORM_DATA_TYPE_INT_NOT_NEGATIVE:
                if (!StringUtil.isDigit(inputValue)) {
                    errorMap.put(errorKey, "GENERAL_ERR0002");
                } else {
                    int i = Integer.parseInt(inputValue);
                    if (i < 0) {
                        errorMap.put(errorKey, "GENERAL_ERR0074");
                    }
                }
                break;
            case HcsaConsts.SUPFORM_DATA_TYPE_INT_POSITIVE:
                if (!StringUtil.isDigit(inputValue)) {
                    errorMap.put(errorKey, "GENERAL_ERR0002");
                } else {
                    int i = Integer.parseInt(inputValue);
                    if (i <= 0) {
                        errorMap.put(errorKey, "GENERAL_ERR0075");
                    }
                }
                break;
            case HcsaConsts.SUPFORM_DATA_TYPE_DOUBLE:
                if (!StringUtil.isNumber(inputValue)) {
                    errorMap.put(errorKey, "GENERAL_ERR0002");
                }
                break;
            case HcsaConsts.SUPFORM_DATA_TYPE_DATE:
            case HcsaConsts.SUPFORM_DATA_TYPE_FUT_DATE_NOW:
            case HcsaConsts.SUPFORM_DATA_TYPE_PAST_DATE:
                if (!CommonValidator.isDate(inputValue)) {
                    //GENERAL_ERR0033 - Invalid Date Format.
                    errorMap.put(errorKey, "GENERAL_ERR0033");
                }
                break;
            case HcsaConsts.SUPFORM_DATA_TYPE_FUT_DATE:
                if (!CommonValidator.isDate(inputValue)) {
                    //GENERAL_ERR0033 - Invalid Date Format.
                    errorMap.put(errorKey, IaisEGPConstant.ERR_DATE_FORMAT);
                } else if (compareDateByDay(inputValue) <= 0) {
                    // GENERAL_ERR0026 - {field} must be a future date
                    errorMap.put(errorKey,
                            MessageUtil.replaceMessage("GENERAL_ERR0026", itemConfigDto.getDisplayInfo(), "field"));
                }
                break;
            case HcsaConsts.SUPFORM_DATA_TYPE_PAST_DATE_NOW:
                if (!CommonValidator.isDate(inputValue)) {
                    //GENERAL_ERR0033 - Invalid Date Format.
                    errorMap.put(errorKey, IaisEGPConstant.ERR_DATE_FORMAT);
                } else if (compareDateByDay(inputValue) >= 0) {
                    // DS_ERR001 - {{field} cannot be future date.
                    errorMap.put(errorKey,
                            MessageUtil.replaceMessage("DS_ERR001", itemConfigDto.getDisplayInfo(), "field"));
                }
                break;
            case HcsaConsts.SUPFORM_DATA_TYPE_DATE_LATER:
                if (!CommonValidator.isDate(inputValue)) {
                    //GENERAL_ERR0033 - Invalid Date Format.
                    errorMap.put(errorKey, IaisEGPConstant.ERR_DATE_FORMAT);
                } else if (srcSuplmItemDto != null && !isLater(inputValue, srcSuplmItemDto.getInputValue())) {
                    // GENERAL_ERR0081 - {date1} must be later than {date2}
                    Map<String, String> repMap = IaisCommonUtils.genNewHashMap(2);
                    repMap.put("date1", itemConfigDto.getDisplayInfo());
                    repMap.put("date2", srcSuplmItemDto.getItemConfigDto().getDisplayInfo());
                    errorMap.put(errorKey, MessageUtil.getMessageDesc("GENERAL_ERR0081", repMap));
                }
                break;

        }
        return size == errorMap.size();
    }

    private static boolean checkConditonMandatory(Map<String, AppSvcSuplmItemDto> itemMap,
            Map<String, List<AppSvcSuplmItemDto>> radioBatchMap,
            Map<String, String> errorMap, AppSvcSuplmItemDto appSvcSuplmItemDto, SuppleFormItemConfigDto itemConfigDto,
            String prefix) {
        if (StringUtil.isIn(appSvcSuplmItemDto.getItemConfigDto().getItemType(), new String[]{
                HcsaConsts.SUPFORM_ITEM_TYPE_TITLE,
                HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE,
                HcsaConsts.SUPFORM_ITEM_TYPE_LABEL})) {
            return true;
        }
        int mandatoryType = itemConfigDto.getMandatoryType();
        String inputValue = appSvcSuplmItemDto.getInputValue();
        if (!(2 == mandatoryType || 3 == mandatoryType || 5 == mandatoryType || 6 == mandatoryType || 7 == mandatoryType)
                || !StringUtil.isEmpty(inputValue)) {
            return true;
        }
        int seqNum = appSvcSuplmItemDto.getSeqNum();
        String radioBatchNum = itemConfigDto.getRadioBatchNum();
        String parentItemId = itemConfigDto.getParentItemId();
        String mandatoryCondition = appSvcSuplmItemDto.getMandatoryCondition();
        if (StringUtil.isEmpty(parentItemId)) {
            return true;
        }
        List<AppSvcSuplmItemDto> conditions = IaisCommonUtils.genNewArrayList();
        for (String id : parentItemId.split(AppConsts.DFT_DELIMITER)) {
            AppSvcSuplmItemDto condDto = itemMap.get(id + seqNum);
            if (condDto != null) {
                conditions.add(condDto);
            } else if (seqNum > 0) {
                // batch
                condDto = itemMap.get(id + 0);
                if (condDto != null) {
                    conditions.add(condDto);
                }
            } else if (5 == mandatoryType) {
                condDto = itemMap.get(id + 0);
                if (condDto != null) {
                    conditions.add(condDto);
                }
            }
        }
        if (conditions.isEmpty()) {
            return true;
        }
        boolean mandatory = false;
        for (AppSvcSuplmItemDto condDto : conditions) {
            if (StringUtil.isIn(condDto.getItemConfigDto().getItemType(), new String[]{
                    HcsaConsts.SUPFORM_ITEM_TYPE_TITLE,
                    HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE,
                    HcsaConsts.SUPFORM_ITEM_TYPE_LABEL})) {
                mandatory = condDto.getItemConfigDto().getMandatoryType() == 1;
            } else if (StringUtil.isNotEmpty(mandatoryCondition)) {
                String condValue = condDto.getInputValue();
                String[] codes = mandatoryCondition.split("#");
                mandatory = StringUtil.isIn(condValue, codes);
            }
            if (mandatory) {
                break;
            }
        }
        if (!mandatory) {
            return true;
        }
        boolean isValid = true;
        List<AppSvcSuplmItemDto> appSvcSuplmItemDtos = radioBatchMap.get(radioBatchNum + seqNum);
        if (IaisCommonUtils.isEmpty(appSvcSuplmItemDtos)) {
            errorMap.put(prefix + appSvcSuplmItemDto.getItemConfigId() + seqNum, "GENERAL_ERR0006");
            isValid = false;
        } else {
            if (appSvcSuplmItemDtos.stream().allMatch(dto -> StringUtil.isEmpty(dto.getInputValue()))) {
                AppSvcSuplmItemDto itemDto = appSvcSuplmItemDtos.stream()
                        .max(Comparator.comparingInt(dto -> dto.getItemConfigDto().getSeqNum()))
                        .orElse(appSvcSuplmItemDto);
                errorMap.put(prefix + itemDto.getItemConfigId() + itemDto.getSeqNum(), "GENERAL_ERR0006");
                isValid = false;
            }
        }
        return isValid;
    }

    public static int compareDateByDay(String date) {
        try {
            return Formatter.compareDateByDay(date);
        } catch (ParseException e) {
            return Integer.MAX_VALUE;
        }
    }

    private static boolean isLater(String target, String source) {
        int result = compareDateByDay(target, source);
        if (Integer.MAX_VALUE == result) {
            return true;
        }
        return result > 0;
    }

    private static int compareDateByDay(String target, String source) {
        try {
            return Formatter.compareDateByDay(target, source);
        } catch (ParseException e) {
            return Integer.MAX_VALUE;
        }
    }

    public static void doValidateSpecialServicesForm(List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoList,
            Map<String, String> errorMap, Map<String, AppSvcPersonAndExtDto> licPersonMap) {
        if (IaisCommonUtils.isEmpty(appSvcSpecialServiceInfoList)) {
            return;
        }
        String prefix = "";
        for (int i = 0; i < appSvcSpecialServiceInfoList.size(); i++) {
            List<SpecialServiceSectionDto> specialServiceSectionDtoList = appSvcSpecialServiceInfoList.get(i).getSpecialServiceSectionDtoList();
            for (int j = 0; j < specialServiceSectionDtoList.size(); j++) {
                SpecialServiceSectionDto specialServiceSectionDto = specialServiceSectionDtoList.get(j);
                List<String> sectionLeaderNames = IaisCommonUtils.genNewArrayList();
                List<String> nurseNames = IaisCommonUtils.genNewArrayList();
                List<String> rsoNames = IaisCommonUtils.genNewArrayList();
                List<String> svNames = IaisCommonUtils.genNewArrayList();
                List<String> mpNames = IaisCommonUtils.genNewArrayList();
                List<String> rpNames = IaisCommonUtils.genNewArrayList();
                List<String> dirNames = IaisCommonUtils.genNewArrayList();
                List<String> nurNames = IaisCommonUtils.genNewArrayList();
                List<String> roNames = IaisCommonUtils.genNewArrayList();
                List<String> mdNames = IaisCommonUtils.genNewArrayList();
                List<String> rtNames = IaisCommonUtils.genNewArrayList();
                List<String> cqmpNames = IaisCommonUtils.genNewArrayList();
                Map<String, Integer> minCount = specialServiceSectionDto.getMinCount();
                int cgoMandatoryCount = minCount.get(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                int slMandatoryCount = minCount.get(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                int nicMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE);
                int rsoMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER);
                int svMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_COMBINE);
                int mpMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST);
                int rpMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL);
                int diMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR);
                int nuMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR);
                int roMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_ONCOLOGIST);
                int mdMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_DOSIMETRIST);
                int rtMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_THERAPIST);
                int cqmpMandatoryCount = minCount.get(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_CQMP);
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = specialServiceSectionDto.getAppSvcCgoDtoList();
                if (appSvcCgoDtoList == null) {
                    if (cgoMandatoryCount > 0) {
                        errorMap.put(prefix + i + j + "cgo"+"personError"+0, "No related Personnel found!");
                    }
                } else if (appSvcCgoDtoList.size() < cgoMandatoryCount) {
                    errorMap.put(prefix + i + j + "cgo"+"personError"+0,
                            validateMandatoryCount(IaisCommonUtils.getPersonName(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, ApplicationHelper.isBackend()), cgoMandatoryCount));
                }else {
                    if (cgoMandatoryCount==0){
                        boolean empty=true;
                        for (AppSvcPrincipalOfficersDto keyPersonnelDto : appSvcCgoDtoList) {
                            boolean result = ReflectionUtil.isEmpty(keyPersonnelDto, "personnelType", "indexNo", "licPerson", "needSpcOptList","backend");
                            if (!result){
                                empty=false;
                                break;
                            }
                        }
                        if (!empty){
                            Map<String, String> map = validateKeyPersonnel(appSvcCgoDtoList,
                                    prefix + i + j + "cgo", licPersonMap, null, null, null, true);
                            if (IaisCommonUtils.isNotEmpty(map)) {
                                errorMap.putAll(map);
                            }
                        }else {
                            appSvcCgoDtoList.clear();
                        }
                    }else {
                        Map<String, String> map = validateKeyPersonnel(appSvcCgoDtoList,
                                prefix + i + j + "cgo", licPersonMap, null, null, null, true);
                        if (IaisCommonUtils.isNotEmpty(map)) {
                            errorMap.putAll(map);
                        }
                    }
                }
                List<AppSvcPersonnelDto> appSvcSectionLeaderList = specialServiceSectionDto.getAppSvcSectionLeaderList();
                if (appSvcSectionLeaderList == null) {
                    if (slMandatoryCount > 0) {
                        errorMap.put(prefix + i + j + "sl"+"personError"+0, "No related Personnel found!");
                    }
                } else if (appSvcSectionLeaderList.size() < slMandatoryCount) {
                    errorMap.put(prefix + i + j + "sl"+"personError"+0,
                            validateMandatoryCount(IaisCommonUtils.getPersonName(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, ApplicationHelper.isBackend()),slMandatoryCount));
                }else {
                    if (slMandatoryCount==0){
                        boolean empty=true;
                        for (AppSvcPersonnelDto svcPersonnelDto : appSvcSectionLeaderList) {
                            boolean result = ReflectionUtil.isEmpty(svcPersonnelDto, "personnelType", "indexNo", "prsLoading", "seqNum");
                            if (!result){
                                empty=false;
                                break;
                            }
                        }
                        if (!empty){
                            for (int x = 0; x < appSvcSectionLeaderList.size(); x++) {
                                specialValidate(errorMap, appSvcSectionLeaderList.get(x), prefix + i + j + "sl",
                                        x, sectionLeaderNames, true);
                            }
                        }else {
                            appSvcSectionLeaderList.clear();
                        }
                    }else {
                        for (int x = 0; x < appSvcSectionLeaderList.size(); x++) {
                            specialValidate(errorMap, appSvcSectionLeaderList.get(x), prefix + i + j + "sl",
                                    x, sectionLeaderNames, true);
                        }
                    }
                }
                validateSpecialPersonMandatory(specialServiceSectionDto.getAppSvcNurseDtoList(),
                        nicMandatoryCount,prefix + i + j + "nic", errorMap, nurseNames,ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE);
                validateSpecialServiceOtherPersonMandatory(specialServiceSectionDto.getAppSvcRadiationSafetyOfficerDtoList(),
                        rsoMandatoryCount,prefix + i + j + "rso",errorMap,rsoNames, ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER);
                validateSpecialServiceOtherPersonMandatory(specialServiceSectionDto.getAppSvcPersonnelDtoList(),
                        svMandatoryCount,prefix + i + j + "sv",errorMap,svNames, ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_COMBINE);
                validateSpecialServiceOtherPersonMandatory(specialServiceSectionDto.getAppSvcMedicalPhysicistDtoList(),
                        mpMandatoryCount,prefix + i + j + "mp",errorMap,mpNames, ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST);
                validateSpecialServiceOtherPersonMandatory(specialServiceSectionDto.getAppSvcRadiationPhysicistDtoList(),
                        rpMandatoryCount,prefix + i + j + "rp",errorMap,rpNames, ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL);
                validateSpecialPersonMandatory(specialServiceSectionDto.getAppSvcRadiationOncologist(),
                        roMandatoryCount,prefix + i + j + "ro", errorMap, roNames,ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_ONCOLOGIST);
                validateSpecialPersonMandatory(specialServiceSectionDto.getAppSvcMedicalDosimetrist(),
                        mdMandatoryCount,prefix + i + j + "md", errorMap, mdNames,ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_DOSIMETRIST);
                validateSpecialPersonMandatory(specialServiceSectionDto.getAppSvcRadiationTherapist(),
                        rtMandatoryCount,prefix + i + j + "rt", errorMap, rtNames,ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_THERAPIST);
                validateSpecialPersonMandatory(specialServiceSectionDto.getAppSvcRadiationCqmp(),
                        cqmpMandatoryCount,prefix + i + j + "cqmp", errorMap, cqmpNames,ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_CQMP);
                validateSpecialServicePersonMandatory(specialServiceSectionDto.getAppSvcDirectorDtoList(),
                        diMandatoryCount,prefix + i + j + "dir", errorMap, dirNames,ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR);
                validateSpecialServicePersonMandatory(specialServiceSectionDto.getAppSvcNurseDirectorDtoList(),
                        nuMandatoryCount,prefix + i + j + "nur", errorMap, nurNames,ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR);
                if (!IaisCommonUtils.isEmpty(specialServiceSectionDto.getAppSvcSuplmFormDto().getAppSvcSuplmGroupDtoList())) {
                    errorMap.putAll(doValidateSupplementaryForm(specialServiceSectionDto.getAppSvcSuplmFormDto(), prefix + i + j));
                }
            }
        }
    }

    private static void validateSpecialServiceOtherPersonMandatory(List<AppSvcPersonnelDto> appSvcPersonnelDto, int mandatoryCount,
            String prefix, Map<String, String> errorMap, List<String> usedNames,String psnType) {
        if (appSvcPersonnelDto == null) {
            if (mandatoryCount > 0) {
                errorMap.put(prefix+"personError"+0, "No related Personnel found!");
            }
        } else if (appSvcPersonnelDto.size() < mandatoryCount) {
            errorMap.put(prefix+"personError"+0, validateMandatoryCount(IaisCommonUtils.getPersonName(psnType, ApplicationHelper.isBackend()), mandatoryCount));
        }else {
            if (mandatoryCount==0){
                boolean empty=true;
                for (AppSvcPersonnelDto svcPersonnelDto : appSvcPersonnelDto) {
                    boolean result = ReflectionUtil.isEmpty(svcPersonnelDto, "personnelType", "indexNo", "prsLoading", "seqNum");
                    if (!result){
                        empty=false;
                        break;
                    }
                }
                if (!empty){
                    for (int x = 0; x < appSvcPersonnelDto.size(); x++) {
                        validateSpecialServiceOtherPersonDetail(appSvcPersonnelDto.get(x),
                                prefix, "" + x, errorMap, usedNames, psnType);
                    }
                }else {
                    appSvcPersonnelDto.clear();
                }
            }else {
                for (int x = 0; x < appSvcPersonnelDto.size(); x++) {
                    validateSpecialServiceOtherPersonDetail(appSvcPersonnelDto.get(x),
                            prefix, "" + x, errorMap, usedNames, psnType);
                }
            }
        }
    }

    private static void validateSpecialServicePersonMandatory(List<AppSvcPersonnelDto> appSvcPersonnelDto, int mandatoryCount,
            String prefix, Map<String, String> errorMap, List<String> usedNames,String psnType) {
        if (appSvcPersonnelDto == null) {
            if (mandatoryCount > 0) {
                errorMap.put(prefix+"personError"+0, "No related Personnel found!");
            }
        } else if (appSvcPersonnelDto.size() < mandatoryCount) {
            errorMap.put(prefix+"personError"+0, validateMandatoryCount(IaisCommonUtils.getPersonName(psnType, ApplicationHelper.isBackend()), mandatoryCount));
        }else {
            if (mandatoryCount==0){
                boolean empty=true;
                for (AppSvcPersonnelDto svcPersonnelDto : appSvcPersonnelDto) {
                    boolean result = ReflectionUtil.isEmpty(svcPersonnelDto, "personnelType", "indexNo", "prsLoading", "seqNum");
                    if (!result){
                        empty=false;
                        break;
                    }
                }
                if (!empty){
                    for (int x = 0; x < appSvcPersonnelDto.size(); x++) {
                        validateSpecialServicePersonDetail(appSvcPersonnelDto.get(x),
                                prefix, "" + x, errorMap, usedNames);
                    }
                }else {
                    appSvcPersonnelDto.clear();
                }
            }else {
                for (int x = 0; x < appSvcPersonnelDto.size(); x++) {
                    validateSpecialServicePersonDetail(appSvcPersonnelDto.get(x),
                            prefix, "" + x, errorMap, usedNames);
                }
            }
        }
    }

    private static void validateSpecialPersonMandatory(List<AppSvcPersonnelDto> appSvcPersonnelDto, int mandatoryCount,
                                                       String prefix, Map<String, String> errorMap, List<String> usedNames, String psnType) {
        if (appSvcPersonnelDto == null) {
            if (mandatoryCount > 0) {
                errorMap.put(prefix + "personError" + 0, "No related Personnel found!");
            }
        } else if (appSvcPersonnelDto.size() < mandatoryCount) {
            errorMap.put(prefix + "personError" + 0, validateMandatoryCount(IaisCommonUtils.getPersonName(psnType, ApplicationHelper.isBackend()), mandatoryCount));
        } else {
            if (mandatoryCount==0){
                boolean empty=true;
                for (AppSvcPersonnelDto svcPersonnelDto : appSvcPersonnelDto) {
                    boolean result = ReflectionUtil.isEmpty(svcPersonnelDto, "personnelType", "indexNo", "prsLoading", "seqNum");
                    if (!result){
                        empty=false;
                        break;
                    }
                }
                if (!empty){
                    for (int x = 0; x < appSvcPersonnelDto.size(); x++) {
                        validateSpecialPersonDetail(appSvcPersonnelDto.get(x),
                                prefix, "" + x, errorMap, usedNames, psnType);
                    }
                }else {
                    appSvcPersonnelDto.clear();
                }
            }else {
                for (int x = 0; x < appSvcPersonnelDto.size(); x++) {
                    validateSpecialPersonDetail(appSvcPersonnelDto.get(x),
                            prefix, "" + x, errorMap, usedNames, psnType);
                }
            }
       }
    }

    private static void validateSpecialServiceOtherPersonDetail(AppSvcPersonnelDto appSvcPersonnelDto, String prefix, String subfix,
            Map<String, String> errorMap, List<String> names, String psnType) {
        String signal = "GENERAL_ERR0006";

        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_COMBINE.equals(psnType)){
            String personnelSel = appSvcPersonnelDto.getPersonnelType();
            if (StringUtil.isEmpty(personnelSel)){
                errorMap.put(prefix + "personnelType" + subfix, signal);
            }
        }

        String salutation = appSvcPersonnelDto.getSalutation();
        if (!ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(psnType)){
            if (StringUtil.isEmpty(salutation)) {
                errorMap.put(prefix + "salutation" + subfix, signal);
            }
        }

        String name = appSvcPersonnelDto.getName();
        if (StringUtil.isEmpty(name)) {
            errorMap.put(prefix + "name" + subfix, signal);
        } else if (name.length() > 100) {
            String errorMsg = repLength("Name", "100");
            errorMap.put(prefix + "name" + subfix, errorMsg);
        } else {
            String target = StringUtil.toUpperCase(salutation + name);
            if (names.contains(target)) {
                errorMap.put(prefix + "name" + subfix, "NEW_ERR0012");
            } else {
                names.add(target);
            }
        }

        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(psnType)) {
            String qualification = appSvcPersonnelDto.getQualification();
            if (StringUtil.isEmpty(qualification)) {
                errorMap.put(prefix + "qualification" + subfix, signal);
            }else if (qualification.length() > 100) {
                errorMap.put(prefix + "qualification" + subfix, repLength("Qualification", "100"));
            }
            String wrkExpYear = appSvcPersonnelDto.getWrkExpYear();
            if (StringUtil.isEmpty(wrkExpYear)) {
                errorMap.put(prefix + "wrkExpYear" + subfix, signal);
            } else {
                if (wrkExpYear.length() > 2) {
                    errorMap.put(prefix + "wrkExpYear" + subfix, repLength("Working Experience (in terms of years)", "2"));
                }
                if (!wrkExpYear.matches("^[0-9]*$")) {
                    errorMap.put(prefix + "wrkExpYear" + subfix, "GENERAL_ERR0002");
                }
            }
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(psnType)) {
            String qualification = appSvcPersonnelDto.getQualification();
            if (StringUtil.isEmpty(qualification)) {
                errorMap.put(prefix + "qualification" + subfix, signal);
            }else if (qualification.length() > 100) {
                errorMap.put(prefix + "qualification" + subfix, repLength("Qualification", "100"));
            }
            String wrkExpYear = appSvcPersonnelDto.getWrkExpYear();
            if (StringUtil.isEmpty(wrkExpYear)) {
                errorMap.put(prefix + "wrkExpYear" + subfix, signal);
            } else {
                if (wrkExpYear.length() > 2) {
                    errorMap.put(prefix + "wrkExpYear" + subfix, repLength("Relevant working experience (Years)", "2"));
                }
                if (!wrkExpYear.matches("^[0-9]*$")) {
                    errorMap.put(prefix + "wrkExpYear" + subfix, "GENERAL_ERR0002");
                }
            }
        }
    }

    private static void validateSpecialPersonDetail(AppSvcPersonnelDto appSvcPersonnelDto, String prefix, String subfix,
                                                           Map<String, String> errorMap, List<String> names,String psnType) {
        String signal = "GENERAL_ERR0006";
        String salutation = appSvcPersonnelDto.getSalutation();
        if (StringUtil.isEmpty(salutation)) {
            errorMap.put(prefix + "salutation" + subfix, signal);
        }
        String name = appSvcPersonnelDto.getName();
        if (StringUtil.isEmpty(name)) {
            errorMap.put(prefix + "name" + subfix, signal);
        } else if (name.length() > 100) {
            String errorMsg = repLength("Name", "100");
            errorMap.put(prefix + "name" + subfix, errorMsg);
        } else {
            String target = StringUtil.toUpperCase(salutation + name);
            if (names.contains(target)) {
                errorMap.put(prefix + "name" + subfix, "NEW_ERR0012");
            } else {
                names.add(target);
            }
        }
        String employedBasis = appSvcPersonnelDto.getEmployedBasis();
        String regnNo = appSvcPersonnelDto.getRegnNo();

        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_ONCOLOGIST.equals(psnType)) {
            if (StringUtil.isEmpty(employedBasis)) {
                errorMap.put(prefix + "employedBasis" + subfix, signal);
            }
            String wrkExpYear = appSvcPersonnelDto.getWrkExpYear();
            if (StringUtil.isEmpty(wrkExpYear)) {
                errorMap.put(prefix + "wrkExpYear" + subfix, signal);
            } else {
                if (wrkExpYear.length() > 2) {
                    errorMap.put(prefix + "wrkExpYear" + subfix, repLength("Relevant working experience (Years)", "2"));
                }
                if (!wrkExpYear.matches("^[0-9]*$")) {
                    errorMap.put(prefix + "wrkExpYear" + subfix, "GENERAL_ERR0002");
                }
            }
            if (StringUtil.isEmpty(regnNo)) {
                errorMap.put(prefix + "regnNo" + subfix, signal);
            } else {
                if (regnNo.length() > 20) {
                    errorMap.put(prefix + "regnNo" + subfix, repLength("SMC Registration No.", "20"));
                }
            }
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_DOSIMETRIST.equals(psnType)){
            if (StringUtil.isEmpty(employedBasis)) {
                errorMap.put(prefix + "employedBasis" + subfix, signal);
            }
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_THERAPIST.equals(psnType)){
            if (StringUtil.isEmpty(employedBasis)) {
                errorMap.put(prefix + "employedBasis" + subfix, signal);
            }
            if (StringUtil.isEmpty(regnNo)) {
                errorMap.put(prefix + "regnNo" + subfix, signal);
            } else {
                if (regnNo.length() > 20) {
                    errorMap.put(prefix + "regnNo" + subfix, repLength("AHPC Registration No.", "20"));
                }
            }
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_CQMP.equals(psnType)){
            if (StringUtil.isEmpty(employedBasis)) {
                errorMap.put(prefix + "employedBasis" + subfix, signal);
            }
        }
        if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(psnType)){
            String profRegNo = appSvcPersonnelDto.getProfRegNo();
            if (StringUtil.isEmpty(profRegNo)) {
                errorMap.put(prefix + "profRegNo" + subfix, signal);
            } else if (profRegNo.length() > 20) {
                errorMap.put(prefix + "profRegNo" + subfix, repLength("Professional Regn. No.", "20"));
            }
        }
    }

    private static void validateSpecialServicePersonDetail(AppSvcPersonnelDto appSvcPersonnelDto, String prefix, String subfix,
            Map<String, String> errorMap, List<String> names) {
        String signal = "GENERAL_ERR0006";

        String salutation = appSvcPersonnelDto.getSalutation();
        if (StringUtil.isEmpty(salutation)) {
            errorMap.put(prefix + "salutation" + subfix, signal);
        }

        String name = appSvcPersonnelDto.getName();
        if (StringUtil.isEmpty(name)) {
            errorMap.put(prefix + "name" + subfix, signal);
        } else if (name.length() > 100) {
            String errorMsg = repLength("Name", "100");
            errorMap.put(prefix + "name" + subfix, errorMsg);
        } else {
            String target = StringUtil.toUpperCase(salutation + name);
            if (names.contains(target)) {
                errorMap.put(prefix + "name" + subfix, "NEW_ERR0012");
            } else {
                names.add(target);
            }
        }

        String designation = appSvcPersonnelDto.getDesignation();
        if (StringUtil.isEmpty(designation)) {
            errorMap.put(prefix + "designation" + subfix, signal);
        } else if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
            String otherDesignation = appSvcPersonnelDto.getOtherDesignation();
            if (StringUtil.isEmpty(otherDesignation)) {
                errorMap.put(prefix + "otherDesignation" + subfix,
                        MessageUtil.replaceMessage("GENERAL_ERR0006", "Others Designation", "field"));
            } else if (otherDesignation.length() > 100) {
                String errorMsg = repLength("Others Designation", "100");
                errorMap.put(prefix + "otherDesignation" + subfix, errorMsg);
            }
        }

        String professionBoard = appSvcPersonnelDto.getProfessionBoard();
        if (StringUtil.isEmpty(professionBoard)) {
            errorMap.put(prefix + "professionBoard" + subfix, signal);
        }

        String professionType = appSvcPersonnelDto.getProfessionType();
        if (StringUtil.isEmpty(professionType)) {
            errorMap.put(prefix + "professionType" + subfix, signal);
        }

        String profRegNo = appSvcPersonnelDto.getProfRegNo();
        if (StringUtil.isEmpty(profRegNo)) {
            errorMap.put(prefix + "profRegNo" + subfix, signal);
        } else if (profRegNo.length() > 20) {
            errorMap.put(prefix + "profRegNo" + subfix, repLength("Professional Regn. No.", "100"));
        }

        String typeOfCurrRegi = appSvcPersonnelDto.getTypeOfCurrRegi();
        if (StringUtil.isEmpty(typeOfCurrRegi)) {
            errorMap.put(prefix + "typeOfCurrRegi" + subfix, signal);
        } else if (typeOfCurrRegi.length() > 100) {
            errorMap.put(prefix + "typeOfCurrRegi" + subfix, repLength("Type of Current Registration", "50"));
        }

        //Current Registration Date
        String currRegiDate = appSvcPersonnelDto.getCurrRegiDate();
        if (StringUtil.isEmpty(currRegiDate)) {
            errorMap.put(prefix + "currRegiDate" + subfix, signal);
        } else if (!CommonValidator.isDate(currRegiDate)) {
            errorMap.put(prefix + "currRegiDate" + subfix, "GENERAL_ERR0033");
        }

        // Practicing Certificate End Date
        String praCerEndDateStr = appSvcPersonnelDto.getPraCerEndDate();
        if (StringUtil.isEmpty(praCerEndDateStr)) {
            errorMap.put(prefix + "praCerEndDate" + subfix, signal);
        } else if (!CommonValidator.isDate(praCerEndDateStr)) {
            errorMap.put(prefix + "praCerEndDate" + subfix, "GENERAL_ERR0033");
        }

        String typeOfRegister = appSvcPersonnelDto.getTypeOfRegister();
        if (StringUtil.isEmpty(typeOfRegister)) {
            errorMap.put(prefix + "typeOfRegister" + subfix, signal);
        } else if (typeOfRegister.length() > 100) {
            errorMap.put(prefix + "typeOfRegister" + subfix, repLength("Type of Register", "50"));
        }

        String specialityOther = appSvcPersonnelDto.getSpecialityOther();
        if (!StringUtil.isEmpty(specialityOther) && specialityOther.length() > 100) {
            errorMap.put(prefix + "specialityOther" + subfix, repLength("Other Specialties", "100"));
        }

        // Date when specialty was obtained
        // 86960
        String specialtyGetDate = appSvcPersonnelDto.getSpecialtyGetDate();
        if (!StringUtil.isEmpty(specialityOther) && StringUtil.isEmpty(specialtyGetDate)) {
            errorMap.put(prefix + "specialtyGetDate" + subfix, "GENERAL_ERR0006");
        } else if (!StringUtil.isEmpty(specialtyGetDate) && !CommonValidator.isDate(specialtyGetDate)){
            errorMap.put(prefix + "specialtyGetDate" + subfix, "GENERAL_ERR0033");
        }

        String wrkExpYear = appSvcPersonnelDto.getWrkExpYear();
        if (StringUtil.isEmpty(wrkExpYear)) {
            errorMap.put(prefix + "wrkExpYear" + subfix, signal);
        } else {
            if (wrkExpYear.length() > 2) {
                errorMap.put(prefix + "wrkExpYear" + subfix, repLength("Relevant working experience (Years) ", "2"));
            }
            if (!wrkExpYear.matches("^[0-9]*$")) {
                errorMap.put(prefix + "wrkExpYear" + subfix, "GENERAL_ERR0002");
            }
        }
    }


    public static void doVolidataPremises(List<AppGrpSecondAddrDto> appGrpSecondAddrDtoList, Map<String, String> errorMap,
                                   HttpServletRequest request, List<String> codeList) {
        if (IaisCommonUtils.isEmpty(appGrpSecondAddrDtoList)) {
            return;
        }
        List<String> addressList = IaisCommonUtils.genNewArrayList();
        List<String> addressLists = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, "appSubmissionDto");
        AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(0);
        StringBuilder mosdAddress = new StringBuilder();
        if (!StringUtil.isEmpty(appGrpPremisesDto)){
            mosdAddress.append(StringUtil.getNonNull(appGrpPremisesDto.getFloorNo()))
                    .append(StringUtil.getNonNull(appGrpPremisesDto.getBlkNo()))
                    .append(StringUtil.getNonNull(appGrpPremisesDto.getPostalCode()))
                    .append(StringUtil.getNonNull(appGrpPremisesDto.getUnitNo()));
        }
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
        if (IaisCommonUtils.isNotEmpty(appPremisesOperationalUnitDtos)){
            for (AppPremisesOperationalUnitDto appPremisesOperationalUnitDto : appPremisesOperationalUnitDtos) {
                mosdAddress.append(StringUtil.getNonNull(appPremisesOperationalUnitDto.getFloorNo()))
                        .append(StringUtil.getNonNull(appPremisesOperationalUnitDto.getUnitNo()));
            }
        }
        String id = null;
        if (!StringUtil.isEmpty(appSubmissionDto)) {
            List<AppGrpPremisesDto> dtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            if (IaisCommonUtils.isNotEmpty(dtoList)) {
                id = dtoList.get(0).getId();
            }
        }
        List<AppGrpSecondAddrDto> secondaryAddressesBypremiss = getAppCommService().getSecondaryAddressesBypremissId(id);
        secondaryAddressesBypremiss.forEach(e->addressList.add(e.getFloorNo() + e.getBlkNo() + e.getPostalCode()+ e.getUnitNo()+
                e.getAppPremisesOperationalUnitDtos().stream().map(item -> item.getFloorNo()+item.getUnitNo()).collect(Collectors.toList())));
        for (int i = 0; i < appGrpSecondAddrDtoList.size(); i++) {
            AppGrpSecondAddrDto appGrpSecondAddrDto = appGrpSecondAddrDtoList.get(i);
            appGrpSecondAddrDto.setAppGrpPremisesId(id);
            List<AppPremisesOperationalUnitDto> unitDtos = appGrpSecondAddrDto.getAppPremisesOperationalUnitDtos();
//            set uniid
            if (IaisCommonUtils.isNotEmpty(unitDtos)) {
                for (AppPremisesOperationalUnitDto unitDto : unitDtos) {
                    unitDto.setPremisesId(id);
                    unitDto.setSecondAddrId(appGrpSecondAddrDto.getId());
                }
            }
            addressList.forEach(e->{
                String replace = e.replace("[", "").replace("]", "");
                addressLists.add(replace);
            });
            addressLists.removeIf(item->Objects.equals(item,appGrpSecondAddrDto.getPreCode()));
            validateContactInfo(appGrpSecondAddrDto, errorMap, i, codeList,addressLists,mosdAddress.toString());
        }
    }

    private static void validateContactInfo(AppGrpSecondAddrDto appGrpSecondAddrDto, Map<String, String> errorMap,
            int i, List<String> codeList,List<String> addressList,String mosdAddress) {
        String postalCode = appGrpSecondAddrDto.getPostalCode();
        String buildingName = appGrpSecondAddrDto.getBuildingName();
        String streetName = appGrpSecondAddrDto.getStreetName();
        String addrType = appGrpSecondAddrDto.getAddrType();
        String blkNo = appGrpSecondAddrDto.getBlkNo();
        String blkNoKey = "blkNo" + i;
        if (!StringUtil.isEmpty(buildingName) && buildingName.length() > 66) {
            String errorMsg = repLength("Building Name", "66");
            errorMap.put("buildingName" + i, errorMsg);
        }
        if (StringUtil.isEmpty(streetName)) {
            errorMap.put("streetName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name", "field"));
        } else if (streetName.length() > 32) {
            errorMap.put("streetName" + i, repLength("Street Name", "32"));
        }

        if (StringUtil.isEmpty(addrType)) {
            errorMap.put("addrType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
        }
        boolean empty1 = StringUtil.isEmpty(blkNo);
        if (empty1 && ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {
            errorMap.put(blkNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
        } else if (!empty1 && blkNo.length() > 10) {
            String errorMsg = repLength("Block / House No.", "10");
            errorMap.put(blkNoKey, errorMsg);
        }
        StringBuilder content=new StringBuilder();
        content.append(appGrpSecondAddrDto.getFloorNo());
        content.append(blkNo);
        content.append(postalCode);
        content.append(appGrpSecondAddrDto.getUnitNo());
        if (IaisCommonUtils.isNotEmpty(appGrpSecondAddrDto.getAppPremisesOperationalUnitDtos())){
            for (int j = 0; j < appGrpSecondAddrDto.getAppPremisesOperationalUnitDtos().size(); j++) {
                AppPremisesOperationalUnitDto dto = appGrpSecondAddrDto.getAppPremisesOperationalUnitDtos().get(j);
                content.append(StringUtil.getNonNull(dto.getFloorNo())).append(StringUtil.getNonNull(dto.getUnitNo()));
            }
        }
        List<String> floorUnitList = IaisCommonUtils.genNewArrayList();
        validateOperaionUnits(appGrpSecondAddrDto, errorMap, i,floorUnitList,true);
        String postalCodeKey = "postalCode" + i;
        if (!StringUtil.isEmpty(postalCode)) {
            if (postalCode.length() > 6) {
                String errorMsg = repLength("Postal Code", "6");
                errorMap.put(postalCodeKey, errorMsg);
            } else if (postalCode.length() < 6) {
                errorMap.put(postalCodeKey, "NEW_ERR0004");
            } else if (!postalCode.matches("^[0-9]{6}$")) {
                errorMap.put(postalCodeKey, "NEW_ERR0004");
            } else if (codeList.contains(postalCode) || addressList.contains(content.toString()) || Objects.equals(content.toString(),mosdAddress)) {
                errorMap.put(postalCodeKey, "NEW_ACK010");
            }else {
                if (!floorUnitList.isEmpty()) {
                    for (String str : floorUnitList) {
                        String sb = postalCode + AppConsts.DFT_DELIMITER3 + str;
                        if (codeList.contains(sb)) {
                            // NEW_ACK010 - Please take note this premises address is licenced under another licensee.
                            errorMap.put(postalCodeKey, "There is a duplicated entry for this premises address");
                        } else {
                            codeList.add(sb);
                        }
                    }
                }
            }
        } else {
            errorMap.put(postalCodeKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code ", "field"));
        }
    }

    private static void validateOperaionUnits(AppGrpSecondAddrDto appGrpSecondAddrDto, Map<String, String> errorMap, int i,List<String> floorUnitList,boolean flag) {
        boolean addrTypeFlag = true;
        String floorNo = appGrpSecondAddrDto.getFloorNo();
        String unitNo = appGrpSecondAddrDto.getUnitNo();
        String blkNo = appGrpSecondAddrDto.getBlkNo();
        String addrType = appGrpSecondAddrDto.getAddrType();
        String floorNoKey = flag ? ApplicationHelper.getParamName(String.valueOf(i), "floorNo0") : ApplicationHelper.getParamName(String.valueOf(i), "FloorNos0");
        String unitNoKey = flag ? ApplicationHelper.getParamName(String.valueOf(i), "unitNo0") : ApplicationHelper.getParamName(String.valueOf(i), "UnitNos0");
        boolean empty = StringUtil.isEmpty(floorNo);
        boolean empty2 = StringUtil.isEmpty(unitNo);
        boolean isAptBlkType = ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType);
        if ((isAptBlkType || !empty2) && empty) {
            addrTypeFlag = false;
            errorMap.put(floorNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
        }
        if (!empty && floorNo.length() > 3) {
            addrTypeFlag = false;
            errorMap.put(floorNoKey, repLength("Floor No.", "3"));
        }
        if ((isAptBlkType || !empty) && empty2) {
            addrTypeFlag = false;
            errorMap.put(unitNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
        }
        if (!empty2 && unitNo.length() > 5) {
            addrTypeFlag = false;
            errorMap.put(unitNoKey, repLength("Unit No.", "5"));
        }
        if (addrTypeFlag) {
            String sb = StringUtil.getNonNull(floorNo) + AppConsts.DFT_DELIMITER3 +
                    StringUtil.getNonNull(blkNo) + AppConsts.DFT_DELIMITER3 + unitNo;
            floorUnitList.add(sb);
        }

        String floorErrName = i + (flag ?"FloorNo":"FloorNos0");
        String unitErrName = i + (flag ?"UnitNo":"UnitNos0");
        String floorUnitErrName = i + (flag?"FloorUnit":"FloorUnits");
        checkOperaionUnit(appGrpSecondAddrDto.getAppPremisesOperationalUnitDtos(), errorMap, floorErrName, unitErrName,
                floorUnitErrName, floorUnitList, appGrpSecondAddrDto);
    }

    private static void checkOperaionUnit(List<AppPremisesOperationalUnitDto> operationalUnitDtos, Map<String, String> errorMap,
                                          String floorErrName, String unitErrName, String floorUnitErrName, List<String> floorUnitList,
                                          AppGrpSecondAddrDto appGrpPremisesDto) {
        if (!IaisCommonUtils.isEmpty(operationalUnitDtos)) {
            int opIndex = 1;
            for (AppPremisesOperationalUnitDto operationalUnitDto : operationalUnitDtos) {
                boolean flag = true;
                String floorNo = operationalUnitDto.getFloorNo();
                String unitNo = operationalUnitDto.getUnitNo();
                boolean floorNoFlag = StringUtil.isEmpty(floorNo);
                boolean unitNoFlag = StringUtil.isEmpty(unitNo);
                if (!(floorNoFlag && unitNoFlag)) {
                    if (floorNoFlag) {
                        flag = false;
                        errorMap.put(floorErrName + opIndex, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
                    } else if (unitNoFlag) {
                        flag = false;
                        errorMap.put(unitErrName + opIndex, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
                    }
                }

                if (!floorNoFlag && floorNo.length() > 3) {
                    flag = false;
                    errorMap.put(floorErrName + opIndex, repLength("Floor No.", "3"));
                }

                if (!unitNoFlag && unitNo.length() > 5) {
                    flag = false;
                    errorMap.put(unitErrName + opIndex, repLength("Unit No.", "5"));
                }
                if (flag) {
                    if (!StringUtil.isEmpty(floorNo) && !StringUtil.isEmpty(unitNo)) {
                        String blkNo = appGrpPremisesDto.getBlkNo();
                        String floorUnitStr = floorNo + AppConsts.DFT_DELIMITER3 +
                                StringUtil.getNonNull(blkNo) + AppConsts.DFT_DELIMITER3 + unitNo;
                        if (floorUnitList.contains(floorUnitStr)) {
                            errorMap.put(floorUnitErrName + opIndex, "NEW_ERR0017");
                        } else {
                            floorUnitList.add(floorUnitStr);
                        }
                    }
                }
                opIndex++;
            }
        }
    }



    public static Map<String, String> doValidateRfi(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto,
            HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<String> premisesHciList = ApplicationHelper.checkPremisesHciList(appSubmissionDto.getLicenseeId(), true,
                oldAppSubmissionDto, false, request);
        doPreviewSubmitValidate(errorMap, premisesHciList, appSubmissionDto, request);
        return errorMap;
    }

    public static Map<String, String> doValidateRenewal(List<AppSubmissionDto> appSubmissionDtos, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (IaisCommonUtils.isEmpty(appSubmissionDtos)) {
            return errorMap;
        }
        AppSubmissionDto firstSubmissionDto = appSubmissionDtos.get(0);
        boolean isSingle = appSubmissionDtos.size() == 1;
        Date effectiveDate = firstSubmissionDto.getEffectiveDate();
        if (effectiveDate != null) {
            if (effectiveDate.before(new Date()) || effectiveDate.equals(new Date())) {
                errorMap.put("rfcEffectiveDate", "RFC_ERR012");
            }
        }
        if (isSingle) {
            String appType = ApplicationConsts.APPLICATION_TYPE_RENEWAL;
            AppDeclarationMessageDto appDeclarationMessageDto = firstSubmissionDto.getAppDeclarationMessageDto();
            DeclarationsUtil.declarationsValidate(errorMap, appDeclarationMessageDto, appType);
            String preQuesKindly = firstSubmissionDto.getAppDeclarationMessageDto().getPreliminaryQuestionKindly();
            validateDeclarationDoc(errorMap, ApplicationHelper.getFileAppendId(appType),
                    "0".equals(preQuesKindly), request);
            //check other eff
            if (errorMap.isEmpty()) {
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) request.getSession().getAttribute(
                        "oldRenewAppSubmissionDto");
                AppEditSelectDto appEditSelectDto = RfcHelper.rfcChangeModuleEvaluationDto(firstSubmissionDto, oldAppSubmissionDto);
                firstSubmissionDto.setChangeSelectDto(appEditSelectDto);
                List<AppGrpPremisesDto> appGrpPremisesDtoList = firstSubmissionDto.getAppGrpPremisesDtoList();
                if (appEditSelectDto.isPremisesEdit()) {
                    Set<String> premiseTypes = appGrpPremisesDtoList.stream().map(AppGrpPremisesDto::getPremisesType).collect(
                            Collectors.toSet());
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                        String[] selectedLicences = appGrpPremisesDto.getSelectedLicences();
                        List<LicenceDto> licenceDtos = null;
                        List<LicenceDto> existLicences = appGrpPremisesDto.getLicenceDtos();
                        if (IaisCommonUtils.isNotEmpty(existLicences)) {
                            licenceDtos = existLicences.stream()
                                    .filter(dto -> StringUtil.isIn(dto.getId(), selectedLicences))
                                    .collect(Collectors.toList());
                        }
                        if (IaisCommonUtils.isNotEmpty(licenceDtos)) {
                            for (LicenceDto licenceDto : licenceDtos) {
                                errorMap.putAll(validateLicences(licenceDto, premiseTypes, null));
                            }
                        }
                    }
                }
            }
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, RenewalConstants.PAGE_SWITCH, RenewalConstants.PAGE2);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return errorMap;
        }
        //check renew own
        Map<String, String> map = validateLicences(appSubmissionDtos, null);
        if (IaisCommonUtils.isNotEmpty(map)) {
            ParamUtil.setRequestAttr(request, RenewalConstants.PAGE_SWITCH, RenewalConstants.PAGE2);
            setErrorRequest(map, false, request);
            return errorMap;
        }
        if (!IaisCommonUtils.isEmpty(appSubmissionDtos)) {
            for (AppSubmissionDto submissionDto : appSubmissionDtos) {
                List<String> premisesHciList = null;
                if (isSingle) {
                    AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) request.getSession().getAttribute(
                            "oldRenewAppSubmissionDto");
                    premisesHciList = ApplicationHelper.checkPremisesHciList(submissionDto.getLicenseeId(),
                            false, oldAppSubmissionDto, false, request);
                }
                doPreviewSubmitValidate(errorMap, premisesHciList, submissionDto, request);
            }
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, RfcConst.SHOW_HEADING_SIGN, Boolean.TRUE);
            ParamUtil.setRequestAttr(request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, RenewalConstants.PAGE_SWITCH, RenewalConstants.PAGE2);
            return errorMap;
        }
        return errorMap;
    }


    public static LicenceDto doValidateRfc(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        String licenceId = appSubmissionDto.getLicenceId();
        LicenceDto licenceById = getLicCommService().getActiveLicenceById(licenceId);
        /*
          when use save it as draft in the previous, and the licence has been updated via other licence,
          the licence will not be valid any more, so when use do the it from the old draft,
          the licence will be null.
         */
        if (licenceById == null) {
            log.warn(StringUtil.changeForLog("Invalid selected Licence - " + licenceId));
            String errorMsg = MessageUtil.getMessageDesc("RFC_ERR023");
            ParamUtil.setRequestAttr(request, RfcConst.INVALID_LIC, errorMsg);
            return null;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Set<String> premiseTypes = null;
        if (appGrpPremisesDtoList != null) {
            premiseTypes = appGrpPremisesDtoList.stream().map(AppGrpPremisesDto::getPremisesType).collect(Collectors.toSet());
        }
        Map<String, String> errorMap = validateLicences(licenceById, premiseTypes, null);
        if (!errorMap.isEmpty()) {
            setErrorRequest(errorMap, false, request);
            return null;
        }
        AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(request);
        List<String> premisesHciList = ApplicationHelper.checkPremisesHciList(appSubmissionDto.getLicenseeId(),
                ApplicationHelper.checkIsRfi(request), oldAppSubmissionDto, false, request);
        doPreviewSubmitValidate(errorMap, premisesHciList, appSubmissionDto, request);
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, "Msg", errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return null;
        }
        return licenceById;
    }

    private static boolean isMandatory(Map<String, Integer> minPerson, Map<String, Integer> maxPerson, String psnType,AppSvcPersonnelDto appSvcPersonnelDto) {
        Integer arMin = minPerson.get(psnType);
        Integer arMax = maxPerson.get(psnType);
        if (StringUtil.isEmpty(arMax) || StringUtil.isEmpty(arMin)){
            return true;
        }
        return (arMin == 0 && arMax > 0) && ReflectionUtil.isEmpty(appSvcPersonnelDto,"personnelType", "indexNo", "prsLoading", "seqNum");
    }

}
