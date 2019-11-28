package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileRepoClientFallback
 *
 * @author Jinhua
 * @date 2019/11/26 12:29
 */
public class FileRepoClientFallback {
    public FeignResponseEntity<String> saveFiles(MultipartFile file, String auditTrailDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
