package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


/**
 * @author Wenkang
 * @date 2019/11/9 16:09
 */
@Service
@Slf4j
public class LicenceFileDownloadServiceImpl implements LicenceFileDownloadService {
    @Value("${iais.syncFileTracking.shared.path}")
    private     String sharedPath;
    private     String download;
    private     String backups;
    private     String fileFormat=".text";
    private     String compressPath;

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private SystemBeLicClient systemClient;
    @Autowired
    private FileRepoClient fileRepoClient;
    @Autowired
    private OrganizationClient organizationClient;

    @Override
    public void compress(List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList){
        log.info("-------------compress start ---------");
        if(new File(backups).isDirectory()){
            File[] files = new File(backups).listFiles();
            for(File fil:files){
                if(fil.getName().endsWith(".zip")){
                    String name = fil.getName();
                    String path = fil.getPath();
                    HashMap<String,String> map=new HashMap<>();
                    map.put("fileName",name);
                    map.put("filePath",path);

                    ProcessFileTrackDto processFileTrackDto = systemClient.isFileExistence(map).getEntity();
                    if(processFileTrackDto!=null){
                        String s = sharedPath+File.separator+System.currentTimeMillis() + "";
                        CheckedInputStream cos=null;
                        BufferedInputStream bis=null;
                        BufferedOutputStream bos=null;
                        OutputStream os=null;
                        try (ZipFile zipFile=new ZipFile(path);)  {
                            for( Enumeration<? extends ZipEntry> entries = zipFile.entries();entries.hasMoreElements();){
                                ZipEntry zipEntry = entries.nextElement();
                                zipFile(zipEntry,os,bos,zipFile,bis,cos,s);
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

                        }

                        try {

                            this.download(processFileTrackDto,listApplicationDto, requestForInfList,s);
                            //save success
                        }catch (Exception e){
                            //save bad

                            continue;
                        }
                    }
                 /*   if(fil.exists()&&aBoolean){
                        fil.delete();
                    }*/


                }

            }

        }

    }
    //todo  delete file
    @Override
    public List<ApplicationDto> listApplication() {

        List<ApplicationDto> byPathParam =   applicationClient. getApplicationDto().getEntity();
        return byPathParam;
    }

    @Override
    public void requestForInfList(List<ApplicationDto> list) {

        List<ApplicationDto> entity = applicationClient.getRequesForInfList().getEntity();
        list.addAll(entity);

    }

    @Override
    public Boolean changeFeApplicationStatus() {
        int status = applicationClient.updateStatus("AGST002").getStatusCode();
        if(status==200){
            return true;
        }else if(status==500){
            return false;
        }
        return false;
    }

    @Override
    public List<TaskDto> getTasksByRefNo(String refNo) {
        return organizationClient.getTaskByAppNo(refNo).getEntity();

    }

    @Override
    public void delete() {
        download= sharedPath+File.separator+"compress"+File.separator+"folder";
        backups=sharedPath+File.separator+"backups"+File.separator;
        compressPath=sharedPath+File.separator+"compress";
        File file =new File(download);
        File b=new File(backups);
        File c=new File(compressPath);
        if(!c.exists()){
            c.mkdirs();
        }
        if(!b.exists()){
            b.mkdirs();
        }

        if(!file.mkdirs()){
            file.mkdirs();
        }

    }

    @Override
    public Boolean  download( ProcessFileTrackDto processFileTrackDto,List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList,String fileName) {
        FileInputStream fileInputStream=null;
        Boolean flag=false;
        try {
            File file =new File(download);
            if(!file.exists()){
                file.mkdirs();
            }
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

                        Boolean aBoolean = fileToDto(by.toString(), listApplicationDto, requestForInfList);
                        flag=aBoolean;
                      /*  Boolean backups = backups(flag, filzz);*/
                        if(aBoolean){
                            if(processFileTrackDto!=null){
                                changeStatus(processFileTrackDto);

                           /*     Boolean aBoolean1 = changeFeApplicationStatus();*/

                                saveFileRepo();
                            }
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
    /*
    * to update fe data
    * */
    private void changeStatus( ProcessFileTrackDto processFileTrackDto){
      /*  applicationClient.updateStatus().getEntity();*/
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        AuditTrailDto batchJobDto = AuditTrailHelper.getBatchJobDto("INTRANET");
        processFileTrackDto.setAuditTrailDto(batchJobDto);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_COMPLETE);
        systemClient.updateProcessFileTrack(processFileTrackDto);

    }



        private void zipFile( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos,String fileName)  {


            try {
                if(!zipEntry.getName().endsWith(File.separator)){

                    String substring = zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator));
                    File file =new File(compressPath+File.separator+substring);
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




    private Boolean fileToDto(String str,List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList){
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
        if(applicationClient.getDownloadFile(applicationListDto).getStatusCode() == 200){
            log.info("-----------getDownloadFile-------");
            requeOrNew(applicationGroup,application,listApplicationDto,requestForInfList);

        log.info("-------"+listApplicationDto+"----------");
        }

        return applicationClient.getDownloadFile(applicationListDto).getStatusCode() == 200;

    }


    private void requeOrNew( List<ApplicationGroupDto> applicationGroup,List<ApplicationDto> dtoList,List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList) {
        Map<String,List<ApplicationDto>> map=new HashMap<>();
        for (ApplicationGroupDto applicationGroupDto : applicationGroup) {
            List<ApplicationDto> list=new ArrayList<>();
            for (ApplicationDto applicationDto : dtoList) {
                if (applicationGroupDto.getId().equals(applicationDto.getAppGrpId())) {
                    list.add(applicationDto);
                }

            }
            map.put(applicationGroupDto.getId(),list);
        }

        map.forEach((k,v)->{
                         int j=0;
                        for(ApplicationDto applicationDto :v){
                            int i=v.size();
                            if(applicationDto.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING)){
                                j++;
                            }
                            if(applicationDto.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION)){
                                requestForInfList.add(applicationDto);
                            }
                            if(j==i){ listApplicationDto.addAll(v); }

                        }


               });


    }
    /*
    *
    * save file to fileRepro*/
    private void saveFileRepo(){
        boolean aBoolean=false;
        File file =new File(download+File.separator+"files");
        if(!file.exists()){
            file.mkdirs();
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f:files){
                if(f.isFile()){
                    try {
                        StringBuilder fileName=new StringBuilder();
                        String[] split = f.getName().split("@");
                        for(int i=1;i<split.length;i++){
                            fileName.append(split[i]);
                        }
                        FileItem fileItem = null;
                        try {
                            fileItem = new DiskFileItem("selectedFile", Files.probeContentType(f.toPath()),
                                    false, fileName.toString(), (int) f.length(), f.getParentFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            InputStream input = new FileInputStream(f);
                            OutputStream os = fileItem.getOutputStream();
                            IOUtils.copy(input, os);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

                        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto("intranet");
                        FileRepoDto fileRepoDto = new FileRepoDto();
                        fileRepoDto.setId(split[0]);
                        fileRepoDto.setAuditTrailDto(intranet);
                        fileRepoDto.setFileName(fileName.toString());
                        fileRepoDto.setRelativePath(download);
                        aBoolean = fileRepoClient.saveFiles(multipartFile, JsonUtil.parseToJson(fileRepoDto)).hasErrors();

                        if(aBoolean){
                            removeFilePath(f);
                        }
                    }catch (Exception e){
                        continue;
                    }

                }

            }

        }

    }

    /*
    * copy file to other folder
    * */
    private void  removeFilePath(File file){
        File f=new File(download);
        if(!f.exists()){
            f.mkdirs();
        }
        if(file.isFile()){
            String path = file.getPath();
            String name = file.getName();
            FileInputStream fileInputStream=null;
            FileOutputStream fileOutputStream=null;
            try {
                fileInputStream=new FileInputStream(file);
                File newFile=new File(download+File.separator+name);
               fileOutputStream=new FileOutputStream(newFile);
                byte[] length=new byte[1024];
                int count =0;
                count=fileInputStream.read(length);
                while(count!=-1){
                    fileOutputStream.write(length,0,count);
                    count=fileInputStream.read(length);
                }

            }  catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(fileInputStream!=null){
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            if(fileOutputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            }

        }
        if(file.exists()){
            file.delete();
        }

    }

}
