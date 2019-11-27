package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import com.ecquaria.sz.commons.util.FileUtil;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Wenkang
 * @date 2019/11/6 20:56
 */
@Service
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {
    private static final String URL_SAVE_FILE_NAME="application:8883/file-name";
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
            log.error(e.getMessage(),e);
            return false;
        }
        finally {
           if(fileInputStream!=null){
               try {
                   fileInputStream.close();
               } catch (IOException e) {
                   log.error(e.getMessage(),e);

               }
           }
           if(fileOutputStream!=null){
               try {
                   fileOutputStream.close();
               } catch (IOException e) {
                   log.error(e.getMessage(),e);
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
        rename();
        deleteFile();
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

        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        finally {
            if(zos!=null){
                try {
                    zos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
            if(cos!=null){
                try {
                    cos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }

    }
    private void zipFile(ZipOutputStream zos,File file)  {
        BufferedInputStream bis=null;
        InputStream is=null;
        try {
            if(file.isDirectory()){
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(FILE_NAME))+File.separator));
                for(File f: Objects.requireNonNull(file.listFiles())){
                    zipFile(zos,f);
                }
            }
            else {
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(FILE_NAME))));
               is=new FileInputStream(file.getPath());
                 bis =new BufferedInputStream(is);
                int count ;
                byte [] b =new byte[1024];
                count=bis.read(b);
                while(count!=-1){
                    zos.write(b,0,count);
                    count=bis.read(b);
                }
            }
        }catch (IOException e){
            log.error(e.getMessage(),e);
        }
        finally {

            if(zos!=null){
                try {
                    zos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    private void rename()  {
        File zipFile =new File(BACKUPS);
       if(zipFile.isDirectory()){
           File[] files = zipFile.listFiles((dir, name) -> {
               if (name.endsWith(".zip")) {
                   return true;
               }
               return false;
           });
           for(File file:files){
               try {
                   FileInputStream is=new FileInputStream(file);
                   ByteArrayOutputStream by=new ByteArrayOutputStream();
                   int count=0;
                   byte [] size=new byte[1024];
                   count=is.read(size);
                   while(count!=-1){
                       by.write(size,0,count);
                       count= is.read(size);
                   }
                    by.close();
                    is.close();
                   byte[] bytes = by.toByteArray();
                   String s = FileUtil.genMd5FileChecksum(bytes);
                   file.renameTo(new File(BACKUPS+File.separator+s+".zip"));
                 /*  saveFileName(file.getName());*/
               } catch (IOException e) {
                   log.error(e.getMessage(),e);
               }
           }
       }
    }

    private void deleteFile(){
        File file =new File(DOWNLOAD);
        if(file.isDirectory()){
            File[] files = file.listFiles((dir, name) -> {
                if (name.endsWith(FILE_FORMAT)) {
                    return true;
                }
                return false;
            });
            for(File f:files){
                if(f.exists()){
                    f.delete();
                }
            }
        }
    }


    private void  saveFileName(String fileName){
        RestApiUtil.save(URL_SAVE_FILE_NAME,fileName);
    }

}
