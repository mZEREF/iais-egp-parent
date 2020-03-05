package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventInspRecItemNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.InspecSaveBeRecByService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Shicheng
 * @date 2019/12/27 14:15
 **/
@Service
@Slf4j
public class InspecSaveBeRecByImpl implements InspecSaveBeRecByService {
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    private String download;
    private String zipFile;
    private String fileFormat = ".text";
    private String compressPath;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Autowired
    private SubmissionClient submissionClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private ApplicationService applicationService;

    @Override
    public List<ProcessFileTrackDto> getFileTypeAndStatus(String applicationStatusFeToBeRectification, String commonStatusActive) {
        List<ProcessFileTrackDto> processFileTrackDtos = systemBeLicClient.getFileTypeAndStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION,
                ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS).getEntity();
        return processFileTrackDtos;
    }

    @Override
    public void deleteUnZipFile() {
        compressPath = sharedPath + "recUnZipFile";
        download = compressPath + File.separator + "userRecFile";
        zipFile = sharedPath + "backupsRec";
        File file = new File(download);
        File b = new File(zipFile);
        File c = new File(compressPath);
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
    public void compressFile(List<ProcessFileTrackDto> processFileTrackDtos) {
        if(new File(zipFile).isDirectory()){
            File[] files = new File(zipFile).listFiles();
            for(File fil:files) {
                for(ProcessFileTrackDto pDto : processFileTrackDtos){
                    if (fil.getName().endsWith(".zip") && fil.getName().equals(pDto.getFileName())) {
                        try (ZipFile unZipFile = new ZipFile(pDto.getFilePath())) {
                            for (Enumeration<? extends ZipEntry> entries = unZipFile.entries(); entries.hasMoreElements(); ) {
                                ZipEntry zipEntry = entries.nextElement();
                                String fileName = pDto.getFileName().substring(0,pDto.getFileName().lastIndexOf("."));
                                unzipFile(zipEntry, unZipFile, fileName);
                            }
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }

    private void unzipFile(ZipEntry zipEntry, ZipFile zipFile, String fileName)  {
        try(OutputStream os = new FileOutputStream(compressPath + File.separator + zipEntry.getName());
            BufferedOutputStream bos = new BufferedOutputStream(os);
            InputStream is = zipFile.getInputStream(zipEntry);
            BufferedInputStream bis = new BufferedInputStream(is);
            CheckedInputStream cos = new CheckedInputStream(bis, new CRC32())) {
            if(!zipEntry.getName().endsWith(File.separator)){
                File file = new File(compressPath + File.separator + zipEntry.getName().substring(0,zipEntry.getName().lastIndexOf(File.separator)) + File.separator + fileName);
                if(!file.exists()){
                    file.mkdirs();
                }
                byte[] b = new byte[1024];
                int count = cos.read(b);
                while(count != -1){
                    bos.write(b,0, count);
                    count = cos.read(b);
                }
            }else {
                new File(compressPath + File.separator + zipEntry.getName()).mkdirs();
            }
        }catch (IOException e){
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Boolean saveData(AuditTrailDto intranet, List<ProcessFileTrackDto> processFileTrackDtos) {
        Boolean saveFlag = false;
        List<String> textJson = new ArrayList<>();
        Boolean fileBoolean = false;
        Boolean aBoolean = false;
        File file = new File(download);
        List<String> appPremCorrIds = new ArrayList<>();
        List<String> appIds = new ArrayList<>();
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(ProcessFileTrackDto pDto:processFileTrackDtos){
                appIds.add(pDto.getRefId());
                String fileName = pDto.getFileName().substring(0,pDto.getFileName().lastIndexOf("."));
                for(File file2:files){
                    //file2 is zip
                    if(file2.getName().equals(fileName)){
                        String eventRefNo = pDto.getRefId();
                        saveDataDtoAndFile(file2, intranet, aBoolean, textJson,
                                fileBoolean, eventRefNo);
                        pDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_SAVE_SUCCESSFUL);
                        pDto.setAuditTrailDto(intranet);
                        pDto.setEventRefNo(eventRefNo);
                        String callbackUrl = systemParamConfig.getInterServerName()
                                + "/hcsa-licence-web/eservice/INTRANET/MohInspecSaveRecRollBack";
                        SubmitReq req = EventBusHelper.getSubmitReq(pDto, eventRefNo, EventBusConsts.SERVICE_NAME_SYSTEM_ADMIN,
                                EventBusConsts.OPERATION_BE_REC_DATA_COPY, "", callbackUrl, "batchjob", false,
                                "INTRANET", "InspecSaveBeRecByFeBatchjob", "start");
                        SubmitResp submitResp = submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE
                                + RestApiUrlConsts.EVENT_BUS, req);
                    }
                }
            }
        }
        if(!IaisCommonUtils.isEmpty(appIds)){
            Set<String> appIdSet = new HashSet<>(appIds);
            for(String appId : appIdSet){
                AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
                appPremCorrIds.add(appPremisesCorrelationDto.getId());
            }
        }
        if(!IaisCommonUtils.isEmpty(appPremCorrIds)){
            EventInspRecItemNcDto eventInspRecItemNcDto = new EventInspRecItemNcDto();
            //get Task
            eventInspRecItemNcDto.setAppPremCorrIds(appPremCorrIds);
            eventInspRecItemNcDto.setAuditTrailDto(intranet);
            eventInspRecItemNcDto = organizationClient.getEventInspRecItemNcTaskByCorrIds(eventInspRecItemNcDto).getEntity();
            String callTaskBackUrl = systemParamConfig.getInterServerName()
                    + "/hcsa-licence-web/eservice/INTRANET/MohInspecSaveRecRollBack";
            SubmitReq reqTask = EventBusHelper.getSubmitReq(eventInspRecItemNcDto, appPremCorrIds.get(0), EventBusConsts.SERVICE_NAME_ROUNTINGTASK,
                    EventBusConsts.OPERATION_BE_REC_DATA_COPY, "", callTaskBackUrl, "batchjob",
                    false, "INTRANET",
                    "InspecSaveBeRecByFeBatchjob", "start");
            SubmitResp submitTaskResp = submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE
                    + RestApiUrlConsts.EVENT_BUS, reqTask);

            //get Application / History / Inspection Status
            if(eventInspRecItemNcDto.getTaskDtos() != null) {
                eventInspRecItemNcDto.setAuditTrailDto(intranet);
                eventInspRecItemNcDto = inspectionTaskClient.getEventInspRecItemNcDtoByCorrIds(eventInspRecItemNcDto).getEntity();
            }
            String callbackUrl = systemParamConfig.getInterServerName()
                    + "/hcsa-licence-web/eservice/INTRANET/MohInspecSaveRecRollBack";
            SubmitReq req = EventBusHelper.getSubmitReq(eventInspRecItemNcDto, appPremCorrIds.get(0), EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_BE_REC_DATA_COPY, "", callbackUrl, "batchjob",
                    false, "INTRANET",
                    "InspecSaveBeRecByFeBatchjob", "start");
            SubmitResp submitResp = submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE
                    + RestApiUrlConsts.EVENT_BUS, req);

            //update Fe application
            if(!IaisCommonUtils.isEmpty(eventInspRecItemNcDto.getApplicationDtos())){
                for(ApplicationDto applicationDto : eventInspRecItemNcDto.getApplicationDtos()){
                    applicationDto.setAuditTrailDto(intranet);
                    applicationService.updateFEApplicaiton(applicationDto);
                }
            }
        }

        return saveFlag;
    }

    private void saveDataDtoAndFile(File file2, AuditTrailDto intranet, boolean aBoolean, List<String> textJson,
                                       boolean fileBoolean, String submissionId) {
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream by = null;
        try {
            if(file2.isDirectory()){
                File[] files2 = file2.listFiles();
                for(File file3:files2){
                    if(file3.isFile() && file3.getName().endsWith(fileFormat)){
                        fileInputStream = new FileInputStream(file3);
                        by = new ByteArrayOutputStream();
                        byte[] size = new byte[1024];
                        int count = fileInputStream.read(size);
                        while(count != -1){
                            by.write(size,0, count);
                            count = fileInputStream.read(size);
                        }
                        fileToDto(by.toString(), intranet, submissionId);
                        textJson.add(by.toString());

                    }
                }
                List<FileRepoDto> list = new ArrayList<>();
                for (File file3:files2) {
                    //file3 is file Directory
                    if (file3.isDirectory()) {
                        String fileReportId = file3.getName();
                        File[] files = file3.listFiles();
                        List<FileRepoDto> fileList = new ArrayList<>();
                        for (File fileReport : files) {
                            FileRepoDto fileRepoDto = new FileRepoDto();
                            fileRepoDto.setId(fileReportId);
                            fileRepoDto.setAuditTrailDto(intranet);
                            fileRepoDto.setFileName(fileReport.getName());
                            String relativePath = fileReport.getPath().replaceFirst(sharedPath, "");
                            fileRepoDto.setRelativePath(relativePath);
                            fileRepoDto.setEventRefNo(submissionId);
                            fileList.add(fileRepoDto);
                        }
                        if(!IaisCommonUtils.isEmpty(fileList)) {
                            list.addAll(fileList);
                        }

                    }
                }
                String callbackUrl = systemParamConfig.getInterServerName()
                        + "/hcsa-licence-web/eservice/INTRANET/MohInspecSaveRecRollBack";
                SubmitReq req = EventBusHelper.getSubmitReq(list, submissionId, EventBusConsts.SERVICE_NAME_FILE_REPO,
                        EventBusConsts.OPERATION_BE_REC_DATA_COPY, "", callbackUrl, "batchjob", false, "INTRANET",
                        "InspecSaveBeRecByFeBatchjob", "start");
                SubmitResp submitResp = submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE
                        + RestApiUrlConsts.EVENT_BUS, req);

            }
        } catch(Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
                if(fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(by != null) {
                    by.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void fileToDto(String toString, AuditTrailDto intranet, String submissionId) {
        ApplicationListFileDto applicationListFileDto = JsonUtil.parseToObject(toString, ApplicationListFileDto.class);
        applicationListFileDto.setAuditTrailDto(intranet);
        String callbackUrl = systemParamConfig.getInterServerName()
                + "/hcsa-licence-web/eservice/INTRANET/MohInspecSaveRecRollBack";
        SubmitReq req = EventBusHelper.getSubmitReq(applicationListFileDto, submissionId, "licenceSave",
                EventBusConsts.OPERATION_BE_REC_DATA_COPY, "", callbackUrl, "batchjob",
                false, "INTRANET",
                "InspecSaveBeRecByFeBatchjob", "start");
        SubmitResp submitResp = submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE
                + RestApiUrlConsts.EVENT_BUS, req);
    }
}
