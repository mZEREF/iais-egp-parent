package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * AppealApproveBatchjob
 *
 * @author suocheng
 * @date 2/6/2020
 */
@Delegator("appealApproveBatchjob")
@Slf4j
public class AppealApproveBatchjob {
    @Autowired
    private AppealService appealService;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    CessationClient cessationClient;
    @Autowired
    private EmailClient emailClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private AppEicClient appEicClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    public void doBatchJob(BaseProcessClass bpc) throws Exception {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        jobExecute();
    }
    public void jobExecute() throws Exception {

        log.info(StringUtil.changeForLog("The AppealApproveBatchjob is start ..."));
        List<AppealApproveGroupDto> appealApproveGroupDtos = appealService.getAppealApproveDtos();
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealApproveGroupDtos length is -->:"+appealApproveGroupDtos.size()));
        if(!IaisCommonUtils.isEmpty(appealApproveGroupDtos)){
          for (AppealApproveGroupDto appealApproveGroupDto :appealApproveGroupDtos ){
              ApplicationGroupDto applicationGroupDto = appealApproveGroupDto.getApplicationGroupDto();
              List<AppealApproveDto> appealApproveDtos = appealApproveGroupDto.getAppealApproveDtoList();
              if(!IaisCommonUtils.isEmpty(appealApproveDtos)&&applicationGroupDto!=null){
                  log.info(StringUtil.changeForLog("The AppealApproveBatchjob group no is -->"+applicationGroupDto.getGroupNo()));
                  log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealApproveDtos.size() is -->"+appealApproveDtos.size()));
                  List<ApplicationDto> appealApplicaiton = new ArrayList();
                  List<ApplicationDto> rollBackApplication = IaisCommonUtils.genNewArrayList();
                  List<LicenceDto> appealLicence = IaisCommonUtils.genNewArrayList();
                  List<AppPremiseMiscDto> appPremiseMiscDtoList=IaisCommonUtils.genNewArrayList();
                  List<LicenceDto> rollBackLicence = IaisCommonUtils.genNewArrayList();
                  List<AppSvcKeyPersonnelDto> appealPersonnel = IaisCommonUtils.genNewArrayList();
                  List<AppSvcKeyPersonnelDto> rollBackPersonnel = IaisCommonUtils.genNewArrayList();
                  List<AppGrpPremisesEntityDto> appealAppGrpPremisesDto = IaisCommonUtils.genNewArrayList();
                  List<AppGrpPremisesEntityDto> rollBackAppGrpPremisesDto = IaisCommonUtils.genNewArrayList();
                  List<AppPremisesRecommendationDto> appealAppPremisesRecommendationDtos = IaisCommonUtils.genNewArrayList();
                  List<AppPremisesRecommendationDto> rollBackAppPremisesRecommendationDtos = IaisCommonUtils.genNewArrayList();
                  List<ApplicationGroupDto> appealApplicationGroupDtos = IaisCommonUtils.genNewArrayList();
                  List<ApplicationGroupDto> rollBackApplicationGroupDtos = IaisCommonUtils.genNewArrayList();
                  for (AppealApproveDto appealApproveDto: appealApproveDtos){
                      ApplicationDto applicationDto = appealApproveDto.getApplicationDto();
                      AppPremiseMiscDto appealDto = appealApproveDto.getAppPremiseMiscDto();
                      if(applicationDto!= null && appealDto != null){
                          log.info(StringUtil.changeForLog("The AppealApproveBatchjob applicationDto no is -->"+applicationDto.getApplicationNo()));
                          String  appealType = appealDto.getAppealType();
                          log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealType  is -->"+appealType));
                          switch(appealType){
                              case ApplicationConsts.APPEAL_TYPE_APPLICAITON :
                                  appealApplicaiton(appealApplicaiton,rollBackApplication,appealPersonnel,rollBackPersonnel,
                                          appealAppGrpPremisesDto,rollBackAppGrpPremisesDto,
                                          appealAppPremisesRecommendationDtos,rollBackAppPremisesRecommendationDtos,appealApplicationGroupDtos,rollBackApplicationGroupDtos,
                                          appealApproveDto);
                                  break;
                              case ApplicationConsts.APPEAL_TYPE_LICENCE :
                                  appealLicence(appPremiseMiscDtoList,appealDto,appealLicence,rollBackLicence,appealApproveDto.getLicenceDto(),appealApproveDto.getNewAppPremisesRecommendationDto(),appealDto.getReason());
                                  break;
//                        case ApplicationConsts.APPEAL_TYPE_OTHER :
//                            appealOther(appealApplicaiton,rollBackApplication,applicationDto);
//                            break;
                              default:break;
                          }
                          try {
                              String reason = appealApproveDto.getAppPremiseMiscDto().getReason();
                              sendAllEmailApproved(applicationDto,reason,appealApproveDto.getLicenceDto(),appealApproveDto.getNewAppPremisesRecommendationDto(),appealDto);
                          }catch (Exception e){
                              log.info(e.getMessage(),e);
                          }

                          /*  appealOther(appealApplicaiton,rollBackApplication,applicationDto);*/
                      }
                  }
                  rollBackApplicationGroupDtos.add(applicationGroupDto);
                  ApplicationGroupDto appealApplicationGroupDto = (ApplicationGroupDto) CopyUtil.copyMutableObject(applicationGroupDto);
                  appealApplicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_LICENCE_GENERATED);
                  appealApplicationGroupDtos.add(appealApplicationGroupDto);
                  //event bus
                  AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
                  //licence
                  if(!IaisCommonUtils.isEmpty(appealLicence)){
                      AppealLicenceDto appealLicenceDto = new AppealLicenceDto();
                      String evenRefNum = String.valueOf(System.currentTimeMillis());
                      appealLicenceDto.setEventRefNo(evenRefNum);
                      appealLicenceDto.setAppealLicence(appealLicence);
                      appealLicenceDto.setRollBackLicence(rollBackLicence);
                      appealLicenceDto.setAuditTrailDto(auditTrailDto);
                      appealLicenceDto.setAppPremiseMiscDto(appPremiseMiscDtoList);
                      appealService.createAppealLicenceDto(appealLicenceDto);
                      appealService.updateAppPremiseMisc(appPremiseMiscDtoList);
                  }else{
                      log.info(StringUtil.changeForLog("The appealLicence is Empty."));
                  }

                  //application
                  AppealApplicationDto appealApplicationDto = new  AppealApplicationDto();
                  String eventRefNo = String.valueOf(System.currentTimeMillis());
                  appealApplicationDto.setEventRefNo(eventRefNo);
                  appealApplicationDto.setApplicationDtos(appealApplicaiton);
                  appealApplicationDto.setRollBackApplicationDto(rollBackApplication);
                  appealApplicationDto.setAppealPersonnel(appealPersonnel);
                  appealApplicationDto.setRollBackPersonnel(rollBackPersonnel);
                  appealApplicationDto.setAppealAppGrpPremisesDto(appealAppGrpPremisesDto);
                  appealApplicationDto.setRollBackAppGrpPremisesDto(rollBackAppGrpPremisesDto);
                  appealApplicationDto.setAppealApplicationGroupDtos(appealApplicationGroupDtos);
                  appealApplicationDto.setRollBackApplicationGroupDtos(rollBackApplicationGroupDtos);
                  appealApplicationDto.setAppealAppPremisesRecommendationDtos(appealAppPremisesRecommendationDtos);
                  appealApplicationDto.setRollBackAppPremisesRecommendationDtos(rollBackAppPremisesRecommendationDtos);
                  appealService.createAppealApplicationDto(appealApplicationDto);
              }
          }
        }else{
            log.info(StringUtil.changeForLog("appealApproveGroupDtos is empty"));
        }
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob is end ..."));
    }

    private void appealApplicaiton( List<ApplicationDto> appealApplicaiton,
                                   List<ApplicationDto> rollBackApplication,
                                   List<AppSvcKeyPersonnelDto> appealPersonnel,
                                   List<AppSvcKeyPersonnelDto> rollBackPersonnel,
                                   List<AppGrpPremisesEntityDto> appealAppGrpPremisesDto,
                                   List<AppGrpPremisesEntityDto> rollBackAppGrpPremisesDto,
                                   List<AppPremisesRecommendationDto> appealAppPremisesRecommendationDtos,
                                   List<AppPremisesRecommendationDto> rollBackAppPremisesRecommendationDtos,
                                   List<ApplicationGroupDto> appealApplicationGroupDtos,
                                   List<ApplicationGroupDto> rollBackApplicationGroupDtos,
                                   AppealApproveDto appealApproveDto) throws Exception {
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealApplicaiton is start ..."));
        ApplicationDto applicationDto = appealApproveDto.getApplicationDto();
        AppPremiseMiscDto appealDto = appealApproveDto.getAppPremiseMiscDto();
        if(applicationDto!= null && appealDto != null){
            String appealReason = appealDto.getReason();
            try {
                switch(appealReason){
                    case ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION :
                        applicationRejection(appealApplicaiton,rollBackApplication,
                                appealAppPremisesRecommendationDtos,rollBackAppPremisesRecommendationDtos,appealApplicationGroupDtos,rollBackApplicationGroupDtos,
                                appealApproveDto);
                        break;
                    case ApplicationConsts.APPEAL_REASON_APPLICATION_LATE_RENEW_FEE:
//                    applicationLateRenewFee(applicationDto);
                        break;
                    case ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO :
                        applicationAddCGO(appealApplicaiton,appealPersonnel,rollBackPersonnel,appealApproveDto,appealApplicationGroupDtos);
                        break;
                    case ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME :
                        applicationChangeHciName(appealApplicaiton,appealAppGrpPremisesDto,rollBackAppGrpPremisesDto,appealApproveDto,appealApplicationGroupDtos);
                        break;
                    default:break;
                }
            }catch (Throwable e){
                log.error(e.getMessage(),e);
            }

        }
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealApplicaiton is end ..."));
    }
    private void applicationRejection(List<ApplicationDto> appealApplicaiton,
                                      List<ApplicationDto> rollBackApplication,
                                      List<AppPremisesRecommendationDto> appealAppPremisesRecommendationDtos,
                                      List<AppPremisesRecommendationDto> rollBackAppPremisesRecommendationDtos,
                                      List<ApplicationGroupDto> appealApplicationGroupDtos,
                                      List<ApplicationGroupDto> rollBackApplicationGroupDtos,
                                      AppealApproveDto appealApproveDto) throws Exception {
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob applicationRejection is start ..."));
        ApplicationGroupDto applicationGroupDto = appealApproveDto.getAppealApplicationGroupDto();
        ApplicationDto appealApplicationDto = appealApproveDto.getAppealApplicationDto();

        AppPremiseMiscDto appealDto = appealApproveDto.getAppPremiseMiscDto();
        String relateRecId = appealDto.getRelateRecId();
        Integer recomInNumber = null;
        String chronoUnit = null;
        ApplicationDto oldApplication = applicationClient.getApplicationById(relateRecId).getEntity();
        if(oldApplication != null){
            String serviceId = oldApplication.getServiceId();
            HcsaServiceDto serviceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
            if(serviceDto != null){
                RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
                riskAcceptiionDto.setScvCode(serviceDto.getSvcCode());
                riskAcceptiionDto.setRiskScore(5.0);
                List<RiskAcceptiionDto> listRiskAcceptiionDto = IaisCommonUtils.genNewArrayList();
                listRiskAcceptiionDto.add(riskAcceptiionDto);
                List<RiskResultDto> listRiskResult = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
                if(!IaisCommonUtils.isEmpty(listRiskResult)){
                    RiskResultDto riskResultDto = listRiskResult.get(listRiskResult.size()-1);
                    String dateType = riskResultDto.getDateType();
                    Integer timeCount = riskResultDto.getTimeCount();
                    if(!StringUtil.isEmpty(dateType)){
                        chronoUnit = dateType;
                    }
                    recomInNumber = timeCount;
                }
            }
        }

        AppPremisesRecommendationDto appPremisesRecommendationDto = appealApproveDto.getAppPremisesRecommendationDto();
        if(appPremisesRecommendationDto != null){
            if(!StringUtil.isEmpty(chronoUnit)){
                appPremisesRecommendationDto.setChronoUnit(chronoUnit);
            }
            if(recomInNumber != null){
                appPremisesRecommendationDto.setRecomInNumber(recomInNumber);
            }
        }
        AppPremisesRecommendationDto newAppPremisesRecommendationDto = appealApproveDto.getNewAppPremisesRecommendationDto();
        if(applicationGroupDto!=null && appPremisesRecommendationDto !=null && newAppPremisesRecommendationDto!=null && appealApplicationDto!=null){
            String recomDecision = newAppPremisesRecommendationDto.getRecomDecision();
            if("approve".equals(recomDecision) || InspectionReportConstants.RFC_APPROVED.equals(recomDecision) || InspectionReportConstants.APPROVED.equals(recomDecision)){

                    rollBackApplication.add(appealApplicationDto);
                    ApplicationDto newAppealApplicaitonDto = (ApplicationDto) CopyUtil.copyMutableObject(appealApplicationDto);
                    newAppealApplicaitonDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                    appealApplicaiton.add(newAppealApplicaitonDto);

                    rollBackApplicationGroupDtos.add(applicationGroupDto);
                    ApplicationGroupDto newAppealApplicationGroupDto = (ApplicationGroupDto) CopyUtil.copyMutableObject(applicationGroupDto);
                    newAppealApplicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                    appealApplicationGroupDtos.add(newAppealApplicationGroupDto);

                rollBackAppPremisesRecommendationDtos.add(appPremisesRecommendationDto);
                AppPremisesRecommendationDto appwalAppPremisesRecommendationDto = (AppPremisesRecommendationDto) CopyUtil.copyMutableObject(appPremisesRecommendationDto);
                appwalAppPremisesRecommendationDto.setRecomInNumber(appPremisesRecommendationDto.getRecomInNumber());
                appwalAppPremisesRecommendationDto.setChronoUnit(appPremisesRecommendationDto.getChronoUnit());
                appealAppPremisesRecommendationDtos.add(appwalAppPremisesRecommendationDto);
            }

        }else{
           log.debug(StringUtil.changeForLog("This Applicaiton  can not get the ApplicationGroupDto "+ appealApproveDto.getApplicationDto().getApplicationNo()));
        }
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        for(ApplicationDto applicationDto : appealApplicaiton){
            beEicGatewayClient.updateApplication(applicationDto,signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
            applicationClient.updateApplication(applicationDto);
        }
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob applicationRejection is end ..."));
    }
    private void applicationLateRenewFee(ApplicationDto application){
        log.debug(StringUtil.changeForLog("send applicationLateRenewFee email start"));
        // send return fee email
        EmailDto emailDto=new EmailDto();
        emailDto.setClientQueryCode("Appeal approved");
        emailDto.setSender(mailSender);
        emailDto.setContent("appeal return fee email");
        emailDto.setSubject("MOH HALP – Appeal - return fee");
        String grpId = application.getAppGrpId();
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(grpId).getEntity();
        String licenseeId = applicationGroupDto.getLicenseeId();
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        if(licenseeEmailAddrs!=null){
            emailDto.setReceipts(licenseeEmailAddrs);
            emailClient.sendNotification(emailDto);
        }else{
            log.debug(StringUtil.changeForLog("licenseeEmailAddrs is none"));
        }
        log.debug(StringUtil.changeForLog("send applicationLateRenewFee email end"));
    }
    private void applicationAddCGO(List<ApplicationDto> appealApplicaiton,List<AppSvcKeyPersonnelDto> appealPersonnel,
                                   List<AppSvcKeyPersonnelDto> rollBackPersonnel,
                                   AppealApproveDto appealApproveDto, List<ApplicationGroupDto> appealApplicationGroupDtos) throws Exception {
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob applicationAddCGO is start ..."));
        AppPremiseMiscDto appealDto = appealApproveDto.getAppPremiseMiscDto();
        ApplicationDto appealApplication = appealApproveDto.getAppealApplicationDto();
        if(appealDto!=null&&appealApplication!=null){
            String  appealApplicaitonId = appealApplication.getId();
            List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = appealApproveDto.getAppSvcKeyPersonnelDtos();
            if(!IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)){
                for (AppSvcKeyPersonnelDto appSvcKeyPersonnelDto : appSvcKeyPersonnelDtos){
                    rollBackPersonnel.add(appSvcKeyPersonnelDto);
                    AppSvcKeyPersonnelDto appealAppSvcKeyPersonnelDto = (AppSvcKeyPersonnelDto) CopyUtil.copyMutableObject(appSvcKeyPersonnelDto);
                    appealAppSvcKeyPersonnelDto.setApplicationId(appealApplicaitonId);
                    appealPersonnel.add(appealAppSvcKeyPersonnelDto);
                }
            }

            ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appealApplication.getAppGrpId()).getEntity();
            ApplicationGroupDto a=(ApplicationGroupDto)CopyUtil.copyMutableObject(applicationGroupDto);
            a.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
            if(appealApplication.isGrpLic()){
                List<ApplicationDto> entity = applicationClient.getGroupAppsByNo(applicationGroupDto.getId()).getEntity();
                for(ApplicationDto applicationDto : entity ){
                    if(!IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)){
                        for (AppSvcKeyPersonnelDto appSvcKeyPersonnelDto : appSvcKeyPersonnelDtos){
                            rollBackPersonnel.add(appSvcKeyPersonnelDto);
                            AppSvcKeyPersonnelDto appealAppSvcKeyPersonnelDto = (AppSvcKeyPersonnelDto) CopyUtil.copyMutableObject(appSvcKeyPersonnelDto);
                            appealAppSvcKeyPersonnelDto.setApplicationId(applicationDto.getId());
                            appealPersonnel.add(appealAppSvcKeyPersonnelDto);
                        }
                    }
                    ApplicationDto o = (ApplicationDto)CopyUtil.copyMutableObject(applicationDto);
                    LicAppCorrelationDto licAppCorrelationDto = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(o.getId()).getEntity();
                    o.setNeedNewLicNo(false);
                    if(licAppCorrelationDto!=null){
                        o.setOriginLicenceId(licAppCorrelationDto.getLicenceId());
                    }
                    o.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                    appealApplicaiton.add(o);
                }
            }else {
                ApplicationDto o = (ApplicationDto)CopyUtil.copyMutableObject(appealApplication);
                LicAppCorrelationDto licAppCorrelationDto = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(o.getId()).getEntity();
                //not need new licence no
                o.setNeedNewLicNo(false);
                if(licAppCorrelationDto!=null){
                    o.setOriginLicenceId(licAppCorrelationDto.getLicenceId());
                }
                o.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                appealApplicaiton.add(o);
            }
            appealApplicationGroupDtos.add(a);
        }

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        beEicGatewayClient.updateAppSvcKeyPersonnelDto(appealPersonnel,signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob applicationAddCGO is end ..."));
    }
    //sync hciName
    public void applicationChangeHciName(List<ApplicationDto> appealApplicaiton,List<AppGrpPremisesEntityDto> appealAppGrpPremisesDto,
                                          List<AppGrpPremisesEntityDto> rollBackAppGrpPremisesDto,
                                          AppealApproveDto appealApproveDto, List<ApplicationGroupDto> appealApplicationGroupDtos) throws Exception {
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob applicationChangeHciName is start ..."));
        AuditTrailDto intranet = AuditTrailHelper.getCurrentAuditTrailDto();
        AppPremiseMiscDto appealDto = appealApproveDto.getAppPremiseMiscDto();
        AppGrpPremisesEntityDto appGrpPremisesDto = appealApproveDto.getAppGrpPremisesEntityDto();
        if(appealDto!=null&&appGrpPremisesDto!=null){
            rollBackAppGrpPremisesDto.add(appGrpPremisesDto);
            String hciName = appealDto.getNewHciName();
            if(!StringUtil.isEmpty(hciName)){
                AppGrpPremisesEntityDto appGrpPremisesDto1 = (AppGrpPremisesEntityDto)
                       CopyUtil.copyMutableObject(appGrpPremisesDto);
                appGrpPremisesDto1.setHciName(hciName);
                appGrpPremisesDto1.setAuditTrailDto(intranet);
                appealAppGrpPremisesDto.add(appGrpPremisesDto1);
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                //save eic record
                EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.batchjob.AppealApproveBatchjob", "applicationChangeHciName",
                        "hcsa-licence-web-intranet", List.class.getName(), JsonUtil.parseToJson(appealAppGrpPremisesDto));
                String eicRefNo = eicRequestTrackingDto.getRefNo();
                beEicGatewayClient.saveHciNameByDto(appealAppGrpPremisesDto, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization());
                //get eic record
                eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
                //update eic record status
                eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
                eicRequestTrackingDtos.add(eicRequestTrackingDto);
                appEicClient.updateStatus(eicRequestTrackingDtos);
           }
        }
        if (appealDto == null) {
            throw new IaisRuntimeException("appeal dto is null");
        }
        ApplicationDto entity = applicationClient.getApplicationById(appealDto.getRelateRecId()).getEntity();
        String appGrpId = entity.getAppGrpId();
        List<ApplicationDto> applicationDtos = otherChangeHciNameApp(appGrpId,entity);
        ApplicationDto o = (ApplicationDto)CopyUtil.copyMutableObject(entity);
        o.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
        String appId = o.getId();
        LicAppCorrelationDto licAppCorrelationDto = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(appId).getEntity();
        if(licAppCorrelationDto!=null){
            String oldLicenceId = licAppCorrelationDto.getLicenceId();
            o.setNeedNewLicNo(false);
            o.setOriginLicenceId(oldLicenceId);
            appealApplicaiton.add(o);
            addOtherChangeHciNameApp(applicationDtos,appealApplicaiton);
            ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(o.getAppGrpId()).getEntity();
            ApplicationGroupDto a=(ApplicationGroupDto)CopyUtil.copyMutableObject(applicationGroupDto);
            a.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
            appealApplicationGroupDtos.add(a);
        }else {
            //if licence no generate to do
           /* ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(o.getAppGrpId()).getEntity();
            if(ApplicationConsts.APPLICATION_GROUP_STATUS_GET_DATA.equals(applicationGroupDto.getStatus())){
                return;
            }
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
            if(appPremisesCorrelationDto!=null){
                AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationClient.getAppGrpPremise(appPremisesCorrelationDto.getAppGrpPremId()).getEntity();
                appGrpPremisesEntityDto.setHciName(appealDto.getNewHciName());

            }*/
        }
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob applicationChangeHciName is end ..."));
    }

    private void addOtherChangeHciNameApp( List<ApplicationDto> applicationDtos,List<ApplicationDto> appealApplicaiton){
        applicationDtos.forEach((v)->{
            if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(v.getStatus())){
                try {
                    ApplicationDto c=(ApplicationDto)CopyUtil.copyMutableObject(v);
                    appealApplicaiton.add(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(v.getStatus())){
                LicAppCorrelationDto entity1 = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(v.getId()).getEntity();
                try {
                    ApplicationDto c=(ApplicationDto)CopyUtil.copyMutableObject(v);
                    c.setNeedNewLicNo(false);
                    c.setOriginLicenceId(entity1.getLicenceId());
                    c.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                    appealApplicaiton.add(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }
    private List<ApplicationDto> otherChangeHciNameApp(String appGrpId,ApplicationDto applicationDto){
        List<ApplicationDto> applicationDtoList = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
        ListIterator<ApplicationDto> iterator = applicationDtoList.listIterator();
        while (iterator.hasNext()){
            ApplicationDto next = iterator.next();
            if(ApplicationConsts.APPLICATION_STATUS_DELETED.equals(next.getStatus())||applicationDto.getApplicationNo().equals(next.getApplicationNo())){
                iterator.remove();
            }
        }
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationDto.getApplicationType())){
            while (iterator.hasNext()){
                ApplicationDto next = iterator.next();
                if(applicationDto.getServiceId().equals(next.getServiceId())){
                    iterator.remove();
                }
            }
        }else {
            AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationClient.getPremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
            String hciCode1 = appGrpPremisesEntityDto.getHciCode();

            while (iterator.hasNext()){
                ApplicationDto next = iterator.next();
                AppGrpPremisesEntityDto entity = applicationClient.getPremisesByAppNo(next.getApplicationNo()).getEntity();
                String hciCode = entity.getHciCode();
                if(!hciCode.equals(hciCode1)){
                    iterator.remove();
                }
            }
        }

        return applicationDtoList;
    }
    private void appealLicence(List<AppPremiseMiscDto>appPremiseMiscDtoList,AppPremiseMiscDto appPremiseMiscDto,List<LicenceDto> appealLicence,
                               List<LicenceDto> rollBackLicence,
                               LicenceDto licenceDto,AppPremisesRecommendationDto appPremisesRecommendationDto,String reason) throws Exception {
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealLicence is start ..."));
        if(ApplicationConsts.APPEAL_REASON_OTHER.equals(reason)){
            return;
        }
        if(appPremisesRecommendationDto==null){
            return;
        }
        if(licenceDto!=null && appPremisesRecommendationDto != null){
            if("reject".equals(appPremisesRecommendationDto.getRecomDecision())){
                return;
            }
            rollBackLicence.add(licenceDto);
            LicenceDto appealLicenceDto = (LicenceDto) CopyUtil.copyMutableObject(licenceDto);
            Date startDate = appealLicenceDto.getStartDate();
            Date expiryDate;
            try {
                expiryDate = LicenceUtil.getExpiryDate(startDate,appPremisesRecommendationDto);
            }catch (Exception e){
                expiryDate = new Date();
            }
            try {
                //get old recommendation
                List<LicAppCorrelationDto> licAppCorrelationDtos = hcsaLicenceClient.getLicCorrBylicId(licenceDto.getId()).getEntity();
                if(!IaisCommonUtils.isEmpty(licAppCorrelationDtos)) {
                    log.debug(StringUtil.changeForLog("licAppCorrelationDtos is Not null=======> Licence Id = " + licenceDto.getId()));
                    for (LicAppCorrelationDto licAppCorrelationDto : licAppCorrelationDtos) {
                        String appId = licAppCorrelationDto.getApplicationId();
                        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
                        String appPremCorreId = appPremisesCorrelationDto.getId();
                        AppPremisesRecommendationDto oldAppPremRecommDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorreId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
                        if(oldAppPremRecommDto == null){
                            continue;
                        }
                        oldAppPremRecommDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                        fillUpCheckListGetAppClient.updateAppRecom(oldAppPremRecommDto);
                        AppPremisesRecommendationDto newAppPremRecomDto = new AppPremisesRecommendationDto();
                        int newVersion = oldAppPremRecommDto.getVersion() + 1;
                        newAppPremRecomDto.setId(null);
                        newAppPremRecomDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        newAppPremRecomDto.setRemarks(appPremisesRecommendationDto.getRemarks());
                        newAppPremRecomDto.setRecomInDate(appPremisesRecommendationDto.getRecomInDate());
                        newAppPremRecomDto.setRecomDecision(appPremisesRecommendationDto.getRecomDecision());
                        newAppPremRecomDto.setAppPremCorreId(appPremCorreId);
                        newAppPremRecomDto.setRecomType(appPremisesRecommendationDto.getRecomType());
                        newAppPremRecomDto.setRecomInNumber(appPremisesRecommendationDto.getRecomInNumber());
                        newAppPremRecomDto.setChronoUnit(appPremisesRecommendationDto.getChronoUnit());
                        newAppPremRecomDto.setVersion(newVersion);
                        fillUpCheckListGetAppClient.saveAppRecom(newAppPremRecomDto);
                    }
                } else {
                    log.debug(StringUtil.changeForLog(""));
                }
                appealLicenceDto.setExpiryDate(expiryDate);
                appPremiseMiscDtoList.add(appPremiseMiscDto);
                appealLicence.add(appealLicenceDto);
            }catch (Throwable e){
                log.error(e.getMessage(),e);
            }
        } else {
            log.debug(StringUtil.changeForLog("licAppCorrelationDtos is null=======> Licence Id = "));
        }
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealLicence is end ..."));
    }
    private void appealOther(List<ApplicationDto> appealApplicaiton,
                             List<ApplicationDto> rollBackApplication,
                             ApplicationDto applicationDto) throws Exception {
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealOther is start ..."));
        if(applicationDto!=null){
            rollBackApplication.add(applicationDto);
            ApplicationDto appealApplicaitonDto = (ApplicationDto) CopyUtil.copyMutableObject(applicationDto);
            appealApplicaitonDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPEAL_ACTIVE);
            appealApplicaiton.add(appealApplicaitonDto);
        }

        log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealOther is end ..."));
    }

    public void  sendAllEmailApproved(ApplicationDto applicationDto,String reason,LicenceDto licenceDto,AppPremisesRecommendationDto appPremisesRecommendationDto,AppPremiseMiscDto appPremiseMiscDto) throws IOException, TemplateException {
        String paymentMethodName = "";
        String paymentMode = "";
        log.info("start send email sms and msg");
        String relateRecId = appPremiseMiscDto.getRelateRecId();
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();

        List<AppPremiseMiscDto> premiseMiscDtoList = cessationClient.getAppPremiseMiscDtoListByAppId(applicationDto.getId()).getEntity();
        String appType = "Licence";
        String subNo="";
        ApplicationDto oldApplication = new ApplicationDto();
        if(premiseMiscDtoList != null){
            AppPremiseMiscDto premiseMiscDto = premiseMiscDtoList.get(0);
            String oldAppId = premiseMiscDto.getRelateRecId();
            oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
            if(oldApplication!=null){
                String applicationType = oldApplication.getApplicationType();
                if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
                    //new
                    appType ="New Licence Application";
                }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
                    //renew
                    appType ="Renewal";
                } else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
                    //RFC
                    appType ="Request For Change";
                }
                subNo=oldApplication.getApplicationNo();
            }else {
                if(licenceDto!=null){
                    subNo= licenceDto.getLicenceNo();
                }
            }

        }
        if(StringUtil.isEmpty(appType)){
            appType = "Licence";
        }


        if(ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION.equals(reason)){

        }else if(ApplicationConsts.APPEAL_REASON_APPLICATION_LATE_RENEW_FEE.equals(reason)){
            //return fee
            ApplicationDto entity = applicationClient.getApplicationById(relateRecId).getEntity();
            ApplicationGroupDto entity1 = applicationClient.getAppById(entity.getAppGrpId()).getEntity();
            if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT .equals(entity1.getPayMethod())) {
                paymentMethodName = "onlinePayment";
                paymentMode = MasterCodeUtil.getCodeDesc(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT);
            }else if(ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(entity1.getPayMethod())){
                paymentMethodName = "onlinePayment";
                paymentMode = ApplicationConsts.PAYMENT_METHOD_NAME_NETS;
            }
            else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(entity1.getPayMethod())) {
                paymentMethodName = MasterCodeUtil.getCodeDesc(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO);
                paymentMode = ApplicationConsts.PAYMENT_METHOD_NAME_GIRO;
            }
        }else if(ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reason)){

        }else if(ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME.equals(reason)){

        }else if(ApplicationConsts.APPEAL_REASON_OTHER.equals(reason)){
            //ohter
            paymentMethodName = "other";
        }else if(ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD.equals(reason)){
        //licence
            paymentMethodName = "applicable";
        }else{
            paymentMethodName = "other";
        }
        log.info(StringUtil.changeForLog("paymentMethodName :" +paymentMethodName));
        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
        templateContent.put("ApplicantName", orgUserDto.getDisplayName());
        templateContent.put("ApplicationType",  appType);
        templateContent.put("ApplicationNo", subNo);
        templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date(),"dd/MM/yyyy"));
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        templateContent.put("newSystem", loginUrl);

        templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
        templateContent.put("paymentMethod", paymentMethodName);
        if("onlinePayment".equals(paymentMethodName)){
            if(!StringUtil.isEmpty(oldApplication.getApplicationNo())){
                AppReturnFeeDto appReturnFeeDto = applicationClient.getReturnFeeByAppNo(oldApplication.getApplicationNo(),ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_APPEAL).getEntity();
                templateContent.put("returnAmount", Formatter.formatterMoney(appReturnFeeDto.getReturnAmount()));
            }else{
                templateContent.put("returnAmount", "$0");
            }
            templateContent.put("paymentMode", paymentMode);
            templateContent.put("adminFee", "$100");

        } else if("GIRO".equals(paymentMethodName)){
            if(!StringUtil.isEmpty(oldApplication.getApplicationNo())){
                AppReturnFeeDto appReturnFeeDto = applicationClient.getReturnFeeByAppNo(oldApplication.getApplicationNo(),ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_APPEAL).getEntity();
                templateContent.put("returnAmount", Formatter.formatterMoney(appReturnFeeDto.getReturnAmount()));
            }else{
                templateContent.put("returnAmount", "$0");
            }
            templateContent.put("adminFee", "$100");
        }else if("applicable".equals(paymentMethodName)){
            Date expiryDate;
            try {
                LicenceDto appealLicenceDto = (LicenceDto) CopyUtil.copyMutableObject(licenceDto);
                Date startDate = appealLicenceDto.getStartDate();
                expiryDate = LicenceUtil.getExpiryDate(startDate,appPremisesRecommendationDto);
            }catch (Exception e){
                expiryDate = new Date();
            }
            templateContent.put("serviceName", licenceDto.getSvcName());
            templateContent.put("licenceNo", licenceDto.getLicenceNo());
            templateContent.put("licenceEndDate", Formatter.formatDate(licenceDto.getExpiryDate()));
            templateContent.put("newEndDate", Formatter.formatDate(expiryDate));
        }else{
//            templateContent.put("content", appPremiseMiscDto.getOtherReason());
        }

        log.info(StringUtil.changeForLog("templateContent :" +JsonUtil.parseToJson(templateContent)));

        MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_APPROVE_EMAIL).getEntity();
        MsgTemplateDto smsTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_APPROVE_SMS).getEntity();
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_APPROVE_MSG).getEntity();
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationType", appType);
        subMap.put("ApplicationNo", subNo);
        String emailSubject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
        String smsSubject = MsgUtil.getTemplateMessageByContent(smsTemplateDto.getTemplateName(),subMap);
        String msgSubject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),subMap);


        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_APPROVE_EMAIL);
        emailParam.setTemplateContent(templateContent);
        emailParam.setSubject(emailSubject);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        notificationHelper.sendNotification(emailParam);
        log.info("appeal approve email");

        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_APPROVE_SMS);
        smsParam.setSubject(smsSubject);
        smsParam.setTemplateContent(templateContent);
        smsParam.setQueryCode(applicationDto.getApplicationNo());
        smsParam.setReqRefNum(applicationDto.getApplicationNo());
        smsParam.setRefId(applicationDto.getApplicationNo());
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        notificationHelper.sendNotification(smsParam);
        log.info("appeal approve sms");

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_APPROVE_MSG);
        msgParam.setTemplateContent(templateContent);
        msgParam.setSubject(msgSubject);
        msgParam.setQueryCode(applicationDto.getApplicationNo());
        msgParam.setReqRefNum(applicationDto.getApplicationNo());
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
        svcCodeList.add(svcDto.getSvcCode());
        msgParam.setSvcCodeList(svcCodeList);
        msgParam.setRefId(applicationDto.getApplicationNo());
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        notificationHelper.sendNotification(msgParam);
        log.info("appeal approve msg");



    }

}
