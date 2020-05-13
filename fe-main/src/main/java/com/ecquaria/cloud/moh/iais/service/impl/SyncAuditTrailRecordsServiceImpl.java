package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.SyncAuditTrailRecordsService;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainClient;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * SyncAuditTrailRecordsServiceImpl
 *
 * @author junyu
 * @date 2020/4/16
 */
@Service
@Slf4j
public class SyncAuditTrailRecordsServiceImpl implements SyncAuditTrailRecordsService {
    @Autowired
    AuditTrailMainClient auditTrailClient;

    @Autowired
    private EicGatewayFeMainClient eicGatewayClient;

    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    private String download;
    private String fileName;
    private String fileFormat = ".text";
    private String backups;

    private boolean flag = true;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public List<AuditTrailEntityDto> getAuditTrailsByMigrated1() {
        return auditTrailClient.getAuditTrailsByMigrated1().getEntity();
    }

    @Override
    public String getData(List<AuditTrailEntityDto> auditTrailDtos) {
        fileName = "userRecFile";
        download = sharedPath +fileName;
        backups = sharedPath + "backupsAudit";
        //if path is not exists create path
        File fileRepPath = MiscUtil.generateFile(download, "files");
        MiscUtil.checkDirs(fileRepPath);

        AuditTrailEntityEventDto auditTrailEntityEventDto = new AuditTrailEntityEventDto();
        auditTrailEntityEventDto.setEventRefNo(System.currentTimeMillis()+"");
        auditTrailEntityEventDto.setAuditTrailEntityDtos(auditTrailDtos);
        return JsonUtil.parseToJson(auditTrailEntityEventDto);
    }

    @Override
    public void saveFile(String data)  {
        fileName = "userRecFile";
        download = sharedPath + fileName;
        backups = sharedPath + "backupsAudit";

        String s = FileUtil.genMd5FileChecksum(data.getBytes());
        File file = MiscUtil.generateFile(download+File.separator, s+fileFormat);
        File groupPath=MiscUtil.generateFile(sharedPath, fileName);

        MiscUtil.checkDirs(groupPath);
        File backupFile = MiscUtil.generateFile(backups, file.getName());

        try (FileOutputStream fileInputStream = new FileOutputStream(backups+File.separator+file.getName());
             FileOutputStream fileOutputStream  =new FileOutputStream(file);){

            fileOutputStream.write(data.getBytes());
            fileInputStream.write(data.getBytes());

        } catch (Exception e) {

            log.error(e.getMessage(),e);
        }
    }

    @Override
    public void compressFile(){
        String compress = compress();
        log.info("-------------compress() end --------------");
        rename(compress);

        deleteFile();
    }


    private String compress(){
        log.info("------------ start compress() -----------------------");
        long l=   System.currentTimeMillis();
        File c= new File(backups+File.separator);
        MiscUtil.checkDirs(c);
        try (OutputStream is=new FileOutputStream(backups+File.separator+ l+ AppServicesConsts.ZIP_NAME);
             CheckedOutputStream cos=new CheckedOutputStream(is,new CRC32());
             ZipOutputStream zos=new ZipOutputStream(cos);){

            log.info(StringUtil.changeForLog("------------zip file name is"+backups+File.separator+ l+AppServicesConsts.ZIP_NAME+"--------------------"));
            File file = new File(download+File.separator);

            MiscUtil.checkDirs(file);
            zipFile(zos, file);
            log.info("----------------end zipFile ---------------------");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

        return l+"";
    }

    private void zipFile(ZipOutputStream zos,File file) throws IOException {
        log.info("-----------start zipFile---------------------");
        if (file.isDirectory()) {
            zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName))+File.separator));
            zos.closeEntry();
            for(File f: Objects.requireNonNull(file.listFiles())){
                zipFile(zos,f);
            }
        } else {
            try  (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))){

                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName))));
                int count ;
                byte [] b =new byte[1024];
                count=bis.read(b);
                while(count!=-1){
                    zos.write(b,0,count);
                    count=bis.read(b);
                }
                zos.closeEntry();
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }

    }

    private void rename(String fileNamesss)  {
        log.info("--------------rename start ---------------------");
        flag = true;
        File zipFile =new File(backups);
        MiscUtil.checkDirs(zipFile);
        if(zipFile.isDirectory()){
            File[] files = zipFile.listFiles((dir, name) -> {
                if (name.endsWith(fileNamesss+AppServicesConsts.ZIP_NAME)) {
                    return true;
                }
                return false;
            });
            for(File file:files){
                try (FileInputStream is=new FileInputStream(file);
                     ByteArrayOutputStream by=new ByteArrayOutputStream();){

                    int count=0;
                    byte [] size=new byte[1024];
                    count=is.read(size);
                    while(count!=-1){
                        by.write(size,0,count);
                        count= is.read(size);
                    }

                    byte[] bytes = by.toByteArray();
                    String s = FileUtil.genMd5FileChecksum(bytes);
                    File curFile =new File(backups + File.separator + s + AppServicesConsts.ZIP_NAME);
                    boolean renameTo = file.renameTo(curFile);
                    log.info(StringUtil.changeForLog("----------- new zip file name is"
                            + backups + File.separator + fileNamesss+AppServicesConsts.ZIP_NAME + " " + renameTo));
                    String s1 = saveFileName(fileNamesss+AppServicesConsts.ZIP_NAME,"backupsAudit" + File.separator+fileNamesss+AppServicesConsts.ZIP_NAME);
                    if(!s1.equals("SUCCESS")){
                        MiscUtil.deleteFile(curFile);
                        flag=false;
                        break;
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    private void deleteFile(){
        File file =new File(backups+File.separator);
        File fileRepPath=new File(download+File.separator+"files");
        File filePath=new File(download+File.separator);
        MiscUtil.checkDirs(fileRepPath);
        MiscUtil.checkDirs(file);
        if(fileRepPath.isDirectory()){
            File[] files = fileRepPath.listFiles();
            for(File f :files){
                if(f.exists()&&f.isFile()){
                    MiscUtil.deleteFile(f);
                }
            }
        }
        File[] files = filePath.listFiles((dir, name) -> {
            if (name.endsWith(fileFormat)) {
                return true;
            }
            return false;
        });
        for(File f:files){
            if(f.exists()&&f.isFile()){
                MiscUtil.deleteFile(f);
            }
        }
        files = file.listFiles((dir, name) -> {
            if (name.endsWith(fileFormat)) {
                return true;
            }
            return false;
        });
        for(File f:files){
            if(f.exists()&&f.isFile()){
                MiscUtil.deleteFile(f);
            }
        }
    }

    private String saveFileName(String fileName ,String filePath){
        ProcessFileTrackDto processFileTrackDto =new ProcessFileTrackDto();
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        processFileTrackDto.setFileName(fileName);
        processFileTrackDto.setFilePath(filePath);
        processFileTrackDto.setRefId(UUID.randomUUID().toString());
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
        AuditTrailDto intenet = AuditTrailHelper.getBatchJobDto("INTERNET");
        processFileTrackDto.setAuditTrailDto(intenet);
        String s="FAIL";
        try {
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            s = eicGatewayClient.saveFile(processFileTrackDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return s;
        }

        return s;
    }
}
