package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.annotation.TimerTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.FeSelfDeclSyncDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclaration;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EmailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.SelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDeclService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEmailClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yichen
 * @date time:11/20/2019 1:56 PM
 * @description:  applicant will be submit self-assessment after application submitted and payment over.
                Each premises will have a common config, one or more service config、subtype config if have.
 */
@Service
@Slf4j
public class AppPremSelfDeclServiceImpl implements AppPremSelfDeclService {
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private FeEmailClient feEmailClient;

    @Autowired
    private FeEicGatewayClient gatewayClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    /**
    * @author: yichen
    * @description: Selfdecl entity is Represent a tab in the web page, (Single)first common , (Multiple)then service, (Multiple)behind subtype
    * @param: [groupId]
    * @return: java.util.List<com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl>
    */
    // Todo Logic can be modified more easily
    @TimerTrack
    public SelfDeclSubmitDto getSelfDeclByGroupId(String groupId){
        SelfDeclSubmitDto selfDeclSubmitDto = new SelfDeclSubmitDto();
        List<SelfDeclaration> selfDeclGroupList = IaisCommonUtils.genNewArrayList();
        // (S) Group , (M) application
        List<ApplicationDto> appList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            selfDeclSubmitDto.setSelfDeclarationList(selfDeclGroupList);
            return selfDeclSubmitDto;
        }

