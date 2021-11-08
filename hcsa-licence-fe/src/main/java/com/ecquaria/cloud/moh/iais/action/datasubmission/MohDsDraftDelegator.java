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
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Process:
 *
 * @Description MohDsDraftDelegator
 * @Auther chenlei on 11/8/2021.
 */
@Slf4j
@Delegator("mohDsDraftDelegator")
public class MohDsDraftDelegator {

    @Autowired
    private ArFeClient arFeClient;

    private static final String DEFAULT_URI = "/main-web/eservice/INTERNET/MohInternetInbox";

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info("------- MohDsDraftDelegator Start ------------");
        HttpSession session = bpc.request.getSession();
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES_MAP);
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES);
        session.removeAttribute(DataSubmissionConstant.AR_DATA_SUBMISSION);
    }

    /**
     * Step: PrepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) {
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        String draftNo = ParamUtil.getString(bpc.request, "draftNo");
        log.info(StringUtil.changeForLog("------DS Type: " + dsType + " : Draft No: " + draftNo + " -----"));
        String uri = "";
        if (StringUtil.isEmpty(dsType) || StringUtil.isEmpty(draftNo)) {
            uri = DEFAULT_URI;
        } else if (DataSubmissionConsts.DS_TYPE_AR.equals(dsType)) {
            ArSuperDataSubmissionDto dataSubmissionDto = arFeClient.getArSuperDataSubmissionDtoDraftByDraftNo(draftNo).getEntity();
            if (dataSubmissionDto == null) {
                uri = DEFAULT_URI;
            } else if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(dataSubmissionDto.getArSubmissionType())) {
                dataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARPatientInformationManual";
            } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(dataSubmissionDto.getArSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARSubmitDonor";
            } else if (dataSubmissionDto.getCurrentDataSubmissionDto() == null
                    || StringUtil.isEmpty(dataSubmissionDto.getCurrentDataSubmissionDto().getCycleStage())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARCycleStagesManual";
            } else {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARCycleStagesManual/PrepareStage?crud_type=fromDraft";
            }
            DataSubmissionHelper.setCurrentArDataSubmission(dataSubmissionDto, bpc.request);
        }
        if (StringUtil.isEmpty(uri)) {
            uri = DEFAULT_URI;
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
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(uri);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

}
