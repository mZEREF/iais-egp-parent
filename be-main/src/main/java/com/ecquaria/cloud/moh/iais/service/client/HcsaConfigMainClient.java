package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Wenkang
 * @date 2019/12/4 15:27
 */
@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class,
        fallback = HcsaConfigMainClientFallback.class)
public interface HcsaConfigMainClient {
    @RequestMapping(path = "/iais-hcsa-service/list-svc-doc-config",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcDocConfigDto>> listSvcDocConfig(@RequestBody List<String> docId);
    @RequestMapping(path = "/hcsa-routing/stage-id",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcRoutingStageDto>> getStageName(@RequestParam("serviceId") String serviceId,
                                                                   @RequestParam("stageId") String stageId);
    @RequestMapping(path = "/iais-hcsa-service/hcsa-service-by-ids",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaService(@RequestBody List<String> serviceId);

    @GetMapping(path = "/iais-hcsa-service/one-of-hcsa-service/{serviceId}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<HcsaServiceDto> getHcsaServiceDtoByServiceId(@PathVariable(value = "serviceId") String serviceId);

    @GetMapping(value = "/iais-hcsa-service/sub-type/{subTypeId}")
    FeignResponseEntity<HcsaServiceSubTypeDto> getHcsaServiceSubTypeById(@PathVariable("subTypeId") String subTypeId);

    @GetMapping(value = "/hcsa-routing/stagelist")
    FeignResponseEntity<List<HcsaSvcRoutingStageDto>> stagelist();

    @GetMapping(value = "/hcsa-routing/list-workload")
    FeignResponseEntity<List<HcsaSvcStageWorkloadDto>> listHcsaSvcStageWorkloadEntity(String code);

    @PostMapping(value = "/hcsa-routing/save-stage")
    FeignResponseEntity<Void> saveStage(@RequestBody Map<String, List<HcsaSvcSpecificStageWorkloadDto>> map);

    @PostMapping(value = "/hcsa-routing/calculate-workload")
    FeignResponseEntity<List<HcsaSvcSpecificStageWorkloadDto>> calculateWorkload(@RequestBody String serviceId);

    @PostMapping(value = "/iais-hcsa-service/application-premises-by-ids")
    FeignResponseEntity<List<HcsaSvcSpePremisesTypeDto>> applicationPremisesByIds(@RequestBody List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList);

    @GetMapping(value = "/kpi-reminder/result-service-and-module")
    FeignResponseEntity<HcsaSvcKpiDto> searchKpiResult(@RequestParam("service") String serviceCode, @RequestParam("module") String module);

    @GetMapping(value = "/kpi-reminder/dashboard-result")
    FeignResponseEntity<List<HcsaSvcKpiDto>> retrieveForDashboard();

    @PostMapping(path = "/iais-hcsa-fee/returnable-fee",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> returnFee(@RequestBody List<ApplicationDto> applicationDtos);

    @RequestMapping(path = "/hcsa-routing/stage-service-id-type-stage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcRoutingStageDto>> getStageName(@RequestParam("serviceId") String serviceId,
                                                                   @RequestParam("stageId") String stageId,
                                                                   @RequestParam("type") String type);

    @PostMapping(value = "/hcsa-routing/stage-can-approve",produces = MediaType.APPLICATION_JSON_VALUE,consumes =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageDto(@RequestBody HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto);

    @PostMapping(value = "/hcsa-config/get-risk-score-lic-tenure", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaRiskScoreDto> getHcsaRiskScoreDtoByHcsaRiskScoreDto(@RequestBody HcsaRiskScoreDto hcsaRiskScoreDto);

    @GetMapping(value = "/iais-hcsa-service/active-service", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getActiveServices();
}
