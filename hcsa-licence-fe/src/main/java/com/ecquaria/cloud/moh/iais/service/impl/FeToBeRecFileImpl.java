package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.FeToBeRecFileService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Shicheng
 * @date 2019/12/26 14:56
 **/
@Service
@Slf4j
public class FeToBeRecFileImpl implements FeToBeRecFileService {
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    private String download;
    private String fileName;
    private String fileFormat = ".text";
    private String backups;

    private Boolean flag = true;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private EicGatewayClient eicGatewayClient;

    @Autowired
    private FileRepoClient fileRepoClient;

    @Override
    public String getRecData() {
        return applicationClient.recDatesToString().getEntity();
    }

    @Override
    public void createDataTxt(String data) {
        fileName = "userRecFile";
        download = sharedPath + fileName;
        backups = sharedPath + "backupsRec";
        FileOutputStream fileOutputStream = null;
        FileOutputStream fileOutputStream2 = null;
        File d = new File(download);
        File b = new File(backups);
        if(!d.exists()){
            d.mkdirs();
        }
        if(!b.exists()){
            b.mkdirs();
        }
        File file = new File(download + File.separator + data + fileFormat);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fileOutputStream2 = new FileOutputStream(backups + File.separator + file.getName());
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());
            fileOutputStream2.write(data.getBytes());

        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        finally {
            if(fileOutputStream2!=null){
                try {
                    fileOutputStream2.close();
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
    }

    @Override
    public void compressFile() {
        String compress = compress();
        rename(compress);
        deleteFile();
    }

    @Override
    public List<ApplicationDto> getDocFile() {
        Map<String, Map<String, AppPremPreInspectionNcDocDto>> fileReportIds = applicationClient.recFileId().getEntity();
        List<ApplicationDto> applicationDtos = new ArrayList<>();
        for(Map.Entry<String, Map<String, AppPremPreInspectionNcDocDto>> entry : fileReportIds.entrySet()){
            String appId = entry.getKey();
            Map<String, AppPremPreInspectionNcDocDto> mapValue = entry.getValue();
            getFileAndClassify(appId, mapValue);
            ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
            applicationDtos.add(applicationDto);
        }
        return applicationDtos;
    }

    @Override
    public void changeStatus(List<ApplicationDto> applicationDtos) {
        if(flag){
            for(ApplicationDto aDto : applicationDtos){
                aDto.setStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION);
                applicationClient.updateApplication(aDto);
            }
        }
    }

    private void getFileAndClassify(String appId, Map<String, AppPremPreInspectionNcDocDto> mapValue) {
        for(Map.Entry<String, AppPremPreInspectionNcDocDto> entry : mapValue.entrySet()){
            String mapKey = entry.getKey();
            AppPremPreInspectionNcDocDto appPremPreInspectionNcDocDto = entry.getValue();
            byte[] fileByte = fileRepoClient.getFileFormDataBase(appPremPreInspectionNcDocDto.getFileRepoId()).getEntity();
            String s;
            try {
                s = new String(fileByte, "UTF-8");
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                s = "";
            }
            File file = new File(download + File.separator + appId + File.separator + mapKey + File.separator + s, appPremPreInspectionNcDocDto.getDocName());
            File backupsFile = new File(backups + File.separator + appId + File.separator + mapKey + File.separator + s, appPremPreInspectionNcDocDto.getDocName());
            writeFileByFileByte(file, backupsFile, fileByte);
        }
    }

    private void writeFileByFileByte(File file, File backupsFile, byte[] fileByte) {
        FileOutputStream fileOutputStream = null;
        FileOutputStream fileOutputStream2 = null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fileOutputStream2 = new FileOutputStream(backupsFile);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(fileByte);
            fileOutputStream2.write(fileByte);

        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        finally {
            if(fileOutputStream2 != null){
                try {
                    fileOutputStream2.close();
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
    }

    private String compress(){
        long l = 0L;
        ZipOutputStream zos = null;
        CheckedOutputStream cos = null;
        OutputStream is = null;
        try {
            l = System.currentTimeMillis();
            is = new FileOutputStream(backups + File.separator + l + ".zip");
            cos = new CheckedOutputStream(is, new CRC32());
            zos = new ZipOutputStream(cos);
            File file = new File(download);
            zipFile(zos, file);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        finally {
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if(cos != null){
                try {
                    cos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return l+"";
    }

    private void zipFile(ZipOutputStream zos, File file)  {
        BufferedInputStream bis = null;
        InputStream is = null;
        try {
            if(file.isDirectory()){
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName)) + File.separator));
                for(File f: Objects.requireNonNull(file.listFiles())){
                    zipFile(zos, f);
                }
            }
            else {
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName))));
                is = new FileInputStream(file.getPath());
                bis = new BufferedInputStream(is);
                int count ;
                byte [] b = new byte[1024];
                count = bis.read(b);
                while(count != -1){
                    zos.write(b,0, count);
                    count = bis.read(b);
                }
            }
        }catch (IOException e){
            log.error(e.getMessage(), e);
        }
        finally {

            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if(bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private void rename(String fileNamesss)  {
        flag = true;
        File zipFile = new File(backups);
        if(zipFile.isDirectory()){
            File[] files = zipFile.listFiles((dir, name) -> {
                if (name.endsWith(fileNamesss + ".zip")) {
                    return true;
                }
                return false;
            });
            for(File file:files){
                try {
                    FileInputStream is = new FileInputStream(file);
                    ByteArrayOutputStream by = new ByteArrayOutputStream();
                    byte [] size = new byte[1024];
                    int count = is.read(size);
                    while(count != -1){
                        by.write(size,0, count);
                        count = is.read(size);
                    }
                    by.close();
                    is.close();
                    byte[] bytes = by.toByteArray();
                    String s = FileUtil.genMd5FileChecksum(bytes);
                    file.renameTo(new File(backups + File.separator + s + ".zip"));
                    String s1 = saveFileName(s + ".zip",backups + File.separator + s + ".zip");
                    if(!s1.equals(AppConsts.SUCCESS)){
                        new File(backups + File.separator + s + ".zip").delete();
                        flag = false;
                        break;
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private void deleteFile(){
        File file = new File(download);
        if(file.isDirectory()){
            File[] files = file.listFiles((dir, name) -> {
                if (name.endsWith(fileFormat)) {
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

    private String saveFileName(String fileName ,String filePath){
        ProcessFileTrackDto processFileTrackDto =new ProcessFileTrackDto();
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION);
        processFileTrackDto.setFileName(fileName);
        processFileTrackDto.setFilePath(filePath);
        processFileTrackDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET);
        processFileTrackDto.setAuditTrailDto(internet);
        String s = AppConsts.FAIL;
        try {
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            s = eicGatewayClient.saveFile(processFileTrackDto, signature.date(), signature.authorization()).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return s;
        }
        return s;
    }
}
