package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * FileRepoClient
 *
 * @author Jinhua
 * @date 2019/11/26 12:29
 */
@FeignClient(name = "FILE-REPOSITORY", configuration = {FeignConfiguration.class},
        fallback = FileRepoClientFallback.class)
public interface FileRepoClient {
    @RequestMapping(method = RequestMethod.POST, produces =  MediaType.APPLICATION_JSON_VALUE ,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FeignResponseEntity<String> saveFiles(@RequestPart("selectedFile") MultipartFile file,
                                          @RequestParam("filerepoInfo") String fileRepoStr);
    @GetMapping(value = "/{guid}")
    FeignResponseEntity<byte[]> getFileFormDataBase(@PathVariable(name = "guid") String guid);

    @PostMapping(value = "/files", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<FileRepoDto>> getFilesByIds(@RequestBody List<String> ids);

    @DeleteMapping(value = "/no-file", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> removeFileById(@RequestBody FileRepoDto fileRepoDto);
}
