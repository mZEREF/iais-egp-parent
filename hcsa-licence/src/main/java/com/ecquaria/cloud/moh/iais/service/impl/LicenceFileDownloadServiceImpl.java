package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * @author Wenkang
 * @date 2019/11/9 16:09
 */
@Service
@Slf4j
public class LicenceFileDownloadServiceImpl implements LicenceFileDownloadService {

    private static  final  String URL="iais-application:8883/files";
    private static  final String DOWNLOAD="D:/folder";
    private static  final String BACKUPS="D:/backups/";
    private static  final  String FILE_FORMAT=".text";
    private static  final String COMPRESS_PATH="D:/compress";

    @Override
    public void compress(){
        if(new File(BACKUPS).isDirectory()){
            File[] files = new File(BACKUPS).listFiles();
            for(File fil:files){
                if(fil.getName().endsWith(".zip")){
                    ZipFile zipFile=null;
                    CheckedInputStream cos=null;
                    BufferedInputStream bis=null;
                    BufferedOutputStream bos=null;
                    OutputStream os=null;
                    try {
                        zipFile =new ZipFile(fil.getPath());

                        for( Enumeration<? extends ZipEntry> entries = zipFile.entries();entries.hasMoreElements();){
                            ZipEntry zipEntry = entries.nextElement();

                            file(zipEntry,os,bos,zipFile,bis,cos);

                        }
                        if(fil.exists()){
                            fil.delete();
                        }

                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                    finally {
                        if(cos!=null){
                            try {
                                cos.close();
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        }

                        if(bis!=null){
                            try {
                                bis.close();
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        }
                        if(bos!=null){
                            try {
                                bos.close();
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        }
                        if(os!=null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                log.error(e.getMessage());
                            }
                        }
                        if(zipFile!=null){
                             try {
                              zipFile.close();
                            } catch (IOException e) {
                                 log.error(e.getMessage());
                            }
                      }
                    }
                }
            }

        }

    }

    @Override
    public String  download() {
        FileInputStream fileInputStream=null;
        InputStreamReader inputStreamReader=null;
        BufferedReader bufferedReader=null;
        try {
            File file =new File(DOWNLOAD);
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(File  filzz:files){
                    if(filzz.isFile() &&filzz.getName().endsWith(FILE_FORMAT)){

                       fileInputStream =new FileInputStream(filzz);
                      inputStreamReader =new InputStreamReader(fileInputStream);
                      bufferedReader=new BufferedReader(inputStreamReader);

                        String count=null;
                        String reader = reader(bufferedReader, count);
                        bufferedReader.close();

                        Boolean   aBoolean = RestApiUtil.save(URL, reader, Boolean.class);

                        Boolean backups = backups(aBoolean, filzz);
                        if(backups&&filzz.exists()){
                            filzz.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if(inputStreamReader!=null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }

            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }

        }

        return  "";
    }

    /*************************/
        private void file( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos) throws IOException {
            if(!zipEntry.getName().endsWith(File.separator)){
                File file =new File(COMPRESS_PATH+File.separator+zipEntry.getName().substring(0,zipEntry.getName().lastIndexOf(File.separator)));
                if(!file.exists()){
                    file.mkdirs();
                }
                os=new FileOutputStream(COMPRESS_PATH+File.separator+zipEntry.getName());
                bos=new BufferedOutputStream(os);
                InputStream is=zipFile.getInputStream(zipEntry);
                bis=new BufferedInputStream(is);
                cos=new CheckedInputStream(bis,new CRC32());
                byte []b=new byte[1024];
                int count =0;
                count=cos.read(b);
                while(count!=-1){
                    bos.write(b,0,count);
                    count=cos.read(b);
                }

            }else {

                new File(COMPRESS_PATH+File.separator+zipEntry.getName()).mkdirs();
            }
        }




    private String reader(BufferedReader bufferedReader,String count){
        StringBuilder stringBuilder=new StringBuilder();

        while(count!=null){
            try {
                count= bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(count==null){
                break;
            }
            stringBuilder.append(count.trim());
        }

        return stringBuilder.toString();
    }


    private Boolean  backups(Boolean aBoolean ,File file){
        if(aBoolean){
            if(!new File(BACKUPS).exists()){
                new File(BACKUPS).mkdirs();
            }
            File newFile=new File(BACKUPS+File.separator+file.getName());
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                 fileInputStream=new FileInputStream(file);
               fileOutputStream=new FileOutputStream(newFile);
                int cout =0;
                byte [] size=new byte[1024];
               cout= fileInputStream.read(size);
               while(cout!=-1){
                   fileOutputStream.write(size,0,cout);
                   cout= fileInputStream.read(size);
               }


                return true;
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            finally {
                if (fileInputStream!=null){
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
        }
        return false;
    }
}
