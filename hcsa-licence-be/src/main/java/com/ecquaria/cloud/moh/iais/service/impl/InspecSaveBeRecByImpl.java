package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoEventDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
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
import java.util.List;
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
                        try (ZipFile unZipFile = new ZipFile(sharedPath + pDto.getFilePath())) {
                            for (Enumeration<? extends ZipEntry> entries = unZipFile.entries(); entries.hasMoreElements(); ) {
                                ZipEntry zipEntry = entries.nextElement();
                                unzipFile(zipEntry, unZipFile);
                            }
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }

    private void unzipFile(ZipEntry zipEntry, ZipFile zipFile)  {
        String realPath = compressPath + File.separator + zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator) + 1);
        String saveFileName = zipEntry.getName().substring(zipEntry.getName().lastIndexOf(File.separator) + 1);
        log.debug(StringUtil.changeForLog("realPath:" + realPath));
        log.debug(StringUtil.changeForLog("saveFileName:" + saveFileName));
        log.debug(StringUtil.changeForLog("zipEntryName:" + zipEntry.getName()));
        log.debug(StringUtil.changeForLog("zipFileName:" + zipFile.getName()));

        File uploadRecFile = MiscUtil.generateFile(realPath, saveFileName);
        try(OutputStream os = new FileOutputStream(uploadRecFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            InputStream is = zipFile.getInputStream(zipEntry);
            BufferedInputStream bis = new BufferedInputStream(is);
            CheckedInputStream cos = new CheckedInputStream(bis, new CRC32())) {
            if(!zipEntry.getName().endsWith(File.separator)){
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
        File file = new File(download);
        List<String> appPremCorrIds = new ArrayList<>();
        List<String> appIds = new ArrayList<>();
        //file is backupsRec
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(ProcessFileTrackDto pDto:processFileTrackDtos){
                appIds.add(pDto.getRefId());
                for(File file2:files){
                    //file2 is upload Directory, name is file report id
                    String eventRefNo = pDto.getRefId();
                    saveDataDtoAndFile(file2, intranet, eventRefNo);
                    pDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_SAVE_SUCCESSFUL);
                    pDto.setAuditTrailDto(intranet);
                    pDto.setEventRefNo(eventRefNo);
                    String callbackUrl = systemParamConfig.getInterServerName()
                            + "/hcsa-licence-web/eservice/INTRANET/MohInspecSaveRecRollBack";
                    SubmitResp submitResp = eventBusHelper.submitAsyncRequest(pDto, eventRefNo, EventBusConsts.SERVICE_NAME_SYSTEM_ADMIN,
                            EventBusConsts.OPERATION_BE_REC_DATA_COPY, pDto.getEventRefNo(), null);
                }
            }
        }
        /*if(!IaisCommonUtils.isEmpty(appIds)){
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
        }*/

        return saveFlag;
    }

    private void saveDataDtoAndFile(File file2, AuditTrailDto intranet, String submissionId) {
        try {
            if(file2.isDirectory()){
                File[] files2 = file2.listFiles();
                List<FileRepoDto> list = new ArrayList<>();
                for (File file3:files2) {
                    //file3 is not Directory, need save
                    String fileReportId = file2.getName();
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