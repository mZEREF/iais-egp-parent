package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArTreatmentSubsidiesStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ArTreatmentSubsidiesDelegator
 *
 * @author jiawei
 * @date 2021/11/1
 */
@Delegator("arTreatmentSubsidiesDelegator")
public class ArTreatmentSubsidiesDelegator extends CommonDelegator {

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto = arSuperDataSubmissionDto.getArTreatmentSubsidiesStageDto();
        if (arTreatmentSubsidiesStageDto == null) {
            arTreatmentSubsidiesStageDto = new ArTreatmentSubsidiesStageDto();
            arTreatmentSubsidiesStageDto.setCoFunding("No");
            arTreatmentSubsidiesStageDto.setIsThereAppeal(false);
            arSuperDataSubmissionDto.setArTreatmentSubsidiesStageDto(arTreatmentSubsidiesStageDto);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        }
    }

    @Override
    public void doPageConfirmAction(BaseProcessClass bpc) {
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
