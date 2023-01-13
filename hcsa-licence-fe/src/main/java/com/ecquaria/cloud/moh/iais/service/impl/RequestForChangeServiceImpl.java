package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.ConfigCommClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private ConfigCommClient configCommClient;

    @Autowired
    private ServiceConfigService serviceConfigService;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private LicCommService licCommService;

    @Autowired
    private AppCommService appCommService;

    @Autowired
    private EventBusHelper eventBusHelper;

    @Override
    public List<PremisesListQueryDto> getPremisesList(String licenseeId) {
        return licenceClient.getPremises(licenseeId).getEntity();
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId) {
        return licCommService.getAppSubmissionDtoByLicenceId(licenceId);
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
        List<ApplicationDto> applicationDtos = appCommService.getAppByLicIdAndExcludeNew(licenceId);
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
            List<ApplicationDto> applicationDtos = appCommService.getAppByLicIdAndExcludeNew(licId);
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
        return licCommService.getLicencePersonnelListQueryDto(licenseeId);
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtosByLicenceIds(List<String> licenceIds) {
        return licCommService.getAppSubmissionDtosByLicenceIds(licenceIds);
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
                    feEicGatewayClient.callEicWithTrack(emailDto, feEicGatewayClient::sendEmail, "sendEmail");
                }
                break;
            case "RfcAndOnPay":
                MsgTemplateDto rfcAndOnPayMsgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(
                        "D9DDBC23-122B-47BA-B579-3B5022816BB6").getEntity();
                if (rfcAndOnPayMsgTemplateDto != null) {
                    Map<String, Object> tempMap = IaisCommonUtils.genNewHashMap();
                    tempMap.put("serviceName", StringUtil.viewHtml(serviceName));
                    tempMap.put("amount", amount);
                    String mesContext = MsgUtil.getTemplateMessageByContent(rfcAndOnPayMsgTemplateDto.getMessageContent(), tempMap);
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject("MOH IAIS – Successful Submission of Request for Change " + appNo);
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                    emailDto.setClientQueryCode(appGrpId);
                    //send
                    feEicGatewayClient.callEicWithTrack(emailDto, feEicGatewayClient::sendEmail, "sendEmail");
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
                    feEicGatewayClient.callEicWithTrack(emailDto, feEicGatewayClient::sendEmail, "sendEmail");
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
                    feEicGatewayClient.callEicWithTrack(emailDto, feEicGatewayClient::sendEmail, "sendEmail");
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
                    feEicGatewayClient.callEicWithTrack(emailDto, feEicGatewayClient::sendEmail, "sendEmail");
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
                    feEicGatewayClient.callEicWithTrack(emailDto, feEicGatewayClient::sendEmail, "sendEmail");
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
                    feEicGatewayClient.callEicWithTrack(emailDto, feEicGatewayClient::sendEmail, "sendEmail");
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
        log.info(StringUtil.changeForLog("Licence Id: " + licenceId));
        if (StringUtil.isEmpty(licenceId)) {
            return null;
        }
        return licenceClient.getLicBylicId(licenceId).getEntity();
    }

    @Override
    public void sendNotification(EmailDto email) {
        feEicGatewayClient.callEicWithTrack(email, feEicGatewayClient::sendEmail, "sendEmail");
    }

    @Override
    public List<PersonnelListDto> getPersonnelListDto(PersonnelTypeDto personnelTypeDto) {
        return licenceClient.getPersonnelListDto(personnelTypeDto).getEntity();
    }

    @Override
    public List<String> getAdminEmail(String orgId) {
        return organizationLienceseeClient.getAdminEmailAdd(orgId).getEntity();
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
    public String baseSpecLicenceRelation(LicenceDto licenceDto,boolean flag) {
        String svcName = licenceDto.getSvcName();
        HcsaServiceDto activeHcsaServiceDtoByName = serviceConfigService.getActiveHcsaServiceDtoByName(svcName);
        if(activeHcsaServiceDtoByName!=null){
            String svcType = activeHcsaServiceDtoByName.getSvcType();
            log.info(StringUtil.changeForLog("The Svc Type: " + svcType));
            if(HcsaConsts.SERVICE_TYPE_BASE.equals(svcType)){
                return flag==true ? String.valueOf(true): activeHcsaServiceDtoByName.getId();
            }else if(HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(svcType)){
                List<HcsaServiceCorrelationDto> serviceCorrelationDtos = configCommClient.getActiveSvcCorrelation().getEntity();
                if(serviceCorrelationDtos==null ||serviceCorrelationDtos.isEmpty()){
                    log.info(StringUtil.changeForLog("The service correlations is empty!"));
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
                    log.info(StringUtil.changeForLog("The base service is empty!"));
                    return flag==true ?String.valueOf(false):"";
                }
/*
                List<LicBaseSpecifiedCorrelationDto> entity = licCommService.getLicBaseSpecifiedCorrelationDtos(HcsaConsts.SERVICE_TYPE_SPECIFIED, licenceDto.getId());
                if(entity==null||entity.isEmpty()){
                    log.info(StringUtil.changeForLog("The related base service is empty!"));
                    return flag==true ? String.valueOf(false): "";
                }
                Iterator<LicBaseSpecifiedCorrelationDto> iterator1 = entity.iterator();
                while (iterator1.hasNext()) {
                    LicBaseSpecifiedCorrelationDto next = iterator1.next();
                    if (next.getSpecLicId().equals(licenceDto.getId())) {
                        String baseLicId = next.getBaseLicId();
                        LicenceDto licenceDto1 = getLicenceById(baseLicId);
                        if (licenceDto1 == null) {
                            log.info(StringUtil.changeForLog("The base Licence is empty!"));
                            return flag == true ? String.valueOf(false) : "";
                        }
                        String svcName1 = licenceDto1.getSvcName();
                        String svc_name = configCommClient.getServiceNameById(baseService).getEntity();
                        if (svcName1.equals(svc_name)) {
                            return flag == true ? String.valueOf(true) : baseService;
                        }
                    }
                }*/

            }
        }
        log.info("The baseSpecLicenceRelation end!");
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
    public void sendRfcSubmittedEmail(List<AppSubmissionDto> appSubmissionDtos, String pmtMethod) throws Exception {
        if (appSubmissionDtos == null || appSubmissionDtos.isEmpty()) {
            log.info("No submissions to send email.");
            return;
        }
        Exception ex = null;
        Map<String, List<AppSubmissionDto>> map = appSubmissionDtos.stream()
                .collect(Collectors.groupingBy(AppSubmissionDto::getAppGrpNo));
        for (Map.Entry<String, List<AppSubmissionDto>> entry : map.entrySet()) {
            String groupNo = entry.getKey();
            List<AppSubmissionDto> appSubmissionDtoList = entry.getValue();
            log.info(StringUtil.changeForLog("The Group No for Email: " + groupNo));
            String method = appSubmissionDtoList.get(0).isAutoRfc() ? null : pmtMethod;
            try {
                sendRfcEmail(appSubmissionDtoList, method);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog(groupNo + " : " + e.getMessage()), e);
                ex = e;
            }
        }
        if (ex != null) {
            throw ex;
        }
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
                        List<HcsaServiceDto> svcDto = configCommClient.getHcsaServiceByNames(Collections.singletonList(svcName)).getEntity();
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

        for (AppSubmissionDto appSubmission:appSubmissionDtos
             ) {
            String appGrpId = appSubmission.getAppGrpId();
            ApplicationGroupDto appGroupDto=applicationFeClient.getApplicationGroup(appGrpId).getEntity();
            if(!appGroupDto.isAutoApprove()){
                for (ApplicationDto application:appSubmission.getApplicationDtos()
                     ) {
                    sendRfc008Email(appGroupDto,application);
                }
            }
        }
    }

    private void sendRfc008Email(ApplicationGroupDto applicationGroupDto, ApplicationDto application) throws IOException, TemplateException {
        String serviceName = HcsaServiceCacheHelper.getServiceById(application.getServiceId()).getSvcName();
        LicenseeDto licenseeDto = organizationLienceseeClient.getLicenseeDtoById(applicationGroupDto.getLicenseeId()).getEntity();
        if(application.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE) ){
            LicenceDto licenceDto=licenceClient.getLicDtoById(application.getOriginLicenceId()).getEntity();
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            emailMap.put("officer_name", "");
            emailMap.put("ServiceLicenceName", serviceName);
            emailMap.put("ApplicationDate", Formatter.formatDate(applicationGroupDto.getSubmitDt()));
            emailMap.put("Licensee", licenseeDto.getName());
            if(licenceDto!=null&&licenceDto.getLicenceNo()!=null){
                emailMap.put("LicenceNumber", licenceDto.getLicenceNo());
            }else {
                emailMap.put("LicenceNumber", "");
            }
            StringBuilder stringBuilder = new StringBuilder();
            List<AppEditSelectDto> appEditSelectDtos = applicationFeClient.getAppEditSelectDtos(application.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFC).getEntity();
            if(appEditSelectDtos!=null&&appEditSelectDtos.size()!=0){
                if (appEditSelectDtos.get(0).isServiceEdit()){
                    stringBuilder.append("<p class=\"line\">   ").append("Remove subsumed service").append("</p>");
                }
            }
            if (applicationGroupDto.getNewLicenseeId()!=null){
                stringBuilder.append("<p class=\"line\">   ").append("Change in Management of Licensee").append("</p>");
            }
            emailMap.put("ServiceNames", stringBuilder);
            emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
            emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER);
            emailParam.setQueryCode(application.getApplicationNo());
            emailParam.setReqRefNum(application.getApplicationNo());
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(application.getApplicationNo());
            emailParam.setTemplateContent(emailMap);
            MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER).getEntity();
            Map<String, Object> map1 = IaisCommonUtils.genNewHashMap();
            map1.put("ApplicationType", MasterCodeUtil.getCodeDesc(application.getApplicationType()));
            map1.put("ApplicationNumber", application.getApplicationNo());
            String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map1);
            emailParam.setSubject(subject);
            log.info("start send email start");
            notificationHelper.sendNotification(emailParam);
            log.info("start send email end");


            //sms
            msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER_SMS).getEntity();
            subject = null;
            try {
                subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map1);
            } catch (IOException |TemplateException e) {
                log.info(e.getMessage(),e);
            }
            EmailParam smsParam = new EmailParam();
            smsParam.setQueryCode(application.getApplicationNo());
            smsParam.setReqRefNum(application.getApplicationNo());
            smsParam.setRefId(application.getApplicationNo());
            smsParam.setTemplateContent(emailMap);
            smsParam.setSubject(subject);
            emailMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(application.getApplicationType()));
            emailMap.put("ApplicationNumber", application.getApplicationNo());
            smsParam.setTemplateContent(emailMap);
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER_SMS);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            log.info("start send sms start");
            notificationHelper.sendNotification(smsParam);
            log.info("start send sms end");
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
                List<HcsaServiceDto> svcDto = configCommClient.getHcsaServiceByNames(Collections.singletonList(svcName)).getEntity();
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
    public LicenceDto getLicenceDtoIncludeMigrated(String licenceId) {
        return licenceClient.getLicBylicIdIncludeMigrated(licenceId).getEntity();
    }

    @Override
    public List<AppSubmissionDto> saveAppSubmissionList(List<AppSubmissionDto> appSubmissionDtoList, String eventRefNo,
            BaseProcessClass bpc) {
        if (IaisCommonUtils.isEmpty(appSubmissionDtoList)) {
            return appSubmissionDtoList;
        }
        // save application
        List<AppSubmissionDto> newAppSubmissionList = appSubmissionService.saveAppsForRequestForGoupAndAppChangeByList(
                appSubmissionDtoList);
        // save other data via event bus
        AppSubmissionListDto autoAppSubmissionListDto = new AppSubmissionListDto();
        autoAppSubmissionListDto.setAuditTrailDto(AuditTrailHelper.getCurrentAuditTrailDto());
        autoAppSubmissionListDto.setEventRefNo(eventRefNo);
        List<AppSubmissionDto> slimList = IaisCommonUtils.genNewArrayList(newAppSubmissionList.size());
        newAppSubmissionList.forEach(dto -> slimList.add(ApplicationHelper.toSlim(dto)));
        autoAppSubmissionListDto.setAppSubmissionDtos(slimList);
        eventBusHelper.submitAsyncRequest(autoAppSubmissionListDto, appCommService.getSeqId(), EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, eventRefNo, bpc.process);
        return newAppSubmissionList;
    }
}
