package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/10/29 14:12
 **/
@Delegator("freezingDelegator")
@Slf4j
public class FreezingStageDelegator extends CommonDelegator {


    @Override
    public void start(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, "arFreeCryoOptions", null);
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ArSubFreezingStageDto arSubFreezingStageDto = arSuperDataSubmission.getArSubFreezingStageDto();
        ArChangeInventoryDto arChangeInventoryDto = new ArChangeInventoryDto();
        arSuperDataSubmission.setArChangeInventoryDto(arChangeInventoryDto);
        if (arChangeInventoryDto != null && arSubFreezingStageDto != null) {
            String cryopreservedType = arSubFreezingStageDto.getCryopreservedType();
            int cryopreservedNum = Integer.parseInt(arSubFreezingStageDto.getCryopreservedNum());
            if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_FRESH_OOCYTE.equals(cryopreservedType)) {
                arChangeInventoryDto.setFreshOocyteNum(-1 * cryopreservedNum);
                arChangeInventoryDto.setFrozenOocyteNum(cryopreservedNum);
            } else if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_FRESH_EMBRYO.equals(cryopreservedType)) {
                arChangeInventoryDto.setFreshEmbryoNum(-1 * cryopreservedNum);
                arChangeInventoryDto.setFrozenEmbryoNum(cryopreservedNum);
            } else if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_THAWED_OOCYTE.equals(cryopreservedType)) {
                arChangeInventoryDto.setThawedOocyteNum(-1 * cryopreservedNum);
                arChangeInventoryDto.setFrozenOocyteNum(cryopreservedNum);
            } else if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_THAWED_EMBRYO.equals(cryopreservedType)) {
                arChangeInventoryDto.setThawedEmbryoNum(-1 * cryopreservedNum);
                arChangeInventoryDto.setFrozenEmbryoNum(cryopreservedNum);
            }
        }
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Fertilisation Stage</strong>");
        List<SelectOption> freeCryoOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.AR_FREE_CRYO_OPTIONS);
        ParamUtil.setSessionAttr(bpc.request, "arFreeCryoOptions", (Serializable) freeCryoOptions);
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
        DataSubmissionHelper.setCurrentArDataSubmission(arDataSubmissionService.setFreeStageDtoDefaultVal(arSuperDataSubmission), request);
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
        String[] freeCryoRadios = ParamUtil.getStrings(bpc.request, "freeCryoRadio");
        String cryopreservedNum = ParamUtil.getRequestString(bpc.request, "cryopreservedNum");
        String cryopreservationDate = ParamUtil.getRequestString(bpc.request, "cryopreservationDate");
        //set cryopreservedNum and cryopreservationDate
        arSubFreezingStageDto = arDataSubmissionService.setFreeCryoNumAndDate(arSubFreezingStageDto, cryopreservedNum, cryopreservationDate);
        if (freeCryoRadios != null && freeCryoRadios.length > 0) {
            //Verify that the value is dirty data
            List<SelectOption> freeCryoOptions = (List<SelectOption>) ParamUtil.getSessionAttr(bpc.request, "arFreeCryoOptions");
            arSubFreezingStageDto = arDataSubmissionService.checkValueIsDirtyData(freeCryoRadios[0], arSubFreezingStageDto, freeCryoOptions);
        }

        arSuperDataSubmission.setArSubFreezingStageDto(arSubFreezingStageDto);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        if (CommonDelegator.ACTION_TYPE_CONFIRM.equals(actionType)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(arSubFreezingStageDto, "common");
            errorMap = validationResult.retrieveAll();
            verifyRfcCommon(bpc.request, errorMap);
            valRFC(bpc.request, arSubFreezingStageDto);
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, CommonDelegator.ACTION_TYPE_PAGE);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmission);
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
