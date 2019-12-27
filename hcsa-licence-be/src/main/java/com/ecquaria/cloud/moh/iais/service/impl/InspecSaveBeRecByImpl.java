package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.service.InspecSaveBeRecByService;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Shicheng
 * @date 2019/12/27 14:15
 **/
@Service
@Slf4j
public class InspecSaveBeRecByImpl implements InspecSaveBeRecByService {
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    private String download;
    private String zipFile;
    private String fileFormat = ".text";
    private String compressPath;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Override
    public List<ProcessFileTrackDto> getFileTypeAndStatus(String applicationStatusFeToBeRectification, String commonStatusActive) {
        List<ProcessFileTrackDto> processFileTrackDtos = systemBeLicClient.getFileTypeAndStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION,
                AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        return processFileTrackDtos;
    }

    @Override
    public void deleteUnZipFile() {
        download = sharedPath + "recUnZipFile" + File.separator + "userRecFile";
        zipFile = sharedPath + "backupsRec";
        compressPath = sharedPath + "recUnZipFile";
        File file = new File(download);
        File b = new File(zipFile);
        File c = new File(compressPath);
        if(!c.exists()){
            c.mkdirs();
        }
        if(!b.exists()){
            b.mkdirs();
        }

        if(!file.mkdirs()){
            file.mkdirs();
        }
        deleteFile(file);
    }

    @Override
    public void compressFile(List<ProcessFileTrackDto> processFileTrackDtos) {
        if(new File(zipFile).isDirectory()){
            File[] files = new File(zipFile).listFiles();
            for(File fil:files) {
                for(ProcessFileTrackDto pDto : processFileTrackDtos){
                    if (fil.getName().endsWith(".zip") && fil.getName().equals(pDto.getFileName())) {
                        ZipFile zipFile = null;
                        CheckedInputStream cos = null;
                        BufferedInputStream bis = null;
                        BufferedOutputStream bos = null;
                        OutputStream os = null;
                        try {
                            zipFile = new ZipFile(pDto.getFilePath());
                            for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); ) {
                                ZipEntry zipEntry = entries.nextElement();
                                zipFile(zipEntry, os, bos, zipFile, bis, cos);
                            }
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        } finally {
                            if (cos != null) {
                                try {
                                    cos.close();
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                }
                            }

                            if (bis != null) {
                                try {
                                    bis.close();
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                }
                            }
                            if (bos != null) {
                                try {
                                    bos.close();
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                }
                            }
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                }
                            }
                            if (zipFile != null) {
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
    }

    private void zipFile( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos)  {
        try {
            if(!zipEntry.getName().endsWith(File.separator)){
                File file =new File(compressPath + File.separator+zipEntry.getName().substring(0,zipEntry.getName().lastIndexOf(File.separator)));
                if(!file.exists()){
                    file.mkdirs();
                }
                os = new FileOutputStream(compressPath + File.separator + zipEntry.getName());
                bos = new BufferedOutputStream(os);
                InputStream is = zipFile.getInputStream(zipEntry);
                bis = new BufferedInputStream(is);
                cos = new CheckedInputStream(bis,new CRC32());
                byte[] b = new byte[1024];
                int count = cos.read(b);
                while(count != -1){
                    bos.write(b,0, count);
                    count = cos.read(b);
                }
            }else {
                new File(compressPath + File.separator + zipEntry.getName()).mkdirs();
            }
        }catch (IOException e){
            log.error(e.getMessage(), e);
        }finally {
            if(cos != null){
                try {
                    cos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
            if(bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if(bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }

        }

    }

    @Override
    public Boolean saveData(AuditTrailDto intranet, List<ProcessFileTrackDto> processFileTrackDtos) {
        FileInputStream fileInputStream=null;
        Boolean flag=false;
        try {
            File file =new File(download);
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(File file2:files){
                    if(file2.isFile() && file2.getName().endsWith(fileFormat)){
                        fileInputStream = new FileInputStream(file2);
                        ByteArrayOutputStream by = new ByteArrayOutputStream();
                        byte [] size = new byte[1024];
                        int count = fileInputStream.read(size);
                        while(count != -1){
                            by.write(size,0,count);
                            count = fileInputStream.read(size);
                        }
                        Boolean aBoolean = fileToDto(by.toString(), intranet, processFileTrackDtos);
                        flag = aBoolean;
                        if(!aBoolean){
                            file.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return flag;
    }

    private Boolean fileToDto(String toString, AuditTrailDto intranet, List<ProcessFileTrackDto> processFileTrackDtos) {
        return true;
    }

    private void deleteFile(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f:files){
                deleteFile(f);
            }
        }else{
            if(file.exists()&&file.getName().endsWith(fileFormat)){
                file.delete();
            }
        }
    }
}
