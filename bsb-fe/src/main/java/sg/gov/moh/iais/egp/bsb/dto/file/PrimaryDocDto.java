package sg.gov.moh.iais.egp.bsb.dto.file;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrimaryDocDto implements ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocsMetaDto implements Serializable {
        private Map<String, List<DocMeta>> metaDtoMap;
    }

    @Getter@Setter@JsonIgnore
    private boolean pvRf;
    private Map<Integer, String> facilityNameIndexMap;

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

    @Override
    public boolean doValidation() {
        return true;
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    @Override
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
     * just a simple way to get saved file list
     * getExistDocTypeList
     */
    public List<DocRecordInfo> getExistDocTypeList() {
        return new ArrayList<>(savedDocMap.values());
    }

    /**
     * get a structure used to display the already saved docs
     * we have not retrieve data of these docs yet, if user wants to download it, we call API to retrieve the data
     * @return a map, the key is the doc type, the value is the exist doc info list
     */
    /**
     * get a structure used to display the already saved docs
     * we have not retrieve data of these docs yet, if user wants to download it, we call API to retrieve the data
     * @return a map, the key is the doc type, the value is the exist doc info list
     */
    public Map<String, List<DocRecordInfo>> getExistDocTypeMap() {
        if(pvRf){
            return getPvRfExistDocTypeMap();
        } else {
            return CollectionUtils.groupCollectionToMap(DocRecordInfo::getDocType, this.savedDocMap.values());
        }
    }

    private Map<String, List<DocRecordInfo>> getPvRfExistDocTypeMap(){
        Map<String, Integer> exchangeMap = Maps.newLinkedHashMapWithExpectedSize(this.facilityNameIndexMap.size());
        for (Map.Entry<Integer, String> entry : this.facilityNameIndexMap.entrySet()) {
            exchangeMap.put(entry.getValue(), entry.getKey());
        }
        Map<String, List<DocRecordInfo>> docRecordInfoKeyMap = Maps.newLinkedHashMapWithExpectedSize(this.savedDocMap.size());
        List<DocRecordInfo> docRecordInfos = new ArrayList<>(this.savedDocMap.values());
        for (DocRecordInfo recordInfo : docRecordInfos) {
            String key;
            if(DocConstants.DOC_TYPE_PV_INVENTORY_REPORTING_FORM.equals(recordInfo.getDocType())){
                Integer index = exchangeMap.get(recordInfo.getDocSubType());
                key = DocConstants.DOC_TYPE_PV_INVENTORY_REPORTING_FORM + FacRegisterConstants.SEPARATOR + index;
                List<DocRecordInfo> reportFormDoc = docRecordInfoKeyMap.get(key);
                if(reportFormDoc == null || reportFormDoc.isEmpty()){
                    reportFormDoc = new ArrayList<>();
                }
                reportFormDoc.add(recordInfo);
                docRecordInfoKeyMap.put(key, reportFormDoc);
            } else {
                key = recordInfo.getDocType();
                List<DocRecordInfo> otherDoc = docRecordInfoKeyMap.get(key);
                if(otherDoc == null || otherDoc.isEmpty()){
                    otherDoc = new ArrayList<>();
                }
                otherDoc.add(recordInfo);
                docRecordInfoKeyMap.put(key, otherDoc);
            }
        }
        return docRecordInfoKeyMap;
    }

    /**
     * get a structure used to display new selected docs
     * these docs have not been saved into DB, if user wants to download it, we send the data from current data structure
     * @return a map, the key is the doc type, the value is the new doc info list
     */

    public Map<String, List<NewDocInfo>> getNewDocTypeMap() {
        if(pvRf){
            return getPvRfNewDocTypeMap();
        } else {
            return CollectionUtils.groupCollectionToMap(NewDocInfo::getDocType, this.newDocMap.values());
        }
    }

    private Map<String, List<NewDocInfo>> getPvRfNewDocTypeMap(){
        Map<String, Integer> exchangeMap = Maps.newLinkedHashMapWithExpectedSize(this.facilityNameIndexMap.size());
        for (Map.Entry<Integer, String> entry : this.facilityNameIndexMap.entrySet()) {
            exchangeMap.put(entry.getValue(), entry.getKey());
        }

        Map<String, List<NewDocInfo>> docRecordInfoKeyMap = Maps.newLinkedHashMapWithExpectedSize(this.savedDocMap.size());
        List<NewDocInfo> newDocInfos = new ArrayList<>(this.newDocMap.values());
        for (NewDocInfo newDocInfo : newDocInfos) {
            String key;
            if(DocConstants.DOC_TYPE_PV_INVENTORY_REPORTING_FORM.equals(newDocInfo.getDocType())){
                Integer index = exchangeMap.get(newDocInfo.getDocSubType());
                key = DocConstants.DOC_TYPE_PV_INVENTORY_REPORTING_FORM + FacRegisterConstants.SEPARATOR + index;
                List<NewDocInfo> reportFormDoc = docRecordInfoKeyMap.get(key);
                if(reportFormDoc == null || reportFormDoc.isEmpty()){
                    reportFormDoc = new ArrayList<>();
                }
                reportFormDoc.add(newDocInfo);
                docRecordInfoKeyMap.put(key, reportFormDoc);
            } else {
                key = newDocInfo.getDocType();
                List<NewDocInfo> otherDoc = docRecordInfoKeyMap.get(key);
                if(otherDoc == null || otherDoc.isEmpty()){
                    otherDoc = new ArrayList<>();
                }
                otherDoc.add(newDocInfo);
                docRecordInfoKeyMap.put(key, otherDoc);
            }
        }
        return docRecordInfoKeyMap;
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


    public Set<String> getToBeDeletedRepoIds() {
        return toBeDeletedRepoIds;
    }

    public Map<Integer, String> getFacilityNameIndexMap() {
        return facilityNameIndexMap;
    }

    public void setFacilityNameIndexMap(Map<Integer, String> facilityNameIndexMap) {
        this.facilityNameIndexMap = facilityNameIndexMap;
    }

    //    ---------------------------- request -> object ----------------------------------------------

    private static final String MASK_PARAM = "file";

    private static final String KEY_DELETED_SAVED_FILES = "deleteExistFiles";
    private static final String KEY_DELETED_NEW_FILES = "deleteNewFiles";

    public void reqObjMapping(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String deleteSavedFilesString = ParamUtil.getString(mulReq, KEY_DELETED_SAVED_FILES);
        if (log.isInfoEnabled()) {
            log.info("deleteSavedFilesString: {}", LogUtil.escapeCrlf(deleteSavedFilesString));
        }
        if (StringUtils.hasLength(deleteSavedFilesString)) {
            List<String> deleteFileRepoIds = Arrays.stream(deleteSavedFilesString.split(","))
                    .map(f -> MaskUtil.unMaskValue(MASK_PARAM, f))
                    .collect(Collectors.toList());
            deleteFileRepoIds.forEach(it -> {this.savedDocMap.remove(it); toBeDeletedRepoIds.add(it);});
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
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        while (inputNameIt.hasNext()) {
            String inputName = inputNameIt.next();
            /* The document type dropdown name related with this file is docType+others+id. */
            String docTypeElName = "docType" + inputName;
            String docType = ParamUtil.getString(request, docTypeElName);
            /* If we can not read the doc type, we just discard the file. This behaviour is easy to implement.
             * We use javascript to validate and alert user in order to simplify java logic. */
            if (docType != null && !"".equals(docType)) {
                List<MultipartFile> files = mulReq.getFiles(inputName);
                saveNewUploadedFile(inputName, docType, files, currentDate, loginContext.getUserId());
            }
        }
    }

    private void saveNewUploadedFile(String inputName, String docType, List<MultipartFile> files, Date submitDate, String submitUserId) {
        for (MultipartFile f : files) {
            if (log.isInfoEnabled()) {
                log.info("input name: {}; filename: {}", LogUtil.escapeCrlf(inputName), LogUtil.escapeCrlf(f.getOriginalFilename()));
            }
            if (f.isEmpty()) {
                log.warn("File is empty, ignore it");
            } else {
                NewDocInfo newDocInfo = new NewDocInfo();
                String tmpId = inputName + f.getSize() + System.nanoTime();
                newDocInfo.setTmpId(tmpId);
                newDocInfo.setDocType(docType);
                newDocInfo.setFilename(f.getOriginalFilename());
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
