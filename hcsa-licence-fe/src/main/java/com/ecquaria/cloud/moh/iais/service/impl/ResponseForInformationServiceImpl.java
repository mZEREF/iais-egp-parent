package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.reqForInfo.RequestForInformationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ResponseForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepositoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ResponseForInformationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.sz.commons.util.FileUtil;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.nio.file.Files.newOutputStream;

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
    @Value("${iais.sharedfolder.requestForInfo.out}")
    private String sharedOutPath;

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
        licPremisesReqForInfoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return responseForInformationClient.acceptLicPremisesReqForInfo(licPremisesReqForInfoDto).getEntity();
    }

    @Override
    public void saveFile(LicPremisesReqForInfoDto licPremisesReqForInfoDto) throws IOException {


        String data = JsonUtil.parseToJson(licPremisesReqForInfoDto);
        String s = FileUtil.genMd5FileChecksum(data.getBytes(StandardCharsets.UTF_8));
        File file=MiscUtil.generateFile(sharedPath + RequestForInformationConstants.FILE_NAME_RFI+File.separator, s+RequestForInformationConstants.FILE_FORMAT);
        if(!file.exists()){
            boolean createFlag = file.createNewFile();
            if (!createFlag) {
                log.debug("Create File fail");
            }
        }
        File groupPath=MiscUtil.generateFile(sharedPath , RequestForInformationConstants.FILE_NAME_RFI);

        if(!groupPath.exists()){
            groupPath.mkdirs();
        }
        File outFile = MiscUtil.generateFile(sharedOutPath, file.getName());
        try (OutputStream fileInputStream = newOutputStream(outFile.toPath());
             OutputStream fileOutputStream  = newOutputStream(file.toPath())){

            fileOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
            fileInputStream.write(data.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public String getData(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        deleteFile();

        //if path is not exists create path
        File fileRepPath=MiscUtil.generateFile(sharedPath + RequestForInformationConstants.FILE_NAME_RFI,"files");
        if(!fileRepPath.exists()){
            fileRepPath.mkdirs();
        }
        String entity1= JsonUtil.parseToJson(licPremisesReqForInfoDto);
        if(licPremisesReqForInfoDto.isNeedDocument()){
            for (LicPremisesReqForInfoDocDto doc:licPremisesReqForInfoDto.getLicPremisesReqForInfoDocDto()
            ) {
                byte[] entity = fileRepositoryClient.getFileFormDataBase(doc.getFileRepoId()).getEntity();
                File file = MiscUtil.generateFile(sharedPath + RequestForInformationConstants.FILE_NAME_RFI + File.separator + "files",
                        doc.getFileRepoId() + "emanelififrcohda" + doc.getDocName());
                try (OutputStream outputStream= newOutputStream(file.toPath());){
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
        long l=   System.currentTimeMillis();
        File c= MiscUtil.generateFile(sharedOutPath);
        if(!c.exists()){
            c.mkdirs();
        }
        File outFile = MiscUtil.generateFile(sharedOutPath, l+RequestForInformationConstants.ZIP_NAME);
        try (OutputStream is=newOutputStream(outFile.toPath());
             CheckedOutputStream cos=new CheckedOutputStream(is,new CRC32());
             ZipOutputStream zos=new ZipOutputStream(cos);){
            log.info(StringUtil.changeForLog("------------zip file name is"+sharedOutPath+File.separator+ l+RequestForInformationConstants.ZIP_NAME+"--------------------"));
            File file = MiscUtil.generateFile(sharedPath , RequestForInformationConstants.FILE_NAME_RFI);

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
            zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(RequestForInformationConstants.FILE_NAME_RFI))+File.separator));
            zos.closeEntry();
            for(File f: Objects.requireNonNull(file.listFiles())){
                zipFile(zos,f);
            }
        } else {
            try  ( BufferedInputStream bis = new BufferedInputStream( Files.newInputStream(file.toPath()));){
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(RequestForInformationConstants.FILE_NAME_RFI))));
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

    private void rename(String fileNamesss, String rfiId)  {
        if (sharedOutPath.endsWith("/") || sharedOutPath.endsWith("\\")) {
            synchronized (this) {
                if (sharedOutPath.endsWith("/") || sharedOutPath.endsWith("\\")) {
                    sharedOutPath = sharedOutPath.substring(0, sharedOutPath.length() - 1);
                }
            }
        }
        File zipFile =MiscUtil.generateFile(sharedOutPath);
        MiscUtil.checkDirs(zipFile);
        if(zipFile.isDirectory()){
            File[] files = zipFile.listFiles((dir, name) -> {
                if (name.endsWith(fileNamesss+RequestForInformationConstants.ZIP_NAME)) {
                    return true;
                }
                return false;
            });
            for(File file:files){
                try (InputStream is= Files.newInputStream(file.toPath());
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
                    File curFile =MiscUtil.generateFile(sharedOutPath , s + RequestForInformationConstants.ZIP_NAME);
                    boolean renameFlag = file.renameTo(curFile);
                    if (!renameFlag) {
                        log.error("Rename file fail");
                    }
                    log.info(StringUtil.changeForLog("----------- new zip file name is"+sharedOutPath+File.separator+s+RequestForInformationConstants.ZIP_NAME));
                    String s1 = saveFileName(s+RequestForInformationConstants.ZIP_NAME,s+RequestForInformationConstants.ZIP_NAME,rfiId);
                    if(!s1.equals("SUCCESS")){
                        MiscUtil.deleteFile(curFile);
                        break;
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    private void deleteFile(){
        if (sharedOutPath.endsWith("/") || sharedOutPath.endsWith("\\")) {
            synchronized (this) {
                if (sharedOutPath.endsWith("/") || sharedOutPath.endsWith("\\")) {
                    sharedOutPath = sharedOutPath.substring(0, sharedOutPath.length() - 1);
                }
            }
        }
        File file = MiscUtil.generateFile(sharedOutPath);
        String repPath = sharedPath + RequestForInformationConstants.FILE_NAME_RFI+File.separator+"files";
        File fileRepPath = MiscUtil.generateFile(repPath);
        String path = sharedPath + RequestForInformationConstants.FILE_NAME_RFI;
        File filePath = MiscUtil.generateFile(path);
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
                if (name.endsWith(RequestForInformationConstants.FILE_FORMAT)) {
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
                if (name.endsWith(RequestForInformationConstants.FILE_FORMAT)) {
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
        AuditTrailDto intenet = AuditTrailHelper.getCurrentAuditTrailDto();
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
