package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolAjaxQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashKpiPoolQuery;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ShiCheng_Xu
 */
@FeignClient(name = "halp-report", configuration = FeignConfiguration.class,
        fallback = IntraDashboardClientFallback.class)
public interface IntraDashboardClient {

    @PostMapping(value = "/halp-intra-dash/dash-kpi-pool")
    FeignResponseEntity<SearchResult<DashKpiPoolQuery>> searchDashKpiPoolResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/dash-kpi-pool/drop")
    FeignResponseEntity<SearchResult<DashKpiPoolAjaxQuery>> searchDashKpiPoolDropResult(@RequestBody SearchParam searchParam);
}
