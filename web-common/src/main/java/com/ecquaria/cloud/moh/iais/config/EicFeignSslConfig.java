package com.ecquaria.cloud.moh.iais.config;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * EicFeignConfig
 *
 * @author Jinhua
 * @date 2019/12/6 10:48
 */
@Configuration
@Slf4j
public class EicFeignSslConfig {

    @Bean
    public Client client() throws GeneralSecurityException, IOException {
        return noSllClient();
    }

    private Client noSllClient() throws KeyManagementException, NoSuchAlgorithmException {
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

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        HostnameVerifier verifier = (requestedHost, remoteServerSession)
                -> requestedHost.equalsIgnoreCase(remoteServerSession.getPeerHost());
        CloseableHttpClient client = HttpClientBuilder.create()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        return new ApacheHttpClient(client);
    }

    private Client sslClient() throws KeyStoreException, IOException,
            CertificateException, NoSuchAlgorithmException, KeyManagementException {
        ClassPathResource resource = new ClassPathResource("client.keystore");
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(resource.getInputStream(), "142857".toCharArray());
        TrustManagerFactory factory = TrustManagerFactory.getInstance("SunX509");
        factory.init(keystore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, factory.getTrustManagers(), null);
        HostnameVerifier verifier = (requestedHost, remoteServerSession)
                -> requestedHost.equalsIgnoreCase(remoteServerSession.getPeerHost());
        CloseableHttpClient client = HttpClientBuilder.create()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(verifier)
                .build();
        return new ApacheHttpClient(client);
    }
}
