package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsConfig;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(" -----VssDataSubmissionDelegator Start ------ ");
        DsConfigHelper.clearVssSession(bpc.request);
        DsConfigHelper.intVssConfig(bpc.request);
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
    }

    private String getActionType(HttpServletRequest request) {
        String actionType = (String) ParamUtil.getRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS);
        if (StringUtil.isEmpty(actionType)) {
            String crudType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
            if (StringUtil.isEmpty(crudType)) {
                List<DsConfig> configs = DsConfigHelper.intVssConfig(request);
                actionType = configs.stream()
                        .filter(config -> config.isActive())
                        .map(DsConfig::getCode)
                        .filter(Objects::nonNull)
                        .findAny()
                        .orElse(DsConfigHelper.VSS_STEP_TREATMENT);
            } else if ("return".equals(crudType)) {
                actionType = "return";
            } else if ("previous".equals(crudType)) {
                DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, request);
                if (1 == currentConfig.getSeqNo()) {
                    actionType = "return";
                } else {
                    DsConfig config = DsConfigHelper.setPreviousActiveConfig(DataSubmissionConsts.DS_VSS, request);
                    if (config == null) {
                        actionType = "return";
                    } else {
                        actionType = config.getCode();
                    }
                }
            } else if ("page".equals(crudType)) {
                DsConfig config = DsConfigHelper.setNextActiveConfig(DataSubmissionConsts.DS_VSS, request);
                if (config == null) {
                    actionType = "submission";
                } else {
                    actionType = config.getCode();
                }
            } else if ("submission".equals(crudType)) {
                actionType = crudType;
            } else {
                List<DsConfig> configs = DsConfigHelper.getDsConfigs(DataSubmissionConsts.DS_VSS);
                actionType = configs.stream()
                        .filter(config -> Objects.equals(crudType, config.getCode()))
                        .map(DsConfig::getCode)
                        .filter(Objects::nonNull)
                        .findAny()
                        .orElse("return");
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
    }

    /**
     * Step: DoStep
     *
     * @param bpc
     */
    public void doStep(BaseProcessClass bpc) {
        log.info(" ----- DoStep ------ ");
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        String crudType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);

        if ("previous".equals(crudType)) {

        }
        if ("return".equals(crudType)) {
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, crudType);
        } else {

        }
        if (!Objects.equals(crudType, currentConfig.getCode())) {
            crudType = null;
        }
    }

    /**
     * Step: DoSubmission
     *
     * @param bpc
     */
    public void doSubmission(BaseProcessClass bpc) {
        log.info(" ----- DoSubmission ------ ");
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


}
