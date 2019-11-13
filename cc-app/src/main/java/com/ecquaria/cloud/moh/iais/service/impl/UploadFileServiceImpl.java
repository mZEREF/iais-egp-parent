package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Date;
import java.util.Objects;
import java.util.zip.*;

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
    private static  final  String FILE_NAME="folder";
    private static  final  String FILE_FORMAT=".text";
    private static  final String BACKUPS="D:/backups";
    private static  final String OUT_PATH="D:/backups/";

    @Override
    public Boolean saveFile(String  str) {
        FileOutputStream fileOutputStream;
        Date date =new Date();
        File file=new File(DOWNLOAD+"/"+date.getTime()+FILE_FORMAT);
        FileOutputStream fileInputStream;
        try {
            file.createNewFile();
            fileInputStream =new FileOutputStream(BACKUPS+"/"+file.getName());
            fileOutputStream =new FileOutputStream(file);
            fileOutputStream.write(str.getBytes());
            fileInputStream.write(str.getBytes());
            fileOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String getData() {

        return  RestApiUtil.getByPathParam(URL_APP,"",String.class);
    }

    @Override
    public String  changeStatus() {
        RestApiUtil.update(URL_STATUS,  Boolean.class);

        return "";
    }
    @Override
    public void compressFile(){
        compress();
    }
/*****************compress*********/

    private void compress(){
        try {
            OutputStream is=new FileOutputStream(OUT_PATH+new Date().getTime()+".zip");
            CheckedOutputStream cos =new CheckedOutputStream(is,new CRC32());
            ZipOutputStream zos =new ZipOutputStream(cos);
            File file =new File(DOWNLOAD);
            zipFile(zos,file);
            zos.close();
            cos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void zipFile(ZipOutputStream zos,File file) throws IOException {

        if(file.isDirectory()){
            zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(FILE_NAME))+File.separator));
            for(File f: Objects.requireNonNull(file.listFiles())){
                zipFile(zos,f);
            }
        }
        else {
            zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(FILE_NAME))));
            InputStream is=new FileInputStream(file.getPath());
            BufferedInputStream bis =new BufferedInputStream(is);
            int count ;
            byte [] b =new byte[1024];
            count=bis.read(b);
            while(count!=-1){
                zos.write(b,0,count);
                count=bis.read(b);
            }
            bis.close();
            is.close();
        }
    }
}
