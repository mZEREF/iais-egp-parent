package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

/**
 * AssessmentSchematicsClientFallback
 *
 * @author junyu
 * @date 2020/6/9
 */
public class AssessmentSchematicsClientFallback implements AssessmentSchematicsClient{
    @Override
    public FeignResponseEntity<SearchResult<InboxAppQueryDto>> searchResultFromApp(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchResultFromApp",searchParam);
    }

    @Override
    public FeignResponseEntity<ApplicationDraftDto> getDraftInfo(String draftId) {
        return IaisEGPHelper.getFeignResponseEntity("getDraftInfo",draftId);
    }
}
