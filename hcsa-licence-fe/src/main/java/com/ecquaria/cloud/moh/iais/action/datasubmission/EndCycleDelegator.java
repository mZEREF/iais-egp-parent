package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EndCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * EndCycleDelegator
 *
 * @author zhixing
 * @date 2021/11/5
 */

@Delegator("endCycleDelegator")
@Slf4j
public class EndCycleDelegator extends CommonDelegator{
    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("crud_action_type is ======>"+ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE)));
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
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

        arSuperDataSubmissionDto = arSuperDataSubmissionDto == null ? new ArSuperDataSubmissionDto() : arSuperDataSubmissionDto;
        EndCycleStageDto endCycleStageDto =
                arSuperDataSubmissionDto.getEndCycleStageDto() == null ? new EndCycleStageDto() : arSuperDataSubmissionDto.getEndCycleStageDto();
            String cycleAbandoned = ParamUtil.getString(bpc.request, "cycleAbandoned");
            String abandonReasonSelect = ParamUtil.getRequestString(bpc.request, "abandonReasonSelect");
            String otherAbandonReason = ParamUtil.getRequestString(bpc.request, "otherAbandonReason");
            endCycleStageDto.setCycleAbandoned(Boolean.valueOf(cycleAbandoned));
            endCycleStageDto.setAbandonReason(abandonReasonSelect);
            endCycleStageDto.setOtherAbandonReason(otherAbandonReason);
            arSuperDataSubmissionDto.setEndCycleStageDto(endCycleStageDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(endCycleStageDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();

            String crud_action_type = ParamUtil.getRequestString(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE);

            if ("confirm".equals(crud_action_type)) {
                errorMap = validationResult.retrieveAll();
                verifyRfcCommon(bpc.request, errorMap);
                valRFC(bpc.request, endCycleStageDto);
            }

            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
                return;
            }
        if(CommonDelegator.ACTION_TYPE_CONFIRM.equals(ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE))){
            valRFC(bpc.request,endCycleStageDto);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {
        super.pageConfirmAction(bpc);

        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (DataSubmissionConsts.DS_APP_TYPE_NEW.equals(arSuperDataSubmission.getAppType()) && ACTION_TYPE_SUBMISSION.equals(actionType)) {
            String errorMapJson = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG);
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            if (StringUtil.isNotEmpty(errorMapJson)) {
                List<Map> mapList = JsonUtil.parseToList(errorMapJson, Map.class);
                if (IaisCommonUtils.isNotEmpty(mapList)) {
                    for (Map map : mapList) {
                        errorMap.putAll(map);
                    }
                }
            }

            ArCurrentInventoryDto arCurrentInventoryDto = DataSubmissionHelper.getCurrentArCurrentInventoryDto(bpc.request);
            if (arCurrentInventoryDto != null) {
                if (arCurrentInventoryDto.getFreshOocyteNum() > 0
                        || arCurrentInventoryDto.getThawedOocyteNum() > 0
                        || arCurrentInventoryDto.getFreshEmbryoNum() > 0
                        || arCurrentInventoryDto.getThawedEmbryoNum() > 0) {
                    errorMap.put("inventoryNoZero", MessageUtil.getMessageDesc("DS_ERR017"));
                }
            }

            if (!errorMap.isEmpty()) {
                log.error("------inventory No Zero-----");
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
            }
        }
    }
    protected void valRFC(HttpServletRequest request, EndCycleStageDto endCycleStageDto){
        if(isRfc(request)){
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if(arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getEndCycleStageDto()!= null && endCycleStageDto.equals(arOldSuperDataSubmissionDto.getEndCycleStageDto())){
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,ACTION_TYPE_PAGE);
            }
        }
    }
}
