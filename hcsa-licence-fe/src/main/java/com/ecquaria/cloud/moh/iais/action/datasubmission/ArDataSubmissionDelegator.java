package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
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

    private static final String CENTRE_SEL = "centreSel";

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.info("----- Assisted Reproduction Submission Start -----");
        DataSubmissionHelper.clearSession(bpc.request);
    }

    /**
     * StartStep: PrepareARSubmission
     *
     * @param bpc
     * @throws
     */
    public void doPrepareARSubmission(BaseProcessClass bpc) {
        // no title
        bpc.request.removeAttribute("title");
        bpc.request.getSession().removeAttribute("title");
        String crud_action_type_ds = bpc.request.getParameter(DataSubmissionConstant.CRUD_TYPE);
        bpc.request.setAttribute(DataSubmissionConstant.CRUD_ACTION_TYPE_AR, crud_action_type_ds);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "ar-submission");
        Map<String, PremisesDto> premisesMap = DataSubmissionHelper.setArPremisesMap(bpc.request);
        if (premisesMap.isEmpty()) {
            Map<String, String> map = IaisCommonUtils.genNewHashMap(2);
            map.put(CENTRE_SEL, "There are no active Assisted Reproduction licences");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
        } else if (premisesMap.size() == 1) {
            premisesMap.forEach((k, v) -> {
                bpc.request.getSession().setAttribute(DataSubmissionConstant.AR_PREMISES, v);
                bpc.request.setAttribute("premisesLabel", v.getPremiseLabel());
            });
        } else {
            bpc.request.setAttribute("premisesOpts", DataSubmissionHelper.genPremisesOptions(premisesMap));
        }
    }

    /**
     * StartStep: PrepareAR
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
            if (DataSubmissionConsts.DS_METHOD_MANUAL_ENTRY.equals(submissionMethod)) {
                actionType = "pim";
            } else if (DataSubmissionConsts.DS_METHOD_FILE_UPLOAD.equals(submissionMethod)) {
                actionType = "pif";
            } else {
                map.put("submissionMethod", "GENERAL_ERR0006");
            }
        } else if (DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE.equals(submissionType)) {
            if (DataSubmissionConsts.DS_METHOD_MANUAL_ENTRY.equals(submissionMethod)) {
                actionType = "csm";
            } else if (DataSubmissionConsts.DS_METHOD_FILE_UPLOAD.equals(submissionMethod)) {
                actionType = "csf";
            } else {
                map.put("submissionMethod", "GENERAL_ERR0006");
            }
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)) {
            actionType = "ds";
        }
        // check premises
        HttpSession session = bpc.request.getSession();
        String centreSel = ParamUtil.getString(bpc.request, CENTRE_SEL);
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) session.getAttribute(
                DataSubmissionConstant.AR_PREMISES_MAP);
        PremisesDto premisesDto = (PremisesDto) session.getAttribute(DataSubmissionConstant.AR_PREMISES);
        if (!StringUtil.isEmpty(centreSel)) {
            if (premisesMap != null) {
                premisesDto = premisesMap.get(centreSel);
            }
            if (premisesDto == null) {
                map.put(CENTRE_SEL, "GENERAL_ERR0049");
            }
        } else if (premisesMap != null && premisesMap.size() > 1) {
            map.put(CENTRE_SEL, "GENERAL_ERR0006");
        } else if (premisesDto == null) {
            map.put(CENTRE_SEL, "There are no active Assisted Reproduction licences");
        }
        ArSuperDataSubmissionDto currentSuper = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        boolean reNew = isNeedReNew(currentSuper, submissionType, submissionMethod, premisesDto);
        if (reNew) {
            currentSuper = new ArSuperDataSubmissionDto();
        }
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        String orgId = loginContext.getOrgId();
        String licenseeId = loginContext.getLicenseeId();
        if (!map.isEmpty()) {
            actionType = "invalid";
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
        } else {
            String hciCode =  premisesDto !=null ? premisesDto.getHciCode() : "";
            String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            log.info(StringUtil.changeForLog("Action Type: " + actionValue));
            if (StringUtil.isEmpty(actionValue)) {
                ArSuperDataSubmissionDto dataSubmissionDraft = null;
                if (!DataSubmissionConsts.DS_METHOD_FILE_UPLOAD.equals(submissionMethod)) {
                    dataSubmissionDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(
                            orgId, submissionType, hciCode);
                }
                if (dataSubmissionDraft != null/* && !Objects.equals(dataSubmissionDraft.getDraftNo(), currentSuper.getDraftNo())*/) {
                    ParamUtil.setRequestAttr(bpc.request, "hasDraft", Boolean.TRUE);
                    actionType = "invalid";
                }
            } else if ("resume".equals(actionValue)) {
                currentSuper = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(orgId, submissionType,
                        hciCode);
                if (currentSuper == null) {
                    log.warn("Can't resume data!");
                    currentSuper = new ArSuperDataSubmissionDto();
                } else {
                    reNew = false;
                }
            } else if ("delete".equals(actionValue)) {
                arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode);
                currentSuper = new ArSuperDataSubmissionDto();
                reNew = true;
            }
        }
        currentSuper.setOrgId(orgId);
        currentSuper.setLicenseeId(licenseeId);
        currentSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        currentSuper.setCentreSel(centreSel);
        currentSuper.setSubmissionType(submissionType);
        currentSuper.setSubmissionMethod(submissionMethod);
        currentSuper.setPremisesDto(premisesDto);
        currentSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        if (reNew) {
            currentSuper.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(currentSuper, true));
            currentSuper.setCycleDto(DataSubmissionHelper.initCycleDto(currentSuper, true));
        }
        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, bpc.request);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_AR, actionType);
    }

    private boolean isNeedReNew(ArSuperDataSubmissionDto arSuperDto, String submissionType, String submissionMethod,
            PremisesDto premisesDto) {
        if (arSuperDto == null
                || !Objects.equals(submissionType, arSuperDto.getSubmissionType())
                || !Objects.equals(submissionMethod, arSuperDto.getSubmissionMethod())) {
            return true;
        }
        String hciCode = Optional.ofNullable(premisesDto)
                .map(PremisesDto::getHciCode)
                .orElse("");
        return !Objects.equals(hciCode, arSuperDto.getHciCode());
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
                .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohDataSubmission/PrepareCompliance");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

}
