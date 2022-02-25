package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.RequestForInformationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
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
 * RequestForInformationServiceImpl
 *
 * @author junyu
 * @date 2019/12/16
 */
@Service
@Slf4j
public class RequestForInformationServiceImpl implements RequestForInformationService {
    @Autowired
    RequestForInformationClient requestForInformationClient;
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    FileRepoClient fileRepoClient;
    @Autowired
    HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    HcsaChklClient hcsaChklClient;
    @Autowired
    private LicEicClient licEicClient;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;

    @Value("${iais.system.rfc.sms.reminder.day}")
    String reminderMax1Day;
    @Value("${iais.system.rfc.sms.sec.reminder.day}")
    String reminderMax2Day;
    @Value("${iais.system.rfc.sms.third.reminder.day}")
    String reminderMax3Day;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    private SystemBeLicClient systemBeLicClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Value("${iais.sharedfolder.requestForInfo.in}")
    private String inSharedPath;
    @Value("${iais.syncFileTracking.shared.path}")
    private     String sharedPath;
    @Autowired
    private GenerateIdClient generateIdClient;


    private static final String[] appType=new String[]{
            ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,
            ApplicationConsts.APPLICATION_TYPE_RENEWAL,
            ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,
            ApplicationConsts.APPLICATION_TYPE_APPEAL,
            ApplicationConsts.APPLICATION_TYPE_CESSATION,
            ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL,
            ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK,
            ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION
    };
    private static final String[] appStatus=new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION,
            //ApplicationConsts.APPLICATION_STATUS_RECALLED,
            ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING,
            ApplicationConsts.APPLICATION_STATUS_PROFESSIONAL_SCREENING_OFFICER_ENQUIRE,
            ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE,
            ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,
            ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
            ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_LETTER,
            ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,
            ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_APPROVED,
            ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
            ApplicationConsts.APPLICATION_STATUS_REJECTED,
            ApplicationConsts.APPLICATION_STATUS_WITHDRAWN,
            ApplicationConsts.APPLICATION_STATUS_CREATE_AUDIT_TASK_CANCELED,
            //ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS,
            ApplicationConsts.APPLICATION_STATUS_GIRO_PAYMENT_FAIL,
            ApplicationConsts.APPLICATION_STATUS_PENDING_PAYMENT_RESUBMIT
    };
    private static final String[] licStatus=new String[]{
            ApplicationConsts.LICENCE_STATUS_ACTIVE,
            ApplicationConsts.LICENCE_STATUS_IACTIVE,
            ApplicationConsts.LICENCE_STATUS_CEASED,
            ApplicationConsts.LICENCE_STATUS_EXPIRY,
            ApplicationConsts.LICENCE_STATUS_LAPSED,
            ApplicationConsts.LICENCE_STATUS_APPROVED,
            ApplicationConsts.LICENCE_STATUS_SUSPENDED,
            ApplicationConsts.LICENCE_STATUS_REVOKED
    };
    @Override
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> selectOptions=MasterCodeUtil.retrieveOptionsByCodes(appType);
        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    @Override
    public List<SelectOption> getAppStatusOption() {
        List<SelectOption> selectOptions= MasterCodeUtil.retrieveOptionsByCodes(appStatus);
        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    @Override
    public List<SelectOption> getLicSvcTypeOption() {
        List<String> svcNames=getSvcNamesByType();
        List<SelectOption> selectOptions= IaisCommonUtils.genNewArrayList();
        for (String svcName:svcNames
        ) {
            SelectOption selectOption=new SelectOption();
            selectOption.setText(svcName);
            selectOption.setValue(svcName);
            selectOptions.add(selectOption);
        }
        HashSet<SelectOption> set = new HashSet<>(selectOptions);
        selectOptions.clear();
        selectOptions.addAll(set);
        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    @Override
    public List<SelectOption> getLicSvcSubTypeOption() {
        List<SelectOption> selectOptions= IaisCommonUtils.genNewArrayList();
        SelectOption selectOption1=new SelectOption();
        selectOption1.setText("Pre-implantation Genetic Screening");
        selectOption1.setValue("PIG_Screening");
        selectOptions.add(selectOption1);
        SelectOption selectOption2=new SelectOption();
        selectOption2.setText("Pre-implantation Genetic Diagnosis");
        selectOption2.setValue("PIG_Diagnosis");
        selectOptions.add(selectOption2);
        SelectOption selectOption=new SelectOption();
        selectOption.setText("Human Immunodeficiency Virus");
        selectOption.setValue("HIV");
        selectOptions.add(selectOption);
        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    @Override
    public List<SelectOption> getLicStatusOption() {
        List<SelectOption> selectOptions= MasterCodeUtil.retrieveOptionsByCodes(licStatus);
        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    @Override
    @SearchTrack(catalog = "ReqForInfoQuery", key = "appLicenceForCommPoolQuery")
    public SearchResult<ApplicationLicenceQueryDto> appLicenceDoForCommPoolQuery(SearchParam searchParam) {
        return applicationClient.searchAppLic(searchParam).getEntity();
    }


    @Override
    @SearchTrack(catalog = "ReqForInfoQuery", key = "appLicenceQuery")
    public SearchResult<ApplicationLicenceQueryDto> appLicenceDoQuery(SearchParam searchParam) {
        return applicationClient.searchAppLic(searchParam).getEntity();
    }

    @Override
    public List<String> getSvcNamesByType() {
        return hcsaChklClient.listServiceName(AppServicesConsts.SVC_TYPE_NORMAL).getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto updateLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        return requestForInformationClient.updateLicPremisesReqForInfoFe(licPremisesReqForInfoDto).getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto createLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        licPremisesReqForInfoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return requestForInformationClient.createLicPremisesReqForInfo(licPremisesReqForInfoDto).getEntity();
    }

    @Override
    public List<LicPremisesReqForInfoDto> getAllReqForInfo() {
        return requestForInformationClient.getAllReqForInfo().getEntity();
    }

    @Override
    public List<LicPremisesReqForInfoDto> searchLicPremisesReqForInfo(String licPremId) {
        return requestForInformationClient.searchLicPremisesReqForInfo(licPremId).getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto getLicPreReqForInfo(String id) {
        return requestForInformationClient.getLicPreReqForInfo(id).getEntity();
    }

    @Override
    public byte[] downloadFile(String fileRepoId) {
        return fileRepoClient.getFileFormDataBase(fileRepoId).getEntity();
    }

    @Override
    public void compress(){
        log.info("-------------compress start ---------");
        List<ProcessFileTrackDto> processFileTrackDtos=systemBeLicClient.getFileTypeAndStatus(RequestForInformationConstants.RFI_CLOSE,ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS).getEntity();
        if(processFileTrackDtos!=null&&!processFileTrackDtos.isEmpty()){
            log.info(StringUtil.changeForLog("-----start process file-----, process file size ==>"+processFileTrackDtos.size()));
            for (ProcessFileTrackDto v : processFileTrackDtos) {
                File fil = MiscUtil.generateFile(inSharedPath , v.getFileName());
                if(fil.exists()&&fil.isFile()){
                    if(fil.getName().endsWith(RequestForInformationConstants.ZIP_NAME)){
                        String name = fil.getName();
                        String path = fil.getPath();
                        HashMap<String,String> map= IaisCommonUtils.genNewHashMap();
                        map.put("fileName",name);
                        map.put("filePath", path);

                        try (InputStream is = newInputStream(fil.toPath());
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
                            s = s + RequestForInformationConstants.ZIP_NAME;
                            if( !s.equals(name)){
                                continue;
                            }
                        }catch (Exception e){
                            log.error(e.getMessage(),e);
                            continue;
                        }
                        String refId = v.getRefId();
                        CheckedInputStream cos=null;
                        BufferedInputStream bis=null;
                        BufferedOutputStream bos=null;
                        OutputStream os=null;
                        try (ZipFile zipFile=new ZipFile(path);)  {
                            for(Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();){
                                ZipEntry zipEntry = entries.nextElement();
                                zipFile(zipEntry,os,bos,zipFile,bis,cos,name,refId);
                            }

                        } catch (IOException e) {
                            log.error(e.getMessage(),e);
                        }

                        try {
                            String submissionId = generateIdClient.getSeqId().getEntity();
                            boolean save=this.download(v,name,refId,submissionId);
                            //save success
                            if(save){
                                FileUtils.deleteTempFile(fil);
                            }
                        }catch (Exception e){
                            //save bad

                        }
                    }

                }
            }
        }
    }

    private void zipFile( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos,String fileName,String refId)  {


        try {
            if(!zipEntry.getName().endsWith(File.separator)){

                String substring = zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator));
                File file =MiscUtil.generateFile(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId,substring);
                if(!file.exists()){
                    file.mkdirs();
                }
                File outFile = MiscUtil.generateFile(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId,zipEntry.getName());
                os= newOutputStream(outFile.toPath());
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

                MiscUtil.generateFile(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId,zipEntry.getName()).mkdirs();
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

    @Override
    public boolean download( ProcessFileTrackDto processFileTrackDto,String fileName,String refId,String submissionId) {

        boolean flag=false;
        File file =MiscUtil.generateFile(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId,RequestForInformationConstants.FILE_NAME_RFI);
        if(!file.exists()){
            file.mkdirs();
        }

        File[] files = file.listFiles();
        for(File  filzz:files){
            if(filzz.isFile() &&filzz.getName().endsWith(RequestForInformationConstants.FILE_FORMAT)){
                try (InputStream fileInputStream = newInputStream(filzz.toPath());
                     ByteArrayOutputStream by=new ByteArrayOutputStream();) {

                    int count=0;
                    byte [] size=new byte[1024];
                    count=fileInputStream.read(size);
                    while(count!=-1){
                        by.write(size,0,count);
                        count= fileInputStream.read(size);
                    }
                    boolean aBoolean = fileToDto(by.toString(),processFileTrackDto,submissionId);
                    flag=aBoolean;
                    if(aBoolean){
                        changeStatus(processFileTrackDto);
                        saveFileRepo( fileName, submissionId,refId);
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }

        return flag;
    }
    private boolean fileToDto(String str,ProcessFileTrackDto processFileTrackDto,String submissionId){
        LicPremisesReqForInfoDto licPremisesReqForInfoDto = JsonUtil.parseToObject(str, LicPremisesReqForInfoDto.class);
        AuditTrailDto intranet = AuditTrailHelper.getCurrentAuditTrailDto();
        licPremisesReqForInfoDto.setAuditTrailDto(intranet);
        //eventbus
        licPremisesReqForInfoDto.setEventRefNo(processFileTrackDto.getRefId());
        eventBusHelper.submitAsyncRequest(licPremisesReqForInfoDto,submissionId, EventBusConsts.SERVICE_NAME_LICENCESAVE,
                EventBusConsts.OPERATION_LIC_REQUEST_FOR_INFO,licPremisesReqForInfoDto.getEventRefNo(),null);
        return true;
        // return requestForInformationClient.rfiFeUpdateToBe(licPremisesReqForInfoDto).getStatusCode() == 200;

    }

    private void changeStatus( ProcessFileTrackDto processFileTrackDto){
        /*  applicationClient.updateStatus().getEntity();*/
        processFileTrackDto.setProcessType(RequestForInformationConstants.RFI_CLOSE);
        AuditTrailDto batchJobDto = AuditTrailHelper.getCurrentAuditTrailDto();
        processFileTrackDto.setAuditTrailDto(batchJobDto);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_COMPLETE);
        systemBeLicClient.updateProcessFileTrack(processFileTrackDto);

    }

    private void saveFileRepo(String fileNames,String submissionId,String eventRefNo){
        boolean flag=false;
        File file =MiscUtil.generateFile(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileNames+File.separator+eventRefNo+File.separator+RequestForInformationConstants.FILE_NAME_RFI,RequestForInformationConstants.FILES);
        if(!file.exists()){
            file.mkdirs();
        }
        File[] files = file.listFiles();
        List<FileRepoDto> fileRepoDtos = IaisCommonUtils.genNewArrayList();
        FileRepoEventDto eventDto = new FileRepoEventDto();
        for(File f:files){
            if(f.isFile()){
                try {
                    StringBuilder fileName=new StringBuilder();
                    String[] split = f.getName().split("emanelififrcohda");
                    for(int i=1;i<split.length;i++){
                        fileName.append(split[i]);
                    }

                    AuditTrailDto intranet = AuditTrailHelper.getCurrentAuditTrailDto();
                    FileRepoDto fileRepoDto = new FileRepoDto();
                    fileRepoDto.setId(split[0]);
                    fileRepoDto.setAuditTrailDto(intranet);
                    fileRepoDto.setFileName(f.getName());
                    fileRepoDto.setRelativePath(RequestForInformationConstants.COMPRESS+File.separator+fileNames+File.separator+eventRefNo+File.separator+RequestForInformationConstants.FILE_NAME_RFI+File.separator+RequestForInformationConstants.FILES);
                    fileRepoDtos.add(fileRepoDto);


                    eventDto.setFileRepoList(fileRepoDtos);
                    flag=true;
                    log.info(StringUtil.changeForLog(f.getPath()+"file path"));
                }catch (Exception e){
                    continue;
                }
            }
        }
        if(flag){
            eventDto.setFileRepoList(fileRepoDtos);
            eventDto.setEventRefNo(eventRefNo);
            eventBusHelper.submitAsyncRequest(eventDto, submissionId, EventBusConsts.SERVICE_NAME_FILE_REPO,
                    EventBusConsts.OPERATION_BE_REC_DATA_COPY, eventRefNo, null);
        }

    }

    @Override
    public void delete() {
        File file =MiscUtil.generateFile(sharedPath,"folder");
        File b=MiscUtil.generateFile(inSharedPath);
        File c=MiscUtil.generateFile(sharedPath,RequestForInformationConstants.COMPRESS);
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

    @Override
    public void reminderRfiJob() {
        List<LicPremisesReqForInfoDto> licPremisesReqForInfoDtos= getAllReqForInfo();
        for (LicPremisesReqForInfoDto rfi:licPremisesReqForInfoDtos
        ) {
            if(rfi.getReminder()<3){
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(rfi.getDueDateSubmission());
                String reminderMaxDay;
                switch (rfi.getReminder()){
                    case 0:reminderMaxDay=reminderMax1Day;break;
                    case 1:reminderMaxDay=reminderMax2Day;break;
                    case 2:reminderMaxDay=reminderMax3Day;break;
                    default:reminderMaxDay="0";
                }
                cal1.add(Calendar.DAY_OF_MONTH, Integer.parseInt(reminderMaxDay));
                String parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal1.getTime());
                String newDt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                if(parse1.compareTo(newDt)<0&&(rfi.getStatus().equals(RequestForInformationConstants.RFI_NEW)||rfi.getStatus().equals(RequestForInformationConstants.RFI_RETRIGGER))){
                    try {
                        reminder(rfi);
                    }catch (Exception e){
                        log.info(e.getMessage(),e);
                    }
                }
            }
        }
        try {
            getInfo();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public StringBuilder setEmailAppend(LicPremisesReqForInfoDto licPremisesReqForInfoDto,boolean reqTypeInfo) {
        StringBuilder stringBuilder=new StringBuilder();
        if(reqTypeInfo){
            for (int i=0;i<licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos().size();i++) {
                stringBuilder.append("<p>   ").append(' ').append("Information : ").append(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos().get(i).getTitle()).append("</p>");
            }
        }
        if(licPremisesReqForInfoDto.isNeedDocument()){

            int seqNum=1;
            for(LicPremisesReqForInfoDocDto doc :licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto()){
                if(doc.getSeqNum()==null){
                    doc.setSeqNum(seqNum);
                }
                seqNum++;
            }
            Map<Integer,List<LicPremisesReqForInfoDocDto>> licPremisesReqForInfoMultiFileDto=IaisCommonUtils.genNewHashMap();
            for (int i=1;i<seqNum;i++){
                List<LicPremisesReqForInfoDocDto> docs=IaisCommonUtils.genNewArrayList();
                for (LicPremisesReqForInfoDocDto docDto:licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto()
                ) {
                    if(docDto.getSeqNum().equals(i)){
                        docs.add(docDto);
                    }
                }
                if(!docs.isEmpty()){
                    licPremisesReqForInfoMultiFileDto.put(i,docs);
                }
            }
            for (Map.Entry<Integer,List<LicPremisesReqForInfoDocDto>> multiFileDto:licPremisesReqForInfoMultiFileDto.entrySet()
                 ) {
                stringBuilder.append("<p>   ").append(' ').append("Documentations : ").append(multiFileDto.getValue().get(0).getTitle()).append("</p>");
            }
        }
        return stringBuilder;
    }

    private void sendEmail(ApplicationDto applicationDto, String time) throws Exception{
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER);
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, systemParamConfig.getRfiDueDate());
        String appGrpId = applicationDto.getAppGrpId();
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appGrpId).getEntity();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(applicationGroupDto.getLicenseeId());
        String applicantName=licenseeDto.getName();
        List<OrgUserDto> orgUserDtoList = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(licenseeDto.getOrganizationId()).getEntity();
        if(orgUserDtoList!=null&&orgUserDtoList.get(0)!=null){
            applicantName=orgUserDtoList.get(0).getDisplayName();
        }
        map.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        emailMap.put("ApplicantName", applicantName);
        emailMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        emailMap.put("ApplicationNumber", applicationDto.getApplicationNo());
        emailMap.put("systemLink", loginUrl);
        emailMap.put("email", systemParamConfig.getSystemAddressOne());
        emailMap.put("ApplicationDate", Formatter.formatDate(applicationDto.getModifiedAt()));
        emailMap.put("TATtime", Formatter.formatDate(cal.getTime()));
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER);
        emailParam.setTemplateContent(emailMap);
        emailParam.setQueryCode(applicationDto.getAppGrpId());
        emailParam.setReqRefNum(applicationDto.getAppGrpId());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setSubject(subject);
        //email
        notificationHelper.sendNotification(emailParam);
        saveMailJob(applicationDto.getId(),"sendRfi"+time);
        //sms
        rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_SMS);
        subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateContent(emailMap);
        smsParam.setQueryCode(applicationDto.getAppGrpId());
        smsParam.setReqRefNum(applicationDto.getAppGrpId());
        smsParam.setRefId(applicationDto.getApplicationNo());
        smsParam.setSubject(subject);
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_SMS);
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        notificationHelper.sendNotification(smsParam);
        EmailParam msgParam = new EmailParam();
        msgParam.setQueryCode(applicationDto.getAppGrpId());
        msgParam.setReqRefNum(applicationDto.getAppGrpId());
        rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_MSG);
        subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
        msgParam.setSubject(subject);
        HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
        List<String> svcCode=IaisCommonUtils.genNewArrayList();
        svcCode.add(svcDto.getSvcCode());
        String url="";
        HashMap<String,String> mapPrem=IaisCommonUtils.genNewHashMap();
        url=HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        emailMap.put("systemLink", url);
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_MSG);
        msgParam.setTemplateContent(emailMap);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setMaskParams(mapPrem);
        msgParam.setSvcCodeList(svcCode);
        msgParam.setRefId(applicationDto.getApplicationNo());
        notificationHelper.sendNotification(msgParam);
    }
    private void reminder(LicPremisesReqForInfoDto licPremisesReqForInfoDto) throws IOException, TemplateException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, systemParamConfig.getRfiDueDate());
        String licenseeId=getLicPreReqForInfo(licPremisesReqForInfoDto.getId()).getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        List<OrgUserDto> orgUserDtoList = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(licenseeDto.getOrganizationId()).getEntity();
        String applicantName=licenseeDto.getName();
        if(orgUserDtoList!=null&&orgUserDtoList.get(0)!=null){
            applicantName=orgUserDtoList.get(0).getDisplayName();
        }
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        StringBuilder stringBuilder=setEmailAppend(licPremisesReqForInfoDto,!StringUtil.isEmpty(licPremisesReqForInfoDto.getLicPremisesReqForInfoReplyDtos()));
        String url = "https://" + systemParamConfig.getInterServerName() +
                "/hcsa-licence-web/eservice/INTERNET/MohClientReqForInfo" +
                "?licenseeId=" + licPremisesReqForInfoDto.getLicenseeId();
        map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.AD_HOC}).get(0).getText());
        map.put("ApplicationNumber", licPremisesReqForInfoDto.getLicenceNo());
        InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER);
        String subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
        HashMap<String,String> mapPrem=IaisCommonUtils.genNewHashMap();
        mapPrem.put("licenseeId",licPremisesReqForInfoDto.getLicenseeId());
        LicenceViewDto licenceViewDto= hcsaLicenceClient.getLicenceViewDtoByLicPremCorrId(licPremisesReqForInfoDto.getLicPremId()).getEntity();
        try{


            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            emailMap.put("ApplicantName", applicantName);
            emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{RequestForInformationConstants.AD_HOC}).get(0).getText());
            emailMap.put("ApplicationNumber", licPremisesReqForInfoDto.getLicenceNo());
            emailMap.put("ApplicationDate", Formatter.formatDate(licPremisesReqForInfoDto.getRequestDate()));
            emailMap.put("email", systemParamConfig.getSystemAddressOne());
            emailMap.put("TATtime", Formatter.formatDate(cal.getTime()));
            emailMap.put("systemLink", loginUrl);
            emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
            emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");

            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER);
            emailParam.setTemplateContent(emailMap);
            emailParam.setQueryCode(licPremisesReqForInfoDto.getLicenceNo());
            emailParam.setReqRefNum(licPremisesReqForInfoDto.getLicenceNo());
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
            emailParam.setRefId(licenceViewDto.getLicenceDto().getId());
            emailParam.setSubject(subject);
            //email
            notificationHelper.sendNotification(emailParam);
            //sms
            rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_SMS);
            subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateContent(emailMap);
            smsParam.setQueryCode(licPremisesReqForInfoDto.getLicenceNo());
            smsParam.setReqRefNum(licPremisesReqForInfoDto.getLicenceNo());
            smsParam.setRefId(licenceViewDto.getLicenceDto().getId());
            smsParam.setSubject(subject);
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_SMS);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
            notificationHelper.sendNotification(smsParam);
            //msg
            EmailParam msgParam = new EmailParam();
            msgParam.setQueryCode(licPremisesReqForInfoDto.getLicenceNo());
            msgParam.setReqRefNum(licPremisesReqForInfoDto.getLicenceNo());
            rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_MSG);
            subject= MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(),map);
            msgParam.setSubject(subject);
            HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(licenceViewDto.getLicenceDto().getSvcName()).getEntity();
            List<String> svcCode=IaisCommonUtils.genNewArrayList();
            svcCode.add(svcDto.getSvcCode());
            emailMap.put("systemLink", url);
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_REMINDER_MSG);
            msgParam.setTemplateContent(emailMap);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
            msgParam.setMaskParams(mapPrem);
            msgParam.setSvcCodeList(svcCode);
            msgParam.setRefId(licenceViewDto.getLicenceDto().getId());
            notificationHelper.sendNotification(msgParam);

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        licPremisesReqForInfoDto.setReminder(licPremisesReqForInfoDto.getReminder()+1);
        licPremisesReqForInfoDto.setStatus(RequestForInformationConstants.RFI_RETRIGGER);
        licPremisesReqForInfoDto.setDueDateSubmission(cal.getTime());
        LicPremisesReqForInfoDto licPremisesReqForInfoDto1 = updateLicPremisesReqForInfo(licPremisesReqForInfoDto);
        licPremisesReqForInfoDto1.setAction("update");

        EicRequestTrackingDto eicRequestTrackingDto=new EicRequestTrackingDto();
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        Date now = new Date();
        eicRequestTrackingDto.setActionClsName("com.ecquaria.cloud.moh.iais.service.RequestForInformationServiceImpl");
        eicRequestTrackingDto.setActionMethod("eicCallFeRfiLic");
        eicRequestTrackingDto.setModuleName("hcsa-licence-web-intranet");
        eicRequestTrackingDto.setDtoClsName(LicPremisesReqForInfoDto.class.getName());
        eicRequestTrackingDto.setDtoObject(JsonUtil.parseToJson(licPremisesReqForInfoDto1));
        eicRequestTrackingDto.setProcessNum(1);
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        eicRequestTrackingDto.setRefNo(System.currentTimeMillis()+"");
        licPremisesReqForInfoDto1.setEventRefNo(eicRequestTrackingDto.getRefNo());
        updateLicEicRequestTrackingDto(eicRequestTrackingDto);
        createFeRfiLicDto(licPremisesReqForInfoDto1);

    }

    private void getInfo() throws Exception{
        List<ApplicationDto> applicationDtos = applicationClient.getRfiReminder().getEntity();
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();

        ListIterator<ApplicationDto> iterator = applicationDtos.listIterator();
        while (iterator.hasNext()){
            ApplicationDto applicationDto = iterator.next();
            Date modifiedAt = applicationDto.getModifiedAt();
            calendar.setTime(modifiedAt);
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(reminderMax1Day));
            Date firstTime = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(reminderMax2Day));
            Date secondTime = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(reminderMax3Day));
            Date thirdTime = calendar.getTime();
            if(date.after(firstTime)&&date.before(secondTime)){
                boolean checkEmailIsSend = checkEmailIsSend(applicationDto.getId(), "sendRfi" + Integer.parseInt(reminderMax1Day));
                if(checkEmailIsSend){
                    try {
                        sendEmail(applicationDto,reminderMax1Day);
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }
                }
            }else if(date.after(secondTime)&&date.before(thirdTime)){
                boolean checkEmailIsSend = checkEmailIsSend(applicationDto.getId(), "sendRfi" + Integer.parseInt(reminderMax2Day));
                if(checkEmailIsSend){
                    try {
                        sendEmail(applicationDto,reminderMax2Day);
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }
                }
            }else if(date.after(thirdTime)){
                boolean checkEmailIsSend = checkEmailIsSend(applicationDto.getId(), "sendRfi" + Integer.parseInt(reminderMax3Day));
                if(checkEmailIsSend){
                    try {
                        sendEmail(applicationDto,reminderMax3Day);
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }
                }
            }
        }

    }

    private boolean checkEmailIsSend(String applicationId,String magKey){
        try {
            JobRemindMsgTrackingDto auto_renew = systemBeLicClient.getJobRemindMsgTrackingDto(applicationId, magKey).getEntity();
            if(auto_renew==null){
                return true;
            }else {
                log.info(StringUtil.changeForLog(JsonUtil.parseToJson(auto_renew)+"auto_renew"));
                return false;
            }

        }catch (Exception e){
            log.info(e.getMessage(),e);
            log.info(StringUtil.changeForLog("-----have error---"));
            return false;
        }
    }
    private void saveMailJob(String applicationId,String magKey){
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto=new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        jobRemindMsgTrackingDto.setMsgKey(magKey);
        jobRemindMsgTrackingDto.setRefNo(applicationId);
        List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos=new ArrayList<>(1);
        jobRemindMsgTrackingDtos.add(jobRemindMsgTrackingDto);
        systemBeLicClient.createJobRemindMsgTrackingDtos(jobRemindMsgTrackingDtos).getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto createFeRfiLicDto(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        EicRequestTrackingDto trackDto = getLicEicRequestTrackingDtoByRefNo(licPremisesReqForInfoDto.getEventRefNo());
        eicCallFeRfiLic(licPremisesReqForInfoDto);
        trackDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        updateLicEicRequestTrackingDto(trackDto);

        return licPremisesReqForInfoDto;
    }

    public void eicCallFeRfiLic(LicPremisesReqForInfoDto licPremisesReqForInfoDto1) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        log.info(StringUtil.changeForLog("=======>>>>>"+licPremisesReqForInfoDto1.getAction()+" Lic Request for Information reqInfoId "+licPremisesReqForInfoDto1.getId()));

        gatewayClient.createLicPremisesReqForInfoFe(licPremisesReqForInfoDto1,
                signature.date(), signature.authorization(), signature2.date(), signature2.authorization());
    }



    @Override
    public void updateLicEicRequestTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto) {
        licEicClient.saveEicTrack(licEicRequestTrackingDto);
    }


    public EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo) {
        return licEicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
    }

}
