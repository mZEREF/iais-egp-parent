package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;

import java.util.List;
import java.util.Map;

/**
 * UploadFileEditService
 *
 * @author junyu
 * @date 2022/5/11
 */
public interface UploadFileEditService {
    String saveFile(ApplicationListFileDto applicationListFileDto ) ;
    String getData();
    String  changeStatus(ApplicationListFileDto applicationListDto, Map<String, List<String>> map);
    String compressFile(String grpId);

    List<ApplicationListFileDto> parse(String data);

    void  getRelatedDocuments(ApplicationListFileDto applicationListFileDto) throws Exception;

    boolean renameAndSave(String file,String groupId);
}
