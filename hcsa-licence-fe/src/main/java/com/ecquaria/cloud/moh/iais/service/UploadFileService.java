package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/11/6 20:55
 */
public interface UploadFileService {
    Boolean saveFile(String  str);
    String getData();
    String  changeStatus(ApplicationListFileDto applicationListDto);
    boolean compressFile();

    List<ApplicationListFileDto> parse(String data);

}
