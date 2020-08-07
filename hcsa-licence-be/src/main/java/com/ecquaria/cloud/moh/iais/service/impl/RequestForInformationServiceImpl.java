package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.RequestForInformationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.FileUtil;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
    AppPremisesCorrClient appPremisesCorrClient;
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
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    private LicEicClient licEicClient;

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

    @Value("${iais.syncFileTracking.shared.path}")
    private     String sharedPath;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private SystemBeLicClient systemClient;


    private final String[] appType=new String[]{
            ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,
            ApplicationConsts.APPLICATION_TYPE_RENEWAL,
            ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,
            ApplicationConsts.APPLICATION_TYPE_APPEAL,
            ApplicationConsts.APPLICATION_TYPE_CESSATION,
            ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL,
            ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK
    };
    private final String[] appStatus=new String[]{
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
            ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS
    };
    private final String[] licStatus=new String[]{
            ApplicationConsts.LICENCE_STATUS_ACTIVE,
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
        List<HcsaServiceSubTypeDto> subTypeNames= hcsaChklClient.listSubTypePhase1().getEntity();
        List<SelectOption> selectOptions= IaisCommonUtils.genNewArrayList();
        for (HcsaServiceSubTypeDto subTypeName:subTypeNames
        ) {
            if( "12B5496B-1123-EA11-BE78-000C29D29DB0".equals(subTypeName.getId())|| "0C0FF57E-1123-EA11-BE78-000C29D29DB0".equals(subTypeName.getId())){
                SelectOption selectOption=new SelectOption();
                selectOption.setText(subTypeName.getSubtypeName());
                selectOption.setValue(subTypeName.getId());
                selectOptions.add(selectOption);
            }
            if("AF58E13F-1023-EA11-BE78-000C29D29DB0".equals(subTypeName.getId())){
                SelectOption selectOption=new SelectOption();
                selectOption.setText("Human Immunodeficiency Virus");
                selectOption.setValue(subTypeName.getId());
                selectOptions.add(selectOption);
            }
        }
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
    public List<String> getActionBysByLicPremCorrId(String licPremCorrId) {
        LicenceViewDto licenceViewDto= hcsaLicenceClient.getLicenceViewDtoByLicPremCorrId(licPremCorrId).getEntity();
        List<LicAppCorrelationDto> licAppCorrelationDtos=hcsaLicenceClient.getLicCorrBylicId(licenceViewDto.getLicenceDto().getId()).getEntity();
        List<String> actionBys=IaisCommonUtils.genNewArrayList();
        for (LicAppCorrelationDto licApp:licAppCorrelationDtos
        ) {
            ApplicationDto applicationDto=applicationClient.getApplicationById(licApp.getApplicationId()).getEntity();
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppNo(applicationDto.getApplicationNo());
            for(AppPremisesRoutingHistoryDto appHis:appPremisesRoutingHistoryDtos){
                actionBys.add(appHis.getActionby());
            }
        }
        return actionBys;
    }


    @Override
    public SearchResult<RfiApplicationQueryDto> appDoQuery(SearchParam searchParam) {
        return applicationClient.searchApp(searchParam).getEntity();
    }

    @Override
    public SearchResult<RfiLicenceQueryDto> licenceDoQuery(SearchParam searchParam) {

        return requestForInformationClient.searchRfiLicence(searchParam).getEntity();
    }

    @Override
    public List<String> getSvcNamesByType() {
        return hcsaChklClient.listServiceName().getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto updateLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        return requestForInformationClient.updateLicPremisesReqForInfoFe(licPremisesReqForInfoDto).getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto createLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
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
    public void deleteLicPremisesReqForInfo(String id) {
        requestForInformationClient.deleteLicPremisesReqForInfo(id);

    }

    @Override
    public void acceptLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        requestForInformationClient.acceptLicPremisesReqForInfo(licPremisesReqForInfoDto);

    }
    @Override
    public byte[] downloadFile(String fileRepoId) {
        return fileRepoClient.getFileFormDataBase(fileRepoId).getEntity();
    }

    @Override
    public void compress(){
        log.info("-------------compress start ---------");
        if(new File(sharedPath+File.separator+RequestForInformationConstants.BACKUPS_REC+File.separator).isDirectory()){
            File[] files = new File(sharedPath+File.separator+RequestForInformationConstants.BACKUPS_REC+File.separator).listFiles();
            for(File fil:files){
                if(fil.getName().endsWith(RequestForInformationConstants.ZIP_NAME)){
                    String name = fil.getName();
                    String path = fil.getPath();
                    String relPath=RequestForInformationConstants.BACKUPS_REC+File.separator+name;
                    HashMap<String,String> map= IaisCommonUtils.genNewHashMap();
                    map.put("fileName",name);
                    map.put("filePath",relPath);

                    ProcessFileTrackDto processFileTrackDto = systemClient.isFileExistence(map).getEntity();
                    if(processFileTrackDto!=null){
                        try (InputStream is= Files.newInputStream(fil.toPath());
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
                        String refId = processFileTrackDto.getRefId();
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
                            this.download(processFileTrackDto,name,refId,submissionId);
                            //save success
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
                File file =new File(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId+File.separator+substring);
                if(!file.exists()){
                    file.mkdirs();
                }
                os= Files.newOutputStream(Paths.get(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId+File.separator+zipEntry.getName()));
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

                new File(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId+File.separator+zipEntry.getName()).mkdirs();
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
        File file =new File(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileName+File.separator+refId+File.separator+RequestForInformationConstants.FILE_NAME_RFI);
        if(!file.exists()){
            file.mkdirs();
        }

        File[] files = file.listFiles();
        for(File  filzz:files){
            if(filzz.isFile() &&filzz.getName().endsWith(RequestForInformationConstants.FILE_FORMAT)){
                try (InputStream fileInputStream =Files.newInputStream(filzz.toPath());
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
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto("intranet");
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
        AuditTrailDto batchJobDto = AuditTrailHelper.getBatchJobDto("INTRANET");
        processFileTrackDto.setAuditTrailDto(batchJobDto);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_COMPLETE);
        systemClient.updateProcessFileTrack(processFileTrackDto);

    }

    private void saveFileRepo(String fileNames,String submissionId,String eventRefNo){
        boolean flag=false;
        File file =new File(sharedPath+File.separator+RequestForInformationConstants.COMPRESS+File.separator+fileNames+File.separator+eventRefNo+File.separator+RequestForInformationConstants.FILE_NAME_RFI+File.separator+RequestForInformationConstants.FILES);
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
                    String[] split = f.getName().split("@");
                    for(int i=1;i<split.length;i++){
                        fileName.append(split[i]);
                    }

                    AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto("intranet");
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
        File file =new File(sharedPath+File.separator+File.separator+"folder");
        File b=new File(sharedPath+File.separator+RequestForInformationConstants.BACKUPS_REC+File.separator);
        File c=new File(sharedPath+File.separator+RequestForInformationConstants.COMPRESS);
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
