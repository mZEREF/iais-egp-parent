package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
            EmailDto email = new EmailDto();

            email.setContent(item.getMsgContent());
            email.setSender("system@wangyi.com");
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

            //update mass email actual time
            blastManagementListService.setActual(item.getId());
        }

        log.debug(StringUtil.changeForLog("SendMassEmailBatchjob end..." ));
    }


    private FileItem createFileItem(File file, String fieldName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }
}
