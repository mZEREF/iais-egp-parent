package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/11/6 20:55
 */
public interface UploadFileService {
    String saveFile(ApplicationListFileDto applicationListFileDto ) ;
    String getData();
    String  changeStatus(ApplicationListFileDto applicationListDto, Map<String,List<String>> map);
    String compressFile(String grpId);

    List<ApplicationListFileDto> parse(String data);

    void  getRelatedDocuments(ApplicationListFileDto applicationListFileDto);

    boolean renameAndSave(String file,String groupId);
}
