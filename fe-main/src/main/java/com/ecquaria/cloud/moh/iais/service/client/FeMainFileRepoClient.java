package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * FeMainFileRepoClient
 *
 * @author Jinhua
 * @date 2020/9/23 15:40
 */
@FeignClient(name = "file-repository", configuration = FeignConfiguration.class,
        fallback = FeMainFileRepoClientFallback.class)
public interface FeMainFileRepoClient {
    @PostMapping(value = "/fetch-file-content")
    FeignResponseEntity<Void> fetchFileContent();
}
