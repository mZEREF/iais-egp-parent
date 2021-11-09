package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EndCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * EndCycleDelegator
 *
 * @author zhixing
 * @date 2021/11/5
 */

@Delegator("endCycleDelegator")
@Slf4j
public class endCycleDelegator extends CommonDelegator{
    @Override
    public void start(BaseProcessClass bpc) {

    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("crud_action_type is ======>"+ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE)));
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stage</strong>");
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);

        arSuperDataSubmissionDto = arSuperDataSubmissionDto == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        EndCycleStageDto endCycleStageDto =
                arSuperDataSubmissionDto.getEndCycleStageDto() == null ? new EndCycleStageDto() : arSuperDataSubmissionDto.getEndCycleStageDto();

        String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (CommonDelegator.ACTION_TYPE_CONFIRM.equals(actionType)) {
            String cycleAbandoned = ParamUtil.getString(bpc.request, "cycleAbandoned");
            String abandonReasonSelect = ParamUtil.getRequestString(bpc.request, "abandonReasonSelect");
            String otherAbandonReason = ParamUtil.getRequestString(bpc.request, "otherAbandonReason");
            endCycleStageDto.setCycleAbandoned(Boolean.parseBoolean(cycleAbandoned));
            endCycleStageDto.setAbandonReason(abandonReasonSelect);
            if (otherAbandonReason != null && "ENDRA005".equals(abandonReasonSelect)) {
                endCycleStageDto.setOtherAbandonReason(otherAbandonReason);
            }
            arSuperDataSubmissionDto.setEndCycleStageDto(endCycleStageDto);
            ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
            validatePageData(bpc.request, endCycleStageDto, "save", ACTION_TYPE_CONFIRM);
        }
    }
}
