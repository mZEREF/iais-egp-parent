package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * @author Wenkang
 * @date 2021/4/27 15:47
 */
public interface ServiceInfoChangeEffectPerson {

    List<AppSubmissionDto> personContact(String licenseeId, AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto)
            throws Exception;

    List<AppSubmissionDto> personContact(String licenseeId, AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto,
            int check) throws Exception;

}
