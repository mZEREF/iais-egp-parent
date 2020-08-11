package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author yichen
 * @Date:2020/8/11
 */
@Component
public class EicFeCommonClient {
    @Value("${iais.inter.gateway.url}")
    private String gateWayUrl;

    public FeignResponseEntity<List> getCurrentTaskAssignedInspectorInfo(Map<String, Object> params,
                                                                         String date, String authorization, String dateSec, String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/new-inbox-msg-no", HttpMethod.POST, params,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, OrgUserDto.class);
    }
}
