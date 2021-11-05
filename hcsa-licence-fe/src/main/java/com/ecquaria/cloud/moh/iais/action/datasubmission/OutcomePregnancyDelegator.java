package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * OutcomePregnancyDelegator
 *
 * @author jiawei
 * @date 2021/11/2
 */

@Delegator("outcomePregnancyDelegator")
public class OutcomePregnancyDelegator extends CommonDelegator {
    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Outcome of Pregnancy Stage</strong>");
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PregnancyOutcomeStageDto pregnancyOutcomeStageDto = arSuperDataSubmissionDto.getPregnancyOutcomeStageDto();
        if (pregnancyOutcomeStageDto == null) {
            pregnancyOutcomeStageDto = new PregnancyOutcomeStageDto();
            pregnancyOutcomeStageDto.setL2CareBabyNum(0);
            pregnancyOutcomeStageDto.setL3CareBabyNum(0);
            arSuperDataSubmissionDto.setPregnancyOutcomeStageDto(pregnancyOutcomeStageDto);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        }

        List<SelectOption> firstUltrasoundOrderShowSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ORDER_IN_ULTRASOUND);
        ParamUtil.setRequestAttr(bpc.request, "firstUltrasoundOrderShowSelectOption", firstUltrasoundOrderShowSelectOption);

        List<SelectOption> pregnancyOutcomeSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_OUTCOME_OFPREGNANCY);
        ParamUtil.setRequestAttr(bpc.request, "pregnancyOutcomeSelectOption", pregnancyOutcomeSelectOption);

        List<SelectOption> deliveryModeSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MODE_OF_DELIVERY);
        ParamUtil.setRequestAttr(bpc.request, "deliveryModeSelectOption", deliveryModeSelectOption);

        List<SelectOption> birthWeightSelectOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BABY_BIRTH_WEIGHT);
        ParamUtil.setRequestAttr(bpc.request, "birthWeightSelectOption", birthWeightSelectOption);

        List<SelectOption> transferNumSelectOption = IaisCommonUtils.genNewArrayList();
        String[] options = new String[]{"0","1","2","3","4","5","6"};
        for (String item : options){
            transferNumSelectOption.add(new SelectOption(item, item));
        }
        ParamUtil.setRequestAttr(bpc.request, "NICUCareBabyNumSelectOption", transferNumSelectOption);
        ParamUtil.setRequestAttr(bpc.request, "l2CareBabyNumSelectOption", transferNumSelectOption);
        ParamUtil.setRequestAttr(bpc.request, "l3CareBabyNumSelectOption", transferNumSelectOption);
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        PregnancyOutcomeStageDto pregnancyOutcomeStageDto = arSuperDataSubmissionDto.getPregnancyOutcomeStageDto();
        HttpServletRequest request = bpc.request;
        fromPageData(pregnancyOutcomeStageDto, request);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);

        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(pregnancyOutcomeStageDto, "save");
            errorMap = validationResult.retrieveAll();
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
    }

    private void fromPageData(PregnancyOutcomeStageDto thawingStageDto, HttpServletRequest request) {
    }

    private int getInt(HttpServletRequest request, String param) {
        String s = ParamUtil.getString(request, param);
        int result = 0;
        if (StringUtil.isNotEmpty(s)) {
            result = Integer.valueOf(s);
        }
        return result;
    }
}
