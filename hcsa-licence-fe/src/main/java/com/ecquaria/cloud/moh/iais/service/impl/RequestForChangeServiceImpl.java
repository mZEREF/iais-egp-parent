package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    private SystemParamConfig systemParamConfig;
    @Autowired
    private SubmissionClient client;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

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
        return applicationClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
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
    public SearchResult<PersonnelListQueryDto> psnDoQuery(SearchParam searchParam) {
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
                    emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
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
                    emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
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
                    emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
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
                    emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
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
                    emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
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
                    emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
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
                    emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
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

}
