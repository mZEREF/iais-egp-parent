package com.ecquaria.cloud.moh.iais.service.impl;



import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.FileUtil;
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
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @author Wenkang
 * @date 2019/11/9 16:09
 */
@Service
@Slf4j
public class LicenceFileDownloadServiceImpl implements LicenceFileDownloadService {
    @Value("${iais.syncFileTracking.shared.path}")
    private     String sharedPath;


    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    private TaskService taskService;
    @Autowired
    private LicenceFileDownloadService licenceFileDownloadService;
    @Autowired
    private BroadcastService broadcastService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private SystemBeLicClient systemClient;
    @Autowired
    private FileRepoClient fileRepoClient;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Override
    public boolean decompression() throws Exception{
        log.info("-------------decompression start ---------");

        File[] files = new File(sharedPath+File.separator+AppServicesConsts.BACKUPS+File.separator).listFiles();
        for(File fil:files){
            if(fil.getName().endsWith(AppServicesConsts.ZIP_NAME)){
                String name = fil.getName();
                String path = fil.getPath();
                String relPath= AppServicesConsts.BACKUPS+File.separator+name;
                HashMap<String,String> map=IaisCommonUtils.genNewHashMap();
                map.put("fileName",name);
                map.put("filePath",relPath);

                ProcessFileTrackDto processFileTrackDto = applicationClient.isFileExistence(map).getEntity();
                if(processFileTrackDto!=null){
                    //check file is changed
                    try (FileInputStream is=new FileInputStream(fil);
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
                    /**************/
                    String refId = processFileTrackDto.getRefId();
                    CheckedInputStream cos=null;
                    BufferedInputStream bis=null;
                    BufferedOutputStream bos=null;
                    OutputStream os=null;
                    try (ZipFile zipFile=new ZipFile(path);)  {
                        for( Enumeration<? extends ZipEntry> entries = zipFile.entries();entries.hasMoreElements();){
                            ZipEntry zipEntry = entries.nextElement();
                            zipFile(zipEntry,os,bos,zipFile,bis,cos,name,refId);
                        }

                    } catch (IOException e) {
                        log.error(e.getMessage(),e);
                    }

                    try {

                        List<ApplicationDto> listApplicationDto =new ArrayList();
                        List<ApplicationDto> requestForInfList=new ArrayList();
                        //need event bus
                        String submissionId = generateIdClient.getSeqId().getEntity();
                        this.download(processFileTrackDto,listApplicationDto, requestForInfList,name,refId,submissionId);
                        sendTask(listApplicationDto,requestForInfList,submissionId);

                      /*  moveFile(fil);*/
                        //save success
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
    //todo  delete file
    @Override
    public List<ApplicationDto> listApplication() {

        List<ApplicationDto> byPathParam =  applicationClient. getApplicationDto().getEntity();
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
    public void initPath() {

        File compress =new File(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+AppServicesConsts.FILE_NAME);
        File backups=new File(sharedPath+File.separator+AppServicesConsts.BACKUPS+File.separator);
        File compressPath=new File(sharedPath+File.separator+AppServicesConsts.COMPRESS);
        File movePath=new File(sharedPath+File.separator+"move");
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

    public Boolean  download( ProcessFileTrackDto processFileTrackDto,List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList,String fileName
    ,String groupPath,String submissionId)  throws Exception {

        Boolean flag=false;

            File file =new File(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+
                    File.separator+groupPath+File.separator+AppServicesConsts.FILE_NAME+File.separator+groupPath);
            log.info(file.getPath()+"**********************");
            if(!file.exists()){
                file.mkdirs();
            }
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(File  filzz:files){
                    if(filzz.isFile() &&filzz.getName().endsWith(AppServicesConsts.FILE_FORMAT)){
                        try (  FileInputStream  fileInputStream =new FileInputStream(filzz);
                                ByteArrayOutputStream by=new ByteArrayOutputStream();) {

                            int count=0;
                            byte [] size=new byte[1024];
                            count=fileInputStream.read(size);
                            while(count!=-1){
                                by.write(size,0,count);
                                count= fileInputStream.read(size);
                            }

                            Boolean aBoolean = fileToDto(by.toString(), listApplicationDto, requestForInfList,processFileTrackDto,submissionId);

                           flag=aBoolean;
                            if(aBoolean){
                                if(processFileTrackDto!=null){

                                    changeStatus(processFileTrackDto,submissionId);

                                    saveFileRepo( fileName,groupPath,submissionId);
                                }
                            }
                        }catch (Exception e){
                            log.error(e.getMessage(),e);
                           throw new Exception();
                        }

                    }
                }
            }


        return flag;
    }

    /*************************/
    /*
    * to update fe data
    * */
    private void changeStatus( ProcessFileTrackDto processFileTrackDto,String submissionId){
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        AuditTrailDto batchJobDto = AuditTrailHelper.getBatchJobDto("INTRANET");
        processFileTrackDto.setAuditTrailDto(batchJobDto);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_COMPLETE);

        eventBusHelper.submitAsyncRequest(processFileTrackDto,submissionId,EventBusConsts.SERVICE_NAME_APPSUBMIT
        ,EventBusConsts.OPERATION_CHANGE_STATUS_FILE_TRACK,processFileTrackDto.getRefId(),null);
       /* systemClient.updateProcessFileTrack(processFileTrackDto);*/

    }



        private void zipFile( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos,String fileName
        ,String groupPath)  {


            try {
                if(!zipEntry.getName().endsWith(File.separator)){

                    String substring = zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator));
                    File file =new File( sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+File.separator+groupPath+File.separator+substring);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    os=new FileOutputStream(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+File.separator+groupPath+File.separator+zipEntry.getName());
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

                    new File(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+File.separator+groupPath+File.separator+zipEntry.getName()).mkdirs();
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




    private Boolean fileToDto(String str,List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList,ProcessFileTrackDto processFileTrackDto,
                              String submissionId)
            throws Exception {
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
        List<AppGrpPremisesEntityDto> appGrpPremises = applicationListDto.getAppGrpPremises();
        for(AppGrpPremisesEntityDto every:appGrpPremises){
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
        update(listApplicationDto,applicationGroup,application);
        requeOrNew(requestForInfList,applicationGroup,application);
        String id = applicationListDto.getApplicationGroup().get(0).getId();
        eventBusHelper.submitAsyncRequest(applicationListDto,submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_SAVE_GROUP_APPLICATION,id,null);

        Boolean flag=true;
       /* try {

            flag=applicationClient.getDownloadFile(applicationListDto).getStatusCode() == 200;
            if(flag){
                log.info("-----------getDownloadFile-------");
                List<ApplicationDto> list = this.listApplication();
                this. requestForInfList(requestForInfList);

                listApplicationDto.addAll(list);

            }

        }catch (Exception e){
             log.info("have error is save data" +e);
            processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            AuditTrailDto batchJobDto = AuditTrailHelper.getBatchJobDto("INTRANET");
            processFileTrackDto.setAuditTrailDto(batchJobDto);
            processFileTrackDto.setStatus("PFT004");
            systemClient.updateProcessFileTrack(processFileTrackDto);
            throw new Exception();
        }*/

        return flag;

    }



    /*
    *
    * save file to fileRepro*/
    private void saveFileRepo(String fileNames,String groupPath,String submissionId){
        boolean aBoolean=false;
        File file =new File(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileNames+File.separator+groupPath+File.separator+"folder"+File.separator+"files");
        if(!file.exists()){
            file.mkdirs();
        }
        List<FileRepoDto> fileRepoDtos = IaisCommonUtils.genNewArrayList();
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
                          log.error(e.getMessage(),e);
                        }
                      /*  try ( InputStream input = new FileInputStream(f)){
                            if(fileItem!=null){
                                OutputStream os = fileItem.getOutputStream();
                                IOUtils.copy(input, os);
                            }
                        } catch (IOException ex) {
                           log.error(ex.getMessage(),ex);
                        }
                        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);*/

                        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto("intranet");
                        FileRepoDto fileRepoDto = new FileRepoDto();

                        fileRepoDto.setId(fileName.toString());
                        fileRepoDto.setAuditTrailDto(intranet);
                        fileRepoDto.setFileName(f.getName());
                        fileRepoDto.setRelativePath(AppServicesConsts.COMPRESS+File.separator+fileNames+
                                File.separator+groupPath+File.separator+"folder"+File.separator+"files");
                        fileRepoDtos.add(fileRepoDto);
                        FileRepoEventDto eventDto = new FileRepoEventDto();
                        eventDto.setFileRepoList(fileRepoDtos);
                        eventDto.setEventRefNo(groupPath);
                        eventBusHelper.submitAsyncRequest(eventDto, submissionId, EventBusConsts.SERVICE_NAME_FILE_REPO,
                                EventBusConsts.OPERATION_BE_REC_DATA_COPY, groupPath, null);

/*

                        fileRepoDto.setId(split[0]);
                        fileRepoDto.setAuditTrailDto(intranet);
                        fileRepoDto.setFileName(fileName.toString());
                        fileRepoDto.setRelativePath(sharedPath+File.separator+AppServicesConsts.COMPRESS
                                +File.separator+fileNames+File.separator+AppServicesConsts.FILE_NAME+File.separator+AppServicesConsts.FILES);
                        aBoolean = fileRepoClient.saveFiles(multipartFile, JsonUtil.parseToJson(fileRepoDto)).hasErrors();
*/

                        if(aBoolean){
                         /*   removeFilePath(f);*/
                        }
                    }catch (Exception e){
                        continue;
                    }

                }

            }

        }

    }



    private TaskHistoryDto getRoutingTaskForRequestForInformation(List<ApplicationDto> applicationDtos,AuditTrailDto auditTrailDto) throws Exception {
        log.debug(StringUtil.changeForLog("the do getRoutingTaskForRequestForInformation start ...."));
        TaskHistoryDto result = new TaskHistoryDto();
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> newApplicationDtos = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> rollBackApplicationDtos = IaisCommonUtils.genNewArrayList();

        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            log.debug(StringUtil.changeForLog("the applicationDtos size is-->"+applicationDtos.size()));
            List<RequestInformationSubmitDto> requestInformationSubmitDtos =  applicationService.getRequestInformationSubmitDtos(applicationDtos);
            if(!IaisCommonUtils.isEmpty(requestInformationSubmitDtos)){
                for(RequestInformationSubmitDto requestInformationSubmitDto : requestInformationSubmitDtos){
                    ApplicationDto applicationDto = requestInformationSubmitDto.getNewApplicationDto();
                    String appStatus = applicationDto.getStatus();
                    ApplicationDto oldApplicationDto = requestInformationSubmitDto.getOldApplicationDto();
                    AppPremisesRoutingHistoryDto reqeustAppPremisesRoutingHistoryDto = requestInformationSubmitDto.getReqeustAppPremisesRoutingHistoryDto();
                    List<AppPremisesCorrelationDto> oldAppPremisesCorrelationDtos =   requestInformationSubmitDto.getOldAppPremisesCorrelationDtos();
                    List<AppPremisesCorrelationDto> newAppPremisesCorrelationDtos =   requestInformationSubmitDto.getNewAppPremisesCorrelationDtos();
                    if(!IaisCommonUtils.isEmpty(oldAppPremisesCorrelationDtos)){
                        List<TaskDto> taskDtos =  licenceFileDownloadService.getTasksByRefNo(oldAppPremisesCorrelationDtos.get(0).getId());
                        if(!IaisCommonUtils.isEmpty(taskDtos)){
                            TaskDto taskDto = taskDtos.get(0);
                            TaskDto newTaskDto = TaskUtil.getUserTaskDto(taskDto.getTaskKey(),newAppPremisesCorrelationDtos.get(0).getId(),taskDto.getWkGrpId(),
                                    taskDto.getUserId(),0,taskDto.getProcessUrl(),taskDto.getRoleId(),auditTrailDto);
                            taskDtoList.add(newTaskDto);
                            //create history
                            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =
                                    createAppPremisesRoutingHistory(applicationDto.getApplicationNo(),appStatus,
                                            taskDto.getTaskKey(),null,taskDto.getRoleId(),auditTrailDto);
                            appPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
                            rollBackApplicationDtos.add(applicationDto);
                            rollBackApplicationDtos.add(oldApplicationDto);
                            //
                            ApplicationDto newApplicationDto = (ApplicationDto) CopyUtil.copyMutableObject(applicationDto);
                            newApplicationDto.setStatus(reqeustAppPremisesRoutingHistoryDto.getAppStatus());
                            ApplicationDto oldApplicationDto1 = (ApplicationDto)CopyUtil.copyMutableObject(oldApplicationDto);
                            oldApplicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_DELETED);
                            newApplicationDtos.add(newApplicationDto);
                            newApplicationDtos.add(oldApplicationDto1);
                            //
                            result.setTaskDtoList(taskDtoList);
                            result.setAppPremisesRoutingHistoryDtos(appPremisesRoutingHistoryDtos);
                            result.setApplicationDtos(applicationDtos);
                            result.setRollBackApplicationDtos(rollBackApplicationDtos);
                        }
                    }
                }
            }
        }else{
            log.debug(StringUtil.changeForLog("There are not reqest information application"));
        }
        log.debug(StringUtil.changeForLog("the do getRoutingTaskForRequestForInformation end ...."));
        return  result;
    }
    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                         String stageId, String internalRemarks,String roleId,
                                                                         AuditTrailDto auditTrailDto){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(auditTrailDto.getMohUserGuid());
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(auditTrailDto);
        return appPremisesRoutingHistoryDto;
    }



