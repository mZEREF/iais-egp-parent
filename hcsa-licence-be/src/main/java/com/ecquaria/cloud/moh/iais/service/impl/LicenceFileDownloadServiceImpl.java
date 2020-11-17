package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationNewAndRequstDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GobalRiskAccpetDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.EventClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.kafka.model.Submission;
import com.ecquaria.sz.commons.util.FileUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    @Value("${iais.sharedfolder.application.in}")
    private String inSharedPath;

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
    private EventClient eventClient;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private EmailClient emailClient;

    @Autowired
    private ApplicationGroupService applicationGroupService;

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;
    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;
    @Override
    public boolean decompression() {
        log.info("-------------decompression start ---------");
        String inFolder = inSharedPath;
        if (!inFolder.endsWith(File.separator)) {
            inFolder += File.separator;
        }
        File[] files = new File(inFolder).listFiles();
        for(File fil:files){
            if(fil.getName().endsWith(AppServicesConsts.ZIP_NAME)){
                String name = fil.getName();
                String path = fil.getPath();
                String relPath = name;
                HashMap<String,String> map = IaisCommonUtils.genNewHashMap();
                map.put("fileName",name);
                map.put("filePath",relPath);
                ProcessFileTrackDto processFileTrackDto = applicationClient.isFileExistence(map).getEntity();
                if(processFileTrackDto!=null){
                    AuditTrailDto intranet = AuditTrailHelper.getCurrentAuditTrailDto();
                    processFileTrackDto.setAuditTrailDto(intranet);
                    processFileTrackDto.setStatus("PFT002");
                    try {
                        applicationClient.updateProcessFileTrack(processFileTrackDto);
                    }catch (Exception e){
                        log.info("error updateProcessFileTrack");
                    }

                    //check file is changed
                    try (InputStream is=Files.newInputStream(fil.toPath());
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
                        download(processFileTrackDto,listApplicationDto, requestForInfList,name,refId,submissionId);

                        log.info(StringUtil.changeForLog(listApplicationDto.size()+"******listApplicationDto*********"));
                        log.info(StringUtil.changeForLog(requestForInfList.toString()+"***requestForInfList***"));

                    /*    sendTask(listApplicationDto,requestForInfList,submissionId);*/
                     /*   moveFile(fil);*/
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
            return Boolean.TRUE;
        }else if(status==500){
            return Boolean.FALSE;
        }
        return Boolean.FALSE;
    }

    @Override
    public List<TaskDto> getTasksByRefNo(String refNo) {
        return organizationClient.getTasksByRefNo(refNo).getEntity();

    }
    @Override
    public void initPath() {

        File compress =new File(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+AppServicesConsts.FILE_NAME);
        File backups=new File(inSharedPath);
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

        Boolean flag=Boolean.FALSE;

            File file =new File(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+
                    File.separator+groupPath+File.separator+AppServicesConsts.FILE_NAME+File.separator+groupPath);
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
                            fileInputStream=Files.newInputStream(filzz.toPath());
                            ByteArrayOutputStream by=new ByteArrayOutputStream();
                            int count=0;
                            byte [] size=new byte[1024];
                            count=fileInputStream.read(size);
                            while(count!=-1){
                                by.write(size,0,count);
                                count= fileInputStream.read(size);
                            }
                            Long l = System.currentTimeMillis();
                            fileToDto(by.toString(), listApplicationDto, requestForInfList,processFileTrackDto,submissionId,l);
                            saveFileRepo( fileName,groupPath,submissionId,l);
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


        private void zipFile( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos,String fileName
        ,String groupPath)  {


            try {
                if(!zipEntry.getName().endsWith(File.separator)){

                    String substring = zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator));
                    File file =new File( sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+File.separator+groupPath+File.separator+substring);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    os=Files.newOutputStream(Paths.get(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+File.separator+groupPath+File.separator+zipEntry.getName()));
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
                              String submissionId,Long l)
           {
        AuditTrailDto intranet = AuditTrailHelper.getCurrentAuditTrailDto();
        ApplicationListFileDto applicationListDto = JsonUtil.parseToObject(str, ApplicationListFileDto.class);


        List<ApplicationDto> application = applicationListDto.getApplication();
        List<GobalRiskAccpetDto> accpetDtos=IaisCommonUtils.genNewArrayList();
        for(ApplicationDto every:application){
            GobalRiskAccpetDto gobalRiskAccpetDto=new GobalRiskAccpetDto();
            gobalRiskAccpetDto.setServiceId(every.getServiceId());
            gobalRiskAccpetDto.setOldLicId(every.getOriginLicenceId());
            gobalRiskAccpetDto.setAppType(every.getApplicationType());
            accpetDtos.add(gobalRiskAccpetDto);
            every.setAuditTrailDto(intranet);
        }
        GobalRiskAccpetDto gobalRiskAccpetDtos=new GobalRiskAccpetDto();
        gobalRiskAccpetDtos.setMoreSvc(true);
        gobalRiskAccpetDtos.setGobalRiskAccpetDtos(accpetDtos);
        try {
            GobalRiskAccpetDto entity = hcsaConfigClient.getGobalRiskAccpetDtoByGobalRiskAccpetDto(gobalRiskAccpetDtos).getEntity();
            String isPreInspect = entity.getIsPreInspect();
            log.info(StringUtil.changeForLog(isPreInspect + "isPreInspect"));
            for(ApplicationGroupDto applicationGroupDto : applicationListDto.getApplicationGroup()){
                if(HcsaConsts.HCSA_REQUIRED_PRE_LICENSING_INSPECTION.equals(isPreInspect)){
                    applicationGroupDto.setIsPreInspection(1);
                }else if(HcsaConsts.HCSA_REQUIRED_POST_LICENSING_INSPECTION.equals(isPreInspect)){
                    applicationGroupDto.setIsPreInspection(0);
                }
            }
        }catch (Throwable e){
            log.error("------gobalRiskAccpetDtos is error",e);
        }

        List<ApplicationGroupDto> applicationGroup = applicationListDto.getApplicationGroup();

        List<AppPremisesCorrelationDto> appPremisesCorrelation = applicationListDto.getAppPremisesCorrelation();
        for(ApplicationDto applicationDto : application){
            String id = applicationDto.getId();
            for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelation){
                String applicationId = appPremisesCorrelationDto.getApplicationId();
                if(id.equals(applicationId)){
                    HcsaRiskScoreDto hcsaRiskScoreDto=new HcsaRiskScoreDto();
                    hcsaRiskScoreDto.setAppType(applicationDto.getApplicationType());
                    hcsaRiskScoreDto.setServiceId(applicationDto.getServiceId());
                    hcsaRiskScoreDto.setLicId(applicationDto.getOriginLicenceId());
                    try {
                        HcsaRiskScoreDto entity = hcsaConfigClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
                        appPremisesCorrelationDto.setRiskScore(entity.getRiskScore());
                        appPremisesCorrelationDto.setRiskCalcDate(new Date());
                        log.info(StringUtil.changeForLog(" getHcsaRiskScoreDtoByHcsaRiskScoreDto ok" + entity.getRiskScore()));
                    }catch (Throwable e){
                        log.error("--------getHcsaRiskScoreDtoByHcsaRiskScoreDto is error ",e);
                    }
                }
            }
        }

        applicationListDto.setAuditTrailDto(intranet);
        List<ApplicationDto> updateTaskList=IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> cessionOrwith=IaisCommonUtils.genNewArrayList();
        requeOrNew(requestForInfList,applicationGroup,application,updateTaskList);
        update(cessionOrwith,listApplicationDto,applicationGroup,application);
        log.info(StringUtil.changeForLog(listApplicationDto.toString()+"listApplicationDto size "+listApplicationDto.size()));
        String requestForInfListString = requestForInfList.toString();
        log.info(StringUtil.changeForLog(requestForInfListString +"requestForInfList size" +requestForInfList .size()));
        log.info(StringUtil.changeForLog(requestForInfListString +"updateTaskList size" +updateTaskList .size()));
        log.info(StringUtil.changeForLog(requestForInfListString +"cessionOrwith size" +cessionOrwith .size()));
        ApplicationNewAndRequstDto applicationNewAndRequstDto=new ApplicationNewAndRequstDto();
        applicationNewAndRequstDto.setListNewApplicationDto(listApplicationDto);
        applicationNewAndRequstDto.setRequestForInfList(requestForInfList);
        applicationNewAndRequstDto.setUpdateTaskList(updateTaskList);
        applicationNewAndRequstDto.setCessionOrWith(cessionOrwith);
        applicationNewAndRequstDto.setEventNo(l);
        applicationNewAndRequstDto.setZipFileName(processFileTrackDto.getFileName());
        applicationNewAndRequstDto.setAuditTrailDto(intranet);
        applicationNewAndRequstDto.setProcessFileTrackDto(processFileTrackDto);
        applicationListDto.setApplicationNewAndRequstDto(applicationNewAndRequstDto);
        processFileTrackDto.setStatus("PFT003");
        applicationListDto.setProcessFileTrackDto(processFileTrackDto);
        applicationListDto.setEventRefNo(l.toString());
        applicationListDto.setRefNo(l.toString());
        log.info("update be application status");

        eventBusHelper.submitAsyncRequest(applicationListDto,submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_SAVE_GROUP_APPLICATION,l.toString(),null);

        return Boolean.TRUE;

    }



    /*
    *
    * save file to fileRepro*/
    private void saveFileRepo(String fileNames,String groupPath,String submissionId,Long l){
        File file =new File(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileNames+File.separator+groupPath+File.separator+"folder"+File.separator+groupPath+File.separator+"files");
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
            Boolean flag=Boolean.FALSE;
            for(File f:files){
                if(f.isFile()){
                    try {
                        StringBuilder fileName=new StringBuilder();
                        String[] split = f.getName().split("@");
                        for(int i=1;i<split.length;i++){
                            fileName.append(split[i]);
                        }

                        FileRepoDto fileRepoDto = new FileRepoDto();

                        fileRepoDto.setId(split[0]);
                        fileRepoDto.setAuditTrailDto(intranet);
                        fileRepoDto.setFileName(f.getName());
                        fileRepoDto.setRelativePath(AppServicesConsts.COMPRESS+File.separator+fileNames+
                                File.separator+groupPath+File.separator+"folder"+File.separator+groupPath+File.separator+"files");
                        fileRepoDtos.add(fileRepoDto);
                        eventDto.setFileRepoList(fileRepoDtos);
                        flag=Boolean.TRUE;
                        log.info(StringUtil.changeForLog(f.getPath()+"file path"));

                    }catch (Exception e){
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
                            TaskDto newTaskDto = TaskUtil.getUserTaskDto(applicationDto.getApplicationNo(),taskDto.getTaskKey(),newAppPremisesCorrelationDtos.get(0).getId(),taskDto.getWkGrpId(),
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
        appPremisesRoutingHistoryDto.setActionby(auditTrailDto == null ?
                AppConsts.USER_ID_SYSTEM : auditTrailDto.getMohUserGuid());
        appPremisesRoutingHistoryDto.setRoleId(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN);
        appPremisesRoutingHistoryDto.setAuditTrailDto(auditTrailDto);
        return appPremisesRoutingHistoryDto;
    }

        private void updateApplication(List<ApplicationDto> applicationDtos){
            EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, LicenceFileDownloadServiceImpl.class.getName(),
                    "saveFileName", currentApp + "-" + currentDomain,
                    ProcessFileTrackDto.class.getName(), JsonUtil.parseToJson(applicationDtos));

        }

        public void  sendTask(String eventRefNum ,String submissionId) throws  Exception{

        AuditTrailDto intranet =new AuditTrailDto();
        ApplicationNewAndRequstDto applicationNewAndRequstDto=new ApplicationNewAndRequstDto();
        List<ApplicationDto> listNewApplicationDto =IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> requestForInfList  =IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> updateTaskList  =IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> cessionOrwith=IaisCommonUtils.genNewArrayList();
        Long eventNo=System.currentTimeMillis();
            List<Submission> submissionList = eventClient.getSubmission(submissionId).getEntity();
            ApplicationListFileDto dto = null;
            log.info(StringUtil.changeForLog(submissionList .size() +"submissionList .size()"));
            for(Submission submission : submissionList){
               if(EventBusConsts.SERVICE_NAME_APPSUBMIT.equals(submission.getSubmissionIdentity().getService())){
                    dto = JsonUtil.parseToObject(submission.getData(), ApplicationListFileDto.class);
                    break;
               }
            }
            if(dto!=null){
                 applicationNewAndRequstDto = dto.getApplicationNewAndRequstDto();
                if(applicationNewAndRequstDto!=null){
                  eventNo = applicationNewAndRequstDto.getEventNo();
                  intranet=applicationNewAndRequstDto.getAuditTrailDto();
                  listNewApplicationDto = applicationNewAndRequstDto.getListNewApplicationDto();
                  requestForInfList = applicationNewAndRequstDto.getRequestForInfList();
                  cessionOrwith=applicationNewAndRequstDto.getCessionOrWith();
                  log.info(StringUtil.changeForLog("cessionOrwith "+cessionOrwith.size()));
                  log.info(StringUtil.changeForLog("listNewApplicationDto size"+listNewApplicationDto.size()));
                  log.info(StringUtil.changeForLog("requestForInfList size"+requestForInfList.size()));
                  log.info("update requeste Application status");
                  for(ApplicationDto applicationDto : requestForInfList){
                      applicationDto.setAuditTrailDto(intranet);
                  }
                  requestForInfList= applicationClient.updateApplicationOfRfi(requestForInfList).getEntity();
                  log.info(StringUtil.changeForLog(JsonUtil.parseToJson(requestForInfList)));
                  updateTaskList = applicationNewAndRequstDto.getUpdateTaskList();
                }
            }
            log.info(StringUtil.changeForLog(listNewApplicationDto.size()+"listNewApplicationDto size"));
            log.info(StringUtil.changeForLog(requestForInfList.size()+"requestForInfList size"));
        TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(listNewApplicationDto, HcsaConsts.ROUTING_STAGE_ASO, RoleConsts.USER_ROLE_ASO,intranet,RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN);
        //for reqeust for information
        TaskHistoryDto requestTaskHistoryDto  = getRoutingTaskForRequestForInformation(requestForInfList,intranet);
        //
        if(taskHistoryDto!=null || requestTaskHistoryDto!=null){
            BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
            BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
            broadcastOrganizationDto.setAuditTrailDto(intranet);
            broadcastOrganizationDto.setApplicationNewAndRequstDto(applicationNewAndRequstDto);

            broadcastApplicationDto.setAuditTrailDto(intranet);
            broadcastOrganizationDto.setEventRefNo(eventNo.toString());
            broadcastApplicationDto.setEventRefNo(eventNo.toString());
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
            try {
                List<TaskDto> taskDtos = sendCessionOrWithdrawal(cessionOrwith,intranet, appPremisesRoutingHistoryDtos);
                log.info(StringUtil.changeForLog("cession task"+JsonUtil.parseToJson(taskDtos)));
                TaskHistoryDto taskHistoryDtoCessionOrWithDrawal  = getRoutingTaskForRequestForInformation(cessionOrwith,intranet);
                onSubmitTaskList.addAll(taskDtos);
                if(taskHistoryDtoCessionOrWithDrawal != null && !IaisCommonUtils.isEmpty(taskHistoryDtoCessionOrWithDrawal.getAppPremisesRoutingHistoryDtos())){
                    appPremisesRoutingHistoryDtos.addAll(taskHistoryDtoCessionOrWithDrawal.getAppPremisesRoutingHistoryDtos());
                }
            }catch (Exception e){
                log.info(StringUtil.changeForLog("cession error"+e.getMessage()));
            }

            broadcastOrganizationDto.setOneSubmitTaskList(onSubmitTaskList);
            broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
            broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,null,submissionId);
            broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,null,submissionId);
            //update fe application stauts
            log.info("update application stauts");
            for(ApplicationDto applicationDto :requestForInfList){
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                beEicGatewayClient.updateApplication(applicationDto,signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization());
            }
            log.info("update request for info start");
            updateRfiTask(requestForInfList);
            log.info("update request for info end");
            log.info(StringUtil.changeForLog(JsonUtil.parseToJson(updateTaskList)+"updateTaskList"));
            updateRfiTask(updateTaskList);
        }
    }

    public void  removeFile(String eventRefNum ,String submissionId){
        List<Submission> submissionList = eventClient.getSubmission(submissionId).getEntity();
        log.info(StringUtil.changeForLog(submissionList .size() +"remove file submissionList .size()"));
        BroadcastOrganizationDto broadcastOrganizationDto =null;
        for(Submission submission : submissionList){
            if(EventBusConsts.SERVICE_NAME_ROUNTINGTASK.equals(submission.getSubmissionIdentity().getService())){
                broadcastOrganizationDto = JsonUtil.parseToObject(submission.getData(), BroadcastOrganizationDto.class);
                break;
            }
        }
        if(broadcastOrganizationDto!=null){
            ApplicationNewAndRequstDto applicationNewAndRequstDto = broadcastOrganizationDto.getApplicationNewAndRequstDto();
            String zipFileName = applicationNewAndRequstDto.getZipFileName();
            log.info(StringUtil.changeForLog(JsonUtil.parseToJson(applicationNewAndRequstDto)+"---applicationNewAndRequstDto-----"));
            String inFolder = inSharedPath;
            if (!inFolder.endsWith(File.separator)) {
                inFolder += File.separator;
            }
            File file =new File(inFolder+zipFileName);
            log.info("start remove file start");
            moveFile(file);
            log.info("update file track start");
            ProcessFileTrackDto processFileTrackDto = applicationNewAndRequstDto.getProcessFileTrackDto();
            processFileTrackDto.setStatus("PFT005");
            applicationClient.updateProcessFileTrack(processFileTrackDto);
            /**
             * Send Email
             */
            List<ApplicationDto> applicationDtoList = applicationNewAndRequstDto.getCessionOrWith();
            if (applicationDtoList != null && applicationDtoList.size() > 0){
                applicationDtoList.forEach(h -> {
                    String applicationNo = h.getApplicationNo();
                    String applicationGrpId = h.getAppGrpId();
                    String applicationType = h.getApplicationType();
                    String serviceId = h.getServiceId();
                    String serviceName = HcsaServiceCacheHelper.getServiceById(serviceId).getSvcName();
                    ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationGrpId);
                    LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(applicationGroupDto.getLicenseeId()).getEntity();
                    try {
                        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(h.getAppPremisesCorrelationId());
                        AppGrpPremisesDto appGrpPremisesDto = inspectionAssignTaskService.getAppGrpPremisesDtoByAppGroId(appPremisesCorrelationDto.getNewCorrelationId());
                        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
                        if (ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType)){
                            Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                            msgInfoMap.put("systemLink",loginUrl);
                            msgInfoMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationType));
                            msgInfoMap.put("ApplicationNumber", applicationNo);
                            msgInfoMap.put("HCIName",appGrpPremisesDto.getHciName());
                            msgInfoMap.put("Address",appGrpPremisesDto.getAddress());
                            msgInfoMap.put("licenseeName",licenseeDto.getName());
                            msgInfoMap.put("submissionDate",Formatter.formatDateTime(applicationGroupDto.getSubmitDt()));
                            msgInfoMap.put("ApplicationDate",Formatter.formatDateTime(new Date()));
                            msgInfoMap.put("S_LName",serviceName);
                            msgInfoMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
                            try {
                                sendEmail(MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_ASO_EMAIL,msgInfoMap,h);
                                sendEmail(MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_ASO_MESSAGE,msgInfoMap,h);
                                sendEmail(MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_ASO_SMS,msgInfoMap,h);
                            } catch (IOException | TemplateException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }catch (Exception e){
                        log.info(e.getMessage(),e);
                    }
                });
            }
            List<ApplicationDto> applicationDtoListRfc = applicationNewAndRequstDto.getListNewApplicationDto();
            if (applicationDtoListRfc != null && applicationDtoListRfc.size() > 0){
                applicationDtoListRfc.forEach(h -> {
                    String applicationGrpId = h.getAppGrpId();
                    String serviceId = h.getServiceId();
                    String serviceName = HcsaServiceCacheHelper.getServiceById(serviceId).getSvcName();
                    ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationGrpId);
                    LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(applicationGroupDto.getLicenseeId()).getEntity();

                    try {
                        if(h.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE) && applicationGroupDto.isAutoApprove()){
                            LicenceDto licenceDto=hcsaLicenceClient.getLicenceDtoById(h.getOriginLicenceId()).getEntity();
                            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                            emailMap.put("officer_name", "");
                            emailMap.put("ServiceLicenceName", serviceName);
                            emailMap.put("ApplicationDate", Formatter.formatDate(applicationGroupDto.getSubmitDt()));
                            emailMap.put("Licensee", licenseeDto.getName());
                            emailMap.put("LicenceNumber", licenceDto.getLicenceNo());
                            StringBuilder stringBuilder = new StringBuilder();
                            List<AppEditSelectDto> appEditSelectDtos = applicationService.getAppEditSelectDtos(h.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFC);
                            if (appEditSelectDtos.get(0).isServiceEdit()){
                                stringBuilder.append("<p class=\"line\">   ").append("Remove subsumed service").append("</p>");
                            }
                            if (applicationGroupDto.getNewLicenseeId()!=null){
                                stringBuilder.append("<p class=\"line\">   ").append("Change in Management of Licensee").append("</p>");
                            }
                            emailMap.put("ServiceNames", stringBuilder);
                            emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
                            emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
                            EmailParam emailParam = new EmailParam();
                            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER);
                            emailParam.setQueryCode(h.getApplicationNo());
                            emailParam.setReqRefNum(h.getApplicationNo());
                            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                            emailParam.setRefId(h.getApplicationNo());
                            emailParam.setTemplateContent(emailMap);
                            MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER).getEntity();
                            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                            map.put("ApplicationType", MasterCodeUtil.getCodeDesc(h.getApplicationType()));
                            map.put("ApplicationNumber", h.getApplicationNo());
                            String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map);
                            emailParam.setSubject(subject);
                            log.info("start send email start");
                            notificationHelper.sendNotification(emailParam);
                            log.info("start send email end");
                            //emailClient.sendNotification(emailDto).getEntity();

                            //sms
                            emailMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(h.getApplicationType()));
                            emailMap.put("ApplicationNumber", h.getApplicationNo());
                            emailParam.setTemplateContent(emailMap);
                            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER_SMS);
                            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                            log.info("start send sms start");
                            notificationHelper.sendNotification(emailParam);
                            log.info("start send sms end");
                        }

                    }catch (Exception e ){
                        log.info(e.getMessage(),e);
                    }


                });
            }

        }

    }

    private void sendEmail(String templateId, Map<String, Object> msgInfoMap, ApplicationDto applicationDto) throws IOException, TemplateException {
        EmailParam emailParam = new EmailParam();
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(templateId).getEntity();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map);
        emailParam.setTemplateContent(msgInfoMap);
        emailParam.setTemplateId(templateId);
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setSubject(subject);
        notificationHelper.sendNotification(emailParam);
    }

    private void  moveFile(File file){
        String name = file.getName();
        File moveFile=new File(sharedPath+File.separator+"move"+File.separator+name);
        try (OutputStream fileOutputStream=Files.newOutputStream(Paths.get( moveFile.getPath()));
             InputStream fileInputStream=Files.newInputStream(file.toPath())) {
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


    private void requeOrNew(List<ApplicationDto> requestForInforList, List<ApplicationGroupDto> applicationGroup,List<ApplicationDto> dtoList,List<ApplicationDto> updateTaskList) {

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
            boolean flag=false;
            for(ApplicationDto application :v){
                if(application.getStatus().equals(ApplicationConsts.PENDING_ASO_REPLY)||application.getStatus().equals(ApplicationConsts.PENDING_PSO_REPLY)
                ||application.getStatus().equals(ApplicationConsts.PENDING_INP_REPLY)){
                    requestForInforList.add(application);
                    flag=true;
                }
            }
            if(flag){
                updateTaskList.addAll(v);
            }
        });
        updateTaskList.removeAll(requestForInforList);
    }

    private void update( List<ApplicationDto> cessionOrwith,List<ApplicationDto> list,List<ApplicationGroupDto> applicationGroup,List<ApplicationDto>  applicationList){

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
            int  cession=0;
            Boolean autoRfc = k.isAutoApprove();
            String appType = k.getAppType();
            if(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(appType) || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType)){
                if(autoRfc){
                    k.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                }
                List<ApplicationDto> applicationDtoList=IaisCommonUtils.genNewArrayList();
                for(ApplicationDto application :v){
                    if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType)&&ApplicationConsts.APPLICATION_STATUS_CESSATION_SPEC_NEED_LICENCE.equals(application.getStatus())){
                        application.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
                    }else if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType)&&ApplicationConsts.APPLICATION_STATUS_CESSATION_SPEC_NOT_LICENCE.equals(application.getStatus())){
                        application.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NOT_LICENCE);
                    }else if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType)&&ApplicationConsts.APPLICATION_STATUS_CESSATION_SPEC_TEMPORARY_LICENCE.equals(application.getStatus())){
                        application.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_TEMPORARY_LICENCE);
                    }

                    if (autoRfc) {
                        application.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                    }else {

                    }
                    int i=v.size();
                    if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(application.getStatus())||
                            ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(application.getStatus())||
                            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION.equals(application.getStatus())||
                            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01.equals(application.getStatus())||
                            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(application.getStatus())||
                            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(application.getStatus())){
                        cession++;
                        applicationDtoList.add(application);
                    }else if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(application.getStatus())){
                        cession++;
                    }else if(ApplicationConsts.PENDING_ASO_REPLY.equals(application.getStatus())||ApplicationConsts.PENDING_PSO_REPLY.equals(application.getStatus())
                            ||ApplicationConsts.PENDING_INP_REPLY.equals(application.getStatus())){
                        cession--;
                    }
                    if(cession==i){
                        cessionOrwith.addAll(applicationDtoList);
                    }
                }
            } else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
                if(autoRfc) {
                    k.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                }else {

                }
                List<ApplicationDto> applicationDtoList=IaisCommonUtils.genNewArrayList();
                for(ApplicationDto application :v){
                    if (autoRfc) {
                        application.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                    }else {

                    }
                    int i=v.size();
                    if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(application.getStatus()) ){
                        requestForChange++;
                        applicationDtoList.add(application);
                    }else if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(application.getStatus())){
                        requestForChange++;
                    }else if(ApplicationConsts.PENDING_ASO_REPLY.equals(application.getStatus())||ApplicationConsts.PENDING_PSO_REPLY.equals(application.getStatus())
                    ||ApplicationConsts.PENDING_INP_REPLY.equals(application.getStatus())){
                        requestForChange--;
                    }
                    if(requestForChange==i){
                        if(!autoRfc){
                            list.addAll(applicationDtoList);
                        }
                    }
                }

            }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                if(autoRfc) {
                    k.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                }else {

                }
                List<ApplicationDto> applicationDtoList=IaisCommonUtils.genNewArrayList();
                for(ApplicationDto application:v){
                    if (autoRfc) {
                        application.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                    }else {

                    }
                    int i=v.size();
                    if(application.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING) ){
                        reNew++;
                        applicationDtoList.add(application);
                    }else if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(application.getStatus())){
                        reNew++;
                    }else if(ApplicationConsts.PENDING_ASO_REPLY.equals(application.getStatus())
                    ||ApplicationConsts.PENDING_PSO_REPLY.equals(application.getStatus())
                    ||ApplicationConsts.PENDING_INP_REPLY.equals(application.getStatus())){
                        reNew--;
                    }

                    if(reNew==i){
                        if(!autoRfc){
                            list.addAll(applicationDtoList);
                        }
                    }
                }


            }else {
                List<ApplicationDto> applicationDtoList=IaisCommonUtils.genNewArrayList();
                for(ApplicationDto application :v){
                    int i=v.size();
                    if(application.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING) ){
                        j++;
                        applicationDtoList.add(application);
                    }else if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(application.getStatus())){
                        j++;
                    }
                    if(j==i){ list.addAll(applicationDtoList); }
                }
            }
        });
    }

    private void updateRfiTask(List<ApplicationDto>  updateTaskList){
        log.info("update rfi task");
        if(updateTaskList==null){
            return;
        }
        AuditTrailDto intranet = AuditTrailHelper.getCurrentAuditTrailDto();
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(updateTaskList)+"updateTaskList"));
        for(ApplicationDto applicationDto : updateTaskList){
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity();
            log.info(StringUtil.changeForLog(JsonUtil.parseToJson(appPremisesCorrelationDtos)+"appPremisesCorrelationDtos"));
            List<TaskDto> taskbyApplicationNo = taskService.getTaskRfi(applicationDto.getApplicationNo());
            log.info(StringUtil.changeForLog(JsonUtil.parseToJson(taskbyApplicationNo)+"taskbyApplicationNo"));
            for(TaskDto taskDto : taskbyApplicationNo){
                for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
                    taskDto.setRefNo(appPremisesCorrelationDto.getId());
                }
                taskDto.setAuditTrailDto(intranet);
            }
            taskService.createTasks(taskbyApplicationNo);
        }
    }

    private List<TaskDto> sendCessionOrWithdrawal(List<ApplicationDto> applicationDtos,AuditTrailDto auditTrailDto, List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos) throws Exception {
        List<TaskDto> list=IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            List<ApplicationDto> applicationDtoList=new ArrayList<>(1);
            List<HcsaSvcRoutingStageDto> entity =
                    hcsaConfigClient.getHcsaSvcRoutingStageDtoByServiceAndType(applicationDto.getServiceId(), applicationDto.getApplicationType()).getEntity();
            if(entity.isEmpty()){
                return list;
            }
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
            if(appPremisesCorrelationDto!=null){
                TaskDto taskDto= taskService.getRoutingTask(applicationDto,entity.get(0).getStageId(),entity.get(0).getStageCode(),appPremisesCorrelationDto.getId());
                list.add(taskDto);
            }
            applicationDtoList.add(applicationDto);
            TaskHistoryDto routingTaskOneUserForSubmisison = taskService.getRoutingTaskOneUserForSubmisison(applicationDtoList, entity.get(0).getStageId(), entity.get(0).getStageCode(), auditTrailDto,RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN);
            log.info(StringUtil.changeForLog("----"+routingTaskOneUserForSubmisison));
            if(routingTaskOneUserForSubmisison!=null&&routingTaskOneUserForSubmisison.getAppPremisesRoutingHistoryDtos()!=null){
                log.info(StringUtil.changeForLog("----"+JsonUtil.parseToJson(routingTaskOneUserForSubmisison)));
                appPremisesRoutingHistoryDtos.addAll(routingTaskOneUserForSubmisison.getAppPremisesRoutingHistoryDtos());
            }
        }
        return list;
    }
}
