package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.VehNoValidator;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/****
 *
 *   @date 12/26/2019
 *   @author zixian
 */
@Service
@Slf4j
public class RequestForChangeServiceImpl implements RequestForChangeService {
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private FeEmailClient feEmailClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private FeMessageClient feMessageClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Value("${iais.email.sender}")
    private String mailSender;


    @Override
    public List<PremisesListQueryDto> getPremisesList(String licenseeId) {
        return licenceClient.getPremises(licenseeId).getEntity();
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId) {
        return licenceClient.getAppSubmissionDto(licenceId).getEntity();
    }

    @Override
    public AppSubmissionDto submitChange(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        //save appGrp and app
        appSubmissionDto = applicationClient.saveAppsForRequestForChange(appSubmissionDto).getEntity();
//asynchronous save the other data.
        //eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }


    @Override
    public List<ApplicationDto> getAppByLicIdAndExcludeNew(String licenceId) {
        List<ApplicationDto> applicationDtos = applicationClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();;
        List<ApplicationDto> newApplicationDtos = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto:applicationDtos){
            if(!ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(applicationDto.getStatus()) &&
                    !ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(applicationDto.getStatus())&&
                    !ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(applicationDto.getStatus())&&
                    !ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT.equals(applicationDto.getStatus())){
                newApplicationDtos.add(applicationDto);
            }
        }
        return newApplicationDtos;
    }


    @Override
    public String getApplicationGroupNumber(String appType) {
        return systemAdminClient.applicationNumber(appType).getEntity();
    }

    @Override
    public void upDateLicStatus(LicenceDto licenceDto) {
        licenceClient.doUpdate(licenceDto);
    }

    @Override
    public void saveLicence(LicenceDto licenceDto) {
        licenceClient.doSave(licenceDto);
    }

    @Override
    public List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByUen(String uenNo) {
        return organizationLienceseeClient.getLicenseeKeyApptPersonDtoListByUen(uenNo).getEntity();
    }

    @Override
    public LicenceDto getByLicNo(String licNo) {
        return licenceClient.getLicBylicNo(licNo).getEntity();
    }

    @Override
    public List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByLicenseeId(String licenseeId) {
        return organizationLienceseeClient.getLicenseeKeyApptPersonDtoListByLicenseeId(licenseeId).getEntity();
    }


