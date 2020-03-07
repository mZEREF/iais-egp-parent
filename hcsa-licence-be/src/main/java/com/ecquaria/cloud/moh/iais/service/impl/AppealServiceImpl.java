package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicEicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.AppealClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * AppealServiceImpl
 *
 * @author suocheng
 * @date 2/6/2020
 */
@Service
@Slf4j
public class AppealServiceImpl implements AppealService {
    @Autowired
    private AppealClient appealClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private LicenceService licenceService;
    @Autowired
    private EventBusHelper eventBusHelper;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public List<AppealApproveGroupDto> getAppealApproveDtos() {
        List<AppealApproveGroupDto> result = appealClient.getApproveAppeal().getEntity();
        if(!IaisCommonUtils.isEmpty(result)){
           for(AppealApproveGroupDto appealApproveGroupDto : result){
               List<AppealApproveDto> appealApproveDtos = appealApproveGroupDto.getAppealApproveDtoList();
               if(!IaisCommonUtils.isEmpty(appealApproveDtos)){
                   List<String> licenceIds = new ArrayList<>();
                   //get all licenceIds
                   for (AppealApproveDto appealApproveDto : appealApproveDtos){
                       AppPremiseMiscDto appealDto = appealApproveDto.getAppPremiseMiscDto();
                       if(ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealDto.getAppealType())){
                           licenceIds.add(appealDto.getRelateRecId());
                       }
                   }
                   //get licenceDto and set to AppealApproveDto
                   if(!IaisCommonUtils.isEmpty(licenceIds)){
                       List<LicenceDto> licenceDtos = hcsaLicenceClient.retrieveLicenceDtos(licenceIds).getEntity();
                       if(!IaisCommonUtils.isEmpty(licenceDtos)){
                           for (AppealApproveDto appealApproveDto : appealApproveDtos){
                               AppPremiseMiscDto appealDto = appealApproveDto.getAppPremiseMiscDto();
                               if(ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealDto.getAppealType())){
                                   String licenceId = appealDto.getRelateRecId();
                                   LicenceDto licenceDto = getLicenceDto(licenceDtos,licenceId);
                                   if(licenceDto!=null) {
                                       appealApproveDto.setLicenceDto(licenceDto);
                                   }
                               }
                           }
                       }
                   }
               }
           }

            //
        }
        return  result;
    }

    @Override
    public AppealLicenceDto createAppealLicenceDto(AppealLicenceDto appealLicenceDto) {
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appealLicenceDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_LICENCESAVE,
                EventBusConsts.OPERATION_LICENCE_SAVE_APPEAL,
                appealLicenceDto.getEventRefNo(),
                null);

        return appealLicenceDto;
    }

    @Override
    public AppealApplicationDto createAppealApplicationDto(AppealApplicationDto appealApplicationDto) {
        String callBackUrl = systemParamConfig.getIntraServerName()+"/hcsa-licence-web/eservice/INTRANET/LicenceEventBusCallBack";
        String sopUrl = systemParamConfig.getIntraServerName()+"/hcsa-licence-web/eservice/INTRANET/ApplicationView";
        String project ="hcsaLicenceBe";
        String processName = "generateLicenceAppeal";
        String step = "start";

        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appealApplicationDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_APPLICATION_UPDATE_APPEAL,
                appealApplicationDto.getEventRefNo(), null);

        return appealApplicationDto;
    }

    @Override
    public AppealLicenceDto updateFEAppealLicenceDto(String eventRefNum,String submissionId) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        LicEicRequestTrackingDto licEicRequestTrackingDto = licenceService.getLicEicRequestTrackingDtoByRefNo(eventRefNum);
        AppealLicenceDto appealLicenceDto = getObjectLic(licEicRequestTrackingDto,AppealLicenceDto.class);
        if(appealLicenceDto!=null){
            appealLicenceDto = beEicGatewayClient.updateAppealLicence(appealLicenceDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
        }else{
            log.error(StringUtil.changeForLog("This eventReo can not get the LicEicRequestTrackingDto -->:"+eventRefNum));
        }

        return appealLicenceDto;
    }


    private <T> T getObjectLic(LicEicRequestTrackingDto licEicRequestTrackingDto, Class<T> cls){
        T result = null;
        if(licEicRequestTrackingDto!=null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                result = mapper.readValue(licEicRequestTrackingDto.getDtoObj(), cls);
            } catch (IOException e) {
                log.error(StringUtil.changeForLog(e.getMessage()),e);
            }
        }
        return  result;
    }

    private  LicenceDto getLicenceDto(List<LicenceDto> licenceDtos,String licenceId){
        LicenceDto result = null;
        if(!IaisCommonUtils.isEmpty(licenceDtos)&&!StringUtil.isEmpty(licenceId)){
          for(LicenceDto licenceDto :licenceDtos){
              if(licenceId.equals(licenceDto.getId())){
                  result = licenceDto;
                  break;
              }
          }
        }
        return result;
    }
}