        boolean addedCoomon = false;
        Map<String, SelfDeclaration> svcDeclMapping = IaisCommonUtils.genNewHashMap();
        for(ApplicationDto app : appList){
            String appId = app.getId();
            String svcId = app.getServiceId();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(svcId);

            String svcCode = hcsaServiceDto.getSvcCode();
            String svcName = hcsaServiceDto.getSvcName();
            String type = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.SELF_ASSESSMENT);
            String module = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);

            if (!addedCoomon){
                ChecklistConfigDto common = appConfigClient.getMaxVersionCommonConfig().getEntity();
                if (common != null){
                    SelfDeclaration commonSelfDecl = new SelfDeclaration();
                    commonSelfDecl.setHasSubtype(false);
                    commonSelfDecl.setConfigId(common.getId());
                    LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = new LinkedHashMap<>();
                    commonSelfDecl.setCommon(true);

                    setCommonQuestion(appId, common, eachPremQuestion);

                    commonSelfDecl.setEachPremQuestion(eachPremQuestion);

                    addedCoomon = true;
                    selfDeclGroupList.add(commonSelfDecl);
                }
            }

            ChecklistConfigDto serviceConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module).getEntity();
            if (serviceConfig != null){
                if (svcDeclMapping.containsKey(svcId)){
                    SelfDeclaration service = svcDeclMapping.get(svcId);
                    LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion =  service.getEachPremQuestion();
                    setServiceQuestion(appId, service, serviceConfig, svcCode, type, module, eachPremQuestion);
                }else {
                    SelfDeclaration service = new SelfDeclaration();
                    service.setSvcId(svcId);
                    service.setSvcName(svcName);
                    service.setSvcCode(svcCode);
                    service.setConfigId(serviceConfig.getId());

                    LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = new LinkedHashMap<>();
                    setServiceQuestion(appId, service, serviceConfig, svcCode, type, module, eachPremQuestion);
                    service.setCommon(false);
                    service.setEachPremQuestion(eachPremQuestion);

                    svcDeclMapping.put(svcId, service);
                    selfDeclGroupList.add(service);
                }
            }
        }

        selfDeclSubmitDto.setSelfDeclarationList(selfDeclGroupList);
        return selfDeclSubmitDto;
    }

    private void setCommonQuestion(String appId, ChecklistConfigDto config,
                                   LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion){
        List<AppPremisesCorrelationDto>  correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
        for (AppPremisesCorrelationDto correlationDto : correlationList) {
            String correlationId = correlationDto.getId();
            String appGrpPremId = correlationDto.getAppGrpPremId();
            AppGrpPremisesEntityDto appGrpPremises = applicationClient.getAppGrpPremise(appGrpPremId).getEntity();
            String address = MiscUtil.getAddress(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(),
                    appGrpPremises.getBuildingName(), appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode());

            List<PremCheckItem> premCheckItemList = SelfChecklistHelper.loadPremisesQuestion(config, address);

            eachPremQuestion.put(correlationId, premCheckItemList);
        }
    }

    private void setServiceQuestion(String appId, SelfDeclaration service, ChecklistConfigDto config, String svcCode, String type, String module,
                                     LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion){
        List<AppPremisesCorrelationDto>  correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
        for (AppPremisesCorrelationDto correlationDto : correlationList) {
            String corrId = correlationDto.getId();
            String appGrpPremId = correlationDto.getAppGrpPremId();
            AppGrpPremisesEntityDto appGrpPremises = applicationClient.getAppGrpPremise(appGrpPremId).getEntity();
            String address = MiscUtil.getAddress(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(),
                    appGrpPremises.getBuildingName(), appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode());

            List<PremCheckItem> premCheckItemList = SelfChecklistHelper.loadPremisesQuestion(config, address);

            List<String> serviceSubtypeName = getServiceSubTypeName(corrId);
            for(String subTypeName : serviceSubtypeName){
                service.setHasSubtype(true);
                ChecklistConfigDto subTypeConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module, subTypeName).getEntity();
                if (subTypeConfig != null){
                    List<PremCheckItem> subtypeCheckItemList = SelfChecklistHelper.loadPremisesQuestion(subTypeConfig, true, address);
                    subtypeCheckItemList.forEach(s -> {
                        premCheckItemList.add(s);
                    });
                }
            }

            eachPremQuestion.put(corrId, premCheckItemList);
        }
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
    /**
    * @author: yichen
    * @description:
    * In case of RFI, the previous record needs to be deactivated.Then synchronize data to be
    * @param: [selfDeclList]
    * @return: void
    */
    @TimerTrack
    public void saveSelfDecl(SelfDeclSubmitDto selfDeclSubmitDto) {
        //if it is RFI, the last version of record should be inactive
        List<String> lastVersionIds = selfDeclSubmitDto.getLastVersionIds();
        List<AppPremisesSelfDeclChklDto> syncData = applicationClient.saveAllSelfDecl(selfDeclSubmitDto).getEntity();

       /* try {
            sendNotificationToInspector(syncData.stream().map(AppPremisesSelfDeclChklDto::getAppPremCorreId).collect(Collectors.toList()));
        }catch (Exception e){
            log.info(StringUtil.changeForLog("encounter failure when self decl send notification" + e.getMessage()));
        }*/

        try {
            //route to be
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

            FeSelfDeclSyncDataDto selfDeclSyncDataDto = new FeSelfDeclSyncDataDto();
            selfDeclSubmitDto.setLastVersionIds(lastVersionIds);
            selfDeclSyncDataDto.setFeSyncData(syncData);

            gatewayClient.routeSelfDeclData(selfDeclSyncDataDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getStatusCode();
        }catch (Exception e){
            log.error(StringUtil.changeForLog("encounter failure when sync self decl to be" + e.getMessage()));
        }

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
            MsgTemplateDto entity = EmailHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_SELF_DECL_ID);
            String messageContent = entity.getMessageContent();
            String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);


            EmailDto emailDto = new EmailDto();
            emailDto.setContent(templateMessageByContent);
            emailDto.setSubject("Self-Assessment submission for Application Number");
            emailDto.setSender("yichen_guo@ecquaria.com");
            emailDto.setReceipts(emailAddress);

            feEmailClient.sendNotification(emailDto);
    }

    @Override
    public Boolean hasSelfDeclRecord(String groupId) {
        return applicationClient.hasSelfDeclRecord(groupId).getEntity();
    }
}
