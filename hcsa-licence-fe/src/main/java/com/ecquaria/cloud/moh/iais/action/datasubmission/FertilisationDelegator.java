package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * FertilisationDelegator
 *
 * @author fanghao
 * @date 2021/10/28
 */

@Delegator("fertilisationDelegator")
@Slf4j
public class FertilisationDelegator extends CommonDelegator{
    private static final String SUBMIT_FLAG = "fertiliStgSubmitFlag__attr";

    public static final String SOURCE_OF_SEMENS = "sourceOfSemens";
    public static final String AR_TECHNIQUES_USEDS = "arTechniquesUseds";
    public static final String SOURCE_OF_OOCYTES = "oocyteSourceOption";
    public static final String FRESH_OR_FROZEN = "freshOrFrozen";

    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,SOURCE_OF_SEMENS, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.SOURCE_OF_SEMEN));

        ParamUtil.setSessionAttr(request,AR_TECHNIQUES_USEDS, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.AR_TECHNIQUES_USED));

        ParamUtil.setSessionAttr(request,SOURCE_OF_OOCYTES, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_SOURCE_OF_OOCYTE));

        ParamUtil.setSessionAttr(request,FRESH_OR_FROZEN, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_FRESH_OR_FROZEN));
        ParamUtil.setSessionAttr(request, SUBMIT_FLAG, null);
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
            verifyCommon(request, errorMap);
            if(errorMap.isEmpty()){
                valRFC(request, fertilisationDto);
            }
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);
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
        String sourceOfOocyteDonor = ParamUtil.getString(request,"sourceOfOocyteOp0");
        String sourceOfOocytePatient = ParamUtil.getString(request,"sourceOfOocyteOp1");
        String sourceOfOocytePot = ParamUtil.getString(request,"sourceOfOocyteOp2");
        String oocyteUsed = ParamUtil.getString(request,"oocyteUsedOp");
        String spermUsed = ParamUtil.getString(request,"spermUsedOp");
        String usedOocytesNum = ParamUtil.getString(request, "usedOocytesNum");
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
        fertilisationDto.setUsedOocytesNum(usedOocytesNum);
        fertilisationDto.setExtractedSpermVialsNum(extractedSpermVialsNum);
        fertilisationDto.setUsedSpermVialsNum(usedSpermVialsNum);
        if (StringUtil.isNotEmpty(sourceOfOocyteDonor)){
            fertilisationDto.setSourceOfOocyte(AppConsts.YES.equals(sourceOfOocyteDonor) ? Boolean.TRUE : Boolean.FALSE);
        } else {
            fertilisationDto.setSourceOfOocyte(Boolean.FALSE);
        }
        if (StringUtil.isNotEmpty(sourceOfOocytePatient)){
            fertilisationDto.setSourceOfOocytePatient(AppConsts.YES.equals(sourceOfOocytePatient) ? Boolean.TRUE : Boolean.FALSE);
        } else {
            fertilisationDto.setSourceOfOocytePatient(Boolean.FALSE);
        }
        if (StringUtil.isNotEmpty(sourceOfOocytePot)){
            fertilisationDto.setSourceOfOocytePot(AppConsts.YES.equals(sourceOfOocytePot) ? Boolean.TRUE : Boolean.FALSE);
        } else {
            fertilisationDto.setSourceOfOocytePot(Boolean.FALSE);
        }
        fertilisationDto.setOocyteUsed(oocyteUsed);
        fertilisationDto.setSpermUsed(spermUsed);
        fertilisationDto.setFreshOocytesInseminatedNum(freshOocytesInseminatedNum);
        fertilisationDto.setThawedOocytesInseminatedNum(thawedOocytesInseminatedNum);
        fertilisationDto.setFreshOocytesMicroInjectedNum(freshOocytesMicroInjectedNum);
        fertilisationDto.setThawedOocytesMicroinjectedNum(thawedOocytesMicroinjectedNum);
        fertilisationDto.setFreshOocytesZiftNum(freshOocytesZiftNum);
        fertilisationDto.setThawedOocytesZiftNum(thawedOocytesZiftNum);
        fertilisationDto.setFreshOocytesGiftNum(freshOocytesGiftNum);
        fertilisationDto.setThawedOocytesGiftNum(thawedOocytesGiftNum);
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
                }
                if(arTechniquesUsed.equals(DataSubmissionConsts.AR_TECHNIQUES_USED_ICSI)){
                    fertilisationDto.setIcsiUsed(true);

                }
                if(arTechniquesUsed.equals(DataSubmissionConsts.AR_TECHNIQUES_USED_GIFT)){
                    fertilisationDto.setGiftUsed(true);

                }
                if(arTechniquesUsed.equals(DataSubmissionConsts.AR_TECHNIQUES_USED_ZIFT)){
                    fertilisationDto.setZiftUsed(true);

                }
            }
        }else{
            fertilisationDto.setAtuList(null);
        }
    }


}
