package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoTransferredOutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
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

/**
 * OutcomeEmbryoTransferredDelegator
 *
 * @author fanghao
 * @date 2021/11/2
 */
@Delegator("outcomeEmbryoTransferredDelegator")
@Slf4j
public class OutcomeEmbryoTransferredDelegator extends CommonDelegator{
    public static final String OUTCOME_OF_EMBRYO_TRANSFERREDS = "OutcomeEmbryoTransferreds";
    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,OUTCOME_OF_EMBRYO_TRANSFERREDS, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.OUTCOME_OF_EMBRYO_TRANSFERRED));
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
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
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);

        arSuperDataSubmissionDto = arSuperDataSubmissionDto  == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        EmbryoTransferredOutcomeStageDto embryoTransferredOutcomeStageDto =
                arSuperDataSubmissionDto.getEmbryoTransferredOutcomeStageDto() == null ? new EmbryoTransferredOutcomeStageDto() : arSuperDataSubmissionDto.getEmbryoTransferredOutcomeStageDto();
        ControllerHelper.get(request,embryoTransferredOutcomeStageDto);
        String transferedOutcome = ParamUtil.getRequestString(request, "transferedOutcome");
        embryoTransferredOutcomeStageDto.setTransferedOutcome(transferedOutcome);
        arSuperDataSubmissionDto.setEmbryoTransferredOutcomeStageDto(embryoTransferredOutcomeStageDto);
        validatePageData(request, embryoTransferredOutcomeStageDto,"save",ACTION_TYPE_CONFIRM);
        if(CommonDelegator.ACTION_TYPE_CONFIRM.equals(ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE))){
            valRFC(request,embryoTransferredOutcomeStageDto);
        }
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }
    protected void valRFC(HttpServletRequest request, EmbryoTransferredOutcomeStageDto embryoTransferredOutcomeStageDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getEmbryoTransferredOutcomeStageDto()!= null && embryoTransferredOutcomeStageDto.equals(arOldSuperDataSubmissionDto.getEmbryoTransferredOutcomeStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
