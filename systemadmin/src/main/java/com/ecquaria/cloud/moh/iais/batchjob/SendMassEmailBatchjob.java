package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SendMassEmailBatchjob
 *
 * @author Guyin
 * @date 03/03/2020
 */
@Delegator("SendMassEmailBatchjob")
@Slf4j
public class SendMassEmailBatchjob {

    @Autowired
    BlastManagementListService blastManagementListService;

    @Value("${iais.email.sender}")
    private String mailSender;
    private static final String EMAIL = "Email";
    private static final String SMS = "SMS";
    public void start(BaseProcessClass bpc){

    }
    public void doBatchJob(BaseProcessClass bpc) throws IOException, TemplateException{

        log.debug(StringUtil.changeForLog("The SendMassEmailBatchjob is  start..." ));
        //get need send email and sms
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<BlastManagementDto> blastManagementDto = blastManagementListService.getBlastBySendTime(df.format(new Date()));

        //foreach get recipient and send
        for (BlastManagementDto item:blastManagementDto
             ) {
            if(EMAIL.equals(item.getMode())){
                EmailDto email = new EmailDto();
//                List<String> roleEmail = blastManagementListService.getEmailByRoleId(item.get)
                email.setContent(item.getMsgContent());
                email.setSender(mailSender);
                email.setSubject(item.getSubject());
                email.setClientQueryCode(item.getId());
                email.setReceipts(item.getEmailAddress());
                email.setReqRefNum(item.getId());

                if(item.getDocName() != null){
                    Map<String , byte[]> emailMap = IaisCommonUtils.genNewHashMap();
                    emailMap.put(item.getDocName(),item.getFileDate());
                    blastManagementListService.sendEmail(email,emailMap);
                }else{
                    blastManagementListService.sendEmail(email,null);
                }
                if(item.getEmailAddress().size() != 0){
                    //update mass email actual time
                    blastManagementListService.setActual(item.getId());
                }
            }else{
//                sendSMS();
            }

        }

        log.debug(StringUtil.changeForLog("SendMassEmailBatchjob end..." ));
    }


    private FileItem createFileItem(File file, String fieldName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return item;
    }

    private void sendSMS(String msgId,String licenseeId,Map<String, Object> msgInfoMap,String content) throws IOException, TemplateException {
//        String templateMessageByContent = "send sms";
//        SmsDto smsDto = new SmsDto();
//        smsDto.setSender(mailSender);
//        smsDto.setContent(templateMessageByContent);
//        smsDto.setOnlyOfficeHour(true);
//        String refNo = inboxMsgService.getMessageNo();
//        try{
//            List<String> recipts = IaisEGPHelper.getLicenseeMobiles(licenseeId);
//            if (!IaisCommonUtils.isEmpty(recipts)) {
//                blastManagementListService.sendSMS(recipts,smsDto,refNo);
//            }
//        }catch (Exception e){
//            log.error(StringUtil.changeForLog("error"));
//        }
    }
}
