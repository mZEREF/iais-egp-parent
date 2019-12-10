package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppliGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;

import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * @author Wenkang
 * @date 2019/11/9 16:09
 */
@Service
@Slf4j
public class LicenceFileDownloadServiceImpl implements LicenceFileDownloadService {
    @Value("iais.syncFileTracking.download")
    private    String download;

    @Value("iais.syncFileTracking.backups")
    private     String backups;

    @Value("iais.syncFileTracking.fileFormat")
    private     String fileFormat;

    @Value("iais.syncFileTracking.compressPath")
    private     String compressPath;

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private SystemBeLicClient systemClient;
    @Override
                public void compress(){
        if(new File(backups).isDirectory()){
            File[] files = new File(backups).listFiles();
            for(File fil:files){
                if(fil.getName().endsWith(".zip")){
                    String name = fil.getName();
                    String path = fil.getPath();
                    HashMap<String,String> map=new HashMap<>();
                    map.put("fileName",name);
                    map.put("filePath",path);

                    Boolean aBoolean = systemClient.isFileExistence(map).getEntity();
                    if(aBoolean){
                        ZipFile zipFile=null;
                        CheckedInputStream cos=null;
                        BufferedInputStream bis=null;
                        BufferedOutputStream bos=null;
                        OutputStream os=null;
                        try {
                            zipFile =new ZipFile(path);
                            for( Enumeration<? extends ZipEntry> entries = zipFile.entries();entries.hasMoreElements();){
                                ZipEntry zipEntry = entries.nextElement();
                                zipFile(zipEntry,os,bos,zipFile,bis,cos);
                            }

                        } catch (IOException e) {
                            log.error(e.getMessage(),e);
                        }
                        finally {
                            if(cos!=null){
                                try {
                                    cos.close();
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                }
                            }

                            if(bis!=null){
                                try {
                                    bis.close();
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                }
                            }
                            if(bos!=null){
                                try {
                                    bos.close();
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                }
                            }
                            if(os!=null) {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                }
                            }
                            if(zipFile!=null){
                                try {
                                    zipFile.close();
                                } catch (IOException e) {
                                    log.error(e.getMessage());
                                }
                            }
                        }
                    }
                 /*   if(fil.exists()&&aBoolean){
                        fil.delete();
                    }*/

                }
            }

        }

    }

    @Override
    public List<ApplicationDto> listApplication() {

        List<ApplicationDto> byPathParam =   applicationClient. getApplicationDto().getEntity();
        return byPathParam;
    }

    @Override
    public void delete() {
        File file =new File(download);
        deleteFile(file);
    }

    @Override
    public Boolean  download() {
        FileInputStream fileInputStream=null;
        Boolean flag=false;
        try {
            File file =new File(download);
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(File  filzz:files){
                    if(filzz.isFile() &&filzz.getName().endsWith(fileFormat)){
                       fileInputStream =new FileInputStream(filzz);
                        ByteArrayOutputStream by=new ByteArrayOutputStream();
                        int count=0;
                        byte [] size=new byte[1024];
                        count=fileInputStream.read(size);
                        while(count!=-1){
                            by.write(size,0,count);
                            count= fileInputStream.read(size);
                        }

                        Boolean aBoolean = fileToDto(by.toString());
                        flag=aBoolean;
                        Boolean backups = backups(flag, filzz);
                        if(backups&&filzz.exists()){
                            filzz.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        finally {
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }

        }

        return flag;
    }

    /*************************/
        private void zipFile( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos)  {
            try {
                if(!zipEntry.getName().endsWith(File.separator)){
                    File file =new File(compressPath+File.separator+zipEntry.getName().substring(0,zipEntry.getName().lastIndexOf(File.separator)));
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    os=new FileOutputStream(compressPath+File.separator+zipEntry.getName());
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

                    new File(compressPath+File.separator+zipEntry.getName()).mkdirs();
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




    private Boolean  backups(Boolean aBoolean ,File file){
            Boolean flag=false;
        if(aBoolean){
            if(!new File(backups).exists()){
                new File(backups).mkdirs();
            }
            File newFile=new File(backups+File.separator+file.getName());
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                 fileInputStream=new FileInputStream(file);
               fileOutputStream=new FileOutputStream(newFile);
                int cout =0;
                byte [] size=new byte[1024];
               cout= fileInputStream.read(size);
               while(cout!=-1){
                   fileOutputStream.write(size,0,cout);
                   cout= fileInputStream.read(size);
               }

                flag=true;

            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
            finally {
                if (fileInputStream!=null){
                    try {
                        fileInputStream.close();
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
        return flag;
    }


    private void deleteFile(File file){
         if(file.isDirectory()){
             File[] files = file.listFiles();
             for(File f:files){
                 deleteFile(f);
             }
         }else{
             if(file.exists()&&file.getName().endsWith(fileFormat)){
                 file.delete();
             }
         }
    }

    private Boolean fileToDto(String str){
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto("INTRANET");
        ApplicationListFileDto applicationListDto = JsonUtil.parseToObject(str, ApplicationListFileDto.class);
        List<AppGrpPersonnelDto> appGrpPersonnel = applicationListDto.getAppGrpPersonnel();
        for(AppGrpPersonnelDto every:appGrpPersonnel){
            every.setAuditTrailDto(intranet);
        }
        List<AppGrpPersonnelExtDto> appGrpPersonnelExt = applicationListDto.getAppGrpPersonnelExt();
        for(AppGrpPersonnelExtDto every:appGrpPersonnelExt){
            every.setAuditTrailDto(intranet);
        }
        List<AppliGrpPremisesDto> appGrpPremises = applicationListDto.getAppGrpPremises();
        for(AppliGrpPremisesDto every:appGrpPremises){
            every.setAuditTrailDto(intranet);
        }
        List<AppGrpPrimaryDocDto> appGrpPrimaryDoc = applicationListDto.getAppGrpPrimaryDoc();
        for(AppGrpPrimaryDocDto every:appGrpPrimaryDoc){
            every.setAuditTrailDto(intranet);
        }
        List<ApplicationDto> application = applicationListDto.getApplication();
        for(ApplicationDto every:application){
            every.setAuditTrailDto(intranet);
        }
        List<ApplicationGroupDto> applicationGroup = applicationListDto.getApplicationGroup();
        for(ApplicationGroupDto every:applicationGroup){
            every.setAuditTrailDto(intranet);
        }
        List<AppPremisesCorrelationDto> appPremisesCorrelation = applicationListDto.getAppPremisesCorrelation();
        for(AppPremisesCorrelationDto every:appPremisesCorrelation){
            every.setAuditTrailDto(intranet);
        }
        List<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklEntity = applicationListDto.getAppPremisesSelfDeclChklEntity();
        for(AppPremisesSelfDeclChklDto every:appPremisesSelfDeclChklEntity){
            every.setAuditTrailDto(intranet);
        }
        List<AppSvcDocDto> appSvcDoc = applicationListDto.getAppSvcDoc();
        for(AppSvcDocDto every:appSvcDoc){
            every.setAuditTrailDto(intranet);
        }
        List<AppSvcKeyPersonnelDto> appSvcKeyPersonnel = applicationListDto.getAppSvcKeyPersonnel();
        for(AppSvcKeyPersonnelDto every:appSvcKeyPersonnel){
            every.setAuditTrailDto(intranet);
        }
        List<AppSvcPersonnelDto> appSvcPersonnel = applicationListDto.getAppSvcPersonnel();
        for(AppSvcPersonnelDto every:appSvcPersonnel){
            every.setAuditTrailDto(intranet);
        }
        List<AppSvcPremisesScopeDto> appSvcPremisesScope = applicationListDto.getAppSvcPremisesScope();
        for(AppSvcPremisesScopeDto every:appSvcPremisesScope){
            every.setAuditTrailDto(intranet);
        }
        List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocation = applicationListDto.getAppSvcPremisesScopeAllocation();
        for(AppSvcPremisesScopeAllocationDto every:appSvcPremisesScopeAllocation){
            every.setAuditTrailDto(intranet);
        }
        applicationListDto.setAuditTrailDto(intranet);
       return applicationClient.getDownloadFile(applicationListDto).getStatusCode() == 200;

    }
}
