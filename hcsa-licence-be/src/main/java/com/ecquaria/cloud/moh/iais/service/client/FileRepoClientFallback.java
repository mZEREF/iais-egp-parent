package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * FileRepoClientFallback
 *
 * @author Jinhua
 * @date 2019/11/26 12:29
 */
public class FileRepoClientFallback implements FileRepoClient{
    @Override
    public FeignResponseEntity<String> saveFiles(MultipartFile file, String auditTrailDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<String> removeFileById(String fileId) {
        return null;
    }

    @Override
    public FeignResponseEntity<byte[]> getFileFormDataBase(String guid) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<FileRepoDto>> getFilesByIds(List<String> ids) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
