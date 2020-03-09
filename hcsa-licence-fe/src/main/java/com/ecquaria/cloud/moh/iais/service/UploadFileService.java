package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/11/6 20:55
 */
public interface UploadFileService {
    boolean saveFile(String  str);
    String getData();
    String  changeStatus(ApplicationListFileDto applicationListDto);
    String compressFile();

    List<ApplicationListFileDto> parse(String data);

    void  getRelatedDocuments(String entity);
    void initFilePath();
    boolean renameAndSave(String file);
}
