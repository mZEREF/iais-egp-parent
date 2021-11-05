package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EndCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

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
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
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
            String abandonReason = ParamUtil.getRequestString(bpc.request, "abandonReason");
            String otherAbandonReason = ParamUtil.getRequestString(bpc.request, "otherAbandonReason");
            endCycleStageDto.setCycleAbandoned(Boolean.parseBoolean(cycleAbandoned));
            endCycleStageDto.setAbandonReason(abandonReason);
            endCycleStageDto.setOtherAbandonReason(otherAbandonReason);
            if (otherAbandonReason != null && "ENDRA005".equals(abandonReason)) {
                endCycleStageDto.setOtherAbandonReason(otherAbandonReason);
            }
            arSuperDataSubmissionDto.setEndCycleStageDto(endCycleStageDto);
            ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
            validatePageData(bpc.request, endCycleStageDto, "save", ACTION_TYPE_CONFIRM);
        }
    }
}
