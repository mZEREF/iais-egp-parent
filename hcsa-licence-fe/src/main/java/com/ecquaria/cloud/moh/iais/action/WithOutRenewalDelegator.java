package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.List;

/**
 * AutoRenewalDelegator
 *
 * @author caijing
 * @date 2020/1/6
 */

@Delegator("withOutRenewalDelegator")
@Slf4j
public class WithOutRenewalDelegator {
    @Autowired
    WithOutRenewalService outRenewalService;

    @Autowired
    AppSubmissionService appSubmissionService;

    public void start(BaseProcessClass bpc){
        log.info("**** the non auto renwal  start ******");

        log.info("**** the non auto renwal  end ******");
    }

    /**
     * AutoStep: prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc)throws Exception{
        log.info("**** the  auto renwal  prepare start  ******");
//      String  licenceId = ParamUtil.getString(bpc.request,"licNo");
        String licenceId="7ECAE165-534A-EA11-BE7F-000C29F371DC";
        AppSubmissionDto appSubmissionDto=appSubmissionService.getAppSubmissionDtoByLicenceId(licenceId);
        List<AppSubmissionDto> appSubmissionDtoList=new ArrayList<>();
        appSubmissionDtoList.add(appSubmissionDto);
        RenewDto renewDto=new RenewDto();
        renewDto.setAppSubmissionDtos(appSubmissionDtoList);
        ParamUtil.setSessionAttr(bpc.request,"renewDto", renewDto);
        log.info("**** the  auto renwal  prepare  end ******");
    }


    /**
     * AutoStep: determineAutoRenewalEligibility
     *
     * @param bpc
     * @throws
     */
    public void determineAutoRenewalEligibility(BaseProcessClass bpc){
        log.info("**** the determineAutoRenewalEligibility  prepare start  ******");






        log.info("**** the determineAutoRenewalEligibility  prepare  end ******");
    }


    /**
     * AutoStep: markPostInspection
     *
     * @param bpc
     * @throws
     */
    public void markPostInspection(BaseProcessClass bpc){
        log.info("**** the markPostInspection start  ******");
        AppSubmissionDto appSubmissionDto=(AppSubmissionDto)ParamUtil.getSessionAttr(bpc.request,"appSubmissionDto");
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
        log.info("**** the markPostInspection end ******");
    }

}
