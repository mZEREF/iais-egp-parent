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
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
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
import com.ecquaria.cloud.moh.iais.service.client.LicCommClient;
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
        previewAndSubmitMap = doPreviewSubmitValidate(previewAndSubmitMap, appSubmissionDto, oldAppSubmissionDto, bpc);
        return previewAndSubmitMap;
    }

    public static Map<String, String> doPreviewSubmitValidate(Map<String, String> previewAndSubmitMap,
            AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto, BaseProcessClass bpc) {
        StringBuilder errorSvcConfig = new StringBuilder();
        boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        List<String> premisesHciList = (List<String>) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.PREMISES_HCI_LIST);
        List<String> errorList = doPreviewSubmitValidate(previewAndSubmitMap, appSubmissionDto, oldAppSubmissionDto,
                premisesHciList, isRfi, errorSvcConfig);
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
        if (errorList.contains(HcsaAppConst.SECTION_DOCUMENT)) {
            coMap.put(HcsaAppConst.SECTION_DOCUMENT, "");
        } else {
            coMap.put(HcsaAppConst.SECTION_DOCUMENT, HcsaAppConst.SECTION_DOCUMENT);
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
            AppSubmissionDto oldAppSubmissionDto, List<String> premisesHciList, boolean isRfi, StringBuilder errorSvcConfig) {
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
        String keyWord = MasterCodeUtil.getCodeDesc("MS001");
        Map<String, String> premissMap = doValidatePremiss(appSubmissionDto, oldAppSubmissionDto,
                premisesHciList, isRfi, false);
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
        // primary document
        Map<String, String> documentMap = IaisCommonUtils.genNewHashMap();
        documentValid(appSubmissionDto, documentMap, false);
        doCommomDocument(appSubmissionDto, documentMap, isRfi);
        if (!documentMap.isEmpty()) {
            errorMap.putAll(documentMap);
            errorList.add(HcsaAppConst.SECTION_DOCUMENT);
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
        if (StringUtil.isEmpty(serviceId) && !StringUtil.isEmpty(dto.getServiceName())) {
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
        }
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
                List<String> appIds = ApplicationHelper.getRelatedAppId(dto.getAppId(), dto.getLicenceId(),
                        dto.getServiceName());

                List<AppSvcVehicleDto> otherExistedVehicles = getAppCommService().getActiveVehicles(appIds);
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
            } else if (HcsaConsts.STEP_LABORATORY_DISCIPLINES.equals(currentStep)) {
                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = dto.getAppSvcLaboratoryDisciplinesDtoList();
                List<HcsaSvcSubtypeOrSubsumedDto> checkList = configCommService.loadLaboratoryDisciplines(serviceId);
                Map<String, String> disciplineMap = doValidateLaboratory(appGrpPremisesDtos,
                        appSvcLaboratoryDisciplinesDtoList, serviceId, checkList);
                if (!disciplineMap.isEmpty()) {
                    errorMap.putAll(disciplineMap);
                    addErrorStep(currentStep, stepName, true, errorList);
                }
            } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(currentStep)) {
                Map<String, String> govenMap = IaisCommonUtils.genNewHashMap();
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = dto.getAppSvcCgoDtoList();
                doAppSvcCgoDto(currentSvcAllPsnConfig, govenMap, appSvcCgoDtoList);
                if (govenMap.isEmpty()) {
                    govenMap.putAll(doValidateGovernanceOfficers(appSvcCgoDtoList, licPersonMap, dto.getServiceCode()));
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
            } else if (HcsaConsts.STEP_DISCIPLINE_ALLOCATION.equals(currentStep)) {
                List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = dto.getAppSvcDisciplineAllocationDtoList();
                doValidateDisciplineAllocation(errorMap, appSvcDisciplineAllocationDtoList, dto, null);
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_CHARGES.equals(currentStep)) {
                new ValidateCharges().doValidateCharges(errorMap, dto.getAppSvcChargesPageDto());
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_SERVICE_PERSONNEL.equals(currentStep)) {
                List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.getAppSvcPersonnelDtoList();
                doAppSvcPersonnelDtoList(errorMap, appSvcPersonnelDtoList, dto.getServiceCode());
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
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = dto.getAppSvcPrincipalOfficersDtoList();
                Map<String, String> map = doValidatePo(appSvcPrincipalOfficersDtoList, licPersonMap,
                        dto.getServiceCode(), subLicenseeDto);
                if (!map.isEmpty()) {
                    errorMap.putAll(map);
                }
                addErrorStep(currentStep, stepName, errorMap.size() != prevSize, errorList);
            } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(currentStep)) {
                List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderList = dto.getAppSvcKeyAppointmentHolderDtoList();
                Map<String, String> map = doValidateKeyAppointmentHolder(appSvcKeyAppointmentHolderList,
                        licPersonMap, dto.getServiceCode());
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
                List<AppSvcDocDto> appSvcDocDtoLit = dto.getAppSvcDocDtoLit();
                doSvcDocument(errorMap, appSvcDocDtoLit, uploadFileLimit, sysFileType);
                List<HcsaSvcDocConfigDto> svcDocConfigDtos = IaisCommonUtils.genNewArrayList();
                List<HcsaSvcDocConfigDto> premServiceDocConfigDtos = IaisCommonUtils.genNewArrayList();
                List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = configCommService.getAllHcsaSvcDocs(dto.getServiceId());
                if (!IaisCommonUtils.isEmpty(hcsaSvcDocDtos)) {
                    for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocDtos) {
                        if ("0".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                            svcDocConfigDtos.add(hcsaSvcDocConfigDto);
                        } else if ("1".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                            premServiceDocConfigDtos.add(hcsaSvcDocConfigDto);
                        }
                    }
                }
                ApplicationHelper.svcDocMandatoryValidate(svcDocConfigDtos, dto.getAppSvcDocDtoLit(), appGrpPremisesDtos, dto,
                        errorMap);
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

    private static void doCommomDocument(AppSubmissionDto appSubmissionDto, Map<String, String> documentMap, boolean isRfi) {
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList;
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos;
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if (isRfi && appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0) {
            hcsaSvcDocDtos = getConfigCommService().getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
        } else {
            hcsaSvcDocDtos = getConfigCommService().getAllHcsaSvcDocs(null);
        }
        if (hcsaSvcDocDtos != null) {
            List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigDto = IaisCommonUtils.genNewArrayList();
            for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocDtos) {
                if ("0".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                    commonHcsaSvcDocConfigDto.add(hcsaSvcDocConfigDto);
                }
            }
            commonHcsaSvcDocConfigList = commonHcsaSvcDocConfigDto;
        } else {
            return;
        }
        for (HcsaSvcDocConfigDto comm : commonHcsaSvcDocConfigList) {
            String name = "common" + comm.getId();

            Boolean isMandatory = comm.getIsMandatory();
            String err006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Document", "field");
            if (isMandatory && appGrpPrimaryDocDtoList == null || isMandatory && appGrpPrimaryDocDtoList.isEmpty()) {
                documentMap.put(name, err006);
            } else if (isMandatory && !appGrpPrimaryDocDtoList.isEmpty()) {
                Boolean flag = Boolean.FALSE;
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
                    if (StringUtil.isEmpty(appGrpPrimaryDocDto.getMd5Code())) {
                        continue;
                    }
                    String svcComDocId = appGrpPrimaryDocDto.getSvcComDocId();
                    if (!comm.getId().equals(svcComDocId) && comm.getDocTitle().equals(appGrpPrimaryDocDto.getSvcComDocName())) {
                        appGrpPrimaryDocDto.setSvcComDocId(comm.getId());
                        svcComDocId = comm.getId();
                    }
                    if (comm.getId().equals(svcComDocId)) {
                        flag = Boolean.TRUE;
                        break;
                    }
                }
                if (!flag) {
                    documentMap.put(name, err006);
                }
            }
        }
    }

    public static Map<String, String> doValidatePremiss(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto,
            List<String> premisesHciList, boolean rfi, boolean checkOthers) {
        //do validate one premiss
        List<String> list = IaisCommonUtils.genNewArrayList();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Set<String> distinctVehicleNo = IaisCommonUtils.genNewHashSet();
        boolean needAppendMsg = false;
        String licenseeId = appSubmissionDto.getLicenseeId();
        String licenceId = appSubmissionDto.getLicenceId();
        String premiseTypeError = "";
        String selectPremises = "";
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
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
                boolean needValidate = false;

                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                        appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
                    String oldPremSel = oldAppSubmissionDto == null ? "-1" :
                            oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0).getPremisesSelect();
                    if ("-1".equals(oldPremSel) || !StringUtil.isEmpty(oldPremSel) && oldPremSel.equals(premisesSelect)) {
                        needValidate = true;
                    }
                }
                if (StringUtil.isEmpty(premisesSelect) || "-1".equals(premisesSelect)) {
                    if ("".equals(selectPremises)) {
                        selectPremises = MessageUtil.replaceMessage("GENERAL_ERR0006", "Add or select a premises from the list",
                                "field");
                    }
                    errorMap.put("premisesSelect" + i, selectPremises);
                } else if (needValidate || !StringUtil.isEmpty(premisesSelect) || "newPremise".equals(premisesSelect)) {
                    List<String> floorUnitNo = new ArrayList<>(10);
                    List<String> floorUnitList = IaisCommonUtils.genNewArrayList();
                    String prefix = "";
                    String hciName = null;
                    String hciNameKey = "";
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)) {
                        prefix = "onSite";
                        String locateWithOthers = appGrpPremisesDto.getLocateWithOthers();
                        if (StringUtil.isEmpty(locateWithOthers)) {
                            errorMap.put("isOtherLic" + i,
                                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Are you co-locating with another licensee",
                                            "field"));
                        }

                        String ScdfRefNo = appGrpPremisesDto.getScdfRefNo();
                        if (!StringUtil.isEmpty(ScdfRefNo) && ScdfRefNo.length() > 66) {
                            String general_err0041 = repLength("Fire Safety & Shelter Bureau Ref. No.", "66");
                            errorMap.put("ScdfRefNo" + i, general_err0041);
                        }

                        hciName = appGrpPremisesDto.getHciName();
                        hciNameKey = "hciName" + i;
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        prefix = "conveyance";
                        hciNameKey = "conveyanceHciName" + i;
                        hciName = appGrpPremisesDto.getConveyanceHciName();

                        validateVehicleNo(errorMap, distinctVehicleNo, i, appGrpPremisesDto.getConveyanceVehicleNo());
                    } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premiseType)) {
                        prefix = "offSite";
                        hciNameKey = "conveyanceHciName" + i;
                        hciName = appGrpPremisesDto.getOffSiteHciName();
                    } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premiseType)) {
                        hciNameKey = "easMtsHciName" + i;
                        hciName = appGrpPremisesDto.getEasMtsHciName();

                        String easMtsUseOnly = appGrpPremisesDto.getEasMtsUseOnly();
                        String easMtsPubHotline = appGrpPremisesDto.getEasMtsPubHotline();
                        if (StringUtil.isEmpty(easMtsPubHotline)) {
                            if (!"UOT002".equals(easMtsUseOnly)) {
                                errorMap.put("easMtsPubHotline" + i,
                                        MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Hotline", "field"));
                            }
                        } else {
                            if (!easMtsPubHotline.matches("^[6|8|9][0-9]{7}$")) {
                                errorMap.put("easMtsPubHotline" + i, MessageUtil.getMessageDesc("GENERAL_ERR0007"));
                            }
                        }
                    }
                    if (!StringUtil.isEmpty(prefix)) {
                        //weekly
                        validateWeek(appGrpPremisesDto, i, prefix, errorMap);
                        //ph
                        validatePh(appGrpPremisesDto, i, prefix, errorMap);
                        //event
                        validateEvent(appGrpPremisesDto, i, prefix, errorMap);
                    }
                    //migrated licence need  judge
                    if (StringUtil.isEmpty(hciName)) {
                        errorMap.put(hciNameKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "HCI Name", "field"));
                    } else {
                        if (hciName.length() > 100) {
                            String general_err0041 = repLength("HCI Name", "100");
                            errorMap.put(hciNameKey, general_err0041);
                        }
                        checkHciName(hciNameKey, hciName, appType, licenceId, errorMap);
                    }
                    Map<String, String> map = validateContactInfo(appGrpPremisesDto, i, floorUnitNo, floorUnitList, list);
                    if (!map.isEmpty()) {
                        errorMap.putAll(map);
                    } else {
                        String hciNameErr = errorMap.get(hciNameKey);
                        String vehicleNo = errorMap.get("conveyanceVehicleNo" + i);
                        hciFlag = StringUtil.isEmpty(hciNameErr) && StringUtil.isEmpty(vehicleNo);
                    }
                    log.info(StringUtil.changeForLog("hciFlag:" + hciFlag));
                } else {
                    //premiseSelect = organization hci code
                    if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        String conveyanceVehicleNo = appGrpPremisesDto.getConveyanceVehicleNo();
                        validateVehicleNo(errorMap, distinctVehicleNo, i, conveyanceVehicleNo);
                    }

                }
            }
            //0062204
            boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
            if (newTypeFlag && hciFlag) {
                boolean clickEdit = appGrpPremisesDto.isClickEdit();
                List<String> currentHcis = ApplicationHelper.genPremisesHciList(appGrpPremisesDto);
                if (!rfi) {
                    //new
                    if (!IaisCommonUtils.isEmpty(premisesHciList)) {
                        checkHciIsSame(currentHcis, premisesHciList, errorMap, "premisesHci" + i);
                    }
                } else if (rfi && clickEdit) {
                    boolean isChange = false;
                    boolean appTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                    if ((appTypeFlag && rfi) && (oldAppSubmissionDto != null)) {
                        AppGrpPremisesDto oldAppGrpPremisesDto = null;
                        if (i >= oldAppSubmissionDto.getAppGrpPremisesDtoList().size()) {
                            oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0);
                        } else {
                            oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(i);
                        }
                        isChange = !Objects.equals(oldAppGrpPremisesDto.getHciName(), appGrpPremisesDto.getHciName())
                                || !Objects.equals(oldAppGrpPremisesDto.getAddressWithoutFU(), appGrpPremisesDto.getAddressWithoutFU())
                                || !RfcHelper.isFloorUnitAllIn(appGrpPremisesDto, oldAppGrpPremisesDto);
                    }
                    if (!IaisCommonUtils.isEmpty(premisesHciList) && isChange) {
                        checkHciIsSame(currentHcis, premisesHciList, errorMap, "premisesHci" + i);
                    }
                }
            }
            if (checkOthers) {
                List<PremisesDto> premisesDtos =
                        getLicCommService().getPremisesDtoByHciNameAndPremType(appGrpPremisesDto.getActualHciName(),
                                appGrpPremisesDto.getPremisesType(), licenseeId);
                if (!IaisCommonUtils.isEmpty(premisesDtos)) {
                    errorMap.put("hciNameUsed", MessageUtil.getMessageDesc("NEW_ACK011"));
                }
                String premisesSelect = ApplicationHelper.getPremisesKey(appGrpPremisesDto);
                if (appGrpPremisesDtoList.stream().anyMatch(dto -> !Objects.equals(appGrpPremisesDto.getPremisesIndexNo(),
                        dto.getPremisesIndexNo()) && Objects.equals(premisesSelect, ApplicationHelper.getPremisesKey(dto)))) {
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
                                MessageUtil.replaceMessage("GENERAL_ERR0050", ApplicationConsts.TITLE_MODE_OF_SVCDLVY, "field"));
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
        }
        if (checkOthers) {
            //65116
            String hciNameUsed = errorMap.get("hciNameUsed");
            String errMsg = MessageUtil.getMessageDesc("NEW_ACK004");
            if (needAppendMsg) {
                if (StringUtil.isEmpty(hciNameUsed)) {
                    errorMap.put("hciNameUsed", errMsg);
                } else {
                    String hciNameMsg = MessageUtil.getMessageDesc(hciNameUsed);
                    errorMap.put("hciNameUsed", hciNameMsg + "<br/>" + errMsg);
                }
            }
        }
        log.info(StringUtil.changeForLog("the do doValidatePremiss end ...."));
        AppValidatorHelper.validatePH(errorMap, appSubmissionDto);
        WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
        return errorMap;

    }

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

    private static void validateVehicleNo(Map<String, String> errorMap, Set<String> distinctVehicleNo, int numberCount,
            String conveyanceVehicleNo) {
        if (StringUtil.isEmpty(conveyanceVehicleNo)) {
            errorMap.put("conveyanceVehicleNo" + numberCount, MessageUtil.replaceMessage("GENERAL_ERR0006", "Vehicle No.", "field"));
        } else {
            if (conveyanceVehicleNo.length() > 10) {
                String general_err0041 = repLength("Vehicle No.", "10");
                errorMap.put("conveyanceVehicleNo" + numberCount, general_err0041);
            }
            boolean b = VehNoValidator.validateNumber(conveyanceVehicleNo);
            if (!b) {
                errorMap.put("conveyanceVehicleNo" + numberCount, "GENERAL_ERR0017");
            }

            if (distinctVehicleNo.contains(conveyanceVehicleNo)) {
                errorMap.put("conveyanceVehicleNo" + numberCount, "NEW_ERR0016");
            } else {
                distinctVehicleNo.add(conveyanceVehicleNo);
            }
        }
    }

    private static Map<String, String> validateContactInfo(AppGrpPremisesDto appGrpPremisesDto, int i, List<String> floorUnitNo,
            List<String> floorUnitList, List<String> list) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";
        String postalCode = appGrpPremisesDto.getPostalCode();
        String buildingName = appGrpPremisesDto.getBuildingName();
        String streetName = appGrpPremisesDto.getStreetName();
        String email = appGrpPremisesDto.getEasMtsPubEmail();
        String addrType = appGrpPremisesDto.getAddrType();
        String blkNo = appGrpPremisesDto.getBlkNo();
        String premisesType = appGrpPremisesDto.getPremisesType();
        String blkNoKey = "";
        String addTypeKey = "";
        String easMtsUseOnly = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)) {
            blkNoKey = "blkNo" + i;
            addTypeKey = "addrType" + i;
            String offTelNo = appGrpPremisesDto.getOffTelNo();
            String offTelNoKey = ApplicationHelper.getParamName(prefix, "offTelNo" + i);
            if (StringUtil.isEmpty(offTelNo)) {
                errorMap.put(offTelNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Office Telephone No.", "field"));
            } else {
                if (offTelNo.length() > 8) {
                    String general_err0041 = repLength("Office Telephone No.", "8");
                    errorMap.put(offTelNoKey, general_err0041);
                }
                boolean matches = offTelNo.matches(IaisEGPConstant.OFFICE_TELNO_MATCH);
                if (!matches) {
                    errorMap.put(offTelNoKey, "GENERAL_ERR0015");
                }
            }
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)) {
            prefix = "conveyance";
            postalCode = appGrpPremisesDto.getConveyancePostalCode();
            buildingName = appGrpPremisesDto.getConveyanceBuildingName();
            streetName = appGrpPremisesDto.getConveyanceStreetName();
            email = appGrpPremisesDto.getConveyanceEmail();
            addrType = appGrpPremisesDto.getConveyanceAddressType();
            blkNo = appGrpPremisesDto.getConveyanceBlockNo();
            blkNoKey = "conveyanceBlockNos" + i;
            addTypeKey = "conveyanceAddressType" + i;
        } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)) {
            prefix = "offSite";
            postalCode = appGrpPremisesDto.getOffSitePostalCode();
            buildingName = appGrpPremisesDto.getOffSiteBuildingName();
            streetName = appGrpPremisesDto.getOffSiteStreetName();
            email = appGrpPremisesDto.getOffSiteEmail();
            addrType = appGrpPremisesDto.getOffSiteAddressType();
            blkNo = appGrpPremisesDto.getOffSiteBlockNo();
            blkNoKey = "offSiteBlockNo" + i;
            addTypeKey = "offSiteAddressType" + i;
        } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType)) {
            prefix = "easMts";
            postalCode = appGrpPremisesDto.getEasMtsPostalCode();
            buildingName = appGrpPremisesDto.getEasMtsBuildingName();
            streetName = appGrpPremisesDto.getEasMtsStreetName();
            email = appGrpPremisesDto.getOffSiteEmail();
            addrType = appGrpPremisesDto.getOffSiteAddressType();
            blkNo = appGrpPremisesDto.getOffSiteBlockNo();
            easMtsUseOnly = appGrpPremisesDto.getEasMtsUseOnly();
            blkNoKey = "easMtsBlockNo" + i;
            addTypeKey = "easMtsAddressType" + i;
        }

        if (!StringUtil.isEmpty(buildingName) && buildingName.length() > 66) {
            String general_err0041 = repLength("Building Name", "66");
            errorMap.put(ApplicationHelper.getParamName(prefix, "buildingName" + i), general_err0041);
        }

        if (StringUtil.isEmpty(streetName)) {
            errorMap.put(ApplicationHelper.getParamName(prefix, "streetName" + i),
                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name", "field"));
        } else if (streetName.length() > 32) {
            String general_err0041 = repLength("Street Name", "32");
            errorMap.put(ApplicationHelper.getParamName(prefix, "streetName" + i), general_err0041);
        }

        if (StringUtil.isEmpty(email)) {
            if (!"UOT002".equals(easMtsUseOnly)) {
                errorMap.put(ApplicationHelper.getParamName(prefix, "email" + i), MessageUtil.replaceMessage("GENERAL_ERR0006",
                        "Email ", "field"));
            }
        } else {
            if (!ValidationUtils.isEmail(email)) {
                errorMap.put(ApplicationHelper.getParamName(prefix, "email" + i), MessageUtil.getMessageDesc("GENERAL_ERR0014"));
            }
        }

        if (StringUtil.isEmpty(addrType)) {
            errorMap.put(addTypeKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
        } else {
            // validate floor and units
            validateOperaionUnits(appGrpPremisesDto, i, floorUnitNo, floorUnitList, errorMap);
            boolean empty1 = StringUtil.isEmpty(blkNo);
            if (empty1) {
                errorMap.put(blkNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
            } else if (blkNo.length() > 10) {
                String general_err0041 = repLength("Block / House No.", "10");
                errorMap.put(blkNoKey, general_err0041);
            }
        }
        String postalCodeKey = ApplicationHelper.getParamName(prefix, "postalCode" + i);
        if (!StringUtil.isEmpty(postalCode)) {
            if (postalCode.length() > 6) {
                String general_err0041 = repLength("Postal Code", "6");
                errorMap.put(postalCodeKey, general_err0041);
            } else if (postalCode.length() < 6) {
                errorMap.put(postalCodeKey, "NEW_ERR0004");
            } else if (!postalCode.matches("^[0-9]{6}$")) {
                errorMap.put(postalCodeKey, "NEW_ERR0004");
            } else {
                if (!floorUnitNo.isEmpty()) {
                    stringBuilder.append(postalCode);
                    if (list.isEmpty()) {
                        for (String str : floorUnitNo) {
                            StringBuilder sb = new StringBuilder(stringBuilder);
                            sb.append(str);
                            list.add(sb.toString());
                        }
                    } else {
                        List<String> sbList = new ArrayList<>();
                        for (String str : floorUnitNo) {
                            StringBuilder sb = new StringBuilder(stringBuilder);
                            sb.append(str);
                            if (list.contains(sb.toString())) {
                                errorMap.put(postalCodeKey, "NEW_ACK010");
                            } else {
                                sbList.add(sb.toString());
                            }
                        }
                        list.addAll(sbList);
                    }
                }
            }
        } else {
            errorMap.put(postalCodeKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code ", "field"));
        }
        return errorMap;
    }

    private static boolean validateOperaionUnits(AppGrpPremisesDto appGrpPremisesDto, int i, List<String> floorUnitNo,
            List<String> floorUnitList, Map<String, String> errorMap) {
        boolean addrTypeFlag = true;
        String premisesType = appGrpPremisesDto.getPremisesType();
        String diff = "";
        String prefix = "";
        String floorNo = null;
        String blkNo = null;
        String unitNo = null;
        String addrType = null;
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)) {
            floorNo = appGrpPremisesDto.getFloorNo();
            unitNo = appGrpPremisesDto.getUnitNo();
            blkNo = appGrpPremisesDto.getBlkNo();
            addrType = appGrpPremisesDto.getAddrType();
            appGrpPremisesDto.setFloorNo(ApplicationHelper.handleFloorNo(floorNo));
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)) {
            prefix = "conveyance";
            diff = "Conv";
            floorNo = appGrpPremisesDto.getConveyanceFloorNo();
            unitNo = appGrpPremisesDto.getConveyanceUnitNo();
            blkNo = appGrpPremisesDto.getConveyanceBlockNo();
            addrType = appGrpPremisesDto.getConveyanceAddressType();
        } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)) {
            prefix = "offSite";
            diff = "Off";
            floorNo = appGrpPremisesDto.getOffSiteFloorNo();
            unitNo = appGrpPremisesDto.getOffSiteUnitNo();
            blkNo = appGrpPremisesDto.getOffSiteBlockNo();
            addrType = appGrpPremisesDto.getOffSiteAddressType();
        } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType)) {
            prefix = "easMts";
            diff = "EasMts";
            floorNo = appGrpPremisesDto.getEasMtsFloorNo();
            unitNo = appGrpPremisesDto.getEasMtsUnitNo();
            blkNo = appGrpPremisesDto.getEasMtsBlockNo();
            addrType = appGrpPremisesDto.getEasMtsAddressType();
        }
        String floorNoKey = ApplicationHelper.getParamName(prefix, "floorNo" + i);
        String blkNoKey = ApplicationHelper.getParamName(prefix, "blkNo" + i);
        String unitNoKey = ApplicationHelper.getParamName(prefix, "unitNo" + i);
        boolean empty = StringUtil.isEmpty(floorNo);
        boolean empty1 = StringUtil.isEmpty(blkNo);
        boolean empty2 = StringUtil.isEmpty(unitNo);
        if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {
            if (empty) {
                addrTypeFlag = false;
                errorMap.put(floorNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
            } else if (floorNo.length() > 3) {
                String general_err0041 = repLength("Floor No.", "3");
                errorMap.put(floorNoKey, general_err0041);
            }
            if (empty1) {
                addrTypeFlag = false;
                errorMap.put(blkNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
            } else if (blkNo.length() > 10) {
                String general_err0041 = repLength("Block / House No.", "10");
                errorMap.put(blkNoKey, general_err0041);
            }
            if (empty2) {
                addrTypeFlag = false;
                errorMap.put(unitNoKey, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
            } else if (unitNo.length() > 5) {
                String general_err0041 = repLength("Unit No.", "5");
                errorMap.put(unitNoKey, general_err0041);
            }
        }
        if (!empty && !empty1 && !empty2) {
            StringBuilder sb = new StringBuilder();
            sb.append(floorNo).append(blkNo).append(unitNo);
            floorUnitNo.add(sb.toString());
        }

        if (addrTypeFlag) {
            floorUnitList.add(floorNo + unitNo);
        }
        String floorErrName = "op" + diff + "FloorNo" + i;
        String unitErrName = "op" + diff + "UnitNo" + i;
        String floorUnitErrName = ApplicationHelper.getParamName(diff, "floorUnit" + i);
        checkOperaionUnit(appGrpPremisesDto.getAppPremisesOperationalUnitDtos(), errorMap, floorErrName, unitErrName, floorUnitList,
                floorUnitErrName, floorUnitNo, appGrpPremisesDto);
        String floorNoErr = errorMap.get(floorNoKey);
        String unitNoErr = errorMap.get(unitNoKey);
        return StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
    }

    private static void checkOperaionUnit(List<AppPremisesOperationalUnitDto> operationalUnitDtos, Map<String, String> errorMap,
            String floorErrName, String unitErrName, List<String> floorUnitList, String floorUnitErrName, List<String> floorUnitNo,
            AppGrpPremisesDto appGrpPremisesDto) {

        if (!IaisCommonUtils.isEmpty(operationalUnitDtos)) {
            int opLength = 0;
            for (AppPremisesOperationalUnitDto operationalUnitDto : operationalUnitDtos) {
                boolean flag = true;
                String floorNo = operationalUnitDto.getFloorNo();
                String unitNo = operationalUnitDto.getUnitNo();
                boolean floorNoFlag = StringUtil.isEmpty(floorNo);
                boolean unitNoFlag = StringUtil.isEmpty(unitNo);
                if (!(floorNoFlag && unitNoFlag)) {
                    if (floorNoFlag) {
                        flag = false;
                        errorMap.put(floorErrName + opLength, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
                    } else if (unitNoFlag) {
                        flag = false;
                        errorMap.put(unitErrName + opLength, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
                    }
                }

                if (!floorNoFlag && floorNo.length() > 3) {
                    String general_err0041 = repLength("Floor No.", "3");
                    errorMap.put(floorErrName + opLength, general_err0041);
                }

                if (!unitNoFlag && unitNo.length() > 5) {
                    String general_err0041 = repLength("Unit No.", "5");
                    errorMap.put(unitErrName + opLength, general_err0041);
                }
                String floorNoErr = errorMap.get(floorErrName + opLength);
                operationalUnitDto.setFloorNo(ApplicationHelper.handleFloorNo(floorNo, floorNoErr));
                if (flag) {
                    if (!StringUtil.isEmpty(operationalUnitDto.getFloorNo()) && !StringUtil.isEmpty(operationalUnitDto.getUnitNo())) {
                        String floorUnitStr = operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo();
                        if (floorUnitList.contains(floorUnitStr)) {
                            errorMap.put(floorUnitErrName + opLength, "NEW_ERR0017");
                        } else {
                            floorUnitList.add(floorUnitStr);
                        }
                        String premisesType = appGrpPremisesDto.getPremisesType();
                        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)) {
                            String blkNo = appGrpPremisesDto.getBlkNo();
                            if (!StringUtil.isEmpty(blkNo)) {
                                floorUnitNo.add(operationalUnitDto.getFloorNo() + blkNo + operationalUnitDto.getUnitNo());
                            }
                        } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)) {
                            String offSiteAddressType = appGrpPremisesDto.getOffSiteAddressType();
                            if (!StringUtil.isEmpty(offSiteAddressType)) {
                                String blkNo = appGrpPremisesDto.getOffSiteBlockNo();
                                if (!StringUtil.isEmpty(blkNo)) {
                                    floorUnitNo.add(operationalUnitDto.getFloorNo() + blkNo + operationalUnitDto.getUnitNo());
                                }
                            }

                        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)) {
                            String conveyanceAddressType = appGrpPremisesDto.getConveyanceAddressType();
                            if (!StringUtil.isEmpty(conveyanceAddressType)) {
                                String blkNo = appGrpPremisesDto.getConveyanceBlockNo();
                                if (!StringUtil.isEmpty(blkNo)) {
                                    floorUnitNo.add(operationalUnitDto.getFloorNo() + blkNo + operationalUnitDto.getUnitNo());
                                }
                            }

                        } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType)) {
                            String easMtsAddressType = appGrpPremisesDto.getEasMtsAddressType();
                            if (!StringUtil.isEmpty(easMtsAddressType)) {
                                String blkNo = appGrpPremisesDto.getEasMtsBlockNo();
                                if (!StringUtil.isEmpty(blkNo)) {
                                    floorUnitNo.add(operationalUnitDto.getFloorNo() + blkNo + operationalUnitDto.getUnitNo());
                                }
                            }
                        }

                    }
                }
                opLength++;
            }
        }

    }

    //event
    private static void validateEvent(AppGrpPremisesDto appGrpPremisesDto, int i, String prefix, Map<String, String> errorMap) {
        List<AppPremEventPeriodDto> eventDtos = appGrpPremisesDto.getEventDtoList();
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
                        errorMap.put(prefix + "Event" + i + j, emptyErrMsg);
                    } else if (eventName.length() > 100) {
                        errorMap.put(prefix + "Event" + i + j, repLength("Event Name", "100"));
                    }
                    if (startDate == null) {
                        errorMap.put(prefix + "EventStart" + i + j, emptyErrMsg);
                        dateIsEmpty = true;
                    }
                    if (endDate == null) {
                        errorMap.put(prefix + "EventEnd" + i + j, emptyErrMsg);
                        dateIsEmpty = true;
                    }
                    if (!dateIsEmpty) {
                        if (startDate.after(endDate)) {
                            errorMap.put(prefix + "EventDate" + i + j, MessageUtil.getMessageDesc("NEW_ERR0020"));
                        }
                    }
                }
                j++;
            }
        }
    }

    //ph
    private static void validatePh(AppGrpPremisesDto appGrpPremisesDto, int i, String prefix, Map<String, String> errorMap) {
        List<OperationHoursReloadDto> phDtos = appGrpPremisesDto.getPhDtoList();
        if (!IaisCommonUtils.isEmpty(phDtos)) {
            int j = 0;
            for (OperationHoursReloadDto phDto : phDtos) {
                Map<String, String> errNameMap = IaisCommonUtils.genNewHashMap();
                errNameMap.put("select", prefix + "PubHoliday");
                errNameMap.put("start", prefix + "PhStart");
                errNameMap.put("end", prefix + "PhEnd");
                errNameMap.put("time", prefix + "PhTime");
                doOperationHoursValidate(phDto, errorMap, errNameMap, i + "" + j, false);
                j++;
            }
            appGrpPremisesDto.setPhDtoList(phDtos);
        }
    }

    //weekly
    private static void validateWeek(AppGrpPremisesDto appGrpPremisesDto, int i, String prefix, Map<String, String> errorMap) {
        List<OperationHoursReloadDto> weeklyDtos = appGrpPremisesDto.getWeeklyDtoList();
        String emptyErrMsg = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        if (IaisCommonUtils.isEmpty(weeklyDtos)) {
            errorMap.put(prefix + "Weekly" + i + 0, emptyErrMsg);
            errorMap.put(prefix + "WeeklyStart" + i + 0, emptyErrMsg);
            errorMap.put(prefix + "WeeklyEnd" + i + 0, emptyErrMsg);
        } else {
            int j = 0;
            for (OperationHoursReloadDto weeklyDto : weeklyDtos) {
                Map<String, String> errNameMap = IaisCommonUtils.genNewHashMap();
                errNameMap.put("select", prefix + "Weekly");
                errNameMap.put("start", prefix + "WeeklyStart");
                errNameMap.put("end", prefix + "WeeklyEnd");
                errNameMap.put("time", prefix + "WeeklyTime");
                doOperationHoursValidate(weeklyDto, errorMap, errNameMap, i + "" + j, true);
                j++;
            }
            appGrpPremisesDto.setWeeklyDtoList(weeklyDtos);
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
                    StringUtil.isEmpty(endMM)) {
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

    public static Map<String, String> doValidatePo(List<AppSvcPrincipalOfficersDto> poDto,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, String svcCode, SubLicenseeDto subLicenseeDto) {
        Map<String, String> oneErrorMap = IaisCommonUtils.genNewHashMap();
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        int poIndex = 0;
        int dpoIndex = 0;
        if (IaisCommonUtils.isEmpty(poDto)) {
            return oneErrorMap;
        }
        String errSalutation = MessageUtil.replaceMessage("GENERAL_ERR0006", "Salutation", "field");
        for (int i = 0; i < poDto.size(); i++) {
            String psnType = poDto.get(i).getPsnType();
            if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
                String assignSelect = poDto.get(i).getAssignSelect();
                if ("-1".equals(assignSelect) || StringUtil.isEmpty(assignSelect)) {
                    oneErrorMap.put("assignSelect" + poIndex,
                            MessageUtil.replaceMessage("GENERAL_ERR0006", "Assign a Principal Officer", "field"));
                } else {
                    String mobileNo = poDto.get(i).getMobileNo();
                    String officeTelNo = poDto.get(i).getOfficeTelNo();
                    String emailAddr = poDto.get(i).getEmailAddr();
                    String idNo = poDto.get(i).getIdNo();
                    String name = poDto.get(i).getName();
                    String salutation = poDto.get(i).getSalutation();
                    String designation = poDto.get(i).getDesignation();
                    String idType = poDto.get(i).getIdType();
                    String nationality = poDto.get(i).getNationality();

                    // check person key
                    String keyIdType = "idType" + poIndex;
                    String keyIdNo = "poNRICFIN" + poIndex;
                    String keyNationality = "nationality" + poIndex;
                    boolean isValid = validateId(nationality, idType, idNo, keyNationality, keyIdType, keyIdNo, oneErrorMap);
                    // check duplicated
                    if (isValid) {
                        String personKey = ApplicationHelper.getPersonKey(nationality, idType, idNo);
                        boolean licPerson = poDto.get(i).isLicPerson();
                        String idTypeNoKey = "poIdTypeNo" + i;
                        isValid = doPsnCommValidate(oneErrorMap, personKey, idNo, licPerson, licPersonMap, idTypeNoKey);
                        if (isValid) {
                            if (stringList.contains(personKey)) {
                                oneErrorMap.put(keyIdNo, "NEW_ERR0012");
                            } else {
                                stringList.add(personKey);
                            }
                        }
                    }

                    String errName = MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field");
                    if (StringUtil.isEmpty(name)) {
                        oneErrorMap.put("name" + poIndex, errName);
                    } else if (name.length() > 110) {
                        String general_err0041 = repLength("Name", "110");
                        oneErrorMap.put("name" + poIndex, general_err0041);
                    }
                    if (StringUtil.isEmpty(salutation)) {
                        oneErrorMap.put("salutation" + poIndex, errSalutation);
                    }
                    if (StringUtil.isEmpty(designation)) {
                        oneErrorMap.put("designation" + poIndex,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Designation", "field"));
                    } else if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                        String otherDesignation = poDto.get(i).getOtherDesignation();
                        if (StringUtil.isEmpty(otherDesignation)) {
                            oneErrorMap.put("otherDesignation" + i,
                                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Others Designation", "field"));
                        } else if (otherDesignation.length() > 100) {
                            String general_err0041 = repLength("Others Designation", "100");
                            oneErrorMap.put("otherDesignation" + i, general_err0041);
                        }
                    }
                    if (!StringUtil.isEmpty(mobileNo)) {
                        if (mobileNo.length() > 8) {
                            String general_err0041 = repLength("Mobile No.", "8");
                            oneErrorMap.put("mobileNo" + poIndex, general_err0041);
                        }
                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            oneErrorMap.put("mobileNo" + poIndex, "GENERAL_ERR0007");
                        }
                    } else {
                        oneErrorMap.put("mobileNo" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006", "Mobile No. ", "field"));
                    }
                    if (!StringUtil.isEmpty(emailAddr)) {
                        if (!ValidationUtils.isEmail(emailAddr)) {
                            oneErrorMap.put("emailAddr" + poIndex, "GENERAL_ERR0014");
                        } else if (emailAddr.length() > 320) {
                            String general_err0041 = repLength("Email Address", "320");
                            oneErrorMap.put("emailAddr" + poIndex, general_err0041);
                        }
                    } else {
                        oneErrorMap.put("emailAddr" + poIndex,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Email Address ", "field"));
                    }
                    if (!StringUtil.isEmpty(officeTelNo)) {
                        if (officeTelNo.length() > 8) {
                            String general_err0041 = repLength("Office Telephone No.", "8");
                            oneErrorMap.put("officeTelNo" + poIndex, general_err0041);
                        }
                        if (!officeTelNo.matches(IaisEGPConstant.OFFICE_TELNO_MATCH)) {
                            oneErrorMap.put("officeTelNo" + poIndex, "GENERAL_ERR0015");
                        }
                    } else {
                        oneErrorMap.put("officeTelNo" + poIndex,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Office Telephone No.", "field"));
                    }
                }
                poIndex++;
            }

            if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)) {
                String salutation = poDto.get(i).getSalutation();
                String name = poDto.get(i).getName();
                String idType = poDto.get(i).getIdType();
                String mobileNo = poDto.get(i).getMobileNo();
                String emailAddr = poDto.get(i).getEmailAddr();
                String idNo = poDto.get(i).getIdNo();
                String designation = poDto.get(i).getDesignation();
                String officeTelNo = poDto.get(i).getOfficeTelNo();
                String nationality = poDto.get(i).getNationality();
                /*if(StringUtil.isEmpty(modeOfMedAlert)||"-1".equals(modeOfMedAlert)){
                    oneErrorMap.put("modeOfMedAlert"+dpoIndex,"GENERAL_ERR0006");
                }*/

                String assignSelect = poDto.get(i).getAssignSelect();
                if (StringUtil.isEmpty(assignSelect) || "-1".equals(assignSelect)) {
                    oneErrorMap.put("deputyAssignSelect" + dpoIndex, MessageUtil.getMessageDesc("NEW_ERR0018"));
                } else {
                    // check person key
                    String keyIdType = "deputyIdType" + dpoIndex;
                    String keyIdNo = "deputyIdNo" + dpoIndex;
                    String keyNationality = "deputyNationality" + dpoIndex;
                    boolean isValid = validateId(nationality, idType, idNo, keyNationality, keyIdType, keyIdNo, oneErrorMap);
                    // check duplicated
                    if (isValid) {
                        String personKey = ApplicationHelper.getPersonKey(nationality, idType, idNo);
                        boolean licPerson = poDto.get(i).isLicPerson();
                        String idTypeNoKey = "dpoIdTypeNo" + dpoIndex;
                        isValid = doPsnCommValidate(oneErrorMap, personKey, idNo, licPerson, licPersonMap, idTypeNoKey);
                        if (isValid) {
                            if (stringList.contains(personKey)) {
                                oneErrorMap.put(keyIdNo, "NEW_ERR0012");
                            } else {
                                stringList.add(personKey);
                            }
                            // 113109
                            if (subLicenseeDto != null && !OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(
                                    subLicenseeDto.getLicenseeType())) {
                                String subLicenseeNationality = subLicenseeDto.getNationality();
                                if (StringUtil.isEmpty(subLicenseeNationality)) {
                                    subLicenseeNationality = AppConsts.NATIONALITY_SG;
                                }
                                String subLicenseeKey = ApplicationHelper.getPersonKey(subLicenseeNationality,
                                        subLicenseeDto.getIdType(), subLicenseeDto.getIdNumber());
                                if (Objects.equals(subLicenseeKey, personKey)) {
                                    oneErrorMap.put("conflictError" + dpoIndex, MessageUtil.getMessageDesc("NEW_ERR0034"));
                                }
                            }
                        }
                    }

                    if (StringUtil.isEmpty(designation) || "-1".equals(designation)) {
                        oneErrorMap.put("deputyDesignation" + dpoIndex,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Designation", "field"));
                    } else if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                        String otherDesignation = poDto.get(i).getOtherDesignation();
                        if (StringUtil.isEmpty(otherDesignation)) {
                            oneErrorMap.put("deputyOtherDesignation" + dpoIndex,
                                    MessageUtil.replaceMessage("GENERAL_ERR0006", "Others Designation", "field"));
                        } else if (otherDesignation.length() > 100) {
                            String general_err0041 = repLength("Others Designation", "100");
                            oneErrorMap.put("deputyOtherDesignation" + dpoIndex, general_err0041);
                        }
                    }
                    if (StringUtil.isEmpty(salutation) || "-1".equals(salutation)) {
                        oneErrorMap.put("deputySalutation" + dpoIndex, errSalutation);
                    }
                    if (StringUtil.isEmpty(name)) {
                        oneErrorMap.put("deputyName" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field"));
                    } else if (name.length() > 110) {
                        String general_err0041 = repLength("Name", "110");
                        oneErrorMap.put("deputyName" + dpoIndex, general_err0041);
                    }
                    if (StringUtil.isEmpty(officeTelNo)) {
                        oneErrorMap.put("deputyofficeTelNo" + dpoIndex,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Office Telephone No.", "field"));
                    } else {
                        if (officeTelNo.length() > 8) {
                            String general_err0041 = repLength("Office Telephone No.", "8");
                            oneErrorMap.put("deputyofficeTelNo" + dpoIndex, general_err0041);
                        }
                        if (!officeTelNo.matches(IaisEGPConstant.OFFICE_TELNO_MATCH)) {
                            oneErrorMap.put("deputyofficeTelNo" + dpoIndex, "GENERAL_ERR0015");
                        }
                    }

                    if (StringUtil.isEmpty(mobileNo)) {
                        oneErrorMap.put("deputyMobileNo" + dpoIndex,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Mobile No.", "field"));
                    } else {
                        if (mobileNo.length() > 8) {
                            String general_err0041 = repLength("Mobile No.", "8");
                            oneErrorMap.put("deputyMobileNo" + dpoIndex, general_err0041);
                        }
                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            oneErrorMap.put("deputyMobileNo" + dpoIndex, "GENERAL_ERR0007");
                        }
                    }
                    if (StringUtil.isEmpty(emailAddr)) {
                        oneErrorMap.put("deputyEmailAddr" + dpoIndex,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Email Address ", "field"));
                    } else {
                        if (emailAddr.length() > 320) {
                            String general_err0041 = repLength("Email Address", "320");
                            oneErrorMap.put("deputyEmailAddr" + dpoIndex, general_err0041);
                        }
                        if (!ValidationUtils.isEmail(emailAddr)) {
                            oneErrorMap.put("deputyEmailAddr" + dpoIndex, "GENERAL_ERR0014");
                        }
                    }

                }
                dpoIndex++;
            }
        }
        return oneErrorMap;
    }

    public static Map<String, String> doValidateKeyAppointmentHolder(List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderList,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, String svcCode) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if (IaisCommonUtils.isEmpty(appSvcKeyAppointmentHolderList)) {
            return errMap;
        }
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < appSvcKeyAppointmentHolderList.size(); i++) {
            AppSvcPrincipalOfficersDto appSvcKeyAppointmentHolder = appSvcKeyAppointmentHolderList.get(i);
            String assignSelect = appSvcKeyAppointmentHolder.getAssignSelect();
            if ("-1".equals(assignSelect) || StringUtil.isEmpty(assignSelect)) {
                errMap.put("assignSel" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Assign a MedAlert Person", "field"));
            } else {
                String idTyp = appSvcKeyAppointmentHolder.getIdType();
                String idNo = appSvcKeyAppointmentHolder.getIdNo();
                String nationality = appSvcKeyAppointmentHolder.getNationality();

                // check person key
                String keyIdType = "idType" + i;
                String keyIdNo = "idNo" + i;
                String keyNationality = "nationality" + i;
                boolean isValid = validateId(nationality, idTyp, idNo, keyNationality, keyIdType, keyIdNo, errMap);
                // check duplicated
                if (isValid) {
                    String personKey = ApplicationHelper.getPersonKey(nationality, idTyp, idNo);
                    boolean licPerson = appSvcKeyAppointmentHolder.isLicPerson();
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


                String salutation = appSvcKeyAppointmentHolder.getSalutation();
                if (StringUtil.isEmpty(salutation)) {
                    errMap.put("salutation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Salutation", "field"));
                }

                String name = appSvcKeyAppointmentHolder.getName();
                if (StringUtil.isEmpty(name)) {
                    errMap.put("name" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field"));
                } else {
                    if (name.length() > 110) {
                        String general_err0041 = repLength("Name", "110");
                        errMap.put("name" + i, general_err0041);
                    }
                }
            }
        }
        return errMap;
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
                String keyIdType = "idTyp" + i;
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

    public static Map<String, String> doValidateLaboratory(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos, String serviceId,
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        int premCount = 0;
        if (appSvcLaboratoryDisciplinesDtos == null || appSvcLaboratoryDisciplinesDtos.isEmpty()) {
            // 117084: This is a mandatory field. Please select one of the following options. (GENERAL_ERR0056)
            map.put("checkError", "GENERAL_ERR0056");
            return map;
        }
        int svcScopeSize = appSvcLaboratoryDisciplinesDtos.size();
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
            if (premCount >= svcScopeSize) {
                break;
            }
            AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto = appSvcLaboratoryDisciplinesDtos.get(premCount);
            List<AppSvcChckListDto> listDtos = IaisCommonUtils.getList(appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList());
            int count = 0;
            if (listDtos.isEmpty()) {
                // 117084: This is a mandatory field. Please select one of the following options. (GENERAL_ERR0056)
                map.put("checkError", "GENERAL_ERR0056");
            } else {
                String err006 = MessageUtil.replaceMessage("GENERAL_ERR0006", HcsaAppConst.PLEASEINDICATE, "field");
                boolean selectOtherScope = selectOtherScope(listDtos);
                if (selectOtherScope) {
                    boolean selectOtherChildrenScope = false;
                    //check children scope is selected
                    List<String> childrenConfigIdList = getOtherScopeChildrenIdList(hcsaSvcSubtypeOrSubsumedDtos);
                    if (!IaisCommonUtils.isEmpty(childrenConfigIdList)) {
                        for (AppSvcChckListDto appSvcChckListDto : listDtos) {
                            if (childrenConfigIdList.contains(appSvcChckListDto.getChkLstConfId())) {
                                selectOtherChildrenScope = true;
                                break;
                            }
                        }
                    }
                    if (!selectOtherChildrenScope) {
                        map.put("otherScopeError" + premCount, err006);
                    }
                }
                for (int i = 0; i < listDtos.size(); i++) {
                    if (HcsaAppConst.PLEASEINDICATE.equals(listDtos.get(i).getChkName()) && StringUtil.isEmpty(
                            listDtos.get(i).getOtherScopeName())) {
                        map.put("pleaseIndicateError" + premCount, err006);
                    }

                    String parentName = listDtos.get(i).getParentName();
                    if (parentName == null) {
                        count++;
                        continue;
                    } else if (listDtos.get(i).isChkLstType()) {
                        if (serviceId.equals(parentName)) {
                            count++;
                            continue;
                        }
                        for (AppSvcChckListDto every : listDtos) {
                            if (every.getChildrenName() != null) {
                                if (every.getChildrenName().equals(parentName)) {
                                    count++;
                                    break;
                                }
                            }
                        }
                    } else if (!listDtos.get(i).isChkLstType()) {
                        for (AppSvcChckListDto every : listDtos) {
                            if (every.getChkLstConfId().equals(parentName)) {
                                count++;
                                break;
                            }
                        }
                    }
                }
            }
            if (count != listDtos.size()) {
                map.put("checkError", "NEW_ERR0012");
            }
            premCount++;
        }
        WebValidationHelper.saveAuditTrailForNoUseResult(map);
        return map;
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
            Map<String, AppSvcPersonAndExtDto> licPersonMap, String svcCode) {
        if (appSvcCgoList == null) {
            return new HashMap<>(1);
        }

        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < appSvcCgoList.size(); i++) {
            String assignSelect = appSvcCgoList.get(i).getAssignSelect();
            if ("-1".equals(assignSelect)) {
                errMap.put("assignSelect" + i,
                        MessageUtil.replaceMessage("GENERAL_ERR0006", "Add/Assign a Clinical Governance Officer", "field"));
            } else {
                String idTyp = appSvcCgoList.get(i).getIdType();
                String idNo = appSvcCgoList.get(i).getIdNo();
                String nationality = appSvcCgoList.get(i).getNationality();

                String keyIdType = "idTyp" + i;
                String keyIdNo = "idNo" + i;
                String keyNationality = "nationality" + i;
                boolean isValid = validateId(nationality, idTyp, idNo, keyNationality, keyIdType, keyIdNo, errMap);
                // check duplicated
                if (isValid) {
                    String personKey = ApplicationHelper.getPersonKey(nationality, idTyp, idNo);
                    boolean licPerson = appSvcCgoList.get(i).isLicPerson();
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

                String salutation = appSvcCgoList.get(i).getSalutation();
                if (StringUtil.isEmpty(salutation)) {
                    errMap.put("salutation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Salutation", "field"));
                }
                String professionType = appSvcCgoList.get(i).getProfessionType();
                if (StringUtil.isEmpty(professionType)) {
                    errMap.put("professionType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Professional Type ", "field"));
                }
                String designation = appSvcCgoList.get(i).getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    errMap.put("designation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Designation", "field"));
                } else if (MasterCodeUtil.DESIGNATION_OTHER_CODE_KEY.equals(designation)) {
                    String otherDesignation = appSvcCgoList.get(i).getOtherDesignation();
                    if (StringUtil.isEmpty(otherDesignation)) {
                        errMap.put("otherDesignation" + i,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Others Designation", "field"));
                    } else if (otherDesignation.length() > 100) {
                        String general_err0041 = repLength("Others Designation", "100");
                        errMap.put("otherDesignation" + i, general_err0041);
                    }

                }
                String professionRegoNo = appSvcCgoList.get(i).getProfRegNo();
                if (!StringUtil.isEmpty(professionRegoNo) && professionRegoNo.length() > 20) {
                    String general_err0041 = repLength("Professional Regn. No.", "20");
                    errMap.put("professionRegoNo" + i, general_err0041);
                }
                String specialty = appSvcCgoList.get(i).getSpeciality();
                if (StringUtil.isEmpty(professionRegoNo) || StringUtil.isEmpty(specialty)) {
                    String otherQualification = appSvcCgoList.get(i).getOtherQualification();
                    if (StringUtil.isEmpty(otherQualification)) {
                        errMap.put("otherQualification" + i,
                                MessageUtil.replaceMessage("GENERAL_ERR0006", "Other Qualification", "field"));
                    } else if (otherQualification.length() > 100) {
                        Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                        repMap.put("number", "100");
                        repMap.put("fieldNo", "Other Qualification");
                        errMap.put("otherQualification" + i, MessageUtil.getMessageDesc("GENERAL_ERR0036", repMap));

                    }
                }

                String name = appSvcCgoList.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    errMap.put("name" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field"));
                } else {
                    if (name.length() > 110) {
                        String general_err0041 = repLength("Name", "110");
                        errMap.put("name" + i, general_err0041);
                    }
                }

                String mobileNo = appSvcCgoList.get(i).getMobileNo();
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
                String emailAddr = appSvcCgoList.get(i).getEmailAddr();

                if (StringUtil.isEmpty(emailAddr)) {
                    errMap.put("emailAddr" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Email Address", "field"));
                } else {
                    if (emailAddr.length() > 320) {
                        String general_err0041 = repLength("Email Address", "320");
                        errMap.put("emailAddr" + i, general_err0041);
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
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
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
        }
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
                int time = getTime(list.get(i).getEndToHH(), list.get(i).getEndToMM());
                int time1 = getTime(list.get(j).getStartFromHH(), list.get(j).getStartFromMM());
                if (time >= time1) {
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
            String businessName = appSvcBusinessDtos.get(i).getBusinessName();
            if (StringUtil.isEmpty(businessName)) {
                errorMap.put("businessName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "businessName", "field"));
            } else {
                if (businessName.length() > 100) {
                    String general_err0041 = repLength("businessName", "100");
                    errorMap.put("businessName" + i, general_err0041);
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
        }
    }

    public static boolean doPsnCommValidate(Map<String, String> errMap, String personKey, String idNo, boolean licPerson,
            Map<String, AppSvcPersonAndExtDto> licPersonMap, String errKey) {
        boolean isValid = true;
        if (needPsnCommValidate() && licPersonMap != null && !StringUtil.isEmpty(personKey) && !StringUtil.isEmpty(
                idNo) && !licPerson) {
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
        return appSubmissionDto != null && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
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
        LicCommClient licenceClient = SpringContextHelper.getContext().getBean(LicCommClient.class);
        PremisesDto premisesDto = licenceClient.getPremisesDtoForBusinessName(licenceId).getEntity();
        if (premisesDto == null) {
            return 0;
        }
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
        if (errorList.contains(HcsaAppConst.SECTION_DOCUMENT)) {
            msg.append(HcsaAppConst.TITLE_DOCUMENT).append(", ");
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

    public static List<AppGrpPrimaryDocDto> documentValid(HttpServletRequest request, Map<String, String> errorMap,
            boolean setIsPassValidate) {
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(request);
        return documentValid(appSubmissionDto, errorMap, setIsPassValidate);
    }

    private static List<AppGrpPrimaryDocDto> documentValid(AppSubmissionDto appSubmissionDto, Map<String, String> errorMap,
            boolean setIsPassValidate) {
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        SystemParamConfig systemParamConfig = getSystemParamConfig();
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        if (appSubmissionDto == null || appSubmissionDto.getAppGrpPrimaryDocDtos() == null) {
            return null;
        }
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
            if (StringUtil.isEmpty(appGrpPrimaryDocDto.getMd5Code())) {
                continue;
            }
            String keyName = "";
            if (StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessName()) && StringUtil.isEmpty(
                    appGrpPrimaryDocDto.getPremisessType())) {
                //common
                keyName = "common" + appGrpPrimaryDocDto.getSvcComDocId();
            } else {
                keyName = "prem" + appGrpPrimaryDocDto.getSvcComDocId() + appGrpPrimaryDocDto.getPremisessName();
            }
            long length = appGrpPrimaryDocDto.getRealDocSize();
            int uploadFileLimit = systemParamConfig.getUploadFileLimit();
            if (length / 1024 / 1024 > uploadFileLimit) {
                errorMap.put(keyName, MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(uploadFileLimit), "sizeMax"));
                continue;
            }
            Boolean flag = Boolean.FALSE;
            String name = appGrpPrimaryDocDto.getDocName();
            if (name.length() > 100) {
                errorMap.put(keyName, MessageUtil.getMessageDesc("GENERAL_ERR0022"));
            }
            String substring = name.substring(name.lastIndexOf('.') + 1);
            String sysFileType = systemParamConfig.getUploadFileType();
            String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
            for (String f : sysFileTypeArr) {
                if (f.equalsIgnoreCase(substring)) {
                    flag = Boolean.TRUE;
                }
            }
            if (!flag) {
                errorMap.put(keyName, MessageUtil.replaceMessage("GENERAL_ERR0018", sysFileType, "fileType"));
            }
            String errMsg = errorMap.get(keyName);
            if (StringUtil.isEmpty(errMsg) && setIsPassValidate) {
                appGrpPrimaryDocDto.setPassValidate(true);
            }
        }
        return appGrpPrimaryDocDtoList;
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

    public static void doValidateDisciplineAllocation(Map<String, String> map, List<AppSvcDisciplineAllocationDto> daList,
            AppSvcRelatedInfoDto currentSvcDto, Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap) {
        if (daList == null || daList.isEmpty()) {
            return;
        }
        int size = daList.size();
        Map<String, String> cgoMap = new HashMap<>();
        Map<String, String> slMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String cgoPerson = daList.get(i).getCgoPerson();
            if (StringUtil.isEmpty(cgoPerson)) {
                map.put("disciplineAllocation" + i,
                        MessageUtil.replaceMessage("GENERAL_ERR0006", "Clinical Governance Officers", "field"));
            } else {
                cgoMap.put(cgoPerson, cgoPerson);
            }
            String indexNo = daList.get(i).getSlIndex();
            if (StringUtil.isEmpty(indexNo)) {
                map.put("disciplineAllocationSl" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Section Leader", "field"));
            } else {
                slMap.put(indexNo, indexNo);
            }
        }
        if (map.isEmpty()) {
            size = ApplicationHelper.getAlocationAutualSize(daList, currentSvcDto.getServiceId(), svcScopeAlignMap);
            String error = MessageUtil.getMessageDesc("NEW_ERR0011");
            List<AppSvcPrincipalOfficersDto> appSvcCgoList = currentSvcDto.getAppSvcCgoDtoList();
            if (appSvcCgoList != null) {
                int mapSize = cgoMap.size();
                int objSize = appSvcCgoList.size();
                if (size > objSize && objSize != mapSize || size <= objSize && size != mapSize) {
                    String result = currentSvcDto.getAppSvcCgoDtoList().stream()
                            .filter(appSvcCgoDto -> !cgoMap.containsKey(ApplicationHelper.getPersonKey(appSvcCgoDto)))
                            .map(AppSvcPrincipalOfficersDto::getName)
                            .filter(Objects::nonNull)
                            .reduce((x, y) -> x + ", " + y)
                            .orElse("");
                    if (result.contains(",")) {
                        error = error.replaceFirst("is", "are");
                    }
                    String replace = error.replace("{CGO Name}", result);
                    map.put("CGO", replace);
                }
            }
            List<AppSvcPersonnelDto> sectionLeaderDtoList = currentSvcDto.getAppSvcSectionLeaderList();
            if (sectionLeaderDtoList != null) {
                int mapSize = slMap.size();
                int objSize = sectionLeaderDtoList.size();
                if (size > objSize && objSize != mapSize || size <= objSize && size != mapSize) {
                    String result = sectionLeaderDtoList.stream()
                            .filter(sectionLeaderDto -> !slMap.containsKey(sectionLeaderDto.getIndexNo()))
                            .map(AppSvcPersonnelDto::getName)
                            .filter(Objects::nonNull)
                            .reduce((x, y) -> x + ", " + y)
                            .orElse("");
                    if (result.contains(",")) {
                        error = error.replaceFirst("is", "are");
                    }
                    String replace = error.replace("{CGO Name}", result);
                    map.put("SL", replace);
                }
            }
        }
    }

    private static void doAppSvcPersonnelDtoList(Map<String, String> map, List<AppSvcPersonnelDto> appSvcPersonnelDtos,
            String svcCode) {
        if (appSvcPersonnelDtos == null) {
            /*if (hcsaSvcPersonnelDtos != null) {
                for (HcsaSvcPersonnelDto every : hcsaSvcPersonnelDtos) {
                    String psnType = every.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL.equals(psnType)) {
                        sB.append(serviceId);
                        return;
                    }
                }
            }*/
            return;
        }

        String errName = MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field");
        String errDesignation = MessageUtil.replaceMessage("GENERAL_ERR0006", "Designation", "field");
        String errRegnNo = MessageUtil.replaceMessage("GENERAL_ERR0006", "Professional Regn. No.", "field");
        String errWrkExpYear = MessageUtil.replaceMessage("GENERAL_ERR0006", "Relevant working experience (Years)", "field");
        String errQualification = MessageUtil.replaceMessage("GENERAL_ERR0006", "Qualification", "field");
        String errSelSvcPsnel = MessageUtil.replaceMessage("GENERAL_ERR0006", "Select Service Personnel", "field");
        for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
            if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)) {
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    map.put("designation" + i, errDesignation);
                }
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    map.put("name" + i, errName);
                }
                String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                if (StringUtil.isEmpty(profRegNo)) {
                    map.put("regnNo" + i, errRegnNo);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    map.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
            } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    map.put("name" + i, errName);
                }
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(quaification)) {
                    map.put("qualification" + i, errQualification);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    map.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
            } else if (!AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)) {
                String name = appSvcPersonnelDtos.get(i).getName();
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(name)) {
                    map.put("name" + i, errName);
                }
                if (StringUtil.isEmpty(wrkExpYear)) {
                    map.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
                if (StringUtil.isEmpty(quaification)) {
                    map.put("quaification" + i, errQualification);
                }
            } else {
                String personnelSel = appSvcPersonnelDtos.get(i).getPersonnelType();
                if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)) {
                    String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                    }
                    if (StringUtil.isEmpty(profRegNo)) {
                        map.put("regnNo" + i, errRegnNo);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String designation = appSvcPersonnelDtos.get(i).getDesignation();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String qualification = appSvcPersonnelDtos.get(i).getQualification();

                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                    }
                    if (StringUtil.isEmpty(designation)) {
                        map.put("designation" + i, errDesignation);
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        map.put("wrkExpYear" + i, errWrkExpYear);
                    } else {
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        }
                    }
                    if (StringUtil.isEmpty(qualification)) {
                        map.put("qualification" + i, errQualification);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String quaification = appSvcPersonnelDtos.get(i).getQualification();
                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        map.put("wrkExpYear" + i, errWrkExpYear);
                    } else {
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        }
                    }
                    if (StringUtil.isEmpty(quaification)) {
                        map.put("quaification" + i, errQualification);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                    }
                }
            }
        }
    }

    private static void doSvcDocument(Map<String, String> map, List<AppSvcDocDto> appSvcDocDtoLit, int uploadFileLimit,
            String sysFileType) {
        if (appSvcDocDtoLit != null) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLit) {
                Integer docSize = appSvcDocDto.getDocSize();
                String docName = appSvcDocDto.getDocName();
                if (docName == null) {
                    continue;
                }
                Boolean flag = Boolean.FALSE;
                String substring = docName.substring(docName.lastIndexOf('.') + 1);
                if (docSize / 1024 > uploadFileLimit) {
                    map.put("svcDocError", "error");
                }

                if (docName.length() > 100) {
                    map.put("svcDocError", "error");
                }

                String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
                for (String f : sysFileTypeArr) {
                    if (f.equalsIgnoreCase(substring)) {
                        flag = Boolean.TRUE;
                    }
                }
                if (!flag) {
                    map.put("svcDocError", "error");
                }
            }

        }
    }

    public static void doValidatetionServicePerson(Map<String, String> errorMap, List<AppSvcPersonnelDto> appSvcPersonnelDtos,
            String svcCode) {
        if(IaisCommonUtils.isEmpty(appSvcPersonnelDtos)){
            return;
        }
        String errName = MessageUtil.replaceMessage("GENERAL_ERR0006","Name","field");
        String errDesignation = MessageUtil.replaceMessage("GENERAL_ERR0006","Designation","field");
        String errRegnNo = MessageUtil.replaceMessage("GENERAL_ERR0006","Professional Regn. No.","field");
        String errWrkExpYear = MessageUtil.replaceMessage("GENERAL_ERR0006","Relevant working experience (Years)","field");
        String errQualification = MessageUtil.replaceMessage("GENERAL_ERR0006","Qualification","field");
        String errSelSvcPsnel = MessageUtil.replaceMessage("GENERAL_ERR0006","Select Service Personnel","field");
        String errOtherDesignation = MessageUtil.replaceMessage("GENERAL_ERR0006","Others Designation","field");

        String errLengthName = repLength("Name","110");
        String errLengthRegnNo = repLength("Professional Regn. No.","20");
        String errLengthWrkExpYear = repLength("Relevant working experience (Years)","2");
        String errLengthQualification = repLength("Qualification","100");
        String errLengthOtherDesignation = repLength("Others Designation","100");
        List<SelectOption> personnelTypeSel = ApplicationHelper.genPersonnelTypeSel(svcCode);
        //Verify that each type of person has at least one
        //person type,value/empty
        Map<String,String> personCountMap = IaisCommonUtils.genNewHashMap();

        for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
            String personType = appSvcPersonnelDtos.get(i).getPersonnelType();
            if(!StringUtil.isEmpty(personType)){
                personCountMap.put(personType,AppConsts.YES);
            }
            if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)) {
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    errorMap.put("designation" + i, errDesignation);
                }else if(HcsaAppConst.DESIGNATION_OTHERS.equals(designation)){
                    String otherDesignation = appSvcPersonnelDtos.get(i).getOtherDesignation();
                    if(StringUtil.isEmpty(otherDesignation)){
                        errorMap.put("otherDesignation"+i,errOtherDesignation);
                    }else if(otherDesignation.length() > 100){
                        errorMap.put("otherDesignation" + i, errLengthOtherDesignation);
                    }
                }
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, errName);
                }else if(name.length() > 110){
                    errorMap.put("name" + i, errLengthName);
                }
                String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                if (StringUtil.isEmpty(profRegNo)) {
                    errorMap.put("regnNo" + i, errRegnNo);
                }else if(profRegNo.length() > 20){
                    errorMap.put("regnNo" + i, errLengthRegnNo);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if(wrkExpYear.length() > 2){
                        errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                    }
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
            } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, errName);
                }else if(name.length() > 110){
                    errorMap.put("name" + i, errLengthName);
                }
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(quaification)) {
                    errorMap.put("qualification" + i, errQualification);
                }else if(quaification.length() > 100){
                    errorMap.put("qualification" + i, errLengthQualification);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if(wrkExpYear.length() > 2){
                        errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                    }
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                    }
                }
            }else if(!AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)){
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    errorMap.put("name" + i, errName);
                }else if(name.length() > 110){
                    errorMap.put("name" + i, errLengthName);
                }
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(quaification)) {
                    errorMap.put("qualification" + i, errQualification);
                }else if(quaification.length() > 100){
                    errorMap.put("qualification" + i, errLengthQualification);
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    errorMap.put("wrkExpYear" + i, errWrkExpYear);
                } else {
                    if(wrkExpYear.length() > 2){
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
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    }else if(name.length() > 110){
                        errorMap.put("name" + i, errLengthName);
                    }
                    if (StringUtil.isEmpty(profRegNo)) {
                        errorMap.put("regnNo" + i, errRegnNo);
                    }else if(profRegNo.length() > 20){
                        errorMap.put("regnNo" + i, errLengthRegnNo);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String designation = appSvcPersonnelDtos.get(i).getDesignation();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String qualification = appSvcPersonnelDtos.get(i).getQualification();

                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    }else if(name.length() > 110){
                        errorMap.put("name" + i, errLengthName);
                    }
                    if (StringUtil.isEmpty(designation)) {
                        errorMap.put("designation" + i, errDesignation);
                    }else if(HcsaAppConst.DESIGNATION_OTHERS.equals(designation)){
                        String otherDesignation = appSvcPersonnelDtos.get(i).getOtherDesignation();
                        if(StringUtil.isEmpty(otherDesignation)){
                            errorMap.put("otherDesignation"+i,errOtherDesignation);
                        }else if(otherDesignation.length() > 100){
                            errorMap.put("otherDesignation" + i, errLengthOtherDesignation);
                        }
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        errorMap.put("wrkExpYear" + i, errWrkExpYear);
                    } else {
                        if(wrkExpYear.length() > 2){
                            errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                        }
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        }
                    }
                    if (StringUtil.isEmpty(qualification)) {
                        errorMap.put("qualification" + i, errQualification);
                    }else if(qualification.length() > 100){
                        errorMap.put("qualification" + i, errLengthQualification);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String quaification = appSvcPersonnelDtos.get(i).getQualification();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    }else if(name.length() > 110){
                        errorMap.put("name" + i, errLengthName);
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        errorMap.put("wrkExpYear" + i, errWrkExpYear);
                    } else {
                        if(wrkExpYear.length() > 2){
                            errorMap.put("wrkExpYear" + i, errLengthWrkExpYear);
                        }
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            errorMap.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        }
                    }
                    if (StringUtil.isEmpty(quaification)) {
                        errorMap.put("qualification" + i, errQualification);
                    }else if(quaification.length() > 100){
                        errorMap.put("qualification" + i, errLengthQualification);
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        errorMap.put("name" + i, errName);
                    }else if(name.length() > 110){
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

}
