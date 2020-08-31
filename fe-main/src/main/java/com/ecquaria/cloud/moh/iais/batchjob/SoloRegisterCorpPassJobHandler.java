package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.IaisSystemClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * SoloRegisterCorpPassJobHandler
 *
 * @author guyin
 * @date 2020/7/30 16:53
 */
@JobHandler(value="soloRegisterCorpPassJobHandler")
@Component
@Slf4j
public class SoloRegisterCorpPassJobHandler extends IJobHandler {

    public static final String SOLO_REGISTER_MSG1 = "E093B0F7-EBB5-EA11-BE84-000C29F371DC";
    public static final String SOLO_REGISTER_MSG2 = "E193B0F7-EBB5-EA11-BE84-000C29F371DC";
    public static final String SOLO_REGISTER_MSG3 = "E293B0F7-EBB5-EA11-BE84-000C29F371DC";
    public static final String SOLO_REGISTER_SMS1 = "AFBAC5CE-A7E6-EA11-BE86-000C29F371DC";
    public static final String SOLO_REGISTER_SMS2 = "18EF1C85-2CEB-EA11-8B79-000C293F0C99";
    public static final String SOLO_REGISTER_SMS3 = "5367961D-2DEB-EA11-8B79-000C293F0C99";
    @Autowired
    private IaisSystemClient systemBeLicClient;

    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    OrgUserManageService orgUserManageService;
    @Override
    public ReturnT<String> execute(String s) {
        try{
            log.info("<====== solo register corPass start======>");
            //get licensee
            List<LicenseeDto> licenseeDtos = orgUserManageService.getLicenseeNoUen();
            for (LicenseeDto item: licenseeDtos
                 ) {
                uenSendNotification(item,SOLO_REGISTER_MSG1,SOLO_REGISTER_SMS1);
                uenSendNotification(item,SOLO_REGISTER_MSG2,SOLO_REGISTER_SMS2);
                uenSendNotification(item,SOLO_REGISTER_MSG3,SOLO_REGISTER_SMS3);
            }
        }catch (Exception e){

            return ReturnT.FAIL;
        }

        return ReturnT.SUCCESS;
    }

    private void uenSendNotification(LicenseeDto licenseeDto,String emailTemp,String smsTemp){
        log.info(StringUtil.changeForLog("send renewal application notification start"));
        //send email
            if(licenseeDto != null){
                String address = IaisEGPHelper.getAddress(licenseeDto.getBlkNo(), licenseeDto.getStreetName(), licenseeDto.getBuildingName(),
                        licenseeDto.getFloorNo(), licenseeDto.getUnitNo(), Integer.toString(licenseeDto.getPostalCode()));
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("HCI_Name", licenseeDto.getName());
                map.put("HCI_Address", address);
                map.put("UEN_No", "");
                map.put("Applicant", licenseeDto.getName());
                map.put("ServiceName", "serviceName");
                map.put("LicenceNo", "LicenceNo");
                map.put("HALP", AppConsts.MOH_AGENCY_NAME);
                map.put("emailAddress", licenseeDto.getEmilAddr());
                map.put("telNo", licenseeDto.getOfficeTelNo());
                //third no need
                map.put("GraceDate", "GraceDate");
                try {
                    String subject = "MOH HALP - Reminder of CorpPass Login";
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(emailTemp);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(licenseeDto.getId());
                    emailParam.setReqRefNum(licenseeDto.getId());
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                    emailParam.setRefId(licenseeDto.getId());
                    emailParam.setSubject(subject);
                    //send email
                    log.info(StringUtil.changeForLog("send uen email"));
                    notificationHelper.sendNotification(emailParam);
                    //send sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(smsTemp);
                    smsParam.setSubject(subject);
                    smsParam.setQueryCode(licenseeDto.getId());
                    smsParam.setReqRefNum(licenseeDto.getId());
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                    smsParam.setRefId(licenseeDto.getId());
                    log.info(StringUtil.changeForLog("send uen sms"));
                    notificationHelper.sendNotification(smsParam);
                    //send message
                    EmailParam messageParam = new EmailParam();
                    messageParam.setTemplateId(emailTemp);
                    messageParam.setTemplateContent(map);
                    messageParam.setQueryCode(licenseeDto.getId());
                    messageParam.setReqRefNum(licenseeDto.getId());
                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    messageParam.setRefId(licenseeDto.getId());
                    messageParam.setSubject(subject);
                    log.info(StringUtil.changeForLog("send uen message"));
                    notificationHelper.sendNotification(messageParam);
                    log.info(StringUtil.changeForLog("send uen end"));
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
        }
    }
}
