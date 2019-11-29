package com.ecquaria.cloud.moh.iais.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * RestBridgeHelper
 *
 * @author Jinhua
 * @date 2019/11/28 15:48
 */
@Component
public class RestBridgeHelper {

    @Value("${iais.syncFileTracking.url}")
    private String syncFileTrackUrl;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    public static <T> T  callOtherSideApi(String url, String keyId, String secretKey,
                                                  Object entity, Class<T> retrunClass, HttpMethod requestType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("", "");
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        headers.add("date", signature.date());
        headers.add("authorization", signature.authorization());
        HttpEntity jsonPart = new HttpEntity<>(entity, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> resp = restTemplate.exchange(url, requestType, jsonPart, retrunClass);

        return resp.getBody();
    }
}
