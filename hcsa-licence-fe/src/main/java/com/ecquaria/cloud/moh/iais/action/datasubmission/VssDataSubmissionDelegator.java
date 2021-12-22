package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Process: MohVSSDataSubmission
 *
 * @Description VssDataSubmissionDelegator
 * @Auther chenlei on 12/13/2021.
 */
@Slf4j
@Delegator("vssDataSubmissionDelegator")
public class VssDataSubmissionDelegator {

    @Autowired
    private VssDataSubmissionService vssDataSubmissionService;

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(" -----VssDataSubmissionDelegator Start ------ ");
        DsConfigHelper.clearVssSession(bpc.request);
        DsConfigHelper.initVssConfig(bpc.request);
    }

    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(" ----- PrepareSwitch ------ ");
        String actionType = getActionType(bpc.request);
        log.info(StringUtil.changeForLog("Action Type: " + actionType));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, actionType);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(DataSubmissionConsts.DS_APP_TYPE_NEW));
        DsConfig config = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        String smallTitle = "";
        if (config != null) {
            smallTitle = config.getText();
        }
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>" + smallTitle + "</strong>");
    }

    private String getActionType(HttpServletRequest request) {
        String actionType = (String) ParamUtil.getRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS);
        if (StringUtil.isEmpty(actionType)) {
            String crudType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
            if (StringUtil.isEmpty(crudType) || "VS".equals(crudType)) {
                actionType = DataSubmissionHelper.initAction(DataSubmissionConsts.DS_VSS, DsConfigHelper.VSS_STEP_TREATMENT, request);
            } else if ("return".equals(crudType)) {
                actionType = "return";
            } else if ("previous".equals(crudType)) {
                actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_VSS, request);
            } else if ("page".equals(crudType) || "next".equals(crudType)) {
                actionType = DataSubmissionHelper.setNextAction(DataSubmissionConsts.DS_VSS, request);
            } else if ("submission".equals(crudType)) {
                actionType = crudType;
            } else {
                actionType = DataSubmissionHelper.initAction(DataSubmissionConsts.DS_VSS, "return", request);
            }
        }
        return actionType;
    }

    /**
     * Step: PrepareStepData
     *
     * @param bpc
     */
    public void prepareStepData(BaseProcessClass bpc) {
        log.info(" -----PrepareStepData ------ ");
        String pageStage = DataSubmissionConstant.PAGE_STAGE_PAGE;
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        if (DsConfigHelper.VSS_STEP_PREVIEW.equals(currentConfig.getCode())) {
            pageStage = DataSubmissionConstant.PAGE_STAGE_PREVIEW;
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, pageStage);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- PrepareStepData Step Code: " + currentCode + " ------ "));
        if (currentCode.equals(DsConfigHelper.VSS_STEP_TREATMENT)) {
            prepareTreatment(bpc.request);
        }
        if (currentCode.equals(DsConfigHelper.VSS_STEP_CONSENT_PARTICULARS)) {
            prepareConsentParticulars(bpc.request);
        }
        if (currentCode.equals(DsConfigHelper.VSS_STEP_TFSSP_PARTICULARS)) {
            prepareTfsspParticulars(bpc.request);
        }
    }

    /**
     * Step: DoStep
     *
     * @param bpc
     */
    public void doStep(BaseProcessClass bpc) {
        log.info(" ----- DoStep ------ ");
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- DoStep Step Code: " + currentCode + " ------ "));
        int status = 0;
        if (currentCode.equals(DsConfigHelper.VSS_STEP_TREATMENT)) {
            status = doTreatment(bpc.request);
        }
        if (currentCode.equals(DsConfigHelper.VSS_STEP_CONSENT_PARTICULARS)) {
            status = doConsentParticulars(bpc.request);
        }
        if (currentCode.equals(DsConfigHelper.VSS_STEP_TFSSP_PARTICULARS)) {
            status = doTfsspParticulars(bpc.request);
        }
        log.info(StringUtil.changeForLog(" ----- DoStep Status: " + status + " ------ "));
        String actionType = null;
        if (0 == status) {// current
            actionType = DataSubmissionHelper.setCurrentAction(DataSubmissionConsts.DS_VSS, bpc.request);
        } else if (1 == status) { // previous
            actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_VSS, bpc.request);
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, actionType);
    }

    private void prepareTreatment(HttpServletRequest request) {
    }

    private int doTreatment(HttpServletRequest request) {
        return 0;
    }

    private void prepareConsentParticulars(HttpServletRequest request) {
    }

    private int doConsentParticulars(HttpServletRequest request) {
        return 0;
    }

    private void prepareTfsspParticulars(HttpServletRequest request) {
    }

    private int doTfsspParticulars(HttpServletRequest request) {
        return 0;
    }

    /**
     * Step: DoSubmission
     *
     * @param bpc
     */
    public void doSubmission(BaseProcessClass bpc) {
        log.info(" ----- DoSubmission ------ ");

          VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
                 DataSubmissionDto dataSubmissionDto = vssSuperDataSubmissionDto.getDataSubmissionDto();
                 CycleDto cycle = vssSuperDataSubmissionDto.getCycleDto();
                 String cycleType = cycle.getCycleType();
              if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
                      String submissionNo = vssDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_VSS);
                      dataSubmissionDto.setSubmissionNo(submissionNo);
                  }
                  if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
                      dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
                }
                  String stage = dataSubmissionDto.getCycleStage();
                  String status = DataSubmissionConsts.DS_STATUS_ACTIVE;

                  cycle.setStatus(status);
                 log.info(StringUtil.changeForLog("-----Cycle Type: " + cycleType + " - Stage : " + stage
                          + " - Status: " + status + " -----"));

                  LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
                  if (loginContext != null) {
                      dataSubmissionDto.setSubmitBy(loginContext.getUserId());
                      dataSubmissionDto.setSubmitDt(new Date());
                 }
                vssSuperDataSubmissionDto = vssDataSubmissionService.saveVssSuperDataSubmissionDto(vssSuperDataSubmissionDto);
                  try {
                      vssSuperDataSubmissionDto = vssDataSubmissionService.saveVssSuperDataSubmissionDtoToBE(vssSuperDataSubmissionDto);
                  } catch (Exception e) {
                      log.error(StringUtil.changeForLog("The Eic saveVssSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
                  }
                  if (!StringUtil.isEmpty(vssSuperDataSubmissionDto.getDraftId())) {
                      vssDataSubmissionService.updateDataSubmissionDraftStatus(vssSuperDataSubmissionDto.getDraftId(),
                              DataSubmissionConsts.DS_STATUS_INACTIVE);
                  }
                  ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
                  ParamUtil.setRequestAttr(bpc.request, "emailAddress", DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
                  ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
                  ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
                  ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKVSS);
                 ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);

    }

    /**
     * Step: DoDraft
     *
     * @param bpc
     */
    public void doDraft(BaseProcessClass bpc) {
        log.info(" ----- DoDraft ------ ");
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, "currentStage");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        if (vssSuperDataSubmissionDto != null) {
            vssSuperDataSubmissionDto.setDraftNo(vssDataSubmissionService.getDraftNo(DataSubmissionConsts.DS_VSS,
                    vssSuperDataSubmissionDto.getDraftNo()));
            vssSuperDataSubmissionDto = vssDataSubmissionService.saveDataSubmissionDraft(vssSuperDataSubmissionDto);
            DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "saveDraftSuccess", "success");
        } else {
            log.info(StringUtil.changeForLog("The vssSuperDataSubmission is null"));
        }
    }

    /**
     * Step: DoRfc
     *
     * @param bpc
     */
    public void doRfc(BaseProcessClass bpc) {
        log.info(" ----- DoRfc ------ ");
    }

    /**
     * Step: DoWithdraw
     *
     * @param bpc
     */
    public void doWithdraw(BaseProcessClass bpc) {
        log.info(" ----- DoWithdraw ------ ");
    }

    /**
     * Step: DoControl
     *
     * @param bpc
     */
    public void doControl(BaseProcessClass bpc) {
        log.info(" ----- DoControl ------ ");
    }

    /**
     * Step: DoReturn
     *
     * @param bpc
     */
    public void doReturn(BaseProcessClass bpc) throws IOException {
        log.info(" ----- DoReturn ------ ");
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        String target = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
        if (vssSuperDataSubmissionDto != null && DataSubmissionConsts.DS_APP_TYPE_NEW.equals(vssSuperDataSubmissionDto.getAppType())) {
            target = InboxConst.URL_LICENCE_WEB_MODULE + "MohDataSubmission";
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(target);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

}
