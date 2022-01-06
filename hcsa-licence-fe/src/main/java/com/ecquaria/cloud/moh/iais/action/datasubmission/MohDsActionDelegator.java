package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Process: MohDsAction
 *
 * @Description MohDsActionDelegator
 * @Auther chenlei on 11/16/2021.
 */
@Slf4j
@Delegator("mohDsActionDelegator")
public class MohDsActionDelegator {

    private static final String DEFAULT_URI = "/main-web/eservice/INTERNET/MohDataSubmissionsInbox";
    private static final String ACTION = "ds_action";

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Autowired
    private DpDataSubmissionService dpDataSubmissionService;

    @Autowired
    private ArCycleStageDelegator arCycleStageDelegator;
    @Autowired
    private IuiCycleStageDelegator iuiCycleStageDelegator;

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info("------- MohDsActionDelegator Start ------------");
        DataSubmissionHelper.clearSession(bpc.request);
    }

    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        String type = ParamUtil.getString(bpc.request, "type"); // rfc / preview
        String submissionNo = ParamUtil.getString(bpc.request, "submissionNo");
        log.info(StringUtil.changeForLog("------DS Type: " + dsType + " --- Submission No: " + submissionNo
                + " --- Type: " + type + " -----"));
        ParamUtil.setRequestAttr(bpc.request, ACTION, type);
    }

    /**
     * Step: PreparePreview
     *
     * @param bpc
     */
    public void preparePreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("------ PreparePreview -----" ));
        ParamUtil.setRequestAttr(bpc.request, "isValid", "Y");
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        String submissionNo = ParamUtil.getString(bpc.request, "submissionNo");
        if (DataSubmissionConsts.DS_AR.equals(dsType)) {
            ArSuperDataSubmissionDto arSuper = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                    submissionNo);
            initDataForView(arSuper, bpc.request);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuper, bpc.request);
        } else if (DataSubmissionConsts.DS_DRP.equals(dsType)){
            DpSuperDataSubmissionDto dpSuper = dpDataSubmissionService.getDpSuperDataSubmissionDto(submissionNo);
            DataSubmissionHelper.setCurrentDpDataSubmission(dpSuper, bpc.request);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "isValid", "N");
        }
        ParamUtil.setRequestAttr(bpc.request, "dsType", dsType);
    }


    public void initDataForView(ArSuperDataSubmissionDto arSuper, HttpServletRequest request) {
        if (arSuper != null) {
            if (arSuper.getArCycleStageDto() != null) {
                arCycleStageDelegator.init(request);
                arCycleStageDelegator.setCycleAgeByPatientInfoDtoAndHcicode(arSuper.getArCycleStageDto(), arSuper.getPatientInfoDto(),
                        arSuper.getPremisesDto().getHciCode());
                arCycleStageDelegator.setEnhancedCounsellingTipShow(request,arSuper.getArCycleStageDto(),true);
            } else if (arSuper.getIuiCycleStageDto() != null) {
                iuiCycleStageDelegator.init(request);
                arDataSubmissionService.setIuiCycleStageDtoDefaultVal(arSuper);
            }
        }
    }

    /**
     * Step: PrepareRfc
     *
     * @param bpc
     */
    public void prepareRfc(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("------ PrepareRfc -----" ));
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        String submissionNo = ParamUtil.getString(bpc.request, "submissionNo");
        String uri = "";
        if (StringUtil.isEmpty(dsType) || StringUtil.isEmpty(submissionNo)) {
            uri = DEFAULT_URI;
        } else if (DataSubmissionConsts.DS_AR.equals(dsType)) {
            uri = prepareArRfc(submissionNo, bpc.request);
        } else if (DataSubmissionConsts.DS_DRP.equals(dsType)) {
            uri = prepareDpRfc(submissionNo, bpc.request);
        }
        log.info(StringUtil.changeForLog("------URI: " + uri));
        ParamUtil.setRequestAttr(bpc.request, "uri", uri);
    }

    private String prepareDpRfc(String submissionNo, HttpServletRequest request) {
        String uri = "";
        DpSuperDataSubmissionDto dpSuper = dpDataSubmissionService.getDpSuperDataSubmissionDto(submissionNo);
        if (dpSuper == null) {
            uri = DEFAULT_URI;
        } else {
            if (DataSubmissionConsts.DP_TYPE_SBT_PATIENT_INFO.equals(dpSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohDPDataSumission/PreparePatientInfo";
            } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(dpSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohDPDataSumission/PrepareDrugPrecribed";
            } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(dpSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohDPDataSumission/PrepareSovenorInventory";
            }
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.DP_OLD_DATA_SUBMISSION,
                    CopyUtil.copyMutableObject(dpSuper));
            dpSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            dpSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
        }
        DataSubmissionHelper.setCurrentDpDataSubmission(dpSuper, request);
        return uri;
    }

    private String prepareArRfc(String submissionNo, HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuper = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                submissionNo);
        String uri;
        if (arSuper == null) {
            uri = DEFAULT_URI;
        } else {
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_OLD_DATA_SUBMISSION,
                    CopyUtil.copyMutableObject(arSuper));
            arSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            arSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
            if (arSuper.getDataSubmissionDto() != null) {
                DataSubmissionDto dataSubmissionDto = arSuper.getDataSubmissionDto();
                dataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
                dataSubmissionDto.setAmendReason(null);
                dataSubmissionDto.setAmendReasonOther(null);
                if (arSuper.getSelectionDto() != null) {
                    CycleStageSelectionDto selectionDto = arSuper.getSelectionDto();
                    if (StringUtil.isEmpty(selectionDto.getStage()) || StringUtil.isEmpty(selectionDto.getCycle())) {
                        selectionDto.setStage(dataSubmissionDto.getCycleStage());
                        selectionDto.setCycle(arSuper.getCycleDto().getCycleType());
                    }
                }
            }
            if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(arSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARPatientInformationManual";
            } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(arSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARSubmitDonor";
            } else if (arSuper.getDataSubmissionDto() == null
                    || StringUtil.isEmpty(arSuper.getDataSubmissionDto().getCycleStage())) {
                uri = DEFAULT_URI;
            } else {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARCycleStagesManual/PrepareStage?crud_type="
                        + DataSubmissionConstant.CRUD_TYPE_RFC;
            }
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuper, request);
        return uri;
    }

    /**
     * Step: Redirection
     *
     * @param bpc
     */
    public void doRedirection(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("------ Redirection -----" ));
        String uri = (String) ParamUtil.getRequestAttr(bpc.request, "uri");
        if (StringUtil.isEmpty(uri)) {
            uri = DEFAULT_URI;
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(uri);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        log.info(StringUtil.changeForLog("------URL: " + tokenUrl));
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

}
