package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AuditTrailRecordsToBeService;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainBeClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicMainClient;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * AuditTrailRecordsToBeServiceImpl
 *
 * @author junyu
 * @date 2020/4/16
 */
@Service
@Slf4j
public class AuditTrailRecordsToBeServiceImpl implements AuditTrailRecordsToBeService {
    @Value("${iais.syncFileTracking.shared.path}")
    private     String sharedPath;

    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private SystemBeLicMainClient systemClient;

    @Autowired
    AuditTrailMainBeClient auditTrailMainBeClient;

    @Override
    public void info() {


        File file =new File(sharedPath+File.separator+File.separator+"folder");
        File b=new File(sharedPath+File.separator+ RequestForInformationConstants.BACKUPS_AUDIT +File.separator);
        File c=new File(sharedPath+File.separator+RequestForInformationConstants.COMPRESS);
        if(!c.exists()){
            c.mkdirs();
        }
        if(!b.exists()){
            b.mkdirs();
        }

        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public void compress(){
        log.info("-------------compress start ---------");
        if(new File(sharedPath+File.separator+ RequestForInformationConstants.BACKUPS_AUDIT +File.separator).isDirectory()){
            File[] files = new File(sharedPath+File.separator+ RequestForInformationConstants.BACKUPS_AUDIT +File.separator).listFiles();
            for(File fil:files){
                if(fil.getName().endsWith(RequestForInformationConstants.ZIP_NAME)){
                    String name = fil.getName();
                    String path = fil.getPath();
                    String relPath=RequestForInformationConstants.BACKUPS_AUDIT+File.separator+name;
                    HashMap<String,String> map= IaisCommonUtils.genNewHashMap();
                    map.put("fileName",name);
                    map.put("filePath",relPath);

                    ProcessFileTrackDto processFileTrackDto = systemClient.isFileExistence(map).getEntity();
                    if(processFileTrackDto!=null){
                        try (InputStream is= Files.newInputStream(fil.toPath());
                             ByteArrayOutputStream by=new ByteArrayOutputStream();) {
                            int count;
                            byte [] size=new byte[1024];
                            count=is.read(size);
                            while(count!=-1){
                                by.write(size,0,count);
                                count= is.read(size);
                            }

                            byte[] bytes = by.toByteArray();
                            String s = FileUtil.genMd5FileChecksum(bytes);
                            s = s + AppServicesConsts.ZIP_NAME;
                            if( !s.equals(name)){
                                continue;
                            }
                        }catch (Exception e){
                            log.error(e.getMessage(),e);
                            continue;
                        }
                        String refId = processFileTrackDto.getRefId();
                        CheckedInputStream cos=null;
                        BufferedInputStream bis=null;
                        BufferedOutputStream bos=null;
                        OutputStream os=null;
                        try (ZipFile zipFile=new ZipFile(path);)  {
                            for(Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();){
                                ZipEntry zipEntry = entries.nextElement();
                                zipFile(zipEntry,os,bos,zipFile,bis,cos,name,refId);
                            }

                        } catch (IOException e) {
                            log.error(e.getMessage(),e);
                        }

                        try {
                            String submissionId = generateIdClient.getSeqId().getEntity();
                            this.download(processFileTrackDto,name,refId,submissionId);
                            //save success
                        }catch (Exception e){
                            //save bad

                        }
                    }
                }
            }
        }
    }
    private void zipFile( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos,String fileName,String refId)  {


        try {
            if(!zipEntry.getName().endsWith(File.separator)){

                String substring = zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator));
                File file =new File(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId+File.separator+substring);
                if(!file.exists()){
                    file.mkdirs();
                }
                os= Files.newOutputStream(Paths.get(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId+File.separator+zipEntry.getName()));
                bos=new BufferedOutputStream(os);
                InputStream is=zipFile.getInputStream(zipEntry);
                bis=new BufferedInputStream(is);
                cos=new CheckedInputStream(bis,new CRC32());
                byte []b=new byte[1024];
                int count =0;
                count=cos.read(b);
                while(count!=-1){
                    bos.write(b,0,count);
                    count=cos.read(b);
                }

            }else {

                new File(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId+File.separator+zipEntry.getName()).mkdirs();
            }
        }catch (IOException e){

        }finally {
            if(cos!=null){
                try {
                    cos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
            if(bos!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    @Override
    public void download(ProcessFileTrackDto processFileTrackDto, String fileName, String refId, String submissionId) {

        File file =new File(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+RequestForInformationConstants.FILE_NAME_AUDIT);
        if(!file.exists()){
            file.mkdirs();
        }

        File[] files = file.listFiles();
        for(File  filzz:files){
            if(filzz.isFile() &&filzz.getName().endsWith(RequestForInformationConstants.FILE_FORMAT)){
                try ( InputStream fileInputStream =Files.newInputStream(filzz.toPath())){
                    ByteArrayOutputStream by=new ByteArrayOutputStream();
                    int count=0;
                    byte [] size=new byte[1024];
                    count=fileInputStream.read(size);
                    while(count!=-1){
                        by.write(size,0,count);
                        count= fileInputStream.read(size);
                    }
                    Boolean aBoolean = fileToDto(by.toString(),processFileTrackDto,submissionId);
                    if(aBoolean&&processFileTrackDto!=null){
                        changeStatus(processFileTrackDto);
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }

    }
    private Boolean fileToDto(String str,ProcessFileTrackDto processFileTrackDto,String submissionId){
        AuditTrailEntityEventDto auditTrailEntityEventDto = JsonUtil.parseToObject(str, AuditTrailEntityEventDto.class);
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto("intranet");
        auditTrailEntityEventDto.setAuditTrailDto(intranet);
        //eventbus
//        auditTrailEntityEventDto.setEventRefNo(processFileTrackDto.getRefId());
//        eventBusHelper.submitAsyncRequest(auditTrailEntityEventDto,submissionId, EventBusConsts.SERVICE_NAME_AUDIT_TRAIL,
//                EventBusConsts.OPERATION_SYNC_AUDIT_TRAIL,auditTrailEntityEventDto.getEventRefNo(),null);
//        return true;
        for (AuditTrailEntityDto a:auditTrailEntityEventDto.getAuditTrailEntityDtos()
        ) {
            a.setMigrated(2);
            a.setAuditId(null);
        }
        return auditTrailMainBeClient.syucUpdateAuditTrail(auditTrailEntityEventDto.getAuditTrailEntityDtos()).getStatusCode() == 200;

    }

    private void changeStatus( ProcessFileTrackDto processFileTrackDto){
        /*  applicationClient.updateStatus().getEntity();*/
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY);
        AuditTrailDto batchJobDto = AuditTrailHelper.getBatchJobDto("INTRANET");
        processFileTrackDto.setAuditTrailDto(batchJobDto);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_COMPLETE);
        systemClient.updateProcessFileTrack(processFileTrackDto);

    }

}
