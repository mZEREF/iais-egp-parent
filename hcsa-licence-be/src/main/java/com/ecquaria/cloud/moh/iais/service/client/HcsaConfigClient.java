package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.*;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/12/4 15:27
 */
@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class,
        fallback = HcsaConfigClientFallback.class)
public interface HcsaConfigClient {
    @RequestMapping(path = "/iais-hcsa-service/list-svc-doc-config",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcDocConfigDto>> listSvcDocConfig(@RequestBody List<String> docId);
    @RequestMapping(path = "/hcsa-routing/stage-id",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcRoutingStageDto>> getStageName(@RequestParam("serviceId") String serviceId ,
                                                                   @RequestParam("stageId") String stageId);
    @RequestMapping(path = "/iais-hcsa-service/hcsa-service-by-ids",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaService(@RequestBody List<String> serviceId);

    @GetMapping(path = "/iais-hcsa-service/one-of-hcsa-service/{serviceId}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<HcsaServiceDto> getHcsaServiceDtoByServiceId(@PathVariable(value = "serviceId") String serviceId);

    @GetMapping(value = "/iais-hcsa-service/sub-type/{subTypeId}")
    FeignResponseEntity<HcsaServiceSubTypeDto> getHcsaServiceSubTypeById(@PathVariable("subTypeId")String subTypeId);

    @GetMapping(value = "/hcsa-routing/stagelist")
    FeignResponseEntity<List<HcsaSvcRoutingStageDto>> stagelist();

    @GetMapping(value = "/hcsa-routing/list-workload")
    FeignResponseEntity<List<HcsaSvcStageWorkloadDto>> listHcsaSvcStageWorkloadEntity(String code);

    @PostMapping(value = "/hcsa-routing/save-stage")
    FeignResponseEntity<Void> saveStage(@RequestBody Map<String , List<HcsaSvcSpecificStageWorkloadDto>> map);

    @PostMapping(value = "/hcsa-routing/calculate-workload")
    FeignResponseEntity<List<HcsaSvcSpecificStageWorkloadDto>> calculateWorkload(@RequestBody String serviceId);

    @PostMapping(value = "/iais-hcsa-service/application-premises-by-ids")
    FeignResponseEntity<List<HcsaSvcSpePremisesTypeDto>> applicationPremisesByIds(@RequestBody List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList);

    @RequestMapping(path = "/iais-hcsa-risk/RiskResult",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<RiskResultDto>> getRiskResult(@RequestBody List<RiskAcceptiionDto> riskAcceptiionDtoList);

    @GetMapping(value = "/iais-hcsa-service/active-service",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getActiveServices();


    @RequestMapping(value = "/iais-hcsa-risk/FinancialShow",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<RiskFinancialShowDto> getRiskFinShow(@RequestBody List<HcsaServiceDto> svcList);

    @GetMapping(value = "finriskbysvccode{serviceCode}")
    FeignResponseEntity<List<HcsaRiskFinanceMatrixDto>> getFinianceRiskBySvcCode(@PathVariable("serviceCode")String serviceCode);

    @PostMapping(value = "/hcsa-routing/svc-work-group-order",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcStageWorkingGroupDto>>getSvcWorkGroup(@RequestBody HcsaSvcStageWorkingGroupDto dto);
}
