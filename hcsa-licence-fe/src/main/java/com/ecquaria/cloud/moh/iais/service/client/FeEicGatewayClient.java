package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.config.FeignMultipartConfig;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * EicGatewayClient
 *
 * @author Jinhua
 * @date 2019/12/3 17:33
 */
@FeignClient(value = "eicgate", url="${iais.inter.gateway.url}", configuration = {FeignMultipartConfig.class},
        fallback = FeEicGatewayClientFallback.class)
public interface FeEicGatewayClient {
    @PostMapping(value = "/v1/file-sync-trackings/",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveFile(@RequestBody ProcessFileTrackDto processFileTrackDto,
                                         @RequestHeader("date") String date,
                                         @RequestHeader("authorization") String authorization,
                                         @RequestHeader("date-Secondary") String dateSec,
                                         @RequestHeader("authorization-Secondary") String authorizationSec);
    /**
    * @author: yichen
    * @description: route to BE db
    * @param:
    * @return:
    */
    @PostMapping(value = "/v1/self-decl-bridge/",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> routeSelfDeclData(@RequestBody List<String> contentJsonList,
                                                  @RequestHeader("date") String date,
                                                  @RequestHeader("authorization") String authorization,
                                                  @RequestHeader("date-Secondary") String dateSec,
                                                  @RequestHeader("authorization-Secondary") String authorizationSec);

/**
 *@Author :weilu on 2020/1/15 12:35
 *@param :
 *@return :
 *@Description :
 */
@PutMapping(value = "/v1/payment-status/",consumes = MediaType.APPLICATION_JSON_VALUE)
FeignResponseEntity<String> routePaymentStatus(@RequestBody ApplicationGroupDto applicationGroupDto,
                                               @RequestHeader("date") String date,
                                               @RequestHeader("authorization") String authorization,
                                               @RequestHeader("date-Secondary") String dateSec,
                                               @RequestHeader("authorization-Secondary") String authorizationSec);





    @PostMapping(value = "/v1/rfi-reply-bridge/",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> routeRfiData(@RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto,
                                                               @RequestHeader("date") String date,
                                                               @RequestHeader("authorization") String authorization,
                                                               @RequestHeader("date-Secondary") String dateSec,
                                                               @RequestHeader("authorization-Secondary") String authorizationSec);
}