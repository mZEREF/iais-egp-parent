package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.arca.uen.IaisUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.GenerateLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGrpDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SuperLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.PostInsGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.OrganizationService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AcraUenBeClient;
import com.ecquaria.cloud.moh.iais.service.client.AppCommClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremSubSvcBeClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaServiceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * LicenceServiceImpl
 *
 * @author suocheng
 * @date 11/29/2019
 */
@Service
@Slf4j
public class LicenceServiceImpl implements LicenceService {
    private static final String[] ALPHABET_ARRAY_PROTOTYPE=new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private SystemBeLicClient systemClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    protected OrganizationService organizationService;


    @Autowired
    private EventBusHelper eventBusHelper;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private AcraUenBeClient acraUenBeClient;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private LicEicClient licEicClient;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Value("${iais.system.two.address}")
    private String systemAddressTwo;

    @Value("${iais.system.phone.number}")
    private String systemPhoneNumber;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private AppCommClient appCommClient;

    @Autowired
    private TaskService taskService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private HcsaServiceClient hcsaServiceClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    private AppPremSubSvcBeClient appPremSubSvcBeClient;

    @Override
    public List<ApplicationLicenceDto> getCanGenerateApplications(GenerateLicenceDto generateLicenceDto) {
        return   applicationClient.getGroup(generateLicenceDto).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getHcsaServiceById(List<String> serviceIds) {

        return  hcsaConfigClient.getHcsaService(serviceIds).getEntity();
    }

    @Override
    public String getHciCode(String serviceCode) {
        log.info(StringUtil.changeForLog("generate the hcicode"));
        return     systemClient.hclCodeByCode(serviceCode).getEntity();
    }



    @Override
    public String getGroupLicenceNo(String serviceCode, AppPremisesRecommendationDto appPremisesRecommendationDto,String orgLicecnceId,Integer premisesNumber) {
        log.info(StringUtil.changeForLog("The getGroupLicenceNo start ..."));
        log.info(StringUtil.changeForLog("The getGroupLicenceNo serviceCode is -->:"+serviceCode));
        log.info(StringUtil.changeForLog("The getGroupLicenceNo orgLicecnceId is -->:"+orgLicecnceId));
        log.info(StringUtil.changeForLog("The getGroupLicenceNo premisesNumber is -->:"+premisesNumber));
        LicenceGrpDto licenceGrpDto = new LicenceGrpDto();
        licenceGrpDto.setSerivceCode(serviceCode);
        licenceGrpDto.setOrgLicecnceId(orgLicecnceId);
        licenceGrpDto.setPremisesNumber(premisesNumber);
        String no = hcsaLicenceClient.groupLicenceNumber(licenceGrpDto).getEntity();
        log.info(StringUtil.changeForLog("The getGroupLicenceNo no -->:"+no));
        int yearLength = 0;
        if(appPremisesRecommendationDto != null && RiskConsts.YEAR.equals(appPremisesRecommendationDto.getChronoUnit())){
            yearLength = appPremisesRecommendationDto.getRecomInNumber();
        }
        log.info(StringUtil.changeForLog("The getGroupLicenceNo yearLength -->:"+yearLength));
        log.info(StringUtil.changeForLog("The getGroupLicenceNo end ..."));
        return   systemClient.groupLicence(serviceCode,String.valueOf(yearLength),no,null).getEntity();
    }

    @Override
    public AppPremisesRecommendationDto getTcu(String appPremCorrecId) {
        return fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrecId,
                InspectionConstants.RECOM_TYPE_TCU).getEntity();
    }
    @Override
    public PremisesDto getLatestVersionPremisesByHciCode(String hciCode) {
        return hcsaLicenceClient.getLatestVersionPremisesByHciCode(hciCode).getEntity();
    }

    @Override
    public KeyPersonnelDto getLatestVersionKeyPersonnelByIdNoAndOrgId(String idNo, String orgId,String nationality) {
        return hcsaLicenceClient.getLatestVersionKeyPersonnelByidNoAndOrgId(idNo,orgId,nationality).getEntity();
    }

    @Override
    public LicenceDto getLicenceDto(String licenceId) {
        LicenceDto result = null;
        if(!StringUtil.isEmpty(licenceId)){
            result = hcsaLicenceClient.getLicDtoById(licenceId).getEntity();
        }
        return result;
    }


    @Override
    public LicenceDto getLicenceDtoByLicNo(String licNo) {
        return hcsaLicenceClient.getLicBylicNo(licNo).getEntity();
    }

    @Override
    public List<String> getLicenceOutDate(int outMonth){
        List<LicenseeDto> lics = organizationClient.getLicenseeDtoFromSingpass().getEntity();
        log.info(StringUtil.changeForLog("The licensee with Singpass ==> " + lics.size()));
        return hcsaLicenceClient.getLicenceOutDate(lics, outMonth).getEntity();
    }

    @Override
    public List<LicenceGroupDto> createSuperLicDto(EventBusLicenceGroupDtos eventBusLicenceGroupDtos) {
        eventBusHelper.submitAsyncRequest(eventBusLicenceGroupDtos,
                eventBusLicenceGroupDtos.getEventBusSubmissionId(),
                EventBusConsts.SERVICE_NAME_LICENCESAVE,
                EventBusConsts.OPERATION_LICENCE_SAVE,
                eventBusLicenceGroupDtos.getEventRefNo(),
                null);
        return null;
    }

