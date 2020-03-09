package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
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
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
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
    @Override
    public boolean compress() throws Exception{
        log.info("-------------compress start ---------");
        if(new File(backups).isDirectory()){
            File[] files = new File(backups).listFiles();
            for(File fil:files){
                if(fil.getName().endsWith(".zip")){

                    String name = fil.getName();
                    String path = fil.getPath();
                    String relPath="backups"+File.separator+name;
                    HashMap<String,String> map=new HashMap<>();
                    map.put("fileName",name);
                    map.put("filePath",relPath);

                    ProcessFileTrackDto processFileTrackDto = systemClient.isFileExistence(map).getEntity();
                    if(processFileTrackDto!=null){
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

                            this.download(processFileTrackDto,listApplicationDto, requestForInfList,name,refId);
                            sendTask(listApplicationDto,requestForInfList);
                            //save success
                        }catch (Exception e){
                            //save bad
                            log.error(e.getMessage(),e);
                            continue;
                        }

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

        if(!file.exists()){
            file.mkdirs();
        }

    }

    public Boolean  download( ProcessFileTrackDto processFileTrackDto,List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList,String fileName
    ,String groupPath)  throws Exception {

        Boolean flag=false;

            File file =new File(compressPath+File.separator+fileName+File.separator+groupPath+File.separator+"folder"+File.separator+groupPath);
            if(!file.exists()){
                file.mkdirs();
            }
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for(File  filzz:files){
                    if(filzz.isFile() &&filzz.getName().endsWith(fileFormat)){
                        try (  FileInputStream  fileInputStream =new FileInputStream(filzz);
                                ByteArrayOutputStream by=new ByteArrayOutputStream();) {

                            int count=0;
                            byte [] size=new byte[1024];
                            count=fileInputStream.read(size);
                            while(count!=-1){
                                by.write(size,0,count);
                                count= fileInputStream.read(size);
                            }

                            Boolean aBoolean = fileToDto(by.toString(), listApplicationDto, requestForInfList,processFileTrackDto);

                           flag=aBoolean;
                            if(aBoolean){
                                if(processFileTrackDto!=null){

                                    changeStatus(processFileTrackDto);

                                    saveFileRepo( fileName,groupPath);
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
    private void changeStatus( ProcessFileTrackDto processFileTrackDto){
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        AuditTrailDto batchJobDto = AuditTrailHelper.getBatchJobDto("INTRANET");
        processFileTrackDto.setAuditTrailDto(batchJobDto);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_COMPLETE);
        systemClient.updateProcessFileTrack(processFileTrackDto);

    }



        private void zipFile( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos,String fileName
        ,String groupPath)  {


            try {
                if(!zipEntry.getName().endsWith(File.separator)){

                    String substring = zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator));
                    File file =new File(compressPath+File.separator+fileName+File.separator+groupPath+File.separator+substring);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    os=new FileOutputStream(compressPath+File.separator+fileName+File.separator+groupPath+File.separator+zipEntry.getName());
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

                    new File(compressPath+File.separator+fileName+File.separator+groupPath+File.separator+zipEntry.getName()).mkdirs();
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




    private Boolean fileToDto(String str,List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList,ProcessFileTrackDto processFileTrackDto)
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
        Boolean flag=false;
        try {
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
        }

        return flag;

    }



    /*
    *
    * save file to fileRepro*/
    private void saveFileRepo(String fileNames,String groupPath){
        boolean aBoolean=false;
        File file =new File(compressPath+File.separator+fileNames+File.separator+groupPath+File.separator+"folder"+File.separator+"files");
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
                        try ( InputStream input = new FileInputStream(f)){
                            if(fileItem!=null){
                                OutputStream os = fileItem.getOutputStream();
                                IOUtils.copy(input, os);
                            }
                        } catch (IOException ex) {
                           log.error(ex.getMessage(),ex);
                        }
                        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

                        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto("intranet");
                        FileRepoDto fileRepoDto = new FileRepoDto();
                        fileRepoDto.setId(split[0]);
                        fileRepoDto.setAuditTrailDto(intranet);
                        fileRepoDto.setFileName(fileName.toString());
                        fileRepoDto.setRelativePath(compressPath+File.separator+fileNames+File.separator+"folder"+File.separator+"files");
                        aBoolean = fileRepoClient.saveFiles(multipartFile, JsonUtil.parseToJson(fileRepoDto)).hasErrors();

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
        List<TaskDto> taskDtoList = new ArrayList<>();
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = new ArrayList<>();
        List<ApplicationDto> newApplicationDtos = new ArrayList<>();
        List<ApplicationDto> rollBackApplicationDtos = new ArrayList<>();

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



    private void  sendTask(List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList) throws  Exception{
        licenceFileDownloadService.initPath();
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

            List<TaskDto> onSubmitTaskList = new ArrayList<>();
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = new ArrayList<>();
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
            broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,null);
            broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,null);

        }

    }
}
