package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ConfigExcelItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.ChecklistConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yi chen
 * @Date:2020/6/9
 **/

@Slf4j
public final class ChecklistHelper {
    private ChecklistHelper(){}

    public static boolean validateFile(HttpServletRequest request, MultipartFile file){
        if (file == null || file.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "GENERAL_ERR0020"));
            return true;
        }

        String originalFileName = file.getOriginalFilename();
        if (!FileUtils.isExcel(originalFileName)){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "GENERAL_ERR0005"));
            return true;
        }

        if (FileUtils.outFileSize(file.getSize())){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "GENERAL_ERR0004"));
            return true;
        }

        return false;
    }

    public static boolean validateTemplate(HttpServletRequest request, ChecklistConfigDto excelTemplate){
        int action = excelTemplate.getWebAction();
        String configId = excelTemplate.getId();
        boolean isCommon = excelTemplate.isCommon();
        String type = excelTemplate.getType();
        String module = excelTemplate.getModule();
        String service = excelTemplate.getSvcName();

        if (HcsaChecklistConstants.UPDATE == action){
            if (StringUtils.isEmpty(configId)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR011"));
                return true;
            }
        }

        if (!isCommon){
            if (StringUtil.isEmpty(module)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, MessageUtil.replaceMessage("GENERAL_ERR0006", "Module", "field")));
                return true;
            }

            if (StringUtil.isEmpty(type)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, MessageUtil.replaceMessage("GENERAL_ERR0006", "Type", "field")));
                return true;
            }

            if (StringUtil.isEmpty(service)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, MessageUtil.replaceMessage("GENERAL_ERR0006", "Service", "field")));
                return true;
            }
        }

        String effectiveStartDate = excelTemplate.getEftStartDate();
        String effectiveEndDate = excelTemplate.getEftEndDate();

        if (StringUtils.isEmpty(effectiveStartDate) || StringUtils.isEmpty(effectiveEndDate)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR014"));
            return true;
        }else {
            try {
                Date sDate = IaisEGPHelper.parseToDate(effectiveStartDate);
                Date eDate = IaisEGPHelper.parseToDate(effectiveEndDate);

                if (IaisEGPHelper.isAfterDate(eDate, sDate)){
                    ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR013"));
                    return true;
                }

            }catch (IaisRuntimeException e){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR014"));
                return true;
            }
        }

        List<ConfigExcelItemDto> allItem = excelTemplate.getExcelTemplate();

        if (IaisCommonUtils.isEmpty(allItem)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR017"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return true;
        }

        return false;
    }

    public static void replaceErrorMsgContentMasterCode(List<ErrorMsgContent> errorMsgContentList) {
        for (ErrorMsgContent errorMsgContent : errorMsgContentList){
            int idx = 0;
            for(String error : errorMsgContent.getErrorMsgList()){
                String msg = MessageUtil.getMessageDesc(error);
                errorMsgContent.getErrorMsgList().set(idx++, msg);
            }
        }
    }

    public static void sendModifiedChecklistEmailToAOStage(String serviceId, String applicationType, String mailSender){
        EmailHelper emailHelper = SpringContextHelper.getContext().getBean(EmailHelper.class);
        EmailClient emailClient = SpringContextHelper.getContext().getBean(EmailClient.class);
        HcsaConfigClient hcsaConfigClient = SpringContextHelper.getContext().getBean(HcsaConfigClient.class);
        OrganizationClient organizationClient = SpringContextHelper.getContext().getBean(OrganizationClient.class);

        if (emailHelper == null || emailClient == null || hcsaConfigClient == null || organizationClient == null){
            log.info("can not find context bean");
            return;
        }

        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
        hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDto.setOrder(2);
        hcsaSvcStageWorkingGroupDto.setType(applicationType);
        HcsaSvcStageWorkingGroupDto dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();

        if (dto != null){
            List<OrgUserDto> orgUserDtos = organizationClient.getUsersByWorkGroupName(dto.getId(), AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            if (!IaisCommonUtils.isEmpty(orgUserDtos)){
                List<String> receiveEmail = orgUserDtos.stream().map(OrgUserDto::getEmail).collect(Collectors.toList());

                log.info("=====address====>>>", receiveEmail);

                try {
                    Map<String,Object> map = new HashMap(1);
                    map.put("MOH_NAME", AppConsts.MOH_AGENCY_NAME);

                    MsgTemplateDto msgTemplate = emailHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_INSPECTOR_MODIFIED_CHECKLIST);
                    String subject = msgTemplate.getTemplateName();
                    String messageContent = msgTemplate.getMessageContent();
                    String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);

                    EmailDto emailDto=new EmailDto();
                    emailDto.setContent(templateMessageByContent);
                    emailDto.setSubject(subject);
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(receiveEmail);
                    emailClient.sendNotification(emailDto).getEntity();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                } catch (TemplateException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
