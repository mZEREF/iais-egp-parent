package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.AttachmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import com.ecquaria.cloud.moh.iais.service.SysInboxMsgService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
@JobHandler(value="SendMassEmailJobHandler")
@Component
@Slf4j
public class SendMassEmailJobHandler extends IJobHandler {

    @Autowired
    BlastManagementListService blastManagementListService;

    @Autowired
    DistributionListService distributionListService;
    @Autowired
    SysInboxMsgService sysInboxMsgService;

    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    private static final String EMAIL = "Email";
    private static final String SMS = "SMS";

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

    private void sendSMS(String msgId,List<String> recipts,String content){
        try{
            SmsDto smsDto = new SmsDto();
            smsDto.setSender(mailSender);
            smsDto.setContent(content);
            smsDto.setOnlyOfficeHour(true);
            String refNo = msgId;
            if (!IaisCommonUtils.isEmpty(recipts)) {
                blastManagementListService.sendSMS(recipts,smsDto,refNo);
            }
        }catch (Exception e){
            log.error(StringUtil.changeForLog("error"));
        }
    }

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info(StringUtil.changeForLog("The SendMassEmailBatchjob is start..." ));
        //get need send email and sms
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<BlastManagementDto> blastManagementDto = blastManagementListService.getBlastBySendTime(df.format(new Date()));
        log.info(StringUtil.changeForLog("blastManagementDto count" + blastManagementDto.size() ));
        //foreach get recipient and send
        int i = 0;
        for (BlastManagementDto item:blastManagementDto
        ) {
            log.info(StringUtil.changeForLog("No." + i ));
            log.info(StringUtil.changeForLog("mode is " + item.getMode()));
            log.info(StringUtil.changeForLog("Id is " + item.getId()));
            if(EMAIL.equals(item.getMode()) && item.getRecipientsRole() != null){
                EmailDto email = new EmailDto();
                List<String> roleEmail = blastManagementListService.getEmailByRole(item.getRecipientsRole());
                email.setContent(item.getMsgContent());
                email.setSender(mailSender);
                email.setSubject(item.getSubject());
                log.info(StringUtil.changeForLog("subject is " + item.getSubject()));

                List<String> allemail = IaisCommonUtils.genNewArrayList();
                if(!IaisCommonUtils.isEmpty(roleEmail)){
                    allemail.addAll(roleEmail);
                }
                if(!IaisCommonUtils.isEmpty(item.getEmailAddress())){
                    allemail.addAll(item.getEmailAddress());
                }

                email.setReceipts(allemail);
                email.setReqRefNum(item.getId());
                email.setClientQueryCode(item.getMessageId());
                log.info(StringUtil.changeForLog("ClientQueryCode is " + item.getMessageId()));
                try{
                    if(item.getAttachmentDtos() != null){
                        Map<String , byte[]> emailMap = IaisCommonUtils.genNewHashMap();
                        for (AttachmentDto att: item.getAttachmentDtos()
                        ) {
                            emailMap.put(att.getDocName(),att.getData());
                        }
                        blastManagementListService.sendEmail(email,emailMap);
                    }else{
                        blastManagementListService.sendEmail(email,null);
                    }
                    if(item.getId() != null){
                        //update mass email actual time
                        blastManagementListService.setActual(item.getId());
                    }
                }catch (Exception e){
                    log.info(StringUtil.changeForLog("Result is fail"));
                    return ReturnT.FAIL;
                }
            }else if(item.getRecipientsRole() != null){
                List<String> mobile = blastManagementListService.getMobileByRole(item.getRecipientsRole());
                sendSMS(item.getMessageId(), mobile,item.getMsgContent());
            }

            //send inbox msg
            InterMessageDto interMessageDto = new InterMessageDto();
            if(!StringUtil.isEmpty(item.getDistributionId())){
                DistributionListWebDto dis = distributionListService.getDistributionListById(item.getDistributionId());
                List<HcsaServiceDto> hcsaServiceDtoList = distributionListService.getServicesInActive();
                String serviceName = "";
                HcsaServiceDto svcDto = null;
                for (HcsaServiceDto serviceDto:hcsaServiceDtoList) {
                    if(serviceDto.getSvcCode().equals(dis.getService())){
                        svcDto = serviceDto;
                        break;
                    }
                }

                if( svcDto == null){
                    svcDto = new HcsaServiceDto();
                }
                List<LicenceDto> licenceList = hcsaLicenceClient.getLicenceDtosBySvcName(serviceName).getEntity();
                //send message to FE user.
                interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
                interMessageDto.setSubject(item.getSubject());
                log.info(StringUtil.changeForLog("interMessage subject is " + item.getSubject()));
                interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
                interMessageDto.setRefNo(item.getMessageId());
                interMessageDto.setService_id(svcDto.getSvcCode()+'@');
                log.info(StringUtil.changeForLog("interMessage ServiceId is " + svcDto.getSvcCode()+'@'));
                interMessageDto.setMsgContent(item.getMsgContent());
                interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
                interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                for (LicenceDto licencedto:licenceList
                     ) {
                    interMessageDto.setUserId(licencedto.getLicenseeId());
                    sysInboxMsgService.saveInterMessage(interMessageDto);
                }
            }

        }
        log.info(StringUtil.changeForLog("Result is success"));
        return ReturnT.SUCCESS;
    }
}
