package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArTreatmentSubsidiesStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
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
            arTreatmentSubsidiesStageDto.setIsThereAppeal(false);
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
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto = arSuperDataSubmissionDto.getArTreatmentSubsidiesStageDto();
        String coFunding = arTreatmentSubsidiesStageDto.getCoFunding();
        int freshCount = ParamUtil.getInt(bpc.request,"freshCount");
        int frozenCount = ParamUtil.getInt(bpc.request,"frozenCount");
        boolean isDisplayAppeal = ("ATSACF002".equals(coFunding) && freshCount >= 3) ||
                ("ATSACF003".equals(coFunding) && frozenCount >= 3);
        ParamUtil.setRequestAttr(bpc.request, "isDisplayAppeal",isDisplayAppeal);

        PatientInventoryDto patientInventoryDto = DataSubmissionHelper.initPatientInventoryTable(bpc.request);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>AR Treatment Subsidies Stage</strong>");
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
}
