package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
        List<String> licenceIDList = (List<String>) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR);

        if (licenceIDList == null || IaisCommonUtils.isEmpty(licenceIDList)){
            log.info("can not find licence id for without renewal");
            return;
        }

        List<AppSubmissionDto> appSubmissionDtoList = outRenewalService.getAppSubmissionDtos(licenceIDList);
        for (AppSubmissionDto appSubmissionDto: appSubmissionDtoList) {
            if(!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
                String serviceName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
                appSubmissionDto.setServiceName(serviceName);
            }
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_RENEWAL);

            String draftNumber = appSubmissionService.getDraftNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            String groupNumber =  appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);

            log.info("without renewal deafrt number =====>" + draftNumber);
            log.info("without renewal group number =====>" + groupNumber);

            appSubmissionDto.setDraftNo(draftNumber);
            appSubmissionDto.setAppGrpNo(groupNumber);
            appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

            try {
                NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
            } catch (CloneNotSupportedException e) {
                log.error(e.getMessage());
            }

            appSubmissionService.setRiskToDto(appSubmissionDto);
        }

        RenewDto renewDto = new RenewDto();

        String parseToJson = JsonUtil.parseToJson(appSubmissionDtoList);
        log.info("without renewal submission json info " + parseToJson);

        renewDto.setAppSubmissionDtos(appSubmissionDtoList);
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        log.info("**** the  renwal  prepare  end ******");
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
