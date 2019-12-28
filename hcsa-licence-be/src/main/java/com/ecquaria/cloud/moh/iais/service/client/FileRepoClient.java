package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.config.FeignMultipartConfig;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileRepoClient
 *
 * @author Jinhua
 * @date 2019/11/26 12:29
 */
@FeignClient(name = "FILE-REPOSITORY", configuration = {FeignMultipartConfig.class},
        fallback = FileRepoClientFallback.class)
public interface FileRepoClient {
    @RequestMapping(method = RequestMethod.POST, produces =  MediaType.APPLICATION_JSON_VALUE ,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FeignResponseEntity<String> saveFiles(@RequestPart("selectedFile") MultipartFile file,
                                          @RequestParam("audittrail") String auditTrailStr,
                                          @RequestParam(value="respId", required = false) String respId);
    @GetMapping(value = "/{guid}")
    FeignResponseEntity<byte[]> getFileFormDataBase(@PathVariable(name = "guid") String guid);
}
