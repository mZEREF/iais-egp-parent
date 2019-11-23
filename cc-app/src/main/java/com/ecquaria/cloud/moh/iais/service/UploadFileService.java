package com.ecquaria.cloud.moh.iais.service;


import java.util.List;

/**
 * @author Wenkang
 * @date 2019/11/6 20:55
 */
public interface UploadFileService {
    Boolean saveFile(String  str);
    String getData();
    String  changeStatus();
    void compressFile();

}
