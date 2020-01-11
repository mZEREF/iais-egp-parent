package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.annotation.EicService;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.InspecSaveBeRecByService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
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
    private SubmissionClient submissionClient;
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private FileRepoClient fileRepoClient;

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
                        try (ZipFile zipFile = new ZipFile(pDto.getFilePath())) {
                            for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); ) {
                                ZipEntry zipEntry = entries.nextElement();
                                String fileName = pDto.getFileName().substring(0,pDto.getFileName().lastIndexOf("."));
                                unzipFile(zipEntry, zipFile, fileName);
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
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(ProcessFileTrackDto pDto:processFileTrackDtos){
                String fileName = pDto.getFileName().substring(0,pDto.getFileName().lastIndexOf("."));
                for(File file2:files){
                    if(file2.getName().equals(fileName)){
                        String eventRefNo = pDto.getRefId();
                        saveDataDtoAndFile(file2, intranet, aBoolean, textJson,
                                fileBoolean, eventRefNo);
                        pDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_SAVE_SUCCESSFUL);
                        pDto.setAuditTrailDto(intranet);
                        pDto.setEventRefNo(eventRefNo);
                        String callbackUrl = systemParamConfig.getInterServerName()
                                + "/hcsa-licence-web/eservice/INTRANET/MohInspecSaveRecRollBack";
                        SubmitReq req = EventBusHelper.getSubmitReq(pDto, eventRefNo, "systemAdmin",
                                "updateStatus", "", callbackUrl, "batchjob", false,
                                "INTRANET", "InspecSaveBeRecByFeBatchjob", "start");
                        SubmitResp submitResp = submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE
                                + RestApiUrlConsts.EVENT_BUS, req);
                    }
                }
            }
        }

        return saveFlag;
    }

    @EicService
    private boolean saveEicAndApp(ProcessFileTrackDto pDto, String inspecSaveBeRecByImpl, ApplicationDto applicationDto) {
        return false;
    }

    private void saveDataDtoAndFile(File file2, AuditTrailDto intranet, boolean aBoolean, List<String> textJson,
                                       boolean fileBoolean, String submissionId) {
        try {
            if(file2.isDirectory()){
                File[] files2 = file2.listFiles();
                for(File file3:files2){
                    if(file3.isFile() && file3.getName().endsWith(fileFormat)){
                        FileInputStream fileInputStream = new FileInputStream(file3);
                        ByteArrayOutputStream by = new ByteArrayOutputStream();
                        byte[] size = new byte[1024];
                        int count = fileInputStream.read(size);
                        while(count != -1){
                            by.write(size,0, count);
                            count = fileInputStream.read(size);
                        }
                        fileToDto(by.toString(), intranet, submissionId);
                        textJson.add(by.toString());
                        fileInputStream.close();
                        by.close();
                    }
                }
                List<FileRepoDto> list = new ArrayList<>();
                for (File file3:files2) {
                    if (file3.isDirectory()) {
                        for (String s : textJson) {
                            list.addAll(saveUploadFile(s, file3, intranet, submissionId));
                        }
                    }
                }
                String callbackUrl = systemParamConfig.getInterServerName()
                        + "/hcsa-licence-web/eservice/INTRANET/MohInspecSaveRecRollBack";
                SubmitReq req = EventBusHelper.getSubmitReq(list, submissionId, "fileRepoSave",
                "save", "", callbackUrl, "batchjob", false, "INTRANET",
                        "InspecSaveBeRecByFeBatchjob", "start");
                SubmitResp submitResp = submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE
                        + RestApiUrlConsts.EVENT_BUS, req);

            }
        } catch(Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    private List<FileRepoDto> saveUploadFile(String toString, File file, AuditTrailDto intranet, String submissionId) {
        List<FileRepoDto> list = new ArrayList<>();
        ApplicationListFileDto applicationListFileDto = JsonUtil.parseToObject(toString, ApplicationListFileDto.class);
        boolean flag = false;
        List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = applicationListFileDto.getAppPremPreInspectionNcDocDtos();
        if(!(IaisCommonUtils.isEmpty(appPremPreInspectionNcDocDtos))) {
            for (AppPremPreInspectionNcDocDto aItemDocDto : appPremPreInspectionNcDocDtos) {
                if(file.getName().equals(aItemDocDto.getNcItemId())){
                    list.addAll(genFileByNcItemId(aItemDocDto.getFileRepoId(), file, intranet, submissionId));
                }
            }
        }
        return list;
    }

    private List<FileRepoDto> genFileByNcItemId(String fileReportId, File file, AuditTrailDto intranet,
                        String submissionId) {
        List<FileRepoDto> list = new ArrayList<>();
        boolean flag = false;
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                FileRepoDto fileRepoDto = new FileRepoDto();
                fileRepoDto.setId(fileReportId);
                fileRepoDto.setAuditTrailDto(intranet);
                fileRepoDto.setFileName(file.getName());
                String relativePath = file2.getPath().replaceFirst(sharedPath, "");
                fileRepoDto.setRelativePath(relativePath);
                fileRepoDto.setEventRefNo(submissionId);
                list.add(fileRepoDto);
            }
        }

        return list;
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
