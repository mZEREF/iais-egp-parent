package com.ecquaria.cloud.moh.iais.utils;

import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.sz.commons.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<PageShowFileDto> transForFileMapToPageShowFileDto(Map<String,File> map){
        if(map==null){
            return new ArrayList<>();
        }
        List<PageShowFileDto> pageShowFileDtos=new ArrayList<>();
        map.forEach((k,v)->{
            if(v!=null){
                long length = v.length();
                if(length>0){
                    Long size=length/1024;
                    PageShowFileDto pageShowFileDto =new PageShowFileDto();
                    pageShowFileDto.setFileName(v.getName());
                    String e = k.substring(k.lastIndexOf("e") + 1);
                    pageShowFileDto.setIndex(e);
                    SingeFileUtil singeFileUtil=SingeFileUtil.getInstance();
                    String fileMd5 = singeFileUtil.getFileMd5(v);
                    pageShowFileDto.setFileMapId("selectedFileDiv"+e);
                    pageShowFileDto.setSize(Integer.valueOf(size.toString()));
                    pageShowFileDto.setMd5Code(fileMd5);
                    pageShowFileDtos.add(pageShowFileDto);
                }
            }
        });
        return pageShowFileDtos;
    }
}