    private void  sendTask(List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList,String submissionId) throws  Exception{
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto("INTRANET");

        TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(listApplicationDto, HcsaConsts.ROUTING_STAGE_ASO, RoleConsts.USER_ROLE_ASO,intranet);
        //for reqeust for information
        TaskHistoryDto requestTaskHistoryDto  = getRoutingTaskForRequestForInformation(requestForInfList,intranet);
        //
        if(taskHistoryDto!=null || requestTaskHistoryDto!=null){
            BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
            BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
            broadcastOrganizationDto.setAuditTrailDto(intranet);
            broadcastApplicationDto.setAuditTrailDto(intranet);
            String evenRefNum = String.valueOf(System.currentTimeMillis());
            broadcastOrganizationDto.setEventRefNo(evenRefNum);
            broadcastApplicationDto.setEventRefNo(evenRefNum);

            List<TaskDto> onSubmitTaskList = IaisCommonUtils.genNewArrayList();
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = IaisCommonUtils.genNewArrayList();
            if(taskHistoryDto!=null){
                if(!IaisCommonUtils.isEmpty(taskHistoryDto.getTaskDtoList())){
                    onSubmitTaskList.addAll(taskHistoryDto.getTaskDtoList());
                }
                if(!IaisCommonUtils.isEmpty(taskHistoryDto.getAppPremisesRoutingHistoryDtos())){
                    appPremisesRoutingHistoryDtos.addAll(taskHistoryDto.getAppPremisesRoutingHistoryDtos());
                }
            }
            if(requestTaskHistoryDto!=null){
                if(!IaisCommonUtils.isEmpty(requestTaskHistoryDto.getTaskDtoList())){
                    onSubmitTaskList.addAll(requestTaskHistoryDto.getTaskDtoList());
                }
                if(!IaisCommonUtils.isEmpty(requestTaskHistoryDto.getAppPremisesRoutingHistoryDtos())){
                    appPremisesRoutingHistoryDtos.addAll(requestTaskHistoryDto.getAppPremisesRoutingHistoryDtos());
                }
                broadcastApplicationDto.setApplicationDtos(requestTaskHistoryDto.getApplicationDtos());
                broadcastApplicationDto.setRollBackApplicationDtos(requestTaskHistoryDto.getRollBackApplicationDtos());
            }
            broadcastOrganizationDto.setOneSubmitTaskList(onSubmitTaskList);
            broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
            broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,null,submissionId);
            broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,null,submissionId);

        }

    }


    private void  moveFile(File file){
        String name = file.getName();
        File moveFile=new File(sharedPath+File.separator+"move"+File.separator+name);
        try (FileOutputStream fileOutputStream=new FileOutputStream(moveFile);
        FileInputStream fileInputStream=new FileInputStream(file)) {
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

        file.delete();

    }



    private void requeOrNew(List<ApplicationDto> requestForInforList, List<ApplicationGroupDto> applicationGroup,List<ApplicationDto> dtoList) {

        Map<String,List<ApplicationDto>> map=IaisCommonUtils.genNewHashMap();
        for (ApplicationGroupDto applicationGroupDto : applicationGroup) {
            List<ApplicationDto> list=IaisCommonUtils.genNewArrayList();
            for (ApplicationDto applicationDto : dtoList) {
                if (applicationGroupDto.getId().equals(applicationDto.getAppGrpId())) {
                    list.add(applicationDto);
                }

            }
            map.put(applicationGroupDto.getId(),list);
        }
        map.forEach((k,v)->{
            for(ApplicationDto application :v){
                if(application.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY)){
                    requestForInforList.add(application);
                }
            }
        });
    }

    private void update(List<ApplicationDto> list,List<ApplicationGroupDto> applicationGroup,List<ApplicationDto>  applicationList){

        List<String> idList=IaisCommonUtils.genNewArrayList();
        for(ApplicationDto every:applicationList){
            idList.add(every.getId());
        }
        Map<ApplicationGroupDto,List<ApplicationDto>> map=IaisCommonUtils.genNewHashMap();
        for (ApplicationGroupDto every : applicationGroup) {
            List<ApplicationDto> applicationslist=IaisCommonUtils.genNewArrayList();

            for (ApplicationDto application : applicationList) {
                if (every.getId().equals(application.getAppGrpId())) {
                    applicationslist.add(application);
                }
            }
            map.put(every,applicationslist);
        }

        map.forEach((k,v)->{
            int j=0;
            int requestForChange=0;
            int reNew=0;
            Boolean autoRfc = k.isAutoApprove();

            String appType = k.getAppType();
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
                if(autoRfc) {
                    k.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                }else {

                }
                for(ApplicationDto application :v){
                    if (autoRfc) {
                     /*   application.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                        application.setApplicationType(ApplicationConsts.APPLICATION_STATUS_APPROVED);*/
                    }else {
                        application.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);

                    }
                    int i=v.size();
                    if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(application.getApplicationType())){
                        requestForChange++;
                    }else if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY.equals(application.getStatus())){
                        requestForChange--;
                    }
                    if(requestForChange==i){

                        if(!autoRfc){
                            list.addAll(v);
                        }

                    }
                }

            }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                if(autoRfc) {
                    k.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                }else {

                }
                for(ApplicationDto application:v){
                    if (autoRfc) {
                       /* application.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                        application.setApplicationType(ApplicationConsts.APPLICATION_STATUS_APPROVED);*/
                    }else {
                        application.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);

                    }
                    int i=v.size();
                    if(application.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_RENEWAL)){
                        reNew++;
                    }else if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY.equals(application.getStatus())){
                        reNew--;
                    }

                    if(reNew==i){

                        if(!autoRfc){
                            //all reNew application
                            list.addAll(v);
                        }
                    }
                }


            }else {
                for(ApplicationDto application :v){
                    int i=v.size();
                    if(application.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING)){
                        j++;
                    }
                    if(j==i){ list.addAll(v); }
                }
            }

        });


    }


}
