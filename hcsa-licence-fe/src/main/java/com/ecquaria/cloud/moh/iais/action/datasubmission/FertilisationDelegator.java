package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;

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

        validatePageData(request, fertilisationDto,"save",ACTION_TYPE_CONFIRM);
        if(CommonDelegator.ACTION_TYPE_CONFIRM.equals(ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE))){
            valRFC(request,fertilisationDto);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
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
}
