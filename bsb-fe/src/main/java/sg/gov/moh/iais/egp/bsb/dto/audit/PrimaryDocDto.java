package sg.gov.moh.iais.egp.bsb.dto.audit;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
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
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
     */
    public List<DocRecordInfo> getExistDocTypeList() {
        return new ArrayList<>(savedDocMap.values());
    }


    /**
     * just a simple way to get new file list
     * getExistDocTypeList
     */
    public List<NewDocInfo> getNewDocTypeList() {
        return new ArrayList<>(newDocMap.values());
    }

    /**
     * get a structure used to display the already saved docs
     * we have not retrieve data of these docs yet, if user wants to download it, we call API to retrieve the data
     * @return a map, the key is the doc type, the value is the exist doc info list
     */
    public Map<String, List<DocRecordInfo>> getExistDocTypeMap() {
        return CollectionUtils.groupCollectionToMap(DocRecordInfo::getDocType, this.savedDocMap.values());
    }

    //----------------------validate------------------------------------
    public List<DocMeta> doValidation() {
        List<DocMeta> metaDtoList = new ArrayList<>(this.savedDocMap.size() + this.newDocMap.size());

        this.newDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize());
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
     *
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

    public Map<String, DocRecordInfo> getSavedDocMap() {
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


    private static final String MASK_PARAM = "file";
    private static final String KEY_DELETED_NEW_FILES = "deleteNewFiles";

    public void reqObjMapping(MultipartHttpServletRequest mulReq, HttpServletRequest request, String docType) {
        //delete new files
        deleteNewFiles(mulReq);

        // read new uploaded files
        Iterator<String> inputNameIt = mulReq.getFileNames();
        Date currentDate = new Date();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        while (inputNameIt.hasNext()) {
            String inputName = inputNameIt.next();
            if (StringUtils.hasLength(inputName)) {
                //upload document toxins and bats
                List<MultipartFile> files = mulReq.getFiles(inputName);
                filedNewFiles(files, inputName, docType, currentDate, loginContext);
            }
        }
    }

    public void reqOtherMapping(MultipartHttpServletRequest mulReq, HttpServletRequest request, String docType) {

        // read new uploaded files
        Iterator<String> inputNameIt = mulReq.getFileNames();
        Date currentDate = new Date();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        while (inputNameIt.hasNext()) {
            String inputName = inputNameIt.next();
            if (StringUtils.hasLength(inputName) && docType.equals(inputName)) {
                //upload other document
                List<MultipartFile> files = mulReq.getFiles(inputName);
                filedNewFiles(files, inputName, DocConstants.DOC_TYPE_OTHERS, currentDate, loginContext);
            }
        }
    }

    private void deleteNewFiles(MultipartHttpServletRequest mulReq) {
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

    private void filedNewFiles(List<MultipartFile> files, String inputName, String docType, Date currentDate, LoginContext loginContext) {
        for (MultipartFile f : files) {
            if (log.isInfoEnabled()) {
                log.info("input name: {}; filename: {}", LogUtil.escapeCrlf(inputName), LogUtil.escapeCrlf(f.getOriginalFilename()));
            }
            if (f.isEmpty()) {
                log.warn("File is empty, ignore it");
            } else {
                String tmpId = inputName + f.getSize() + System.nanoTime();
                this.newDocMap.put(tmpId, filedOneNewFiles(tmpId, f, docType, currentDate, loginContext));
            }
        }
    }

    private NewDocInfo filedOneNewFiles(String inputName, MultipartFile f, String docType, Date currentDate, LoginContext loginContext) {
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
