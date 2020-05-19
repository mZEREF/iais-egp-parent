package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskEmailDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * NotifyUnprocessedTaskBatchjob
 *
 * @author Guyin
 * @date 12/04/2019
 */
@Delegator("NotifyUnprocessedTaskBatchjob")
@Slf4j
public class NotifyUnprocessedTaskBatchjob {
    @Autowired
    private TaskService taskService;
    @Autowired
    private InspEmailService inspEmailService;
    @Autowired
    private EmailClient emailClient;

    private final String EMAILMPLATEID = "A0D4EADD-D61B-EA11-BE7D-000C29F371DC";

    @Autowired
    MsgTemplateClient msgTemplateClient;

    @Autowired
    SystemBeLicClient systemBeLicClient;
    public void doBatchJob(BaseProcessClass bpc) throws IOException, TemplateException{

        log.debug(StringUtil.changeForLog("The NotifyUnprocessedTaskBatchjob is  start..." ));


        log.debug(StringUtil.changeForLog("Unprocessed Task Notification to Officer..." ));
        Map<String, List<TaskEmailDto>> emailmap = IaisCommonUtils.genNewHashMap();
        //get officer groupleader admin to notify
        emailmap = taskService.getEmailNotifyList();
        //get email template
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(EMAILMPLATEID);


        List<TaskEmailDto> officerDtoList= emailmap.get("officer");
        List<TaskEmailDto> workgroupLeaderDtoList= emailmap.get("workgroup");
        List<TaskEmailDto> adminDtoList= emailmap.get("admin");

        String templateHtml = inspectionEmailTemplateDto.getMessageContent();

        if(officerDtoList != null) {
            for (TaskEmailDto item : officerDtoList
            ) {
                List<String> emailaddr = IaisCommonUtils.genNewArrayList();
                emailaddr.add(item.getEmailAddr());
                sendEmail(item,templateHtml,emailaddr," MOH IAIS – Unprocessed Task Notification to Officer");
            }
        }

        if (workgroupLeaderDtoList != null){
            List<String> groupIdList = IaisCommonUtils.genNewArrayList();
            for (TaskEmailDto item: workgroupLeaderDtoList
            ) {
                if(!groupIdList.contains(item.getWkGrpId())){
                    groupIdList.add(item.getWkGrpId());
                }
            }
            Map<String, List<String>> emailList= taskService.getAllWorkGroupMembers(groupIdList);
            for (TaskEmailDto item: workgroupLeaderDtoList
            ) {
                 sendEmail(item,templateHtml,emailList.get(item.getWkGrpId())," MOH IAIS – Unprocessed Task Notification to Supervisors Workgroup");
            }
        }

        if(adminDtoList != null){
            for (TaskEmailDto item: adminDtoList
            ) {
                List<String> adminEmailAddr = IaisCommonUtils.genNewArrayList();
                adminEmailAddr.add(item.getEmailAddr());
                sendEmail(item,templateHtml,adminEmailAddr," MOH IAIS – Unprocessed Task Notification to the System’s User Administrator");
            }
        }

        log.debug(StringUtil.changeForLog("Unprocessed Task Notification end..." ));


    }

    private void sendEmail(TaskEmailDto item,String templateHtml,List<String> emailAddr,String subject) throws IOException, TemplateException{
        EmailDto email = new EmailDto();
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        map.put("OFFICER_NAME",item.getName());
        map.put("NC_DETAILS","There is an unprocessed task.");
        map.put("MOH_NAME","MOH");
        map.put("TASK_HREF",item.getId());
        String mesContext = null;
        try {
            mesContext= MsgUtil.getTemplateMessageByContent(templateHtml,map);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(),e);
        }

        email.setReqRefNum(item.getId());
        email.setSubject(subject);
        email.setContent(mesContext);
        email.setSender(AppConsts.MOH_AGENCY_NAME);
        email.setClientQueryCode(item.getId());
        email.setReceipts(emailAddr);
        emailClient.sendNotification(email).getEntity();
    }
}
