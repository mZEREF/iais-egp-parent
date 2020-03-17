package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskEmailDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    private TaskService taskService;
    private InspEmailService inspEmailService;

    private final String EMAILMPLATEID = "A0D4EADD-D61B-EA11-BE7D-000C29F371DC";
    String REQ_REF_NUM = "AAAAA-SSSSSS-XXXXXX";

    @Autowired
    MsgTemplateClient msgTemplateClient;

    @Autowired
    SystemBeLicClient systemBeLicClient;
    public void doBatchJob(BaseProcessClass bpc) throws IOException, TemplateException{

        log.debug(StringUtil.changeForLog("The NotifyUnprocessedTaskBatchjob is  start..." ));

        log.debug(StringUtil.changeForLog("Unprocessed Task Notification to Officer..." ));
        Map<String, Object> emailmap = new HashMap<>();
        //get officer groupleader admin to notify
        emailmap = taskService.getEmailNotifyList();
        //get email template
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(EMAILMPLATEID);


        List<TaskEmailDto> officerDtoList= (ArrayList<TaskEmailDto>)emailmap.get("officer");
        List<TaskEmailDto> workgroupDtoList= (ArrayList<TaskEmailDto>)emailmap.get("workgroup");
        List<TaskEmailDto> adminDtoList= (ArrayList<TaskEmailDto>)emailmap.get("admin");

        String templateHtml = inspectionEmailTemplateDto.getMessageContent();
        List<String> receipts = new ArrayList<String>();
        EmailDto email = new EmailDto();
        for (TaskEmailDto item: officerDtoList
        ) {
            //todo jugde email sent
            receipts.clear();
//            templateHtml.replace("OFFICER_NAME",item.getName());
            email.setReqRefNum(REQ_REF_NUM);
            receipts.add(item.getEmailAddr());
            email.setContent(templateHtml);
            email.setReceipts(receipts);
            email.setSender("guyin@ecquaria.com");
            email.setClientQueryCode("FFFF=EEEEEE");
            inspEmailService.SendAndSaveEmail(email);
        }

        for (TaskEmailDto item: workgroupDtoList
        ) {
            //todo jugde email sent
            receipts.clear();
//            templateHtml.replace("OFFICER_NAME",item.getName());
            email.setReqRefNum(REQ_REF_NUM);
            receipts.add(item.getEmailAddr());
            email.setContent(templateHtml);
            email.setReceipts(receipts);
            email.setSender("guyin@ecquaria.com");
            email.setClientQueryCode("FFFF=EEEEEE");
            inspEmailService.SendAndSaveEmail(email);
        }

//        List<String> adminEmailAddr = IaisCommonUtils.genNewArrayList();
//        for (TaskEmailDto item: adminDtoList
//        ) {
//            //todo jugde email sent
//            receipts.clear();
//            templateHtml.replace("OFFICER_NAME",item.getName());
//            email.setReqRefNum(REQ_REF_NUM);
//            for (String adminemail:adminEmailAddr
//                 ) {
//                receipts.add(adminemail);
//            }
//            email.setContent(templateHtml);
//            email.setReceipts(receipts);
//            email.setSender("guyin@ecquaria.com");
//            email.setClientQueryCode("FFFF=EEEEEE");
//            inspEmailService.SendAndSaveEmail(email);
//        }




    }

    private void sendEmail(TaskEmailDto item,String templateHtml) throws IOException, TemplateException{
        EmailDto email = new EmailDto();
        Map<String,Object> map=new HashMap<>();
        map.put("OFFICER_NAME",item.getName());
        map.put("NC_DETAILS","There is an unprocessed task.");
        map.put("MOH_NAME","MOH");
        String mesContext= MsgUtil.getTemplateMessageByContent(templateHtml,map);
        email.setReqRefNum(REQ_REF_NUM);
        email.setContent(mesContext);
        email.setSender("guyin@ecquaria.com");
        email.setClientQueryCode("FFFF=EEEEEE");
        inspEmailService.SendAndSaveEmail(email);
    }
}
