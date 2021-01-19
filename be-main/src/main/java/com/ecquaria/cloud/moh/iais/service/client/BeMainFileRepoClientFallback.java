package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * BeMainFileRepoClientFallback
 *
 * @author Jinhua
 * @date 2020/9/23 15:34
 */
public class BeMainFileRepoClientFallback {
    public FeignResponseEntity<Void> fetchFileContent() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
