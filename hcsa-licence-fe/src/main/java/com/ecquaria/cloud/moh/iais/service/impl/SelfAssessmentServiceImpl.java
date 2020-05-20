package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.FeSelfAssessmentSyncDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.EmailHelper;
import com.ecquaria.cloud.moh.iais.helper.FeSelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.SelfAssessmentService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEmailClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/5/6
 **/

@Service
@Slf4j
public class SelfAssessmentServiceImpl implements SelfAssessmentService {
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private FeEicGatewayClient gatewayClient;

    @Autowired
    private EmailHelper emailHelper;

    @Autowired
    private FeEmailClient feEmailClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Override
    public List<SelfAssessment> receiveSelfAssessmentByGroupId(String groupId) {
        List<SelfAssessment> selfAssessmentList = IaisCommonUtils.genNewArrayList();

        // (S) Group , (M) application
        List<ApplicationDto> appList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return selfAssessmentList;
        }

        //common data
        ChecklistConfigDto common = appConfigClient.getMaxVersionCommonConfig().getEntity();
        List<PremCheckItem> commonQuestion = FeSelfChecklistHelper.loadPremisesQuestion(common, false);

        SelfAssessmentConfig commonConfig = new SelfAssessmentConfig();
        commonConfig.setConfigId(common.getId());
        commonConfig.setCommon(true);
        commonConfig.setQuestion(commonQuestion);

