package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2021/10/29 14:12
 **/
@Delegator("freezingDelegator")
@Slf4j
public class FreezingStageDelegator extends CommonDelegator {
    private static final String SUBMIT_FLAG = "freezeStgSubmitFlag__attr";

    @Override
    public void start(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, null);
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ArSubFreezingStageDto arSubFreezingStageDto = arSuperDataSubmission.getArSubFreezingStageDto();
        ArChangeInventoryDto arChangeInventoryDto = new ArChangeInventoryDto();
        arSuperDataSubmission.setArChangeInventoryDto(arChangeInventoryDto);
        if (arSubFreezingStageDto != null) {
            if ("1".equals(arSubFreezingStageDto.getIsFreshOocyte())) {
                int cryopreservedNum = Integer.parseInt(arSubFreezingStageDto.getFreshOocyteCryopNum());
                arChangeInventoryDto.setFreshOocyteNum(-1 * cryopreservedNum);
                arChangeInventoryDto.setFrozenOocyteNum(cryopreservedNum);
            }
            if ("1".equals(arSubFreezingStageDto.getIsThawedOocyte())) {
                int cryopreservedNum = Integer.parseInt(arSubFreezingStageDto.getThawedOocyteCryopNum());
                arChangeInventoryDto.setThawedOocyteNum(-1 * cryopreservedNum);
                arChangeInventoryDto.setFrozenOocyteNum(cryopreservedNum);
            }
            if ("1".equals(arSubFreezingStageDto.getIsFreshEmbryo())) {
                int cryopreservedNum = Integer.parseInt(arSubFreezingStageDto.getFreshEmbryoCryopNum());
                arChangeInventoryDto.setFreshEmbryoNum(-1 * cryopreservedNum);
                arChangeInventoryDto.setFrozenEmbryoNum(cryopreservedNum);
            }
            if ("1".equals(arSubFreezingStageDto.getIsThawedEmbryo())) {
                int cryopreservedNum = Integer.parseInt(arSubFreezingStageDto.getThawedEmbryoCryopNum());
                arChangeInventoryDto.setThawedEmbryoNum(-1 * cryopreservedNum);
                arChangeInventoryDto.setFrozenEmbryoNum(cryopreservedNum);
            }
        }
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Fertilisation Stage</strong>");
        String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        if (StringUtil.isEmpty(actionType)) {
            actionType = ACTION_TYPE_PAGE;
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        }
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        //init FreeStageDto The default value
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ArSubFreezingStageDto arSubFreezingStageDto = arSuperDataSubmission.getArSubFreezingStageDto();
        if (arSubFreezingStageDto == null) {
            arSubFreezingStageDto = new ArSubFreezingStageDto();
        }
        String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        //get value by form page
        String isFreshOocyte = ParamUtil.getString(bpc.request, "isFreshOocyte");
        String isThawedOocyte = ParamUtil.getString(bpc.request, "isThawedOocyte");
        String isFreshEmbryo = ParamUtil.getString(bpc.request, "isFreshEmbryo");
        String isThawedEmbryo = ParamUtil.getString(bpc.request, "isThawedEmbryo");
        String freshOocyteCryopNum = ParamUtil.getRequestString(bpc.request, "freshOocyteCryopNum");
        String thawedOocyteCryopNum = ParamUtil.getRequestString(bpc.request, "thawedOocyteCryopNum");
        String freshEmbryoCryopNum = ParamUtil.getRequestString(bpc.request, "freshEmbryoCryopNum");
        String thawedEmbryoCryopNum = ParamUtil.getRequestString(bpc.request, "thawedEmbryoCryopNum");
        String cryopreservationDate = ParamUtil.getRequestString(bpc.request, "cryopreservationDate");

        if (StringUtil.isNotEmpty(isFreshOocyte)) {
            arSubFreezingStageDto.setIsFreshOocyte(isFreshOocyte);
        } else {
            arSubFreezingStageDto.setIsFreshOocyte("0");
        }
        if (StringUtil.isNotEmpty(isThawedOocyte)) {
            arSubFreezingStageDto.setIsThawedOocyte(isThawedOocyte);
        } else {
            arSubFreezingStageDto.setIsThawedOocyte("0");
        }
        if (StringUtil.isNotEmpty(isFreshEmbryo)) {
            arSubFreezingStageDto.setIsFreshEmbryo(isFreshEmbryo);
        } else {
            arSubFreezingStageDto.setIsFreshEmbryo("0");
        }
        if (StringUtil.isNotEmpty(isThawedEmbryo)) {
            arSubFreezingStageDto.setIsThawedEmbryo(isThawedEmbryo);
        } else {
            arSubFreezingStageDto.setIsThawedEmbryo("0");
        }

        arSubFreezingStageDto.setFreshOocyteCryopNum(freshOocyteCryopNum);
        arSubFreezingStageDto.setThawedOocyteCryopNum(thawedOocyteCryopNum);
        arSubFreezingStageDto.setFreshEmbryoCryopNum(freshEmbryoCryopNum);
        arSubFreezingStageDto.setThawedEmbryoCryopNum(thawedEmbryoCryopNum);




        try {
            Date date = Formatter.parseDate(cryopreservationDate);
            arSubFreezingStageDto.setCryopreservedDate(date);
        } catch (Exception e) {
            arSubFreezingStageDto.setCryopreservedDate(null);
            log.info("Freezing invalid cryopreservationDate");
        }


        arSuperDataSubmission.setArSubFreezingStageDto(arSubFreezingStageDto);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        if (CommonDelegator.ACTION_TYPE_CONFIRM.equals(actionType)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(arSubFreezingStageDto, "common");
            errorMap = validationResult.retrieveAll();
            verifyCommon(bpc.request, errorMap);
            if(errorMap.isEmpty()){
                valRFC(bpc.request, arSubFreezingStageDto);
            }
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, CommonDelegator.ACTION_TYPE_PAGE);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmission);
    }

    @Override
    public void doSubmission(BaseProcessClass bpc) {
        String submitFlag = (String) ParamUtil.getSessionAttr(bpc.request, SUBMIT_FLAG);
        if (!StringUtil.isEmpty(submitFlag)) {
            throw new IaisRuntimeException("Double Submit");
        }
        super.doSubmission(bpc);
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, AppConsts.YES);
    }

    protected void valRFC(HttpServletRequest request, ArSubFreezingStageDto arSubFreezingStageDto) {
        if (isRfc(request)) {
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if (arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getArSubFreezingStageDto() != null && arSubFreezingStageDto.equals(arOldSuperDataSubmissionDto.getArSubFreezingStageDto())) {
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            }
        }
    }
}
