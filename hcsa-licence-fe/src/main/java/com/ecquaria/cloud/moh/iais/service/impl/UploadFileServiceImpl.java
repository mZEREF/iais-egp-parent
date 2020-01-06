package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepositoryClient;
import com.ecquaria.sz.commons.util.FileUtil;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Wenkang
 * @date 2019/11/6 20:56
 */
@Service
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    private String download;
    private String fileName;
    private String fileFormat = ".text";
    private String backups;

    private Boolean flag=true;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private EicGatewayClient eicGatewayClient;
    @Autowired
    private FileRepositoryClient fileRepositoryClient;

    @Override
    public Boolean saveFile(String  str) {
        String s = FileUtil.genMd5FileChecksum(str.getBytes());
        File file=MiscUtil.generateFile(download, s+fileFormat);
        try (FileOutputStream fileInputStream = new FileOutputStream(backups+File.separator+file.getName());
             FileOutputStream fileOutputStream  =new FileOutputStream(file);) {
            if(!file.exists()){
                file.createNewFile();
            }
            fileOutputStream.write(str.getBytes());
            fileInputStream.write(str.getBytes());

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return false;
        }
        return true;
    }

    @Override
    public String getData() {
        fileName = "folder";
        download = sharedPath + "folder";
        backups = sharedPath + "backups";
        String entity = applicationClient.fileAll().getEntity();
        try{
            ApplicationListFileDto applicationListFileDto = JsonUtil.parseToObject(entity, ApplicationListFileDto.class);
            List<AppSvcDocDto> appSvcDoc = applicationListFileDto.getAppSvcDoc();
            List<AppGrpPrimaryDocDto> appGrpPrimaryDoc = applicationListFileDto.getAppGrpPrimaryDoc();
            appSvcDoc(appSvcDoc,appGrpPrimaryDoc);
        }catch (Exception e){
            log.error("***************** there have a error is "+e+"***************");
            log.error(e.getMessage(),e);
        }

        return    entity;
    }

    @Override
    public String  changeStatus() {
        if(flag){
            applicationClient.updateStatus(ApplicationConsts.APPLICATION_SUCCESS_ZIP).getEntity();
        }

        return "";
    }
    @Override
    public void compressFile(){
        String compress = compress();
        log.info("-------------compress() end --------------");
        rename(compress);

        deleteFile();
    }
    /*****************compress*********/
/*
*
*
* file id */
    private void appSvcDoc( List<AppSvcDocDto> appSvcDoc, List<AppGrpPrimaryDocDto> appGrpPrimaryDoc){
        //if path is not exists create path
            File fileRepPath=new File(download+File.separator+"files");
            if(!fileRepPath.exists()){
                fileRepPath.mkdirs();
            }
        for(AppSvcDocDto every:appSvcDoc){
            byte[] entity = fileRepositoryClient.getFileFormDataBase(every.getFileRepoId()).getEntity();
            File file = MiscUtil.generateFile(download + File.separator + "files",
                    every.getFileRepoId() + "@" + every.getDocName());
            try (FileOutputStream outputStream=new FileOutputStream(file)) {
              outputStream.write(entity);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }

        }
        for(AppGrpPrimaryDocDto every:appGrpPrimaryDoc){
            byte[] entity = fileRepositoryClient.getFileFormDataBase(every.getFileRepoId()).getEntity();
            File file = MiscUtil.generateFile(download+File.separator+"files",
                    every.getFileRepoId()+"@"+ every.getDocName());
            try (FileOutputStream fileOutputStream=new FileOutputStream(file);) {
                fileOutputStream.write(entity);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    private String compress(){
        log.info("------------ start compress() -----------------------");
        long l=0L;
        try (OutputStream is=new FileOutputStream(backups+File.separator+ l+".zip");
               CheckedOutputStream cos=new CheckedOutputStream(is,new CRC32());
               ZipOutputStream zos=new ZipOutputStream(cos)) {
            l = System.currentTimeMillis();
            log.info("------------zip file name is"+backups+File.separator+ l+".zip"+"--------------------");
            File file = new File(download);
            MiscUtil.checkDirs(file);
            zipFile(zos, file);
            log.info("----------------end zipFile ---------------------");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

        return l+"";
    }
    private void zipFile(ZipOutputStream zos,File file)  {
        log.info("-----------start zipFile---------------------");
        try (InputStream is = new FileInputStream(file.getPath());
             BufferedInputStream bis = new BufferedInputStream(is)) {
            if(file.isDirectory()){
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName))+File.separator));
                for(File f: Objects.requireNonNull(file.listFiles())){
                    zipFile(zos,f);
                }
            }
            else {
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName))));
                int count ;
                byte [] b =new byte[1024];
                count=bis.read(b);
                while(count!=-1){
                    zos.write(b,0,count);
                    count=bis.read(b);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }

    private void rename(String fileNamesss)  {
        log.info("--------------rename start ---------------------");
        flag = true;
        File zipFile =new File(backups);
        MiscUtil.checkDirs(zipFile);
        if(zipFile.isDirectory()){
           File[] files = zipFile.listFiles((dir, name) -> {
               if (name.endsWith(fileNamesss+".zip")) {
                   return true;
               }
               return false;
           });
           for(File file:files){
               try {
                   FileInputStream is=new FileInputStream(file);
                   ByteArrayOutputStream by=new ByteArrayOutputStream();
                   int count=0;
                   byte [] size=new byte[1024];
                   count=is.read(size);
                   while(count!=-1){
                       by.write(size,0,count);
                       count= is.read(size);
                   }
                    by.close();
                    is.close();
                   byte[] bytes = by.toByteArray();
                   String s = FileUtil.genMd5FileChecksum(bytes);
                   File curFile = MiscUtil.generateFile(backups, s + ".zip");
                   file.renameTo(curFile);
                   log.info("----------- new zip file name is"+backups+File.separator+s+".zip");
                   String s1 = saveFileName(s+".zip",backups + File.separator+s+".zip");
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
        File file =new File(download);
        File fileRepPath=new File(download+File.separator+"files");
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
        if(file.isDirectory()){
            File[] files = file.listFiles((dir, name) -> {
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
    }

    private String saveFileName(String fileName ,String filePath){
        ProcessFileTrackDto processFileTrackDto =new ProcessFileTrackDto();
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        processFileTrackDto.setFileName(fileName);
        processFileTrackDto.setFilePath(filePath);
        processFileTrackDto.setRefId("BE30AB5D-A92A-EA11-BE7D-000C29F371DC");
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
        AuditTrailDto intenet = AuditTrailHelper.getBatchJobDto("INTERNET");
        processFileTrackDto.setAuditTrailDto(intenet);
        String s="FAIL";
        try {
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            s = eicGatewayClient.saveFile(processFileTrackDto, signature.date(), signature.authorization()).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return s;
        }

        return s;
    }
}
