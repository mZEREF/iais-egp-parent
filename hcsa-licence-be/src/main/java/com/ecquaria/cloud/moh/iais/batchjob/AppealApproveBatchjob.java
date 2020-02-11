package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void doBatchJob(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("The AppealApproveBatchjob is start ..."));
        List<AppealApproveGroupDto> appealApproveGroupDtos = appealService.getAppealApproveDtos();
        if(!IaisCommonUtils.isEmpty(appealApproveGroupDtos)){
          for (AppealApproveGroupDto appealApproveGroupDto :appealApproveGroupDtos ){
              ApplicationGroupDto applicationGroupDto = appealApproveGroupDto.getApplicationGroupDto();
              List<AppealApproveDto> appealApproveDtos = appealApproveGroupDto.getAppealApproveDtoList();
              if(!IaisCommonUtils.isEmpty(appealApproveDtos)&&applicationGroupDto!=null){
                  List<ApplicationDto> appealApplicaiton = new ArrayList();
                  List<ApplicationDto> rollBackApplication = new ArrayList<>();
                  List<LicenceDto> appealLicence = new ArrayList<>();
                  List<LicenceDto> rollBackLicence = new ArrayList<>();
                  List<AppSvcKeyPersonnelDto> appealPersonnel = new ArrayList<>();
                  List<AppSvcKeyPersonnelDto> rollBackPersonnel = new ArrayList<>();
                  List<AppGrpPremisesEntityDto> appealAppGrpPremisesDto = new ArrayList<>();
                  List<AppGrpPremisesEntityDto> rollBackAppGrpPremisesDto = new ArrayList<>();
                  for (AppealApproveDto appealApproveDto: appealApproveDtos){
                      ApplicationDto applicationDto = appealApproveDto.getApplicationDto();
                      AppealDto appealDto = appealApproveDto.getAppealDto();
                      if(applicationDto!= null && appealDto != null){
                          String  appealType = appealDto.getAppealType();
                          switch(appealType){
                              case ApplicationConsts.APPEAL_TYPE_APPLICAITON :
                                  appealApplicaiton(appealApplicaiton,rollBackApplication,appealPersonnel,rollBackPersonnel,
                                          appealAppGrpPremisesDto,rollBackAppGrpPremisesDto,appealApproveDto);
                                  appealOther(appealApplicaiton,rollBackApplication,applicationDto);
                                  break;
                              case ApplicationConsts.APPEAL_TYPE_LICENCE :
                                  appealLicence(appealLicence,rollBackLicence,appealApproveDto.getLicenceDto(),
                                          appealDto.getNewLicYears());
                                  appealOther(appealApplicaiton,rollBackApplication,applicationDto);
                                  break;
//                        case ApplicationConsts.APPEAL_TYPE_OTHER :
//                            appealOther(appealApplicaiton,rollBackApplication,applicationDto);
//                            break;
                          }
                      }
                  }
                  //event bus
                  AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
                  String eventRefNo = EventBusHelper.getEventRefNo();
                  //licence
                  AppealLicenceDto appealLicenceDto = new AppealLicenceDto();
                  appealLicenceDto.setEventRefNo(eventRefNo);
                  appealLicenceDto.setAppealLicence(appealLicence);
                  appealLicenceDto.setRollBackLicence(rollBackLicence);
                  appealLicenceDto.setAuditTrailDto(auditTrailDto);
                  appealService.createAppealLicenceDto(appealLicenceDto);
                  //application
                  AppealApplicationDto appealApplicationDto = new  AppealApplicationDto();
                  appealApplicationDto.setEventRefNo(eventRefNo);
                  appealApplicationDto.setRollBackApplicationGroupDto(applicationGroupDto);
                  ApplicationGroupDto appealApplicationGroupDto = (ApplicationGroupDto) CopyUtil.copyMutableObject(applicationGroupDto);
                  appealApplicationDto.setAppealApplicationGroupDto(appealApplicationGroupDto);
                  appealApplicationDto.setAppealApplicationDto(appealApplicaiton);
                  appealApplicationDto.setRollBackApplicationDto(rollBackApplication);
                  appealApplicationDto.setAppealPersonnel(appealPersonnel);
                  appealApplicationDto.setRollBackPersonnel(rollBackPersonnel);
                  appealApplicationDto.setAppealAppGrpPremisesDto(appealAppGrpPremisesDto);
                  appealApplicationDto.setRollBackAppGrpPremisesDto(rollBackAppGrpPremisesDto);
                  appealService.createAppealApplicationDto(appealApplicationDto);

              }
          }
        }
        log.debug(StringUtil.changeForLog("The AppealApproveBatchjob is end ..."));
    }
    private void appealApplicaiton(List<ApplicationDto> appealApplicaiton,
                                   List<ApplicationDto> rollBackApplication,
                                   List<AppSvcKeyPersonnelDto> appealPersonnel,
                                   List<AppSvcKeyPersonnelDto> rollBackPersonnel,
                                   List<AppGrpPremisesEntityDto> appealAppGrpPremisesDto,
                                   List<AppGrpPremisesEntityDto> rollBackAppGrpPremisesDto,
                                   AppealApproveDto appealApproveDto) throws Exception {
        ApplicationDto applicationDto = appealApproveDto.getApplicationDto();
        AppealDto appealDto = appealApproveDto.getAppealDto();
        if(applicationDto!= null && appealDto != null){
            String appealReason = appealDto.getAppealReason();
            switch(appealReason){
                case ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION :
                    applicationRejection(appealApplicaiton,rollBackApplication,appealApproveDto);
                    break;
                case ApplicationConsts.APPEAL_REASON_APPLICATION_LATE_RENEW_FEE:
                    applicationLateRenewFee();
                    break;
                case ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO :
                    applicationAddCGO(appealPersonnel,rollBackPersonnel,appealApproveDto);
                    break;
                case ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME :
                    applicationChangeHciName(appealAppGrpPremisesDto,rollBackAppGrpPremisesDto,appealApproveDto);
                    break;
            }
        }
    }
    private void applicationRejection(List<ApplicationDto> appealApplicaiton,
                                      List<ApplicationDto> rollBackApplication,
                                      AppealApproveDto appealApproveDto) throws Exception {
        ApplicationDto applicationDto = appealApproveDto.getAppealApplicationDto();
        //todo:if LIcence had generate  need  to Generate himself else only  need  change this aoolication to approve.
        if(applicationDto!=null){
            rollBackApplication.add(applicationDto);
            ApplicationDto appealApplicaitonDto = (ApplicationDto) CopyUtil.copyMutableObject(applicationDto);
            appealApplicaitonDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            appealApplicaiton.add(appealApplicaitonDto);
        }

    }
    private void applicationLateRenewFee(){
     // do not need to do.
    }
    private void applicationAddCGO(List<AppSvcKeyPersonnelDto> appealPersonnel,
                                   List<AppSvcKeyPersonnelDto> rollBackPersonnel,
                                   AppealApproveDto appealApproveDto) throws Exception {
        AppealDto appealDto = appealApproveDto.getAppealDto();
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
        }
    }
    private void applicationChangeHciName(List<AppGrpPremisesEntityDto> appealAppGrpPremisesDto,
                                          List<AppGrpPremisesEntityDto> rollBackAppGrpPremisesDto,
                                          AppealApproveDto appealApproveDto) throws Exception {
        AppealDto appealDto = appealApproveDto.getAppealDto();
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

    }
    private void appealLicence(List<LicenceDto> appealLicence,
                               List<LicenceDto> rollBackLicence,
                               LicenceDto licenceDto,int newLicYears) throws Exception {
        if(licenceDto!=null && newLicYears >0){
            rollBackLicence.add(licenceDto);
            LicenceDto appealLicenceDto = (LicenceDto) CopyUtil.copyMutableObject(licenceDto);
            Date startDate = appealLicenceDto.getStartDate();
            Date expiryDate = LicenceUtil.getExpiryDate(startDate,newLicYears);
            appealLicenceDto.setExpiryDate(expiryDate);
            appealLicence.add(appealLicenceDto);
        }

    }
    private void appealOther(List<ApplicationDto> appealApplicaiton,
                             List<ApplicationDto> rollBackApplication,
                             ApplicationDto applicationDto) throws Exception {
        if(applicationDto!=null){
            rollBackApplication.add(applicationDto);
            ApplicationDto appealApplicaitonDto = (ApplicationDto) CopyUtil.copyMutableObject(applicationDto);
            appealApplicaitonDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPEAL_ACTIVE);
            appealApplicaiton.add(appealApplicaitonDto);
        }
    }
}
