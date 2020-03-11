package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloudfeign.FeignException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * HcsaApplicationDelegator
 *
 * @author suocheng
 * @date 10/17/2019
 */
@Delegator("hcsaApplicationDelegator")
@Slf4j
public class HcsaApplicationDelegator {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationGroupService applicationGroupService;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;

    @Autowired
    private BroadcastService broadcastService;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private InsRepService insRepService;

//    public void routingTask(BaseProcessClass bpc) throws FeignException {
//        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
//        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence");
//        List<ApplicationDto> applicationDtos = new ArrayList();
//        ApplicationDto applicationDto0 = new ApplicationDto();
//        applicationDto0.setApplicationNo("test applicaitonNo");
//        applicationDto0.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
//        applicationDto0.setApplicationType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
//        applicationDtos.add(applicationDto0);
//       // taskService.routingAdminScranTask(applicationDtos);
//        //taskService.routingTask(applicationDto0,HcsaConsts.ROUTING_STAGE_PSO);
//        log.debug(StringUtil.changeForLog("the do routingTask end ...."));
//    }

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do cleanSession start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence");
        ParamUtil.setSessionAttr(bpc.request,"taskDto",null);
        ParamUtil.setSessionAttr(bpc.request,"applicationViewDto",null);
        ParamUtil.setSessionAttr(bpc.request,"isSaveRfiSelect",null);
        log.debug(StringUtil.changeForLog("the do cleanSession end ...."));
    }

    /**
     * StartStep: prepareData
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc) throws Exception{
        log.debug(StringUtil.changeForLog("the do prepareData start ..."));
        //get the task
       String  taskId = ParamUtil.getString(bpc.request,"taskId");
       TaskDto taskDto = taskService.getTaskById(taskId);

        String correlationId = taskDto.getRefNo();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(correlationId);
        appPremisesCorrelationDto.setOldCorrelationId(correlationId);
        String newCorrelationId = appPremisesCorrelationDto.getId();
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(newCorrelationId);
        applicationViewDto.setNewAppPremisesCorrelationDto(appPremisesCorrelationDto);
//        get routing stage dropdown send to page.
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList=applicationViewService.getStage(applicationViewDto.getApplicationDto().getServiceId(),taskDto.getTaskKey());

        Map<String,String> routingStage=new HashMap<>();
        for (HcsaSvcRoutingStageDto hcsaSvcRoutingStage:hcsaSvcRoutingStageDtoList) {
            routingStage.put(hcsaSvcRoutingStage.getStageCode(),hcsaSvcRoutingStage.getStageName());
        }
        //   rollback
        Map<String,String> rollBackMap = new HashMap<>();
        if(applicationViewDto.getRollBackHistroyList()!=null){
        for (AppPremisesRoutingHistoryDto e:applicationViewDto.getRollBackHistroyList()) {
            String stageName=applicationViewService.getStageById(e.getStageId()).getStageName();
            String userId=e.getActionby();
            String wrkGrpId=e.getWrkGrpId();
            OrgUserDto user=applicationViewService.getUserById(userId);
            String actionBy=user.getDisplayName();

            rollBackMap.put(actionBy+"("+stageName+")",e.getStageId()+","+wrkGrpId+","+userId);
          }
        }
        applicationViewDto.setRollBack(rollBackMap);


        //add special stages

     //   routingStage.put(ApplicationConsts.PROCESSING_DECISION_LICENCE_START_DATE, "Licence Start Date");

       Integer rfiCount =  applicationService.getAppBYGroupIdAndStatus(applicationViewDto.getApplicationDto().getAppGrpId(),
               ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
       log.info(StringUtil.changeForLog("The rfiCount is -->:"+rfiCount));

        if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationViewDto.getApplicationDto().getStatus())){
            routingStage.put(ApplicationConsts.PROCESSING_DECISION_AO3_BROADCAST_QUERY,"Broadcast Query For Internal");
        }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(applicationViewDto.getApplicationDto().getStatus())){
            routingStage.put(ApplicationConsts.PROCESSING_DECISION_AO3_BROADCAST_REPLY,"Broadcast Reply For Internal");
        }

        if(HcsaConsts.ROUTING_STAGE_AO3.equals(taskDto.getTaskKey())){
            routingStage.put(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL,
                    MasterCodeUtil.getCodeDesc(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL));
            routingStage.put(ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS,"Route To DMS");
        }else if(HcsaConsts.ROUTING_STAGE_AO2.equals(taskDto.getTaskKey())
                ||HcsaConsts.ROUTING_STAGE_AO1.equals(taskDto.getTaskKey())){
        }else if(HcsaConsts.ROUTING_STAGE_PSO.equals(taskDto.getTaskKey()) || HcsaConsts.ROUTING_STAGE_ASO.equals(taskDto.getTaskKey()) ){
            if(rfiCount==0){
                routingStage.put(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION,"Request For Information");
            }
        }
        applicationViewDto.setVerified(routingStage);

        //recomeDation
        HcsaServiceDto hcsaServiceDto=applicationViewService.getHcsaServiceDtoById(applicationViewDto.getApplicationDto().getServiceId());
        String svcCode=hcsaServiceDto.getSvcCode();
        RiskAcceptiionDto riskAcceptiionDto=new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = new ArrayList<>();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        List<String> riskResult = new ArrayList<>();
        if(listRiskResultDto!=null && !listRiskResultDto.isEmpty()){
            for(RiskResultDto riskResultDto :listRiskResultDto){
                String dateType = riskResultDto.getDateType();
                String codeDesc = MasterCodeUtil.getCodeDesc(dateType);
                String count = String.valueOf(riskResultDto.getTimeCount());
                String recommTime = count+" "+codeDesc;
                riskResult.add(recommTime);
            }
        }
        applicationViewDto.setRecomeDation(riskResult);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        if(applicationDto != null){
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType()) ){
                //RFC
                if (!StringUtil.isEmpty(applicationDto.getId())) {
                    List<AppEditSelectDto> appEditSelectDtos = applicationService.getAppEditSelectDtos(applicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFC);
                    if (!IaisCommonUtils.isEmpty(appEditSelectDtos)) {
                        applicationViewDto.setAppEditSelectDto(appEditSelectDtos.get(0));
                    }
                }
            }else{
                AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appEditSelectDto.setDocEdit(true);
                appEditSelectDto.setMedAlertEdit(true);
                appEditSelectDto.setServiceEdit(true);
                appEditSelectDto.setPoEdit(true);
                appEditSelectDto.setDpoEdit(true);
                applicationViewDto.setAppEditSelectDto(appEditSelectDto);
            }
        }

        ParamUtil.setSessionAttr(bpc.request,"applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request,"taskDto", taskDto);
        log.debug(StringUtil.changeForLog("the do prepareData end ...."));
    }

    /**
     * StartStep: chooseStage
     *
     * @param bpc
     * @throws
     */
    public void chooseStage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do chooseStage start ...."));
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        String appPremCorreId=taskDto.getRefNo();
        //save recommendation
        String recommendationStr = ParamUtil.getString(bpc.request,"recomedation");
        if(("---select---").equals(recommendationStr)){

        }else if(("reject").equals(recommendationStr)){
            AppPremisesRecommendationDto appPremisesRecommendationDto=new AppPremisesRecommendationDto();
            appPremisesRecommendationDto.setAppPremCorreId(appPremCorreId);
            appPremisesRecommendationDto.setRecomInNumber(0);
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
            insRepService.updateRecommendation(appPremisesRecommendationDto);
        }else{
            AppPremisesRecommendationDto appPremisesRecommendationDto=new AppPremisesRecommendationDto();
            String[] strs=recommendationStr.split("\\s+");
            appPremisesRecommendationDto.setAppPremCorreId(appPremCorreId);
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
            appPremisesRecommendationDto.setRecomInNumber( Integer.valueOf(strs[0]));
            appPremisesRecommendationDto.setChronoUnit(strs[1]);
            insRepService.updateRecommendation(appPremisesRecommendationDto);
        }
        String verified = ParamUtil.getString(bpc.request,"verified");
        String rollBack=ParamUtil.getString(bpc.request,"rollBack");
        String nextStage=null;
        if(("---select---").equals(rollBack)){
            nextStage=verified;
        }else{
            nextStage="PROCRB";
        }

        String reply=ParamUtil.getString(bpc.request,"nextStageReply");
        if(!("---select---").equals(reply)){
            nextStage=reply;
        }

        log.debug(StringUtil.changeForLog("the nextStage is -->:"+nextStage));
        ParamUtil.setRequestAttr(bpc.request, "crud_action_type", nextStage);

        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())){
            String[] fastTracking =  ParamUtil.getStrings(bpc.request,"fastTracking");
            if(fastTracking!=null){
                applicationViewDto.getApplicationDto().setFastTracking(true);
            }
        }
        log.debug(StringUtil.changeForLog("the do chooseStage end ...."));
    }

    /**
     * StartStep: rontingTaskToPSO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToPSO(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_PSO,ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING,RoleConsts.USER_ROLE_PSO);
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO end ...."));
    }


    /**
     * StartStep: rontingTaskToINS
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToINS(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_INS,ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT,RoleConsts.USER_ROLE_INSPECTIOR);
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS end ...."));
    }


    /**
     * StartStep: rontingTaskToASO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToASO(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_ASO,ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING,RoleConsts.USER_ROLE_ASO);
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO end ...."));
    }

    /**
     * StartStep: rontingTaskToAO1
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO1(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO1,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,RoleConsts.USER_ROLE_AO1);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 end ...."));
    }



    /**
     * StartStep: rontingTaskToAO2
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO2(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO2,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,RoleConsts.USER_ROLE_AO2);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 end ...."));
    }


    /**
     * StartStep: rontingTaskToAO3
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO3(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO3,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,RoleConsts.USER_ROLE_AO3);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 end ...."));
    }


    /**
     * StartStep: approve
     *
     * @param bpc
     * @throws
     */
    public void approve(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do approve start ...."));
        routingTask(bpc,null,ApplicationConsts.APPLICATION_STATUS_APPROVED,null);
        log.debug(StringUtil.changeForLog("the do approve end ...."));
    }

    /**
     * StartStep: routeToDMS
     *
     * @param bpc
     * @throws
     */
    public void routeToDMS(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do routeToDMS start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto application=applicationViewDto.getApplicationDto();
        application.setStatus(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS);
        applicationViewDto.setApplicationDto(application);
        //todo
        //update to DB
        applicationService.updateFEApplicaiton(application);
        log.debug(StringUtil.changeForLog("the do routeToDMS end ...."));
    }

    /**
     * StartStep: routeBack
     *
     * @param bpc
     * @throws
     */
    public void routeBack(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do routeBack start ...."));
        String str=ParamUtil.getString(bpc.request,"rollBack");
        String[] result=str.split(",");
        String satageId=result[0];
        String wrkGpId=result[1];
        String userId=result[2];
        if(HcsaConsts.ROUTING_STAGE_ASO.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_ASO,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_ASO,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_PSO.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_PSO,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_PSO,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_INS.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_INS,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_INSPECTIOR,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_AO1.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_AO1,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_AO1,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_AO2.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_AO2,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_AO2,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_AO3.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_AO3,ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,RoleConsts.USER_ROLE_AO3,wrkGpId,userId);
        }
        log.debug(StringUtil.changeForLog("the do routeBack end ...."));
    }

    /**
     * StartStep: internalEnquiry
     *
     * @param bpc
     * @throws
     */
    public void internalEnquiry(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do internalEnquiry start ...."));
        //TODO:internalEnquiry
        log.debug(StringUtil.changeForLog("the do internalEnquiry end ...."));
    }


    /**
     * StartStep: support
     *
     * @param bpc
     * @throws
     */
    public void support(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do support start ...."));
        //TODO:support

        log.debug(StringUtil.changeForLog("the do support end ...."));
    }

    /**
     * StartStep: broadcast
     *
     * @param bpc
     * @throws
     */
    public void broadcast(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do broadcast start ...."));
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
          List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = appPremisesRoutingHistoryService.
                  getAppPremisesRoutingHistoryDtosByAppNo(applicationDto.getApplicationNo());
        List<String> userIds = getUserIds(appPremisesRoutingHistoryDtos);
        if(userIds != null && userIds.size() > 0){
            BroadcastOrganizationDto broadcastOrganizationDto = broadcastService.getBroadcastOrganizationDto(
                    applicationDto.getApplicationNo(),AppConsts.DOMAIN_TEMPORARY);
            BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
            //create workgroup
            WorkingGroupDto workingGroupDto = broadcastOrganizationDto.getWorkingGroupDto();
            broadcastOrganizationDto.setRollBackworkingGroupDto((WorkingGroupDto)CopyUtil.copyMutableObject(workingGroupDto));
            if(workingGroupDto ==  null){
                workingGroupDto = new WorkingGroupDto();
                workingGroupDto.setGroupName(applicationDto.getApplicationNo());
                workingGroupDto.setGroupDomain(AppConsts.DOMAIN_TEMPORARY);
            }
            workingGroupDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);

            //add this user to this workgroup
            List<UserGroupCorrelationDto> userGroupCorrelationDtoList = broadcastOrganizationDto.getUserGroupCorrelationDtoList();
            if(broadcastOrganizationDto.getWorkingGroupDto()!= null && userGroupCorrelationDtoList != null && userGroupCorrelationDtoList.size() > 0){
                List<UserGroupCorrelationDto> cloneUserGroupCorrelationDtos = new ArrayList<>();
                CopyUtil.copyMutableObjectList(userGroupCorrelationDtoList,cloneUserGroupCorrelationDtos);
                broadcastOrganizationDto.setRollBackUserGroupCorrelationDtoList(cloneUserGroupCorrelationDtos);
                userGroupCorrelationDtoList =changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtoList,AppConsts.COMMON_STATUS_ACTIVE);
            }else{
                userGroupCorrelationDtoList = new ArrayList<>();
                for(String id : userIds) {
                    UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                    userGroupCorrelationDto.setUserId(id);
                    userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    userGroupCorrelationDto.setIsLeadForGroup(Integer.parseInt(AppConsts.NO));
                    userGroupCorrelationDtoList.add(userGroupCorrelationDto);
                }
            }
            broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtoList);

            //complated this task and create the history
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
            taskDto =  completedTask(taskDto);
            broadcastOrganizationDto.setComplateTask(taskDto);
            String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
            String processDecision = ParamUtil.getString(bpc.request,"nextStage");
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                    applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),internalRemarks,processDecision,taskDto.getRoleId());
            broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
            broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto)CopyUtil.copyMutableObject(applicationDto));
            //update application status
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST);
            broadcastApplicationDto.setApplicationDto(applicationDto);
            //create the new task and create the history
            TaskDto taskDtoNew = TaskUtil.getTaskDto(taskDto.getTaskKey(),TaskConsts.TASK_TYPE_MAIN_FLOW, taskDto.getRefNo(),null,
                    null,null,0,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW, RoleConsts.USER_ROLE_BROADCAST,IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastOrganizationDto.setCreateTask(taskDtoNew);
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),
                    taskDto.getTaskKey(),null, taskDto.getWkGrpId(),null,null,RoleConsts.USER_ROLE_AO3);
            broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            //save the broadcast
            broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,null);
            broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,null);
            //0062460 update FE  application status.
            applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
        }

        log.debug(StringUtil.changeForLog("the do broadcast end ...."));
    }


    /**
     * StartStep: broadcastReply
     *
     * @param bpc
     * @throws
     */
    public void broadcastReply(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do broadcastReply start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO3,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,RoleConsts.USER_ROLE_AO3);
        log.debug(StringUtil.changeForLog("the do broadcastReply end ...."));
    }


    /**
     * StartStep: replay
     *
     * @param bpc
     * @throws
     */
    public void replay(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do replay start ...."));
        //TODO:replay
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        String appCorrId=taskDto.getRefNo();
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto=appPremisesRoutingHistoryService.getSecondRouteBackHistoryByCorrId(appCorrId);
        String wrkGrpId=appPremisesRoutingHistoryDto.getWrkGrpId();
        String roleId=appPremisesRoutingHistoryDto.getRoleId();
        String stageId=appPremisesRoutingHistoryDto.getStageId();
        String userId=appPremisesRoutingHistoryDto.getActionby();
        rollBack(bpc,stageId,ApplicationConsts.APPLICATION_STATUS_REPLY,roleId,wrkGrpId,userId);
        log.debug(StringUtil.changeForLog("the do replay end ...."));
    }


    /**
     * StartStep: reject
     *
     * @param bpc
     * @throws
     */
    public void reject(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do reject start ...."));
        //TODO:reject
        log.debug(StringUtil.changeForLog("the do reject end ...."));
    }

    /**
     * StartStep: requestForInformation
     *
     * @param bpc
     * @throws
     */
    public void requestForInformation(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do requestForInformation start ...."));
        routingTask(bpc,null,ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION,null);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        //send message to FE user.
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
        String mesNO = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(mesNO);
        interMessageDto.setService_id(applicationDto.getServiceId());
        String url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION+applicationDto.getApplicationNo();
        //Request For Change
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())){
            //judge premises amend or licence amend
            AppEditSelectDto appEditSelectDto = applicationViewDto.getAppEditSelectDto();
            if(appEditSelectDto != null){
                if(appEditSelectDto.isPremisesListEdit()){
                    url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+MessageConstants.MESSAGE_CALL_BACK_URL_PREMISES_LIST+applicationDto.getApplicationNo();
                }
            }
        }

        interMessageDto.setProcessUrl(url);
        interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
        interMessageDto.setUserId(applicationViewDto.getSubmitBy());
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        inboxMsgService.saveInterMessage(interMessageDto);
        log.debug(StringUtil.changeForLog("the do requestForInformation end ...."));
    }

    /**
     * StartStep: lienceStartDate
     *
     * @param bpc
     * @throws
     */
    public void lienceStartDate(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do lienceStartDate start ...."));
        //TODO:lienceStartDate
        log.debug(StringUtil.changeForLog("the do lienceStartDate end ...."));
    }


    /**
     * StartStep: doDocument
     *
     * @param bpc
     * @throws
     */
    public void doDocument(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doDocument start ...."));
//        //TODO:save file
//        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
//        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
//        String dcrudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
//        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
//
//        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
//        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);


        log.debug(StringUtil.changeForLog("the do doDocument end ...."));
    }



    //***************************************
    //private methods
    //*************************************

    private void routingTask(BaseProcessClass bpc,String stageId,String appStatus,String roleId ) throws FeignException, CloneNotSupportedException {

        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
        String newCorrelationId = newAppPremisesCorrelationDto.getId();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();

        //complated this task and create the history
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto =  completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String processDecision = ParamUtil.getString(bpc.request,"nextStage");
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),internalRemarks,processDecision,taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        String oldStatus = applicationDto.getStatus();
        applicationDto.setStatus(appStatus);
        broadcastApplicationDto.setApplicationDto(applicationDto);
        // send the task
        if(!StringUtil.isEmpty(stageId)){
            //For the BROADCAST Rely
            if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(oldStatus)){
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1 = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(
                        applicationDto.getApplicationNo(),stageId
                );
                log.debug(StringUtil.changeForLog("The appId is-->;"+ applicationDto.getId()));
                log.debug(StringUtil.changeForLog("The stageId is-->;"+ stageId));
                if(appPremisesRoutingHistoryDto1 != null){
                    TaskDto newTaskDto = TaskUtil.getTaskDto(stageId,TaskConsts.TASK_TYPE_MAIN_FLOW,
                            taskDto.getRefNo(),appPremisesRoutingHistoryDto1.getWrkGrpId(),
                            appPremisesRoutingHistoryDto1.getActionby(),new Date(),0,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW,roleId,
                            IaisEGPHelper.getCurrentAuditTrailDto());
                    broadcastOrganizationDto.setCreateTask(newTaskDto);
                    //delete workgroup
                    BroadcastOrganizationDto broadcastOrganizationDto1 = broadcastService.getBroadcastOrganizationDto(
                            applicationDto.getApplicationNo(),AppConsts.DOMAIN_TEMPORARY);

                    WorkingGroupDto workingGroupDto = broadcastOrganizationDto1.getWorkingGroupDto();
                    broadcastOrganizationDto.setRollBackworkingGroupDto((WorkingGroupDto) CopyUtil.copyMutableObject(workingGroupDto));
                    workingGroupDto = changeStatusWrokGroup(workingGroupDto,AppConsts.COMMON_STATUS_DELETED);
                    broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);
                    List<UserGroupCorrelationDto> userGroupCorrelationDtos = broadcastOrganizationDto1.getUserGroupCorrelationDtoList();
                    List<UserGroupCorrelationDto> cloneUserGroupCorrelationDtos = new ArrayList<>();
                    CopyUtil.copyMutableObjectList(userGroupCorrelationDtos,cloneUserGroupCorrelationDtos);
                    broadcastOrganizationDto.setRollBackUserGroupCorrelationDtoList(cloneUserGroupCorrelationDtos);
                    userGroupCorrelationDtos = changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtos,AppConsts.COMMON_STATUS_DELETED);
                    broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtos);
                }else{
                    throw new IaisRuntimeException("This getAppPremisesCorrelationId can not get the broadcast -- >:"+applicationViewDto.getAppPremisesCorrelationId());
                }
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)&&!applicationDto.isFastTracking()){
                List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
                boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getApplicationNo(),
                        ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                if(isAllSubmit){
                    // send the task to Ao3
                    TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(applicationDtoList,
                            HcsaConsts.ROUTING_STAGE_AO3,roleId,IaisEGPHelper.getCurrentAuditTrailDto());
                    List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                    broadcastOrganizationDto.setOneSubmitTaskList(taskDtos);
                    broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
                }
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
                AppInspectionStatusDto appInspectionStatusDto = new AppInspectionStatusDto();
                appInspectionStatusDto.setAppPremCorreId(taskDto.getRefNo());
                appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
                broadcastApplicationDto.setAppInspectionStatusDto(appInspectionStatusDto);
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,HcsaConsts.ROUTING_STAGE_PRE,
                        taskDto.getWkGrpId(),null,null,roleId);
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }else{
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
            }
            //add history for next stage start
            if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)&&!ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,null,
                        taskDto.getWkGrpId(),null,null,roleId);
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }
        }else{
            List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
            boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getApplicationNo(),
                    ApplicationConsts.APPLICATION_STATUS_APPROVED);
            if(isAllSubmit || applicationDto.isFastTracking()){
                //update application Group status
                ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                broadcastApplicationDto.setRollBackApplicationGroupDto((ApplicationGroupDto)CopyUtil.copyMutableObject(applicationGroupDto));
                applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                broadcastApplicationDto.setApplicationGroupDto(applicationGroupDto);
            }
        }
        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String evenRefNum = String.valueOf(System.currentTimeMillis());
        broadcastOrganizationDto.setEventRefNo(evenRefNum);
        broadcastApplicationDto.setEventRefNo(evenRefNum);
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process);
        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process);
        //0062460 update FE  application status.
        applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
    }

    private void rollBack(BaseProcessClass bpc,String stageId,String appStatus,String roleId ,String wrkGpId,String userId) throws FeignException, CloneNotSupportedException {
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();

        //complated this task and create the history
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto =  completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String processDecision = ParamUtil.getString(bpc.request,"nextStage");
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),internalRemarks,processDecision,taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        String oldStatus = applicationDto.getStatus();
        applicationDto.setStatus(appStatus);
        broadcastApplicationDto.setApplicationDto(applicationDto);

        TaskDto newTaskDto = TaskUtil.getTaskDto(stageId,TaskConsts.TASK_TYPE_MAIN_FLOW,
                taskDto.getRefNo(),wrkGpId, userId,new Date(),0,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW,roleId,
                IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastOrganizationDto.setCreateTask(newTaskDto);

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,null,
                taskDto.getWkGrpId(),null,null,roleId);
        broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);

        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String evenRefNum = String.valueOf(System.currentTimeMillis());
        broadcastOrganizationDto.setEventRefNo(evenRefNum);
        broadcastApplicationDto.setEventRefNo(evenRefNum);
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process);
        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process);

        //0062460 update FE  application status.
        applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());

    }

    private List<UserGroupCorrelationDto> changeStatusUserGroupCorrelationDtos(List<UserGroupCorrelationDto> userGroupCorrelationDtos,String status){
        List<UserGroupCorrelationDto> result = new ArrayList<>();
        if(userGroupCorrelationDtos!= null && userGroupCorrelationDtos.size() >0){
            for (UserGroupCorrelationDto userGroupCorrelationDto : userGroupCorrelationDtos){
                userGroupCorrelationDto.setStatus(status);
                result.add(userGroupCorrelationDto);
            }
        }
        return  result;
    }
    private WorkingGroupDto changeStatusWrokGroup(WorkingGroupDto workingGroupDto,String staus){
        if(workingGroupDto!= null && !StringUtil.isEmpty(staus)){
            workingGroupDto.setStatus(staus);
        }
       return workingGroupDto;
    }
    private AppPremisesRoutingHistoryDto getAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                         String stageId,String subStageId,String wrkGrpId, String internalRemarks,String processDecision,
                                                                      String roleId){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setSubStage(subStageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDecision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGrpId);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return appPremisesRoutingHistoryDto;
    }
    private int remainDays(TaskDto taskDto){
       int result = 0;
       //todo: wait count kpi
      // String  resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(),taskDto.getSlaDateCompleted().getTime(), "d");
     // log.debug(StringUtil.changeForLog("The resultStr is -->:")+resultStr);
      return  result;
    }


    private TaskDto completedTask(TaskDto taskDto){
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskDto;
    }
    private ApplicationDto updateApplicaiton(ApplicationDto applicationDto,String appStatus){
        applicationDto.setStatus(appStatus);
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private List<String> getUserIds(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos){
        Set<String> set = new HashSet<>();
        if(appPremisesRoutingHistoryDtos == null || appPremisesRoutingHistoryDtos.size() ==0){
            return  null;
        }
        for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto :appPremisesRoutingHistoryDtos ){
            set.add(appPremisesRoutingHistoryDto.getActionby());
        }
        return  new ArrayList(set);


    }

    /************************/

}
