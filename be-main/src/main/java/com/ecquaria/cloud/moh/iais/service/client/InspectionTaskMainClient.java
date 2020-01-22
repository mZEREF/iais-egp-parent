package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Shicheng
 * @date 2019/11/26 10:33
 **/
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = InspectionTaskMainClientFallback.class)
public interface InspectionTaskMainClient {
    @RequestMapping(path = "/iais-inspection/one-of-inspection/{applicationNo}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationDto> getApplicationDtoByAppNo(@PathVariable("applicationNo") String applicationNo);

    @RequestMapping(path = "/iais-inspection/appGroup-of-inspection/{appGroupId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationGroupDto> getApplicationGroupDtoByAppGroId(@PathVariable("appGroupId") String appGroupId);

    @RequestMapping(path = "/iais-inspection/application-premises-by-app-id/{appCorrId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppGroId(@PathVariable("appCorrId") String applicationId);

    @RequestMapping(path = "/iais-inspection/inspection-searchParam",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<InspectionCommonPoolQueryDto>> searchInspectionPool(SearchParam searchParam);

    @RequestMapping(path = "/iais-inspection/inspection-sub-searchParam",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<InspectionSubPoolQueryDto>> searchInspectionSupPool(SearchParam searchParam);

    @RequestMapping(path = "/iais-inspection/inspection-be-appGroup",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<InspectionAppGroupQueryDto>> searchInspectionBeAppGroup(SearchParam searchParam);

    @RequestMapping(path = "/iais-inspection/inspection-be-appGroup-ajax",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
        consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<InspectionAppInGroupQueryDto>> searchInspectionBeAppGroupAjax(SearchParam searchParam);

    @PostMapping(value = "/iais-application-history/historys-kpi",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getHistoryForKpi(@RequestBody AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto );

    @GetMapping(value = "/iais-inspection/ins-draf-date/{appPreCorrId}")
    FeignResponseEntity<AppPremInsDraftDto> getAppPremInsDraftDtoByAppPreCorrId(@PathVariable(name = "appPreCorrId") String appPreCorrId);
}
