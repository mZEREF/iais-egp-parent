package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Wenkang
 * @date 2019/12/26 20:35
 */
@FeignClient(name = "file-repository", configuration = FeignConfiguration.class,
        fallback = FileRepositoryClientFallback.class)
public interface FileRepositoryClient  {
    @GetMapping(value = "/{guid}")
    FeignResponseEntity<byte[]> getFileFormDataBase(@PathVariable(name = "guid") String guid);
}
