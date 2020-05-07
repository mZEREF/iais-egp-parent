package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AutoRenwalService;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private SystemBeLicClient systemBeLicClient;
    private SimpleDateFormat simpleDateFormat =new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);

    private static final String EMAIL_SUBJECT="MOH IAIS – REMINDER TO RENEW LICENCE";
    private static final String EMAIL_TO_OFFICER_SUBJECT="MOH IAIS – Licence is due to expiry";
    @Override
    public void startRenwal(HttpServletRequest request) {
        List<Integer> dayList= IaisCommonUtils.genNewArrayList();
        dayList.add(30);
        dayList.add(45);
        dayList.add(60);
        dayList.add(90);
        dayList.add(120);
        dayList.add(150);
        dayList.add(180);
        List<JobRemindMsgTrackingDto> JobRemindMsgTrackingDto = systemBeLicClient.listJob().getEntity();
        Map<String, List<LicenceDto>> entity = hcsaLicenClient.licenceRenwal(dayList).getEntity();
        entity.forEach((k, v) -> {
            licenceToRemove(v,JobRemindMsgTrackingDto);
            for(int i=0;i<v.size();i++){

                String licenceNo = v.get(i).getLicenceNo();

                boolean autoOrNon = isAutoOrNon(licenceNo);

                if(autoOrNon){

                    try {
                        isAuto(v.get(i),request);

                    } catch (Exception e) {

                        log.info(e.getMessage(),e);

                    }

                }else {

                    try {
                        isNoAuto(v.get(i),request);
                    } catch (Exception e) {
                        log.error(e.getMessage(),e);
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
    private void  isNoAuto(LicenceDto licenceDto ,HttpServletRequest request) throws IOException, TemplateException {

        String svcName = licenceDto.getSvcName();

        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenceDto.getLicenseeId());
        log.info(licenseeEmailAddrs.toString()+"----------");
        log.info(licenceDto.getLicenseeId()+"licenseeId");
        Date expiryDate = licenceDto.getExpiryDate();

        String id = licenceDto.getId();

        List<String> list = useLicenceIdFindHciNameAndAddress(id);

            for(String every:list){
            String address = every.substring(every.indexOf("/")+1);
            String substring = every.substring(0, every.indexOf("/"));
            String format = simpleDateFormat.format(expiryDate);
            Map<String,Object> map =new HashMap();
            map.put("IAIS_URL","aaaaa");
            map.put("NAME_OF_HCI",substring);
            map.put("Name_of_Service",svcName);
            map.put("Licence_Expiry_Date",format);
            map.put("HCI_Address",address);
            MsgTemplateDto entity = msgTemplateClient.getMsgTemplate("079E4C27-7937-EA11-BE7E-000C29F371DC").getEntity();

            String messageContent = entity.getMessageContent();
            String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
                EmailDto emailDto=new EmailDto();
                emailDto.setContent(templateMessageByContent);
                emailDto.setSubject(EMAIL_SUBJECT);
                emailDto.setSender("Ministry of Health");
                emailDto.setClientQueryCode("isNotAuto");

                if(!licenseeEmailAddrs.isEmpty()){
                    emailDto.setReceipts(licenseeEmailAddrs);
                    String requestRefNum = emailClient.sendNotification(emailDto).getEntity();
                }



        }

    }
    /*
    *
    * auto to send
    * */
    private void isAuto(LicenceDto licenceDto ,HttpServletRequest request ) throws IOException, TemplateException {
        /*Name of Service*/
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenceDto.getLicenseeId());

        String svcName = licenceDto.getSvcName();
        Date expiryDate = licenceDto.getExpiryDate();
        String licenceNo = licenceDto.getLicenceNo();
        String[] split = licenceNo.split("/");

        Double total=0.0;
        String id = licenceDto.getId();

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
                licenceFeeDto.setGroupId(groupId);
                licenceFeeDto.setBaseService(split[4]);
                licenceFeeDto.setServiceCode(split[4]);
                licenceFeeDto.setServiceName(svcName);
                licenceFeeDto.setPremises(premises);
                licenceFeeDto.setExpiryDate(expiryDate1);
              if(isMigrated){
                  licenceFeeDto.setMigrated(isMigrated);
                  licenceFeeDto.setGroupId(groupId);
                  licenceFeeDto.setOldAmount(amount);
                  licenceFeeDto.setRenewCount(count);
              }else  if(!isMigrated){
                  licenceFeeDto.setMigrated(isMigrated);
                licenceFeeDto.setOldAmount(amount);
                  licenceFeeDto.setRenewCount(0);
              }
                licenceFeeDtos.add(licenceFeeDto);
            }

            FeeDto feeDto = hcsaConfigClient.renewFee(licenceFeeDtos).getEntity();
             total = feeDto.getTotal();
            for(String every:useLicenceIdFindHciNameAndAddress){
                String hciName = every.substring(0, every.indexOf("/"));
                String address = every.substring(every.indexOf("/") + 1);
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
                emailDto.setSender("Ministry of Health");
                emailDto.setClientQueryCode("auto");

                if(!licenseeEmailAddrs.isEmpty()){
                    emailDto.setReceipts(licenseeEmailAddrs);
                    String requestRefNum = emailClient.sendNotification(emailDto).getEntity();
                }
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
        for (String every:useLicenceIdFindHciNameAndAddress
             ) {
            String svcName = licenceDto.getSvcName();
            String hciName = every.substring(0, every.indexOf("/"));
            String address = every.substring(every.indexOf("/") + 1);
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
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
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

    private void saveMailJob(){
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto=new JobRemindMsgTrackingDto();

        Void entity = systemBeLicClient.saveSendMailJob(jobRemindMsgTrackingDto).getEntity();

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
                stringBuilder.append(hciName+"/");
                stringBuilder.append(blkNo+" ");
                stringBuilder.append(streetName+" ");
                stringBuilder.append(buildingName+" # ");
                stringBuilder.append(floorNo+"-");
                stringBuilder.append(unitNo+",");
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
