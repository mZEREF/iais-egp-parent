package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsConfig;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description TopDataSubmissionDelegator
 * @Auther chenlei on 12/22/2021.
 */
@Slf4j
@Delegator("topDataSubmissionDelegator")
public class TopDataSubmissionDelegator {

    @Autowired
    private TopDataSubmissionService topDataSubmissionService;

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(" -----TopDataSubmissionDelegator Start ------ ");
        DsConfigHelper.clearTopSession(bpc.request);
        DsConfigHelper.initTopConfig(bpc.request);
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
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP, actionType);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(DataSubmissionConsts.DS_APP_TYPE_NEW));
        DsConfig config = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_TOP, bpc.request);
        String smallTitle = "";
        if (config != null) {
            smallTitle = config.getText();
        }
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>" + smallTitle + "</strong>");
    }

    private String getActionType(HttpServletRequest request) {
        String actionType = (String) ParamUtil.getRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP);
        if (StringUtil.isEmpty(actionType)) {
            String crudType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
            if (StringUtil.isEmpty(crudType) || "TP".equals(crudType)) {
                actionType = DataSubmissionHelper.initAction(DataSubmissionConsts.DS_TOP, DsConfigHelper.TOP_STEP_PATIENT, request);
            } else if ("return".equals(crudType)) {
                actionType = "return";
            } else if ("previous".equals(crudType)) {
                actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_TOP, request);
            } else if ("page".equals(crudType) || "next".equals(crudType)) {
                actionType = DataSubmissionHelper.setNextAction(DataSubmissionConsts.DS_TOP, request);
            } else if ("submission".equals(crudType)) {
                actionType = crudType;
            } else {
                actionType = DataSubmissionHelper.initAction(DataSubmissionConsts.DS_TOP, "return", request);
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
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_TOP, bpc.request);
        if (DsConfigHelper.TOP_STEP_PATIENT.equals(currentConfig.getCode())) {
            pageStage = DataSubmissionConstant.PAGE_STAGE_PREVIEW;
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, pageStage);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- PrepareStepData Step Code: " + currentCode + " ------ "));
        if (DsConfigHelper.TOP_STEP_PATIENT.equals(currentCode)) {
            preparePatient(bpc.request);
        } else if (DsConfigHelper.TOP_STEP_PLANNING.equals(currentCode)) {

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
        if (DsConfigHelper.TOP_STEP_PATIENT.equals(currentCode)) {
            status = doPatient(bpc.request);
        } else if (DsConfigHelper.TOP_STEP_PLANNING.equals(currentCode)) {

        }
        log.info(StringUtil.changeForLog(" ----- DoStep Status: " + status + " ------ "));
        String actionType = null;
        if (0 == status) {// current
            actionType = DataSubmissionHelper.setCurrentAction(DataSubmissionConsts.DS_VSS, bpc.request);
        } else if (-1 == status) { // previous
            actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_VSS, bpc.request);
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, actionType);
    }

    private void preparePatient(HttpServletRequest request) {
    }

    private int doPatient(HttpServletRequest request) {
        return 0;
    }

    /**
     * Step: DoSubmission
     *
     * @param bpc
     */
    public void doSubmission(BaseProcessClass bpc) {
        log.info(" ----- DoSubmission ------ ");
        /**
         * DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
         *         DataSTubmissionDto dataSubmissionDto = dpSuperDataSubmissionDto.getDataSubmissionDto();
         *          *         CycleDto cycle = dpSuperDataSubmissionDto.getCycleDto();
         *          *         String cycleType = cycle.getCycleType();
         *          *         if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
         *          *             String submissionNo = dpDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_DRP);
         *          *             dataSubmissionDto.setSubmissionNo(submissionNo);
         *          *         }
         *          *         if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
         *          *             dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
         *          *         }
         *          *         String stage = dataSubmissionDto.getCycleStage();
         *          *         String status = DataSubmissionConsts.DS_STATUS_ACTIVE;
         *          *
         *          *         cycle.setStatus(status);
         *          *         log.info(StringUtil.changeForLog("-----Cycle Type: " + cycleype + " - Stage : " + stage
         *                 + " - Status: " + status + " -----"));
         *
         *         LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
         *         if (loginContext != null) {
         *             dataSubmissionDto.setSubmitBy(loginContext.getUserId());
         *             dataSubmissionDto.setSubmitDt(new Date());
         *         }
         *         dpSuperDataSubmissionDto = dpDataSubmissionService.saveDpSuperDataSubmissionDto(dpSuperDataSubmissionDto);
         *         try {
         *             dpSuperDataSubmissionDto = dpDataSubmissionService.saveDpSuperDataSubmissionDtoToBE(dpSuperDataSubmissionDto);
         *         } catch (Exception e) {
         *             log.error(StringUtil.changeForLog("The Eic saveDpSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
         *         }
         *         if (!StringUtil.isEmpty(dpSuperDataSubmissionDto.getDraftId())) {
         *             dpDataSubmissionService.updateDataSubmissionDraftStatus(dpSuperDataSubmissionDto.getDraftId(),
         *                     DataSubmissionConsts.DS_STATUS_INACTIVE);
         *         }
         *         ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.DP_DATA_SUBMISSION, dpSuperDataSubmissionDto);
         *         ParamUtil.setRequestAttr(bpc.request, "emailAddress", DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
         *         ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
         *         ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
         *         ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKDRP);
         */
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);

    }

    /**
     * Step: DoDraft
     *
     * @param bpc
     */
    public void doDraft(BaseProcessClass bpc) {
        log.info(" ----- DoDraft ------ ");
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
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        String target = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
        if (dpSuperDataSubmissionDto != null && DataSubmissionConsts.DS_APP_TYPE_NEW.equals(dpSuperDataSubmissionDto.getAppType())) {
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
