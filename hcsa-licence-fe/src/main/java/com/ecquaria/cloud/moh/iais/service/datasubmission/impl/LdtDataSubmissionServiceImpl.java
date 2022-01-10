package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LdtFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.LdtDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LdtDataSubmissionServiceImpl implements LdtDataSubmissionService {
    @Autowired
    private LdtFeClient ldtFeClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private LicEicClient licEicClient;

    @Override
    public LdtSuperDataSubmissionDto saveLdtSuperDataSubmissionDto(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto) {
        return ldtFeClient.saveLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto).getEntity();
    }

    @Override
    public LdtSuperDataSubmissionDto getLdtSuperDataSubmissionDto(String submissionNo) {
        return ldtFeClient.getLdtSuperDataSubmissionDto(submissionNo).getEntity();
    }

    @Override
    public String getSubmissionNo() {
        String submissionNo = systemAdminClient.submissionID(DataSubmissionConsts.DS_LDT).getEntity();
        log.info(StringUtil.changeForLog("The submissionNo : " + submissionNo));
        return submissionNo;
    }

    @Override
    public String getDraftNo() {
        String draftNo = systemAdminClient.draftNumber(ApplicationConsts.DATA_SUBMISSION).getEntity();
        log.info(StringUtil.changeForLog("The Draft No: " + draftNo));
        return draftNo;
    }

    @Override
    public LdtSuperDataSubmissionDto saveDataSubmissionDraft(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto) {
        return ldtFeClient.doUpdateDataSubmissionDraft(ldtSuperDataSubmissionDto).getEntity();
    }

    @Override
    public LdtSuperDataSubmissionDto getLdtSuperDataSubmissionDraftByConds(String orgId) {
        if (StringUtil.isEmpty(orgId)) {
            return null;
        }
        return ldtFeClient.getLdtSuperDataSubmissionDtoDraftByConds(orgId).getEntity();
    }

    @Override
    public void deleteLdtSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType) {
        log.info(StringUtil.changeForLog("-----Draft Param: " + orgId + " : " + submissionType + " -----"));
        if (StringUtil.isEmpty(orgId) || StringUtil.isEmpty(submissionType)) {
            return;
        }
        ldtFeClient.deleteLdtSuperDataSubmissionDtoDraftByConds(orgId, submissionType);
    }

    @Override
    public void updateDataSubmissionDraftStatus(String draftId, String status) {
        log.info(StringUtil.changeForLog("------Draft ID: " + draftId + " - Status: " + status + "------"));
        if (StringUtil.isEmpty(draftId) || StringUtil.isEmpty(status)) {
            return;
        }
        ldtFeClient.updateDataSubmissionDraftStatus(draftId, status);
    }

    @Override
    public LdtSuperDataSubmissionDto saveLdtSuperDataSubmissionDtoToBE(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto) {
        log.info(StringUtil.changeForLog(" the saveLdtSuperDataSubmissionDtoToBE start ..."));
        ldtSuperDataSubmissionDto.setFe(false);
        ldtSuperDataSubmissionDto = feEicGatewayClient.saveBeLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto).getEntity();

        DataSubmissionDto dataSubmission = ldtSuperDataSubmissionDto.getDataSubmissionDto();
        String refNo = dataSubmission.getSubmissionNo() + dataSubmission.getVersion();
        log.info(StringUtil.changeForLog(" the saveLdtSuperDataSubmissionDtoToBE refNo is -->:" + refNo));
        EicRequestTrackingDto eicRequestTrackingDto = licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        if (eicRequestTrackingDto != null) {
            eicRequestTrackingDto.setProcessNum(eicRequestTrackingDto.getProcessNum() + 1);
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            licEicClient.saveEicTrack(eicRequestTrackingDto);
        } else {
            log.warn(StringUtil.changeForLog(" do not have the eicRequestTrackingDto for this  refNo -->:" + refNo));
        }
        log.info(StringUtil.changeForLog(" the saveLdtSuperDataSubmissionDtoToBE end ..."));
        return ldtSuperDataSubmissionDto;
    }

    @Override
    public LdtSuperDataSubmissionDto getLdtSuperDataSubmissionDtoByDraftNo(String draftNo) {
        if (StringUtil.isEmpty(draftNo)) {
            return null;
        }
        return ldtFeClient.getLdtSuperDataSubmissionDtoDraftByDraftNo(draftNo).getEntity();
    }
}
