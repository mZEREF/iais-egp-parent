package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.FeToBeRecFileService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
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

    private Boolean flag = Boolean.TRUE;
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

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    private AppEicClient appEicClient;

    @Override
    public void compressFile(Map<String, String> appIdItemIdMap) {
        if(appIdItemIdMap != null) {
            for (Map.Entry<String, String> fileId : appIdItemIdMap.entrySet()) {
                String compress = compress(fileId.getKey());
                String appId = appIdItemIdMap.get(fileId.getKey());
                rename(compress, appId);
                deleteFile();
            }
        }
    }

    @Override
    public Map<String, String> getDocFile() {
        fileName = "userRecFile";
        download = sharedPath + fileName;
        backups = sharedPath + "backupsRec";
        Map<String, List<AppPremPreInspectionNcDocDto>> fileReportIds = applicationClient.recFileId().getEntity();
        Map<String, String> appIdNcItemIdMap = IaisCommonUtils.genNewHashMap();
        if(fileReportIds != null) {
            for (Map.Entry<String, List<AppPremPreInspectionNcDocDto>> entry : fileReportIds.entrySet()) {
                String appId = entry.getKey();
                List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = entry.getValue();
                appIdNcItemIdMap = getFileAndClassify(appId, appPremPreInspectionNcDocDtos, appIdNcItemIdMap);
            }
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
        OutputStream fileOutputStream = null;
        OutputStream fileOutputStream2 = null;
        try {
            if(!file.exists()){
                boolean fileStatus = file.createNewFile();
                if(fileStatus) {
                    fileOutputStream = Files.newOutputStream(file.toPath());
                    fileOutputStream.write(fileByte);
                }
            } else {
                fileOutputStream = Files.newOutputStream(file.toPath());
                fileOutputStream.write(fileByte);
            }
            if(!backupsFile.exists()){
                boolean fileStatus2 = backupsFile.createNewFile();
                if(fileStatus2) {
                    fileOutputStream2 = Files.newOutputStream(backupsFile.toPath());
                    fileOutputStream2.write(fileByte);
                }
            } else {
                fileOutputStream2 = Files.newOutputStream(backupsFile.toPath());
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
                log.error(e.getMessage(), e);
            }
            for(File f: Objects.requireNonNull(file.listFiles())){
                zipFile(zos, f, curFileName);
            }
        } else {
            try(BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
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
        flag = Boolean.TRUE;
        File zipFile = new File(backups);
        if(zipFile.isDirectory()){
            File[] files = zipFile.listFiles((dir, name) -> {
                if (name.endsWith(fileNamesss + ".zip")) {
                    return true;
                }
                return false;
            });
            for(File file:files){
                try (InputStream is = Files.newInputStream(file.toPath());
                     ByteArrayOutputStream by = new ByteArrayOutputStream()){
                    byte [] size = new byte[1024];
                    int count = is.read(size);
                    while(count != -1){
                        by.write(size,0, count);
                        count = is.read(size);
                    }
                    byte[] bytes = by.toByteArray();
                    String s = FileUtil.genMd5FileChecksum(bytes);
                    File curFile = new File(backups, s + ".zip");
                    if (!curFile.exists()){
                        boolean createFlag = curFile.createNewFile();
                        if (!createFlag) {
                            log.error("Create file fail");
                        }
                    }
                    boolean reNameFlag = file.renameTo(curFile);
                    if(reNameFlag) {
                        String s1 = saveFileName(s + ".zip", "backupsRec" + File.separator + s + ".zip", appId);
                        if(!s1.equals(AppConsts.SUCCESS)){
                            boolean fileDelStatus = new File(backups + File.separator + s + ".zip").delete();
                            if(!fileDelStatus){
                                log.debug(StringUtil.changeForLog(file.getName() + "delete false"));
                            }
                            flag = Boolean.FALSE;
                            break;
                        }
                    } else {
                        log.debug(StringUtil.changeForLog("file rename fail!!!"));
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

    public String saveFileName(String fileName ,String filePath, String appId){
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
            EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.FeToBeRecFileImpl", "saveFileName",
                    "hcsa-licence-web-internet", ProcessFileTrackDto.class.getName(), JsonUtil.parseToJson(processFileTrackDto));
            String eicRefNo = eicRequestTrackingDto.getRefNo();
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            s = eicGatewayClient.saveFile(processFileTrackDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
            //get eic record
            eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
            //update eic record status
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
            eicRequestTrackingDtos.add(eicRequestTrackingDto);
            appEicClient.updateStatus(eicRequestTrackingDtos);
            if(AppConsts.SUCCESS.equals(s)) {
                ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
                applicationDto.setAuditTrailDto(internet);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_BE_CREATE_TASK);
                applicationClient.updateApplication(applicationDto);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return s;
        }
        return s;
    }
}
