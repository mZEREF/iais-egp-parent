package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
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
    public void start(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmission = currentArDataSubmission.getCurrentDataSubmissionDto();
        if (dataSubmission == null) {
            dataSubmission = new DataSubmissionDto();
            currentArDataSubmission.setCurrentDataSubmissionDto(dataSubmission);
        }
        dataSubmission.setSubmissionType(currentArDataSubmission.getSubmissionType());
        dataSubmission.setCycleStage(DataSubmissionConsts.DS_CYCLE_STAGE_DONOR_SAMPLE);
        dataSubmission.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        currentArDataSubmission.setCycleDto(initCycleDto(currentArDataSubmission));
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, bpc.request);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SAMPLE_FROM_HCICODE,(Serializable) getSampleFromHciCode());
    }


    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DonorSampleDto donorSampleDto = arSuperDataSubmissionDto.getDonorSampleDto();
        if(donorSampleDto == null){
            bpc.request.setAttribute("ageCount",0);
        }else{
            bpc.request.setAttribute("ageCount",donorSampleDto.getAges().length);
        }
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("submitDonorDelegator The pageAction start ..."));
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DonorSampleDto donorSampleDto = arSuperDataSubmissionDto.getDonorSampleDto();
        if(donorSampleDto == null){
            donorSampleDto =  new DonorSampleDto();
        }
        donorSampleDto =  ControllerHelper.get(bpc.request,donorSampleDto);
        arSuperDataSubmissionDto.setDonorSampleDto(donorSampleDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto,bpc.request);
        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (ACTION_TYPE_CONFIRM.equals(actionType)) {
           // validatePageData(bpc.request, donorSampleDto, "save", ACTION_TYPE_CONFIRM);
        }
        log.info(StringUtil.changeForLog("submitDonorDelegator The pageAction end ..."));
    }

    //TODO from ar center
    private List<SelectOption> getSampleFromHciCode(){
        List<SelectOption> selectOptions  = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption("others","Others"));
        return selectOptions;
    }
    private CycleDto initCycleDto(ArSuperDataSubmissionDto currentArDataSubmission) {
        CycleDto cycleDto = currentArDataSubmission.getCycleDto();
        if (cycleDto == null) {
            cycleDto = new CycleDto();
        }
        String hicCode = Optional.ofNullable(currentArDataSubmission.getPremisesDto())
                .map(PremisesDto::getHciCode)
                .orElse("");
        cycleDto.setHciCode(hicCode);
        cycleDto.setDsType(DataSubmissionConsts.DS_AR);
        cycleDto.setCycleType(DataSubmissionConsts.DS_CYCLE_DONOR_SAMPLE);
        return cycleDto;
    }
}
