package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * ARDataSubmissionDelegator
 * <p>
 * Process: MohARDataSubmission
 *
 * @author suocheng
 * @date 10/20/2021
 */
@Delegator("arDataSubmissionDelegator")
@Slf4j
public class ArDataSubmissionDelegator {

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    private static final String PREMISES = "premises";

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.info("----- Assisted Reproduction Submission Start -----");
        HttpSession session = bpc.request.getSession();
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES_MAP);
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES);
        session.removeAttribute(DataSubmissionConstant.AR_DATA_SUBMISSION);
    }

    /**
     * StartStep: PrepareARSubmission
     *
     * @param bpc
     * @throws
     */
    public void doPrepareARSubmission(BaseProcessClass bpc) {
        String crud_action_type_ds = bpc.request.getParameter(DataSubmissionConstant.CRUD_TYPE);
        bpc.request.setAttribute(DataSubmissionConstant.CRUD_ACTION_TYPE_AR, crud_action_type_ds);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "ar-submission");
        Map<String, AppGrpPremisesDto> appGrpPremisesMap =
                (Map<String, AppGrpPremisesDto>) bpc.request.getSession().getAttribute(DataSubmissionConstant.AR_PREMISES_MAP);
        if (appGrpPremisesMap == null || appGrpPremisesMap.isEmpty()) {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
            String licenseeId = null;
            if (loginContext != null) {
                licenseeId = loginContext.getLicenseeId();
            }
            appGrpPremisesMap = arDataSubmissionService.getAppGrpPremises(licenseeId, "");
            bpc.request.getSession().setAttribute(DataSubmissionConstant.AR_PREMISES_MAP, appGrpPremisesMap);
        }
        if (appGrpPremisesMap.isEmpty()) {
            Map<String, String> map = IaisCommonUtils.genNewHashMap(2);
            map.put(PREMISES, "There are no active Assisted Reproduction licences");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
        } else if (appGrpPremisesMap.size() == 1) {
            appGrpPremisesMap.forEach((k, v) -> {
                bpc.request.getSession().setAttribute(DataSubmissionConstant.AR_PREMISES, v);
                bpc.request.setAttribute("premisesLabel", DataSubmissionHelper.getPremisesLabel(v));
            });
        } else {
            bpc.request.setAttribute("premisesOpts", DataSubmissionHelper.genPremisesOptions(appGrpPremisesMap));
        }
    }

    /**
     * StartStep: PreparePIM
     *
     * @param bpc
     * @throws
     */
    public void doPrepareAR(BaseProcessClass bpc) {
        String crudype = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (StringUtil.isIn(crudype, new String[]{"back", "return"})) {
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_AR, "back");
            return;
        }
        String submissionType = ParamUtil.getString(bpc.request, "submissionType");
        String submissionMethod = ParamUtil.getString(bpc.request, "submissionMethod");
        String actionType = null;
        Map<String, String> map = IaisCommonUtils.genNewHashMap(3);
        if (StringUtil.isEmpty(submissionType)) {
            map.put("submissionType", "GENERAL_ERR0006");
        } else if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            if (DataSubmissionConsts.DATA_SUBMISSION_METHOD_MANUAL_ENTRY.equals(submissionMethod)) {
                actionType = "pim";
            } else if (DataSubmissionConsts.DATA_SUBMISSION_METHOD_MANUAL_ENTRY.equals(submissionMethod)) {
                actionType = "pif";
            } else {
                map.put("submissionMethod", "GENERAL_ERR0006");
            }
        } else if (DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE.equals(submissionType)) {
            if (DataSubmissionConsts.DATA_SUBMISSION_METHOD_MANUAL_ENTRY.equals(submissionMethod)) {
                actionType = "csm";
            } else if (DataSubmissionConsts.DATA_SUBMISSION_METHOD_MANUAL_ENTRY.equals(submissionMethod)) {
                actionType = "csf";
            } else {
                map.put("submissionMethod", "GENERAL_ERR0006");
            }
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)) {
            actionType = "ds";
        }
        // check premises
        HttpSession session = bpc.request.getSession();
        String premises = ParamUtil.getString(bpc.request, PREMISES);
        Map<String, AppGrpPremisesDto> appGrpPremisesMap = (Map<String, AppGrpPremisesDto>) session.getAttribute(
                DataSubmissionConstant.AR_PREMISES_MAP);
        AppGrpPremisesDto appGrpPremisesDto = (AppGrpPremisesDto) session.getAttribute(DataSubmissionConstant.AR_PREMISES);
        if (!StringUtil.isEmpty(premises)) {
            if (appGrpPremisesMap != null) {
                appGrpPremisesDto = appGrpPremisesMap.get(premises);
            }
            if (appGrpPremisesDto == null) {
                map.put(PREMISES, "GENERAL_ERR0049");
            }
        } else if (IaisCommonUtils.isNotEmpty(appGrpPremisesMap)) {
            map.put(PREMISES, "GENERAL_ERR0006");
        } else if (appGrpPremisesDto == null) {
            map.put(PREMISES, "There are no active Assisted Reproduction licences");
        }
        ArSuperDataSubmissionDto dataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (reNew(dataSubmission, submissionType, submissionMethod, appGrpPremisesDto)) {
            dataSubmission = new ArSuperDataSubmissionDto();
        }
        if (!map.isEmpty()) {
            dataSubmission.setArSubmissionType(submissionType);
            dataSubmission.setSubmissionMethod(submissionMethod);
            dataSubmission.setAppGrpPremisesDto(appGrpPremisesDto);
            actionType = "invalid";
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
        } else {
            String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                    .map(LoginContext::getOrgId).orElse("");
            String hciCode = Optional.ofNullable(appGrpPremisesDto)
                    .map(AppGrpPremisesDto::getHciCode)
                    .orElse("");
            String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            log.info(StringUtil.changeForLog("Action Type: " + actionValue));
            if (StringUtil.isEmpty(actionValue)) {
                ArSuperDataSubmissionDto dataSubmissionDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(
                        orgId, submissionType, hciCode);
                if (dataSubmissionDraft != null) {
                    ParamUtil.setRequestAttr(bpc.request, "hasDraft", true);
                    actionType = "invalid";
                }
            } else if ("resume".equals(actionValue)) {
                dataSubmission = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode);
                if (dataSubmission == null) {
                    log.warn("Can't resume data!");
                    dataSubmission = new ArSuperDataSubmissionDto();
                }
            } else if ("delete".equals(actionValue)) {
                arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode);
            }
            dataSubmission.setSubmissionType(DataSubmissionConsts.DATA_SUBMISSION_TYPE_AR);
            dataSubmission.setOrgId(orgId);
            dataSubmission.setArSubmissionType(submissionType);
            dataSubmission.setSubmissionMethod(submissionMethod);
            dataSubmission.setAppGrpPremisesDto(appGrpPremisesDto);
            dataSubmission.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        DataSubmissionHelper.setCurrentArDataSubmission(dataSubmission, bpc.request);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_AR, actionType);
    }

    private boolean reNew(ArSuperDataSubmissionDto arSuperDto, String submissionType, String submissionMethod,
            AppGrpPremisesDto appGrpPremisesDto) {
        if (arSuperDto == null
                || !Objects.equals(submissionType, arSuperDto.getArSubmissionType())
                || !Objects.equals(submissionMethod, arSuperDto.getSubmissionMethod())) {
            return true;
        }
        String hciCode = Optional.ofNullable(appGrpPremisesDto)
                .map(AppGrpPremisesDto::getHciCode)
                .orElse("");
        String old = Optional.ofNullable(arSuperDto.getAppGrpPremisesDto())
                .map(AppGrpPremisesDto::getHciCode)
                .orElse("");
        return !Objects.equals(hciCode, old);
    }

    /**
     * StartStep: PreparePIM
     *
     * @param bpc
     * @throws
     */
    public void doPreparePIM(BaseProcessClass bpc) {
        log.info("----- Patient Information -----");
    }

    /**
     * StartStep: PreparePIF
     *
     * @param bpc
     * @throws
     */
    public void doPreparePIF(BaseProcessClass bpc) {

    }

    /**
     * StartStep: PrepareCSM
     *
     * @param bpc
     * @throws
     */
    public void doPrepareCSM(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareCSF
     *
     * @param bpc
     * @throws
     */
    public void doPrepareCSF(BaseProcessClass bpc) {

    }

    /**
     * StartStep: PrepareDS
     *
     * @param bpc
     * @throws
     */
    public void doPrepareDS(BaseProcessClass bpc) {

    }

    /**
     * StartStep: Back
     *
     * @param bpc
     * @throws
     */
    public void doBack(BaseProcessClass bpc) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohDataSubmission");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

}
