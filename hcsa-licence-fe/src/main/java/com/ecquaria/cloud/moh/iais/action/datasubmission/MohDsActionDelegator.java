package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpSession;
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

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info("------- MohDsActionDelegator Start ------------");
        HttpSession session = bpc.request.getSession();
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES_MAP);
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES);
        session.removeAttribute(DataSubmissionConstant.AR_DATA_SUBMISSION);
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
        ParamUtil.setRequestAttr(bpc.request, "title", "Assisted Reproduction Submission");
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        String submissionNo = ParamUtil.getString(bpc.request, "submissionNo");
        if (DataSubmissionConsts.DS_AR.equals(dsType)) {
            ArSuperDataSubmissionDto dataSubmissionDto = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                    submissionNo);
            DataSubmissionHelper.setCurrentArDataSubmission(dataSubmissionDto, bpc.request);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "isValid", "N");
        }
    }

    /**
     * Step: PrepareRfc
     *
     * @param bpc
     */
    public void prepareRfc(BaseProcessClass bpc) {
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        String submissionNo = ParamUtil.getString(bpc.request, "submissionNo");
        String uri = "";
        if (StringUtil.isEmpty(dsType) || StringUtil.isEmpty(submissionNo)) {
            uri = DEFAULT_URI;
        } else if (DataSubmissionConsts.DS_AR.equals(dsType)) {
            ArSuperDataSubmissionDto dataSubmissionDto = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                    submissionNo);
            if (dataSubmissionDto == null) {
                uri = DEFAULT_URI;
            } else {
                dataSubmissionDto.setSubmissionType(DataSubmissionConsts.DS_TYPE_RFC);
                if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(dataSubmissionDto.getArSubmissionType())) {
                    dataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARPatientInformationManual";
                } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(dataSubmissionDto.getArSubmissionType())) {
                    uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARSubmitDonor";
                } else if (dataSubmissionDto.getCurrentDataSubmissionDto() == null
                        || StringUtil.isEmpty(dataSubmissionDto.getCurrentDataSubmissionDto().getCycleStage())) {
                    uri = DEFAULT_URI;
                } else {
                    uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARCycleStagesManual/PrepareStage?crud_type="
                            + DataSubmissionConstant.CRUD_TYPE_RFC;
                }
            }
            DataSubmissionHelper.setCurrentArDataSubmission(dataSubmissionDto, bpc.request);
        }
        ParamUtil.setRequestAttr(bpc.request, "uri", uri);
    }

    /**
     * Step: Redirection
     *
     * @param bpc
     */
    public void doRedirection(BaseProcessClass bpc) throws IOException {
        String uri = (String) ParamUtil.getRequestAttr(bpc.request, "uri");
        if (StringUtil.isEmpty(uri)) {
            uri = DEFAULT_URI;
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(uri);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

}
