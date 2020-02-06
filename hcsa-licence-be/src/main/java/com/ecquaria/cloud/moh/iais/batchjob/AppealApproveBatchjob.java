package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApproveDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AppealService;
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
        if(!IaisCommonUtils.isEmpty(appealApproveDtos)){
            for (AppealApproveDto appealApproveDto: appealApproveDtos){
                ApplicationDto applicationDto = appealApproveDto.getApplicationDto();
                AppealDto appealDto = appealApproveDto.getAppealDto();
                if(applicationDto!= null && appealDto != null){
                 String  appealType = appealDto.getAppealType();
                    switch(appealType){
                        case ApplicationConsts.APPEAL_TYPE_APPLICAITON :
                            appealApplicaiton();
                            break;
                        case ApplicationConsts.APPEAL_TYPE_LICENCE :
                            appealLicence(appealLicence,rollBackLicence,appealApproveDto.getLicenceDto(),
                                    appealDto.getNewLicYears());
                            appealOther(appealApplicaiton,rollBackApplication,applicationDto);
                            break;
                        case ApplicationConsts.APPEAL_TYPE_OTHER :
                            appealOther(appealApplicaiton,rollBackApplication,applicationDto);
                            break;
                    }
                }
            }
        }
        log.debug(StringUtil.changeForLog("The AppealApproveBatchjob is start ..."));
    }
    private void appealApplicaiton(){

    }
    private void appealLicence(List<LicenceDto> appealLicence,
                               List<LicenceDto> rollBackLicence,
                               LicenceDto licenceDto,int newLicYears) throws Exception {
        if(licenceDto!=null && newLicYears >0){
            rollBackLicence.add(licenceDto);
            LicenceDto licenceDto1 = (LicenceDto) CopyUtil.copyMutableObject(licenceDto);
            Date startDate = licenceDto1.getStartDate();

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
