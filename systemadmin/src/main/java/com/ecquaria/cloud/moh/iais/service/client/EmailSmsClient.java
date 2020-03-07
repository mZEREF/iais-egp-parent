package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * EmailSmsClient
 *
 * @author Jinhua
 * @date 2020/3/7 16:45
 */
@Component
public class EmailSmsClient {
    @Autowired
    private RestTemplate restTemplate;

    public void sendEmail(EmailDto emailDto, Map<String, byte[]> attachments) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();

        // creating an HttpEntity for the JSON part
        HttpHeaders jsonHeader = new HttpHeaders();
        jsonHeader.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmailDto> jsonPart = new HttpEntity<>(emailDto, jsonHeader);
        multipartRequest.add("email", jsonPart);
        // creating an HttpEntity for the binary part
        if (attachments != null && !attachments.isEmpty()) {
            for (Map.Entry<String, byte[]> ent : attachments.entrySet()) {
                HttpHeaders fileHeader = new HttpHeaders();
                ByteArrayResource fileContentAsResource = new ByteArrayResource(ent.getValue()) {
                    @Override
                    public String getFilename() {
                        return ent.getKey();
                    }
                };
                HttpEntity<ByteArrayResource> fileEnt = new HttpEntity<>(fileContentAsResource, fileHeader);
                multipartRequest.add("attachments", fileEnt);
            }
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, headers);
        restTemplate.postForObject("http://email-sms/emails", requestEntity, String.class);
    }
}
