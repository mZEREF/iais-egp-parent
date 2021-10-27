package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

@Delegator("fertilisationDelegator")
@Slf4j
public class FertilisationDelegator extends CommonDelegator{

    public static final String SOURCE_OF_SEMENS = "sourceOfSemens";
    public static final String AR_TECHNIQUES_USEDS = "arTechniquesUseds";
    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,SOURCE_OF_SEMENS, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.SOURCE_OF_SEMEN));

        ParamUtil.setSessionAttr(request,AR_TECHNIQUES_USEDS, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.AR_TECHNIQUES_USED));
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

    }

    @Override
    public void returnStep(BaseProcessClass bpc) {

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {

    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {

    }

    @Override
    public void draft(BaseProcessClass bpc) {

    }

    @Override
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String[] sourceOfSemen = ParamUtil.getStrings(request,"sourceOfSemen");
        String[] arTechniquesUsed = ParamUtil.getStrings(request,"arTechniquesUsed");

        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        //arSuperDataSubmissionDto = new ArSuperDataSubmissionDto();
        FertilisationDto fertilisationDto = arSuperDataSubmissionDto.getFertilisationDto() == null ? new FertilisationDto() : arSuperDataSubmissionDto.getFertilisationDto();

        fertilisationDto.setExtractedSpermVialsNum(ParamUtil.getString(request,"extractedSpermVialsNum"));
        fertilisationDto.setUsedSpermVialsNum(ParamUtil.getString(request,"usedSpermVialsNum"));
        fertilisationDto.setFreshOocytesInseminatedNum(ParamUtil.getString(request,"freshOocytesInseminatedNum"));
        fertilisationDto.setFreshOocytesMicroInjectedNum(ParamUtil.getString(request,"freshOocytesMicroinjectedNum"));
        fertilisationDto.setFreshOocytesGiftNum(ParamUtil.getString(request,"freshOocytesGiftNum"));
        fertilisationDto.setFreshOocytesZiftNum(ParamUtil.getString(request,"freshOocytesZiftNum"));
        fertilisationDto.setThawedOocytesInseminatedNum(ParamUtil.getString(request,"thawedOocytesInseminatedNum"));
        fertilisationDto.setThawedOocytesMicroinjectedNum(ParamUtil.getString(request,"thawedOocytesMicroinjectedNum"));
        ParamUtil.getStringsToString(request,"sourceOfSemen");
        ParamUtil.getStringsToString(request,"arTechniquesUsed");
        fertilisationDto.setThawedOocytesGiftNum(ParamUtil.getString(request,"thawedOocytesGiftNum"));
        fertilisationDto.setThawedOocytesZiftNum(ParamUtil.getString(request,"thawedOocytesZiftNum"));

        ValidationResult validationResult = WebValidationHelper.validateProperty(fertilisationDto, "save");
        Map<String, String> errorMap = validationResult.retrieveAll();

        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
            return;
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "confirm");

    }
    //private FertilisationDto getFertilisationFromPage(HttpServletRequest request)

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
