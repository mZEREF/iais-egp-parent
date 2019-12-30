package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.annotation.EicService;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.service.InspecSaveBeRecByService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
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

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private FileRepoClient fileRepoClient;

    @Override
    public List<ProcessFileTrackDto> getFileTypeAndStatus(String applicationStatusFeToBeRectification, String commonStatusActive) {
        List<ProcessFileTrackDto> processFileTrackDtos = systemBeLicClient.getFileTypeAndStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION,
                ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS).getEntity();
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
                                String fileName = pDto.getFileName().substring(0,pDto.getFileName().lastIndexOf("."));
                                zipFile(zipEntry, os, bos, zipFile, bis, cos, fileName);
                            }
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }

    private void zipFile(ZipEntry zipEntry, OutputStream os, BufferedOutputStream bos, ZipFile zipFile, BufferedInputStream bis, CheckedInputStream cos, String fileName)  {
        try {
            if(!zipEntry.getName().endsWith(File.separator)){
                File file = new File(compressPath + File.separator + zipEntry.getName().substring(0,zipEntry.getName().lastIndexOf(File.separator)) + File.separator + fileName);
                if(!file.exists()){
                    file.mkdirs();
                }
                os = new FileOutputStream(compressPath + File.separator + zipEntry.getName());
                bos = new BufferedOutputStream(os);
                InputStream is = zipFile.getInputStream(zipEntry);
                bis = new BufferedInputStream(is);
                cos = new CheckedInputStream(bis, new CRC32());
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
        }
    }

    @Override
    public Boolean saveData(AuditTrailDto intranet, List<ProcessFileTrackDto> processFileTrackDtos) {
        Boolean saveFlag = false;
        List<String> textJson = new ArrayList<>();
        Boolean fileBoolean = false;
        Boolean aBoolean = false;
        try {
            File file = new File(download);
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(ProcessFileTrackDto pDto:processFileTrackDtos){
                    String fileName = pDto.getFileName().substring(0,pDto.getFileName().lastIndexOf("."));
                    for(File file2:files){
                        if(file2.getName().equals(fileName)){
                            saveFlag = saveDataDtoAndFile(file2, intranet, aBoolean, textJson,
                                    fileBoolean);
                            if(saveFlag){
                                pDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_SAVE_SUCCESSFUL);
                                pDto.setAuditTrailDto(intranet);
                                systemBeLicClient.updateProcessFileTrack(pDto);
                                ApplicationDto applicationDto = applicationClient.getApplicationById(pDto.getRefId()).getEntity();
                                boolean feSaveFlag = saveEicAndApp(pDto, "InspecSaveBeRecByImpl", applicationDto);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return saveFlag;
    }

    @EicService
    private boolean saveEicAndApp(ProcessFileTrackDto pDto, String inspecSaveBeRecByImpl, ApplicationDto applicationDto) {
        return false;
    }

    private boolean saveDataDtoAndFile(File file2, AuditTrailDto intranet, Boolean aBoolean, List<String> textJson,
                                       Boolean fileBoolean) {
        boolean saveFlag = false;
        try {
            if(file2.isDirectory()){
                File[] files2 = file2.listFiles();
                for(File file3:files2){
                    if(file3.isFile() && file3.getName().endsWith(fileFormat)){
                        FileInputStream fileInputStream = new FileInputStream(file3);
                        ByteArrayOutputStream by = new ByteArrayOutputStream();
                        byte[] size = new byte[1024];
                        int count = fileInputStream.read(size);
                        while(count != -1){
                            by.write(size,0, count);
                            count = fileInputStream.read(size);
                        }
                        aBoolean = fileToDto(by.toString(), intranet);
                        textJson.add(by.toString());
                    }
                }
                for(File file3:files2){
                    if(file3.isDirectory()) {
                        for(String s:textJson) {
                            fileBoolean = saveUploadFile(s, file3, intranet);
                        }
                    }
                }
                if(aBoolean && fileBoolean){
                    saveFlag = true;
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage(),e);
            saveFlag = false;
        }
        return saveFlag;
    }

    private boolean saveUploadFile(String toString, File file, AuditTrailDto intranet) {
        ApplicationListFileDto applicationListFileDto = JsonUtil.parseToObject(toString, ApplicationListFileDto.class);
        boolean flag = false;
        List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = applicationListFileDto.getAppPremPreInspectionNcDocDtos();
        if(!(IaisCommonUtils.isEmpty(appPremPreInspectionNcDocDtos))) {
            for (AppPremPreInspectionNcDocDto aItemDocDto : appPremPreInspectionNcDocDtos) {
                if(file.getName().equals(aItemDocDto.getNcItemId())){
                    flag = saveFileByNcItemId(aItemDocDto.getFileRepoId(), file, intranet);
                }
            }
        }
        return flag;
    }

    private boolean saveFileByNcItemId(String fileReportId, File file, AuditTrailDto intranet) {
        boolean flag = false;
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                FileItem fileItem = null;
                try {
                    fileItem = new DiskFileItem("selectedFile", Files.probeContentType(file2.toPath()),
                            false, file2.getName(), (int) file2.length(), file2.getParentFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream input = new FileInputStream(file2);
                    OutputStream os = fileItem.getOutputStream();
                    IOUtils.copy(input, os);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
                FileRepoDto fileRepoDto = new FileRepoDto();
                fileRepoDto.setId(fileReportId);
                fileRepoDto.setAuditTrailDto(intranet);
                fileRepoDto.setFileName(file.getName());
                fileRepoDto.setRelativePath(download);
                flag = fileRepoClient.saveFiles(multipartFile, JsonUtil.parseToJson(fileRepoDto)).hasErrors();
            }
        }
        return flag;
    }

    private Boolean fileToDto(String toString, AuditTrailDto intranet) {
        ApplicationListFileDto applicationListFileDto = JsonUtil.parseToObject(toString, ApplicationListFileDto.class);
        applicationListFileDto.setAuditTrailDto(intranet);
        boolean saveFlag = applicationClient.saveInspecRecDate(applicationListFileDto).getStatusCode() == 200;
        return saveFlag;
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
