package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Shicheng
 * @date 2021/11/15 10:59
 **/
@Delegator("iuiCycleStageDelegator")
@Slf4j
public class IuiCycleStageDelegator extends CommonDelegator {

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Override
    public void start(BaseProcessClass bpc) {
        //set SelectOption
        ParamUtil.setSessionAttr(bpc.request, "sourceOfSemenOption", (Serializable) arDataSubmissionService.getSourceOfSemenOption());
        ParamUtil.setSessionAttr(bpc.request, "curMarrChildNumOption", (Serializable)  arDataSubmissionService.getChildNumOption());
        ParamUtil.setSessionAttr(bpc.request, "prevMarrChildNumOption", (Serializable) arDataSubmissionService.getChildNumOption());
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
        DataSubmissionHelper.setCurrentArDataSubmission( arSuperDataSubmission,request);
        List<DonorDto> donorDtos = iuiCycleStageDto.getDonorDtos();
        validatePageDataHaveValidationProperty(request,iuiCycleStageDto,"common",donorDtos,getByArCycleStageDto(donorDtos), ACTION_TYPE_CONFIRM);
        actionArDonorDtos(request,donorDtos);
        valiateDonorDtos(request,donorDtos);
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
        Integer curMarrChildNum = arDataSubmissionService.stringTransferInteger(curMarrChildNumStr);
        Integer prevMarrChildNum = arDataSubmissionService.stringTransferInteger(prevMarrChildNumStr);
        Integer iuiDeliverChildNum = arDataSubmissionService.stringTransferInteger(iuiDeliverChildNumStr);
        Integer extractVialsOfSpermNum = arDataSubmissionService.stringTransferInteger(extractVialsOfSpermStr);
        Integer usedVialsOfSpermNum = arDataSubmissionService.stringTransferInteger(usedVialsOfSpermStr);

        iuiCycleStageDto.setCurMarrChildNum(curMarrChildNum);
        iuiCycleStageDto.setPrevMarrChildNum(prevMarrChildNum);
        iuiCycleStageDto.setIuiDeliverChildNum(iuiDeliverChildNum);
        iuiCycleStageDto.setExtractVialsOfSperm(extractVialsOfSpermNum);
        iuiCycleStageDto.setUsedVialsOfSperm(usedVialsOfSpermNum);
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
        }
        return iuiCycleStageDto;
    }
}
