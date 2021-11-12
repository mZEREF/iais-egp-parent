package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;

import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

/**
 * FertilisationDelegator
 *
 * @author fanghao
 * @date 2021/10/28
 */

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
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Fertilisation Stage</strong>");
    }

    @Override
    public void returnStep(BaseProcessClass bpc) {

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
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

        arSuperDataSubmissionDto = arSuperDataSubmissionDto  == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        FertilisationDto fertilisationDto = arSuperDataSubmissionDto.getFertilisationDto() == null ? new FertilisationDto() : arSuperDataSubmissionDto.getFertilisationDto();
        ControllerHelper.get(request,fertilisationDto);
         if( !IaisCommonUtils.isEmpty(sourceOfSemen)){
             fertilisationDto.setSosList(Arrays.asList(sourceOfSemen));
         }else{
             fertilisationDto.setSosList(null);
         }
        if( !IaisCommonUtils.isEmpty(arTechniquesUsed)){
            fertilisationDto.setAtuList(Arrays.asList(arTechniquesUsed));
        }else{
            fertilisationDto.setAtuList(null);
        }
        arSuperDataSubmissionDto.setFertilisationDto(fertilisationDto);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
        validatePageData(request, fertilisationDto,"save",ACTION_TYPE_CONFIRM);
    }
    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
