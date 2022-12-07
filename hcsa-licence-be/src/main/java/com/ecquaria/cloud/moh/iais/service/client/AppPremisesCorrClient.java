package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesUpdateEmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.HcsaTaskAssignDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * AppPremisesCorrelation
 *
 * @author Jinhua
 * @date 2019/12/10 19:53
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class, fallback = AppPremisesCorrClientFallback.class)
public interface AppPremisesCorrClient {

    @GetMapping(value = "/iais-apppremisescorrelation-be/app-premises-correlations/{appId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremisesCorrelationsByAppId(@PathVariable("appId") String appId);

    @GetMapping(value = "/iais-apppremisescorrelation-be/applicationDtosByPremCorreid/{appCorreId}", produces = MediaType.APPLICATION_JSON_VALUE ,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<ApplicationDto>> getApplicationDtosByCorreId(@PathVariable("appCorreId") String appCorreId);

    @GetMapping(value = "/iais-apppremisescorrelation-be/AppPremisesCorrelationsByPremises/{appCorreId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremisesCorrelationsByPremises(@PathVariable("appCorreId") String appCorreId);

    ///application-number-grp-premiese/{appPremcorrId}
    @GetMapping(value = "/application-number-grp-premiese/{appPremcorrId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppInsRepDto> appGrpPremises(@PathVariable("appPremcorrId") String appPremcorrId);

    @GetMapping(value = "/app-grp/all-address-pool",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaTaskAssignDto> getUnitNoAndAddressByAppGrpIds(@RequestBody List<String> appGroupIds);

    @GetMapping(value = "/opera-unit-floor/premises-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesOperationalUnitDto>> getUnitNoAndFloorByPremisesId(@RequestParam("premisesId") String premisesId);

    @GetMapping(value = "/iais-email/draft-email/{appPremCorrId}/{type}")
    FeignResponseEntity<AppPremisesUpdateEmailDto> retrieveEmailDraft(@PathVariable("appPremCorrId") String appPremCorrId, @PathVariable("type") String type);

    @PostMapping(value = "/iais-email/email-draft", consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> saveEmailDraft(@RequestBody AppPremisesUpdateEmailDto appPremisesUpdateEmailDto);


}
