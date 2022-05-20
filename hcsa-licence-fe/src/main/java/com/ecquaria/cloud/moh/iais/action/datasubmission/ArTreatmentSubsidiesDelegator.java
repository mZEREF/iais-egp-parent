package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArTreatmentSubsidiesStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * ArTreatmentSubsidiesDelegator
 *
 * @author jiawei
 * @date 2021/11/1
 */
@Delegator("arTreatmentSubsidiesDelegator")
public class ArTreatmentSubsidiesDelegator extends CommonDelegator {
    @Autowired
    private ArFeClient arFeClient;

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto = arSuperDataSubmissionDto.getArTreatmentSubsidiesStageDto();
        if (arTreatmentSubsidiesStageDto == null) {
            arTreatmentSubsidiesStageDto = new ArTreatmentSubsidiesStageDto();
            arTreatmentSubsidiesStageDto.setCoFunding("ATSACF001");
            arTreatmentSubsidiesStageDto.setIsThereAppeal(Boolean.FALSE);
            arSuperDataSubmissionDto.setArTreatmentSubsidiesStageDto(arTreatmentSubsidiesStageDto);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        }

        List<SelectOption> artCoFundingOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ATS_ART_CO_FUNDING);
        ParamUtil.setRequestAttr(bpc.request, "artCoFundingOptions", artCoFundingOptions);

        CycleDto cycleDto = arSuperDataSubmissionDto.getCycleDto();
        List<ArTreatmentSubsidiesStageDto> oldArTreatmentSubsidiesStageDtos = arFeClient.getArTreatmentSubsidiesStagesByPatientInfo(cycleDto.getPatientCode(), cycleDto.getHciCode(), cycleDto.getCycleType()).getEntity();
        int freshCount = 0;
        int frozenCount = 0;
        for (ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto1 : oldArTreatmentSubsidiesStageDtos) {
            if ("ATSACF002".equals(arTreatmentSubsidiesStageDto1.getCoFunding())) {
                freshCount++;
            } else if ("ATSACF003".equals(arTreatmentSubsidiesStageDto1.getCoFunding())) {
                frozenCount++;
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "freshCount", freshCount);
        ParamUtil.setRequestAttr(bpc.request, "frozenCount", frozenCount);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>AR Treatment Co-funding Stage</strong>");
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto = arSuperDataSubmissionDto.getArTreatmentSubsidiesStageDto();
        HttpServletRequest request = bpc.request;
        fromPageData(arTreatmentSubsidiesStageDto, request);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);

        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(arTreatmentSubsidiesStageDto, "save");
            errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);
            if(errorMap.isEmpty()){
                valRFC(request, arSuperDataSubmissionDto.getArTreatmentSubsidiesStageDto());
            }
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
    }

    private void fromPageData(ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto, HttpServletRequest request) {
        String coFunding = ParamUtil.getString(request, "coFunding");
        Boolean isThereAppeal = "true".equals(ParamUtil.getString(request, "isThereAppeal"));

        arTreatmentSubsidiesStageDto.setCoFunding(coFunding);
        arTreatmentSubsidiesStageDto.setIsThereAppeal(isThereAppeal);
    }

    protected void valRFC(HttpServletRequest request, ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto) {
        if (isRfc(request)) {
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if (arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getArTreatmentSubsidiesStageDto() != null && arTreatmentSubsidiesStageDto.equals(arOldSuperDataSubmissionDto.getArTreatmentSubsidiesStageDto())) {
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            }
        }
    }
}
