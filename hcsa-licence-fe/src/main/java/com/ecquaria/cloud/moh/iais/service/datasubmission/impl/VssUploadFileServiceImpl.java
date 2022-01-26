package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;


import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssDocumentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssFileDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.FileRepositoryClient;
import com.ecquaria.cloud.moh.iais.service.client.VssFeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssUploadFileService;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.Files.newOutputStream;
@Service
@Slf4j
public class VssUploadFileServiceImpl implements VssUploadFileService {

    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;
    @Value("${iais.sharedfolder.application.out}")
    private String sharedOutPath;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private VssFeClient vssFeClient;

    @Autowired
    private FileRepositoryClient fileRepositoryClient;
    @Override
    public void vssFile(){
        String status = DataSubmissionConsts.VSS_NEED_SYSN_BE_STATUS;
        log.info("------------------- getData  end --------------");
        //Parse the
        List<VssFileDto> parse = vssFeClient.getListVssDocumentDtoStatus(status).getEntity();
        if (parse.isEmpty()){
            return;
        }
        for(VssFileDto dto :parse){
            try {
                //create  non-compressed file
                getRelatedDocuments(dto);
                String vssTreId = saveFile(dto);
                log.info("------------------- saveFile  end --------------");
                String compressFileName = compressFile(vssTreId);
                if(!compressFileName.equals("")){
                    vssFeClient.updateVssDocumentStatusByTreId(vssTreId,DataSubmissionConsts.VSS_NEED_SYSN_BE_SUCCESS_STATUS);
                }
              /*  renameAndSave(compressFileName,vssTreId);*/
                log.info("------------------- compressFile  end --------------");
            }catch (Throwable e){

            }
        }
    }

    private void createweiyasuofile(List<VssDocumentDto> dtos,String vssTreId) throws Exception {
        String path = sharedPath+ AppServicesConsts.FILE_NAME+ File.separator+vssTreId+File.separator+"files";
        File fileRepPath = MiscUtil.generateFile(path);
        if(!fileRepPath.exists()){
            fileRepPath.mkdirs();
        }
        for (int i = 0; i < dtos.size(); i++) {
            getFileRep(dtos.get(i).getFileRepoId(),vssTreId);
        }

    }

    private void getFileRep(String id,String vssTreId) throws Exception{
        if(id==null||"".equals(id)){
            return;
        }
        byte[] entity = fileRepositoryClient.getFileFormDataBase(id).getEntity();
        log.info(StringUtil.changeForLog("file repo id is " + id));
        File file=MiscUtil.generateFile(sharedPath+AppServicesConsts.FILE_NAME+File.separator+vssTreId+ File.separator + AppServicesConsts.FILES,
                id);
        try (OutputStream outputStream= newOutputStream(file.toPath())){
            if(entity!=null){
                outputStream.write(entity);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.info("file stream is null");
            throw e;
        }
    }



    @Override
    public String saveFile(VssFileDto vssFileDto) {
        String str = JsonUtil.parseToJson(vssFileDto);
        List<VssDocumentDto> vssDocs = vssFileDto.getVssDocs();
        String vsstreId="";
        String s = "";
        try{
            s = FileUtil.genMd5FileChecksum(str.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        if(!vssDocs.isEmpty()){
            vsstreId = vssDocs.get(0).getTreatmentId();
        }
        File file = MiscUtil.generateFile(sharedPath+ AppServicesConsts.FILE_NAME+File.separator+vsstreId, s+AppServicesConsts.FILE_FORMAT);
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
            return null;
        }
        return vsstreId;
    }
    @Override
    public String compressFile(String vssTreId) {
        String compress = compress(vssTreId);
        log.info("-------------compress() end --------------");
        return compress;
    }

    private String compress(String vssTreId){
        log.info("------------ start compress() -----------------------");
        String outFolder = sharedOutPath;
        if (!outFolder.endsWith(File.separator)) {
            outFolder += File.separator;
        }
        String soPath = sharedOutPath;
        File zipFile = MiscUtil.generateFile(soPath);
        MiscUtil.checkDirs(zipFile);
        String osPath = outFolder + vssTreId + AppServicesConsts.ZIP_NAME;
        File osFile = MiscUtil.generateFile(osPath);
        try (OutputStream outputStream = newOutputStream(osFile.toPath());//Destination compressed folder
             CheckedOutputStream cos=new CheckedOutputStream(outputStream,new CRC32());
             ZipOutputStream zos=new ZipOutputStream(cos)) {

            log.info(StringUtil.changeForLog("------------zip file name is"+ outFolder + vssTreId+".zip"+"--------------------"));
            String path = sharedPath + AppServicesConsts.FILE_NAME + File.separator + vssTreId;
            File file = MiscUtil.generateFile(path);

            zipFile(zos, file);
            log.info("----------------end zipFile ---------------------");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new IaisRuntimeException(e);
        }
        return vssTreId;
    }

    private void zipFile(ZipOutputStream zos,File file) throws IOException {
        log.info("-----------start zipFile---------------------");
        if (file.isDirectory()) {
            zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(AppServicesConsts.FILE_NAME))+File.separator));
            zos.closeEntry();
            for(File f: Objects.requireNonNull(file.listFiles())){
                zipFile(zos,f);
            }
        } else {
            try (
                    BufferedInputStream bis = new BufferedInputStream( Files.newInputStream(file.toPath()))) {
                zos.putNextEntry(new ZipEntry(file.getPath().substring(file.getPath().indexOf(AppServicesConsts.FILE_NAME))));
                int count ;
                byte [] b =new byte[1024];
                count = bis.read(b);
                while(count!=-1){
                    zos.write(b,0,count);
                    count=bis.read(b);
                }
                zos.closeEntry();
            }catch (Exception e){
                log.error(e.getMessage(),e);
                throw new IaisRuntimeException(e);
            }
        }

    }

    @Override
    public void getRelatedDocuments(VssFileDto vssFileDto) throws Exception {
        try{
           List<VssDocumentDto> vssDocs = vssFileDto.getVssDocs();
            if(vssDocs.isEmpty()){
                log.info("************* this grp is empty**************");
                return;
            }
            String vssTreId = vssDocs.get(0).getTreatmentId();
            //delete old grp history (that the document is blank before zip)
            deleteFile(vssTreId);

            createweiyasuofile(vssDocs,vssTreId);
        }catch (Exception e){
            log.error(StringUtil.changeForLog("***************** there have a error is "+e+"***************"));
            log.error(e.getMessage(),e);
            throw e;
        }
    }

    private void deleteFile(String vssTreId){
        String path = sharedPath+AppServicesConsts.FILE_NAME+File.separator+vssTreId;
        File fileRepPath = MiscUtil.generateFile(path);
        MiscUtil.checkDirs(fileRepPath);
        if(fileRepPath.isDirectory()){
            File[] files = fileRepPath.listFiles();
            for(File f :files){
                if(f.exists()&&f.isFile()){
                    MiscUtil.deleteFile(f);
                }
            }
        }

    }

}
