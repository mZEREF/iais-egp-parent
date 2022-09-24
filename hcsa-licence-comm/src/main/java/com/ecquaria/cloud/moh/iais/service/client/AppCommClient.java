package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpSecondAddrDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.service.callback.AppCommClientFallback;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther chenlei on 5/3/2022.
 */
@RequestMapping(value = "/hcsa-app-common")
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class, fallback = AppCommClientFallback.class)
public interface AppCommClient {

    @GetMapping(path = "/application-licenceId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getAppByLicIdAndExcludeNew(@RequestParam(name = "licenceId") String licenceId);

    @GetMapping(value = "/application-group-no", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getApplicationsByGroupNo(@RequestParam("groupNo") String groupNo);

    @GetMapping(value = "applicationdto-id/{appId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationById(@PathVariable(name = "appId") String appId);

    @GetMapping(value = "/application-dto-by-appNo",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationDtoByAppNo(@RequestParam("appNo") String appNo);

    @GetMapping(value = "/licence-appeal-or-cessation-by-licence-id")
    FeignResponseEntity<Boolean> isLiscenceAppealOrCessation(@RequestParam("licenceId") String licenceId);

    @GetMapping(value = "/application/grp-premises/{appPreId}")
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesById(@PathVariable(name = "appPreId")String appPreId);

    @GetMapping(value = "/active-pending-premises/{licenseeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPremisesDto>> getActivePendingPremises(@PathVariable("licenseeId") String licenseeId);

    @GetMapping(value = "/max-seq-num-svc-doc", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcDocDto>> getMaxSeqNumSvcDocList(@RequestParam("appGrpId") String appGrpId);

    @PostMapping(value = "/pending-app-premises", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPremisesDto>> getPendAppPremises(@RequestBody AppPremisesDoQueryDto appPremisesDoQueryDto);

    @GetMapping(path = "/active-application-premises/{appNo}")
    FeignResponseEntity<AppGrpPremisesDto> getActivePremisesByAppNo(@PathVariable("appNo") String appNo);

    @GetMapping(value = "/active-vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> getActiveVehicles();

    @GetMapping(value = "/active-pending-vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> getActivePendingVehicles();

    @GetMapping(value = "/{premType}/active-pending-Premises", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPremisesDto>> getActivePendingPremisesByPremType(@PathVariable("premType") String premType);

    @GetMapping(value = "/app-premise-miscs", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscsByConds(@RequestParam("type") String type,
            @RequestParam("appId") String appId,
            @RequestParam(value = "excludeStatus", required = false) List<String> excludeStatus);

    @PostMapping(value = "/app-premise-misc-dto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> saveAppPremiseMiscDto(@RequestBody List<AppPremiseMiscDto> appPremiseMiscDtoList);

    @GetMapping(path = "/app-submission/{appNo}", produces = MediaType.APPLICATION_JSON_VALUE )
    FeignResponseEntity<AppSubmissionDto> getAppSubmissionDtoByAppNo(@PathVariable("appNo") String appNo,
            @RequestParam(name="onlyItself", required = false) Boolean onlyItself);

    @GetMapping(path = "/rfi-app-submission/{appNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> getRfiAppSubmissionDtoByAppNo(@PathVariable("appNo") String appNo);

    @PostMapping(value = "/app-group-misc-dto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGroupMiscDto> saveAppGroupMiscDto(@RequestBody AppGroupMiscDto appGroupMiscDto);

    @GetMapping(value = "/max-version-svc-com-doc", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSvcDocDto> getMaxVersionSvcComDoc(@RequestParam(name = "appGrpId") String appGrpId,
            @RequestParam(name = "configDocId") String configDocId, @RequestParam(name = "seqNum") String seqNum);

    @PostMapping(value = "/max-version-svc-spec-doc", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSvcDocDto> getMaxVersionSvcSpecDoc(@RequestBody AppSvcDocDto appSvcDocDto,
            @RequestParam(name = "appNo") String appNo);

    @PostMapping(value = "/find-all-app-prem-specialised-dto", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremSpecialisedDto>> getAppPremSpecialisedDtoList(List<String> appPremCorreIds);

    @PostMapping(value = "/save-secondAddress",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpSecondAddrDto>> saveSecondAddress(@RequestBody List<AppGrpSecondAddrDto> addrDtos);
}
