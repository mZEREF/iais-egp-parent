package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * FileRepoClientFallback
 *
 * @author Jinhua
 * @date 2019/11/26 12:29
 */
public class FileRepoClientFallback implements FileRepoClient {

    @Override
    public FeignResponseEntity<String> saveFiles(MultipartFile file, String fileRepoStr) {
        return IaisEGPHelper.getFeignResponseEntity("saveFiles",file,fileRepoStr);
    }

    @Override
    public FeignResponseEntity<byte[]> getFileFormDataBase(String guid) {
        return IaisEGPHelper.getFeignResponseEntity("getFileFormDataBase",guid);
    }

    @Override
    public FeignResponseEntity<List<FileRepoDto>> getFilesByIds(List<String> ids) {
        return IaisEGPHelper.getFeignResponseEntity("getFilesByIds",ids);
    }

    @Override
    public FeignResponseEntity<String> removeFileById(FileRepoDto fileRepoDto) {
        return IaisEGPHelper.getFeignResponseEntity("removeFileById",fileRepoDto);
    }
}
