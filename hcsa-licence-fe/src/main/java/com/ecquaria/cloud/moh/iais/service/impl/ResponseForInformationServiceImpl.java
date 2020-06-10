package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ResponseForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepositoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ResponseForInformationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
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
import java.nio.charset.StandardCharsets;
import java.util.Date;
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
    @Autowired
    SystemAdminClient systemAdminClient;
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    private String download;
    private String fileName;
    private String backups;

    private Boolean flag=Boolean.TRUE;
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
    public void saveFile(LicPremisesReqForInfoDto licPremisesReqForInfoDto)  {
        fileName = "userRecFile";
        download = sharedPath + fileName;
        backups = sharedPath + "backupsRec";

        String data = JsonUtil.parseToJson(licPremisesReqForInfoDto);
        String reqInfoId=licPremisesReqForInfoDto.getId();
        String s = FileUtil.genMd5FileChecksum(data.getBytes(StandardCharsets.UTF_8));

        File file = MiscUtil.generateFile(backups+File.separator+reqInfoId, s+AppServicesConsts.FILE_FORMAT);
        try (FileOutputStream fileOutputStream  = new FileOutputStream(file);) {
            if(!file.exists()){
                boolean newFile = file.createNewFile();
                if(newFile){
                    log.info("***newFile createNewFile***");
                }
            }
            fileOutputStream.write(data.getBytes());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }

    }

    @Override
    public String getData(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        fileName = "userRecFile";
        download = sharedPath +fileName;
        backups = sharedPath + "backupsRec";
        //if path is not exists create path
        File fileRepPath=new File(download+File.separator+"files");
        if(!fileRepPath.exists()){
            fileRepPath.mkdirs();
        }
        String entity1= JsonUtil.parseToJson(licPremisesReqForInfoDto);
        if(licPremisesReqForInfoDto.isNeedDocument()){
            for (LicPremisesReqForInfoDocDto doc:licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto()
                 ) {
                byte[] entity = fileRepositoryClient.getFileFormDataBase(doc.getFileRepoId()).getEntity();
                File file = MiscUtil.generateFile(download + File.separator + "files",
                        doc.getFileRepoId() + "@" + doc.getDocName());
                try (FileOutputStream outputStream=new FileOutputStream(file);){
                    outputStream.write(entity);
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
        return entity1;
    }


    @Override
    public void compressFile(String rfiId){
        String compress = compress( rfiId);
        log.info("-------------compress() end --------------");
        rename(compress,rfiId);

        deleteFile();
    }


    private String compress(String rfiId){
        log.info("------------ start compress() -----------------------");
        long l =   System.currentTimeMillis();
        try (OutputStream outputStream = new FileOutputStream(backups + File.separator + l + AppServicesConsts.ZIP_NAME);
             CheckedOutputStream cos=new CheckedOutputStream(outputStream,new CRC32());
             ZipOutputStream zos=new ZipOutputStream(cos)) {

            log.info(StringUtil.changeForLog("------------zip file name is"+backups+File.separator+ l+".zip"+"--------------------"));
            File file = new File(sharedPath + fileName + File.separator + rfiId);

            zipFile(zos, file,"backupsRec");
            log.info("----------------end zipFile ---------------------");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return l+"";
    }

    private void zipFile(ZipOutputStream zos, File file, String curFileName)  {
        if(file.isDirectory()){
            String filePath = file.getPath().substring(file.getPath().indexOf(curFileName));
            try {
                zos.putNextEntry(new ZipEntry(filePath + File.separator));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            for(File f: Objects.requireNonNull(file.listFiles())){
                zipFile(zos, f, curFileName);
            }
        } else {
            try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                String filePath = file.getPath().substring(file.getPath().indexOf(curFileName));
                zos.putNextEntry(new ZipEntry(filePath));
                int count ;
                byte [] b = new byte[1024];
                count = bis.read(b);
                while(count != -1){
                    zos.write(b,0, count);
                    count = bis.read(b);
                }
            }catch (IOException e){
                log.error(e.getMessage(), e);
            }
        }
    }

    private void rename(String fileNamesss, String rfiId)  {
        flag = Boolean.TRUE;
        File zipFile = new File(backups);
        if(zipFile.isDirectory()){
            File[] files = zipFile.listFiles((dir, name) -> {
                if (name.endsWith(fileNamesss + ".zip")) {
                    return true;
                }
                return false;
            });
            for(File file:files){
                try (FileInputStream is = new FileInputStream(file);
                     ByteArrayOutputStream by = new ByteArrayOutputStream()){
                    byte [] size = new byte[1024];
                    int count = is.read(size);
                    while(count != -1){
                        by.write(size,0, count);
                        count = is.read(size);
                    }
                    byte[] bytes = by.toByteArray();
                    String s = FileUtil.genMd5FileChecksum(bytes);
                    File curFile = new File(backups, s + ".zip");
                    if (!curFile.exists()){
                        boolean createFlag = curFile.createNewFile();
                        if (!createFlag) {
                            log.error("Create file fail");
                        }
                    }
                    boolean reNameFlag = file.renameTo(curFile);
                    if(reNameFlag) {
                        String s1 = saveFileName(s + ".zip", "backupsRec" + File.separator + s + ".zip", rfiId);
                        if(!s1.equals(AppConsts.SUCCESS)){
                            boolean fileDelStatus = new File(backups + File.separator + s + ".zip").delete();
                            if(!fileDelStatus){
                                log.debug(StringUtil.changeForLog(file.getName() + "delete false"));
                            }
                            flag = Boolean.FALSE;
                            break;
                        }
                    } else {
                        log.debug(StringUtil.changeForLog("file rename fail!!!"));
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private void deleteFile(){
        File file =new File(backups+File.separator);
        File fileRepPath=new File(download+File.separator+"files");
        File filePath=new File(download+File.separator);
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
        {
            File[] files = filePath.listFiles((dir, name) -> {
                if (name.endsWith(AppServicesConsts.FILE_FORMAT)) {
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
        {
            File[] files = file.listFiles((dir, name) -> {
                if (name.endsWith(AppServicesConsts.FILE_FORMAT)) {
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

    private String saveFileName(String fileName ,String filePath,String rfiId){
        ProcessFileTrackDto processFileTrackDto =new ProcessFileTrackDto();
        processFileTrackDto.setEventRefNo(System.currentTimeMillis()+"");
        processFileTrackDto.setProcessType(RequestForInformationConstants.RFI_CLOSE);
        processFileTrackDto.setFileName(fileName);
        processFileTrackDto.setFilePath(filePath);
        processFileTrackDto.setRefId(rfiId);
        processFileTrackDto.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_PENDING_PROCESS);
        AuditTrailDto intenet = AuditTrailHelper.getBatchJobDto("INTERNET");
        processFileTrackDto.setAuditTrailDto(intenet);
        EicRequestTrackingDto eicRequestTrackingDto=new EicRequestTrackingDto();
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        Date now = new Date();
        eicRequestTrackingDto.setActionClsName("com.ecquaria.cloud.moh.iais.service.ResponseForInformationServiceImpl");
        eicRequestTrackingDto.setActionMethod("eicCallRfiBeProcessFileTrack");
        eicRequestTrackingDto.setModuleName("hcsa-licence-web-internet");
        eicRequestTrackingDto.setDtoClsName(ProcessFileTrackDto.class.getName());
        eicRequestTrackingDto.setDtoObject(JsonUtil.parseToJson(processFileTrackDto));
        eicRequestTrackingDto.setProcessNum(1);
        eicRequestTrackingDto.setFirstActionAt(now);
        eicRequestTrackingDto.setLastActionAt(now);
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        eicRequestTrackingDto.setRefNo(processFileTrackDto.getEventRefNo());
        updateSysAdmEicRequestTrackingDto(eicRequestTrackingDto);
        String s="FAIL";
        try {
            s=createBeRfiLicProcessFileTrack(processFileTrackDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return s;
        }

        return s;
    }

    @Override
    public String createBeRfiLicProcessFileTrack(ProcessFileTrackDto processFileTrackDto) {
        EicRequestTrackingDto trackDto = getLicEicRequestTrackingDtoByRefNo(processFileTrackDto.getEventRefNo());
        String s=eicCallRfiBeProcessFileTrack(processFileTrackDto);
        trackDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        updateSysAdmEicRequestTrackingDto(trackDto);

        return s;
    }

    public String eicCallRfiBeProcessFileTrack(ProcessFileTrackDto processFileTrackDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        return eicGatewayClient.saveFile(processFileTrackDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
    }



    @Override
    public void   updateSysAdmEicRequestTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto) {
        List<EicRequestTrackingDto> eicRequestTrackingDtos= IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(licEicRequestTrackingDto);
        systemAdminClient.saveEicTrack(eicRequestTrackingDtos);
    }


    public EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo) {
        return systemAdminClient.getByRefNum(refNo).getEntity();
    }

}
