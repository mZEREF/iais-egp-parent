package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.AuditSystemResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.AuditSystemRiskAccpetDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GolbalRiskShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaLastInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGlobalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGolbalExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLeadershipMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLegislativeMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLicenceTenureDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.LicenceTenShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLeaderShipShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLegislativeShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @GetMapping(value = "/manHour/{serviceId}/{stageId}")
    FeignResponseEntity<Integer> getManHour(@PathVariable("serviceId")String serviceId,@PathVariable("stageId")String stageId);

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

    @GetMapping(value = "/iais-hcsa-risk/finriskbysvccode{serviceCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskFinanceMatrixDto>> getFinianceRiskBySvcCode(@PathVariable("serviceCode")String serviceCode);

    @PostMapping(value = "/hcsa-routing/svc-work-group-order",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcStageWorkingGroupDto>>getSvcWorkGroup(@RequestBody HcsaSvcStageWorkingGroupDto dto);

    @RequestMapping(value = "/iais-hcsa-risk/FinanceMatrixMemoryStorage",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskFinanceMatrixDto>> saveFinRiskMatrix(@RequestBody List<HcsaRiskFinanceMatrixDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/FinanceMatrixMemoryStorageupdate",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskFinanceMatrixDto>> updateFinRiskMatrix(@RequestBody List<HcsaRiskFinanceMatrixDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/leadership/show",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<RiskLeaderShipShowDto> getRiskLeaderShipShow(@RequestBody List<HcsaServiceDto> svcList);

    @PostMapping(value = "/iais-hcsa-service/hcsa-service-type",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getHcsaServiceNameByType(@RequestBody String type);
    @GetMapping(value = "/iais-hcsa-risk/leadershipbysvccode{serviceCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskLeadershipMatrixDto>> getLeadershipRiskBySvcCode(@PathVariable("serviceCode")String serviceCode);

    @RequestMapping(value = "/iais-hcsa-risk/LeaderShipMatrixStorage",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskLeadershipMatrixDto>> saveLeadershipRiskMatrix(@RequestBody List<HcsaRiskLeadershipMatrixDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/LeaderShipMatrixStorageupdate",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskLeadershipMatrixDto>> updateLeadershipRiskMatrix(@RequestBody List<HcsaRiskLeadershipMatrixDto> finDtoList);

    @GetMapping(value = "/iais-hcsa-risk/leadershipbysvccode{serviceCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskLegislativeMatrixDto>> getLegislativeRiskBySvcCode(@PathVariable("serviceCode")String serviceCode);

    @RequestMapping(value = "/iais-hcsa-risk/legislativeshow",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<RiskLegislativeShowDto> getLegislativeShow(@RequestBody List<HcsaServiceDto> svcList);

    @PostMapping(value = "/iais-hcsa-service/pref-period/results",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<HcsaServicePrefInspPeriodQueryDto>> getHcsaServicePrefInspPeriodList(@RequestBody SearchParam searchParam);

    @PutMapping(value = "/iais-hcsa-service/pref-period",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> savePrefInspPeriod(@RequestBody HcsaServicePrefInspPeriodDto period);

    @RequestMapping(value = "/iais-hcsa-risk/legislativematrixstorage",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskLegislativeMatrixDto>> saveLegislativeRiskMatrix(@RequestBody List<HcsaRiskLegislativeMatrixDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/legislativematrixstorageup",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskLegislativeMatrixDto>> updateLegislativeRiskMatrix(@RequestBody List<HcsaRiskLegislativeMatrixDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/weightageshow",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaRiskWeightageShowDto> getWeightageShow(@RequestBody List<HcsaServiceDto> svcList);


    @PostMapping  (value = "/iais-hcsa-fee/fee-renew", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeeDto> renewFee(@RequestBody @Required List<LicenceFeeDto> dtos);

    @GetMapping(value = "/iais-hcsa-risk/weightagebycode{serviceCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskWeightageDto>> getWeightageRiskBySvcCode(@PathVariable("serviceCode")String serviceCode);

    @PutMapping(value = "/iais-hcsa-risk/WeightageMatrixup",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskWeightageDto>> updateWeightageMatrixList(@RequestBody List<HcsaRiskWeightageDto> finDtoList);

    @PostMapping(value = "/iais-hcsa-risk/WeightageMatrixStorage",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskWeightageDto>> saveWeightageMatrixList(@RequestBody List<HcsaRiskWeightageDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/golbalshow",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<GolbalRiskShowDto> getgolbalshow(@RequestBody List<HcsaServiceDto> svcList);

    @GetMapping(value = "/iais-hcsa-risk/golbalRiskMatraix/{serviceCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaRiskGlobalDto> getRiskGolbalRiskMatraixBySvcCode(@PathVariable("serviceCode")String serviceCode);

    @GetMapping(value = "/iais-hcsa-risk/golbalextbyid{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskGolbalExtDto>> getRiskGolbalextDtoById(@PathVariable("id")String id);

    @RequestMapping(value = "/iais-hcsa-risk/golbalrisktorage",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskGlobalDto>> saveGoalbalMatrixList(@RequestBody List<HcsaRiskGlobalDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/golbalriskupdate",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskGlobalDto>> udpateGoalbalMatrixList(@RequestBody List<HcsaRiskGlobalDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/golbalextrisktorage",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskGolbalExtDto>> saveGoalbalExtMatrixList(@RequestBody List<HcsaRiskGolbalExtDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/golbalextriskupdate",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskGolbalExtDto>> updateGoalbalExtMatrixList(@RequestBody List<HcsaRiskGolbalExtDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/singlegolbalextriskstorage",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaRiskGlobalDto> saveGoalbalMatrix(@RequestBody HcsaRiskGlobalDto finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/singlegolbalextriskup",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaRiskGlobalDto> udpateGoalbalMatrix(@RequestBody HcsaRiskGlobalDto finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/inspectionshow",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspectionShowDto> getInspectionshow(@RequestBody List<HcsaServiceDto> svcList);

    @RequestMapping(value = "/iais-hcsa-risk/InspectionMatrixStorage",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskInspectionMatrixDto>> saveInspectionMatrix(@RequestBody List<HcsaRiskInspectionMatrixDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/InspectionMatrixup",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskInspectionMatrixDto>> udpateInspectionMatrix(@RequestBody List<HcsaRiskInspectionMatrixDto> finDtoList);

    @GetMapping(value = "/iais-hcsa-risk/riskInspection/{svcCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskInspectionMatrixDto>> getInspectionBySvcCode(@PathVariable("svcCode")String svcCode);

    @RequestMapping(value = "/iais-hcsa-risk/licencetenureshow",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceTenShowDto> getLicenceTenureShow(@RequestBody List<HcsaServiceDto> svcList);

    @RequestMapping(path = "/iais-hcsa-service/subtype-subsumed/{svcId}",method = RequestMethod.GET)
    FeignResponseEntity<List<HcsaSvcSubtypeOrSubsumedDto>> listSubCorrelation(@PathVariable(name = "svcId")String serviceId);


    @RequestMapping(value = "/kpi-reminder/module-name-service-code",method = RequestMethod.GET)
    FeignResponseEntity<List<String>> getModuleName(@RequestParam("serviceCode") String serviceCode);

    @RequestMapping(value = "/kpi-reminder/result-service-and-module")
    FeignResponseEntity<HcsaSvcKpiDto> searchResult(@RequestParam("service") String serviceCode, @RequestParam("module") String module);

    @PostMapping(value = "/hcsa-routing/svc-work-task-dto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaSvcStageWorkingGroupDto> getHcsaSvcStageWorkingGroupDto(@RequestBody HcsaSvcStageWorkingGroupDto dto);
     @PostMapping(value = "/kpi-reminder/kpi-and-reminder",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity saveKpiAndReminder(@RequestBody HcsaSvcKpiDto  hcsaSvcKpiDto);


    @RequestMapping(value = "/iais-hcsa-risk/LicenceTenureMatrixStorage",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskLicenceTenureDto>> savehcsaRiskLicenceTenure(@RequestBody List<HcsaRiskLicenceTenureDto> finDtoList);

    @RequestMapping(value = "/iais-hcsa-risk/LicenceTenureMatrixup",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskLicenceTenureDto>> updatehcsaRiskLicenceTenure(@RequestBody List<HcsaRiskLicenceTenureDto> finDtoList);


    @GetMapping(value = "/iais-hcsa-risk/lictenurebycode{serviceCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaRiskLicenceTenureDto>> getgetLictenureByCode(@PathVariable("serviceCode")String serviceCode);

    @GetMapping(value = "/iais-hcsa-service/sub-correlation/{svcId}")
    FeignResponseEntity<List<HcsaSvcSubtypeOrSubsumedDto>> listSubCorrelationFooReport(@PathVariable(name = "svcId")String serviceId);

    @GetMapping(value = "/hcsa-routing/list-all-routing-stage",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <List<HcsaSvcRoutingStageDto>> getAllHcsaSvcRoutingStage();

    @GetMapping(value = "/hcsa-routing/hcsa-svc-routing-by-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageById(@RequestParam("id") String id);

    @GetMapping(path = "/iais-hcsa-risk/lastandaecriskScore",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaLastInspectionDto> getLastAndSecRiskScore(@RequestBody HcsaLastInspectionDto inspDto);

    @GetMapping(path = "/iais-hcsa-service/service-by-name/{svcName}")
    FeignResponseEntity<String> getServiceCodeByName(@PathVariable(name= "svcName") String svcName);

    @PostMapping(value = "/iais-hcsa-service/search-svcNames-param",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<HcsaSvcQueryDto>> searchSvcNamesParam(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-hcsa-service/appt-date-service",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppointmentDto> getApptStartEndDateByService(@RequestBody AppointmentDto appointmentDto);
    @PostMapping(value = "/iais-hcsa-risk/aduitsystemrsik", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AuditSystemResultDto>> getAuditSystemRiskResult(@RequestBody List<AuditSystemRiskAccpetDto> acceptDtoList);
    @GetMapping(value = "/iais-hcsa-service/all-service",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>>  allHcsaService();
    @PostMapping(value = "/iais-hcsa-service/hcsa-service-code",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceDtoByCode(@RequestBody List<String> code);
    @PostMapping(value = "/iais-hcsa-service/application-type-by-ids",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Set<String>> getAppGrpPremisesTypeBySvcId(@RequestBody List<String> serviceId);
    @PostMapping(value = "/hcsa-config/hcsa-service-config-dto",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity saveHcsaServiceConfig(@RequestBody HcsaServiceConfigDto hcsaServiceConfigDto);
    @GetMapping(value = "/iais-hcsa-service/svc-personnel-by-service-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcPersonnelDto>> getSvcPersonnelByServiceId(@RequestParam("serviceId") String serviceId);
    @PostMapping(value = "/hcsa-config/existence-of-hcsa-service",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> isExistHcsaService(@RequestBody HcsaServiceDto hcsaServiceDto );
    @GetMapping(value = "/hcsa-config/hcsa-routing-scheme-of-service-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcStageWorkloadDto>> getHcsaSvcSpeRoutingSchemeByServiceId(@RequestParam("serviceId") String serviceId);
    @GetMapping(value = "/hcsa-config/hcsa-working-group-service-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcStageWorkingGroupDto>>  getHcsaStageWorkingGroup(@RequestParam("serviceId") String serivceId);

    @GetMapping(path = "/iais-hcsa-service/servicedto-by-name/{svcName}")
    FeignResponseEntity<HcsaServiceDto> getServiceDtoByName(@PathVariable(name= "svcName") String svcName);

    @PostMapping(value = "/hcsa-config/hcsa-svc-working-group-by-stages",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcStageWorkingGroupDto>> getHcsaSvcWorkingGroupByStages(@RequestBody List<String> stageIds);
}
