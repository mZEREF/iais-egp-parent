package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpSecondAddrDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremNonLicRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoItemAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubLicenseeCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcClinicalDirectorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoAbortDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoMedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoNurseDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoTopDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoTopPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.UploadFileEditService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.Files.newOutputStream;

/**
 * UploadFileEditServiceImpl
 *
 * @author junyu
 * @date 2022/5/11
 */
@Service
@Slf4j
public class UploadFileEditServiceImpl implements UploadFileEditService {
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    @Value("${iais.sharedfolder.beapp.out}")
    private String sharedOutPath;

    @Autowired
    private ApplicationClient applicationFeClient;
    @Autowired
    private BeEicGatewayClient eicGatewayClient;
    @Autowired
    private FileRepoClient fileRepositoryClient;

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
        return groupId;
    }

    @Override
    public String getData() {
        List<String> list=applicationFeClient.checkPendToFeAppGrps().getEntity();
        if(IaisCommonUtils.isNotEmpty(list)){
            return applicationFeClient.fileAll(list).getEntity();
        }
        return null;
    }

    @Override
    public String  changeStatus(ApplicationListFileDto applicationListDto, Map<String,List<String>> map) {
        List<ApplicationGroupDto> applicationGroup = applicationListDto.getApplicationGroup();

        List<String> groupIds= IaisCommonUtils.genNewArrayList();

        for(ApplicationGroupDto every:applicationGroup){
            String id = every.getId();
            groupIds.add(id)  ;
        }

        map.put("groupIds",groupIds);
        applicationFeClient.updateStatus(map).getEntity();


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
    private void appSvcDoc(List<AppSvcDocDto> appSvcDoc, List<AppPremisesSpecialDocDto> appPremisesSpecialDocEntities, String groupId) throws Exception{
        //if path is not exists create path
        String path = sharedPath+ AppServicesConsts.FILE_NAME+File.separator+groupId+File.separator+"files";
        File fileRepPath = MiscUtil.generateFile(path);
        if(!fileRepPath.exists()){
            fileRepPath.mkdirs();
        }
        for(AppPremisesSpecialDocDto every:appPremisesSpecialDocEntities){
            getFileRep(every.getFileRepoId(),every.getDocName(),groupId);
        }
        for(AppSvcDocDto every:appSvcDoc){
            getFileRep(every.getFileRepoId(),every.getDocName(),groupId);
        }
    }
    /*--------*/
    private void appDeclarationDocs(List<AppDeclarationDocDto> appDeclarationDocs, String groupId) throws Exception{
        String path = sharedPath+ AppServicesConsts.FILE_NAME+File.separator+groupId+File.separator+"files";
        File fileRepPath = MiscUtil.generateFile(path);
        if(!fileRepPath.exists()){
            fileRepPath.mkdirs();
        }
        if(appDeclarationDocs!=null){
            for (AppDeclarationDocDto v : appDeclarationDocs) {
                getFileRep(v.getFileRepoId(),v.getDocName(),groupId);
            }
        }
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
    public boolean renameAndSave(String fileNamesss,String groupId)  {
        log.info("--------------rename start ---------------------");
        boolean flag = true;
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
                        flag=false;
                        break;
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                    throw new IaisRuntimeException(e);
                }
            }
        }
        return flag;
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
            s = eicGatewayClient.callEicWithTrack(processFileTrackDto, eicGatewayClient::saveFileApplication,
                    "saveFileApplication").getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.info(StringUtil.changeForLog("have error-------" +s));
            throw new IaisRuntimeException(e);
        }
        return s;
    }

    private void deleteFile(String groupId){
        String path = sharedPath+AppServicesConsts.FILE_NAME+File.separator+groupId;
        File fileRepPath = MiscUtil.generateFile(path);
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
        List<AppGrpPremisesDto> appGrpPremises = applicationListDto.getAppGrpPremises();
        List<ApplicationDto> application = applicationListDto.getApplication();
        List<AppPremisesCorrelationDto> appPremisesCorrelation = applicationListDto.getAppPremisesCorrelation();
        List<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklEntity = applicationListDto.getAppPremisesSelfDeclChklEntity();
        List<AppSvcDocDto> appSvcDoc = applicationListDto.getAppSvcDoc();
        List<AppSvcKeyPersonnelDto> appSvcKeyPersonnel = applicationListDto.getAppSvcKeyPersonnel();
        List<AppSvcPersonnelDto> appSvcPersonnel = applicationListDto.getAppSvcPersonnel();
        List<AppPremiseMiscDto> appPremiseMiscEntities = applicationListDto.getAppPremiseMiscEntities();
        List<AppPremisesSpecialDocDto> appPremisesSpecialDocEntities = applicationListDto.getAppPremisesSpecialDocEntities();
        List<AppEditSelectDto> appEditSelects = applicationListDto.getAppEditSelects();
        List<AppGroupMiscDto> appGroupMiscs = applicationListDto.getAppGroupMiscs();
        List<AppFeeDetailsDto> appFeeDetails = applicationListDto.getAppFeeDetails();
        List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = applicationListDto.getAppPremisesOperationalUnits();
        List<AppPremEventPeriodDto> appPremEventPeriods = applicationListDto.getAppPremEventPeriods();
        List<AppPremOpenPeriodDto> appPremOpenPeriods = applicationListDto.getAppPremOpenPeriods();
        List<AppSvcVehicleDto> appSvcVehicles = applicationListDto.getAppSvcVehicles();
        List<AppSvcChargesDto> appSvcChargesPages = applicationListDto.getAppSvcChargesPages();
        List<AppSvcClinicalDirectorDto> appSvcClinicalDirectors = applicationListDto.getAppSvcClinicalDirectors();
        List<AppDeclarationMessageDto> appDeclarationMessages = applicationListDto.getAppDeclarationMessages();
        List<AppDeclarationDocDto> appDeclarationDocs = applicationListDto.getAppDeclarationDocs();
        List<AppSubLicenseeCorrelationDto> appSubLicenseeCorrelationDtos= applicationListDto.getAppSubLicenseeCorrelations();
        List<SubLicenseeDto> subLicenseeDtos=applicationListDto.getAppGrpSubLicenseeInfos();
        List<AppPremNonLicRelationDto> appPremNonLicRelationDtos=applicationListDto.getAppPremNonLicRelations();
        List<AppPremScopeDto> appPremScopeDtos=applicationListDto.getAppPremScopes();
        List<AppPremSubSvcRelDto> appPremSubSvcRelDtos=applicationListDto.getAppPremSubSvcRels();
        List<AppSvcSuplmItemDto> appSvcSuplmItemDtos=applicationListDto.getAppSvcSuplmItems();
        List<AppSvcOtherInfoItemAnswerDto> appPremOtherInfoItemAnswerDtos=applicationListDto.getAppPremOtherInfoItemAnswers();
        List<AppSvcBusinessDto> appSvcBusinessDtos=applicationListDto.getAppSvcBusinesses();
        List<AppGrpSecondAddrDto> appGrpSecondAddrDtos=applicationListDto.getAppGrpSecondAddrs();
        List<AppSvcOtherInfoMedDto> appSvcOtherInfoMedDtos=applicationListDto.getAppSvcOtherInfoMeds();
        List<AppSvcOtherInfoNurseDto> appSvcOtherInfoNurseDtos=applicationListDto.getAppSvcOtherInfoNurses();
        List<AppSvcOtherInfoTopDto> appSvcOtherInfoTopDtos=applicationListDto.getAppSvcOtherInfoTops();
        List<AppSvcOtherInfoAbortDto> appSvcOtherInfoAbortDtos=applicationListDto.getAppSvcOtherInfoAborts();
        List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtos=applicationListDto.getAppSvcOtherInfoTopPersons();

        List<ApplicationListFileDto> applicationListFileDtoList=IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(applicationGroup)){
            for(ApplicationGroupDto every :applicationGroup){

                Set<String> appliGrpPremisesIds=IaisCommonUtils.genNewHashSet();

                ApplicationListFileDto applicationListFileDto=new ApplicationListFileDto();
                Set<ApplicationGroupDto> groupDtos=IaisCommonUtils.genNewHashSet();
                Set<ApplicationDto> applicationDtos=IaisCommonUtils.genNewHashSet();
                Set<AppGrpPremisesDto> appGrpPremisesDtos=IaisCommonUtils.genNewHashSet();
                Set<AppPremisesCorrelationDto> appPremisesCorrelationDtos=IaisCommonUtils.genNewHashSet();
                List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppGrpPersonnelDto> appGrpPersonnelDtos=IaisCommonUtils.genNewHashSet();
                List<AppGrpPersonnelDto> appGrpPersonnelDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppGrpPersonnelExtDto> appGrpPersonnelExtDtos=IaisCommonUtils.genNewHashSet();
                List<AppGrpPersonnelExtDto> appGrpPersonnelExtDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos=IaisCommonUtils.genNewHashSet();
                List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcPersonnelDto >  appSvcPersonnelDtos=IaisCommonUtils.genNewHashSet();
                List<AppSvcPersonnelDto >  appSvcPersonnelDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklDtos=IaisCommonUtils.genNewHashSet();
                List<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcDocDto> appSvcDocDtos=IaisCommonUtils.genNewHashSet();
                List<AppSvcDocDto> appSvcDocDtoList=IaisCommonUtils.genNewArrayList();
                List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtoSet=new HashSet<>(16);

                List<AppPremiseMiscDto> appPremiseMiscDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoList=IaisCommonUtils.genNewHashSet();
                List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoList1=IaisCommonUtils.genNewArrayList();

                List<AppPremEventPeriodDto > appPremEventPeriodDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppPremEventPeriodDto> appPremEventPeriodDtoSet=new HashSet<>(16);

                List<AppPremOpenPeriodDto> appPremOpenPeriodDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppPremOpenPeriodDto> appPremOpenPeriodDtoSet=new HashSet<>(16);

                Set<AppEditSelectDto> appEditSelectDtos=IaisCommonUtils.genNewHashSet();
                List<AppEditSelectDto> appEditSelectDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppGroupMiscDto> appGroupMiscDtos=IaisCommonUtils.genNewHashSet();
                List<AppGroupMiscDto> appGroupMiscDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppFeeDetailsDto> appFeeDetailsDtos =IaisCommonUtils.genNewHashSet();
                List<AppFeeDetailsDto> appFeeDetailsDtoList =IaisCommonUtils.genNewArrayList();
                List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtoSet=IaisCommonUtils.genNewHashSet();

                Set<AppSvcChargesDto> appSvcChargesDtoList=IaisCommonUtils.genNewHashSet();
                List<AppSvcChargesDto> appSvcChargesDtoList1=IaisCommonUtils.genNewArrayList();
                Set<AppSvcVehicleDto> appSvcVehicleDtoList=IaisCommonUtils.genNewHashSet();
                List<AppSvcVehicleDto> appSvcVehicleDtoList1=IaisCommonUtils.genNewArrayList();
                Set<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtoSet=IaisCommonUtils.genNewHashSet();
                List<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtoList=IaisCommonUtils.genNewArrayList();
                List<AppDeclarationMessageDto> appDeclarationMessageDtos=new ArrayList<>(10);
                List<AppDeclarationDocDto> appDeclarationDocDtoList=new ArrayList<>(10);
                List<AppSubLicenseeCorrelationDto> appSubLicenseeCorrelationDtoList= IaisCommonUtils.genNewArrayList();
                List<SubLicenseeDto> subLicenseeDtoList=IaisCommonUtils.genNewArrayList();
                List<AppPremScopeDto> appPremScopeDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppPremScopeDto> appPremScopeDtoSet=new HashSet<>(16);
                List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppPremSubSvcRelDto> appPremSubSvcRelDtoSet=new HashSet<>(16);
                List<AppPremNonLicRelationDto> appPremNonLicRelationDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppPremNonLicRelationDto> appPremNonLicRelationDtoSet=new HashSet<>(16);

                List<AppSvcSuplmItemDto> appSvcSuplmItemDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcSuplmItemDto> appSvcSuplmItemDtoSet=new HashSet<>();
                List<AppSvcOtherInfoItemAnswerDto> appPremOtherInfoItemAnswerDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcOtherInfoItemAnswerDto> appPremOtherInfoItemAnswerDtoSet=new HashSet<>();
                List<AppSvcBusinessDto> appSvcBusinessDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcBusinessDto> appSvcBusinessDtoSet=new HashSet<>();
                List<AppGrpSecondAddrDto> appGrpSecondAddrDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppGrpSecondAddrDto> appGrpSecondAddrDtoSet=new HashSet<>();
                List<AppSvcOtherInfoMedDto> appSvcOtherInfoMedDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcOtherInfoMedDto> appSvcOtherInfoMedDtoSet=new HashSet<>();
                List<AppSvcOtherInfoNurseDto> appSvcOtherInfoNurseDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcOtherInfoNurseDto> appSvcOtherInfoNurseDtoSet=new HashSet<>();
                List<AppSvcOtherInfoTopDto> appSvcOtherInfoTopDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcOtherInfoTopDto> appSvcOtherInfoTopDtoSet=new HashSet<>();
                List<AppSvcOtherInfoAbortDto> appSvcOtherInfoAbortDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcOtherInfoAbortDto> appSvcOtherInfoAbortDtoSet=new HashSet<>();
                List<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtoList=IaisCommonUtils.genNewArrayList();
                Set<AppSvcOtherInfoTopPersonDto> appSvcOtherInfoTopPersonDtoSet=new HashSet<>();
                groupDtos.add(every);
                String groupId = every.getId();
                if(subLicenseeDtos!=null){
                    for (SubLicenseeDto v : subLicenseeDtos) {
                        if(v.getAppGrpId().equals(groupId)){
                            subLicenseeDtoList.add(v);
                        }
                    }
                }
                if(appDeclarationMessages!=null){
                    for (AppDeclarationMessageDto v : appDeclarationMessages) {
                        String appGrpId = v.getAppGrpId();
                        if(groupId.equals(appGrpId)){
                            appDeclarationMessageDtos.add(v);
                        }
                    }
                }
                if(appDeclarationDocs!=null){
                    for (AppDeclarationDocDto v : appDeclarationDocs) {
                        String appGrpId = v.getAppGrpId();
                        if(groupId.equals(appGrpId)){
                            appDeclarationDocDtoList.add(v);
                        }
                    }
                }
                for(AppGrpPremisesDto appliGrpPremisesDto:appGrpPremises){
                    String grpPremisesDtoAppGrpId = appliGrpPremisesDto.getAppGrpId();
                    if(groupId.equals(grpPremisesDtoAppGrpId)){
                        appGrpPremisesDtos.add(appliGrpPremisesDto);
                        appliGrpPremisesIds.add(appliGrpPremisesDto.getId());
                        String appliGrpPremisesDtoId = appliGrpPremisesDto.getId();

                        for(AppPremisesOperationalUnitDto appPremisesOperationalUnitDto : appPremisesOperationalUnitDtos){
                            String premisesId = appPremisesOperationalUnitDto.getPremisesId();
                            if(appliGrpPremisesDtoId.equals(premisesId)){
                                appPremisesOperationalUnitDtoSet.add(appPremisesOperationalUnitDto);
                            }
                        }
                        if(appPremNonLicRelationDtos!=null){
                            for(AppPremNonLicRelationDto appPremNonLicRelationDto : appPremNonLicRelationDtos){
                                String appGrpPremId = appPremNonLicRelationDto.getPremId();
                                if(appliGrpPremisesDtoId.equals(appGrpPremId)){
                                    appPremNonLicRelationDtoSet.add(appPremNonLicRelationDto);
                                }
                            }
                        }
                        if(appGrpSecondAddrDtos!=null){
                            for(AppGrpSecondAddrDto appGrpSecondAddrDto : appGrpSecondAddrDtos){
                                String appGrpPremId = appGrpSecondAddrDto.getAppGrpPremisesId();
                                if(appliGrpPremisesDtoId.equals(appGrpPremId)){
                                    appGrpSecondAddrDtoSet.add(appGrpSecondAddrDto);
                                }
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
                    if(groupId.equals(appGrpId)){
                        if(appFeeDetails!=null){
                            for(AppFeeDetailsDto appFeeDetailsDto : appFeeDetails){
                                if(applicationNo.equals(appFeeDetailsDto.getApplicationNo())){
                                    appFeeDetailsDtos.add(appFeeDetailsDto);
                                }
                            }
                        }
                        applicationDtos.add(applicationDto);
                        for(AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelation){
                            String applicationId = appPremisesCorrelationDto.getApplicationId();
                            String appGrpPremId = appPremisesCorrelationDto.getAppGrpPremId();
                            String premisesCorrelationDtoId = appPremisesCorrelationDto.getId();

                            if(applicationDtoId.equals(applicationId) && appliGrpPremisesIds.contains(appGrpPremId)){
                                appPremisesCorrelationDtos.add(appPremisesCorrelationDto);
                                if(appSubLicenseeCorrelationDtos!=null){
                                    for (AppSubLicenseeCorrelationDto v : appSubLicenseeCorrelationDtos) {
                                        if(v.getApplicationId().equals(applicationId)){
                                            appSubLicenseeCorrelationDtoList.add(v);
                                        }
                                    }
                                }
                                for(AppPremPhOpenPeriodDto appPremPhOpenPeriodDto :appPremPhOpenPeriodDtos){
                                    String appPremCorreId = appPremPhOpenPeriodDto.getAppPremId();
                                    if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                        appPremPhOpenPeriodDtoSet.add(appPremPhOpenPeriodDto);
                                    }
                                }

                                if(appPremEventPeriods!=null){
                                    for(AppPremEventPeriodDto appPremEventPeriodDto: appPremEventPeriods){
                                        String appPremCorreId = appPremEventPeriodDto.getAppPremId();
                                        if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                            appPremEventPeriodDtoSet.add(appPremEventPeriodDto);
                                        }
                                    }
                                }
                                if(appPremOpenPeriods!=null){
                                    for(AppPremOpenPeriodDto appPremOpenPeriodDto : appPremOpenPeriods){
                                        String appPremCorreId = appPremOpenPeriodDto.getAppPremId();
                                        if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                            appPremOpenPeriodDtoSet.add(appPremOpenPeriodDto);
                                        }
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
                                for(AppSvcVehicleDto appSvcVehicleDto : appSvcVehicles){
                                    String appPremCorreId = appSvcVehicleDto.getAppPremCorreId();
                                    if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                        appSvcVehicleDtoList.add(appSvcVehicleDto);
                                    }
                                }
                                for(AppSvcChargesDto appSvcChargesDto : appSvcChargesPages){
                                    String appPremCorreId = appSvcChargesDto.getAppPremCorreId();
                                    if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                        appSvcChargesDtoList.add(appSvcChargesDto);
                                    }
                                }
                                for(AppPremScopeDto appPremScopeDto : appPremScopeDtos){
                                    String appPremCorreId = appPremScopeDto.getAppPremCorreId();
                                    if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                        appPremScopeDtoSet.add(appPremScopeDto);
                                    }
                                }
                                for(AppPremSubSvcRelDto appPremSubSvcRelDto : appPremSubSvcRelDtos){
                                    String appPremCorreId = appPremSubSvcRelDto.getAppPremCorreId();
                                    if(premisesCorrelationDtoId.equals(appPremCorreId)){
                                        appPremSubSvcRelDtoSet.add(appPremSubSvcRelDto);
                                    }
                                }
                                if(appSvcSuplmItemDtos!=null){
                                    for (AppSvcSuplmItemDto appSvcSuplmItemDto:appSvcSuplmItemDtos
                                    ) {
                                        if(appSvcSuplmItemDto.getPremCorreId().equals(premisesCorrelationDtoId)){
                                            appSvcSuplmItemDtoSet.add(appSvcSuplmItemDto);
                                        }
                                    }
                                }
                                if(appPremOtherInfoItemAnswerDtos!=null){
                                    for (AppSvcOtherInfoItemAnswerDto appPremOtherInfoItemAnswerDto:appPremOtherInfoItemAnswerDtos
                                    ) {
                                        if(appPremOtherInfoItemAnswerDto.getAppPremCorrId().equals(premisesCorrelationDtoId)){
                                            appPremOtherInfoItemAnswerDtoSet.add(appPremOtherInfoItemAnswerDto);
                                        }
                                    }
                                }
                                if(appSvcBusinessDtos!=null){
                                    for (AppSvcBusinessDto appSvcBusinessDto:appSvcBusinessDtos
                                    ) {
                                        if(appSvcBusinessDto.getAppPremId().equals(premisesCorrelationDtoId)){
                                            appSvcBusinessDtoSet.add(appSvcBusinessDto);
                                        }
                                    }
                                }
                                if(appSvcOtherInfoMedDtos!=null){
                                    for (AppSvcOtherInfoMedDto appSvcOtherInfoMedDto:appSvcOtherInfoMedDtos
                                    ) {
                                        if(appSvcOtherInfoMedDto.getAppPremId().equals(premisesCorrelationDtoId)){
                                            appSvcOtherInfoMedDtoSet.add(appSvcOtherInfoMedDto);
                                        }
                                    }
                                }
                                if(appSvcOtherInfoNurseDtos!=null){
                                    for (AppSvcOtherInfoNurseDto appSvcOtherInfoNurseDto:appSvcOtherInfoNurseDtos
                                    ) {
                                        if(appSvcOtherInfoNurseDto.getAppPremId().equals(premisesCorrelationDtoId)){
                                            appSvcOtherInfoNurseDtoSet.add(appSvcOtherInfoNurseDto);
                                        }
                                    }
                                }
                                if(appSvcOtherInfoTopDtos!=null){
                                    for (AppSvcOtherInfoTopDto appSvcOtherInfoTopDto:appSvcOtherInfoTopDtos
                                    ) {
                                        if(appSvcOtherInfoTopDto.getAppPremId().equals(premisesCorrelationDtoId)){
                                            appSvcOtherInfoTopDtoSet.add(appSvcOtherInfoTopDto);
                                        }
                                    }
                                }
                                if(appSvcOtherInfoAbortDtos!=null){
                                    for (AppSvcOtherInfoAbortDto appSvcOtherInfoAbortDto:appSvcOtherInfoAbortDtos
                                    ) {
                                        if(appSvcOtherInfoAbortDto.getAppPremId().equals(premisesCorrelationDtoId)){
                                            appSvcOtherInfoAbortDtoSet.add(appSvcOtherInfoAbortDto);
                                        }
                                    }
                                }
                                if(appSvcOtherInfoTopPersonDtos!=null){
                                    for (AppSvcOtherInfoTopPersonDto appSvcOtherInfoTopPersonDto:appSvcOtherInfoTopPersonDtos
                                    ) {
                                        if(appSvcOtherInfoTopPersonDto.getAppPremId().equals(premisesCorrelationDtoId)){
                                            appSvcOtherInfoTopPersonDtoSet.add(appSvcOtherInfoTopPersonDto);
                                        }
                                    }
                                }
                                if(appSvcClinicalDirectors!=null){
                                    for (AppSvcClinicalDirectorDto appSvcClinicalDirectorDto:appSvcClinicalDirectors
                                    ) {
                                        if(appSvcClinicalDirectorDto.getAppPremCorreId().equals(premisesCorrelationDtoId)){
                                            appSvcClinicalDirectorDtoSet.add(appSvcClinicalDirectorDto);
                                        }
                                    }
                                }
                            }
                        }

                        for(AppSvcKeyPersonnelDto appSvcKeyPersonnelDto:appSvcKeyPersonnel){
                            String applicationId = appSvcKeyPersonnelDto.getApplicationId();

                            if(applicationDtoId.equals(applicationId) ){
                                appSvcKeyPersonnelDtos.add(appSvcKeyPersonnelDto);
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
                        for(AppSvcPersonnelDto appSvcPersonnelDto:appSvcPersonnel){
                            String applicationId = appSvcPersonnelDto.getApplicationId();
                            if(applicationDtoId.equals(applicationId)){
                                appSvcPersonnelDtos.add(appSvcPersonnelDto);

                            }
                        }
                        if(appEditSelects!=null){
                            for(AppEditSelectDto appEditSelectDto:appEditSelects){
                                String applicationId = appEditSelectDto.getApplicationId();
                                if(applicationDto.getId().equals(applicationId)){
                                    appEditSelectDtos.add(appEditSelectDto);

                                }
                            }
                        }

                    }


                }

                List<ApplicationGroupDto> applicationGroupDtoList=IaisCommonUtils.genNewArrayList();
                applicationGroupDtoList.addAll(groupDtos);
                applicationListFileDto.setApplicationGroup(applicationGroupDtoList);
                List<ApplicationDto> applicationDtoList=IaisCommonUtils.genNewArrayList();
                applicationDtoList.addAll(applicationDtos);
                applicationListFileDto.setApplication( applicationDtoList);
                List<AppGrpPremisesDto> appGrpPremisesEntityDtoList=IaisCommonUtils.genNewArrayList();
                appGrpPremisesEntityDtoList.addAll(appGrpPremisesDtos);
                applicationListFileDto.setAppGrpPremises(appGrpPremisesEntityDtoList);
                appPremisesOperationalUnitDtoList.addAll(appPremisesOperationalUnitDtoSet);
                appPremEventPeriodDtoList.addAll(appPremEventPeriodDtoSet);
                appPremOpenPeriodDtoList.addAll(appPremOpenPeriodDtoSet);
                appPremPhOpenPeriodDtoList.addAll(appPremPhOpenPeriodDtoSet);
                appPremNonLicRelationDtoList.addAll(appPremNonLicRelationDtoSet);
                appPremScopeDtoList.addAll(appPremScopeDtoSet);
                appPremSubSvcRelDtoList.addAll(appPremSubSvcRelDtoSet);
                applicationListFileDto.setAppPremPhOpenPeriods(appPremPhOpenPeriodDtoList);
                appPremisesCorrelationDtoList.addAll(appPremisesCorrelationDtos);
                applicationListFileDto.setAppPremisesCorrelation (appPremisesCorrelationDtoList);
                appGrpPersonnelDtoList.addAll(appGrpPersonnelDtos);
                applicationListFileDto.setAppGrpPersonnel (appGrpPersonnelDtoList);
                appGrpPersonnelExtDtoList.addAll(appGrpPersonnelExtDtos);
                applicationListFileDto.setAppGrpPersonnelExt (appGrpPersonnelExtDtoList);
                appSvcKeyPersonnelDtoList.addAll(appSvcKeyPersonnelDtos);
                applicationListFileDto.setAppSvcKeyPersonnel (appSvcKeyPersonnelDtoList);
                appSvcPersonnelDtoList.addAll(appSvcPersonnelDtos);
                applicationListFileDto.setAppSvcPersonnel (appSvcPersonnelDtoList);
                appPremisesSelfDeclChklDtoList.addAll(appPremisesSelfDeclChklDtos);
                applicationListFileDto.setAppPremisesSelfDeclChklEntity(appPremisesSelfDeclChklDtoList);
                appSvcDocDtoList.addAll(appSvcDocDtos);
                applicationListFileDto.setAppSvcDoc(appSvcDocDtoList);
                applicationListFileDto.setAppPremiseMiscEntities(appPremiseMiscDtoList);
                appPremisesSpecialDocDtoList1.addAll(appPremisesSpecialDocDtoList);
                applicationListFileDto.setAppPremisesSpecialDocEntities(appPremisesSpecialDocDtoList1);
                appEditSelectDtoList.addAll(appEditSelectDtos);
                applicationListFileDto.setAppEditSelects(appEditSelectDtoList);
                appGroupMiscDtoList.addAll(appGroupMiscDtos);
                applicationListFileDto.setAppGroupMiscs(appGroupMiscDtoList);
                appFeeDetailsDtoList.addAll(appFeeDetailsDtos);
                applicationListFileDto.setAppFeeDetails(appFeeDetailsDtoList);
                applicationListFileDto.setAppPremisesOperationalUnits(appPremisesOperationalUnitDtoList);
                applicationListFileDto.setAppPremEventPeriods(appPremEventPeriodDtoList);
                applicationListFileDto.setAppPremOpenPeriods(appPremOpenPeriodDtoList);
                appSvcVehicleDtoList1.addAll(appSvcVehicleDtoList);
                applicationListFileDto.setAppSvcVehicles(appSvcVehicleDtoList1);
                appSvcChargesDtoList1.addAll(appSvcChargesDtoList);
                applicationListFileDto.setAppSvcChargesPages(appSvcChargesDtoList1);
                appSvcClinicalDirectorDtoList.addAll(appSvcClinicalDirectorDtoSet);
                applicationListFileDto.setAppSvcClinicalDirectors(appSvcClinicalDirectorDtoList);
                applicationListFileDto.setAppDeclarationMessages(appDeclarationMessageDtos);
                applicationListFileDto.setAppDeclarationDocs(appDeclarationDocDtoList);
                applicationListFileDto.setAppGrpSubLicenseeInfos(subLicenseeDtoList);
                applicationListFileDto.setAppSubLicenseeCorrelations(appSubLicenseeCorrelationDtoList);
                applicationListFileDto.setAppPremScopes(appPremScopeDtoList);
                applicationListFileDto.setAppPremSubSvcRels(appPremSubSvcRelDtoList);
                applicationListFileDto.setAppPremNonLicRelations(appPremNonLicRelationDtoList);
                appSvcBusinessDtoList.addAll(appSvcBusinessDtoSet);
                applicationListFileDto.setAppSvcBusinesses(appSvcBusinessDtoList);

                appGrpSecondAddrDtoList.addAll(appGrpSecondAddrDtoSet);
                applicationListFileDto.setAppGrpSecondAddrs(appGrpSecondAddrDtoList);

                appSvcOtherInfoMedDtoList.addAll(appSvcOtherInfoMedDtoSet);
                applicationListFileDto.setAppSvcOtherInfoMeds(appSvcOtherInfoMedDtoList);

                appSvcOtherInfoNurseDtoList.addAll(appSvcOtherInfoNurseDtoSet);
                applicationListFileDto.setAppSvcOtherInfoNurses(appSvcOtherInfoNurseDtoList);

                appSvcOtherInfoTopDtoList.addAll(appSvcOtherInfoTopDtoSet);
                applicationListFileDto.setAppSvcOtherInfoTops(appSvcOtherInfoTopDtoList);

                appSvcOtherInfoTopPersonDtoList.addAll(appSvcOtherInfoTopPersonDtoSet);
                applicationListFileDto.setAppSvcOtherInfoTopPersons(appSvcOtherInfoTopPersonDtoList);

                appSvcOtherInfoAbortDtoList.addAll(appSvcOtherInfoAbortDtoSet);
                applicationListFileDto.setAppSvcOtherInfoAborts(appSvcOtherInfoAbortDtoList);

                appSvcSuplmItemDtoList.addAll(appSvcSuplmItemDtoSet);
                applicationListFileDto.setAppSvcSuplmItems(appSvcSuplmItemDtoList);

                appPremOtherInfoItemAnswerDtoList.addAll(appPremOtherInfoItemAnswerDtoSet);
                applicationListFileDto.setAppPremOtherInfoItemAnswers(appPremOtherInfoItemAnswerDtoList);

                applicationListFileDtoList.add(applicationListFileDto);
            }

        }
        return applicationListFileDtoList;

    }

    @Override
    public void getRelatedDocuments(ApplicationListFileDto applicationListFileDto) throws Exception{
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
            List<AppPremisesSpecialDocDto> appPremisesSpecialDocEntities = applicationListFileDto.getAppPremisesSpecialDocEntities();
            List<AppDeclarationDocDto> appDeclarationDocs = applicationListFileDto.getAppDeclarationDocs();
            appSvcDoc(appSvcDoc,appPremisesSpecialDocEntities,groupId);
            appDeclarationDocs(appDeclarationDocs,groupId);
        }catch (Exception e){
            log.error(StringUtil.changeForLog("***************** there have a error is "+e+"***************"));
            log.error(e.getMessage(),e);
            throw e;
        }

    }


    private void getFileRep(String id,String docName,String groupId) throws Exception{
        if(id==null||"".equals(id)){
            return;
        }
        byte[] entity = fileRepositoryClient.getFileFormDataBase(id).getEntity();
        log.info(StringUtil.changeForLog("file repo id is " + id));
        File file=MiscUtil.generateFile(sharedPath+AppServicesConsts.FILE_NAME+File.separator+groupId+ File.separator + AppServicesConsts.FILES,
                id);
        try (OutputStream outputStream= newOutputStream(file.toPath())) {
            if(entity!=null){
                outputStream.write(entity);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.info("file stream is null");
            throw e;
        }
    }
}
