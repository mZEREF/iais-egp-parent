package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.GenerateUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.IaisUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.IssuanceAddresses;
import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.IssuanceBasic;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.GenerateLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGrpDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SuperLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.AcraUenBeClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * LicenceServiceImpl
 *
 * @author suocheng
 * @date 11/29/2019
 */
@Service
@Slf4j
public class LicenceServiceImpl implements LicenceService {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private SystemBeLicClient systemClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    OrganizationClient organizationClient;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private EventBusHelper eventBusHelper;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private AcraUenBeClient acraUenBeClient;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private LicEicClient licEicClient;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Value("${iais.system.two.address}")
    private String systemAddressTwo;

    @Value("${iais.system.phone.number}")
    private String systemPhoneNumber;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;
    @Autowired
    private SystemParamConfig systemParamConfig;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public List<ApplicationLicenceDto> getCanGenerateApplications(GenerateLicenceDto generateLicenceDto) {
        return   applicationClient.getGroup(generateLicenceDto).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getHcsaServiceById(List<String> serviceIds) {

        return  hcsaConfigClient.getHcsaService(serviceIds).getEntity();
    }

    @Override
    public String getHciCode(String serviceCode) {
        log.info(StringUtil.changeForLog("generate the hcicode"));
        return     systemClient.hclCodeByCode(serviceCode).getEntity();
    }

    @Override
    public String getLicenceNo(String hciCode, String serviceCode, AppPremisesRecommendationDto appPremisesRecommendationDto) {
        log.info(StringUtil.changeForLog("The getLicenceNo start ..."));
        Integer licenceSeq =  hcsaLicenceClient.licenceNumber(hciCode,serviceCode).getEntity();
        log.info(StringUtil.changeForLog("The getLicenceNo licenceSeq -->:"+licenceSeq));
        int yearLength = 0;
        if(appPremisesRecommendationDto != null && RiskConsts.YEAR.equals(appPremisesRecommendationDto.getChronoUnit())){
            yearLength = appPremisesRecommendationDto.getRecomInNumber();
        }
        log.info(StringUtil.changeForLog("The getLicenceNo yearLength -->:"+yearLength));
        log.info(StringUtil.changeForLog("The getLicenceNo end ..."));
        return    systemClient.licence(hciCode,serviceCode,yearLength,licenceSeq).getEntity();
    }

    @Override
    public String getGroupLicenceNo(String serviceCode, AppPremisesRecommendationDto appPremisesRecommendationDto,String orgLicecnceId,Integer premisesNumber) {
        log.info(StringUtil.changeForLog("The getGroupLicenceNo start ..."));
        log.info(StringUtil.changeForLog("The getGroupLicenceNo serviceCode is -->:"+serviceCode));
        log.info(StringUtil.changeForLog("The getGroupLicenceNo orgLicecnceId is -->:"+orgLicecnceId));
        log.info(StringUtil.changeForLog("The getGroupLicenceNo premisesNumber is -->:"+premisesNumber));
        LicenceGrpDto licenceGrpDto = new LicenceGrpDto();
        licenceGrpDto.setSerivceCode(serviceCode);
        licenceGrpDto.setOrgLicecnceId(orgLicecnceId);
        licenceGrpDto.setPremisesNumber(premisesNumber);
        String no = hcsaLicenceClient.groupLicenceNumber(licenceGrpDto).getEntity();
        log.info(StringUtil.changeForLog("The getGroupLicenceNo no -->:"+no));
        int yearLength = 0;
        if(appPremisesRecommendationDto != null && RiskConsts.YEAR.equals(appPremisesRecommendationDto.getChronoUnit())){
            yearLength = appPremisesRecommendationDto.getRecomInNumber();
        }
        log.info(StringUtil.changeForLog("The getGroupLicenceNo yearLength -->:"+yearLength));
        log.info(StringUtil.changeForLog("The getGroupLicenceNo end ..."));
        return   systemClient.groupLicence(serviceCode,String.valueOf(yearLength),no,null).getEntity();
    }

    @Override
    public AppPremisesRecommendationDto getTcu(String appPremCorrecId) {
        return fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrecId,
                InspectionConstants.RECOM_TYPE_TCU).getEntity();
    }
    @Override
    public PremisesDto getLatestVersionPremisesByHciCode(String hciCode) {
        return hcsaLicenceClient.getLatestVersionPremisesByHciCode(hciCode).getEntity();
    }

