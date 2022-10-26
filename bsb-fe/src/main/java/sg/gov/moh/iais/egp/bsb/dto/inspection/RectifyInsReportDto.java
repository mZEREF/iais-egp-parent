package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.Maps;
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
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sop.servlet.webflow.HttpHandler;

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


@Slf4j
@Data
public class RectifyInsReportDto implements Serializable {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocsMetaDto implements Serializable {
        private Map<String, List<DocMeta>> metaDtoMap;
    }

    /* docs already saved in DB, key is repoId */
    private Map<String, DocRecordInfo> savedDocMap;
    /* docs new uploaded, key is tmpId */
    private Map<String, NewDocInfo> newDocMap;
    /* to be deleted files (which already saved), the string is repoId, used to delete file in repo */
    private Set<String> toBeDeletedRepoIds;
    private Set<String> toBeDeletedDocIds;



    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public RectifyInsReportDto() {
        savedDocMap = new LinkedHashMap<>();
        newDocMap = new LinkedHashMap<>();
        toBeDeletedRepoIds = new HashSet<>();
        toBeDeletedDocIds = new HashSet<>();
    }

    @JsonValue
    public Map<String, Object> jsonMap() {
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(4);
        map.put("savedDocMap", savedDocMap);
        map.put("newDocMap", newDocMap);
        map.put("toBeDeletedRepoIds", toBeDeletedRepoIds);
        map.put("toBeDeletedDocIds", toBeDeletedDocIds);
        return map;
    }



