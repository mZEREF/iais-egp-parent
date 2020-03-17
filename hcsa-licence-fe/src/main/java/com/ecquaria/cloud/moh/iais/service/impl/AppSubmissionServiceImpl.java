package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * AppSubmisionServiceImpl
 *
 * @author suocheng
 * @date 11/6/2019
 */
@Service
@Slf4j
public class AppSubmissionServiceImpl implements AppSubmissionService {
    String draftUrl =  RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION_DRAFT;
    String submission = RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION;

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private HcsaLicenClient hcsaLicenClient;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;


    @Override
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto= applicationClient.saveSubmision(appSubmissionDto).getEntity();
        //asynchronous save the other data.
        eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        //asynchronous save the other data.
        informationEventBus(appSubmissionRequestInformationDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitPremisesListRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        //asynchronous save the other data.
        premisesListInformationEventBus(appSubmissionRequestInformationDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        return  applicationClient.saveDraft(appSubmissionDto).getEntity();
    }

    @Override
    public String getDraftNo(String appType) {
        return   systemAdminClient.draftNumber(appType).getEntity();
    }

    @Override
    public String getGroupNo(String appType) {

        return   systemAdminClient.applicationNumber(appType).getEntity();
    }

    @Override
    public FeeDto getGroupAmount(AppSubmissionDto appSubmissionDto) {
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount start ...."));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<LicenceFeeDto> linenceFeeQuaryDtos = new ArrayList();
        List<String> premisessTypes =  new ArrayList();
        //todo when multiple base service
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
            premisessTypes.add(appGrpPremisesDto.getPremisesType());
        }
        String baseServiceCode = "";
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                baseServiceCode = appSvcRelatedInfoDto.getServiceCode();
            }
        }

        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
            licenceFeeDto.setBaseService(baseServiceCode);
            licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
            licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
            licenceFeeDto.setPremises(premisessTypes);

            if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
                licenceFeeDto.setRenewCount(1);
                String licenceId = appSubmissionDto.getLicenceId();
                List<String> licenceIds = IaisCommonUtils.genNewArrayList();
                licenceIds.add(licenceId);
                List<HcsaLicenceGroupFeeDto> hcsaLicenceGroupFeeDtos =  hcsaLicenClient.retrieveHcsaLicenceGroupFee(licenceIds).getEntity();
                if(!IaisCommonUtils.isEmpty(hcsaLicenceGroupFeeDtos)){
                    HcsaLicenceGroupFeeDto hcsaLicenceGroupFeeDto =  hcsaLicenceGroupFeeDtos.get(0);
                    licenceFeeDto.setGroupId(hcsaLicenceGroupFeeDto.getGroupId());
                    licenceFeeDto.setMigrated(hcsaLicenceGroupFeeDto.isMigrated());
                    licenceFeeDto.setOldAmount(hcsaLicenceGroupFeeDto.getAmount());
                    licenceFeeDto.setExpiryDate(hcsaLicenceGroupFeeDto.getExpiryDate());
                    if(hcsaLicenceGroupFeeDto.isMigrated()){
                        licenceFeeDto.setRenewCount(hcsaLicenceGroupFeeDto.getCount());
                    }else{
                        licenceFeeDto.setRenewCount(0);
                    }
                }
               // licenceFeeDto.setCharity();
            }
            linenceFeeQuaryDtos.add(licenceFeeDto);
        }
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:"+linenceFeeQuaryDtos.size()));
        String appTYpe = appSubmissionDto.getAppType();
        FeeDto entity ;
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appTYpe)){
            entity = appConfigClient.renewFee(linenceFeeQuaryDtos).getEntity();
        }else{
            entity = appConfigClient.newFee(linenceFeeQuaryDtos).getEntity();
        }
        //Double amount = entity.getTotal();
        //log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl amount is -->:"+amount));
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }

    @Override
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto) {
        RecommendInspectionDto recommendInspectionDto = new RecommendInspectionDto();
        List<RiskAcceptiionDto> riskAcceptiionDtos = new ArrayList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setSvcType(appSvcRelatedInfoDto.getServiceType());
            riskAcceptiionDto.setBaseServiceCodeList(appSvcRelatedInfoDto.getBaseServiceCodeList());
            riskAcceptiionDtos.add(riskAcceptiionDto);
        }

        return     appConfigClient.recommendIsPreInspection(recommendInspectionDto).getEntity();
    }

    @Override
    public void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = new ArrayList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }

        List<RiskResultDto> riskResultDtoList = appConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();

        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList,serviceCode);
            if(riskResultDto!= null){
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo) {

        return applicationClient.getAppSubmissionDtoByAppNo(appNo).getEntity();
    }

    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList,String serviceCode){
       RiskResultDto result = null;
       if(riskResultDtoList == null || StringUtil.isEmpty(serviceCode)){
        return result;
       }
       for(RiskResultDto riskResultDto : riskResultDtoList){
           if(serviceCode.equals(riskResultDto.getSvcCode())){
               result = riskResultDto ;
           }
       }
       return result;
   }

   private void  informationEventBus(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process){
       //prepare request parameters
       appSubmissionRequestInformationDto.setEventRefNo(appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpNo());
       SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
               generateIdClient.getSeqId().getEntity(),
               EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_REQUEST_INFORMATION,
               appSubmissionRequestInformationDto.getEventRefNo(), process);
   }

   private void premisesListInformationEventBus(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
                                                Process process){
        appSubmissionRequestInformationDto.setEventRefNo(appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpNo());
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
               generateIdClient.getSeqId().getEntity(),
               EventBusConsts.SERVICE_NAME_APPSUBMIT,
               EventBusConsts.OPERATION_REQUEST_INFORMATION,
               appSubmissionRequestInformationDto.getEventRefNo(), process);
   }

    private  void eventBus(AppSubmissionDto appSubmissionDto, Process process){
        //prepare request parameters
        appSubmissionDto.setEventRefNo(appSubmissionDto.getAppGrpNo());

        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_NEW_APP_SUBMIT,
                appSubmissionDto.getEventRefNo(), process);
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId) {
        return licenceClient.getAppSubmissionDto(licenceId).getEntity();
    }

    @Override
    public FeeDto getGroupAmendAmount(AmendmentFeeDto amendmentFeeDto) {
        return appConfigClient.amendmentFee(amendmentFeeDto).getEntity();
    }

    @Override
    public AppSubmissionDto submitRequestChange(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationClient.saveAppsForRequestForChange(appSubmissionDto).getEntity();
        //eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitRenew(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationClient.saveAppsForRenew(appSubmissionDto).getEntity();
        return appSubmissionDto;
    }

    @Override
    public MsgTemplateDto getMsgTemplateById(String id) {
        MsgTemplateDto msgTemplateDto = systemAdminClient.getMsgTemplate(id).getEntity();
        return msgTemplateDto;
    }

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public void feSendEmail(EmailDto emailDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }
}
