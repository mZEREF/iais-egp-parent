package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Process SendGrioFailEmailForAsoJobHandler
 *
 * @author wangyu
 * @date 2021/2/4
 **/
@JobHandler(value="sendGrioFailEmailForAsoJobHandler")
@Component
@Slf4j
public class SendGrioFailEmailForAsoJobHandler extends IJobHandler {

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private  OrganizationClient organizationClient;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private TaskService taskService;
    @Override
    public ReturnT<String> execute(String s) {
        logAbout("sendGrioFailEmailForAsoJobHandler");
        try{
             AuditTrailHelper.setupBatchJobAuditTrail(this);
            AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
            List<ApplicationGroupDto> applicationGroupDtos = applicationClient.getApplicationGroupByPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL).getEntity();
             if(!IaisCommonUtils.isEmpty(applicationGroupDtos)){
               for(ApplicationGroupDto applicationGroupDto : applicationGroupDtos){
                   if(sendEmailAndSms(applicationGroupDto)){
                       applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL_REMIND_OK);
                       applicationGroupDto.setAuditTrailDto(auditTrailDto);
                       applicationClient.updateApplication(applicationGroupDto);
                       ApplicationDto applicationDto = new ApplicationDto();
                       applicationDto.setAppGrpId(applicationGroupDto.getId());
                       applicationDto.setAuditTrailDto(auditTrailDto);
                       applicationDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL_REMIND_OK);
                       applicationDto.setUpdateGroupForGrio(true);
                       applicationService.updateFEApplicaiton(applicationDto);
                   }
               }
             }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        logAbout("sendGrioFailEmailForAsoJobHandler end ");
        return ReturnT.SUCCESS;
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
        JobLogger.log(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }

    private boolean sendEmailAndSms(ApplicationGroupDto applicationGroupDto){
        try{
            Map<String,Object> emailMap = getParamByAppGroup(applicationGroupDto);
            Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
            subMap.put("ApplicationNumber",emailMap.get("ApplicationNumber"));
            subMap.put("ApplicationType",emailMap.get("ApplicationType"));
            String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_002_EMAIL, subMap);
            String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_002_SMS, subMap);
            log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
            log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
            EmailParam emailParam = new EmailParam();
            String appNo = applicationGroupDto.getGroupNo()+"-01";
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_002_EMAIL);
            emailParam.setTemplateContent(emailMap);
            emailParam.setQueryCode(applicationGroupDto.getGroupNo());
            emailParam.setReqRefNum(applicationGroupDto.getGroupNo());
            emailParam.setRefIdType(NotificationHelper. RECEIPT_TYPE_APP);
            emailParam.setRefId(appNo);
            emailParam.setSubject(emailSubject);
            //send email
            log.info(StringUtil.changeForLog("sendGrioFailEmailForAso"));
            setAsoEmailAndName(appNo,emailParam);
            notificationHelper.sendNotification(emailParam);
            //send sms
            EmailParam  emailParamSms = new EmailParam();
            emailParamSms.setTemplateContent(emailMap);
            emailParamSms.setQueryCode(applicationGroupDto.getGroupNo());
            emailParamSms.setReqRefNum(applicationGroupDto.getGroupNo());
            emailParamSms.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_002_SMS);
            emailParamSms.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            emailParamSms.setRefId(appNo);
            emailParamSms.setSubject(smsSubject);
            notificationHelper.sendNotification(emailParamSms);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return false;
        }
          return true;
    }

    private void setAsoEmailAndName(String appNo, EmailParam emailParam){
        List<TaskDto> taskDtos = taskService.getTaskbyApplicationNo(appNo);
        if(IaisCommonUtils.isNotEmpty(taskDtos)){
            for(TaskDto taskDto : taskDtos){
                if(RoleConsts.USER_ROLE_ASO.equalsIgnoreCase(taskDto.getRoleId())){
                    if(StringUtil.isNotEmpty(taskDto.getUserId())){
                        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(taskDto.getUserId()).getEntity();
                        if(orgUserDto != null && StringUtil.isNotEmpty(orgUserDto.getEmail())){
                            emailParam.setRecipientEmail(orgUserDto.getEmail());
                            emailParam.setRecipientName(orgUserDto.getDisplayName());
                            return;
                        }
                    }
                }
            }
        }

    }

    private String getEmailSubject(String templateId, Map<String, Object> subMap) {
        if (!StringUtil.isEmpty(templateId)) {
            MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(templateId).getEntity();
            if (emailTemplateDto != null) {
                try {
                    if (!IaisCommonUtils.isEmpty(subMap)) {
                        return MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(), subMap);
                    } else {
                        return emailTemplateDto.getTemplateName();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return "";
    }
    private Map<String,Object> getParamByAppGroup(ApplicationGroupDto applicationGroupDto){
        Map<String,Object> param =   IaisCommonUtils.genNewHashMap();
        OrganizationLicDto organizationLicDto = organizationClient.getOrganizationLicDtoByLicenseeId(applicationGroupDto.getLicenseeId()).getEntity();
        LicenseeDto licenseeDto = organizationLicDto.getLicenseeDto();
        LicenseeEntityDto licenseeEntityDto = organizationLicDto.getLicenseeEntityDto();
        String syName = AppConsts.MOH_AGENCY_NAME;
        Date date = new Date();
        String appDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationGroupDto.getAppType());
        param.put("officer_name","");
        param.put("ApplicationNumber", applicationGroupDto.getGroupNo());
        param.put("ApplicationType", applicationTypeShow);
        param.put("ApplicationDate", appDate);
        param.put("licenseeName", licenseeDto.getName());
        param.put("licenseeContactNum",getStringForNullToEmptyString(licenseeEntityDto.getOfficeTelNo()) + '/' + getStringForNullToEmptyString(licenseeEntityDto.getOfficeEmailAddr()));
        param.put("MOH_AGENCY_NAME", syName);
        return param;
    }

    private String getStringForNullToEmptyString(String string){
        return StringUtil.isEmpty(string) ? "" : string;
    }
}
