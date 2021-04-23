package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolAjaxQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolQuery;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author Shicheng
 * @date 2021/4/23 14:49
 **/
public class IntraDashboardClientFallback implements IntraDashboardClient {
    @Override
    public FeignResponseEntity<SearchResult<DashKpiPoolQuery>> searchDashKpiPoolResult(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<SearchResult<DashKpiPoolAjaxQuery>> searchDashKpiPoolDropResult(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
