package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.MonitoringSheetsDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ExcelMonitoringService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.sz.commons.util.FileUtil;
import ecq.commons.sequence.uuid.UUID;
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
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.Files.newOutputStream;

/**
 * ExcelMonitoringServiceImpl
 *
 * @author junyu
 * @date 2022/5/19
 */
@Service
@Slf4j
public class ExcelMonitoringServiceImpl implements ExcelMonitoringService {
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    @Value("${iais.sharedfolder.datacompair.out}")
    private String sharedOutPath;

    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;
    @Autowired
    private FeEicGatewayClient eicGatewayClient;

    @Override
    public MonitoringSheetsDto parse() {
        MonitoringSheetsDto monitoringAppSheetsDto=applicationFeClient.getMonitoringAppSheetsDto().getEntity();
        MonitoringSheetsDto monitoringLicSheetsDto=licenceClient.getMonitoringLicenceSheetsDto().getEntity();
        MonitoringSheetsDto monitoringUserSheetsDto=organizationLienceseeClient.getMonitoringUserSheetsDto().getEntity();
        monitoringAppSheetsDto.setLicenceExcelDtoMap(monitoringLicSheetsDto.getLicenceExcelDtoMap());
        monitoringAppSheetsDto.setUserAccountExcelDtoMap(monitoringUserSheetsDto.getUserAccountExcelDtoMap());
        return monitoringAppSheetsDto;
    }

    @Override
    public String saveFile(MonitoringSheetsDto parse) {
        String str = JsonUtil.parseToJson(parse);
        String uuId= UUID.randomUUID().toString();
        String s = "";
        try{
            s = FileUtil.genMd5FileChecksum(str.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        File file = MiscUtil.generateFile(sharedPath+ AppServicesConsts.FILE_NAME+File.separator+uuId, s+AppServicesConsts.FILE_FORMAT);
        try (OutputStream fileOutputStream  = newOutputStream(file.toPath());) {
            if(!file.exists()){
                boolean newFile = file.createNewFile();
                if(newFile){
                    log.info("***newFile createNewFile***");
                }
            }
            fileOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return null;
        }
        return uuId;
    }

    @Override
    public String compressFile(String uuId) {
        String compress = compress(uuId);
        log.info("-------------compress() end --------------");
        return compress;
    }

    private String compress(String groupId){
        log.info("------------ start compress() -----------------------");
        long l =   System.currentTimeMillis();
        String outFolder = sharedOutPath;
        if (!outFolder.endsWith(File.separator)) {
            outFolder += File.separator;
        }
        String soPath = sharedOutPath;
        File zipFile = MiscUtil.generateFile(soPath);
        MiscUtil.checkDirs(zipFile);
        String osPath = outFolder + l + AppServicesConsts.ZIP_NAME;
        File osFile = MiscUtil.generateFile(osPath);
        try (OutputStream outputStream = newOutputStream(osFile.toPath());//Destination compressed folder
             CheckedOutputStream cos=new CheckedOutputStream(outputStream,new CRC32());
             ZipOutputStream zos=new ZipOutputStream(cos)) {

            log.info(StringUtil.changeForLog("------------zip file name is"+ outFolder + l+".zip"+"--------------------"));
            String path = sharedPath + AppServicesConsts.FILE_NAME + File.separator + groupId;
            File file = MiscUtil.generateFile(path);

            zipFile(zos, file);
            log.info("----------------end zipFile ---------------------");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new IaisRuntimeException(e);
        }
        return l+"";
    }

    private void zipFile(ZipOutputStream zos,File file) throws IOException {
        log.info("-----------start zipFile---------------------");
        if (file.isDirectory()) {
            zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(AppServicesConsts.FILE_NAME))+File.separator));
            zos.closeEntry();
            for(File f: Objects.requireNonNull(file.listFiles())){
                zipFile(zos,f);
            }
        } else {
            try (
                    BufferedInputStream bis = new BufferedInputStream( Files.newInputStream(file.toPath()))) {
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(AppServicesConsts.FILE_NAME))));
                int count ;
                byte [] b =new byte[1024];
                count = bis.read(b);
                while(count!=-1){
                    zos.write(b,0,count);
                    count=bis.read(b);
                }
                zos.closeEntry();
            }catch (Exception e){
                log.error(e.getMessage(),e);
                throw new IaisRuntimeException(e);
            }
        }

    }

    @Override
    public void renameAndSave(String fileNamesss,String groupId) {
        log.info("--------------rename start ---------------------");
        String outFolder = sharedOutPath;
        if (!outFolder.endsWith(File.separator)) {
            outFolder += File.separator;
        }
        String soPath = sharedOutPath;
        File zipFile = MiscUtil.generateFile(soPath);
        if(zipFile.isDirectory()){
            File[] files = zipFile.listFiles((dir, name) -> {
                if (name.endsWith(fileNamesss+".zip")) {
                    return true;
                }
                return false;
            });
            for(File file:files){
                try (InputStream is= Files.newInputStream(file.toPath());
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
                    File curFile = MiscUtil.generateFile(sharedOutPath, s + ".zip");
                    boolean b = file.renameTo(curFile);
                    if(b){
                        log.info(StringUtil.changeForLog("----------- new zip file name is"+outFolder+s+".zip"));
                    }
                    String string = saveFileName(s + AppServicesConsts.ZIP_NAME, s + AppServicesConsts.ZIP_NAME, groupId);
                    /*        String s1 = saveFileName(s+AppServicesConsts.ZIP_NAME,AppServicesConsts.BACKUPS + File.separator+s+AppServicesConsts.ZIP_NAME,groupId);*/
                    log.info(StringUtil.changeForLog("----"+string));
                    if(!string.equals("SUCCESS")){
                        MiscUtil.deleteFile(curFile);
                        break;
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                    throw new IaisRuntimeException(e);
                }
            }
        }
    }

    private String saveFileName(String fileName ,String filePath,String groupId){
        ProcessFileTrackDto processFileTrackDto =new ProcessFileTrackDto();
        processFileTrackDto.setProcessType(ApplicationConsts.APP_GROUP_MISC_TYPE_TRANSFER_REASON);
        processFileTrackDto.setFileName(fileName);
        processFileTrackDto.setFilePath(filePath);
        processFileTrackDto.setRefId(groupId);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
        AuditTrailDto intenet = AuditTrailHelper.getCurrentAuditTrailDto();
        processFileTrackDto.setAuditTrailDto(intenet);
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(processFileTrackDto)+"processFileTrackDto"));
        String s="FAIL";
        try {
            s = eicGatewayClient.callEicWithTrack(processFileTrackDto, eicGatewayClient::saveFileApplication,
                    "saveFileApplication").getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.info(StringUtil.changeForLog("have error-------" +s));
            throw new IaisRuntimeException(e);
        }
        return s;
    }

}
