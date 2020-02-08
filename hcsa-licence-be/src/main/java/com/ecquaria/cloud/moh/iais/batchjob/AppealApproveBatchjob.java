package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
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
        log.debug(StringUtil.changeForLog("The AppealApproveBatchjob is end ..."));
        List<AppealApproveDto> appealApproveDtos = appealService.getAppealApproveDtos();
        List<ApplicationDto> appealApplicaiton = new ArrayList();
        List<ApplicationDto> rollBackApplication = new ArrayList<>();
        List<LicenceDto> appealLicence = new ArrayList<>();
        List<LicenceDto> rollBackLicence = new ArrayList<>();
        List<AppSvcKeyPersonnelDto> appealPersonnel = new ArrayList<>();
        List<AppSvcKeyPersonnelDto> rollBackPersonnel = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(appealApproveDtos)){
            for (AppealApproveDto appealApproveDto: appealApproveDtos){
                ApplicationDto applicationDto = appealApproveDto.getApplicationDto();
                AppealDto appealDto = appealApproveDto.getAppealDto();
                if(applicationDto!= null && appealDto != null){
                 String  appealType = appealDto.getAppealType();
                    switch(appealType){
                        case ApplicationConsts.APPEAL_TYPE_APPLICAITON :
                            appealApplicaiton(appealApplicaiton,rollBackApplication,appealPersonnel,rollBackPersonnel,
                                    appealApproveDto);
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
        }
        log.debug(StringUtil.changeForLog("The AppealApproveBatchjob is start ..."));
    }
    private void appealApplicaiton(List<ApplicationDto> appealApplicaiton,
                                   List<ApplicationDto> rollBackApplication,
                                   List<AppSvcKeyPersonnelDto> appealPersonnel,
                                   List<AppSvcKeyPersonnelDto> rollBackPersonnel,
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
                    applicationChangeHciName();
                    break;
            }
        }
    }
    private void applicationRejection(List<ApplicationDto> appealApplicaiton,
                                      List<ApplicationDto> rollBackApplication,
                                      AppealApproveDto appealApproveDto) throws Exception {
        ApplicationDto applicationDto = appealApproveDto.getAppealApplicationDto();
        //todo:change the group
        if(applicationDto!=null){
            rollBackApplication.add(applicationDto);
            ApplicationDto appealApplicaitonDto = (ApplicationDto) CopyUtil.copyMutableObject(applicationDto);
            appealApplicaitonDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            appealApplicaiton.add(appealApplicaitonDto);
        }

    }
    private void applicationLateRenewFee(){

    }
    private void applicationAddCGO(List<AppSvcKeyPersonnelDto> appealPersonnel,
                                   List<AppSvcKeyPersonnelDto> rollBackPersonnel,
                                   AppealApproveDto appealApproveDto) throws Exception {
        AppealDto appealDto = appealApproveDto.getAppealDto();
        if(appealDto!=null){
            String  applicaitonId = appealDto.getApplicationId();
            List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = appealApproveDto.getAppSvcKeyPersonnelDtos();
            if(!IaisCommonUtils.isEmpty(appSvcKeyPersonnelDtos)){
                for (AppSvcKeyPersonnelDto appSvcKeyPersonnelDto : appSvcKeyPersonnelDtos){
                    rollBackPersonnel.add(appSvcKeyPersonnelDto);
                    AppSvcKeyPersonnelDto appealAppSvcKeyPersonnelDto = (AppSvcKeyPersonnelDto) CopyUtil.copyMutableObject(appSvcKeyPersonnelDto);
                    appealAppSvcKeyPersonnelDto.setApplicationId(applicaitonId);
                    appealPersonnel.add(appealAppSvcKeyPersonnelDto);
                }
            }
        }
    }
    private void applicationChangeHciName(){

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
