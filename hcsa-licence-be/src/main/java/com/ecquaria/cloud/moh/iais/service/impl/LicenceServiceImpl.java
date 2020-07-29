package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGrpDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private GenerateIdClient generateIdClient;

    @Autowired
    private EventBusHelper eventBusHelper;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private LicEicClient licEicClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public List<ApplicationLicenceDto> getCanGenerateApplications(int day) {
        Map<String,Object> param = IaisCommonUtils.genNewHashMap();
        param.put("day",day);

        return   applicationClient.getGroup(day).getEntity();
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
        Integer licenceSeq =  hcsaLicenceClient.licenceNumber(hciCode).getEntity();
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
            hcsaLicenceClient.updateEicTrackStatus(trackDto);
        }else{
            log.error(StringUtil.changeForLog("This eventReo can not get the LicEicRequestTrackingDto -->:"+eventRefNum));
        }

        return eventBusLicenceGroupDtos;
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