        for(ApplicationDto app : appList){
            String appId = app.getId();
            String svcId = app.getServiceId();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(svcId);
            String svcCode = hcsaServiceDto.getSvcCode();
            String svcName = hcsaServiceDto.getSvcName();
            String type = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.SELF_ASSESSMENT);
            String module = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);

            ChecklistConfigDto serviceConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module).getEntity();

            List<AppPremisesCorrelationDto>  correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
            for (AppPremisesCorrelationDto correlationDto : correlationList) {
                String corrId = correlationDto.getId();
                String appGrpPremId = correlationDto.getAppGrpPremId();
                AppGrpPremisesEntityDto appGrpPremises = applicationClient.getAppGrpPremise(appGrpPremId).getEntity();
                String address = MiscUtil.getAddress(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(),
                        appGrpPremises.getBuildingName(), appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode());

                SelfAssessment selfAssessment = new SelfAssessment();
                selfAssessment.setSvcId(svcId);
                selfAssessment.setCorrId(corrId);
                selfAssessment.setCanEdit(true);
                selfAssessment.setSvcName(svcName);
                selfAssessment.setPremises(address);

                List<SelfAssessmentConfig> configList = IaisCommonUtils.genNewArrayList();
                configList.add(commonConfig);

                SelfAssessmentConfig svcConfig = new SelfAssessmentConfig();
                svcConfig.setSvcId(svcId);
                svcConfig.setSvcName(svcName);
                svcConfig.setConfigId(serviceConfig.getId());
                svcConfig.setCommon(false);
                svcConfig.setSvcCode(serviceConfig.getSvcCode());
                svcConfig.setSvcName(serviceConfig.getSvcName());

                List<PremCheckItem> serviceQuestion = FeSelfChecklistHelper.loadPremisesQuestion(serviceConfig, false);
                List<String> serviceSubtypeName = getServiceSubTypeName(corrId);
                for(String subTypeName : serviceSubtypeName){
                    ChecklistConfigDto subTypeConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module, subTypeName).getEntity();
                    if (subTypeConfig != null){
                        svcConfig.setHasSubtype(true);
                        List<PremCheckItem> subTypeQuestion = FeSelfChecklistHelper.loadPremisesQuestion(subTypeConfig, true);
                        serviceQuestion.addAll(subTypeQuestion);
                    }
                }

                svcConfig.setQuestion(serviceQuestion);
                configList.add(svcConfig);
                selfAssessment.setSelfAssessmentConfig(configList);
                selfAssessmentList.add(selfAssessment);
            }
        }

        return selfAssessmentList;
    }

    @Override
    public List<SelfAssessment> receiveSelfAssessmentRfiByCorrId(String corrId) {
        return FeSelfChecklistHelper.receiveSelfAssessmentDataByCorrId(corrId);
    }

    private List<String> getServiceSubTypeName(String correlationId){
        List<String> serviceSubtypeName = IaisCommonUtils.genNewArrayList();
        List<AppSvcPremisesScopeDto> scopeList = applicationClient.getAppSvcPremisesScopeListByCorreId(correlationId).getEntity();
        for (AppSvcPremisesScopeDto premise : scopeList){
            boolean isSubService = premise.isSubsumedType();
            if (!isSubService){
                String subTypeId = premise.getScopeName();
                HcsaServiceSubTypeDto subType = appConfigClient.getHcsaServiceSubTypeById(subTypeId).getEntity();
                String subTypeName = subType.getSubtypeName();
                serviceSubtypeName.add(subTypeName);
            }
        }
        return serviceSubtypeName;
    }

    @Override
    public List<SelfAssessment> receiveSubmittedSelfAssessmentDataByGroupId(String groupId) {
        List<SelfAssessment> viewData = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> appList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return viewData;
        }

        for(ApplicationDto app : appList){
           String appId = app.getId();
            List<AppPremisesCorrelationDto>  correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
            for (AppPremisesCorrelationDto correlationDto : correlationList) {
                String corrId = correlationDto.getId();
                List<SelfAssessment> dataByCorrId = receiveSelfAssessmentRfiByCorrId(corrId);
                dataByCorrId.forEach(i -> viewData.add(i));
            }
        }


        return viewData;
    }

   private void sendNotificationToInspector(List<String> correlationId) throws IOException, TemplateException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("corrData", correlationId);
        jsonObject.put("status", Arrays.asList(TaskConsts.TASK_STATUS_PENDING, TaskConsts.TASK_STATUS_READ, TaskConsts.TASK_STATUS_COMPLETED));
        jsonObject.put("roleId", RoleConsts.USER_ROLE_INSPECTIOR);

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<String> emailAddress = gatewayClient.getEmailByCorrelationIdAndStatusAndRole(jsonObject.toString(), signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();

        Map<String,Object> map = new HashMap(1);
        map.put("APPLICANT_NAME", StringUtil.viewHtml("Yi chen"));
        map.put("DETAILS", StringUtil.viewHtml("test"));
        map.put("A_HREF", StringUtil.viewHtml(""));
        map.put("MOH_NAME", AppConsts.MOH_AGENCY_NAME);
        MsgTemplateDto entity = emailHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_SELF_DECL_ID);
        String messageContent = entity.getMessageContent();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);


        EmailDto emailDto = new EmailDto();
        emailDto.setContent(templateMessageByContent);
        emailDto.setSubject("Self-Assessment submission for Application Number");
        emailDto.setSender("yichen_guo@ecquaria.com");
        emailDto.setReceipts(emailAddress);

        feEmailClient.sendNotification(emailDto);
    }


    private void callFeEicAppPremisesSelfDeclChkl(List<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklDtos) {
        //route to be
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        FeSelfAssessmentSyncDataDto selfDeclSyncDataDto = new FeSelfAssessmentSyncDataDto();
        selfDeclSyncDataDto.setFeSyncData(appPremisesSelfDeclChklDtos);

        gatewayClient.routeSelfAssessment(selfDeclSyncDataDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getStatusCode();
    }

    @Override
    public Boolean saveAllSelfAssessment(List<SelfAssessment> selfAssessmentList) {
        boolean successSubmit = false;
        FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> result =  applicationClient.saveAllSelfAssessment(selfAssessmentList);
        if (result.getStatusCode() == HttpStatus.SC_OK){
            successSubmit = true;

           try {
               List<String> sendEmailAddress = selfAssessmentList.stream().map(SelfAssessment::getCorrId).collect(Collectors.toList());
                sendNotificationToInspector(sendEmailAddress);
            }catch (Exception e){
                log.info(StringUtil.changeForLog("encounter failure when self decl send notification" + e.getMessage()));
            }

            EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT,
                    SelfAssessmentServiceImpl.class.getName(),
                    "callFeEicAppPremisesSelfDeclChkl", currentApp + "-" + currentDomain,
                    AppPremisesSelfDeclChklDto.class.getName(), JsonUtil.parseToJson(result.getEntity()));

            try {
                FeignResponseEntity<EicRequestTrackingDto> fetchResult =  eicRequestTrackingHelper.getAppEicClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
                if (HttpStatus.SC_OK == fetchResult.getStatusCode()){
                    EicRequestTrackingDto preEicRequest = fetchResult.getEntity();
                    if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(preEicRequest.getStatus())){
                        callFeEicAppPremisesSelfDeclChkl(result.getEntity());
                        preEicRequest.setProcessNum(1);
                        Date now = new Date();
                        preEicRequest.setFirstActionAt(now);
                        preEicRequest.setLastActionAt(now);
                        preEicRequest.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                        eicRequestTrackingHelper.getAppEicClient().saveEicTrack(preEicRequest);
                    }
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog("encounter failure when sync self assessment to be" + e.getMessage()));
            }
        }

        return successSubmit;
    }

    @Override
    public Boolean hasSubmittedSelfAssMtByGroupId(String groupId) {
        FeignResponseEntity<Integer> result = applicationClient.getApplicationSelfAssMtStatusByGroupId(groupId);
        if (HttpStatus.SC_OK == result.getStatusCode()){
            if (ApplicationConsts.PENDING_SUBMIT_SELF_ASSESSMENT == result.getEntity().intValue()){
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean hasSubmittedSelfAssMtRfiByCorrId(String corrId) {
        List<ApplicationDto> appList = applicationClient.getPremisesApplicationsByCorreId(corrId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return Boolean.TRUE;
        }

        boolean submitted = true;
        List<Integer> status = appList.stream()
                .map(ApplicationDto::getSelfAssMtFlag).collect(Collectors.toList());

        for (Integer i : status){
            if (ApplicationConsts.SUBMITTED_RFI_SELF_ASSESSMENT.equals(i)){
                submitted = false;
                break;
            }
        }

        return submitted;
    }

    @Override
    public void changePendingSelfAssMtStatus(String value, Boolean isGroupId) {
        log.info(StringUtil.changeForLog("changePendingSelfAssMtStatus =====>>" + value + "isGroupId" + isGroupId));
        List<ApplicationDto> appList;
        if (isGroupId){
            appList = applicationClient.listApplicationByGroupId(value).getEntity();
        }else {
            appList = applicationClient.getPremisesApplicationsByCorreId(value).getEntity();
        }

        if (IaisCommonUtils.isEmpty(appList)) {
            return;
        }

        boolean appSubmitToBeNotYet = false;
        for (ApplicationDto i : appList){
            if (ApplicationConsts.APPLICATION_STATUS_RFI_ONLY_SELF_CHECKLIST
                    .equals(i.getApplicationType())){
                appSubmitToBeNotYet = true;
            }

            i.setSelfAssMtFlag(ApplicationConsts.SUBMITTED_SELF_ASSESSMENT);
        }

        applicationClient.updateApplicationList(appList);

        //rfi solution
        //when the application does not return to be,  need to create a task
        boolean rfiSolution = isGroupId == true ? false : true;
        if (rfiSolution && appSubmitToBeNotYet){
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

            ApptFeConfirmDateDto apptFeConfirmDateDto = new ApptFeConfirmDateDto();
            TaskDto taskDto = new TaskDto();
            taskDto.setRefNo(value);
            taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
            taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            apptFeConfirmDateDto.setTaskDto(taskDto);

            log.info("create fe reply task start");
            gatewayClient.createFeReplyTask(apptFeConfirmDateDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
            log.info("create fe reply task end");
        }
    }
}
