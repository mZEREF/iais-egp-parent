package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * FeMainFileRepoClientFallback
 *
 * @author Jinhua
 * @date 2020/9/23 15:40
 */
public class FeMainFileRepoClientFallback {
    public FeignResponseEntity<Void> fetchFileContent() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
