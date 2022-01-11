package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.action.HcsaApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
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
import com.ecquaria.cloud.moh.iais.service.AppGroupMiscService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.LicenceFileDownloadService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailHistoryCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.EventClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.systeminfo.ServicesSysteminfo;
import com.ecquaria.kafka.model.Submission;
import com.ecquaria.sz.commons.util.FileUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;


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
    private EmailSmsClient emailSmsClient;
    @Autowired
    private EmailHistoryCommonClient emailHistoryCommonClient;
    @Autowired
    private CessationClient cessationClient;

    @Autowired
    HcsaApplicationDelegator newApplicationDelegator;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Value("${spring.application.name}")
    private String currentApp;
    @Autowired
    private AppGroupMiscService appGroupMiscService;
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
                        download(v,listApplicationDto, requestForInfList,name,refId,submissionId);

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
    public void sendRfc008Email(ApplicationGroupDto applicationGroupDto, ApplicationDto application) throws IOException, TemplateException {
        String serviceName = HcsaServiceCacheHelper.getServiceById(application.getServiceId()).getSvcName();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(applicationGroupDto.getLicenseeId()).getEntity();
        if(application.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE) && applicationGroupDto.isAutoApprove()){
            LicenceDto licenceDto=hcsaLicenceClient.getLicenceDtoById(application.getOriginLicenceId()).getEntity();
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            emailMap.put("officer_name", "");
            emailMap.put("ServiceLicenceName", serviceName);
            emailMap.put("ApplicationDate", Formatter.formatDate(applicationGroupDto.getSubmitDt()));
            emailMap.put("Licensee", licenseeDto.getName());
            if(licenceDto!=null&&licenceDto.getLicenceNo()!=null){
                emailMap.put("LicenceNumber", licenceDto.getLicenceNo());
            }else {
                emailMap.put("LicenceNumber", "");
            }
            StringBuilder stringBuilder = new StringBuilder();
            List<AppEditSelectDto> appEditSelectDtos = applicationService.getAppEditSelectDtos(application.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFC);
            if(appEditSelectDtos!=null&&appEditSelectDtos.size()!=0){
                if (appEditSelectDtos.get(0).isServiceEdit()){
                    stringBuilder.append("<p class=\"line\">   ").append("Remove subsumed service").append("</p>");
                }
            }
            if (applicationGroupDto.getNewLicenseeId()!=null){
                stringBuilder.append("<p class=\"line\">   ").append("Change in Management of Licensee").append("</p>");
            }
            emailMap.put("ServiceNames", stringBuilder);
            emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
            emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER);
            emailParam.setQueryCode(application.getApplicationNo());
            emailParam.setReqRefNum(application.getApplicationNo());
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(application.getApplicationNo());
            emailParam.setTemplateContent(emailMap);
            MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER).getEntity();
            Map<String, Object> map1 = IaisCommonUtils.genNewHashMap();
            map1.put("ApplicationType", MasterCodeUtil.getCodeDesc(application.getApplicationType()));
            map1.put("ApplicationNumber", application.getApplicationNo());
            String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map1);
            emailParam.setSubject(subject);
            log.info("start send email start");
            notificationHelper.sendNotification(emailParam);
            log.info("start send email end");
            //emailClient.sendNotification(emailDto).getEntity();

            //sms
            msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER_SMS).getEntity();
            subject = null;
            try {
                subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map1);
            } catch (IOException |TemplateException e) {
                log.info(e.getMessage(),e);
            }
            EmailParam smsParam = new EmailParam();
            smsParam.setQueryCode(application.getApplicationNo());
            smsParam.setReqRefNum(application.getApplicationNo());
            smsParam.setRefId(application.getApplicationNo());
            smsParam.setTemplateContent(emailMap);
            smsParam.setSubject(subject);
            emailMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(application.getApplicationType()));
            emailMap.put("ApplicationNumber", application.getApplicationNo());
            smsParam.setTemplateContent(emailMap);
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_008_SUBMIT_OFFICER_SMS);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            log.info("start send sms start");
            notificationHelper.sendNotification(smsParam);
            log.info("start send sms end");
        }

    }

    @Override
    public void initPath() {

        File compress =MiscUtil.generateFile(sharedPath+File.separator+AppServicesConsts.COMPRESS,AppServicesConsts.FILE_NAME);
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

    public Boolean  download( ProcessFileTrackDto processFileTrackDto,List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList,String fileName
    ,String groupPath,String submissionId)  throws Exception {

        Boolean flag=Boolean.FALSE;

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
                            Boolean aBoolean = fileToDto(by.toString(), listApplicationDto, requestForInfList, processFileTrackDto, submissionId, l);
                            if(Boolean.TRUE.equals(aBoolean)){
                                saveFileRepo( fileName,groupPath,submissionId,l);
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
        List<AppPremiseMiscDto> appPremiseMiscEntities = applicationListDto.getAppPremiseMiscEntities();
        List<AppGrpPremisesEntityDto> appGrpPremises = applicationListDto.getAppGrpPremises();
        for(ApplicationDto applicationDto : application){
            String id = applicationDto.getId();
            for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelation){
                String applicationId = appPremisesCorrelationDto.getApplicationId();
                if(id.equals(applicationId)){
                    HcsaRiskScoreDto hcsaRiskScoreDto=new HcsaRiskScoreDto();
                    hcsaRiskScoreDto.setAppType(applicationDto.getApplicationType());
                    hcsaRiskScoreDto.setServiceId(applicationDto.getServiceId());
                    hcsaRiskScoreDto.setLicId(applicationDto.getOriginLicenceId());
                    hcsaRiskScoreDto.setGrpLic(applicationDto.isGrpLic());
                    for(AppGrpPremisesEntityDto appGrpPremisesEntityDto : appGrpPremises){
                        if(appPremisesCorrelationDto.getAppGrpPremId().equalsIgnoreCase(appGrpPremisesEntityDto.getId())){
                            hcsaRiskScoreDto.setHcicode(appGrpPremisesEntityDto.getHciCode());
                            break;
                        }
                    }
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
        try {
            sendAsoWithdrow(applicationGroup,application,appPremisesCorrelation, appPremiseMiscEntities);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
            applicationClient.updateProcessFileTrack(processFileTrackDto);
            return Boolean.FALSE;
        }
        requeOrNew(requestForInfList,applicationGroup,application,updateTaskList);
        update(cessionOrwith,listApplicationDto,applicationGroup,application);
        log.info(StringUtil.changeForLog(listApplicationDto.toString()+"listApplicationDto size "+listApplicationDto.size()));
        String requestForInfListString = requestForInfList.toString();
        log.info(StringUtil.changeForLog(requestForInfListString +"requestForInfList size" +requestForInfList .size()));
        log.info(StringUtil.changeForLog(requestForInfListString +"updateTaskList size" +updateTaskList .size()));
        log.info(StringUtil.changeForLog(requestForInfListString +"cessionOrwith size" +cessionOrwith .size()));
        ApplicationNewAndRequstDto applicationNewAndRequstDto=new ApplicationNewAndRequstDto();
        withdrow(listApplicationDto);
        withdrow(requestForInfList);
        withdrow(cessionOrwith);
        boolean b = withdrowAppToBe(cessionOrwith, applicationListDto, processFileTrackDto);
        if(b){
           return Boolean.FALSE;
        }
        applicationNewAndRequstDto.setListNewApplicationDto(listApplicationDto);
        applicationNewAndRequstDto.setRequestForInfList(requestForInfList);
        applicationNewAndRequstDto.setUpdateTaskList(updateTaskList);
        applicationNewAndRequstDto.setCessionOrWith(cessionOrwith);
        applicationNewAndRequstDto.setEventNo(l);
        applicationNewAndRequstDto.setZipFileName(processFileTrackDto.getFileName());
        applicationNewAndRequstDto.setAuditTrailDto(intranet);
        applicationNewAndRequstDto.setProcessFileTrackDto(processFileTrackDto);
        applicationListDto.setApplicationNewAndRequstDto(applicationNewAndRequstDto);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_COMPLETE);
        applicationListDto.setProcessFileTrackDto(processFileTrackDto);
        applicationListDto.setEventRefNo(l.toString());
        applicationListDto.setRefNo(l.toString());
        log.info("update be application status");

        eventBusHelper.submitAsyncRequest(applicationListDto,submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_SAVE_GROUP_APPLICATION,l.toString(),null);

        return Boolean.TRUE;

    }
    public boolean withdrowAppToBe(List<ApplicationDto> applicationDtos,ApplicationListFileDto applicationListDto,ProcessFileTrackDto processFileTrackDto){
        if(applicationDtos==null || applicationDtos.isEmpty()){
            return false;
        }
        for(ApplicationDto applicationDto : applicationDtos){
            if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType())){
                return false;
            }
        }
        List<AppPremiseMiscDto> appPremiseMiscEntities = applicationListDto.getAppPremiseMiscEntities();
        log.info("----start withdrowAppToBe-----");
        if(appPremiseMiscEntities!=null && !appPremiseMiscEntities.isEmpty()){
            log.info("---- appPremiseMiscEntities is not null ----start withdrowAppToBe-----");
            List<String> list=new ArrayList<>(appPremiseMiscEntities.size());
            for(AppPremiseMiscDto appPremiseMiscDto : appPremiseMiscEntities){
                String relateRecId = appPremiseMiscDto.getRelateRecId();
                if(!StringUtil.isEmpty(relateRecId)){
                    list.add(relateRecId);
                }
            }
            log.info(StringUtil.changeForLog(JsonUtil.parseToJson(list)));
            List<ApplicationDto> entity = applicationClient.getApplicationDtoByAppIds(list).getEntity();
            if(entity.isEmpty()){
                processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
                applicationClient.updateProcessFileTrack(processFileTrackDto);
                return true;
            }
        }
        log.info("---- end ----start withdrowAppToBe-----");
        return false;
    }

    private void sendAsoWithdrow(List<ApplicationGroupDto> applicationGroup,List<ApplicationDto> applicationList,List<AppPremisesCorrelationDto> appPremisesCorrelation,List<AppPremiseMiscDto> appPremiseMiscEntities){
        log.info("withdrow email function start");
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
        Map<String,String> appIdAndAppCorrIds=IaisCommonUtils.genNewHashMap();
        for (AppPremisesCorrelationDto appCorr:appPremisesCorrelation
        ) {
            appIdAndAppCorrIds.put(appCorr.getApplicationId(),appCorr.getId());
        }
        Map<String,String> appPremiseMiscMap=IaisCommonUtils.genNewHashMap();
        if(appPremiseMiscEntities!=null){
            for (AppPremiseMiscDto miscDto:appPremiseMiscEntities
            ) {
                appPremiseMiscMap.put(miscDto.getAppPremCorreId(),miscDto.getRelateRecId());
            }
        }
        map.forEach((k,v)->{
            for(ApplicationDto application :v){
                if(k.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL)){
                    try {
                        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(k.getLicenseeId()).getEntity();
                        String oldAppId=appPremiseMiscMap.get(appIdAndAppCorrIds.get(application.getId()));
                        ApplicationDto oldAppDto=applicationClient.getApplicationById(oldAppId).getEntity();
                        List<TaskDto> oldTaskDtos= taskService.getTaskbyApplicationNo(oldAppDto.getApplicationNo());
                        String asoId="";
                        if(oldTaskDtos.size()!=0){
                            for (TaskDto task:oldTaskDtos
                            ) {
                                if(task.getRoleId().equals(RoleConsts.USER_ROLE_ASO)){
                                    asoId=task.getUserId();
                                }
                                if(task.getTaskStatus().equals(TaskConsts.TASK_STATUS_PENDING)||task.getTaskStatus().equals(TaskConsts.TASK_STATUS_READ)){
                                    task.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                                    taskService.updateTask(task);
                                }
                            }
                        }
                        OrgUserDto orgUserDto= organizationClient.retrieveOrgUserAccountById(asoId).getEntity();

                        if(application.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL)&&oldTaskDtos.size()!=0){
                            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                            emailMap.put("officer_name", orgUserDto.getDisplayName());
                            emailMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(oldAppDto.getApplicationType()));
                            emailMap.put("ApplicationNumber", oldAppDto.getApplicationNo());
                            AppGrpPremisesEntityDto premisesDto=applicationClient.getPremisesByAppNo(oldAppDto.getApplicationNo()).getEntity();
                            emailMap.put("hci_name", premisesDto.getHciName());
                            emailMap.put("submission_date", Formatter.formatDate(k.getSubmitDt()));
                            emailMap.put("licensee_name", licenseeDto.getName());
                            String address = MiscUtil.getAddressForApp(premisesDto.getBlkNo(),premisesDto.getStreetName()
                                    ,premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()
                                    ,premisesDto.getAppPremisesOperationalUnitDtos());
                            emailMap.put("address", address);
                            if(!application.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED)){
                                emailMap.put("already", "already");
                                String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                                emailMap.put("systemLink", loginUrl);
                                Calendar calendar=Calendar.getInstance();
                                calendar.add(Calendar.DATE, systemParamConfig.getWithdrewTatDate());
                                String dueDay=new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(calendar.getTime());
                                emailMap.put("TAT_time", dueDay);

                            }

                            emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
                            MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.TEMPLATE_WITHDRAWAL_005_EMAIL).getEntity();
                            Map<String, Object> map1 = IaisCommonUtils.genNewHashMap();
                            map1.put("ApplicationType", MasterCodeUtil.getCodeDesc(oldAppDto.getApplicationType()));
                            map1.put("ApplicationNumber", oldAppDto.getApplicationNo());
                            String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map1);
                            log.info("start send email start");
                            EmailDto emailDto = new EmailDto();
                            List<String> receiptEmail=IaisCommonUtils.genNewArrayList();
                            receiptEmail.add(orgUserDto.getEmail());
                            List<String> mobile = IaisCommonUtils.genNewArrayList();
                            mobile.add(orgUserDto.getMobileNo());
                            emailDto.setReceipts(receiptEmail);
                            String emailContent = getEmailContent(MsgTemplateConstants.TEMPLATE_WITHDRAWAL_005_EMAIL,emailMap);
                            emailDto.setContent(emailContent);
                            emailDto.setSubject(subject);
                            emailDto.setSender(this.mailSender);
                            emailDto.setClientQueryCode(oldAppDto.getApplicationNo());
                            emailDto.setReqRefNum(oldAppDto.getApplicationNo());
                            if(orgUserDto.getEmail()!=null){
                                emailSmsClient.sendEmail(emailDto, null);
                            }
                            log.info("start send email end");

                            //sms
                            msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.TEMPLATE_WITHDRAWAL_005_SMS).getEntity();
                            subject = null;
                            try {
                                subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map1);
                            } catch (IOException |TemplateException e) {
                                log.info(e.getMessage(),e);
                            }


                            log.info("start send sms start");
                            SmsDto smsDto = new SmsDto();
                            smsDto.setSender(mailSender);
                            smsDto.setContent(subject);
                            smsDto.setOnlyOfficeHour(false);
                            smsDto.setReceipts(mobile);
                            smsDto.setReqRefNum(oldAppDto.getApplicationNo());
                            if(orgUserDto.getMobileNo()!=null){
                                emailHistoryCommonClient.sendSMS(mobile, smsDto, oldAppDto.getApplicationNo());
                            }
                            log.info("start send sms end");
                        }

                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }
                }

            }

                });
        log.info("withdrow email function end");

    }

    private List<ApplicationDto> withdrow(List<ApplicationDto> applicationDtos){
        log.info("withdrow function start");
        ListIterator<ApplicationDto> applicationDtoListIterator = applicationDtos.listIterator();
        while (applicationDtoListIterator.hasNext()){
            ApplicationDto next = applicationDtoListIterator.next();
            String status = next.getStatus();
            if(!ApplicationConsts.PENDING_ASO_REPLY.equals(status)&&
               !ApplicationConsts.PENDING_PSO_REPLY.equals(status)&&
               !ApplicationConsts.PENDING_INP_REPLY.equals(status)){
                List<AppPremiseMiscDto> entity = applicationClient.getAppPremiseMiscDtoRelateId(next.getId()).getEntity();
                if(!entity.isEmpty()){
                    Iterator<AppPremiseMiscDto> iterator = entity.iterator();
                    while (iterator.hasNext()){
                        AppPremiseMiscDto next1 = iterator.next();
                        if(ApplicationConsts.WITHDROW_TYPE_APPLICATION.equals(next1.getAppealType())){
                            String appPremCorreId = next1.getAppPremCorreId();
                            List<String> corrIds=new ArrayList<>(1);
                            corrIds.add(appPremCorreId);
                            List<ApplicationDto> applicationDtoList = applicationClient.getApplicationDtoByCorrIds(corrIds).getEntity();
                            if(applicationDtoList!=null&&!applicationDtoList.isEmpty()){
                                String status1 = applicationDtoList.get(0).getStatus();
                                if(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(status1)){
                                    applicationDtoListIterator.remove();
                                }
                            }
                        }
                    }
                }
            }

        }
        return applicationDtos;
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



    private TaskHistoryDto getRoutingTaskForRequestForInformation(List<ApplicationDto> applicationDtos,AuditTrailDto auditTrailDto) {
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
                                    createAppPremisesRoutingHistory(applicationDto,appStatus,
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
    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(ApplicationDto applicationDto, String appStatus,
                                                                         String stageId, String internalRemarks,String roleId,
                                                                         AuditTrailDto auditTrailDto){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(applicationDto.getApplicationNo());
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(applicationDto.getModifiedBy());
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
        try {
           // appGroupMiscService.notificationApplicationUpdateBatchjob();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
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
        TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(listNewApplicationDto, HcsaConsts.ROUTING_STAGE_ASO, RoleConsts.USER_ROLE_ASO,intranet,RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN,null,true);
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
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos1 = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                    for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : appPremisesRoutingHistoryDtos1){
                        appPremisesRoutingHistoryDto.setWrkGrpId(null);
                    }
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
            if(onSubmitTaskList.size()!=0){
                HashMap<String,TaskDto> tempMap = IaisCommonUtils.genNewHashMap();
                for (TaskDto c : onSubmitTaskList) {
                    String key = c.getRefNo();
                    if(!tempMap.containsKey(key)){
                        tempMap.put(key, c);
                    }
                }
                List<TaskDto> tempList = IaisCommonUtils.genNewArrayList();
                for(Map.Entry<String,TaskDto> entry : tempMap.entrySet()){
                    tempList.add(entry.getValue());
                }
                onSubmitTaskList=tempList;
            }
            broadcastOrganizationDto.setOneSubmitTaskList(onSubmitTaskList);
            broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
            eventBusHelper.submitAsyncRequest(broadcastOrganizationDto, submissionId,
                    EventBusConsts.SERVICE_NAME_ROUNTINGTASK,
                    EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING,
                    broadcastOrganizationDto.getEventRefNo(), null);
            eventBusHelper.submitAsyncRequest(broadcastApplicationDto, submissionId,
                    EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING,
                    broadcastApplicationDto.getEventRefNo(), null);
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
            if(EventBusConsts.SERVICE_NAME_ROUNTINGTASK.equals(submission.getSubmissionIdentity().getService())&& "Batchjob".equals(submission.getProcess())){
                broadcastOrganizationDto = JsonUtil.parseToObject(submission.getData(), BroadcastOrganizationDto.class);
                break;
            }
        }
        if(broadcastOrganizationDto!=null&&broadcastOrganizationDto.getApplicationNewAndRequstDto()!=null){
            ApplicationNewAndRequstDto applicationNewAndRequstDto = broadcastOrganizationDto.getApplicationNewAndRequstDto();
            String zipFileName = applicationNewAndRequstDto.getZipFileName();
            log.info(StringUtil.changeForLog(JsonUtil.parseToJson(applicationNewAndRequstDto)+"---applicationNewAndRequstDto-----"));
            String inFolder = inSharedPath;
            if (!inFolder.endsWith(File.separator)) {
                inFolder += File.separator;
            }
            File file =MiscUtil.generateFile(inFolder + zipFileName);
            log.info("start remove file start");
            moveFile(file);
            log.info("update file track start");
            ProcessFileTrackDto processFileTrackDto = applicationNewAndRequstDto.getProcessFileTrackDto();
            processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_SEND_TSAK_SUCCESS);
            applicationClient.updateProcessFileTrack(processFileTrackDto);
        }
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
    private String getEmailContent(String templateId, Map<String, Object> subMap){
        String mesContext = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto =notificationHelper.getMsgTemplate(templateId);
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        mesContext = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getMessageContent(), subMap);
                    }
                    //replace num
                    mesContext = MessageTemplateUtil.replaceNum(mesContext);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return mesContext;
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
                    }else if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(application.getStatus())||ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE.equals(application.getStatus())){
                        cession++;
                    }else if(ApplicationConsts.PENDING_ASO_REPLY.equals(application.getStatus())||ApplicationConsts.PENDING_PSO_REPLY.equals(application.getStatus())
                            ||ApplicationConsts.PENDING_INP_REPLY.equals(application.getStatus())){
                        cession--;
                    }
                    if(cession==i){
                        cessionOrwith.addAll(applicationDtoList);
                    }

                }
            }
            else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
                log.info(StringUtil.changeForLog("=============="+k.getGroupNo()));
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

            }
            else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
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


            }
            else {
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
                    hcsaConfigClient.getHcsaSvcRoutingStageDtoByServiceAndType(applicationDto.getRoutingServiceId(), applicationDto.getApplicationType()).getEntity();
            if(entity.isEmpty()){
                return list;
            }
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
            if(appPremisesCorrelationDto!=null){
                TaskDto taskDto= taskService.getRoutingTask(applicationDto,entity.get(0).getStageId(),entity.get(0).getStageCode(),appPremisesCorrelationDto.getId());
                list.add(taskDto);
            }
            applicationDtoList.add(applicationDto);
            TaskHistoryDto routingTaskOneUserForSubmisison = taskService.getRoutingTaskOneUserForSubmisison(applicationDtoList, entity.get(0).getStageId(), entity.get(0).getStageCode(), auditTrailDto,RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN,null, true);
            log.info(StringUtil.changeForLog("----"+routingTaskOneUserForSubmisison));
            if(routingTaskOneUserForSubmisison!=null&&routingTaskOneUserForSubmisison.getAppPremisesRoutingHistoryDtos()!=null){
                log.info(StringUtil.changeForLog("----"+JsonUtil.parseToJson(routingTaskOneUserForSubmisison)));
                List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos1 = routingTaskOneUserForSubmisison.getAppPremisesRoutingHistoryDtos();
                for(AppPremisesRoutingHistoryDto v : appPremisesRoutingHistoryDtos1){
                    v.setWrkGrpId(null);
                }
                appPremisesRoutingHistoryDtos.addAll(routingTaskOneUserForSubmisison.getAppPremisesRoutingHistoryDtos());
            }
        }
        return list;
    }

}
