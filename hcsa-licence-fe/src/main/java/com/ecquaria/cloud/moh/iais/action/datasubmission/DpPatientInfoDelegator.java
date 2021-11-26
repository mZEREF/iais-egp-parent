package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;
/**
 * DpPatientInfoDelegator
 *
 * @author fanghao
 * @date 2021/11/25
 */


@Delegator("dpPatientInfoDelegator")
@Slf4j
public class DpPatientInfoDelegator extends DpCommonDelegator {

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {

    }
}
