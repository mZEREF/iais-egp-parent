package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        ParamUtil.setSessionAttr(bpc.request, "sourceOfSemenOption", null);
        ParamUtil.setSessionAttr(bpc.request, "curMarrChildNumOption", null);
        ParamUtil.setSessionAttr(bpc.request, "prevMarrChildNumOption", null);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
        //set SelectOption
        List<SelectOption> sourceOfSemenOption = arDataSubmissionService.getSourceOfSemenOption();
        List<SelectOption> curMarrChildNumOption = arDataSubmissionService.getChildNumOption();
        List<SelectOption> prevMarrChildNumOption = arDataSubmissionService.getChildNumOption();
        ParamUtil.setSessionAttr(bpc.request, "sourceOfSemenOption", (Serializable) sourceOfSemenOption);
        ParamUtil.setSessionAttr(bpc.request, "curMarrChildNumOption", (Serializable) curMarrChildNumOption);
        ParamUtil.setSessionAttr(bpc.request, "prevMarrChildNumOption", (Serializable) prevMarrChildNumOption);
        //init actionType
        String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        if (StringUtil.isEmpty(actionType)) {
            actionType = ACTION_TYPE_PAGE;
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        }
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        //init IuiCycleStageDto The default value
        arSuperDataSubmission = arDataSubmissionService.setIuiCycleStageDtoDefaultVal(arSuperDataSubmission);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmission);
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        IuiCycleStageDto iuiCycleStageDto = arSuperDataSubmission.getIuiCycleStageDto();
        if(iuiCycleStageDto == null) {
            iuiCycleStageDto = new IuiCycleStageDto();
        }
        //get form value set dto
        iuiCycleStageDto = getIuiCycleFormValue(iuiCycleStageDto, bpc.request);
        arSuperDataSubmission.setIuiCycleStageDto(iuiCycleStageDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(iuiCycleStageDto, "common");
        if (validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, CommonDelegator.ACTION_TYPE_PAGE);
        } else {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, CommonDelegator.ACTION_TYPE_CONFIRM);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmission);
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
        return iuiCycleStageDto;
    }

    private IuiCycleStageDto setSourceOfSemenAllValue(IuiCycleStageDto iuiCycleStageDto, List<String> sourceOfSemenList) {
        iuiCycleStageDto.setSemenSources(sourceOfSemenList);
        if(!IaisCommonUtils.isEmpty(sourceOfSemenList)) {
            if(sourceOfSemenList.contains("AR_SOS_003")) {
                iuiCycleStageDto.setFromDonorFlag(true);
            } else {
                iuiCycleStageDto.setFromDonorFlag(false);
            }
            if(sourceOfSemenList.contains("AR_SOS_001")) {
                iuiCycleStageDto.setFromHusbandFlag(true);
            } else {
                iuiCycleStageDto.setFromHusbandFlag(false);
            }
            if(sourceOfSemenList.contains("AR_SOS_002")) {
                iuiCycleStageDto.setFromHusbandTissueFlag(true);
            } else {
                iuiCycleStageDto.setFromHusbandTissueFlag(false);
            }
        }
        return iuiCycleStageDto;
    }
}
