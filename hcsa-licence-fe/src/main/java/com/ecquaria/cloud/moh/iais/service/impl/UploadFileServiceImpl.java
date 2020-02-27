package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepositoryClient;
import com.ecquaria.sz.commons.util.FileUtil;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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

    private String groupId;
    private Boolean flag=true;
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
    private FileRepositoryClient fileRepositoryClient;

    @Override
    public Boolean saveFile(String  str) {
        List<ApplicationListFileDto> parse = parse(str);
        if(parse.isEmpty()){
           return false;
        }
        ApplicationListFileDto applicationListFileDto = parse.get(0);
        List<ApplicationGroupDto> applicationGroup = applicationListFileDto.getApplicationGroup();
        List<AppPremisesCorrelationDto> appPremisesCorrelation = applicationListFileDto.getAppPremisesCorrelation();
        if(appPremisesCorrelation.isEmpty()){
            log.info("appPremisesCorrelation is empty data is not ");
            return false;
        }
        groupId="";
        String s = FileUtil.genMd5FileChecksum(str.getBytes());
        if(!applicationGroup.isEmpty()){
             groupId = applicationGroup.get(0).getId();

        }


        File file=MiscUtil.generateFile(download+File.separator+groupId, s+fileFormat);

        File groupPath=new File(download+File.separator+groupId);

        if(!groupPath.exists()){
            groupPath.mkdirs();
        }

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
            List<ApplicationGroupDto> applicationGroup = applicationListFileDto.getApplicationGroup();

            groupId=applicationGroup.get(0).getId();
            List<AppGrpPrimaryDocDto> appGrpPrimaryDoc = applicationListFileDto.getAppGrpPrimaryDoc();
            List<AppPremisesSpecialDocDto> appPremisesSpecialDocEntities = applicationListFileDto.getAppPremisesSpecialDocEntities();
            appSvcDoc(appSvcDoc,appGrpPrimaryDoc,appPremisesSpecialDocEntities);
        }catch (Exception e){
            log.error("***************** there have a error is "+e+"***************");
            log.error(e.getMessage(),e);
        }

        return    entity;
    }

    @Override
    public String  changeStatus(ApplicationListFileDto applicationListDto) {
        if(flag){
            List<ApplicationGroupDto> applicationGroup = applicationListDto.getApplicationGroup();
            Map<String,List<String>> map =new HashMap<>();
            List<String> oldStatus=new ArrayList<>();
            oldStatus.add(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
            List<String> newStatus=new ArrayList<>();
            newStatus.add(ApplicationConsts.APPLICATION_SUCCESS_ZIP);
            List<String> groupIds=new ArrayList<>();

            for(ApplicationGroupDto every:applicationGroup){
                String id = every.getId();
                groupIds.add(id)  ;
            }
            map.put("oldStatus",oldStatus);
            map.put("newStatus",newStatus);
            map.put("groupIds",groupIds);
             applicationClient.updateStatus(map).getEntity();
         }

        return "";
    }
    @Override
    public boolean compressFile(){
        String compress = compress();
        log.info("-------------compress() end --------------");
        boolean rename = rename(compress);

        deleteFile();
        return rename;
    }
    /*****************compress*********/
/*
*
*
* file id */
    private void appSvcDoc( List<AppSvcDocDto> appSvcDoc, List<AppGrpPrimaryDocDto> appGrpPrimaryDoc,List<AppPremisesSpecialDocDto> appPremisesSpecialDocEntities){
        //if path is not exists create path
        File fileRepPath=new File(download+File.separator+groupId+File.separator+"files");
        if(!fileRepPath.exists()){
            fileRepPath.mkdirs();
        }

        for(AppPremisesSpecialDocDto every:appPremisesSpecialDocEntities){
            byte[] entity = fileRepositoryClient.getFileFormDataBase(every.getFileRepoId()).getEntity();
            File file=MiscUtil.generateFile(download +File.separator+groupId+ File.separator + "files",
                    every.getFileRepoId() + "@" + every.getDocName());
            try (FileOutputStream outputStream=new FileOutputStream(file)) {
                if(entity!=null){
                    outputStream.write(entity);
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }


        for(AppSvcDocDto every:appSvcDoc){
            byte[] entity = fileRepositoryClient.getFileFormDataBase(every.getFileRepoId()).getEntity();
            File file = MiscUtil.generateFile(download +File.separator+groupId+ File.separator + "files",
                    every.getFileRepoId() + "@" + every.getDocName());
            try (FileOutputStream outputStream=new FileOutputStream(file)) {
                if(entity!=null){
                    outputStream.write(entity);
                }

            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }

        }
        for(AppGrpPrimaryDocDto every:appGrpPrimaryDoc){
            byte[] entity = fileRepositoryClient.getFileFormDataBase(every.getFileRepoId()).getEntity();
            File file = MiscUtil.generateFile(download+File.separator+groupId+File.separator+"files",
                    every.getFileRepoId()+"@"+ every.getDocName());
            try (FileOutputStream fileOutputStream=new FileOutputStream(file);) {
                if(entity!=null){
                    fileOutputStream.write(entity);
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    private String compress(){
        log.info("------------ start compress() -----------------------");
        long l=   System.currentTimeMillis();
        try (OutputStream is=new FileOutputStream(backups+File.separator+ l+".zip");
               CheckedOutputStream cos=new CheckedOutputStream(is,new CRC32());
               ZipOutputStream zos=new ZipOutputStream(cos)) {

            log.info("------------zip file name is"+backups+File.separator+ l+".zip"+"--------------------");
            File file = new File(download+File.separator+groupId);
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
            try (
                 BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
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

    private boolean rename(String fileNamesss)  {
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
               try ( FileInputStream is=new FileInputStream(file);
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
                   File curFile = MiscUtil.generateFile(backups, s + ".zip");
                 /*  File curFile = new File(backups, s + ".zip");
                   if (!curFile.exists()){
                       curFile.createNewFile();
                   }*/
                   file.renameTo(curFile);
                   log.info("----------- new zip file name is"+backups+File.separator+s+".zip");
                   String s1 = saveFileName(s+".zip","backups" + File.separator+s+".zip");
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
        return flag;
    }

    private void deleteFile(){
        File file =new File(download);
        File fileRepPath=new File(download+File.separator+groupId);
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
        if("".equals(groupId)){
            processFileTrackDto.setRefId("BE30AB5D-A92A-EA11-BE7D-000C29F371DC");
        }else {
            processFileTrackDto.setRefId(groupId);
        }

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

    @Override
    public  List<ApplicationListFileDto> parse(String str){
        ApplicationListFileDto applicationListDto = JsonUtil.parseToObject(str, ApplicationListFileDto.class);
        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = applicationListDto.getAppPremPhOpenPeriods();
        List<ApplicationGroupDto> applicationGroup = applicationListDto.getApplicationGroup();
        List<AppGrpPersonnelDto> appGrpPersonnel = applicationListDto.getAppGrpPersonnel();
        List<AppGrpPersonnelExtDto> appGrpPersonnelExt = applicationListDto.getAppGrpPersonnelExt();
        List<AppGrpPremisesEntityDto> appGrpPremises = applicationListDto.getAppGrpPremises();
        List<AppGrpPrimaryDocDto> appGrpPrimaryDoc = applicationListDto.getAppGrpPrimaryDoc();
        List<ApplicationDto> application = applicationListDto.getApplication();
        List<AppPremisesCorrelationDto> appPremisesCorrelation = applicationListDto.getAppPremisesCorrelation();
        List<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklEntity = applicationListDto.getAppPremisesSelfDeclChklEntity();
        List<AppSvcDocDto> appSvcDoc = applicationListDto.getAppSvcDoc();
        List<AppSvcKeyPersonnelDto> appSvcKeyPersonnel = applicationListDto.getAppSvcKeyPersonnel();
        List<AppSvcPersonnelDto> appSvcPersonnel = applicationListDto.getAppSvcPersonnel();
        List<AppSvcPremisesScopeDto> appSvcPremisesScope = applicationListDto.getAppSvcPremisesScope();
        List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocation = applicationListDto.getAppSvcPremisesScopeAllocation();
        List<AppPremiseMiscDto> appPremiseMiscEntities = applicationListDto.getAppPremiseMiscEntities();
        List<AppPremisesSpecialDocDto> appPremisesSpecialDocEntities = applicationListDto.getAppPremisesSpecialDocEntities();
        List<AppEditSelectDto> appEditSelects = applicationListDto.getAppEditSelects();


        List<ApplicationListFileDto> applicationListFileDtoList=new ArrayList<>();
        Set<ApplicationGroupDto> applicationGroupDtoSet=new HashSet<>();
        applicationGroupDtoSet.addAll(applicationGroup);
        for(ApplicationGroupDto every :applicationGroupDtoSet){

            Set<String > appGrpIds=new HashSet<>();
            Set<String> appGrpPersonIds=new HashSet<>();
            Set<String> appGrpPersonExtIds=new HashSet<>();
            Set<String> appSvcKeyPersonIds=new HashSet<>();
            Set<String> appSvcPremisesScopeIds=new HashSet<>();

            ApplicationListFileDto applicationListFileDto=new ApplicationListFileDto();
            List<ApplicationGroupDto> groupDtos=new ArrayList<>();
            List<ApplicationDto> applicationDtos=new ArrayList<>();
            Set<ApplicationDto> applicationDtoSet=new HashSet<>();

            List<AppGrpPremisesEntityDto> appGrpPremisesDtos=new ArrayList<>();
            Set<AppGrpPremisesEntityDto> appliGrpPremisesDtoSet=new HashSet<>();

            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos=new ArrayList<>();
            Set<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoSet =new HashSet<>();

            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=new ArrayList<>();
            Set<AppPremisesCorrelationDto> appPremisesCorrelationDtoSet =new HashSet<>();

            List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos=new ArrayList<>();
            Set<AppSvcPremisesScopeDto> appSvcPremisesScopeDtoSet =new HashSet<>();

            List<AppGrpPersonnelDto> appGrpPersonnelDtos=new ArrayList<>();
            Set<AppGrpPersonnelDto> appGrpPersonnelDtoSet =new HashSet<>();

            List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos=new ArrayList<>();
            Set<AppGrpPersonnelExtDto> appGrpPersonnelExtDtoSet=new HashSet<>();

            List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos=new ArrayList<>();
            Set<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtoSet=new HashSet<>();

            List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos=new ArrayList<>();
            Set<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtoSet=new HashSet<>();

            List<AppSvcPersonnelDto >  appSvcPersonnelDtos=new ArrayList<>();
            Set<AppSvcPersonnelDto> appSvcPersonnelDtoSet =new HashSet<>();

            List<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklDtos=new ArrayList<>();
            Set<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklDtoSet =new HashSet<>();

            List<AppSvcDocDto> appSvcDocDtos=new ArrayList<>();
            Set<AppSvcDocDto> appSvcDocDtoSet=new HashSet<>();

            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtoList=new ArrayList<>();
            Set<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtoSet=new HashSet<>();

            List<AppPremiseMiscDto> appPremiseMiscDtoList=new ArrayList<>();
            Set<AppPremiseMiscDto> appPremiseMiscDtoSet=new HashSet<>();

            List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoList=new ArrayList<>();
            Set<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoSet=new HashSet<>();

            List<AppEditSelectDto> appEditSelectDtos=new ArrayList<>();
            Set<AppEditSelectDto> appEditSelectDtoSet=new HashSet<>();

            groupDtos.add(every);
            String groupId = every.getId();
            for(AppGrpPremisesEntityDto appliGrpPremisesDto:appGrpPremises){
                String grpPremisesDtoAppGrpId = appliGrpPremisesDto.getAppGrpId();
                if(groupId.equals(grpPremisesDtoAppGrpId)){
                    appliGrpPremisesDtoSet.add(appliGrpPremisesDto);
                    appGrpIds.add(appliGrpPremisesDto.getId());
                    String appliGrpPremisesDtoId = appliGrpPremisesDto.getId();
                    for(AppPremPhOpenPeriodDto appPremPhOpenPeriodDto :appPremPhOpenPeriodDtos){
                        String premId = appPremPhOpenPeriodDto.getPremId();
                        if(appliGrpPremisesDtoId.equals(premId)){
                            appPremPhOpenPeriodDtoSet.add(appPremPhOpenPeriodDto);
                        }

                    }
                }
            }
            for (AppGrpPersonnelDto appGrpPersonnelDto:appGrpPersonnel){
                String appGrpId = appGrpPersonnelDto.getAppGrpId();
                if(groupId.equals(appGrpId)){
                    appGrpPersonnelDtoSet.add(appGrpPersonnelDto);
                    String appGrpPersonnelDtoId = appGrpPersonnelDto.getId();
                    appGrpPersonIds.add(appGrpPersonnelDtoId);
                    for(AppGrpPersonnelExtDto appGrpPersonnelExtDto: appGrpPersonnelExt){
                        String appGrpPsnId = appGrpPersonnelExtDto.getAppGrpPsnId();
                        if(appGrpPersonnelDtoId.equals(appGrpPsnId)){
                            appGrpPersonnelExtDtoSet.add(appGrpPersonnelExtDto);
                            appGrpPersonExtIds.add(appGrpPersonnelExtDto.getId());
                        }

                    }
                }

            }
            for(ApplicationDto applicationDto:application){
                String applicationDtoId = applicationDto.getId();
                String appGrpId = applicationDto.getAppGrpId();
                if(groupId.equals(appGrpId)){
                    applicationDtoSet.add(applicationDto);
                    for(AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelation){
                        String applicationId = appPremisesCorrelationDto.getApplicationId();
                        String appGrpPremId = appPremisesCorrelationDto.getAppGrpPremId();
                        String premisesCorrelationDtoId = appPremisesCorrelationDto.getId();

                        if(applicationDtoId.equals(applicationId) && appGrpIds.contains(appGrpPremId)){
                            appPremisesCorrelationDtoSet.add(appPremisesCorrelationDto);
                            for (AppSvcPremisesScopeDto appSvcPremisesScopeDto:appSvcPremisesScope){
                                String appPremCorreId = appSvcPremisesScopeDto.getAppPremCorreId();

                                if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                    appSvcPremisesScopeDtoSet.add(appSvcPremisesScopeDto);
                                    appSvcPremisesScopeIds.add(appSvcPremisesScopeDto.getId());
                                }

                            }

                            for (AppPremisesSelfDeclChklDto appPremisesSelfDeclChklDto :appPremisesSelfDeclChklEntity){
                                String appPremCorreId = appPremisesSelfDeclChklDto.getAppPremCorreId();
                                if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                    appPremisesSelfDeclChklDtoSet.add(appPremisesSelfDeclChklDto);

                                }
                            }

                            for(AppSvcDocDto appSvcDocDto:appSvcDoc){
                                String appPremCorreId = appSvcDocDto.getAppPremCorreId();
                                if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                    appSvcDocDtoSet.add(appSvcDocDto);
                                }

                            }

                            for(AppPremiseMiscDto appPremiseMiscDto:appPremiseMiscEntities){
                                String appPremCorreId = appPremiseMiscDto.getAppPremCorreId();
                                if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                    appPremiseMiscDtoSet.add(appPremiseMiscDto);
                                }
                            }

                            for(AppPremisesSpecialDocDto appPremisesSpecialDocDto:appPremisesSpecialDocEntities){
                                String appPremCorreId = appPremisesSpecialDocDto.getAppPremCorreId();
                                if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                    appPremisesSpecialDocDtoSet.add(appPremisesSpecialDocDto);
                                }
                            }
                        }
                    }

                    for(AppSvcKeyPersonnelDto appSvcKeyPersonnelDto:appSvcKeyPersonnel){
                        String applicationId = appSvcKeyPersonnelDto.getApplicationId();
                        String appGrpPsnId = appSvcKeyPersonnelDto.getAppGrpPsnId();
                        if(applicationDtoId.equals(applicationId) &&appGrpPersonIds.contains(appGrpPsnId)){
                            appSvcKeyPersonnelDtoSet.add(appSvcKeyPersonnelDto);
                            appSvcKeyPersonIds.add(appSvcKeyPersonnelDto.getId());
                        }

                    }
                    for(AppSvcPremisesScopeAllocationDto appSvcPremisesScopeAllocationDto:appSvcPremisesScopeAllocation){
                        String applicationId = appSvcPremisesScopeAllocationDto.getApplicationId();
                        String appSvcKeyPsnId = appSvcPremisesScopeAllocationDto.getAppSvcKeyPsnId();
                        String appSvcPremScopeId = appSvcPremisesScopeAllocationDto.getAppSvcPremScopeId();
                        if(applicationDtoId.equals(applicationId)&&appSvcKeyPersonIds.contains(appSvcKeyPsnId)
                                &&appSvcPremisesScopeIds.contains(appSvcPremScopeId)){
                            appSvcPremisesScopeAllocationDtoSet.add(appSvcPremisesScopeAllocationDto);

                        }
                    }
                    for(AppSvcPersonnelDto appSvcPersonnelDto:appSvcPersonnel){
                        String applicationId = appSvcPersonnelDto.getApplicationId();
                        if(applicationDtoId.equals(applicationId)){
                            appSvcPersonnelDtoSet.add(appSvcPersonnelDto);

                        }
                    }
                    if(appEditSelects!=null){
                        for(AppEditSelectDto appEditSelectDto:appEditSelects){
                            String applicationId = appEditSelectDto.getApplicationId();
                            String editType = appEditSelectDto.getEditType();
                            if(applicationDto.getId().equals(applicationId)&&ApplicationConsts.APPLICATION_EDIT_TYPE_RFC.equals(editType)){
                                appEditSelectDtoSet.add(appEditSelectDto);

                            }
                        }
                    }

                }


            }

            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDoc){
                String appGrpId = appGrpPrimaryDocDto.getAppGrpId();
                if(groupId.equals(appGrpId)){
                    appGrpPrimaryDocDtoSet.add(appGrpPrimaryDocDto);

                }
            }


            applicationDtos.addAll(applicationDtoSet);
            appGrpPremisesDtos.addAll(appliGrpPremisesDtoSet);
            appGrpPrimaryDocDtos.addAll(appGrpPrimaryDocDtoSet);
            appPremisesCorrelationDtos.addAll(appPremisesCorrelationDtoSet);
            appSvcPremisesScopeDtos.addAll(appSvcPremisesScopeDtoSet);
            appGrpPersonnelDtos.addAll(appGrpPersonnelDtoSet);
            appGrpPersonnelExtDtos.addAll(appGrpPersonnelExtDtoSet);
            appSvcKeyPersonnelDtos.addAll(appSvcKeyPersonnelDtoSet);
            appSvcPremisesScopeAllocationDtos.addAll(appSvcPremisesScopeAllocationDtoSet);
            appSvcPersonnelDtos.addAll(appSvcPersonnelDtoSet);
            appPremisesSelfDeclChklDtos.addAll(appPremisesSelfDeclChklDtoSet);
            appSvcDocDtos.addAll(appSvcDocDtoSet);
            appPremPhOpenPeriodDtoList.addAll(appPremPhOpenPeriodDtoSet);
            appPremiseMiscDtoList.addAll(appPremiseMiscDtoSet);
            appPremisesSpecialDocDtoList.addAll(appPremisesSpecialDocDtoSet);
            appEditSelectDtos.addAll(appEditSelectDtoSet);

            applicationListFileDto.setApplicationGroup(groupDtos);
            applicationListFileDto.setApplication( applicationDtos);
            applicationListFileDto.setAppGrpPremises(appGrpPremisesDtos);
            applicationListFileDto.setAppGrpPrimaryDoc (appGrpPrimaryDocDtos);

            applicationListFileDto.setAppPremPhOpenPeriods(appPremPhOpenPeriodDtoList);

            applicationListFileDto.setAppPremisesCorrelation (appPremisesCorrelationDtos);
            applicationListFileDto.setAppSvcPremisesScope  (appSvcPremisesScopeDtos);
            applicationListFileDto.setAppGrpPersonnel (appGrpPersonnelDtos);
            applicationListFileDto.setAppGrpPersonnelExt (appGrpPersonnelExtDtos);
            applicationListFileDto.setAppSvcKeyPersonnel (appSvcKeyPersonnelDtos);
            applicationListFileDto.setAppSvcPremisesScopeAllocation (appSvcPremisesScopeAllocationDtos);
            applicationListFileDto.setAppSvcPersonnel (appSvcPersonnelDtos);
            applicationListFileDto.setAppPremisesSelfDeclChklEntity(appPremisesSelfDeclChklDtos);
            applicationListFileDto.setAppSvcDoc(appSvcDocDtos);
            applicationListFileDto.setAppPremiseMiscEntities(appPremiseMiscDtoList);
            applicationListFileDto.setAppPremisesSpecialDocEntities(appPremisesSpecialDocDtoList);
            applicationListFileDto.setAppEditSelects(appEditSelectDtos);

            applicationListFileDtoList.add(applicationListFileDto);
        }
        return applicationListFileDtoList;

    }

    private static void parseApplication() {

    }
}
