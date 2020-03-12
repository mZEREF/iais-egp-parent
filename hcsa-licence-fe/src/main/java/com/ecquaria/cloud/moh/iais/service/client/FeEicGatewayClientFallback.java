package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * EicGatewayClientFallback
 *
 * @author Jinhua
 * @date 2020/1/9 12:13
 */
public class FeEicGatewayClientFallback {
    public FeignResponseEntity<String> saveFile(ProcessFileTrackDto processFileTrackDto, String date,
                                         String authorization, String dateSec, String authorizationSec) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<String> routeSelfDeclData(List<String> contentJsonList, String date,
                                                         String authorization, String dateSec,
                                                         String authorizationSec) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    public FeignResponseEntity<List<List<ApptUserCalendarDto>>> getUserCalendarByUserId(AppointmentDto appointmentDto,
                                                                                        String date,
                                                                                        String authorization,
                                                                                        String dateSec,
                                                                                        String authorizationSec){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

//    public FeignResponseEntity<List<ApptUserCalendarAndUserIdDto>> getAppointmentByApptRefNo(List<String> apptRefNos,
//                                                                                             String date,
//                                                                                             String authorization,
//                                                                                             String dateSec,
//                                                                                             String authorizationSec){
//        FeignResponseEntity entity = new FeignResponseEntity<>();
//        HttpHeaders headers = new HttpHeaders();
//        entity.setHeaders(headers);
//        return entity;
//    }

    public FeignResponseEntity<ApptInspectionDateDto> apptFeDataUpdateCreateBe(ApptInspectionDateDto apptInspectionDateDto,
                                                                               String date,
                                                                               String authorization,
                                                                               String dateSec,
                                                                               String authorizationSec){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<InspRectificationSaveDto> feCreateAndUpdateItemDoc(InspRectificationSaveDto inspRectificationSaveDto,
                                                                                  String date,
                                                                                  String authorization,
                                                                                  String dateSec,
                                                                                  String authorizationSec){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<String> updateLicenceStatus(@RequestBody List<LicenceDto> licenceDtos,
                                                    @RequestHeader("date") String date,
                                                    @RequestHeader("authorization") String authorization,
                                                    @RequestHeader("date-Secondary") String dateSec,
                                                    @RequestHeader("authorization-Secondary") String authorizationSec){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;

    }

}
