package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.*;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author weilu
 * @date 2020/2/26 16:29
 */
@Service
@Slf4j
public class CessationBeServiceImpl implements CessationBeService {
    private final static String workGroupId = "4C43D448-F90C-EA11-BE7D-000C29F371DC";
    private final static String ASO_WORKGROUPID = "BF3B0634-F80C-EA11-BE7D-000C29F371DC";
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private CessationClient cessationClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HcsaAppClient hcsaAppClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Autowired
    private TaskApplicationClient taskApplicationClient;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private ApplicationService applicationService;

    @Override
    public List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds) {
        List<AppCessLicDto> appCessDtos = IaisCommonUtils.genNewArrayList();
        if (licIds != null && !licIds.isEmpty()) {
            for (String licId : licIds) {
                AppCessLicDto appCessDto = new AppCessLicDto();
                LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(licId).getEntity();
                List<PremisesDto> premisesDtos = hcsaLicenceClient.getPremisess(licId).getEntity();
                String svcName = licenceDto.getSvcName();
                String licenceNo = licenceDto.getLicenceNo();
                appCessDto.setLicenceNo(licenceNo);
                appCessDto.setSvcName(svcName);
                appCessDto.setLicenceId(licId);
                List<AppCessHciDto> appCessHciDtos = IaisCommonUtils.genNewArrayList();
                if (premisesDtos != null && !premisesDtos.isEmpty()) {
                    for (PremisesDto premisesDto : premisesDtos) {
                        String blkNo = premisesDto.getBlkNo();
                        String premisesId = premisesDto.getId();
                        String streetName = premisesDto.getStreetName();
                        String buildingName = premisesDto.getBuildingName();
                        String floorNo = premisesDto.getFloorNo();
                        String unitNo = premisesDto.getUnitNo();
                        String postalCode = premisesDto.getPostalCode();
                        String hciCode = premisesDto.getHciCode();
                        String hciAddress = MiscUtil.getAddress(blkNo, streetName, buildingName, floorNo, unitNo, postalCode,premisesDto.getPremisesOperationalUnitDtos());
                        AppCessHciDto appCessHciDto = new AppCessHciDto();
                        String hciName = premisesDto.getHciName();
                        appCessHciDto.setHciCode(hciCode);
                        appCessHciDto.setHciName(hciName);
                        appCessHciDto.setPremiseId(premisesId);
                        appCessHciDto.setHciAddress(hciAddress);
                        appCessHciDtos.add(appCessHciDto);
                    }
                }
                appCessDto.setAppCessHciDtos(appCessHciDtos);
                appCessDtos.add(appCessDto);
            }
            return appCessDtos;
        } else {
            return null;
        }
    }


    @Override
    public Map<String, List<String>> saveCessations(List<AppCessationDto> appCessationDtos) {
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
        Map<String, List<String>> licPremiseIdMap = IaisCommonUtils.genNewHashMap();
        for (AppCessationDto appCessationDto : appCessationDtos) {
            String licId = appCessationDto.getLicId();
            String premiseId = appCessationDto.getPremiseId();
            List<String> premiseIds = licPremiseIdMap.get(licId);
            if (premiseIds == null) {
                List<String> premisesIds = IaisCommonUtils.genNewArrayList();
                premisesIds.add(premiseId);
                licPremiseIdMap.put(licId, premisesIds);
            } else {
                premiseIds.add(premiseId);
            }
        }
        Map<String, List<String>> appIdPremisesMap = IaisCommonUtils.genNewHashMap();
        licPremiseIdMap.forEach((licId, premiseIds) -> {
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            licIds.clear();
            licIds.add(licId);
            AppSubmissionDto appSubmissionDto = hcsaLicenceClient.getAppSubmissionDtos(licIds).getEntity().get(0);
            filetDoc(appSubmissionDto);
            List<String> specLicIds = hcsaLicenceClient.getActSpecIdByActBaseId(licId).getEntity();
            Map<String, List<String>> baseMap = transform(appSubmissionDto, premiseIds);
            if (!IaisCommonUtils.isEmpty(specLicIds)) {
                AppSubmissionDto appSubmissionDtoSpec = hcsaLicenceClient.getAppSubmissionDtos(specLicIds).getEntity().get(0);
                filetDoc(appSubmissionDtoSpec);
                Map<String, List<String>> specMap = transform(appSubmissionDtoSpec, premiseIds);
                for (String premiseId : premiseIds) {
                    List<String> baseAppIds = baseMap.get(premiseId);
                    List<String> specAppIds = specMap.get(premiseId);
                    baseAppIds.addAll(specAppIds);
                }
            }
            appIdPremisesMap.putAll(baseMap);
        });

        appIdPremisesMap.forEach((premiseId, appIds) -> {
            for (AppCessationDto appCessationDto : appCessationDtos) {
                String premiseId1 = appCessationDto.getPremiseId();
                if (premiseId.equals(premiseId1)) {
                    List<AppCessMiscDto> appCessMiscDtos1 = setMiscData(appCessationDto, appIds);
                    appCessMiscDtos.addAll(appCessMiscDtos1);
                }
            }
        });
        cessationClient.saveCessation(appCessMiscDtos).getEntity();
        return appIdPremisesMap;
    }

    @Override
    public Map<String, Boolean> listResultCeased(List<String> licIds) {
        return cessationClient.listCanCeased(licIds).getEntity();
    }

    @Override
    public List<String> listHciName() {
        List<String> hciNames = hcsaLicenceClient.listHciNames().getEntity();
        return hciNames;
    }

    @Override
    public List<AppSpecifiedLicDto> getSpecLicInfo(List<String> licIds) {
        List<AppSpecifiedLicDto> appSpecifiedLicDtos = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(licIds)) {
            return appSpecifiedLicDtos;
        }
        for (String licId : licIds) {
            LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(licId).getEntity();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
                List<String> specLicIds = hcsaLicenceClient.getActSpecIdByActBaseId(licId).getEntity();
                if (!IaisCommonUtils.isEmpty(specLicIds)) {
                    for (String specLicId : specLicIds) {
                        AppSpecifiedLicDto appSpecifiedLicDto = new AppSpecifiedLicDto();
                        LicenceDto specLicenceDto = hcsaLicenceClient.getLicDtoById(specLicId).getEntity();
                        if (specLicenceDto != null) {
                            String specLicenceNo = specLicenceDto.getLicenceNo();
                            String licenceDtoId = specLicenceDto.getId();
                            String specSvcName = specLicenceDto.getSvcName();
                            appSpecifiedLicDto.setBaseLicNo(licenceNo);
                            appSpecifiedLicDto.setBaseSvcName(svcName);
                            appSpecifiedLicDto.setSpecLicNo(specLicenceNo);
                            appSpecifiedLicDto.setSpecSvcName(specSvcName);
                            appSpecifiedLicDto.setSpecLicId(licenceDtoId);
                            appSpecifiedLicDtos.add(appSpecifiedLicDto);
                        }
                    }
            }
        }
        return appSpecifiedLicDtos;
    }


    @Override
    public List<String> filtrateSpecLicIds(List<String> licIds) {
        List<String> specLicIds = IaisCommonUtils.genNewArrayList();
        List<String> specLicIdsE = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(licIds)) {
            return specLicIds;
        }
        for (String licId : licIds) {
                List<String> specIds = hcsaLicenceClient.getActSpecIdByActBaseId(licId).getEntity();
                if (!IaisCommonUtils.isEmpty(specIds)) {
                    for (String specLicId : specIds) {
                        LicenceDto specLicenceDto = hcsaLicenceClient.getLicDtoById(specLicId).getEntity();
                        if (specLicenceDto != null) {
                            String licenceDtoId = specLicenceDto.getId();
                            specLicIds.add(licenceDtoId);
                        }
                    }
                }
            }
        if (!IaisCommonUtils.isEmpty(specLicIds)) {
            for (String specId : specLicIds) {
                if (licIds.contains(specId)) {
                    specLicIdsE.add(specId);
                }
            }
        }
        return specLicIdsE;
    }

    @Override
    public List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, Map<String, List<String>> appIdPremisesMap, LoginContext loginContext) throws Exception {
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        List<AppCessatonConfirmDto> appCessationDtosConfirms = IaisCommonUtils.genNewArrayList();
        List<String> svcCodes = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < appCessationDtos.size(); i++) {
            AppCessationDto appCessationDto = appCessationDtos.get(i);
            String premiseId = appCessationDto.getPremiseId();
            String licId = appCessationDto.getLicId();
            LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(licId).getEntity();
            licIds.clear();
            licIds.add(licId);
            List<String> appIds = appIdPremisesMap.get(premiseId);
            String appId = null;
            String baseAppNo = null ;
            List<ApplicationDto> specApplicationDtos = IaisCommonUtils.genNewArrayList();
            for(String id : appIds) {
                ApplicationDto applicationDto = applicationClient.getApplicationById(id).getEntity();
                String appNo = applicationDto.getApplicationNo();
                String originLicenceId = applicationDto.getOriginLicenceId();
                if(licId.equals(originLicenceId)){
                    baseAppNo = appNo ;
                    break;
                }
            }
            for(String id : appIds){
                ApplicationDto applicationDto = applicationClient.getApplicationById(id).getEntity();
                String originLicenceId = applicationDto.getOriginLicenceId();
                if(licId.equals(originLicenceId)){
                    applicationDtos.add(applicationDto);
                    appId = id;
                }else {
                    applicationDto.setBaseApplicationNo(baseAppNo);
                    specApplicationDtos.add(applicationDto);
                }
            }
            if(!IaisCommonUtils.isEmpty(specApplicationDtos)){
                applicationClient.updateCessationApplications(specApplicationDtos).getEntity();
            }
            List<AppCessLicDto> appCessDtosByLicIds = getAppCessDtosByLicIds(licIds);
            AppCessLicDto appCessLicDto = appCessDtosByLicIds.get(0);
            AppGrpPremisesDto appGrpPremisesDto = cessationClient.getAppGrpPremisesDtoByAppId(appId).getEntity();
            String hciCode = appGrpPremisesDto.getHciCode();
            String hciName = null;
            String hciAddress = null;
            List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
            if (!IaisCommonUtils.isEmpty(appCessHciDtos)) {
                for (AppCessHciDto appCessHciDto : appCessHciDtos) {
                    String hciCode1 = appCessHciDto.getHciCode();
                    if (hciCode.equals(hciCode1)) {
                        hciName = appCessHciDto.getHciName();
                        hciAddress = appCessHciDto.getHciAddress();
                    }
                }
            }
            String licenceNo = licenceDto.getLicenceNo();
            String svcName = appCessLicDto.getSvcName();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
            svcCodes.add(hcsaServiceDto.getSvcCode());
            Date effectiveDate = appCessationDto.getEffectiveDate();
            AppCessatonConfirmDto appCessatonConfirmDto = new AppCessatonConfirmDto();
            appCessatonConfirmDto.setAppNo(baseAppNo);
            appCessatonConfirmDto.setEffectiveDate(effectiveDate);
            appCessatonConfirmDto.setHciAddress(hciAddress);
            appCessatonConfirmDto.setSvcName(svcName);
            appCessatonConfirmDto.setLicenceNo(licenceNo);
            appCessatonConfirmDto.setHciName(hciName);
            appCessationDtosConfirms.add(appCessatonConfirmDto);
        }
        routingTaskToAo3(applicationDtos, loginContext);
        List<String> licNos = IaisCommonUtils.genNewArrayList();
        for (AppCessatonConfirmDto appCessatonConfirmDto : appCessationDtosConfirms) {
            String licenceNo = appCessatonConfirmDto.getLicenceNo();
            Date effectiveDate = appCessatonConfirmDto.getEffectiveDate();
            if (effectiveDate.before(new Date())) {
                licNos.add(licenceNo);
            }
        }
        Collections.sort(appCessationDtosConfirms, (s1, s2) -> (s1.getAppNo().compareTo(s2.getAppNo())));
        return appCessationDtosConfirms;
    }

    @Override
    public boolean isGrpLicence(List<String> licIds) {
        LicenceDto entity = hcsaLicenceClient.getLicDtoById(licIds.get(0)).getEntity();
        return entity.isGrpLic();
    }

    @Override
    public List<AppCessLicDto> initData(String corrId) {
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(corrId);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String originLicenceId = applicationDto.getOriginLicenceId();
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        licIds.add(originLicenceId);
        List<String> corrIds = IaisCommonUtils.genNewArrayList();
        corrIds.add(corrId);
        List<AppCessLicDto> appCessDtosByLicIds = getAppCessDtosByLicIds(licIds);
        List<AppCessMiscDto> appCessMiscDtos = cessationClient.getAppCessMiscDtosByCorrIds(corrIds).getEntity();
        if (!IaisCommonUtils.isEmpty(appCessDtosByLicIds)) {
            AppCessLicDto appCessLicDto = appCessDtosByLicIds.get(0);
            if (!IaisCommonUtils.isEmpty(appCessMiscDtos)) {
                AppCessMiscDto appCessMiscDto = appCessMiscDtos.get(0);
                AppCessHciDto appCessHciDto = appCessLicDto.getAppCessHciDtos().get(0);
                Map<String, String> fieldMap = IaisCommonUtils.genNewHashMap();
                MiscUtil.transferEntityDto(appCessMiscDto, AppCessHciDto.class, fieldMap, appCessHciDto);
                Boolean patNeedTrans = appCessMiscDto.getPatNeedTrans();
                if (patNeedTrans) {
                    String patTransType = appCessMiscDto.getPatTransType();
                    String patTransTo = appCessMiscDto.getPatTransTo();
                    String mobileNo = appCessMiscDto.getMobileNo();
                    String emailAddress = appCessMiscDto.getEmailAddress();

                    appCessHciDto.setPatientSelect(patTransType);
                    if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
                        appCessHciDto.setPatHciName(patTransTo);
                    }
                    if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
                        appCessHciDto.setPatRegNo(patTransTo);
                    }
                    if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
                        appCessHciDto.setPatOthers(patTransTo);
                        appCessHciDto.setMobileNo(mobileNo);
                        appCessHciDto.setEmailAddress(emailAddress);
                    }
                } else {
                    String remarks = appCessMiscDto.getPatNoReason();
                    appCessHciDto.setPatNoRemarks(remarks);
                    appCessHciDto.setPatNoConfirm("no");
                }
            }
        }
        return appCessDtosByLicIds;
    }

    @Override
    public void saveRfiCessation(AppCessationDto appCessationDto, TaskDto taskDto, LoginContext loginContext) throws FeignException {
        String refNo = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(refNo);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        completedTask(taskDto, applicationDto.getApplicationNo());
        String originLicenceId = applicationDto.getOriginLicenceId();
        List<String> licIds = IaisCommonUtils.genNewArrayList();
        licIds.add(originLicenceId);
        List<String> corrIds = IaisCommonUtils.genNewArrayList();
        corrIds.add(refNo);
        String applicationDtoId = applicationDto.getId();
        List<String> appIds = IaisCommonUtils.genNewArrayList();
        appIds.add(applicationDtoId);
        List<AppCessMiscDto> appCessMiscDtos = setMiscData(appCessationDto, appIds);
        cessationClient.updateCessation(appCessMiscDtos.get(0)).getEntity();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        updateApplicaitonStatus(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
        routingTaskToAo3(applicationDtos, loginContext);
    }

    @Override
    public PremisesDto getPremiseByHciCodeName(String hciNameCode) {
        PremisesDto premisesDto = hcsaLicenceClient.getPremiseDtoByHciCodeOrName(hciNameCode).getEntity();
        if(premisesDto!=null){
            String blkNo = premisesDto.getBlkNo();
            String streetName = premisesDto.getStreetName();
            String buildingName = premisesDto.getBuildingName();
            String floorNo = premisesDto.getFloorNo();
            String unitNo = premisesDto.getUnitNo();
            String postalCode = premisesDto.getPostalCode();
            String hciAddress = MiscUtil.getAddress(blkNo, streetName, buildingName, floorNo, unitNo, postalCode,
                    premisesDto.getPremisesOperationalUnitDtos());
            premisesDto.setHciAddress(hciAddress);
        }
        return premisesDto;
    }

    private TaskDto completedTask(TaskDto taskDto, String appNo) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setApplicationNo(appNo);
        taskDto.setSlaRemainInDays(taskService.remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskService.updateTask(taskDto);
    }

    private void updateApplicaitonStatus(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        try {
            applicationService.updateFEApplicaiton(applicationDto);
        } catch (Exception e) {
            log.info(StringUtil.changeForLog("========================eic error===================="));
        }
        applicationClient.updateApplication(applicationDto).getEntity();
    }


    private void routingTaskToAo3(List<ApplicationDto> applicationDtos, LoginContext loginContext) throws FeignException {
        String curRoleId = loginContext.getCurRoleId();
        for (ApplicationDto applicationDto : applicationDtos) {
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = applicationClient.getHistoryByAppNoAndDecision(applicationDto.getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_CESSATION_BE_DECISION).getEntity();
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
            appPremisesRoutingHistoryDto.setRoleId(curRoleId);
            appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_ASO);
            appPremisesRoutingHistoryDto.setApplicationNo(applicationDto.getApplicationNo());
            appPremisesRoutingHistoryDto.setActionby(loginContext.getUserId());
            appPremisesRoutingHistoryDto.setAppStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
            appPremisesRoutingHistoryDto.setAuditTrailDto(AuditTrailDto.getThreadDto());
            appPremisesRoutingHistoryDto.setWrkGrpId(ASO_WORKGROUPID);
            List<AppPremisesRoutingHistoryDto> asoHistory = IaisCommonUtils.genNewArrayList();
            asoHistory.add(appPremisesRoutingHistoryDto);
            if(IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtos)){
                appPremisesRoutingHistoryDto.setProcessDecision(ApplicationConsts.APPLICATION_STATUS_CESSATION_BE_DECISION);
            }else {
                appPremisesRoutingHistoryDto.setProcessDecision(ApplicationConsts.PROCESSING_DECISION_REPLY);
            }
            taskService.createHistorys(asoHistory);
        }

        TaskHistoryDto taskHistoryDto = prepareTask(applicationDtos);
        List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
        taskService.createTasks(taskDtos);
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
        appPremisesRoutingHistoryDtos.get(0).setActionby(loginContext.getUserId());
       //appPremisesRoutingHistoryDtos.get(0).setWrkGrpId(workGroupId);
        appPremisesRoutingHistoryDtos.get(0).setWrkGrpId(ASO_WORKGROUPID);
        taskService.createHistorys(appPremisesRoutingHistoryDtos);
    }

    private Map<String, List<String>> transform(AppSubmissionDto appSubmissionDto, List<String> premiseIds) {
        Map<String, List<String>> map = IaisCommonUtils.genNewHashMap();
        String licenseeId = appSubmissionDto.getLicenseeId();
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getCurrentAuditTrailDto();
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_CESSATION, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
        log.info(StringUtil.changeForLog("============================serviceName" + serviceName));
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
        String svcId = hcsaServiceDto.getId();
        String svcCode = hcsaServiceDto.getSvcCode();
        appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
        appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
        appSubmissionDto.setAppGrpNo(grpNo);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        appSubmissionDto.setAmount(amount);
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setFromBe(true);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        setRiskToDto(appSubmissionDto);
        AppSubmissionDto entity = applicationClient.saveApps(appSubmissionDto).getEntity();
        AppSubmissionDto appSubmissionDtoSave = applicationClient.saveSubmision(entity).getEntity();
        List<ApplicationDto> applicationDtos = appSubmissionDtoSave.getApplicationDtos();
        List<String> hciCodes = IaisCommonUtils.genNewArrayList();
        for (String premiseId : premiseIds) {
            PremisesDto premisesDto = hcsaLicenceClient.getLicPremisesDtoById(premiseId).getEntity();
            String hciCode = premisesDto.getHciCode();
            hciCodes.add(hciCode);
        }
        for (ApplicationDto applicationDto : applicationDtos) {
            String id = applicationDto.getId();
            AppGrpPremisesDto dto = cessationClient.getAppGrpPremisesDtoByAppId(id).getEntity();
            String hciCode = dto.getHciCode();
            if (hciCodes.contains(hciCode)) {
                String appId = id;
                for (String premiseId : premiseIds) {
                    PremisesDto entity1 = hcsaLicenceClient.getLicPremisesDtoById(premiseId).getEntity();
                    String hciCode1 = entity1.getHciCode();
                    hciCodes.add(hciCode);
                    if (hciCode.equals(hciCode1)) {
                        List<String> appIds = IaisCommonUtils.genNewArrayList();
                        appIds.add(appId);
                        map.put(premiseId, appIds);
                    }
                }
                applicationDto.setNeedNewLicNo(false);
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NOT);
            } else {
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NEED);
                applicationDto.setNeedNewLicNo(true);
            }
            applicationClient.updateApplication(applicationDto);
        }
        return map;
    }

    private List<AppCessMiscDto> setMiscData(AppCessationDto appCessationDto, List<String> appIds) {
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        Date effectiveDate = appCessationDto.getEffectiveDate();
        String reason = appCessationDto.getReason();
        String otherReason = appCessationDto.getOtherReason();
        Boolean patNeedTrans = appCessationDto.getPatNeedTrans();
        String patientSelect = appCessationDto.getPatientSelect();
        String patHciName = appCessationDto.getPatHciName();
        String patRegNo = appCessationDto.getPatRegNo();
        String patOthers = appCessationDto.getPatOthers();
        String patNoRemarks = appCessationDto.getPatNoRemarks();
        String emailAddress = appCessationDto.getEmailAddress();
        String mobileNo = appCessationDto.getMobileNo();
        for (String appId : appIds) {
            AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
            appCessMiscDto.setAppealType(ApplicationConsts.CESSATION_TYPE_APPLICATION);
            appCessMiscDto.setEffectiveDate(effectiveDate);
            appCessMiscDto.setReason(reason);
            appCessMiscDto.setPatNeedTrans(patNeedTrans);
            appCessMiscDto.setPatTransType(patientSelect);
            appCessMiscDto.setTransferDetail(appCessationDto.getTransferDetail());
            appCessMiscDto.setTransferredWhere(appCessationDto.getTransferredWhere());
            appCessMiscDto.setAppId(appId);
            appCessMiscDto.setAuditTrailDto(currentAuditTrailDto);
            //reason
            if(ApplicationConsts.CESSATION_REASON_OTHER.equals(reason)){
                appCessMiscDto.setOtherReason(otherReason);
            }
            if(patNeedTrans){
                if(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patientSelect)) {
                    appCessMiscDto.setPatTransTo(patHciName);
                }else if(ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patientSelect)){
                    appCessMiscDto.setPatTransTo(patRegNo);
                }else {
                    appCessMiscDto.setPatTransTo(patOthers);
                    appCessMiscDto.setMobileNo(mobileNo);
                    appCessMiscDto.setEmailAddress(emailAddress);
                }
            }else {
                appCessMiscDto.setPatNoReason(patNoRemarks);
            }
            appCessMiscDtos.add(appCessMiscDto);
        }
        return appCessMiscDtos;
    }

    private void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        List<RiskResultDto> riskResultDtoList = hcsaConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList, serviceCode);
            if (riskResultDto != null) {
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }

    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList, String serviceCode) {
        if (riskResultDtoList == null || StringUtil.isEmpty(serviceCode)) {
            return null;
        }
        for (RiskResultDto riskResultDto : riskResultDtoList) {
            if (serviceCode.equals(riskResultDto.getSvcCode())) {
                return riskResultDto;
            }
        }
        return null;
    }

    private TaskHistoryDto prepareTask(List<ApplicationDto> applicationDtos) throws FeignException {
        TaskHistoryDto taskHistoryDto = new TaskHistoryDto();
        List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = IaisCommonUtils.genNewArrayList();
        Date assignDate = new Date();
        String TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        TaskDto userIdForWorkGroup = taskService.getUserIdForWorkGroup(workGroupId);
        String userId = userIdForWorkGroup.getUserId();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_AO3);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        for (ApplicationDto applicationDto : applicationDtos) {
            Set<String> ao3 = taskService.getInspectiors(applicationDto.getApplicationNo(), TaskConsts.TASK_PROCESS_URL_MAIN_FLOW, RoleConsts.USER_ROLE_AO3);
            if(!IaisCommonUtils.isEmpty(ao3)){
                Iterator<String> iterator = ao3.iterator();
                userId = iterator.next();
            }
            int score = getConfigScoreForService(hcsaSvcStageWorkingGroupDtos, applicationDto.getServiceId(),
                    HcsaConsts.ROUTING_STAGE_AO3, applicationDto.getApplicationType());
            List<AppPremisesCorrelationDto> appPremisesCorrelations = getAppPremisesCorrelationId(applicationDto.getId());
            if (!IaisCommonUtils.isEmpty(appPremisesCorrelations)) {
                for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelations) {
                    TaskDto taskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(), HcsaConsts.ROUTING_STAGE_AO3, TaskConsts.TASK_TYPE_MAIN_FLOW,
                            appPremisesCorrelationDto.getId(),TaskConsts.TASK_STATUS_PENDING, workGroupId,
                            userId, assignDate,null, score, TaskUrl, RoleConsts.USER_ROLE_AO3,
                            IaisEGPHelper.getCurrentAuditTrailDto());
                    taskDtos.add(taskDto);
                    //create history
                    log.debug(StringUtil.changeForLog("the appPremisesCorrelationId is -->;" + appPremisesCorrelationDto.getId()));
                    AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =
                            createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(),
                                    HcsaConsts.ROUTING_STAGE_AO3, null, RoleConsts.USER_ROLE_AO3, IaisEGPHelper.getCurrentAuditTrailDto());
                    appPremisesRoutingHistoryDto.setWrkGrpId(workGroupId);
                    appPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
                }
            }
        }
        taskHistoryDto.setTaskDtoList(taskDtos);
        taskHistoryDto.setAppPremisesRoutingHistoryDtos(appPremisesRoutingHistoryDtos);
        return taskHistoryDto;
    }


    private int getConfigScoreForService(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos, String serviceId,
                                         String stageId, String appType) {
        int result = 0;
        if (StringUtil.isEmpty(serviceId) || StringUtil.isEmpty(stageId) || StringUtil.isEmpty(appType)) {
            return result;
        }
        for (HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto : hcsaSvcStageWorkingGroupDtos) {
            if (serviceId.equals(hcsaSvcStageWorkingGroupDto.getServiceId())
                    && stageId.equals(hcsaSvcStageWorkingGroupDto.getStageId())
                    && appType.equals(hcsaSvcStageWorkingGroupDto.getType())) {
                result = hcsaSvcStageWorkingGroupDto.getCount() == null ? 0 : hcsaSvcStageWorkingGroupDto.getCount();
            }
        }
        return result;
    }

    private List<AppPremisesCorrelationDto> getAppPremisesCorrelationId(String appId) {
        return taskApplicationClient.getAppPremisesCorrelationsByAppId(appId).getEntity();
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId) {
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos start ...."));
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos stageId -->:" + stageId));
        for (ApplicationDto applicationDto : applicationDtos) {
            AppGrpPremisesEntityDto appGrpPremisesEntityDto = hcsaAppClient.getPremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            if (appGrpPremisesEntityDto != null) {
                hcsaSvcStageWorkingGroupDto.setPremiseType(appGrpPremisesEntityDto.getPremisesType());
            } else {
                log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos this APP do not have the premise :" + applicationDto.getApplicationNo()));
            }
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos end ...."));
        return hcsaSvcStageWorkingGroupDtos;
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                         String stageId, String internalRemarks, String roleId,
                                                                         AuditTrailDto auditTrailDto) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(auditTrailDto == null ?
                AppConsts.USER_ID_SYSTEM : auditTrailDto.getMohUserGuid());
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(auditTrailDto);
        return appPremisesRoutingHistoryDto;
    }

    private void filetDoc(AppSubmissionDto appSubmissionDto){
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                if(!IaisCommonUtils.isEmpty(appSvcDocDtoLit)){
                    ListIterator<AppSvcDocDto> appSvcDocDtoListIterator = appSvcDocDtoLit.listIterator();
                    while (appSvcDocDtoListIterator.hasNext()){
                        AppSvcDocDto appSvcDocDto = appSvcDocDtoListIterator.next();
                        String fileRepoId = appSvcDocDto.getFileRepoId();
                        String svcDocId = appSvcDocDto.getSvcDocId();
                        if(StringUtil.isEmpty(fileRepoId)||StringUtil.isEmpty(svcDocId)){
                            appSvcDocDtoListIterator.remove();
                        }
                    }
                }
            }
        }
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            ListIterator<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoListIterator = appGrpPrimaryDocDtos.listIterator();
            while (appGrpPrimaryDocDtoListIterator.hasNext()){
                AppGrpPrimaryDocDto next = appGrpPrimaryDocDtoListIterator.next();
                String fileRepoId = next.getFileRepoId();
                String svcDocId = next.getSvcDocId();
                if(StringUtil.isEmpty(fileRepoId)||StringUtil.isEmpty(svcDocId)){
                    appGrpPrimaryDocDtoListIterator.remove();
                }
            }
        }
    }
}
