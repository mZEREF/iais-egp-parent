package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
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
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description DpDataSubmissionDelegator
 * @Auther chenlei on 11/18/2021.
 */
@Slf4j
@Delegator("dpDataSubmissionDelegator")
public class DpDataSubmissionDelegator {

    @Autowired
    private DpDataSubmissionService dpDataSubmissionService;

    private static final String PREMISES = "premises";

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.info("----- Drug Practices Submission Start -----");
        DataSubmissionHelper.clearSession(bpc.request);
    }

    /**
     * StartStep: PrepareSubmission
     *
     * @param bpc
     * @throws
     */
    public void doPrepareSubmission(BaseProcessClass bpc) {
        log.info("----- Drug Practices Prepare Submission -----");
        // no title
        bpc.request.removeAttribute("title");
        String crudype = bpc.request.getParameter(DataSubmissionConstant.CRUD_TYPE);
        bpc.request.setAttribute(DataSubmissionConstant.CRUD_ACTION_TYPE_DP, crudype);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "dp-submission");
        Map<String, PremisesDto> premisesMap =
                (Map<String, PremisesDto>) bpc.request.getSession().getAttribute(DataSubmissionConstant.DP_PREMISES_MAP);
        if (premisesMap == null || premisesMap.isEmpty()) {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
            String licenseeId = null;
            if (loginContext != null) {
                licenseeId = loginContext.getLicenseeId();
            }
            premisesMap = dpDataSubmissionService.getDpCenterPremises(licenseeId);
            bpc.request.getSession().setAttribute(DataSubmissionConstant.DP_PREMISES_MAP, premisesMap);
        }
        if (premisesMap.isEmpty()) {
            Map<String, String> map = IaisCommonUtils.genNewHashMap(2);
            map.put(PREMISES, "There are no active Assisted Reproduction licences");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
        } else if (premisesMap.size() == 1) {
            premisesMap.forEach((k, v) -> {
                bpc.request.getSession().setAttribute(DataSubmissionConstant.DP_PREMISES, v);
                bpc.request.setAttribute("premisesLabel", v.getPremiseLabel());
            });
        } else {
            bpc.request.setAttribute("premisesOpts", DataSubmissionHelper.genPremisesOptions(premisesMap));
        }
    }

    /**
     * StartStep: PrepareDP
     *
     * @param bpc
     * @throws
     */
    public void doPrepareDP(BaseProcessClass bpc) {
        log.info("----- Do Drug Practices Submission -----");
        String crudype = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (StringUtil.isIn(crudype, new String[]{"back", "return"})) {
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_DP, "return");
            return;
        }
        String submissionType = ParamUtil.getString(bpc.request, "submissionType");
        String actionType = null;
        Map<String, String> map = IaisCommonUtils.genNewHashMap(3);
        if (StringUtil.isEmpty(submissionType)) {
            map.put("submissionType", "GENERAL_ERR0006");
        } else if (DataSubmissionConsts.DP_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
            actionType = "pi";
        } else if (DataSubmissionConsts.DP_TYPE_SBT_DRUG_PRESCRIBED.equals(submissionType)) {
            actionType = "dp";
        } else if (DataSubmissionConsts.DP_TYPE_SBT_SOVENOR_INVENTORY.equals(submissionType)) {
            actionType = "di";
        }
        // check premises
        HttpSession session = bpc.request.getSession();
        String premises = ParamUtil.getString(bpc.request, PREMISES);
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) session.getAttribute(
                DataSubmissionConstant.DP_PREMISES_MAP);
        PremisesDto premisesDto = (PremisesDto) session.getAttribute(DataSubmissionConstant.DP_PREMISES);
        if (!StringUtil.isEmpty(premises)) {
            if (premisesMap != null) {
                premisesDto = premisesMap.get(premises);
            }
            if (premisesDto == null) {
                map.put(PREMISES, "GENERAL_ERR0049");
            }
        } else if (premisesMap != null && premisesMap.size() > 1) {
            map.put(PREMISES, "GENERAL_ERR0006");
        } else if (premisesDto == null) {
            map.put(PREMISES, "There are no active Assisted Reproduction licences");
        }
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(bpc.request);
        if (reNew(dpSuperDataSubmissionDto, submissionType, premisesDto)) {
            dpSuperDataSubmissionDto = new DpSuperDataSubmissionDto();
        }
        if (!map.isEmpty()) {
            dpSuperDataSubmissionDto.setSubmissionType(submissionType);
            dpSuperDataSubmissionDto.setPremisesDto(premisesDto);
            actionType = "back";
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
        } else {
            String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                    .map(LoginContext::getOrgId).orElse("");
            String hciCode = premisesDto.getHciCode();
            String svcName = premisesDto.getSvcName();
            String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            log.info(StringUtil.changeForLog("Action Type: " + actionValue));
            if (StringUtil.isEmpty(actionValue)) {
                DpSuperDataSubmissionDto dataSubmissionDraft = dpDataSubmissionService.getDpSuperDataSubmissionDtoDraftByConds(
                        orgId, submissionType, svcName, hciCode);
                if (dataSubmissionDraft != null) {
                    ParamUtil.setRequestAttr(bpc.request, "hasDraft", true);
                    actionType = "back";
                }
            } else if ("resume".equals(actionValue)) {
                dpSuperDataSubmissionDto = dpDataSubmissionService.getDpSuperDataSubmissionDtoDraftByConds(
                        orgId, submissionType, svcName, hciCode);
                if (dpSuperDataSubmissionDto == null) {
                    log.warn("Can't resume data!");
                    dpSuperDataSubmissionDto = new DpSuperDataSubmissionDto();
                }
            } else if ("delete".equals(actionValue)) {
                dpDataSubmissionService.deleteDpSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode);
            }
            dpSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
            dpSuperDataSubmissionDto.setOrgId(orgId);
            dpSuperDataSubmissionDto.setSubmissionType(submissionType);
            dpSuperDataSubmissionDto.setPremisesDto(premisesDto);
            dpSuperDataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            dpSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(dpSuperDataSubmissionDto,
                    false));
            dpSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(dpSuperDataSubmissionDto, false));
        }
        DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, bpc.request);
        if (StringUtil.isEmpty(actionType)) {
            actionType = "back";
        }
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_DP, actionType);
    }

    private boolean reNew(DpSuperDataSubmissionDto dpSuperDto, String submissionType, PremisesDto premisesDto) {
        if (dpSuperDto == null
                || !Objects.equals(submissionType, dpSuperDto.getSubmissionType())) {
            return true;
        }
        String hciCode = Optional.ofNullable(premisesDto)
                .map(PremisesDto::getHciCode)
                .orElse("");
        String old = Optional.ofNullable(dpSuperDto.getPremisesDto())
                .map(PremisesDto::getHciCode)
                .orElse("");
        return !Objects.equals(hciCode, old);
    }

    /**
     * Step: PreparePatientInfo
     * @param bpc
     */
    public void preparePatientInfo(BaseProcessClass bpc) {
        log.info("----- PreparePatientInfo -----");
    }

    /**
     * Step: PrepareDrugPrecribed
     * @param bpc
     */
    public void prepareDrugPrecribed(BaseProcessClass bpc) {
        log.info("----- PrepareDrugPrecribed -----");
    }

    /**
     * Step: PrepareSovenorInventory
     * @param bpc
     */
    public void prepareSovenorInventory(BaseProcessClass bpc) {
        log.info("----- PrepareSovenorInventory -----");
    }

    /**
     * Step: PrepareReturn
     * @param bpc
     */
    public void prepareReturn(BaseProcessClass bpc) throws IOException {
        log.info("----- Prepare Return -----");
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohDataSubmission");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }
}
