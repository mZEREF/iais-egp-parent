package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GobalRiskAccpetDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssFileDownloadService;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

/**
 * @author fanghao
 * @date 2022/1/25
 */
@Service
@Slf4j
public class VssFileDownloadServiceImpl implements VssFileDownloadService {
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    @Value("${iais.sharedfolder.dsvss.in}")
    private String inSharedPath;

    @Autowired
    private HcsaLicenceClient client;
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
    public boolean decompression() throws Exception {
        log.info("-------------decompression start ---------");
        String inFolder = inSharedPath;
        if (!inFolder.endsWith(File.separator)) {
            inFolder += File.separator;
        }
        String status = DataSubmissionConsts.VSS_NEED_SYSN_BE_STATUS;
        List<VssFileDto> vssFileDtos = client.getListVssDocumentDtoStatus(status).getEntity();
        if(vssFileDtos!=null&&!vssFileDtos.isEmpty()){
            log.info(StringUtil.changeForLog("-----start process file-----, process file size ==>" + vssFileDtos.size()));
            for (VssFileDto v : vssFileDtos) {
                File file = MiscUtil.generateFile(inFolder ,v.getVssDocs().get(0).getTreatmentId()+".zip");
                if(file.exists()&&file.isFile()){
                    String name = file.getName().replace(".zip","");
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
                        if( StringUtil.isEmpty(s)){
                            continue;
                        }
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                        continue;
                    }
                    /**************/
                    String refId = v.getVssDocs().get(0).getTreatmentId();
                    CheckedInputStream cos=null;
                    BufferedInputStream bis=null;
                    BufferedOutputStream bos=null;
                    OutputStream os=null;
                    try (ZipFile zipFile=new ZipFile(path))  {
                        for(Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();){
                            ZipEntry zipEntry = entries.nextElement();
                            zipFile(zipEntry,os,bos,zipFile,bis,cos,name,refId);
                        }

                    } catch (IOException e) {
                        log.error(e.getMessage(),e);
                    }
                    try {
                        download(name,refId);
                        client.updateVssDocumentStatusByTreId(refId,DataSubmissionConsts.VSS_NEED_SYSN_BE_SUCCESS_STATUS);
                    }catch (Exception e){
                        //save bad
                        log.error(e.getMessage(),e);
                        continue;
                    }
                }
            }
        }
        return true;
    }
    public Boolean download(String fileName
            ,String vssTreId)  throws Exception {

        Boolean flag=Boolean.FALSE;

        File file =MiscUtil.generateFile(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+
                File.separator+vssTreId+File.separator+AppServicesConsts.FILE_NAME,vssTreId);
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
                            saveFileRepo(fileName,vssTreId,l);
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
    private void saveFileRepo(String fileNames,String vssTreId,Long l){
        File file =MiscUtil.generateFile(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileNames+File.separator+vssTreId+File.separator+"folder"+File.separator+vssTreId,"files");
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
                                File.separator+vssTreId+File.separator+"folder"+File.separator+vssTreId+File.separator+"files");
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
                eventBusHelper.submitAsyncRequest(eventDto, vssTreId, EventBusConsts.SERVICE_NAME_FILE_REPO,
                        EventBusConsts.OPERATION_SAVE_GROUP_APPLICATION, l.toString(), null);
            }
        }

    }
   /* private Boolean fileToDto(String str,VssFileDto vssFileDto, Long l) {


        return Boolean.TRUE;

    }*/
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
}
