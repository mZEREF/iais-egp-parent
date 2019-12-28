package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.config.FeignMultipartConfig;
import com.ecquaria.cloudfeign.FeignResponseEntity;
<<<<<<< Updated upstream
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
=======
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
>>>>>>> Stashed changes
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
<<<<<<< Updated upstream
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
                                          @RequestParam("audittrail") String auditTrailStr);
    @GetMapping(value = "/{guid}")
    FeignResponseEntity<byte[]> getFileFormDataBase(@PathVariable(name = "guid") String guid);
=======
 * @author Wenkang
 * @date 2019/12/28 14:21
 */
@FeignClient(value = "file-repository", configuration = {FeignMultipartConfig.class},
        fallback = FileRepoClientFallback.class)
public interface FileRepoClient {
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
FeignResponseEntity<String> saveFileToDataBase(
        @RequestPart("selectedFile") MultipartFile files,
        @ApiParam(name = "audittrail", required = true)
        @RequestParam("audittrail") String auditTrailStr,
        @RequestParam(value="respId", required = false) String respId);
>>>>>>> Stashed changes
}
