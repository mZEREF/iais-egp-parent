package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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
    private GenerateIdClient generateIdClient;

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Drug Practices</strong>");
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.DP_DATA_SUBMISSION, dpSuperDataSubmissionDto);

    }
    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
        dpSuperDataSubmissionDto = dpSuperDataSubmissionDto  == null ? new DpSuperDataSubmissionDto() : dpSuperDataSubmissionDto;
        PatientDto patientDto = dpSuperDataSubmissionDto.getPatientDto() == null ? new PatientDto() : dpSuperDataSubmissionDto.getPatientDto();
        ControllerHelper.get(request,patientDto);
        patientDto.setPatientType(DataSubmissionConsts.DS_PATIENT_DRP);
        if (StringUtil.isEmpty(patientDto.getEthnicGroup())) {
            patientDto.setEthnicGroup("");
        }
        if(StringUtil.isEmpty(patientDto.getOrgId())){
            patientDto.setOrgId(dpSuperDataSubmissionDto.getOrgId());
        }
        String patientCode = Optional.ofNullable(dpSuperDataSubmissionDto.getPatientDto())
                .map(dto -> dto.getPatientCode())
                .orElseGet(() -> generateIdClient.getSeqId().getEntity());
        patientDto.setPatientCode(patientCode);
        dpSuperDataSubmissionDto.setPatientDto(patientDto);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.DP_DATA_SUBMISSION, dpSuperDataSubmissionDto);
        validatePageData(request, patientDto,"DRP",ACTION_TYPE_CONFIRM);
    }
    @Override
    public void prepareConfim(BaseProcessClass bpc) {
    }
}