    @Override
    public KeyPersonnelDto getLatestVersionKeyPersonnelByIdNoAndOrgId(String idNo, String orgId) {
        return hcsaLicenceClient.getLatestVersionKeyPersonnelByidNoAndOrgId(idNo,orgId).getEntity();
    }

    @Override
    public LicenceDto getLicenceDto(String licenceId) {
        LicenceDto result = null;
        if(!StringUtil.isEmpty(licenceId)){
            result = hcsaLicenceClient.getLicenceDtoById(licenceId).getEntity();
        }
        return result;
    }


    @Override
    public LicenceDto getLicenceDtoByLicNo(String licNo) {
        return hcsaLicenceClient.getLicBylicNo(licNo).getEntity();
    }

    @Override
    public List<LicenceGroupDto> createSuperLicDto(EventBusLicenceGroupDtos eventBusLicenceGroupDtos) {
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(eventBusLicenceGroupDtos,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_LICENCESAVE,
                EventBusConsts.OPERATION_LICENCE_SAVE,
                eventBusLicenceGroupDtos.getEventRefNo(),
                null);

        return null;
    }

    @Override
    public EventBusLicenceGroupDtos createFESuperLicDto(String eventRefNum,String submissionId) {
        EventBusLicenceGroupDtos eventBusLicenceGroupDtos =  getEventBusLicenceGroupDtosByRefNo(eventRefNum);
        if(eventBusLicenceGroupDtos!=null){
            EicRequestTrackingDto trackDto = licEicClient.getPendingRecordByReferenceNumber(eventRefNum).getEntity();
            eicCallFeSuperLic(eventBusLicenceGroupDtos);
            trackDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            //send approve notification
            try{
                //save new acra info
//                saveNewAcra(eventBusLicenceGroupDtos);
                //send issue uen email
                log.info(StringUtil.changeForLog("send uen email"));
                sendUenEmail(eventBusLicenceGroupDtos);
                for (LicenceGroupDto item:eventBusLicenceGroupDtos.getLicenceGroupDtos()
                ) {
                    for (SuperLicDto superLicDto : item.getSuperLicDtos()
                    ) {
                        sendNotification(superLicDto);
                    }
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            saveLicenceAppRiskInfoDtos(eventBusLicenceGroupDtos.getLicenceGroupDtos(),eventBusLicenceGroupDtos.getAuditTrailDto());
            hcsaLicenceClient.updateEicTrackStatus(trackDto);
        }else{
            log.error(StringUtil.changeForLog("This eventReo can not get the LicEicRequestTrackingDto -->:"+eventRefNum));
        }

        return eventBusLicenceGroupDtos;
    }
    private void saveLicenceAppRiskInfoDtos( List<LicenceGroupDto> licenceGroupDtos,AuditTrailDto auditTrailDto){
        log.info("----- create save-lic-app-risk-by-licdtos ");
        if( !IaisCommonUtils.isEmpty(licenceGroupDtos)){
            for(LicenceGroupDto licenceGroupDto : licenceGroupDtos){
                try{
                    List<SuperLicDto>  superLicDtos = licenceGroupDto.getSuperLicDtos();
                    List<LicenceDto> licenceDtos = IaisCommonUtils.genNewArrayList();
                    for(SuperLicDto superLicDto : superLicDtos){
                        superLicDto.getLicenceDto().setAuditTrailDto(auditTrailDto);
                        licenceDtos.add(superLicDto.getLicenceDto());
                    }
                    hcsaLicenceClient.saveLicenceAppRiskInfoDtosByLicIds(licenceDtos);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        log.info("----- create save-lic-app-risk-by-licdtos end ");
    }
    private void saveNewAcra(EventBusLicenceGroupDtos eventBusLicenceGroupDtos){
        log.info(StringUtil.changeForLog("save New Acra"));
        for (LicenceGroupDto item:eventBusLicenceGroupDtos.getLicenceGroupDtos()
        ) {
            for (SuperLicDto superLicDto:item.getSuperLicDtos()
            ) {
                LicenceDto licenceDto = superLicDto.getLicenceDto();
                LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenceDto.getLicenseeId()).getEntity();
                GenerateUENDto generateUENDto = new GenerateUENDto();
                int sequenceNumber = 1;
                String consumerId = "waiting";
                String agencyReferenceNumber = "waiting";
                generateUENDto.setSequenceNumber(sequenceNumber);
                //basic
                IssuanceBasic issuanceBasic = new IssuanceBasic();
                generateUENDto.getBasic().setAgencyReferenceNumber(agencyReferenceNumber);
                String uen = generateUen();
                issuanceBasic.setUen(uen);
                issuanceBasic.setIssuanceAgency("ACRA");
                issuanceBasic.setEntityType("CL");
                issuanceBasic.setEntityName(licenseeDto.getName());
                Date now = new Date();
                issuanceBasic.setUenIssueDate(Formatter.formatDateTime(now," YYYY-MM-DD"));
                issuanceBasic.setRegistrationDate(Formatter.formatDateTime(now," YYYY-MM-DD"));
                issuanceBasic.setTelephone(licenseeDto.getOfficeTelNo());
                issuanceBasic.setEmail(licenseeDto.getEmilAddr());
                generateUENDto.setBasic(issuanceBasic);

                //address
                IssuanceAddresses addresses = new IssuanceAddresses();
                addresses.setSequenceNumber(sequenceNumber);
                addresses.setAgencyReferenceNumber(agencyReferenceNumber);
                addresses.setStandard("D");
                addresses.setPostalCode(Integer.valueOf(licenseeDto.getPostalCode()));
                addresses.setHouseBlockNumber(licenseeDto.getBlkNo());
                addresses.setStreetName(licenseeDto.getStreetName());
                addresses.setBuildingName(licenseeDto.getBuildingName());
                addresses.setLevelNumber(licenseeDto.getFloorNo());
                addresses.setUnitNumber(licenseeDto.getUnitNo());
                addresses.setIsInvalid(Boolean.TRUE);
                List<IssuanceAddresses> addressesList = IaisCommonUtils.genNewArrayList();
                addressesList.add(addresses);
                generateUENDto.setAddresses(addressesList);
                log.info(StringUtil.changeForLog("generateUenDto : " + JsonUtil.parseToJson(generateUENDto)));
                IaisUENDto iaisUENDto = new IaisUENDto();
                iaisUENDto.setGenerateUENDto(generateUENDto);
                iaisUENDto.setLicenseeId("licensee");
                acraUenBeClient.generateUen(iaisUENDto);

            }
        }
    }

    private String generateUen(){
        int max=10,min=0;
        List<Integer> retNum = new ArrayList<Integer>(9);
        for(Integer i=0;i<9;i++){
            Integer ran2 = (int) (Math.random()*(max-min)+min);
            retNum.add(ran2);
        }

        List<Integer> uenIdx = Arrays.asList(4, 5, 6, 7, 8, 0, 1, 2, 3);
        Integer chkSum = 0;
        for (Integer i = 1; i < 10; i++) {
            chkSum += retNum.get(uenIdx.get(i-1))*i;
        }
        chkSum = 11 - (chkSum % 11) - 1;
        String sNo = "CDEGHKMNRWZ";
        String lastAlphabet = sNo.substring(chkSum, chkSum + 1);
        StringBuilder resBuilder = new StringBuilder();
        for (Integer integer : retNum) {
            resBuilder.append(integer);
        }
        resBuilder.append(lastAlphabet);
        return resBuilder.toString();
    }

    private void sendUenEmail(EventBusLicenceGroupDtos eventBusLicenceGroupDtos){
        log.info(StringUtil.changeForLog("send uen email start"));
        for (LicenceGroupDto item:eventBusLicenceGroupDtos.getLicenceGroupDtos()
             ) {
            for (SuperLicDto superLicDto:item.getSuperLicDtos()
                 ) {
                try {
                    LicenceDto licenceDto = superLicDto.getLicenceDto();
                    log.info(StringUtil.changeForLog("licence id = " + licenceDto.getId()));
                    LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenceDto.getLicenseeId()).getEntity();
                    LicenseeIndividualDto licenseeIndividualDto = licenseeDto.getLicenseeIndividualDto();
                    PremisesDto premisesDto = superLicDto.getPremisesGroupDtos().get(0).getPremisesDto();
                    log.info(StringUtil.changeForLog("licenseeIndividualDto.getUenMailFlag() = " + licenseeIndividualDto.getUenMailFlag()));
                    log.info(StringUtil.changeForLog("premisesDto = " + JsonUtil.parseToJson(premisesDto)));
                    //judge licence is singlepass
                    if(licenseeIndividualDto.getUenMailFlag() == 0){
                        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
                        templateContent.put("HCI_Name", premisesDto.getHciName());
                        String address = MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode());
                        templateContent.put("HCI_Address", address);
                        log.info(StringUtil.changeForLog("HCI_Address = " + address));
                        OrganizationDto organizationDto = organizationClient.getOrganizationById(licenseeDto.getOrganizationId()).getEntity();
                        templateContent.put("UEN_No", organizationDto.getUenNo());
                        templateContent.put("Applicant", licenseeDto.getName());
                        templateContent.put("ServiceName", licenceDto.getSvcName());
                        templateContent.put("LicenceNo", licenceDto.getLicenceNo());
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DAY_OF_MONTH, systemParamConfig.getIssueUenGraceDay());
                        templateContent.put("GraceDate", Formatter.formatDate(c.getTime()));
                        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
                        templateContent.put("newSystem", loginUrl);
                        templateContent.put("emailAddress", systemAddressOne);
                        templateContent.put("telNo", systemPhoneNumber);

                        MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_EMAIL).getEntity();
                        MsgTemplateDto smsTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_SMS).getEntity();
                        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_MSG).getEntity();
                        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                        String emailSubject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
                        String smsSubject = MsgUtil.getTemplateMessageByContent(smsTemplateDto.getTemplateName(),subMap);
                        String msgSubject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),subMap);


                        EmailParam emailParam = new EmailParam();
                        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_EMAIL);
                        emailParam.setTemplateContent(templateContent);
                        emailParam.setSubject(emailSubject);
                        emailParam.setQueryCode(licenceDto.getLicenceNo());
                        emailParam.setReqRefNum(licenceDto.getLicenceNo());
                        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                        emailParam.setRefId(licenceDto.getId());
                        notificationHelper.sendNotification(emailParam);
                        log.info(StringUtil.changeForLog("send email end"));

