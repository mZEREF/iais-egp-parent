package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import java.io.Serializable;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * SubmitDonorDelegator
 *
 * @author suocheng
 * @date 11/8/2021
 */
@Delegator("submitDonorDelegator")
@Slf4j
public class SubmitDonorDelegator extends CommonDelegator {
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
        DonorSampleDto donorSampleDto = arSuperDataSubmissionDto.getDonorSampleDto();
        int ageCount = 1;

        if(donorSampleDto != null && donorSampleDto.getAges() != null){
            ageCount = donorSampleDto.getAges().length+1;
        }
        bpc.request.setAttribute("ageCount",ageCount);
    }
    @Override
    public void pageAction(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("submitDonorDelegator The pageAction start ..."));
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DonorSampleDto donorSampleDto = arSuperDataSubmissionDto.getDonorSampleDto();
        if(donorSampleDto == null){
            donorSampleDto =  new DonorSampleDto();
        }
        if(DataSubmissionConsts.DS_APP_TYPE_RFC.equals(arSuperDataSubmissionDto.getAppType())){
           String[] ages =  bpc.request.getParameterValues("ages");
            donorSampleDto.setAges(ages);
            String[] ageCheckName =  bpc.request.getParameterValues("ageCheckName");
            List<DonorSampleAgeDto> donorSampleAgeDtos = donorSampleDto.getDonorSampleAgeDtos();
            changeAvailable(donorSampleAgeDtos,ageCheckName);
            donorSampleDto.setDonorSampleAgeDtos(donorSampleAgeDtos);
        }else{
            donorSampleDto =  ControllerHelper.get(bpc.request,donorSampleDto);
        }

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
            validatePageData(bpc.request, donorSampleDto, "save", ACTION_TYPE_CONFIRM);
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
}
