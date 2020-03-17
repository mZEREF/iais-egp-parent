package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import java.util.HashMap;
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
    private SystemParamConfig systemParamConfig;
    @Autowired
    private EventBusHelper eventBusHelper;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private EmailClient emailClient;

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
        Map<String,Object> param = new HashMap<>();
        param.put("day",day);

        return   applicationClient.getGroup(day).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getHcsaServiceById(List<String> serviceIds) {

        return  hcsaConfigClient.getHcsaService(serviceIds).getEntity();
    }

    @Override
    public String getHciCode(String serviceCode) {
        return     systemClient.hclCodeByCode(serviceCode).getEntity();
    }

    @Override
    public String getLicenceNo(String hciCode, String serviceCode, int yearLength) {

        Integer licenceSeq =  hcsaLicenceClient.licenceNumber(hciCode).getEntity();
        Map<String,Object> param = new HashMap();
        param.put("hciCode",hciCode);
        param.put("serviceCode",serviceCode);
        param.put("yearLength",yearLength);
        param.put("licenceSeq",licenceSeq);

        return    systemClient.licence(hciCode,serviceCode,yearLength,licenceSeq).getEntity();
    }

    @Override
    public String getGroupLicenceNo(String hscaCode, int yearLength) {
        String entity = hcsaLicenceClient.groupLicenceNumber(hscaCode).getEntity();

        return   systemClient.groupLicence(hscaCode,yearLength+"",entity).getEntity();
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
        return hcsaLicenceClient.getLicenceDtoById(licenceId).getEntity();
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
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        EventBusLicenceGroupDtos eventBusLicenceGroupDtos =  getEventBusLicenceGroupDtosByRefNo(eventRefNum);
        if(eventBusLicenceGroupDtos!=null){
            eventBusLicenceGroupDtos =beEicGatewayClient.createLicence(eventBusLicenceGroupDtos, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
        }else{
            log.error(StringUtil.changeForLog("This eventReo can not get the LicEicRequestTrackingDto -->:"+eventRefNum));
        }

        return eventBusLicenceGroupDtos;
    }

    @Override
    public EventBusLicenceGroupDtos getEventBusLicenceGroupDtosByRefNo(String refNo) {
        return hcsaLicenceClient.getEventBusLicenceGroupDtosByRefNo(refNo).getEntity();
    }

    @Override
    public EicRequestTrackingDto updateLicEicRequestTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto) {
        return hcsaLicenceClient.updateLicEicRequestTracking(licEicRequestTrackingDto).getEntity();
    }

    @Override
    public EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo) {
        return hcsaLicenceClient.getLicEicRequestTrackingDto(refNo).getEntity();
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


}