                        EmailParam smsParam = new EmailParam();
                        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_SMS);
                        smsParam.setSubject(smsSubject);
                        smsParam.setTemplateContent(templateContent);
                        smsParam.setQueryCode(licenceDto.getLicenceNo());
                        smsParam.setReqRefNum(licenceDto.getLicenceNo());
                        smsParam.setRefId(licenceDto.getId());
                        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                        notificationHelper.sendNotification(smsParam);
                        log.info(StringUtil.changeForLog("send sms end"));

                        EmailParam msgParam = new EmailParam();
                        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_UEN_001_MSG);
                        msgParam.setTemplateContent(templateContent);
                        msgParam.setSubject(msgSubject);
                        msgParam.setQueryCode(licenceDto.getLicenceNo());
                        msgParam.setReqRefNum(licenceDto.getLicenceNo());
                        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
                        svcCodeList.add(licenseeIndividualDto.getFirstServiceCode());
                        msgParam.setSvcCodeList(svcCodeList);
                        msgParam.setRefId(licenceDto.getId());
                        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                        notificationHelper.sendNotification(msgParam);
                        log.info(StringUtil.changeForLog("send msg end"));
                        //set flag = 1
                        organizationClient.updateIndividualFlag(licenseeIndividualDto.getId());
                        log.info(StringUtil.changeForLog("updateIndividualFlag end"));
                    }

                }catch (Exception e){
                    continue;
                }

            }
        }

    }

    private void sendNotification(SuperLicDto superLicDto){
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
        String corpPassUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + "/main-web/eservice/INTERNET/FE_Landing";
        if(superLicDto != null) {
            LicenceDto licenceDto = superLicDto.getLicenceDto();
            if(licenceDto != null){
                String licenceNo = licenceDto.getLicenceNo();
                String licenseeId = licenceDto.getLicenseeId();
                List<String> appIdList = hcsaLicenceClient.getAppIdsByLicId(superLicDto.getLicenceDto().getId()).getEntity();
                log.debug(StringUtil.changeForLog("send approve email --- get app list by licence id : " + superLicDto.getLicenceDto().getId()));
                if(appIdList != null && appIdList.size() >0) {
                    String appId = appIdList.get(0);
                    log.debug(StringUtil.changeForLog("send approve email --- get app by app id : " + appId));
                    ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
                    //getAppPremisesCorrelationsByAppId
                    AppPremisesRecommendationDto inspectionRecommendation = null;
                    AppPremisesRecommendationDto tempRecommendation = null;
                    for(String applicationId : appIdList){
                        ApplicationDto appDto = applicationClient.getApplicationById(applicationId).getEntity();
                        if(appDto != null){
                            AppPremisesCorrelationDto appPremisesCorrelationDto = appPremisesCorrClient.getAppPremisesCorrelationsByAppId(appDto.getId()).getEntity().get(0);
                            if(appPremisesCorrelationDto != null){
                                tempRecommendation = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();
                                if(tempRecommendation != null){
                                    inspectionRecommendation = tempRecommendation;
                                    break;
                                }else{
                                    continue;
                                }
                            }
                        }
                    }

                    if (applicationDto != null) {
                        HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
                        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
                        svcCodeList.add(svcDto.getSvcCode());
                        String applicationNo = applicationDto.getApplicationNo();
                        log.debug(StringUtil.changeForLog("send approve email --- get app by applicationNo : " + applicationNo));
                        String applicationType = applicationDto.getApplicationType();
                        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
                        String appGrpId = applicationDto.getAppGrpId();
                        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appGrpId).getEntity();
                        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                        if(licenseeDto != null && orgUserDto != null){
                            String applicantName = orgUserDto.getDisplayName();
                            String organizationId = licenseeDto.getOrganizationId();
                            OrganizationDto organizationDto = organizationClient.getOrganizationById(organizationId).getEntity();
                            String appDate = Formatter.formatDateTime(new Date(), "dd/MM/yyyy");
                            String MohName = AppConsts.MOH_AGENCY_NAME;
                            log.info(StringUtil.changeForLog("send notification applicantName : " + applicantName));
                            String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationType);
                            log.info(StringUtil.changeForLog("send notification applicationType : " + applicationTypeShow));
                            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
                                sendNewAppApproveNotification(applicantName,applicationTypeShow,applicationNo,appDate,licenceNo,svcCodeList,loginUrl,corpPassUrl,MohName,organizationDto,inspectionRecommendation);
                            }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
                                sendRenewalAppApproveNotification(applicantName,applicationTypeShow,applicationNo,appDate,licenceNo,svcCodeList,loginUrl,MohName,inspectionRecommendation);
                            }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
                                try {
                                    if(applicationGroupDto.getNewLicenseeId()!=null){
                                        sendRfcApproveLicenseeEmail(applicationGroupDto,applicationDto,licenceNo,svcCodeList);
                                    }else {
                                        sendRfcApproveNotification(applicantName,applicationTypeShow,applicationNo,appDate,licenceNo,svcCodeList);
                                    }
                                } catch (IOException e) {
                                    log.info(e.getMessage(),e);
                                }
                            }
                        }else{
                            if(licenseeDto == null){
                                log.error(StringUtil.changeForLog("---licenseeDto == null"));
                            }
                            if(orgUserDto == null){
                                log.error(StringUtil.changeForLog("---orgUserDto == null"));
                            }
                        }
                    }
                }
            }
        }
    }


    private void sendRfcApproveLicenseeEmail(ApplicationGroupDto applicationGroupDto,  ApplicationDto applicationDto,String licenceNo,
                                             List<String> svcCodeList)  {
        String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(applicationGroupDto.getLicenseeId()).getEntity();
        LicenseeDto newLicenseeDto = organizationClient.getLicenseeDtoById(applicationGroupDto.getNewLicenseeId()).getEntity();
        String applicantName = licenseeDto.getName();
        emailMap.put("name_transferee", newLicenseeDto.getName());
        emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
        emailMap.put("ApplicationNumber", applicationDto.getApplicationNo());
        emailMap.put("ApplicationDate", Formatter.formatDate(new Date()));
        emailMap.put("ExistingLicensee", applicantName);
        emailMap.put("transferee_licensee", newLicenseeDto.getName());
        emailMap.put("LicenceNumber", licenceNo);
        emailMap.put("specialText", "");
        //emailMap.put("Hypelink", loginUrl);
        emailMap.put("isSpecial", "N");
        emailMap.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
        emailMap.put("HCSA_Regulations", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
        emailMap.put("Number", systemPhoneNumber);
        emailMap.put("email_1", systemAddressOne);
        emailMap.put("Email_2", systemAddressTwo);
        emailMap.put("systemLink", loginUrl);
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED);
        emailParam.setTemplateContent(emailMap);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        MsgTemplateDto rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED).getEntity();
        map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (IOException | TemplateException e) {
            log.info(e.getMessage(),e);
        }
        emailParam.setSubject(subject);
        //email
        log.info(StringUtil.changeForLog("send RfcApproveLicensee application email"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send RfcApproveLicensee application email end"));
        //msg
        try {
            emailParam.setSvcCodeList(svcCodeList);
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED_MSG);
            emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            emailParam.setRefId(applicationDto.getApplicationNo());
            log.info(StringUtil.changeForLog("send RfcApproveLicensee application msg"));
            notificationHelper.sendNotification(emailParam);
            log.info(StringUtil.changeForLog("send RfcApproveLicensee application msg end"));
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        //sms
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED_SMS);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        log.info(StringUtil.changeForLog("send RfcApproveLicensee application sms"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send RfcApproveLicensee application sms end"));
    }

    public void sendRfcApproveNotification(String applicantName,
                                           String applicationTypeShow,
                                           String applicationNo,
                                           String appDate,
                                           String licenceNo,
                                           List<String> svcCodeList) throws IOException {
        String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        emailMap.put("change", "true");
        emailMap.put("ApplicantName", applicantName);
        emailMap.put("ApplicationType", applicationTypeShow);
        emailMap.put("ApplicationNumber", applicationNo);
        emailMap.put("ApplicationDate", appDate);
        emailMap.put("systemLink", loginUrl);
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT);
        emailParam.setTemplateContent(emailMap);
        emailParam.setQueryCode(applicationNo);
        emailParam.setReqRefNum(applicationNo);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationNo);
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        MsgTemplateDto rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT).getEntity();
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        String subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (TemplateException e) {
            log.info(e.getMessage(),e);
        }
        emailParam.setSubject(subject);
        //email
        log.info(StringUtil.changeForLog("send RfcApprove application email"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send RfcApprove application email end"));
        //msg
        try {
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT_MSG);
            emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            emailParam.setSvcCodeList(svcCodeList);
            emailParam.setRefId(applicationNo);
            log.info(StringUtil.changeForLog("send RfcApprove application msg"));
            notificationHelper.sendNotification(emailParam);
            log.info(StringUtil.changeForLog("send RfcApprove application msg end"));
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }

        //sms
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT_SMS);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        log.info(StringUtil.changeForLog("send RfcApprove application sms"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send RfcApprove application sms end"));
    }

    private void sendRenewalAppApproveNotification(String applicantName,
                                               String applicationTypeShow,
                                               String applicationNo,
                                               String appDate,
                                               String licenceNo,
                                               List<String> svcCodeList,
                                               String loginUrl,
                                               String MohName,
                                               AppPremisesRecommendationDto inspectionRecommendation){
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicantName", applicantName);
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        map.put("applicationDate", appDate);
        map.put("licenceNumber", licenceNo);
        map.put("isSpecial", "N");
        if(inspectionRecommendation != null){
            map.put("inInspection", "Y");
            map.put("inspectionText", inspectionRecommendation.getRemarks());
        }else {
            map.put("inInspection", "N");
        }
        map.put("systemLink", loginUrl);

        map.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
        map.put("regulationLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
        map.put("link", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_LINK));
        map.put("scdfLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_SCDF_LINK));
        map.put("momLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_MOM_LINK));
        map.put("irasLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_IRAS_LINK));

        map.put("phoneNumber", systemPhoneNumber);
        map.put("emailAddress1", systemAddressOne);
        map.put("emailAddress2", systemAddressTwo);
        map.put("MOH_AGENCY_NAME", MohName);
        try {
            String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is approved ";
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE);
            emailParam.setTemplateContent(map);
            emailParam.setQueryCode(applicationNo);
            emailParam.setReqRefNum(applicationNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(applicationNo);
            emailParam.setSubject(subject);
            //send email
            log.info(StringUtil.changeForLog("send renewal application email"));
            notificationHelper.sendNotification(emailParam);
            log.info(StringUtil.changeForLog("send renewal application email end"));
            //send sms
            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE_SMS);
            smsParam.setSubject(subject);
            smsParam.setQueryCode(applicationNo);
            smsParam.setReqRefNum(applicationNo);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            smsParam.setRefId(applicationNo);
            log.info(StringUtil.changeForLog("send renewal application sms"));
            notificationHelper.sendNotification(smsParam);
            log.info(StringUtil.changeForLog("send renewal application sms end"));
            //send message
            EmailParam messageParam = new EmailParam();
            messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE_MESSAGE);
            messageParam.setTemplateContent(map);
            messageParam.setQueryCode(applicationNo);
            messageParam.setReqRefNum(applicationNo);
            messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            messageParam.setRefId(applicationNo);
            messageParam.setSubject(subject);
            messageParam.setSvcCodeList(svcCodeList);
            log.info(StringUtil.changeForLog("send renewal application message"));
            notificationHelper.sendNotification(messageParam);
            log.info(StringUtil.changeForLog("send renewal application message end"));
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    private void sendNewAppApproveNotification(String applicantName,
                                               String applicationTypeShow,
                                               String applicationNo,
                                               String appDate,
                                               String licenceNo,
                                               List<String> svcCodeList,
                                               String loginUrl,
                                               String corpPassUrl,
                                               String MohName,
                                               OrganizationDto organizationDto,
                                               AppPremisesRecommendationDto inspectionRecommendation){
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicantName", applicantName);
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        map.put("applicationDate", appDate);
        map.put("licenceNumber", licenceNo);
        map.put("isSpecial", "N");
        map.put("isCorpPass", "N");
        if(inspectionRecommendation != null){
            map.put("inInspection", "Y");
            map.put("inspectionText", inspectionRecommendation.getRemarks());
        }else {
            map.put("inInspection", "N");
        }
        if(organizationDto != null){
            if(StringUtil.isEmpty(organizationDto.getUenNo())){
                map.put("isCorpPass", "Y");
                map.put("corpPassLink", corpPassUrl);
            }
        }
        map.put("systemLink", loginUrl);

        map.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
        map.put("regulationLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
        map.put("link", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_LINK));
        map.put("scdfLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_SCDF_LINK));
        map.put("momLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_MOM_LINK));

        map.put("phoneNumber", systemPhoneNumber);
        map.put("emailAddress1", systemAddressOne);
        map.put("emailAddress2", systemAddressTwo);
        map.put("MOH_AGENCY_NAME", MohName);

        try {
            String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is approved ";
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_ID);
            emailParam.setTemplateContent(map);
            emailParam.setQueryCode(applicationNo);
            emailParam.setReqRefNum(applicationNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(applicationNo);
            emailParam.setSubject(subject);
            //send email
            log.info(StringUtil.changeForLog("send new application email"));
            notificationHelper.sendNotification(emailParam);
            log.info(StringUtil.changeForLog("send new application email end"));
            //send sms
            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_SMS_ID);
            smsParam.setSubject(subject);
            smsParam.setQueryCode(applicationNo);
            smsParam.setReqRefNum(applicationNo);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            smsParam.setRefId(applicationNo);
            log.info(StringUtil.changeForLog("send new application sms"));
            notificationHelper.sendNotification(smsParam);
            log.info(StringUtil.changeForLog("send new application sms end"));
            //send message
            EmailParam messageParam = new EmailParam();
            messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_MESSAGE_ID);
            messageParam.setTemplateContent(map);
            messageParam.setQueryCode(applicationNo);
            messageParam.setReqRefNum(applicationNo);
            messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            messageParam.setRefId(applicationNo);
            messageParam.setSubject(subject);
            messageParam.setSvcCodeList(svcCodeList);
            log.info(StringUtil.changeForLog("send new application message"));
            notificationHelper.sendNotification(messageParam);
            log.info(StringUtil.changeForLog("send new application message end"));
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public void eicCallFeSuperLic(EventBusLicenceGroupDtos dto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        beEicGatewayClient.createLicence(dto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
    }

    @Override
    public EventBusLicenceGroupDtos getEventBusLicenceGroupDtosByRefNo(String refNo) {
        return hcsaLicenceClient.getEventBusLicenceGroupDtosByRefNo(refNo).getEntity();
    }

    @Override
    public void updateLicEicRequestTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto) {
        licEicClient.saveEicTrack(licEicRequestTrackingDto);
    }

    @Override
    public EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo) {
        return licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
    }

    @Override
    public MsgTemplateDto getMsgTemplateById(String id) {
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(id).getEntity();
        return msgTemplateDto;
    }

    @Override
    public void sendEmail(EmailDto emailDto) {
        emailClient.sendNotification(emailDto);
    }

    @Override
    public List<PremisesGroupDto> getPremisesGroupDtoByOriginLicenceId(String originLicenceId) {
        return hcsaLicenceClient.getPremisesGroupDtos(originLicenceId).getEntity();
    }

    @Override
    public List<LicAppCorrelationDto> getLicAppCorrelationDtosByApplicationIds(List<String> appIds) {
        return hcsaLicenceClient.getLicAppCorrelationDtosByApplicationIds(appIds).getEntity();
    }

    @Override
    public PremisesDto getHciCode(AppGrpPremisesEntityDto appGrpPremisesEntityDto) {
        log.info(StringUtil.changeForLog("The getHciCode start ..."));
        PremisesDto result = null;
        if(appGrpPremisesEntityDto != null){
            result =  hcsaLicenceClient.getHciCodePremises(appGrpPremisesEntityDto).getEntity();
        }
        log.info(StringUtil.changeForLog("The getHciCode end ..."));
        return result;
    }
}