    public boolean doValidation() {
        return true;
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

    public List<DocMeta> convertToDocMetaList() {
        List<DocMeta> metaDtoList = new ArrayList<>(this.savedDocMap.size() + this.newDocMap.size());
        this.savedDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getRepoId(), i.getDocType(), i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });
        this.newDocMap.values().forEach(i -> {
            DocMeta docMeta = new DocMeta(i.getTmpId(), i.getDocType(), i.getFilename(), i.getSize());
            metaDtoList.add(docMeta);
        });
        return metaDtoList;
    }

    /**
     * get a structure used to display the already saved docs
     * we have not retrieve data of these docs yet, if user wants to download it, we call API to retrieve the data
     * @return a map, the key is the doc type, the value is the exist doc info list
     */
    public Map<String, List<DocRecordInfo>> getExistDocTypeMap() {
        return CollectionUtils.groupCollectionToMap(DocRecordInfo::getDocType, this.savedDocMap.values());
    }

    /**
     * get a structure used to display new selected docs
     * these docs have not been saved into DB, if user wants to download it, we send the data from current data structure
     * @return a map, the key is the doc type, the value is the new doc info list
     */
    public Map<String, List<NewDocInfo>> getNewDocTypeMap() {
        return CollectionUtils.groupCollectionToMap(NewDocInfo::getDocType, this.newDocMap.values());
    }

    public Map<String,List<NewDocInfo>> getNewDocSubTypeMap(){
        return CollectionUtils.groupCollectionToMap(NewDocInfo::getDocSubType, this.newDocMap.values());
    }

    public Map<String,List<DocRecordInfo>> getSavedDocSubTypeMap(){
        return CollectionUtils.groupCollectionToMap(DocRecordInfo::getDocSubType, this.savedDocMap.values());
    }

    /**
     * get a structure used to display preview
     * the returned map contains two file types
     * the map will keep the pre-setting order of doc types
     * @return a map, the key is the doc type, the value is the docs
     */
    public Map<String, List<DocMeta>> getAllDocTypeMap() {
        Map<String, List<DocMeta>> data = Maps.newLinkedHashMapWithExpectedSize(DocConstants.FAC_REG_DOC_TYPE_ORDER.size());

        Map<String, List<DocRecordInfo>> savedMap = getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newMap = getNewDocTypeMap();

        for (String docType : DocConstants.FAC_REG_DOC_TYPE_ORDER) {
            List<DocMeta> metaList = new ArrayList<>();
            List<DocRecordInfo> savedFiles = savedMap.get(docType);
            if (savedFiles != null) {
                savedFiles.forEach(i -> {
                    DocMeta docMeta = new DocMeta(i.getDocType(), i.getFilename(), i.getSize());
                    metaList.add(docMeta);
                });
            }
            List<NewDocInfo> newFiles = newMap.get(docType);
            if (newFiles != null) {
                newFiles.forEach(i -> {
                    DocMeta docMeta = new DocMeta(i.getDocType(), i.getFilename(), i.getSize());
                    metaList.add(docMeta);
                });
            }
            if (!metaList.isEmpty()) {
                data.put(docType, metaList);
            }
        }

        return data;
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
            docRecordInfo.setDocSubType(newDocInfo.getDocSubType());
            docRecordInfo.setSubmitBy(newDocInfo.getSubmitBy());
            docRecordInfo.setSubmitDate(newDocInfo.getSubmitDate());
            savedDocMap.put(repoId, docRecordInfo);

            NewFileSyncDto newFileSyncDto = new NewFileSyncDto();
            newFileSyncDto.setId(repoId);
            newFileSyncDto.setData(newDocInfo.getMultipartFile().getBytes());
            newFileSyncDtoList.add(newFileSyncDto);
        }
        newDocMap.clear();
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

    public void setNewDocMap(Map<String, NewDocInfo> newDocMap) {
        this.newDocMap = newDocMap;
    }

    public void setToBeDeletedRepoIds(Set<String> toBeDeletedRepoIds) {
        this.toBeDeletedRepoIds = toBeDeletedRepoIds;
    }

    public Set<String> getToBeDeletedRepoIds() {
        return toBeDeletedRepoIds;
    }

    public void setToBeDeletedDocIds(Set<String> toBeDeletedDocIds) {
        this.toBeDeletedDocIds = toBeDeletedDocIds;
    }

    public Set<String> getToBeDeletedDocIds() {
        return toBeDeletedDocIds;
    }

    //    ---------------------------- request -> object ----------------------------------------------

    private static final String MASK_PARAM = "file";

    private static final String KEY_DELETED_SAVED_FILES = "deleteExistFiles";
    private static final String KEY_DELETED_NEW_FILES = "deleteNewFiles";
    private static final String KEY_DOCUMENT = "others";

    public void reqObjMapping(HttpServletRequest request, String docSubType) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String deleteSavedFilesString = ParamUtil.getString(mulReq, KEY_DELETED_SAVED_FILES);
        if (log.isInfoEnabled()) {
            log.info("deleteSavedFilesString: {}", LogUtil.escapeCrlf(deleteSavedFilesString));
        }
        if (StringUtils.hasLength(deleteSavedFilesString)) {
            List<String> deleteFileRepoIds = Arrays.stream(deleteSavedFilesString.split(","))
                    .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                    .collect(Collectors.toList());
            deleteFileRepoIds.forEach(it -> {
                this.toBeDeletedDocIds.add(this.savedDocMap.remove(it).getDocEntityId());
                toBeDeletedRepoIds.add(it);
            });
        }

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


        // read new uploaded files
        Iterator<String> inputNameIt = mulReq.getFileNames();
        Date currentDate = new Date();
        LoginContext loginContext = (LoginContext) com.ecquaria.cloud.moh.iais.common.utils.ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        while (inputNameIt.hasNext()) {
            String inputName = inputNameIt.next();
            if (inputName.startsWith(KEY_DOCUMENT)) {
                /* The document type dropdown name related with this file is docType+others+id. */
                String docTypeElName = "docType" + inputName;
                String docType = ParamUtil.getString(request, docTypeElName);
                /* If we can not read the doc type, we just discard the file. This behaviour is easy to implement.
                 * We use javascript to validate and alert user in order to simplify java logic. */
                if (docType != null && !"".equals(docType)) {
                    List<MultipartFile> files = mulReq.getFiles(inputName);
                    saveNewUploadedFile(inputName,files,docType,docSubType,currentDate,loginContext.getUserId());
                }
            }
        }
    }

    public void saveNewUploadedFile(String inputName, List<MultipartFile> files, String docType, String docSubType,Date submitDate, String submitUserId){
        for (MultipartFile f : files) {
            if (f.isEmpty()) {
                log.warn("File is empty, ignore it");
            } else {
                NewDocInfo newDocInfo = new NewDocInfo();
                String tmpId = inputName + f.getSize() + System.nanoTime();
                newDocInfo.setTmpId(tmpId);
                newDocInfo.setDocType(docType);
                newDocInfo.setFilename(f.getOriginalFilename());
                newDocInfo.setDocSubType(docSubType);
                newDocInfo.setSize(f.getSize());
                newDocInfo.setSubmitDate(submitDate);
                newDocInfo.setSubmitBy(submitUserId);
                byte[] bytes = new byte[0];
                try {
                    bytes = f.getBytes();
                } catch (IOException e) {
                    log.warn("Fail to read bytes for file {}, tmpId {}", f.getOriginalFilename(), tmpId);
                }
                ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(f.getName(), f.getOriginalFilename(), f.getContentType(), bytes);
                newDocInfo.setMultipartFile(multipartFile);
                this.newDocMap.put(tmpId, newDocInfo);
            }
        }

    }
}
