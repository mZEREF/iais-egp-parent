package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventInspRecItemNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.InspecSaveBeRecByService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
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
    private String compressPath;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private EventBusHelper eventBusHelper;

    @Override
    public List<ProcessFileTrackDto> getFileTypeAndStatus(String applicationStatusFeToBeRectification, String commonStatusActive) {
        List<ProcessFileTrackDto> processFileTrackDtos = systemBeLicClient.getFileTypeAndStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION,
                ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS).getEntity();
        return processFileTrackDtos;
    }

    @Override
    public void deleteUnZipFile() {
        compressPath = sharedPath + "recUnZipFile";
        download = compressPath + File.separator + "backupsRec";
        zipFile = sharedPath + "backupsRec";
        File downloadFile = new File(download);
        File zipFiles = new File(zipFile);
        File compressPathFile = new File(compressPath);
        //delete old zip and folder
        FileUtils.deleteTempFile(downloadFile);
        FileUtils.deleteTempFile(zipFiles);
        FileUtils.deleteTempFile(compressPathFile);
        //create new
        MiscUtil.checkDirs(downloadFile);
        MiscUtil.checkDirs(zipFiles);
        MiscUtil.checkDirs(compressPathFile);
    }

    @Override
    public List<String> compressFile(List<ProcessFileTrackDto> processFileTrackDtos) {
        List<String> reportIds = new ArrayList<>();
        if(new File(zipFile).isDirectory()){
            File[] files = new File(zipFile).listFiles();
            for(File fil:files) {
                for(ProcessFileTrackDto pDto : processFileTrackDtos){
                    if (fil.getName().endsWith(".zip") && fil.getName().equals(pDto.getFileName())) {
                        try (ZipFile unZipFile = new ZipFile(sharedPath + pDto.getFilePath())) {
                            for (Enumeration<? extends ZipEntry> entries = unZipFile.entries(); entries.hasMoreElements(); ) {
                                ZipEntry zipEntry = entries.nextElement();
                                String reportId = unzipFile(zipEntry, unZipFile);
                                if(!StringUtil.isEmpty(reportId)) {
                                    reportIds.add(reportId);
                                }
                            }
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return reportIds;
    }

    private String unzipFile(ZipEntry zipEntry, ZipFile zipFile)  {
        if(!zipEntry.getName().endsWith(File.separator)) {
            String realPath = compressPath + File.separator + zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator) + 1);
            String reportFilePath = realPath.substring(realPath.lastIndexOf(File.separator,realPath.lastIndexOf(File.separator) - 1) + 1);
            String reportId = reportFilePath.substring(0, reportFilePath.lastIndexOf(File.separator));
            String saveFileName = zipEntry.getName().substring(zipEntry.getName().lastIndexOf(File.separator) + 1);
            log.debug(StringUtil.changeForLog("realPath:" + realPath));
            log.debug(StringUtil.changeForLog("saveFileName:" + saveFileName));
            log.debug(StringUtil.changeForLog("zipEntryName:" + zipEntry.getName()));
            log.debug(StringUtil.changeForLog("zipFileName:" + zipFile.getName()));
            log.debug(StringUtil.changeForLog("reportFilePath:" + reportFilePath));
            log.debug(StringUtil.changeForLog("reportId:" + reportId));

            File uploadRecFile = MiscUtil.generateFile(realPath, saveFileName);
            try(OutputStream os = new FileOutputStream(uploadRecFile);
                BufferedOutputStream bos = new BufferedOutputStream(os);
                InputStream is = zipFile.getInputStream(zipEntry);
                BufferedInputStream bis = new BufferedInputStream(is);
                CheckedInputStream cos = new CheckedInputStream(bis, new CRC32())) {

                byte[] b = new byte[1024];
                int count = cos.read(b);
                while(count != -1){
                    bos.write(b,0, count);
                    count = cos.read(b);
                }

            }catch (IOException e){
                log.error(e.getMessage(), e);
            }
            return reportId;
        } else {
            new File(compressPath + File.separator + zipEntry.getName()).mkdirs();
        }
        return null;
    }

    @Override
    public Boolean saveData(AuditTrailDto intranet, List<ProcessFileTrackDto> processFileTrackDtos, List<String> reportIds) {
        Boolean saveFlag = false;
        File file = new File(download);
        List<String> appPremCorrIds = new ArrayList<>();
        List<String> appIds = new ArrayList<>();
        String submissionId = processFileTrackDtos.get(0).getRefId();
        //file is backupsRec
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(ProcessFileTrackDto pDto:processFileTrackDtos){
                appIds.add(pDto.getRefId());
                for(File file2:files){
                    //file2 is upload Directory, name is file report id
                    String reportId = file2.getName();
                    if(reportIds.contains(reportId)) {
                        String eventRefNo = pDto.getRefId();
                        saveDataDtoAndFile(file2, intranet, submissionId, reportIds);
                        pDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_SAVE_SUCCESSFUL);
                        pDto.setAuditTrailDto(intranet);
                        pDto.setEventRefNo(eventRefNo);

                        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(pDto, submissionId, EventBusConsts.SERVICE_NAME_SYSTEM_ADMIN,
                                EventBusConsts.OPERATION_BE_REC_DATA_COPY, pDto.getEventRefNo(), null);
                    }
                }
            }
        }
        log.debug(StringUtil.changeForLog("appIds:" + appIds.toString()));
        if(!IaisCommonUtils.isEmpty(appIds)){
            Set<String> appIdSet = new HashSet<>(appIds);
            for(String appId : appIdSet){
                AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
                appPremCorrIds.add(appPremisesCorrelationDto.getId());
            }
            log.debug(StringUtil.changeForLog("appPremCorrIds:" + appPremCorrIds.toString()));
        }
        if(!IaisCommonUtils.isEmpty(appPremCorrIds)){
            EventInspRecItemNcDto eventInspRecItemNcDto = new EventInspRecItemNcDto();
            //get Task
            eventInspRecItemNcDto.setAppPremCorrIds(appPremCorrIds);
            eventInspRecItemNcDto.setAuditTrailDto(intranet);
            eventInspRecItemNcDto = organizationClient.getEventInspRecItemNcTaskByCorrIds(eventInspRecItemNcDto).getEntity();

            SubmitResp createTask = eventBusHelper.submitAsyncRequest(eventInspRecItemNcDto, submissionId, EventBusConsts.SERVICE_NAME_ROUNTINGTASK,
                    EventBusConsts.OPERATION_BE_REC_DATA_COPY, eventInspRecItemNcDto.getEventRefNo(), null);

            //get Application / History / Inspection Status
            if(eventInspRecItemNcDto.getTaskDtos() != null) {
                eventInspRecItemNcDto.setAuditTrailDto(intranet);
                eventInspRecItemNcDto = inspectionTaskClient.getEventInspRecItemNcDtoByCorrIds(eventInspRecItemNcDto).getEntity();
            }

            SubmitResp updateApp = eventBusHelper.submitAsyncRequest(eventInspRecItemNcDto, submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_BE_REC_DATA_COPY, eventInspRecItemNcDto.getEventRefNo(), null);

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

    private void saveDataDtoAndFile(File file2, AuditTrailDto intranet, String submissionId, List<String> reportIds) {
        try {
            if(file2.isDirectory()){
                File[] files2 = file2.listFiles();
                List<FileRepoDto> list = new ArrayList<>();
                for (File file3:files2) {
                    //file3 is not Directory, need save
                    String fileReportId = file2.getName();
                    log.debug(StringUtil.changeForLog("fileReportId:" + fileReportId));
                    List<FileRepoDto> fileList = new ArrayList<>();
                    FileRepoDto fileRepoDto = new FileRepoDto();
                    fileRepoDto.setId(fileReportId);
                    fileRepoDto.setAuditTrailDto(intranet);
                    fileRepoDto.setFileName(file3.getName());
                    log.debug(StringUtil.changeForLog("saveDtoFileName:" + file3.getName()));
                    String relativePath = file3.getPath().replaceFirst(sharedPath, "");
                    log.debug(StringUtil.changeForLog("relativePath:" + relativePath));
                    fileRepoDto.setRelativePath(relativePath);
                    fileList.add(fileRepoDto);
                    if(!IaisCommonUtils.isEmpty(fileList)) {
                        list.addAll(fileList);
                    }
                }
                FileRepoEventDto eventDto = new FileRepoEventDto();
                eventDto.setFileRepoList(list);
                eventDto.setEventRefNo(submissionId);
                SubmitResp submitResp = eventBusHelper.submitAsyncRequest(eventDto, submissionId, EventBusConsts.SERVICE_NAME_FILE_REPO,
                        EventBusConsts.OPERATION_BE_REC_DATA_COPY, eventDto.getEventRefNo(), null);

            }
        } catch(Exception e) {
            log.error(e.getMessage(),e);
        }
    }
}