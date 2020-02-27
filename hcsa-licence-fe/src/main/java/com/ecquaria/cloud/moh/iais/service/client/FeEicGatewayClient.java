package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
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
@FeignClient(value = "eicgate", url="${iais.inter.gateway.url}", configuration = {FeignConfiguration.class},
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
    FeignResponseEntity<IaisApiResult<AppPremisesSelfDeclChklDto>> routeSelfDeclData(@RequestBody List<String> contentJsonList,
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

    @PostMapping(value = "/v1/hcsa-reschedule-appt/",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<List<ApptUserCalendarDto>>> getUserCalendarByUserId(@RequestBody AppointmentDto appointmentDto,
                                                                                 @RequestHeader("date") String date,
                                                                                 @RequestHeader("authorization") String authorization,
                                                                                 @RequestHeader("date-Secondary") String dateSec,
                                                                                 @RequestHeader("authorization-Secondary") String authorizationSec);

//    @PostMapping(value = "/v1/hcsa-appt-refno",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
//    FeignResponseEntity<List<ApptUserCalendarAndUserIdDto>> getAppointmentByApptRefNo(@RequestBody List<String> apptRefNos,
//                                                                                      @RequestHeader("date") String date,
//                                                                                      @RequestHeader("authorization") String authorization,
//                                                                                      @RequestHeader("date-Secondary") String dateSec,
//                                                                                      @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsa-app-insdate-up", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApptInspectionDateDto> apptFeDataUpdateCreateBe(@RequestBody ApptInspectionDateDto apptInspectionDateDto,
                                                                        @RequestHeader("date") String date,
                                                                        @RequestHeader("authorization") String authorization,
                                                                        @RequestHeader("date-Secondary") String dateSec,
                                                                        @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsa-task-assignment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> createFeReplyTask(@RequestBody TaskDto taskDto,
                                                         @RequestHeader("date") String date,
                                                         @RequestHeader("authorization") String authorization,
                                                         @RequestHeader("date-Secondary") String dateSec,
                                                         @RequestHeader("authorization-Secondary") String authorizationSec);
}