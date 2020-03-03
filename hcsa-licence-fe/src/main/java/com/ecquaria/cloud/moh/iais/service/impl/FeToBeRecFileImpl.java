package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.FeToBeRecFileService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
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
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private FeEicGatewayClient eicGatewayClient;

    @Autowired
    private FileRepoClient fileRepoClient;

    @Override
    public void compressFile(Map<String, String> appIdItemIdMap) {
        Set<String> fileIds = appIdItemIdMap.keySet();
        for(String fileId : fileIds) {
            String compress = compress(fileId);
            String appId = appIdItemIdMap.get(fileId);
            rename(compress, appId);
            deleteFile();
        }
    }

    @Override
    public Map<String, String> getDocFile() {
        /*fileName = "userRecFile";
        download = sharedPath + fileName;
        backups = sharedPath + "backupsRec";*/
        fileName = "userRecFile";
        download = "D:" + File.separator + fileName;
        backups = "D:" + File.separator + "backupsRec";
        Map<String, List<AppPremPreInspectionNcDocDto>> fileReportIds = applicationClient.recFileId().getEntity();
        Map<String, String> appIdNcItemIdMap = new HashMap<>();
        for(Map.Entry<String, List<AppPremPreInspectionNcDocDto>> entry : fileReportIds.entrySet()){
            String appId = entry.getKey();
            List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = entry.getValue();
            appIdNcItemIdMap = getFileAndClassify(appId, appPremPreInspectionNcDocDtos, appIdNcItemIdMap);
        }
        return appIdNcItemIdMap;
    }

    private Map<String, String> getFileAndClassify(String appId, List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos, Map<String, String> appIdNcItemIdMap) {
        for(AppPremPreInspectionNcDocDto appPremPreInspectionNcDocDto : appPremPreInspectionNcDocDtos){
            String fileId = appPremPreInspectionNcDocDto.getFileRepoId();
            byte[] fileByte = fileRepoClient.getFileFormDataBase(fileId).getEntity();

            File file = new File(download + File.separator + fileId, appPremPreInspectionNcDocDto.getDocName());
            File backupsFile = new File(backups + File.separator + fileId, appPremPreInspectionNcDocDto.getDocName());
            File groupPath = new File(download + File.separator + fileId);
            if(!groupPath.exists()){
                groupPath.mkdirs();
            }
            File groupBackPath = new File(backups + File.separator + fileId);
            if(!groupBackPath.exists()){
                groupBackPath.mkdirs();
            }
            appIdNcItemIdMap.put(fileId, appId);
            writeFileByFileByte(file, backupsFile, fileByte);
        }
        return appIdNcItemIdMap;
    }

    private void writeFileByFileByte(File file, File backupsFile, byte[] fileByte) {
        FileOutputStream fileOutputStream = null;
        FileOutputStream fileOutputStream2 = null;
        try {
            if(!file.exists()){
                boolean fileStatus = file.createNewFile();
                if(fileStatus) {
                    fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(fileByte);
                }
            } else {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(fileByte);
            }
            if(!backupsFile.exists()){
                boolean fileStatus2 = backupsFile.createNewFile();
                if(fileStatus2) {
                    fileOutputStream2 = new FileOutputStream(backupsFile);
                    fileOutputStream2.write(fileByte);
                }
            } else {
                fileOutputStream2 = new FileOutputStream(backupsFile);
                fileOutputStream2.write(fileByte);
            }

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

    private String compress(String fileId){
        long l = System.currentTimeMillis();
        try (OutputStream is = new FileOutputStream(backups + File.separator + l + ".zip");
             CheckedOutputStream cos = new CheckedOutputStream(is, new CRC32());
             OutputStream is2 = new FileOutputStream(download + File.separator + l + ".zip");
             CheckedOutputStream cos2 = new CheckedOutputStream(is2, new CRC32());
             ZipOutputStream zos = new ZipOutputStream(cos);
             ZipOutputStream zos2 = new ZipOutputStream(cos2)){

            File file2 = new File(download + File.separator + fileId);
            File file = new File(backups + File.separator + fileId);
            MiscUtil.checkDirs(file);
            MiscUtil.checkDirs(file2);
            zipFile(zos, file, "backupsRec");
            zipFile(zos2, file2, fileName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return l+"";
    }

    private void zipFile(ZipOutputStream zos, File file, String curFileName)  {
        if(file.isDirectory()){
            String filePath = file.getPath().substring(file.getPath().indexOf(curFileName));
            try {
                zos.putNextEntry(new ZipEntry(filePath + File.separator));
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(File f: Objects.requireNonNull(file.listFiles())){
                zipFile(zos, f, curFileName);
            }
        } else {
            try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            String filePath = file.getPath().substring(file.getPath().indexOf(curFileName));
            zos.putNextEntry(new ZipEntry(filePath));
            int count ;
            byte [] b = new byte[1024];
            count = bis.read(b);
            while(count != -1){
                zos.write(b,0, count);
                count = bis.read(b);
            }
            }catch (IOException e){
                log.error(e.getMessage(), e);
            }
        }
    }

    private void rename(String fileNamesss, String appId)  {
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
                try (FileInputStream is = new FileInputStream(file);
                     ByteArrayOutputStream by = new ByteArrayOutputStream()){
                    byte [] size = new byte[1024];
                    int count = is.read(size);
                    while(count != -1){
                        by.write(size,0, count);
                        count = is.read(size);
                    }
                    byte[] bytes = by.toByteArray();
                    String s = FileUtil.genMd5FileChecksum(bytes);
                    boolean fileStatus = file.renameTo(new File(backups + File.separator + s + ".zip"));
                    if(!fileStatus){
                        log.debug(StringUtil.changeForLog(file.getName() + "renameTo false"));
                    }
                    String s1 = saveFileName(s + ".zip",backups + File.separator + s + ".zip", appId);
                    if(!s1.equals(AppConsts.SUCCESS)){
                        AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET);
                        ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
                        applicationDto.setAuditTrailDto(internet);
                        applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_BE_CREATE_TASK);
                        applicationClient.updateApplication(applicationDto);
                        boolean fileDelStatus = new File(backups + File.separator + s + ".zip").delete();
                        if(!fileDelStatus){
                            log.debug(StringUtil.changeForLog(file.getName() + "delete false"));
                        }
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
                    boolean fileStatus = f.delete();
                    if(!fileStatus){
                        log.debug(StringUtil.changeForLog(file.getName() + "delete false"));
                    }
                }
            }
        }
    }

    private String saveFileName(String fileName ,String filePath, String appId){
        ProcessFileTrackDto processFileTrackDto =new ProcessFileTrackDto();
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION);
        processFileTrackDto.setFileName(fileName);
        processFileTrackDto.setFilePath(filePath);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
        processFileTrackDto.setRefId(appId);
        AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET);
        processFileTrackDto.setAuditTrailDto(internet);
        String s = AppConsts.FAIL;
        try {
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            s = eicGatewayClient.saveFile(processFileTrackDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return s;
        }
        return s;
    }
}
