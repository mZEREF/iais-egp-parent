package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.zip.*;

/**
 * @author Wenkang
 * @date 2019/11/6 20:56
 */
@Service
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {

    private static final String URL_APP="iais-application:8883/iais-application/all-file";
    private static final  String URL_STATUS="iais-application:8883/iais-application/status";
    private static  final String DOWNLOAD="D:/folder";
    private static  final  String FILE_NAME="folder";
    private static  final  String FILE_FORMAT=".text";
    private static  final String BACKUPS="D:/backups";


    @Override
    public Boolean saveFile(String  str) {
        FileOutputStream fileOutputStream = null;
        String s = FileUtil.genMd5FileChecksum(str.getBytes());
        File file=new File(DOWNLOAD+ File.separator+s+FILE_FORMAT);
        FileOutputStream fileInputStream = null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fileInputStream =new FileOutputStream(BACKUPS+File.separator+file.getName());
            fileOutputStream =new FileOutputStream(file);
            fileOutputStream.write(str.getBytes());
            fileInputStream.write(str.getBytes());

        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        finally {
           if(fileInputStream!=null){
               try {
                   fileInputStream.close();
               } catch (IOException e) {
                   log.error(e.getMessage());

               }
           }
           if(fileOutputStream!=null){
               try {
                   fileOutputStream.close();
               } catch (IOException e) {
                   log.error(e.getMessage());
               }
           }
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
        ZipOutputStream zos=null;
        CheckedOutputStream cos=null;
        OutputStream is=null;
        try {
             is=new FileOutputStream(BACKUPS+File.separator+System.currentTimeMillis()+".zip");
            cos =new CheckedOutputStream(is,new CRC32());
             zos =new ZipOutputStream(cos);
            File file =new File(DOWNLOAD);
            zipFile(zos,file);

            is.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        finally {
            if(zos!=null){
                try {
                    zos.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if(cos!=null){
                try {
                    cos.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

    }
    private void zipFile(ZipOutputStream zos,File file)  {
        BufferedInputStream bis=null;
        try {
            if(file.isDirectory()){
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(FILE_NAME))+File.separator));
                for(File f: Objects.requireNonNull(file.listFiles())){
                    zipFile(zos,f);
                }
            }
            else {
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(FILE_NAME))));
                InputStream is=new FileInputStream(file.getPath());
                 bis =new BufferedInputStream(is);
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
        }catch (IOException e){
            log.error(e.getMessage());
        }
        finally {
            if(zos!=null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }

        }

    }

    private void rename(){}
    File file =new File(BACKUPS);


}
