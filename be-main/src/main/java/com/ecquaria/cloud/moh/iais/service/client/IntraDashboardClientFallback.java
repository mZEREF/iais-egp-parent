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
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author Shicheng
 * @date 2021/4/23 14:49
 **/
public class IntraDashboardClientFallback implements IntraDashboardClient {
    @Override
    public FeignResponseEntity<SearchResult<DashKpiPoolQuery>> searchDashKpiPoolResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashKpiPoolResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashKpiPoolAjaxQuery>> searchDashKpiPoolDropResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashKpiPoolDropResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashReplyQueryDto>> searchDashReplyPoolResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashReplyPoolResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashReplyAjaxQueryDto>> searchDashReplyPoolDropResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashReplyPoolDropResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashWaitApproveQueryDto>> searchDashWaitApproveResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashWaitApproveResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashWaitApproveAjaxQueryDto>> searchDashWaitApproveDropResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashWaitApproveDropResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashRenewQueryDto>> searchDashRenewResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashRenewResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashRenewAjaxQueryDto>> searchDashRenewDropResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashRenewDropResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashAllActionAppQueryDto>> searchDashAllActionAppResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashAllActionAppResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashAllGrpAppQueryDto>> searchDashAllGrpAppResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashAllGrpAppResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashAppDetailsQueryDto>> searchDashAppDetailsResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashAppDetailsResult",searchParam);
    }
}
