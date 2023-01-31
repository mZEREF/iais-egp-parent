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
        ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(bpc.request);
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
        arSuperDataSubmissionDto.setDonorSampleDto(donorSampleDto);
        arDataSubmissionService.getDonorInventory(arSuperDataSubmissionDto);
        setDonorChangeInv(arSuperDataSubmissionDto,arOldSuperDataSubmissionDto);
        setDonorInv(arSuperDataSubmissionDto);

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
                donorSampleDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
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

    private void setDonorInv(ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        DonorSampleDto donorSampleDto = arSuperDataSubmissionDto.getDonorSampleDto();
        int freshOocyteNum = 0;
        int frozenOocyteNum = 0;
        int frozenEmbryoNum = 0;
        int frozenSpermNum = 0;
        int freshSpermNum = 0;

        if (DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE.equals(donorSampleDto.getSampleType())) {
            freshOocyteNum += getSamplesNum(donorSampleDto);
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE.equals(donorSampleDto.getSampleType())) {
            frozenOocyteNum += getSamplesNum(donorSampleDto);
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(donorSampleDto.getSampleType())) {
            frozenEmbryoNum += getSamplesNum(donorSampleDto);
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM.equals(donorSampleDto.getSampleType())){
            frozenSpermNum += getSamplesNum(donorSampleDto);
        } else  if (DataSubmissionConsts.DONATED_TYPE_FRESH_SPERM.equals(donorSampleDto.getSampleType())){
            freshSpermNum += getSamplesNum(donorSampleDto);
        }
        ArChangeInventoryDto arChangeInventoryDto = arSuperDataSubmissionDto.getArChangeInventoryDto();
        if (DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(donorSampleDto.getSampleType())) {
            ArChangeInventoryDto secondArChangeInventoryDto = arSuperDataSubmissionDto.getSecondArChangeInventoryDto();
            if (secondArChangeInventoryDto == null) {
                secondArChangeInventoryDto = new ArChangeInventoryDto();
            }
            int secondFrozenEmbryoNum = 0;
            int secondFrozenSpermNum = 0;
            if (DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(donorSampleDto.getSampleType())) {
                secondFrozenEmbryoNum += getSamplesNum(donorSampleDto);
            } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM.equals(donorSampleDto.getSampleType())){
                secondFrozenSpermNum += getSamplesNum(donorSampleDto);
            }
            secondArChangeInventoryDto.setFrozenEmbryoNum(secondFrozenEmbryoNum);
            secondArChangeInventoryDto.setFrozenSpermNum(secondFrozenSpermNum);
            arSuperDataSubmissionDto.setSecondArChangeInventoryDto(secondArChangeInventoryDto);
        }
        if (arChangeInventoryDto == null){
            arChangeInventoryDto = new ArChangeInventoryDto();
        }
        arChangeInventoryDto.setFreshOocyteNum(freshOocyteNum);
        arChangeInventoryDto.setFrozenOocyteNum(frozenOocyteNum);
        arChangeInventoryDto.setFrozenEmbryoNum(frozenEmbryoNum);
        arChangeInventoryDto.setFrozenSpermNum(frozenSpermNum);
        arChangeInventoryDto.setFreshSpermNum(freshSpermNum);
        arSuperDataSubmissionDto.setArChangeInventoryDto(arChangeInventoryDto);
    }

    private int getSamplesNum(DonorSampleDto donorSampleDto){
        int res = 0;
        if(StringUtil.isNumber(donorSampleDto.getTrainingNum())) {
            res += Integer.parseInt(donorSampleDto.getTrainingNum());
        }
        if(StringUtil.isNumber(donorSampleDto.getTreatNum())) {
            res += Integer.parseInt(donorSampleDto.getTreatNum());
        }
        if(StringUtil.isNumber(donorSampleDto.getDonResForTreatNum())) {
            res += Integer.parseInt(donorSampleDto.getDonResForTreatNum());
        }
        if(StringUtil.isNumber(donorSampleDto.getDonResForCurCenNotTreatNum())) {
            res += Integer.parseInt(donorSampleDto.getDonResForCurCenNotTreatNum());
        }
        return res;
    }

    private void setDonorChangeInv(ArSuperDataSubmissionDto arSuperDataSubmissionDto, ArSuperDataSubmissionDto arOldSuperDataSubmissionDto) {
        DonorSampleDto oldDonorSampleDto = arOldSuperDataSubmissionDto.getDonorSampleDto();
        ArCurrentInventoryDto arCurrentInventoryDto = arSuperDataSubmissionDto.getArCurrentInventoryDto();
        if (oldDonorSampleDto != null) {
            int changeNum = getSamplesNum(oldDonorSampleDto);
            String sampleType = oldDonorSampleDto.getSampleType();
            if (DataSubmissionConsts.DONATED_TYPE_FRESH_OOCYTE.equals(sampleType)) {
                arCurrentInventoryDto.setFreshOocyteNum(arCurrentInventoryDto.getFreshOocyteNum()-1 * changeNum);
            } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE.equals(sampleType)) {
                arCurrentInventoryDto.setFrozenOocyteNum(arCurrentInventoryDto.getFrozenOocyteNum()-1 * changeNum);
            } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(sampleType)) {
                arCurrentInventoryDto.setFrozenEmbryoNum(arCurrentInventoryDto.getFrozenEmbryoNum()-1 * changeNum);
            } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM.equals(sampleType)) {
                arCurrentInventoryDto.setFrozenSpermNum(arCurrentInventoryDto.getFrozenSpermNum()-1 * changeNum);
            } else if (DataSubmissionConsts.DONATED_TYPE_FRESH_SPERM.equals(sampleType)) {
                arCurrentInventoryDto.setFreshSpermNum(arCurrentInventoryDto.getFreshSpermNum()-1 * changeNum);
            }
        }
    }
}
