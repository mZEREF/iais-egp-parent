package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Delegator("transferInOutDelegator")
@Slf4j
public class TransferInOutDelegator extends CommonDelegator {
    public static final String WHAT_WAS_TRANSFERREDs = "transferreds";
    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,WHAT_WAS_TRANSFERREDs, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.WHAT_WAS_TRANSFERRED));

    }

    @Override
    public void returnStep(BaseProcessClass bpc) {

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
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {

    }
    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
