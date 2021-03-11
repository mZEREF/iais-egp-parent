package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * ComFileRepoClient
 *
 * @author Jinhua
 * @date 2021/3/11 10:27
 */
@Component
@Slf4j
public class ComFileRepoClient {
    @Autowired
    @Qualifier(value = "iaisRestTemplate")
    private RestTemplate restTemplate;

    public List<String> saveFileRepo(List<File> files) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
        if (IaisCommonUtils.isEmpty(files)) {
            for (File file : files) {
                HttpHeaders fileHeader = new HttpHeaders();
                try (FileInputStream fis = new FileInputStream(file)) {
                    InputStreamResource fileContentAsResource = new InputStreamResource(fis) {
                        @Override
                        public String getFilename() {
                            return file.getName();
                        }
                    };
                    HttpEntity<InputStreamResource> fileEnt = new HttpEntity<>(fileContentAsResource, fileHeader);
                    multipartRequest.add("selectedFiles", fileEnt);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    throw new IaisRuntimeException(e);
                }
            }
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, headers);

            return restTemplate.postForObject("http://file-repository/saveFiles", requestEntity, List.class);
        }

        return null;
    }
}
