package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDonorSampleService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    ArDonorSampleService arDonorSampleService;
    private final static String  SAMPLE_FROM_HCICODE          =  "SampleFromHciCode";
    private static final String SUBMIT_FLAG = "submitDonSubmitFlag__attr";

    @Override
    public void start(BaseProcessClass bpc) {
        super.start(bpc);
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, null);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SAMPLE_FROM_HCICODE,(Serializable) getSourseList(bpc.request));
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, null);
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

    @Override
    public void doSubmission(BaseProcessClass bpc) {
        String submitFlag = (String) ParamUtil.getSessionAttr(bpc.request, SUBMIT_FLAG);
        if (!StringUtil.isEmpty(submitFlag)) {
            throw new IaisRuntimeException("Double Submit");
        }
        super.doSubmission(bpc);
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, AppConsts.YES);
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
            freshOocyteNum += donorSampleDto.calTotalNum();
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_OOCYTE.equals(donorSampleDto.getSampleType())) {
            frozenOocyteNum += donorSampleDto.calTotalNum();
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_EMBRYO.equals(donorSampleDto.getSampleType())) {
            frozenEmbryoNum += donorSampleDto.calTotalNum();
        } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM.equals(donorSampleDto.getSampleType())){
            frozenSpermNum += donorSampleDto.calTotalNum();
        } else  if (DataSubmissionConsts.DONATED_TYPE_FRESH_SPERM.equals(donorSampleDto.getSampleType())){
            freshSpermNum += donorSampleDto.calTotalNum();
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
                secondFrozenEmbryoNum += donorSampleDto.calTotalNum();;
            } else if (DataSubmissionConsts.DONATED_TYPE_FROZEN_SPERM.equals(donorSampleDto.getSampleType())){
                secondFrozenSpermNum += donorSampleDto.calTotalNum();;
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

}
