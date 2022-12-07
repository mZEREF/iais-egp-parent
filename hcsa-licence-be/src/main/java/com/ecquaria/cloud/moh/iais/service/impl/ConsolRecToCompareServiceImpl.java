package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.AppGroupExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.AppLicExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.AppProcessFileTrackExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.ApplicationExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.LicEicTrackExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.LicenceExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.MonitoringSheetsDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoring.excel.UserAccountExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.ExcelSheetDto;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.ConsolRecToCompareService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.systeminfo.ServicesSysteminfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

/**
 * ConsolRecToCompareServiceImpl
 *
 * @author junyu
 * @date 2022/5/19
 */
@Service
@Slf4j
public class ConsolRecToCompareServiceImpl implements ConsolRecToCompareService {
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    @Value("${iais.sharedfolder.datacompair.in}")
    private String inSharedPath;
    @Value("${iais.sharedfolder.datacompair.out}")
    private String outSharedPath;
    @Value("${iais.sharedfolder.datacompair.rslt}")
    private String rsltSharedPath;
    @Value("${iais.sharedfolder.datacompair.subfolder:folder}")
    private String subFolder;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private EmailSmsClient emailSmsClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Value("${iais.datacompair.email}")
    private String mailRecipient ;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Override
    public void initPath() {
        File compress = MiscUtil.generateFile(sharedPath+File.separator+ AppServicesConsts.COMPRESS,subFolder);
        File backups=MiscUtil.generateFile(inSharedPath);
        File rslt=MiscUtil.generateFile(rsltSharedPath);
        File compressPath=MiscUtil.generateFile(sharedPath,AppServicesConsts.COMPRESS);
        File movePath=MiscUtil.generateFile(sharedPath,"move");
        if(!compressPath.exists()){
            compressPath.mkdirs();
        }
        if(!backups.exists()){
            backups.mkdirs();
        }
        if(!rslt.exists()){
            rslt.mkdirs();
        }
        if(!compress.exists()){
            compress.mkdirs();
        }
        if(!movePath.exists()){
            movePath.mkdirs();
        }
    }

