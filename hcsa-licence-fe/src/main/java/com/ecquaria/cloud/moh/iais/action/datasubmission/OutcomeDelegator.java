package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
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

    @Override
    public void start(BaseProcessClass bpc) {

    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

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
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);

            arSuperDataSubmissionDto = arSuperDataSubmissionDto  == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
            OutcomeStageDto outcomeStageDto =
                arSuperDataSubmissionDto.getOutcomeStageDto() == null ? new OutcomeStageDto() : arSuperDataSubmissionDto.getOutcomeStageDto();

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

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
