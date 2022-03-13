package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * DpPatientInfoDelegator
 *
 * @author fanghao
 * @date 2021/11/25
 */


@Delegator("dpPatientInfoDelegator")
@Slf4j
public class DpPatientInfoDelegator extends DpCommonDelegator {

    @Autowired
    private PatientService patientService;

    public void start(BaseProcessClass bpc) {
        bpc.request.removeAttribute(IaisEGPConstant.CRUD_ACTION_TYPE);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Drug Practices</strong>");
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
        if (dpSuperDataSubmissionDto == null) {
            dpSuperDataSubmissionDto = new DpSuperDataSubmissionDto();
        }
        String cycleStages=dpSuperDataSubmissionDto.getCycleDto().getCycleType();
        if (cycleStages.equals("DSCL_005")) {
            cycleStages = DataSubmissionConsts.DS_CYCLE_PATIENT_DRP;
            dpSuperDataSubmissionDto.getCycleDto().setCycleType(cycleStages);
        }
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.DP_DATA_SUBMISSION, dpSuperDataSubmissionDto);

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
        dpSuperDataSubmissionDto = dpSuperDataSubmissionDto == null ? new DpSuperDataSubmissionDto() : dpSuperDataSubmissionDto;
        PatientDto patientDto = dpSuperDataSubmissionDto.getPatientDto() == null ? new PatientDto() : dpSuperDataSubmissionDto.getPatientDto();
        ControllerHelper.get(request, patientDto);
        patientDto.setPatientType(DataSubmissionConsts.DS_PATIENT_DRP);
        if (StringUtil.isEmpty(patientDto.getEthnicGroup())) {
            patientDto.setEthnicGroup("");
        }
        if (StringUtil.isNotEmpty(patientDto.getEthnicGroup()) && !DataSubmissionConsts.ETHNIC_GROUP_OTHER.equals(patientDto.getEthnicGroup())) {
            patientDto.setEthnicGroupOther(null);
        }
        if (StringUtil.isEmpty(patientDto.getOrgId())) {
            patientDto.setOrgId(dpSuperDataSubmissionDto.getOrgId());
        }
        patientDto.setPatientCode(patientService.getPatientCode(patientDto.getPatientCode()));
        dpSuperDataSubmissionDto.setPatientDto(patientDto);
        validatePageData(request, patientDto, "DRP", ACTION_TYPE_CONFIRM);
        if (DpCommonDelegator.ACTION_TYPE_CONFIRM.equals(ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE))) {
            valRFC(request, patientDto);
        }
        DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, request);
    }

    protected void valRFC(HttpServletRequest request, PatientDto patientDto) {
        if (isRfc(request)) {
            DpSuperDataSubmissionDto dpOldSuperDataSubmissionDto = DataSubmissionHelper.getOldDpSuperDataSubmissionDto(request);
            if (dpOldSuperDataSubmissionDto != null && dpOldSuperDataSubmissionDto.getPatientDto() != null && patientDto.equals(dpOldSuperDataSubmissionDto.getPatientDto())) {
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            }
        }
    }
}
