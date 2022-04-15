package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LaboratoryDevelopTestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private EicRequestTrackingHelper requestTrackingHelper;

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    public <T, R> R callEicWithTrackForApp(T obj, Function<T, R> function, String actionMethod) {
        return requestTrackingHelper.callEicWithTrack(obj, function, this.getClass().getName(), actionMethod, currentApp, currentDomain,
                EicClientConstant.APPLICATION_CLIENT);
    }

    public <T, R> R callEicWithTrackForOrg(T obj, Function<T, R> function, String actionMethod) {
        return callEicWithTrackForOrg(obj, function, this.getClass(), actionMethod);
    }

    public <T, R> R callEicWithTrackForOrg(T obj, Function<T, R> function, Class<?> actionClass, String actionMethod) {
        return requestTrackingHelper.callEicWithTrack(obj, function, actionClass.getName(), actionMethod, currentApp, currentDomain,
                EicClientConstant.ORGANIZATION_CLIENT);
    }

    public FeignResponseEntity<Boolean> updateApplicationStatus(RecallApplicationDto recallApplicationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/hcsa-app-recall", HttpMethod.POST, recallApplicationDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                Boolean.class);
    }

    public FeignResponseEntity<RecallApplicationDto> syncAccountDataFormFe(OrganizationDto organizationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/user-account-sync", HttpMethod.POST, organizationDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), RecallApplicationDto.class);
    }

    public FeignResponseEntity<RecallApplicationDto> recallAppChangeTask(RecallApplicationDto recallApplicationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/task-recall", HttpMethod.POST, recallApplicationDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                RecallApplicationDto.class);
    }

    public FeignResponseEntity<List> recallAppTasks(List<RecallApplicationDto> recallApplicationDtoList) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/task-list-recall", HttpMethod.POST, recallApplicationDtoList,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                RecallApplicationDto.class);
    }

    public FeignResponseEntity<String> saveFile(ProcessFileTrackDto processFileTrackDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/file-sync-trackings", HttpMethod.POST, processFileTrackDto,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                String.class);
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
