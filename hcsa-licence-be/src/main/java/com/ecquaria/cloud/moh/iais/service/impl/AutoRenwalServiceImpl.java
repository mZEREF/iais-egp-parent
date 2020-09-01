package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AutoRenwalService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/12/26 10:08
 */
@Service
@Slf4j
public class AutoRenwalServiceImpl implements AutoRenwalService {

    @Autowired
    private HcsaLicenceClient hcsaLicenClient;
    @Autowired
    private EmailClient emailClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private InboxMsgService inboxMsgService;
    @Autowired
    private SystemBeLicClient systemBeLicClient;
    private SimpleDateFormat simpleDateFormat =new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
    @Value("${iais.email.sender}")
    private String mailSender;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private NotificationHelper notificationHelper;

    private static final String EMAIL_SUBJECT="MOH IAIS – REMINDER TO RENEW LICENCE";
    private static final String EMAIL_TO_OFFICER_SUBJECT="MOH IAIS – Licence is due to expiry";
    @Override
    public void startRenwal(HttpServletRequest request) {
        List<Integer> dayList= IaisCommonUtils.genNewArrayList();
        dayList.add(-1);
        dayList.add(systemParamConfig.getSeventhLicenceReminder());
        dayList.add(systemParamConfig.getSixthLicenceReminder());
        dayList.add(systemParamConfig.getFifthLicenceReminder());
        dayList.add(systemParamConfig.getFourthLicenceReminder());
        dayList.add(systemParamConfig.getThirdLicenceReminder());
        dayList.add(systemParamConfig.getSecondLicenceReminder());
        dayList.add(systemParamConfig.getLicenceIsEligible());
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(dayList)+"dayList"));
        Map<String, List<LicenceDto>> entity = hcsaLicenClient.licenceRenwal(dayList).getEntity();
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(entity+"------entity")));
        entity.forEach((k, v) -> {
            for(int i=0;i<v.size();i++){
                String licenceNo = v.get(i).getLicenceNo();

                boolean autoOrNon = isAutoOrNon(licenceNo);

                if(autoOrNon){

                    try {
                        isAuto(v.get(i),k);

                    } catch (Exception e) {
                        log.info(e.getMessage(),e);
                    }
                }else {

                    try {
                        isNoAuto(v.get(i),k);
                    } catch (Exception e) {
                        log.error(e.getMessage(),e);
                    }

                }
                if ("-1".equals(k)){
                    try {
                        sendEmailToOffice(v.get(i),request);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    } catch (TemplateException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                if ("30".equals(k)){
                    try {
                        sendEmailToOffice(v.get(i),request);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    } catch (TemplateException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        });

    }




    /****************/
/*
* use organization id
* */
    private OrganizationDto getOrganizationBy(String id){

        return  organizationClient.getOrganizationById(id).getEntity();

    }


    /*
    * if true is auto
    * else non auto
    * */
    private boolean isAutoOrNon(String licenceId){


        return false;
    }
/*
*  is sended to user or orgainization
*
* */
    private void licenceToRemove(List<LicenceDto>  applicationDtos ,  List<JobRemindMsgTrackingDto> JobRemindMsgTrackingDto ){
        if(applicationDtos!=null){
            for(int i=0;i< applicationDtos.size();i++){
                String originLicenceId = applicationDtos.get(i).getOriginLicenceId();
                for(JobRemindMsgTrackingDto e:JobRemindMsgTrackingDto){
                    String refNo = e.getRefNo();
                    if(originLicenceId.equals(refNo)){
                        applicationDtos.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }

    }


    /*
    * non auto to send
    *
    * */
    private void  isNoAuto(LicenceDto licenceDto ,String time) throws IOException, TemplateException {

        String svcName = licenceDto.getSvcName();

        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenceDto.getLicenseeId());
        log.info(StringUtil.changeForLog(licenseeEmailAddrs.toString()+"----------"));
        log.info(StringUtil.changeForLog(licenceDto.getLicenseeId()+"licenseeId"));
        Date expiryDate = licenceDto.getExpiryDate();

        String id = licenceDto.getId();
        log.info(StringUtil.changeForLog(time+"time"));
        log.info(id);
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(licenceDto)));
        boolean b = checkEmailIsSend(id, "IS_NO_AUTO" + time);
        String lastReminderString = systemParamConfig.getSeventhLicenceReminder()+"";
        log.info(StringUtil.changeForLog(b+"-------type"));
        if(!b){
            return;
        }
        String serviceName = licenceDto.getSvcName();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenceDto.getLicenseeId()).getEntity();
        if(licenseeDto != null){
            String licenceId = licenceDto.getId();
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
            String applicationName = licenseeDto.getName();
            String MohName = AppConsts.MOH_AGENCY_NAME;
            log.info(StringUtil.changeForLog("send renewal application notification applicantName : " + applicationName));
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("ApplicantName", applicationName);
            map.put("MOH_AGENCY_NAME", MohName);
            map.put("serviceName", serviceName);
            map.put("systemLink", loginUrl);
            map.put("email", systemAddressOne);
            if(lastReminderString.equals(time)){
                //last
                log.info(StringUtil.changeForLog("send renewal application last reminder"));
                String subject = "MOH HALP - Final Reminder: Your " + serviceName + " is due for renewal";
                try {
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_LAST_REMINDER);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(licenceId);
                    emailParam.setReqRefNum(licenceId);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                    emailParam.setRefId(licenceId);
                    emailParam.setSubject(subject);
                    //send email
                    log.info(StringUtil.changeForLog("send renewal application email"));
                    notificationHelper.sendNotification(emailParam);
                    log.info(StringUtil.changeForLog("send renewal application email end"));
                    //send sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_LAST_REMINDER_SMS);
                    smsParam.setSubject(subject);
                    smsParam.setQueryCode(licenceId);
                    smsParam.setReqRefNum(licenceId);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                    smsParam.setRefId(licenceId);
                    log.info(StringUtil.changeForLog("send renewal application sms"));
                    notificationHelper.sendNotification(smsParam);
                    log.info(StringUtil.changeForLog("send renewal application sms end"));
                    //send message
                    EmailParam messageParam = new EmailParam();
                    messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_LAST_REMINDER_MESSAGE);
                    messageParam.setTemplateContent(map);
                    messageParam.setQueryCode(licenceId);
                    messageParam.setReqRefNum(licenceId);
                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    messageParam.setRefId(licenceId);
                    messageParam.setSubject(subject);
                    log.info(StringUtil.changeForLog("send renewal application message"));
                    notificationHelper.sendNotification(messageParam);
                    log.info(StringUtil.changeForLog("send renewal application notification end"));
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }else{

            }
        }
        saveMailJob(id,"IS_NO_AUTO"+time);
        List<String> list = useLicenceIdFindHciNameAndAddress(id);
            for(String every:list){
            String address = every.substring(every.indexOf('/')+1);
            String substring = every.substring(0, every.indexOf('/'));
            String format = simpleDateFormat.format(expiryDate);
           /* Map<String,Object> map =new HashMap();
            map.put("IAIS_URL","https://egp.sit.inter.iais.com/hcsa-licence-web/eservice/INTERNET/MohWithOutRenewal?licenceId="+licenceDto.getId());
            map.put("NAME_OF_HCI",substring);
            map.put("Name_of_Service",svcName);
            map.put("Licence_Expiry_Date",format);
            map.put("HCI_Address",address);*/
            StringBuilder stringBuilder =new StringBuilder();
                stringBuilder.append("License expiration time").append(format).append("---").append(time).append(licenceDto.getLicenceNo());
                EmailDto emailDto=new EmailDto();
                emailDto.setContent(stringBuilder.toString());
                emailDto.setSubject(EMAIL_SUBJECT);
                emailDto.setSender(mailSender);
                emailDto.setClientQueryCode("isNotAuto");
                if(!licenseeEmailAddrs.isEmpty()){
                    emailDto.setReceipts(licenseeEmailAddrs);
                    emailClient.sendNotification(emailDto).getEntity();
                }
                String messageNo = inboxMsgService.getMessageNo();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(licenceDto.getSvcName());
                InterMessageDto interMessageDto = MessageTemplateUtil.getInterMessageDto(licenceDto.getId(), MessageConstants.MESSAGE_TYPE_NOTIFICATION,
                        messageNo, hcsaServiceDto.getSvcCode()+"@", stringBuilder.toString(), licenceDto.getLicenseeId(), IaisEGPHelper.getCurrentAuditTrailDto());
                interMessageDto.setSubject("MOH IAIS – Licence is due to expiry");
                HashMap<String,String> mapParam = IaisCommonUtils.genNewHashMap();
                mapParam.put("licenceId",licenceDto.getId());
                interMessageDto.setMaskParams(mapParam);
                inboxMsgService.saveInterMessage(interMessageDto);
        }

    }
    /*
    *
    * auto to send
    * */
    private void isAuto(LicenceDto licenceDto ,String time ) throws IOException, TemplateException {
        /*Name of Service*/
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenceDto.getLicenseeId());

        String svcName = licenceDto.getSvcName();
        Date expiryDate = licenceDto.getExpiryDate();
        String licenceNo = licenceDto.getLicenceNo();
        String[] split = licenceNo.split("/");

        Double total=0.0;
        String id = licenceDto.getId();
        boolean b = checkEmailIsSend(id, "IS_AUTO" + time);
        if(!b){
            return;
        }
        saveMailJob(id,"IS_AUTO"+time);
        List<String> useLicenceIdFindHciNameAndAddress = useLicenceIdFindHciNameAndAddress(id);

        Boolean isMigrated = licenceDto.isMigrated();
        List<String> list=IaisCommonUtils.genNewArrayList();
        List<LicenceFeeDto> licenceFeeDtos=IaisCommonUtils.genNewArrayList();
        list.add(id);
        List<HcsaLicenceGroupFeeDto> entity = hcsaLicenClient.retrieveHcsaLicenceGroupFee(list).getEntity();

        List<PremisesDto> premisesDtoList = hcsaLicenClient.getPremisess(id).getEntity();
        List<String> premises=IaisCommonUtils.genNewArrayList();
        for(PremisesDto premisesDto:premisesDtoList){
            premises.add(premisesDto.getPremisesType());
        }

        if(!entity.isEmpty()){
            for(HcsaLicenceGroupFeeDto every:entity){
                LicenceFeeDto licenceFeeDto=new LicenceFeeDto();
                licenceFeeDto.setLicenceId(id);
                double amount = every.getAmount();
                int count = every.getCount();
                Date expiryDate1 = every.getExpiryDate();
                String groupId = every.getGroupId();
                licenceFeeDto.setOldLicenceId(groupId);
                licenceFeeDto.setBaseService(split[4]);
                licenceFeeDto.setServiceCode(split[4]);
                licenceFeeDto.setServiceName(svcName);
                licenceFeeDto.setPremises(premises);
                licenceFeeDto.setExpiryDate(expiryDate1);
                licenceFeeDtos.add(licenceFeeDto);
            }

            FeeDto feeDto = hcsaConfigClient.renewFee(licenceFeeDtos).getEntity();
             total = feeDto.getTotal();
            for(String every:useLicenceIdFindHciNameAndAddress){
                String hciName = every.substring(0, every.indexOf('/'));
                String address = every.substring(every.indexOf('/') + 1);
                Map<String ,Object> map=IaisCommonUtils.genNewHashMap();
                String format = simpleDateFormat.format(expiryDate);
                map.put("Payment_Amount",total);
                map.put("NAME_OF_HCI",hciName);
                map.put("HCI_Address",address);
                map.put("GIRO_Account_Number","***");
                map.put("IAIS_URL","IAIS_URL");
                map.put("System_Generated_Special_Link","System_Generated_Special_Link");
                map.put("Name_of_Service",svcName);
                map.put("Licence_Expiry_Date",format);
                MsgTemplateDto autoEntity = msgTemplateClient.getMsgTemplate("8D6746B1-6F37-EA11-BE7E-000C29F371DC").getEntity();
                String templateMessageByContent = MsgUtil.getTemplateMessageByContent(autoEntity.getMessageContent(), map);

                EmailDto emailDto=new EmailDto();
                emailDto.setContent(templateMessageByContent);
                emailDto.setSubject(EMAIL_SUBJECT);
                emailDto.setSender(mailSender);
                emailDto.setClientQueryCode("auto");
                if(!licenseeEmailAddrs.isEmpty()){
                    emailDto.setReceipts(licenseeEmailAddrs);
                    String requestRefNum = emailClient.sendNotification(emailDto).getEntity();
                }
                String messageNo = inboxMsgService.getMessageNo();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(licenceDto.getSvcName());
                InterMessageDto interMessageDto = MessageTemplateUtil.getInterMessageDto(licenceDto.getId(), MessageConstants.MESSAGE_TYPE_NOTIFICATION,
                        messageNo, hcsaServiceDto.getSvcCode()+"@", templateMessageByContent, licenceDto.getLicenseeId(), IaisEGPHelper.getCurrentAuditTrailDto());
                interMessageDto.setSubject("MOH IAIS – Licence is due to expiry");
                inboxMsgService.saveInterMessage(interMessageDto);
            }
        }
    }


    private void sendEmailToOffice(LicenceDto licenceDto ,HttpServletRequest request )throws IOException, TemplateException{
        List<String> ASOEmailAddrs = IaisCommonUtils.genNewArrayList();
        organizationClient.retrieveUserRoleByRoleId(RoleConsts.USER_ROLE_ASO).getEntity().stream().forEach(v ->{
            ASOEmailAddrs.add(v.getEmail());
        });

        Date expiryDate = licenceDto.getExpiryDate();
        String id = licenceDto.getId();
        List<String> useLicenceIdFindHciNameAndAddress = useLicenceIdFindHciNameAndAddress(id);
        for (String every:useLicenceIdFindHciNameAndAddress) {
            String svcName = licenceDto.getSvcName();
            String hciName = every.substring(0, every.indexOf('/'));
            String address = every.substring(every.indexOf('/') + 1);
            Map<String ,Object> map=IaisCommonUtils.genNewHashMap();
            String format = simpleDateFormat.format(expiryDate);
            map.put("HCIName",hciName);
            map.put("HCI_Address",address);
            map.put("serviceName",svcName);
            map.put("licenceExpiryDate",format);
            MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SEND_TO_OFFICER_SEVENTH).getEntity();

            String templateMessageByContent = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);

            EmailDto emailDto=new EmailDto();
            emailDto.setContent(templateMessageByContent);
            emailDto.setSubject(EMAIL_TO_OFFICER_SUBJECT);
            emailDto.setSender(mailSender);
            emailDto.setClientQueryCode(licenceDto.getLicenseeId());


            if(!ASOEmailAddrs.isEmpty()){
                emailDto.setReceipts(ASOEmailAddrs);
                emailClient.sendNotification(emailDto).getEntity();
            }
        }
    }
    /*
    * remind sended email
    *
    * */

    private void saveMailJob(String licence,String magKey){
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto=new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        jobRemindMsgTrackingDto.setMsgKey(magKey);
        jobRemindMsgTrackingDto.setRefNo(licence);
        List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos=new ArrayList<>(1);
        jobRemindMsgTrackingDtos.add(jobRemindMsgTrackingDto);
        systemBeLicClient.createJobRemindMsgTrackingDtos(jobRemindMsgTrackingDtos).getEntity();
    }
    private boolean checkEmailIsSend(String licence,String magKey){

        JobRemindMsgTrackingDto auto_renew = systemBeLicClient.getJobRemindMsgTrackingDto(licence, magKey).getEntity();
        if(auto_renew==null){
            return true;
        }else {
            log.info(StringUtil.changeForLog(JsonUtil.parseToJson(auto_renew+"auto_renew")));
            return false;
        }

    }
        /*
        * message id
        * */

        private String messageId(String numberOfMonth){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("Email01");
            stringBuilder.append(numberOfMonth);
            return stringBuilder.toString();
        }


       /*
       * licence id  hci name and address
       * */

       private  List<String> useLicenceIdFindHciNameAndAddress(String licenceId){
           List<String> nameAndAddress=IaisCommonUtils.genNewArrayList();
           if(licenceId==null){
             return    nameAndAddress;
           }
           List<PremisesDto> entity = hcsaLicenClient.getPremisess(licenceId).getEntity();
            for(PremisesDto every:entity){
                StringBuilder stringBuilder=new StringBuilder();
                String hciName = every.getHciName();
                String blkNo = every.getBlkNo();
                String streetName = every.getStreetName();
                String buildingName = every.getBuildingName();

                String floorNo = every.getFloorNo();
                String unitNo = every.getUnitNo();
                String postalCode = every.getPostalCode();
                stringBuilder.append(hciName).append('/');
                stringBuilder.append(blkNo).append(' ');
                stringBuilder.append(streetName).append(' ');
                stringBuilder.append(buildingName).append(" # ");
                stringBuilder.append(floorNo).append('-');
                stringBuilder.append(unitNo).append(',');
                stringBuilder.append(postalCode);
                nameAndAddress.add(stringBuilder.toString());
            }
        return nameAndAddress;
       }


       private  List<OrgUserRoleDto> getSendMailUser(String organizationId){
           List<OrgUserRoleDto> entity = organizationClient.getSendEmailUser(organizationId).getEntity();
           return entity;

       }

}
