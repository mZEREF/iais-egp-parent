package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
    @Autowired
    private DpDataSubmissionService dpDataSubmissionService;
    @Autowired
    private LicenceClient licenceClient;

    @Override
    public void start(BaseProcessClass bpc) {
        bpc.request.removeAttribute(IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setSessionAttr(bpc.request, SUBMIT_FLAG, null);
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
        if (cycleStages.equals("DSCL_005") || cycleStages.equals("DSCL_014")) {
            dpSuperDataSubmissionDto.setSubmissionType(DataSubmissionConsts.DP_TYPE_SBT_PATIENT_INFO);
            dpSuperDataSubmissionDto = DataSubmissionHelper.dpReNew(dpSuperDataSubmissionDto);
            DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(request,"isNew","Y");
        }
        String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dpSuperDataSubmissionDto.getDataSubmissionDto().getAppType())) {
            if ("resume".equals(actionValue)) {
                String userId = "";
                LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
                if (loginContext != null) {
                    userId = loginContext.getUserId();
                }
                dpSuperDataSubmissionDto = dpDataSubmissionService.getDpSuperDataSubmissionDtoRfcDraftByConds(
                        dpSuperDataSubmissionDto.getOrgId(), dpSuperDataSubmissionDto.getSubmissionType(), dpSuperDataSubmissionDto.getSvcName(), dpSuperDataSubmissionDto.getHciCode(), dpSuperDataSubmissionDto.getDataSubmissionDto().getId(),userId);
                if (dpSuperDataSubmissionDto == null) {
                    log.warn("Can't resume data!");
                    dpSuperDataSubmissionDto = new DpSuperDataSubmissionDto();
                }
                DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, bpc.request);
            } else if ("delete".equals(actionValue)) {
                dpDataSubmissionService.deleteDpSuperDataSubmissionDtoRfcDraftByConds(dpSuperDataSubmissionDto.getOrgId(), dpSuperDataSubmissionDto.getSubmissionType(), dpSuperDataSubmissionDto.getHciCode(), dpSuperDataSubmissionDto.getDataSubmissionDto().getId());
            }
        }
        DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, bpc.request);
        //set back url
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dpSuperDataSubmissionDto.getAppType())) {
            DataSubmissionHelper.setGoBackUrl(bpc.request);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "goBackUrl", null);
        }
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
        dpSuperDataSubmissionDto = dpSuperDataSubmissionDto == null ? new DpSuperDataSubmissionDto() : dpSuperDataSubmissionDto;
        PatientDto patientDto = dpSuperDataSubmissionDto.getPatientDto() == null ? new PatientDto() : dpSuperDataSubmissionDto.getPatientDto();
        ControllerHelper.get(request, patientDto);
        patientDto.setPatientType(DataSubmissionConsts.DS_PATIENT_DRP);
        if (StringUtil.isNotEmpty(patientDto.getIdNumber())) {
            patientDto.setIdNumber(patientDto.getIdNumber().toUpperCase());
        }
        if (StringUtil.isEmpty(patientDto.getEthnicGroup())) {
            patientDto.setEthnicGroup("");
        }
        if (StringUtil.isNotEmpty(patientDto.getEthnicGroup()) && !patientDto.getEthnicGroup().equals("ECGP004")) {
            patientDto.setEthnicGroupOther(null);
        }
        if (StringUtil.isEmpty(patientDto.getOrgId())) {
            patientDto.setOrgId(dpSuperDataSubmissionDto.getOrgId());
        }
        patientDto.setPatientCode(patientService.getPatientCode(patientDto.getPatientCode()));
        dpSuperDataSubmissionDto.setPatientDto(patientDto);
        String crudActionType = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);
        String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if ("resume".equals(actionValue)||"delete".equals(actionValue)) {
            crudActionType ="page";
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, crudActionType);
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if ("confirm".equals(crudActionType)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(patientDto, "DRP");
            errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);
            if (errorMap.isEmpty()) {
                valRFC(request,patientDto);
            }
            //for ds center validation
            LoginContext login = AccessUtil.getLoginUser(bpc.request);
            List<DsCenterDto> centerDtos = licenceClient.getDsCenterDtosByOrgIdAndCentreType(login.getOrgId(), DataSubmissionConsts.DS_DRP).getEntity();
            if (IaisCommonUtils.isEmpty(centerDtos)) {
                errorMap.put("topErrorMsg", "DS_ERR070");
            }
        }
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMAP, errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
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
