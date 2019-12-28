package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author: yichen
 * @date time:12/28/2019 2:55 PM
 * @description:
 */

public class OnlineApptClientFallback implements OnlineApptClient{

    @Override
    public FeignResponseEntity<SearchResult<ApptBlackoutDateQueryDto>> doQuery(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
