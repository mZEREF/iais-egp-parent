package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.RequestForInformationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

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
import java.util.Enumeration;
import java.util.HashMap;
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

    @Value("${iais.syncFileTracking.shared.path}")
    private     String sharedPath;
    private     String download;
    private     String backups;
    private     String fileFormat=".text";
    private     String compressPath;
    private     String downZip;

    @Autowired
    private SystemBeLicClient systemClient;

    private static final String FOLDER="folder";

    private final String[] appType=new String[]{
            ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,
            ApplicationConsts.APPLICATION_TYPE_RENEWAL,
            ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,
            ApplicationConsts.APPLICATION_TYPE_APPEAL,
            ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT,
            ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL
    };
    private final String[] appStatus=new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,
            ApplicationConsts.APPLICATION_STATUS_APPROVED,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
            ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,
            ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING,
            ApplicationConsts.APPLICATION_STATUS_REJECTED,
            ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,
            ApplicationConsts.APPLICATION_STATUS_DRAFT,
            ApplicationConsts.APPLICATION_STATUS_DELETED,
            ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
            ApplicationConsts.APPLICATION_STATUS_SUPPORT,
            ApplicationConsts.APPLICATION_STATUS_VERIFIED,
            ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_INFORMATION,
            ApplicationConsts.APPLICATION_STATUS_LICENCE_START_DATE,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION,
            ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION,
            ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY,
            ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION
    };
    private final String[] licServiceType=new String[]{
            ApplicationConsts.SERVICE_CONFIG_TYPE_BASE,
            ApplicationConsts.SERVICE_CONFIG_TYPE_SPECIFIED,
            ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED
    };
    private final String[] licStatus=new String[]{
            ApplicationConsts.LICENCE_STATUS_ACTIVE,
            ApplicationConsts.LICENCE_STATUS_IACTIVE,
            ApplicationConsts.LICENCE_STATUS_EXPIRY
    };

    @Override
    public List<SelectOption> getAppTypeOption() {
        return MasterCodeUtil.retrieveOptionsByCodes(appType);
    }

    @Override
    public List<SelectOption> getAppStatusOption() {
        return  MasterCodeUtil.retrieveOptionsByCodes(appStatus);
    }

    @Override
    public List<SelectOption> getLicSvcTypeOption() {
        return MasterCodeUtil.retrieveOptionsByCodes(licServiceType);
    }

    @Override
    public List<SelectOption> getLicStatusOption() {
        return MasterCodeUtil.retrieveOptionsByCodes(licStatus);
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
    public List<String> getSvcNamesByType(String type) {
        return hcsaConfigClient.getHcsaServiceNameByType(type).getEntity();
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
    public void compress(LicPremisesReqForInfoDto licPremisesReqForInfoDto){
        log.info("-------------compress start ---------");
        if(new File(backups).isDirectory()){
            File[] files = new File(backups).listFiles();
            for(File fil:files){
                if(fil.getName().endsWith(".zip")){
                    String name = fil.getName();
                    String path = fil.getPath();
                    HashMap<String,String> map=new HashMap<>();
                    map.put("fileName",name);
                    map.put("filePath",path);

                    ProcessFileTrackDto processFileTrackDto = systemClient.isFileExistence(map).getEntity();
                    if(processFileTrackDto!=null){

                        CheckedInputStream cos=null;
                        BufferedInputStream bis=null;
                        BufferedOutputStream bos=null;
                        OutputStream os=null;
                        try (ZipFile zipFile=new ZipFile(path);)  {
                            for(Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();){
                                ZipEntry zipEntry = entries.nextElement();
                                zipFile(zipEntry,os,bos,zipFile,bis,cos,name);
                            }

                        } catch (IOException e) {
                            log.error(e.getMessage(),e);
                        }

                        try {

                            this.download(processFileTrackDto,name);
                            //save success
                        }catch (Exception e){
                            //save bad

                        }
                    }
                }
            }
        }
    }

    private void zipFile( ZipEntry zipEntry, OutputStream os,BufferedOutputStream bos,ZipFile zipFile ,BufferedInputStream bis,CheckedInputStream cos,String fileName)  {


        try {
            if(!zipEntry.getName().endsWith(File.separator)){

                String substring = zipEntry.getName().substring(0, zipEntry.getName().lastIndexOf(File.separator));
                File file =new File(compressPath+File.separator+fileName+File.separator+substring);
                if(!file.exists()){
                    file.mkdirs();
                }
                os=new FileOutputStream(compressPath+File.separator+fileName+File.separator+zipEntry.getName());
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

                new File(compressPath+File.separator+fileName+File.separator+zipEntry.getName()).mkdirs();
            }
        }catch (IOException ignored){

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
    public boolean download( ProcessFileTrackDto processFileTrackDto,String fileName) {

        Boolean flag=false;
        File file =new File(downZip+File.separator+fileName+File.separator+FOLDER);
        if(!file.exists()){
            file.mkdirs();
        }
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File  filzz:files){
                if(filzz.isFile() &&filzz.getName().endsWith(fileFormat)){
                    try (FileInputStream fileInputStream =new FileInputStream(filzz);
                         ByteArrayOutputStream by=new ByteArrayOutputStream();) {

                        int count=0;
                        byte [] size=new byte[1024];
                        count=fileInputStream.read(size);
                        while(count!=-1){
                            by.write(size,0,count);
                            count= fileInputStream.read(size);
                        }
                        Boolean aBoolean = fileToDto(by.toString());
                        flag=aBoolean;
                        if(aBoolean&&processFileTrackDto!=null){
                            changeStatus(processFileTrackDto);
                            saveFileRepo( fileName);
                        }
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }
                }
            }
        }
        return flag;
    }
    private Boolean fileToDto(String str){
        LicPremisesReqForInfoDto licPremisesReqForInfoDto1 = JsonUtil.parseToObject(str, LicPremisesReqForInfoDto.class);
        return requestForInformationClient.rfiFeUpdateToBe(licPremisesReqForInfoDto1).getStatusCode() == 200;

    }

    private void changeStatus( ProcessFileTrackDto processFileTrackDto){
        /*  applicationClient.updateStatus().getEntity();*/
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        AuditTrailDto batchJobDto = AuditTrailHelper.getBatchJobDto("INTRANET");
        processFileTrackDto.setAuditTrailDto(batchJobDto);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_COMPLETE);
        systemClient.updateProcessFileTrack(processFileTrackDto);

    }

    private void saveFileRepo(String fileNames){
        boolean aBoolean=false;
        File file =new File(downZip+File.separator+fileNames+File.separator+FOLDER+File.separator+"files");
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
                            log.error(e.getMessage(),e);
                        }
                        try ( InputStream input = new FileInputStream(f);){
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
                        fileRepoDto.setRelativePath(downZip+File.separator+fileNames+File.separator+FOLDER+File.separator+"files");
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

    @Override
    public void delete() {
        download= sharedPath+File.separator+"compress"+File.separator+FOLDER;
        backups=sharedPath+File.separator+"backups"+File.separator;
        compressPath=sharedPath+File.separator+"compress";
        downZip=sharedPath+File.separator+"compress";
        File file =new File(download);
        File b=new File(backups);
        File c=new File(compressPath);
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
}
