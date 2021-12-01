package sg.gov.moh.iais.egp.bsb.dto.submission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.LogUtil;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YiMing
 * @version 2021/11/5 13:48
 **/
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrimaryDocDto implements Serializable {


    //this class is used to
    @Data
    @NoArgsConstructor
    public static class DocRecordInfo implements Serializable {
        private String docEntityId;
        private String docType;
        private String filename;
        private long size;
        private String repoId;
        private Date submitDate;
        private String submitBy;
    }

    //new doc
    @Data
    @NoArgsConstructor
    public static class NewDocInfo implements Serializable {
        private String index;
        private String tmpId;
        private String docType;
        private String filename;
        private long size;
        private Date submitDate;
        private String submitBy;
        private ByteArrayMultipartFile multipartFile;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocsMetaDto implements Serializable {
        private Map<String, List<DocMeta>> metaDtoMap;
    }

    /* docs already saved in DB, key is repoId */
    private Map<String, DocRecordInfo> savedDocMap;
    /* docs new uploaded, key is tmpId */
    private final Map<String, NewDocInfo> newDocMap;
    /* to be deleted files (which already saved), the string is repoId, used to delete file in repo */
    private final Set<String> toBeDeletedRepoIds;


    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public PrimaryDocDto() {
        savedDocMap = new LinkedHashMap<>();
        newDocMap = new LinkedHashMap<>();
        toBeDeletedRepoIds = new HashSet<>();
    }

    /**
     * just a simple way to get saved file list
     * getExistDocTypeList
     * */
    public List<PrimaryDocDto.DocRecordInfo> getExistDocTypeList(){
        return new ArrayList<>(savedDocMap.values());
    }


    /**
     * just a simple way to get new file list
     * getExistDocTypeList
     * */
    public List<PrimaryDocDto.NewDocInfo> getNewDocTypeList(){
        return new ArrayList<>(newDocMap.values());
    }


    //----------------------validate------------------------------------
    public List<DocMeta> doValidation() {
        List<DocMeta> metaDtoList = new ArrayList<>(this.savedDocMap.size() + this.newDocMap.size());

        this.newDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize(), "dataSub");
            metaDtoList.add(docMeta);
        });
        return metaDtoList;
    }


    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    public void clearValidationResult() {
        this.validationResultDto = null;
    }


    /**
     * This method will put new added files to the important data structure which is used to update the FacilityDoc.
     * This file is called when new uploaded files are saved and we get the repo Ids.
     * ATTENTION!!!
     * This method is dangerous! The relationship between the ids and the files in this dto is fragile!
     * We rely on the order is not changed! So we use a LinkedHashMap to save our data.
     * <p>
     * This method will generate id-bytes pairs at the same time, the result will be used to sync files to BE.
     * @return a list of file data to be synchronized to BE
     */
    public List<NewFileSyncDto> newFileSaved(List<String> repoIds) {
        Iterator<String> repoIdIt = repoIds.iterator();
        Iterator<NewDocInfo> newDocIt = newDocMap.values().iterator();

        List<NewFileSyncDto> newFileSyncDtoList = new ArrayList<>(repoIds.size());
        while (repoIdIt.hasNext() && newDocIt.hasNext()) {
            String repoId = repoIdIt.next();
            NewDocInfo newDocInfo = newDocIt.next();
            DocRecordInfo docRecordInfo = new DocRecordInfo();
            docRecordInfo.setDocType(newDocInfo.getDocType());
            docRecordInfo.setFilename(newDocInfo.getFilename());
            docRecordInfo.setSize(newDocInfo.getSize());
            docRecordInfo.setRepoId(repoId);
            docRecordInfo.setSubmitBy(newDocInfo.getSubmitBy());
            docRecordInfo.setSubmitDate(newDocInfo.getSubmitDate());
            savedDocMap.put(repoId, docRecordInfo);

            NewFileSyncDto newFileSyncDto = new NewFileSyncDto();
            newFileSyncDto.setId(repoId);
            newFileSyncDto.setData(newDocInfo.getMultipartFile().getBytes());
            newFileSyncDtoList.add(newFileSyncDto);
        }
        return newFileSyncDtoList;
    }

    public Map<String,DocRecordInfo> getSavedDocMap() {
        return savedDocMap;
    }

    public void setSavedDocMap(Map<String, DocRecordInfo> savedDocMap) {
        this.savedDocMap = savedDocMap;
    }

    public Map<String, NewDocInfo> getNewDocMap() {
        return newDocMap;
    }

    public Set<String> getToBeDeletedRepoIds() {
        return toBeDeletedRepoIds;
    }


    private static final String MASK_PARAM              = "file";
    private static final String SEPARATOR               = "--v--";
    private static final String KEY_DELETED_NEW_FILES   = "deleteNewFiles";

    public void reqObjMapping(MultipartHttpServletRequest mulReq,HttpServletRequest request,String docType,String amt){
        //delete new files
        deleteNewFiles(mulReq);

        // read new uploaded files
        Iterator<String> inputNameIt = mulReq.getFileNames();
        Date currentDate = new Date();
        LoginContext loginContext = (LoginContext) com.ecquaria.cloud.moh.iais.common.utils.ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        while (inputNameIt.hasNext()) {
            String inputName = inputNameIt.next();
            if(StringUtils.hasLength(inputName)){
                String[] indexs = inputName.split(SEPARATOR);
                String index = null;
                if(indexs.length>=2){
                    index = inputName.split(SEPARATOR)[1];
                }
                if(StringUtils.hasLength(index) && index.equals(amt)){
                    //upload document toxins and bats
                    List<MultipartFile> files = mulReq.getFiles(inputName);
                    filedNewFiles(files,inputName,docType,currentDate,loginContext);
                }
            }
        }
    }

    public void reqOtherMapping(MultipartHttpServletRequest mulReq,HttpServletRequest request,String docType){

        // read new uploaded files
        Iterator<String> inputNameIt = mulReq.getFileNames();
        Date currentDate = new Date();
        LoginContext loginContext = (LoginContext) com.ecquaria.cloud.moh.iais.common.utils.ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        while (inputNameIt.hasNext()) {
            String inputName = inputNameIt.next();
            if(StringUtils.hasLength(inputName) && docType.equals(inputName)){
                //upload other document
                List<MultipartFile> files = mulReq.getFiles(inputName);
                filedNewFiles(files,inputName, DocConstants.DOC_TYPE_OTHERS,currentDate,loginContext);
            }
        }
    }

    private void deleteNewFiles(MultipartHttpServletRequest mulReq){
        String deleteNewFilesString = ParamUtil.getString(mulReq, KEY_DELETED_NEW_FILES);
        if (log.isInfoEnabled()) {
            log.info("deleteNewFilesString: {}", LogUtil.escapeCrlf(deleteNewFilesString));
        }
        if (StringUtils.hasLength(deleteNewFilesString)) {
            List<String> deleteFileTmpIds = Arrays.stream(deleteNewFilesString.split(","))
                    .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                    .collect(Collectors.toList());
            deleteFileTmpIds.forEach(this.newDocMap::remove);
        }
    }

    private void filedNewFiles(List<MultipartFile> files,String inputName,String docType,Date currentDate,LoginContext loginContext){
        for (MultipartFile f : files) {
            if (log.isInfoEnabled()) {
                log.info("input name: {}; filename: {}", LogUtil.escapeCrlf(inputName), LogUtil.escapeCrlf(f.getOriginalFilename()));
            }
            if (f.isEmpty()) {
                log.warn("File is empty, ignore it");
            } else {
                String tmpId = inputName + f.getSize() + System.nanoTime();
                this.newDocMap.put(tmpId, filedOneNewFiles(tmpId,f,docType,currentDate,loginContext));
            }
        }
    }

    private NewDocInfo filedOneNewFiles(String inputName,MultipartFile f,String docType,Date currentDate,LoginContext loginContext){
        NewDocInfo newDocInfo = new NewDocInfo();
        String tmpId = inputName + f.getSize() + System.nanoTime();
        newDocInfo.setTmpId(tmpId);
        newDocInfo.setDocType(docType);
        newDocInfo.setFilename(f.getOriginalFilename());
        newDocInfo.setSize(f.getSize());
        newDocInfo.setSubmitDate(currentDate);
        newDocInfo.setSubmitBy(loginContext.getUserId());
        byte[] bytes = new byte[0];
        try {
            bytes = f.getBytes();
        } catch (IOException e) {
            log.warn("Fail to read bytes for file {}, tmpId {}", f.getOriginalFilename(), tmpId);
        }
        ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(f.getName(), f.getOriginalFilename(), f.getContentType(), bytes);
        newDocInfo.setMultipartFile(multipartFile);
        return newDocInfo;
    }

}
