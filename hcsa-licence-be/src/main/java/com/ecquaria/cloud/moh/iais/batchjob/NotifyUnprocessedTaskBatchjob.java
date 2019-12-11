package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskEmailDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

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
    public void doBatchJob(BaseProcessClass bpc){

        log.debug(StringUtil.changeForLog("The NotifyUnprocessedTaskBatchjob is  start..." ));

        log.debug(StringUtil.changeForLog("Unprocessed Task Notification to Officer..." ));
        Map<String, Object> emailmap = new HashMap<>();
        //get officer groupleader admin to notify
        emailmap = taskService.getEmailNotifyList();
        //get email template
        String tempalteid = "A0D4EADD-D61B-EA11-BE7D-000C29F371DC";
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(tempalteid);


        List<TaskEmailDto> officerDtoList= (ArrayList<TaskEmailDto>)emailmap.get("officer");
        List<TaskEmailDto> workgroupDtoList= (ArrayList<TaskEmailDto>)emailmap.get("workgroup");
        List<TaskEmailDto> adminDtoList= (ArrayList<TaskEmailDto>)emailmap.get("admin");

        String templateHtml = inspectionEmailTemplateDto.getMessageContent();
        String REQ_REF_NUM = "AAAAA-SSSSSS-XXXXXX";
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

//        List<String> adminEmailAddr = new ArrayList<>();
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

}
