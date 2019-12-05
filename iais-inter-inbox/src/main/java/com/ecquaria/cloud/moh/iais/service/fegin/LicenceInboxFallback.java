package com.ecquaria.cloud.moh.iais.service.fegin;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-29 13:20
 **/
@Component
public class LicenceInboxFallback implements LicenceInboxClient {
    @Override
    public FeignResponseEntity<SearchResult<InboxLicenceQueryDto>> searchResultFromLicence(SearchParam searchParam){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
