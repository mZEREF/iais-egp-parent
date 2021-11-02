package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoTransferStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EmbryoTransferDelegator
 *
 * @author jiawei
 * @date 11/1/2021
 */

@Delegator("embryoTransferDelegator")
@Slf4j
public class EmbryoTransferDelegator extends CommonDelegator {
    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Embryo Transferred Stage</strong>");
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        EmbryoTransferStageDto embryoTransferStageDto = arSuperDataSubmissionDto.getEmbryoTransferStageDto();
        if (embryoTransferStageDto == null) {
            embryoTransferStageDto = new EmbryoTransferStageDto();
            embryoTransferStageDto.setTransferNum(1);
            arSuperDataSubmissionDto.setEmbryoTransferStageDto(embryoTransferStageDto);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        }

        List<SelectOption> transferNumSelectOption = IaisCommonUtils.genNewArrayList();
        String[] options = new String[]{"1","2","3"};
        for (String item : options){
            transferNumSelectOption.add(new SelectOption(item, item));
        }
        ParamUtil.setRequestAttr(bpc.request, "transferNumSelectOption", transferNumSelectOption);

        List<SelectOption> firstEmbryoAgeSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_AGE_OF_EMBRYO_TRANSFER);
        ParamUtil.setRequestAttr(bpc.request, "firstEmbryoAgeSelectOption", firstEmbryoAgeSelectOption);

        List<SelectOption> secondEmbryoAgeSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_AGE_OF_EMBRYO_TRANSFER);
        ParamUtil.setRequestAttr(bpc.request, "secondEmbryoAgeSelectOption", secondEmbryoAgeSelectOption);

        List<SelectOption> thirdEmbryoAgeSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_AGE_OF_EMBRYO_TRANSFER);
        ParamUtil.setRequestAttr(bpc.request, "thirdEmbryoAgeSelectOption", thirdEmbryoAgeSelectOption);
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        EmbryoTransferStageDto embryoTransferStageDto = arSuperDataSubmissionDto.getEmbryoTransferStageDto();
        HttpServletRequest request = bpc.request;
        fromPageData(embryoTransferStageDto, request);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);

        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(embryoTransferStageDto, "save");
            errorMap = validationResult.retrieveAll();
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
    }

    private void fromPageData(EmbryoTransferStageDto embryoTransferStageDto, HttpServletRequest request) {
        Integer transferNum =  ParamUtil.getInt(request, "transferNum");

        String firstEmbryoAge = ParamUtil.getRequestString(request, "firstEmbryoAge");
        String firstEmbryoType = ParamUtil.getRequestString(request, "firstEmbryoType");

        String secondEmbryoAge = ParamUtil.getRequestString(request, "secondEmbryoAge");
        String secondEmbryoType = ParamUtil.getRequestString(request, "secondEmbryoType");

        String thirdEmbryoAge = ParamUtil.getRequestString(request, "thirdEmbryoAge");
        String thirdEmbryoType = ParamUtil.getRequestString(request, "thirdEmbryoType");

        String firstTransferDateString = ParamUtil.getRequestString(request, "firstTransferDate");
        String secondTransferDateString = ParamUtil.getRequestString(request, "secondTransferDate");
        Date firstTransferDate = DateUtil.parseDate(firstTransferDateString, AppConsts.DEFAULT_DATE_FORMAT);
        Date secondTransferDate = DateUtil.parseDate(secondTransferDateString, AppConsts.DEFAULT_DATE_FORMAT);

        embryoTransferStageDto.setTransferNum(transferNum);
        embryoTransferStageDto.setFirstEmbryoAge(firstEmbryoAge);
        embryoTransferStageDto.setFirstEmbryoType(firstEmbryoType);
        embryoTransferStageDto.setSecondEmbryoAge(secondEmbryoAge);
        embryoTransferStageDto.setSecondEmbryoType(secondEmbryoType);
        embryoTransferStageDto.setThirdEmbryoAge(thirdEmbryoAge);
        embryoTransferStageDto.setThirdEmbryoType(thirdEmbryoType);
        embryoTransferStageDto.setFirstTransferDate(firstTransferDate);
        embryoTransferStageDto.setSecondTransferDate(secondTransferDate);
    }
}
