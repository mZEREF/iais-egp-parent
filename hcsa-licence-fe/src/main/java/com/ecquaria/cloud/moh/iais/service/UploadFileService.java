package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/11/6 20:55
 */
public interface UploadFileService {
    String saveFile(ApplicationListFileDto applicationListFileDto );
    String getData();
    String  changeStatus(ApplicationListFileDto applicationListDto);
    String compressFile(String grpId);

    List<ApplicationListFileDto> parse(String data);

    void  getRelatedDocuments(ApplicationListFileDto applicationListFileDto);
    void initFilePath();
    boolean renameAndSave(String file,String groupId);
}
