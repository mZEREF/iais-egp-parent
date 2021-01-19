package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * BeMainFileRepoClient
 *
 * @author Jinhua
 * @date 2020/9/23 15:14
 */
@FeignClient(name = "file-repository", configuration = FeignConfiguration.class,
        fallback = BeMainFileRepoClientFallback.class)
public interface BeMainFileRepoClient {
    @PostMapping(value = "/fetch-file-content")
    FeignResponseEntity<Void> fetchFileContent();
}
