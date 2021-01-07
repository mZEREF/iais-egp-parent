package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.VehNoValidator;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
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
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;
    @Autowired
    private LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private FeMessageClient feMessageClient;
    @Autowired
    private ServiceConfigService serviceConfigService;
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
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    AppSubmissionService appSubmissionService;

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
        appSubmissionDto = applicationFeClient.saveAppsForRequestForChange(appSubmissionDto).getEntity();
//asynchronous save the other data.
        //eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }


    @Override
    public List<ApplicationDto> getAppByLicIdAndExcludeNew(String licenceId) {
        List<ApplicationDto> applicationDtos = applicationFeClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
        List<ApplicationDto> newApplicationDtos = IaisCommonUtils.genNewArrayList();
        for (ApplicationDto applicationDto : applicationDtos) {
            if (!ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(applicationDto.getStatus()) &&
                    !ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(applicationDto.getStatus()) &&
                    !ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT.equals(applicationDto.getStatus()) &&
                    !ApplicationConsts.APPLICATION_STATUS_DELETED.equals(applicationDto.getStatus())&&
                    !ApplicationConsts.APPLICATION_STATUS_RECALLED.equals(applicationDto.getStatus())&&
                    !ApplicationConsts.APPLICATION_STATUS_WITHDRAWN.equals(applicationDto.getStatus())&&
                    !ApplicationConsts.APPLICATION_STATUS_CESSATION_NOT_LICENCE.equals(applicationDto.getStatus())&&
                    !ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())) {
                newApplicationDtos.add(applicationDto);
            }
        }
        return newApplicationDtos;
    }

    @Override
    public Boolean isAllCanRfc(List<String> licIds) {
        List<ApplicationDto> newApplicationDtos = IaisCommonUtils.genNewArrayList();
        for (String licId : licIds) {
            List<ApplicationDto> applicationDtos = applicationFeClient.getAppByLicIdAndExcludeNew(licId).getEntity();
            List<String> appFinalStatus = IaisCommonUtils.getAppFinalStatus();
            appFinalStatus.remove("APST005");
            for (ApplicationDto applicationDto : applicationDtos) {
                if (!appFinalStatus.contains(applicationDto.getStatus())) {
                    newApplicationDtos.add(applicationDto);
                }
            }
        }
        if (!IaisCommonUtils.isEmpty(newApplicationDtos)) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
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
    public List<FeUserDto> getAccountByOrgId(String orgId){
        return organizationLienceseeClient.getAccountByOrgId(orgId).getEntity();
    }

    @Override
    public LicenseeDto getLicenseeByOrgId(String orgId){
        return organizationLienceseeClient.getLicenseeByOrgId(orgId).getEntity();
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
        return applicationFeClient.saveAppsForRequestForChangeByList(appSubmissionDtos).getEntity();
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
    @SearchTrack(catalog = "applicationPersonnelQuery", key = "appPersonnelQuery")
    public SearchResult<PersonnelQueryDto> psnDoQuery(SearchParam searchParam) {
        return licenceClient.psnDoQuery(searchParam).getEntity();
    }

    @Override
    public void sendEmail(String appGrpId, String type, String appNo, String serviceName, String licenceNo, Double amount, String licenceeName, String giroNo, String licenseeId, String subject, String aoName) throws Exception {
        //send email  rfc submit and pay giro
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        switch (subject) {
            case "RfcAndGiro":
                MsgTemplateDto RfcAndGiroMsgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate("D1CC7398-8C50-4178-BE83-1659CD7DBAA8").getEntity();
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
                    feEicGatewayClient.feSendEmail(emailDto, signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "RfcAndOnPay":
                MsgTemplateDto RfcAndOnPayMsgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate("D9DDBC23-122B-47BA-B579-3B5022816BB6").getEntity();
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
                    feEicGatewayClient.feSendEmail(emailDto, signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "rfcApproval":
                MsgTemplateDto rfcApprovalMsgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate("25C8B704-1FE1-42DF-B27C-7993B1208BAC").getEntity();
                if (rfcApprovalMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("appNo", appNo);
                    String mesContext = MsgUtil.getTemplateMessageByContent(rfcApprovalMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Request for Change " + appNo + " – Approved ");
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.feSendEmail(emailDto, signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "rfcReject":
                MsgTemplateDto rfcRejectMsgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate("B6C8231E-940D-485A-BFFB-9E65CADB5CA9").getEntity();
                if (rfcRejectMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("appNo", appNo);
                    String mesContext = MsgUtil.getTemplateMessageByContent(rfcRejectMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Request for Change " + appNo + " – Rejected  ");
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.feSendEmail(emailDto, signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "rfcToLicensee":
                MsgTemplateDto rfcToLicenseeMsgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate("8D3AC0E0-6684-490C-8DE8-D0452129C67D").getEntity();
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
                    feEicGatewayClient.feSendEmail(emailDto, signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "rfcForInterClarification":
                MsgTemplateDto rfcForInterClMsgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate("7C6DD026-7EC3-4D58-AAE5-170C8CF208C4").getEntity();
                if (rfcForInterClMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("appNo", appNo);
                    tempMap.put("aoName", aoName);
                    String mesContext = MsgUtil.getTemplateMessageByContent(rfcForInterClMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Internal Clarification for Request for Change " + appNo);
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.feSendEmail(emailDto, signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
                break;
            case "rfcToNotificationLicence":
                MsgTemplateDto rfcToNotificationMsgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate("CC3610B1-01A4-4370-9DEF-936827D2880D").getEntity();
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
                    feEicGatewayClient.feSendEmail(emailDto, signature.date(), signature.authorization(),
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
    @SearchTrack(catalog = "applicationPersonnelQuery", key = "queryPremises")
    public SearchResult<PremisesListQueryDto> searchPreInfo(SearchParam searchParam) {
        return licenceClient.getPremises(searchParam).getEntity();
    }

    @Override
    public LicenceDto getLicenceById(String licenceId) {
        return licenceClient.getLicBylicId(licenceId).getEntity();
    }

    @Override
    public List<LicenceDto> getLicenceDtoByHciCode(String hciCode, String licenseeId) {
        return licenceClient.getLicenceDtoByHciCode(hciCode, licenseeId).getEntity();
    }

    @Override
    public String sendNotification(EmailDto email) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return feEicGatewayClient.feSendEmail(email, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
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
        if (!IaisCommonUtils.isEmpty(entity)) {
            idNo = entity.get(0).getKeyPersonnelDto().getIdNo();
        }
        return idNo;
    }

    @Override
    public List<PersonnelListDto> getPersonnelListDto(PersonnelTypeDto personnelTypeDto) {
        return licenceClient.getPersonnelListDto(personnelTypeDto).getEntity();
    }

    @Override
    public List<AppSubmissionDto> saveAppsForRequestForGoupAndAppChangeByList(List<AppSubmissionDto> appSubmissionDtos) {
        List<AppSubmissionDto> entity = applicationFeClient.saveAppsForRequestForGoupAndAppChangeByList(appSubmissionDtos).getEntity();

        return entity;
    }

    @Override
    public List<String> getAdminEmail(String orgId) {
        return organizationLienceseeClient.getAdminEmailAdd(orgId).getEntity();
    }

    @Override
    public Boolean isOtherOperation(String licenceId) {

        return applicationFeClient.isLiscenceAppealOrCessation(licenceId).getEntity();
    }

    @Override
    public Map<String, String> doValidatePremiss(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto, List<String> premisesHciList, String masterCodeDto, boolean rfi) {

        //do validate one premiss
        List<String> list = IaisCommonUtils.genNewArrayList();//NOSONAR
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Set<String> distinctVehicleNo = IaisCommonUtils.genNewHashSet();
        List<String> oldPremiseHciList = IaisCommonUtils.genNewArrayList();
        //new rfi
        boolean appTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
        if ((appTypeFlag && rfi) && (oldAppSubmissionDto != null)) {
            AppGrpPremisesDto oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0);
            oldPremiseHciList = NewApplicationHelper.genPremisesHciList(oldAppGrpPremisesDto);
        }
        boolean needAppendMsg = false;
        String licenseeId = appSubmissionDto.getLicenseeId();
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
            String premiseType = appGrpPremisesDtoList.get(i).getPremisesType();
            if (StringUtil.isEmpty(premiseType)) {
                errorMap.put("premisesType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "What is your premises type", "field"));
            } else {
                String premisesSelect = appGrpPremisesDtoList.get(i).getPremisesSelect();
                String appType = appSubmissionDto.getAppType();
                boolean needValidate = false;

                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
                    String oldPremSel = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0).getPremisesSelect();
                    if (!StringUtil.isEmpty(oldPremSel) && oldPremSel.equals(premisesSelect)) {
                        needValidate = true;
                    }
                }
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
                if (StringUtil.isEmpty(premisesSelect) || "-1".equals(premisesSelect)) {
                    errorMap.put("premisesSelect" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Add or select a premises from the list", "field"));
                } else if (needValidate || !StringUtil.isEmpty(premisesSelect) || "newPremise".equals(premisesSelect)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    List<AppPremisesOperationalUnitDto> operationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
                    List<String> floorUnitList = IaisCommonUtils.genNewArrayList();
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)) {
                        String onsiteStartHH = appGrpPremisesDtoList.get(i).getOnsiteStartHH();
                        String onsiteStartMM = appGrpPremisesDtoList.get(i).getOnsiteStartMM();
                        int startDate = 0;
                        int endDate = 0;
                        if (StringUtil.isEmpty(onsiteStartHH) || StringUtil.isEmpty(onsiteStartMM)) {
                            errorMap.put("onsiteStartMM" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Operating Hours (Start)", "field"));
                        } else {
                            startDate = validateTime(errorMap, onsiteStartHH, onsiteStartMM, startDate, "onsiteStartMM", i);
                        }

                        String onsiteEndHH = appGrpPremisesDtoList.get(i).getOnsiteEndHH();
                        String onsiteEndMM = appGrpPremisesDtoList.get(i).getOnsiteEndMM();
                        if (StringUtil.isEmpty(onsiteEndHH) || StringUtil.isEmpty(onsiteEndMM)) {
                            errorMap.put("onsiteEndMM" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Operating Hours (End)", "field"));
                        } else {
                            endDate = validateTime(errorMap, onsiteEndHH, onsiteEndMM, endDate, "onsiteEndMM", i);
                        }
                        if (!StringUtil.isEmpty(onsiteStartHH) && !StringUtil.isEmpty(onsiteStartMM) && !StringUtil.isEmpty(onsiteEndHH) && !StringUtil.isEmpty(onsiteEndMM)) {
                            if (endDate != 0) {
                                if (endDate < startDate) {
                                    errorMap.put("onsiteStartMM" + i, "NEW_ERR0015");
                                }
                            }
                        }

                        String locateWithOthers = appGrpPremisesDtoList.get(i).getLocateWithOthers();
                        if (StringUtil.isEmpty(locateWithOthers)) {
                            errorMap.put("isOtherLic" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Are you co-locating with another licensee", "field"));
                        }

                        //set  time
                        if (!errorMap.containsKey("onsiteStartMM" + i) && !errorMap.containsKey("onsiteEndMM" + i)) {
                            LocalTime startTime = LocalTime.of(Integer.parseInt(onsiteStartHH), Integer.parseInt(onsiteStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(onsiteEndHH), Integer.parseInt(onsiteEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if (!IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)) {
                            for (int j = 0; j < appPremPhOpenPeriodList.size(); j++) {
                                AppPremPhOpenPeriodDto appPremPhOpenPeriodDto = appPremPhOpenPeriodList.get(j);
                                String convStartFromHH = appPremPhOpenPeriodDto.getOnsiteStartFromHH();
                                String convStartFromMM = appPremPhOpenPeriodDto.getOnsiteStartFromMM();
                                String onsiteEndToHH = appPremPhOpenPeriodDto.getOnsiteEndToHH();
                                String onsiteEndToMM = appPremPhOpenPeriodDto.getOnsiteEndToMM();
                                String phDate = appPremPhOpenPeriodDto.getPhDate();
                                if (!StringUtil.isEmpty(phDate)) {
                                    if (StringUtil.isEmpty(convStartFromHH) || StringUtil.isEmpty(convStartFromMM)) {
                                        errorMap.put("onsiteStartToMM" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (Start)", "field"));
                                    }
                                    if (StringUtil.isEmpty(onsiteEndToHH) || StringUtil.isEmpty(onsiteEndToMM)) {
                                        errorMap.put("onsiteEndToMM" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (End)", "field"));
                                    }
                                } else if (StringUtil.isEmpty(phDate)) {
                                    errorMap.put("onsitephDate" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Select Public Holiday", "field"));
                                }
                                if (!StringUtil.isEmpty(convStartFromHH) && !StringUtil.isEmpty(convStartFromMM) && !StringUtil.isEmpty(onsiteEndToHH)
                                        && !StringUtil.isEmpty(onsiteEndToMM) || StringUtil.isEmpty(convStartFromHH) && StringUtil.isEmpty(convStartFromMM)
                                        && StringUtil.isEmpty(onsiteEndToHH) && StringUtil.isEmpty(onsiteEndToMM)) {
                                    if (!StringUtil.isEmpty(convStartFromHH) && !StringUtil.isEmpty(convStartFromMM) && !StringUtil.isEmpty(onsiteEndToHH)
                                            && !StringUtil.isEmpty(onsiteEndToMM)) {
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);

                                            if (i1 >= 24) {
                                                errorMap.put("onsiteStartToMM" + i + j, "NEW_ERR0013");
                                            } else if (i2 >= 60) {
                                                errorMap.put("onsiteStartToMM" + i + j, "NEW_ERR0014");
                                            }

                                        } catch (Exception e) {
                                            errorMap.put("onsiteStartToMM" + i + j, "GENERAL_ERR0002");
                                        }
                                        try {
                                            int i3 = Integer.parseInt(onsiteEndToHH);
                                            int i4 = Integer.parseInt(onsiteEndToMM);
                                            if (i3 >= 24) {
                                                errorMap.put("onsiteEndToMM" + i + j, "NEW_ERR0013");
                                            } else if (i4 >= 60) {
                                                errorMap.put("onsiteEndToMM" + i + j, "NEW_ERR0014");
                                            }
                                        } catch (Exception e) {
                                            errorMap.put("onsiteEndToMM" + i + j, "GENERAL_ERR0002");
                                        }
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            int i3 = Integer.parseInt(onsiteEndToHH);
                                            int i4 = Integer.parseInt(onsiteEndToMM);
                                            if (i3 * 60 + i4 != 0) {
                                                if ((i1 * 60 + i2) > (i3 * 60 + i4)) {
                                                    errorMap.put("onsiteStartToMM" + i + j, "NEW_ERR0015");
                                                }
                                            }
                                        } catch (Exception e) {
                                        }
                                    }


                                } else {
                                    if (StringUtil.isEmpty(convStartFromHH) && StringUtil.isEmpty(convStartFromMM) || StringUtil.isEmpty(convStartFromMM) || StringUtil.isEmpty(convStartFromHH)) {
                                        errorMap.put("onsiteStartToMM" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (Start) ", "field"));
                                    } else {
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);

                                            if (i1 >= 24) {
                                                errorMap.put("onsiteStartToMM" + i + j, "NEW_ERR0013");
                                            } else if (i2 >= 60) {
                                                errorMap.put("onsiteStartToMM" + i + j, "NEW_ERR0014");
                                            }

                                        } catch (Exception e) {
                                            errorMap.put("onsiteStartToMM" + i + j, "GENERAL_ERR0002");
                                        }
                                    }
                                    if (StringUtil.isEmpty(onsiteEndToHH) && StringUtil.isEmpty(onsiteEndToMM) || StringUtil.isEmpty(onsiteEndToHH) || StringUtil.isEmpty(onsiteEndToMM)) {
                                        errorMap.put("onsiteEndToMM" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (End)", "field"));
                                    } else {
                                        try {
                                            int i3 = Integer.parseInt(onsiteEndToHH);
                                            int i4 = Integer.parseInt(onsiteEndToMM);
                                            if (i3 >= 24) {
                                                errorMap.put("onsiteEndToMM" + i + j, "NEW_ERR0013");
                                            } else if (i4 >= 60) {
                                                errorMap.put("onsiteEndToMM" + i + j, "NEW_ERR0014");
                                            }
                                        } catch (Exception e) {
                                            errorMap.put("onsiteEndToMM" + i + j, "GENERAL_ERR0002");
                                        }

                                    }
                                }
                                //set ph time
                                String errorOnsiteStartToMM = errorMap.get("onsiteStartToMM" + i + j);
                                String errorOnsiteEndToMM = errorMap.get("onsiteEndToMM" + i + j);
                                if (StringUtil.isEmpty(errorOnsiteEndToMM) && StringUtil.isEmpty(errorOnsiteStartToMM) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)) {
                                    LocalTime startTime = LocalTime.of(Integer.parseInt(convStartFromHH), Integer.parseInt(convStartFromMM));
                                    appPremPhOpenPeriodDto.setStartFrom(Time.valueOf(startTime));
                                    LocalTime endTime = LocalTime.of(Integer.parseInt(onsiteEndToHH), Integer.parseInt(onsiteEndToMM));
                                    appPremPhOpenPeriodDto.setEndTo(Time.valueOf(endTime));
                                }
                            }
                        }
                        String ScdfRefNo = appGrpPremisesDtoList.get(i).getScdfRefNo();
                        if(!StringUtil.isEmpty(ScdfRefNo) && ScdfRefNo.length() > 66){
                            String general_err0041=NewApplicationHelper.repLength("Fire Safety & Shelter Bureau Ref. No.","66");
                            errorMap.put("ScdfRefNo" + i, general_err0041);
                        }

                        String buildingName = appGrpPremisesDtoList.get(i).getBuildingName();
                        if(!StringUtil.isEmpty(buildingName) && buildingName.length() > 66){
                            String general_err0041=NewApplicationHelper.repLength("Building Name","66");
                            errorMap.put("buildingName" + i, general_err0041);
                        }

                        String hciName = appGrpPremisesDtoList.get(i).getHciName();
                        if (StringUtil.isEmpty(hciName)) {
                            errorMap.put("hciName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "HCI Name", "field"));
                        } else {
                            if(hciName.length()>100){
                                String general_err0041=NewApplicationHelper.repLength("HCI Name","100");
                                errorMap.put("hciName" + i, general_err0041);
                            }
                            if (masterCodeDto != null) {
                                String[] s = masterCodeDto.split(" ");
                                for (int index = 0; index < s.length; index++) {
                                    if (hciName.toUpperCase().contains(s[index].toUpperCase())) {
                                        errorMap.put("hciName" + i, MessageUtil.replaceMessage("GENERAL_ERR0016", s[index].toUpperCase(), "keywords"));
                                    }
                                }
                            }
                            if (StringUtil.isEmpty(licenseeId)) {
                                licenseeId = "9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
                            }
                            List<AppGrpPremisesDto> entity = applicationFeClient.getAppGrpPremisesDtoByHciName(hciName, licenseeId).getEntity();
                            if (!entity.isEmpty()) {
                                errorMap.put("hciNameUsed", MessageUtil.getMessageDesc("NEW_ACK011"));
                            }
                        }
                        String offTelNo = appGrpPremisesDtoList.get(i).getOffTelNo();
                        if (StringUtil.isEmpty(offTelNo)) {
                            errorMap.put("offTelNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Office Telephone No.", "field"));
                        } else {
                            if(offTelNo.length()>8){
                                String general_err0041=NewApplicationHelper.repLength("Office Telephone No.","8");
                                errorMap.put("offTelNo" + i, general_err0041);
                            }
                            boolean matches = offTelNo.matches("^[6][0-9]{7}$");
                            if (!matches) {
                                errorMap.put("offTelNo" + i, "GENERAL_ERR0015");
                            }
                        }

                        String streetName = appGrpPremisesDtoList.get(i).getStreetName();
                        if (StringUtil.isEmpty(streetName)) {
                            errorMap.put("streetName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name", "field"));
                        }else if(streetName.length() > 32){
                            String general_err0041 = NewApplicationHelper.repLength("Street Name", "32");
                            errorMap.put("streetName" + i, general_err0041);
                        }

                        String addrType = appGrpPremisesDtoList.get(i).getAddrType();

                        boolean addrTypeFlag = true;
                        if (StringUtil.isEmpty(addrType)) {
                            addrTypeFlag = false;
                            errorMap.put("addrType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
                        } else {
                            String floorNo = appGrpPremisesDtoList.get(i).getFloorNo();
                            String blkNo = appGrpPremisesDtoList.get(i).getBlkNo();
                            String unitNo = appGrpPremisesDtoList.get(i).getUnitNo();
                            boolean empty = StringUtil.isEmpty(floorNo);
                            boolean empty1 = StringUtil.isEmpty(blkNo);
                            boolean empty2 = StringUtil.isEmpty(unitNo);
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {

                                if (empty) {
                                    addrTypeFlag = false;
                                    errorMap.put("floorNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
                                }else if(floorNo.length() > 3){
                                    String general_err0041=NewApplicationHelper.repLength("Floor No.","3");
                                    errorMap.put("floorNo" + i, general_err0041);
                                }
                                if (empty1) {
                                    addrTypeFlag = false;
                                    errorMap.put("blkNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
                                }else if(blkNo.length() > 10){
                                    String general_err0041=NewApplicationHelper.repLength("Block / House No.","10");
                                    errorMap.put("blkNo" + i, general_err0041);
                                }
                                if (empty2) {
                                    addrTypeFlag = false;
                                    errorMap.put("unitNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
                                }else if(unitNo.length() > 5){
                                    String general_err0041=NewApplicationHelper.repLength("Unit No.","5");
                                    errorMap.put("unitNo" + i, general_err0041);
                                }
                            }
                            String floorNoErr = errorMap.get("floorNo" + i);
                            if (StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)) {
                                Pattern pattern = compile("[0-9]*");
                                boolean noFlag = pattern.matcher(floorNo).matches();
                                if (noFlag) {
                                    int floorNum = Integer.parseInt(floorNo);
                                    if (10 > floorNum) {
                                        floorNo = "0" + floorNum;
                                        appGrpPremisesDtoList.get(i).setFloorNo(floorNo);
                                    }
                                }

                            }
                            if (!empty && !empty1 && !empty2) {
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getBlkNo())
                                        .append(appGrpPremisesDtoList.get(i).getUnitNo());
                            }
                        }
                        if(addrTypeFlag){
                            floorUnitList.add(appGrpPremisesDtoList.get(i).getFloorNo() + appGrpPremisesDtoList.get(i).getUnitNo());
                        }

                        checkOperaionUnit(operationalUnitDtos,errorMap,"opFloorNo"+i,"opUnitNo"+i,floorUnitList,"floorUnit"+i);

                        String postalCode = appGrpPremisesDtoList.get(i).getPostalCode();
                        if (!StringUtil.isEmpty(postalCode)) {
                            if(postalCode.length() > 6){
                                String general_err0041=NewApplicationHelper.repLength("Postal Code","6");
                                errorMap.put("postalCode" + i, general_err0041);
                            }else if (postalCode.length() < 6) {
                                errorMap.put("postalCode" + i, "NEW_ERR0004");
                            } else if (!postalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("postalCode" + i, "GENERAL_ERR0002");
                            } else {
                                if (!StringUtil.isEmpty(stringBuilder.toString())) {
                                    stringBuilder.append(postalCode);
                                    if (list.contains(stringBuilder.toString())) {
                                        errorMap.put("postalCode" + i, "NEW_ACK010");

                                    } else {
                                        list.add(stringBuilder.toString());
                                    }
                                }
                            }
                        } else {
                            errorMap.put("postalCode" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code ", "field"));
                        }
                        //0062204
                        String currentHci = hciName + IaisCommonUtils.genPremisesKey(postalCode, appGrpPremisesDto.getBlkNo(), appGrpPremisesDto.getFloorNo(), appGrpPremisesDto.getUnitNo());
                        String hciNameErr = errorMap.get("hciName" + i);
                        String postalCodeErr = errorMap.get("postalCode" + i);
                        String blkNoErr = errorMap.get("blkNo" + i);
                        String floorNoErr = errorMap.get("floorNo" + i);
                        String unitNoErr = errorMap.get("unitNo" + i);
                        boolean clickEdit = appGrpPremisesDto.isClickEdit();
                        boolean hciFlag = StringUtil.isEmpty(hciNameErr) && StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:" + hciFlag));
                        boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                        if ((newTypeFlag && hciFlag) && !rfi) {
                            //new
                            if (!IaisCommonUtils.isEmpty(premisesHciList)) {
                                checkHciIsSame(appGrpPremisesDto,premisesHciList,errorMap,"premisesHci" + i);
                            }
                        } else if (((newTypeFlag && hciFlag) && rfi) && clickEdit) {
                            //new rfi
                            if (!IaisCommonUtils.isEmpty(premisesHciList) && !oldPremiseHciList.contains(currentHci)) {
                                checkHciIsSame(appGrpPremisesDto,premisesHciList,errorMap,"premisesHci" + i);
                            }
                        }
                        //0065116
                        String isOtherLic = appGrpPremisesDto.getLocateWithOthers();
                        //new
                        if (hciFlag && AppConsts.YES.equals(isOtherLic)) {
                            CheckCoLocationDto checkCoLocationDto = new CheckCoLocationDto();
                            checkCoLocationDto.setLicenseeId(licenseeId);
                            checkCoLocationDto.setAppGrpPremisesDto(appGrpPremisesDto);
                            Boolean flag = licenceClient.getOtherLicseePremises(checkCoLocationDto).getEntity();
                            if (flag) {
                                needAppendMsg = true;
                            }
                        }
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        String conStartHH = appGrpPremisesDtoList.get(i).getConStartHH();
                        String conStartMM = appGrpPremisesDtoList.get(i).getConStartMM();
                        int conStartDate = 0;
                        int conEndDate = 0;

                        if (StringUtil.isEmpty(conStartHH) || StringUtil.isEmpty(conStartMM)) {
                            errorMap.put("conStartMM" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Operating Hours (Start)", "field"));
                        } else {
                            conStartDate = validateTime(errorMap, conStartHH, conStartMM, conStartDate, "conStartMM", i);
                        }
                        String conEndHH = appGrpPremisesDtoList.get(i).getConEndHH();
                        String conEndMM = appGrpPremisesDtoList.get(i).getConEndMM();
                        if (StringUtil.isEmpty(conEndHH) || StringUtil.isEmpty(conEndMM)) {
                            errorMap.put("conEndMM" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Operating Hours (End)", "field"));
                        } else {
                            conEndDate = validateTime(errorMap, conEndHH, conEndMM, conEndDate, "conEndMM", i);
                        }
                        if (!StringUtil.isEmpty(conStartHH) && !StringUtil.isEmpty(conStartMM) && !StringUtil.isEmpty(conEndHH) && !StringUtil.isEmpty(conEndMM)) {
                            if (0 != conEndDate) {
                                if (conEndDate < conStartDate) {
                                    errorMap.put("conStartMM" + i, "NEW_ERR0015");
                                }
                            }
                        }


                        //set  time
                        String errorStartMM = errorMap.get("conStartMM" + i);
                        String errorEndMM = errorMap.get("conEndMM" + i);
                        if (StringUtil.isEmpty(errorStartMM) && StringUtil.isEmpty(errorEndMM)) {
                            LocalTime startTime = LocalTime.of(Integer.parseInt(conStartHH), Integer.parseInt(conStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(conEndHH), Integer.parseInt(conEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if (appPremPhOpenPeriodList != null) {
                            for (int j = 0; j < appPremPhOpenPeriodList.size(); j++) {
                                AppPremPhOpenPeriodDto appPremPhOpenPeriodDto = appPremPhOpenPeriodList.get(j);
                                String convEndToHH = appPremPhOpenPeriodDto.getConvEndToHH();
                                String convEndToMM = appPremPhOpenPeriodDto.getConvEndToMM();
                                String convStartFromHH = appPremPhOpenPeriodDto.getConvStartFromHH();
                                String convStartFromMM = appPremPhOpenPeriodDto.getConvStartFromMM();
                                String phDate = appPremPhOpenPeriodDto.getPhDate();
                                if (!StringUtil.isEmpty(phDate)) {
                                    if (StringUtil.isEmpty(convEndToHH) || StringUtil.isEmpty(convEndToMM)) {
                                        errorMap.put("convEndToHH" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (End) ", "field"));
                                    }
                                    if (StringUtil.isEmpty(convStartFromHH) || StringUtil.isEmpty(convStartFromMM)) {
                                        errorMap.put("convStartToHH" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (Start) ", "field"));
                                    }
                                } else if (StringUtil.isEmpty(phDate)) {
                                    errorMap.put("convphDate" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006","convphDate","field"));
                                }

                                if (StringUtil.isEmpty(convEndToHH) && StringUtil.isEmpty(convEndToMM) & StringUtil.isEmpty(convStartFromHH)
                                        & StringUtil.isEmpty(convStartFromMM) || !StringUtil.isEmpty(convEndToHH) && !StringUtil.isEmpty(convEndToMM)
                                        && !StringUtil.isEmpty(convStartFromHH) & !StringUtil.isEmpty(convStartFromMM)) {
                                    if (!StringUtil.isEmpty(convEndToHH) && !StringUtil.isEmpty(convEndToMM)
                                            && !StringUtil.isEmpty(convStartFromHH) & !StringUtil.isEmpty(convStartFromMM)) {
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            if (i1 >= 24) {
                                                errorMap.put("convStartToHH" + i + j, "NEW_ERR0013");
                                            } else if (i2 >= 60) {
                                                errorMap.put("convStartToHH" + i + j, "NEW_ERR0014");
                                            }

                                        } catch (Exception e) {
                                            errorMap.put("convStartToHH" + i + j, "GENERAL_ERR0002");
                                        }
                                        try {
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if (i3 >= 24) {
                                                errorMap.put("convEndToHH" + i + j, "NEW_ERR0013");
                                            } else if (i4 >= 60) {
                                                errorMap.put("convEndToHH" + i + j, "NEW_ERR0014");
                                            }
                                        } catch (Exception e) {
                                            errorMap.put("convEndToHH" + i + j, "GENERAL_ERR0002");
                                        }
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if (i3 * 60 + i4 != 0) {
                                                if ((i1 * 60 + i2) > (i3 * 60 + i4)) {
                                                    errorMap.put("convStartToHH" + i + j, "NEW_ERR0015");
                                                }
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                } else {
                                    if (StringUtil.isEmpty(convStartFromHH) || StringUtil.isEmpty(convStartFromMM) || StringUtil.isEmpty(convStartFromMM) && StringUtil.isEmpty(convStartFromHH)) {
                                        errorMap.put("convStartToHH" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (Start) ", "field"));
                                    } else {
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            if (i1 >= 24) {
                                                errorMap.put("convStartToHH" + i + j, "NEW_ERR0013");
                                            } else if (i2 >= 60) {
                                                errorMap.put("convStartToHH" + i + j, "NEW_ERR0014");
                                            }
                                        } catch (Exception e) {


                                        }
                                    }
                                    if (StringUtil.isEmpty(convEndToHH) || StringUtil.isEmpty(convEndToMM) || StringUtil.isEmpty(convEndToHH) && StringUtil.isEmpty(convEndToMM)) {
                                        errorMap.put("convEndToHH" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (End) ", "field"));
                                    } else {
                                        try {
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if (i3 >= 24) {
                                                errorMap.put("convEndToHH" + i + j, "NEW_ERR0013");
                                            } else if (i4 >= 60) {
                                                errorMap.put("convEndToHH" + i + j, "NEW_ERR0014");
                                            }

                                        } catch (Exception e) {


                                        }
                                    }
                                }
                                //set ph time
                                String errorConvStartToMM = errorMap.get("convStartToHH" + i + j);
                                String errorConvEndToMM = errorMap.get("convEndToHH" + i + j);
                                if (StringUtil.isEmpty(errorConvStartToMM) && StringUtil.isEmpty(errorConvEndToMM) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)) {
                                    LocalTime startTime = LocalTime.of(Integer.parseInt(convStartFromHH), Integer.parseInt(convStartFromMM));
                                    appPremPhOpenPeriodDto.setStartFrom(Time.valueOf(startTime));
                                    LocalTime endTime = LocalTime.of(Integer.parseInt(convEndToHH), Integer.parseInt(convEndToMM));
                                    appPremPhOpenPeriodDto.setEndTo(Time.valueOf(endTime));
                                }
                            }
                        }
                        String buildingName = appGrpPremisesDtoList.get(i).getConveyanceBuildingName();
                        if(!StringUtil.isEmpty(buildingName) && buildingName.length() > 66){
                            String general_err0041=NewApplicationHelper.repLength("Building Name","66");
                            errorMap.put("conveyanceBuildingName" + i, general_err0041);
                        }

                        String conveyanceVehicleNo = appGrpPremisesDtoList.get(i).getConveyanceVehicleNo();
                        validateVehicleNo(errorMap, distinctVehicleNo, i, conveyanceVehicleNo);

                        String cStreetName = appGrpPremisesDtoList.get(i).getConveyanceStreetName();
                        if (StringUtil.isEmpty(cStreetName)) {
                            errorMap.put("conveyanceStreetName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name ", "field"));
                        }else if(cStreetName.length() > 32){
                            String general_err0041=NewApplicationHelper.repLength("Street Name","32");
                            errorMap.put("conveyanceStreetName" + i, general_err0041);
                        }
                        boolean addrTypeFlag = true;
                        String conveyanceAddressType = appGrpPremisesDtoList.get(i).getConveyanceAddressType();
                        if (StringUtil.isEmpty(conveyanceAddressType)) {
                            addrTypeFlag = false;
                            errorMap.put("conveyanceAddressType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type ", "field"));
                        } else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceBlockNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(conveyanceAddressType)) {

                                if (empty) {
                                    addrTypeFlag = false;
                                    errorMap.put("conveyanceFloorNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
                                }else if(appGrpPremisesDtoList.get(i).getConveyanceFloorNo().length()>3){
                                    String general_err0041=NewApplicationHelper.repLength("Floor No.","3");
                                    errorMap.put("conveyanceFloorNo" + i, general_err0041);
                                }
                                if (empty1) {
                                    addrTypeFlag = false;
                                    errorMap.put("conveyanceBlockNos" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
                                }else if(appGrpPremisesDtoList.get(i).getConveyanceBlockNo().length()>10){
                                    String general_err0041=NewApplicationHelper.repLength("Block / House No.","10");
                                    errorMap.put("conveyanceBlockNos" + i, general_err0041);
                                }
                                if (empty2) {
                                    addrTypeFlag = false;
                                    errorMap.put("conveyanceUnitNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
                                }else if(appGrpPremisesDtoList.get(i).getConveyanceUnitNo().length()>5){
                                    String general_err0041=NewApplicationHelper.repLength("Unit No.","5");
                                    errorMap.put("conveyanceUnitNo" + i, general_err0041);
                                }

                            }
                            String floorNoErr = errorMap.get("conveyanceFloorNo" + i);
                            String floorNo = appGrpPremisesDtoList.get(i).getConveyanceFloorNo();
                            if (StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)) {
                                Pattern pattern = compile("[0-9]*");
                                boolean noFlag = pattern.matcher(floorNo).matches();
                                if (noFlag) {
                                    int floorNum = Integer.parseInt(floorNo);
                                    if (10 > floorNum) {
                                        floorNo = "0" + floorNum;
                                        appGrpPremisesDtoList.get(i).setConveyanceFloorNo(floorNo);
                                    }
                                }

                            }
                            if (!empty && !empty1 && !empty2) {
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getConveyanceFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getConveyanceBlockNo())
                                        .append(appGrpPremisesDtoList.get(i).getConveyanceUnitNo());
                            }
                        }
                        if(addrTypeFlag){
                            floorUnitList.add(appGrpPremisesDtoList.get(i).getConveyanceFloorNo() + appGrpPremisesDtoList.get(i).getConveyanceUnitNo());
                        }

                        checkOperaionUnit(operationalUnitDtos,errorMap,"opConvFloorNo"+i,"opConvUnitNo"+i,floorUnitList,"ConvFloorUnit"+i);

                        String conveyancePostalCode = appGrpPremisesDtoList.get(i).getConveyancePostalCode();
                        if (StringUtil.isEmpty(conveyancePostalCode)) {
                            errorMap.put("conveyancePostalCode" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code", "field"));
                        } else {
                            if(conveyancePostalCode.length() > 6){
                                String general_err0041=NewApplicationHelper.repLength("Postal Code","6");
                                errorMap.put("conveyancePostalCode" + i, general_err0041);
                            }else if (conveyancePostalCode.length() < 6) {
                                errorMap.put("conveyancePostalCode" + i, "NEW_ERR0004");
                            } else if (!conveyancePostalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("conveyancePostalCode" + i, "GENERAL_ERR0002");
                            } else {
                                if (!StringUtil.isEmpty(stringBuilder.toString())) {
                                    stringBuilder.append(conveyancePostalCode);
                                    if (list.contains(stringBuilder.toString())) {
                                        errorMap.put("conveyancePostalCode" + i, "NEW_ACK010");
                                    } else {
                                        list.add(stringBuilder.toString());
                                    }
                                }
                            }
                        }

                        //0062204
                        String currentHci = conveyanceVehicleNo + IaisCommonUtils.genPremisesKey(conveyancePostalCode, appGrpPremisesDto.getConveyanceBlockNo(), appGrpPremisesDto.getConveyanceFloorNo(), appGrpPremisesDto.getConveyanceUnitNo());
                        String vehicleNo = errorMap.get("conveyanceVehicleNo" + i);
                        String postalCodeErr = errorMap.get("conveyancePostalCode" + i);
                        String blkNoErr = errorMap.get("conveyanceBlockNos" + i);
                        String floorNoErr = errorMap.get("conveyanceFloorNo" + i);
                        String unitNoErr = errorMap.get("conveyanceUnitNo" + i);
                        boolean clickEdit = appGrpPremisesDto.isClickEdit();
                        boolean hciFlag = StringUtil.isEmpty(vehicleNo) && StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:" + hciFlag));
                        boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                        if (newTypeFlag && hciFlag && !rfi) {
                            //new
                            if (!IaisCommonUtils.isEmpty(premisesHciList)) {
                                checkHciIsSame(appGrpPremisesDto,premisesHciList,errorMap,"premisesHci" + i);
                            }
                        } else if (newTypeFlag && hciFlag && rfi && clickEdit) {
                            //new rfi
                            if (!IaisCommonUtils.isEmpty(premisesHciList) && !oldPremiseHciList.contains(currentHci)) {
                                checkHciIsSame(appGrpPremisesDto,premisesHciList,errorMap,"premisesHci" + i);
                            }
                        }
                        //65116
                        if (hciFlag) {
                            CheckCoLocationDto checkCoLocationDto = new CheckCoLocationDto();
                            checkCoLocationDto.setLicenseeId(licenseeId);
                            checkCoLocationDto.setAppGrpPremisesDto(appGrpPremisesDto);
                            Boolean flag = licenceClient.getOtherLicseePremises(checkCoLocationDto).getEntity();
                            if (flag) {
                                needAppendMsg = true;
                            }
                        }
                    } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premiseType)) {

                        String buildingName = appGrpPremisesDtoList.get(i).getOffSiteBuildingName();
                        if(!StringUtil.isEmpty(buildingName) && buildingName.length() > 66){
                            String general_err0041=NewApplicationHelper.repLength("Building Name","66");
                            errorMap.put("offSiteBuildingName" + i, general_err0041);
                        }

                        String offSiteStreetName = appGrpPremisesDtoList.get(i).getOffSiteStreetName();
                        if (StringUtil.isEmpty(offSiteStreetName)) {
                            errorMap.put("offSiteStreetName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name", "field"));
                        }else if(offSiteStreetName.length() > 32){
                            String general_err0041=NewApplicationHelper.repLength("Street Name","32");
                            errorMap.put("offSiteStreetName" + i, general_err0041);
                        }

                        String offSiteAddressType = appGrpPremisesDtoList.get(i).getOffSiteAddressType();
                        boolean addrTypeFlag = true;
                        if (StringUtil.isEmpty(offSiteAddressType)) {
                            addrTypeFlag = false;
                            errorMap.put("offSiteAddressType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
                        } else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getOffSiteFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getOffSiteBlockNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getOffSiteUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(offSiteAddressType)) {

                                if (empty) {
                                    addrTypeFlag = false;
                                    errorMap.put("offSiteFloorNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
                                }else if(appGrpPremisesDtoList.get(i).getOffSiteFloorNo().length() > 3){
                                    String general_err0041=NewApplicationHelper.repLength("Floor No.","3");
                                    errorMap.put("offSiteFloorNo" + i, general_err0041);
                                }
                                if (empty1) {
                                    addrTypeFlag = false;
                                    errorMap.put("offSiteBlockNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
                                }else if(appGrpPremisesDtoList.get(i).getOffSiteBlockNo().length() > 10){
                                    String general_err0041=NewApplicationHelper.repLength("Block / House No.","10");
                                    errorMap.put("offSiteBlockNo" + i, general_err0041);
                                }
                                if (empty2) {
                                    addrTypeFlag = false;
                                    errorMap.put("offSiteUnitNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
                                }else if(appGrpPremisesDtoList.get(i).getOffSiteUnitNo().length() > 5){
                                    String general_err0041=NewApplicationHelper.repLength("Floor No.","5");
                                    errorMap.put("offSiteUnitNo" + i, general_err0041);
                                }
                            }
                            String floorNoErr = errorMap.get("offSiteFloorNo" + i);
                            String floorNo = appGrpPremisesDtoList.get(i).getOffSiteFloorNo();
                            if (StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)) {
                                Pattern pattern = compile("[0-9]*");
                                boolean noFlag = pattern.matcher(floorNo).matches();
                                if (noFlag) {
                                    int floorNum = Integer.parseInt(floorNo);
                                    if (10 > floorNum) {
                                        floorNo = "0" + floorNum;
                                        appGrpPremisesDtoList.get(i).setOffSiteFloorNo(floorNo);
                                    }
                                }

                            }
                            if (!empty && !empty1 && !empty2) {
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getOffSiteFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getOffSiteBlockNo())
                                        .append(appGrpPremisesDtoList.get(i).getOffSiteUnitNo());
                            }
                        }
                        if(addrTypeFlag){
                            floorUnitList.add(appGrpPremisesDtoList.get(i).getOffSiteFloorNo() + appGrpPremisesDtoList.get(i).getOffSiteUnitNo());
                        }

                        checkOperaionUnit(operationalUnitDtos,errorMap,"opOffFloorNo"+i,"opOffUnitNo"+i,floorUnitList,"offFloorUnit"+i);

                        String offSitePostalCode = appGrpPremisesDtoList.get(i).getOffSitePostalCode();
                        if (!StringUtil.isEmpty(offSitePostalCode)) {
                            if(offSitePostalCode.length() > 6){
                                String general_err0041=NewApplicationHelper.repLength("Postal Code","6");
                                errorMap.put("offSitePostalCode" + i, general_err0041);
                            }else if (offSitePostalCode.length() < 6) {
                                errorMap.put("offSitePostalCode" + i, "NEW_ERR0004");
                            } else if (!offSitePostalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("offSitePostalCode" + i, "GENERAL_ERR0002");
                            } else {
                                if (!StringUtil.isEmpty(stringBuilder.toString())) {
                                    stringBuilder.append(offSitePostalCode);
                                    if (list.contains(stringBuilder.toString())) {
                                        errorMap.put("offSitePostalCode" + i, "NEW_ACK010");

                                    } else {
                                        list.add(stringBuilder.toString());
                                    }
                                }
                            }
                        } else {
                            errorMap.put("offSitePostalCode" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code ", "field"));
                        }
                        String offSiteStartHH = appGrpPremisesDtoList.get(i).getOffSiteStartHH();
                        String offSiteStartMM = appGrpPremisesDtoList.get(i).getOffSiteStartMM();
                        int startDate = 0;
                        int endDate = 0;
                        if (StringUtil.isEmpty(offSiteStartHH) || StringUtil.isEmpty(offSiteStartMM)) {
                            errorMap.put("offSiteStartMM" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Operating Hours (Start)", "field"));
                        } else {
                            startDate = validateTime(errorMap, offSiteStartHH, offSiteStartMM, startDate, "offSiteStartMM", i);
                        }

                        String offSiteEndHH = appGrpPremisesDtoList.get(i).getOffSiteEndHH();
                        String offSiteEndMM = appGrpPremisesDtoList.get(i).getOffSiteEndMM();
                        if (StringUtil.isEmpty(offSiteEndHH) || StringUtil.isEmpty(offSiteEndMM)) {
                            errorMap.put("offSiteEndMM" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Operating Hours (End) ", "field"));
                        } else {
                            endDate = validateTime(errorMap, offSiteEndHH, offSiteEndMM, endDate, "offSiteEndMM", i);
                        }
                        if (!StringUtil.isEmpty(offSiteStartHH) && !StringUtil.isEmpty(offSiteStartMM) && !StringUtil.isEmpty(offSiteEndHH) && !StringUtil.isEmpty(offSiteEndMM)) {
                            if (endDate != 0) {
                                if (endDate < startDate) {
                                    errorMap.put("offSiteStartMM" + i, "NEW_ERR0015");
                                }
                            }
                        }
                        //set  time
                        String errorStartMM = errorMap.get("offSiteStartMM" + i);
                        String errorEndMM = errorMap.get("offSiteEndMM" + i);
                        if (StringUtil.isEmpty(errorStartMM) && StringUtil.isEmpty(errorEndMM)) {
                            LocalTime startTime = LocalTime.of(Integer.parseInt(offSiteStartHH), Integer.parseInt(offSiteStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(offSiteEndHH), Integer.parseInt(offSiteEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if (appPremPhOpenPeriodList != null) {

                            for (int j = 0; j < appPremPhOpenPeriodList.size(); j++) {
                                AppPremPhOpenPeriodDto appPremPhOpenPeriodDto = appPremPhOpenPeriodList.get(j);
                                String offSiteEndToHH = appPremPhOpenPeriodDto.getOffSiteEndToHH();
                                String offSiteEndToMM = appPremPhOpenPeriodDto.getOffSiteEndToMM();
                                String offSiteStartFromHH = appPremPhOpenPeriodDto.getOffSiteStartFromHH();
                                String offSiteStartFromMM = appPremPhOpenPeriodDto.getOffSiteStartFromMM();
                                String phDate = appPremPhOpenPeriodDto.getPhDate();
                                if (!StringUtil.isEmpty(phDate)) {
                                    if (StringUtil.isEmpty(offSiteEndToHH) || StringUtil.isEmpty(offSiteEndToMM)) {
                                        errorMap.put("offSiteEndToHH" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (End) ", "field"));
                                    }
                                    if (StringUtil.isEmpty(offSiteStartFromHH) || StringUtil.isEmpty(offSiteStartFromMM)) {
                                        errorMap.put("offSiteStartToHH" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (Start) ", "field"));
                                    }
                                } else if (StringUtil.isEmpty(phDate)) {
                                    errorMap.put("offSitephDate" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Select Public Holiday", "field"));
                                }

                                if (StringUtil.isEmpty(offSiteEndToHH) && StringUtil.isEmpty(offSiteEndToMM) & StringUtil.isEmpty(offSiteStartFromHH)
                                        & StringUtil.isEmpty(offSiteStartFromMM) || !StringUtil.isEmpty(offSiteEndToHH) && !StringUtil.isEmpty(offSiteEndToMM)
                                        && !StringUtil.isEmpty(offSiteStartFromHH) & !StringUtil.isEmpty(offSiteStartFromMM)) {
                                    if (!StringUtil.isEmpty(offSiteEndToHH) && !StringUtil.isEmpty(offSiteEndToMM)
                                            && !StringUtil.isEmpty(offSiteEndToHH) & !StringUtil.isEmpty(offSiteStartFromMM)) {
                                        try {
                                            int i1 = Integer.parseInt(offSiteStartFromHH);
                                            int i2 = Integer.parseInt(offSiteStartFromMM);
                                            if (i1 >= 24) {
                                                errorMap.put("offSiteStartToHH" + i + j, "NEW_ERR0013");
                                            } else if (i2 >= 60) {
                                                errorMap.put("offSiteStartToHH" + i + j, "NEW_ERR0014");
                                            }

                                        } catch (Exception e) {
                                            errorMap.put("offSiteStartToHH" + i + j, "GENERAL_ERR0002");
                                        }
                                        try {
                                            int i3 = Integer.parseInt(offSiteEndToHH);
                                            int i4 = Integer.parseInt(offSiteEndToMM);
                                            if (i3 >= 24) {
                                                errorMap.put("offSiteEndToHH" + i + j, "NEW_ERR0013");
                                            } else if (i4 >= 60) {
                                                errorMap.put("offSiteEndToHH" + i + j, "NEW_ERR0014");
                                            }
                                        } catch (Exception e) {
                                            errorMap.put("offSiteEndToHH" + i + j, "GENERAL_ERR0002");
                                        }
                                        try {
                                            int i1 = Integer.parseInt(offSiteStartFromHH);
                                            int i2 = Integer.parseInt(offSiteStartFromMM);
                                            int i3 = Integer.parseInt(offSiteEndToHH);
                                            int i4 = Integer.parseInt(offSiteEndToMM);
                                            if (i3 * 60 + i4 != 0) {
                                                if ((i1 * 60 + i2) > (i3 * 60 + i4)) {
                                                    errorMap.put("offSiteStartToHH" + i + j, "NEW_ERR0015");
                                                }
                                            }

                                        } catch (Exception e) {

                                        }
                                    }
                                } else {
                                    if (StringUtil.isEmpty(offSiteStartFromHH) || StringUtil.isEmpty(offSiteStartFromMM) || StringUtil.isEmpty(offSiteStartFromHH) && StringUtil.isEmpty(offSiteStartFromMM)) {
                                        errorMap.put("offSiteStartToHH" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (Start) ", "field"));
                                    } else {
                                        try {
                                            int i1 = Integer.parseInt(offSiteStartFromHH);
                                            int i2 = Integer.parseInt(offSiteStartFromMM);
                                            if (i1 >= 24) {
                                                errorMap.put("offSiteStartToHH" + i + j, "NEW_ERR0013");
                                            } else if (i2 >= 60) {
                                                errorMap.put("offSiteStartToHH" + i + j, "NEW_ERR0014");
                                            }
                                        } catch (Exception e) {
                                            errorMap.put("offSiteStartToHH" + i + j, "GENERAL_ERR0002");

                                        }
                                    }
                                    if (StringUtil.isEmpty(offSiteEndToHH) || StringUtil.isEmpty(offSiteEndToMM) || StringUtil.isEmpty(offSiteEndToHH) && StringUtil.isEmpty(offSiteEndToMM)) {
                                        errorMap.put("offSiteEndToHH" + i + j, MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Holiday Operating Hours (End) ", "field"));
                                    } else {

                                        try {
                                            int i3 = Integer.parseInt(offSiteEndToHH);
                                            int i4 = Integer.parseInt(offSiteEndToMM);
                                            if (i3 >= 24) {
                                                errorMap.put("offSiteEndToHH" + i + j, "NEW_ERR0013");
                                            } else if (i4 >= 60) {
                                                errorMap.put("offSiteEndToHH" + i + j, "NEW_ERR0014");
                                            }

                                        } catch (Exception e) {
                                            errorMap.put("offSiteEndToHH" + i + j, "GENERAL_ERR0002");

                                        }
                                    }
                                }

                                //set ph time
                                String errorOffSiteStartToMM = errorMap.get("offSiteStartToHH" + i + j);
                                String errorOffSiteEndToMM = errorMap.get("offSiteEndToHH" + i + j);
                                if (StringUtil.isEmpty(errorOffSiteStartToMM) && StringUtil.isEmpty(errorOffSiteEndToMM) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)) {
                                    LocalTime startTime = LocalTime.of(Integer.parseInt(offSiteStartFromHH), Integer.parseInt(offSiteStartFromMM));
                                    appPremPhOpenPeriodDto.setStartFrom(Time.valueOf(startTime));
                                    LocalTime endTime = LocalTime.of(Integer.parseInt(offSiteEndToHH), Integer.parseInt(offSiteEndToMM));
                                    appPremPhOpenPeriodDto.setEndTo(Time.valueOf(endTime));
                                }
                            }
                        }
                        //0062204
                        String currentHci = IaisCommonUtils.genPremisesKey(offSitePostalCode, appGrpPremisesDto.getOffSiteBlockNo(), appGrpPremisesDto.getOffSiteFloorNo(), appGrpPremisesDto.getOffSiteUnitNo());
                        String postalCodeErr = errorMap.get("offSitePostalCode" + i);
                        String blkNoErr = errorMap.get("offSiteBlockNo" + i);
                        String floorNoErr = errorMap.get("offSiteFloorNo" + i);
                        String unitNoErr = errorMap.get("offSiteUnitNo" + i);
                        boolean clickEdit = appGrpPremisesDto.isClickEdit();
                        boolean hciFlag = StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:" + hciFlag));
                        boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                        if (newTypeFlag && hciFlag && !rfi) {
                            //new
                            if (!IaisCommonUtils.isEmpty(premisesHciList)) {
                                checkHciIsSame(appGrpPremisesDto,premisesHciList,errorMap,"premisesHci" + i);
                            }
                        } else if (newTypeFlag && hciFlag && rfi && clickEdit) {
                            //new rfi
                            if (!IaisCommonUtils.isEmpty(premisesHciList) && !oldPremiseHciList.contains(currentHci)) {
                                checkHciIsSame(appGrpPremisesDto,premisesHciList,errorMap,"premisesHci" + i);
                            }
                        }
                        //65116
                        if (hciFlag) {
                            CheckCoLocationDto checkCoLocationDto = new CheckCoLocationDto();
                            checkCoLocationDto.setLicenseeId(licenseeId);
                            checkCoLocationDto.setAppGrpPremisesDto(appGrpPremisesDto);
                            Boolean flag = licenceClient.getOtherLicseePremises(checkCoLocationDto).getEntity();
                            if (flag) {
                                needAppendMsg = true;
                            }
                        }
                    }


                } else {
                    //premiseSelect = organization hci code

                    if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        String conveyanceVehicleNo = appGrpPremisesDtoList.get(i).getConveyanceVehicleNo();
                        validateVehicleNo(errorMap, distinctVehicleNo, i, conveyanceVehicleNo);
                    }

                }
            }
        }
        //65116
        String hciNameUsed = errorMap.get("hciNameUsed");
        String errMsg = MessageUtil.getMessageDesc("NEW_ACK004");
        if (needAppendMsg) {
            if (StringUtil.isEmpty(hciNameUsed)) {
                errorMap.put("hciNameUsed", errMsg);
            } else {
                String hciNameMsg = MessageUtil.getMessageDesc(hciNameUsed);
                errorMap.put("hciNameUsed", hciNameMsg + "<br/>" + errMsg);
            }
        }
        log.info(StringUtil.changeForLog("the do doValidatePremiss end ...."));
        WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
        return errorMap;
    }


    @Override
    public void svcDocToPresmise(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcDocDto> appSvcDocDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcDocDto> deleteSvcDoc=IaisCommonUtils.genNewArrayList();
        List<AppGrpPrimaryDocDto> deletePrimary=IaisCommonUtils.genNewArrayList();
        if (appSvcDocDtoLit != null) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLit) {
                String svcDocId = appSvcDocDto.getSvcDocId();
                String fileRepoId = appSvcDocDto.getFileRepoId();
                if (StringUtil.isEmpty(svcDocId)) {
                    continue;
                }
                if(StringUtil.isEmpty(fileRepoId)){
                    deleteSvcDoc.add(appSvcDocDto);
                    continue;
                }
                HcsaSvcDocConfigDto entity = appConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if (entity != null) {
                    String serviceId = entity.getServiceId();
                    if (StringUtil.isEmpty(serviceId)) {
                        AppGrpPrimaryDocDto appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                        appGrpPrimaryDocDto.setSvcDocId(svcDocId);
                        appGrpPrimaryDocDto.setSvcComDocId(svcDocId);
                        appGrpPrimaryDocDto.setSvcComDocName(entity.getDocTitle());
                        appGrpPrimaryDocDto.setDocName(appSvcDocDto.getDocName());
                        appGrpPrimaryDocDto.setAppGrpId(appSubmissionDto.getAppGrpId());
                        appGrpPrimaryDocDto.setDocSize(appSvcDocDto.getDocSize());
                        appGrpPrimaryDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
                        appGrpPrimaryDocDto.setPassValidate(appSvcDocDto.isPassValidate());
                        appGrpPrimaryDocDto.setMd5Code(appSvcDocDto.getMd5Code());
                        appGrpPrimaryDocDto.setVersion(appSvcDocDto.getVersion());
                        appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                        appSvcDocDtos.add(appSvcDocDto);
                    }else {
                        appSvcDocDto.setUpFileName(entity.getDocTitle());
                    }
                }
            }
            appSvcDocDtoLit.removeAll(deleteSvcDoc);
            appSvcDocDtoLit.removeAll(appSvcDocDtos);
            for (int i = 0; i < appSvcDocDtoLit.size(); i++) {
                for (int j = 0; j < appSvcDocDtoLit.size() && j != i; j++) {
                    if (appSvcDocDtoLit.get(i).getFileRepoId().equals(appSvcDocDtoLit.get(j).getFileRepoId())) {
                        appSvcDocDtoLit.remove(appSvcDocDtoLit.get(i));
                        i--;
                        break;
                    }
                }
            }
        }
        if (dtoAppGrpPrimaryDocDtos != null) {
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos){
                String fileRepoId = appGrpPrimaryDocDto.getFileRepoId();
                if(StringUtil.isEmpty(fileRepoId)){
                    deletePrimary.add(appGrpPrimaryDocDto);
                    continue;
                }
                if(StringUtil.isEmpty(appGrpPrimaryDocDto.getSvcComDocName())){
                    String svcDocId = appGrpPrimaryDocDto.getSvcDocId();
                    if(svcDocId!=null){
                        HcsaSvcDocConfigDto hcsaSvcDocConfigDto = appConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                        if (hcsaSvcDocConfigDto != null) {
                            appGrpPrimaryDocDto.setSvcComDocName(hcsaSvcDocConfigDto.getDocTitle());
                        }
                    }
                }
            }
            dtoAppGrpPrimaryDocDtos.removeAll(deletePrimary);
            if (appGrpPrimaryDocDtos.isEmpty()) {
                appGrpPrimaryDocDtos.addAll(dtoAppGrpPrimaryDocDtos);
            } else {
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto1 : dtoAppGrpPrimaryDocDtos) {
                    for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                        String svcComDocName = appGrpPrimaryDocDto.getSvcComDocName();
                        String svcComDocName1 = appGrpPrimaryDocDto1.getSvcComDocName();
                        if (svcComDocName1 != null) {
                            if (svcComDocName1.equals(svcComDocName)) {
                                continue;
                            } else {
                                appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto1);
                            }
                        } else if (svcComDocName != null) {
                            if (svcComDocName.equals(svcComDocName1)) {
                                continue;
                            } else {
                                appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto1);
                            }
                        }
                    }
                }
                appGrpPrimaryDocDtos.addAll(appGrpPrimaryDocDtoList);
            }
        }
        for (int i = 0; i < appGrpPrimaryDocDtos.size(); i++) {
            for (int j = 0; j < appGrpPrimaryDocDtos.size() && j != i; j++) {
                if (appGrpPrimaryDocDtos.get(i).getFileRepoId().equals(appGrpPrimaryDocDtos.get(j).getFileRepoId())) {
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
        log.debug(StringUtil.changeForLog("do premisesDocToSvcDoc start ..."));
        //handler primary doc
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionService.handlerPrimaryDoc(appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList(),appSubmissionDtoByLicenceId.getAppGrpPrimaryDocDtos());
        appSubmissionDtoByLicenceId.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);

//        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDtoByLicenceId.getAppGrpPrimaryDocDtos();
        List<AppSvcDocDto> appSvcDocDtoLits = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();
        List<AppSvcDocDto> removeList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(appSvcDocDtoLits)) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLits) {
                String svcDocId = appSvcDocDto.getSvcDocId();
                if (StringUtil.isEmpty(svcDocId)) {
                    continue;
                }
                HcsaSvcDocConfigDto entity = appConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if (StringUtil.isEmpty(entity.getServiceId())) {
                    removeList.add(appSvcDocDto);
                }
            }
            appSvcDocDtoLits.removeAll(removeList);
        }

        List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        if (appGrpPrimaryDocDtos != null) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                AppSvcDocDto appSvcDocDto = MiscUtil.transferEntityDto(appGrpPrimaryDocDto, AppSvcDocDto.class);
                appSvcDocDto.setSvcDocId(appGrpPrimaryDocDto.getSvcComDocId());
                appSvcDocDto.setDocName(appGrpPrimaryDocDto.getDocName());
                appSvcDocDto.setVersion(appGrpPrimaryDocDto.getVersion());
                appSvcDocDtoList.add(appSvcDocDto);
            }
            appSubmissionDtoByLicenceId.setAppGrpPrimaryDocDtos(null);
        }
        List<AppSvcDocDto> appSvcDocDtoLit = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();
        if (appSvcDocDtoLit != null) {
            appSvcDocDtoList.addAll(appSvcDocDtoLit);
        }
        appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).setAppSvcDocDtoLit(appSvcDocDtoList);
    }

    @Override
    public List<FeUserDto> getFeUserDtoByLicenseeId(String licenseeId) {
        return organizationLienceseeClient.getFeUserDtoByLicenseeId(licenseeId).getEntity();
    }

    @Override
    public LicenceDto getLicenceDtoByLicNo(String licenceNo) {
        return licenceClient.getLicenceDtoByLicNo(licenceNo).getEntity();
    }
    @Override
    public boolean eqChangeConfigPresmiseType(List<LicenceDto> list,List<String> presmiseType){
        List<String>serviceName=new ArrayList<>(list.size());
        for(LicenceDto licenceDto : list) {
            String svcName = licenceDto.getSvcName();
            if(!serviceName.contains(svcName)){
                serviceName.add(svcName);
            }
        }
        List<HcsaServiceDto> hcsaServiceByNames = serviceConfigService.getHcsaServiceByNames(serviceName);
        List<String> serviceIds=new ArrayList<>(hcsaServiceByNames.size());
        for(HcsaServiceDto hcsaServiceDto : hcsaServiceByNames){
            serviceIds.add(hcsaServiceDto.getId());
        }
        for(String s  : presmiseType){
            boolean configIsChange = serviceConfigIsChange(serviceIds, s);
            if(!configIsChange){
                return true;
            }
        }
        return false;
    }

    @Override
    public void sendRfcSubmittedEmail(List<AppSubmissionDto> appSubmissionDtos) {
        try {
            AppSubmissionDto appSubmissionDto=appSubmissionDtos.get(0);
            String appGroupId = appSubmissionDto.getAppGrpId();
            if(appSubmissionDtos.size()>=2){
                double a = 0.0;
                for (AppSubmissionDto appSubmDto : appSubmissionDtos) {
                    a = a + appSubmDto.getAmount();
                }
                appSubmissionDto.setAmountStr(Formatter.formatterMoney(a));
            }
            String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();

            if(appSubmissionDto.getLicenceNo()==null){
                LicenceDto licenceDto= licenceClient.getLicBylicId(appSubmissionDto.getLicenceId()).getEntity();
                appSubmissionDto.setLicenceNo(licenceDto.getLicenceNo());
                appSubmissionDto.setServiceName(licenceDto.getSvcName());
            }
            OrgUserDto orgUserDto = null;
            ApplicationGroupDto applicationGroupDto = applicationFeClient.getApplicationGroup(appGroupId).getEntity();
            if (applicationGroupDto != null){

               orgUserDto = organizationLienceseeClient.retrieveOneOrgUserAccount(applicationGroupDto.getSubmitBy()).getEntity();
                if (orgUserDto != null){
                    emailMap.put("ApplicantName", orgUserDto.getDisplayName());
                }
            }
            if("$0.0".equals(appSubmissionDto.getAmountStr())){
                emailMap.remove("GIRO_PAY");
                emailMap.remove("Online_PAY");
            }
            emailMap.put("Payment_Amount", appSubmissionDto.getAmountStr());
            emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{appSubmissionDto.getAppType()}).get(0).getText());
            emailMap.put("ApplicationNumber", appSubmissionDto.getAppGrpNo());
            emailMap.put("ApplicationDate", Formatter.formatDate(new Date()));
            emailMap.put("systemLink", loginUrl);
            emailMap.put("email_address", systemParamConfig.getSystemAddressOne());
            emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
            emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT);
            emailParam.setTemplateContent(emailMap);
            if(appSubmissionDto.getAppGrpId()==null){
                emailParam.setQueryCode(appSubmissionDto.getLicenceNo());
                emailParam.setReqRefNum(appSubmissionDto.getLicenceNo());
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(appSubmissionDto.getLicenceId());
            }else {
                emailParam.setQueryCode(appSubmissionDto.getAppGrpNo());
                emailParam.setReqRefNum(appSubmissionDto.getAppGrpNo());
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP_GRP);
                emailParam.setRefId(appSubmissionDto.getAppGrpId());
            }
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            MsgTemplateDto rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT).getEntity();
            map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{appSubmissionDto.getAppType()}).get(0).getText());
            map.put("ApplicationNumber", appSubmissionDto.getAppGrpNo());
            String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            emailParam.setSubject(subject);
            //email
            notificationHelper.sendNotification(emailParam);
            //msg
            try {
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_MSG);
                String svcName=appSubmissionDto.getServiceName();
                if(svcName==null){
                    LicenceDto licenceDto= licenceClient.getLicBylicNo(appSubmissionDto.getLicenceNo()).getEntity();
                    svcName=licenceDto.getSvcName();
                }
                List<HcsaServiceDto> svcDto = appConfigClient.getHcsaServiceByNames(Collections.singletonList(svcName)).getEntity();
                List<String> svcCode=IaisCommonUtils.genNewArrayList();
                svcCode.add(svcDto.get(0).getSvcCode());
                rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_MSG).getEntity();
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
                emailParam.setSubject(subject);
                emailParam.setSvcCodeList(svcCode);
                emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                emailParam.setRefId(appSubmissionDto.getLicenceId());
                notificationHelper.sendNotification(emailParam);
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
            //sms
            rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_SMS).getEntity();
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            emailParam.setSubject(subject);
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_SMS);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
            notificationHelper.sendNotification(emailParam);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public boolean serviceConfigIsChange(List<String> serviceId, String presmiseType) {
        if(serviceId!=null && !serviceId.isEmpty() && presmiseType!=null){
            Set<String> appGrpPremisesTypeBySvcId = serviceConfigService.getAppGrpPremisesTypeBySvcId(serviceId);
            if(appGrpPremisesTypeBySvcId.contains(presmiseType)){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void sendRfcSubmittedEmail(List<AppSubmissionDto> appSubmissionDtos, String pmtMethod) throws IOException, TemplateException {
        AppSubmissionDto appSubmissionDto=appSubmissionDtos.get(0);
        String appGroupId = appSubmissionDto.getAppGrpId();

        double a = 0.0;
        for (AppSubmissionDto appSubmDto : appSubmissionDtos) {
            if(appSubmDto.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                a = a + appSubmDto.getAmount();
            }
        }
        appSubmissionDto.setAmountStr(Formatter.formatterMoney(a));
        String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();

        if(appSubmissionDto.getLicenceNo()==null){
            LicenceDto licenceDto= licenceClient.getLicBylicId(appSubmissionDto.getLicenceId()).getEntity();
            appSubmissionDto.setLicenceNo(licenceDto.getLicenceNo());
            appSubmissionDto.setServiceName(licenceDto.getSvcName());
        }

        //TODO Need to be replaced with appSubmissionDto, and set submit by id to it
        ApplicationGroupDto applicationGroupDto = applicationFeClient.getApplicationGroup(appGroupId).getEntity();
        if (applicationGroupDto != null){
            OrgUserDto orgUserDto = organizationLienceseeClient.retrieveOneOrgUserAccount(applicationGroupDto.getSubmitBy()).getEntity();
            if (orgUserDto != null){
                emailMap.put("ApplicantName", orgUserDto.getDisplayName());
            }
        }

        if(pmtMethod==null){
            emailMap.remove("GIRO_PAY");
            emailMap.remove("Online_PAY");
        }else {
            if (pmtMethod.equals(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO)) {
                emailMap.put("GIRO_PAY", "true");
                emailMap.put("GIRO_account_number", "");
                emailMap.put("usual_text_for_GIRO_deduction", appSubmissionDto.getLateFeeStr());
            } else {
                emailMap.put("Online_PAY", "true");
            }
        }
         if("$0.0".equals(appSubmissionDto.getAmountStr())){
             emailMap.remove("GIRO_PAY");
             emailMap.remove("Online_PAY");
         }

        emailMap.put("Payment_Amount", appSubmissionDto.getAmountStr());
        emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{appSubmissionDto.getAppType()}).get(0).getText());
        emailMap.put("ApplicationNumber", appSubmissionDto.getAppGrpNo());
        emailMap.put("ApplicationDate", Formatter.formatDate(new Date()));
        emailMap.put("systemLink", loginUrl);
        emailMap.put("email_address", systemParamConfig.getSystemAddressOne());
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT);
        emailParam.setTemplateContent(emailMap);
        if(appSubmissionDto.getAppGrpId()==null){
            emailParam.setQueryCode(appSubmissionDto.getLicenceNo());
            emailParam.setReqRefNum(appSubmissionDto.getLicenceNo());
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
            emailParam.setRefId(appSubmissionDto.getLicenceId());
        }else {
            emailParam.setQueryCode(appSubmissionDto.getAppGrpNo());
            emailParam.setReqRefNum(appSubmissionDto.getAppGrpNo());
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP_GRP);
            emailParam.setRefId(appSubmissionDto.getAppGrpId());
        }
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        MsgTemplateDto rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT).getEntity();
        map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{appSubmissionDto.getAppType()}).get(0).getText());
        map.put("ApplicationNumber", appSubmissionDto.getAppGrpNo());
        String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        emailParam.setSubject(subject);
        //email
        notificationHelper.sendNotification(emailParam);
        //msg
        try {
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_MSG);

            List<String> svcCode=IaisCommonUtils.genNewArrayList();
            for (AppSubmissionDto appSubmissionDto1:appSubmissionDtos){
                String svcName=appSubmissionDto1.getServiceName();
                if(svcName==null){
                    LicenceDto licenceDto= licenceClient.getLicBylicNo(appSubmissionDto1.getLicenceNo()).getEntity();
                    svcName=licenceDto.getSvcName();
                }
                List<HcsaServiceDto> svcDto = appConfigClient.getHcsaServiceByNames(Collections.singletonList(svcName)).getEntity();
                if(appSubmissionDto1.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)) {
                    svcCode.add(svcDto.get(0).getSvcCode());
                }
            }
            rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_MSG).getEntity();
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            emailParam.setSubject(subject);
            emailParam.setSvcCodeList(svcCode);
            emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            emailParam.setRefId(appSubmissionDto.getLicenceId());
            notificationHelper.sendNotification(emailParam);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        //sms
        rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_SMS).getEntity();
        subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        emailParam.setSubject(subject);
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_SMS);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
        notificationHelper.sendNotification(emailParam);
    }

    @Override
    public  List<AppSvcDocDto> updateSvcDoc(List<AppSvcDocDto> appSvcDocDtos,List<HcsaSvcDocConfigDto> oldSvcDocConfigDtos,List<HcsaSvcDocConfigDto> svcDocConfigDtos) throws Exception {

        List<AppSvcDocDto> newAppSvcDocDtoList =  IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos) && !IaisCommonUtils.isEmpty(oldSvcDocConfigDtos) && !IaisCommonUtils.isEmpty(svcDocConfigDtos)){
            for(AppSvcDocDto appSvcDocDto : appSvcDocDtos){
                for(HcsaSvcDocConfigDto oldSvcDocConfigDto:oldSvcDocConfigDtos){
                    if(appSvcDocDto.getSvcDocId().equals(oldSvcDocConfigDto.getId())){
                        String titleName = oldSvcDocConfigDto.getDocTitle();
                        if(!StringUtil.isEmpty(titleName)){
                            for(HcsaSvcDocConfigDto svcDocConfigDto:svcDocConfigDtos){
                                if(titleName.equals(svcDocConfigDto.getDocTitle())){
                                    AppSvcDocDto newAppSvcDocDto = (AppSvcDocDto) CopyUtil.copyMutableObject(appSvcDocDto);
                                    newAppSvcDocDto.setSvcDocId(svcDocConfigDto.getId());
                                    newAppSvcDocDtoList.add(newAppSvcDocDto);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return newAppSvcDocDtoList;
    }


    private void validateVehicleNo(Map<String, String> errorMap, Set<String> distinctVehicleNo, int numberCount, String conveyanceVehicleNo) {
        if (StringUtil.isEmpty(conveyanceVehicleNo)) {
            errorMap.put("conveyanceVehicleNo" + numberCount, MessageUtil.replaceMessage("GENERAL_ERR0006", "Vehicle No.", "field"));
        } else {
            if(conveyanceVehicleNo.length() > 10){
                String general_err0041=NewApplicationHelper.repLength("Vehicle No.","10");
                errorMap.put("conveyanceVehicleNo" + numberCount, general_err0041);
            }
            boolean b = VehNoValidator.validateNumber(conveyanceVehicleNo);
            if (!b) {
                errorMap.put("conveyanceVehicleNo" + numberCount, "GENERAL_ERR0017");
            }

            if (distinctVehicleNo.contains(conveyanceVehicleNo)) {
                errorMap.put("conveyanceVehicleNo" + numberCount, "NEW_ERR0016");
            } else {
                distinctVehicleNo.add(conveyanceVehicleNo);
            }
        }
    }

    private int validateTime(Map<String, String> errorMap, String onsiteHH, String onsiteMM, int date, String key, int i) {
        try {
            int i1 = Integer.parseInt(onsiteHH);
            int i2 = Integer.parseInt(onsiteMM);
            date = i1 * 60 + i2 * 1;
            if (i1 >= 24) {
                errorMap.put(key + i, "NEW_ERR0013");
            } else if (i2 >= 60) {
                errorMap.put(key + i, "NEW_ERR0014");
            }
        } catch (Exception e) {
            errorMap.put(key + i, "GENERAL_ERR0002");
        }
        return date;
    }

    private void  checkHciIsSame(AppGrpPremisesDto appGrpPremisesDto,List<String> premisesHciList,Map<String, String> errorMap,String errName){
        List<String> currHciList = NewApplicationHelper.genPremisesHciList(appGrpPremisesDto);
        for(String hci:currHciList){
            if(premisesHciList.contains(hci)){
                errorMap.put(errName, "NEW_ERR0005");
            }
        }
    }

    private void checkOperaionUnit(List<AppPremisesOperationalUnitDto> operationalUnitDtos,Map<String, String> errorMap,String floorErrName,String unitErrName,List<String> floorUnitList,String floorUnitErrName){

        if(!IaisCommonUtils.isEmpty(operationalUnitDtos)){
            int opLength = 0;
            for(AppPremisesOperationalUnitDto operationalUnitDto:operationalUnitDtos){
                boolean flag = true;
                String floorNo = operationalUnitDto.getFloorNo();
                String unitNo = operationalUnitDto.getUnitNo();
                boolean floorNoFlag = StringUtil.isEmpty(floorNo);
                boolean unitNoFlag = StringUtil.isEmpty(unitNo);
                if(!(floorNoFlag && unitNoFlag)){
                    if(floorNoFlag){
                        flag = false;
                        errorMap.put(floorErrName + opLength, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
                    }else if(unitNoFlag) {
                        flag = false;
                        errorMap.put(unitErrName + opLength, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
                    }
                }

                if(!floorNoFlag && floorNo.length() > 3){
                    String general_err0041=NewApplicationHelper.repLength("Floor No.","3");
                    errorMap.put(floorErrName + opLength, general_err0041);
                }

                if(!unitNoFlag && unitNo.length() > 5){
                    String general_err0041=NewApplicationHelper.repLength("Unit No.","5");
                    errorMap.put(unitErrName + opLength, general_err0041);
                }

                String floorNoErr = errorMap.get(floorErrName + opLength);
                if (StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)) {
                    Pattern pattern = compile("[0-9]*");
                    boolean noFlag = pattern.matcher(floorNo).matches();
                    if (noFlag) {
                        int floorNum = Integer.parseInt(floorNo);
                        if (10 > floorNum) {
                            floorNo = "0" + floorNum;
                            operationalUnitDto.setFloorNo(floorNo);
                        }
                    }
                }
                if(flag){
                    if(!StringUtil.isEmpty(operationalUnitDto.getFloorNo()) && !StringUtil.isEmpty(operationalUnitDto.getUnitNo())){
                        String floorUnitStr = operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo();
                        if(floorUnitList.contains(floorUnitStr)){
                            errorMap.put(floorUnitErrName + opLength, "NEW_ERR0017");
                        }else{
                            floorUnitList.add(floorUnitStr);
                        }
                    }
                }
                opLength++;
            }
        }

    }

}
