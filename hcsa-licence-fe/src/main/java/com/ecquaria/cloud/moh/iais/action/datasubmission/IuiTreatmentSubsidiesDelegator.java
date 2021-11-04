package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiTreatmentSubsidiesDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
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
import java.util.Map;

/**
 * IuiTreatmentSubsidiesDelegator
 *
 * @author zhixing
 * @date 2021/10/28
 */

@Delegator("iuiTreatmentSubsidiesDelegator")
@Slf4j
public class IuiTreatmentSubsidiesDelegator extends CommonDelegator {

    public static final String PLEASE_INDICATE_IUI_CO_FUNDING = "pleaseIndicateIuiCoFunding";
    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        IuiTreatmentSubsidiesDto iuiTreatmentSubsidiesDto=arSuperDataSubmissionDto.getIuiTreatmentSubsidiesDto();
        if(iuiTreatmentSubsidiesDto == null) {
            iuiTreatmentSubsidiesDto = new IuiTreatmentSubsidiesDto();
        }
        iuiTreatmentSubsidiesDto.setArtCoFunding("PICF001");
        arSuperDataSubmissionDto.setIuiTreatmentSubsidiesDto(iuiTreatmentSubsidiesDto);
        ParamUtil.setSessionAttr(request,PLEASE_INDICATE_IUI_CO_FUNDING, (Serializable) MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.PLEASE_INDICATE_IUI_CO_FUNDING));
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {


    }

    @Override

    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        IuiTreatmentSubsidiesDto iuiTreatmentSubsidiesDto=arSuperDataSubmissionDto.getIuiTreatmentSubsidiesDto();
        if(iuiTreatmentSubsidiesDto == null) {
            iuiTreatmentSubsidiesDto = new IuiTreatmentSubsidiesDto();
        }
        String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (CommonDelegator.ACTION_TYPE_CONFIRM.equals(actionType)) {
            String pleaseIndicateIui = ParamUtil.getString(bpc.request, "pleaseIndicateIui");
            iuiTreatmentSubsidiesDto.setArtCoFunding(pleaseIndicateIui);

            arSuperDataSubmissionDto.setIuiTreatmentSubsidiesDto(iuiTreatmentSubsidiesDto);

            ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);

            ValidationResult validationResult = WebValidationHelper.validateProperty(iuiTreatmentSubsidiesDto, "save");
            Map<String, String> errorMap = validationResult.retrieveAll();

            if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "page");
                return;
            }
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "confirm");
        }
    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
