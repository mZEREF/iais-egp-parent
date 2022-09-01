package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

/**
 * OutcomeDelegator
 *
 * @author zhixing
 * @date 2021/11/4
 */

@Delegator("outcomeDelegator")
@Slf4j
public class OutcomeDelegator extends CommonDelegator{
    public static final String OUTCOME_OF_EMBRYO_TRANSFERREDS = "OutcomeEmbryoTransferreds";

    @Autowired
    private OutcomePregnancyDelegator outcomePregnancyDelegator;
    @Autowired
    private OutcomeEmbryoTransferredDelegator outcomeEmbryoTransferredDelegator;

    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,OUTCOME_OF_EMBRYO_TRANSFERREDS, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.OUTCOME_OF_EMBRYO_TRANSFERRED));
    }
    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("crud_action_type is ======>"+ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE)));
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
        outcomePregnancyDelegator.preparePage(bpc);
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);

        arSuperDataSubmissionDto = arSuperDataSubmissionDto == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        OutcomeStageDto outcomeStageDto =
                arSuperDataSubmissionDto.getOutcomeStageDto() == null ? new OutcomeStageDto() : arSuperDataSubmissionDto.getOutcomeStageDto();
            String pregnancyDetected = ParamUtil.getString(bpc.request, "pregnancyDetected");
            outcomeStageDto.setPregnancyDetected(Boolean.valueOf(pregnancyDetected));
            arSuperDataSubmissionDto.setOutcomeStageDto(outcomeStageDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(outcomeStageDto, "save");

            Map<String, String> errorMap = validationResult.retrieveAll();
            if ("true".equals(pregnancyDetected)) {
                HttpServletRequest request = bpc.request;
                EmbryoTransferredOutcomeStageDto embryoTransferredOutcomeStageDto =
                        arSuperDataSubmissionDto.getEmbryoTransferredOutcomeStageDto() == null ? new EmbryoTransferredOutcomeStageDto() : arSuperDataSubmissionDto.getEmbryoTransferredOutcomeStageDto();
                PregnancyOutcomeStageDto pregnancyOutcomeStageDto =
                        arSuperDataSubmissionDto.getPregnancyOutcomeStageDto() == null ? new PregnancyOutcomeStageDto() : arSuperDataSubmissionDto.getPregnancyOutcomeStageDto();
                ControllerHelper.get(request, embryoTransferredOutcomeStageDto);
                outcomePregnancyDelegator.fromPageData(pregnancyOutcomeStageDto, request);
                String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);
                if ("confirm".equals(crud_action_type)) {
                    ValidationResult validationResult1 = WebValidationHelper.validateProperty(embryoTransferredOutcomeStageDto, "save");
                    ValidationResult validationResult2 = WebValidationHelper.validateProperty(pregnancyOutcomeStageDto, "save");

                    errorMap = validationResult1.retrieveAll();
                    errorMap.putAll(validationResult2.retrieveAll());
                    verifyRfcCommon(request, errorMap);
                    if(errorMap.isEmpty()){
                        outcomeEmbryoTransferredDelegator.valRFC(request, embryoTransferredOutcomeStageDto);
                        outcomePregnancyDelegator.valRFC(request, pregnancyOutcomeStageDto);

                    }
                    if (!errorMap.isEmpty()) {
                        WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                        ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMAP, errorMap);
                        ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                        ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
                    }
                }
                arSuperDataSubmissionDto.setEmbryoTransferredOutcomeStageDto(embryoTransferredOutcomeStageDto);
            }

            if(StringUtil.isEmpty(pregnancyDetected)){
                errorMap.put("pregnancyDetected" ,"GENERAL_ERR0006");
            }
            String crud_action_type = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);

            if ("confirm".equals(crud_action_type)) {
                errorMap = validationResult.retrieveAll();
                verifyRfcCommon(bpc.request, errorMap);
                if(errorMap.isEmpty()){
                    valRFC(bpc.request, outcomeStageDto);
                }
            }

            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
                return;
            }
        if(CommonDelegator.ACTION_TYPE_CONFIRM.equals(ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE))){
            valRFC(bpc.request,outcomeStageDto);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }
    protected void valRFC(HttpServletRequest request, OutcomeStageDto outcomeStageDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getOutcomeStageDto()!= null && outcomeStageDto.equals(arOldSuperDataSubmissionDto.getOutcomeStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
