package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.GenerateUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EicGatewayFeMainClient {
    @Value("${iais.inter.gateway.url}")
    private String gateWayUrl;

    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    public FeignResponseEntity<Boolean> updateApplicationStatus(RecallApplicationDto recallApplicationDto,
                                                                String date, String authorization, String dateSec,
                                                                String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-app-recall", HttpMethod.POST, recallApplicationDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, Boolean.class);
    }

    public FeignResponseEntity<RecallApplicationDto> syncAccountDataFormFe(OrganizationDto organizationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/user-account-sync", HttpMethod.POST, organizationDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), RecallApplicationDto.class);
    }


    public FeignResponseEntity<RecallApplicationDto> recallAppChangeTask(RecallApplicationDto recallApplicationDto,
                                                                         String date, String authorization, String dateSec,
                                                                         String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/task-recall", HttpMethod.POST, recallApplicationDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, RecallApplicationDto.class);
    }

    public FeignResponseEntity<List> recallAppTasks(List<RecallApplicationDto> recallApplicationDtoList,
                                                                         String date, String authorization, String dateSec,
                                                                         String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/task-list-recall", HttpMethod.POST, recallApplicationDtoList,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, RecallApplicationDto.class);
    }

    public FeignResponseEntity<String> saveFile(ProcessFileTrackDto processFileTrackDto,
                                         String date, String authorization, String dateSec,
                                         String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/file-sync-trackings", HttpMethod.POST, processFileTrackDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, String.class);
    }

    public FeignResponseEntity<GenerateUENDto> getUen(String uen) {

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        Map<String, Object> params = IaisCommonUtils.genNewHashMap();
        params.put("uen", uen);
        return IaisEGPHelper.callEicGatewayWithParam(gateWayUrl + "/v1/svc-sync-acra/{uen}", HttpMethod.PUT, params,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), GenerateUENDto.class);
    }
}
