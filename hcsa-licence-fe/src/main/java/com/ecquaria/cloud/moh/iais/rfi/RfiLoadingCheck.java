package com.ecquaria.cloud.moh.iais.rfi;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;

/**
 * @author Wenkang
 * @date 2021/3/17 14:29
 */
public interface RfiLoadingCheck {
    default void checkPremiseInfo(AppSubmissionDto appSubmissionDto,String appNo) throws Exception{}
    default void beforeSubmitRfi(AppSubmissionDto appSubmissionDto,String appNo) throws Exception{}
}
