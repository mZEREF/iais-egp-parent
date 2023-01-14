package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDonorSampleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * SubmitDonorDelegator
 *
 * @author suocheng
 * @date 11/8/2021
 */
@Delegator("submitDonorDelegator")
@Slf4j
public class SubmitDonorDelegator extends CommonDelegator {
    @Autowired
    ArDonorSampleService arDonorSampleService;
    private final static String  SAMPLE_FROM_HCICODE          =  "SampleFromHciCode";

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SAMPLE_FROM_HCICODE,(Serializable) getSourseList(bpc.request));
//        String stage = MasterCodeUtil.getCodeDesc(DataSubmissionConsts.DS_CYCLE_STAGE_DONOR_SAMPLE);
//        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>" + stage + "</strong>");
    }


    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ParamUtil.setRequestAttr(bpc.request,"localPremisesLabel",arSuperDataSubmissionDto.getPremisesDto().getPremiseLabel());

    }
    @Override
    public void pageAction(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("submitDonorDelegator The pageAction start ..."));
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DonorSampleDto donorSampleDto = arDonorSampleService.genDonorSampleDtoByPage(bpc.request);
        arSuperDataSubmissionDto.setDonorSampleDto(donorSampleDto);

        //RFC
        String amendReason = ParamUtil.getString(bpc.request, "amendReason");
        String amendReasonOther = ParamUtil.getString(bpc.request, "amendReasonOther");
        log.info(StringUtil.changeForLog("submitDonorDelegator The pageAction  amendReason is -->:"+amendReason));
        log.info(StringUtil.changeForLog("submitDonorDelegator The pageAction  amendReasonOther is -->:"+amendReasonOther));
        DataSubmissionDto dataSubmissionDto = arSuperDataSubmissionDto.getDataSubmissionDto();
        dataSubmissionDto.setAmendReason(amendReason);
        dataSubmissionDto.setAmendReasonOther(amendReasonOther);
        donorSampleDto.setAmendReason(amendReason);
        donorSampleDto.setAmendReasonOther(amendReasonOther);
        donorSampleDto.setAppType(dataSubmissionDto.getAppType());

        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);


        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (ACTION_TYPE_CONFIRM.equals(actionType)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(donorSampleDto, "rfc");
            Map<String, String> errorMap = validationResult.retrieveAll();
            verifyCommon(bpc.request, errorMap);
            boolean validate = validatePageData(bpc.request, donorSampleDto, "save", ACTION_TYPE_CONFIRM);
            if(!validate){
                return;
            }
            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            }else
                valRFC(bpc.request,donorSampleDto);

        }
        log.info(StringUtil.changeForLog("submitDonorDelegator The pageAction end ..."));
    }


    private  void changeAvailable(List<DonorSampleAgeDto> donorSampleAgeDtos,String[] ageCheckName){
        log.info(StringUtil.changeForLog("submitDonorDelegator changeAvailable start ..."));
        if(IaisCommonUtils.isNotEmpty(donorSampleAgeDtos)){
            for(DonorSampleAgeDto donorSampleAgeDto : donorSampleAgeDtos){
               String donorSampleId = donorSampleAgeDto.getId();
               boolean IsAvailable = false;
               if(ageCheckName != null){
                   IsAvailable = ArrayUtils.contains(ageCheckName,donorSampleId);
               }
                log.info(StringUtil.changeForLog("submitDonorDelegator changeAvailable donorSampleId is-->"+donorSampleId));
                log.info(StringUtil.changeForLog("submitDonorDelegator changeAvailable IsAvailable is-->"+IsAvailable));
               donorSampleAgeDto.setAvailable(IsAvailable);
            }
        }
        log.info(StringUtil.changeForLog("submitDonorDelegator changeAvailable end ..."));
    }

    protected void valRFC(HttpServletRequest request, DonorSampleDto donorSampleDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getDonorSampleDto()!= null && donorSampleDto.equals(arOldSuperDataSubmissionDto.getDonorSampleDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
