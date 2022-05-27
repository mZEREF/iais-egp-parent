package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.systeminfo.ServicesSysteminfo;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

/**
 * LicenceFileDownloadServiceImpl
 *
 * @author junyu
 * @date 2022/5/11
 */
@Service
@Slf4j
public class LicenceFileDownloadServiceImpl implements LicenceFileDownloadService {

    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    @Value("${iais.sharedfolder.beapp.in}")
    private String inSharedPath;

    @Autowired
    private ApplicationFeClient applicationClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private EventBusHelper eventBusHelper;

    @Override
    public void initPath() {

        File compress = MiscUtil.generateFile(sharedPath+File.separator+ AppServicesConsts.COMPRESS,AppServicesConsts.FILE_NAME);
        File backups=MiscUtil.generateFile(inSharedPath);
        File compressPath=MiscUtil.generateFile(sharedPath,AppServicesConsts.COMPRESS);
        File movePath=MiscUtil.generateFile(sharedPath,"move");
        if(!compressPath.exists()){
            compressPath.mkdirs();
        }
        if(!backups.exists()){
            backups.mkdirs();
        }

        if(!compress.exists()){
            compress.mkdirs();
        }
        if(!movePath.exists()){
            movePath.mkdirs();
        }
    }

    @Override
    public boolean decompression() {
        log.info("-------------decompression start ---------");
        String inFolder = inSharedPath;
        if (!inFolder.endsWith(File.separator)) {
            inFolder += File.separator;
        }
        List<ProcessFileTrackDto> processFileTrackDtos = applicationClient.allNeedProcessFile().getEntity();
        if(processFileTrackDtos!=null&&!processFileTrackDtos.isEmpty()){
            log.info(StringUtil.changeForLog("-----start process file-----, process file size ==>" + processFileTrackDtos.size()));
            for (ProcessFileTrackDto v : processFileTrackDtos) {
                File file = MiscUtil.generateFile(inFolder , v.getFileName());
                if(file.exists()&&file.isFile()){
                    String name = file.getName();
                    String path = file.getPath();
                    log.info(StringUtil.changeForLog("-----file name is " + name + "====> file path is ==>" + path));
                    try (InputStream is = newInputStream(file.toPath());
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
                            log.info(StringUtil.changeForLog(s+" not equals "+name));
                            v.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
                            try {
                                applicationClient.updateProcessFileTrack(v);
                            }catch (Exception e){
                                log.info("error updateProcessFileTrack");
                            }
                            continue;
                        }
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                        continue;
                    }
                    /**************/
                    String refId = v.getRefId();
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
                        boolean aBoolean=download(name,refId,submissionId);
                        if(aBoolean){
                            log.info("start remove file start");
                            moveFile(file);
                            log.info("update file track start");
                            v.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_SEND_TSAK_SUCCESS);
                            applicationClient.updateProcessFileTrack(v);
                        }

                        //save success
                    }catch (Exception e){
                        //save bad
                        log.error(e.getMessage(),e);
                        continue;
                    }
                } else {
                    v.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
                    try {
                        applicationClient.updateProcessFileTrack(v);
                    }catch (Exception e){
                        log.info("error updateProcessFileTrack");
                    }
                }
            }
        }
        return true;
    }

    private void moveFile(File file) {
        if (!file.exists()) {
            List<String> ipAddrs = ServicesSysteminfo.getInstance().getAddressesByServiceName("hcsa-licence-web");
            if (ipAddrs != null && ipAddrs.size() > 1) {
                String localIp = MiscUtil.getLocalHostExactAddress();
                log.info(StringUtil.changeForLog("Local Ip is ==>" + localIp));
                for (String ip : ipAddrs) {
                    if (localIp.equals(ip)) {
                        continue;
                    }
                    String port = ConfigHelper.getString("server.port", "8080");
                    StringBuilder apiUrl = new StringBuilder("http://");
                    apiUrl.append(ip).append(':').append(port).append("/hcsa-licence-web/moveFile");
                    log.info("Request URL ==> {}", apiUrl);
                    RestTemplate restTemplate = new RestTemplate();
                    try {
                        HttpHeaders header = new HttpHeaders();
                        header.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity entity = new HttpEntity<>(file.getName(), header);
                        log.info(StringUtil.changeForLog("file name ==> " + file.getName()));
                        restTemplate.exchange(apiUrl.toString(), HttpMethod.POST, entity, String.class);
                    } catch (Throwable e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            return;
        }
        String name = file.getName();
        log.info(StringUtil.changeForLog("file name is  {}"+name));
        File outFile = MiscUtil.generateFile(sharedPath+File.separator+"move", name);
        try (OutputStream fileOutputStream = newOutputStream(outFile.toPath());
             InputStream fileInputStream = newInputStream(file.toPath())) {
            int count;
            byte []size=new byte[1024];
            count= fileInputStream.read(size);
            while(count!=-1){
                fileOutputStream.write(size,0,count);
                count= fileInputStream.read(size);
            }
        }catch (Exception e){

            log.error(e.getMessage(),e);

            return;
        }

        MiscUtil.deleteFile(file);

    }


    private void zipFile( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos,String fileName
            ,String groupPath)  {


        try {
            if(!zipEntry.getName().endsWith(File.separator)){

                String substring = zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator));
                String s1=sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+File.separator+groupPath+File.separator+substring;
                File file =MiscUtil.generateFile(s1);
                if(!file.exists()){
                    file.mkdirs();
                }
                log.info(StringUtil.changeForLog(file.getPath()+"-----zipFile---------"));
                String s=sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+File.separator+groupPath+File.separator+zipEntry.getName();
                File outFile = MiscUtil.generateFile(s);
                os= newOutputStream(outFile.toPath());
                bos=new BufferedOutputStream(os);
                InputStream is=zipFile.getInputStream(zipEntry);
                bis=new BufferedInputStream(is);
                cos=new CheckedInputStream(bis,new CRC32());
                byte []b=new byte[1024];
                int count ;
                count=cos.read(b);
                while(count!=-1){
                    bos.write(b,0,count);
                    count=cos.read(b);
                }

            }else {
                log.info(StringUtil.changeForLog(zipEntry.getName()+"------zipEntry.getName()------"));
                String s=sharedPath + File.separator + AppServicesConsts.COMPRESS + File.separator + fileName + File.separator + groupPath + File.separator + zipEntry.getName();
                if(s.endsWith(File.separator)){
                    s=s.substring(0,s.length()-1);
                }
                File file = MiscUtil.generateFile(s);
                file.mkdirs();
                log.info(StringUtil.changeForLog(file.getPath()+"-----else  zipFile-----"));

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

    public Boolean  download( String fileName
            ,String groupPath,String submissionId)  throws Exception {

        boolean flag=Boolean.FALSE;

        File file =MiscUtil.generateFile(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+
                File.separator+groupPath+File.separator+AppServicesConsts.FILE_NAME,groupPath);
        log.info(StringUtil.changeForLog(file.getPath()+"**********************"));
        if(!file.exists()){
            file.mkdirs();
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            log.info(StringUtil.changeForLog(files.length+"FILE_FORMAT --files.length______"));
            for(File  filzz:files){
                if(filzz.isFile() &&filzz.getName().endsWith(AppServicesConsts.FILE_FORMAT)){
                    InputStream  fileInputStream = null;
                    try{
                        fileInputStream= newInputStream(filzz.toPath());
                        ByteArrayOutputStream by=new ByteArrayOutputStream();
                        int count;
                        byte [] size=new byte[1024];
                        count=fileInputStream.read(size);
                        while(count!=-1){
                            by.write(size,0,count);
                            count= fileInputStream.read(size);
                        }
                        Long l = System.currentTimeMillis();
                        Boolean aBoolean = fileToDto(by.toString());
                        if(Boolean.TRUE.equals(aBoolean)){
                            saveFileRepo( fileName,groupPath,submissionId,l);
                            flag= true;
                        }

                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }finally {
                        if(fileInputStream !=null){
                            fileInputStream.close();
                        }
                    }

                }
            }
        }


        return flag;
    }

    private Boolean fileToDto(String str)
    {
        ApplicationListFileDto applicationListDto = JsonUtil.parseToObject(str, ApplicationListFileDto.class);

        return applicationClient.saveFeData(applicationListDto).getStatusCode()==200;

    }

    /*
     *
     * save file to fileRepro*/
    private void saveFileRepo(String fileNames,String groupPath,String submissionId,Long l){
        File file =MiscUtil.generateFile(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileNames+File.separator+groupPath+File.separator+"folder"+File.separator+groupPath,"files");
        if(!file.exists()){
            file.mkdirs();
        }
        log.info(StringUtil.changeForLog(file.getPath()+"file path*************"));
        List<FileRepoDto> fileRepoDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto intranet = AuditTrailHelper.getCurrentAuditTrailDto();
        if(file.isDirectory()){
            File[] files = file.listFiles();
            log.info(StringUtil.changeForLog(files.length+"files.length------"));
            FileRepoEventDto eventDto = new FileRepoEventDto();
            boolean flag=false;
            for(File f:files){
                if(f.isFile()){
                    try {
                        FileRepoDto fileRepoDto = new FileRepoDto();
                        String name = f.getName();//file_id
                        fileRepoDto.setId(name);
                        fileRepoDto.setAuditTrailDto(intranet);
                        //not use generateFile function.this have floder name have dian
                        fileRepoDto.setFileName(name);
                        fileRepoDto.setRelativePath(AppServicesConsts.COMPRESS+File.separator+fileNames+
                                File.separator+groupPath+File.separator+"folder"+File.separator+groupPath+File.separator+"files");
                        fileRepoDtos.add(fileRepoDto);
                        eventDto.setFileRepoList(fileRepoDtos);
                        flag=true;
                        log.info(StringUtil.changeForLog(f.getPath()+"file path------"));
                        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(fileRepoDto)+"fileRepoDto------"));
                    }catch (Exception e){
                        log.info(StringUtil.changeForLog(e+e.getMessage()+"--------error- save file"));
                        continue;
                    }

                }

            }
            if(flag){
                eventDto.setEventRefNo(l.toString());
                eventDto.setAuditTrailDto(intranet);
                eventBusHelper.submitAsyncRequest(eventDto, submissionId, EventBusConsts.SERVICE_NAME_FILE_REPO,
                        EventBusConsts.OPERATION_SAVE_GROUP_APPLICATION, l.toString(), null);
            }
        }

    }

}
