package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicAppMainQueryResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenseeQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.RfiTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Shicheng
 * @date 2021/11/29 17:40
 **/
@FeignClient(name = "halp-report", configuration = FeignConfiguration.class,
        fallback = ReportBeViewTaskAssignClientFallback.class)
public interface ReportBeViewTaskAssignClient {
    @PostMapping(value = "/halp-intra-dash/super-pool-drop")
    FeignResponseEntity<SearchResult<SuperPoolTaskQueryDto>> searchSuperDropPoolResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-enquiry/enquiry-main")
    FeignResponseEntity<SearchResult<LicAppMainQueryResultDto>> searchMainQueryResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-enquiry/enquiry-application")
    FeignResponseEntity<SearchResult<ApplicationQueryResultsDto>> searchApplicationQueryResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-enquiry/enquiry-licence")
    FeignResponseEntity<SearchResult<LicenceQueryResultsDto>> searchLicenceQueryResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-enquiry/enquiry-licensee")
    FeignResponseEntity<SearchResult<LicenseeQueryResultsDto>> searchLicenseeQueryResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-enquiry/enquiry-licence-app-tab")
    FeignResponseEntity<SearchResult<ApplicationTabQueryResultsDto>> searchLicenceAppTabQueryResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-enquiry/enquiry-licence-ins-tab")
    FeignResponseEntity<SearchResult<InspectionTabQueryResultsDto>> searchLicenceInsTabQueryResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/halp-intra-enquiry/enquiry-licence-Rfi-tab")
    FeignResponseEntity<SearchResult<RfiTabQueryResultsDto>> searchLicenceRfiTabQueryResult(@RequestBody SearchParam searchParam);

}
