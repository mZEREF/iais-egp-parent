package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.SyncAuditTrailRecordsService;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainClient;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminMainFeClient;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
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
    SystemAdminMainFeClient systemAdminMainFeClient;
    @Autowired
    private EicGatewayFeMainClient eicGatewayClient;

    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    @Value("${iais.sharedfolder.auditTrail.out}")
    private String sharedOutPath;

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
        deleteFile();
        //if path is not exists create path
        File fileRepPath = MiscUtil.generateFile(sharedPath +File.separator+ RequestForInformationConstants.FILE_NAME_AUDIT, RequestForInformationConstants.FILES);
        MiscUtil.checkDirs(fileRepPath);

        AuditTrailEntityEventDto auditTrailEntityEventDto = new AuditTrailEntityEventDto();
        auditTrailEntityEventDto.setEventRefNo(System.currentTimeMillis()+"");
        auditTrailEntityEventDto.setAuditTrailEntityDtos(auditTrailDtos);
        return JsonUtil.parseToJson(auditTrailEntityEventDto);
    }

    @Override
    public void saveFile(String data) throws IOException {


        String s = FileUtil.genMd5FileChecksum(data.getBytes(StandardCharsets.UTF_8));
        File file=MiscUtil.generateFile(sharedPath +File.separator+ RequestForInformationConstants.FILE_NAME_AUDIT+File.separator, s+RequestForInformationConstants.FILE_FORMAT);
        if(!file.exists()){
            boolean createFlag = file.createNewFile();
            if (!createFlag) {
                log.debug("Create File fail");
            }
        }
        File groupPath = MiscUtil.generateFile(sharedPath , RequestForInformationConstants.FILE_NAME_AUDIT);

        if(!groupPath.exists()){
            groupPath.mkdirs();
        }
        File outFile = MiscUtil.generateFile(sharedOutPath,file.getName());
        try (OutputStream fileInputStream = java.nio.file.Files.newOutputStream(outFile.toPath());
             OutputStream fileOutputStream  = Files.newOutputStream(file.toPath())){

            fileOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
            fileInputStream.write(data.getBytes(StandardCharsets.UTF_8));

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
        File c= MiscUtil.generateFile(sharedOutPath);
        if(!c.exists()){
            c.mkdirs();
        }
        File outFile = MiscUtil.generateFile(sharedOutPath, l+ AppServicesConsts.ZIP_NAME);
        try (OutputStream is=java.nio.file.Files.newOutputStream(outFile.toPath());
             CheckedOutputStream cos=new CheckedOutputStream(is,new CRC32());
             ZipOutputStream zos=new ZipOutputStream(cos);){

            log.info(StringUtil.changeForLog("------------zip file name is"+sharedOutPath+File.separator+ l+AppServicesConsts.ZIP_NAME+"--------------------"));
            File file = MiscUtil.generateFile(sharedPath , RequestForInformationConstants.FILE_NAME_AUDIT);

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
            zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(RequestForInformationConstants.FILE_NAME_AUDIT))+File.separator));
            zos.closeEntry();
            for(File f: Objects.requireNonNull(file.listFiles())){
                zipFile(zos,f);
            }
        } else {
            try  (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(file.toPath()))){

                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(RequestForInformationConstants.FILE_NAME_AUDIT))));
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
        File zipFile =MiscUtil.generateFile(sharedOutPath);
        MiscUtil.checkDirs(zipFile);
        if(zipFile.isDirectory()){
            File[] files = zipFile.listFiles((dir, name) -> {
                if (name.endsWith(fileNamesss+AppServicesConsts.ZIP_NAME)) {
                    return true;
                }
                return false;
            });
            for(File file:files){
                try (InputStream is=Files.newInputStream(file.toPath());
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
                    File curFile =MiscUtil.generateFile(sharedOutPath , s + RequestForInformationConstants.ZIP_NAME);
                    boolean renameFlag = file.renameTo(curFile);
                    if (!renameFlag) {
                        log.debug("Rename file fail");
                    }
                    log.info(StringUtil.changeForLog("----------- new zip file name is"
                            +sharedOutPath+File.separator+s+RequestForInformationConstants.ZIP_NAME));
                    String s1 = saveFileName(s+RequestForInformationConstants.ZIP_NAME,s+RequestForInformationConstants.ZIP_NAME);
                    if(!s1.equals("SUCCESS")){
                        MiscUtil.deleteFile(curFile);
                        break;
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    private void deleteFile(){
        File file =MiscUtil.generateFile(sharedOutPath);
        File fileRepPath=MiscUtil.generateFile(sharedPath +File.separator+ RequestForInformationConstants.FILE_NAME_AUDIT,RequestForInformationConstants.FILES);
        File filePath=MiscUtil.generateFile(sharedPath , RequestForInformationConstants.FILE_NAME_AUDIT);
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
            if (name.endsWith(RequestForInformationConstants.FILE_FORMAT)) {
                return true;
            }
            return false;
        });
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.exists() && f.isFile()) {
                    MiscUtil.deleteFile(f);
                }
            }
        }
        files = file.listFiles((dir, name) -> {
            if (name.endsWith(RequestForInformationConstants.FILE_FORMAT)) {
                return true;
            }
            return false;
        });
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.exists() && f.isFile()) {
                    MiscUtil.deleteFile(f);
                }
            }
        }
    }

    private String saveFileName(String fileName ,String filePath ){
        ProcessFileTrackDto processFileTrackDto =new ProcessFileTrackDto();
        processFileTrackDto.setEventRefNo(System.currentTimeMillis()+"");
        processFileTrackDto.setProcessType(ApplicationConsts.AUDIT_TYPE_ROUTINE);
        processFileTrackDto.setFileName(fileName);
        processFileTrackDto.setFilePath(filePath);
        processFileTrackDto.setRefId(UUID.randomUUID().toString());
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
        AuditTrailDto intenet = AuditTrailHelper.getCurrentAuditTrailDto();
        processFileTrackDto.setAuditTrailDto(intenet);
        EicRequestTrackingDto eicRequestTrackingDto=new EicRequestTrackingDto();
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        Date now = new Date();
        eicRequestTrackingDto.setActionClsName("com.ecquaria.cloud.moh.iais.service.SyncAuditTrailRecordsServiceImpl");
        eicRequestTrackingDto.setActionMethod("eicCallSyncAuditTrailProcessFileTrack");
        eicRequestTrackingDto.setModuleName("main-web-internet");
        eicRequestTrackingDto.setDtoClsName(ProcessFileTrackDto.class.getName());
        eicRequestTrackingDto.setDtoObject(JsonUtil.parseToJson(processFileTrackDto));
        eicRequestTrackingDto.setProcessNum(1);
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        eicRequestTrackingDto.setRefNo(processFileTrackDto.getEventRefNo());
        updateSysAdmEicRequestTrackingDto(eicRequestTrackingDto);
        String s="FAIL";
        try {
            s=createBeAuditTrailProcessFileTrack(processFileTrackDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return s;
        }

        return s;
    }

    @Override
    public String createBeAuditTrailProcessFileTrack(ProcessFileTrackDto processFileTrackDto) {
        EicRequestTrackingDto trackDto = getLicEicRequestTrackingDtoByRefNo(processFileTrackDto.getEventRefNo());
        String s=eicCallSyncAuditTrailProcessFileTrack(processFileTrackDto);
        trackDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        updateSysAdmEicRequestTrackingDto(trackDto);

        return s;
    }

    public String eicCallSyncAuditTrailProcessFileTrack(ProcessFileTrackDto processFileTrackDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return eicGatewayClient.saveFile(processFileTrackDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
    }



    @Override
    public void   updateSysAdmEicRequestTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto) {
        List<EicRequestTrackingDto> eicRequestTrackingDtos= IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(licEicRequestTrackingDto);
        systemAdminMainFeClient.saveEicTrack(eicRequestTrackingDtos);
    }


    public EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo) {
        return systemAdminMainFeClient.getByRefNum(refNo).getEntity();
    }
}
