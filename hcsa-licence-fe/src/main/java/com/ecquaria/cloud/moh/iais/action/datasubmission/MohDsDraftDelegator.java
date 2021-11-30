package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.Optional;

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
    private ArDataSubmissionService arDataSubmissionService;

    private static final String DEFAULT_URI = "/main-web/eservice/INTERNET/MohDataSubmissionsInbox";

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info("------- MohDsDraftDelegator Start ------------");
        DataSubmissionHelper.clearSession(bpc.request);
    }

    /**
     * Step: PrepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) {
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        String draftNo = ParamUtil.getString(bpc.request, "draftNo");
        log.info(StringUtil.changeForLog("------DS Type: " + dsType + " --- Draft No: " + draftNo + " -----"));
        String uri = "";
        if (StringUtil.isEmpty(dsType) || StringUtil.isEmpty(draftNo)) {
            uri = DEFAULT_URI;
        } else if (DataSubmissionConsts.DS_AR.equals(dsType)) {
            ArSuperDataSubmissionDto dataSubmissionDto = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByDraftNo(draftNo);
            if (dataSubmissionDto == null) {
                uri = DEFAULT_URI;
            } else if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(dataSubmissionDto.getSubmissionType())) {
                dataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARPatientInformationManual";
            } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(dataSubmissionDto.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARSubmitDonor";
            } else if (dataSubmissionDto.getDataSubmissionDto() == null
                    || StringUtil.isEmpty(dataSubmissionDto.getDataSubmissionDto().getCycleStage())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARCycleStagesManual";
            } else {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARCycleStagesManual/PrepareStage?crud_type=" + DataSubmissionConstant.CRUD_TYPE_FROM_DRAFT;
            }
            DataSubmissionHelper.setCurrentArDataSubmission(dataSubmissionDto, bpc.request);
            if (dataSubmissionDto != null && !DataSubmissionConsts.DS_APP_TYPE_NEW.equals(dataSubmissionDto.getAppType())) {
                String submissionNo = Optional.ofNullable(dataSubmissionDto.getDataSubmissionDto())
                        .map(DataSubmissionDto::getSubmissionNo)
                        .orElse(null);
                ArSuperDataSubmissionDto arSuper = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                        submissionNo);
                ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_OLD_DATA_SUBMISSION,
                        CopyUtil.copyMutableObject(arSuper));
            }
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
