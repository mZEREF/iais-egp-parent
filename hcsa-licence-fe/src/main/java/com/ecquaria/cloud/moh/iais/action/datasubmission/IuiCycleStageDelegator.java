package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Shicheng
 * @date 2021/11/15 10:59
 **/
@Delegator("iuiCycleStageDelegator")
@Slf4j
public class IuiCycleStageDelegator extends DonorCommonDelegator {

    @Override
    public void start(BaseProcessClass bpc) {
        //set SelectOption
        HttpServletRequest request = bpc.request;
        init(request);
    }

    public void init(HttpServletRequest request){
        ParamUtil.setSessionAttr(request, "sourceOfSemenOption", (Serializable) MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.SOURCE_OF_SEMEN));
        ParamUtil.setSessionAttr(request, "curMarrChildNumOption", (Serializable) DataSubmissionHelper.getNumsSelections(10));
        ParamUtil.setSessionAttr(request, "prevMarrChildNumOption", (Serializable)DataSubmissionHelper.getNumsSelections(10));
        ParamUtil.setSessionAttr(request, "iuiDeliverChildNumOption", (Serializable)DataSubmissionHelper.getNumsSelections(10));
        setDonorUserSession(request);
        ParamUtil.setSessionAttr(request, "DSACK003Message","<p>"+ MessageUtil.getMessageDesc("DS_ACK003")+"</p>");
        ParamUtil.setRequestAttr(request,"iuiCycleStageInit",AppConsts.YES);
    }


    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        //init actionType
        String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        if (StringUtil.isEmpty(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
        }
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        //init IuiCycleStageDto The default value
        DataSubmissionHelper.setCurrentArDataSubmission(arDataSubmissionService.setIuiCycleStageDtoDefaultVal(arSuperDataSubmission),request);
        if(AppConsts.YES.equalsIgnoreCase(ParamUtil.getRequestString(request,"iuiCycleStageInit")) && isRfc(request)){
            arSuperDataSubmission.getIuiCycleStageDto().setOldDonorDtos(IaisCommonUtils.isNotEmpty(arSuperDataSubmission.getIuiCycleStageDto().getDonorDtos()) ?(List<DonorDto>) CopyUtil.copyMutableObjectList(arSuperDataSubmission.getIuiCycleStageDto().getDonorDtos()) : null);
            DataSubmissionHelper.setCurrentArDataSubmission(arDataSubmissionService.setIuiCycleStageDtoDefaultVal(arSuperDataSubmission),request);
            initOldDonorSelectSession(request,2);
        }
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        IuiCycleStageDto iuiCycleStageDto = arSuperDataSubmission.getIuiCycleStageDto();
        if(iuiCycleStageDto == null) {
            iuiCycleStageDto = new IuiCycleStageDto();
        }
        iuiCycleStageDto = getIuiCycleFormValue(iuiCycleStageDto,request);
        arSuperDataSubmission.setIuiCycleStageDto(iuiCycleStageDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission,request);
        List<DonorDto> donorDtos = iuiCycleStageDto.getDonorDtos();
        validatePageDataHaveValidationProperty(request,iuiCycleStageDto,"common",donorDtos,getByArCycleStageDto(donorDtos), ACTION_TYPE_CONFIRM);
        actionArDonorDtos(request,donorDtos);
        valiateDonorDtos(request,donorDtos);
        donorDtos.forEach(arDonorDto -> setEmptyDataForNullDrDonorDto(arDonorDto));
        checkDonorsVerifyPass(donorDtos,request);
        valRFC(request,iuiCycleStageDto);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission,request);
    }

    private IuiCycleStageDto getIuiCycleFormValue(IuiCycleStageDto iuiCycleStageDto, HttpServletRequest request) {
        //get form
        String iuiCycleStartDateStr = ParamUtil.getRequestString(request, "iuiCycleStartDate");
        String curMarrChildNumStr = ParamUtil.getRequestString(request, "curMarrChildNum");
        String prevMarrChildNumStr = ParamUtil.getRequestString(request, "prevMarrChildNum");
        String iuiDeliverChildNumStr = ParamUtil.getRequestString(request, "iuiDeliverChildNum");
        String[] sourceOfSemenOpStrs = ParamUtil.getStrings(request, "sourceOfSemenOp");
        String extractVialsOfSpermStr = ParamUtil.getRequestString(request, "extractVialsOfSperm");
        String usedVialsOfSpermStr = ParamUtil.getRequestString(request, "usedVialsOfSperm");
        String ownPremises = ParamUtil.getRequestString(request,"ownPremises");
        String otherPremises = ParamUtil.getRequestString(request,"otherPremises");
        //set date
        if (!StringUtil.isEmpty(iuiCycleStartDateStr)) {
            try {
                Date date = Formatter.parseDate(iuiCycleStartDateStr);
                iuiCycleStageDto.setStartDate(date);
            } catch (Exception e) {
                iuiCycleStageDto.setStartDate(null);
                log.info("Iui Cycle Stage invalid iuiCycleStartDate");
            }
        }
        //get and set Integer Number
        Integer curMarrChildNum =  IaisCommonUtils.stringTransferInteger(curMarrChildNumStr);
        Integer prevMarrChildNum = IaisCommonUtils.stringTransferInteger(prevMarrChildNumStr);
        iuiCycleStageDto.setOwnPremises(AppConsts.YES.equalsIgnoreCase(ownPremises));
        iuiCycleStageDto.setOtherPremises(iuiCycleStageDto.isOwnPremises() ? null : otherPremises);
        iuiCycleStageDto.setCurMarrChildNum(curMarrChildNum);
        iuiCycleStageDto.setPrevMarrChildNum(prevMarrChildNum);
        iuiCycleStageDto.setIuiDeliverChildNum(iuiDeliverChildNumStr);
        iuiCycleStageDto.setExtractVialsOfSperm(extractVialsOfSpermStr);
        iuiCycleStageDto.setUsedVialsOfSperm(usedVialsOfSpermStr);
        //Verify SourceOfSemenOp the value is dirty data, and Set Dto
        List<SelectOption> sourceOfSemenOption = (List<SelectOption>)ParamUtil.getSessionAttr(request, "sourceOfSemenOption");
        List<String> sourceOfSemenList = arDataSubmissionService.checkBoxIsDirtyData(sourceOfSemenOpStrs, sourceOfSemenOption);

        iuiCycleStageDto = setSourceOfSemenAllValue(iuiCycleStageDto, sourceOfSemenList);
        setDonorDtos(request,iuiCycleStageDto.getDonorDtos(),false);
        return iuiCycleStageDto;
    }

    private IuiCycleStageDto setSourceOfSemenAllValue(IuiCycleStageDto iuiCycleStageDto, List<String> sourceOfSemenList) {
        iuiCycleStageDto.setSemenSources(sourceOfSemenList);
        if(!IaisCommonUtils.isEmpty(sourceOfSemenList)) {
            iuiCycleStageDto.setFromDonorFlag(sourceOfSemenList.contains(DataSubmissionConsts.AR_SOURCE_OF_SEMEN_DONOR));
            iuiCycleStageDto.setFromHusbandFlag(sourceOfSemenList.contains(DataSubmissionConsts.AR_SOURCE_OF_SEMEN_HUSBAND));
            iuiCycleStageDto.setFromHusbandTissueFlag(sourceOfSemenList.contains(DataSubmissionConsts.AR_SOURCE_OF_H_SEMEN_TESTICULAR));
            iuiCycleStageDto.setFromDonorTissueFlag(sourceOfSemenList.contains(DataSubmissionConsts.AR_SOURCE_OF_D_SEMEN_TESTICULAR));
        }
        return iuiCycleStageDto;
    }

    protected void valRFC(HttpServletRequest request, IuiCycleStageDto iuiCycleStageDto){
        if( ACTION_TYPE_CONFIRM.equalsIgnoreCase(ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE)) && isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(IaisCommonUtils.isNotEmpty(iuiCycleStageDto.getSemenSources())){
                Collections.sort(iuiCycleStageDto.getSemenSources());
            }
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getIuiCycleStageDto()!= null && iuiCycleStageDto .equals(arOldSuperDataSubmissionDto.getIuiCycleStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
