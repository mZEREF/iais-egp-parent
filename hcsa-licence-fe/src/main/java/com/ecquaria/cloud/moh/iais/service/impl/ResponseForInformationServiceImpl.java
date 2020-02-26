package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.ResponseForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepositoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ResponseForInformationClient;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ResponseForInformationServiceImpl
 *
 * @author junyu
 * @date 2019/12/30
 */
@Service
@Slf4j
public class ResponseForInformationServiceImpl implements ResponseForInformationService {
    @Autowired
    ResponseForInformationClient responseForInformationClient;
    @Autowired
    private FeEicGatewayClient eicGatewayClient;
    @Autowired
    private FileRepositoryClient fileRepositoryClient;
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    private String download;
    private String fileName;
    private String fileFormat = ".text";
    private String backups;

    private Boolean flag=true;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public List<LicPremisesReqForInfoDto> searchLicPreRfiBylicenseeId(String licenseeId) {
        return responseForInformationClient.searchLicPreRfiBylicenseeId(licenseeId).getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto getLicPreReqForInfo(String id) {
        return responseForInformationClient.getLicPreReqForInfo(id).getEntity();
    }



    @Override
    public LicPremisesReqForInfoDto acceptLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        return responseForInformationClient.acceptLicPremisesReqForInfo(licPremisesReqForInfoDto).getEntity();
    }

    @Override
    public void saveFile(String data) {
        fileName = "userRecFile";
        download = sharedPath + fileName;
        backups = sharedPath + "backupsRec";

        String s = FileUtil.genMd5FileChecksum(data.getBytes());
        File file=MiscUtil.generateFile(download+File.separator, s+fileFormat);
        File groupPath=new File(download+File.separator);

        if(!groupPath.exists()){
            groupPath.mkdirs();
        }
        try (FileOutputStream fileInputStream = new FileOutputStream(backups+File.separator+file.getName());
             FileOutputStream fileOutputStream  =new FileOutputStream(file);) {
            if(!file.exists()){
                file.createNewFile();
            }
            fileOutputStream.write(data.getBytes());
            fileInputStream.write(data.getBytes());

        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public String getData(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        fileName = "folder";
        download = sharedPath + "folder";
        backups = sharedPath + "backups";
        //if path is not exists create path
        File fileRepPath=new File(download+File.separator+"files");
        if(!fileRepPath.exists()){
            fileRepPath.mkdirs();
        }
        String entity1= JsonUtil.parseToJson(licPremisesReqForInfoDto);
        byte[] entity = fileRepositoryClient.getFileFormDataBase(licPremisesReqForInfoDto.getFileRepoId()).getEntity();
        File file = MiscUtil.generateFile(download + File.separator + "files",
                licPremisesReqForInfoDto.getFileRepoId() + "@" + licPremisesReqForInfoDto.getDocName());
        try (FileOutputStream outputStream=new FileOutputStream(file)) {
            outputStream.write(entity);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return entity1;
    }


    @Override
    public void compressFile(){
        String compress = compress();
        log.info("-------------compress() end --------------");
        rename(compress);

        deleteFile();
    }


    private String compress(){
        log.info("------------ start compress() -----------------------");
        long l=   System.currentTimeMillis();
        try (OutputStream is=new FileOutputStream(backups+File.separator+ l+".zip");
             CheckedOutputStream cos=new CheckedOutputStream(is,new CRC32());
             ZipOutputStream zos=new ZipOutputStream(cos)) {

            log.info("------------zip file name is"+backups+File.separator+ l+".zip"+"--------------------");
            File file = new File(download+File.separator);
            MiscUtil.checkDirs(file);
            zipFile(zos, file);
            log.info("----------------end zipFile ---------------------");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

        return l+"";
    }

    private void zipFile(ZipOutputStream zos,File file) throws IOException {
        log.info("-----------start zipFile---------------------");
        if (file.isDirectory()) {
            zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName))+File.separator));
            zos.closeEntry();
            for(File f: Objects.requireNonNull(file.listFiles())){
                zipFile(zos,f);
            }
        } else {
            try (
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(fileName))));
                int count ;
                byte [] b =new byte[1024];
                count=bis.read(b);
                while(count!=-1){
                    zos.write(b,0,count);
                    count=bis.read(b);
                }
                zos.closeEntry();
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }

    }

    private void rename(String fileNamesss)  {
        log.info("--------------rename start ---------------------");
        flag = true;
        File zipFile =new File(backups);
        MiscUtil.checkDirs(zipFile);
        if(zipFile.isDirectory()){
            File[] files = zipFile.listFiles((dir, name) -> {
                if (name.endsWith(fileNamesss+".zip")) {
                    return true;
                }
                return false;
            });
            for(File file:files){
                try (FileInputStream is=new FileInputStream(file);
                     ByteArrayOutputStream by=new ByteArrayOutputStream();){
                    int count=0;
                    byte [] size=new byte[1024];
                    count=is.read(size);
                    while(count!=-1){
                        by.write(size,0,count);
                        count= is.read(size);
                    }

                    byte[] bytes = by.toByteArray();
                    String s = FileUtil.genMd5FileChecksum(bytes);
                    File curFile =new File(backups + File.separator + s + ".zip");
                    file.renameTo(curFile);
                    log.info("----------- new zip file name is"+backups+File.separator+s+".zip");
                    String s1 = saveFileName(s+".zip","backups" + File.separator+s+".zip");
                    if(!s1.equals("SUCCESS")){
                        MiscUtil.deleteFile(curFile);
                        flag=false;
                        break;
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    private void deleteFile(){
        File file =new File(download);
        File fileRepPath=new File(download+File.separator);
        MiscUtil.checkDirs(fileRepPath);
        MiscUtil.checkDirs(file);
        if(fileRepPath.isDirectory()){
            File[] files = fileRepPath.listFiles();
            for(File f :files){
                if(f.exists()&&f.isFile()){
                    MiscUtil.deleteFile(f);
                }
            }
        }
        if(file.isDirectory()){
            File[] files = file.listFiles((dir, name) -> {
                if (name.endsWith(fileFormat)) {
                    return true;
                }
                return false;
            });
            for(File f:files){
                if(f.exists()&&f.isFile()){
                    MiscUtil.deleteFile(f);
                }
            }
        }
    }

    private String saveFileName(String fileName ,String filePath){
        ProcessFileTrackDto processFileTrackDto =new ProcessFileTrackDto();
        processFileTrackDto.setProcessType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        processFileTrackDto.setFileName(fileName);
        processFileTrackDto.setFilePath(filePath);
        processFileTrackDto.setRefId("BE30AB5D-A92A-EA11-BE7D-000C29F371DC");
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
        AuditTrailDto intenet = AuditTrailHelper.getBatchJobDto("INTERNET");
        processFileTrackDto.setAuditTrailDto(intenet);
        String s="FAIL";
        try {
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            s = eicGatewayClient.saveFile(processFileTrackDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return s;
        }

        return s;
    }

}
