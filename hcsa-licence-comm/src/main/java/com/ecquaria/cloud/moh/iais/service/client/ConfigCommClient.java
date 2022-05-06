package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SpecicalPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaLastInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGlobalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGolbalExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.service.callback.ConfigCommClientFallback;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * AppConfigClient
 *
 * @author Jinhua
 * @date 2019/11/19 9:47
 */
@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class, fallback = ConfigCommClientFallback.class)
public interface ConfigCommClient {

    @RequestMapping(path = "/iais-hcsa-service/hcsa-service-by-ids",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceDtosById(List<String> ids);

    @RequestMapping(path = "/iais-hcsa-service/all-service",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>>  allHcsaService();

    @GetMapping(value = "/iais-hcsa-service/active-service",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getActiveServices();

    @PostMapping(path = "/iais-hcsa-service/list-svc-doc-config", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcDocConfigDto>> listSvcDocConfig(@RequestBody List<String> docId);

    @GetMapping(path = "/iais-hcsa-service/list-svc-doc-config/{docId}",produces =MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcDocConfigDto>> getPrimaryDocConfigList(@PathVariable(name="docId") String docId);

    @PostMapping(value = "/iais-hcsa-service/service-by-name", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceByNames(@RequestBody List<String> svcNames);

    @GetMapping(value = {"/iais-service-step/steps/{serviceId}"})
    FeignResponseEntity<List<HcsaServiceStepSchemeDto>> getServiceStepsByServiceId(@PathVariable(name ="serviceId") String serviceId);

    @PostMapping(value = "/iais-hcsa-service/all-specific-service-id-type",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity< List<HcsaSvcPersonnelDto>>  getServiceSpecificPerson(@RequestBody List<SpecicalPersonDto> list);

    @PostMapping(path = "/iais-hcsa-checklist/config/self-desc-results",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ChecklistQuestionDto>> listSelfDescConfig(@RequestBody SearchParam searchParam);

    @RequestMapping(path = "/iais-hcsa-service/hcsa-service-by-ids",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaService(@RequestBody List<String> serviceId);

    @RequestMapping(path = "/iais-hcsa-service/application-type-by-ids",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Set<String>> getAppGrpPremisesTypeBySvcId(@RequestBody List<String> serviceId);

    @RequestMapping(path = "/iais-hcsa-service/service/{code}",method = RequestMethod.GET)
    FeignResponseEntity<String>  getServiceIdByCode(@PathVariable(name = "code")String svcCode);

    @RequestMapping(path = "/iais-hcsa-service/svc-doc-config-results",method = RequestMethod.GET)
    FeignResponseEntity<List<HcsaSvcDocConfigDto>> getHcsaSvcDocConfig(@RequestParam(value = "string") String docMapJson);

    @GetMapping(path = "/iais-hcsa-service/one-of-hcsa-service/{serviceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaServiceDto> getHcsaServiceDtoByServiceId(@PathVariable(value = "serviceId") String serviceId);

    @GetMapping(path = "/iais-hcsa-checklist/config/common-self-desc/id")
    FeignResponseEntity<String> getCommonConfigIdForSelfDecl();

    @RequestMapping(path = "/iais-hcsa-risk/PreOrPostInspection",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PreOrPostInspectionResultDto> recommendIsPreInspection(@RequestBody RecommendInspectionDto dto);

    @RequestMapping(path = "/iais-hcsa-risk/RiskResult",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<RiskResultDto>> getRiskResult(@RequestBody List<RiskAcceptiionDto> riskAcceptiionDtoList);

    @RequestMapping(path = "/iais-hcsa-fee/calculate-fee",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeeDto> newFee(@RequestBody List<LicenceFeeDto> dto);

    @GetMapping(path = "/iais-hcsa-checklist/config/{id}")
    FeignResponseEntity<ChecklistConfigDto> getChecklistConfigById(@PathVariable(value = "id") String configId);

    @GetMapping(path = "/iais-hcsa-checklist/config/{ids}/list")
    FeignResponseEntity<List<ChecklistConfigDto>> getChecklistConfigByIds(@PathVariable(value = "ids")  List<String> ids);

    @RequestMapping(path = "/iais-hcsa-service/subtype-subsumed/{svcId}",method = RequestMethod.GET)
    FeignResponseEntity<List<HcsaSvcSubtypeOrSubsumedDto>> listSubCorrelation(@PathVariable(name = "svcId")String serviceId);

    @RequestMapping(path = "/iais-hcsa-service/service-type-results",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcPersonnelDto>> getServiceType(@RequestParam("serviceId") String serviceId, @RequestParam("psnType") String psnType);

    @GetMapping(value = "/iais-hcsa-service/sub-type/{subTypeId}")
    FeignResponseEntity<HcsaServiceSubTypeDto> getHcsaServiceSubTypeById(@PathVariable("subTypeId")String subTypeId);


    @GetMapping(value = "/iais-hcsa-checklist/config/results-max-version/{svcCode}/{type}/{module}")
    FeignResponseEntity<ChecklistConfigDto> getMaxVersionConfigByParams(@PathVariable("svcCode")String svcCode,
                                                                        @PathVariable("type")String type,
                                                                        @PathVariable("module") String module);

    @GetMapping(value = "/iais-hcsa-checklist/config/results-max-version/{svcCode}/{type}/{module}/{subTypeName}")
    FeignResponseEntity<ChecklistConfigDto> getMaxVersionConfigByParams(@PathVariable("svcCode")String svcCode,
                                                                        @PathVariable("type")String type,
                                                                        @PathVariable("module") String module,
                                                                        @PathVariable(value = "subTypeName", required = false) String subTypeName);

    @GetMapping(path = "/iais-hcsa-checklist/common-config-max-version/results", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ChecklistConfigDto> getMaxVersionCommonConfig();

    @GetMapping(value = "/iais-hcsa-service/service-correlation",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceCorrelationDto>> serviceCorrelation();

    @GetMapping(path = "/iais-hcsa-checklist/item/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ChecklistItemDto> getChklItemById(@PathVariable(value = "id") String id);

    /**
    * @author: yichen 
    * @description: FE DATA
    * @param: 
    * @return: 
    */
    @PostMapping(value = "/iais-hcsa-service/pref-period/results",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<HcsaServicePrefInspPeriodQueryDto>> getHcsaServicePrefInspPeriodList(@RequestBody SearchParam searchParam);

    @GetMapping(value = "/iais-hcsa-service/pref-period/{svcCode}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaServicePrefInspPeriodDto> getHcsaServicePrefInspPeriod(@PathVariable(name = "svcCode") String svcCode);

    @GetMapping(value = "/iais-hcsa-service/pref-period/", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServicePrefInspPeriodDto>> getPrefInspPeriodList();

    @PostMapping(value = "/iais-hcsa-service/pref-period/after-app/max-period",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> getMaxAfterAppBySvcCodeList(@RequestBody List<String> svcCodeList);

    @PostMapping(value = {"/iais-service-step/steps/serviceIds"}, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceStepSchemeDto>> getServiceStepsByServiceIds(@RequestBody List<String> serviceIds);

    @GetMapping(value = "/iais-hcsa-risk/riskInspection/{svcCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskInspectionMatrixDto>> getInspectionBySvcCode(@PathVariable("svcCode")String svcCode);

    @GetMapping(value = "/iais-hcsa-risk/golbalRiskMatraix/{serviceCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaRiskGlobalDto> getRiskGolbalRiskMatraixBySvcCode(@PathVariable("serviceCode")String serviceCode);

    @GetMapping(value = "/iais-hcsa-risk/golbalextbyid{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskGolbalExtDto>> getRiskGolbalextDtoById(@PathVariable("id")String id);

    @PostMapping(value = "/iais-hcsa-fee/fee-amendment", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeeDto> amendmentFee(@RequestBody AmendmentFeeDto amendmentFeeDto);

    @GetMapping(path = "/iais-hcsa-risk/lastandaecriskScore",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaLastInspectionDto> getLastAndSecRiskScore(@RequestBody HcsaLastInspectionDto inspDto);

    @GetMapping(path = "/iais-hcsa-service/service-by-name/{svcName}")
    FeignResponseEntity<String> getServiceCodeByName(@PathVariable(name= "svcName") String svcName);

    @GetMapping(value = "/iais-hcsa-service/avtice-hcas-service-by-name",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaServiceDto> getActiveHcsaServiceDtoByName(@RequestParam("svcName") String svcName);
    @GetMapping(path = "/iais-hcsa-service/{serviceId}")
    FeignResponseEntity<String> getServiceNameById(@PathVariable("serviceId")String serviceId);

    @PostMapping  (value = "/iais-hcsa-fee/fee-renew", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeeDto> renewFee(@RequestBody List<LicenceFeeDto> dtos);

    @GetMapping(value = "/hcsa-config/hcsa-svc-perosnnel-by-service-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaSvcPersonnelDto> getHcsaSvcPersonnelDtoByServiceId(@RequestParam("serviceId") String serviceId);

    @GetMapping (value = "/iais-regulation/regulation/{id}")
    FeignResponseEntity<HcsaChklSvcRegulationDto> getRegulationById(@PathVariable(value = "id") String id);
    @GetMapping(value = "/iais-hcsa-service/hcsa-svc-doc-config-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaSvcDocConfigDto> getHcsaSvcDocConfigDtoById(@RequestParam("id") String id);

    @GetMapping(value = "/iais-hcsa-service/avtice-hcas-service-by-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaServiceDto> getActiveHcsaServiceDtoById(@RequestParam("serviceId") String serviceId);

    @RequestMapping(path = "/iais-hcsa-service/primary-doc-by-version",method = RequestMethod.GET)
    FeignResponseEntity<List<HcsaSvcDocConfigDto>> getPrimaryDocConfigByVersion(@RequestParam(value = "version") String version);

    @GetMapping(value = "/iais-hcsa-service/active-service-correlation",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceCorrelationDto>> getActiveSvcCorrelation();

    @PostMapping(value = "/iais-hcsa-service/subtype-subsumed-list",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcSubtypeOrSubsumedDto>> getSvcSubtypeOrSubsumedByIdList(@RequestBody List<String> ids);

    @GetMapping(value = "/iais-hcsa-fee/active-bundle-item", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaFeeBundleItemDto>> getActiveBundleDtoList();

    @GetMapping(value = "/iais-hcsa-fee/fee-MatchingTh-ServiceCode/{serviceCode}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> getFeeMaxMatchingThByServiceCode(@PathVariable(name ="serviceCode") String svcCode);
}