    @Override
    public EventBusLicenceGroupDtos createFESuperLicDto(String eventRefNum,String submissionId) {
        EventBusLicenceGroupDtos eventBusLicenceGroupDtos =  getEventBusLicenceGroupDtosByRefNo(eventRefNum);
        if(eventBusLicenceGroupDtos!=null){
            boolean isAsoEmailFlow=false;
            List<AppPremiseMiscDto> appPremiseMiscDtoList=IaisCommonUtils.genNewArrayList();
            try {
                for (LicenceGroupDto item:eventBusLicenceGroupDtos.getLicenceGroupDtos()) {
                    for (SuperLicDto superLicDto : item.getSuperLicDtos()) {

                        if(superLicDto != null) {
                            LicenceDto licenceDto = superLicDto.getLicenceDto();
                            if(licenceDto != null){
                                List<String> appIdList = hcsaLicenceClient.getAppIdsByLicId(superLicDto.getLicenceDto().getId()).getEntity();
                                log.debug(StringUtil.changeForLog("send approve email --- get app list by licence id : " + superLicDto.getLicenceDto().getId()));
                                if(appIdList != null && appIdList.size() >0) {
                                    for(String applicationId : appIdList){
                                        ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
                                        List<AppPremisesRoutingHistoryDto> rollBackHistroyList = applicationClient.getHistoryByAppNoAndDecision(applicationDto.getApplicationNo(), ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL).getEntity();

                                        if(IaisCommonUtils.isNotEmpty(rollBackHistroyList)&& (applicationDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)
                                                || applicationDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_RENEWAL)
                                                || applicationDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE))) {
                                            //getAppPremisesCorrelationsByAppId
                                            isAsoEmailFlow=true;
                                            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_ASO_EMAIL_PENDING);
                                            applicationClient.updateApplication(applicationDto);
                                            AppPremisesCorrelationDto appPremisesCorrelationDto = appPremisesCorrClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity().get(0);
                                            if (appPremisesCorrelationDto != null) {
                                                AppPremiseMiscDto appPremiseMiscDto=new AppPremiseMiscDto();
                                                appPremiseMiscDto.setAppPremCorreId(appPremisesCorrelationDto.getId());
                                                appPremiseMiscDto.setOtherReason(eventRefNum);
                                                appPremiseMiscDto.setAppealType(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL);
                                                appPremiseMiscDtoList.add(appPremiseMiscDto);
                                                String refNo=appPremisesCorrelationDto.getId();
                                                String applicationNo=applicationDto.getApplicationNo();

                                                List<TaskDto> oldTaskDtos= taskService.getTaskRfi(applicationNo);
                                                TaskDto taskDto=null;
                                                boolean hasAso=false;
                                                List<TaskDto> taskDtoList=IaisCommonUtils.genNewArrayList();
                                                if(IaisCommonUtils.isNotEmpty(oldTaskDtos)){
                                                    for (TaskDto task:oldTaskDtos
                                                    ) {
                                                        if(task.getSlaDateCompleted()!=null){
                                                            taskDtoList.add(task);
                                                        }
                                                    }
                                                }
                                                taskDtoList.sort(Comparator.comparing(TaskDto::getSlaDateCompleted).reversed());
                                                if(taskDtoList.size()!=0){
                                                    for (TaskDto task:taskDtoList
                                                    ) {
                                                        if(task.getRoleId().equals(RoleConsts.USER_ROLE_ASO)){
                                                            OrgUserDto aso=organizationClient.retrieveOrgUserAccountById(task.getUserId()).getEntity();
                                                            taskDto=task;
                                                            if(aso!=null){
                                                                if(aso.getUserRoles().contains(RoleConsts.USER_ROLE_ASO)&&aso.getStatus().equals(AppConsts.COMMON_STATUS_ACTIVE)&&aso.getAvailable()){
                                                                    hasAso=true;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if(taskDto!=null){
                                                    if(!hasAso){
                                                        String userId;
                                                        TaskDto taskScoreDto=taskService.getUserIdForWorkGroup(taskDto.getWkGrpId());
                                                        if(taskScoreDto != null){
                                                            userId = taskScoreDto.getUserId();
                                                            taskDto.setUserId(userId);
                                                        }else{
                                                            List<OrgUserDto> orgUserDtos = organizationClient.retrieveOrgUserAccountByRoleId(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN).getEntity();
                                                            if(!IaisCommonUtils.isEmpty(orgUserDtos)){
                                                                OrgUserDto userDto=null;
                                                                for (OrgUserDto orgUserDto:orgUserDtos
                                                                ) {
                                                                    if(orgUserDto.getAvailable()&&orgUserDto.getStatus().equals(AppConsts.COMMON_STATUS_ACTIVE)){
                                                                        userDto=orgUserDto;
                                                                        break;
                                                                    }
                                                                }
                                                                if(userDto!=null){
                                                                    taskDto.setUserId(userDto.getId());
                                                                    taskDto.setWkGrpId(null);
                                                                }else {
                                                                    taskDto.setUserId(null);
                                                                }
                                                            }else {
                                                                taskDto.setUserId(null);
                                                            }
                                                        }
                                                    }
                                                    taskDto.setDateAssigned(new Date());
                                                    taskDto.setId(null);
                                                    taskDto.setRefNo(refNo);
                                                    taskDto.setSlaDateCompleted(null);
                                                    taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
                                                    AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
                                                    taskDto.setAuditTrailDto(auditTrailDto);
                                                    List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();

                                                    taskDtos.add(taskDto);
                                                    taskService.createTasks(taskDtos);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            if(!isAsoEmailFlow){
                createFESuperLicDto(eventBusLicenceGroupDtos,eventRefNum);

                for (LicenceGroupDto item:eventBusLicenceGroupDtos.getLicenceGroupDtos()) {
                    for (SuperLicDto superLicDto : item.getSuperLicDtos()) {
                        sendNotification(superLicDto);
                    }
                }
            }else if(IaisCommonUtils.isNotEmpty(appPremiseMiscDtoList)){
                appCommClient.saveAppPremiseMiscDto(appPremiseMiscDtoList);
            }

        }else{
            log.debug(StringUtil.changeForLog("This eventReo can not get the LicEicRequestTrackingDto -->:"+eventRefNum));
        }

        return eventBusLicenceGroupDtos;
    }

    @Override
    public void createFESuperLicDto(EventBusLicenceGroupDtos eventBusLicenceGroupDtos,String eventRefNum){
        Date now = new Date();
        EicRequestTrackingDto trackDto = licEicClient.getPendingRecordByReferenceNumber(eventRefNum).getEntity();
        trackDto.setProcessNum(trackDto.getProcessNum() + 1);
        trackDto.setFirstActionAt(now);
        trackDto.setLastActionAt(now);
        try {
            eicCallFeSuperLic(eventBusLicenceGroupDtos);
            trackDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        hcsaLicenceClient.updateEicTrackStatus(trackDto);
        //send approve notification
        trackDto = licEicClient.getPendingRecordByReferenceNumber(eventRefNum + "uen").getEntity();
        trackDto.setProcessNum(trackDto.getProcessNum() + 1);
        trackDto.setFirstActionAt(now);
        trackDto.setLastActionAt(now);
        try{
            //save new acra info
//                saveNewAcra(eventBusLicenceGroupDtos);
            //send issue uen email
            log.info(StringUtil.changeForLog("send uen email"));
            generateUEN(eventBusLicenceGroupDtos);
            trackDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        hcsaLicenceClient.updateEicTrackStatus(trackDto);

        saveLicenceAppRiskInfoDtos(eventBusLicenceGroupDtos.getLicenceGroupDtos(),eventBusLicenceGroupDtos.getAuditTrailDto());
    }
    private void saveLicenceAppRiskInfoDtos( List<LicenceGroupDto> licenceGroupDtos,AuditTrailDto auditTrailDto){
        log.info("----- create save-lic-app-risk-by-licdtos ");
        if( !IaisCommonUtils.isEmpty(licenceGroupDtos)){
            for(LicenceGroupDto licenceGroupDto : licenceGroupDtos){
                try{
                    List<SuperLicDto>  superLicDtos = licenceGroupDto.getSuperLicDtos();
                    List<LicenceDto> licenceDtos = IaisCommonUtils.genNewArrayList();
                    for(SuperLicDto superLicDto : superLicDtos){
                        superLicDto.getLicenceDto().setAuditTrailDto(auditTrailDto);
                        licenceDtos.add(superLicDto.getLicenceDto());
                    }
                    hcsaLicenceClient.saveLicenceAppRiskInfoDtosByLicIds(licenceDtos);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        log.info("----- create save-lic-app-risk-by-licdtos end ");
    }

    public void generateUEN(EventBusLicenceGroupDtos eventBusLicenceGroupDtos) {
        log.info(StringUtil.changeForLog("The generateUen start ..."));
        IaisUENDto iaisUENDto = new IaisUENDto();
        List<LicenceGroupDto> licenceGroupDtos = eventBusLicenceGroupDtos.getLicenceGroupDtos();
        if(!IaisCommonUtils.isEmpty(licenceGroupDtos)){
            LicenceGroupDto  licenceGroupDto = licenceGroupDtos.get(0);
            List<SuperLicDto> superLicDtos = licenceGroupDto.getSuperLicDtos();
            if(!IaisCommonUtils.isEmpty(superLicDtos)){
                SuperLicDto superLicDto = superLicDtos.get(0);
                LicenceDto licenceDto = superLicDto.getLicenceDto();
                String svcCode = licenceDto.getSvcCode();
                String licenseeId = licenceDto.getLicenseeId();
                log.info(StringUtil.changeForLog("The generateUen svcCode is -->: "+svcCode));
                log.info(StringUtil.changeForLog("The generateUen licenseeId is -->: "+licenseeId));
                iaisUENDto.setLicenseeId(licenseeId);
                iaisUENDto.setSvcCode(svcCode);
                List<PremisesGroupDto> premisesGroupDtos = superLicDto.getPremisesGroupDtos();
                PremisesGroupDto premisesGroupDto = premisesGroupDtos.get(0);
                PremisesDto premisesDto = premisesGroupDto.getPremisesDto();
                log.info(StringUtil.changeForLog("The generateUen premisesDto.getHciCode() is -->: "+premisesDto.getHciCode()));
                iaisUENDto.setPremises(premisesDto);
            }else{
                log.info(StringUtil.changeForLog("The generateUen superLicDtos is null "));
            }
        }else{
            log.info(StringUtil.changeForLog("The generateUen licenceGroupDtos is null "));
        }
        String result = acraUenBeClient.generateUen(iaisUENDto).getEntity();
        if ("Fail".equals(result)) {
            throw new IaisRuntimeException("Failed to generate UEN");
        } else if ("Success".equals(result)) {
            sendUenEmail(eventBusLicenceGroupDtos);
        }
        log.info(StringUtil.changeForLog("The generateUen end ..."));
    }

    private void saveNewAcra(EventBusLicenceGroupDtos eventBusLicenceGroupDtos) {
        log.info(StringUtil.changeForLog("save New Acra"));
        for (LicenceGroupDto item : eventBusLicenceGroupDtos.getLicenceGroupDtos()) {
            for (SuperLicDto superLicDto : item.getSuperLicDtos()) {
                LicenceDto licenceDto = superLicDto.getLicenceDto();
                IaisUENDto iaisUENDto = new IaisUENDto();
                iaisUENDto.setLicenseeId(licenceDto.getLicenseeId());
                iaisUENDto.setSvcCode(licenceDto.getSvcCode());
                List<PremisesGroupDto> premisesGroupDtos = superLicDto.getPremisesGroupDtos();
                PremisesGroupDto premisesGroupDto = premisesGroupDtos.get(0);
                iaisUENDto.setPremises(premisesGroupDto.getPremisesDto());
                log.info(StringUtil.changeForLog("Iais UEN Dto is -->: " + JsonUtil.parseToJson(iaisUENDto)));
                acraUenBeClient.generateUen(iaisUENDto);
            }
        }
    }

    @Override
    public void sendUenEmail(EventBusLicenceGroupDtos eventBusLicenceGroupDtos){
        log.info("send uen email ............ {}", JsonUtil.parseToJson(eventBusLicenceGroupDtos));
        log.info(StringUtil.changeForLog("send uen email start"));
        for (LicenceGroupDto item:eventBusLicenceGroupDtos.getLicenceGroupDtos()) {
            for (SuperLicDto superLicDto:item.getSuperLicDtos()) {
                try {
                    LicenceDto licenceDto = superLicDto.getLicenceDto();
                    if (Optional.ofNullable(licenceDto).isPresent()){
                        log.info(StringUtil.changeForLog("licence id = " + licenceDto.getId()));
                        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenceDto.getLicenseeId()).getEntity();
                        log.info("licensee dto json =>>>> {} " , JsonUtil.parseToJson(licenseeDto));
                        if (Optional.ofNullable(licenseeDto).isPresent()){
                            Date singpassExpiredDate = licenseeDto.getSingpassExpiredDate();
                            OrganizationDto organizationDto = organizationClient.getOrganizationById(licenseeDto.getOrganizationId()).getEntity();
                            if (Optional.ofNullable(organizationDto).isPresent()){
                                String uenNo = organizationDto.getUenNo();
                                if(StringUtil.isNotEmpty(uenNo)){
                                    LicenseeIndividualDto individual = licenseeDto.getLicenseeIndividualDto();

                                    List<PremisesGroupDto> premisesGroupDtos = superLicDto.getPremisesGroupDtos();

                                    premisesGroupDtos = Optional.ofNullable(premisesGroupDtos).orElseGet(ArrayList::new);

                                    log.info("Premises Group Dto {}", JsonUtil.parseToJson(premisesGroupDtos));

                                    Optional<PremisesGroupDto> premisesGroupOptional = premisesGroupDtos.stream().findFirst();

                                    log.info("Uen Mail Flag {}", individual != null ? individual.getUenMailFlag() : "");

                                    if (individual != null && premisesGroupOptional.isPresent() && individual.getUenMailFlag() == 0){
                                        PremisesDto premisesDto = premisesGroupDtos.get(0).getPremisesDto();
                                        log.info("Premises {}", JsonUtil.parseToJson(premisesDto));
                                        //judge licence is singlepass
                                        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
                                        templateContent.put("HCI_Name", premisesDto.getHciName());
                                        String address = IaisCommonUtils.getAddress(premisesDto);
                                        templateContent.put("HCI_Address", address);
                                        log.info(StringUtil.changeForLog("HCI_Address = " + address));
                                        templateContent.put("UEN_No", uenNo);
                                        List<OrgUserDto> orgUserDtoList = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(organizationDto.getId()).getEntity();
                                        String applicantName = orgUserDtoList.get(0).getDisplayName();

                                        templateContent.put("Applicant", applicantName);
                                        templateContent.put("ServiceName", licenceDto.getSvcName());
                                        templateContent.put("LicenceNo", licenceDto.getLicenceNo());
                                        Calendar c = Calendar.getInstance();
                                        c.add(Calendar.DAY_OF_MONTH, systemParamConfig.getIssueUenGraceDay());

                                        if (Optional.ofNullable(singpassExpiredDate).isPresent()){
                                            templateContent.put("GraceDate", Formatter.formatDate(singpassExpiredDate));
                                        }

                                        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                                        templateContent.put("newSystem", loginUrl);
                                        templateContent.put("emailAddress", systemAddressOne);
                                        templateContent.put("telNo", systemPhoneNumber);

                                        MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_EMAIL).getEntity();
                                        MsgTemplateDto smsTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_SMS).getEntity();
                                        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_MSG).getEntity();
                                        String emailSubject = emailTemplateDto.getTemplateName();
                                        String smsSubject = smsTemplateDto.getTemplateName();
                                        String msgSubject = msgTemplateDto.getTemplateName();


                                        EmailParam emailParam = new EmailParam();
                                        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_EMAIL);
                                        emailParam.setTemplateContent(templateContent);
                                        emailParam.setSubject(emailSubject);
                                        emailParam.setQueryCode(licenceDto.getLicenceNo());
                                        emailParam.setReqRefNum(licenceDto.getLicenceNo());
                                        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
                                        emailParam.setRefId(licenseeDto.getId());
                                        notificationHelper.sendNotification(emailParam);

                                        EmailParam smsParam = new EmailParam();
                                        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_SMS);
                                        smsParam.setSubject(smsSubject);
                                        smsParam.setTemplateContent(templateContent);
                                        smsParam.setQueryCode(licenceDto.getLicenceNo());
                                        smsParam.setReqRefNum(licenceDto.getLicenceNo());
                                        smsParam.setRefId(licenseeDto.getId());
                                        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENSEE_ID);
                                        notificationHelper.sendNotification(smsParam);

                                        EmailParam msgParam = new EmailParam();
                                        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_MSG);
                                        msgParam.setTemplateContent(templateContent);
                                        msgParam.setSubject(msgSubject);
                                        msgParam.setQueryCode(licenceDto.getLicenceNo());
                                        msgParam.setReqRefNum(licenceDto.getLicenceNo());

                                        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
                                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(licenceDto.getSvcName());
                                        svcCodeList.add(hcsaServiceDto.getSvcCode());
                                        msgParam.setSvcCodeList(svcCodeList);
                                        msgParam.setRefId(licenseeDto.getId());
                                        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                                        notificationHelper.sendNotification(msgParam);
                                        //set flag = 1
                                        organizationClient.updateIndividualFlag(individual.getId());
                                    }
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }

            }
        }

    }

    @Override
    public void sendNotification(SuperLicDto superLicDto){
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        String corpPassUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + "/main-web/eservice/INTERNET/FE_Landing";
        if(superLicDto != null) {
            LicenceDto licenceDto = superLicDto.getLicenceDto();
            if(licenceDto != null){
                String licenceNo = licenceDto.getLicenceNo();
                String licenseeId = licenceDto.getLicenseeId();
                List<String> appIdList = hcsaLicenceClient.getAppIdsByLicId(superLicDto.getLicenceDto().getId()).getEntity();
                log.debug(StringUtil.changeForLog("send approve email --- get app list by licence id : " + superLicDto.getLicenceDto().getId()));
                if(appIdList != null && appIdList.size() >0) {
                    for(String applicationId : appIdList){
                        ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
                        if(applicationDto != null){
                            //getAppPremisesCorrelationsByAppId
                            AppPremisesRecommendationDto inspectionRecommendation = new AppPremisesRecommendationDto();
                            AppPremisesCorrelationDto appPremisesCorrelationDto = appPremisesCorrClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity().get(0);
                            if(appPremisesCorrelationDto != null){
                                inspectionRecommendation = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();
                            }
                            List<AppPremisesRoutingHistoryDto> rollBackHistroyList = applicationClient.getHistoryByAppNoAndDecision(applicationDto.getApplicationNo(), ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL).getEntity();

                            if (IaisCommonUtils.isEmpty(rollBackHistroyList)) {
                                HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
                                List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
                                svcCodeList.add(svcDto.getSvcCode());
                                String applicationNo = applicationDto.getApplicationNo();
                                log.debug(StringUtil.changeForLog("send approve email --- get app by applicationNo : " + applicationNo));
                                String applicationType = applicationDto.getApplicationType();
                                LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
                                String appGrpId = applicationDto.getAppGrpId();
                                ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appGrpId).getEntity();
                                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                                if(licenseeDto != null && orgUserDto != null){
                                    String applicantName = orgUserDto.getDisplayName();
                                    String organizationId = licenseeDto.getOrganizationId();
                                    OrganizationDto organizationDto = organizationClient.getOrganizationById(organizationId).getEntity();
                                    String appDate = Formatter.formatDateTime(applicationGroupDto.getSubmitDt(), "dd/MM/yyyy");
                                    String MohName = AppConsts.MOH_AGENCY_NAME;
                                    log.info(StringUtil.changeForLog("send notification applicantName : " + applicantName));
                                    String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationType);
                                    log.info(StringUtil.changeForLog("send notification applicationType : " + applicationTypeShow));
                                    if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
                                        if (appPremisesCorrelationDto != null) {
                                            sendNewAppApproveNotification(applicantName,applicationTypeShow,applicationNo,appDate,licenceNo,svcCodeList,loginUrl,corpPassUrl,MohName,organizationDto,inspectionRecommendation,appPremisesCorrelationDto);
                                        }
                                    }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
                                        sendRenewalAppApproveNotification(applicantName,applicationTypeShow,applicationNo,appDate,licenceNo,svcCodeList,loginUrl,MohName,inspectionRecommendation);
                                        sendPostInspectionNotification(applicationGroupDto,applicantName,svcDto,svcCodeList,MohName,applicationNo);
                                    }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
                                        try {
                                            if(applicationGroupDto.getNewLicenseeId()==null){
                                                sendRfcApproveNotification(applicantName,applicationTypeShow,applicationNo,appDate,licenceNo,svcCodeList);
                                            }
                                            //transfee
                                            if(applicationGroupDto.getNewLicenseeId()!=null){
                                                sendRfcApproveLicenseeEmail(applicationGroupDto,applicationDto,licenceNo,svcCodeList);
                                            }
                                        } catch (IOException e) {
                                            log.info(e.getMessage(),e);
                                        }
                                    }
                                }else{
                                    if(licenseeDto == null){
                                        log.debug(StringUtil.changeForLog("---licenseeDto == null"));
                                    }
                                    if(orgUserDto == null){
                                        log.debug(StringUtil.changeForLog("---orgUserDto == null"));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendPostInspectionNotification(ApplicationGroupDto applicationGroupDto,String applicantName,HcsaServiceDto svcDto,List<String> svcCodeList,String MohName,String applicationNo){
        if(applicationGroupDto != null && 0 == applicationGroupDto.getIsPreInspection()){
            if(svcDto != null){
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("ApplicantName", applicantName);
                map.put("MOH_AGENCY_NAME", MohName);
                try {
//                    String subject = "MOH HALP - Post Inspection for " + svcDto.getSvcName();
                    Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                    subMap.put("ServiceName", svcDto.getSvcName());
                    String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_POST_INSPECTION,subMap);
                    String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_POST_INSPECTION_SMS,subMap);
                    String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_POST_INSPECTION_MESSAGE,subMap);
                    log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                    log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                    log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_POST_INSPECTION);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(applicationNo);
                    emailParam.setReqRefNum(applicationNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(applicationNo);
                    emailParam.setSubject(emailSubject);
                    //send email
                    log.debug(StringUtil.changeForLog("send sendPostInspectionNotification application email"));
                    notificationHelper.sendNotification(emailParam);
                    log.debug(StringUtil.changeForLog("send sendPostInspectionNotification application email end"));
                    //send sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_POST_INSPECTION_SMS);
                    smsParam.setSubject(smsSubject);
                    smsParam.setQueryCode(applicationNo);
                    smsParam.setReqRefNum(applicationNo);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    smsParam.setRefId(applicationNo);
                    log.debug(StringUtil.changeForLog("send sendPostInspectionNotification application sms"));
                    notificationHelper.sendNotification(smsParam);
                    log.debug(StringUtil.changeForLog("send sendPostInspectionNotification application sms end"));
                    //send message
                    EmailParam messageParam = new EmailParam();
                    messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_POST_INSPECTION_MESSAGE);
                    messageParam.setTemplateContent(map);
                    messageParam.setQueryCode(applicationNo);
                    messageParam.setReqRefNum(applicationNo);
                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    messageParam.setRefId(applicationNo);
                    messageParam.setSubject(messageSubject);
                    messageParam.setSvcCodeList(svcCodeList);
                    log.debug(StringUtil.changeForLog("send sendPostInspectionNotification application message"));
                    notificationHelper.sendNotification(messageParam);
                    log.debug(StringUtil.changeForLog("send sendPostInspectionNotification application message end"));
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }else{
                log.debug(StringUtil.changeForLog("sendPostInspectionNotification svcDto == null"));
            }
        }else{
            if(applicationGroupDto == null){
                log.debug(StringUtil.changeForLog("sendPostInspectionNotification is applicationGroupDto"));
            }else{
                log.debug(StringUtil.changeForLog("sendPostInspectionNotification applicationGroupDto.getIsPreInspection() : " + applicationGroupDto.getIsPreInspection()));
            }

        }
    }

    @Override
    public void sendRfcApproveLicenseeEmail(ApplicationGroupDto applicationGroupDto,  ApplicationDto applicationDto,String licenceNo,
                                             List<String> svcCodeList)  {
        String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(applicationGroupDto.getLicenseeId()).getEntity();
        LicenseeDto newLicenseeDto = organizationClient.getLicenseeDtoById(applicationGroupDto.getNewLicenseeId()).getEntity();
        String applicantName = licenseeDto.getName();
        emailMap.put("name_transferee", newLicenseeDto.getName());
        emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
        emailMap.put("ApplicationNumber", applicationDto.getApplicationNo());
        emailMap.put("ApplicationDate", Formatter.formatDate(applicationGroupDto.getSubmitDt()));
        emailMap.put("ExistingLicensee", applicantName);
        emailMap.put("transferee_licensee", newLicenseeDto.getName());
        emailMap.put("LicenceNumber", licenceNo);
        emailMap.put("specialText", "");
        //emailMap.put("Hypelink", loginUrl);
        emailMap.put("isSpecial", "N");
        emailMap.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
        emailMap.put("HCSA_Regulations", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
        emailMap.put("Number", systemPhoneNumber);
        emailMap.put("email_1", systemAddressOne);
        emailMap.put("Email_2", systemAddressTwo);
        emailMap.put("systemLink", loginUrl);
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setNeedSendNewLicensee(true);
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED);
        emailParam.setTemplateContent(emailMap);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        MsgTemplateDto rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED).getEntity();
        map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (IOException | TemplateException e) {
            log.info(e.getMessage(),e);
        }
        emailParam.setSubject(subject);
        //email
        log.info(StringUtil.changeForLog("send RfcApproveLicensee application email"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send RfcApproveLicensee application email end"));
        //msg
        try {
            rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED_MSG).getEntity();
            subject = null;
            try {
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            } catch (TemplateException e) {
                log.info(e.getMessage(),e);
            }
            EmailParam msgParam = new EmailParam();
            msgParam.setNeedSendNewLicensee(true);
            msgParam.setQueryCode(applicationDto.getApplicationNo());
            msgParam.setReqRefNum(applicationDto.getApplicationNo());
            msgParam.setRefId(applicationDto.getApplicationNo());
            msgParam.setTemplateContent(emailMap);
            msgParam.setSubject(subject);
            msgParam.setSvcCodeList(svcCodeList);
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED_MSG);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            msgParam.setRefId(applicationDto.getApplicationNo());
            log.info(StringUtil.changeForLog("send RfcApproveLicensee application msg"));
            notificationHelper.sendNotification(msgParam);
            log.info(StringUtil.changeForLog("send RfcApproveLicensee application msg end"));
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        //sms
        rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED_SMS).getEntity();
        subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (IOException |TemplateException e) {
            log.info(e.getMessage(),e);
        }
        EmailParam smsParam = new EmailParam();
        smsParam.setNeedSendNewLicensee(true);
        smsParam.setQueryCode(applicationDto.getApplicationNo());
        smsParam.setReqRefNum(applicationDto.getApplicationNo());
        smsParam.setRefId(applicationDto.getApplicationNo());
        smsParam.setTemplateContent(emailMap);
        smsParam.setSubject(subject);
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED_SMS);
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        log.info(StringUtil.changeForLog("send RfcApproveLicensee application sms"));
        notificationHelper.sendNotification(smsParam);
        log.info(StringUtil.changeForLog("send RfcApproveLicensee application sms end"));
    }

    public void sendRfcApproveNotification(String applicantName,
                                           String applicationTypeShow,
                                           String applicationNo,
                                           String appDate,
                                           String licenceNo,
                                           List<String> svcCodeList) throws IOException {
        String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        emailMap.put("change", "true");
        emailMap.put("ApplicantName", applicantName);
        emailMap.put("ApplicationType", applicationTypeShow);
        emailMap.put("ApplicationNumber", applicationNo);
        emailMap.put("ApplicationDate", appDate);
        emailMap.put("systemLink", loginUrl);
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT);
        emailParam.setTemplateContent(emailMap);
        emailParam.setQueryCode(applicationNo);
        emailParam.setReqRefNum(applicationNo);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationNo);
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        MsgTemplateDto rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT).getEntity();
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        String subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (TemplateException e) {
            log.info(e.getMessage(),e);
        }
        emailParam.setSubject(subject);
        //email
        log.info(StringUtil.changeForLog("send RfcApprove application email"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send RfcApprove application email end"));
        //msg
        try {
            EmailParam msgParam = new EmailParam();
            msgParam.setQueryCode(applicationNo);
            msgParam.setReqRefNum(applicationNo);
            msgParam.setTemplateContent(emailMap);
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT_MSG);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            msgParam.setSvcCodeList(svcCodeList);
            msgParam.setRefId(applicationNo);
            rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT_MSG).getEntity();
            subject = null;
            try {
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            } catch (TemplateException e) {
                log.info(e.getMessage(),e);
            }
            msgParam.setSubject(subject);
            log.info(StringUtil.changeForLog("send RfcApprove application msg"));
            notificationHelper.sendNotification(msgParam);
            log.info(StringUtil.changeForLog("send RfcApprove application msg end"));
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }

        //sms
        rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT_SMS).getEntity();
        subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (TemplateException e) {
            log.info(e.getMessage(),e);
        }
        EmailParam smsParam = new EmailParam();
        smsParam.setQueryCode(applicationNo);
        smsParam.setReqRefNum(applicationNo);
        smsParam.setRefId(applicationNo);
        smsParam.setTemplateContent(emailMap);
        smsParam.setSubject(subject);
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT_SMS);
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        log.info(StringUtil.changeForLog("send RfcApprove application sms"));
        notificationHelper.sendNotification(smsParam);
        log.info(StringUtil.changeForLog("send RfcApprove application sms end"));
    }

    private void sendRenewalAppApproveNotification(String applicantName,
                                               String applicationTypeShow,
                                               String applicationNo,
                                               String appDate,
                                               String licenceNo,
                                               List<String> svcCodeList,
                                               String loginUrl,
                                               String MohName,
                                               AppPremisesRecommendationDto inspectionRecommendation){
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicantName", applicantName);
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        map.put("applicationDate", appDate);
        map.put("licenceNumber", licenceNo);
        map.put("isSpecial", "N");
        if(inspectionRecommendation != null){
            map.put("inInspection", "Y");
            map.put("inspectionText", inspectionRecommendation.getRemarks());
        }else {
            map.put("inInspection", "N");
        }
        map.put("systemLink", loginUrl);

        map.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
        map.put("regulationLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
        map.put("link", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_LINK));
        map.put("scdfLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_SCDF_LINK));
        map.put("momLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_MOM_LINK));
        map.put("irasLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_IRAS_LINK));

        map.put("phoneNumber", systemPhoneNumber);
        map.put("emailAddress1", systemAddressOne);
        map.put("emailAddress2", systemAddressTwo);
        map.put("MOH_AGENCY_NAME", MohName);
        try {
//            String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is approved ";
            Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
            subMap.put("ApplicationType", applicationTypeShow);
            subMap.put("ApplicationNumber", applicationNo);
            String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE,subMap);
            String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE_SMS,subMap);
            String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE_MESSAGE,subMap);
            log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
            log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
            log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE);
            emailParam.setTemplateContent(map);
            emailParam.setQueryCode(applicationNo);
            emailParam.setReqRefNum(applicationNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(applicationNo);
            emailParam.setSubject(emailSubject);
            //send email
            log.info(StringUtil.changeForLog("send renewal application email"));
            notificationHelper.sendNotification(emailParam);
            log.info(StringUtil.changeForLog("send renewal application email end"));
            //send sms
            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE_SMS);
            smsParam.setSubject(smsSubject);
            smsParam.setQueryCode(applicationNo);
            smsParam.setReqRefNum(applicationNo);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            smsParam.setRefId(applicationNo);
            log.info(StringUtil.changeForLog("send renewal application sms"));
            notificationHelper.sendNotification(smsParam);
            log.info(StringUtil.changeForLog("send renewal application sms end"));
            //send message
            EmailParam messageParam = new EmailParam();
            messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE_MESSAGE);
            messageParam.setTemplateContent(map);
            messageParam.setQueryCode(applicationNo);
            messageParam.setReqRefNum(applicationNo);
            messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            messageParam.setRefId(applicationNo);
            messageParam.setSubject(messageSubject);
            messageParam.setSvcCodeList(svcCodeList);
            log.info(StringUtil.changeForLog("send renewal application message"));
            notificationHelper.sendNotification(messageParam);
            log.info(StringUtil.changeForLog("send renewal application message end"));
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private String getEmailSubject(String templateId,Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto =msgTemplateClient.getMsgTemplate(templateId).getEntity();
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
                    }else{
                        subject = emailTemplateDto.getTemplateName();
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return subject;
    }

    private void sendNewAppApproveNotification(String applicantName,
                                               String applicationTypeShow,
                                               String applicationNo,
                                               String appDate,
                                               String licenceNo,
                                               List<String> svcCodeList,
                                               String loginUrl,
                                               String corpPassUrl,
                                               String MohName,
                                               OrganizationDto organizationDto,
                                               AppPremisesRecommendationDto inspectionRecommendation,AppPremisesCorrelationDto appPremisesCorrelationDto){
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        ApplicationDto applicationDto = applicationClient.getApplicationById(appPremisesCorrelationDto.getApplicationId()).getEntity();
        HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        AppGrpPremisesDto appGrpPremisesDto=appCommClient.getAppGrpPremisesById(appPremisesCorrelationDto.getAppGrpPremId()).getEntity();
        map.put("ApplicantName", applicantName);
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        map.put("applicationDate", appDate);
        map.put("licenceNumber", licenceNo);
        map.put("svcNameMOSD", baseServiceDto.getSvcName()+" ("+appGrpPremisesDto.getAddress()+")");
        map.put("BusinessName", appGrpPremisesDto.getHciName());
        SubLicenseeDto subLicensee = appCommClient.getSubLicenseeDtoByAppId(applicationDto.getId()).getEntity();
        map.put("LicenseeName",  subLicensee.getLicenseeName());
        map.put("isSpecial", "N");
        List<AppPremSubSvcRelDto> appPremSubSvcRelDtos = appPremSubSvcBeClient.getAppPremSubSvcRelDtoListByCorrIdAndType(
                        appPremisesCorrelationDto.getId(), HcsaConsts.SERVICE_TYPE_SPECIFIED)
                .getEntity();
        if (!IaisCommonUtils.isEmpty(appPremSubSvcRelDtos)) {
            appPremSubSvcRelDtos = appPremSubSvcRelDtos.stream()
                    .filter(dto -> ApplicationConsts.RECORD_STATUS_APPROVE_CODE.equals(dto.getStatus()))
                    .collect(Collectors.toList());
        }
        if (!IaisCommonUtils.isEmpty(appPremSubSvcRelDtos)) {
            int i=0;
            StringBuilder svcNameLicNo = new StringBuilder();
            for (AppPremSubSvcRelDto specSvc : appPremSubSvcRelDtos) {
                HcsaServiceDto specServiceDto = HcsaServiceCacheHelper.getServiceById(specSvc.getSvcId());
                String svcName1 = specServiceDto.getSvcName();
                String index=ALPHABET_ARRAY_PROTOTYPE[i++];
                svcNameLicNo.append("<p>(").append(index).append(")&nbsp;&nbsp;").append(svcName1).append("</p>");
            }
            map.put("isSpecial", "Y");
            String specialSvcSecName="Specified Services";
            List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceClient.getHcsaSvcSpePremisesTypeDtos(baseServiceDto.getSvcName(),
                    applicationDto.getServiceId()).getEntity();
            for (HcsaSvcSpePremisesTypeDto spe:hcsaSvcSpePremisesTypeDtos
            ) {
                if(StringUtil.isNotEmpty(spe.getSpecialSvcSecName())&&spe.getPremisesType().equals(appGrpPremisesDto.getPremisesType())){
                    specialSvcSecName=spe.getSpecialSvcSecName();
                    break;
                }
            }
            map.put("ss1ss2Header", specialSvcSecName);
            map.put("ss1ss2", svcNameLicNo.toString());

        }
        map.put("isCorpPass", "N");
        if(inspectionRecommendation != null){
            map.put("inInspection", "Y");
            map.put("inspectionText", inspectionRecommendation.getRemarks());
        }else {
            map.put("inInspection", "N");
        }
        if(organizationDto != null){
            if(StringUtil.isEmpty(organizationDto.getUenNo())){
                map.put("isCorpPass", "Y");
                map.put("corpPassLink", corpPassUrl);
            }
        }
        map.put("systemLink", loginUrl);

        map.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
        map.put("regulationLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
        map.put("link", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_LINK));
        map.put("scdfLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_SCDF_LINK));
        map.put("momLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_MOM_LINK));

        map.put("phoneNumber", systemPhoneNumber);
        map.put("emailAddress1", systemAddressOne);
        map.put("emailAddress2", systemAddressTwo);
        map.put("MOH_AGENCY_NAME", MohName);

        try {
//            String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is approved ";
            Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
            subMap.put("ApplicationType", applicationTypeShow);
            subMap.put("ApplicationNumber", applicationNo);
            String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_ID,subMap);
            String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_SMS_ID,subMap);
            String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_MESSAGE_ID,subMap);
            log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
            log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
            log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_ID);
            emailParam.setTemplateContent(map);
            emailParam.setQueryCode(applicationNo);
            emailParam.setReqRefNum(applicationNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(applicationNo);
            emailParam.setSubject(emailSubject);
            //send email
            log.info(StringUtil.changeForLog("send new application email"));
            notificationHelper.sendNotification(emailParam);
            log.info(StringUtil.changeForLog("send new application email end"));
            //send sms
            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_SMS_ID);
            smsParam.setSubject(smsSubject);
            smsParam.setQueryCode(applicationNo);
            smsParam.setReqRefNum(applicationNo);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            smsParam.setRefId(applicationNo);
            log.info(StringUtil.changeForLog("send new application sms"));
            notificationHelper.sendNotification(smsParam);
            log.info(StringUtil.changeForLog("send new application sms end"));
            //send message
            EmailParam messageParam = new EmailParam();
            messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_MESSAGE_ID);
            messageParam.setTemplateContent(map);
            messageParam.setQueryCode(applicationNo);
            messageParam.setReqRefNum(applicationNo);
            messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            messageParam.setRefId(applicationNo);
            messageParam.setSubject(messageSubject);
            messageParam.setSvcCodeList(svcCodeList);
            log.info(StringUtil.changeForLog("send new application message"));
            notificationHelper.sendNotification(messageParam);
            log.info(StringUtil.changeForLog("send new application message end"));
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    @Autowired
    private ApplicationService applicationService;

    public void eicCallFeSuperLic(EventBusLicenceGroupDtos dto) {
        log.info(StringUtil.changeForLog("The eicCallFeSuperLic start ..."));
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        beEicGatewayClient.createLicence(dto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();

        List<LicenceGroupDto> licenceGroupDtos  = dto.getLicenceGroupDtos();
        if(IaisCommonUtils.isNotEmpty(licenceGroupDtos)){
            LicenceGroupDto licenceGroupDto = licenceGroupDtos.get(0);
            if(licenceGroupDto != null){
                List<SuperLicDto> superLicDtos = licenceGroupDto.getSuperLicDtos();
                if(IaisCommonUtils.isNotEmpty(superLicDtos)){
                    LicenceDto licenceDto = superLicDtos.get(0).getLicenceDto();
                    if(licenceDto != null){
                        List<ApplicationDto> applicationDtos = dto.getApplicationDto();
                        if(IaisCommonUtils.isNotEmpty(applicationDtos)){
                            for(ApplicationDto applicationDto : applicationDtos){
                                applicationDto.setNewLicenceId(licenceDto.getId());
                                applicationService.callEicInterApplication(applicationDto);
                            }
                        }else{
                            log.error(StringUtil.changeForLog("The applicationDtos is null"));
                        }
                    }else{
                        log.error(StringUtil.changeForLog("The licenceDto is null"));
                    }
                }else{
                    log.error(StringUtil.changeForLog("The superLicDtos is null"));
                }
            }else{
                log.error(StringUtil.changeForLog("The licenceGroupDto is null"));
            }
        }else{
            log.error(StringUtil.changeForLog("The licenceGroupDtos is null"));
        }

        log.info(StringUtil.changeForLog("The eicCallFeSuperLic end ..."));
    }

    @Override
    public EventBusLicenceGroupDtos getEventBusLicenceGroupDtosByRefNo(String refNo) {
        return hcsaLicenceClient.getEventBusLicenceGroupDtosByRefNo(refNo).getEntity();
    }

    @Override
    public void updateLicEicRequestTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto) {
        licEicClient.saveEicTrack(licEicRequestTrackingDto);
    }

    @Override
    public EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo) {
        return licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
    }

    @Override
    public MsgTemplateDto getMsgTemplateById(String id) {
        return msgTemplateClient.getMsgTemplate(id).getEntity();
    }

    @Override
    public List<PremisesGroupDto> getPremisesGroupDtoByOriginLicenceId(String originLicenceId) {
        return hcsaLicenceClient.getPremisesGroupDtos(originLicenceId).getEntity();
    }

    @Override
    public List<LicAppCorrelationDto> getLicAppCorrelationDtosByApplicationIds(List<String> appIds) {
        return hcsaLicenceClient.getLicAppCorrelationDtosByApplicationIds(appIds).getEntity();
    }

    @Override
    public PremisesDto getHciCode(AppGrpPremisesDto appGrpPremisesDto) {
        log.info(StringUtil.changeForLog("The getHciCode start ..."));
        PremisesDto result = null;
        if(appGrpPremisesDto != null){
            result =  hcsaLicenceClient.getHciCodePremises(appGrpPremisesDto).getEntity();
        }
        log.info(StringUtil.changeForLog("The getHciCode end ..."));
        return result;
    }

   /* @Override
    public List<LicBaseSpecifiedCorrelationDto> getLicBaseSpecifiedCorrelationDtos(String svcType, String originLicenceId) {
        return licCommService.getLicBaseSpecifiedCorrelationDtos(svcType,originLicenceId);
    }*/
    @Override
    public  void changePostInsForTodoAudit( ApplicationViewDto applicationViewDto ){
        if(applicationViewDto.getLicPremisesAuditDto() != null && ApplicationConsts.INCLUDE_RISK_TYPE_INSPECTION_KEY.equalsIgnoreCase(applicationViewDto.getLicPremisesAuditDto().getIncludeRiskType())){
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
            if(applicationDto.getApplicationType().equalsIgnoreCase(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK)){
                PostInsGroupDto postInsGroupDto = hcsaLicenceClient.getPostInsGroupDto(applicationDto.getOriginLicenceId(),newAppPremisesCorrelationDto.getId()).getEntity();
                if(postInsGroupDto != null && postInsGroupDto.getLicInspectionGroupDto() != null && postInsGroupDto.getLicPremInspGrpCorrelationDto()!= null){
                    LicInspectionGroupDto licInspectionGroupDto = postInsGroupDto.getLicInspectionGroupDto();
                    licInspectionGroupDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    LicPremInspGrpCorrelationDto licPremInspGrpCorrelationDto = postInsGroupDto.getLicPremInspGrpCorrelationDto();
                    licPremInspGrpCorrelationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    postInsGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    hcsaLicenceClient.savePostInsGroupDto(postInsGroupDto);
                }
            }
        }
    }

    @Override
    public LicenceDto getLicDtoById(String licenceId) {
        LicenceDto result = null;
        if(!StringUtil.isEmpty(licenceId)){
            result = hcsaLicenceClient.getLicDtoById(licenceId).getEntity();
        }
        return result;
    }

    @Override
    public SearchResult<LicPremisesQueryDto> searchLicencesInChangeTCUDate(SearchParam searchParam) {
        return hcsaLicenceClient.searchLicencesInChangeTCUDate(searchParam).getEntity();
    }

    @Override
    public List<LicPremisesDto> getPremisesByLicIds(List<String> licenceIds) {
        return  hcsaLicenceClient.getPremisesByLicIds(licenceIds).getEntity();
    }

    @Override
    public List<LicPremisesDto> saveLicPremises(List<LicPremisesDto> licPremisesDtos) {
        return hcsaLicenceClient.saveLicPremises(licPremisesDtos).getEntity();
    }
}
