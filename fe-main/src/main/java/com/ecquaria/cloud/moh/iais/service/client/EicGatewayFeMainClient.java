package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.GenerateUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LaboratoryDevelopTestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * @deprecated now we don't need to call this API any more
     *
     * @param uen
     * @return
     */
    @Deprecated
    public FeignResponseEntity<GenerateUENDto> getUen(String uen) {

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        Map<String, Object> params = IaisCommonUtils.genNewHashMap();
        params.put("uen", uen);
        return IaisEGPHelper.callEicGatewayWithParam(gateWayUrl + "/v1/svc-sync-acra/{uen}", HttpMethod.PUT, params,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), GenerateUENDto.class);
    }

    public ResponseEntity<String> getMyInfoEic(@RequestHeader("Authorization") String authorizationMyInfo, @PathVariable(name = "uinfin") String idNumber,
                                               @RequestParam(name = "attributes") String[] attrs, @RequestParam(name = "clientId") String clientId,
                                               @RequestParam(name = "singpassEserviceId") String singPassEServiceId, @RequestParam(name = "txnNo", required = false) String txnNo,String myInfoPath) {
        Map<String, Object> params = IaisCommonUtils.genNewHashMap();
        params.put("uinfin", idNumber);
        params.put("attributes",attrs);
        params.put("clientId",clientId);
        params.put("singpassEserviceId",singPassEServiceId);
        if( !StringUtil.isEmpty(txnNo)){
            params.put("txnNo",txnNo);
        }
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        HttpHeaders httpHeaders = IaisEGPHelper.getHttpHeadersForEic( new MediaType("application","jose"), signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        httpHeaders.add("authorization-Myinfo",authorizationMyInfo);
        StringBuilder stringBuffer = new  StringBuilder();
        stringBuffer.append(gateWayUrl).append("/v1/myinfo-server/").append(myInfoPath).append("/v2/person-basic/{uinfin}/");
        return  IaisCommonUtils.callEicGatewayWithHeaderParam(stringBuffer.toString(),HttpMethod.GET,params,httpHeaders,String.class);
    }


    public FeignResponseEntity<LaboratoryDevelopTestDto> syncLaboratoryDevelopTestFormFe(LaboratoryDevelopTestDto laboratoryDevelopTestDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "v1/ldt-sync", HttpMethod.POST, laboratoryDevelopTestDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), LaboratoryDevelopTestDto.class);
    }

    public FeignResponseEntity<List> getHcsaSvcRoutingStageDtoByStageId(String stageId) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        Map<String, Object> params = IaisCommonUtils.genNewHashMap();
        params.put("stageId", stageId);
        return  IaisEGPHelper.callEicGatewayWithParamForList(gateWayUrl + "/v1/routing-stage-stageId", HttpMethod.GET, params,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), HcsaSvcRoutingStageDto.class);
    }

    public FeignResponseEntity<String> refreshSubLicenseeInfo(LicenseeDto licenseeDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "v1/sync-sub-licensee", HttpMethod.PUT, licenseeDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), String.class);
    }

}
