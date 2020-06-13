package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.client.EicClient;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.EmailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ApplicationServiceImpl
 *
 * @author suocheng
 * @date 11/28/2019
 */
@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private EmailHelper emailHelper;

    @Autowired
    private EicClient eicClient;

    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Override
    public List<ApplicationDto> getApplicaitonsByAppGroupId(String appGroupId) {
        return applicationClient.getGroupAppsByNo(appGroupId).getEntity();
    }

    @Override
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,String appNo,String status) {
        if(IaisCommonUtils.isEmpty(applicationDtoList)|| StringUtil.isEmpty(appNo) || StringUtil.isEmpty(status)){
            return  false;
        }
        boolean result = true;
        Map<String,List<ApplicationDto>> applicationMap = tidyApplicationDto(applicationDtoList);
        if(applicationMap!=null && applicationMap.size()>0){
          for (Map.Entry<String,List<ApplicationDto>> entry : applicationMap.entrySet()){
              String key = entry.getKey();
              List<ApplicationDto> value = entry.getValue();
              if(appNo.equals(key)){
                  continue;
              }else if(!containStatus(value,status)){
                  result = false;
                  break;
              }

          }
          for(ApplicationDto applicationDto : applicationDtoList){
              applicationDto.setStatus(status);
          }
        }
        return result;
    }

    @Override
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId) {
        return appPremisesCorrClient.getGroupAppsByNo(appGroupId).getEntity();
    }

    @Override
    public Integer getAppBYGroupIdAndStatus(String appGroupId, String status) {
        return applicationClient.getAppCountByGroupIdAndStatus(appGroupId,status).getEntity();
    }


    public ApplicationDto callEicInterApplication(ApplicationDto applicationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return beEicGatewayClient.updateApplication(applicationDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
    }


    @Override
    public ApplicationDto updateFEApplicaiton(ApplicationDto applicationDto) {
        log.info(StringUtil.changeForLog("The updateFEApplicaiton start ..."));
        String moduleName = currentApp + "-" + currentDomain;
        EicRequestTrackingDto dto = new EicRequestTrackingDto();
        dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dto.setActionClsName(this.getClass().getName());
        dto.setActionMethod("callEicInterApplication");
        dto.setDtoClsName(applicationDto.getClass().getName());
        dto.setDtoObject(JsonUtil.parseToJson(applicationDto));
        String refNo = String.valueOf(System.currentTimeMillis());
        log.info(StringUtil.changeForLog("The updateFEApplicaiton refNo is  -- >:"+refNo));
        dto.setRefNo(refNo);
        dto.setModuleName(moduleName);
        eicClient.saveEicTrack(dto);
        callEicInterApplication(applicationDto);
        dto = eicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        Date now = new Date();
        dto.setProcessNum(1);
        dto.setFirstActionAt(now);
        dto.setLastActionAt(now);
        dto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        List<EicRequestTrackingDto> list = IaisCommonUtils.genNewArrayList(1);
        list.add(dto);
        eicClient.updateStatus(list);
        log.info(StringUtil.changeForLog("The updateFEApplicaiton end ..."));
        return applicationDto;
    }

    @Override
    public List<RequestInformationSubmitDto> getRequestInformationSubmitDtos(List<ApplicationDto> applicationDtos) {
        return  applicationClient.getRequestInformationSubmitDto(applicationDtos).getEntity();
    }

    @Override
    public List<AppEditSelectDto> getAppEditSelectDtos(String appId, String changeType) {
        return applicationClient.getAppEditSelectDto(appId, changeType).getEntity();
    }

    @Override
    public void postInspectionBack() {
        String appType = ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION;
        String appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING;
        List<ApplicationDto> postApps = applicationClient.getPostApplication(appType, appStatus).getEntity();
        //ApplicationDto applicationDto, String statgId,String roleId,String correlationId
    }

    @Override
    public void alertSelfDeclNotification() {
        log.info("===>>>>alertSelfDeclNotification start");
        List<ApplicationGroupDto> userAccountList = applicationClient.getUserAccountByNotSubmittedSelfDecl().getEntity();
        try {
            Map<String,Object> map = new HashMap(1);
            map.put("APPLICANT_NAME", StringUtil.viewHtml("Yi chen"));
            map.put("DETAILS", StringUtil.viewHtml("test"));
            map.put("A_HREF", StringUtil.viewHtml(""));
            map.put("MOH_NAME", AppConsts.MOH_AGENCY_NAME);
            MsgTemplateDto entity = emailHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_SELF_DECL_ID);
            String messageContent = entity.getMessageContent();
            String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);

            List<String> licenseeIdList =  userAccountList.stream().map(ApplicationGroupDto::getLicenseeId).collect(Collectors.toList());
            List<String> emailAddress = emailHelper.getEmailAddressListByLicenseeId(licenseeIdList);

            EmailDto emailDto = new EmailDto();
            emailDto.setContent(templateMessageByContent);
            emailDto.setSubject("Self-Assessment submission for Application Number");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(emailAddress);

            emailClient.sendNotification(emailDto);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (TemplateException e) {
            log.error(e.getMessage());
        }

        log.info("===>>>>alertSelfDeclNotification end");
    }

    @Override
    public void applicationRfiAndEmail(ApplicationViewDto applicationViewDto, ApplicationDto applicationDto, String licenseeId, LicenseeDto licenseeDto,
                                       LoginContext loginContext, String externalRemarks) throws IOException, TemplateException {
        //send message to FE user.
        String messageNo = inboxMsgService.getMessageNo();
        String url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+ MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION+applicationDto.getApplicationNo();
        String editSelect = "";
        //judge premises amend or licence amend
        AppEditSelectDto appEditSelectDto = applicationViewDto.getAppEditSelectDto();
        //Request For Change
        boolean rfcFlag = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType());
        if((appEditSelectDto != null) && rfcFlag){
            if(appEditSelectDto.isPremisesListEdit()){
                url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+MessageConstants.MESSAGE_CALL_BACK_URL_PREMISES_LIST+applicationDto.getApplicationNo();
            }
        }
        //0065135
        if(appEditSelectDto != null){
            if(appEditSelectDto.isPremisesEdit()){
                editSelect = editSelect + "Premises";
            }
            if(appEditSelectDto.isDocEdit()){
                editSelect = editSelect +(StringUtil.isEmpty(editSelect)?"":",") +"Primary Documents";
            }
            if(appEditSelectDto.isServiceEdit()){
                editSelect = editSelect + "Service Related Information - " + applicationViewDto.getServiceType();
            }
            if(appEditSelectDto.isPoEdit()){
                editSelect = editSelect + "PO";
            }
            if(appEditSelectDto.isDpoEdit()){
                editSelect = editSelect + "DPO";
            }
            if(appEditSelectDto.isMedAlertEdit()){
                editSelect = editSelect + "medAlert";
            }
        }
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        MsgTemplateDto autoEntity = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_RFI).getEntity();
        Map<String ,Object> map=IaisCommonUtils.genNewHashMap();
        map.put("APPLICANT_NAME",licenseeDto.getName());
        map.put("APPLICATION_NUMBER",StringUtil.viewHtml(applicationNo));
        map.put("DETAILS","");
        map.put("COMMENTS",StringUtil.viewHtml(externalRemarks));
        map.put("EDITSELECT",editSelect);
        map.put("A_HREF",url);
        map.put("MOH_NAME",AppConsts.MOH_AGENCY_NAME);
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(autoEntity.getMessageContent(), map);

        InterMessageDto interMessageDto = MessageTemplateUtil.getInterMessageDto(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION+applicationNo,MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED,
                messageNo,applicationDto.getServiceId(),templateMessageByContent, applicationViewDto.getApplicationGroupDto().getLicenseeId(),IaisEGPHelper.getCurrentAuditTrailDto());
        HashMap<String,String> mapParam = IaisCommonUtils.genNewHashMap();
        mapParam.put("appNo",applicationDto.getApplicationNo());
        interMessageDto.setMaskParams(mapParam);
        inboxMsgService.saveInterMessage(interMessageDto);
        //new application send email
        String applicationType = applicationDto.getApplicationType();
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
            MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVAL_OFFICE_ROUTES_ID).getEntity();
            if(msgTemplateDto != null){

                String username = licenseeDto.getName();
                String approvalOfficerName = loginContext.getUserName();

                Map<String ,Object> tempMap = IaisCommonUtils.genNewHashMap();
                tempMap.put("userName",StringUtil.viewHtml(username));
                tempMap.put("applicationNumber",StringUtil.viewHtml(applicationNo));
                tempMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
                tempMap.put("approvalOfficerName",StringUtil.viewHtml(approvalOfficerName));

                String mesContext = null;
                mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);

                EmailDto emailDto = new EmailDto();
                emailDto.setContent(mesContext);
                emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + applicationNo);
                emailDto.setSender(mailSender);
                emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
                //send
                emailClient.sendNotification(emailDto).getEntity();
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
            Map<String ,Object> tempMap = IaisCommonUtils.genNewHashMap();
            String username = licenseeDto.getName();
            String appGrpId = applicationViewDto.getApplicationDto().getAppGrpId();
            String approvalOfficerName = loginContext.getUserName();
            tempMap.put("userName",StringUtil.viewHtml(username));
            tempMap.put("applicationNumber",StringUtil.viewHtml(applicationNo));
            tempMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
            tempMap.put("approvalOfficerName",StringUtil.viewHtml(approvalOfficerName));
            String subject = " " + applicationNo;
            sendEmailHelper(tempMap,MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_INSPECTION_IS_IDENTIFIED,subject,licenseeId,appGrpId);
        }
    }

    //send email helper
    private String sendEmailHelper(Map<String ,Object> tempMap,String msgTemplateId,String subject,String licenseeId,String clientQueryCode){
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgTemplateId).getEntity();
        if(tempMap == null || tempMap.isEmpty() || msgTemplateDto == null
                || StringUtil.isEmpty(msgTemplateId)
                || StringUtil.isEmpty(subject)
                || StringUtil.isEmpty(licenseeId)
                || StringUtil.isEmpty(clientQueryCode)){
            return null;
        }
        String mesContext = null;
        try {
            mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(),e);
        }
        EmailDto emailDto = new EmailDto();
        emailDto.setContent(mesContext);
        emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + subject);
        emailDto.setSender(mailSender);
        emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
        emailDto.setClientQueryCode(clientQueryCode);
        //send
        emailClient.sendNotification(emailDto).getEntity();

        return mesContext;
    }

    private boolean containStatus(List<ApplicationDto> applicationDtos,String status){
        boolean result = false;
        if(!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(status)){
            for(ApplicationDto applicationDto : applicationDtos){
                if(status.equals(applicationDto.getStatus())
                        || ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(applicationDto.getStatus())
                        || ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(applicationDto.getStatus())
                        || ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(applicationDto.getStatus())){
                    result = true;
                    break;
                }
            }
        }
        return  result;
    }

    private Map<String,List<ApplicationDto>> tidyApplicationDto(List<ApplicationDto> applicationDtoList){
        Map<String,List<ApplicationDto>> result = null;
        if(!IaisCommonUtils.isEmpty(applicationDtoList)){
            result = IaisCommonUtils.genNewHashMap();
            for(ApplicationDto applicationDto : applicationDtoList){
                String appNo = applicationDto.getApplicationNo();
                List<ApplicationDto> applicationDtos = result.get(appNo);
                if(applicationDtos ==null){
                    applicationDtos = IaisCommonUtils.genNewArrayList();
                }
                applicationDtos.add(applicationDto);
                result.put(appNo,applicationDtos);
            }
        }
        return result;
    }

    @Override
    public ApplicationDto getApplicationBytaskId(String ref){
        return applicationClient.getApplicationBytaskId(ref).getEntity();
    }

    @Override
    public AppFeeDetailsDto getAppFeeDetailsDtoByApplicationNo(String applicationNo) {
        return applicationClient.getAppFeeDetailsDtoByApplicationNo(applicationNo).getEntity();
    }

    @Override
    public AppReturnFeeDto saveAppReturnFee(AppReturnFeeDto appReturnFeeDto) {
        return applicationClient.saveAppReturnFee(appReturnFeeDto).getEntity();
    }

    @Override
    public ApplicationDto getApplicationDtoByGroupIdAndStatus(String appGroupId, String status) {
        log.info(StringUtil.changeForLog("The containStatus is start ..."));
        log.info(StringUtil.changeForLog("The containStatus appGrpId is -->:"+appGroupId));
        log.info(StringUtil.changeForLog("The containStatus status is -->:"+status));
        ApplicationDto result = null;
        if(!StringUtil.isEmpty(appGroupId)){
            List<ApplicationDto> applicationDtoList = getApplicaitonsByAppGroupId(appGroupId);
            if(!IaisCommonUtils.isEmpty(applicationDtoList)){
                log.info(StringUtil.changeForLog("The containStatus applicationDtoList.size() is -->:"+applicationDtoList.size()));
                for (ApplicationDto applicationDto : applicationDtoList){
                    if(status.equals(applicationDto.getStatus())){
                        log.info(StringUtil.changeForLog("The containStatus had approved ApplicationNo is -->:"+applicationDto.getApplicationNo()));
                        result = applicationDto;
                        break;
                    }
                }
            }
        }

        //log.info(StringUtil.changeForLog("The containStatus result is -->:" + result));
        log.info(StringUtil.changeForLog("The containStatus is end ..."));
        return result;
    }
}
