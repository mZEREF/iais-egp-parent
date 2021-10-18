package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminMainFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author yichen
 * @Date:2021/4/26
 */

@Component
@Slf4j
public class FeMainEmailHelper {

    public final static String SINGPASS_EXPIRE_REMINDER_JOB = "singpass_expire_reminder_job";

    @Autowired
    private OrgUserManageService userManageService;

    @Autowired
    private LicenceInboxClient licenceClient;

    @Autowired
    private SystemParamConfig paramConfig;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private SystemAdminMainFeClient systemAdminMainFeClient;

    private final static String SINGPASS_AUTO_CEASED_EMAIL_KEY_PRIX = "msg_template_singpass_auto_caased";

    public boolean hasBeenReminder(String refNumber, String trackKey){
        JobRemindMsgTrackingDto tracking = systemAdminMainFeClient.getJobRemindMsgTrackingDto(refNumber, trackKey).getEntity();
        if (Optional.ofNullable(tracking).isPresent()){
            return true;
        }
        return false;
    }

    public void sendSingPassAutoCeasedMsg(String uen, String nricNumber){
        log.info("uen => {}, nric => {}", uen, nricNumber);
        if (StringUtil.isNotEmpty(uen) && StringUtil.isNotEmpty(nricNumber)){
            log.info("send singpass auto ceased email start");
            OrganizationDto organ = userManageService.findOrganizationByUen(uen);
            if (Optional.ofNullable(organ).isPresent()){
                List<LicenseeDto> licList =  userManageService.getLicenseeByOrgId(organ.getId());
                if (IaisCommonUtils.isNotEmpty(licList)){
                    for (LicenseeDto lic : licList){
                        //It has to be solo licensee, The licensee list should have only one record
                        log.info(StringUtil.changeForLog("SingPass Json Str"+ JsonUtil.parseToJson(lic)));
                        if (OrganizationConstants.LICENSEE_TYPE_SINGPASS.equals(lic.getLicenseeType())){
                            String licId = lic.getId();
                            String licenseeName = lic.getName();
                            List<LicenceDto> licenceList = licenceClient.getLicenceDtosByLicenseeId(licId).getEntity();
                            if (IaisCommonUtils.isNotEmpty(licenceList)){
                                StringBuilder hciNameBuilder = new StringBuilder();
                                StringBuilder hciAddressBuilder = new StringBuilder();
                                for (LicenceDto licence : licenceList){
                                    List<PremisesDto> licPremises = licenceClient.getPremisesDto(licence.getId()).getEntity();
                                    if( IaisCommonUtils.isNotEmpty(licPremises)){
                                        for(PremisesDto premises : licPremises){
                                            if(StringUtil.isNotEmpty(premises.getHciName())){
                                                String hciName = premises.getHciName();
                                                String hciAddress = MiscUtil.getAddress(premises.getBlkNo(),premises.getStreetName(),
                                                        premises.getBuildingName(),premises.getFloorNo(),premises.getUnitNo(),premises.getPostalCode());
                                                hciNameBuilder.append(hciName).append(',');
                                                hciAddressBuilder.append(hciAddress).append(',');
                                            }
                                        }
                                    }
                                }

                                String uenNo = organ.getUenNo();
                                String hciStr = hciNameBuilder.substring(0, hciNameBuilder.length() - 1);
                                String addressStr = hciAddressBuilder.substring(0, hciAddressBuilder.length() - 1);
                                Map<String, Object> templateMap = IaisCommonUtils.genNewHashMap();
                                templateMap.put("HCI_Name", hciStr);
                                templateMap.put("HCI_Address", addressStr);
                                templateMap.put("UEN_No", uenNo);

                                String idType = IaisEGPHelper.checkIdentityNoType(nricNumber);
                                FeUserDto user = userManageService.getFeUserAccountByNricAndType(nricNumber, idType, uenNo);
                                if (Optional.ofNullable(user).isPresent()){
                                    templateMap.put("Applicant", user.getDisplayName());
                                }else {
                                    templateMap.put("Applicant", licenseeName);
                                }

                                templateMap.put("emailAddress", paramConfig.getSystemAddressOne());
                                templateMap.put("telNo", paramConfig.getSystemPhoneNumber());
                                String loginUrl = HmacConstants.HTTPS +"://" + paramConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                                templateMap.put("newSystem", loginUrl);


                                EmailParam emailParam = new EmailParam();
                                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_SINGPASS_AUTO_CAASED_EMAIL);
                                emailParam.setTemplateContent(templateMap);
                                emailParam.setQueryCode("msg_template_singpass_auto_caased_email");
                                emailParam.setReqRefNum(licId);
                                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
                                emailParam.setRefId(licId);

                                EmailParam smsParam = MiscUtil.transferEntityDto(emailParam, EmailParam.class);
                                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_SINGPASS_AUTO_CAASED_SMS);
                                smsParam.setQueryCode("msg_template_singpass_auto_caased_sms");
                                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENSEE_ID);
                                notificationHelper.sendNotification(smsParam);

                                EmailParam msgParam = MiscUtil.transferEntityDto(emailParam, EmailParam.class);
                                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_SINGPASS_AUTO_CAASED_MSG);
                                msgParam.setQueryCode("msg_template_singpass_auto_caased_msg");
                                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                                notificationHelper.sendNotification(msgParam);

                                JobRemindMsgTrackingDto tracking = new JobRemindMsgTrackingDto();
                                tracking.setRefNo(uenNo + "_" + nricNumber);
                                tracking.setMsgKey(FeMainEmailHelper.SINGPASS_EXPIRE_REMINDER_JOB);
                                tracking.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                                emailParam.setJobRemindMsgTrackingDto(tracking);
                                notificationHelper.sendNotification(emailParam);
                            }
                        }
                    }
                }
            }
        }
    }
}
