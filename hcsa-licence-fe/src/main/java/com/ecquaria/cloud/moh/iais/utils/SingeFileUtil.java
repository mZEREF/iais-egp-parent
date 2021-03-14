package com.ecquaria.cloud.moh.iais.utils;

import com.ecquaria.sz.commons.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author Wenkang
 * @date 2021/3/11 13:09
 */
public class SingeFileUtil {
    private SingeFileUtil(){}
    private final static SingeFileUtil fileUtils=new SingeFileUtil();
    public static SingeFileUtil getInstance(){
        return fileUtils;
    }
    public  String getFileMd5(File file){
        if (file==null){
            throw new NullPointerException();
        }
        String s="";
        try (InputStream is = Files.newInputStream(file.toPath());
             ByteArrayOutputStream by=new ByteArrayOutputStream()) {
                int count;
                byte[] size=new byte[1024];
                count=is.read(size);
                while (count!=-1){
                    by.write(size,0,count);
                    count=is.read(size);
                }
                 s = FileUtil.genMd5FileChecksum(by.toByteArray());
                return s;

        }catch (Exception e){

        }
        return s;
    }

    public  String getFileMd5(MultipartFile multipartFile) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        String s = FileUtil.genMd5FileChecksum(bytes);
        return s;
    }
}
