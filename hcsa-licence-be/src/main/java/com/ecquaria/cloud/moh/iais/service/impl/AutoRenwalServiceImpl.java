package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
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
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private ApplicationClient applicationClient;
    @Autowired
    private EmailClient emailClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private InspectionTaskClient inspectionTaskClient;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private InboxMsgService inboxMsgService;
    @Autowired
    private SystemBeLicClient systemBeLicClient;
    @Value("${iais.email.sender}")
    private String mailSender;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private LicCommService licCommService;

    private static final String EMAIL_SUBJECT="MOH IAIS – REMINDER TO RENEW LICENCE";
    private static final String EMAIL_TO_OFFICER_SUBJECT="MOH IAIS – Licence is due to expiry";
    private static final String F_180="E418B2D1-AD35-EA11-BE7D-000C29F371DC";
    private static final String S_150="08ED7E7E-4359-EA11-BE7F-000C29F371DC";
    private static final String T_120="09ED7E7E-4359-EA11-BE7F-000C29F371DC";
    private static final String F_90="0AED7E7E-4359-EA11-BE7F-000C29F371DC";
    private static final String F_60="0BED7E7E-4359-EA11-BE7F-000C29F371DC";
    private static final String S_45="0CED7E7E-4359-EA11-BE7F-000C29F371DC";
    private static final String S_30="BBF06A97-4359-EA11-BE7F-000C29F371DC";
    private static final String DAY="TPOF00006";
    private static final String MONTH_DAY="TPOF00005";
    private static final String WEEK_DAY="TPOF00004";
    private static final String REMINDER_DAY="TPOF00012";
    private static final String INT="INT";
    @Override
    public void startRenwal() {
        List<Integer> dayList= IaisCommonUtils.genNewArrayList();
        SystemParameterDto systemParameterDto = systemBeLicClient.getParameterByRowguid(F_180).getEntity();
        SystemParameterDto systemParameterDto1 = systemBeLicClient.getParameterByRowguid(S_150).getEntity();
        SystemParameterDto systemParameterDto2 = systemBeLicClient.getParameterByRowguid(T_120).getEntity();
        SystemParameterDto systemParameterDto3 = systemBeLicClient.getParameterByRowguid(F_90).getEntity();
        SystemParameterDto systemParameterDto4 = systemBeLicClient.getParameterByRowguid(F_60).getEntity();
        SystemParameterDto systemParameterDto5 = systemBeLicClient.getParameterByRowguid(S_45).getEntity();
        SystemParameterDto systemParameterDto6 = systemBeLicClient.getParameterByRowguid(S_30).getEntity();
        Map<String,String> mouth=IaisCommonUtils.genNewHashMap();
       /* dayList.add(getDay(systemParameterDto6,mouth));
        dayList.add(getDay(systemParameterDto5 ,mouth ));*/
        dayList.add(getDay(systemParameterDto4, mouth));
        dayList.add(getDay(systemParameterDto3,mouth));
        dayList.add(getDay(systemParameterDto2,mouth));
        dayList.add(getDay(systemParameterDto1,mouth));
        dayList.add(getDay(systemParameterDto,mouth));
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(dayList)+"dayList"));
        Map<String, List<LicenceDto>> entity = hcsaLicenClient.licenceRenwal(dayList).getEntity();
        Map<String, List<LicenceDto>> entity1 = hcsaLicenClient.unsendEmail(dayList).getEntity();
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(entity+"------entity")));
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(entity1 + "-----entity1")));
        clearDouble(entity,entity1);
        sendEmail(entity,mouth);
        sendEmail(entity1,mouth);
        List<LicenceDto> licence = getLicence("IS_NO_AUTO" + F_60, "IS_NO_AUTO" + F_60,systemParameterDto5);
        List<LicenceDto> licence1 = getLicence("IS_NO_AUTO" + S_45, "IS_NO_AUTO" + S_45,systemParameterDto6);
        log.info(StringUtil.changeForLog("======"+StringUtil.changeForLog(JsonUtil.parseToJson(licence))));
        log.info(StringUtil.changeForLog("======"+StringUtil.changeForLog(JsonUtil.parseToJson(licence1))));
        entity.clear();
        mouth.put(S_45,"-1");
        mouth.put(S_30,"-2");
        entity.put("-1",licence);
        entity.put("-2",licence1);
        sendEmail(entity,mouth);
    }

    private  void clearDouble(Map<String, List<LicenceDto>> entity ,Map<String, List<LicenceDto>> entity1 ){
        entity.forEach((k,v)->{
            List<LicenceDto> licenceDtos = entity1.get(k);
            for(LicenceDto l : v){
                if(licenceDtos.contains(l)){
                    licenceDtos.remove(l);
                }
            }
        });
    }
    private List<LicenceDto> getLicence(String msgKey,String selectKey,  SystemParameterDto systemParameterDto){
        List<JobRemindMsgTrackingDto> entity = systemBeLicClient.getJobRemindMsgTrackingDtos(msgKey).getEntity();
        List<LicenceDto> licenceDtos=IaisCommonUtils.genNewArrayList();
        String paramType = systemParameterDto.getParamType();
        String value = systemParameterDto.getValue();
        Calendar calendar=Calendar.getInstance();
        if(entity!=null && !entity.isEmpty()){
            Date now = new Date();
            for(JobRemindMsgTrackingDto jobRemindMsgTrackingDto : entity){
                String refNo = jobRemindMsgTrackingDto.getRefNo();
                Date createTime = jobRemindMsgTrackingDto.getCreateTime();
                calendar.setTime(createTime);
                if(WEEK_DAY.equals(paramType)){
                  calendar.add(Calendar.WEEK_OF_MONTH,Integer.parseInt(value));
                }
                LicenceDto licenceDto = hcsaLicenClient.getLicenceDtoById(refNo).getEntity();
                if(licenceDto!=null&&ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())){
                    boolean b = checkEmailIsSend(refNo, selectKey);
                    if(!b){
                        if(now.after(calendar.getTime()) ){
                            licenceDtos.add(licenceDto);
                        }
                    }
                }
            }
        }
        return licenceDtos;
    }
    private void sendEmail( Map<String, List<LicenceDto>> entity,Map<String,String> map){
        entity.forEach((k, v) -> {
            for(int i=0;i<v.size();i++){
                String licenceNo = v.get(i).getLicenceNo();

                boolean autoOrNon = isAutoOrNon(licenceNo);

                if(autoOrNon){

                    try {
                        isAuto(v.get(i),k,map);

                    } catch (Exception e) {
                        log.info(e.getMessage(),e);
                    }
                }else {

                    try {
                        isNoAuto(v.get(i),k,map);
                    } catch (Exception e) {
                        log.error(e.getMessage(),e);
                    }

                }
//                if ("-1".equals(k)){
//                    try {
//                        sendEmailToOffice(v.get(i));
//                    } catch (IOException e) {
//                        log.error(e.getMessage(), e);
//                    } catch (TemplateException e) {
//                        log.error(e.getMessage(), e);
//                    }
//                }
//                if ("30".equals(k)){
//                    try {
//                        sendEmailToOffice(v.get(i));
//                    } catch (IOException e) {
//                        log.error(e.getMessage(), e);
//                    } catch (TemplateException e) {
//                        log.error(e.getMessage(), e);
//                    }
//                }
            }
        });
    }
    private Integer getDay(SystemParameterDto systemParameterDto,Map<String ,String> map){
        String value = systemParameterDto.getValue();
        String valueType = systemParameterDto.getValueType();
        String paramType = systemParameterDto.getParamType();
        String id = systemParameterDto.getId();
        if(INT.equals(valueType)){
            int i = Integer.parseInt(value);
            //month
            Calendar c=Calendar.getInstance();
            c.setTime(new Date());
            if(MONTH_DAY.equals(paramType)){
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH,i);
                int i1 = Integer.parseInt(String.valueOf((calendar.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                map.put(systemParameterDto.getId(),i1+"");
                return i1;
            }else if(WEEK_DAY.equals(paramType)){//week
                SystemParameterDto systemParameterDto4 = systemBeLicClient.getParameterByRowguid(F_60).getEntity();
                if(S_45.equals(id)){
                    Calendar calendar =Calendar.getInstance();
                    calendar.setTime(new Date());
                    Calendar calendar1=Calendar.getInstance();
                    calendar1.setTime(new Date());
                    calendar1.add(Calendar.WEEK_OF_MONTH,i);
                    if(MONTH_DAY.equals(systemParameterDto4.getParamType())){
                        calendar.add(Calendar.MONTH,Integer.parseInt(systemParameterDto4.getValue()));
                        int i1 = Integer.parseInt(String.valueOf((calendar.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        int i2 = Integer.parseInt(String.valueOf((calendar1.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        int i3 = i1 - i2;
                        if(i3>0){
                            map.put(systemParameterDto.getId(),i3+"");
                            return i3;
                        }else if(i3<=0){
                            map.put(systemParameterDto.getId(),0+"");
                            return 0;
                        }
                    }else if(WEEK_DAY.equals(systemParameterDto4.getParamType())){
                        calendar.add(Calendar.WEEK_OF_MONTH,Integer.parseInt(systemParameterDto4.getValue()));
                        int i1 = Integer.parseInt(String.valueOf((calendar.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        int i2 = Integer.parseInt(String.valueOf((calendar1.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        int i3 = i1 - i2;
                        if(i3>0){
                            map.put(systemParameterDto.getId(),i3+"");
                            return i3;
                        }else if(i3<=0){
                            map.put(systemParameterDto.getId(),0+"");
                            return 0;
                        }
                    }
                }else if(S_30.equals(id)){
                    SystemParameterDto systemParameterDto5 = systemBeLicClient.getParameterByRowguid(S_45).getEntity();
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTime(new Date());
                    Calendar calendar1=Calendar.getInstance();
                    calendar1.setTime(new Date());
                    calendar1.add(Calendar.WEEK_OF_MONTH,i);
                    Calendar calendar2=Calendar.getInstance();
                    calendar2.setTime(new Date());
                    calendar2.add(Calendar.MONTH,Integer.parseInt(systemParameterDto.getValue()));
                    if(MONTH_DAY.equals(systemParameterDto5.getParamType())){
                        calendar.add(Calendar.MONTH,Integer.parseInt(systemParameterDto5.getValue()));
                        int i1 = Integer.parseInt(String.valueOf((calendar.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        int i2 = Integer.parseInt(String.valueOf((calendar1.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        int i4 = Integer.parseInt(String.valueOf((calendar2.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        int i3 = i4 - i1 - i2;
                        if(i3>0){
                            map.put(systemParameterDto.getId(),i3+"");
                            return i3;
                        }else if(i3<=0){
                            map.put(systemParameterDto.getId(),0+"");
                            return 0;
                        }

                    }else if(WEEK_DAY.equals(systemParameterDto5.getParamType())){
                        calendar.add(Calendar.WEEK_OF_MONTH,Integer.parseInt(systemParameterDto5.getValue()));
                        int i1 = Integer.parseInt(String.valueOf((calendar.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        int i2 = Integer.parseInt(String.valueOf((calendar1.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        int i4 = Integer.parseInt(String.valueOf((calendar2.getTimeInMillis() - c.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
                        int i3 = i4 - i1 - i2;
                        if(i3>0){
                            map.put(systemParameterDto.getId(),i3+"");
                            return i3;
                        }else if(i3<=0){
                            map.put(systemParameterDto.getId(),0+"");
                            return 0;
                        }
                    }
                }
            }else if(REMINDER_DAY.equals(paramType)){
                map.put(systemParameterDto.getId(),i+"");
                return i;
            }else if(DAY.equals(paramType)){
                map.put(systemParameterDto.getId(),i+"");
                return i;
            }
        }
        return 0;
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
    private void  isNoAuto(LicenceDto licenceDto ,String time,Map<String,String> mounth) throws IOException, TemplateException {
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenceDto.getLicenseeId());
        log.info(StringUtil.changeForLog(licenseeEmailAddrs.toString()+"----------"));
        log.info(StringUtil.changeForLog(licenceDto.getLicenseeId()+"licenseeId"));
        Date expiryDate = licenceDto.getExpiryDate();

        String id = licenceDto.getId();
        log.info(StringUtil.changeForLog(time+"time"));
        String s = mounth.get(F_180);
        String s1 = mounth.get(S_150);
        String s2 = mounth.get(T_120);
        String s3 = mounth.get(F_90);
        String s4 = mounth.get(F_60);
        String s5 = mounth.get(S_45);
        String s6 = mounth.get(S_30);
        log.info(StringUtil.changeForLog(s+"-----------mounth-------------"));
        log.info(id);
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(licenceDto)));
        boolean b = false;
        String emailTime="1";
        String emailSendTime=time;
        if(time.equals(s)){
            b = checkEmailIsSend(id, "IS_NO_AUTO" + F_180);
            emailSendTime=F_180;
            emailTime="7";
        }else if(time.equals(s1)){
            b = checkEmailIsSend(id, "IS_NO_AUTO" + S_150);
            emailTime="6";
            emailSendTime=S_150;
        }else if(time.equals(s2)){
            b = checkEmailIsSend(id, "IS_NO_AUTO" + T_120);
            emailTime="5";
            emailSendTime=T_120;
        }else if(time.equals(s3)){
            b = checkEmailIsSend(id, "IS_NO_AUTO" + F_90);
            emailTime="4";
            emailSendTime=F_90;
        }else if(time.equals(s4)){
            b = checkEmailIsSend(id, "IS_NO_AUTO" + F_60);
            emailTime="3";
            emailSendTime=F_60;
        }else if(time.equals(s5)){
            b = checkEmailIsSend(id, "IS_NO_AUTO" + S_45);
            emailTime="2";
            emailSendTime=S_45;
        }else if(time.equals(s6)){
            b = checkEmailIsSend(id, "IS_NO_AUTO" + S_30);
            emailTime="1";
            emailSendTime=S_30;
        }
        log.info(StringUtil.changeForLog(b+"-------type"));
        if(!b){
            return;
        }
        saveMailJob(id,"IS_NO_AUTO"+emailSendTime);
        String serviceName = licenceDto.getSvcName();
        String serviceCode = hcsaConfigClient.getServiceCodeByName(serviceName).getEntity();
        List<String> serviceCodeList = IaisCommonUtils.genNewArrayList();
        serviceCodeList.add(serviceCode);
        String applicantName = getApplicantNameByLicId(id);
        if(!StringUtil.isEmpty(applicantName)){
            String licenceId = licenceDto.getId();
            String licenceNo = licenceDto.getLicenceNo();
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            String MohName = AppConsts.MOH_AGENCY_NAME;
            log.info(StringUtil.changeForLog("send renewal application notification applicantName : " + applicantName));
            List<PremisesDto> list = hcsaLicenClient.getPremisess(licenceId).getEntity();

            String address = list.get(0).getAddress();
            String substring = list.get(0).getHciName();
            if("3".equals(emailTime)){
                //fifth reminder, System will send an alert to MOH Officer

                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("officer_name", "officer_name");
                map.put("MOH_AGENCY_NAME", MohName);
                map.put("Name_of_Service", serviceName);
                map.put("NAME_OF_HCI",substring);
                map.put("HCI_Address",address);
                String format = Formatter.formatDate(expiryDate);
                map.put("Licence_Expiry_Date", format);
                log.info(StringUtil.changeForLog("send renewal application last reminder"));
                try {
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_NOT_AUTO_REMINDER_FIFTH_FOR_ASO);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(licenceNo);
                    emailParam.setReqRefNum(licenceId);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                    emailParam.setRefId(licenceId);
                    //send email
                    log.info(StringUtil.changeForLog("send renewal application email"));
                    notificationHelper.sendNotification(emailParam);
                    log.info(StringUtil.changeForLog("send renewal application email end"));
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }

            {
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("ApplicantName", applicantName);
                map.put("NAME_OF_HCI",substring);
                map.put("HCI_Address",address);
                map.put("Name_of_Service", serviceName);
                map.put("IAIS_URL", loginUrl);
                String format = Formatter.formatDate(expiryDate);
                map.put("Licence_Expiry_Date", format);

                Calendar expireCalendar = Calendar.getInstance();
                expireCalendar.setTime(expiryDate);
                expireCalendar.add(Calendar.MONTH, -2);
                String expireDateString = new SimpleDateFormat("dd/MM/yyyy").format(expiryDate);
                String temp =new SimpleDateFormat("dd/MM/yyyy").format(expireCalendar.getTime());
                map.put("endDate", temp);
                map.put("expireDate", expireDateString);
                try {
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_NOT_AUTO_REMINDER);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(licenceNo);
                    emailParam.setReqRefNum(licenceId);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                    emailParam.setRefId(licenceId);
                    //send email
                    log.info(StringUtil.changeForLog("send renewal application first - sixth email"));
                    notificationHelper.sendNotification(emailParam);
                    log.info(StringUtil.changeForLog("send renewal application first - sixth email end"));
                    //send sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_NOT_AUTO_REMINDER_SMS);
                    smsParam.setQueryCode(licenceNo);
                    smsParam.setReqRefNum(licenceId);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                    smsParam.setRefId(licenceId);
                    log.info(StringUtil.changeForLog("send renewal application first - sixth sms"));
                    notificationHelper.sendNotification(smsParam);
                    log.info(StringUtil.changeForLog("send renewal application first - sixth sms end"));
                    //send message
                    EmailParam messageParam = new EmailParam();
                    messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_NOT_AUTO_REMINDER_MESSAGE);
                    messageParam.setTemplateContent(map);
                    messageParam.setQueryCode(licenceNo);
                    messageParam.setReqRefNum(licenceId);
                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    messageParam.setSvcCodeList(serviceCodeList);
                    messageParam.setRefId(licenceId);
                    log.info(StringUtil.changeForLog("send renewal application first - sixth message"));
                    notificationHelper.sendNotification(messageParam);
                    log.info(StringUtil.changeForLog("send renewal application notification first - sixth end"));
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        } else {
            log.info(StringUtil.changeForLog("applicantName is null"));
        }


    }

    private String getEmailSubject( MsgTemplateDto emailTemplateDto,Map<String, Object> subMap) {
        String subject = "-";
        if (!StringUtil.isEmpty(emailTemplateDto)) {
            try {
                if (!IaisCommonUtils.isEmpty(subMap)) {
                    subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(), subMap);
                } else {
                    subject = emailTemplateDto.getTemplateName();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return subject;
    }

    private String getEmailSubject(String templateId,Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(templateId).getEntity();
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
                    }else{
                        subject = emailTemplateDto.getTemplateName();
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return subject;
    }

    private String getApplicantNameByLicId(String id) {
        String applicantName = "-";
        List<LicAppCorrelationDto> licAppCorrelationDtos = licCommService.getLicCorrBylicId(id);
        if(!IaisCommonUtils.isEmpty(licAppCorrelationDtos)){
            for(LicAppCorrelationDto licAppCorrelationDto : licAppCorrelationDtos){
                if(licAppCorrelationDto != null){
                    String appId = licAppCorrelationDto.getApplicationId();
                    if(!StringUtil.isEmpty(appId)){
                        ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
                        if(applicationDto != null && !ApplicationConsts.APPLICATION_STATUS_DELETED.equals(applicationDto.getStatus())){
                            ApplicationGroupDto applicationGroupDto = inspectionTaskClient.getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId()).getEntity();
                            if(applicationGroupDto != null){
                                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                                if(orgUserDto != null) {
                                    applicantName = orgUserDto.getDisplayName();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }else{
            log.debug(StringUtil.changeForLog("get applicant name error"));
        }
        return applicantName;
    }

    /*
    *
    * auto to send
    * */
    private void isAuto(LicenceDto licenceDto ,String time ,Map<String,String> mouth) throws IOException, TemplateException {
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
        List<PremisesDto> HciNameAndAddress = hcsaLicenClient.getPremisess(id).getEntity();

        String address = HciNameAndAddress.get(0).getAddress();
        String hciName = HciNameAndAddress.get(0).getHciName();

        List<String> list=IaisCommonUtils.genNewArrayList();
        List<LicenceFeeDto> licenceFeeDtos=IaisCommonUtils.genNewArrayList();
        list.add(id);
        List<HcsaLicenceGroupFeeDto> entity = hcsaLicenClient.retrieveHcsaLicenceGroupFee(list).getEntity();

        List<PremisesDto> premisesDtoList = hcsaLicenClient.getPremisess(id).getEntity();

        if(!entity.isEmpty()){
            for(PremisesDto premisesDto:premisesDtoList){
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
                    licenceFeeDto.setBundle(0);
                    licenceFeeDto.setPremises(premisesDto.getAddress());
                    licenceFeeDto.setExpiryDate(expiryDate1);
                    licenceFeeDtos.add(licenceFeeDto);
                }
            }

            FeeDto feeDto = hcsaConfigClient.renewFee(licenceFeeDtos).getEntity();
             total = feeDto.getTotal();

                Map<String ,Object> map=IaisCommonUtils.genNewHashMap();
                String format = Formatter.formatDate(expiryDate);
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
        try {
            JobRemindMsgTrackingDto auto_renew = systemBeLicClient.getJobRemindMsgTrackingDto(licence, magKey).getEntity();
            if(auto_renew==null){
                return true;
            }else {
                log.info(StringUtil.changeForLog(JsonUtil.parseToJson(auto_renew)+"auto_renew"));
                return false;
            }

        }catch (Exception e){
            log.info(e.getMessage(),e);
            log.info(StringUtil.changeForLog("-----have error---"));
            return false;
        }
    }






}