    @Override
    public List<PersonnelListQueryDto> getLicencePersonnelListQueryDto(String licenseeId) {
        return licenceClient.getPersonnel(licenseeId).getEntity();
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtoByLicenceIds(List<String> licenceIds) {
        return licenceClient.getAppSubmissionDtos(licenceIds).getEntity();
    }

    @Override
    public List<AppSubmissionDto> saveAppsBySubmissionDtos(List<AppSubmissionDto> appSubmissionDtos) {
        return applicationClient.saveAppsForRequestForChangeByList(appSubmissionDtos).getEntity();
    }

    @Override
    public LicenceDto getLicenceDtoByLicenceId(String licenceId) {
        return licenceClient.getLicBylicId(licenceId).getEntity();
    }

    @Override
    public LicenseeIndividualDto getLicIndByNRIC(String nric) {
        return organizationLienceseeClient.getlicIndByNric(nric).getEntity();
    }

    @Override
    public LicenseeDto getLicenseeByUenNo(String uenNo) {
        return organizationLienceseeClient.getLicenseeByUenNo(uenNo).getEntity();
    }

    @Override
    public SearchResult<PersonnelQueryDto> psnDoQuery(SearchParam searchParam) {
        return licenceClient.psnDoQuery(searchParam).getEntity();
    }

    @Override
    public void sendEmail(String appGrpId,String type, String appNo, String serviceName, String licenceNo, Double amount, String licenceeName, String giroNo, String licenseeId, String subject,String aoName) throws Exception {
        //send email  rfc submit and pay giro
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        switch (subject) {
            case "RfcAndGiro":
                MsgTemplateDto RfcAndGiroMsgTemplateDto = msgTemplateClient.getMsgTemplate("D1CC7398-8C50-4178-BE83-1659CD7DBAA8").getEntity();
                if (RfcAndGiroMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("serviceName", StringUtil.viewHtml(serviceName));
                    tempMap.put("amount", amount);
                    tempMap.put("giroNo", giroNo);
                    String mesContext = MsgUtil.getTemplateMessageByContent(RfcAndGiroMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Successful Submission of Request for Change " + appNo);
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "RfcAndOnPay":
                MsgTemplateDto RfcAndOnPayMsgTemplateDto = msgTemplateClient.getMsgTemplate("D9DDBC23-122B-47BA-B579-3B5022816BB6").getEntity();
                if (RfcAndOnPayMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("serviceName", StringUtil.viewHtml(serviceName));
                    tempMap.put("amount", amount);
                    String mesContext = MsgUtil.getTemplateMessageByContent(RfcAndOnPayMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Successful Submission of Request for Change " + appNo);
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "rfcApproval":
                MsgTemplateDto rfcApprovalMsgTemplateDto = msgTemplateClient.getMsgTemplate("25C8B704-1FE1-42DF-B27C-7993B1208BAC").getEntity();
                if (rfcApprovalMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("appNo", appNo);
                    String mesContext = MsgUtil.getTemplateMessageByContent(rfcApprovalMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Request for Change "+appNo+" – Approved ");
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "rfcReject":
                MsgTemplateDto rfcRejectMsgTemplateDto = msgTemplateClient.getMsgTemplate("B6C8231E-940D-485A-BFFB-9E65CADB5CA9").getEntity();
                if (rfcRejectMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("appNo", appNo);
                    String mesContext = MsgUtil.getTemplateMessageByContent(rfcRejectMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Request for Change "+appNo+" – Rejected  ");
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "rfcToLicensee":
                MsgTemplateDto rfcToLicenseeMsgTemplateDto = msgTemplateClient.getMsgTemplate("8D3AC0E0-6684-490C-8DE8-D0452129C67D").getEntity();
                if (rfcToLicenseeMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("licenceeName", licenceeName);
                    tempMap.put("licNo-serviceNameList", serviceName);
                    String mesContext = MsgUtil.getTemplateMessageByContent(rfcToLicenseeMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Notification of Change of Licensee");
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "rfcForInterClarification":
                MsgTemplateDto rfcForInterClMsgTemplateDto = msgTemplateClient.getMsgTemplate("7C6DD026-7EC3-4D58-AAE5-170C8CF208C4").getEntity();
                if (rfcForInterClMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("appNo", appNo);
                    tempMap.put("aoName", aoName);
                    String mesContext = MsgUtil.getTemplateMessageByContent(rfcForInterClMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Internal Clarification for Request for Change "+appNo);
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "rfcToNotificationLicence":
                MsgTemplateDto rfcToNotificationMsgTemplateDto = msgTemplateClient.getMsgTemplate("CC3610B1-01A4-4370-9DEF-936827D2880D").getEntity();
                if (rfcToNotificationMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("licenceeName", licenceeName);
                    tempMap.put("licNo-serviceNameList", serviceName);
                    tempMap.put("result", "Is Approved");
                    String mesContext = MsgUtil.getTemplateMessageByContent(rfcToNotificationMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Notification of Change of Licensee ");
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            default:
        }
    }

    @Override
    public List<LicenceDto> getLicenceDtoByPremisesId(String premisesId) {
        return licenceClient.getLicenceDtosBypremisesId(premisesId).getEntity();
    }

    @Override
    public SearchResult<PremisesListQueryDto> searchPreInfo(SearchParam searchParam) {
        return licenceClient.getPremises(searchParam).getEntity();
    }

    @Override
    public LicenceDto getLicenceById(String licenceId) {
        return licenceClient.getLicBylicId(licenceId).getEntity();
    }

    @Override
    public List<LicenceDto> getLicenceDtoByHciCode(String hciCode) {
        return  licenceClient.getLicenceDtoByHciCode(hciCode).getEntity();
    }

    @Override
    public String sendNotification(EmailDto email){
        return feEmailClient.sendNotification(email).getEntity();
    }


    @Override
    public List<LicKeyPersonnelDto> getLicKeyPersonnelDtoByPerId(List<String> personIds) {
        List<LicKeyPersonnelDto> entity = licenceClient.getLicBypersonId(personIds).getEntity();
        return entity;
    }

    @Override
    public List<String> getPersonnelIdsByIdNo(String idNo) {
        List<String> entity = licenceClient.getPersonnelDtoByIdNo(idNo).getEntity();
        return entity;
    }

    @Override
    public String getIdNoByLicId(String licId) {
        List<PersonnelsDto> entity = licenceClient.getPersonnelDtoByLicId(licId).getEntity();
        String idNo = null;
        if(!IaisCommonUtils.isEmpty(entity)){
            idNo = entity.get(0).getKeyPersonnelDto().getIdNo();
        }
        return idNo;
    }

    @Override
    public List<AppSubmissionDto> saveAppsForRequestForGoupAndAppChangeByList(List<AppSubmissionDto> appSubmissionDtos) {
        List<AppSubmissionDto> entity = applicationClient.saveAppsForRequestForGoupAndAppChangeByList(appSubmissionDtos).getEntity();

        return entity;
    }

    @Override
    public void sendMessageHelper(String subject, String messageType, String srcSystemId,String serviceId, String licenseeId, String templateMessageByContent, HashMap<String, String> maskParams){
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(srcSystemId);
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(messageType);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String refNo = feEicGatewayClient.getMessageNo(signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        interMessageDto.setRefNo(refNo);
        interMessageDto.setService_id(serviceId);
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        feMessageClient.createInboxMessage(interMessageDto);
    }

    @Override
    public List<String> getAdminEmail(String orgId){
        return organizationLienceseeClient.getAdminEmailAdd(orgId).getEntity();
    }

    @Override
    public Boolean isOtherOperation(String licenceId) {

        return   applicationClient.isLiscenceAppealOrCessation(licenceId).getEntity();
    }
    @Override
    public  Map<String, String> doValidatePremiss( AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto, List<String> premisesHciList, String masterCodeDto, boolean rfi ) {
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        //do validate one premiss
        List<String> list=IaisCommonUtils.genNewArrayList();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Set<String> distinctVehicleNo = IaisCommonUtils.genNewHashSet();
        String oldPremiseHci = "";
        //new rfi
        boolean appTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
        if((appTypeFlag && rfi) && (oldAppSubmissionDto != null)){
            AppGrpPremisesDto oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0);
            String premiseKey = NewApplicationHelper.getPremKey(oldAppGrpPremisesDto);
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(oldAppGrpPremisesDto.getPremisesType())){
                oldPremiseHci = oldAppGrpPremisesDto.getHciName() + premiseKey;
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(oldAppGrpPremisesDto.getPremisesType())){
                oldPremiseHci = oldAppGrpPremisesDto.getConveyanceVehicleNo() + premiseKey;
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(oldAppGrpPremisesDto.getPremisesType())){
                oldPremiseHci = premiseKey;
            }
        }
        for(int i=0;i<appGrpPremisesDtoList.size();i++){
            String premiseType = appGrpPremisesDtoList.get(i).getPremisesType();
            if (StringUtil.isEmpty(premiseType)) {
                errorMap.put("premisesType"+i, "UC_CHKLMD001_ERR001");
            }else {
                String premisesSelect = appGrpPremisesDtoList.get(i).getPremisesSelect();
                String appType = appSubmissionDto.getAppType();
                boolean needValidate = false;

                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                    String oldPremSel = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0).getPremisesSelect();
                    if(!StringUtil.isEmpty(oldPremSel) && oldPremSel.equals(premisesSelect)){
                        needValidate = true;
                    }
                }
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
                if (StringUtil.isEmpty(premisesSelect) || "-1".equals(premisesSelect)) {
                    errorMap.put("premisesSelect"+i, "UC_CHKLMD001_ERR001");
                } else if ( needValidate||!StringUtil.isEmpty(premisesSelect)||"newPremise".equals(premisesSelect) ) {
                    StringBuilder stringBuilder=new StringBuilder();
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)) {
                        String onsiteStartHH = appGrpPremisesDtoList.get(i).getOnsiteStartHH();
                        String onsiteStartMM = appGrpPremisesDtoList.get(i).getOnsiteStartMM();
                        int startDate=0;
                        int endDate=0;
                        if(StringUtil.isEmpty(onsiteStartHH)||StringUtil.isEmpty(onsiteStartMM)){
                            errorMap.put("onsiteStartMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            startDate = validateTime(errorMap, onsiteStartHH, onsiteStartMM, startDate, "onsiteStartMM", i);
                        }

                        String onsiteEndHH = appGrpPremisesDtoList.get(i).getOnsiteEndHH();
                        String onsiteEndMM = appGrpPremisesDtoList.get(i).getOnsiteEndMM();
                        if(StringUtil.isEmpty(onsiteEndHH)||StringUtil.isEmpty(onsiteEndMM)){
                            errorMap.put("onsiteEndMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            endDate = validateTime(errorMap, onsiteEndHH, onsiteEndMM, endDate, "onsiteEndMM", i);
                        }
                        if(!StringUtil.isEmpty(onsiteStartHH)&&!StringUtil.isEmpty(onsiteStartMM)&&!StringUtil.isEmpty(onsiteEndHH)&&!StringUtil.isEmpty(onsiteEndMM)){
                            if(endDate<startDate){
                                errorMap.put("onsiteEndMM"+i,"UC_CHKLMD001_ERR008");
                            }
                        }

                        /*Boolean isOtherLic = appGrpPremisesDtoList.get(i).isLocateWithOthers();
                        if(StringUtil.isEmpty(isOtherLic)){
                            errorMap.put("isOtherLic"+i,"UC_CHKLMD001_ERR002");
                        }*/

                        //set  time
                        if(!errorMap.containsKey("onsiteStartMM"+i) && !errorMap.containsKey("onsiteEndMM"+i)){
                            LocalTime startTime = LocalTime.of(Integer.parseInt(onsiteStartHH),Integer.parseInt(onsiteStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(onsiteEndHH),Integer.parseInt(onsiteEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if(!IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)){
                            for(int j=0;j<appPremPhOpenPeriodList.size();j++){
                                AppPremPhOpenPeriodDto appPremPhOpenPeriodDto = appPremPhOpenPeriodList.get(j);
                                String convStartFromHH = appPremPhOpenPeriodDto.getOnsiteStartFromHH();
                                String convStartFromMM = appPremPhOpenPeriodDto.getOnsiteStartFromMM();
                                String onsiteEndToHH = appPremPhOpenPeriodDto.getOnsiteEndToHH();
                                String onsiteEndToMM = appPremPhOpenPeriodDto.getOnsiteEndToMM();
                                Date phDate = appPremPhOpenPeriodDto.getPhDate();
                                if(!StringUtil.isEmpty(phDate)){
                                    if(StringUtil.isEmpty(convStartFromHH)||StringUtil.isEmpty(convStartFromMM)){
                                        errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                    if(StringUtil.isEmpty(onsiteEndToHH)||StringUtil.isEmpty(onsiteEndToMM)){
                                        errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                }else if(StringUtil.isEmpty(phDate)){
                                    errorMap.put("onsitephDate"+i+j,"UC_CHKLMD001_ERR001");
                                }
                                if(!StringUtil.isEmpty(convStartFromHH)&&!StringUtil.isEmpty(convStartFromMM)&&!StringUtil.isEmpty(onsiteEndToHH)
                                        &&!StringUtil.isEmpty(onsiteEndToMM)||StringUtil.isEmpty(convStartFromHH)&&StringUtil.isEmpty(convStartFromMM)
                                        &&StringUtil.isEmpty(onsiteEndToHH)&&StringUtil.isEmpty(onsiteEndToMM)){
                                    if(!StringUtil.isEmpty(convStartFromHH)&&!StringUtil.isEmpty(convStartFromMM)&&!StringUtil.isEmpty(onsiteEndToHH)
                                            &&!StringUtil.isEmpty(onsiteEndToMM)){
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);

                                            if(i1>=24){
                                                errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("onsiteStartToMM"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i3 = Integer.parseInt(onsiteEndToHH);
                                            int i4 = Integer.parseInt(onsiteEndToMM);
                                            if(i3>=24){
                                                errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("onsiteEndToMM"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            int i3 = Integer.parseInt(onsiteEndToHH);
                                            int i4 = Integer.parseInt(onsiteEndToMM);
                                            if((i1*60+i2)>(i3*60+i4)){
                                                errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR008");
                                            }
                                        }catch (Exception e){
                                        }
                                    }


                                }else {
                                    if(StringUtil.isEmpty(convStartFromHH)&&StringUtil.isEmpty(convStartFromMM)||StringUtil.isEmpty(convStartFromMM)||StringUtil.isEmpty(convStartFromHH)){
                                        errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);

                                            if(i1>=24){
                                                errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("onsiteStartToMM"+i+j,"CHKLMD001_ERR003");
                                        }
                                    }
                                    if(StringUtil.isEmpty(onsiteEndToHH)&&StringUtil.isEmpty(onsiteEndToMM)||StringUtil.isEmpty(onsiteEndToHH)||StringUtil.isEmpty(onsiteEndToMM)){
                                        errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i3 = Integer.parseInt(onsiteEndToHH);
                                            int i4 = Integer.parseInt(onsiteEndToMM);
                                            if(i3>=24){
                                                errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("onsiteEndToMM"+i+j,"CHKLMD001_ERR003");
                                        }

                                    }
                                }
                                //set ph time
                                String errorOnsiteStartToMM = errorMap.get("onsiteStartToMM"+i+j);
                                String errorOnsiteEndToMM = errorMap.get("onsiteEndToMM"+i+j);
                                if(StringUtil.isEmpty(errorOnsiteEndToMM) && StringUtil.isEmpty(errorOnsiteStartToMM) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)){
                                    LocalTime startTime = LocalTime.of(Integer.parseInt(convStartFromHH),Integer.parseInt(convStartFromMM));
                                    appPremPhOpenPeriodDto.setStartFrom(Time.valueOf(startTime));
                                    LocalTime endTime = LocalTime.of(Integer.parseInt(onsiteEndToHH),Integer.parseInt(onsiteEndToMM));
                                    appPremPhOpenPeriodDto.setEndTo(Time.valueOf(endTime));
                                }
                            }
                        }
                        String hciName = appGrpPremisesDtoList.get(i).getHciName();
                        if(StringUtil.isEmpty(hciName)){
                            errorMap.put("hciName"+i,"UC_CHKLMD001_ERR001");
                        } else {

                            if(masterCodeDto!=null){
                                String[] s = masterCodeDto.split(" ");
                                for(int index=0;index<s.length;index++){
                                    if(hciName.toUpperCase().contains(s[index].toUpperCase())){
                                        errorMap.put("hciName"+i,"CHKLMD001_ERR002");
                                    }
                                }
                            }
                            String licenseeId = appSubmissionDto.getLicenseeId();
                            if(StringUtil.isEmpty(licenseeId)){
                                licenseeId="9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
                            }
                            List<AppGrpPremisesDto> entity = applicationClient.getAppGrpPremisesDtoByHciName(hciName,licenseeId).getEntity();
                            if(!entity.isEmpty()){
                                errorMap.put("hciNameUsed","The HCI name you have keyed in is currently in used");
                            }
                        }
                        String offTelNo = appGrpPremisesDtoList.get(i).getOffTelNo();
                        if(StringUtil.isEmpty(offTelNo)){
                            errorMap.put("offTelNo"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            boolean matches = offTelNo.matches("^[6][0-9]{7}$");
                            if(!matches) {
                                errorMap.put("offTelNo"+i,"GENERAL_ERR0015");
                            }
                        }

                        String streetName = appGrpPremisesDtoList.get(i).getStreetName();
                        if(StringUtil.isEmpty(streetName)){
                            errorMap.put("streetName"+i,"UC_CHKLMD001_ERR001");
                        }

                        String addrType = appGrpPremisesDtoList.get(i).getAddrType();

                        if(StringUtil.isEmpty(addrType)){
                            errorMap.put("addrType"+i, "UC_CHKLMD001_ERR001");
                        }else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getBlkNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {

                                if (empty) {
                                    errorMap.put("floorNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty1) {
                                    errorMap.put("blkNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty2) {
                                    errorMap.put("unitNo"+i, "UC_CHKLMD001_ERR001");
                                }
                            }
                            String floorNoErr = errorMap.get("floorNo"+i);
                            String floorNo = appGrpPremisesDtoList.get(i).getFloorNo();
                            if(StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)){
                                Pattern pattern = compile("[0-9]*");
                                boolean noFlag =  pattern.matcher(floorNo).matches();
                                if (noFlag) {
                                    int floorNum = Integer.parseInt(floorNo);
                                    if (10 > floorNum) {
                                        floorNo = "0" + floorNum;
                                        appGrpPremisesDtoList.get(i).setFloorNo(floorNo);
                                    }
                                }

                            }
                            if(!empty&&!empty1&&!empty2){
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getBlkNo())
                                        .append(appGrpPremisesDtoList.get(i).getUnitNo());
                            }
                        }
                        String postalCode = appGrpPremisesDtoList.get(i).getPostalCode();
                        if (!StringUtil.isEmpty(postalCode)) {
                            if (!postalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("postalCode"+i, "NEW_ERR0004");
                            }else {

                                if(!StringUtil.isEmpty(stringBuilder.toString())){
                                    stringBuilder.append(postalCode);
                                    if(list.contains(stringBuilder.toString())){
                                        errorMap.put("postalCode"+i,"There is a duplicated entry for this premises address");

                                    }else {
                                        list.add(stringBuilder.toString());
                                    }
                                }
                            }
                        }else {
                            errorMap.put("postalCode"+i, "UC_CHKLMD001_ERR001");
                        }
                        //0062204
                        String currentHci = hciName + IaisCommonUtils.genPremisesKey(postalCode,appGrpPremisesDto.getBlkNo(),appGrpPremisesDto.getFloorNo(),appGrpPremisesDto.getUnitNo());
                        String hciNameErr = errorMap.get("hciName"+i);
                        String postalCodeErr =errorMap.get("postalCode"+i);
                        String blkNoErr =errorMap.get("blkNo"+i);
                        String floorNoErr =errorMap.get("floorNo"+i);
                        String unitNoErr =errorMap.get("unitNo"+i);
                        boolean clickEdit = appGrpPremisesDto.isClickEdit();
                        boolean hciFlag =  StringUtil.isEmpty(hciNameErr) && StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:"+hciFlag));
                        boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                        if((newTypeFlag && hciFlag) && !rfi){
                            //new
                            if(!IaisCommonUtils.isEmpty(premisesHciList) && premisesHciList.contains(currentHci)){
                                errorMap.put("premisesHci"+i,"NEW_ERR0005");
                            }
                        }else if(((newTypeFlag && hciFlag) && rfi) && clickEdit){
                            //new rfi
                            if(!IaisCommonUtils.isEmpty(premisesHciList) && !oldPremiseHci.equals(currentHci) && premisesHciList.contains(currentHci)){
                                errorMap.put("premisesHci"+i,"NEW_ERR0005");
                            }
                        }
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        String conStartHH = appGrpPremisesDtoList.get(i).getConStartHH();
                        String conStartMM = appGrpPremisesDtoList.get(i).getConStartMM();
                        int conStartDate=0;
                        int conEndDate=0;

                        if(StringUtil.isEmpty(conStartHH)||StringUtil.isEmpty(conStartMM)){
                            errorMap.put("conStartMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            conStartDate = validateTime(errorMap, conStartHH, conStartMM, conStartDate, "conStartMM", i);
                        }
                        String conEndHH = appGrpPremisesDtoList.get(i).getConEndHH();
                        String conEndMM = appGrpPremisesDtoList.get(i).getConEndMM();
                        if(StringUtil.isEmpty(conEndHH)||StringUtil.isEmpty(conEndMM)){
                            errorMap.put("conEndMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            conEndDate = validateTime(errorMap, conEndHH, conEndMM, conEndDate, "conEndMM", i);
                        }
                        if(!StringUtil.isEmpty(conStartHH)&&!StringUtil.isEmpty(conStartMM)&&!StringUtil.isEmpty(conEndHH)&&!StringUtil.isEmpty(conEndMM)){
                            if(conEndDate<conStartDate){
                                errorMap.put("conEndMM"+i,"UC_CHKLMD001_ERR008");
                            }
                        }


                        //set  time
                        String errorStartMM = errorMap.get("conStartMM"+i);
                        String errorEndMM = errorMap.get("conEndMM"+i);
                        if(StringUtil.isEmpty(errorStartMM) && StringUtil.isEmpty(errorEndMM)){
                            LocalTime startTime = LocalTime.of(Integer.parseInt(conStartHH),Integer.parseInt(conStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(conEndHH),Integer.parseInt(conEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if(appPremPhOpenPeriodList!=null){
                            for(int j=0;j<appPremPhOpenPeriodList.size();j++){
                                AppPremPhOpenPeriodDto appPremPhOpenPeriodDto = appPremPhOpenPeriodList.get(j);
                                String convEndToHH = appPremPhOpenPeriodDto.getConvEndToHH();
                                String convEndToMM = appPremPhOpenPeriodDto.getConvEndToMM();
                                String convStartFromHH = appPremPhOpenPeriodDto.getConvStartFromHH();
                                String convStartFromMM = appPremPhOpenPeriodDto.getConvStartFromMM();
                                Date phDate = appPremPhOpenPeriodDto.getPhDate();
                                if(!StringUtil.isEmpty(phDate)){
                                    if(StringUtil.isEmpty(convEndToHH)||StringUtil.isEmpty(convEndToMM)){
                                        errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                    if(StringUtil.isEmpty(convStartFromHH)||StringUtil.isEmpty(convStartFromMM)){
                                        errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                }else if(StringUtil.isEmpty(phDate)){
                                    errorMap.put("convphDate"+i+j,"UC_CHKLMD001_ERR001");
                                }

                                if(StringUtil.isEmpty(convEndToHH)&&StringUtil.isEmpty(convEndToMM)&StringUtil.isEmpty(convStartFromHH)
                                        &StringUtil.isEmpty(convStartFromMM)||!StringUtil.isEmpty(convEndToHH)&&!StringUtil.isEmpty(convEndToMM)
                                        &&!StringUtil.isEmpty(convStartFromHH)&!StringUtil.isEmpty(convStartFromMM)){
                                    if(!StringUtil.isEmpty(convEndToHH)&&!StringUtil.isEmpty(convEndToMM)
                                            &&!StringUtil.isEmpty(convStartFromHH)&!StringUtil.isEmpty(convStartFromMM)){
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            if(i1>=24){
                                                errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("convStartToHH"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if(i3>=24){
                                                errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("convEndToHH"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if((i1*60+i2)>(i3*60+i4)){
                                                errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR008");
                                            }
                                        }catch (Exception e){

                                        }
                                    }
                                }else {
                                    if(StringUtil.isEmpty(convStartFromHH)||StringUtil.isEmpty(convStartFromMM)||StringUtil.isEmpty(convStartFromMM)&&StringUtil.isEmpty(convStartFromHH)){
                                        errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            if(i1>=24){
                                                errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){


                                        }
                                    }
                                    if(StringUtil.isEmpty(convEndToHH)||StringUtil.isEmpty(convEndToMM)||StringUtil.isEmpty(convEndToHH)&&StringUtil.isEmpty(convEndToMM)){
                                        errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if(i3>=24){
                                                errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){


                                        }
                                    }
                                }
                                //set ph time
                                String errorConvStartToMM = errorMap.get("convStartToHH"+i+j);
                                String errorConvEndToMM = errorMap.get("convEndToHH"+i+j);
                                if(StringUtil.isEmpty(errorConvStartToMM) && StringUtil.isEmpty(errorConvEndToMM) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)){
                                    LocalTime startTime = LocalTime.of(Integer.parseInt(convStartFromHH),Integer.parseInt(convStartFromMM));
                                    appPremPhOpenPeriodDto.setStartFrom(Time.valueOf(startTime));
                                    LocalTime endTime = LocalTime.of(Integer.parseInt(convEndToHH),Integer.parseInt(convEndToMM));
                                    appPremPhOpenPeriodDto.setEndTo(Time.valueOf(endTime));
                                }
                            }
                        }
                        String conveyanceVehicleNo = appGrpPremisesDtoList.get(i).getConveyanceVehicleNo();
                        validateVehicleNo(errorMap, distinctVehicleNo, i, conveyanceVehicleNo);

                        String cStreetName = appGrpPremisesDtoList.get(i).getConveyanceStreetName();
                        if(StringUtil.isEmpty(cStreetName)){
                            errorMap.put("conveyanceStreetName"+i,"UC_CHKLMD001_ERR001");
                        }
                        String conveyanceAddressType = appGrpPremisesDtoList.get(i).getConveyanceAddressType();
                        if(StringUtil.isEmpty(conveyanceAddressType)){
                            errorMap.put("conveyanceAddressType"+i, "UC_CHKLMD001_ERR001");
                        }else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceBlockNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(conveyanceAddressType)) {

                                if (empty) {
                                    errorMap.put("conveyanceFloorNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty1) {
                                    errorMap.put("conveyanceBlockNos"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty2) {
                                    errorMap.put("conveyanceUnitNo"+i, "UC_CHKLMD001_ERR001");
                                }

                            }
                            String floorNoErr = errorMap.get("conveyanceFloorNo"+i);
                            String floorNo = appGrpPremisesDtoList.get(i).getConveyanceFloorNo();
                            if(StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)){
                                Pattern pattern = compile("[0-9]*");
                                boolean noFlag =  pattern.matcher(floorNo).matches();
                                if (noFlag) {
                                    int floorNum = Integer.parseInt(floorNo);
                                    if (10 > floorNum) {
                                        floorNo = "0" + floorNum;
                                        appGrpPremisesDtoList.get(i).setConveyanceFloorNo(floorNo);
                                    }
                                }

                            }
                            if(!empty&&!empty1&&!empty2){
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getConveyanceFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getConveyanceBlockNo())
                                        .append(appGrpPremisesDtoList.get(i).getConveyanceUnitNo());
                            }
                        }
                        String conveyancePostalCode = appGrpPremisesDtoList.get(i).getConveyancePostalCode();
                        if(StringUtil.isEmpty(conveyancePostalCode)){
                            errorMap.put("conveyancePostalCode"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            if(!conveyancePostalCode.matches("^[0-9]{6}$")){
                                errorMap.put("conveyancePostalCode"+i, "NEW_ERR0004");
                            }else {
                                if(!StringUtil.isEmpty(stringBuilder.toString())){
                                    stringBuilder.append(conveyancePostalCode);
                                    if(list.contains(stringBuilder.toString())){
                                        errorMap.put("conveyancePostalCode"+i,"There is a duplicated entry for this premises address.");
                                    }else {
                                        list.add(stringBuilder.toString());
                                    }
                                }
                            }
                        }

                        //0062204
                        String currentHci = conveyanceVehicleNo + IaisCommonUtils.genPremisesKey(conveyancePostalCode,appGrpPremisesDto.getConveyanceBlockNo(),appGrpPremisesDto.getConveyanceFloorNo(),appGrpPremisesDto.getConveyanceUnitNo());
                        String vehicleNo = errorMap.get("conveyanceVehicleNo"+i);
                        String postalCodeErr =errorMap.get("conveyancePostalCode"+i);
                        String blkNoErr =errorMap.get("conveyanceBlockNos"+i);
                        String floorNoErr =errorMap.get("conveyanceFloorNo"+i);
                        String unitNoErr =errorMap.get("conveyanceUnitNo"+i);
                        boolean clickEdit = appGrpPremisesDto.isClickEdit();
                        boolean hciFlag =  StringUtil.isEmpty(vehicleNo) && StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:"+hciFlag));
                        boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                        if(newTypeFlag && hciFlag && !rfi){
                            //new
                            if(!IaisCommonUtils.isEmpty(premisesHciList) && premisesHciList.contains(currentHci)){
                                errorMap.put("premisesHci"+i,"NEW_ERR0005");
                            }
                        }else if(newTypeFlag && hciFlag && rfi && clickEdit){
                            //new rfi
                            if(!IaisCommonUtils.isEmpty(premisesHciList) && !oldPremiseHci.equals(currentHci) && premisesHciList.contains(currentHci)){
                                errorMap.put("premisesHci"+i,"NEW_ERR0005");
                            }
                        }
                    }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premiseType)){

                        String offSitePostalCode = appGrpPremisesDtoList.get(i).getOffSitePostalCode();
                        if (!StringUtil.isEmpty(offSitePostalCode)) {
                            if (!offSitePostalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("offSitePostalCode"+i, "NEW_ERR0004");
                            }else {

                                if(!StringUtil.isEmpty(stringBuilder.toString())){
                                    stringBuilder.append(offSitePostalCode);
                                    if(list.contains(stringBuilder.toString())){
                                        errorMap.put("offSitePostalCode"+i,"There is a duplicated entry for this premises address");

                                    }else {
                                        list.add(stringBuilder.toString());
                                    }
                                }
                            }
                        }else {
                            errorMap.put("offSitePostalCode"+i, "UC_CHKLMD001_ERR001");
                        }

                        String offSiteStreetName = appGrpPremisesDtoList.get(i).getOffSiteStreetName();
                        if(StringUtil.isEmpty(offSiteStreetName)){
                            errorMap.put("offSiteStreetName"+i,"UC_CHKLMD001_ERR001");
                        }

                        String offSiteAddressType = appGrpPremisesDtoList.get(i).getOffSiteAddressType();

                        if(StringUtil.isEmpty(offSiteAddressType)){
                            errorMap.put("offSiteAddressType"+i, "UC_CHKLMD001_ERR001");
                        }else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getOffSiteFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getOffSiteBlockNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getOffSiteUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(offSiteAddressType)) {

                                if (empty) {
                                    errorMap.put("offSiteFloorNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty1) {
                                    errorMap.put("offSiteBlockNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty2) {
                                    errorMap.put("offSiteUnitNo"+i, "UC_CHKLMD001_ERR001");
                                }
                            }
                            String floorNoErr = errorMap.get("offSiteFloorNo"+i);
                            String floorNo = appGrpPremisesDtoList.get(i).getOffSiteFloorNo();
                            if(StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)){
                                Pattern pattern = compile("[0-9]*");
                                boolean noFlag =  pattern.matcher(floorNo).matches();
                                if (noFlag) {
                                    int floorNum = Integer.parseInt(floorNo);
                                    if (10 > floorNum) {
                                        floorNo = "0" + floorNum;
                                        appGrpPremisesDtoList.get(i).setOffSiteFloorNo(floorNo);
                                    }
                                }

                            }
                            if(!empty&&!empty1&&!empty2){
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getBlkNo())
                                        .append(appGrpPremisesDtoList.get(i).getUnitNo());
                            }
                        }
                        String offSiteStartHH = appGrpPremisesDtoList.get(i).getOffSiteStartHH();
                        String offSiteStartMM = appGrpPremisesDtoList.get(i).getOffSiteStartMM();
                        int startDate=0;
                        int endDate=0;
                        if(StringUtil.isEmpty(offSiteStartHH)||StringUtil.isEmpty(offSiteStartMM)){
                            errorMap.put("offSiteStartMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            startDate = validateTime(errorMap, offSiteStartHH, offSiteStartMM, startDate, "offSiteStartMM", i);
                        }

                        String offSiteEndHH = appGrpPremisesDtoList.get(i).getOffSiteEndHH();
                        String offSiteEndMM = appGrpPremisesDtoList.get(i).getOffSiteEndMM();
                        if(StringUtil.isEmpty(offSiteEndHH)||StringUtil.isEmpty(offSiteEndMM)){
                            errorMap.put("offSiteEndMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            endDate = validateTime(errorMap, offSiteEndHH, offSiteEndMM, endDate, "offSiteEndMM", i);
                        }
                        if(!StringUtil.isEmpty(offSiteStartHH)&&!StringUtil.isEmpty(offSiteStartMM)&&!StringUtil.isEmpty(offSiteEndHH)&&!StringUtil.isEmpty(offSiteEndMM)){
                            if(endDate<startDate){
                                errorMap.put("offSiteEndMM"+i,"UC_CHKLMD001_ERR008");
                            }
                        }

                        //set  time
                        String errorStartMM = errorMap.get("offSiteStartMM"+i);
                        String errorEndMM = errorMap.get("offSiteEndMM"+i);
                        if(StringUtil.isEmpty(errorStartMM) && StringUtil.isEmpty(errorEndMM)){
                            LocalTime startTime = LocalTime.of(Integer.parseInt(offSiteStartHH),Integer.parseInt(offSiteStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(offSiteEndHH),Integer.parseInt(offSiteEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if(appPremPhOpenPeriodList!=null){

                            for(int j=0;j<appPremPhOpenPeriodList.size();j++){
                                AppPremPhOpenPeriodDto appPremPhOpenPeriodDto = appPremPhOpenPeriodList.get(j);
                                String offSiteEndToHH = appPremPhOpenPeriodDto.getOffSiteEndToHH();
                                String offSiteEndToMM = appPremPhOpenPeriodDto.getOffSiteEndToMM();
                                String offSiteStartFromHH = appPremPhOpenPeriodDto.getOffSiteStartFromHH();
                                String offSiteStartFromMM = appPremPhOpenPeriodDto.getOffSiteStartFromMM();
                                Date phDate = appPremPhOpenPeriodDto.getPhDate();
                                if(!StringUtil.isEmpty(phDate)){
                                    if(StringUtil.isEmpty(offSiteEndToHH)||StringUtil.isEmpty(offSiteEndToMM)){
                                        errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                    if(StringUtil.isEmpty(offSiteStartFromHH)||StringUtil.isEmpty(offSiteStartFromMM)){
                                        errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                }else if(StringUtil.isEmpty(phDate)){
                                    errorMap.put("offSitephDate"+i+j,"UC_CHKLMD001_ERR001");
                                }

                                if(StringUtil.isEmpty(offSiteEndToHH)&&StringUtil.isEmpty(offSiteEndToMM)&StringUtil.isEmpty(offSiteStartFromHH)
                                        &StringUtil.isEmpty(offSiteStartFromMM)||!StringUtil.isEmpty(offSiteEndToHH)&&!StringUtil.isEmpty(offSiteEndToMM)
                                        &&!StringUtil.isEmpty(offSiteStartFromHH)&!StringUtil.isEmpty(offSiteStartFromMM)){
                                    if(!StringUtil.isEmpty(offSiteEndToHH)&&!StringUtil.isEmpty(offSiteEndToMM)
                                            &&!StringUtil.isEmpty(offSiteEndToHH)&!StringUtil.isEmpty(offSiteStartFromMM)){
                                        try {
                                            int i1 = Integer.parseInt(offSiteStartFromHH);
                                            int i2 = Integer.parseInt(offSiteStartFromMM);
                                            if(i1>=24){
                                                errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("offSiteStartToHH"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i3 = Integer.parseInt(offSiteEndToHH);
                                            int i4 = Integer.parseInt(offSiteEndToMM);
                                            if(i3>=24){
                                                errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("offSiteEndToHH"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i1 = Integer.parseInt(offSiteStartFromHH);
                                            int i2 = Integer.parseInt(offSiteStartFromMM);
                                            int i3 = Integer.parseInt(offSiteEndToHH);
                                            int i4 = Integer.parseInt(offSiteEndToMM);
                                            if((i1*60+i2)>(i3*60+i4)){
                                                errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR008");
                                            }
                                        }catch (Exception e){

                                        }
                                    }
                                }else {
                                    if(StringUtil.isEmpty(offSiteStartFromHH)||StringUtil.isEmpty(offSiteStartFromMM)||StringUtil.isEmpty(offSiteStartFromHH)&&StringUtil.isEmpty(offSiteStartFromMM)){
                                        errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i1 = Integer.parseInt(offSiteStartFromHH);
                                            int i2 = Integer.parseInt(offSiteStartFromMM);
                                            if(i1>=24){
                                                errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("offSiteStartToHH"+i+j,"CHKLMD001_ERR003");

                                        }
                                    }
                                    if(StringUtil.isEmpty(offSiteEndToHH)||StringUtil.isEmpty(offSiteEndToMM)||StringUtil.isEmpty(offSiteEndToHH)&&StringUtil.isEmpty(offSiteEndToMM)){
                                        errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {

                                        try {
                                            int i3 = Integer.parseInt(offSiteEndToHH);
                                            int i4 = Integer.parseInt(offSiteEndToMM);
                                            if(i3>=24){
                                                errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("offSiteEndToHH"+i+j,"CHKLMD001_ERR003");

                                        }
                                    }
                                }

                                //set ph time
                                String errorOffSiteStartToMM = errorMap.get("offSiteStartToHH"+i+j);
                                String errorOffSiteEndToMM = errorMap.get("offSiteEndToHH"+i+j);
                                if(StringUtil.isEmpty(errorOffSiteStartToMM) && StringUtil.isEmpty(errorOffSiteEndToMM) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)){
                                    LocalTime startTime = LocalTime.of(Integer.parseInt(offSiteStartFromHH),Integer.parseInt(offSiteStartFromMM));
                                    appPremPhOpenPeriodDto.setStartFrom(Time.valueOf(startTime));
                                    LocalTime endTime = LocalTime.of(Integer.parseInt(offSiteEndToHH),Integer.parseInt(offSiteEndToMM));
                                    appPremPhOpenPeriodDto.setEndTo(Time.valueOf(endTime));
                                }
                            }
                        }
                        //0062204
                        String currentHci = IaisCommonUtils.genPremisesKey(offSitePostalCode,appGrpPremisesDto.getOffSiteBlockNo(),appGrpPremisesDto.getOffSiteFloorNo(),appGrpPremisesDto.getOffSiteUnitNo());
                        String postalCodeErr =errorMap.get("offSitePostalCode"+i);
                        String blkNoErr =errorMap.get("offSiteBlockNo"+i);
                        String floorNoErr =errorMap.get("offSiteFloorNo"+i);
                        String unitNoErr =errorMap.get("offSiteUnitNo"+i);
                        boolean clickEdit = appGrpPremisesDto.isClickEdit();
                        boolean hciFlag = StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:"+hciFlag));
                        boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                        if(newTypeFlag && hciFlag && !rfi){
                            //new
                            if(!IaisCommonUtils.isEmpty(premisesHciList) && premisesHciList.contains(currentHci)){
                                errorMap.put("premisesHci"+i,"NEW_ERR0005");
                            }
                        }else if(newTypeFlag && hciFlag && rfi && clickEdit){
                            //new rfi
                            if(!IaisCommonUtils.isEmpty(premisesHciList) && !oldPremiseHci.equals(currentHci) && premisesHciList.contains(currentHci)){
                                errorMap.put("premisesHci"+i,"NEW_ERR0005");
                            }
                        }

                    }


                } else {
                    //premiseSelect = organization hci code

                    if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)){
                        String conveyanceVehicleNo = appGrpPremisesDtoList.get(i).getConveyanceVehicleNo();
                        validateVehicleNo(errorMap, distinctVehicleNo, i, conveyanceVehicleNo);
                    }

                }
            }
        }
        log.info(StringUtil.changeForLog("the do doValidatePremiss end ...."));

        return errorMap;
    }

    @Override
    public void svcDocToPresmise(AppSubmissionDto appSubmissionDto) {
        if(appSubmissionDto==null){
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos=IaisCommonUtils.genNewArrayList();
        List<AppSvcDocDto> appSvcDocDtos=IaisCommonUtils.genNewArrayList();
        if(appSvcDocDtoLit!=null){
            for(AppSvcDocDto appSvcDocDto : appSvcDocDtoLit){
                String svcDocId = appSvcDocDto.getSvcDocId();
                if(StringUtil.isEmpty(svcDocId)){
                    continue;
                }
                HcsaSvcDocConfigDto entity = appConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if(entity!=null){
                    String serviceId = entity.getServiceId();
                    if(StringUtil.isEmpty(serviceId)){
                        AppGrpPrimaryDocDto appGrpPrimaryDocDto= new  AppGrpPrimaryDocDto();
                        appGrpPrimaryDocDto.setSvcDocId(svcDocId);
                        appGrpPrimaryDocDto.setSvcComDocId(svcDocId);
                        appGrpPrimaryDocDto.setSvcComDocName(entity.getDocTitle());
                        appGrpPrimaryDocDto.setDocName(appSvcDocDto.getDocName());
                        appGrpPrimaryDocDto.setAppGrpId(appSubmissionDto.getAppGrpId());
                        appGrpPrimaryDocDto.setDocSize(appSvcDocDto.getDocSize());
                        appGrpPrimaryDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
                        appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                        appSvcDocDtos.add(appSvcDocDto);
                    }
                }
            }
            appSvcDocDtoLit.removeAll(appSvcDocDtos);
            for(int i=0;i < appSvcDocDtoLit.size();i++){
                for(int j=0;j < appSvcDocDtoLit.size() && j!=i;j++){
                    if(appSvcDocDtoLit.get(i).getFileRepoId().equals(appSvcDocDtoLit.get(j).getFileRepoId())){
                        appSvcDocDtoLit.remove(appSvcDocDtoLit.get(i));
                        i--;
                        break;
                    }
                }
            }
        }
        if(dtoAppGrpPrimaryDocDtos!=null){
            if(appGrpPrimaryDocDtos.isEmpty()){
                appGrpPrimaryDocDtos.addAll(dtoAppGrpPrimaryDocDtos);
            }else {
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList=IaisCommonUtils.genNewArrayList();
                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto1 : dtoAppGrpPrimaryDocDtos){
                    for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos){
                        String svcDocId = appGrpPrimaryDocDto.getSvcDocId();
                        String svcDocId1 = appGrpPrimaryDocDto1.getSvcDocId();
                        if(svcDocId1!=null){
                            if(svcDocId1.equals(svcDocId)){
                                continue;
                            }else {
                                appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto1);
                            }
                        }else if(svcDocId!=null){
                            if(svcDocId.equals(svcDocId1)){
                                continue;
                            }else {
                                appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto1);
                            }
                        }
                    }
                }
                appGrpPrimaryDocDtos.addAll(appGrpPrimaryDocDtoList);
            }
        }
        for(int i=0;i < appGrpPrimaryDocDtos.size();i++){
            for(int j=0;j < appGrpPrimaryDocDtos.size() && j != i;j++){
                if(appGrpPrimaryDocDtos.get(i).getFileRepoId().equals(appGrpPrimaryDocDtos.get(j).getFileRepoId())){
                    appGrpPrimaryDocDtos.remove(appGrpPrimaryDocDtos.get(i));
                    i--;
                    break;
                }
            }
        }
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);


    }

    @Override
    public void premisesDocToSvcDoc(AppSubmissionDto appSubmissionDtoByLicenceId) {
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDtoByLicenceId.getAppGrpPrimaryDocDtos();
        List<AppSvcDocDto> appSvcDocDtoLits = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();
        List<AppSvcDocDto> removeList=IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(appSvcDocDtoLits)){
            for(AppSvcDocDto appSvcDocDto : appSvcDocDtoLits){
                String svcDocId = appSvcDocDto.getSvcDocId();
                if(StringUtil.isEmpty(svcDocId)){
                    continue;
                }
                HcsaSvcDocConfigDto entity = appConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if(StringUtil.isEmpty(entity.getServiceId())){
                    removeList.add(appSvcDocDto);
                }
            }
            appSvcDocDtoLits.removeAll(removeList);
        }

        List<AppSvcDocDto> appSvcDocDtoList=IaisCommonUtils.genNewArrayList();
        if(appGrpPrimaryDocDtos!=null){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos){
                AppSvcDocDto appSvcDocDto = MiscUtil.transferEntityDto(appGrpPrimaryDocDto, AppSvcDocDto.class);
                appSvcDocDto.setSvcDocId(appGrpPrimaryDocDto.getSvcComDocId());
                appSvcDocDto.setDocName(appGrpPrimaryDocDto.getDocName());
                appSvcDocDtoList.add(appSvcDocDto);
            }
            appSubmissionDtoByLicenceId.setAppGrpPrimaryDocDtos(null);
        }
        List<AppSvcDocDto> appSvcDocDtoLit = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();
        if(appSvcDocDtoLit!=null){
            appSvcDocDtoList.addAll(appSvcDocDtoLit);
        }
        appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).setAppSvcDocDtoLit(appSvcDocDtoList);
    }


    private  void validateVehicleNo(Map<String, String> errorMap, Set<String> distinctVehicleNo, int numberCount, String conveyanceVehicleNo){
        if(StringUtil.isEmpty(conveyanceVehicleNo)){
            errorMap.put("conveyanceVehicleNo"+numberCount,"UC_CHKLMD001_ERR001");
        }else {
            boolean b = VehNoValidator.validateNumber(conveyanceVehicleNo);
            if(!b){
                errorMap.put("conveyanceVehicleNo"+numberCount,"CHKLMD001_ERR008");
            }

            if (distinctVehicleNo.contains(conveyanceVehicleNo)){
                errorMap.put("conveyanceVehicleNo"+numberCount, "CHKLMD001_ERR009");
            }else {
                distinctVehicleNo.add(conveyanceVehicleNo);
            }
        }
    }
    private  int validateTime(Map<String, String> errorMap, String onsiteHH, String onsiteMM, int date, String key, int i){
        try {
            int i1 = Integer.parseInt(onsiteHH);
            int i2= Integer.parseInt(onsiteMM);
            date=  i1*60+i2*1;
            if(i1>=24){
                errorMap.put(key+i,"UC_CHKLMD001_ERR009");
            }else if(i2>=60){
                errorMap.put(key+i,"UC_CHKLMD001_ERR010");
            }
        }catch (Exception e){
            errorMap.put(key+i,"CHKLMD001_ERR003");
        }
        return date;
    }
}
