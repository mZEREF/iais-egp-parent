package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Map;

public class OutcomeStageDelegator extends CommonDelegator{
    @Override
    public void start(BaseProcessClass bpc) {

    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {


    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        OutcomeStageDto outcomeStageDto=arSuperDataSubmissionDto.getOutcomeStageDto();
        if(outcomeStageDto == null) {
            outcomeStageDto = new OutcomeStageDto();
        }
        String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (CommonDelegator.ACTION_TYPE_CONFIRM.equals(actionType)) {
            String detectedRadio = ParamUtil.getString(bpc.request, "detectedRadio");
            outcomeStageDto.setPregnancyDetected(Boolean.parseBoolean(detectedRadio));

            arSuperDataSubmissionDto.setOutcomeStageDto(outcomeStageDto);

            ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);

            ValidationResult validationResult = WebValidationHelper.validateProperty(outcomeStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();

            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
                return;
            }
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "confirm");
        }
    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
