package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.AppGroupExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.AppProcessFileTrackExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.ApplicationExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.LicenceExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.MonitoringSheetsDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.UserAccountExcelDto;
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
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.systeminfo.ServicesSysteminfo;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
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
    @Value("${iais.sharedfolder.application.in}")
    private String inSharedPath;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Override
    public void initPath() {
        File compress = MiscUtil.generateFile(sharedPath+File.separator+ AppServicesConsts.COMPRESS,AppServicesConsts.FILE_NAME);
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

    @Override
    public void decompression() {
        log.info("-------------decompression start ---------");
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

        boolean flag=Boolean.FALSE;

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
                        MonitoringSheetsDto sheetsDto = fileToDto(by.toString());
                        Date date = new Date();
                        String dateStr = Formatter.formatDateTime(date, Formatter.DATE_ELIS);
                        String inputFileName =  "CompareResults_"+dateStr;
                        File path = MiscUtil.generateFile(inSharedPath+File.separator+inputFileName+".xlsx" );
                        path.createNewFile();
                        List<ExcelSheetDto> excelSheetDtos = getExcelSheetDtos(sheetsDto);
                        File configInfoTemplate = ResourceUtils.getFile("classpath:template/ConsolRecToCompare_Template.xlsx");
                        File writerToExcel= ExcelWriter.writerToExcel(excelSheetDtos, configInfoTemplate,inputFileName);
                        byte[] bytes = FileUtils.readFileToByteArray(writerToExcel);
                        try (OutputStream fileOutputStream  = newOutputStream(path.toPath());) {
                            fileOutputStream.write(bytes);
                            flag=true;
                        } catch (Exception e) {
                            log.error(e.getMessage(),e);
                            return null;
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

    private List<ExcelSheetDto> getExcelSheetDtos(MonitoringSheetsDto sheetsDto) {
        List<AppGroupExcelDto> groupExcelDtos= IaisCommonUtils.genNewArrayList();
        for (Map.Entry<String,AppGroupExcelDto> entry:sheetsDto.getAppGroupExcelDtoMap().entrySet()
             ) {
            groupExcelDtos.add(entry.getValue());
        }
        List<ApplicationExcelDto> applicationExcelDtos= IaisCommonUtils.genNewArrayList();
        for (Map.Entry<String,ApplicationExcelDto> entry:sheetsDto.getApplicationExcelDtoMap().entrySet()
        ) {
            applicationExcelDtos.add(entry.getValue());
        }
        List<AppProcessFileTrackExcelDto> appProcessFileTrackExcelDtos= IaisCommonUtils.genNewArrayList();
        for (Map.Entry<String,AppProcessFileTrackExcelDto> entry:sheetsDto.getAppProcessFileTrackExcelDtoMap().entrySet()
        ) {
            appProcessFileTrackExcelDtos.add(entry.getValue());
        }
        List<LicenceExcelDto> licenceExcelDtos= IaisCommonUtils.genNewArrayList();
        for (Map.Entry<String,LicenceExcelDto> entry:sheetsDto.getLicenceExcelDtoMap().entrySet()
        ) {
            licenceExcelDtos.add(entry.getValue());
        }
        List<UserAccountExcelDto> userAccountExcelDtos= IaisCommonUtils.genNewArrayList();
        for (Map.Entry<String,UserAccountExcelDto> entry:sheetsDto.getUserAccountExcelDtoMap().entrySet()
        ) {
            userAccountExcelDtos.add(entry.getValue());
        }
        List<ExcelSheetDto> excelSheetDtos = IaisCommonUtils.genNewArrayList();
        int sheetAt = 0;
        excelSheetDtos.add(getExcelSheetDto(sheetAt++, "app_group", groupExcelDtos,AppGroupExcelDto.class));
        excelSheetDtos.add(getExcelSheetDto(sheetAt++, "application", applicationExcelDtos,ApplicationExcelDto.class));
        excelSheetDtos.add(getExcelSheetDto(sheetAt++, "apft", appProcessFileTrackExcelDtos,AppProcessFileTrackExcelDto.class));
        excelSheetDtos.add(getExcelSheetDto(sheetAt++, "licence", licenceExcelDtos,LicenceExcelDto.class));
        excelSheetDtos.add(getExcelSheetDto(sheetAt, "user_account", userAccountExcelDtos,UserAccountExcelDto.class));


        return excelSheetDtos;
    }

    private ExcelSheetDto getExcelSheetDto(int sheetAt, String sheetName, List<?> data ,Class<?> sourceClass) {
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
            default:
        }

        return excelSheetDto;
    }


    public static Map<Integer, Integer> getAppWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(9);
        widthMap.put(0, 40);
        widthMap.put(1, 30);
        widthMap.put(2, 18);
        widthMap.put(3, 18);
        widthMap.put(4, 30);
        widthMap.put(5, 18);
        widthMap.put(6, 18);
        widthMap.put(7, 15);
        widthMap.put(8, 15);

        return widthMap;
    }
    public static Map<Integer, Integer> getGrpWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(9);
        widthMap.put(0, 40);
        widthMap.put(1, 28);
        widthMap.put(2, 18);
        widthMap.put(3, 18);
        widthMap.put(4, 28);
        widthMap.put(5, 18);
        widthMap.put(6, 18);
        widthMap.put(7, 15);
        widthMap.put(8, 15);
        return widthMap;
    }
    public static Map<Integer, Integer> getLicWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(9);
        widthMap.put(0, 40);
        widthMap.put(1, 30);
        widthMap.put(2, 18);
        widthMap.put(3, 18);
        widthMap.put(4, 30);
        widthMap.put(5, 18);
        widthMap.put(6, 18);
        widthMap.put(7, 15);
        widthMap.put(8, 15);
        return widthMap;
    }
    public static Map<Integer, Integer> getUserWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(11);
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
        return widthMap;
    }
    public static Map<Integer, Integer> getApftWidthMap() {
        Map<Integer, Integer> widthMap;
        widthMap = IaisCommonUtils.genNewHashMap(7);
        widthMap.put(0, 40);
        widthMap.put(1, 40);
        widthMap.put(2, 40);
        widthMap.put(3, 10);
        widthMap.put(4, 15);
        widthMap.put(5, 15);
        widthMap.put(6, 15);

        return widthMap;
    }

    private MonitoringSheetsDto fileToDto(String str)
    {
        MonitoringSheetsDto monitoringSheetsDto = JsonUtil.parseToObject(str, MonitoringSheetsDto.class);
        MonitoringSheetsDto monitoringAppSheetsDto=applicationClient.getMonitoringAppSheetsDto().getEntity();
        MonitoringSheetsDto monitoringLicSheetsDto=hcsaLicenceClient.getMonitoringLicenceSheetsDto().getEntity();
        MonitoringSheetsDto monitoringUserSheetsDto=organizationClient.getMonitoringUserSheetsDto().getEntity();
        monitoringAppSheetsDto.setLicenceExcelDtoMap(monitoringLicSheetsDto.getLicenceExcelDtoMap());
        monitoringAppSheetsDto.setUserAccountExcelDtoMap(monitoringUserSheetsDto.getUserAccountExcelDtoMap());

        monitoringSheetsDto.setAppProcessFileTrackExcelDtoMap(monitoringAppSheetsDto.getAppProcessFileTrackExcelDtoMap());

        for (Map.Entry<String, ApplicationExcelDto> entry:monitoringAppSheetsDto.getApplicationExcelDtoMap().entrySet()
        ) {
            if(monitoringSheetsDto.getApplicationExcelDtoMap().containsKey(entry.getKey())){
                ApplicationExcelDto excelDto=monitoringSheetsDto.getApplicationExcelDtoMap().get(entry.getKey());
                excelDto.setApplicationNoBe(entry.getValue().getApplicationNoBe());
                excelDto.setStatusBe(entry.getValue().getStatusBe());
                excelDto.setUpdatedDtBe(entry.getValue().getUpdatedDtBe());
            }else {
                monitoringSheetsDto.getApplicationExcelDtoMap().put(entry.getKey(),entry.getValue());
            }
        }
        for (Map.Entry<String, LicenceExcelDto> entry:monitoringAppSheetsDto.getLicenceExcelDtoMap().entrySet()
        ) {
            if(monitoringSheetsDto.getLicenceExcelDtoMap().containsKey(entry.getKey())){
                LicenceExcelDto excelDto=monitoringSheetsDto.getLicenceExcelDtoMap().get(entry.getKey());
                excelDto.setLicenceNoBe(entry.getValue().getLicenceNoBe());
                excelDto.setStatusBe(entry.getValue().getStatusBe());
                excelDto.setUpdatedDtBe(entry.getValue().getUpdatedDtBe());

            }else {
                monitoringSheetsDto.getLicenceExcelDtoMap().put(entry.getKey(),entry.getValue());
            }
        }
        for (Map.Entry<String, AppGroupExcelDto> entry:monitoringAppSheetsDto.getAppGroupExcelDtoMap().entrySet()
        ) {
            if(monitoringSheetsDto.getAppGroupExcelDtoMap().containsKey(entry.getKey())){
                AppGroupExcelDto excelDto=monitoringSheetsDto.getAppGroupExcelDtoMap().get(entry.getKey());
                excelDto.setAppGroupNoBe(entry.getValue().getAppGroupNoBe());
                excelDto.setStatusBe(entry.getValue().getStatusBe());
                excelDto.setUpdatedDtBe(entry.getValue().getUpdatedDtBe());

            }else {
                monitoringSheetsDto.getAppGroupExcelDtoMap().put(entry.getKey(),entry.getValue());
            }
        }
        for (Map.Entry<String, UserAccountExcelDto> entry:monitoringAppSheetsDto.getUserAccountExcelDtoMap().entrySet()
        ) {
            if(monitoringSheetsDto.getUserAccountExcelDtoMap().containsKey(entry.getKey())){
                UserAccountExcelDto excelDto=monitoringSheetsDto.getUserAccountExcelDtoMap().get(entry.getKey());
                excelDto.setDisplayNameBe(entry.getValue().getDisplayNameBe());
                excelDto.setStatusBe(entry.getValue().getStatusBe());
                excelDto.setUpdatedDtBe(entry.getValue().getUpdatedDtBe());
                excelDto.setUserDomainBe(entry.getValue().getUserDomainBe());

            }else {
                monitoringSheetsDto.getUserAccountExcelDtoMap().put(entry.getKey(),entry.getValue());
            }
        }

        return monitoringSheetsDto;

    }
}
