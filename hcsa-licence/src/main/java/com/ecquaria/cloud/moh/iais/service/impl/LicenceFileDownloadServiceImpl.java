package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import org.springframework.stereotype.Service;

import java.io.*;


/**
 * @author Wenkang
 * @date 2019/11/9 16:09
 */
@Service
public class LicenceFileDownloadServiceImpl implements LicenceFileDownloadService {

    private static  final  String URL="iais-application:8883/files";
    private static  final String DOWNLOAD="D:/folder";
    private static  final String BACKUPS="D:/backups";
    private static  final  String FILE_FORMAT=".text";
    @Override
    public String  download() {
        try {
            File file =new File(DOWNLOAD);
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(File  filzz:files){
                    if(filzz.isFile() &&filzz.getName().endsWith(FILE_FORMAT)){
                        StringBuilder stringBuilder=new StringBuilder();
                        FileInputStream fileInputStream =new FileInputStream(filzz);
                        InputStreamReader inputStreamReader =new InputStreamReader(fileInputStream);
                        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                        String count="";
                        while(count!=null){
                            count= bufferedReader.readLine();
                            if(count==null){
                                break;
                            }
                            stringBuilder.append(count.trim());
                        }
                        bufferedReader.close();
                        String st=stringBuilder.toString();
                        Boolean   aBoolean = RestApiUtil.save(URL, st, Boolean.class);
                        System.out.println(aBoolean);
                        Boolean backups = backups(aBoolean, filzz);
                        if(backups){
                            filzz.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  "";
    }

    /*************************/

    private Boolean  backups(Boolean aBoolean ,File file){
        if(aBoolean){
            if(!new File(BACKUPS).exists()){
                new File(BACKUPS).mkdirs();
            }
            File newFile=new File(BACKUPS+"/"+file.getName());
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
                fileInputStream.close();
                fileOutputStream.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
