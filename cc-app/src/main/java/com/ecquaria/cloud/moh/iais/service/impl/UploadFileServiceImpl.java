package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import com.ecquaria.cloud.sftp.SftpApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Date;

/**
 * @author Wenkang
 * @date 2019/11/6 20:56
 */
@Service
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {

    private static final String URL_APP="iais-application:8883/all-file";
    private static final  String URL_STATUS="iais-application:8883/status";
    private static  final String DOWNLOAD="D:/folder";
    private static  final  String FILE_FORMAT=".text";
    @Autowired
    private SftpApi sftpApi;

    private String fileName;
    @Override
    public Boolean uploadFile(String  str) {
        FileOutputStream fileOutputStream;

        Date date =new Date();
        File file=new File(DOWNLOAD+"/"+date.getTime()+FILE_FORMAT);
        try {
            file.createNewFile();
            fileOutputStream =new FileOutputStream(file);
            fileOutputStream.write(str.getBytes());
            fileOutputStream.close();
            sftpApi.uploadFile(file);
            fileName= file.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getData() {
        String    str = RestApiUtil.getByPathParam(URL_APP,"",String.class);
        return str;
    }

    @Override
    public String  changeStatus() {
        Boolean byPathParam = RestApiUtil.update(URL_STATUS,  Boolean.class);
        if(byPathParam){
           return  fileName;
        }

        return "";
    }


}
