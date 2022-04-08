package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private EventBusHelper eventBusHelper;

    @Override
    public List<AppealApproveGroupDto> getAppealApproveDtos() {
        List<AppealApproveGroupDto> result = applicationClient.getApproveAppeal().getEntity();
        if(!IaisCommonUtils.isEmpty(result)){
           for(AppealApproveGroupDto appealApproveGroupDto : result){
               List<AppealApproveDto> appealApproveDtos = appealApproveGroupDto.getAppealApproveDtoList();
               if(!IaisCommonUtils.isEmpty(appealApproveDtos)){
                   List<String> licenceIds = IaisCommonUtils.genNewArrayList();
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
                                       List<LicenceDto> entity = hcsaLicenceClient.getBaseOrSpecLicence(licenceDto.getId()).getEntity();
                                       appealApproveDto.setOtherLicenceDto(entity);
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
        String callBackUrl = systemParamConfig.getIntraServerName()+"/hcsa-licence-web/eservice/INTRANET/ComEventBusCallback";
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
    public void updateAppPremiseMisc(List<AppPremiseMiscDto> appPremiseMiscDtoList) {
        List<String> corrIds=new ArrayList<>(appPremiseMiscDtoList.size());
        for(AppPremiseMiscDto appPremiseMiscDto : appPremiseMiscDtoList){
            corrIds.add(appPremiseMiscDto.getAppPremCorreId());
        }
        List<ApplicationDto> applicationDtos = applicationClient.getApplicationDtoByCorrIds(corrIds).getEntity();
        for(ApplicationDto applicationDto : applicationDtos){
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
            applicationClient.updateApplication(applicationDto);
            beEicGatewayClient.callEicWithTrack(applicationDto, beEicGatewayClient::updateApplication, "updateApplication");
        }
    }

    private <T> T getObjectLic(EicRequestTrackingDto licEicRequestTrackingDto, Class<T> cls){
        T result = null;
        if(licEicRequestTrackingDto!=null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                result = mapper.readValue(licEicRequestTrackingDto.getDtoObject(), cls);
            } catch (IOException e) {
                log.debug(StringUtil.changeForLog("can not get the licEicRequestTrackingDto"));
                log.error(StringUtil.changeForLog(e.getMessage()),e);
            }
        }else{
            log.debug(StringUtil.changeForLog("The licEicRequestTrackingDto is null ..."));
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
