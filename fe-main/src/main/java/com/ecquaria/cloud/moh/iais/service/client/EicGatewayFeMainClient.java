package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public class EicGatewayFeMainClient {
    @Value("${iais.inter.gateway.url}")
    private String gateWayUrl;

    public FeignResponseEntity<Boolean> updateApplicationStatus(RecallApplicationDto recallApplicationDto,
                                                                String date, String authorization, String dateSec,
                                                                String authorizationSec) {
        return IaisEGPHelper.callEicGateway(gateWayUrl + "/v1/hcsa-app-recall", HttpMethod.POST, recallApplicationDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, Boolean.class);
    }

    public FeignResponseEntity<RecallApplicationDto> syncAccountDataFormFe(OrganizationDto organizationDto,
                                                                  String date, String authorization, String dateSec,
                                                                  String authorizationSec) {
        return IaisEGPHelper.callEicGateway(gateWayUrl + "/v1/user-account-sync", HttpMethod.POST, organizationDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, RecallApplicationDto.class);
    }


    public FeignResponseEntity<RecallApplicationDto> recallAppChangeTask(RecallApplicationDto recallApplicationDto,
                                                                         String date, String authorization, String dateSec,
                                                                         String authorizationSec) {
        return IaisEGPHelper.callEicGateway(gateWayUrl + "/v1/task-recall", HttpMethod.POST, recallApplicationDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, RecallApplicationDto.class);
    }

    public FeignResponseEntity<String> saveFile(ProcessFileTrackDto processFileTrackDto,
                                         String date, String authorization, String dateSec,
                                         String authorizationSec) {
        return IaisEGPHelper.callEicGateway(gateWayUrl + "/v1/file-sync-trackings", HttpMethod.POST, processFileTrackDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, String.class);
    }
}
