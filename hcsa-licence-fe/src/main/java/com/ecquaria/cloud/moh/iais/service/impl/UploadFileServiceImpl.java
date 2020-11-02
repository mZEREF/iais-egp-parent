package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
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
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepositoryClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Wenkang
 * @date 2019/11/6 20:56
 */
@Service
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    @Value("${iais.sharedfolder.application.out}")
    private String sharedOutPath;

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

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;
    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;
    @Override
    public String saveFile(ApplicationListFileDto applicationListFileDto ) {

        String str = JsonUtil.parseToJson(applicationListFileDto);
        List<ApplicationGroupDto> applicationGroup = applicationListFileDto.getApplicationGroup();
        List<AppPremisesCorrelationDto> appPremisesCorrelation = applicationListFileDto.getAppPremisesCorrelation();
        if(appPremisesCorrelation.isEmpty()){
            log.info("appPremisesCorrelation is empty data is not ");
            return null;
        }
        String groupId="";
        String s = "";
        try{
          s = FileUtil.genMd5FileChecksum(str.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        if(!applicationGroup.isEmpty()){
             groupId = applicationGroup.get(0).getId();
        }
        File file = MiscUtil.generateFile(sharedPath+ AppServicesConsts.FILE_NAME+File.separator+groupId, s+AppServicesConsts.FILE_FORMAT);
        try (OutputStream fileOutputStream  = Files.newOutputStream(file.toPath());) {
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
        return groupId;
    }

    @Override
    public String getData() {
        List<String> list=new ArrayList<>();
        String entity = applicationClient.fileAll(list).getEntity();
        return entity;
    }

    @Override
    public String  changeStatus(ApplicationListFileDto applicationListDto, Map<String,List<String>> map) {
            List<ApplicationGroupDto> applicationGroup = applicationListDto.getApplicationGroup();

            List<String> groupIds=IaisCommonUtils.genNewArrayList();

            for(ApplicationGroupDto every:applicationGroup){
                String id = every.getId();
                groupIds.add(id)  ;
            }

            map.put("groupIds",groupIds);
            applicationClient.updateStatus(map).getEntity();


        return "";
    }
    @Override
    public String compressFile(String grpId){
        String compress = compress(grpId);
        log.info("-------------compress() end --------------");
        return compress;
    }
    /*****************compress*********/
/*
*
*
* file id */
    private void appSvcDoc( List<AppSvcDocDto> appSvcDoc, List<AppGrpPrimaryDocDto> appGrpPrimaryDoc,List<AppPremisesSpecialDocDto> appPremisesSpecialDocEntities,String groupId){
        //if path is not exists create path
        File fileRepPath=new File(sharedPath+ AppServicesConsts.FILE_NAME+File.separator+groupId+File.separator+"files");
        if(!fileRepPath.exists()){
            fileRepPath.mkdirs();
        }
        for(AppPremisesSpecialDocDto every:appPremisesSpecialDocEntities){
            getFileRep(every.getFileRepoId(),every.getDocName(),groupId);
        }
        for(AppSvcDocDto every:appSvcDoc){
            getFileRep(every.getFileRepoId(),every.getDocName(),groupId);
        }
        for(AppGrpPrimaryDocDto every:appGrpPrimaryDoc){
            getFileRep(every.getFileRepoId(),every.getDocName(),groupId);
        }
    }

    private String compress(String groupId){
        log.info("------------ start compress() -----------------------");
        long l =   System.currentTimeMillis();
        String outFolder = sharedOutPath;
        if (!outFolder.endsWith(File.separator)) {
            outFolder += File.separator;
        }
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(outFolder + l + AppServicesConsts.ZIP_NAME));
                CheckedOutputStream cos=new CheckedOutputStream(outputStream,new CRC32());
               ZipOutputStream zos=new ZipOutputStream(cos)) {

            log.info(StringUtil.changeForLog("------------zip file name is"+ outFolder + l+".zip"+"--------------------"));
            File file = new File(sharedPath + AppServicesConsts.FILE_NAME + File.separator + groupId);

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
            }
        }

    }

    @Override
    public boolean renameAndSave(String fileNamesss,String groupId)  {
        log.info("--------------rename start ---------------------");
        boolean flag = true;
        String outFolder = sharedOutPath;
        if (!outFolder.endsWith(File.separator)) {
            outFolder += File.separator;
        }
        File zipFile =new File(sharedOutPath);
        MiscUtil.checkDirs(zipFile);
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
                   String string = eicGateway(s + AppServicesConsts.ZIP_NAME, s + AppServicesConsts.ZIP_NAME, groupId);
           /*        String s1 = saveFileName(s+AppServicesConsts.ZIP_NAME,AppServicesConsts.BACKUPS + File.separator+s+AppServicesConsts.ZIP_NAME,groupId);*/
                   log.info(StringUtil.changeForLog("----"+string));
                   if(!string.equals("SUCCESS")){
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

    private String eicGateway(String fileName ,String filePath,String groupId){
        ProcessFileTrackDto processFileTrackDto =new ProcessFileTrackDto();
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        processFileTrackDto.setFileName(fileName);
        processFileTrackDto.setFilePath(filePath);
        processFileTrackDto.setRefId(groupId);
        AuditTrailDto intenet = AuditTrailHelper.getCurrentAuditTrailDto();
        processFileTrackDto.setAuditTrailDto(intenet);
        EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, UploadFileServiceImpl.class.getName(),
                "saveFileName", currentApp + "-" + currentDomain,
                ProcessFileTrackDto.class.getName(), JsonUtil.parseToJson(processFileTrackDto));
        FeignResponseEntity<EicRequestTrackingDto> fetchResult = eicRequestTrackingHelper.getAppEicClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
        if (fetchResult != null && HttpStatus.SC_OK == fetchResult.getStatusCode()) {
            log.info(StringUtil.changeForLog("------"+JsonUtil.parseToJson(fetchResult)));
            EicRequestTrackingDto entity = fetchResult.getEntity();
            if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(entity.getStatus())){
                String string = saveFileName(fileName, filePath, groupId);
                entity.setProcessNum(1);
                Date now = new Date();
                entity.setFirstActionAt(now);
                entity.setLastActionAt(now);
                entity.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                entity.setAuditTrailDto(intenet);
                eicRequestTrackingHelper.getAppEicClient().saveEicTrack(entity);
                return string;
            }
        } else {
            log.info(StringUtil.changeForLog("------ null----"));
        }
        return "FAIL";
    }

    private String saveFileName(String fileName ,String filePath,String groupId){
        ProcessFileTrackDto processFileTrackDto =new ProcessFileTrackDto();
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        processFileTrackDto.setFileName(fileName);
        processFileTrackDto.setFilePath(filePath);
        processFileTrackDto.setRefId(groupId);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
        AuditTrailDto intenet = AuditTrailHelper.getCurrentAuditTrailDto();
        processFileTrackDto.setAuditTrailDto(intenet);
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(processFileTrackDto)+"processFileTrackDto"));
        String s="FAIL";
        try {
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            s = eicGatewayClient.saveFileApplication(processFileTrackDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.info(StringUtil.changeForLog("have error-------" +s));
            return s;
        }
        return s;
    }

    private void deleteFile(String groupId){
        File fileRepPath=new File(sharedPath+AppServicesConsts.FILE_NAME+File.separator+groupId);
        MiscUtil.checkDirs(fileRepPath);
        if(fileRepPath.isDirectory()){
            File[] files = fileRepPath.listFiles();
            for(File f :files){
                if(f.exists()&&f.isFile()){
                    MiscUtil.deleteFile(f);
                }
            }
        }

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
        List<AppGroupMiscDto> appGroupMiscs = applicationListDto.getAppGroupMiscs();
        List<AppFeeDetailsDto> appFeeDetails = applicationListDto.getAppFeeDetails();
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = applicationListDto.getAppPremisesOperationalUnits();

        List<ApplicationListFileDto> applicationListFileDtoList=IaisCommonUtils.genNewArrayList();
        for(ApplicationGroupDto every :applicationGroup){

            Set<String> appliGrpPremisesIds=IaisCommonUtils.genNewHashSet();
            Set<String> appSvcKeyPersonIds=IaisCommonUtils.genNewHashSet();
            Set<String> appSvcPremisesScopeIds=IaisCommonUtils.genNewHashSet();

            ApplicationListFileDto applicationListFileDto=new ApplicationListFileDto();
            List<ApplicationGroupDto> groupDtos=IaisCommonUtils.genNewArrayList();
            List<ApplicationDto> applicationDtos=IaisCommonUtils.genNewArrayList();

            List<AppGrpPremisesEntityDto> appGrpPremisesDtos=IaisCommonUtils.genNewArrayList();

            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos=IaisCommonUtils.genNewArrayList();

            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=IaisCommonUtils.genNewArrayList();

            List<AppSvcPremisesScopeDto> appSvcPremisesScopeDtos=IaisCommonUtils.genNewArrayList();

            List<AppGrpPersonnelDto> appGrpPersonnelDtos=IaisCommonUtils.genNewArrayList();

            List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos=IaisCommonUtils.genNewArrayList();

            List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos=IaisCommonUtils.genNewArrayList();


            List<AppSvcPremisesScopeAllocationDto> appSvcPremisesScopeAllocationDtos=IaisCommonUtils.genNewArrayList();

            List<AppSvcPersonnelDto >  appSvcPersonnelDtos=IaisCommonUtils.genNewArrayList();

            List<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklDtos=IaisCommonUtils.genNewArrayList();

            List<AppSvcDocDto> appSvcDocDtos=IaisCommonUtils.genNewArrayList();

            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtoList=IaisCommonUtils.genNewArrayList();

            List<AppPremiseMiscDto> appPremiseMiscDtoList=IaisCommonUtils.genNewArrayList();

            List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoList=IaisCommonUtils.genNewArrayList();

            List<AppEditSelectDto> appEditSelectDtos=IaisCommonUtils.genNewArrayList();
            List<AppGroupMiscDto> appGroupMiscDtos=IaisCommonUtils.genNewArrayList();
            List<AppFeeDetailsDto> appFeeDetailsDtos =IaisCommonUtils.genNewArrayList();
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtoList=IaisCommonUtils.genNewArrayList();
            groupDtos.add(every);
            String groupId = every.getId();
            for(AppGrpPremisesEntityDto appliGrpPremisesDto:appGrpPremises){
                String grpPremisesDtoAppGrpId = appliGrpPremisesDto.getAppGrpId();
                if(groupId.equals(grpPremisesDtoAppGrpId)){
                    appGrpPremisesDtos.add(appliGrpPremisesDto);
                    appliGrpPremisesIds.add(appliGrpPremisesDto.getId());
                    String appliGrpPremisesDtoId = appliGrpPremisesDto.getId();
                    for(AppPremPhOpenPeriodDto appPremPhOpenPeriodDto :appPremPhOpenPeriodDtos){
                        String premId = appPremPhOpenPeriodDto.getPremId();
                        if(appliGrpPremisesDtoId.equals(premId)){
                            appPremPhOpenPeriodDtoList.add(appPremPhOpenPeriodDto);
                        }
                    }
                    for(AppPremisesOperationalUnitDto appPremisesOperationalUnitDto : appPremisesOperationalUnitDtos){
                        String premisesId = appPremisesOperationalUnitDto.getPremisesId();
                        if(appliGrpPremisesDtoId.equals(premisesId)){
                            appPremisesOperationalUnitDtoList.add(appPremisesOperationalUnitDto);
                        }
                    }
                }
            }
            if(appGroupMiscs!=null){
                for(AppGroupMiscDto appGroupMiscDto : appGroupMiscs){
                    if(appGroupMiscDto.getAppGrpId().equals(groupId)){
                        appGroupMiscDtos.add(appGroupMiscDto);
                    }
                }
            }
            for(ApplicationDto applicationDto:application){
                String applicationDtoId = applicationDto.getId();
                String appGrpId = applicationDto.getAppGrpId();
                String applicationNo = applicationDto.getApplicationNo();
                if(appFeeDetails!=null){
                    for(AppFeeDetailsDto appFeeDetailsDto : appFeeDetails){
                        if(applicationNo.equals(appFeeDetailsDto.getApplicationNo())){
                            appFeeDetailsDtos.add(appFeeDetailsDto);
                        }
                    }
                }
                if(groupId.equals(appGrpId)){
                    applicationDtos.add(applicationDto);
                    for(AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelation){
                        String applicationId = appPremisesCorrelationDto.getApplicationId();
                        String appGrpPremId = appPremisesCorrelationDto.getAppGrpPremId();
                        String premisesCorrelationDtoId = appPremisesCorrelationDto.getId();

                        if(applicationDtoId.equals(applicationId) && appliGrpPremisesIds.contains(appGrpPremId)){
                            appPremisesCorrelationDtos.add(appPremisesCorrelationDto);
                            for (AppSvcPremisesScopeDto appSvcPremisesScopeDto:appSvcPremisesScope){
                                String appPremCorreId = appSvcPremisesScopeDto.getAppPremCorreId();

                                if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                    appSvcPremisesScopeDtos.add(appSvcPremisesScopeDto);
                                    appSvcPremisesScopeIds.add(appSvcPremisesScopeDto.getId());
                                }

                            }

                            for (AppPremisesSelfDeclChklDto appPremisesSelfDeclChklDto :appPremisesSelfDeclChklEntity){
                                String appPremCorreId = appPremisesSelfDeclChklDto.getAppPremCorreId();
                                if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                    appPremisesSelfDeclChklDtos.add(appPremisesSelfDeclChklDto);

                                }
                            }

                            for(AppSvcDocDto appSvcDocDto:appSvcDoc){
                                String appPremCorreId = appSvcDocDto.getAppPremCorreId();
                                if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                    appSvcDocDtos.add(appSvcDocDto);
                                }

                            }

                            for(AppPremiseMiscDto appPremiseMiscDto:appPremiseMiscEntities){
                                String appPremCorreId = appPremiseMiscDto.getAppPremCorreId();
                                if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                    appPremiseMiscDtoList.add(appPremiseMiscDto);
                                }
                            }

                            for(AppPremisesSpecialDocDto appPremisesSpecialDocDto:appPremisesSpecialDocEntities){
                                String appPremCorreId = appPremisesSpecialDocDto.getAppPremCorreId();
                                if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                    appPremisesSpecialDocDtoList.add(appPremisesSpecialDocDto);
                                }
                            }
                        }
                    }

                    for(AppSvcKeyPersonnelDto appSvcKeyPersonnelDto:appSvcKeyPersonnel){
                        String applicationId = appSvcKeyPersonnelDto.getApplicationId();

                        if(applicationDtoId.equals(applicationId) ){
                            appSvcKeyPersonnelDtos.add(appSvcKeyPersonnelDto);
                            appSvcKeyPersonIds.add(appSvcKeyPersonnelDto.getId());
                        }

                    }
                    for(AppSvcKeyPersonnelDto appSvcKeyPersonnelDto: appSvcKeyPersonnelDtos){
                        String appGrpPsnExtId = appSvcKeyPersonnelDto.getAppGrpPsnExtId();
                        String appGrpPsnId = appSvcKeyPersonnelDto.getAppGrpPsnId();
                        for (AppGrpPersonnelDto appGrpPersonnelDto:appGrpPersonnel){
                            if(appGrpPsnId.equals(appGrpPersonnelDto.getId())){
                                appGrpPersonnelDtos.add(appGrpPersonnelDto);
                            }
                        }
                        if(!StringUtil.isEmpty(appGrpPsnExtId)){
                            for(AppGrpPersonnelExtDto appGrpPersonnelExtDto: appGrpPersonnelExt){
                                if (appGrpPsnExtId.equals(appGrpPersonnelExtDto.getId())){
                                    appGrpPersonnelExtDtos.add(appGrpPersonnelExtDto);
                                }
                            }
                        }
                    }
                    for(AppSvcPremisesScopeAllocationDto appSvcPremisesScopeAllocationDto:appSvcPremisesScopeAllocation){
                        String applicationId = appSvcPremisesScopeAllocationDto.getApplicationId();
                        String appSvcKeyPsnId = appSvcPremisesScopeAllocationDto.getAppSvcKeyPsnId();
                        String appSvcPremScopeId = appSvcPremisesScopeAllocationDto.getAppSvcPremScopeId();
                        if(applicationDtoId.equals(applicationId)&&appSvcKeyPersonIds.contains(appSvcKeyPsnId)
                                &&appSvcPremisesScopeIds.contains(appSvcPremScopeId)){
                            appSvcPremisesScopeAllocationDtos.add(appSvcPremisesScopeAllocationDto);

                        }
                    }
                    for(AppSvcPersonnelDto appSvcPersonnelDto:appSvcPersonnel){
                        String applicationId = appSvcPersonnelDto.getApplicationId();
                        if(applicationDtoId.equals(applicationId)){
                            appSvcPersonnelDtos.add(appSvcPersonnelDto);

                        }
                    }
                    if(appEditSelects!=null){
                        for(AppEditSelectDto appEditSelectDto:appEditSelects){
                            String applicationId = appEditSelectDto.getApplicationId();
                            String editType = appEditSelectDto.getEditType();
                            if(applicationDto.getId().equals(applicationId)&&ApplicationConsts.APPLICATION_EDIT_TYPE_RFC.equals(editType)){
                                appEditSelectDtos.add(appEditSelectDto);

                            }
                        }
                    }

                }


            }

            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDoc){
                String appGrpId = appGrpPrimaryDocDto.getAppGrpId();
                if(groupId.equals(appGrpId)){
                    appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);

                }
            }

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
            applicationListFileDto.setAppGroupMiscs(appGroupMiscDtos);
            applicationListFileDto.setAppFeeDetails(appFeeDetailsDtos);
            applicationListFileDto.setAppPremisesOperationalUnits(appPremisesOperationalUnitDtoList);
            applicationListFileDtoList.add(applicationListFileDto);
        }
        return applicationListFileDtoList;

    }

    @Override
    public void getRelatedDocuments(ApplicationListFileDto applicationListFileDto) {
        try{
            List<ApplicationGroupDto> applicationGroup = applicationListFileDto.getApplicationGroup();
            if(applicationGroup.isEmpty()){
                log.info("************* this grp is empty**************");
              return;
            }
            String groupId = applicationGroup.get(0).getId();
            //delete old grp history (that the document is blank before zip)
            deleteFile(groupId);

            List<AppSvcDocDto> appSvcDoc = applicationListFileDto.getAppSvcDoc();
            List<AppGrpPrimaryDocDto> appGrpPrimaryDoc = applicationListFileDto.getAppGrpPrimaryDoc();
            List<AppPremisesSpecialDocDto> appPremisesSpecialDocEntities = applicationListFileDto.getAppPremisesSpecialDocEntities();
            appSvcDoc(appSvcDoc,appGrpPrimaryDoc,appPremisesSpecialDocEntities,groupId);
        }catch (Exception e){
            log.error(StringUtil.changeForLog("***************** there have a error is "+e+"***************"));
            log.error(e.getMessage(),e);
        }

    }


    private void getFileRep(String id,String docName,String groupId){
        byte[] entity = fileRepositoryClient.getFileFormDataBase(id).getEntity();
        File file=MiscUtil.generateFile(sharedPath+AppServicesConsts.FILE_NAME+File.separator+groupId+ File.separator + AppServicesConsts.FILES,
                id + "@" + docName);
        try (OutputStream outputStream=Files.newOutputStream(file.toPath())) {
            if(entity!=null){
                outputStream.write(entity);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }
}