    @Override
    public void decompression() {
        String outFolder = outSharedPath;
        if (!outFolder.endsWith(File.separator)) {
            outFolder += File.separator;
        }
        log.info("-------------decompression start ---------");
        MonitoringSheetsDto monitoringAppSheetsDto=applicationClient.getMonitoringAppSheetsDto().getEntity();
        MonitoringSheetsDto monitoringLicSheetsDto=hcsaLicenceClient.getMonitoringLicenceSheetsDto().getEntity();
        MonitoringSheetsDto monitoringUserSheetsDto=organizationClient.getMonitoringUserSheetsDto().getEntity();
        monitoringAppSheetsDto.setLicenceExcelDtoMap(monitoringLicSheetsDto.getLicenceExcelDtoMap());
        monitoringAppSheetsDto.setLicEicTrackExcelDtoMap(monitoringLicSheetsDto.getLicEicTrackExcelDtoMap());
        monitoringAppSheetsDto.setAppLicExcelDtoMap(monitoringLicSheetsDto.getAppLicExcelDtoMap());
        monitoringAppSheetsDto.setUserAccountExcelDtoMap(monitoringUserSheetsDto.getUserAccountExcelDtoMap());

        String str = JsonUtil.parseToJson(monitoringAppSheetsDto);
        String s = "";
        try{
            Date date = new Date();
            String dateStr = Formatter.formatDateTime(date, Formatter.DATE_FILE);
            s =  "BECompareResults_"+dateStr;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        File file = MiscUtil.generateFile(outFolder+ subFolder, s+AppServicesConsts.FILE_FORMAT);
        try (OutputStream fileOutputStream  = newOutputStream(file.toPath());) {
            if(!file.exists()){
                boolean newFile = file.createNewFile();
                if(newFile){
                    log.info("***newFile createNewFile***");
                }
            }
            fileOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error(e.getMessage(),e);

        }
        saveFileToOtherNodes(str.getBytes(StandardCharsets.UTF_8),s+AppServicesConsts.FILE_FORMAT,outFolder+ subFolder);

    }

    @Override
    public void compareFeBe() {
        String inFolder = inSharedPath;
        if (!inFolder.endsWith(File.separator)) {
            inFolder += File.separator;
        }
        List<ProcessFileTrackDto> processFileTrackDtos = applicationClient.getFileTypeAndStatus(ApplicationConsts.APP_GROUP_MISC_TYPE_TRANSFER_REASON,
                ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS).getEntity();
        if(processFileTrackDtos!=null&&!processFileTrackDtos.isEmpty()){
            log.info(StringUtil.changeForLog("-----start process file-----, process file size ==>" + processFileTrackDtos.size()));
            for (ProcessFileTrackDto v : processFileTrackDtos) {
                File file = MiscUtil.generateFile(inFolder , v.getFileName());
                if(file.exists()&&file.isFile()){
                    String name = file.getName();
                    String path = file.getPath();
                    log.info(StringUtil.changeForLog("-----file name is " + name + "====> file path is ==>" + path));

                    /**************/
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

                        boolean aBoolean=download(name,refId);
                        if(aBoolean){
                            log.info("start remove file start");
                            moveFile(file);
                            log.info("update file track start");
                            v.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_SEND_TSAK_SUCCESS);
                            applicationClient.updateProcessFileTrack(v);
                        }

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

    public Boolean  download( String fileName
            ,String groupPath)  throws Exception {

        boolean flag = false;

        File file =MiscUtil.generateFile(sharedPath+File.separator+AppServicesConsts.COMPRESS+File.separator+fileName+
                File.separator+groupPath+File.separator+subFolder,groupPath);
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
                        flag=fileToDto(by.toString());

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

    private List<ExcelSheetDto> getExcelSheetDtos(MonitoringSheetsDto sheetsDto) {
        List<AppGroupExcelDto> groupExcelDtos= IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(sheetsDto.getAppGroupExcelDtoMap())){
            for (Map.Entry<String,AppGroupExcelDto> entry:sheetsDto.getAppGroupExcelDtoMap().entrySet()
            ) {
                groupExcelDtos.add(entry.getValue());
            }
        }
        List<ApplicationExcelDto> applicationExcelDtos= IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(sheetsDto.getApplicationExcelDtoMap())){
            for (Map.Entry<String,ApplicationExcelDto> entry:sheetsDto.getApplicationExcelDtoMap().entrySet()
            ) {
                applicationExcelDtos.add(entry.getValue());
            }
        }
        List<AppProcessFileTrackExcelDto> appProcessFileTrackExcelDtos= IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(sheetsDto.getAppProcessFileTrackExcelDtoMap())){
            for (Map.Entry<String,AppProcessFileTrackExcelDto> entry:sheetsDto.getAppProcessFileTrackExcelDtoMap().entrySet()
            ) {
                appProcessFileTrackExcelDtos.add(entry.getValue());
            }
        }
        List<LicenceExcelDto> licenceExcelDtos= IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(sheetsDto.getLicenceExcelDtoMap())){
            for (Map.Entry<String,LicenceExcelDto> entry:sheetsDto.getLicenceExcelDtoMap().entrySet()
            ) {
                licenceExcelDtos.add(entry.getValue());
            }
        }
        List<UserAccountExcelDto> userAccountExcelDtos= IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(sheetsDto.getUserAccountExcelDtoMap())){
            for (Map.Entry<String,UserAccountExcelDto> entry:sheetsDto.getUserAccountExcelDtoMap().entrySet()
            ) {
                userAccountExcelDtos.add(entry.getValue());
            }
        }
        List<AppLicExcelDto> appLicExcelDtos= IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(sheetsDto.getAppLicExcelDtoMap())){
            for (Map.Entry<String,AppLicExcelDto> entry:sheetsDto.getAppLicExcelDtoMap().entrySet()
            ) {
                appLicExcelDtos.add(entry.getValue());
            }
        }
        List<LicEicTrackExcelDto> licEicTrackExcelDtos= IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(sheetsDto.getLicEicTrackExcelDtoMap())){
            for (Map.Entry<String,LicEicTrackExcelDto> entry:sheetsDto.getLicEicTrackExcelDtoMap().entrySet()
            ) {
                licEicTrackExcelDtos.add(entry.getValue());
            }
        }
        List<ExcelSheetDto> excelSheetDtos = IaisCommonUtils.genNewArrayList();
        int sheetAt = 0;
        excelSheetDtos.add(getExcelSheetDto(sheetAt++, "app_group", groupExcelDtos,AppGroupExcelDto.class));
        excelSheetDtos.add(getExcelSheetDto(sheetAt++, "application", applicationExcelDtos,ApplicationExcelDto.class));
        excelSheetDtos.add(getExcelSheetDto(sheetAt++, "apft", appProcessFileTrackExcelDtos,AppProcessFileTrackExcelDto.class));
        excelSheetDtos.add(getExcelSheetDto(sheetAt++, "licence", licenceExcelDtos,LicenceExcelDto.class));
        excelSheetDtos.add(getExcelSheetDto(sheetAt++, "user_account", userAccountExcelDtos,UserAccountExcelDto.class));
        excelSheetDtos.add(getExcelSheetDto(sheetAt++, "lic_app_correlation", appLicExcelDtos,AppLicExcelDto.class));
        excelSheetDtos.add(getExcelSheetDto(sheetAt, "lic_eic_request_tracking", licEicTrackExcelDtos,LicEicTrackExcelDto.class));

        return excelSheetDtos;
    }

    private ExcelSheetDto getExcelSheetDto(int sheetAt, String sheetName, List<? extends Serializable> data ,Class<? extends Serializable> sourceClass) {
        ExcelSheetDto excelSheetDto = new ExcelSheetDto();
        excelSheetDto.setSheetAt(sheetAt);
        excelSheetDto.setSheetName(sheetName);
        excelSheetDto.setBlock(false);
        excelSheetDto.setPwd(Formatter.formatDateTime(new Date(), "yyyyMMdd"));
        excelSheetDto.setStartRowIndex(2);
        excelSheetDto.setSource(data);
        excelSheetDto.setSourceClass(sourceClass);
        excelSheetDto.setDefaultRowHeight((short) 600);
        excelSheetDto.setChangeHeight(false);
        switch (sheetName){
            case "app_group":excelSheetDto.setWidthMap(getGrpWidthMap());break;
            case "application":excelSheetDto.setWidthMap(getAppWidthMap());break;
            case "apft":excelSheetDto.setWidthMap(getApftWidthMap());break;
            case "licence":excelSheetDto.setWidthMap(getLicWidthMap());break;
            case "user_account":excelSheetDto.setWidthMap(getUserWidthMap());break;
            case "lic_app_correlation":excelSheetDto.setWidthMap(getAppLicWidthMap());break;
            case "lic_eic_request_tracking":excelSheetDto.setWidthMap(getLicEicWidthMap());break;
            default:
        }

        return excelSheetDto;
    }


    public static Map<Integer, Integer> getAppWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(14);
        widthMap.put(0, 40);
        widthMap.put(1, 30);
        widthMap.put(2, 18);
        widthMap.put(3, 18);
        widthMap.put(4, 40);
        widthMap.put(5, 18);
        widthMap.put(6, 30);
        widthMap.put(7, 18);
        widthMap.put(8, 18);
        widthMap.put(9, 30);
        widthMap.put(10, 15);
        widthMap.put(11, 15);
        widthMap.put(12, 15);
        return widthMap;
    }
    public static Map<Integer, Integer> getGrpWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(18);
        widthMap.put(0, 40);
        widthMap.put(1, 28);
        widthMap.put(2, 18);
        widthMap.put(3, 18);
        widthMap.put(4, 18);
        widthMap.put(5, 40);
        widthMap.put(6, 18);
        widthMap.put(7, 18);
        widthMap.put(8, 28);
        widthMap.put(9, 18);
        widthMap.put(10, 18);
        widthMap.put(11, 18);
        widthMap.put(12, 40);
        widthMap.put(13, 18);
        widthMap.put(14, 18);
        widthMap.put(15, 15);
        widthMap.put(16, 15);
        return widthMap;
    }
    public static Map<Integer, Integer> getLicWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(20);
        widthMap.put(0, 40);

        widthMap.put(1, 30);
        widthMap.put(2, 18);
        widthMap.put(3, 18);
        widthMap.put(4, 40);
        widthMap.put(5, 15);
        widthMap.put(6, 18);
        widthMap.put(7, 30);
        widthMap.put(8, 30);

        widthMap.put(9, 30);
        widthMap.put(10, 18);
        widthMap.put(11, 18);
        widthMap.put(12, 40);
        widthMap.put(13, 15);
        widthMap.put(14, 18);
        widthMap.put(15, 30);
        widthMap.put(16, 30);

        widthMap.put(17, 15);
        widthMap.put(18, 15);
        widthMap.put(19, 15);

        return widthMap;
    }
    public static Map<Integer, Integer> getUserWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(12);
        widthMap.put(0, 40);
        widthMap.put(1, 20);
        widthMap.put(2, 20);
        widthMap.put(3, 15);
        widthMap.put(4, 18);
        widthMap.put(5, 18);
        widthMap.put(6, 20);
        widthMap.put(7, 15);
        widthMap.put(8, 20);
        widthMap.put(9, 15);
        widthMap.put(10, 15);
        widthMap.put(11, 15);
        return widthMap;
    }
    public static Map<Integer, Integer> getApftWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(12);
        widthMap.put(0, 40);

        widthMap.put(1, 40);
        widthMap.put(2, 40);
        widthMap.put(3, 10);
        widthMap.put(4, 15);

        widthMap.put(5, 40);
        widthMap.put(6, 40);
        widthMap.put(7, 10);
        widthMap.put(8, 15);

        widthMap.put(9, 15);
        widthMap.put(10, 15);
        widthMap.put(11, 15);

        return widthMap;
    }

    public static Map<Integer, Integer> getAppLicWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(10);
        widthMap.put(0, 40);

        widthMap.put(1, 40);
        widthMap.put(2, 40);
        widthMap.put(3, 25);

        widthMap.put(4, 40);
        widthMap.put(5, 40);
        widthMap.put(6, 25);

        widthMap.put(7, 15);
        widthMap.put(8, 15);
        widthMap.put(9, 15);

        return widthMap;
    }

    public static Map<Integer, Integer> getLicEicWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(9);
        widthMap.put(0, 40);
        widthMap.put(1, 30);
        widthMap.put(2, 30);
        widthMap.put(3, 10);
        widthMap.put(4, 20);
        widthMap.put(5, 20);
        widthMap.put(6, 15);
        widthMap.put(7, 20);
        widthMap.put(8, 20);



        return widthMap;
    }

    private boolean fileToDto(String str) throws Exception
    {
        boolean hasNotMatch=false;
        boolean flag=false;
        //fe
        MonitoringSheetsDto monitoringSheetsDto = JsonUtil.parseToObject(str, MonitoringSheetsDto.class);
        MonitoringSheetsDto monitoringAppSheetsDto=null;

        //be
        InputStream  fileInputStream = null;
        try {
            String outFolder = outSharedPath;
            if (!outFolder.endsWith(File.separator)) {
                outFolder += File.separator;
            }
            String s = "";
            try{
                Date date = new Date();
                String dateStr = Formatter.formatDateTime(date, Formatter.DATE_FILE);
                s =  "BECompareResults_"+dateStr;
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            File file = MiscUtil.generateFile(outFolder+ subFolder, s+AppServicesConsts.FILE_FORMAT);
            fileInputStream= newInputStream(file.toPath());
            ByteArrayOutputStream by=new ByteArrayOutputStream();
            int count;
            byte [] size=new byte[1024];
            count=fileInputStream.read(size);
            while(count!=-1){
                by.write(size,0,count);
                count= fileInputStream.read(size);
            }
            monitoringAppSheetsDto = JsonUtil.parseToObject(by.toString(), MonitoringSheetsDto.class);

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }finally {
            if(fileInputStream !=null){
                fileInputStream.close();
            }
        }

        if(monitoringAppSheetsDto!=null){
            monitoringSheetsDto.setLicEicTrackExcelDtoMap(monitoringAppSheetsDto.getLicEicTrackExcelDtoMap());

            if(IaisCommonUtils.isNotEmpty(monitoringSheetsDto.getAppProcessFileTrackExcelDtoMap())){
                monitoringSheetsDto.getAppProcessFileTrackExcelDtoMap().putAll(monitoringAppSheetsDto.getAppProcessFileTrackExcelDtoMap());
            }else {
                monitoringSheetsDto.setAppProcessFileTrackExcelDtoMap(monitoringAppSheetsDto.getAppProcessFileTrackExcelDtoMap());
            }

            if(IaisCommonUtils.isNotEmpty(monitoringAppSheetsDto.getAppProcessFileTrackExcelDtoMap())){
                for (Map.Entry<String, AppProcessFileTrackExcelDto> entry:monitoringAppSheetsDto.getAppProcessFileTrackExcelDtoMap().entrySet()
                ) {
                    if(monitoringSheetsDto.getAppProcessFileTrackExcelDtoMap().containsKey(entry.getKey())){
                        AppProcessFileTrackExcelDto excelDto=monitoringSheetsDto.getAppProcessFileTrackExcelDtoMap().get(entry.getKey());

                        if ("PFT005".equals(excelDto.getStatus()) || "PFT005".equals(excelDto.getStatusFe())) {
                            excelDto.setResult("Match");
                        } else {
                            excelDto.setResult("Not Match");
                            hasNotMatch=true;
                        }
                    }
                }
            }
            if(IaisCommonUtils.isNotEmpty(monitoringAppSheetsDto.getApplicationExcelDtoMap())){
                for (Map.Entry<String, ApplicationExcelDto> entry:monitoringAppSheetsDto.getApplicationExcelDtoMap().entrySet()
                ) {
                    if(monitoringSheetsDto.getApplicationExcelDtoMap()!=null&&monitoringSheetsDto.getApplicationExcelDtoMap().containsKey(entry.getKey())){
                        ApplicationExcelDto excelDto=monitoringSheetsDto.getApplicationExcelDtoMap().get(entry.getKey());
                        excelDto.setApplicationNoBe(entry.getValue().getApplicationNoBe());
                        excelDto.setStatusBe(entry.getValue().getStatusBe());
                        excelDto.setUpdatedDtBe(entry.getValue().getUpdatedDtBe());
                        excelDto.setVersionBe(entry.getValue().getVersionBe());
                        excelDto.setOriginLicenceIdBe(entry.getValue().getOriginLicenceIdBe());
                        if(excelDto.getApplicationNoBe().equals(excelDto.getApplicationNoFe())
                                && excelDto.getVersionBe().equals(excelDto.getVersionFe())
                                && excelDto.getStatusBe().equals(excelDto.getStatusFe())){
                            excelDto.setResult("Match");
                        }else {
                            excelDto.setResult("Not Match");
                            hasNotMatch=true;
                        }
                    }else {
                        entry.getValue().setResult("Not Match");
                        hasNotMatch=true;
                        if(IaisCommonUtils.isEmpty(monitoringSheetsDto.getApplicationExcelDtoMap())){
                            monitoringSheetsDto.setApplicationExcelDtoMap(IaisCommonUtils.genNewHashMap());
                        }
                        monitoringSheetsDto.getApplicationExcelDtoMap().put(entry.getKey(),entry.getValue());
                    }
                }
            }
            if(IaisCommonUtils.isNotEmpty(monitoringAppSheetsDto.getLicenceExcelDtoMap())){
                for (Map.Entry<String, LicenceExcelDto> entry:monitoringAppSheetsDto.getLicenceExcelDtoMap().entrySet()
                ) {
                    if(monitoringSheetsDto.getLicenceExcelDtoMap()!=null&&monitoringSheetsDto.getLicenceExcelDtoMap().containsKey(entry.getKey())){
                        LicenceExcelDto excelDto=monitoringSheetsDto.getLicenceExcelDtoMap().get(entry.getKey());
                        excelDto.setLicenceNoBe(entry.getValue().getLicenceNoBe());
                        excelDto.setStatusBe(entry.getValue().getStatusBe());
                        excelDto.setUpdatedDtBe(entry.getValue().getUpdatedDtBe());
                        excelDto.setOriginLicenceIdBe(entry.getValue().getOriginLicenceIdBe());
                        excelDto.setVersionBe(entry.getValue().getVersionBe());
                        excelDto.setEffectiveDtBe(entry.getValue().getEffectiveDtBe());
                        excelDto.setUenNoBe(entry.getValue().getUenNoBe());
                        excelDto.setLicenseeNameBe(entry.getValue().getLicenseeNameBe());
                        if(excelDto.getLicenceNoBe().equals(excelDto.getLicenceNoFe())
                                && excelDto.getVersionBe().equals(excelDto.getVersionFe())
                                && excelDto.getStatusBe().equals(excelDto.getStatusFe())){
                            excelDto.setResult("Match");
                        }else {
                            excelDto.setResult("Not Match");
                            hasNotMatch=true;
                        }
                    }else {
                        entry.getValue().setResult("Not Match");
                        hasNotMatch=true;
                        if(IaisCommonUtils.isEmpty(monitoringSheetsDto.getLicenceExcelDtoMap())){
                            monitoringSheetsDto.setLicenceExcelDtoMap(IaisCommonUtils.genNewHashMap());
                        }
                        monitoringSheetsDto.getLicenceExcelDtoMap().put(entry.getKey(),entry.getValue());
                    }
                }
            }
            if(IaisCommonUtils.isNotEmpty(monitoringAppSheetsDto.getAppGroupExcelDtoMap())){
                for (Map.Entry<String, AppGroupExcelDto> entry:monitoringAppSheetsDto.getAppGroupExcelDtoMap().entrySet()
                ) {
                    if(monitoringSheetsDto.getAppGroupExcelDtoMap()!=null&&monitoringSheetsDto.getAppGroupExcelDtoMap().containsKey(entry.getKey())){
                        AppGroupExcelDto excelDto=monitoringSheetsDto.getAppGroupExcelDtoMap().get(entry.getKey());
                        excelDto.setAppGroupNoBe(entry.getValue().getAppGroupNoBe());
                        excelDto.setStatusBe(entry.getValue().getStatusBe());
                        excelDto.setUpdatedDtBe(entry.getValue().getUpdatedDtBe());
                        excelDto.setAmountBe(entry.getValue().getAmountBe());
                        excelDto.setPmtStatusBe(entry.getValue().getPmtStatusBe());
                        excelDto.setPmtRefNoBe(entry.getValue().getPmtRefNoBe());
                        excelDto.setIsAutoApproveBe(entry.getValue().getIsAutoApproveBe());
                        if(excelDto.getAppGroupNoBe().equals(excelDto.getAppGroupNoFe())
                                && excelDto.getPmtStatusBe().equals(excelDto.getPmtStatusFe())
                                && excelDto.getAmountBe().equals(excelDto.getAmountFe())
                                && excelDto.getIsAutoApproveBe().equals(excelDto.getIsAutoApproveFe())
                                && excelDto.getStatusBe().equals(excelDto.getStatusFe())){
                            excelDto.setResult("Match");
                        }else {
                            excelDto.setResult("Not Match");
                            hasNotMatch=true;
                        }
                    }else {
                        entry.getValue().setResult("Not Match");
                        hasNotMatch=true;
                        if(IaisCommonUtils.isEmpty(monitoringSheetsDto.getAppGroupExcelDtoMap())){
                            monitoringSheetsDto.setAppGroupExcelDtoMap(IaisCommonUtils.genNewHashMap());
                        }
                        monitoringSheetsDto.getAppGroupExcelDtoMap().put(entry.getKey(),entry.getValue());
                    }
                }
            }
            if(IaisCommonUtils.isNotEmpty(monitoringAppSheetsDto.getUserAccountExcelDtoMap())){
                for (Map.Entry<String, UserAccountExcelDto> entry:monitoringAppSheetsDto.getUserAccountExcelDtoMap().entrySet()
                ) {
                    if(monitoringSheetsDto.getUserAccountExcelDtoMap()!=null&&monitoringSheetsDto.getUserAccountExcelDtoMap().containsKey(entry.getKey())){
                        UserAccountExcelDto excelDto=monitoringSheetsDto.getUserAccountExcelDtoMap().get(entry.getKey());
                        excelDto.setDisplayNameBe(entry.getValue().getDisplayNameBe());
                        excelDto.setStatusBe(entry.getValue().getStatusBe());
                        excelDto.setUpdatedDtBe(entry.getValue().getUpdatedDtBe());
                        excelDto.setUserDomainBe(entry.getValue().getUserDomainBe());
                        if(excelDto.getDisplayNameBe().equals(excelDto.getDisplayNameFe())&&
                                excelDto.getStatusBe().equals(excelDto.getStatusFe())&&
                                excelDto.getUserDomainBe().equals(excelDto.getUserDomainFe())){
                            excelDto.setResult("Match");
                        }else {
                            excelDto.setResult("Not Match");
                            hasNotMatch=true;
                        }
                    }else {
                        entry.getValue().setResult("Not Match");
                        hasNotMatch=true;
                        if(IaisCommonUtils.isEmpty(monitoringSheetsDto.getUserAccountExcelDtoMap())){
                            monitoringSheetsDto.setUserAccountExcelDtoMap(IaisCommonUtils.genNewHashMap());
                        }
                        monitoringSheetsDto.getUserAccountExcelDtoMap().put(entry.getKey(),entry.getValue());
                    }
                }
            }
            if(IaisCommonUtils.isNotEmpty(monitoringAppSheetsDto.getAppLicExcelDtoMap())){
                for (Map.Entry<String, AppLicExcelDto> entry:monitoringAppSheetsDto.getAppLicExcelDtoMap().entrySet()
                ) {
                    if(monitoringSheetsDto.getAppLicExcelDtoMap()!=null&&monitoringSheetsDto.getAppLicExcelDtoMap().containsKey(entry.getKey())){
                        AppLicExcelDto excelDto=monitoringSheetsDto.getAppLicExcelDtoMap().get(entry.getKey());
                        excelDto.setApplicationIdBe(entry.getValue().getApplicationIdBe());
                        excelDto.setLicenceIdBe(entry.getValue().getLicenceIdBe());
                        excelDto.setUpdatedDtBe(entry.getValue().getUpdatedDtBe());
                        if(excelDto.getApplicationIdBe().equals(excelDto.getApplicationIdFe())&&
                                excelDto.getLicenceIdBe().equals(excelDto.getLicenceIdFe())){
                            excelDto.setResult("Match");
                        }else {
                            excelDto.setResult("Not Match");
                            hasNotMatch=true;
                        }
                    }else {
                        entry.getValue().setResult("Not Match");
                        hasNotMatch=true;
                        if(IaisCommonUtils.isEmpty(monitoringSheetsDto.getAppLicExcelDtoMap())){
                            monitoringSheetsDto.setAppLicExcelDtoMap(IaisCommonUtils.genNewHashMap());
                        }
                        monitoringSheetsDto.getAppLicExcelDtoMap().put(entry.getKey(),entry.getValue());
                    }
                }
            }
        }

        Date date = new Date();
        String dateStr = Formatter.formatDateTime(date, Formatter.DATE_ELIS);
        String inputFileName =  "CompareResults_"+dateStr;
        File path = MiscUtil.generateFile(rsltSharedPath+File.separator+inputFileName+".xlsx" );
        path.createNewFile();
        List<ExcelSheetDto> excelSheetDtos = getExcelSheetDtos(monitoringSheetsDto);
        File configInfoTemplate = ResourceUtils.getFile("classpath:template/ConsolRecToCompare_Template.xlsx");
        File writerToExcel= ExcelWriter.writerToExcel(excelSheetDtos, configInfoTemplate,inputFileName);
        byte[] bytes = FileUtils.readFileToByteArray(writerToExcel);

        try (OutputStream fileOutputStream  = newOutputStream(path.toPath());) {
            fileOutputStream.write(bytes);
            flag=true;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        if(hasNotMatch){
            log.info("start send email start");
            EmailDto emailDto = new EmailDto();
            List<String> receiptEmail= Arrays.asList(this.mailRecipient.split(","));

            emailDto.setReceipts(receiptEmail);
            String emailContent = "CompareFEBE Not Match";
            emailDto.setContent(emailContent);
            emailDto.setSubject("CompareFEBE Not Match");
            emailDto.setSender(this.mailSender);
            emailDto.setReqRefNum(UUID.randomUUID().toString());
            emailDto.setClientQueryCode(UUID.randomUUID().toString());

            try {
                Map<String, byte[]> attachments =IaisCommonUtils.genNewHashMap();
                attachments.put(inputFileName+".xlsx",bytes);
                emailSmsClient.sendEmail(emailDto, attachments);
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            log.info("start send email end");
        }
        return flag;

    }

    private void saveFileToOtherNodes(byte[] content, String fileName, String tempFolder) {
        List<String> ipAddrs = ServicesSysteminfo.getInstance().getAddressesByServiceName("hcsa-licence-web");
        if (ipAddrs != null && ipAddrs.size() > 1 && fileName != null) {
            String localIp = MiscUtil.getLocalHostExactAddress();
            log.info(StringUtil.changeForLog("Local Ip is ==>" + localIp));
            RestTemplate restTemplate = new RestTemplate();
            for (String ip : ipAddrs) {
                if (localIp.equals(ip)) {
                    continue;
                }
                try {
                    String port = ConfigHelper.getString("server.port", "8080");
                    StringBuilder apiUrl = new StringBuilder("http://");
                    apiUrl.append(ip).append(':').append(port).append("/hcsa-licence-web/tempFile-handler");
                    log.info("Request URL ==> {}", apiUrl);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                    MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
                    HttpHeaders fileHeader = new HttpHeaders();
                    ByteArrayResource fileContentAsResource = new ByteArrayResource(content) {
                        @Override
                        public String getFilename() {
                            return fileName;
                        }
                    };
                    HttpEntity<ByteArrayResource> fileEnt = new HttpEntity<>(fileContentAsResource, fileHeader);
                    multipartRequest.add("selectedFile", fileEnt);
                    HttpHeaders jsonHeader = new HttpHeaders();
                    jsonHeader.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> jsonPart = new HttpEntity<>(fileName, jsonHeader);
                    multipartRequest.add("fileName", jsonPart);
                    jsonHeader = new HttpHeaders();
                    jsonHeader.setContentType(MediaType.APPLICATION_JSON);
                    jsonPart = new HttpEntity<>("ajaxUpload" + tempFolder, jsonHeader);
                    multipartRequest.add("folderName", jsonPart);
                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, headers);
                    restTemplate.postForObject(apiUrl.toString(), requestEntity, String.class);
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

}
