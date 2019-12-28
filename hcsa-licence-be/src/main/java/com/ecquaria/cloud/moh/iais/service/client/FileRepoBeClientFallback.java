package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
<<<<<<< Updated upstream
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
    public FeignResponseEntity<byte[]> getFileFormDataBase(String guid) {
=======
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Wenkang
 * @date 2019/12/28 14:21
 */
public class FileRepoClientFallback {
    FeignResponseEntity<String> saveFileToDataBase(
           MultipartFile files,
            @ApiParam(name = "audittrail", required = true)
            String auditTrailStr,
             String respId){
>>>>>>> Stashed changes
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    }
}
