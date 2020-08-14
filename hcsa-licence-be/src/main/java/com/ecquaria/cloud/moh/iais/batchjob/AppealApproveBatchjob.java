package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

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
    private EmailClient emailClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    public void doBatchJob(BaseProcessClass bpc) throws Exception {
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
                                  appealLicence(applicationDto,appealLicence,rollBackLicence,appealApproveDto.getLicenceDto(),appealApproveDto.getNewAppPremisesRecommendationDto());
                                  break;
//                        case ApplicationConsts.APPEAL_TYPE_OTHER :
//                            appealOther(appealApplicaiton,rollBackApplication,applicationDto);
//                            break;
                              default:break;
                          }
                        /*  appealOther(appealApplicaiton,rollBackApplication,applicationDto);*/
                      }
                  }
                  rollBackApplicationGroupDtos.add(applicationGroupDto);
                  ApplicationGroupDto appealApplicationGroupDto = (ApplicationGroupDto) CopyUtil.copyMutableObject(applicationGroupDto);
                  appealApplicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_LICENCE_GENERATED);
                  appealApplicationGroupDtos.add(appealApplicationGroupDto);
                  //event bus
                  AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
                  //licence
                  if(!IaisCommonUtils.isEmpty(appealLicence)){
                      AppealLicenceDto appealLicenceDto = new AppealLicenceDto();
                      String evenRefNum = String.valueOf(System.currentTimeMillis());
                      appealLicenceDto.setEventRefNo(evenRefNum);
                      appealLicenceDto.setAppealLicence(appealLicence);
                      appealLicenceDto.setRollBackLicence(rollBackLicence);
                      appealLicenceDto.setAuditTrailDto(auditTrailDto);
                      appealService.createAppealLicenceDto(appealLicenceDto);
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
            switch(appealReason){
                case ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION :
                    applicationRejection(appealApplicaiton,rollBackApplication,
                            appealAppPremisesRecommendationDtos,rollBackAppPremisesRecommendationDtos,appealApplicationGroupDtos,rollBackApplicationGroupDtos,
                            appealApproveDto);
                    break;
                case ApplicationConsts.APPEAL_REASON_APPLICATION_LATE_RENEW_FEE:
                    applicationLateRenewFee();
                    break;
                case ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO :
                    applicationAddCGO(appealApplicaiton,appealPersonnel,rollBackPersonnel,appealApproveDto,appealApplicationGroupDtos);
                    break;
                case ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME :
                    applicationChangeHciName(appealApplicaiton,appealAppGrpPremisesDto,rollBackAppGrpPremisesDto,appealApproveDto,appealApplicationGroupDtos);
                    break;
                default:break;
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
        AppPremisesRecommendationDto appPremisesRecommendationDto = appealApproveDto.getAppPremisesRecommendationDto();
        AppPremisesRecommendationDto newAppPremisesRecommendationDto = appealApproveDto.getNewAppPremisesRecommendationDto();
        if(applicationGroupDto!=null && appPremisesRecommendationDto !=null && newAppPremisesRecommendationDto!=null && appealApplicationDto!=null){
            String recomDecision = newAppPremisesRecommendationDto.getRecomDecision();
            if("approve".equals(recomDecision)){

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
           log.error(StringUtil.changeForLog("This Applicaiton  can not get the ApplicationGroupDto "+ appealApproveDto.getApplicationDto().getApplicationNo()));
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
    private void applicationLateRenewFee(){
     // do not need to do.
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

            ApplicationDto o = (ApplicationDto)CopyUtil.copyMutableObject(appealApplication);
            o.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            appealApplicaiton.add(o);
            ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(o.getAppGrpId()).getEntity();
            ApplicationGroupDto a=(ApplicationGroupDto)CopyUtil.copyMutableObject(applicationGroupDto);
            a.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
            appealApplicationGroupDtos.add(a);
        }

        if(appealDto != null){
            String relateRecId = appealDto.getRelateRecId();
            if(relateRecId!=null){
                ApplicationDto applicationDto = applicationClient.getApplicationById(relateRecId).getEntity();
                if(applicationDto!=null){
                    try {
                        sendEmailApproved(applicationDto,ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO,"",
                                "","" );
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }

                }

            }
        }

        log.info(StringUtil.changeForLog("The AppealApproveBatchjob applicationAddCGO is end ..."));
    }
    private void applicationChangeHciName(List<ApplicationDto> appealApplicaiton,List<AppGrpPremisesEntityDto> appealAppGrpPremisesDto,
                                          List<AppGrpPremisesEntityDto> rollBackAppGrpPremisesDto,
                                          AppealApproveDto appealApproveDto, List<ApplicationGroupDto> appealApplicationGroupDtos) throws Exception {
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob applicationChangeHciName is start ..."));
        AppPremiseMiscDto appealDto = appealApproveDto.getAppPremiseMiscDto();
        ApplicationDto appealApplication = appealApproveDto.getAppealApplicationDto();
        AppGrpPremisesEntityDto appGrpPremisesDto = appealApproveDto.getAppGrpPremisesEntityDto();
        if(appealDto!=null&&appGrpPremisesDto!=null){
            rollBackAppGrpPremisesDto.add(appGrpPremisesDto);
           String hciName = appealDto.getNewHciName();
            if(!StringUtil.isEmpty(hciName)){
               AppGrpPremisesEntityDto appGrpPremisesDto1 = (AppGrpPremisesEntityDto)
                       CopyUtil.copyMutableObject(appGrpPremisesDto);
               appGrpPremisesDto1.setHciName(hciName);
               appealAppGrpPremisesDto.add(appGrpPremisesDto1);
           }
        }
        ApplicationDto entity = applicationClient.getApplicationById(appealDto.getRelateRecId()).getEntity();
        ApplicationDto o = (ApplicationDto)CopyUtil.copyMutableObject(entity);
        o.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
        String appId = o.getId();
        LicAppCorrelationDto licAppCorrelationDto = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(appId).getEntity();
        String oldLicenceId = licAppCorrelationDto.getLicenceId();
        o.setOriginLicenceId(oldLicenceId);
        appealApplicaiton.add(o);
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(o.getAppGrpId()).getEntity();
        ApplicationGroupDto a=(ApplicationGroupDto)CopyUtil.copyMutableObject(applicationGroupDto);
        a.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
        appealApplicationGroupDtos.add(a);
        try {
            if(appealDto!=null){
                String relateRecId = appealDto.getRelateRecId();
                if(!StringUtil.isEmpty(relateRecId)){
                    ApplicationDto applicationDto = applicationClient.getApplicationById(relateRecId).getEntity();
                    if(applicationDto!=null){
                        sendEmailApproved(applicationDto,ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME,"","",appealDto.getNewHciName());
                    }
                }
            }
        }catch (Exception e){

            log.error(e.getMessage(),e);
        }

        log.info(StringUtil.changeForLog("The AppealApproveBatchjob applicationChangeHciName is end ..."));
    }
    private void appealLicence(ApplicationDto applicationDto,List<LicenceDto> appealLicence,
                               List<LicenceDto> rollBackLicence,
                               LicenceDto licenceDto,AppPremisesRecommendationDto appPremisesRecommendationDto) throws Exception {
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealLicence is start ..."));
        if(appPremisesRecommendationDto==null){
            return;
        }
        if(licenceDto!=null && appPremisesRecommendationDto != null){
            rollBackLicence.add(licenceDto);
            LicenceDto appealLicenceDto = (LicenceDto) CopyUtil.copyMutableObject(licenceDto);
            Date startDate = appealLicenceDto.getStartDate();
            Date expiryDate;
            try {
                expiryDate= LicenceUtil.getExpiryDate(startDate,appPremisesRecommendationDto);
            }catch (Exception e){
                expiryDate=new Date();
            }

            appealLicenceDto.setExpiryDate(expiryDate);
            appealLicence.add(appealLicenceDto);
            try {
                sendEmailApproved(applicationDto,ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD,"",expiryDate.toString(),"");
            }catch (Exception e){

            log.error(e.getMessage(),e);

            }
        }else{
            log.error(StringUtil.changeForLog("The AppealApproveBatchjob appealLicence licenceDto is null or newLicYears <0"));
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

        try {

            sendEmailApproved(applicationDto,ApplicationConsts.CESSATION_REASON_OTHER,"","","");

        }catch (Exception e){

            log.error(e.getMessage(),e);

        }
        log.info(StringUtil.changeForLog("The AppealApproveBatchjob appealOther is end ..."));
    }


    public void  sendEmailApproved(ApplicationDto application,String reason,String money,String date, String hciName) throws IOException, TemplateException {
        MsgTemplateDto entity = msgTemplateClient.getMsgTemplate("5B9EADD2-F27D-EA11-BE82-000C29F371DC").getEntity();
        if(entity!=null){
            Map<String,Object> map=IaisCommonUtils.genNewHashMap();
            map.put("applicationNumber",application.getApplicationNo());
            StringBuilder stringBuilder=new StringBuilder();
            if(ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION.equals(reason)){


            }else if(ApplicationConsts.APPEAL_REASON_APPLICATION_LATE_RENEW_FEE.equals(reason)){
                stringBuilder.append("For refund cases:  A refund of ").append(money).append(" has been credited back to your account.");

            }else if(ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reason)){

            }else if(ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD.equals(reason)){

                stringBuilder.append("For licence period adjustment: The licence period is now ").append(date);

            }else if(ApplicationConsts.APPEAL_REASON_OTHER.equals(reason)){


            }else if(ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME.equals(reason)){
                stringBuilder.append("For application change hci name : The hci name is now ").append(hciName);

            }
            map.put("reason",stringBuilder.toString());
            String messageContent = entity.getMessageContent();
            String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
            EmailDto emailDto=new EmailDto();
            emailDto.setClientQueryCode("Appeal approved");
            emailDto.setSender(mailSender);
            emailDto.setContent(templateMessageByContent);
            emailDto.setSubject("MOH IAIS – Appeal -"+application.getApplicationNo()+" is approved");
            String grpId = application.getAppGrpId();
            ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(grpId).getEntity();
            String licenseeId = applicationGroupDto.getLicenseeId();
            List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
            if(licenseeEmailAddrs!=null){
                emailDto.setReceipts(licenseeEmailAddrs);
                emailClient.sendNotification(emailDto);
            }
        }

    }

}
