package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
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
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
    }


    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);

    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        FertilisationDto fertilisationDto = arSuperDataSubmissionDto.getFertilisationDto();
        int FreshOocytesInseminatedNum = IaisCommonUtils.getIntByNum(fertilisationDto.getFreshOocytesInseminatedNum(),0);
        int FreshOocytesMicroInjectedNum=IaisCommonUtils.getIntByNum(fertilisationDto.getFreshOocytesMicroInjectedNum(),0);
        int FreshOocytesGiftNum=IaisCommonUtils.getIntByNum(fertilisationDto.getFreshOocytesGiftNum(),0);
        int FreshOocytesZiftNu=IaisCommonUtils.getIntByNum(fertilisationDto.getFreshOocytesZiftNum(),0);
        int ThawedOocytesInseminatedNum= IaisCommonUtils.getIntByNum(fertilisationDto.getThawedOocytesInseminatedNum(),0);
        int ThawedOocytesMicroinjectedNum= IaisCommonUtils.getIntByNum(fertilisationDto.getThawedOocytesMicroinjectedNum(),0);
        int ThawedOocytesGiftNum=IaisCommonUtils.getIntByNum(fertilisationDto.getThawedOocytesGiftNum(),0);
        int ThawedOocytesZiftNum=IaisCommonUtils.getIntByNum(fertilisationDto.getThawedOocytesZiftNum(),0);
        ArChangeInventoryDto arChangeInventoryDto = new ArChangeInventoryDto();
        arSuperDataSubmissionDto.setArChangeInventoryDto(arChangeInventoryDto);
        int changeFreshOocytes = FreshOocytesInseminatedNum + FreshOocytesMicroInjectedNum + FreshOocytesGiftNum + FreshOocytesZiftNu;
        int changeThawedOocytes = ThawedOocytesInseminatedNum + ThawedOocytesMicroinjectedNum + ThawedOocytesGiftNum + ThawedOocytesZiftNum;
        arChangeInventoryDto.setFreshOocyteNum(-1 * changeFreshOocytes);
        arChangeInventoryDto.setThawedOocyteNum(-1 * changeThawedOocytes);
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        arSuperDataSubmissionDto = arSuperDataSubmissionDto  == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        FertilisationDto fertilisationDto = arSuperDataSubmissionDto.getFertilisationDto() == null ? new FertilisationDto() : arSuperDataSubmissionDto.getFertilisationDto();
        setFertilisationDto(request,fertilisationDto);
        arSuperDataSubmissionDto.setFertilisationDto(fertilisationDto);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);

        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(fertilisationDto, "save");
            errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);
            valRFC(request, fertilisationDto);
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);
    }

    protected void valRFC(HttpServletRequest request, FertilisationDto fertilisationDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getFertilisationDto()!= null && fertilisationDto.equals(arOldSuperDataSubmissionDto.getFertilisationDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
    private void setFertilisationDto(HttpServletRequest request, FertilisationDto fertilisationDto){
        String[] sourceOfSemens = ParamUtil.getStrings(request,"sourceOfSemen");
        String[] arTechniquesUseds = ParamUtil.getStrings(request,"arTechniquesUsed");
        String extractedSpermVialsNum = ParamUtil.getRequestString(request,"extractedSpermVialsNum");
        String usedSpermVialsNum = ParamUtil.getRequestString(request,"usedSpermVialsNum");
        String freshOocytesInseminatedNum = ParamUtil.getRequestString(request,"freshOocytesInseminatedNum");
        String freshOocytesMicroInjectedNum = ParamUtil.getRequestString(request,"freshOocytesMicroInjectedNum");
        String freshOocytesGiftNum = ParamUtil.getRequestString(request,"freshOocytesGiftNum");
        String freshOocytesZiftNum = ParamUtil.getRequestString(request,"freshOocytesZiftNum");
        String thawedOocytesInseminatedNum = ParamUtil.getRequestString(request,"thawedOocytesInseminatedNum");
        String thawedOocytesMicroinjectedNum = ParamUtil.getRequestString(request,"thawedOocytesMicroinjectedNum");
        String thawedOocytesGiftNum = ParamUtil.getRequestString(request,"thawedOocytesGiftNum");
        String thawedOocytesZiftNum = ParamUtil.getRequestString(request,"thawedOocytesZiftNum");
        fertilisationDto.setExtractedSpermVialsNum(extractedSpermVialsNum);
        fertilisationDto.setUsedSpermVialsNum(usedSpermVialsNum);
        if( !IaisCommonUtils.isEmpty(sourceOfSemens)){
            fertilisationDto.setSosList(Arrays.asList(sourceOfSemens));
            fertilisationDto.setFromDonorTissue(false);
            fertilisationDto.setFromHusband(false);
            fertilisationDto.setFromHusbandTissue(false);
            fertilisationDto.setFromDonor(false);
            for (String sourceOfSeme:sourceOfSemens) {
                if(sourceOfSeme.equals(DataSubmissionConsts.AR_SOURCE_OF_SEMEN_HUSBAND)){
                    fertilisationDto.setFromHusband(true);
                }
                if(sourceOfSeme.equals(DataSubmissionConsts.AR_SOURCE_OF_H_SEMEN_TESTICULAR)){
                    fertilisationDto.setFromHusbandTissue(true);
                }
                if(sourceOfSeme.equals(DataSubmissionConsts.AR_SOURCE_OF_SEMEN_DONOR)){
                    fertilisationDto.setFromDonor(true);
                }
                if(sourceOfSeme.equals(DataSubmissionConsts.AR_SOURCE_OF_D_SEMEN_TESTICULAR)){
                    fertilisationDto.setFromDonorTissue(true);
                }
            }
        }else{
            fertilisationDto.setSosList(null);
        }
        if( !IaisCommonUtils.isEmpty(arTechniquesUseds)){
            fertilisationDto.setAtuList(Arrays.asList(arTechniquesUseds));
            fertilisationDto.setIvfUsed(false);
            fertilisationDto.setIcsiUsed(false);
            fertilisationDto.setGiftUsed(false);
            fertilisationDto.setZiftUsed(false);
            for (String arTechniquesUsed:arTechniquesUseds) {
                if(arTechniquesUsed.equals(DataSubmissionConsts.AR_TECHNIQUES_USED_IVF)){
                    fertilisationDto.setIvfUsed(true);
                    fertilisationDto.setFreshOocytesInseminatedNum(freshOocytesInseminatedNum);
                    fertilisationDto.setThawedOocytesInseminatedNum(thawedOocytesInseminatedNum);
                }
                if(arTechniquesUsed.equals(DataSubmissionConsts.AR_TECHNIQUES_USED_ICSI)){
                    fertilisationDto.setIcsiUsed(true);
                    fertilisationDto.setFreshOocytesMicroInjectedNum(freshOocytesMicroInjectedNum);
                    fertilisationDto.setThawedOocytesMicroinjectedNum(thawedOocytesMicroinjectedNum);
                }
                if(arTechniquesUsed.equals(DataSubmissionConsts.AR_TECHNIQUES_USED_GIFT)){
                    fertilisationDto.setGiftUsed(true);
                    fertilisationDto.setFreshOocytesGiftNum(freshOocytesGiftNum);
                    fertilisationDto.setThawedOocytesGiftNum(thawedOocytesGiftNum);
                }
                if(arTechniquesUsed.equals(DataSubmissionConsts.AR_TECHNIQUES_USED_ZIFT)){
                    fertilisationDto.setZiftUsed(true);
                    fertilisationDto.setFreshOocytesZiftNum(freshOocytesZiftNum);
                    fertilisationDto.setThawedOocytesZiftNum(thawedOocytesZiftNum);
                }
            }
        }else{
            fertilisationDto.setAtuList(null);
            fertilisationDto.setFreshOocytesInseminatedNum(null);
            fertilisationDto.setThawedOocytesInseminatedNum(null);
            fertilisationDto.setFreshOocytesMicroInjectedNum(null);
            fertilisationDto.setThawedOocytesMicroinjectedNum(null);
            fertilisationDto.setFreshOocytesGiftNum(null);
            fertilisationDto.setThawedOocytesGiftNum(null);
            fertilisationDto.setFreshOocytesZiftNum(null);
            fertilisationDto.setThawedOocytesZiftNum(null);
        }
    }


}
