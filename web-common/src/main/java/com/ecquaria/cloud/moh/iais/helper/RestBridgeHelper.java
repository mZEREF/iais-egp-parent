package com.ecquaria.cloud.moh.iais.helper;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Component
public class RestBridgeHelper {


    public RestBridgeHelper() {
        disableSslVerification();
    }

    public <T> T  callOtherSideApi(String url, String keyId, String secretKey,
                                                  Object entity, Class<T> retrunClass, HttpMethod requestType) {
        disableSslVerification();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        headers.add("date", signature.date());
        headers.add("authorization", signature.authorization());
        HttpEntity jsonPart = new HttpEntity<>(entity, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> resp = restTemplate.exchange(url, requestType, jsonPart, retrunClass);

        return resp.getBody();
    }

    private void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } catch (KeyManagementException e) {
            log.error(e.getMessage(), e);
        }
    }
}
