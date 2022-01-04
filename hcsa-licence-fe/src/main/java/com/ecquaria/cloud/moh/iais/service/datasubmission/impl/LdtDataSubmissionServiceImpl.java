package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.LdtFeClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.LdtDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LdtDataSubmissionServiceImpl implements LdtDataSubmissionService {
    @Autowired
    LdtFeClient ldtFeClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Override
    public LdtSuperDataSubmissionDto saveDpSuperDataSubmissionDto(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto) {
        return ldtFeClient.saveDpSuperDataSubmissionDto(ldtSuperDataSubmissionDto).getEntity();
    }

    @Override
    public LdtSuperDataSubmissionDto getDpSuperDataSubmissionDto(String submissionNo) {
        return ldtFeClient.getDpSuperDataSubmissionDto(submissionNo).getEntity();
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
    public LdtSuperDataSubmissionDto saveDataSubmissionDraft(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto){
        return ldtFeClient.doUpdateDataSubmissionDraft(ldtSuperDataSubmissionDto).getEntity();
    }
}
