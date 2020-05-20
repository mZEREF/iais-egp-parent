package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.FeSelfAssessmentSyncDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "v1/hcsa-app-officer", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getEmailByCorrelationIdAndStatusAndRole(@RequestParam(value = "corrData") String corrDataJson,
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
    @PostMapping(value = "/v1/app-file-sync-trackings/",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveFileApplication(@RequestBody ProcessFileTrackDto processFileTrackDto,
                                         @RequestHeader("date") String date,
                                         @RequestHeader("authorization") String authorization,
                                         @RequestHeader("date-Secondary") String dateSec,
                                         @RequestHeader("authorization-Secondary") String authorizationSec);


    @PostMapping(value = "/v1/self-decl-bridge/",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<IaisApiResult<AppPremisesSelfDeclChklDto>> routeSelfAssessment(@RequestBody FeSelfAssessmentSyncDataDto selfDeclSyncDataDto,
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
    @PostMapping(value = "/v1/app-self-desc-status-sync/",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> inactiveLastVersionRecord(@RequestBody List<String> lastVersionId,
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
    FeignResponseEntity<List<LicenceDto>> routePaymentStatus(@RequestBody ApplicationGroupDto applicationGroupDto,
                                                   @RequestHeader("date") String date,
                                                   @RequestHeader("authorization") String authorization,
                                                   @RequestHeader("date-Secondary") String dateSec,
                                                   @RequestHeader("authorization-Secondary") String authorizationSec);


    /**
     *@Author :weilu on 2020/3/12 17:03
     *@param :
     *@return :
     *@Description :update licence when effective date <= new Date
     */
    @PutMapping(value = "/v1/cessation-status/",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> updateLicenceStatus(@RequestBody List<LicenceDto> licenceDtos,
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

    @PostMapping(value = "/v1/hcsa-reschedule-appt/",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, List<ApptUserCalendarDto>>> getUserCalendarByUserId(@RequestBody AppointmentDto appointmentDto,
                                                                                 @RequestHeader("date") String date,
                                                                                 @RequestHeader("authorization") String authorization,
                                                                                 @RequestHeader("date-Secondary") String dateSec,
                                                                                 @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsa-appt-refno",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, List<ApptUserCalendarDto>>> getAppointmentByApptRefNo(@RequestBody List<String> apptRefNos,
                                                                                          @RequestHeader("date") String date,
                                                                                          @RequestHeader("authorization") String authorization,
                                                                                          @RequestHeader("date-Secondary") String dateSec,
                                                                                          @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsa-app-insdate-up", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApptInspectionDateDto> apptFeDataUpdateCreateBe(@RequestBody ApptInspectionDateDto apptInspectionDateDto,
                                                                        @RequestHeader("date") String date,
                                                                        @RequestHeader("authorization") String authorization,
                                                                        @RequestHeader("date-Secondary") String dateSec,
                                                                        @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsa-task-assignment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> createFeReplyTask(@RequestBody ApptFeConfirmDateDto apptFeConfirmDateDto,
                                                         @RequestHeader("date") String date,
                                                         @RequestHeader("authorization") String authorization,
                                                         @RequestHeader("date-Secondary") String dateSec,
                                                         @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsa-inspect-nc-up", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspRectificationSaveDto> feCreateAndUpdateItemDoc(@RequestBody InspRectificationSaveDto inspRectificationSaveDto,
                                                                           @RequestHeader("date") String date,
                                                                           @RequestHeader("authorization") String authorization,
                                                                           @RequestHeader("date-Secondary") String dateSec,
                                                                           @RequestHeader("authorization-Secondary") String authorizationSec);


    @PostMapping(value = "/v1/no-attach-emails", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> feSendEmail(@RequestBody EmailDto email,
                                                                           @RequestHeader("date") String date,
                                                                           @RequestHeader("authorization") String authorization,
                                                                           @RequestHeader("date-Secondary") String dateSec,
                                                                           @RequestHeader("authorization-Secondary") String authorizationSec);


    @GetMapping(value = "/v1/appt-public-holiday")
    FeignResponseEntity<List<PublicHolidayDto>> getpublicHoliday(@RequestHeader("date") String date,
                                                                 @RequestHeader("authorization") String authorization,
                                                                 @RequestHeader("date-Secondary") String dateSec,
                                                                 @RequestHeader("authorization-Secondary") String authorizationSec);

    @PutMapping(value = "/v1/appointment-status",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateUserCalendarStatus(@RequestBody ApptCalendarStatusDto apptCalDto,
                                                       @RequestHeader("date") String date,
                                                       @RequestHeader("authorization") String authorization,
                                                       @RequestHeader("date-Secondary") String dateSec,
                                                       @RequestHeader("authorization-Secondary") String authorizationSec);

    @GetMapping(value = "/v1/new-inbox-msg-no", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getMessageNo(@RequestHeader("date") String date,
                                            @RequestHeader("authorization") String authorization,
                                            @RequestHeader("date-Secondary") String dateSec,
                                            @RequestHeader("authorization-Secondary") String authorizationSec);

}