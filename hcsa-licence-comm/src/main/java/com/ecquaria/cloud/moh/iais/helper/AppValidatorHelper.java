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
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSecDetailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocumentShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremNonLicRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.CheckCoLocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
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
import com.ecquaria.cloud.moh.iais.validation.ValidateCharges;
import com.ecquaria.cloud.moh.iais.validation.ValidateClincalDirector;
import com.ecquaria.cloud.moh.iais.validation.ValidateVehicle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
        String general_err0041 = MessageUtil.getMessageDesc("GENERAL_ERR0041");
        if (length == 0) {
            repLength(general_err0041);
        } else if (length == 1) {
            String field = ars[0].replace("{field}", "field");
            field = field.replace("{maxlength}", "100");
            return field;
        } else if (length == 2) {
            Iterator<String> iterator = Arrays.stream(ars).iterator();
            if (iterator.hasNext()) {
                general_err0041 = general_err0041.replace("{field}", iterator.next());
            }
            if (iterator.hasNext()) {
                general_err0041 = general_err0041.replace("{maxlength}", iterator.next());
            }

            return general_err0041;
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
            general_err0041 = general_err0041.replace(ars0, ars1);
            general_err0041 = general_err0041.replace(ars2, ars3);
            return general_err0041;
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
        } else {
            return general_err0041;
        }

        return general_err0041;
    }

    /**
     * validate the all submission data
     *
     * @param bpc
     * @return
     */
    public static Map<String, String> doPreviewAndSumbit(BaseProcessClass bpc) {
        Map<String, String> previewAndSubmitMap = IaisCommonUtils.genNewHashMap();
        //
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(bpc.request);
        AppSubmissionDto oldAppSubmissionDto = ApplicationHelper.getOldAppSubmissionDto(bpc.request);
        ApplicationHelper.checkPremisesHciList(appSubmissionDto.getLicenseeId(), ApplicationHelper.checkIsRfi(bpc.request),
                oldAppSubmissionDto, false, bpc.request);
        previewAndSubmitMap = doPreviewSubmitValidate(previewAndSubmitMap, appSubmissionDto, bpc);
        return previewAndSubmitMap;
    }

    public static Map<String, String> doPreviewSubmitValidate(Map<String, String> previewAndSubmitMap,
            AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) {
        StringBuilder errorSvcConfig = new StringBuilder();
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        List<String> premisesHciList = (List<String>) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.PREMISES_HCI_LIST);
        List<String> errorList = doPreviewSubmitValidate(previewAndSubmitMap, appSubmissionDto, premisesHciList, isRfi,
                errorSvcConfig);
        HashMap<String, String> coMap = (HashMap<String, String>) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.CO_MAP);
        if (errorList.contains(HcsaAppConst.SECTION_LICENSEE)) {
            coMap.put(HcsaAppConst.SECTION_LICENSEE, "");
        } else {
            coMap.put(HcsaAppConst.SECTION_LICENSEE, HcsaAppConst.SECTION_LICENSEE);
        }
        if (errorList.contains(HcsaAppConst.SECTION_PREMISES)) {
            coMap.put(HcsaAppConst.SECTION_PREMISES, "");
        } else {
            coMap.put(HcsaAppConst.SECTION_PREMISES, HcsaAppConst.SECTION_PREMISES);
        }
        if (errorList.contains(HcsaAppConst.SECTION_SPECIALISED)) {
            coMap.put(HcsaAppConst.SECTION_SPECIALISED, "");
        } else {
            coMap.put(HcsaAppConst.SECTION_SPECIALISED, HcsaAppConst.SECTION_SPECIALISED);
        }
        if (errorList.contains(HcsaAppConst.SECTION_SVCINFO)) {
            coMap.put(HcsaAppConst.SECTION_SVCINFO, "");
        } else {
            coMap.put(HcsaAppConst.SECTION_SVCINFO, HcsaAppConst.SECTION_SVCINFO);
        }
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.CO_MAP, coMap);
        ParamUtil.setSessionAttr(bpc.request, "serviceConfig", errorSvcConfig.toString());
        return previewAndSubmitMap;
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
            List<String> premisesHciList, boolean isRfi, StringBuilder errorSvcConfig) {
        List<String> errorList = IaisCommonUtils.genNewArrayList();
        if (appSubmissionDto == null) {
            return errorList;
        }
        if (errorMap == null) {
            errorMap = IaisCommonUtils.genNewHashMap();
        }
        // sub licensee (licensee details)
        SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
        boolean isValid = validateSubLicenseeDto(errorMap, subLicenseeDto, null);
        if (!isValid) {
            errorList.add(HcsaAppConst.SECTION_LICENSEE);
        }
        // premises
        Map<String, String> premissMap = doValidatePremises(appSubmissionDto, premisesHciList, false);
        premissMap.remove("hciNameUsed");
        if (!premissMap.isEmpty()) {
            errorMap.putAll(premissMap);
            errorList.add(HcsaAppConst.SECTION_PREMISES);
        }
        // service info
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (int i = 0; i < dto.size(); i++) {
            AppSvcRelatedInfoDto currSvcInfoDto = dto.get(i);
            Map<String, String> map = doCheckBox(currSvcInfoDto, appSubmissionDto, null, errorList);
            if (!map.isEmpty()) {
                errorMap.putAll(map);
                if (!errorList.contains(HcsaAppConst.SECTION_SVCINFO)) {
                    errorList.add(HcsaAppConst.SECTION_SVCINFO);
                }
                if (errorSvcConfig != null) {
                    errorSvcConfig.append(currSvcInfoDto.getServiceId());
                }
            }
        }

        setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                appSubmissionDto.getLicenceNo());
        log.info(StringUtil.changeForLog("Error Message for App Submission Validation: " + errorMap));
        log.info(StringUtil.changeForLog("Error List for App Submission Validation: " + errorList));
        return errorList;
    }

    public static Map<String, String> doCheckBox(AppSvcRelatedInfoDto dto, AppSubmissionDto appSubmissionDto,
            Map<String, AppSvcPersonAndExtDto> licPersonMap) {
        return doCheckBox(dto, appSubmissionDto, licPersonMap, IaisCommonUtils.genNewArrayList());
    }

    private static Map<String, String> doCheckBox(AppSvcRelatedInfoDto dto, AppSubmissionDto appSubmissionDto,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, List<String> errorList) {
        return doCheckBox(dto, appSubmissionDto.getAppSvcRelatedInfoDtoList(), licPersonMap,
                appSubmissionDto.getAppGrpPremisesDtoList(), appSubmissionDto.getSubLicenseeDto(), errorList);
    }

    private static Map<String, String> doCheckBox(AppSvcRelatedInfoDto dto,
            List<AppSvcRelatedInfoDto> dtos,
            Map<String, AppSvcPersonAndExtDto> licPersonMap,
            List<AppGrpPremisesDto> appGrpPremisesDtos,
            SubLicenseeDto subLicenseeDto,
            List<String> errorList) {
        if (dto == null) {
            return IaisCommonUtils.genNewHashMap();
        }
        String serviceId = dto.getServiceId();
        /*if (StringUtil.isEmpty(serviceId) && !StringUtil.isEmpty(dto.getServiceName())) {
            HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceByServiceName(dto.getServiceName());
            if (serviceDto != null) {
                serviceId = serviceDto.getId();
                dto.setServiceId(serviceId);
                dto.setServiceCode(serviceDto.getSvcCode());
            }
        } else if (!StringUtil.isEmpty(serviceId) && StringUtil.isEmpty(dto.getServiceCode())) {
            HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
            if (serviceDto != null) {
                serviceId = serviceDto.getId();
                dto.setServiceId(serviceId);
                dto.setServiceCode(serviceDto.getSvcCode());
                dto.setServiceName(serviceDto.getSvcName());
            }
        }*/
        String prsFlag = ApplicationHelper.getPrsFlag();
        SystemParamConfig systemParamConfig = getSystemParamConfig();
        int uploadFileLimit = systemParamConfig.getUploadFileLimit();
        String sysFileType = systemParamConfig.getUploadFileType();

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
            if (HcsaConsts.STEP_BUSINESS_NAME.equals(currentStep)) {
                // business name
                List<AppSvcBusinessDto> appSvcBusinessDtoList = dto.getAppSvcBusinessDtoList();
                doValidateBusiness(appSvcBusinessDtoList, dto.getApplicationType(), dto.getLicenceId(), errorMap);
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_VEHICLES.equals(currentStep)) {
                // Vehicles
                List<String> ids = ApplicationHelper.getRelatedId(dto.getAppId(), dto.getLicenceId(),
                        dto.getServiceName());

                List<AppSvcVehicleDto> otherExistedVehicles = getAppCommService().getActiveVehicles(ids);
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
                        otherExistedVehicles);
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(currentStep)) {
                // Clinical Director
                String currSvcCode = dto.getServiceCode();
                if (StringUtil.isEmpty(currSvcCode)) {
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
                    currSvcCode = Optional.of(hcsaServiceDto).map(HcsaServiceDto::getSvcCode).orElseGet(() -> "");
                }
                List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = dto.getAppSvcClinicalDirectorDtoList();
                new ValidateClincalDirector().doValidateClincalDirector(errorMap, dto.getAppSvcClinicalDirectorDtoList(), licPersonMap,
                        currSvcCode);
                if (appSvcClinicalDirectorDtos != null && "Y".equals(prsFlag)) {
                    int i = 0;
                    for (AppSvcPrincipalOfficersDto person : appSvcClinicalDirectorDtos) {
                        if (!checkProfRegNo(person.getProfRegNo())) {
                            errorMap.put("profRegNo" + i, prsError);
                            break;
                        }
                        i++;
                    }
                }
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(currentStep)) {
                Map<String, String> govenMap = IaisCommonUtils.genNewHashMap();
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = dto.getAppSvcCgoDtoList();
                doAppSvcCgoDto(currentSvcAllPsnConfig, govenMap, appSvcCgoDtoList);
                if (govenMap.isEmpty()) {
                    govenMap.putAll(doValidateGovernanceOfficers(appSvcCgoDtoList, licPersonMap, false));
                }
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
            } else if (HcsaConsts.STEP_SECTION_LEADER.equals(currentStep)) {
                // Section Leader
                Map<String, String> map = validateSectionLeaders(dto.getAppSvcSectionLeaderList(), dto.getServiceCode());
                if (!map.isEmpty()) {
                    errorMap.putAll(map);
                }
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_CHARGES.equals(currentStep)) {
                new ValidateCharges().doValidateCharges(errorMap, dto.getAppSvcChargesPageDto());
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_SERVICE_PERSONNEL.equals(currentStep)) {
                List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.getAppSvcPersonnelDtoList();
                doValidateSvcPersonnel(errorMap, appSvcPersonnelDtoList, dto.getServiceCode());
                if (appSvcPersonnelDtoList != null && "Y".equals(prsFlag)) {
                    int i = 0;
                    for (AppSvcPersonnelDto person : appSvcPersonnelDtoList) {
                        if (!checkProfRegNo(person.getProfRegNo())) {
                            errorMap.put("regnNo" + i, prsError);
                            break;
                        }
                        i++;
                    }
                }
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(currentStep)) {
                List<AppSvcPrincipalOfficersDto> poList = dto.getAppSvcPrincipalOfficersDtoList();
                List<AppSvcPrincipalOfficersDto> dpoList = dto.getAppSvcNomineeDtoList();
                Map<String, String> map = doValidatePoAndDpo(poList, dpoList, dto.getDeputyPoFlag(), licPersonMap,
                        subLicenseeDto, false);
                if (!map.isEmpty()) {
                    errorMap.putAll(map);
                }
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(currentStep)) {
                List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderList = dto.getAppSvcKeyAppointmentHolderDtoList();
                Map<String, String> map = doValidateKeyAppointmentHolder(appSvcKeyAppointmentHolderList,
                        licPersonMap, false);
                if (!map.isEmpty()) {
                    errorMap.putAll(map);
                }
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_MEDALERT_PERSON.equals(currentStep)) {
                List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = dto.getAppSvcMedAlertPersonList();
                Map<String, String> map = doValidateMedAlertPsn(appSvcMedAlertPersonList, licPersonMap,
                        dto.getServiceCode());
                if (!map.isEmpty()) {
                    errorMap.putAll(map);
                }
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_DOCUMENTS.equals(currentStep)) {
                doValidateSvcDocuments(dto.getDocumentShowDtoList(), errorMap);
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
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
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcPersonnelDtoList(), errorMap,
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
                    .orElseGet(() -> null);
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

    private static void validatePersonMandatoryCount(List<?> list, Map<String, String> map, String psnType, Integer mandatoryCount,
            List<HcsaServiceStepSchemeDto> stepDtos, List<String> errorList) {
        boolean isValid = true;
        if (list == null) {
            if (mandatoryCount > 0) {
                map.put("error" + psnType, "No related Personnel found!");
                isValid = false;
            }
        } else if (list.size() < mandatoryCount) {
            String mandatoryErrMsg = MessageUtil.getMessageDesc("NEW_ERR0025");
            mandatoryErrMsg = mandatoryErrMsg.replace("{psnType}", ApplicationHelper.getName(psnType));
            mandatoryErrMsg = mandatoryErrMsg.replace("{mandatoryCount}", String.valueOf(mandatoryCount));
            map.put("error" + psnType, mandatoryErrMsg);
            isValid = false;
        }
        if (!isValid) {
            String stepName = getStepName(ApplicationHelper.getStep(psnType), stepDtos);
            addErrorStep(ApplicationHelper.getStep(psnType), stepName, true, errorList);
        }
    }

    public static Map<String, String> doValidatePremises(AppSubmissionDto appSubmissionDto, List<String> premisesHciList,
            boolean checkOthers) {
        //do validate one premiss
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        List<String> list = IaisCommonUtils.genNewArrayList();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Set<String> distinctVehicleNos = IaisCommonUtils.genNewHashSet();
        boolean needAppendMsg = false;
        String licenseeId = appSubmissionDto.getLicenseeId();
        String licenceId = appSubmissionDto.getLicenceId();
        String premiseTypeError = "";
        String selectPremises = "";
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
            String premiseType = appGrpPremisesDto.getPremisesType();
            boolean hciFlag = false;
            if (StringUtil.isEmpty(premiseType)) {
                if ("".equals(premiseTypeError)) {
                    premiseTypeError = MessageUtil.replaceMessage("GENERAL_ERR0006", "What is your premises type", "field");
                }
                errorMap.put("premisesType" + i, premiseTypeError);
            } else {
                String premisesSelect = appGrpPremisesDto.getPremisesSelect();
                String appType = appSubmissionDto.getAppType();
                if (StringUtil.isEmpty(premisesSelect) || "-1".equals(premisesSelect)) {
                    if ("".equals(selectPremises)) {
                        selectPremises = MessageUtil.replaceMessage("GENERAL_ERR0006", "Add or select a premises from the list",
                                "field");
                    }
                    errorMap.put("premisesSelect" + i, selectPremises);
                } else {
                    //List<String> floorUnitNo = new ArrayList<>(10);
                    List<String> floorUnitList = IaisCommonUtils.genNewArrayList();
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

                    if (ApplicationConsts.PREMISES_TYPE_PERMANENT.equals(premiseType)) {
                        String scdfRefNo = appGrpPremisesDto.getScdfRefNo();
                        if (!StringUtil.isEmpty(scdfRefNo) && scdfRefNo.length() > 66) {
                            errorMap.put("scdfRefNo" + i, repLength("Fire Safety & Shelter Bureau Ref. No.", "66"));
                        }
                        String certIssuedDtStr = appGrpPremisesDto.getCertIssuedDtStr();
                        if (!StringUtil.isEmpty(certIssuedDtStr) && !CommonValidator.isDate(certIssuedDtStr)) {
                            errorMap.put("certIssuedDt" + i, "GENERAL_ERR0033");
                        }
                        // Co-Location Services
                        validateCoLocation(errorMap, i, appGrpPremisesDto.getLocateWtihHcsa(),
                                appGrpPremisesDto.getLocateWtihNonHcsa(), appGrpPremisesDto.getAppPremNonLicRelationDtos());
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        String vehicleNo = appGrpPremisesDto.getVehicleNo();
                        if (appSubmissionDto.getAppSvcRelatedInfoDtoList().size() > 1) {
                            // GENERAL_ERR0072 - The {type} can't be applied to mutiple services.
                            errorMap.put("premisesType" + i, MessageUtil.replaceMessage("GENERAL_ERR0072",
                                    ApplicationConsts.PREMISES_TYPE_CONVEYANCE_SHOW, "type"));
                        }
                        AppSvcRelatedInfoDto dto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                        List<String> ids = ApplicationHelper.getRelatedId(dto.getAppId(), dto.getLicenceId(),
                                dto.getServiceName());
                        List<String> vehicles = getAppCommService().getActiveConveyanceVehicles(ids);
                        validateVehicleNo(errorMap, i, vehicleNo, distinctVehicleNos, vehicles);
                        // Co-Location Services
                        validateCoLocation(errorMap, i, appGrpPremisesDto.getLocateWtihHcsa(),
                                appGrpPremisesDto.getLocateWtihNonHcsa(), appGrpPremisesDto.getAppPremNonLicRelationDtos());
                    } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premiseType)) {
                        String easMtsUseOnly = appGrpPremisesDto.getEasMtsUseOnly();
                        String easMtsPubHotline = appGrpPremisesDto.getEasMtsPubHotline();
                        String email = appGrpPremisesDto.getEasMtsPubEmail();
                        // "Public Hotline"
                        if (StringUtil.isEmpty(easMtsPubHotline)) {
                            if (!"UOT002".equals(easMtsUseOnly)) {
                                errorMap.put("easMtsPubHotline" + i, MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                            }
                        } else if (!easMtsPubHotline.matches("^[6|8|9][0-9]{7}$")) {
                            errorMap.put("easMtsPubHotline" + i, MessageUtil.getMessageDesc("GENERAL_ERR0007"));
                        }
                        if (StringUtil.isEmpty(email)) {
                            if (!"UOT002".equals(easMtsUseOnly)) {
                                errorMap.put("easMtsPubEmail" + i, MessageUtil.replaceMessage("GENERAL_ERR0006",
                                        "Email ", "field"));
                            }
                        } else if (email.length() > 320) {
                            errorMap.put("easMtsPubEmail" + i, repLength("Email", "320"));
                        } else if (!ValidationUtils.isEmail(email)) {
                            errorMap.put("easMtsPubEmail" + i, MessageUtil.getMessageDesc("GENERAL_ERR0014"));
                        }
                    }

                    Map<String, String> map = validateContactInfo(appGrpPremisesDto, i, floorUnitList, list);
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
            if (checkOthers) {
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
                appGrpPremisesDto.setHasError(true);
                errMap.putAll(errorMap);
            } else {
                appGrpPremisesDto.setHasError(false);
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
            hciNameChanged = AppValidatorHelper.checkNameChanged(hciName, null, licenceId);
        }
        if (2 == hciNameChanged || 4 == hciNameChanged) {
            //no need validate hci name have keyword (is migrated and hci name never changed)
        } else {
            Map<Integer, String> map = AppValidatorHelper.checkBlacklist(hciName);
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
        } else if (AppConsts.YES.equals(locateWtihNonHcsa)) {
            if (IaisCommonUtils.isEmpty(appPremNonLicRelationDtos)) {
                errorMap.put(index + "CoBusinessName0", "GENERAL_ERR0006");
                errorMap.put(index + "CoSvcName0", "GENERAL_ERR0006");
            } else {
                int i = 0;
                for (AppPremNonLicRelationDto appPremNonLicRelationDto : appPremNonLicRelationDtos) {
                    String coBusinessName = appPremNonLicRelationDto.getBusinessName();
                    String coSvcName = appPremNonLicRelationDto.getProvidedService();
                    if (StringUtil.isEmpty(coBusinessName) && StringUtil.isEmpty(coSvcName)) {
                        continue;
                    }
                    if (StringUtil.isEmpty(coBusinessName)) {
                        errorMap.put(index + "CoBusinessName" + i, "GENERAL_ERR0006");
                    } else if (coBusinessName.length() > 100) {
                        errorMap.put(index + "CoBusinessName" + i, repLength("Business Name", "100"));
                    }
                    if (StringUtil.isEmpty(coSvcName)) {
                        errorMap.put(index + "CoSvcName" + i, "GENERAL_ERR0006");
                    } else if (coSvcName.length() > 100) {
                        errorMap.put(index + "CoSvcName" + i, repLength("Services Provided", "100"));
                    }
                    i++;
                }

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
                String general_err0041 = repLength("Vehicle No.", "10");
                errorMap.put("vehicleNo" + index, general_err0041);
            }
            boolean b = VehNoValidator.validateNumber(vehicleNo);
            if (!b) {
                errorMap.put("vehicleNo" + index, "GENERAL_ERR0017");
            }

            if (distinctVehicleNos.contains(vehicleNo)) {
                // NEW_ERR0012 - This is a repeated entry
                errorMap.put("vehicleNo" + index, "NEW_ERR0012");
            } else if (vehicles.contains(vehicleNo)) {
                // NEW_ERR0016 - This record already exists.
                errorMap.put("vehicleNo" + index, "NEW_ERR0016");
            } else {
                distinctVehicleNos.add(vehicleNo);
            }
        }

    }

    private static Map<String, String> validateContactInfo(AppGrpPremisesDto appGrpPremisesDto, int i, List<String> floorUnitList,
            List<String> list) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String postalCode = appGrpPremisesDto.getPostalCode();
        String buildingName = appGrpPremisesDto.getBuildingName();
        String streetName = appGrpPremisesDto.getStreetName();
        String addrType = appGrpPremisesDto.getAddrType();
        String blkNo = appGrpPremisesDto.getBlkNo();
        String blkNoKey = "blkNo" + i;

        if (!StringUtil.isEmpty(buildingName) && buildingName.length() > 66) {
            String general_err0041 = repLength("Building Name", "66");
            errorMap.put("buildingName" + i, general_err0041);
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
            String general_err0041 = repLength("Block / House No.", "10");
            errorMap.put(blkNoKey, general_err0041);
        }
        // validate floor and units
        validateOperaionUnits(appGrpPremisesDto, i, floorUnitList, errorMap);
        String postalCodeKey = "postalCode" + i;
        if (!StringUtil.isEmpty(postalCode)) {
            if (postalCode.length() > 6) {
                String general_err0041 = repLength("Postal Code", "6");
                errorMap.put(postalCodeKey, general_err0041);
            } else if (postalCode.length() < 6) {
                errorMap.put(postalCodeKey, "NEW_ERR0004");
            } else if (!postalCode.matches("^[0-9]{6}$")) {
                errorMap.put(postalCodeKey, "NEW_ERR0004");
            } else {
                if (!floorUnitList.isEmpty()) {
                    List<String> sbList = new ArrayList<>();
                    for (String str : floorUnitList) {
                        String sb = new StringBuilder(postalCode).append(AppConsts.DFT_DELIMITER).append(str).toString();
                        if (list.contains(sb)) {
                            // NEW_ACK010 - Please take note this premises address is licenced under another licensee.
                            errorMap.put(postalCodeKey, "NEW_ACK010");
                        } else {
                            sbList.add(sb);
                        }
                    }
                    list.addAll(sbList);
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
        //String premisesType = appGrpPremisesDto.getPremisesType();
        String diff = "";
        String prefix = "";
        String floorNo = appGrpPremisesDto.getFloorNo();
        String unitNo = appGrpPremisesDto.getUnitNo();
        String blkNo = appGrpPremisesDto.getBlkNo();
        String addrType = appGrpPremisesDto.getAddrType();
        appGrpPremisesDto.setFloorNo(ApplicationHelper.handleFloorNo(floorNo));
        String floorNoKey = ApplicationHelper.getParamName(String.valueOf(i), "floorNo0");
        String unitNoKey = ApplicationHelper.getParamName(String.valueOf(i), "unitNo0");
        //String blkNoKey = ApplicationHelper.getParamName(prefix, "blkNo" + i);
        boolean empty = StringUtil.isEmpty(floorNo);
        //boolean empty1 = StringUtil.isEmpty(blkNo);
        boolean empty2 = StringUtil.isEmpty(unitNo);
        boolean isAptBlkType = ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType);
        if ((isAptBlkType || !empty2) && empty) {
            addrTypeFlag = false;
            errorMap.put(floorNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
        }
        if (!empty && floorNo.length() > 3) {
            addrTypeFlag = false;
            String general_err0041 = repLength("Floor No.", "3");
            errorMap.put(floorNoKey, general_err0041);
        }
        if ((isAptBlkType || !empty) && empty2) {
            addrTypeFlag = false;
            errorMap.put(unitNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
        }
        if (!empty2 && unitNo.length() > 5) {
            addrTypeFlag = false;
            String general_err0041 = repLength("Unit No.", "5");
            errorMap.put(unitNoKey, general_err0041);
        }
        /*if (isAptBlkType && empty1) {
            addrTypeFlag = false;
            errorMap.put(blkNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
        } else if (!empty1 && blkNo.length() > 10) {
            String general_err0041 = repLength("Block / House No.", "10");
            errorMap.put(blkNoKey, general_err0041);
        }*/
        if (addrTypeFlag) {
            String floorNoErr = errorMap.get(floorNoKey);
            StringBuilder sb = new StringBuilder();
            sb.append(ApplicationHelper.handleFloorNo(floorNo, floorNoErr)).append(AppConsts.DFT_DELIMITER)
                    .append(StringUtil.getNonNull(blkNo)).append(AppConsts.DFT_DELIMITER).append(unitNo);
            floorUnitList.add(sb.toString());
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
                    String general_err0041 = repLength("Floor No.", "3");
                    errorMap.put(floorErrName + opIndex, general_err0041);
                }

                if (!unitNoFlag && unitNo.length() > 5) {
                    flag = false;
                    String general_err0041 = repLength("Unit No.", "5");
                    errorMap.put(unitErrName + opIndex, general_err0041);
                }
                String floorNoErr = errorMap.get(floorErrName + opIndex);
                operationalUnitDto.setFloorNo(ApplicationHelper.handleFloorNo(floorNo, floorNoErr));
                if (flag) {
                    if (!StringUtil.isEmpty(operationalUnitDto.getFloorNo()) && !StringUtil.isEmpty(operationalUnitDto.getUnitNo())) {
                        String floorUnitStr = operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo();
                        if (floorUnitList.contains(floorUnitStr)) {
                            errorMap.put(floorUnitErrName + opIndex, "NEW_ERR0017");
                        } else {
                            floorUnitList.add(floorUnitStr);
                        }
                        String blkNo = appGrpPremisesDto.getBlkNo();
                        StringBuilder sb = new StringBuilder();
                        sb.append(ApplicationHelper.handleFloorNo(floorNo, floorNoErr)).append(AppConsts.DFT_DELIMITER)
                                .append(StringUtil.getNonNull(blkNo)).append(AppConsts.DFT_DELIMITER).append(unitNo);
                        floorUnitList.add(sb.toString());
                    }
                }
                opIndex++;
            }
        }

    }

    public static boolean validateSubLicenseeDto(Map<String, String> errorMap, SubLicenseeDto subLicenseeDto,
            HttpServletRequest request) {
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
            return map.isEmpty();
        } else {
            if (errorMap != null) {
                errorMap.putAll(map);
            }
            return map.isEmpty();
        }
    }

    public static Map<String, String> doValidatePoAndDpo(List<AppSvcPrincipalOfficersDto> poList,
            List<AppSvcPrincipalOfficersDto> dpoList, String deputySelect, Map<String, AppSvcPersonAndExtDto> licPersonMap,
            SubLicenseeDto subLicenseeDto, boolean checkPRS) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        List<String> psnList = IaisCommonUtils.genNewArrayList();
        map.putAll(validateKeyPersonnel(poList, "", licPersonMap, psnList, subLicenseeDto, null, checkPRS));
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
                                          Map<String, AppSvcPersonAndExtDto> licPersonMap, boolean checkPRS) {
        if (appSvcClinicalDirectorList == null) {
            return new HashMap<>(1);
        }
        return validateKeyPersonnel(appSvcClinicalDirectorList, "", licPersonMap, checkPRS);
    }

    public static Map<String, String> doValidateMedAlertPsn(List<AppSvcPrincipalOfficersDto> medAlertPsnDtos,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, String svcCode) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if (IaisCommonUtils.isEmpty(medAlertPsnDtos)) {
            return errMap;
        }
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < medAlertPsnDtos.size(); i++) {
            String assignSelect = medAlertPsnDtos.get(i).getAssignSelect();
            if ("-1".equals(assignSelect) || StringUtil.isEmpty(assignSelect)) {
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
                    if (name.length() > 110) {
                        String general_err0041 = repLength("Name", "110");
                        errMap.put("name" + i, general_err0041);
                    }
                }

                String mobileNo = medAlertPsnDtos.get(i).getMobileNo();
                if (StringUtil.isEmpty(mobileNo)) {
                    errMap.put("mobileNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Mobile No. ", "field"));
                } else if (!StringUtil.isEmpty(mobileNo)) {
                    if (mobileNo.length() > 8) {
                        String general_err0041 = repLength("Mobile No.", "8");
                        errMap.put("mobileNo" + i, general_err0041);
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
                        String general_err0041 = repLength("Email Address", "320");
                        errMap.put("emailAddr" + i, general_err0041);
                    }
                    if (!ValidationUtils.isEmail(emailAddr)) {
                        errMap.put("emailAddr" + i, "GENERAL_ERR0014");
                    } else if (emailAddr.length() > 66) {

                    }
                }

            }
        }
        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
        return errMap;
    }

    private static List<String> getOtherScopeChildrenIdList(List<HcsaSvcSubtypeOrSubsumedDto> scopeConfigDtoList) {
        List<String> otherScopeChildrenList = IaisCommonUtils.genNewArrayList();
        HcsaSvcSubtypeOrSubsumedDto otherScopeConfigDto = null;
        if (!IaisCommonUtils.isEmpty(scopeConfigDtoList)) {
            for (HcsaSvcSubtypeOrSubsumedDto scopeConfigDto : scopeConfigDtoList) {
                if (HcsaAppConst.SERVICE_SCOPE_LAB_OTHERS.equals(scopeConfigDto.getName())) {
                    otherScopeConfigDto = scopeConfigDto;
                    break;
                }
            }
            if (otherScopeConfigDto != null) {
                List<HcsaSvcSubtypeOrSubsumedDto> otherScopeChildrenDtoList = otherScopeConfigDto.getList();
                if (!IaisCommonUtils.isEmpty(otherScopeChildrenDtoList)) {
                    for (HcsaSvcSubtypeOrSubsumedDto otherScopeChildrenDto : otherScopeChildrenDtoList) {
                        otherScopeChildrenList.add(otherScopeChildrenDto.getId());
                    }
                }

            }
        }
        return otherScopeChildrenList;
    }

    private static boolean selectOtherScope(List<AppSvcChckListDto> appSvcChckListDtos) {
        boolean flag = false;
        if (!IaisCommonUtils.isEmpty(appSvcChckListDtos)) {
            for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtos) {
                if (HcsaAppConst.SERVICE_SCOPE_LAB_OTHERS.equals(appSvcChckListDto.getChkName())) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
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
        String psnType = null;
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if (psnList == null) {
            psnList = IaisCommonUtils.genNewArrayList();
        }
        List<String> assignList = new ArrayList<>();
        for (int i = 0; i < personList.size(); i++) {
            AppSvcPrincipalOfficersDto person = personList.get(i);
            psnType = person.getPsnType();
            String assignSelect = person.getAssignSelect();
            if ("-1".equals(assignSelect) || StringUtil.isEmpty(assignSelect)) {
                errMap.put(prefix + "assignSelect" + i, "GENERAL_ERR0006");
            } else {
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
                        if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType) && subLicenseeDto != null
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
                if (isValid) {
                    if (assignList.contains(assignSelect)) {
                        errMap.put("assignSelect" + i, "NEW_ERR0012");
                    } else if (!HcsaAppConst.NEW_PSN.equals(assignSelect)) {
                        assignList.add(assignSelect);
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
                    if (name.length() > 110) {
                        String general_err0041 = repLength("Name", "110");
                        errMap.put(prefix + "name" + i, general_err0041);
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
                        String general_err0041 = repLength("Others Designation", "100");
                        errMap.put(prefix + "otherDesignation" + i, general_err0041);
                    }

                }

                String professionType = person.getProfessionType();
                String professionalRegoNo = person.getProfRegNo();
                String typeOfCurrRegi = person.getTypeOfCurrRegi();
                String currRegiDate = person.getCurrRegiDateStr();
                String praCerEndDate = person.getPraCerEndDateStr();
                String typeOfRegister = person.getTypeOfRegister();
                String otherQualification = person.getOtherQualification();
                if (StringUtil.isIn(psnType, new String[]{ApplicationConsts.PERSONNEL_PSN_TYPE_CGO})) {
                    if (StringUtil.isEmpty(professionType)) {
                        errMap.put(prefix + "professionType" + i,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Professional Type ", "field"));
                    }
                    if (StringUtil.isEmpty(professionalRegoNo)) {
                        errMap.put(prefix + "profRegNo" + i, "GENERAL_ERR0006");
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
                    if (StringUtil.isEmpty(otherQualification)) {
                        errMap.put(prefix + "otherQualification" + i, "GENERAL_ERR0006");
                    }
                }

                if (StringUtil.isIn(psnType, new String[]{ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR})) {
                    if (StringUtil.isEmpty(professionalRegoNo)) {
                        errMap.put(prefix + "profRegNo" + i, "GENERAL_ERR0006");
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

                }

                if (!StringUtil.isEmpty(professionalRegoNo)) {
                    if (professionalRegoNo.length() > 20) {
                        errMap.put(prefix + "profRegNo" + i, repLength("Professional Regn. No.", "20"));
                    } else if (checkPRS) {
                        validateProfRegNo(errMap, professionalRegoNo, "profRegNo" + i);
                    }
                }
                if (StringUtil.isNotEmpty(typeOfCurrRegi) && typeOfCurrRegi.length() > 50) {
                    errMap.put(prefix + "typeOfCurrRegi" + i, repLength("Type of Registration Date", "50"));
                }
                //Current Registration Date
                if (StringUtil.isNotEmpty(currRegiDate) && !CommonValidator.isDate(currRegiDate)) {
                    errMap.put(prefix + "currRegiDate" + i, "GENERAL_ERR0033");
                }
                if (StringUtil.isNotEmpty(praCerEndDate) && !CommonValidator.isDate(praCerEndDate)) {
                    errMap.put(prefix + "praCerEndDate" + i, "GENERAL_ERR0033");
                }
                if (StringUtil.isNotEmpty(typeOfRegister) && typeOfRegister.length() > 50) {
                    errMap.put(prefix + "typeOfRegister" + i, AppValidatorHelper.repLength("Type of Register", "50"));
                }
                String specialityOther = person.getSpecialityOther();
                if (StringUtil.isNotEmpty(specialityOther) && specialityOther.length() > 100) {
                    errMap.put(prefix + "otherQualification" + i, repLength("Other Specialities", "100"));
                }
                String specialtyGetDate = person.getSpecialtyGetDateStr();
                if (StringUtil.isNotEmpty(specialtyGetDate) && !CommonValidator.isDate(praCerEndDate)) {
                    errMap.put(prefix + "specialtyGetDate" + i, "GENERAL_ERR0006");
                } else if (!CommonValidator.isDate(praCerEndDate)) {
                    errMap.put(prefix + "specialtyGetDate" + i, "GENERAL_ERR0033");
                }
                if (StringUtil.isNotEmpty(otherQualification) && otherQualification.length() > 100) {
                    errMap.put(prefix + "otherQualification" + i, repLength("Other Qualification", "100"));
                }
                if (StringUtil.isIn(psnType, new String[]{ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR})) {
                    String holdCerByEMS = person.getHoldCerByEMS();
                    if (StringUtil.isEmpty(holdCerByEMS)) {
                        errMap.put("holdCerByEMS" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "holdCerByEMS", "field"));
                    } else if (AppConsts.NO.equals(holdCerByEMS)) {
                        errMap.put("holdCerByEMS" + i, MessageUtil.getMessageDesc("NEW_ERR0031"));
                    }
                }
                String aclsExpiryDate = person.getAclsExpiryDateStr();
                if (StringUtil.isNotEmpty(aclsExpiryDate) && aclsExpiryDate.length() > 100) {
                    errMap.put(prefix + "aclsExpiryDate" + i, repLength("Expiry Date (ACLS)", "100"));
                }
                String relevantExperience = person.getRelevantExperience();
                if (StringUtil.isNotEmpty(relevantExperience) && relevantExperience.length() > 100) {
                    errMap.put(prefix + "relevantExperience" + i, repLength("Relevant Experience", "100"));
                }
                String bclsExpiryDate = person.getBclsExpiryDateStr();
                if (StringUtil.isNotEmpty(bclsExpiryDate) && bclsExpiryDate.length() > 100) {
                    errMap.put(prefix + "bclsExpiryDate" + i, repLength("Expiry Date (BCLS and AED)", "100"));
                }
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
        return errMap;
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
            String fileType = filename.substring(filename.lastIndexOf('.') + 1);
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

    public static void validatePH(Map<String, String> errorMap, AppSubmissionDto appSubmissionDto) {
        /*List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if (appGrpPremisesDtoList != null) {
            for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                String premisesType = appGrpPremisesDtoList.get(i).getPremisesType();
                String s = "";
                if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)) {
                    s = "conveyance";
                } else if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)) {
                    s = "onSite";
                } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)) {
                    s = "offSite";
                }
                List<OperationHoursReloadDto> weeklyDtoList = appGrpPremisesDtoList.get(i).getWeeklyDtoList();
                List<OperationHoursReloadDto> phDtoList = appGrpPremisesDtoList.get(i).getPhDtoList();
                List<AppPremEventPeriodDto> eventDtoList = appGrpPremisesDtoList.get(i).getEventDtoList();
                validate(phDtoList, errorMap, i, s + "PubHoliday");
                validate(weeklyDtoList, errorMap, i, s + "Weekly");
                validateEvent(eventDtoList, errorMap, i, s + "Event");
            }
        }*/
    }

    public static void validate(List<OperationHoursReloadDto> list, Map<String, String> errorMap, int index, String errorId) {
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
                    errorMap.put(errorId + index + j, errMsg);
                    continue;
                }
                int stime = getTime(list.get(i).getStartFromHH(), list.get(i).getStartFromMM());
                int etime = getTime(list.get(i).getEndToHH(), list.get(i).getEndToMM());
                int stime1 = getTime(list.get(j).getStartFromHH(), list.get(j).getStartFromMM());
                int etime1 = getTime(list.get(j).getEndToHH(), list.get(j).getEndToMM());
                if (stime <= etime1 && etime >= stime1) {
                    errorMap.put(errorId + index + j, errMsg);
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

    public static void validateEvent(List<AppPremEventPeriodDto> appPremEventPeriodDtoList, Map<String, String> map, int index,
            String errorId) {
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
                            map.put(errorId + index + j, MessageUtil.getMessageDesc("NEW_ERR0021"));
                        }
                    }
                }
            }
        }
    }

    public static void doValidateBusiness(List<AppSvcBusinessDto> appSvcBusinessDtos, String appType,
            String licenceId, Map<String, String> errorMap) {
        if (appSvcBusinessDtos == null || appSvcBusinessDtos.isEmpty()) {
            return;
        }
        for (int i = 0; i < appSvcBusinessDtos.size(); i++) {

            String prefix="";

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
            }

            String ContactNo = appSvcBusinessDtos.get(i).getContactNo();
            if (StringUtil.isEmpty(ContactNo)) {
                errorMap.put("contactNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Contact No. ", "field"));
            } else if (!StringUtil.isEmpty(ContactNo)) {
                if (ContactNo.length() > 8) {
                    String general_err0041 = repLength("Contact No.", "8");
                    errorMap.put("contactNo" + i, general_err0041);
                }
                if (!ContactNo.matches("^[3|6|8|9][0-9]{7}$")) {
                    errorMap.put("contactNo" + i, "GENERAL_ERR0007");
                }
            }

            String emailAddr = appSvcBusinessDtos.get(i).getEmailAddr();
            if (StringUtil.isEmpty(emailAddr)) {
                errorMap.put("emailAddr" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Email Address", "field"));
            } else if (!StringUtil.isEmpty(emailAddr)) {
                if (emailAddr.length() > 320) {
                    String general_err0041 = repLength("Email Address", "320");
                    errorMap.put("emailAddr" + i, general_err0041);
                }
                if (!ValidationUtils.isEmail(emailAddr)) {
                    errorMap.put("emailAddr" + i, "GENERAL_ERR0014");
                }
            }

            if(appSvcBusinessDtos.get(i).getWeeklyDtoList()!=null){
                validateWeek(appSvcBusinessDtos.get(i),prefix,errorMap);
            }
            validatePh(appSvcBusinessDtos.get(i),prefix, errorMap);
            validateEvent(appSvcBusinessDtos.get(i), prefix,errorMap);
        }
    }

    //event
    private static void validateEvent(AppSvcBusinessDto appSvcBusinessDto, String prefix, Map<String, String> errorMap) {
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
                        errorMap.put(prefix + "onSiteEvent" + j, emptyErrMsg);
                    } else if (eventName.length() > 100) {
                        errorMap.put(prefix + "onSiteEvent" + j, repLength("Event Name", "100"));
                    }
                    if (startDate == null) {
                        errorMap.put(prefix + "onSiteEventStart" + j, emptyErrMsg);
                        dateIsEmpty = true;
                    }
                    if (endDate == null) {
                        errorMap.put(prefix + "onSiteEventEnd"  + j, emptyErrMsg);
                        dateIsEmpty = true;
                    }
                    if (!dateIsEmpty) {
                        if (startDate.after(endDate)) {
                            errorMap.put(prefix + "onSiteEventDate" + j, MessageUtil.getMessageDesc("NEW_ERR0020"));
                        }
                    }
                }
                j++;
            }
        }
    }

    //ph
    private static void validatePh(AppSvcBusinessDto appSvcBusinessDto, String prefix, Map<String, String> errorMap) {
        List<OperationHoursReloadDto> phDtos = appSvcBusinessDto.getPhDtoList();
        if (!IaisCommonUtils.isEmpty(phDtos)) {
            int j = 0;
            for (OperationHoursReloadDto phDto : phDtos) {
                Map<String, String> errNameMap = IaisCommonUtils.genNewHashMap();
                errNameMap.put("select", prefix + "onSitePubHoliday");
                errNameMap.put("start", prefix + "onSitePhStart");
                errNameMap.put("end", prefix + "onSitePhEnd");
                errNameMap.put("time", prefix + "onSitePhTime");
                doOperationHoursValidate(phDto, errorMap, errNameMap, j + "", false);
                j++;
            }
        }
    }

    //weekly
    private static void validateWeek(AppSvcBusinessDto appSvcBusinessDto, String prefix, Map<String, String> errorMap) {
        List<OperationHoursReloadDto> weeklyDtos = appSvcBusinessDto.getWeeklyDtoList();
        String emptyErrMsg = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if (IaisCommonUtils.isEmpty(weeklyDtos)) {
            errorMap.put(prefix + "onSiteWeekly" + 0 , emptyErrMsg);
            errorMap.put(prefix + "onSiteWeeklyStart" + 0 , emptyErrMsg);
            errorMap.put(prefix + "onSiteWeeklyEnd" + 0 , emptyErrMsg);
        } else {
            int j = 0;
            for (OperationHoursReloadDto weeklyDto : weeklyDtos) {
                Map<String, String> errNameMap = IaisCommonUtils.genNewHashMap();
                errNameMap.put("select", prefix + "onSiteWeekly");
                errNameMap.put("start", prefix + "onSiteWeeklyStart");
                errNameMap.put("end", prefix + "onSiteWeeklyEnd");
                errNameMap.put("time", prefix + "onSiteWeeklyTime");
                doOperationHoursValidate(weeklyDto, errorMap, errNameMap, j+"", true);
                j++;
            }
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
                    StringUtil.isEmpty(endMM)&&!selectAllDay){
                return;
            }
        }
        if (StringUtil.isEmpty(selectVal)) {
            errorMap.put(errNameMap.get("select") + count, emptyErrMsg);
        }
        if (selectAllDay) {
            if (!isEmpty) {
                Time time = Time.valueOf(LocalTime.of(0, 0, 0));
                operationHoursReloadDto.setStartFrom(time);
                operationHoursReloadDto.setEndTo(time);
            }
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

    public static boolean doPsnCommValidate(Map<String, String> errMap, String personKey, String idNo, boolean licPerson,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, String errKey) {
        boolean isValid = true;
        if (needPsnCommValidate() && licPersonMap != null && !StringUtil.isEmpty(personKey)
                && !StringUtil.isEmpty(idNo) && !licPerson) {
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
        if (StringUtil.isEmpty(blacklist)) {
            blacklist = MasterCodeUtil.getCodeDesc("MS001");
        }
        Map<Integer, String> map = new LinkedHashMap<>();
        if (blacklist == null || StringUtil.isEmpty(blacklist) || StringUtil.isEmpty(name)) {
            return map;
        }
        String[] s = blacklist.split("[ ]+");
        name = name.toUpperCase(AppConsts.DFT_LOCALE);
        String[] target = name.split("[ ]+");
        for (int index = 0; index < s.length; index++) {
            String t = s[index].toUpperCase();
            if (Arrays.stream(target).parallel().anyMatch(x -> x.equals(t))) {
                map.put(name.indexOf(t), s[index]);
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
        List<PremisesDto> premisesDtoList = getLicCommService().getPremisesListByLicenceId(licenceId);
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

    public static boolean checkProfRegNo(String profRegNo, HttpServletRequest request) {
        return checkProfRegNo(profRegNo, null, request);
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
        if ("-1".equals(idType) || StringUtil.isEmpty(idType)) {
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
            if ("-1".equals(nationality) || StringUtil.isEmpty(nationality)) {
                errMap.put(keyNationality, MessageUtil.replaceMessage("GENERAL_ERR0006", "Country of issuance", "field"));
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

    private static StringBuilder handleTabHames(AppSubmissionDto appSubmissionDto, List<String> errorList, StringBuilder msg) {
        if (msg == null) {
            msg = new StringBuilder();
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
        return msg;
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
            if (name.length() > 110) {
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
            String mandatoryErrMsg = MessageUtil.getMessageDesc("NEW_ERR0025");
            mandatoryErrMsg = mandatoryErrMsg.replace("{psnType}", psnName);
            mandatoryErrMsg = mandatoryErrMsg.replace("{mandatoryCount}", String.valueOf(mandatoryCount));
            errMap.put(errName, mandatoryErrMsg);
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

    public static Map<String, String> validateSectionLeaders(List<AppSvcPersonnelDto> appSvcSectionLeaderList, String svcCode) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (appSvcSectionLeaderList == null || appSvcSectionLeaderList.isEmpty()) {
            return errorMap;
        }
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        if (request != null) {
            request.setAttribute(HcsaAppConst.SECTION_LEADER_LIST, appSvcSectionLeaderList);
            request.setAttribute(HcsaAppConst.CURRENT_SVC_CODE, svcCode);
        }
        for (int i = 0, len = appSvcSectionLeaderList.size(); i < len; i++) {
            ValidationResult result = WebValidationHelper.validateProperty(appSvcSectionLeaderList.get(i),
                    ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
            if (result != null) {
                int index = i;
                Map<String, String> map = result.retrieveAll();
                map.forEach((k, v) -> errorMap.put(k + index, v));
            }
        }
        return errorMap;
    }

    private static void doAppSvcCgoDto(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map,
            List<AppSvcPrincipalOfficersDto> list) {
        if (list == null) {
            if (hcsaSvcPersonnelDtos != null) {
                for (HcsaSvcPersonnelDto every : hcsaSvcPersonnelDtos) {
                    String psnType = every.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
                        log.info("PERSONNEL_PSN_TYPE_CGO null");
                        map.put(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, "CGO can't be null");
                        return;
                    }
                }
            }
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
                Boolean flag = Boolean.FALSE;
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
                for (String f : sysFileTypeArr) {
                    if (f.equalsIgnoreCase(substring)) {
                        flag = Boolean.TRUE;
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

    public static void doValidateSvcPersonnel(Map<String, String> errorMap, List<AppSvcPersonnelDto> appSvcPersonnelDtos,
            String svcCode) {
        if (IaisCommonUtils.isEmpty(appSvcPersonnelDtos)) {
            return;
        }
        String errName = MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field");
        String errDesignation = MessageUtil.replaceMessage("GENERAL_ERR0006", "Designation", "field");
        String errRegnNo = MessageUtil.replaceMessage("GENERAL_ERR0006", "Professional Regn. No.", "field");
        String errWrkExpYear = MessageUtil.replaceMessage("GENERAL_ERR0006", "Relevant working experience (Years)", "field");
        String errQualification = MessageUtil.replaceMessage("GENERAL_ERR0006", "Qualification", "field");
        String errSelSvcPsnel = MessageUtil.replaceMessage("GENERAL_ERR0006", "Select Service Personnel", "field");
        String errOtherDesignation = MessageUtil.replaceMessage("GENERAL_ERR0006", "Others Designation", "field");

        String errLengthName = repLength("Name", "110");
        String errLengthRegnNo = repLength("Professional Regn. No.", "20");
        String errLengthWrkExpYear = repLength("Relevant working experience (Years)", "2");
        String errLengthQualification = repLength("Qualification", "100");
        String errLengthOtherDesignation = repLength("Others Designation", "100");
        //Verify that each type of person has at least one
        List<String> data = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
            String personType = appSvcPersonnelDtos.get(i).getPersonnelType();
            if (StringUtil.isEmpty(personType)) {
                personType = ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL;
            }
            String name = appSvcPersonnelDtos.get(i).getName();
            String personKey = StringUtil.getNonNull(name) + "__" + personType;
            if (data.contains(personKey)) {
                errorMap.put("name" + i, "NEW_ERR0012");
            } else {
                data.add(personKey);
            }
            if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)) {
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    errorMap.put("designation" + i, errDesignation);
                } else if (HcsaAppConst.DESIGNATION_OTHERS.equals(designation)) {
                    String otherDesignation = appSvcPersonnelDtos.get(i).getOtherDesignation();
                    if (StringUtil.isEmpty(otherDesignation)) {
                        errorMap.put("otherDesignation" + i, errOtherDesignation);
                    } else if (otherDesignation.length() > 100) {
                        errorMap.put("otherDesignation" + i, errLengthOtherDesignation);
                    }
                }
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, errName);
                } else if (name.length() > 110) {
                    errorMap.put("name" + i, errLengthName);
                }
                String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                if (StringUtil.isEmpty(profRegNo)) {
                    errorMap.put("regnNo" + i, errRegnNo);
                } else if (profRegNo.length() > 20) {
                    errorMap.put("regnNo" + i, errLengthRegnNo);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if (wrkExpYear.length() > 2) {
                        errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                    }
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
            } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, errName);
                } else if (name.length() > 110) {
                    errorMap.put("name" + i, errLengthName);
                }
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(quaification)) {
                    errorMap.put("qualification" + i, errQualification);
                } else if (quaification.length() > 100) {
                    errorMap.put("qualification" + i, errLengthQualification);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if (wrkExpYear.length() > 2) {
                        errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                    }
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
            } else if (!AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)) {
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, errName);
                } else if (name.length() > 110) {
                    errorMap.put("name" + i, errLengthName);
                }
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(quaification)) {
                    errorMap.put("qualification" + i, errQualification);
                } else if (quaification.length() > 100) {
                    errorMap.put("qualification" + i, errLengthQualification);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if (wrkExpYear.length() > 2) {
                        errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                    }
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
            } else {
                String personnelSel = appSvcPersonnelDtos.get(i).getPersonnelType();
                if (StringUtils.isEmpty(personnelSel)) {
                    errorMap.put("personnelSelErrorMsg" + i, errSelSvcPsnel);
                }

                if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)) {
                    String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    } else if (name.length() > 110) {
                        errorMap.put("name" + i, errLengthName);
                    }
                    if (StringUtil.isEmpty(profRegNo)) {
                        errorMap.put("regnNo" + i, errRegnNo);
                    } else if (profRegNo.length() > 20) {
                        errorMap.put("regnNo" + i, errLengthRegnNo);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)) {
                    String designation = appSvcPersonnelDtos.get(i).getDesignation();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String qualification = appSvcPersonnelDtos.get(i).getQualification();

                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    } else if (name.length() > 110) {
                        errorMap.put("name" + i, errLengthName);
                    }
                    if (StringUtil.isEmpty(designation)) {
                        errorMap.put("designation" + i, errDesignation);
                    } else if (HcsaAppConst.DESIGNATION_OTHERS.equals(designation)) {
                        String otherDesignation = appSvcPersonnelDtos.get(i).getOtherDesignation();
                        if (StringUtil.isEmpty(otherDesignation)) {
                            errorMap.put("otherDesignation" + i, errOtherDesignation);
                        } else if (otherDesignation.length() > 100) {
                            errorMap.put("otherDesignation" + i, errLengthOtherDesignation);
                        }
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        errorMap.put("wrkExpYear" + i, errWrkExpYear);
                    } else {
                        if (wrkExpYear.length() > 2) {
                            errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                        }
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        }
                    }
                    if (StringUtil.isEmpty(qualification)) {
                        errorMap.put("qualification" + i, errQualification);
                    } else if (qualification.length() > 100) {
                        errorMap.put("qualification" + i, errLengthQualification);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String quaification = appSvcPersonnelDtos.get(i).getQualification();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    } else if (name.length() > 110) {
                        errorMap.put("name" + i, errLengthName);
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        errorMap.put("wrkExpYear" + i, errWrkExpYear);
                    } else {
                        if (wrkExpYear.length() > 2) {
                            errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                        }
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        }
                    }
                    if (StringUtil.isEmpty(quaification)) {
                        errorMap.put("qualification" + i, errQualification);
                    } else if (quaification.length() > 100) {
                        errorMap.put("qualification" + i, errLengthQualification);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    } else if (name.length() > 110) {
                        errorMap.put("name" + i, errLengthName);
                    }
                }
            }

        }
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
        LicCommService licCommService = SpringHelper.getBean(LicCommService.class);
        if (StringUtil.isEmpty(type) || HcsaAppConst.SECTION_PREMISES.equals(type)) {
            boolean b = licCommService.baseSpecLicenceRelation(licenceDto);
            if (!b) {
                log.warn(StringUtil.changeForLog("#####The error for baseSpecLicenceRelation: " + licenceDto.getLicenceNo()));
                errorMap.put(RfcConst.PENDING_APP, RfcConst.PENDING_APP_VALUE);
                return errorMap;
            }
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
                .filter(Objects::nonNull)
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

    private static void validateProfRegNo(Map<String, String> errMap, String profRegNo, String fieldKey) {
        if (StringUtil.isEmpty(profRegNo)) {
            return;
        }
        ProfessionalResponseDto professionalResponseDto = getAppCommService().retrievePrsInfo(profRegNo);
        if (professionalResponseDto != null) {
            if (professionalResponseDto.isHasException() || StringUtil.isNotEmpty(
                    professionalResponseDto.getStatusCode())) {
                log.debug(StringUtil.changeForLog("prs svc down ..."));
                if (professionalResponseDto.isHasException()) {
                    errMap.put(HcsaAppConst.PRS_SERVICE_DOWN, HcsaAppConst.PRS_SERVICE_DOWN);
                } else if ("401".equals(professionalResponseDto.getStatusCode())) {
                    errMap.put(fieldKey, MessageUtil.getMessageDesc("GENERAL_ERR0054"));
                } else {
                    errMap.put(fieldKey, MessageUtil.getMessageDesc("GENERAL_ERR0042"));
                }
            }
        }
    }

    public static void doValidateSpecialisedDtoList(Map<String, String> errorMap,
            List<AppPremSpecialisedDto> appPremSpecialisedDtoList, HttpServletRequest request) {
    }

}
