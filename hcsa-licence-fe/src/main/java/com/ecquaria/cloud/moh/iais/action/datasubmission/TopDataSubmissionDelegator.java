package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
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
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Process: MohNEWTOPDataSumission
 *
 * @Description TopDataSubmissionDelegator
 * @Auther zhixing on 2/14/2022.
 */
@Slf4j
@Delegator("topDataSubmissionDelegator")
public class TopDataSubmissionDelegator {
        @Autowired
        private TopDataSubmissionService topDataSubmissionService;

    public static final String CRUD_ACTION_TYPE_TOP = "crud_action_type_top";
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
            bpc.request.setAttribute(DataSubmissionConstant.CRUD_ACTION_TYPE_TOP, crudype);
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "top-submission");
            Map<String, PremisesDto> premisesMap =
                    (Map<String, PremisesDto>) bpc.request.getSession().getAttribute(DataSubmissionConstant.TOP_PREMISES_MAP);
            if (premisesMap == null || premisesMap.isEmpty()) {
                LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
                String licenseeId = null;
                if (loginContext != null) {
                    licenseeId = loginContext.getLicenseeId();
                }
                premisesMap = topDataSubmissionService.getTopCenterPremises(licenseeId);
                bpc.request.getSession().setAttribute(DataSubmissionConstant.TOP_PREMISES_MAP, premisesMap);
            }
            if (premisesMap.isEmpty()) {
                Map<String, String> map = IaisCommonUtils.genNewHashMap(2);
                map.put(PREMISES, "There are no active Assisted Reproduction licences");
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
            } else if (premisesMap.size() == 1) {
                premisesMap.forEach((k, v) -> {
                    bpc.request.getSession().setAttribute(DataSubmissionConstant.TOP_PREMISES, v);
                    bpc.request.setAttribute("premisesLabel", v.getPremiseLabel());
                });
            } else {
                bpc.request.setAttribute("premisesOpts", DataSubmissionHelper.genPremisesOptions(premisesMap));
            }
        }

        /**
         * StartStep: PrepareTOP
         *
         * @param bpc
         * @throws
         */
        public void doPrepareTOP(BaseProcessClass bpc) {
            log.info("----- Do Drug Practices Submission -----");
            String crudype = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
            if (StringUtil.isIn(crudype, new String[]{"back", "return","previous"})) {
                ParamUtil.setRequestAttr(bpc.request, CRUD_ACTION_TYPE_TOP, "return");
                return;
            }
            String submissionType = ParamUtil.getString(bpc.request, "submissionType");
            String actionType = null;
            Map<String, String> map = IaisCommonUtils.genNewHashMap(3);
            if (StringUtil.isEmpty(submissionType)) {
                map.put("submissionType", "GENERAL_ERR0006");
            } else if (DataSubmissionConsts.TOP_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
                actionType = "pi";
            } else if (DataSubmissionConsts.TOP_TYPE_SBT_TERMINATION_OF_PRE.equals(submissionType)) {
                actionType = "top";
            }
            // check premises
            HttpSession session = bpc.request.getSession();
            String premises = ParamUtil.getString(bpc.request, PREMISES);
            Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) session.getAttribute(
                    DataSubmissionConstant.TOP_PREMISES_MAP);
            PremisesDto premisesDto = (PremisesDto) session.getAttribute(DataSubmissionConstant.TOP_PREMISES);
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
            TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(bpc.request);
            if (reNew(topSuperDataSubmissionDto, submissionType, premisesDto)) {
                topSuperDataSubmissionDto = new TopSuperDataSubmissionDto();
            }
            if (!map.isEmpty()) {
                topSuperDataSubmissionDto.setSubmissionType(submissionType);
                topSuperDataSubmissionDto.setPremisesDto(premisesDto);
                actionType = "back";
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
            } else {
                String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                        .map(LoginContext::getOrgId).orElse("");
                String hciCode = null;
                String svcName = null;
                if(premisesDto != null){
                    hciCode = premisesDto.getHciCode();
                    svcName = premisesDto.getSvcName();
                }
                String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
                log.info(StringUtil.changeForLog("Action Type: " + actionValue));
                if (StringUtil.isEmpty(actionValue)) {
                    TopSuperDataSubmissionDto dataSubmissionDraft = topDataSubmissionService.getTopSuperDataSubmissionDtoDraftByConds(
                            orgId, submissionType);
                    if (dataSubmissionDraft != null) {
                        ParamUtil.setRequestAttr(bpc.request, "hasDraft", Boolean.TRUE);
                        actionType = "back";
                    }
                } else if ("resume".equals(actionValue)) {
                    topSuperDataSubmissionDto = topDataSubmissionService.getTopSuperDataSubmissionDtoDraftByConds(
                            orgId, submissionType);
                    if (topSuperDataSubmissionDto == null) {
                        log.warn("Can't resume data!");
                        topSuperDataSubmissionDto = new TopSuperDataSubmissionDto();
                    }
                } else if ("delete".equals(actionValue)) {
                    topDataSubmissionService.deleteTopSuperDataSubmissionDtoDraftByConds(orgId, submissionType, hciCode);
                }
                topSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
                topSuperDataSubmissionDto.setOrgId(orgId);
                topSuperDataSubmissionDto.setSubmissionType(submissionType);
                topSuperDataSubmissionDto.setPremisesDto(premisesDto);
                topSuperDataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                topSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(topSuperDataSubmissionDto,
                        false));
                topSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(topSuperDataSubmissionDto,
                        DataSubmissionHelper.getLicenseeId(bpc.request), false));
            }
            DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, bpc.request);
            if (StringUtil.isEmpty(actionType)) {
                actionType = "back";
            }
            log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_TOP, actionType);
        }

        private boolean reNew(TopSuperDataSubmissionDto topSuperDto, String submissionType, PremisesDto premisesDto) {
            if (topSuperDto == null
                    || !Objects.equals(submissionType, topSuperDto.getSubmissionType())) {
                return true;
            }
            String hciCode = Optional.ofNullable(premisesDto)
                    .map(PremisesDto::getHciCode)
                    .orElse("");
            String old = Optional.ofNullable(topSuperDto.getPremisesDto())
                    .map(PremisesDto::getHciCode)
                    .orElse("");
            return !Objects.equals(hciCode, old);
        }

        /**
         * Step: PatientInformation
         * @param bpc
         */
        public void PatientInformation(BaseProcessClass bpc) {
            log.info("----- PatientInformation -----");
        }

        /**
         * Step: TerminationOfPregnancy
         * @param bpc
         */
        public void TerminationOfPregnancy(BaseProcessClass bpc) {
            log.info("----- TerminationOfPregnancy -----");
        }

        /**
         * Step: PrepareReturn
         * @param bpc
         */
        public void PrepareReturn(BaseProcessClass bpc) throws IOException {
            log.info("----- Prepare Return -----");
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS)
                    .append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohDataSubmission");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
    }
