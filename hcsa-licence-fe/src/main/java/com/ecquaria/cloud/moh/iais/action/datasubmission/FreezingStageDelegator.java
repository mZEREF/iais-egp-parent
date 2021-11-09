package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Override
    public void start(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, "arFreeCryoOptions", null);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
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
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if(arSuperDataSubmission != null) {
            ArSubFreezingStageDto arSubFreezingStageDto = arSuperDataSubmission.getArSubFreezingStageDto();
            if(arSubFreezingStageDto == null) {
                arSubFreezingStageDto = new ArSubFreezingStageDto();
            }
            String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if(CommonDelegator.ACTION_TYPE_CONFIRM.equals(actionType)) {
                //get value by form page
                String[] freeCryoRadios = ParamUtil.getStrings(bpc.request, "freeCryoRadio");
                String cryopreservedNum = ParamUtil.getRequestString(bpc.request, "cryopreservedNum");
                String cryopreservationDate = ParamUtil.getRequestString(bpc.request, "cryopreservationDate");
                //set cryopreservedNum and cryopreservationDate
                arSubFreezingStageDto = arDataSubmissionService.setFreeCryoNumAndDate(arSubFreezingStageDto, cryopreservedNum, cryopreservationDate);
                if (freeCryoRadios != null && freeCryoRadios.length > 0) {
                    //Verify that the value is dirty data
                    List<SelectOption> freeCryoOptions = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "arFreeCryoOptions");
                    arSubFreezingStageDto = arDataSubmissionService.checkValueIsDirtyData(freeCryoRadios[0], arSubFreezingStageDto, freeCryoOptions);
                }
                arSuperDataSubmission.setArSubFreezingStageDto(arSubFreezingStageDto);
                ValidationResult validationResult = WebValidationHelper.validateProperty(arSubFreezingStageDto, "common");
                if (validationResult.isHasErrors()) {
                    Map<String, String> errorMap = validationResult.retrieveAll();
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, CommonDelegator.ACTION_TYPE_PAGE);
                } else {
                    PatientInventoryDto patientInventoryDto = DataSubmissionHelper.initPatientInventoryTable(bpc.request);
                    patientInventoryDto = arDataSubmissionService.setFreezingPatientChange(patientInventoryDto, arSubFreezingStageDto);
                    ParamUtil.setRequestAttr(bpc.request, "patientInventoryDto", patientInventoryDto);
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, CommonDelegator.ACTION_TYPE_CONFIRM);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmission);
    }


}
