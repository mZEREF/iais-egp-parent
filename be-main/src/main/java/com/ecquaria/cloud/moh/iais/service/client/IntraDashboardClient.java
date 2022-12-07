package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAllActionAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAllGrpAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAppDetailsQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashKpiPoolAjaxQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashKpiPoolQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashRenewAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashRenewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashReplyAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashReplyQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWaitApproveAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWaitApproveQueryDto;
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

    @PostMapping(value = "/halp-intra-dash/dash-kpi-pool/drop")
    FeignResponseEntity<SearchResult<DashKpiPoolAjaxQuery>> searchDashKpiPoolDropResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-dash/dash-reply-pool")
    FeignResponseEntity<SearchResult<DashReplyQueryDto>> searchDashReplyPoolResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-dash/dash-reply-pool/drop")
    FeignResponseEntity<SearchResult<DashReplyAjaxQueryDto>> searchDashReplyPoolDropResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-dash/dash-w-appr")
    FeignResponseEntity<SearchResult<DashWaitApproveQueryDto>> searchDashWaitApproveResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-dash/dash-w-appr/drop")
    FeignResponseEntity<SearchResult<DashWaitApproveAjaxQueryDto>> searchDashWaitApproveDropResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-dash/dash-renew-pool")
    FeignResponseEntity<SearchResult<DashRenewQueryDto>> searchDashRenewResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-dash/dash-renew-pool/drop")
    FeignResponseEntity<SearchResult<DashRenewAjaxQueryDto>> searchDashRenewDropResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-dash/dash-all-action-app")
    FeignResponseEntity<SearchResult<DashAllActionAppQueryDto>> searchDashAllActionAppResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-dash/dash-all-grp-app")
    FeignResponseEntity<SearchResult<DashAllGrpAppQueryDto>> searchDashAllGrpAppResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-dash/dash-app-details")
    FeignResponseEntity<SearchResult<DashAppDetailsQueryDto>> searchDashAppDetailsResult(@RequestBody SearchParam searchParam);
}
