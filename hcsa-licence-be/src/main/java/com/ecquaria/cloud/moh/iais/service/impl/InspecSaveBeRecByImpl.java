package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventInspRecItemNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRecJobFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.InspecSaveBeRecByService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    @Value("${iais.sharedfolder.rectification.in}")
    private String inSharedPath;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private InspectionRectificationProService inspectionRectificationProService;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private EventBusHelper eventBusHelper;

    @Autowired
    private GenerateIdClient generateIdClient;

    public static final int THRESHOLD_ENTRIES = 10000;
    public static final int THRESHOLD_SIZE = 1000000000; // 1 GB
    public static final double THRESHOLD_RATIO = 10;

    private InspRecJobFieldDto getInspRecJobFieldDto() {
        InspRecJobFieldDto inspRecJobFieldDto = new InspRecJobFieldDto();
        String compressPath = sharedPath + "recUnZipFile";
        String download = compressPath + File.separator + "backupsRec";
        String inFolder = inSharedPath;
        if (!inFolder.endsWith(File.separator)) {
            inFolder += File.separator;
        }
        String zipFile = inFolder;
        inspRecJobFieldDto.setDownload(download);
        inspRecJobFieldDto.setCompressPath(compressPath);
        inspRecJobFieldDto.setZipFile(zipFile);
        return inspRecJobFieldDto;
    }

    @Override
    public List<ProcessFileTrackDto> getFileTypeAndStatus(String applicationStatusFeToBeRectification, String commonStatusActive) {
        List<ProcessFileTrackDto> processFileTrackDtos = applicationClient.getFileTypeAndStatus(ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION,
                ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS).getEntity();
        return processFileTrackDtos;
    }

    @Override
    public void deleteUnZipFile() {
        InspRecJobFieldDto inspRecJobFieldDto = getInspRecJobFieldDto();
        String download = inspRecJobFieldDto.getDownload();
        String compressPath = inspRecJobFieldDto.getCompressPath();
        String zipFile = inspRecJobFieldDto.getZipFile();
        File downloadFile = MiscUtil.generateFile(download);
        File zipFiles = MiscUtil.generateFile(zipFile);
        File compressPathFile = MiscUtil.generateFile(compressPath);
        //delete old zip and folder
        FileUtils.deleteTempFile(downloadFile);
        FileUtils.deleteTempFile(compressPathFile);
        //create new
        MiscUtil.checkDirs(downloadFile);
        MiscUtil.checkDirs(zipFiles);
        MiscUtil.checkDirs(compressPathFile);
    }

    @Override
    public List<String> compressFile(List<ProcessFileTrackDto> processFileTrackDtos) {
        InspRecJobFieldDto inspRecJobFieldDto = getInspRecJobFieldDto();
        String zipFile = inspRecJobFieldDto.getZipFile();
        log.debug(StringUtil.changeForLog("zipFile:" + zipFile));
        JobLogger.log(StringUtil.changeForLog("zipFile:" + zipFile));
        List<String> reportIds = IaisCommonUtils.genNewArrayList();
        if(MiscUtil.generateFile(zipFile).isDirectory()){
            File[] files = MiscUtil.generateFile(zipFile).listFiles();
            int allSize = processFileTrackDtos.size();
            int nowSize = 0;
            List<String> appIds = IaisCommonUtils.genNewArrayList();
            for(File fil:files) {
                for(ProcessFileTrackDto pDto : processFileTrackDtos){
                    if (fil.getName().endsWith(".zip") && fil.getName().equals(pDto.getFileName())) {
                        nowSize++;
                        appIds.add(pDto.getRefId());
                    }
                }
            }
            Set<String> appIdSet = new HashSet<>(appIds);
            appIds = new ArrayList<>(appIdSet);
            appIds = filterAppDocSizeFileSize(appIds, processFileTrackDtos);
            log.debug(StringUtil.changeForLog("Rectification allSize:" + allSize));
            JobLogger.log(StringUtil.changeForLog("Rectification allSize:" + allSize));
            log.debug(StringUtil.changeForLog("Rectification nowSize:" + nowSize));
            JobLogger.log(StringUtil.changeForLog("Rectification nowSize:" + nowSize));
            if(allSize == nowSize) {
                for (File fil : files) {
                    for (ProcessFileTrackDto pDto : processFileTrackDtos) {
                        if(appIds.contains(pDto.getRefId())){
                            if (fil.getName().endsWith(".zip") && fil.getName().equals(pDto.getFileName())) {
                                try (ZipFile unZipFile = new ZipFile(zipFile + pDto.getFilePath())) {
                                    int totalEntryArchive = 0;
                                    int totalSizeArchive = 0;
                                    Enumeration<? extends ZipEntry> entries = unZipFile.entries();
                                    while(entries.hasMoreElements()) {
                                        ZipEntry zipEntry = entries.nextElement();
                                        totalEntryArchive ++;

                                        // too much entries in this archive, can lead to inodes exhaustion of the system
                                        if(totalEntryArchive > THRESHOLD_ENTRIES) {
                                            break;
                                        }
                                        //get report Id
                                        String reportId = null;
                                        String compressPath = inspRecJobFieldDto.getCompressPath();
                                        if(!zipEntry.getName().endsWith(File.separator)) {
                                            String realPath = compressPath + File.separator + zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator) + 1);
                                            String reportFilePath = realPath.substring(realPath.lastIndexOf(File.separator,realPath.lastIndexOf(File.separator) - 1) + 1);
                                            reportId = reportFilePath.substring(0, reportFilePath.lastIndexOf(File.separator));
                                            String saveFileName = zipEntry.getName().substring(zipEntry.getName().lastIndexOf(File.separator) + 1);

                                            log.debug(StringUtil.changeForLog("realPath:" + realPath));
                                            log.debug(StringUtil.changeForLog("saveFileName:" + saveFileName));
                                            log.debug(StringUtil.changeForLog("zipEntryName:" + zipEntry.getName()));
                                            log.debug(StringUtil.changeForLog("zipFileName:" + unZipFile.getName()));
                                            log.debug(StringUtil.changeForLog("reportFilePath:" + reportFilePath));
                                            log.debug(StringUtil.changeForLog("reportId:" + reportId));

                                            JobLogger.log(StringUtil.changeForLog("realPath:" + realPath));
                                            JobLogger.log(StringUtil.changeForLog("saveFileName:" + saveFileName));
                                            JobLogger.log(StringUtil.changeForLog("zipEntryName:" + zipEntry.getName()));
                                            JobLogger.log(StringUtil.changeForLog("zipFileName:" + unZipFile.getName()));
                                            JobLogger.log(StringUtil.changeForLog("reportFilePath:" + reportFilePath));
                                            JobLogger.log(StringUtil.changeForLog("reportId:" + reportId));

                                            File uploadRecFile = MiscUtil.generateFile(realPath, saveFileName);
                                            try(OutputStream os = Files.newOutputStream(uploadRecFile.toPath());
                                                BufferedOutputStream bos = new BufferedOutputStream(os);
                                                InputStream is = unZipFile.getInputStream(zipEntry);
                                                BufferedInputStream bis = new BufferedInputStream(is);
                                                CheckedInputStream cos = new CheckedInputStream(bis, new CRC32())) {

                                                byte[] b = new byte[1024];
                                                int nBytes = is.read(b);
                                                int totalSizeEntry = 0;
                                                while(nBytes > 0) { // Compliant
                                                    totalSizeEntry += nBytes;
                                                    totalSizeArchive += nBytes;

                                                    double compressionRatio = division(totalSizeEntry, zipEntry.getCompressedSize(), 0);
                                                    log.debug(StringUtil.changeForLog("totalSizeEntry:" + totalSizeEntry));
                                                    JobLogger.log(StringUtil.changeForLog("totalSizeEntry:" + totalSizeEntry));
                                                    log.debug(StringUtil.changeForLog("compressionRatio:" + compressionRatio));
                                                    JobLogger.log(StringUtil.changeForLog("compressionRatio:" + compressionRatio));
                                                    if(compressionRatio > THRESHOLD_RATIO) {
                                                        // ratio between compressed and uncompressed data is highly suspicious, looks like a Zip Bomb Attack
                                                        break;
                                                    }
                                                }
                                                // the uncompressed data size is too much for the application resource capacity
                                                log.debug(StringUtil.changeForLog("totalSizeArchive:" + totalSizeArchive));
                                                JobLogger.log(StringUtil.changeForLog("totalSizeArchive:" + totalSizeArchive));
                                                if(totalSizeArchive > THRESHOLD_SIZE) {
                                                    break;
                                                }
                                                int count = cos.read(b);
                                                while(count != -1){
                                                    bos.write(b,0, count);
                                                    count = cos.read(b);
                                                }
                                            }catch (IOException e){
                                                log.error(e.getMessage(), e);
                                                JobLogger.log(e.getMessage(), e);
                                            }
                                        } else {
                                            MiscUtil.generateFile(compressPath ,zipEntry.getName()).mkdirs();
                                        }
                                        if (!StringUtil.isEmpty(reportId)) {
                                            reportIds.add(reportId);
                                        }
                                    }
                                } catch (IOException e) {
                                    log.error(e.getMessage(), e);
                                    JobLogger.log(e.getMessage(), e);
                                }
                            }
                        }
                    }
                }
            }
        }
        return reportIds;
    }

    private double division(double a, double b, int accurate) {
        if (accurate < 0) {
            throw new RuntimeException("The accuracy parameter must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(a);
        BigDecimal b2 = BigDecimal.valueOf(b);
        return b1.divide(b2, accurate, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private List<String> filterAppDocSizeFileSize(List<String> appIds, List<ProcessFileTrackDto> processFileTrackDtos) {
        List<String> appIdList = IaisCommonUtils.genNewArrayList();
        for(String appId : appIds){
            log.debug(StringUtil.changeForLog("Application Id:" + appId));
            JobLogger.log(StringUtil.changeForLog("Application Id:" + appId));
            int docSize = getFileSizeByAppId(appId);
            int fileSize = 0;
            for (ProcessFileTrackDto pDto : processFileTrackDtos) {
                if(pDto != null){
                    if(appId.equals(pDto.getRefId())){
                        fileSize++;
                    }
                }
            }
            log.debug(StringUtil.changeForLog("Rectification docSize:" + docSize));
            JobLogger.log(StringUtil.changeForLog("Rectification docSize:" + docSize));
            log.debug(StringUtil.changeForLog("Rectification nowSize:" + fileSize));
            JobLogger.log(StringUtil.changeForLog("Rectification nowSize:" + fileSize));
            if(docSize == fileSize){
                appIdList.add(appId);
            }
        }
        return appIdList;
    }

    private int getFileSizeByAppId(String appId) {
        int fileSize = 0;
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
        if(appPremisesCorrelationDto != null){
            AppPremPreInspectionNcDto appPremPreInspectionNcDto =
                    inspectionRectificationProService.getAppPremPreInspectionNcDtoByCorrId(appPremisesCorrelationDto.getId());
            if(appPremPreInspectionNcDto != null){
                List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos =
                        fillUpCheckListGetAppClient.getAppNcItemByNcId(appPremPreInspectionNcDto.getId()).getEntity();
                if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtos)){
                    for(AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtos){
                        if(appPremisesPreInspectionNcItemDto != null) {
                            List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos =
                                    inspectionRectificationProService.getAppNcDocList(appPremisesPreInspectionNcItemDto.getId());
                            if(appPremPreInspectionNcDocDtos != null) {
                                fileSize = appPremPreInspectionNcDocDtos.size() + fileSize;
                            }
                        }
                    }
                }
            }
        }
        return fileSize;
    }

    @Override
    public void saveData(AuditTrailDto intranet, List<ProcessFileTrackDto> processFileTrackDtos, List<String> reportIds) {
        InspRecJobFieldDto inspRecJobFieldDto = getInspRecJobFieldDto();
        String download = inspRecJobFieldDto.getDownload();
        File file = MiscUtil.generateFile(download);
        List<String> appPremCorrIds = IaisCommonUtils.genNewArrayList();
        List<String> appIds = IaisCommonUtils.genNewArrayList();
        String submissionId = generateIdClient.getSeqId().getEntity();
        String eventRefNo = generateIdClient.getSeqId().getEntity();
        log.info(StringUtil.changeForLog("submissionId:" + submissionId));
        log.info(StringUtil.changeForLog("eventRefNo:" + eventRefNo));
        JobLogger.log(StringUtil.changeForLog("submissionId:" + submissionId));
        JobLogger.log(StringUtil.changeForLog("eventRefNo:" + eventRefNo));
        //file is backupsRec
        List<ProcessFileTrackDto> saveProcessFileTrackDto = IaisCommonUtils.genNewArrayList();
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File file2:files){
                //file2 is upload Directory, name is file report id
                String reportId = file2.getName();
                if(reportIds.contains(reportId)) {
                    saveDataDtoAndFile(file2, intranet, submissionId, eventRefNo);
                }
            }
            for(ProcessFileTrackDto pDto:processFileTrackDtos){
                appIds.add(pDto.getRefId());
                pDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_SAVE_SUCCESSFUL);
                pDto.setAuditTrailDto(intranet);
                pDto.setEventRefNo(eventRefNo);
                saveProcessFileTrackDto.add(pDto);
            }
        }
        StringBuilder strAppIds = new StringBuilder();
        for (String s : appIds) {
            strAppIds.append(s);
        }
        StringBuilder strAppCorrIds = new StringBuilder();
        for (String s : appPremCorrIds) {
            strAppCorrIds.append(s);
        }
        String strAppIdsTo=strAppIds.toString();
        String strAppCorrIdsTo=strAppCorrIds.toString();

        log.info(StringUtil.changeForLog("appIds:" + strAppIdsTo));
        JobLogger.log(StringUtil.changeForLog("appIds:" + strAppIdsTo));
        Map<String, String> appNoCorrMap = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(appIds)){
            Set<String> appIdSet = new HashSet<>(appIds);
            for(String appId : appIdSet){
                ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
                AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
                appPremCorrIds.add(appPremisesCorrelationDto.getId());
                appNoCorrMap.put(applicationDto.getApplicationNo(), appPremisesCorrelationDto.getId());
            }
            log.info(StringUtil.changeForLog("appPremCorrIds:" + strAppCorrIdsTo));
            JobLogger.log(StringUtil.changeForLog("appPremCorrIds:" + strAppCorrIdsTo));
        }
        if(!IaisCommonUtils.isEmpty(appPremCorrIds)){
            EventInspRecItemNcDto eventInspRecItemNcDto = new EventInspRecItemNcDto();
            eventInspRecItemNcDto.setProcessFileTrackDtos(saveProcessFileTrackDto);
            //get Task
            eventInspRecItemNcDto.setEventRefNo(eventRefNo);
            eventInspRecItemNcDto.setAppPremCorrIds(appPremCorrIds);
            eventInspRecItemNcDto = setAppNoListByCorrIds(eventInspRecItemNcDto);
            eventInspRecItemNcDto.setAuditTrailDto(intranet);
            eventInspRecItemNcDto.setAppNoCorrMap(appNoCorrMap);
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
    }

    @Override
    public Map<String, List<ProcessFileTrackDto>> getProcessFileTrackDtosWithAppId(List<ProcessFileTrackDto> processFileTrackDtos) {
        Map<String, List<ProcessFileTrackDto>> appIdProFileMap = IaisCommonUtils.genNewHashMap();
        for(ProcessFileTrackDto processFileTrackDto : processFileTrackDtos){
            String appId = processFileTrackDto.getRefId();
            List<ProcessFileTrackDto> processFileTrackDtoList = appIdProFileMap.get(appId);
            if(IaisCommonUtils.isEmpty(processFileTrackDtoList)){
                processFileTrackDtoList = IaisCommonUtils.genNewArrayList();
            }
            processFileTrackDtoList.add(processFileTrackDto);
            appIdProFileMap.put(appId, processFileTrackDtoList);
        }
        return appIdProFileMap;
    }

    private EventInspRecItemNcDto setAppNoListByCorrIds(EventInspRecItemNcDto eventInspRecItemNcDto) {
        Map<String, String> appNoCorrMap = IaisCommonUtils.genNewHashMap();
        List<String> refNoList = eventInspRecItemNcDto.getAppPremCorrIds();
        if(!IaisCommonUtils.isEmpty(refNoList)){
            Set<String> refNoSet = new HashSet<>(refNoList);
            refNoList = new ArrayList<>(refNoSet);
            for(String refNo : refNoList){
                ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(refNo).getEntity();
                appNoCorrMap.put(applicationDto.getApplicationNo(), refNo);
            }
        }
        eventInspRecItemNcDto.setAppNoCorrMap(appNoCorrMap);
        return eventInspRecItemNcDto;
    }

    private void saveDataDtoAndFile(File file2, AuditTrailDto intranet, String submissionId, String eventRefNo) {
        try {
            if(file2.isDirectory()){
                File[] files2 = file2.listFiles();
                List<FileRepoDto> list = IaisCommonUtils.genNewArrayList();
                for (File file3:files2) {
                    //file3 is not Directory, need save
                    String fileReportId = file2.getName();
                    log.info(StringUtil.changeForLog("fileReportId:" + fileReportId));
                    JobLogger.log(StringUtil.changeForLog("fileReportId:" + fileReportId));
                    List<FileRepoDto> fileList = IaisCommonUtils.genNewArrayList();
                    FileRepoDto fileRepoDto = new FileRepoDto();
                    fileRepoDto.setId(fileReportId);
                    fileRepoDto.setAuditTrailDto(intranet);
                    fileRepoDto.setFileName(file3.getName());
                    log.info(StringUtil.changeForLog("saveDtoFileName:" + file3.getName()));
                    JobLogger.log(StringUtil.changeForLog("saveDtoFileName:" + file3.getName()));
                    String relativePath = file3.getPath().replaceFirst(sharedPath, "");
                    //remove file name
                    String subRelativePath = relativePath.substring(0, relativePath.lastIndexOf(File.separator));
                    log.info(StringUtil.changeForLog("relativePath:" + relativePath));
                    log.info(StringUtil.changeForLog("subRelativePath:" + subRelativePath));
                    JobLogger.log(StringUtil.changeForLog("relativePath:" + relativePath));
                    JobLogger.log(StringUtil.changeForLog("subRelativePath:" + subRelativePath));
                    fileRepoDto.setRelativePath(subRelativePath);
                    fileList.add(fileRepoDto);
                    if(!IaisCommonUtils.isEmpty(fileList)) {
                        list.addAll(fileList);
                    }
                }
                FileRepoEventDto eventDto = new FileRepoEventDto();
                eventDto.setFileRepoList(list);
                eventDto.setEventRefNo(eventRefNo);
                eventDto.setAuditTrailDto(intranet);
                SubmitResp submitResp = eventBusHelper.submitAsyncRequest(eventDto, submissionId, EventBusConsts.SERVICE_NAME_FILE_REPO,
                        EventBusConsts.OPERATION_BE_REC_DATA_COPY, eventDto.getEventRefNo(), null);

            }
        } catch(Exception e) {
            log.error(e.getMessage(),e);
            JobLogger.log(e.getMessage(),e);
        }
    }
}