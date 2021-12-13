package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.action.RequestForChangeMenuDelegator;
import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.CheckCoLocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicBaseSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
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
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.VehNoValidator;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.rfcutil.EqRequestForChangeSubmitResultChange;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.validate.impl.ValidateEasmts;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    @Autowired
    private ValidateEasmts validateEasmts;
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
        List<String> finalStatusList = IaisCommonUtils.getAppFinalStatus();
        for (ApplicationDto applicationDto : applicationDtos) {
            if(!finalStatusList.contains(applicationDto.getStatus())){
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
    public LicenceDto getLicDtoById(String licenceId) {
        LicenceDto result = null;
        if(!StringUtil.isEmpty(licenceId)){
            result = licenceClient.getLicDtoById(licenceId).getEntity();
        }
        return result;
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
        log.info(StringUtil.changeForLog("Hci code: " + hciCode + " - Licensee: " + licenseeId));
        if (StringUtil.isEmpty(hciCode) || StringUtil.isEmpty(licenseeId)) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        return licenceClient.getLicenceDtoByHciCode(hciCode, licenseeId).getEntity();
    }

    @Override
    public List<LicenceDto> getLicenceDtoByHciCode(String licenseeId, AppGrpPremisesDto appGrpPremisesDto, String... excludeNos) {
        if (StringUtil.isEmpty(licenseeId) || appGrpPremisesDto == null) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        String hciCode = appGrpPremisesDto.getHciCode();
        String oldHciCode = appGrpPremisesDto.getOldHciCode();
        if (!StringUtil.isEmpty(oldHciCode) && !oldHciCode.equals(hciCode)) {
            hciCode = oldHciCode;
        }
        log.info(StringUtil.changeForLog("Hci code: " + hciCode + " - Licensee: " + licenseeId));
        List<LicenceDto> licenceDtos = getLicenceDtoByHciCode(hciCode, licenseeId);
        if (licenceDtos == null || licenceDtos.isEmpty()) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        List<LicenceDto> licenceDtoList = licenceDtos.stream()
                .filter(dto -> !StringUtil.isIn(dto.getLicenceNo(), excludeNos))
                .collect(Collectors.toList());
        licenceDtoList.forEach(
                licenceDto -> log.info(StringUtil.changeForLog("--- licenceDto licenceNo : " + licenceDto.getLicenceNo())));
        return licenceDtoList;
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
    public Map<String, String> doValidatePremiss(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto,
            List<String> premisesHciList, boolean rfi, boolean checkOthers) {
        //do validate one premiss
        String keywords = MasterCodeUtil.getCodeDesc("MS001");
        List<String> list = IaisCommonUtils.genNewArrayList();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Set<String> distinctVehicleNo = IaisCommonUtils.genNewHashSet();
        boolean needAppendMsg = false;
        String licenseeId = appSubmissionDto.getLicenseeId();
        String licenceId = appSubmissionDto.getLicenceId();
        String premiseTypeError="";
        String selectPremises="";
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
            String premiseType = appGrpPremisesDto.getPremisesType();
            boolean hciFlag = false;
            if (StringUtil.isEmpty(premiseType)) {
                if("".equals(premiseTypeError)){
                    premiseTypeError= MessageUtil.replaceMessage("GENERAL_ERR0006", "What is your premises type", "field");
                }
                errorMap.put("premisesType" + i, premiseTypeError);
            } else {
                String premisesSelect = appGrpPremisesDto.getPremisesSelect();
                String appType = appSubmissionDto.getAppType();
                boolean needValidate = false;

                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
                    String oldPremSel = oldAppSubmissionDto == null ? "-1" :
                            oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0).getPremisesSelect();
                    if ("-1".equals(oldPremSel) || !StringUtil.isEmpty(oldPremSel) && oldPremSel.equals(premisesSelect)) {
                        needValidate = true;
                    }
                }
                if (StringUtil.isEmpty(premisesSelect) || "-1".equals(premisesSelect)) {
                    if("".equals(selectPremises)){
                        selectPremises= MessageUtil.replaceMessage("GENERAL_ERR0006", "Add or select a premises from the list", "field");
                    }
                    errorMap.put("premisesSelect" + i, selectPremises);
                } else if (needValidate || !StringUtil.isEmpty(premisesSelect) || "newPremise".equals(premisesSelect)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    List<String> floorUnitNo=new ArrayList<>(10);
                    List<AppPremisesOperationalUnitDto> operationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
                    List<String> floorUnitList = IaisCommonUtils.genNewArrayList();
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)) {

                        String locateWithOthers = appGrpPremisesDto.getLocateWithOthers();
                        if (StringUtil.isEmpty(locateWithOthers)) {
                            errorMap.put("isOtherLic" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Are you co-locating with another licensee", "field"));
                        }

                        //weekly
                        List<OperationHoursReloadDto> weeklyDtos = appGrpPremisesDto.getWeeklyDtoList();
                        String emptyErrMsg = MessageUtil.getMessageDesc("GENERAL_ERR0006");
                        if(IaisCommonUtils.isEmpty(weeklyDtos)){
                            errorMap.put("onSiteWeekly" + i+0,emptyErrMsg);
                            errorMap.put("onSiteWeeklyStart" + i+0,emptyErrMsg);
                            errorMap.put("onSiteWeeklyEnd" + i+0,emptyErrMsg);
                        }else {
                            int j = 0;
                            for(OperationHoursReloadDto weeklyDto:weeklyDtos){
                                Map<String,String> errNameMap = IaisCommonUtils.genNewHashMap();
                                errNameMap.put("select","onSiteWeekly");
                                errNameMap.put("start","onSiteWeeklyStart");
                                errNameMap.put("end","onSiteWeeklyEnd");
                                errNameMap.put("time","onSiteWeeklyTime");
                                doOperationHoursValidate(weeklyDto,errorMap,errNameMap,i+""+j,true);
                                j++;
                            }
                            appGrpPremisesDto.setWeeklyDtoList(weeklyDtos);
                        }
                        //ph
                        List<OperationHoursReloadDto> phDtos = appGrpPremisesDto.getPhDtoList();
                        if(!IaisCommonUtils.isEmpty(phDtos)){
                            int j = 0;
                            for(OperationHoursReloadDto phDto:phDtos){
                                Map<String,String> errNameMap = IaisCommonUtils.genNewHashMap();
                                errNameMap.put("select","onSitePubHoliday");
                                errNameMap.put("start","onSitePhStart");
                                errNameMap.put("end","onSitePhEnd");
                                errNameMap.put("time","onSitePhTime");
                                doOperationHoursValidate(phDto,errorMap,errNameMap,i+""+j,false);
                                j++;
                            }
                            appGrpPremisesDto.setPhDtoList(phDtos);
                        }
                        //event
                        List<AppPremEventPeriodDto> eventDtos = appGrpPremisesDto.getEventDtoList();
                        if(!IaisCommonUtils.isEmpty(eventDtos)){
                            int j = 0;
                            for(AppPremEventPeriodDto eventDto:eventDtos){
                                String eventName = eventDto.getEventName();
                                Date startDate  = eventDto.getStartDate();
                                Date endDate  = eventDto.getEndDate();
                                if(!StringUtil.isEmpty(eventName) || startDate != null || endDate != null){
                                    boolean dateIsEmpty = false;
                                    if(StringUtil.isEmpty(eventName)){
                                        errorMap.put("onSiteEvent" + i+j,emptyErrMsg);
                                    }else if(eventName.length() > 100){
                                        errorMap.put("onSiteEvent" + i+j,NewApplicationHelper.repLength("Event Name","100"));
                                    }
                                    if(startDate == null){
                                        errorMap.put("onSiteEventStart" + i+j,emptyErrMsg);
                                        dateIsEmpty = true;
                                    }
                                    if(endDate == null){
                                        errorMap.put("onSiteEventEnd" + i+j,emptyErrMsg);
                                        dateIsEmpty = true;
                                    }
                                    if(!dateIsEmpty){
                                        if(startDate.after(endDate)){
                                            errorMap.put("onSiteEventDate"+i+j,MessageUtil.getMessageDesc("NEW_ERR0020"));
                                        }
                                    }
                                }
                                j++;
                            }
                        }

                        String ScdfRefNo = appGrpPremisesDto.getScdfRefNo();
                        if(!StringUtil.isEmpty(ScdfRefNo) && ScdfRefNo.length() > 66){
                            String general_err0041=NewApplicationHelper.repLength("Fire Safety & Shelter Bureau Ref. No.","66");
                            errorMap.put("ScdfRefNo" + i, general_err0041);
                        }

                        String buildingName = appGrpPremisesDto.getBuildingName();
                        if(!StringUtil.isEmpty(buildingName) && buildingName.length() > 66){
                            String general_err0041=NewApplicationHelper.repLength("Building Name","66");
                            errorMap.put("buildingName" + i, general_err0041);
                        }

                        String hciName = appGrpPremisesDto.getHciName();
                        //migrated licence need  judge
                        if (StringUtil.isEmpty(hciName)) {
                            errorMap.put("hciName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "HCI Name", "field"));
                        } else {
                            if (hciName.length() > 100) {
                                String general_err0041=NewApplicationHelper.repLength("HCI Name","100");
                                errorMap.put("hciName" + i, general_err0041);
                            }
                            checkHciName("hciName" + i, hciName, appType, licenceId, errorMap);

                            if (StringUtil.isEmpty(licenseeId)) {
                                //licenseeId = "9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
                                log.debug(StringUtil.changeForLog("can not found licenseeId"));
                            }
                            List<PremisesDto> premisesDtos = licenceClient.getPremisesDtoByHciNameAndPremType(hciName,ApplicationConsts.PREMISES_TYPE_ON_SITE,licenseeId).getEntity();
                            if(!IaisCommonUtils.isEmpty(premisesDtos)){
                                errorMap.put("hciNameUsed", MessageUtil.getMessageDesc("NEW_ACK011"));
                            }
                        }
                        String offTelNo = appGrpPremisesDto.getOffTelNo();
                        if (StringUtil.isEmpty(offTelNo)) {
                            errorMap.put("offTelNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Office Telephone No.", "field"));
                        } else {
                            if(offTelNo.length()>8){
                                String general_err0041=NewApplicationHelper.repLength("Office Telephone No.","8");
                                errorMap.put("offTelNo" + i, general_err0041);
                            }
                            boolean matches = offTelNo.matches(IaisEGPConstant.OFFICE_TELNO_MATCH);
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

                        String email = appGrpPremisesDto.getEasMtsPubEmail();
                        if(StringUtil.isEmpty(email)){
                            errorMap.put("onSiteEmail" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Email ", "field"));
                        }else {
                            if(!ValidationUtils.isEmail(email)){
                                errorMap.put("onSiteEmail" + i, MessageUtil.getMessageDesc("GENERAL_ERR0014"));
                            }
                        }

                        String addrType = appGrpPremisesDto.getAddrType();

                        boolean addrTypeFlag = true;
                        if (StringUtil.isEmpty(addrType)) {
                            addrTypeFlag = false;
                            errorMap.put("addrType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
                        } else {
                            String floorNo = appGrpPremisesDto.getFloorNo();
                            String blkNo = appGrpPremisesDto.getBlkNo();
                            String unitNo = appGrpPremisesDto.getUnitNo();
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
                            appGrpPremisesDto.setFloorNo(NewApplicationHelper.handleFloorNo(floorNo, floorNoErr));
                            if (!empty && !empty1 && !empty2) {
                                StringBuilder sb=new StringBuilder();
                                sb.append(appGrpPremisesDto.getFloorNo())
                                        .append(appGrpPremisesDto.getBlkNo())
                                        .append(appGrpPremisesDto.getUnitNo());
                                floorUnitNo.add(sb.toString());
                            }
                        }
                        if(addrTypeFlag){
                            floorUnitList.add(appGrpPremisesDto.getFloorNo() + appGrpPremisesDto.getUnitNo());
                        }

                        checkOperaionUnit(operationalUnitDtos,errorMap,"opFloorNo"+i,"opUnitNo"+i,floorUnitList,"floorUnit"+i, floorUnitNo,appGrpPremisesDto);

                        String postalCode = appGrpPremisesDto.getPostalCode();
                        if (!StringUtil.isEmpty(postalCode)) {
                            if(postalCode.length() > 6){
                                String general_err0041=NewApplicationHelper.repLength("Postal Code","6");
                                errorMap.put("postalCode" + i, general_err0041);
                            }else if (postalCode.length() < 6) {
                                errorMap.put("postalCode" + i, "NEW_ERR0004");
                            } else if (!postalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("postalCode" + i, "NEW_ERR0004");
                            } else {
                                if (!floorUnitNo.isEmpty()) {
                                    stringBuilder.append(postalCode);
                                    if(list.isEmpty()){
                                        for(String str:floorUnitNo){
                                            StringBuilder sb=new StringBuilder(stringBuilder);
                                            sb.append(str);
                                            list.add(sb.toString());
                                        }
                                    }else {
                                        List<String> sbList=new ArrayList<>();
                                        for(String str:floorUnitNo){
                                            StringBuilder sb=new StringBuilder(stringBuilder);
                                            sb.append(str);
                                            if(list.contains(sb.toString())){
                                                errorMap.put("postalCode" + i, "NEW_ACK010");
                                            }else {
                                                sbList.add(sb.toString());
                                            }
                                        }
                                        list.addAll(sbList);
                                    }

                                }
                            }
                        } else {
                            errorMap.put("postalCode" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code ", "field"));
                        }
                        String hciNameErr = errorMap.get("hciName" + i);
                        String postalCodeErr = errorMap.get("postalCode" + i);
                        String blkNoErr = errorMap.get("blkNo" + i);
                        String floorNoErr = errorMap.get("floorNo" + i);
                        String unitNoErr = errorMap.get("unitNo" + i);
                        hciFlag = StringUtil.isEmpty(hciNameErr) && StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:" + hciFlag));
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

                        //weekly
                        List<OperationHoursReloadDto> weeklyDtos = appGrpPremisesDto.getWeeklyDtoList();
                        String emptyErrMsg = MessageUtil.getMessageDesc("GENERAL_ERR0006");
                        if(IaisCommonUtils.isEmpty(weeklyDtos)){
                            errorMap.put("conveyanceWeekly" + i+0,emptyErrMsg);
                            errorMap.put("conveyanceWeeklyStart" + i+0,emptyErrMsg);
                            errorMap.put("conveyanceWeeklyEnd" + i+0,emptyErrMsg);
                        }else {
                            int j = 0;
                            for(OperationHoursReloadDto weeklyDto:weeklyDtos){
                                Map<String,String> errNameMap = IaisCommonUtils.genNewHashMap();
                                errNameMap.put("select","conveyanceWeekly");
                                errNameMap.put("start","conveyanceWeeklyStart");
                                errNameMap.put("end","conveyanceWeeklyEnd");
                                errNameMap.put("time","conveyanceWeeklyTime");
                                doOperationHoursValidate(weeklyDto,errorMap,errNameMap,i+""+j,true);
                                j++;
                            }
                            appGrpPremisesDto.setWeeklyDtoList(weeklyDtos);
                        }
                        //ph
                        List<OperationHoursReloadDto> phDtos = appGrpPremisesDto.getPhDtoList();
                        if(!IaisCommonUtils.isEmpty(phDtos)){
                            int j = 0;
                            for(OperationHoursReloadDto phDto:phDtos){
                                Map<String,String> errNameMap = IaisCommonUtils.genNewHashMap();
                                errNameMap.put("select","conveyancePubHoliday");
                                errNameMap.put("start","conveyancePhStart");
                                errNameMap.put("end","conveyancePhEnd");
                                errNameMap.put("time","conveyancePhTime");
                                doOperationHoursValidate(phDto,errorMap,errNameMap,i+""+j,false);
                                j++;
                            }
                            appGrpPremisesDto.setPhDtoList(phDtos);
                        }
                        //event
                        List<AppPremEventPeriodDto> eventDtos = appGrpPremisesDto.getEventDtoList();
                        if(!IaisCommonUtils.isEmpty(eventDtos)){
                            int j = 0;
                            for(AppPremEventPeriodDto eventDto:eventDtos){
                                String eventName = eventDto.getEventName();
                                Date startDate  = eventDto.getStartDate();
                                Date endDate  = eventDto.getEndDate();
                                if(!StringUtil.isEmpty(eventName) || startDate != null || endDate != null){
                                    boolean dateIsEmpty = false;
                                    if(StringUtil.isEmpty(eventName)){
                                        errorMap.put("conveyanceEvent" + i+j,emptyErrMsg);
                                    }else if(eventName.length() > 100){
                                        errorMap.put("conveyanceEvent" + i+j,NewApplicationHelper.repLength("Event Name","100"));
                                    }
                                    if(startDate == null){
                                        errorMap.put("conveyanceEventStart" + i+j,emptyErrMsg);
                                        dateIsEmpty = true;
                                    }
                                    if(endDate == null){
                                        errorMap.put("conveyanceEventEnd" + i+j,emptyErrMsg);
                                        dateIsEmpty = true;
                                    }
                                    if(!dateIsEmpty){
                                        if(startDate.after(endDate)){
                                            errorMap.put("conveyanceEventDate"+i+j,MessageUtil.getMessageDesc("NEW_ERR0020"));
                                        }
                                    }
                                }
                                j++;
                            }
                        }

                        String convHciName = appGrpPremisesDto.getConveyanceHciName();
                        if (StringUtil.isEmpty(convHciName)) {
                            errorMap.put("conveyanceHciName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "HCI Name", "field"));
                        }else{
                            if(convHciName.length()>100){
                                String general_err0041=NewApplicationHelper.repLength("HCI Name","100");
                                errorMap.put("conveyanceHciName" + i, general_err0041);
                            }
                            checkHciName("conveyanceHciName" + i, convHciName, appType, licenceId, errorMap);

                            List<PremisesDto> premisesDtos = licenceClient.getPremisesDtoByHciNameAndPremType(convHciName,
                                    ApplicationConsts.PREMISES_TYPE_CONVEYANCE, licenseeId).getEntity();
                            if (!IaisCommonUtils.isEmpty(premisesDtos)) {
                                errorMap.put("hciNameUsed", MessageUtil.getMessageDesc("NEW_ACK011"));
                            }

                        }

                        String buildingName = appGrpPremisesDto.getConveyanceBuildingName();
                        if(!StringUtil.isEmpty(buildingName) && buildingName.length() > 66){
                            String general_err0041=NewApplicationHelper.repLength("Building Name","66");
                            errorMap.put("conveyanceBuildingName" + i, general_err0041);
                        }

                        String email = appGrpPremisesDto.getConveyanceEmail();
                        if(StringUtil.isEmpty(email)){
                            errorMap.put("conveyanceEmail" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Email ", "field"));
                        }else {
                            if(!ValidationUtils.isEmail(email)){
                                errorMap.put("conveyanceEmail" + i, MessageUtil.getMessageDesc("GENERAL_ERR0014"));
                            }
                        }

                        String conveyanceVehicleNo = appGrpPremisesDto.getConveyanceVehicleNo();
                        validateVehicleNo(errorMap, distinctVehicleNo, i, conveyanceVehicleNo);

                        String cStreetName = appGrpPremisesDto.getConveyanceStreetName();
                        if (StringUtil.isEmpty(cStreetName)) {
                            errorMap.put("conveyanceStreetName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name ", "field"));
                        }else if(cStreetName.length() > 32){
                            String general_err0041=NewApplicationHelper.repLength("Street Name","32");
                            errorMap.put("conveyanceStreetName" + i, general_err0041);
                        }
                        boolean addrTypeFlag = true;
                        String conveyanceAddressType = appGrpPremisesDto.getConveyanceAddressType();
                        if (StringUtil.isEmpty(conveyanceAddressType)) {
                            addrTypeFlag = false;
                            errorMap.put("conveyanceAddressType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type ", "field"));
                        } else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDto.getConveyanceFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDto.getConveyanceBlockNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDto.getConveyanceUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(conveyanceAddressType)) {

                                if (empty) {
                                    addrTypeFlag = false;
                                    errorMap.put("conveyanceFloorNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
                                }else if(appGrpPremisesDto.getConveyanceFloorNo().length()>3){
                                    String general_err0041=NewApplicationHelper.repLength("Floor No.","3");
                                    errorMap.put("conveyanceFloorNo" + i, general_err0041);
                                }
                                if (empty1) {
                                    addrTypeFlag = false;
                                    errorMap.put("conveyanceBlockNos" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
                                }else if(appGrpPremisesDto.getConveyanceBlockNo().length()>10){
                                    String general_err0041=NewApplicationHelper.repLength("Block / House No.","10");
                                    errorMap.put("conveyanceBlockNos" + i, general_err0041);
                                }
                                if (empty2) {
                                    addrTypeFlag = false;
                                    errorMap.put("conveyanceUnitNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
                                }else if(appGrpPremisesDto.getConveyanceUnitNo().length()>5){
                                    String general_err0041=NewApplicationHelper.repLength("Unit No.","5");
                                    errorMap.put("conveyanceUnitNo" + i, general_err0041);
                                }

                            }
                            String floorNoErr = errorMap.get("conveyanceFloorNo" + i);
                            String floorNo = appGrpPremisesDto.getConveyanceFloorNo();
                            appGrpPremisesDto.setConveyanceFloorNo(NewApplicationHelper.handleFloorNo(floorNo, floorNoErr));
                            if (!empty && !empty1 && !empty2) {
                                StringBuilder sb=new StringBuilder();
                                sb.append(appGrpPremisesDto.getConveyanceFloorNo())
                                        .append(appGrpPremisesDto.getConveyanceBlockNo())
                                        .append(appGrpPremisesDto.getConveyanceUnitNo());
                                floorUnitNo.add(sb.toString());
                            }
                        }
                        if(addrTypeFlag){
                            floorUnitList.add(appGrpPremisesDto.getConveyanceFloorNo() + appGrpPremisesDto.getConveyanceUnitNo());
                        }

                        checkOperaionUnit(operationalUnitDtos,errorMap,"opConvFloorNo"+i,"opConvUnitNo"+i,floorUnitList,"ConvFloorUnit"+i,floorUnitNo,appGrpPremisesDto);

                        String conveyancePostalCode = appGrpPremisesDto.getConveyancePostalCode();
                        if (StringUtil.isEmpty(conveyancePostalCode)) {
                            errorMap.put("conveyancePostalCode" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code", "field"));
                        } else {
                            if(conveyancePostalCode.length() > 6){
                                String general_err0041=NewApplicationHelper.repLength("Postal Code","6");
                                errorMap.put("conveyancePostalCode" + i, general_err0041);
                            }else if (conveyancePostalCode.length() < 6) {
                                errorMap.put("conveyancePostalCode" + i, "NEW_ERR0004");
                            } else if (!conveyancePostalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("conveyancePostalCode" + i, "NEW_ERR0004");
                            } else {
                                if (!floorUnitNo.isEmpty()) {
                                    stringBuilder.append(conveyancePostalCode);
                                    if(list.isEmpty()){
                                        for(String str:floorUnitNo){
                                            StringBuilder sb=new StringBuilder(stringBuilder);
                                            sb.append(str);
                                            list.add(sb.toString());
                                        }
                                    }else {
                                        List<String> sbList=new ArrayList<>();
                                        for(String str:floorUnitNo){
                                            StringBuilder sb=new StringBuilder(stringBuilder);
                                            sb.append(str);
                                            if(list.contains(sb.toString())){
                                                errorMap.put("conveyancePostalCode" + i, "NEW_ACK010");
                                            }else {
                                                sbList.add(sb.toString());
                                            }
                                        }
                                        list.addAll(sbList);
                                    }

                                 /*   if (list.contains(stringBuilder.toString())) {
                                        errorMap.put("postalCode" + i, "NEW_ACK010");

                                    } else {
                                        list.add(stringBuilder.toString());
                                    }*/
                                }
                            }
                        }

                        //0062204
                        String vehicleNo = errorMap.get("conveyanceVehicleNo" + i);
                        String postalCodeErr = errorMap.get("conveyancePostalCode" + i);
                        String blkNoErr = errorMap.get("conveyanceBlockNos" + i);
                        String floorNoErr = errorMap.get("conveyanceFloorNo" + i);
                        String unitNoErr = errorMap.get("conveyanceUnitNo" + i);
                        String hciNAmeErr = errorMap.get("conveyanceHciName" + i);
                        hciFlag = StringUtil.isEmpty(hciNAmeErr) && StringUtil.isEmpty(vehicleNo) && StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:" + hciFlag));
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

                        //weekly
                        List<OperationHoursReloadDto> weeklyDtos = appGrpPremisesDto.getWeeklyDtoList();
                        String emptyErrMsg = MessageUtil.getMessageDesc("GENERAL_ERR0006");
                        if(IaisCommonUtils.isEmpty(weeklyDtos)){
                            errorMap.put("offSiteWeekly" + i+0,emptyErrMsg);
                            errorMap.put("offSiteWeeklyStart" + i+0,emptyErrMsg);
                            errorMap.put("offSiteWeeklyEnd" + i+0,emptyErrMsg);
                        }else {
                            int j = 0;
                            for(OperationHoursReloadDto weeklyDto:weeklyDtos){
                                Map<String,String> errNameMap = IaisCommonUtils.genNewHashMap();
                                errNameMap.put("select","offSiteWeekly");
                                errNameMap.put("start","offSiteWeeklyStart");
                                errNameMap.put("end","offSiteWeeklyEnd");
                                errNameMap.put("time","offSiteWeeklyTime");
                                doOperationHoursValidate(weeklyDto,errorMap,errNameMap,i+""+j,true);
                                j++;
                            }
                            appGrpPremisesDto.setWeeklyDtoList(weeklyDtos);
                        }
                        //ph
                        List<OperationHoursReloadDto> phDtos = appGrpPremisesDto.getPhDtoList();
                        if(!IaisCommonUtils.isEmpty(phDtos)){
                            int j = 0;
                            for(OperationHoursReloadDto phDto:phDtos){
                                Map<String,String> errNameMap = IaisCommonUtils.genNewHashMap();
                                errNameMap.put("select","offSitePubHoliday");
                                errNameMap.put("start","offSitePhStart");
                                errNameMap.put("end","offSitePhEnd");
                                errNameMap.put("time","offSitePhTime");
                                doOperationHoursValidate(phDto,errorMap,errNameMap,i+""+j,false);
                                j++;
                            }
                            appGrpPremisesDto.setPhDtoList(phDtos);
                        }
                        //event
                        List<AppPremEventPeriodDto> eventDtos = appGrpPremisesDto.getEventDtoList();
                        if(!IaisCommonUtils.isEmpty(eventDtos)){
                            int j = 0;
                            for(AppPremEventPeriodDto eventDto:eventDtos){
                                String eventName = eventDto.getEventName();
                                Date startDate  = eventDto.getStartDate();
                                Date endDate  = eventDto.getEndDate();
                                if(!StringUtil.isEmpty(eventName) || startDate != null || endDate != null){
                                    boolean dateIsEmpty = false;
                                    if(StringUtil.isEmpty(eventName)){
                                        errorMap.put("offSiteEvent" + i+j,emptyErrMsg);
                                    }else if(eventName.length() > 100){
                                        errorMap.put("offSiteEvent" + i+j,NewApplicationHelper.repLength("Event Name","100"));
                                    }
                                    if(startDate == null){
                                        errorMap.put("offSiteEventStart" + i+j,emptyErrMsg);
                                        dateIsEmpty = true;
                                    }
                                    if(endDate == null){
                                        errorMap.put("offSiteEventEnd" + i+j,emptyErrMsg);
                                        dateIsEmpty = true;
                                    }
                                    if(!dateIsEmpty){
                                        if(startDate.after(endDate)){
                                            errorMap.put("offSiteEventDate"+i+j,MessageUtil.getMessageDesc("NEW_ERR0020"));
                                        }
                                    }
                                }

                                j++;
                            }
                        }

                        String offSiteHciName = appGrpPremisesDto.getOffSiteHciName();
                        if (StringUtil.isEmpty(offSiteHciName)) {
                            errorMap.put("offSiteHciName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "HCI Name", "field"));
                        } else {
                            if(offSiteHciName.length()>100){
                                String general_err0041=NewApplicationHelper.repLength("HCI Name","100");
                                errorMap.put("offSiteHciName" + i, general_err0041);
                            }
                            checkHciName("offSiteHciName" + i, offSiteHciName, appType, licenceId, errorMap);
                            List<PremisesDto> premisesDtos = licenceClient.getPremisesDtoByHciNameAndPremType(offSiteHciName,ApplicationConsts.PREMISES_TYPE_OFF_SITE,licenseeId).getEntity();
                            if(!IaisCommonUtils.isEmpty(premisesDtos)){
                                errorMap.put("hciNameUsed", MessageUtil.getMessageDesc("NEW_ACK011"));
                            }
                        }
                        String buildingName = appGrpPremisesDto.getOffSiteBuildingName();
                        if(!StringUtil.isEmpty(buildingName) && buildingName.length() > 66){
                            String general_err0041=NewApplicationHelper.repLength("Building Name","66");
                            errorMap.put("offSiteBuildingName" + i, general_err0041);
                        }

                        String email = appGrpPremisesDto.getOffSiteEmail();
                        if(StringUtil.isEmpty(email)){
                            errorMap.put("offSiteEmail" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Email ", "field"));
                        }else {
                            if(!ValidationUtils.isEmail(email)){
                                errorMap.put("offSiteEmail" + i, MessageUtil.getMessageDesc("GENERAL_ERR0014"));
                            }
                        }

                        String offSiteStreetName = appGrpPremisesDto.getOffSiteStreetName();
                        if (StringUtil.isEmpty(offSiteStreetName)) {
                            errorMap.put("offSiteStreetName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name", "field"));
                        }else if(offSiteStreetName.length() > 32){
                            String general_err0041=NewApplicationHelper.repLength("Street Name","32");
                            errorMap.put("offSiteStreetName" + i, general_err0041);
                        }

                        String offSiteAddressType = appGrpPremisesDto.getOffSiteAddressType();
                        boolean addrTypeFlag = true;
                        if (StringUtil.isEmpty(offSiteAddressType)) {
                            addrTypeFlag = false;
                            errorMap.put("offSiteAddressType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
                        } else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDto.getOffSiteFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDto.getOffSiteBlockNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDto.getOffSiteUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(offSiteAddressType)) {

                                if (empty) {
                                    addrTypeFlag = false;
                                    errorMap.put("offSiteFloorNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Floor No.", "field"));
                                }else if(appGrpPremisesDto.getOffSiteFloorNo().length() > 3){
                                    String general_err0041=NewApplicationHelper.repLength("Floor No.","3");
                                    errorMap.put("offSiteFloorNo" + i, general_err0041);
                                }
                                if (empty1) {
                                    addrTypeFlag = false;
                                    errorMap.put("offSiteBlockNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Block / House No.", "field"));
                                }else if(appGrpPremisesDto.getOffSiteBlockNo().length() > 10){
                                    String general_err0041=NewApplicationHelper.repLength("Block / House No.","10");
                                    errorMap.put("offSiteBlockNo" + i, general_err0041);
                                }
                                if (empty2) {
                                    addrTypeFlag = false;
                                    errorMap.put("offSiteUnitNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Unit No.", "field"));
                                }else if(appGrpPremisesDto.getOffSiteUnitNo().length() > 5){
                                    String general_err0041=NewApplicationHelper.repLength("Unit No.","5");
                                    errorMap.put("offSiteUnitNo" + i, general_err0041);
                                }
                            }
                            String floorNoErr = errorMap.get("offSiteFloorNo" + i);
                            String floorNo = appGrpPremisesDto.getOffSiteFloorNo();
                            appGrpPremisesDto.setOffSiteFloorNo(NewApplicationHelper.handleFloorNo(floorNo, floorNoErr));
                            if (!empty && !empty1 && !empty2) {
                                StringBuilder sb=new StringBuilder();
                                sb.append(appGrpPremisesDto.getOffSiteFloorNo())
                                        .append(appGrpPremisesDto.getOffSiteBlockNo())
                                        .append(appGrpPremisesDto.getOffSiteUnitNo());
                                floorUnitNo.add(sb.toString());
                            }
                        }
                        if(addrTypeFlag){
                            floorUnitList.add(appGrpPremisesDto.getOffSiteFloorNo() + appGrpPremisesDto.getOffSiteUnitNo());
                        }

                        checkOperaionUnit(operationalUnitDtos,errorMap,"opOffFloorNo"+i,"opOffUnitNo"+i,floorUnitList,"offFloorUnit"+i,floorUnitNo,appGrpPremisesDto);

                        String offSitePostalCode = appGrpPremisesDto.getOffSitePostalCode();
                        if (!StringUtil.isEmpty(offSitePostalCode)) {
                            if(offSitePostalCode.length() > 6){
                                String general_err0041=NewApplicationHelper.repLength("Postal Code","6");
                                errorMap.put("offSitePostalCode" + i, general_err0041);
                            }else if (offSitePostalCode.length() < 6) {
                                errorMap.put("offSitePostalCode" + i, "NEW_ERR0004");
                            } else if (!offSitePostalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("offSitePostalCode" + i, "NEW_ERR0004");
                            } else {
                                if (!floorUnitNo.isEmpty()) {
                                    stringBuilder.append(offSitePostalCode);
                                    if(list.isEmpty()){
                                        for(String str:floorUnitNo){
                                            StringBuilder sb=new StringBuilder(stringBuilder);
                                            sb.append(str);
                                            list.add(sb.toString());
                                        }
                                    }else {
                                        List<String> sbList=new ArrayList<>();
                                        for(String str:floorUnitNo){
                                            StringBuilder sb=new StringBuilder(stringBuilder);
                                            sb.append(str);
                                            if(list.contains(sb.toString())){
                                                errorMap.put("offSitePostalCode" + i, "NEW_ACK010");
                                            }else {
                                                sbList.add(sb.toString());
                                            }
                                        }
                                        list.addAll(sbList);
                                    }

                                 /*   if (list.contains(stringBuilder.toString())) {
                                        errorMap.put("postalCode" + i, "NEW_ACK010");

                                    } else {
                                        list.add(stringBuilder.toString());
                                    }*/
                                }
                            }
                        } else {
                            errorMap.put("offSitePostalCode" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code ", "field"));
                        }
                        String postalCodeErr = errorMap.get("offSitePostalCode" + i);
                        String blkNoErr = errorMap.get("offSiteBlockNo" + i);
                        String floorNoErr = errorMap.get("offSiteFloorNo" + i);
                        String unitNoErr = errorMap.get("offSiteUnitNo" + i);
                        String hciNAmeErr = errorMap.get("offSiteHciName" + i);
                        hciFlag = StringUtil.isEmpty(hciNAmeErr) && StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:" + hciFlag));
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
                    }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premiseType)){
                        validateEasmts.doValidatePremises(errorMap, appGrpPremisesDto, i, keywords, floorUnitList, floorUnitNo,
                                licenseeId, appType, licenceId);
                        validateEasmts.doValidatePremises(errorMap,appSubmissionDto.getAppType(),
                                i,licenseeId,appGrpPremisesDto,needAppendMsg,rfi);
                    }
                } else {
                    //premiseSelect = organization hci code

                    if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        String conveyanceVehicleNo = appGrpPremisesDto.getConveyanceVehicleNo();
                        validateVehicleNo(errorMap, distinctVehicleNo, i, conveyanceVehicleNo);
                    }

                }
            }
            //0062204
            boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
            if (newTypeFlag && hciFlag) {
                boolean clickEdit = appGrpPremisesDto.isClickEdit();
                List<String> currentHcis = NewApplicationHelper.genPremisesHciList(appGrpPremisesDto);
                if (!rfi) {
                    //new
                    if (!IaisCommonUtils.isEmpty(premisesHciList)) {
                        checkHciIsSame(currentHcis, premisesHciList, errorMap, "premisesHci" + i);
                    }
                } else if (rfi && clickEdit) {
                    boolean isChange = false;
                    boolean appTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                    if ((appTypeFlag && rfi) && (oldAppSubmissionDto != null)) {
                        AppGrpPremisesDto oldAppGrpPremisesDto = null;
                        if (i >= oldAppSubmissionDto.getAppGrpPremisesDtoList().size()) {
                            oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0);
                        } else {
                            oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(i);
                        }
                        isChange = !Objects.equals(oldAppGrpPremisesDto.getHciName(), appGrpPremisesDto.getHciName())
                                || !Objects.equals(oldAppGrpPremisesDto.getAddressWithoutFU(), appGrpPremisesDto.getAddressWithoutFU())
                                || !EqRequestForChangeSubmitResultChange.isFloorUnitAllIn(appGrpPremisesDto, oldAppGrpPremisesDto);
                    }
                    if (!IaisCommonUtils.isEmpty(premisesHciList) && isChange) {
                        checkHciIsSame(currentHcis, premisesHciList, errorMap, "premisesHci" + i);
                    }
                }
            }
            if (checkOthers) {
                String premisesSelect = NewApplicationHelper.getPremisesKey(appGrpPremisesDto);
                if (appGrpPremisesDtoList.stream().anyMatch(dto -> !Objects.equals(appGrpPremisesDto.getPremisesIndexNo(),
                        dto.getPremisesIndexNo()) && Objects.equals(premisesSelect, NewApplicationHelper.getPremisesKey(dto)))) {
                    errorMap.put("premisesHci" + i, "NEW_ERR0012");
                } else {
                    HttpServletRequest request = MiscUtil.getCurrentRequest();
                    AppGrpPremisesDto premises = null;
                    if (request != null) {
                        premises = NewApplicationHelper.getPremisesFromMap(premisesSelect, request);
                    }
                    if (premises != null && ("newPremise".equals(appGrpPremisesDto.getPremisesSelect())
                            || !Objects.equals(appGrpPremisesDto.getPremisesSelect(), premisesSelect))) {
                        errorMap.put("premisesHci" + i,
                                MessageUtil.replaceMessage("GENERAL_ERR0050", ApplicationConsts.TITLE_MODE_OF_SVCDLVY, "field"));
                        appGrpPremisesDto.setPremisesSelect("newPremise");
                        NewApplicationHelper.setAppSubmissionDto(appSubmissionDto, request);
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
        NewApplicationHelper.validatePH(errorMap,appSubmissionDto);
        WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
        return errorMap;
    }

    private void checkHciName(String key, String hciName, String appType, String licenceId, Map<String, String> errorMap) {
        int hciNameChanged = 0;
        if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
            hciNameChanged = NewApplicationHelper.checkNameChanged(hciName, null, licenceId);
        }
        if (2 == hciNameChanged || 4 == hciNameChanged) {
            //no need validate hci name have keyword (is migrated and hci name never changed)
        } else {
            Map<Integer, String> map = NewApplicationHelper.checkBlacklist(hciName);
            if (!map.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                AtomicInteger length = new AtomicInteger();
                map.forEach((k, v) -> {
                    length.getAndIncrement();
                    sb.append(v);
                    if (map.size() != length.get()) {
                        sb.append(',').append(' ');
                    }
                });
                errorMap.put(key, MessageUtil.replaceMessage("GENERAL_ERR0016", sb.toString(), "keywords"));
            }
        }
    }

    /*
    * if svc doc have primary doc ,change doc -> primary doc (only rfc ,renew now)
    * */
    @Override
    public void svcDocToPresmise(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            return;
        }
        String premisesIndexNo = Optional.of(appSubmissionDto.getAppGrpPremisesDtoList())
                .filter(list -> list.size() > 0)
                .map(list -> list.get(0))
                .map(AppGrpPremisesDto::getPremisesIndexNo)
                .orElse("");
        String premisesType = Optional.of(appSubmissionDto.getAppGrpPremisesDtoList())
                .filter(list -> list.size() > 0)
                .map(list -> list.get(0))
                .map(AppGrpPremisesDto::getPremisesType)
                .orElse("");
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
                    deleteSvcDoc.add(appSvcDocDto);
                    continue;
                }
                if(StringUtil.isEmpty(fileRepoId)){
                    deleteSvcDoc.add(appSvcDocDto);
                    continue;
                }
                HcsaSvcDocConfigDto entity = appConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if (entity != null) {
                    String serviceId = entity.getServiceId();
                    if (StringUtil.isEmpty(serviceId)) {// the current is primary document
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
                        appGrpPrimaryDocDto.setSeqNum(appSvcDocDto.getSeqNum());
                        if ("1".equals(entity.getDupForPrem())) {
                            appGrpPrimaryDocDto.setPremisessName(premisesIndexNo);
                            appGrpPrimaryDocDto.setPremisessType(premisesType);
                        }
                        appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                        appSvcDocDtos.add(appSvcDocDto);
                    }else {
                        appSvcDocDto.setUpFileName(entity.getDocTitle());
                    }
                }
            }
            appSvcDocDtoLit.removeAll(deleteSvcDoc);
            // remove the primary documents
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
        // check seq num and remove duplicate the documents
        int maxSeqNum = NewApplicationHelper.getMaxFileIndex(appSubmissionDto.getMaxFileIndex() + 1);
        for (int i = 0; i < appGrpPrimaryDocDtos.size(); i++) {
            if (appGrpPrimaryDocDtos.get(i).getSeqNum() == null) {
                appGrpPrimaryDocDtos.get(i).setSeqNum(maxSeqNum++);
            }
            for (int j = 0; j < appGrpPrimaryDocDtos.size() && j != i; j++) {
                if (appGrpPrimaryDocDtos.get(i).getFileRepoId().equals(appGrpPrimaryDocDtos.get(j).getFileRepoId())) {
                    appGrpPrimaryDocDtos.remove(appGrpPrimaryDocDtos.get(i));
                    i--;
                    break;
                }
            }
        }
        NewApplicationHelper.reSetMaxFileIndex(maxSeqNum);
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
                    removeList.add(appSvcDocDto);
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
        this.setRelatedInfoBaseServiceId(appSubmissionDtoByLicenceId);
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

    /*--------------------------------
    * appSubmissionDto application type must be determine
    * -------------------------------
    * version 1 doc id is A
    * ------------------------
    * update version 2 doc id is B
    * ------------------
    * licence XXX(version 1) change doc id A ->B
    * */
    @Override
    public void changeDocToNewVersion(AppSubmissionDto appSubmissionDto) throws Exception{
        log.debug(StringUtil.changeForLog("do changeDocToNewVersion start ..."));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
            boolean isRfi = false;
            List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
            //rfc/renew for primary doc
            List<AppGrpPrimaryDocDto> newGrpPrimaryDocList = appSubmissionService.syncPrimaryDoc(appSubmissionDto.getAppType(),isRfi,appGrpPrimaryDocDtos,primaryDocConfig);
            log.debug(StringUtil.changeForLog("newGrpPrimaryDocList size: "+newGrpPrimaryDocList.size()));
            appSubmissionDto.setAppGrpPrimaryDocDtos(newGrpPrimaryDocList);
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String currentSvcId = appSvcRelatedInfoDto.getServiceId();
                if(!StringUtil.isEmpty(currentSvcId)){
                    List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                    List<String> svcDocConfigIdList = IaisCommonUtils.genNewArrayList();
                    if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
                        for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                            svcDocConfigIdList.add(appSvcDocDto.getSvcDocId());
                        }
                    }
                    List<HcsaSvcDocConfigDto>  oldSvcDocConfigDtos = serviceConfigService.getPrimaryDocConfigByIds(svcDocConfigIdList);
                    List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
                    appSvcDocDtos =updateSvcDoc(appSvcDocDtos,oldSvcDocConfigDtos,svcDocConfig);
                    appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                }
            }
        }
        log.debug(StringUtil.changeForLog("do changeDocToNewVersion end ..."));
    }

    @Override
    public void svcDocToPrimaryForGiroDeduction(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            return;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            List<AppGrpPrimaryDocDto> newPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
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
                            deleteSvcDoc.add(appSvcDocDto);
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
                                AppGrpPrimaryDocDto appGrpPrimaryDocDto = svcTransferPrimaryDoc(appSvcDocDto);
                                appGrpPrimaryDocDto.setSvcDocId(svcDocId);
                                appGrpPrimaryDocDto.setSvcComDocId(svcDocId);
                                appGrpPrimaryDocDto.setSvcComDocName(entity.getDocTitle());
                                appGrpPrimaryDocDto.setAppGrpId(appSubmissionDto.getAppGrpId());
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
                newPrimaryDocDtoList.addAll(appGrpPrimaryDocDtos);
            }
            appSubmissionDto.setAppGrpPrimaryDocDtos(newPrimaryDocDtoList);
        }

    }

    @Override
    public void setRelatedInfoBaseServiceId(AppSubmissionDto appSubmissionDto) {
        if(appSubmissionDto==null){
            return;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(appSvcRelatedInfoDtoList==null || appSvcRelatedInfoDtoList.isEmpty()){
            return;
        }
        for (AppSvcRelatedInfoDto var1 : appSvcRelatedInfoDtoList) {
            if(var1.getBaseServiceId()!=null&&var1.getServiceId()!=null){
                continue;
            }
            String serviceName = var1.getServiceName();//cannot null
            HcsaServiceDto activeHcsaServiceDtoByName = serviceConfigService.getActiveHcsaServiceDtoByName(serviceName);
            String svcType = activeHcsaServiceDtoByName.getSvcType();
            String id = activeHcsaServiceDtoByName.getId();
            String baseService="";
            if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)){
                var1.setBaseServiceId(activeHcsaServiceDtoByName.getId());
            }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(svcType)){
                String licenceId = appSubmissionDto.getLicenceId();
                List<HcsaServiceCorrelationDto> serviceCorrelationDtos = appConfigClient.getActiveSvcCorrelation().getEntity();
                if(serviceCorrelationDtos==null || serviceCorrelationDtos.isEmpty()){
                    continue;
                }
                Iterator<HcsaServiceCorrelationDto> iterator = serviceCorrelationDtos.iterator();
                while (iterator.hasNext()){
                    HcsaServiceCorrelationDto next = iterator.next();
                    String specifiedSvcId = next.getSpecifiedSvcId();
                    if(id.equals(specifiedSvcId)){
                        baseService=next.getBaseSvcId();
                        break;
                    }
                }
                if(!StringUtil.isEmpty(baseService)){
                    String service_name = appConfigClient.getServiceNameById(baseService).getEntity();
                    List<LicBaseSpecifiedCorrelationDto> entity = licenceClient.getLicBaseSpecifiedCorrelationDtos(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED, licenceId).getEntity();
                    if(entity!=null && !entity.isEmpty()){
                        Iterator<LicBaseSpecifiedCorrelationDto> iterator1 = entity.iterator();
                        while (iterator1.hasNext()){
                            LicBaseSpecifiedCorrelationDto next = iterator1.next();
                            String baseLicId = next.getBaseLicId();
                            LicenceDto licenceDto = licenceClient.getLicBylicId(baseLicId).getEntity();
                            if(licenceDto.getSvcName().equals(service_name)){
                                var1.setBaseServiceId(baseService);
                                break;
                            }
                        }
                    }
                }

            }
            var1.setServiceId(activeHcsaServiceDtoByName.getId());

        }
    }

    @Override
    public String baseSpecLicenceRelation(LicenceDto licenceDto,boolean flag) {
        String svcName = licenceDto.getSvcName();
        HcsaServiceDto activeHcsaServiceDtoByName = serviceConfigService.getActiveHcsaServiceDtoByName(svcName);
        if(activeHcsaServiceDtoByName!=null){
            String svcType = activeHcsaServiceDtoByName.getSvcType();
            if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(svcType)){
                return flag==true ? String.valueOf(true): activeHcsaServiceDtoByName.getId();
            }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(svcType)){
                List<HcsaServiceCorrelationDto> serviceCorrelationDtos = appConfigClient.getActiveSvcCorrelation().getEntity();
                if(serviceCorrelationDtos==null ||serviceCorrelationDtos.isEmpty()){
                    return flag==true ? String.valueOf(false): "";
                }
                String baseService="";
                Iterator<HcsaServiceCorrelationDto> iterator = serviceCorrelationDtos.iterator();
                while (iterator.hasNext()){
                    HcsaServiceCorrelationDto next = iterator.next();
                    if(next.getSpecifiedSvcId().equals(activeHcsaServiceDtoByName.getId())){
                        baseService=next.getBaseSvcId();
                        break;
                    }
                }
                if(StringUtil.isEmpty(baseService)){
                    return flag==true ?String.valueOf(false):"";
                }

                List<LicBaseSpecifiedCorrelationDto> entity = licenceClient.getLicBaseSpecifiedCorrelationDtos(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED, licenceDto.getId()).getEntity();
                if(entity==null||entity.isEmpty()){
                    return flag==true ? String.valueOf(false): "";
                }
                Iterator<LicBaseSpecifiedCorrelationDto> iterator1 = entity.iterator();
                while (iterator1.hasNext()){
                    LicBaseSpecifiedCorrelationDto next = iterator1.next();
                    if(next.getSpecLicId().equals(licenceDto.getId())){
                        String baseLicId = next.getBaseLicId();
                        LicenceDto licenceDto1 = licenceClient.getLicBylicId(baseLicId).getEntity();
                        if(licenceDto1==null){
                            return flag==true ? String.valueOf(false): "";
                        }
                        String svcName1 = licenceDto1.getSvcName();
                        String svc_name = appConfigClient.getServiceNameById(baseService).getEntity();
                        if(svcName1.equals(svc_name)){
                            return flag==true ?String.valueOf(true):baseService;
                        }
                    }
                }

            }
        }
        return flag==true ? String.valueOf(false): "";
    }

    @Override
    public boolean baseSpecLicenceRelation(LicenceDto licenceDto) {
        return Boolean.parseBoolean(baseSpecLicenceRelation(licenceDto, true));
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
        if (appSubmissionDtos == null || appSubmissionDtos.isEmpty()) {
            log.info("No submissions to send email.");
        }
        appSubmissionDtos.stream()
                .collect(Collectors.groupingBy(AppSubmissionDto::getAppGrpNo))
                .forEach((groupNo, appSubmissionDtoList) -> {
                    log.info(StringUtil.changeForLog("The Group No for Email: " + groupNo));
                    String method = appSubmissionDtoList.get(0).isAutoRfc() ? null : pmtMethod;
                    try {
                        sendRfcEmail(appSubmissionDtoList, method);
                    } catch (Exception e) {
                        log.error(StringUtil.changeForLog(groupNo + " : " + e.getMessage()), e);
                    }
                });
    }

    private void sendRfcEmail(List<AppSubmissionDto> appSubmissionDtos, String pmtMethod) throws IOException,
            TemplateException {
        AppSubmissionDto appSubmissionDto=appSubmissionDtos.get(0);
        String appGroupId = appSubmissionDto.getAppGrpId();
        ApplicationGroupDto applicationGroupDto=applicationFeClient.getApplicationGroup(appGroupId).getEntity();
        if(applicationGroupDto!=null&&!StringUtil.isEmpty(applicationGroupDto.getNewLicenseeId())){
            sendRfcLicenseeSubmittedEmail(appSubmissionDtos,pmtMethod);
        }else {
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
                    emailMap.put("GIRO_account_number", serviceConfigService.getGiroAccountByGroupNo(appSubmissionDto.getAppGrpNo()));
                    emailMap.put("usual_text_for_GIRO_deduction",StringUtil.isEmpty(appSubmissionDto.getLateFeeStr()) ? "next 7 working days" : appSubmissionDto.getLateFeeStr());
                } else {
                    emailMap.put("Online_PAY", "true");
                }
            }
            if(MiscUtil.doubleEquals(0.0, a)){
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
                List<String> svcCode = IaisCommonUtils.genNewArrayList();
                for (AppSubmissionDto appSubmissionDto1 : appSubmissionDtos) {
                    String svcName = appSubmissionDto1.getServiceName();
                    if (appSubmissionDto1.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)) {
                        if (svcName == null) {
                            LicenceDto licenceDto = licenceClient.getLicBylicId(appSubmissionDto1.getLicenceId()).getEntity();
                            svcName = licenceDto != null ? licenceDto.getSvcName() : null;
                        }
                        List<HcsaServiceDto> svcDto = appConfigClient.getHcsaServiceByNames(Collections.singletonList(svcName)).getEntity();
                        if (svcDto != null && !svcDto.isEmpty()) {
                            svcCode.add(svcDto.get(0).getSvcCode());
                        }
                    }
                }
                log.info(StringUtil.changeForLog("App Grp No: " + appSubmissionDto.getAppGrpNo() +
                        " - AppSubmission size: " + appSubmissionDtos.size() + " - Service Code: " + svcCode));
                rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_MSG).getEntity();
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
                EmailParam msgParam = new EmailParam();
                if(appSubmissionDto.getAppGrpId()==null){
                    msgParam.setQueryCode(appSubmissionDto.getLicenceNo());
                    msgParam.setReqRefNum(appSubmissionDto.getLicenceNo());
                    msgParam.setRefId(appSubmissionDto.getLicenceId());
                }else {
                    msgParam.setQueryCode(appSubmissionDto.getAppGrpNo());
                    msgParam.setReqRefNum(appSubmissionDto.getAppGrpNo());
                    msgParam.setRefId(appSubmissionDto.getAppGrpId());
                }
                msgParam.setTemplateContent(emailMap);
                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_MSG);
                msgParam.setSubject(subject);
                msgParam.setSvcCodeList(svcCode);
                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                msgParam.setRefId(appSubmissionDto.getLicenceId());
                notificationHelper.sendNotification(msgParam);
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
            //sms
            rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_SMS).getEntity();
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            EmailParam smsParam = new EmailParam();
            smsParam.setQueryCode(appSubmissionDto.getAppGrpNo());
            smsParam.setReqRefNum(appSubmissionDto.getAppGrpNo());
            if (applicationGroupDto != null) {
                smsParam.setRefId(applicationGroupDto.getLicenseeId());
            }
            smsParam.setTemplateContent(emailMap);
            smsParam.setSubject(subject);
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_001_SUBMIT_SMS);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENSEE_ID);
            notificationHelper.sendNotification(smsParam);
        }
    }

    private void sendRfcLicenseeSubmittedEmail(List<AppSubmissionDto> appSubmissionDtos, String pmtMethod) throws IOException, TemplateException {
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
            LicenseeDto licenseeDto=organizationLienceseeClient.getLicenseeDtoById(applicationGroupDto.getLicenseeId()).getEntity();
            LicenseeDto newLicenseeDto=organizationLienceseeClient.getLicenseeDtoById(applicationGroupDto.getNewLicenseeId()).getEntity();
            emailMap.put("ExistingLicensee", licenseeDto.getName());
            emailMap.put("TransfereeLicensee", newLicenseeDto.getName());

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
                emailMap.put("GIRO_account_number", serviceConfigService.getGiroAccountByGroupNo(appSubmissionDto.getAppGrpNo()));
                emailMap.put("usual_text_for_GIRO_deduction",StringUtil.isEmpty(appSubmissionDto.getLateFeeStr()) ? "next 7 working days" : appSubmissionDto.getLateFeeStr());
            } else {
                emailMap.put("Online_PAY", "true");
            }
        }
        if(MiscUtil.doubleEquals(0.0, a)){
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
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_009_LICENSEE_SUBMIT);
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
        MsgTemplateDto rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_009_LICENSEE_SUBMIT).getEntity();
        map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{appSubmissionDto.getAppType()}).get(0).getText());
        map.put("ApplicationNumber", appSubmissionDto.getAppGrpNo());
        String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        emailParam.setSubject(subject);
        //email
        notificationHelper.sendNotification(emailParam);
        //msg
        try {

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
            rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_009_LICENSEE_SUBMIT_MSG).getEntity();
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            EmailParam msgParam = new EmailParam();
            if(appSubmissionDto.getAppGrpId()==null){
                msgParam.setQueryCode(appSubmissionDto.getLicenceNo());
                msgParam.setReqRefNum(appSubmissionDto.getLicenceNo());
                msgParam.setRefId(appSubmissionDto.getLicenceId());
            }else {
                msgParam.setQueryCode(appSubmissionDto.getAppGrpNo());
                msgParam.setReqRefNum(appSubmissionDto.getAppGrpNo());
                msgParam.setRefId(appSubmissionDto.getAppGrpId());
            }
            msgParam.setTemplateContent(emailMap);
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_009_LICENSEE_SUBMIT_MSG);
            msgParam.setSubject(subject);
            msgParam.setSvcCodeList(svcCode);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            msgParam.setRefId(appSubmissionDto.getLicenceId());
            notificationHelper.sendNotification(msgParam);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        //sms
        rfiEmailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_009_LICENSEE_SUBMIT_SMS).getEntity();
        subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        EmailParam smsParam = new EmailParam();
        smsParam.setQueryCode(appSubmissionDto.getAppGrpNo());
        smsParam.setReqRefNum(appSubmissionDto.getAppGrpNo());
        if (applicationGroupDto != null) {
            smsParam.setRefId(applicationGroupDto.getLicenseeId());
        }
        smsParam.setTemplateContent(emailMap);
        smsParam.setSubject(subject);
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_009_LICENSEE_SUBMIT_SMS);
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENSEE_ID);
        notificationHelper.sendNotification(smsParam);
    }

    @Override
    public  List<AppSvcDocDto> updateSvcDoc(List<AppSvcDocDto> appSvcDocDtos,List<HcsaSvcDocConfigDto> oldSvcDocConfigDtos,List<HcsaSvcDocConfigDto> svcDocConfigDtos) throws Exception {
        //todo: old psn doc use psn type 1 config change to psn type 2, doc will be missing
        List<AppSvcDocDto> newAppSvcDocDtoList =  IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appSvcDocDtos) && !IaisCommonUtils.isEmpty(oldSvcDocConfigDtos) && !IaisCommonUtils.isEmpty(svcDocConfigDtos)){
            for(AppSvcDocDto appSvcDocDto : appSvcDocDtos){
                for(HcsaSvcDocConfigDto oldSvcDocConfigDto:oldSvcDocConfigDtos){
                    if(appSvcDocDto.getSvcDocId()!=null && oldSvcDocConfigDto.getId()!=null){
                        if(appSvcDocDto.getSvcDocId().equals(oldSvcDocConfigDto.getId())){
                            String titleName = oldSvcDocConfigDto.getDocTitle();
                            if(!StringUtil.isEmpty(titleName)){
                                for(HcsaSvcDocConfigDto svcDocConfigDto:svcDocConfigDtos){
                                    if(titleName.equals(svcDocConfigDto.getDocTitle())){
                                        AppSvcDocDto newAppSvcDocDto = (AppSvcDocDto) CopyUtil.copyMutableObject(appSvcDocDto);
                                        newAppSvcDocDto.setSvcDocId(svcDocConfigDto.getId());
                                        newAppSvcDocDto.setDupForPerson(svcDocConfigDto.getDupForPerson());
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

    private void checkHciIsSame(List<String> currentHcis, List<String> premisesHciList, Map<String, String> errorMap, String errName) {
        for (String hci : currentHcis) {
            if (premisesHciList.contains(hci)) {
                errorMap.put(errName, "NEW_ERR0005");
            }
        }
    }

    private void checkOperaionUnit(List<AppPremisesOperationalUnitDto> operationalUnitDtos,Map<String, String> errorMap,String floorErrName,String unitErrName,List<String> floorUnitList,String floorUnitErrName,List<String> floorUnitNo,AppGrpPremisesDto appGrpPremisesDto){

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
                operationalUnitDto.setFloorNo(NewApplicationHelper.handleFloorNo(floorNo, floorNoErr));
                if(flag){
                    if(!StringUtil.isEmpty(operationalUnitDto.getFloorNo()) && !StringUtil.isEmpty(operationalUnitDto.getUnitNo())){
                        String floorUnitStr = operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo();
                        if(floorUnitList.contains(floorUnitStr)){
                            errorMap.put(floorUnitErrName + opLength, "NEW_ERR0017");
                        }else{
                            floorUnitList.add(floorUnitStr);
                        }
                        String premisesType = appGrpPremisesDto.getPremisesType();
                        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)){
                            String addrType = appGrpPremisesDto.getAddrType();
                            String blkNo = appGrpPremisesDto.getBlkNo();
                            if(!StringUtil.isEmpty(blkNo)){
                                floorUnitNo.add(operationalUnitDto.getFloorNo()+blkNo+operationalUnitDto.getUnitNo());
                            }
                        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)){
                            String offSiteAddressType = appGrpPremisesDto.getOffSiteAddressType();
                            if(!StringUtil.isEmpty(offSiteAddressType)){
                                String blkNo = appGrpPremisesDto.getOffSiteBlockNo();
                                if(!StringUtil.isEmpty(blkNo)){
                                    floorUnitNo.add(operationalUnitDto.getFloorNo()+blkNo+operationalUnitDto.getUnitNo());
                                }
                            }

                        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)){
                            String conveyanceAddressType = appGrpPremisesDto.getConveyanceAddressType();
                            if(!StringUtil.isEmpty(conveyanceAddressType)){
                                String blkNo = appGrpPremisesDto.getConveyanceBlockNo();
                                if(!StringUtil.isEmpty(blkNo)){
                                    floorUnitNo.add(operationalUnitDto.getFloorNo()+blkNo+operationalUnitDto.getUnitNo());
                                }
                            }

                        }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType)){
                            String easMtsAddressType = appGrpPremisesDto.getEasMtsAddressType();
                            if(!StringUtil.isEmpty(easMtsAddressType)){
                                String blkNo = appGrpPremisesDto.getEasMtsBlockNo();
                                if(!StringUtil.isEmpty(blkNo)){
                                    floorUnitNo.add(operationalUnitDto.getFloorNo()+blkNo+operationalUnitDto.getUnitNo());
                                }
                            }
                        }

                    }
                }
                opLength++;
            }
        }

    }

    private static void doOperationHoursValidate(OperationHoursReloadDto operationHoursReloadDto,Map<String,String>errorMap,Map<String,String> errNameMap,String count,boolean isMandatory){
        boolean isEmpty = false;
        String emptyErrMsg = MessageUtil.getMessageDesc("GENERAL_ERR0006");
        boolean selectAllDay = operationHoursReloadDto.isSelectAllDay();
        String selectVal = operationHoursReloadDto.getSelectVal();
        String startHH = operationHoursReloadDto.getStartFromHH();
        String startMM = operationHoursReloadDto.getStartFromMM();
        String endHH = operationHoursReloadDto.getEndToHH();
        String endMM = operationHoursReloadDto.getEndToMM();
        if(!isMandatory){
            if(StringUtil.isEmpty(selectVal) &&
                    StringUtil.isEmpty(startHH) &&
                    StringUtil.isEmpty(startMM) &&
                    StringUtil.isEmpty(endHH) &&
                    StringUtil.isEmpty(endMM)){
                return;
            }
        }


        if(StringUtil.isEmpty(selectVal)){
            errorMap.put(errNameMap.get("select") + count,emptyErrMsg);
        }
        if(selectAllDay){
            if(!isEmpty){
                Time time = Time.valueOf(LocalTime.of(0,0,0));
                operationHoursReloadDto.setStartFrom(time);
                operationHoursReloadDto.setEndTo(time);
            }
        }else{
            if(StringUtil.isEmpty(startHH) || StringUtil.isEmpty(startMM)){
                errorMap.put(errNameMap.get("start") + count,emptyErrMsg);
                isEmpty = true;
            }
            if(StringUtil.isEmpty(endHH) || StringUtil.isEmpty(endMM)){
                errorMap.put(errNameMap.get("end") + count,emptyErrMsg);
                isEmpty = true;
            }

            if(!isEmpty){
                LocalTime startTime = LocalTime.of(Integer.parseInt(startHH), Integer.parseInt(startMM));
                operationHoursReloadDto.setStartFrom(Time.valueOf(startTime));
                LocalTime endTime = LocalTime.of(Integer.parseInt(endHH), Integer.parseInt(endMM));
                operationHoursReloadDto.setEndTo(Time.valueOf(endTime));
                //compare
                if(startTime.isAfter(endTime)){
                    errorMap.put(errNameMap.get("time") + count,MessageUtil.getMessageDesc("NEW_ERR0015"));
                }else if(startTime.equals(endTime)){
                    errorMap.put(errNameMap.get("time") + count,MessageUtil.getMessageDesc("NEW_ERR0019"));
                }

            }
        }

    }

    private static AppGrpPrimaryDocDto svcTransferPrimaryDoc(AppSvcDocDto appSvcDocDto){
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
        appGrpPrimaryDocDto.setDocName(appSvcDocDto.getDocName());
        appGrpPrimaryDocDto.setDocSize(appSvcDocDto.getDocSize());
        appGrpPrimaryDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
        appGrpPrimaryDocDto.setPassValidate(appSvcDocDto.isPassValidate());
        appGrpPrimaryDocDto.setMd5Code(appSvcDocDto.getMd5Code());
        appGrpPrimaryDocDto.setVersion(appSvcDocDto.getVersion());
        appGrpPrimaryDocDto.setSeqNum(appSvcDocDto.getSeqNum());
        return appGrpPrimaryDocDto;
    }

    @Override
    public LicenceDto getLicenceDtoIncludeMigrated(String licenceId) {
        return licenceClient.getLicBylicIdIncludeMigrated(licenceId).getEntity();
    }

    @Override
    public boolean checkAffectedAppSubmissions(List<LicenceDto> selectLicence, AppGrpPremisesDto appGrpPremisesDto,
            AppGrpPremisesDto oldAppGrpPremisesDto, double amount,  String draftNo, String appGroupNo,
            AppEditSelectDto appEditSelectDto, List<AppSubmissionDto> appSubmissionDtos, HttpServletRequest request) throws Exception {
        if (selectLicence == null) {
            return true;
        }
        for (LicenceDto licence : selectLicence) {
            AppSubmissionDto appSubmissionDtoByLicenceId = getAppSubmissionDtoByLicenceId(licence.getId());
            // Premises
            boolean groupLic = appSubmissionDtoByLicenceId.isGroupLic();
            if (oldAppGrpPremisesDto == null) {
                oldAppGrpPremisesDto = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0);
            }
            String premisesIndexNo;
            if (groupLic) {
                premisesIndexNo = oldAppGrpPremisesDto.getPremisesIndexNo();
            } else {
                premisesIndexNo = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getPremisesIndexNo();
            }
            List<AppGrpPremisesDto> appGrpPremisesDtos = new ArrayList<>(1);
            AppGrpPremisesDto copyMutableObject = (AppGrpPremisesDto) CopyUtil.copyMutableObject(appGrpPremisesDto);
            appGrpPremisesDtos.add(copyMutableObject);
            if (groupLic) {
                appGrpPremisesDtos.get(0).setGroupLicenceFlag(licence.getId());
            }
            appSubmissionDtoByLicenceId.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).setPremisesIndexNo(premisesIndexNo);
            // check app edit select dto
            if (StringUtil.isEmpty(draftNo)) {
                appSubmissionService.setDraftNo(appSubmissionDtoByLicenceId);
                draftNo = appSubmissionDtoByLicenceId.getDraftNo();
            }
            boolean isValid = checkAffectedAppSubmissions(appSubmissionDtoByLicenceId, licence, amount, draftNo, appGroupNo,
                    appEditSelectDto, appSubmissionDtos, request);
            if (!isValid) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
                ParamUtil.setRequestAttr(request, "isrfiSuccess", "N");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkAffectedAppSubmissions(AppSubmissionDto appSubmissionDto,LicenceDto licence, Double amount,
            String draftNo, String appGroupNo, AppEditSelectDto appEditSelectDto,List<AppSubmissionDto> appSubmissionDtos,
            HttpServletRequest request) {
        if (appSubmissionDto == null) {
            return true;
        }
        if (licence == null) {
            licence = licenceClient.getLicBylicId(appSubmissionDto.getLicenceId()).getEntity();
        }
        if (licence == null) {
            log.info("Invalid Licence");
            if (request != null) {
                request.setAttribute("rfcInvalidLic", MessageUtil.getMessageDesc("RFC_ERR024"));
            }
            return false;
        }
        // set draft no
        appSubmissionDto.setDraftNo(draftNo);
        try {
            appSubmissionService.transform(appSubmissionDto, licence.getLicenseeId(), appGroupNo);
        } catch (Exception e) {
            log.warn(StringUtil.changeForLog(e.getMessage()), e);
        }
        if (request != null) {
            String errorSvcMsg = MessageUtil.getMessageDesc("RFC_ERR020").replace("{ServiceName}", licence.getSvcName());
            String baseServiceId1 = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getBaseServiceId();
            if (StringUtil.isEmpty(baseServiceId1)) {
                log.info(StringUtil.changeForLog("BaseService is null - " + errorSvcMsg));
                request.setAttribute("SERVICE_CONFIG_CHANGE", errorSvcMsg);
                return false;
            }
        }
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(
                appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
        double total = 0.0;
        if (amount != null) {
            total = amount.doubleValue();
        }
        if (licence.getStatus().equals(ApplicationConsts.LICENCE_STATUS_APPROVED) && licence.getMigrated() == 1
                && IaisEGPHelper.isActiveMigrated()) {
            total = 0.0;
        }
        appSubmissionDto.setAmount(total);
        // check app edit select dto
        if (appEditSelectDto == null) {
            appEditSelectDto = appSubmissionDto.getChangeSelectDto();
        }
        if (appEditSelectDto == null) {
            appEditSelectDto = new AppEditSelectDto();
        }
        AppEditSelectDto editDto = MiscUtil.transferEntityDto(appEditSelectDto, AppEditSelectDto.class);
        appSubmissionDto.setChangeSelectDto(editDto);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
        if (MiscUtil.doubleEquals(0.0, total)) {
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
        }
        appSubmissionDto.setGetAppInfoFromDto(true);
        RequestForChangeMenuDelegator.oldPremiseToNewPremise(appSubmissionDto);
        premisesDocToSvcDoc(appSubmissionDto);
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        // set app GrpPremisess
        boolean groupLic = appSubmissionDto.isGroupLic();
        int hciNameChange = appEditSelectDto.isChangeHciName() ? 1 : 0;
        appSubmissionDto.setGroupLic(groupLic);
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if (groupLic) {
            appGrpPremisesDtos.get(0).setGroupLicenceFlag(licence.getId());
            appSubmissionDto.setPartPremise(true);
        }
        appSubmissionDto.getAppGrpPremisesDtoList().get(0).setHciNameChanged(hciNameChange);
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
        NewApplicationHelper.reSetAdditionalFields(appSubmissionDto, appEditSelectDto);
        if (appSubmissionDtos != null) {
            appSubmissionDtos.add(appSubmissionDto);
        }
        return true;
    }

}
